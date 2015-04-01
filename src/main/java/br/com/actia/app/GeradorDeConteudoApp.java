package br.com.actia.app;

import br.com.actia.controller.MainScreenController;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Início da Aplicação de geração de conteúdo da Actia do Brasil
 * 
 * @author Armani <anderson.armani@actia.com.br>
 */
public class GeradorDeConteudoApp extends Application {
    private MainScreenController controller;
    
    @Override
    public void start(Stage stage){
        Locale.setDefault(new Locale("pt","BR"));
        controller = new MainScreenController(stage);
    }

    @Override
    public void stop() throws Exception {
        controller.cleanUp();
    }
    
    public static void main(String[] args) {
        setProxy();
        GeradorDeConteudoApp.launch(args);
    }
    
    private static void setProxy() {
        try {
            ProxySelector.getDefault().select(new URI("http://google.com"));
        } catch (URISyntaxException ex) {
            Logger.getLogger(GeradorDeConteudoApp.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.setProperty("http.proxyHost", "10.0.0.202");
        System.setProperty("http.proxyPort", "3128");
        System.setProperty("http.proxyUser", "armani");
        System.setProperty("http.proxyPassword", "aa4873");
        System.setProperty("java.net.useSystemProxies", "true");
    }
}
