package br.com.actia.controller;

import br.com.actia.action.AbstractAction;
import br.com.actia.action.BooleanExpression;
import br.com.actia.action.ConditionalAction;
import br.com.actia.action.TransactionalAction;
import br.com.actia.dao.PoiDAO;
import br.com.actia.dao.PoiDAOJPA;
import br.com.actia.model.AbstractEntity;
import br.com.actia.ui.EntityListView;
import java.util.Collection;
import java.util.ResourceBundle;
import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public class EntityListController<T> extends PersistenceController {
    private final ResourceBundle rb;
    private final Pane parentPane;
    private final EntityListView<T> view;
    private AbstractEntity abstractEntity;

    public EntityListController(AbstractController parent, Pane pane, ResourceBundle rb, AbstractEntity aeEntity) {
        super(parent);
        loadPersistenceContext(((PersistenceController) getParentController()).getPersistenceContext());
        this.rb = rb;
        this.parentPane = pane;
        this.view = new EntityListView<T>(this.rb);
        this.abstractEntity = aeEntity;
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
                        /*Banner banner = view.loadBannerFromPanel();
                        String msg = validador.validate(banner);
                        if (!"".equals(msg == null ? "" : msg)) {
                            // Dialog.showInfo("Validacão", msg, );
                             System.out.println(msg);
                             return false;
                        }*/

                        //return true;
                        
                        return false;
                    }
                })
                .addAction(TransactionalAction.build()
                    .persistenceCtxOwner(EntityListController.this)
                    .addAction(new AbstractAction() {

                        @Override
                        protected void action() {
                            
                        }
                        
                         @Override
                        protected void posAction() {
                            cleanUp();
                        }
                    }))
        );
        
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
        
        this.view.getLsvEntity().getSourceItems().addAll((Collection<T>)poiDAO.getAll());
    }
}
