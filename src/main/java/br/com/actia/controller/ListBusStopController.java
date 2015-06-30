package br.com.actia.controller;

import br.com.actia.action.AbstractAction;
import br.com.actia.action.BooleanExpression;
import br.com.actia.action.ConditionalAction;
import br.com.actia.action.TransactionalAction;
import br.com.actia.dao.BusStopDAO;
import br.com.actia.dao.BusStopDAOJPA;
import br.com.actia.dao.ListBusStopDAO;
import br.com.actia.dao.ListBusStopDAOJPA;
import br.com.actia.event.CrudListBusStopEvent;
import br.com.actia.model.BusStop;
import br.com.actia.model.ListBusStop;
import br.com.actia.ui.BusStopListVerticalView;
import br.com.actia.ui.BusStopListView;
import br.com.actia.ui.MainScreenView;
import br.com.actia.validation.ListBusStopValidator;
import br.com.actia.validation.Validator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public class ListBusStopController extends PersistenceController {
    private final ResourceBundle rb;
    private final Pane parentPane;
    private MainScreenView mainScreenView;
    private final BusStopListView view;
    private final BusStopListVerticalView viewList;
    private Validator<ListBusStop> validador = new ListBusStopValidator();

    private ListBusStop editLstBusStop;
    private ObservableList<ListBusStop> obsListBusStop;
    private ObservableList<BusStop> obsBusStopSelecteds;

    public ListBusStopController(AbstractController parent, MainScreenView mainScreenView, Pane pane, ResourceBundle rb) {
        super(parent);
        loadPersistenceContext(((PersistenceController) getParentController()).getPersistenceContext());
        this.rb = rb;
        this.mainScreenView = mainScreenView;
        this.parentPane = pane;
        this.view = new BusStopListView(this.rb);
        this.viewList = new BusStopListVerticalView(this.rb);
        
        obsBusStopSelecteds = FXCollections.observableArrayList();
        this.view.getLstvBusStops().setItems(obsBusStopSelecteds);

        //LIST INITIALIZE
        ListBusStopDAO lstBSDAO = new ListBusStopDAOJPA(getPersistenceContext());
        obsListBusStop = FXCollections.observableArrayList(lstBSDAO.getAll());
        FilteredList<ListBusStop> filteredData = new FilteredList<>(obsListBusStop, s -> true);
        viewList.getLstvBSList().setItems(filteredData);
        
        this.viewList.getTfFilter().textProperty().addListener(obs->{
            String filter = this.viewList.getTfFilter().getText(); 
            if(filter == null || filter.isEmpty()) {
                filteredData.setPredicate(s -> true);
            }
            else {
                filteredData.setPredicate(s -> s.toString().toLowerCase().contains(filter.toLowerCase()));
            }
        });
        
        //Drag and Drop ListView  ##############################################
        final IntegerProperty dragFromIndex = new SimpleIntegerProperty(-1);
        view.getLstvBusStops().setCellFactory(new Callback<ListView<BusStop>, ListCell<BusStop>>() {

            @Override
            public ListCell<BusStop> call(ListView<BusStop> lv) {
                final ListCell<BusStop> cell = new ListCell<BusStop>() {
                    @Override
                    public void updateItem(BusStop item, boolean empty) {
                        super.updateItem(item,  empty);
                        if (empty) {
                            setText(null);
                        } else {
                            setText(item.getName());
                        }
                    }
                };

                cell.setOnDragDetected(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (! cell.isEmpty()) {
                            dragFromIndex.set(cell.getIndex());
                            Dragboard db = cell.startDragAndDrop(TransferMode.MOVE);
                            ClipboardContent cc = new ClipboardContent();
                            cc.putString(cell.getItem().getName());
                            db.setContent(cc);
                            // Java 8 only:
//                          db.setDragView(cell.snapshot(null, null));
                        }
                    }
                });

                cell.setOnDragOver(new EventHandler<DragEvent>() {
                    @Override
                    public void handle(DragEvent event) {
                        if (dragFromIndex.get() >= 0 && dragFromIndex.get() != cell.getIndex()) {
                            event.acceptTransferModes(TransferMode.MOVE);
                        }
                    }
                });

                // highlight drop target by changing background color:
                cell.setOnDragEntered(new EventHandler<DragEvent>() {
                    @Override
                    public void handle(DragEvent event) {
                        if (dragFromIndex.get() >= 0 && dragFromIndex.get() != cell.getIndex()) {
                            // should really set a style class and use an external style sheet,
                            // but this works for demo purposes:
                            cell.setStyle("-fx-background-color: gold;");
                        }
                    }
                });

                // remove highlight:
                cell.setOnDragExited(new EventHandler<DragEvent>() {
                    @Override
                    public void handle(DragEvent event) {
                        cell.setStyle("");
                    }
                });

                cell.setOnDragDropped(new EventHandler<DragEvent>() {
                    @Override
                    public void handle(DragEvent event) {

                        int dragItemsStartIndex ;
                        int dragItemsEndIndex ;
                        int direction ;
                        if (cell.isEmpty()) {
                            dragItemsStartIndex = dragFromIndex.get();
                            dragItemsEndIndex = view.getLstvBusStops().getItems().size();
                            direction = -1;
                        } else {
                            if (cell.getIndex() < dragFromIndex.get()) {
                                dragItemsStartIndex = cell.getIndex();
                                dragItemsEndIndex = dragFromIndex.get() + 1 ;
                                direction = 1 ;
                            } else {
                                dragItemsStartIndex = dragFromIndex.get();
                                dragItemsEndIndex = cell.getIndex() + 1 ;
                                direction = -1 ;
                            }
                        }

                        List<BusStop> rotatingItems = view.getLstvBusStops().getItems().subList(dragItemsStartIndex, dragItemsEndIndex);
                        List<BusStop> rotatingItemsCopy = new ArrayList<>(rotatingItems);
                        Collections.rotate(rotatingItemsCopy, direction);
                        rotatingItems.clear();
                        rotatingItems.addAll(rotatingItemsCopy);
                        dragFromIndex.set(-1);
                    }
                });

                cell.setOnDragDone(new EventHandler<DragEvent>() {
                    @Override
                    public void handle(DragEvent event) {
                        dragFromIndex.set(-1);
                        view.getLstvBusStops().getSelectionModel().clearSelection();
                    }
                });
                return cell ;
            }
        });
        //######################################################################

        registerAction(this.view.getBtnCancel(), new AbstractAction() {
            @Override
            protected void action() {
                closeView();
            }
            
            @Override
            protected void posAction() {
                if (parent instanceof GoogleMapController && parent.getParentController() instanceof RouteController) {
                    GoogleMapController googleMapCtrl = (GoogleMapController) parent;
                    googleMapCtrl.closeView();

                    closeView();
                }
            }
        });
        
        registerAction(this.view.getBtnSave(),
            ConditionalAction.build()
                .addConditional(new BooleanExpression() {
                    @Override
                    public boolean conditional() {
                        ListBusStop listBusStop = loadBusStopListFromPanel();
                        String msg = validador.validate(listBusStop);
                        if (!"".equals(msg == null ? "" : msg)) {
                            // Dialog.showInfo("Validacão", msg, );
                             System.out.println(msg);
                             return false;
                        }
                        return true;
                    }
                })
                .addAction(TransactionalAction.build()
                    .persistenceCtxOwner(ListBusStopController.this)
                    .addAction(new AbstractAction() {
                        private ListBusStop listBusStopAux;
                        @Override
                        protected void action() {
                            ListBusStop listBusStop = loadBusStopListFromPanel();
                            ListBusStopDAO listBusStopDAO = new ListBusStopDAOJPA(getPersistenceContext());
                            listBusStopAux = listBusStopDAO.save(listBusStop);
                        }
                        
                        @Override
                        protected void posAction() {
                            fireEvent(new CrudListBusStopEvent(listBusStopAux));
                            refreshVerticalList();//listBusStopAux);
                            refreshForm(null);
                            
                            if (parent instanceof GoogleMapController && parent.getParentController() instanceof RouteController) {
                                GoogleMapController googleMapCtrl = (GoogleMapController) parent;
                                googleMapCtrl.closeView();

                                closeView();
                            }
                        }
                    }))
        );
        
        registerAction(this.view.getBtnDelete(),
                TransactionalAction.build()
                    .persistenceCtxOwner(ListBusStopController.this)
                    .addAction(new AbstractAction() {
                        private ListBusStop listBusStopAux;
                        
                        @Override
                        protected void action() {
                            Integer id = Integer.parseInt(view.getTfId().getText());
                            if (id != null) {
                                ListBusStopDAO listBusStopDao = new ListBusStopDAOJPA(getPersistenceContext());
                                listBusStopAux = listBusStopDao.findById(id);
                                if(listBusStopAux != null) {
                                    listBusStopDao.remove(listBusStopAux);
                                }
                            }
                        }
                        @Override
                        protected void posAction() {
                            view.resetForm();
                            fireEvent(new CrudListBusStopEvent(listBusStopAux));
                            refreshVerticalList();
                            refreshForm(null);
                            obsListBusStop.remove(listBusStopAux);
                        }
                        @Override
                        protected void actionFailure(){
                            // TO DO
                        }
                    })
        );
        
        this.viewList.getLstvBSList().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                editLstBusStop = viewList.getLstvBSList().getSelectionModel().getSelectedItem();
                loadListBusStopToPanel(editLstBusStop);
            }
        });
        
        this.view.getLstvBusStops().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                BusStop selectedBST = view.getLstvBusStops().getSelectionModel().getSelectedItem();
                if(obsBusStopSelecteds.contains(selectedBST)) {
                    obsBusStopSelecteds.remove(selectedBST);
                    view.getLstvBusStops().getSelectionModel().clearSelection();
                }
            }
            
        });
        
        StackPane.setAlignment(view, Pos.BOTTOM_CENTER);
        StackPane.setAlignment(viewList, Pos.TOP_RIGHT);
    }

    public void showView() {
        refreshForm(null);
        
        if(!parentPane.getChildren().contains(view))
            parentPane.getChildren().add(view);
        
        if(!parentPane.getChildren().contains(viewList))
            parentPane.getChildren().add(viewList);
    }
        
    public void closeView() {
        parentPane.getChildren().remove(view);
        parentPane.getChildren().remove(viewList);
        editLstBusStop = null;
    }
    
    public void addBusStop(String busStopName) {
        BusStopDAO busStopDAOJPA = new BusStopDAOJPA(getPersistenceContext());
        List<BusStop> lstBusStop = busStopDAOJPA.getBusStopByName(busStopName);
        
        if(lstBusStop.size() >= 1) {
            addBusStop(lstBusStop.get(0));
        }
    }
    
    public void addBusStop(BusStop busStop) {
        if(obsBusStopSelecteds.contains(busStop)) {
            obsBusStopSelecteds.remove(busStop);
        }
        else {
            obsBusStopSelecteds.add(busStop);
        }
    }
    
    public void removeAllBusStop() {
        obsBusStopSelecteds.clear();
    }
    
    private void refreshForm(ListBusStop listBusStop) {
        if(listBusStop != null) {
            this.view.getTfId().setText(listBusStop.getId().toString());
            this.view.getTfName().setText(listBusStop.getName());
            this.obsBusStopSelecteds.clear();
            this.obsBusStopSelecteds.setAll(listBusStop.getListBusStop());
            this.view.getBtnDelete().setVisible(true);
        }
        else {
            this.view.getTfId().clear();
            this.view.getTfName().clear();
            this.obsBusStopSelecteds.clear();
            this.view.getBtnDelete().setVisible(false);
            editLstBusStop = null;
        }
    }

    private void refreshVerticalList() {
        obsListBusStop.clear();
        ListBusStopDAO lstBSDAO = new ListBusStopDAOJPA(getPersistenceContext());
        obsListBusStop.setAll(lstBSDAO.getAll());
        
        editLstBusStop = null;
    }
    
    boolean isVisible() {
        return parentPane.getChildren().contains(view);
    }
    
    private ListBusStop loadBusStopListFromPanel() {
        Integer id = null;
        if(!view.getTfId().getText().trim().isEmpty()) {
            id = Integer.valueOf(view.getTfId().getText());
        }
        
        String name = null;
        if(!view.getTfName().getText().trim().isEmpty()) {
            name = view.getTfName().getText();
        }
        ListBusStop listBusStop = new ListBusStop(id, name, obsBusStopSelecteds);
        return listBusStop;
    }
    
    private void loadListBusStopToPanel(ListBusStop lstBusStop) {
        refreshForm(lstBusStop);
        removeAllBusStop();
        
        obsBusStopSelecteds.setAll(lstBusStop.getListBusStop());
    }
    
    @Override
    protected void cleanUp() {
        view.resetForm();
        closeView();
        super.cleanUp();
    }
}
