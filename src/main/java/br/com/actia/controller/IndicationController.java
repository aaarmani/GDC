package br.com.actia.controller;

import br.com.actia.action.AbstractAction;
import br.com.actia.action.BooleanExpression;
import br.com.actia.action.ConditionalAction;
import br.com.actia.action.TransactionalAction;
import br.com.actia.dao.IndicationDAO;
import br.com.actia.dao.IndicationDAOJPA;
import br.com.actia.event.CopyFileEvent;
import br.com.actia.event.CrudIndicationEvent;
import br.com.actia.model.Indication;
import br.com.actia.ui.Dialog;
import service.FileToCopy;
import br.com.actia.ui.IndicationView;
import br.com.actia.ui.MainScreenView;
import br.com.actia.validation.IndicationValidator;
import br.com.actia.validation.Validator;
import java.io.File;
import java.util.ResourceBundle;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;

import java.util.List;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class IndicationController extends PersistenceController {
    private IndicationView view;
    private final Validator<Indication> validador = new IndicationValidator();
    private final Pane parentPane;
    private MainScreenView mainScreenView;
    
    private File imageFile;
    private File audioFile;
    
    private Media mediaFile = null;
    private MediaPlayer mediaPlayer = null;
    private Boolean mediaStarted = false;
    
    private ResourceBundle rb;
    
    public IndicationController(AbstractController parent, MainScreenView mainScreenView, ResourceBundle rb) {
        super(parent);
        loadPersistenceContext(((PersistenceController) getParentController()).getPersistenceContext());
        this.rb = rb;
        this.mainScreenView = mainScreenView;
        this.parentPane = mainScreenView.getPaneCenter();
        this.view = new IndicationView(rb);
        this.view.setMaxHeight(parentPane.getHeight());
        this.view.setMaxWidth(parentPane.getWidth());
        this.view.setMinHeight(parentPane.getHeight());
        this.view.setMinWidth(parentPane.getWidth());
        this.view.getBtnPlay().setVisible(false);
        
        registerAction(this.view.getBtnCancelIndication(), new AbstractAction() {
            @Override
            protected void action() {
                closeView();
            }
        });
        
        registerAction(this.view.getBtnSaveIndication(),
            ConditionalAction.build()
                .addConditional(new BooleanExpression() {
                    @Override
                    public boolean conditional() {
                        Indication indication = view.loadIndicationFromPanel();
                        String msg = validador.validate(indication, rb);
                        if (!"".equals(msg == null ? "" : msg)) {
                            Dialog.showError(rb.getString("VALIDATION"), msg);
                            return false;
                        }

                        return true;
                    }
                })
                .addAction(TransactionalAction.build()
                            .persistenceCtxOwner(IndicationController.this)
                            .addAction(new AbstractAction() {
                                private Indication indication;

                                @Override
                                protected void action() {
                                    indication = view.loadIndicationFromPanel();
                                    IndicationDAO indicationDao = new IndicationDAOJPA(getPersistenceContext());
                                    indication = indicationDao.save(indication);
                                }

                                @Override
                                protected void posAction() {
                                    indication = view.loadIndicationFromPanel();
                                    fireEvent(new CrudIndicationEvent(indication));
                                    if(indication.getImagePath() != null || indication.getAudioPath() != null){
                                        copyFileToDisk(indication);
                                    }
                                    view.resetForm();
                                    refreshTable();
                                }
                            }))
        );
        
        registerAction(this.view.getBtnDeleteIndication(),
            TransactionalAction.build()
                .persistenceCtxOwner(IndicationController.this)
                .addAction(new AbstractAction() {
                    private Indication indication;

                    @Override
                    protected void action() {
                        Integer id = view.getIndicationId();
                        if (id != null) {
                            IndicationDAO indicationDao = new IndicationDAOJPA(getPersistenceContext());
                            indication = indicationDao.findById(id);
                            if (indication != null) {
                                indicationDao.remove(indication);
                            }
                        }
                    }
                    @Override
                    protected void posAction() {
                        view.resetForm();
                        refreshTable();
                        fireEvent(new CrudIndicationEvent(indication));
                    }
                    @Override
                    protected void actionFailure(){

                    }
                })
        );
        
        registerAction(this.view.getBtnChooseImage(), new AbstractAction() {
            @Override
            protected void action() {
                chooseImage();
            }
            
            @Override
            protected void posAction() {
                showImage();
            }
        });
        
        registerAction(this.view.getBtnChooseAudio(), new AbstractAction() {
            @Override
            protected void action() {
                chooseAudio();
            }
            
            @Override
            protected void posAction() {
                loadAudio();
            }
        });
        
        registerAction(view.getBtnPlay(), new AbstractAction() {
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
                    Indication indication = (Indication)view.getTable().getEntitySelected();
                    if (indication != null) {
                        view.loadIndicationToEdit(indication);
                        imageFile = new File(view.getTfImgPath().getText());
                        showImage();
                        audioFile = new File(view.getTfAudioPath().getText());
                        loadAudio();
                    }
                    
                }
            }
        });
        
        StackPane.setAlignment(view, Pos.CENTER);
        this.view.resetForm();
        this.refreshTable();
    }
    
    public void showView() {
        parentPane.getChildren().add(view);
    }
        
    public void closeView() {
        parentPane.getChildren().remove(view);
    }
    
    private void chooseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecione uma imagem");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("IMAGEM", "*.jpg", "*.jpeg", "*.bmp", "*.bmp", "*.gif", "*.png", "*.webp"));
        imageFile = fileChooser.showOpenDialog(null);
    }
    
    private void showImage() {
        if(imageFile != null) {
            view.getTfImgName().setText(imageFile.getName());
            view.getTfImgPath().setText(imageFile.getAbsolutePath());
            
            view.getIvImageView().setImage(new Image(imageFile.toURI().toString()));
        }
    }
    
    private void chooseAudio() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecione um áudio");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("ÁUDIO", "*.3gp", "*.mp4",
                "*.m4a", "*.aac", "*.ts", "*.flac", "*.mp3", "*.mid", "*.xmf", "*.mxmf", "*.rtttl", "*.rtx",
                "*.ota", "*.imy", "*.ogg", "*.mkv", "*.wav"));
        audioFile = fileChooser.showOpenDialog(null);
    }
    
    private void loadAudio() {
        if(audioFile != null) {
            view.getTfAudioName().setText(audioFile.getName());
            view.getTfAudioPath().setText(audioFile.getAbsolutePath());
            view.getBtnPlay().setVisible(true);
        }
    }
    
    private void playAction() {
        if(mediaStarted == false)
            audioPlay();
        else
            audioStop();
    }
    
    void audioPlay() {
        if(audioFile == null)
            return;
        
        mediaFile = new Media(audioFile.toURI().toString());
        
        if(mediaFile != null) {
            mediaPlayer = new MediaPlayer(mediaFile);
            mediaPlayer.play();
            mediaStarted = true;
            this.view.setBtToStop();
            
            mediaPlayer.setOnEndOfMedia(new Runnable() {
                @Override
                public void run() {
                    audioStop();
                }
            });
        }
    }
    
    void audioStop() {
        if(mediaPlayer == null)
            return;
        
        mediaStarted = false;
        this.view.setBtToPlay();
        mediaPlayer.stop();
    }

    @Override
    protected void cleanUp() {
        view.resetForm();
        this.view.getBtnPlay().setVisible(false);
        if(mediaPlayer != null)
            mediaPlayer.dispose();

        closeView();
        super.cleanUp(); //To change body of generated methods, choose Tools | Templates.
    }
    
    private void refreshTable() {
        refreshTable(null);
    }
    
    private void refreshTable(List<Indication> list) {
        //view.addTransition();
        if (list != null) {
            view.refreshTable(list);
            return;
        }
        
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                IndicationDAO dao = new IndicationDAOJPA(getPersistenceContext());
                view.refreshTable(dao.getAll());
            }
        });
    }
    
    /**
     * 
     * @param indication
     */
    private void copyFileToDisk(Indication indication) {
        FileToCopy fcpy = null;
        
        if(indication.getImage() != null && !indication.getImage().isEmpty() && indication.getImagePath() != null && !indication.getImagePath().isEmpty()) {
            fcpy = new FileToCopy(FileToCopy.TYPE_IMAGE_INDICATION, indication.getImage(), indication.getImagePath());
            fireEvent(new CopyFileEvent(fcpy));
        }
        
        if(indication.getAudio() != null && !indication.getAudio().isEmpty() && indication.getAudioPath() != null && !indication.getAudioPath().isEmpty()) {
            fcpy = new FileToCopy(FileToCopy.TYPE_AUDIO, indication.getAudio(), indication.getAudioPath());
            fireEvent(new CopyFileEvent(fcpy));
        }
    }
    
}
