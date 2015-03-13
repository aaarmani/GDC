package br.com.actia.app;

import br.com.actia.controller.ListaMercadoriaController;
import br.com.actia.controller.TelaPrincipalController;
import java.util.Locale;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Ponto de entrada da aplicacão.
 * 
 * @author YaW Tecnologia
 */
public class MercadoriaApp extends Application {
    
    //private ListaMercadoriaController controller;
    private TelaPrincipalController controller;
    
    @Override
    public void start(Stage stage){
        Locale.setDefault(new Locale("pt","BR"));
        //controller = new ListaMercadoriaController(stage);
        controller = new TelaPrincipalController(stage);
    }

    @Override
    public void stop() throws Exception {
        controller.cleanUp();
    }
    
    public static void main(String[] args) {
        MercadoriaApp.launch(args);
    }
    
}
