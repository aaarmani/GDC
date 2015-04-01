package br.com.actia.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public class GoogleMapView  extends StackPane {
    private  final int BUTTON_SIZE = 32;
    private  final int MENU_SIZE = BUTTON_SIZE + 4;
    
    private WebView webview;
    private Button btnZoomIn;
    private Button btnZoomOut;
    private Button btnNewBusStop;
    private Button btnNewPOI;
    private AnchorPane ap;
    private VBox vbControl;
    
    public GoogleMapView() {
        webview = new WebView();
        
        ap = new AnchorPane();
        ap.setTopAnchor(webview,0.0);
        ap.setLeftAnchor(webview,0.0);
        ap.setBottomAnchor(webview, 0.0);
        ap.setRightAnchor(webview, 0.0);
        ap.getChildren().add(webview);
        
          
        getChildren().add(ap);
        setAlignment(Pos.CENTER_RIGHT);
        getChildren().add(createControlPane());
    }
    
    private VBox createControlPane() {
        btnNewBusStop = getNewButton("location.png","Novo Ponto de Parada");
        btnNewPOI = getNewButton("pin.png", "Novo Ponto de Interesse");
        btnZoomIn = getNewButton("zoom-in.png", "Zoom mais");
        btnZoomOut = getNewButton("zoom-out.png", "Zoom menos");
        
        VBox vbox = new VBox();
        vbox.setSpacing(4);
        vbox.setAlignment(Pos.CENTER);
        vbox.setMaxHeight(4 * MENU_SIZE);
        vbox.setMaxWidth(MENU_SIZE);
        vbox.getStyleClass().add("MenuMain");
        vbox.getChildren().addAll(btnNewBusStop, btnNewPOI, btnZoomIn, btnZoomOut);
        
        return vbox;
    }
    
    private Button getNewButton(String imgPath, String toolType) {
        Image img = new Image(imgPath);
        ImageView imgView = new ImageView(img);
        
        imgView.maxHeight(BUTTON_SIZE);
        imgView.minHeight(BUTTON_SIZE);
        imgView.maxWidth(BUTTON_SIZE);
        imgView.minWidth(BUTTON_SIZE);
               
        Button btn = new Button(null, imgView);
        btn.setTooltip(new Tooltip(toolType));
        
        btn.setMaxWidth(BUTTON_SIZE);
        btn.setMinWidth(BUTTON_SIZE);
        btn.setMaxHeight(BUTTON_SIZE);
        btn.setMinHeight(BUTTON_SIZE);
        btn.setBackground(Background.EMPTY);
        btn.getStyleClass().add("flatButton");
        return btn;
    }
    
//##################################
    public WebView getWebview() {
        return webview;
    }

    public void setWebview(WebView webview) {
        this.webview = webview;
    }

    public Button getBtnZoomIn() {
        return btnZoomIn;
    }

    public void setBtnZoomIn(Button btnZoomIn) {
        this.btnZoomIn = btnZoomIn;
    }

    public Button getBtnZoomOut() {
        return btnZoomOut;
    }

    public void setBtnZoomOut(Button btnZoomOut) {
        this.btnZoomOut = btnZoomOut;
    }

    public Button getBtnNewBusStop() {
        return btnNewBusStop;
    }

    public void setBtnNewBusStop(Button btnNewBusStop) {
        this.btnNewBusStop = btnNewBusStop;
    }

    public Button getBtnNewPOI() {
        return btnNewPOI;
    }

    public void setBtnNewPOI(Button btnNewPOI) {
        this.btnNewPOI = btnNewPOI;
    }

    public VBox getVbControl() {
        return vbControl;
    }

    public void setVbControl(VBox vbControl) {
        this.vbControl = vbControl;
    }

}
