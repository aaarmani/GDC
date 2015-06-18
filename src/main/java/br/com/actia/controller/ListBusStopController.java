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
import br.com.actia.ui.BusStopList2View;
import br.com.actia.ui.BusStopListView;
import br.com.actia.ui.MainScreenView;
import br.com.actia.validation.ListBusStopValidator;
import br.com.actia.validation.Validator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public class ListBusStopController extends PersistenceController {
    private final ResourceBundle rb;
    private final Pane parentPane;
    private MainScreenView mainScreenView;
    private final BusStopListView view;
    private final BusStopList2View viewList;
    private Validator<ListBusStop> validador = new ListBusStopValidator();
    private ListBusStop listBusStop;
    private Map<String, Label> mapOfBusStops;
    private ObservableList<ListBusStop> obsListBusStop;

    public ListBusStopController(AbstractController parent, MainScreenView mainScreenView, Pane pane, ResourceBundle rb) {
        super(parent);
        loadPersistenceContext(((PersistenceController) getParentController()).getPersistenceContext());
        this.rb = rb;
        this.mainScreenView = mainScreenView;
        this.parentPane = pane;
        this.view = new BusStopListView(this.rb);
        this.viewList = new BusStopList2View(this.rb);
        
        mapOfBusStops = new HashMap<>();
        

        //LIST INITIALIZE
        ListBusStopDAO lstBSDAO = new ListBusStopDAOJPA(getPersistenceContext());
        obsListBusStop = FXCollections.observableArrayList(lstBSDAO.getAll());
        FilteredList<ListBusStop> filteredData = new FilteredList<>(obsListBusStop, s -> true);
        viewList.getLstvBSList().setItems(filteredData);
        
        //ListView Filter        
        //FilteredList<ListBusStop> filteredData = new FilteredList<>(obsListBusStop, s -> true);
        this.viewList.getTfFilter().textProperty().addListener(obs->{
            String filter = this.viewList.getTfFilter().getText(); 
            if(filter == null || filter.isEmpty()) {
                filteredData.setPredicate(s -> true);
            }
            else {
                filteredData.setPredicate(s -> s.toString().toLowerCase().contains(filter.toLowerCase()));
            }
        });

        
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
                        loadBusStopListFromPanel();

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
                            loadBusStopListFromPanel();
                            ListBusStopDAO listBusStopDAO = new ListBusStopDAOJPA(getPersistenceContext());
                            listBusStopAux = listBusStopDAO.save(listBusStop);
                        }
                        
                        @Override
                        protected void posAction() {
                            fireEvent(new CrudListBusStopEvent(listBusStopAux));
                            //cleanUp();
                            view.resetForm();
                            refreshForm(null);
                            obsListBusStop.add(listBusStopAux);
                            
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
                ListBusStop lstBusStop = viewList.getLstvBSList().getSelectionModel().getSelectedItem();
                loadListBusStopToPanel(lstBusStop);
            }
        });
        
        StackPane.setAlignment(view, Pos.BOTTOM_CENTER);
        StackPane.setAlignment(viewList, Pos.TOP_LEFT);
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
    }
    
    public void addBusStop(String busStop) {
        if(mapOfBusStops.containsKey(busStop)) {
            view.getHbLabels().getChildren().remove(mapOfBusStops.get(busStop));
            mapOfBusStops.remove(busStop);
        }
        else {
            Label lb = new Label(busStop);
            lb.getStyleClass().add("lbList");
            view.getHbLabels().getChildren().add(lb);
            mapOfBusStops.put(busStop, lb);
        }
    }
    
    public void removeAllBusStop() {
        mapOfBusStops.clear();
        view.getHbLabels().getChildren().clear();
    }
    
    private void refreshForm(ListBusStop listBusStop) {
        if(listBusStop != null) {
            this.view.getTfId().setText(listBusStop.getId().toString());
            this.view.getTfName().setText(listBusStop.getName());
            this.listBusStop = listBusStop;
            this.view.getBtnDelete().setVisible(true);
        }
        else {
            this.view.getTfId().clear();
            this.view.getTfName().clear();
            this.listBusStop = new ListBusStop();
            this.view.getBtnDelete().setVisible(false);
            this.mapOfBusStops.clear();
            this.view.getHbLabels().getChildren().clear();
        }
    }

    boolean isVisible() {
        return parentPane.getChildren().contains(view);
    }
    
    private void loadBusStopListFromPanel() {
        this.listBusStop = view.loadBusStopFromPanel();
        
        this.listBusStop.getListBusStop().clear();
        
        for(Map.Entry<String, Label> entry : mapOfBusStops.entrySet()) {
            
            BusStopDAO bstpDAO = new BusStopDAOJPA(this.getPersistenceContext());
            List<BusStop> lstBstp = bstpDAO.getBusStopByName(entry.getKey());
            
            if(lstBstp != null && lstBstp.size() >= 1)
            {
                BusStop bst = lstBstp.get(0);
                this.listBusStop.getListBusStop().add(bst);
            }
        }
    }
    
    private void loadListBusStopToPanel(ListBusStop lstBusStop) {
        refreshForm(lstBusStop);
        removeAllBusStop();
        
        for (BusStop bst : lstBusStop.getListBusStop()) {
            addBusStop(bst.getName());
        }
    }
    
    @Override
    protected void cleanUp() {
        view.resetForm();
        closeView();
        super.cleanUp();
    }
}
