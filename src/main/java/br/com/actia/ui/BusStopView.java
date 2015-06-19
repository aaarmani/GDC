package br.com.actia.ui;

import br.com.actia.model.Banner;
import br.com.actia.model.BusStop;
import br.com.actia.model.ListPoi;
import br.com.actia.model.ListVideo;
import br.com.actia.validation.MaskTextField;
import java.util.ResourceBundle;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public class BusStopView extends VBox {
    private final int MAX_HEIGHT = 200;
    private TextField tfId;
    private MaskTextField tfName;
    private MaskTextField tfDescription;
    private MaskTextField tfLatitude;
    private MaskTextField tfLongitude;
    private MaskTextField tfRadius;
    private Button btnSaveBusStop;
    private Button btnCancelBusStop;
    private Button btnDeleteBusStop;
    private Button btnNewListPoi;
    private Button btnNewListVideo;
    private ComboBox<Banner> cbBanner;
    private ComboBox<ListPoi> cbListPois;
    private ComboBox<ListVideo> cbListVideos;
    private final ResourceBundle rb;
    
    public BusStopView(ResourceBundle rb) {
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
        Label lblTitle = new Label(rb.getString("NewBusStop"));
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
        
        Label lblDesc = new Label(rb.getString("Description"));
        tfDescription = new MaskTextField();
        tfDescription.setMinWidth(180);
        tfDescription.setMaxWidth(180);
        tfDescription.setMaskCompleteWord(MaskTextField.FIELD_DESC);
        
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
        
        Label lblRadius = new Label(rb.getString("Radius"));
        tfRadius = new MaskTextField();
        tfRadius.setMinWidth(180);
        tfRadius.setMaxWidth(180);
        tfRadius.setMaskCompleteWord(MaskTextField.FIELD_RAD);
        
        Label lblBanner = new Label(rb.getString("Banner"));
        cbBanner = new ComboBox<Banner>();
        cbBanner.setMaxWidth(Double.MAX_VALUE);
        cbBanner.setPromptText(rb.getString("ChooseBanner"));
        
        Label lblListVideo = new Label(rb.getString("Video"));
        cbListVideos = new ComboBox<ListVideo>();
        cbListVideos.setMaxWidth(Double.MAX_VALUE);
        cbListVideos.setPromptText(rb.getString("ChooseList"));
        
        Label lblNewListVideo = new Label(rb.getString("NewVideoList"));
        btnNewListVideo = new Button("+");
        btnNewListVideo.tooltipProperty().set(new Tooltip(rb.getString("CreateNewVideoList")));
        btnNewListVideo.getStyleClass().add("flatButton");
        btnNewListVideo.setId("newLstVideoBusStop");
        
        Label lblListPoi = new Label(rb.getString("Poi"));
        cbListPois = new ComboBox<ListPoi>();
        cbListPois.setMaxWidth(Double.MAX_VALUE);
        cbListPois.setPromptText(rb.getString("ChooseList"));
        
        Label lblNewListPoi = new Label(rb.getString("NewPoiList"));
        btnNewListPoi = new Button("+");
        btnNewListPoi.getStyleClass().add("flatButton");
        btnNewListPoi.tooltipProperty().set(new Tooltip(rb.getString("CreateNewPoiList")));
        btnNewListPoi.setId("newLstPoiBusStop");
        
        GridFormBuilder grid = new GridFormBuilder();
        grid.addRowGenerics(lblName, tfName, lblDesc, tfDescription, lblBanner, cbBanner);
        grid.addRowGenerics(lblLat, tfLatitude, lblLong, tfLongitude, lblRadius, tfRadius);
        grid.addRowGenerics(lblListVideo, cbListVideos, btnNewListVideo, lblNewListVideo);
        grid.addRowGenerics(lblListPoi, cbListPois, btnNewListPoi, lblNewListPoi);
        
        return grid.build();
    }

    private HBox buildButtons() {
        btnDeleteBusStop = new Button(rb.getString("Delete"));
        btnDeleteBusStop.setId("deleteBusStop");
        btnDeleteBusStop.getStyleClass().add("flatButton");
        
        btnCancelBusStop = new Button(rb.getString("Cancel"));
        btnCancelBusStop.setId("cancelBusStop");
        btnCancelBusStop.setCancelButton(true);
        btnCancelBusStop.getStyleClass().add("flatButton");
        
        btnSaveBusStop = new Button(rb.getString("Save"));
        btnSaveBusStop.setId("salveBusStop");
        btnSaveBusStop.setDefaultButton(true);
        btnSaveBusStop.getStyleClass().add("flatButton");
        
        HBox hbox = new HBox();
        hbox.getChildren().addAll(btnDeleteBusStop, btnCancelBusStop, btnSaveBusStop);
        hbox.getStyleClass().add("buttonBar");
        hbox.setAlignment(Pos.CENTER_RIGHT);
        
        return hbox;
    }
    
    public void resetForm() {
        tfName.setText(null);
        tfDescription.setText(null);
        tfLatitude.setText(null);
        tfLongitude.setText(null);
        tfRadius.setText(null);
    }
    
    public void setBusStop(BusStop busStop) {
        if(busStop != null) {
            if(busStop.getId() != null){
                tfId.setText(busStop.getId().toString());
            }
            
            tfLatitude.setText(Double.toString(busStop.getLatitude()));
            tfLongitude.setText(Double.toString(busStop.getLongitude()));
            tfRadius.setText(Float.toString(busStop.getRadius()));
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

    public MaskTextField getTfDescription() {
        return tfDescription;
    }

    public void setTfDescription(MaskTextField tfDescription) {
        this.tfDescription = tfDescription;
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

    public MaskTextField getTfRadius() {
        return tfRadius;
    }

    public void setTfRadius(MaskTextField tfRadius) {
        this.tfRadius = tfRadius;
    }

    public Button getBtnSaveBusStop() {
        return btnSaveBusStop;
    }

    public void setBtnSaveBusStop(Button btnSaveBusStop) {
        this.btnSaveBusStop = btnSaveBusStop;
    }

    public Button getBtnCancelBusStop() {
        return btnCancelBusStop;
    }

    public void setBtnCancelBusStop(Button btnCancelBusStop) {
        this.btnCancelBusStop = btnCancelBusStop;
    }

    public Button getBtnDeleteBusStop() {
        return btnDeleteBusStop;
    }

    public void setBtnDeleteBusStop(Button btnDeleteBusStop) {
        this.btnDeleteBusStop = btnDeleteBusStop;
    }

    public ComboBox<Banner> getCbBanner() {
        return cbBanner;
    }

    public void setCbBanner(ComboBox<Banner> cbBanner) {
        this.cbBanner = cbBanner;
    }

    public ComboBox<ListPoi> getCbListPois() {
        return cbListPois;
    }

    public void setCbListPois(ComboBox<ListPoi> cbListPois) {
        this.cbListPois = cbListPois;
    }

    public ComboBox<ListVideo> getCbListVideos() {
        return cbListVideos;
    }

    public void setCbListVideos(ComboBox<ListVideo> cbListVideos) {
        this.cbListVideos = cbListVideos;
    }

    public Button getBtnNewListPoi() {
        return btnNewListPoi;
    }

    public void setBtnNewListPoi(Button btnNewListPoi) {
        this.btnNewListPoi = btnNewListPoi;
    }

    public Button getBtnNewListVideo() {
        return btnNewListVideo;
    }

    public void setBtnNewListVideo(Button btnNewListVideo) {
        this.btnNewListVideo = btnNewListVideo;
    }

    public BusStop loadBusStopFromPanel() {
        Integer id = null;
        if(!tfId.getText().trim().isEmpty()) {
            id = Integer.valueOf(tfId.getText());
        }
        
        String name = null;
        if(!tfName.getText().trim().isEmpty()) {
            name = tfName.getText();
        }
        
        String description = null;
        if(!tfDescription.getText().trim().isEmpty()) {
            description = tfDescription.getText();
        }
        
        Double latitude = null;
        if(!tfLatitude.getText().trim().isEmpty()) {
            latitude = Double.valueOf(tfLatitude.getText());
        }
        
        Double longitude = null;
        if(!tfLongitude.getText().trim().isEmpty()) {
            longitude = Double.valueOf(tfLongitude.getText());
        }
        
        float radius = 0;
        if(!tfRadius.getText().trim().isEmpty()) {
            radius = Float.valueOf(tfRadius.getText());
        }
    
        Banner banner = null;
        if(!cbBanner.getSelectionModel().isEmpty()) {
            banner = cbBanner.getSelectionModel().getSelectedItem();
        }
    
        ListPoi listPoi = null;
        if(!cbListPois.getSelectionModel().isEmpty()) {
            listPoi = cbListPois.getSelectionModel().getSelectedItem();
        }
        
        ListVideo listVideo = null;
        if(!cbListVideos.getSelectionModel().isEmpty()) {
            listVideo = cbListVideos.getSelectionModel().getSelectedItem();
        }
        
        return new BusStop(id, name, description, latitude, longitude, radius, banner, listPoi, listVideo);
    }
    
}
