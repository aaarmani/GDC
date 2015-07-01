package br.com.actia.ui;

import br.com.actia.model.Banner;
import br.com.actia.validation.MaskTextField;
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
import java.util.List;

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public class BannerView extends VBox {
    private final int MAX_HEIGHT = 600;
    // private final int VIEWER_SIZE = 300;
    private final int VIEWER_SIZE = 100;
    
    private TextField tfId;
    // private TextField tfName;
    private MaskTextField tfName;
    private TextField tfImgName;
    private TextField tfImgPath;
    private Button btnChooseImage;
    private Button btnCancelBanner;
    private Button btnSaveBanner;
    private Button btnDeleteBanner;
    private ImageView ivImageView;
    private ResourceBundle rb;
    private EntityTable<Banner> table;
    
    public BannerView(ResourceBundle rb) {
        this.rb = rb;
        this.setMaxHeight(MAX_HEIGHT);
        this.getStyleClass().add("whitePanel");
        initializeComponents();
    }

    private void initializeComponents() {
        VBox head = buildHead();
        GridPane chooser = buildChooser();
        HBox buttons = buildButtons();
        VBox viewer = buildViewer();
        table = new EntityTable();
        
        this.getChildren().addAll(head, chooser, buttons, viewer, table);
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
        tfName = new MaskTextField();
        tfName.setMaskCompleteWord(MaskTextField.FIELD_NAME);
        
        Label lblImage = new Label(rb.getString("BNChooseImage"));
        tfImgName = new TextField();
        btnChooseImage = new Button(rb.getString("Search"));
        btnChooseImage.setId("btnChooseImage");
        btnChooseImage.getStyleClass().add("flatButton");
        
        tfImgPath = new TextField();
        
        GridFormBuilder grid = new GridFormBuilder();
        grid.addRowGenerics(lblName, tfName);
        grid.addRowGenerics(lblImage, tfImgName, btnChooseImage, null);
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
        
        btnDeleteBanner = new Button(rb.getString("Delete"));
        btnDeleteBanner.setId("btnDeleteBanner");
        btnDeleteBanner.getStyleClass().add("flatButton");
        btnDeleteBanner.setVisible(false);
        
        HBox hbox = new HBox();
        hbox.getChildren().addAll(btnDeleteBanner, btnCancelBanner, btnSaveBanner);
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
        
        VBox vbox = new VBox(separator, ivImageView);
        vbox.getStyleClass().add("viewPane");
        
        return vbox;
    }

    public Integer getBannerId() {
        try {
            return Integer.parseInt(tfId.getText());
        } catch (Exception nex) {
            return null;
        }
    }
    
    public TextField getTfId() {
        return tfId;
    }

    public void setTfId(TextField tfId) {
        this.tfId = tfId;
    }

    public MaskTextField getTfName() {
        return tfName;
    }

    public void setTfName(MaskTextField tfName) {
        this.tfName = tfName;
    }
    
    public TextField getTfImgName() {
        return tfImgName;
    }

    public void setTfImgName(TextField tfImgName) {
        this.tfImgName = tfImgName;
    }

    public TextField getTfImgPath() {
        return tfImgPath;
    }

    public void setTfImgPath(TextField tfImgPath) {
        this.tfImgPath = tfImgPath;
    }

    public Button getBtnChooseImage() {
        return btnChooseImage;
    }

    public void setBtnChooseImage(Button btnChooseImage) {
        this.btnChooseImage = btnChooseImage;
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

    public Button getBtnDeleteBanner() {
        return btnDeleteBanner;
    }

    public void setBtnDeleteBanner(Button btnDeleteBanner) {
        this.btnDeleteBanner = btnDeleteBanner;
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
        
        String imageName = null;
        if(!tfImgName.getText().trim().isEmpty()) {
            imageName = tfImgName.getText();
        }
        
        String imagePath = null;
        if(!tfImgPath.getText().trim().isEmpty()) {
            imagePath = tfImgPath.getText();
        }
        
        return new Banner(id, name, imageName, imagePath);
    }

    public void resetForm() {
        tfId.setText("");
        tfName.setText("");
        tfImgName.setText("");
        tfImgPath.setText("");
        ivImageView.setImage(null);
        btnDeleteBanner.setVisible(false);
    }

    public void refreshTable(List<Banner> listEntity) {
        table.reload(listEntity);   
    }
    
    public EntityTable getTable() {
        return table;
    }
    
    public void loadBannerToEdit(Banner banner) {
        if(banner.getId() != null) {
            tfId.setText(banner.getId().toString());
            btnDeleteBanner.setVisible(true);
        }
        tfName.setText(banner.getName());
        tfImgName.setText(banner.getImage());
        tfImgPath.setText(banner.getImagePath());
    }
}
