package br.com.actia.controller;

import br.com.actia.action.AbstractAction;
import br.com.actia.action.BooleanExpression;
import br.com.actia.action.ConditionalAction;
import br.com.actia.action.TransactionalAction;
import br.com.actia.dao.ListVideoDAO;
import br.com.actia.dao.ListVideoDAOJPA;
import br.com.actia.dao.VideoDAO;
import br.com.actia.dao.VideoDAOJPA;
import br.com.actia.event.CrudListVideoEvent;
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
import javafx.application.Platform;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;
import javafx.collections.ObservableList;

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public class ListVideoController extends PersistenceController {
    private final ResourceBundle rb;
    private final Pane parentPane;
    private final EntityListView<Video, ListVideo> view;
    private Validator<ListVideo> validador = new ListVideoValidator();
    private VideoController videoController = null;
    
    private VideoDAO videoDAO = null;
    private Collection<Video> listVideoAll = null;
    
    public ListVideoController(AbstractController parent, Pane pane, ResourceBundle rb) {
        super(parent);
        loadPersistenceContext(((PersistenceController) getParentController()).getPersistenceContext());
        this.rb = rb;
        this.parentPane = pane;
        this.view = new EntityListView<Video, ListVideo>(this.rb);
        this.view.setMaxHeight(parentPane.getHeight());
        this.view.setMaxWidth(parentPane.getWidth());
        this.view.setMinHeight(parentPane.getHeight());
        this.view.setMinWidth(parentPane.getWidth());
        
        this.videoDAO = new VideoDAOJPA(getPersistenceContext());
        this.listVideoAll = (Collection<Video>)this.videoDAO.getAll();

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
                            //cleanUp();
                            resetForm();
                            //fireEvent(new CrudListVideoEvent(listVideo));
                            refreshTable();
                        }
                    }))
        );
        
        registerAction(view.getBtnNewEntity(), new AbstractAction() {
            @Override
            protected void action() {
                showNewVideoController();
            }
        });
        
        view.getTable().setMouseEvent(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent t) {
                if (t.getClickCount() == 2) {
                    ListVideo listVideo = (ListVideo)view.getTable().getEntitySelected();
                    if (listVideo != null) {
                        loadListVideoToEdit(listVideo);
                    }
                }
            }
        });
        
        registerAction(this.view.getBtnDelete(),
                TransactionalAction.build()
                    .persistenceCtxOwner(ListVideoController.this)
                    .addAction(new AbstractAction() {
                        private ListVideo listVideo;
                        
                        @Override
                        protected void action() {
                            Integer id = Integer.parseInt(view.getTfId().getText());
                            if (id != null) {
                                ListVideoDAO listVideoDao = new ListVideoDAOJPA(getPersistenceContext());
                                listVideo = listVideoDao.findById(id);
                                if(listVideo != null) {
                                    listVideoDao.remove(listVideo);
                                }
                            }
                        }
                        @Override
                        protected void posAction() {
                            resetForm();
                            refreshTable();
                        }
                        @Override
                        protected void actionFailure(){
                            // TO DO
                        }
                    })
        );
        
        StackPane.setAlignment(view, Pos.CENTER);
        this.view.resetForm(this.listVideoAll);
        this.refreshTable();
    }

    public void showView() {
        parentPane.getChildren().add(view);
    }
        
    public void closeView() {
        parentPane.getChildren().remove(view);
    }
    
    @Override
    protected void cleanUp() {
        view.resetForm(this.listVideoAll);
        closeView();
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
    
    private void refreshTable() {
        refreshTable(null);
    }
    
    private void refreshTable(List<ListVideo> list) {
        //view.addTransition();
        if (list != null) {
            view.refreshTable(list);
            return;
        }
        
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                ListVideoDAO dao = new ListVideoDAOJPA(getPersistenceContext());
                view.refreshTable(dao.getAll());
            }
        });
    }
    
    private void loadListVideoToEdit(ListVideo listVideo) {
        this.view.getTfId().setText(listVideo.getId().toString());
        this.view.getTfName().setText(listVideo.getName());
        this.view.getTfDescription().setText(listVideo.getDescription());
        
        this.view.getLsvEntity().getSourceItems().clear();
        this.view.getLsvEntity().getSourceItems().addAll((Collection<Video>)this.listVideoAll);
        this.view.getLsvEntity().getSourceItems().removeAll((Collection<Video>)listVideo.getListVideo());
        
        this.view.getLsvEntity().getTargetItems().clear();
        this.view.getLsvEntity().getTargetItems().addAll((Collection<Video>)listVideo.getListVideo());
        
        this.view.getBtnDelete().setVisible(true);
    }
    
    private void resetForm(){
        this.view.resetForm(this.listVideoAll);
    }
}
