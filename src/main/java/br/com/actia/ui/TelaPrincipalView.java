
package br.com.actia.ui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author Armani
 */
public class TelaPrincipalView {
    private Scene mainScene;
    
    public TelaPrincipalView(Stage stage) {
        inicializaComponentes();

        BorderPane borderPane = new BorderPane();
        borderPane.getStyleClass().add("MainScreen");
        mainScene = new Scene(borderPane);
        mainScene.getStylesheets().add("style.css");
        
        VBox vbMenu = getVboxMenu();
        VBox vbAction = getVboxAction();
        
        borderPane.setLeft(vbMenu);
        borderPane.setRight(vbAction);
        
        stage.setTitle("ADB Gerador de Conteúdo");
        stage.setWidth(1000);
        stage.setHeight(620);
        stage.setScene(mainScene);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }

    private void inicializaComponentes() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private VBox getVboxMenu() {
        VBox vbox = new VBox();
        vbox.setMinWidth(150);
        vbox.setMaxWidth(150);
        vbox.getStyleClass().add("MenuMain");
        
        Image logoImg = new Image("actiaLogo.png");//getClass().getResourceAsStream("bugIcon.png"));
        ImageView imgvLogo = new ImageView(logoImg);
        imgvLogo.maxHeight(40);
        imgvLogo.maxWidth(150);
        vbox.getChildren().add(imgvLogo);
        
        Button btn = new Button("Teste Armani");
        btn.setMinSize(150, 40);
        btn.getStyleClass().add("button");
        vbox.getChildren().add(btn);
        
        Button btn2 = new Button("Teste Armani 2");
        btn2.setMinSize(150, 40);
        btn2.getStyleClass().add("button");
        vbox.getChildren().add(btn2);

        return vbox;
    }

    private VBox getVboxAction() {
        VBox vbox = new VBox();
        vbox.setMinWidth(40);
        vbox.setMaxWidth(40);
        vbox.getStyleClass().add("MenuMain");
        
        Image logoImg = new Image("bugIcon.png");//getClass().getResourceAsStream("bugIcon.png"));
        ImageView imgvLogo = new ImageView(logoImg);
        imgvLogo.maxHeight(40);
        imgvLogo.maxWidth(40);
        vbox.getChildren().add(imgvLogo);
        
        Button btn = new Button("Dw1");
        btn.setMinSize(40, 40);
        btn.getStyleClass().add("button");
        vbox.getChildren().add(btn);
        
        Button btn2 = new Button("Dw");
        btn2.setMinSize(40, 40);
        btn2.getStyleClass().add("button");
        vbox.getChildren().add(btn2);
        
        return vbox;
    }
}
