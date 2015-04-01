
package br.com.actia.controller;

import br.com.actia.ui.MainScreenlView;
import br.com.actia.util.JPAUtil;
import javafx.stage.Stage;

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public class MainScreenController extends PersistenceController {
    private MainScreenlView view;
    private GoogleMapController googleMapCtrl;
    
    //private static ResourceBundle resBundle = ResourceBundle.getBundle("resources/languages/messages");
    
    public MainScreenController(final Stage mainStage) {
        loadPersistenceContext();
        this.view = new MainScreenlView(mainStage);
        googleMapCtrl = new GoogleMapController(this, this.view.getBpCenter());
    }
    
    @Override
    public void cleanUp() {
        super.cleanUp();
        JPAUtil.closeEntityManagerFactory();
    }
}
