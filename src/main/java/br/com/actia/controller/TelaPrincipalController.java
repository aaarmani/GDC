
package br.com.actia.controller;

import br.com.actia.ui.TelaPrincipalView;
import br.com.actia.util.JPAUtil;
import javafx.stage.Stage;

/**
 *
 * @author Actia
 */
public class TelaPrincipalController extends PersistenceController {
    private TelaPrincipalView view;

    public TelaPrincipalController(final Stage mainStage) {
        loadPersistenceContext();
        this.view = new TelaPrincipalView(mainStage);
    }
    
    @Override
    public void cleanUp() {
        super.cleanUp();
        JPAUtil.closeEntityManagerFactory();
    }
}
