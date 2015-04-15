
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
    private MainScreenlView view;
    private GoogleMapController googleMapController;
    private BannerController bannerController;
    private VideoController videoController;
    private ResourceBundle rb;
    private ListPoiController listPoiController;
    private ListVideoController listVideoController;
        
    public MainScreenController(final Stage mainStage, ResourceBundle rb) {
        loadPersistenceContext();
        this.rb = rb;
        this.view = new MainScreenlView(mainStage, rb);
        
        googleMapController = new GoogleMapController(this, this.view.getPaneCenter(), this.rb);
        bannerController = new BannerController(this, this.view.getPaneCenter(), this.rb);
        videoController = new VideoController(this, this.view.getPaneCenter(), this.rb);
        listPoiController = new ListPoiController(this, this.view.getPaneCenter(), this.rb);
        listVideoController = new ListVideoController(this, this.view.getPaneCenter(), this.rb);
        
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
        
        registerAction(this.view.getBtnListPoi(), new AbstractAction() {
            @Override
            protected void action() {
                showListPoiController();
            }
        });
        
        registerAction(this.view.getBtnListVideo(), new AbstractAction() {
            @Override
            protected void action() {
                showListVideoController();
            }
        });
        
        registerAction(this.view.getBtnBR(), new AbstractAction() {
            @Override
            protected void action() {
                System.out.println("SET LOCALE PT");
                setLanguage(new Locale("pt","BR"));
            }
        });
        
        registerAction(this.view.getBtnEN(), new AbstractAction() {
            @Override
            protected void action() {
                System.out.println("SET LOCALE UN");
                setLanguage(new Locale("en",""));
            }
        });
        
        registerAction(this.view.getBtnES(), new AbstractAction() {
            @Override
            protected void action() {
                System.out.println("SET LOCALE ES");
                setLanguage(new Locale("es",""));
            }
        });
    }

    private void showGoogleMapController() {
        cleanUpOldControllers();
        googleMapController.showView();
    }
    
    private void showBannerController() {
        cleanUpOldControllers();
        bannerController.showView();
    }
    
    private void showVideoController() {
        cleanUpOldControllers();
        videoController.showView();
    }
    
    private void showListPoiController() {
        cleanUpOldControllers();
        listPoiController.showView();
    }
    
    private void showListVideoController() {
        cleanUpOldControllers();
        listVideoController.showView();
    }
    
    private void cleanUpOldControllers() {
        this.view.getPaneCenter().getChildren().removeAll();
        
        if(googleMapController != null) {
            googleMapController.closeView();
            googleMapController.cleanUp();
        }
        
        if(bannerController != null) {
            bannerController.closeView();
            bannerController.cleanUp();
        }
            
        if(videoController != null) {
            videoController.closeView();
            videoController.cleanUp();
        }
        
        if(listPoiController != null) {
            listPoiController.closeView();
            listPoiController.cleanUp();
        }
        
        if(listVideoController != null) {
            listVideoController.closeView();
            listVideoController.cleanUp();
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
