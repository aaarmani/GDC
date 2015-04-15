package br.com.actia.controller;

import br.com.actia.action.AbstractAction;
import br.com.actia.action.BooleanExpression;
import br.com.actia.action.ConditionalAction;
import br.com.actia.action.TransactionalAction;
import br.com.actia.dao.ListPoiDAO;
import br.com.actia.dao.ListPoiDAOJPA;
import br.com.actia.dao.PoiDAO;
import br.com.actia.dao.PoiDAOJPA;
import br.com.actia.javascript.event.CrudListPoiEvent;
import br.com.actia.model.ListPoi;
import br.com.actia.model.Poi;
import br.com.actia.ui.EntityListView;
import br.com.actia.validation.ListPoiValidator;
import br.com.actia.validation.Validator;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public class ListPoiController extends PersistenceController {
    private final ResourceBundle rb;
    private final Pane parentPane;
    private final EntityListView<Poi> view;
    private Validator<ListPoi> validador = new ListPoiValidator();
    private GoogleMapController googleMapController = null;

    public ListPoiController(AbstractController parent, Pane pane, ResourceBundle rb) {
        super(parent);
        loadPersistenceContext(((PersistenceController) getParentController()).getPersistenceContext());
        this.rb = rb;
        this.parentPane = pane;
        this.view = new EntityListView<Poi>(this.rb);
        this.view.setMaxHeight(parentPane.getHeight());
        this.view.setMaxWidth(parentPane.getWidth());
        this.view.setMinHeight(parentPane.getHeight());
        this.view.setMinWidth(parentPane.getWidth());
        
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
                            cleanUp();
                            fireEvent(new CrudListPoiEvent(listPoi));
                        }
                    }))
        );
        
        registerAction(view.getBtnNewEntity(), new AbstractAction() {
            @Override
            protected void action() {
                showGoogleMapController();
            }
        });
        
        StackPane.setAlignment(view, Pos.CENTER);
        this.view.resetForm();
    }

    public void showView() {
        parentPane.getChildren().add(view);
    }
        
    public void closeView() {
        parentPane.getChildren().remove(view);
    }
    
    @Override
    protected void cleanUp() {
        view.resetForm();
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
}
