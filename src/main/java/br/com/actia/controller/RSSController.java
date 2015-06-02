package br.com.actia.controller;

import br.com.actia.action.AbstractAction;
import br.com.actia.action.BooleanExpression;
import br.com.actia.action.ConditionalAction;
import br.com.actia.action.TransactionalAction;
import br.com.actia.dao.RSSDAO;
import br.com.actia.dao.RSSDAOJPA;
import br.com.actia.event.CrudRSSEvent;
import br.com.actia.model.RSS;
import br.com.actia.ui.RSSView;
import br.com.actia.validation.RSSValidator;
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

public class RSSController extends PersistenceController {
    private RSSView view;
    private final Validator<RSS> validador = new RSSValidator();
    private final Pane parentPane;
    
    private File feedFile;
    
    // private Media mediaFile = null;
    // private MediaPlayer mediaPlayer = null;
    private Boolean feedStarted = false;
    
    private ResourceBundle rb;
    
    public RSSController(AbstractController parent, Pane pane, ResourceBundle rb) {
        super(parent);
        loadPersistenceContext(((PersistenceController) getParentController()).getPersistenceContext());
        this.rb = rb;
        
        this.parentPane = pane;
        this.view = new RSSView(rb);
        this.view.setMaxHeight(parentPane.getHeight());
        this.view.setMaxWidth(parentPane.getWidth());
        this.view.setMinHeight(parentPane.getHeight());
        this.view.setMinWidth(parentPane.getWidth());
        this.view.getBtnPlay().setVisible(false);
        
        registerAction(this.view.getBtnCancelRSS(), new AbstractAction() {
            @Override
            protected void action() {
                closeView();
            }
        });
        
        registerAction(this.view.getBtnSaveRSS(),
            ConditionalAction.build()
                .addConditional(new BooleanExpression() {
                    @Override
                    public boolean conditional() {
                        RSS RSS = view.loadRSSFromPanel();
                        String msg = validador.validate(RSS);
                        if (!"".equals(msg == null ? "" : msg)) {
                            // Dialog.showInfo("Validacão", msg, );
                             System.out.println(msg);
                             return false;
                        }

                        return true;
                    }
                })
                .addAction(TransactionalAction.build()
                            .persistenceCtxOwner(RSSController.this)
                            .addAction(new AbstractAction() {
                                private RSS RSS;

                                @Override
                                protected void action() {
                                    RSS = view.loadRSSFromPanel();
                                    RSSDAO RSSDao = new RSSDAOJPA(getPersistenceContext());
                                    RSS = RSSDao.save(RSS);
                                }

                                @Override
                                protected void posAction() {
                                    view.resetForm();
                                    refreshTable();
                                    //cleanUp();
                                    //MOVER ARQUIVO PARA PASTA DO SISTEMA
                                    fireEvent(new CrudRSSEvent(RSS));
                                    
                                    if(parent instanceof ListRSSController){
                                        closeView();
                                    }
                                }
                            }))
        );
        
        registerAction(this.view.getBtnDeleteRSS(),
            TransactionalAction.build()
                .persistenceCtxOwner(RSSController.this)
                .addAction(new AbstractAction() {
                    private RSS RSS;

                    @Override
                    protected void action() {
                        Integer id = view.getRSSId();
                        if (id != null) {
                            RSSDAO RSSDao = new RSSDAOJPA(getPersistenceContext());
                            RSS = RSSDao.findById(id);
                            if (RSS != null) {
                                RSSDao.remove(RSS);
                            }
                        }
                    }
                    @Override
                    protected void posAction() {
                        view.resetForm();
                        refreshTable();
                        fireEvent(new CrudRSSEvent(RSS));
                    }
                    @Override
                    protected void actionFailure(){

                    }
                })
        );
        
        registerAction(this.view.getBtnChooseFeed(), new AbstractAction() {
            @Override
            protected void action() {
                chooseXML();
            }
            
            @Override
            protected void posAction() {
                loadFeed();
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
                    RSS RSS = (RSS)view.getTable().getEntitySelected();
                    if (RSS != null) {
                        view.loadRSSToEdit(RSS);
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
    
    private void chooseXML() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecione um arquivo de feed RSS");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("RSS", "*.xml", "*.rss", "*.opml"));        
        feedFile = fileChooser.showOpenDialog(null);
    }
    
    private void loadFeed() {
        if(feedFile != null) {
            view.getTfFeedPath().setText(feedFile.getName());
            view.getBtnPlay().setVisible(true);
        }
    }
    
    private void playAction() {
        if(feedStarted == false)
            feedPlay();
        else
            feedStop();
    }
    
    void feedPlay() {
        if(feedFile == null)
            return;
        
        /*
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
        */
    }
    
    void feedStop() {
        /*
        if(mediaPlayer == null)
            return;
        
        mediaStarted = false;
        this.view.setBtToPlay();
        mediaPlayer.stop();
        */
    }

    @Override
    protected void cleanUp() {
        view.resetForm();
        this.view.getBtnPlay().setVisible(false);
        
        /*
        if(mediaPlayer != null)
            mediaPlayer.dispose();
        */
        
        closeView();
        super.cleanUp(); //To change body of generated methods, choose Tools | Templates.
    }
    
    private void refreshTable() {
        refreshTable(null);
    }
    
    private void refreshTable(List<RSS> list) {
        //view.addTransition();
        if (list != null) {
            view.refreshTable(list);
            return;
        }
        
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                RSSDAO dao = new RSSDAOJPA(getPersistenceContext());
                view.refreshTable(dao.getAll());
            }
        });
    }
    
}
