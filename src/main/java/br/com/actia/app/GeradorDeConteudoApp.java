package br.com.actia.app;

import br.com.actia.controller.MainScreenController;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Pré carregamento do listSelectionView
 */
import org.controlsfx.control.ListSelectionView;

/**
 * Início da Aplicação de geração de conteúdo da Actia do Brasil
 * 
 * @author Armani <anderson.armani@actia.com.br>
 */
public class GeradorDeConteudoApp extends Application {
    private MainScreenController controller;
    private ResourceBundle rb;
    
    @Override
    public void start(Stage stage){
        try {
            Locale.setDefault(Locale.ENGLISH);
            rb = ResourceBundle.getBundle("languages.messages");
            System.out.println(" BUNDLE ret ==  " + rb.getString("MainFrameTitle"));
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        controller = new MainScreenController(stage, rb);
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
