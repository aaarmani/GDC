package br.com.actia.controller;

import br.com.actia.action.AbstractAction;
import br.com.actia.action.BooleanExpression;
import br.com.actia.action.ConditionalAction;
import br.com.actia.action.TransactionalAction;
import br.com.actia.dao.ListPoiDAO;
import br.com.actia.dao.ListPoiDAOJPA;
import br.com.actia.dao.PoiDAO;
import br.com.actia.dao.PoiDAOJPA;
import br.com.actia.event.AbstractEventListener;
import br.com.actia.event.CrudListPoiEvent;
import br.com.actia.event.CrudPoiEvent;
import br.com.actia.model.ListPoi;
import br.com.actia.model.Poi;
import br.com.actia.ui.EntityListView;
import br.com.actia.ui.MainScreenView;
import br.com.actia.validation.ListPoiValidator;
import br.com.actia.validation.Validator;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public class ListPoiController extends PersistenceController {
    private final ResourceBundle rb;
    private final Pane parentPane;
    private final EntityListView<Poi, ListPoi> view;
    private Validator<ListPoi> validador = new ListPoiValidator();
    private GoogleMapController googleMapController = null;
    private MainScreenView mainScreenView;
    
    private PoiDAO poiDAO = null;
    private Collection<Poi> listPoiAll = null;
    
    public ListPoiController(AbstractController parent, MainScreenView mainScreenView, ResourceBundle rb) {
        super(parent);
        loadPersistenceContext(((PersistenceController) getParentController()).getPersistenceContext());
        this.rb = rb;
        this.mainScreenView = mainScreenView;
        this.parentPane = mainScreenView.getPaneCenter();
        this.view = new EntityListView<>(this.rb);
        this.view.setMaxHeight(parentPane.getHeight());
        this.view.setMaxWidth(parentPane.getWidth());
        this.view.setMinHeight(parentPane.getHeight());
        this.view.setMinWidth(parentPane.getWidth());
        
        this.poiDAO = new PoiDAOJPA(getPersistenceContext());
        this.listPoiAll = (Collection<Poi>)this.poiDAO.getAll();
        
        registerAction(this.view.getBtnCancel(), new AbstractAction() {
            @Override
            protected void action() {
                closeView();
            }
        });
        
        registerAction(this.view.getBtnSave(),
            ConditionalAction.build()
                .addConditional(new BooleanExpression() {
                    @Override
                    public boolean conditional() {
                        ListPoi listPoi = loadListPoiFromView();

                        String msg = validador.validate(listPoi);
                        if (!"".equals(msg == null ? "" : msg)) {
                            // Dialog.showInfo("Validacão", msg, );
                             System.out.println(msg);
                             return false;
                        }
                        return true;
                    }
                })
                .addAction(TransactionalAction.build()
                    .persistenceCtxOwner(ListPoiController.this)
                    .addAction(new AbstractAction() {
                        ListPoi listPoi = null;
                        
                        @Override
                        protected void action() {
                            listPoi = loadListPoiFromView();
                            ListPoiDAO listPoiDAO = new ListPoiDAOJPA(getPersistenceContext());
                            listPoi = listPoiDAO.save(listPoi);
                        }
                        
                        @Override
                        protected void posAction() {
                            fireEvent(new CrudListPoiEvent(listPoi));
                            //cleanUp();
                            resetForm();
                            refreshTable();
                            
                            if(parent instanceof BusStopController){
                                closeView();
                            }                            
                        }
                    }))
        );
        
        registerAction(this.view.getBtnDelete(),
                TransactionalAction.build()
                    .persistenceCtxOwner(ListPoiController.this)
                    .addAction(new AbstractAction() {
                        private ListPoi listPoi;
                        
                        @Override
                        protected void action() {
                            Integer id = Integer.parseInt(view.getTfId().getText());
                            if (id != null) {
                                ListPoiDAO listPoiDao = new ListPoiDAOJPA(getPersistenceContext());
                                listPoi = listPoiDao.findById(id);
                                if(listPoi != null) {
                                    listPoiDao.remove(listPoi);
                                }
                            }
                        }
                        @Override
                        protected void posAction() {
                            resetForm();
                            refreshTable();
                            fireEvent(new CrudListPoiEvent(listPoi));
                        }
                        @Override
                        protected void actionFailure(){
                            // TO DO
                        }
                    })
        );
        
        view.getTable().setMouseEvent(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent t) {
                if (t.getClickCount() == 2) {
                    ListPoi listPoi = (ListPoi)view.getTable().getEntitySelected();
                    if (listPoi != null) {
                        loadListPoiToEdit(listPoi);
                    }
                }
            }
        });
        
        registerAction(view.getBtnNewEntity(), new AbstractAction() {
            @Override
            protected void action() {
                showGoogleMapControllerAndNewPOI();
                
            }
        });
        
        registerEventListener(CrudPoiEvent.class, new AbstractEventListener<CrudPoiEvent>() {
            @Override
            public void handleEvent(CrudPoiEvent event) {
                refreshListPoisForSelection();
                resetForm();
                refreshTable();
            }
        });
        
        StackPane.setAlignment(view, Pos.CENTER);
        this.view.resetForm(this.listPoiAll);
        refreshTable();
    }

    public void showView() {
        parentPane.getChildren().add(view);
    }
        
    public void closeView() {
        parentPane.getChildren().remove(view);
    }
    
    @Override
    protected void cleanUp() {
        view.resetForm(this.listPoiAll);
        closeView();
        super.cleanUp();
    }

    private ListPoi loadListPoiFromView() {
        Integer id = null;
        if(!view.getTfId().getText().trim().isEmpty()) {
            id = Integer.valueOf(view.getTfId().getText());
        }
        
        String name = null;
        if(!view.getTfName().getText().trim().isEmpty()) {
            name = view.getTfName().getText();
        }
        
        String description = null;
        if(!view.getTfDescription().getText().trim().isEmpty()) {
            description = view.getTfDescription().getText();
        }
        
        List<Poi> listPoi = null;
        if(!view.getLsvEntity().getTargetItems().isEmpty()) {
            listPoi = view.getLsvEntity().getTargetItems();
        }

        return new ListPoi(id, name, description, listPoi);
    }
    
    private void showGoogleMapControllerAndNewPOI() {
        if(googleMapController == null)
            googleMapController = new GoogleMapController(this, mainScreenView, rb);

        googleMapController.showView();
        googleMapController.showPoiController(null);
    }
    
    public void enableNewPoiButton(Boolean enable) {
        view.getBtnNewEntity().setVisible(enable);
    }
    
    public void refreshTable() {
        refreshTable(null);
    }
    
    private void refreshTable(List<ListPoi> list) {
        //view.addTransition();
        if (list != null) {
            view.refreshTable(list);
            return;
        }
        
        Platform.runLater(new Runnable() {
            
            @Override
            public void run() {
                ListPoiDAO dao = new ListPoiDAOJPA(getPersistenceContext());
                view.refreshTable(dao.getAll());
            }
        });
    }
    
    private void loadListPoiToEdit(ListPoi listPoi) {
        if(listPoi.getId() != null) {
            this.view.getTfId().setText(listPoi.getId().toString());
            this.view.getTfName().setText(listPoi.getName());
            this.view.getTfDescription().setText(listPoi.getDescription());

            this.view.getLsvEntity().getSourceItems().clear();
            this.view.getLsvEntity().getSourceItems().addAll((Collection<Poi>)this.listPoiAll);
            this.view.getLsvEntity().getSourceItems().removeAll((Collection<Poi>)listPoi.getListPoi());

            this.view.getLsvEntity().getTargetItems().clear();
            this.view.getLsvEntity().getTargetItems().addAll((Collection<Poi>)listPoi.getListPoi());

            this.view.getBtnDelete().setVisible(true);
        }
    }
    
    public void resetForm(){
        this.view.resetForm(this.listPoiAll);
    }
    
    public void refreshListPoisForSelection(){
        this.poiDAO = new PoiDAOJPA(getPersistenceContext());
        this.listPoiAll = (Collection<Poi>)this.poiDAO.getAll();
    }
}
