package br.com.actia.ui;

import br.com.actia.model.Poi;
import br.com.actia.model.PoiType;
import br.com.actia.validation.MaskTextField;
import java.util.ResourceBundle;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public class PoiView extends VBox {
    private final int MAX_HEIGHT = 200;
    
    private TextField tfId;
    private MaskTextField tfName;
    private MaskTextField tfLatitude;
    private MaskTextField tfLongitude;
    private Button btnCancelPOI;
    private Button btnDeletePOI;
    private Button btnSavePOI;
    private ComboBox<PoiType> cbPoiType;
    private final ResourceBundle rb;
        
    public PoiView(ResourceBundle rb) {
        this.rb = rb;
        this.setMaxHeight(MAX_HEIGHT);
        this.getStyleClass().add("transparentPanel");
        
        initializeComponents();
    }

    private void initializeComponents() {
        VBox head = buildHead();
        GridPane inputs = buildInputs();
        HBox buttons = buildButtons();
        
        this.getChildren().addAll(head, inputs, buttons);        
    }

    private VBox buildHead() {
        Label lblTitle = new Label(rb.getString("NewPoi"));
        lblTitle.getStyleClass().add("titleLabel");
        Separator separator = new Separator();

        VBox vbox = new VBox();
        vbox.getChildren().addAll(lblTitle, separator);
        return vbox;
    }

    private GridPane buildInputs() {
        tfId = new TextField();
        
        Label lblName = new Label(rb.getString("Name"));
        tfName = new MaskTextField();
        tfName.setMinWidth(180);
        tfName.setMaxWidth(180);
        tfName.setMaskCompleteWord(MaskTextField.FIELD_NAME);
        
        Label lblType = new Label(rb.getString("Type"));
        cbPoiType = new ComboBox();
        cbPoiType.setPromptText("escolha um tipo");
        
        Label lblLat = new Label(rb.getString("Latitude"));
        tfLatitude = new MaskTextField();
        tfLatitude.setMinWidth(180);
        tfLatitude.setMaxWidth(180);
        tfLatitude.setMaskCompleteWord(MaskTextField.FIELD_LATLONG);
        
        Label lblLong = new Label(rb.getString("Longitude"));
        tfLongitude = new MaskTextField();
        tfLongitude.setMinWidth(180);
        tfLongitude.setMaxWidth(180);
        tfLongitude.setMaskCompleteWord(MaskTextField.FIELD_LATLONG);
        
        GridFormBuilder grid = new GridFormBuilder();
        grid.addRowGenerics(lblName, tfName, lblType, cbPoiType);
        grid.addRowGenerics(lblLat, tfLatitude, lblLong, tfLongitude);
        
        return grid.build();
    }

    private HBox buildButtons() {
        btnDeletePOI = new Button(rb.getString("Delete"));
        btnDeletePOI.setId("deletePOI");
        btnDeletePOI.getStyleClass().add("flatButton");
                
        btnCancelPOI = new Button(rb.getString("Cancel"));
        btnCancelPOI.setId("cancelPOI");
        btnCancelPOI.setCancelButton(true);
        btnCancelPOI.getStyleClass().add("flatButton");
        
        btnSavePOI = new Button(rb.getString("Save"));
        btnSavePOI.setId("salvePOI");
        btnSavePOI.setDefaultButton(true);
        btnSavePOI.getStyleClass().add("flatButton");
        
        HBox hbox = new HBox();
        hbox.getChildren().addAll(btnDeletePOI, btnCancelPOI, btnSavePOI);
        hbox.getStyleClass().add("buttonBar");
        hbox.setAlignment(Pos.CENTER_RIGHT);
        
        return hbox;
    }

    public void resetForm() {
        tfId.setText("");
        tfName.setText("");
        //tfType
        tfLatitude.setText("");
        tfLongitude.setText("");
    }
    
    public Poi loadPoiFromPanel() {
        Integer id = null;
        if(!tfId.getText().trim().isEmpty()) {
            id = Integer.valueOf(tfId.getText());
        }
        
        String name = null;
        if (!tfName.getText().trim().isEmpty()) {
            name = tfName.getText().trim();
        }
        
        PoiType type = null;
        if(!cbPoiType.getSelectionModel().isEmpty()) {
            type = cbPoiType.getSelectionModel().getSelectedItem();
        }
        
        Double latitude = null;
        if(!tfLatitude.getText().trim().isEmpty()) {
            latitude = Double.valueOf(tfLatitude.getText());
        }
        
        Double longitude = null;
        if(!tfLongitude.getText().trim().isEmpty()) {
            longitude = Double.valueOf(tfLongitude.getText());
        }
        
        return new Poi(id, type, name, latitude, longitude);
    }
    
    public void setPoi(Poi poi) {
        resetForm();
        if(poi != null) {
            if(poi.getId() != null)
                tfId.setText(poi.getId().toString());
            //tfType
            tfLatitude.setText(Double.toString(poi.getLatitude()));
            tfLongitude.setText(Double.toString(poi.getLongitude()));
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

    public MaskTextField getTfLatitude() {
        return tfLatitude;
    }

    public void setTfLatitude(MaskTextField tfLatitude) {
        this.tfLatitude = tfLatitude;
    }

    public MaskTextField getTfLongitude() {
        return tfLongitude;
    }

    public void setTfLongitude(MaskTextField tfLongitude) {
        this.tfLongitude = tfLongitude;
    }

    public Button getBtnCancelPOI() {
        return btnCancelPOI;
    }

    public void setBtnCancelPOI(Button btnCancelPOI) {
        this.btnCancelPOI = btnCancelPOI;
    }

    public Button getBtnDeletePOI() {
        return btnDeletePOI;
    }

    public void setBtnDeletePOI(Button btnDeletePOI) {
        this.btnDeletePOI = btnDeletePOI;
    }
    
    public Button getBtnSavePOI() {
        return btnSavePOI;
    }

    public void setBtnSavePOI(Button btnSavePOI) {
        this.btnSavePOI = btnSavePOI;
    }

    public ComboBox<PoiType> getCbPoiType() {
        return cbPoiType;
    }

    public void setCbPoiType(ComboBox<PoiType> cbPoiType) {
        this.cbPoiType = cbPoiType;
    }
}
