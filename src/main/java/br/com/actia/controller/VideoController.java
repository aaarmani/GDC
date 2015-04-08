package br.com.actia.controller;

import br.com.actia.action.AbstractAction;
import br.com.actia.action.BooleanExpression;
import br.com.actia.action.ConditionalAction;
import br.com.actia.action.TransactionalAction;
import br.com.actia.dao.VideoDAO;
import br.com.actia.dao.VideoDAOJPA;
import br.com.actia.dao.VideoTypeDAO;
import br.com.actia.dao.VideoTypeDAOJPA;
import br.com.actia.model.Video;
import br.com.actia.model.VideoType;
import br.com.actia.ui.VideoView;
import br.com.actia.validation.Validator;
import br.com.actia.validation.VideoValidator;
import java.io.File;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
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
    
    private File videoFile;
    
    private MediaPlayer mediaPlayer = null;
    private Boolean mediaStarted = false;
    
    private ResourceBundle rb;

    public VideoController(AbstractController parent, Pane pane, ResourceBundle rb) {
        super(parent);
        loadPersistenceContext(((PersistenceController) getParentController()).getPersistenceContext());
        this.rb = rb;
        
        this.parentPane = pane;
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
                        String msg = validador.validate(video);
                        if (!"".equals(msg == null ? "" : msg)) {
                            // Dialog.showInfo("Validacão", msg, );
                             System.out.println(msg);
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
                                cleanUp();
                                //MOVER ARQUIVO PARA PASTA DO SISTEMA
                            }
                        })
                )
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
        
        parentPane.getChildren().add(view);
        StackPane.setAlignment(view, Pos.CENTER);
        this.view.resetForm();
        showView();
    }
    
    private void chooseVideo() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecione um vídeo");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("*.3gp", "*.mp4", "*.ts"));        
        videoFile = fileChooser.showOpenDialog(null);
    }
    
    private void showVideo() {
        if(videoFile != null) {
            view.getTfVideoPath().setText(videoFile.getName());
            
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
        this.view.setVisible(true);
    }
    
    private void closeView() {
        this.view.setVisible(false);
        this.cleanUp();
    }
    
    @Override
    protected void cleanUp() {
        view.resetForm();
        this.view.getBtnPlayVideo().setVisible(false);
        if(mediaPlayer != null)
            mediaPlayer.dispose();
        
        super.cleanUp(); //To change body of generated methods, choose Tools | Templates.
    }

    private ObservableList<VideoType> getVideoTypeList() {
        VideoTypeDAO videoTypeDAO = new VideoTypeDAOJPA(this.getPersistenceContext());
        return FXCollections.observableArrayList(videoTypeDAO.getAll());
    }
}
