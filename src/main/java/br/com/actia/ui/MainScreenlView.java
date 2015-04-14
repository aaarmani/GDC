package br.com.actia.ui;

import java.util.ResourceBundle;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author Armani
 */
public class MainScreenlView {

    private Scene mainScene;
    private BorderPane bpMenu;
    private VBox vboxAction;
    private Pane paneCenter;
    private ImageView imgvLogo;
    private ImageView imgBR;
    private ImageView imgES;
    private ImageView imgEN;
    private ImageView imgUser;
    
    private Button btnMapEntitys;
    private Button btnBanner;
    private Button btnVideo;
    private Button btnListPoi;
    private Button btnListVideo;
    
    private Button btnBR;
    private Button btnEN;
    private Button btnES;
    
    private ResourceBundle rb;

    public MainScreenlView(Stage stage, ResourceBundle rb) {
        this.rb = rb;
        inicializaComponentes();

        BorderPane borderPane = new BorderPane();
        borderPane.getStyleClass().add("MainScreen");
        mainScene = new Scene(borderPane);
        mainScene.getStylesheets().add("style.css");

        bpMenu = getBorderPaneMenu();
        vboxAction = createVboxAction();
        paneCenter = createPaneCenter();

        borderPane.setLeft(bpMenu);
        borderPane.setCenter(paneCenter);
        borderPane.setRight(vboxAction);

        stage.setTitle(rb.getString("MainFrameTitle"));
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

    private BorderPane getBorderPaneMenu() {
        BorderPane borderPane = new BorderPane();
        borderPane.setMinWidth(150);
        borderPane.setMaxWidth(150);
        borderPane.getStyleClass().add("MenuMain");

        borderPane.setTop(getLogo());
        borderPane.setCenter(getMenu());
        borderPane.setBottom(getFlags());

        return borderPane;
    }

    private Pane createPaneCenter() {
        return new Pane();
    }
        
    private VBox createVboxAction() {
        VBox vbox = new VBox();
        vbox.setMinWidth(50);
        vbox.setMaxWidth(50);
        vbox.getStyleClass().add("MenuMain");

        vbox.getChildren().add(getUserLogo());

        return vbox;
    }

    /**
     *
     * @return VBox with actia image logo
     */
    private VBox getLogo() {
        Image logoImg = new Image("actiaLogo.png");
        imgvLogo = new ImageView(logoImg);
        imgvLogo.setFitWidth(130);
        imgvLogo.setFitHeight(40);
        imgvLogo.setPreserveRatio(true);
        imgvLogo.setSmooth(true);
        imgvLogo.setCache(true);
        imgvLogo.getStyleClass().add("Img");

        VBox logoVBox = new VBox();
        logoVBox.getStyleClass().add("LogoPanel");
        logoVBox.getChildren().add(imgvLogo);
        return logoVBox;
    }

    /**
     * 
     * @return VBox menu
     */
    private VBox getMenu() {
        VBox vbMenu = new VBox();
        
        btnMapEntitys = newMenuItem(rb.getString("Map"));
        btnBanner = newMenuItem(rb.getString("Banner"));
        btnVideo = newMenuItem(rb.getString("Video"));
        btnListPoi = newMenuItem(rb.getString("ListPoi"));
        btnListVideo = newMenuItem(rb.getString("ListVideo"));
        
        vbMenu.getChildren().addAll(btnMapEntitys, btnBanner, btnVideo, btnListPoi, btnListVideo);
        return vbMenu;
    }
    
    /**
     *
     * @return HBox with logo images for i18n
     */
    private HBox getFlags() {
        imgBR = new ImageView("gn.png");
        imgBR.getStyleClass().add("flag");
        btnBR = new Button(null, imgBR);
        btnBR.getStyleClass().add("LangButton");
        btnBR.setId("btnLanBR");

        imgES = new ImageView("es.png");
        imgES.getStyleClass().add("flag");
        btnES = new Button(null, imgES);
        btnES.getStyleClass().add("LangButton");
        btnES.setId("btnLanES");
        
        imgEN = new ImageView("us.png");
        imgEN.getStyleClass().add("flag");
        btnEN = new Button(null, imgEN);
        btnEN.getStyleClass().add("LangButton");
        btnEN.setId("btnLanEN");

        HBox flagsPanel = new HBox();
        flagsPanel.getStyleClass().add("LangPanel");
        flagsPanel.getChildren().addAll(btnBR, btnEN, btnES);
        return flagsPanel;
    }
    
    private VBox getUserLogo() {
        Image logoImg = new Image("user_verde.png");
        imgUser = new ImageView(logoImg);
        imgUser.setFitWidth(30);
        imgUser.setFitHeight(30);
        imgUser.setPreserveRatio(true);
        imgUser.setSmooth(true);
        imgUser.setCache(true);
        imgUser.getStyleClass().add("Img");
        
        VBox vbox = new VBox();
        vbox.getStyleClass().add("ImgUser");
        vbox.getChildren().add(imgUser);
        
        return vbox;
    }
    
    private Button newMenuItem(String item) {
        Button btn = new Button(item);
        btn.setMinSize(150, 40);
        btn.getStyleClass().add("flatButton");
        btn.setId(item);
        btn.setAlignment(Pos.CENTER_LEFT);
        return btn;
    }
    
    //#####################################################
    public Scene getMainScene() {
        return mainScene;
    }

    public void setMainScene(Scene mainScene) {
        this.mainScene = mainScene;
    }

    public BorderPane getBpMenu() {
        return bpMenu;
    }

    public void setBpMenu(BorderPane bpMenu) {
        this.bpMenu = bpMenu;
    }

    public VBox getVbAction() {
        return vboxAction;
    }

    public void setVbAction(VBox vbAction) {
        this.vboxAction = vbAction;
    }

    public ImageView getImgvLogo() {
        return imgvLogo;
    }

    public void setImgvLogo(ImageView imgvLogo) {
        this.imgvLogo = imgvLogo;
    }

    public ImageView getImgBR() {
        return imgBR;
    }

    public void setImgBR(ImageView imgBR) {
        this.imgBR = imgBR;
    }

    public ImageView getImgES() {
        return imgES;
    }

    public void setImgES(ImageView imgES) {
        this.imgES = imgES;
    }

    public ImageView getImgEN() {
        return imgEN;
    }

    public void setImgEN(ImageView imgEN) {
        this.imgEN = imgEN;
    }

    public ImageView getImgUser() {
        return imgUser;
    }

    public void setImgUser(ImageView imgUser) {
        this.imgUser = imgUser;
    }

    public Button getBtnMapEntitys() {
        return btnMapEntitys;
    }

    public void setBtnMapsEntitys(Button btnMapEntitys) {
        this.btnMapEntitys = btnMapEntitys;
    }

    public Button getBtnBanner() {
        return btnBanner;
    }

    public void setBtnBanner(Button btnBanner) {
        this.btnBanner = btnBanner;
    }

    public Button getBtnVideo() {
        return btnVideo;
    }

    public void setBtnVideo(Button btnVideo) {
        this.btnVideo = btnVideo;
    }

    public Button getBtnListPoi() {
        return btnListPoi;
    }

    public void setBtnListPoi(Button btnListPoi) {
        this.btnListPoi = btnListPoi;
    }

    public Button getBtnListVideo() {
        return btnListVideo;
    }

    public void setBtnListVideo(Button btnListVideo) {
        this.btnListVideo = btnListVideo;
    }
     public VBox getVboxAction() {
        return vboxAction;
    }

    public void setVboxAction(VBox vboxAction) {
        this.vboxAction = vboxAction;
    }

    public Pane getPaneCenter() {
        return paneCenter;
    }

    public void setPaneCenter(Pane paneCenter) {
        this.paneCenter = paneCenter;
    }

    public Button getBtnBR() {
        return btnBR;
    }

    public void setBtnBR(Button btnBR) {
        this.btnBR = btnBR;
    }

    public Button getBtnEN() {
        return btnEN;
    }

    public void setBtnEN(Button btnEN) {
        this.btnEN = btnEN;
    }

    public Button getBtnES() {
        return btnES;
    }

    public void setBtnES(Button btnES) {
        this.btnES = btnES;
    }

    public ResourceBundle getRb() {
        return rb;
    }

    public void setRb(ResourceBundle rb) {
        this.rb = rb;
    }
}
