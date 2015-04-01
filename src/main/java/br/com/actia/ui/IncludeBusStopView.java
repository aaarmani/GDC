package br.com.actia.ui;

import br.com.actia.model.Banner;
import br.com.actia.model.BusStop;
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
public class IncludeBusStopView extends VBox {
    private final int MAX_HEIGHT = 200;
    private TextField tfId;
    private TextField tfName;
    private TextField tfDescription;
    private TextField tfLatitude;
    private TextField tfLongitude;
    private TextField tfRadius;
    private Button btnSaveBusStop;
    private Button btnCancelBusStop;
    private ComboBox<Banner> cbBanner;
    private ComboBox<Object> cbListPois;
    private ComboBox<Object> cbListVideos;
    private Button btnNewListPoi;
    private Button btnNewListVideo;
        
    public IncludeBusStopView() {
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
        Label lblTitle = new Label("Novo Ponto de Parada");
        Separator separator = new Separator();

        VBox vbox = new VBox();
        vbox.getChildren().addAll(lblTitle, separator);
        return vbox;
    }

    private GridPane buildInputs() {
        tfId = new TextField();
        
        Label lblName = new Label("nome");
        tfName = new TextField();
        tfName.setMinWidth(180);
        tfName.setMaxWidth(180);
        
        Label lblDesc = new Label("descricao");
        tfDescription = new TextField();
        tfDescription.setMinWidth(180);
        tfDescription.setMaxWidth(180);
        
        Label lblLat = new Label("latitude");
        tfLatitude = new TextField();
        tfLatitude.setMinWidth(180);
        tfLatitude.setMaxWidth(180);
        
        Label lblLong = new Label("longitude");
        tfLongitude = new TextField();
        tfLongitude.setMinWidth(180);
        tfLongitude.setMaxWidth(180);
        
        Label lblRadius = new Label("raio");
        tfRadius = new TextField();
        tfRadius.setMinWidth(180);
        tfRadius.setMaxWidth(180);
        
        Label lblBanner = new Label ("banner");
        cbBanner = new ComboBox<>();
        
        Label lblListVideo = new Label ("videos");
        cbListVideos = new ComboBox<>();
        Label lblNewListVideo = new Label ("Nova Lista");
        
        btnNewListVideo = new Button("+");
        btnNewListVideo.tooltipProperty().set(new Tooltip("Criar nova lista de vídeos"));
        btnNewListVideo.getStyleClass().add("flatButton");
        
        Label lblListPoi = new Label ("pois");
        cbListPois = new ComboBox<>();
        
        Label lblNewListPoi = new Label ("Nova Lista");
        btnNewListPoi = new Button("+");
        btnNewListPoi.getStyleClass().add("flatButton");
        btnNewListPoi.tooltipProperty().set(new Tooltip("Criar nova lista de POI"));
        
        GridFormBuilder grid = new GridFormBuilder();
        grid.addRowGenerics(lblName, tfName, lblDesc, tfDescription);
        grid.addRowGenerics(lblLat, tfLatitude, lblLong, tfLongitude, lblRadius, tfRadius);
        grid.addRowGenerics(lblBanner, cbBanner, lblListVideo, cbListVideos, lblNewListVideo, btnNewListVideo);
        grid.addRowGenerics(lblListPoi, cbListPois, lblNewListPoi, btnNewListPoi);
        
        return grid.build();
    }

    private HBox buildButtons() {
        btnCancelBusStop = new Button("Cancelar");
        btnCancelBusStop.setId("cancelBusStop");
        btnCancelBusStop.setDefaultButton(true);
        btnCancelBusStop.getStyleClass().add("flatButton");
        
        btnSaveBusStop = new Button("Salvar");
        btnSaveBusStop.setId("salveBusStop");
        btnSaveBusStop.setCancelButton(true);
        btnSaveBusStop.getStyleClass().add("flatButton");
        
        HBox hbox = new HBox();
        hbox.getChildren().addAll(btnCancelBusStop, btnSaveBusStop);
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
        resetForm();
        if(busStop != null) {
            if(busStop.getId() != null)
                tfId.setText(busStop.getId().toString());
            tfName.setText(busStop.getName());
            tfDescription.setText(busStop.getDescription());
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

    public TextField getTfName() {
        return tfName;
    }

    public void setTfName(TextField tfName) {
        this.tfName = tfName;
    }

    public TextField getTfDescription() {
        return tfDescription;
    }

    public void setTfDescription(TextField tfDescription) {
        this.tfDescription = tfDescription;
    }

    public TextField getTfLatitude() {
        return tfLatitude;
    }

    public void setTfLatitude(TextField tfLatitude) {
        this.tfLatitude = tfLatitude;
    }

    public TextField getTfLongitude() {
        return tfLongitude;
    }

    public void setTfLongitude(TextField tfLongitude) {
        this.tfLongitude = tfLongitude;
    }

    public TextField getTfRadius() {
        return tfRadius;
    }

    public void setTfRadius(TextField tfRadius) {
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

    public ComboBox<Banner> getCbBanner() {
        return cbBanner;
    }

    public void setCbBanner(ComboBox<Banner> cbBanner) {
        this.cbBanner = cbBanner;
    }

    public ComboBox<Object> getCbListPois() {
        return cbListPois;
    }

    public void setCbListPois(ComboBox<Object> cbListPois) {
        this.cbListPois = cbListPois;
    }

    public ComboBox<Object> getCbListVideos() {
        return cbListVideos;
    }

    public void setCbListVideos(ComboBox<Object> cbListVideos) {
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
    
}
