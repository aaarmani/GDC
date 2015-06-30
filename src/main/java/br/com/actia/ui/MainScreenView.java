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
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

/**
 *
 * @author Armani
 */
public class MainScreenView {

    private Scene mainScene;
    private BorderPane bpMenu;
    private BorderPane bpAction;
    private Pane paneCenter;
    private ImageView imgvLogo;
    private ImageView imgBR;
    private ImageView imgES;
    private ImageView imgEN;
    private ImageView imgUser;
    
    private Button btnRoute;
    private Button btnMapEntitys;
    private Button btnBanner;
    private Button btnVideo;
    private Button btnRSS;
    private Button btnListPoi;
    private Button btnListBanner;
    private Button btnListVideo;
    private Button btnListRSS;
    private Button btnFileGenerator;
    private Button btnDowloadService;
	private Button btnUserOptions;
    
    private Button btnBR;
    private Button btnEN;
    private Button btnES;
    
    BorderPane borderPane = null;
    
    private ResourceBundle rb;
	private FontAwesome fontAwesome;

    public MainScreenView(Stage stage, ResourceBundle rb) {
        this.rb = rb;
        inicializaComponentes();
        
        this.borderPane = new BorderPane();
        borderPane.getStyleClass().add("MainScreen");
        mainScene = new Scene(borderPane);
        mainScene.getStylesheets().add("css/style.css");

        bpMenu = getBorderPaneMenu();
        bpAction = createBpAction();
        paneCenter = createPaneCenter();
        paneCenter.setStyle("-fx-background-image: url('actiaLogo.png'); " +
                   "-fx-background-position: center center; " +
                   "-fx-background-repeat: stretch;");
        
        borderPane.setLeft(bpMenu);
        borderPane.setCenter(paneCenter);
        borderPane.setRight(bpAction);

        stage.setTitle(rb.getString("MainFrameTitle"));
        stage.setWidth(1000);
        stage.setHeight(620);
        stage.setScene(mainScene);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }

