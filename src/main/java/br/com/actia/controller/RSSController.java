package br.com.actia.controller;

import br.com.actia.action.AbstractAction;
import br.com.actia.action.BooleanExpression;
import br.com.actia.action.ConditionalAction;
import br.com.actia.action.TransactionalAction;
import br.com.actia.dao.RSSDAO;
import br.com.actia.dao.RSSDAOJPA;
import br.com.actia.event.CrudRSSEvent;
import br.com.actia.model.RSS;
import br.com.actia.ui.MainScreenView;
import br.com.actia.ui.RSSView;
import br.com.actia.validation.RSSValidator;
import br.com.actia.validation.Validator;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.util.List;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndImage;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Iterator;

import java.util.Timer;
import java.util.TimerTask;
import javafx.scene.image.Image;

public class RSSController extends PersistenceController {
    private RSSView view;
    private final Validator<RSS> validador = new RSSValidator();
    private final Pane parentPane;
    private MainScreenView mainScreenView;
    private String feedURL;
    private Boolean feedStarted = false;
    private ResourceBundle rb;
    private ArrayList<SyndEntry> lstRSS = null;
    private Image feedImg = null;
    private Timer timerPlayFeed = null;
    
    public RSSController(AbstractController parent, MainScreenView mainScreenView, ResourceBundle rb) {
        super(parent);
        loadPersistenceContext(((PersistenceController) getParentController()).getPersistenceContext());
        this.rb = rb;
        this.mainScreenView = mainScreenView;
        this.parentPane = mainScreenView.getPaneCenter();
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
        
        registerAction(this.view.getBtnPreviewFeed(), new AbstractAction() {
            @Override
            protected void action() {
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
    
    private void loadFeed() {
        feedURL = this.view.getTfFeedPath().getText();
        
        if(feedURL != null && feedURL != "") {
            view.getTfFeedPath().setText(feedURL.trim());
            
            if(this.timerPlayFeed != null) {
                this.timerPlayFeed.cancel();
            }
            
            this.view.getFeedView().setVisible(false);
            this.feedStarted = false;
            this.view.setBtToPlay();
            this.view.getBtnPlay().setVisible(true);
            
            URL url;
            this.lstRSS = new ArrayList<SyndEntry>();

            // Image rssImageDrawable = null;

            SyndImage syndImage = null;
            
            try {
                url = new URL(feedURL);
                SyndFeedInput input = new SyndFeedInput();

                SyndFeed feed = input.build(new XmlReader(url));
                List inputs = feed.getEntries();
                Iterator itInputs = inputs.iterator();

                try {
                    System.out.println("####CARREGANDO IMAGEM DO RSS###");
                    syndImage = feed.getImage();
                    String link = syndImage.getUrl();

                    System.out.println("#### RSS LINK IMAGE = " + link);
                    if (link != null && !link.isEmpty()) {
                        // InputStream is = (InputStream) new URL(link).getContent();
                        InputStream is = (InputStream)  new URL(link).openStream();//getContent();//feed.getImage();
                        this.feedImg = new Image(is);

                        // rssImageDrawable = Drawable.createFromStream(is, "src name");
                    }
                } catch(Exception rssE) {
                    rssE.printStackTrace();
                }

                while(itInputs.hasNext()) {
                    SyndEntry aux = (SyndEntry) itInputs.next();
                    this.lstRSS.add(aux);
                }

                System.out.println("QUANTIDADE DE RSS = " + this.lstRSS.size());
            } catch (MalformedURLException e) {
               e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (FeedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void playAction() {
        if(feedStarted == false)
            feedPlay();
        else
            feedStop();
    }
    
    private void feedPlay() {
        if(feedURL == null || feedURL == "")
            return;
        
        this.timerPlayFeed = new Timer();
        timerPlayFeed.scheduleAtFixedRate(new TimerTask() {
            int rssIndex = 0;
            
            @Override
            public void run() {
                Platform.runLater(() -> {
                    // System.out.println("----- RODANDO THREAD -----");
                    
                    if(lstRSS != null && lstRSS.size() > 0) {
                    //while(true) {
                        final SyndEntry entry = lstRSS.get(rssIndex++);
                        rssIndex = rssIndex >= lstRSS.size() ? 0 : rssIndex;
                        
                        try {
                            String aux = entry.getTitle();

                            view.getFeedTitleText().setText(getTextFromHtml(aux));
                            aux = entry.getDescription().getValue();
                            view.getFeedContentText().setText(getTextFromHtml(aux));
                            view.getFeedDateText().setText("");

                            //rssImage.setImageDrawable(rssImageDrawable);
                        } catch(Exception e) {
                            e.printStackTrace();
                        }

                        view.getFeedImageView().setImage(feedImg);
                        view.getFeedImageView().setVisible(true);

                        //imageView.setVisibility(View.GONE);
                        //rssView.setVisibility(View.VISIBLE);
                   }
                });
            }
        }, 0, 10000);
        
        this.view.getFeedView().setVisible(true);
        feedStarted = true;
        this.view.setBtToStop();
    }
    
    private void feedStop() {
        if(feedURL == null || feedURL == "") 
            return;
        
        this.timerPlayFeed.cancel();
        this.view.getFeedView().setVisible(false);
        feedStarted = false;
        this.view.setBtToPlay();
    }

    @Override
    protected void cleanUp() {
        view.resetForm();
        this.view.getBtnPlay().setVisible(false);
        this.view.getFeedView().setVisible(false);
        
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
    
    /**
     * Remove all Html tags from text
     * @param html with tags
     * @return String withou html tags
     */
    private String getTextFromHtml(String html) {
        String text = "";

        if(html == null || html.isEmpty()) {
            return text;
        }
        html = html.trim();
        html = html.replaceAll("<(.*?)\\>"," ");//Removes all items in brackets
        html = html.replaceAll("<(.*?)\\\n"," ");//Must be undeneath
        html = html.replaceFirst("(.*?)\\>", " ");//Removes any connected item to the last bracket
        html = html.replaceAll("&nbsp;"," ");
        html = html.replaceAll("&amp;"," ");
        text = html.trim();

        return text;
    }
}