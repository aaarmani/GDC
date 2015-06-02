
package br.com.actia.controller;

import br.com.actia.action.AbstractAction;
import br.com.actia.ui.MainScreenlView;
import br.com.actia.util.JPAUtil;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.stage.Stage;

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public class MainScreenController extends PersistenceController {
    private final MainScreenlView view;
    private GoogleMapController googleMapController;
    private BannerController bannerController;
    private VideoController videoController;
    private RSSController RSSController;
    private ResourceBundle rb;
    private ListPoiController listPoiController;
    private ListBannerController listBannerController;
    private ListVideoController listVideoController;
    private ListRSSController listRSSController;
    
    public MainScreenController(final Stage mainStage, ResourceBundle rb) {
        loadPersistenceContext();
        this.rb = rb;
        this.view = new MainScreenlView(mainStage, rb);
        
        registerAction(this.view.getBtnMapEntitys(), new AbstractAction() {
            @Override
            protected void action() {
                showGoogleMapController();
            }
        });
        
        registerAction(this.view.getBtnBanner(), new AbstractAction() {
            @Override
            protected void action() {
                showBannerController();
            }
        });
        
        registerAction(this.view.getBtnVideo(), new AbstractAction() {
            @Override
            protected void action() {
                showVideoController();
            }
        });
        
        registerAction(this.view.getBtnRSS(), new AbstractAction() {
            @Override
            protected void action() {
                showRSSController();
            }
        });
        
        registerAction(this.view.getBtnListPoi(), new AbstractAction() {
            @Override
            protected void action() {
                showListPoiController();
            }
        });
        
        registerAction(this.view.getBtnListBanner(), new AbstractAction() {
           @Override
           protected void action() {
               showListBannerController();
           }
        });
        
        registerAction(this.view.getBtnListVideo(), new AbstractAction() {
            @Override
            protected void action() {
                showListVideoController();
            }
        });
        
        registerAction(this.view.getBtnListRSS(), new AbstractAction() {
            @Override
            protected void action() {
                showListRSSController();
            }
        });
        
        registerAction(this.view.getBtnBR(), new AbstractAction() {
            @Override
            protected void action() {
                setLanguage(new Locale("pt","BR"));
            }
        });
        
        registerAction(this.view.getBtnEN(), new AbstractAction() {
            @Override
            protected void action() {
                setLanguage(new Locale("en",""));
            }
        });
        
        registerAction(this.view.getBtnES(), new AbstractAction() {
            @Override
            protected void action() {
                setLanguage(new Locale("es",""));
            }
        });
    }

    private void showGoogleMapController() {
        cleanUpOldControllers();
        if(googleMapController == null)
            googleMapController = new GoogleMapController(this, this.view.getPaneCenter(), this.rb);
        googleMapController.showView();
    }
    
    private void showBannerController() {
        cleanUpOldControllers();
        if(bannerController == null)
            bannerController = new BannerController(this, this.view.getPaneCenter(), this.rb);
        bannerController.showView();
    }
    
    private void showVideoController() {
        cleanUpOldControllers();
        if(videoController == null)
            videoController = new VideoController(this, this.view.getPaneCenter(), this.rb);
        videoController.showView();
    }
    
    private void showRSSController() {
        cleanUpOldControllers();
        if(RSSController == null)
            RSSController = new RSSController(this, this.view.getPaneCenter(), this.rb);
        RSSController.showView();
    }
    
    private void showListPoiController() {
        cleanUpOldControllers();
        if(listPoiController == null)
            listPoiController = new ListPoiController(this, this.view.getPaneCenter(), this.rb);
        listPoiController.showView();
    }
    
    private void showListBannerController() {
        cleanUpOldControllers();
        if(listBannerController == null)
            listBannerController = new ListBannerController(this, this.view.getPaneCenter(), this.rb);
        listBannerController.showView();
    }
    
    private void showListVideoController() {
        cleanUpOldControllers();
        if(listVideoController == null)
            listVideoController = new ListVideoController(this, this.view.getPaneCenter(), this.rb);
        listVideoController.showView();
    }
    
    private void showListRSSController() {
        cleanUpOldControllers();
        if(listRSSController == null)
            listRSSController = new ListRSSController(this, this.view.getPaneCenter(), this.rb);
        listRSSController.showView();
    }
    
    private void cleanUpOldControllers() {

        if(googleMapController != null) {
            googleMapController.cleanUp();
        }
        
        if(bannerController != null) {
            bannerController.cleanUp();
        }
            
        if(videoController != null) {
            videoController.cleanUp();
        }
        
        if(RSSController != null) {
            RSSController.cleanUp();
        }
        
        if(listPoiController != null) {
            listPoiController.cleanUp();
        }
     
        if(listBannerController != null) {
            listBannerController.cleanUp();
        }
        
        if(listVideoController != null) {
            listVideoController.cleanUp();
        }
        
        if(listRSSController != null) {
            listRSSController.cleanUp();
        }
    }
    
    private void setLanguage(Locale locale) {
        Locale.setDefault(locale);
    }
    
    @Override
    public void cleanUp() {
        JPAUtil.closeEntityManagerFactory();
        super.cleanUp();
    }
    
}
