package br.com.actia.ui;

import br.com.actia.model.Indication;
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

public class IndicationView extends VBox {
    private final int MAX_HEIGHT = 600;
    // private final int VIEWER_SIZE = 300;
    private final int VIEWER_SIZE = 100;
    
    private TextField tfId;
    // private TextField tfName;
    private MaskTextField tfName;
    private TextField tfImgName;
    private TextField tfImgPath;
    private TextField tfAudioName;
    private TextField tfAudioPath;
    private Button btnChooseImage;
    private Button btnChooseAudio;
    private Button btnCancelIndication;
    private Button btnSaveIndication;
    private Button btnDeleteIndication;
    private ImageView ivImageView;
    private Button btnPlay;
    private ResourceBundle rb;
    private EntityTable<Indication> table;
    
    public IndicationView(ResourceBundle rb) {
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
        Label lblTitle = new Label(rb.getString("INNewIndication"));
        lblTitle.getStyleClass().add("titleLabel");
        Separator separator = new Separator();
        return new VBox(lblTitle, separator);
    }

    private GridPane buildChooser() {
        tfId = new TextField();
        
        Label lblName = new Label(rb.getString("Name"));
        // tfName = new TextField();
        tfName = new MaskTextField();
        //tfName.setMask("****************");
        tfName.setMaskCompleteWord(MaskTextField.FIELD_NAME);
        
        Label lblImage = new Label(rb.getString("INChooseImage"));
        tfImgName = new TextField();
        btnChooseImage = new Button(rb.getString("Search"));
        btnChooseImage.setId("btnChooseImage");
        btnChooseImage.getStyleClass().add("flatButton");
        
        Label lblAudio = new Label(rb.getString("INChooseAudio"));
        tfAudioName = new TextField();
        btnChooseAudio = new Button(rb.getString("Search"));
        btnChooseAudio.setId("btnChooseAudio");
        btnChooseAudio.getStyleClass().add("flatButton");
        
        tfImgPath = new TextField();
        tfAudioPath = new TextField();
        
        GridFormBuilder grid = new GridFormBuilder();
        grid.addRowGenerics(lblName, tfName);
        grid.addRowGenerics(lblImage, tfImgName, btnChooseImage, null);
        grid.addRowGenerics(lblAudio, tfAudioName, btnChooseAudio, null);
        
        return grid.build();
    }

    private HBox buildButtons() {
        btnCancelIndication = new Button(rb.getString("Cancel"));
        btnCancelIndication.setId("btnCancelIndication");
        btnCancelIndication.setCancelButton(true);
        btnCancelIndication.getStyleClass().add("flatButton");
        
        btnSaveIndication = new Button(rb.getString("Save"));
        btnSaveIndication.setId("btnSalveIndication");
        btnSaveIndication.setDefaultButton(true);
        btnSaveIndication.getStyleClass().add("flatButton");
        
        btnDeleteIndication = new Button(rb.getString("Delete"));
        btnDeleteIndication.setId("btnDeleteIndication");
        btnDeleteIndication.getStyleClass().add("flatButton");
        btnDeleteIndication.setVisible(false);
        
        HBox hbox = new HBox();
        hbox.getChildren().addAll(btnDeleteIndication, btnCancelIndication, btnSaveIndication);
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
        btnPlay.setId("btnPlayIndication");
        btnPlay.getStyleClass().add("flatButton");
        setBtToPlay();
        
        VBox vbox = new VBox(separator, ivImageView, btnPlay);
        vbox.getStyleClass().add("viewPane");
        
        return vbox;
    }

    public Integer getIndicationId() {
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

    public TextField getTfAudioName() {
        return tfAudioName;
    }

    public void setTfAudioName(TextField tfAudioName) {
        this.tfAudioName = tfAudioName;
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

    public Button getBtnCancelIndication() {
        return btnCancelIndication;
    }

    public void setBtnCancelIndication(Button btnCancelIndication) {
        this.btnCancelIndication = btnCancelIndication;
    }

    public Button getBtnSaveIndication() {
        return btnSaveIndication;
    }

    public void setBtnSaveIndication(Button btnSaveIndication) {
        this.btnSaveIndication = btnSaveIndication;
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

    public Button getBtnDeleteIndication() {
        return btnDeleteIndication;
    }

    public void setBtnDeleteIndication(Button btnDeleteIndication) {
        this.btnDeleteIndication = btnDeleteIndication;
    }
    
    public Indication loadIndicationFromPanel() {
        Integer id = null;
        if(!tfId.getText().trim().isEmpty()) {
            id = Integer.valueOf(tfId.getText());
        }
        
        String name = null;
        if(!tfName.getText().trim().isEmpty()) {
            name = tfName.getText();
        }
        
        String imageName = null;
        if(tfImgName != null && !tfImgName.getText().trim().isEmpty()) {
            imageName = tfImgName.getText();
        }
        
        String imagePath = null;
        if(tfImgPath.getText() != null && !tfImgPath.getText().trim().isEmpty()) {
            imagePath = tfImgPath.getText();
        }
        
        String audioName = null;
        if(tfAudioName.getText() != null && !tfAudioName.getText().trim().isEmpty()) {
            audioName = tfAudioName.getText();
        }
        
        String audioPath = null;
        if(tfAudioPath.getText() != null && !tfAudioPath.getText().trim().isEmpty()) {
            audioPath = tfAudioPath.getText();
        }
        
        return new Indication(id, name, imageName, imagePath, audioName, audioPath);
    }

    public void resetForm() {
        tfId.setText("");
        tfName.setText("");
        tfImgName.setText("");
        tfImgPath.setText("");
        tfAudioName.setText("");
        tfAudioPath.setText("");
        ivImageView.setImage(null);
        btnDeleteIndication.setVisible(false);
    }

    public void setBtToPlay() {
        btnPlay.setText(rb.getString("Play"));
    }

    public void setBtToStop() {
        btnPlay.setText(rb.getString("Stop"));
    }
    
    public void refreshTable(List<Indication> listEntity) {
        table.reload(listEntity);   
    }
    
    public EntityTable getTable() {
        return table;
    }
    
    public void loadIndicationToEdit(Indication indication) {
        if(indication.getId() != null) {
            tfId.setText(indication.getId().toString());
            btnDeleteIndication.setVisible(true);
        }
        tfName.setText(indication.getName());
        tfImgName.setText(indication.getImage());
        tfImgPath.setText(indication.getImagePath());
        tfAudioName.setText(indication.getAudio());
        tfAudioPath.setText(indication.getAudioPath());
    }
    
}
