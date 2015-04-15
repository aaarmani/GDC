package br.com.actia.controller;

import br.com.actia.action.AbstractAction;
import br.com.actia.action.BooleanExpression;
import br.com.actia.action.ConditionalAction;
import br.com.actia.action.TransactionalAction;
import br.com.actia.dao.BannerDAO;
import br.com.actia.dao.BannerDAOJPA;
import br.com.actia.javascript.event.CrudBannerEvent;
import br.com.actia.model.Banner;
import br.com.actia.ui.BannerView;
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

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public class BannerController extends PersistenceController {
    private BannerView view;
    private final Validator<Banner> validador = new BannerValidator();
    private final Pane parentPane;
    
    private File imageFile;
    private File audioFile;
    
    private Media mediaFile = null;
    private MediaPlayer mediaPlayer = null;
    private Boolean mediaStarted = false;
    
    private ResourceBundle rb;
    
    public BannerController(AbstractController parent, Pane pane, ResourceBundle rb) {
        super(parent);
        loadPersistenceContext(((PersistenceController) getParentController()).getPersistenceContext());
        this.rb = rb;
        
        this.parentPane = pane;
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
                        String msg = validador.validate(banner);
                        if (!"".equals(msg == null ? "" : msg)) {
                            // Dialog.showInfo("Validacão", msg, );
                             System.out.println(msg);
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
                                    BannerDAO dao = new BannerDAOJPA(getPersistenceContext());
                                    banner = dao.save(banner);
                                }

                                @Override
                                protected void posAction() {
                                    cleanUp();
                                    //MOVER ARQUIVO PARA PASTA DO SISTEMA
                                    fireEvent(new CrudBannerEvent(banner));
                                }
                            }))
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
        
        StackPane.setAlignment(view, Pos.CENTER);
        this.view.resetForm();
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
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPG", "*.jpg", ".png"));        
        imageFile = fileChooser.showOpenDialog(null);
    }
    
    private void showImage() {
        if(imageFile != null) {
            view.getTfImgPath().setText(imageFile.getName());
            Image img = new Image(imageFile.toURI().toString());

            view.getIvImageView().setImage(img);
        }
    }
    
    private void chooseAudio() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecione uma imagem");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("MP3", "*.mp3", ".wmv"));        
        audioFile = fileChooser.showOpenDialog(null);
    }
    
    private void loadAudio() {
        if(audioFile != null) {
            view.getTfAudioPath().setText(audioFile.getName());
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

        super.cleanUp(); //To change body of generated methods, choose Tools | Templates.
    }
    
}
