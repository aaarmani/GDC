package br.com.actia.controller;

import br.com.actia.action.AbstractAction;
import br.com.actia.action.BooleanExpression;
import br.com.actia.action.ConditionalAction;
import br.com.actia.action.TransactionalAction;
import br.com.actia.dao.ListPoiDAO;
import br.com.actia.dao.ListPoiDAOJPA;
import br.com.actia.dao.PoiDAO;
import br.com.actia.dao.PoiDAOJPA;
import br.com.actia.event.CrudListPoiEvent;
import br.com.actia.model.ListPoi;
import br.com.actia.model.Poi;
import br.com.actia.ui.EntityListView;
import br.com.actia.validation.ListPoiValidator;
import br.com.actia.validation.Validator;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import org.controlsfx.control.ListSelectionView;

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
    
    private PoiDAO poiDAO = null;
    private Collection<Poi> listPoiAll = null;
    
    public ListPoiController(AbstractController parent, Pane pane, ResourceBundle rb) {
        super(parent);
        loadPersistenceContext(((PersistenceController) getParentController()).getPersistenceContext());
        this.rb = rb;
        this.parentPane = pane;
        this.view = new EntityListView<Poi, ListPoi>(this.rb);
        this.view.setMaxHeight(parentPane.getHeight());
        this.view.setMaxWidth(parentPane.getWidth());
        this.view.setMinHeight(parentPane.getHeight());
        this.view.setMinWidth(parentPane.getWidth());
        
        this.poiDAO = new PoiDAOJPA(getPersistenceContext());
        this.listPoiAll = (Collection<Poi>)this.poiDAO.getAll();
        
        loadEntityToList();
        
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
                        }
                    }))
        );
        
        view.getTable().setMouseEvent(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent t) {
                if (t.getClickCount() == 2) {
                    
System.out.println("getTable: " + view.getTable().toString() );
                    
                    ListPoi listPoi = (ListPoi)view.getTable().getEntitySelected();
                    
System.out.println("listPoi getListPoi: " + listPoi.toString());
                    
                    if (listPoi != null) {
                        loadListPoiToEdit(listPoi);
                    }
                }
            }
        });
        
        registerAction(view.getBtnNewEntity(), new AbstractAction() {
            @Override
            protected void action() {
                showGoogleMapController();
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

    private void loadEntityToList() {
        PoiDAO poiDAO = new PoiDAOJPA(getPersistenceContext());
        this.view.getLsvEntity().getSourceItems().addAll((Collection<Poi>)poiDAO.getAll());
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
    
    private void showGoogleMapController() {
        if(googleMapController == null)
            googleMapController = new GoogleMapController(this, parentPane, rb);

        googleMapController.showView();
    }
    
    public void enableNewPoiButton(Boolean enable) {
        view.getBtnNewEntity().setVisible(enable);
    }
    
    private void refreshTable() {
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
        //ListPoi listPoi2 = null; 
        
        ListPoiDAO listPoiDAO = new ListPoiDAOJPA(getPersistenceContext());
        ListPoi listPoi2 = listPoiDAO.findById(listPoi.getId());
        
        this.view.getTfId().setText(listPoi.getId().toString());
        this.view.getTfName().setText(listPoi.getName());
        this.view.getTfDescription().setText(listPoi.getDescription());
        
// System.out.println("list de todos os pois: " + this.listPoiAll.toString());
// System.out.println("list de pois da lista selecionada: " + listPoi2.getListPoi().toString());
        
        this.view.getLsvEntity().getSourceItems().clear();
        this.view.getLsvEntity().getSourceItems().addAll((Collection<Poi>)this.listPoiAll);
        this.view.getLsvEntity().getSourceItems().removeAll((Collection<Poi>)listPoi2.getListPoi());
        
        this.view.getLsvEntity().getTargetItems().clear();
        this.view.getLsvEntity().getTargetItems().addAll((Collection<Poi>)listPoi2.getListPoi());
    }
    
    private void resetForm(){
        this.view.resetForm(this.listPoiAll);
    }
}
