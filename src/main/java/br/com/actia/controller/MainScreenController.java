
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
    private GoogleMapController googleMapCtrl;
    private BannerController bannerController;
    private VideoController videoController;
    private ResourceBundle rb;
    private ListPoiController lpoiCtrl;
    private ListVideoController lvideoCtrl;
        
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
        googleMapCtrl = new GoogleMapController(this, this.view.getPaneCenter(), this.rb);
    }
    
    private void showBannerController() {
        cleanUpOldControllers();
        bannerController = new BannerController(this, this.view.getPaneCenter(), this.rb);
    }
    
    private void showVideoController() {
        cleanUpOldControllers();
        videoController = new VideoController(this, this.view.getPaneCenter(), this.rb);
    }
    
    private void showListPoiController() {
        cleanUpOldControllers();
        lpoiCtrl = new ListPoiController(this, this.view.getPaneCenter(), this.rb);
    }
    
    private void showListVideoController() {
        cleanUpOldControllers();
        lvideoCtrl = new ListVideoController(this, this.view.getPaneCenter(), this.rb);
    }
    
    private void cleanUpOldControllers() {
        this.view.getPaneCenter().getChildren().removeAll();
        
        if(googleMapCtrl != null) {
            googleMapCtrl.cleanUp();
            googleMapCtrl = null;
        }
        
        if(bannerController != null) {
            bannerController.cleanUp();
            bannerController = null;
        }
            
        if(videoController != null) {
            videoController.cleanUp();
            videoController = null;
        }
        
        if(lpoiCtrl != null) {
            lpoiCtrl.cleanUp();
            lpoiCtrl = null;
        }
        
        if(lvideoCtrl != null) {
            lvideoCtrl.cleanUp();
            lvideoCtrl = null;
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
