package br.com.actia.controller;

import br.com.actia.action.AbstractAction;
import br.com.actia.action.BooleanExpression;
import br.com.actia.action.ConditionalAction;
import br.com.actia.action.TransactionalAction;
import br.com.actia.dao.ListVideoDAO;
import br.com.actia.dao.ListVideoDAOJPA;
import br.com.actia.dao.VideoDAO;
import br.com.actia.dao.VideoDAOJPA;
import br.com.actia.model.ListVideo;
import br.com.actia.model.Video;
import br.com.actia.ui.EntityListView;
import br.com.actia.validation.ListVideoValidator;
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
public class ListVideoController extends PersistenceController {
    private final ResourceBundle rb;
    private final Pane parentPane;
    private final EntityListView<Video> view;
    private Validator<ListVideo> validador = new ListVideoValidator();
    private VideoController videoController = null;
    
    public ListVideoController(AbstractController parent, Pane pane, ResourceBundle rb) {
        super(parent);
        loadPersistenceContext(((PersistenceController) getParentController()).getPersistenceContext());
        this.rb = rb;
        this.parentPane = pane;
        this.view = new EntityListView<Video>(this.rb);
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
                        ListVideo listVideo = loadListVideoFromView();

                        String msg = validador.validate(listVideo);
                        if (!"".equals(msg == null ? "" : msg)) {
                            // Dialog.showInfo("Validacão", msg, );
                             System.out.println(msg);
                             return false;
                        }
                        return true;
                    }
                })
                .addAction(TransactionalAction.build()
                    .persistenceCtxOwner(ListVideoController.this)
                    .addAction(new AbstractAction() {
                        ListVideo listVideo = null;
                        @Override
                        protected void action() {
                            listVideo = loadListVideoFromView();
                            ListVideoDAO listVideoDAO = new ListVideoDAOJPA(getPersistenceContext());
                            listVideo = listVideoDAO.save(listVideo);
                        }
                        
                         @Override
                        protected void posAction() {
                            cleanUp();
                        }
                    }))
        );
        
        registerAction(view.getBtnNewEntity(), new AbstractAction() {
            @Override
            protected void action() {
                showNewVideoController();
            }
        });
        
        parentPane.getChildren().add(view);
        StackPane.setAlignment(view, Pos.CENTER);
        this.view.resetForm();
        showView();
    }

    public void showView() {
        this.view.setVisible(true);
    }
        
    public void closeView() {
        this.view.setVisible(false);
        this.cleanUp();
    }
    
    @Override
    protected void cleanUp() {
        view.resetForm();
        super.cleanUp();
    }

    private void loadEntityToList() {
        VideoDAO videoDAO = new VideoDAOJPA(getPersistenceContext());
        this.view.getLsvEntity().getSourceItems().addAll((Collection<Video>)videoDAO.getAll());
    }
    
    private ListVideo loadListVideoFromView() {
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
        
        List<Video> listVideo = null;
        if(!view.getLsvEntity().getTargetItems().isEmpty()) {
            listVideo = view.getLsvEntity().getTargetItems();
        }

        return new ListVideo(id, name, description, listVideo);
    }
    
    private void showNewVideoController() {
        if(videoController == null)
            videoController = new VideoController(this, parentPane, rb);
        
        videoController.showView();
    }
}
