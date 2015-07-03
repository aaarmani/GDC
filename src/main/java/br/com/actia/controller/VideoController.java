package br.com.actia.controller;

import br.com.actia.action.AbstractAction;
import br.com.actia.action.BooleanExpression;
import br.com.actia.action.ConditionalAction;
import br.com.actia.action.TransactionalAction;
import br.com.actia.dao.VideoDAO;
import br.com.actia.dao.VideoDAOJPA;
import br.com.actia.dao.VideoTypeDAO;
import br.com.actia.dao.VideoTypeDAOJPA;
import br.com.actia.event.CopyFileEvent;
import br.com.actia.event.CrudBannerEvent;
import br.com.actia.event.CrudVideoEvent;
import service.FileToCopy;
import br.com.actia.model.Video;
import br.com.actia.model.VideoType;
import br.com.actia.ui.Dialog;
import br.com.actia.ui.MainScreenView;
import br.com.actia.ui.VideoView;
import br.com.actia.validation.Validator;
import br.com.actia.validation.VideoValidator;
import java.io.File;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public class VideoController extends PersistenceController {
    private VideoView view;
    private final Validator<Video> validador = new VideoValidator();
    private final Pane parentPane;
    private MainScreenView mainScreenView;
    
    private File videoFile;
    
    private MediaPlayer mediaPlayer = null;
    private Boolean mediaStarted = false;
    
    private ResourceBundle rb;

    public VideoController(AbstractController parent, MainScreenView mainScreenView, ResourceBundle rb) {
        super(parent);
        loadPersistenceContext(((PersistenceController) getParentController()).getPersistenceContext());
        this.rb = rb;
        this.mainScreenView = mainScreenView;
        this.parentPane = mainScreenView.getPaneCenter();
        this.view = new VideoView(rb);
        this.view.setMaxHeight(parentPane.getHeight());
        this.view.setMaxWidth(parentPane.getWidth());
        this.view.setMinHeight(parentPane.getHeight());
        this.view.setMinWidth(parentPane.getWidth());
        this.view.getBtnPlayVideo().setVisible(false);
        
        ObservableList<VideoType> lstVideoType = getVideoTypeList();
        this.view.getCbVideoType().getItems().addAll(lstVideoType);
        
        registerAction(this.view.getBtnCancelVideo(), new AbstractAction() {
            @Override
            protected void action() {
                closeView();         
            }
        });
        
        registerAction(this.view.getBtnSaveVideo(),
            ConditionalAction.build()
                .addConditional(new BooleanExpression() {
                    @Override
                    public boolean conditional() {
                        Video video = view.loadVideoFromPanel();
                        String msg = validador.validate(video, rb);
                        if (!"".equals(msg == null ? "" : msg)) {
                            Dialog.showError(rb.getString("VALIDATION"), msg);
                            return false;
                        }

                        return true;
                    }
                })
                .addAction(TransactionalAction.build()
                        .persistenceCtxOwner(VideoController.this)
                        .addAction(new AbstractAction() {
                            private Video video;
                            
                            @Override
                            protected void action() {
                                video = view.loadVideoFromPanel();
                                VideoDAO videoDao = new VideoDAOJPA(getPersistenceContext());
                                video = videoDao.save(video);
                            }
                            @Override
                            protected void posAction() {
                                video = view.loadVideoFromPanel();
                                fireEvent(new CrudVideoEvent(video));
                                if(video.getVideoPath() != null){
                                    copyFileToDisk(video);
                                }
                                view.resetForm();
                                refreshTable();

                                if(parent instanceof ListVideoController){
                                    closeView();
                                }
                            }
                            @Override
                            protected void actionFailure(){
                                
                            }
                        })
                )
        );
        
        registerAction(this.view.getBtnDeleteVideo(),
            TransactionalAction.build()
                .persistenceCtxOwner(VideoController.this)
                .addAction(new AbstractAction() {
                    private Video video;

                    @Override
                    protected void action() {
                        Integer id = view.getVideoId();
                        if (id != null) {
                            VideoDAO videoDao = new VideoDAOJPA(getPersistenceContext());
                            video = videoDao.findById(id);
                            if (video != null) { 
                                videoDao.remove(video);
                            }
                        }
                    }
                    @Override
                    protected void posAction() {
                        view.resetForm();
                        refreshTable();
                        //fireEvent(new CrudVideoEvent(video));
                        //Excluir ARQUIVO DA PASTA DO SISTEMA
                    }
                    @Override
                    protected void actionFailure(){

                    }
                })
        );
        
        registerAction(this.view.getBtnChooseVideo(), new AbstractAction() {
            @Override
            protected void action() {
                chooseVideo();
            }
            @Override
            protected void posAction() {
                showVideo();
            }
        });
        
        registerAction(this.view.getBtnPlayVideo(), new AbstractAction() {
            @Override
            protected void action() {
                playAction();
            }
        });
        
        view.getTable().setMouseEvent(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent t) {
                if (t.getClickCount() == 2) {
                    view.resetForm();
                    Video video = (Video)view.getTable().getEntitySelected();
                    if (video != null) {
                        view.loadVideoToEdit(video);
                        videoFile = new File(view.getTfVideoPath().getText());
                        showVideo();
                    }
                }
            }
        });
        
        StackPane.setAlignment(view, Pos.CENTER);
        this.view.resetForm();
        refreshTable();
    }
    
    private void chooseVideo() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecione um vídeo");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("VÍDEO", "*.3gp", "*.mp4", "*.webm", "*.mkv"));
        videoFile = fileChooser.showOpenDialog(null);
    }
    
    private void showVideo() {
        if(videoFile != null) {
            //view.getTfName().setText(videoFile.getName());
            view.getTfVideoName().setText(videoFile.getName());
            view.getTfVideoPath().setText(videoFile.getAbsolutePath());

            Media mediaVideo = new Media(videoFile.toURI().toString());
            mediaPlayer = new MediaPlayer(mediaVideo);
            view.getMediaView().setMediaPlayer(mediaPlayer);
            view.getBtnPlayVideo().setVisible(true);
        }
    }
    
    private void playAction() {
        if(mediaStarted == false)
            videoPlay();
        else
            videoStop();
    }
    
    void videoPlay() {
        if(mediaPlayer == null)
            return;
        mediaPlayer.play();
        mediaStarted = true;
        this.view.setBtToStop();

        mediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                videoStop();
            }
        });
    }
    
    void videoStop() {
        if(mediaPlayer == null)
            return;
        
        mediaStarted = false;
        this.view.setBtToPlay();
        mediaPlayer.stop();
    }
    
    public void showView() {
        view.resetForm();
        refreshTable();
        parentPane.getChildren().add(view);
    }
    
    void closeView() {
        parentPane.getChildren().remove(view);
    }
    
    @Override
    protected void cleanUp() {
        view.resetForm();
        this.view.getBtnPlayVideo().setVisible(false);
        if(mediaPlayer != null)
            mediaPlayer.dispose();
        
        closeView();
        super.cleanUp();
    }

    private ObservableList<VideoType> getVideoTypeList() {
        VideoTypeDAO videoTypeDAO = new VideoTypeDAOJPA(this.getPersistenceContext());
        return FXCollections.observableArrayList(videoTypeDAO.getAll());
    }
    
    private void refreshTable() {
        refreshTable(null);
    }
    
    private void refreshTable(List<Video> list) {
        //view.addTransition();
        if (list != null) {
            view.refreshTable(list);
            return;
        }
        
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                VideoDAO dao = new VideoDAOJPA(getPersistenceContext());
                view.refreshTable(dao.getAll());
            }
        });
    }
    
    /**
     * 
     * @param video
     */
    private void copyFileToDisk(Video video) {
        FileToCopy fcpy = new FileToCopy(FileToCopy.TYPE_VIDEO, video.getVideoName(), video.getVideoPath());
        fireEvent(new CopyFileEvent(fcpy));
    }
}
