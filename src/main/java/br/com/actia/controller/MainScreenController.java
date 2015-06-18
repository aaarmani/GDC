package br.com.actia.controller;

import br.com.actia.action.AbstractAction;
import br.com.actia.event.AbstractEventListener;
import br.com.actia.event.CopyFileEvent;
import service.FileToCopy;
import br.com.actia.ui.MainScreenView;
import br.com.actia.util.JPAUtil;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.animation.ScaleTransition;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public class MainScreenController extends PersistenceController {
    private final MainScreenView view;
    private RouteController routeController;
    private GoogleMapController googleMapController;
    private BannerController bannerController;
    private VideoController videoController;
    private RSSController RSSController;
    private final ResourceBundle rb;
    private ListPoiController listPoiController;
    private ListBannerController listBannerController;
    private ListVideoController listVideoController;
    private ListRSSController listRSSController;
    private ActionScreenController actionController;
    private UserOptionsController userOptionsController;
    
    public MainScreenController(final Stage mainStage, ResourceBundle rb) {
        loadPersistenceContext();
        this.rb = rb;
        this.view = new MainScreenView(mainStage, rb);
        actionController = new ActionScreenController(this, this.view.getBtnDowloadService(), this.rb);
        
        registerAction(this.view.getBtnUserOptions(), new AbstractAction() {
            @Override
            protected void action() {
                showUserOptionController();
            }
        });
        
        registerAction(this.view.getBtnDowloadService(), new AbstractAction() {
            @Override
            protected void action() {
                showActionScreenController();
            }
        });
        
        registerAction(this.view.getBtnRoute(), new AbstractAction() {
            @Override
            protected void action() {
                showRouteController();
            }
        });   
        
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
        
        registerEventListener(CopyFileEvent.class, new AbstractEventListener<CopyFileEvent>() {
            @Override
            public void handleEvent(CopyFileEvent event) {
                FileToCopy ftcpy = event.getTarget();
                actionController.startDownload(ftcpy.getOrigFile(), ftcpy.getDestFile());
                startDownloadAnimation();
            }
        });
    }

    private void showRouteController() {
        cleanUpOldControllers();
        if(routeController == null)
            routeController = new RouteController(this, this.view, this.rb);
        routeController.showView();
    }
    
    private void showGoogleMapController() {
        cleanUpOldControllers();
        if(googleMapController == null)
            googleMapController = new GoogleMapController(this, this.view, this.rb);
        googleMapController.showView();
    }
    
    private void showBannerController() {
        cleanUpOldControllers();
        if(bannerController == null)
            bannerController = new BannerController(this, this.view, this.rb);
        bannerController.showView();
    }
    
    private void showVideoController() {
        cleanUpOldControllers();
        if(videoController == null)
            videoController = new VideoController(this, this.view, this.rb);
        videoController.showView();
    }
    
    private void showRSSController() {
        cleanUpOldControllers();
        if(RSSController == null)
            RSSController = new RSSController(this, this.view, this.rb);
        RSSController.showView();
    }
    
    private void showListPoiController() {
        cleanUpOldControllers();
        if(listPoiController == null)
            listPoiController = new ListPoiController(this, this.view, this.rb);
        listPoiController.showView();
    }
    
    private void showListBannerController() {
        cleanUpOldControllers();
        if(listBannerController == null)
            listBannerController = new ListBannerController(this, this.view, this.rb);
        listBannerController.showView();
    }
    
    private void showListVideoController() {
        cleanUpOldControllers();
        if(listVideoController == null)
            listVideoController = new ListVideoController(this, this.view, this.rb);
        listVideoController.showView();
    }
    
    private void showListRSSController() {
        cleanUpOldControllers();
        if(listRSSController == null)
            listRSSController = new ListRSSController(this, this.view, this.rb);
        listRSSController.showView();
    }
    
    private void showActionScreenController() {
        
        //if(actionController == null)
            //actionController = new ActionScreenController(this, this.view.getPaneCenter(), this.rb);
        actionController.showView();
    }
    
    private void showUserOptionController() {
        if(userOptionsController == null)
            userOptionsController = new UserOptionsController(this, this.view.getBtnUserOptions(), this.rb);
        userOptionsController.showView();
    }
    
    private void cleanUpOldControllers() {
        if(routeController != null) {
            routeController.cleanUp();
        }

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
        
        if(actionController != null) {
            actionController.cleanUp();
        }
        
        if(userOptionsController != null) {
            userOptionsController.cleanUp();
        }
    }
    
    private void setLanguage(Locale locale) {
        Locale.setDefault(locale);
    }
    
    private void startDownloadAnimation() {
        ScaleTransition transition = new ScaleTransition(Duration.millis(400), view.getBtnDowloadService());
        transition.setFromX(1.5);
        transition.setFromY(1.5);
        transition.setToX(1.0);
        transition.setToY(1.0);
        transition.play();
    }
    
    @Override
    public void cleanUp() {
        JPAUtil.closeEntityManagerFactory();
        super.cleanUp();
    }
}