    private void inicializaComponentes() {
        fontAwesome = new FontAwesome();
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
        
    private BorderPane createBpAction() {
        BorderPane bpAct = new BorderPane();
        bpAct.setMinWidth(50);
        bpAct.setMaxWidth(50);
        bpAct.getStyleClass().add("MenuMain");
        
        bpAct.setTop(getUserLogo());
        bpAct.setBottom(getDownloadButton());
        
        return bpAct;
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
       //imgvLogo.getStyleClass().add("Img");

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
        
        btnRoute = newMenuItem(rb.getString("Route"), "CODE_FORK");
        btnMapEntitys = newMenuItem(rb.getString("Map"), "GLOBE");
        btnBanner = newMenuItem(rb.getString("Banner"), "IMAGE");
        btnVideo = newMenuItem(rb.getString("Video"), "VIDEO_CAMERA");
        btnRSS = newMenuItem(rb.getString("RSS"), "RSS");
        btnListPoi = newMenuItem(rb.getString("ListPoi"), "LIST_UL");
        btnListBanner = newMenuItem(rb.getString("ListBanner"), "LIST_UL");
        btnListVideo = newMenuItem(rb.getString("ListVideo"), "LIST_UL");
        btnListRSS = newMenuItem(rb.getString("ListRSS"), "LIST_UL");
        btnFileGenerator = newMenuItem(rb.getString("GenerateFiles"), "GEARS");
        
        vbMenu.getChildren().addAll(btnRoute, btnMapEntitys, btnBanner, btnVideo, btnRSS, btnListPoi, btnListBanner, btnListVideo, btnListRSS, btnFileGenerator);
        return vbMenu;
    }
    
    /**
     *
     * @return HBox with logo images for i18n
     */
    private HBox getFlags() {
        imgBR = new ImageView("br.png");
        imgBR.getStyleClass().add("flag");
        imgBR.setFitHeight(20);
        btnBR = new Button(null, imgBR);
        btnBR.getStyleClass().add("LangButton");
        btnBR.setId("btnLanBR");

        imgES = new ImageView("es.png");
        imgES.getStyleClass().add("flag");
        imgES.setFitHeight(20);
        btnES = new Button(null, imgES);
        btnES.getStyleClass().add("LangButton");
        btnES.setId("btnLanES");

        imgEN = new ImageView("us.png");
        imgEN.getStyleClass().add("flag");
        imgEN.setFitHeight(20);
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
		
        btnUserOptions = new Button(null, imgUser);
        btnUserOptions.setMinWidth(50);
        btnUserOptions.setMaxWidth(50);
        btnUserOptions.setMinHeight(50);
        btnUserOptions.setMaxHeight(50);
        btnUserOptions.getStyleClass().add("flatButton");
        btnUserOptions.getStyleClass().add("flatIcon");
        btnUserOptions.setId("btnUserOptions");
        
        VBox vbox = new VBox();
        vbox.getStyleClass().add("Img");
        vbox.getChildren().add(btnUserOptions);
        
        return vbox;
    }
    
    private VBox getDownloadButton() {
        Image downloadImg = new Image("download.png");
        ImageView imgvDownload = new ImageView(downloadImg);
        imgvDownload.setFitWidth(30);
        imgvDownload.setFitHeight(30);
        imgvDownload.setPreserveRatio(true);
        imgvDownload.setSmooth(true);
        imgvDownload.setCache(true);
        btnDowloadService = new Button(null, imgvDownload);
        btnDowloadService.setMinWidth(50);
        btnDowloadService.setMaxWidth(50);
        btnDowloadService.setMinHeight(50);
        btnDowloadService.setMaxHeight(50);
        btnDowloadService.getStyleClass().add("flatButton");
        btnDowloadService.getStyleClass().add("flatIcon");
        btnDowloadService.setId("btnDownloadService");
        
        VBox vbox = new VBox();
        vbox.getStyleClass().add("Img");
        vbox.getChildren().add(btnDowloadService);
        
        return vbox;
    }
    
   private Button newMenuItem(String item, String strImage) {
        Button btn = new Button(item, fontAwesome.create(strImage));
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

    public BorderPane getBpAction() {
        return bpAction;
    }

    public void setBpAction(BorderPane bpAction) {
        this.bpAction = bpAction;
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
    
    public Button getBtnRoute() {
        return btnRoute;
    }
    
    public void setBtnRoute(Button btnRoute) {
        this.btnRoute = btnRoute;
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
    
    public Button getBtnRSS() {
        return btnRSS;
    }
    
    public void setBtnRSS(Button btnRSS) {
        this.btnRSS = btnRSS;
    }
    
    public Button getBtnListPoi() {
        return btnListPoi;
    }
    
    public void setBtnListPoi(Button btnListPoi) {
        this.btnListPoi = btnListPoi;
    }
    
    public Button getBtnListBanner() {
        return btnListBanner;
    }
    
    public void setBtnListBanner(Button btnListBanner) {
        this.btnListBanner = btnListBanner;
    }
    
    public Button getBtnListVideo() {
        return btnListVideo;
    }
    
    public void setBtnListVideo(Button btnListVideo) {
        this.btnListVideo = btnListVideo;
    }
    
    public Button getBtnListRSS() {
        return btnListRSS;
    }
    
    public void setBtnListRSS(Button btnListRSS) {
        this.btnListRSS = btnListRSS;
    }

    public Button getBtnFileGenerator() {
        return btnFileGenerator;
    }
    
    public void setBtnFileGenerator(Button btnFileGenerator) {
        this.btnFileGenerator = btnFileGenerator;
    }
    
	public Button getBtnUserOptions() {
        return btnUserOptions;
    }

    public void setBtnUserOptions(Button btnUserOptions) {
        this.btnUserOptions = btnUserOptions;
    }    

	public Button getBtnDowloadService() {
        return btnDowloadService;
    }

    public void setBtnDowloadService(Button btnDowloadService) {
        this.btnDowloadService = btnDowloadService;
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
    
    public BorderPane getBorderPane() {
        return this.borderPane;
    }
}
