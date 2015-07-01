package br.com.actia.controller;

import br.com.actia.action.AbstractAction;
import br.com.actia.action.BooleanExpression;
import br.com.actia.action.ConditionalAction;
import br.com.actia.action.TransactionalAction;
import br.com.actia.dao.BannerDAO;
import br.com.actia.dao.BannerDAOJPA;
import br.com.actia.event.CopyFileEvent;
import br.com.actia.event.CrudBannerEvent;
import br.com.actia.model.Banner;
import service.FileToCopy;
import br.com.actia.ui.BannerView;
import br.com.actia.ui.Dialog;
import br.com.actia.ui.MainScreenView;
import br.com.actia.validation.BannerValidator;
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

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public class BannerController extends PersistenceController {
    private BannerView view;
    private final Validator<Banner> validador = new BannerValidator();
    private final Pane parentPane;
    private MainScreenView mainScreenView;
    
    private File imageFile;
    private File audioFile;
    
    private Media mediaFile = null;
    private MediaPlayer mediaPlayer = null;
    private Boolean mediaStarted = false;
    
    private ResourceBundle rb;
    
    public BannerController(AbstractController parent, MainScreenView mainScreenView, ResourceBundle rb) {
        super(parent);
        loadPersistenceContext(((PersistenceController) getParentController()).getPersistenceContext());
        this.rb = rb;
        this.mainScreenView = mainScreenView;
        this.parentPane = mainScreenView.getPaneCenter();
        this.view = new BannerView(rb);
        this.view.setMaxHeight(parentPane.getHeight());
        this.view.setMaxWidth(parentPane.getWidth());
        this.view.setMinHeight(parentPane.getHeight());
        this.view.setMinWidth(parentPane.getWidth());
        this.view.getBtnPlay().setVisible(false);
        
        registerAction(this.view.getBtnCancelBanner(), new AbstractAction() {
            @Override
            protected void action() {
                closeView();
            }
        });
        
        registerAction(this.view.getBtnSaveBanner(),
            ConditionalAction.build()
                .addConditional(new BooleanExpression() {
                    @Override
                    public boolean conditional() {
                        Banner banner = view.loadBannerFromPanel();
                        String msg = validador.validate(banner, rb);
                        if (!"".equals(msg == null ? "" : msg)) {
                            Dialog.showError(rb.getString("VALIDATION"), msg);
                            return false;
                        }

                        return true;
                    }
                })
                .addAction(TransactionalAction.build()
                            .persistenceCtxOwner(BannerController.this)
                            .addAction(new AbstractAction() {
                                private Banner banner;

                                @Override
                                protected void action() {
                                    banner = view.loadBannerFromPanel();
                                    BannerDAO bannerDao = new BannerDAOJPA(getPersistenceContext());
                                    banner = bannerDao.save(banner);
                                }

                                @Override
                                protected void posAction() {
                                    view.resetForm();
                                    refreshTable();
                                    //cleanUp();
                                    //MOVER ARQUIVO PARA PASTA DO SISTEMA
                                    fireEvent(new CrudBannerEvent(banner));
                                    copyFileToDisk(banner);
                                    if(parent instanceof ListBannerController){
                                        closeView();
                                    }
                                }
                            }))
        );
        
        registerAction(this.view.getBtnDeleteBanner(),
            TransactionalAction.build()
                .persistenceCtxOwner(BannerController.this)
                .addAction(new AbstractAction() {
                    private Banner banner;

                    @Override
                    protected void action() {
                        Integer id = view.getBannerId();
                        if (id != null) {
                            BannerDAO bannerDao = new BannerDAOJPA(getPersistenceContext());
                            banner = bannerDao.findById(id);
                            if (banner != null) {
                                bannerDao.remove(banner);
                            }
                        }
                    }
                    @Override
                    protected void posAction() {
                        view.resetForm();
                        refreshTable();
                        fireEvent(new CrudBannerEvent(banner));
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
                    Banner banner = (Banner)view.getTable().getEntitySelected();
                    if (banner != null) {
                        view.loadBannerToEdit(banner);
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
    
    private void refreshTable(List<Banner> list) {
        //view.addTransition();
        if (list != null) {
            view.refreshTable(list);
            return;
        }
        
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                BannerDAO dao = new BannerDAOJPA(getPersistenceContext());
                view.refreshTable(dao.getAll());
            }
        });
    }
    
    /**
     * 
     * @param banner
     */
    private void copyFileToDisk(Banner banner) {
        FileToCopy fcpy = new FileToCopy(FileToCopy.TYPE_IMAGE, banner.getImage(), banner.getImagePath());
        fireEvent(new CopyFileEvent(fcpy));
        
        if(banner.getAudio() != null && !banner.getAudio().isEmpty() && !banner.getAudioPath().isEmpty()) {
            fcpy = new FileToCopy(FileToCopy.TYPE_AUDIO, banner.getAudio(), banner.getAudioPath());
            fireEvent(new CopyFileEvent(fcpy));
        }
    }
    
}
