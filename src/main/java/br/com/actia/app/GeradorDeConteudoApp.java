package br.com.actia.app;

import br.com.actia.authenticator.SimpleAuthenticator;
import br.com.actia.controller.MainScreenController;
import java.net.Authenticator;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.scene.text.Font;
import javafx.stage.Stage;

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
            Font.loadFont(GeradorDeConteudoApp.class.getResource("/fonts/fontawesome-webfont.ttf").toExternalForm(), 12);
            //Locale.setDefault(Locale.ENGLISH);
            rb = ResourceBundle.getBundle("languages.messages");
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
            
            String username = "armani";
            String password = "aa4873";
            
            System.setProperty("http.proxyHost", "10.0.0.202");
            System.setProperty("http.proxyPort", "3128");
            System.setProperty("http.proxyUser", username);
            System.setProperty("http.proxyPassword", password);
            System.setProperty("java.net.useSystemProxies", "true");
            
            Authenticator.setDefault(new SimpleAuthenticator(username, password));
            
        } catch (URISyntaxException ex) {
            Logger.getLogger(GeradorDeConteudoApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
