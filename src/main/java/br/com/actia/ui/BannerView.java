package br.com.actia.ui;

import br.com.actia.model.Banner;
import java.util.ResourceBundle;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public class BannerView extends VBox {
    //private final int MAX_HEIGHT = 600;
    private final int VIEWER_SIZE = 300;
    
    private TextField tfId;
    private TextField tfName;
    private TextField tfImgPath;
    private TextField tfAudioPath;
    private Button btnChooseImage;
    private Button btnChooseAudio;
    private Button btnCancelBanner;
    private Button btnSaveBanner;
    private ImageView ivImageView;
    private Button btnPlay;
    
    private ResourceBundle rb;
    
    public BannerView(ResourceBundle rb) {
        this.rb = rb;
        this.getStyleClass().add("whitePanel");
        initializeComponents();
    }

    private void initializeComponents() {
        VBox head = buildHead();
        GridPane chooser = buildChooser();
        HBox buttons = buildButtons();
        VBox viewer = buildViewer();
        
        this.getChildren().addAll(head, chooser, buttons, viewer);
    }

    private VBox buildHead() {
        Label lblTitle = new Label(rb.getString("BNNewBanner"));
        lblTitle.getStyleClass().add("titleLabel");
        Separator separator = new Separator();
        return new VBox(lblTitle, separator);
    }

    private GridPane buildChooser() {
        tfId = new TextField();
        
        Label lblName = new Label(rb.getString("Name"));
        tfName = new TextField();
        
        Label lblImage = new Label(rb.getString("BNChooseImage"));
        tfImgPath = new TextField();
        btnChooseImage = new Button(rb.getString("Search"));
        btnChooseImage.setId("btnChooseImage");
        btnChooseImage.getStyleClass().add("flatButton");
        
        Label lblAudio = new Label(rb.getString("BNChooseAudio"));
        tfAudioPath = new TextField();
        btnChooseAudio = new Button(rb.getString("Search"));
        btnChooseAudio.setId("btnChooseAudio");
        btnChooseAudio.getStyleClass().add("flatButton");
        
        GridFormBuilder grid = new GridFormBuilder();
        grid.addRowGenerics(lblName, tfName);
        grid.addRowGenerics(lblImage, tfImgPath, btnChooseImage, null);
        grid.addRowGenerics(lblAudio, tfAudioPath, btnChooseAudio, null);
        
        return grid.build();
    }

    private HBox buildButtons() {
        btnCancelBanner = new Button(rb.getString("Cancel"));
        btnCancelBanner.setId("btnCancelBanner");
        btnCancelBanner.setCancelButton(true);
        btnCancelBanner.getStyleClass().add("flatButton");
        
        btnSaveBanner = new Button(rb.getString("Save"));
        btnSaveBanner.setId("btnSalveBanner");
        btnSaveBanner.setDefaultButton(true);
        btnSaveBanner.getStyleClass().add("flatButton");
        
        HBox hbox = new HBox();
        hbox.getChildren().addAll(btnCancelBanner, btnSaveBanner);
        hbox.getStyleClass().add("buttonBar");
        hbox.setAlignment(Pos.CENTER_RIGHT);
        
        return hbox;
    }
    
    private VBox buildViewer() {
        Separator separator = new Separator();
        ivImageView = new ImageView();
        ivImageView.setFitWidth(VIEWER_SIZE);
        ivImageView.setFitHeight(VIEWER_SIZE);
        ivImageView.setPreserveRatio(true);
        ivImageView.setSmooth(true);
        ivImageView.setCache(true);
        ivImageView.getStyleClass().add("Img");
        
        btnPlay = new Button(rb.getString("Play"));
        btnPlay.setId("btnPlayBanner");
        btnPlay.getStyleClass().add("flatButton");
        setBtToPlay();
        
        VBox vbox = new VBox(separator, ivImageView, btnPlay);
        vbox.getStyleClass().add("viewPane");
        
        return vbox;
    }

    public TextField getTfId() {
        return tfId;
    }

    public void setTfId(TextField tfId) {
        this.tfId = tfId;
    }

    public TextField getTfName() {
        return tfName;
    }

    public void setTfName(TextField tfName) {
        this.tfName = tfName;
    }
    
    public TextField getTfImgPath() {
        return tfImgPath;
    }

    public void setTfImgPath(TextField tfImgPath) {
        this.tfImgPath = tfImgPath;
    }

    public TextField getTfAudioPath() {
        return tfAudioPath;
    }

    public void setTfAudioPath(TextField tfAudioPath) {
        this.tfAudioPath = tfAudioPath;
    }

    public Button getBtnChooseImage() {
        return btnChooseImage;
    }

    public void setBtnChooseImage(Button btnChooseImage) {
        this.btnChooseImage = btnChooseImage;
    }

    public Button getBtnChooseAudio() {
        return btnChooseAudio;
    }

    public void setBtnChooseAudio(Button btnChooseAudio) {
        this.btnChooseAudio = btnChooseAudio;
    }

    public Button getBtnCancelBanner() {
        return btnCancelBanner;
    }

    public void setBtnCancelBanner(Button btnCancelBanner) {
        this.btnCancelBanner = btnCancelBanner;
    }

    public Button getBtnSaveBanner() {
        return btnSaveBanner;
    }

    public void setBtnSaveBanner(Button btnSaveBanner) {
        this.btnSaveBanner = btnSaveBanner;
    }

    public ImageView getIvImageView() {
        return ivImageView;
    }

    public void setIvImageView(ImageView ivImageView) {
        this.ivImageView = ivImageView;
    }

    public Button getBtnPlay() {
        return btnPlay;
    }

    public void setBtnPlay(Button btnPlay) {
        this.btnPlay = btnPlay;
    }

    public Banner loadBannerFromPanel() {
        Integer id = null;
        if(!tfId.getText().trim().isEmpty()) {
            id = Integer.valueOf(tfId.getText());
        }
        
        String name = null;
        if(!tfName.getText().trim().isEmpty()) {
            name = tfName.getText();
        }
        
        String imagePath = null;
        if(!tfImgPath.getText().trim().isEmpty()) {
            imagePath = tfImgPath.getText();
        }
        
        String audioPath = null;
        if(!tfAudioPath.getText().trim().isEmpty()) {
            audioPath = tfAudioPath.getText();
        }
        return new Banner(id, name, imagePath, audioPath);
    }

    public void resetForm() {
        tfId.setText("");
        tfName.setText("");
        tfImgPath.setText("");
        tfAudioPath.setText("");
        ivImageView.setImage(null);
    }

    public void setBtToPlay() {
        btnPlay.setText(rb.getString("Play"));
    }

    public void setBtToStop() {
        btnPlay.setText(rb.getString("Stop"));
    }
}
