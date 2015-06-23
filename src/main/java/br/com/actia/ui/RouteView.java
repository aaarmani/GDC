package br.com.actia.ui;

import br.com.actia.model.ListBanner;
import br.com.actia.model.ListBusStop;
import br.com.actia.model.ListRSS;
import br.com.actia.model.ListVideo;
import br.com.actia.model.Route;
import br.com.actia.validation.MaskTextField;
import java.util.ResourceBundle;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.List;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;

public class RouteView extends VBox {
    private final int MAX_HEIGHT = 600;
    // private final int VIEWER_SIZE = 100;
    
    private TextField tfId;
    private MaskTextField tfName;
    private MaskTextField tfDescription;
    private Button btnCancelRoute;
    private Button btnSaveRoute;
    private Button btnDeleteRoute;
    private ResourceBundle rb;
    private EntityTable<Route> table;
    private ComboBox<ListBanner> cbListBanners;

    private Button btnNewListBanner;
    private ComboBox<ListRSS> cbListRSSs;
    private Button btnNewListRSS;
    private ComboBox<ListVideo> cbListVideos;
    private Button btnNewListVideo;
    private ComboBox<ListBusStop> cbListBusStops;
    private Button btnNewListBusStop;
    
    public RouteView(ResourceBundle rb) {
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
        Label lblTitle = new Label(rb.getString("RTNewRoute"));
        lblTitle.getStyleClass().add("titleLabel");
        Separator separator = new Separator();
        return new VBox(lblTitle, separator);
    }

    private GridPane buildChooser() {
        tfId = new TextField();
        
        Label lblName = new Label(rb.getString("Name"));
        tfName = new MaskTextField();
        tfName.setMaskCompleteWord(MaskTextField.FIELD_NAME);
        Label lblDescription = new Label(rb.getString("Description"));
        tfDescription = new MaskTextField();
        tfDescription.setMaskCompleteWord(MaskTextField.FIELD_DESC);
        
        Label lblListBanner = new Label(rb.getString("Banner"));
        cbListBanners = new ComboBox<ListBanner>();
        cbListBanners.setMaxWidth(Double.MAX_VALUE);
        cbListBanners.setPromptText(rb.getString("ChooseList"));
        // TRATAR: new AutoCompleteComboBoxListener<ListBanner>(cbListBanners);
        
        Label lblNewListBanner = new Label(rb.getString("NewBannerList"));
        btnNewListBanner = new Button("+");
        btnNewListBanner.tooltipProperty().set(new Tooltip(rb.getString("CreateNewBannerList")));
        btnNewListBanner.getStyleClass().add("flatButton");
        btnNewListBanner.setId("newLstBannerRoute");
        
        Label lblListRSS = new Label(rb.getString("RSS"));
        cbListRSSs = new ComboBox<ListRSS>();
        cbListRSSs.setMaxWidth(Double.MAX_VALUE);
        cbListRSSs.setPromptText(rb.getString("ChooseList"));
        // TRATAR: new AutoCompleteComboBoxListener<ListRSS>(cbListRSSs);
        
        Label lblNewListRSS = new Label(rb.getString("NewRSSList"));
        btnNewListRSS = new Button("+");
        btnNewListRSS.tooltipProperty().set(new Tooltip(rb.getString("CreateNewRSSList")));
        btnNewListRSS.getStyleClass().add("flatButton");
        btnNewListRSS.setId("newLstRSSRoute");

        Label lblListVideo = new Label(rb.getString("Video"));
        cbListVideos = new ComboBox<ListVideo>();
        cbListVideos.setMaxWidth(Double.MAX_VALUE);
        cbListVideos.setPromptText(rb.getString("ChooseList"));
        // TRATAR: new AutoCompleteComboBoxListener<ListVideo>(cbListVideos);
        
        Label lblNewListVideo = new Label(rb.getString("NewVideoList"));
        btnNewListVideo = new Button("+");
        btnNewListVideo.tooltipProperty().set(new Tooltip(rb.getString("CreateNewVideoList")));
        btnNewListVideo.getStyleClass().add("flatButton");
        btnNewListVideo.setId("newLstVideoRoute");

        Label lblListBusStop = new Label(rb.getString("BusStop"));
        cbListBusStops = new ComboBox<ListBusStop>();
        cbListBusStops.setMaxWidth(Double.MAX_VALUE);
        cbListBusStops.setPromptText(rb.getString("ChooseList"));
        // TRATAR: new AutoCompleteComboBoxListener<ListBusStop>(cbListBusStops);
        
        Label lblNewListBusStop = new Label(rb.getString("NewBusStopList"));
        btnNewListBusStop = new Button("+");
        btnNewListBusStop.tooltipProperty().set(new Tooltip(rb.getString("CreateNewBusStopList")));
        btnNewListBusStop.getStyleClass().add("flatButton");
        btnNewListBusStop.setId("newLstBusStopRoute");

        GridFormBuilder grid = new GridFormBuilder();
        grid.addRowGenerics(lblName, tfName);
        grid.addRowGenerics(lblDescription, tfDescription);
        grid.addRowGenerics(lblListBanner, cbListBanners, btnNewListBanner, lblNewListBanner);
        grid.addRowGenerics(lblListRSS, cbListRSSs, btnNewListRSS, lblNewListRSS);
        grid.addRowGenerics(lblListVideo, cbListVideos, btnNewListVideo, lblNewListVideo);
        grid.addRowGenerics(lblListBusStop, cbListBusStops, btnNewListBusStop, lblNewListBusStop);
        
        return grid.build();
    }

    private HBox buildButtons() {
        btnCancelRoute = new Button(rb.getString("Cancel"));
        btnCancelRoute.setId("btnCancelRoute");
        btnCancelRoute.setCancelButton(true);
        btnCancelRoute.getStyleClass().add("flatButton");
        
        btnSaveRoute = new Button(rb.getString("Save"));
        btnSaveRoute.setId("btnSalveRoute");
        btnSaveRoute.setDefaultButton(true);
        btnSaveRoute.getStyleClass().add("flatButton");
        
        btnDeleteRoute = new Button(rb.getString("Delete"));
        btnDeleteRoute.setId("btnDeleteRoute");
        btnDeleteRoute.getStyleClass().add("flatButton");
        btnDeleteRoute.setVisible(false);
        
        HBox hbox = new HBox();
        hbox.getChildren().addAll(btnDeleteRoute, btnCancelRoute, btnSaveRoute);
        hbox.getStyleClass().add("buttonBar");
        hbox.setAlignment(Pos.CENTER_RIGHT);
        
        return hbox;
    }
    
    private VBox buildViewer() {
        // TO DO
        
        Separator separator = new Separator();
        
        VBox vbox = new VBox(separator/*, feedView, btnPlay*/);
        vbox.getStyleClass().add("viewPane");
        
        return vbox;
    }

    public Integer getRouteId() {
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
    
    public Button getBtnCancelRoute() {
        return btnCancelRoute;
    }

    public void setBtnCancelRoute(Button btnCancelRoute) {
        this.btnCancelRoute = btnCancelRoute;
    }

    public Button getBtnSaveRoute() {
        return btnSaveRoute;
    }

    public void setBtnSaveRoute(Button btnSaveRoute) {
        this.btnSaveRoute = btnSaveRoute;
    }

    public Button getBtnDeleteRoute() {
        return btnDeleteRoute;
    }

    public void setBtnDeleteRoute(Button btnDeleteRoute) {
        this.btnDeleteRoute = btnDeleteRoute;
    }
    
    public MaskTextField getTfDescription() {
        return tfDescription;
    }

    public void setTfDescription(MaskTextField tfDescription) {
        this.tfDescription = tfDescription;
    }

    public ComboBox<ListBanner> getCbListBanners() {
        return cbListBanners;
    }

    public void setCbListBanners(ComboBox<ListBanner> cbListBanners) {
        this.cbListBanners = cbListBanners;
    }

    public Button getBtnNewListBanner() {
        return btnNewListBanner;
    }

    public void setBtnNewListBanner(Button btnNewListBanner) {
        this.btnNewListBanner = btnNewListBanner;
    }

    public ComboBox<ListRSS> getCbListRSSs() {
        return cbListRSSs;
    }

    public void setCbListRSSs(ComboBox<ListRSS> cbListRSSs) {
        this.cbListRSSs = cbListRSSs;
    }

    public Button getBtnNewListRSS() {
        return btnNewListRSS;
    }

    public void setBtnNewListRSS(Button btnNewListRSS) {
        this.btnNewListRSS = btnNewListRSS;
    }

    public ComboBox<ListVideo> getCbListVideos() {
        return cbListVideos;
    }

    public void setCbListVideos(ComboBox<ListVideo> cbListVideos) {
        this.cbListVideos = cbListVideos;
    }

    public Button getBtnNewListVideo() {
        return btnNewListVideo;
    }

    public void setBtnNewListVideo(Button btnNewListVideo) {
        this.btnNewListVideo = btnNewListVideo;
    }

    public ComboBox<ListBusStop> getCbListBusStops() {
        return cbListBusStops;
    }

    public void setCbListBusStops(ComboBox<ListBusStop> cbListBusStops) {
        this.cbListBusStops = cbListBusStops;
    }

    public Button getBtnNewListBusStop() {
        return btnNewListBusStop;
    }

    public void setBtnNewListBusStop(Button btnNewListBusStop) {
        this.btnNewListBusStop = btnNewListBusStop;
    }
    
    public Route loadRouteFromPanel() {
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
        
        ListBanner listBanner = null;
        if(!cbListBanners.getSelectionModel().isEmpty()) {
            listBanner = cbListBanners.getSelectionModel().getSelectedItem();
        }
        
        ListRSS listRSS = null;
        if(!cbListRSSs.getSelectionModel().isEmpty()) {
            listRSS = cbListRSSs.getSelectionModel().getSelectedItem();
        }
        
        ListVideo listVideo = null;
        if(!cbListVideos.getSelectionModel().isEmpty()) {
            listVideo = cbListVideos.getSelectionModel().getSelectedItem();
        }
        
        ListBusStop listBusStop = null;
        if(!cbListBusStops.getSelectionModel().isEmpty()) {
            listBusStop = cbListBusStops.getSelectionModel().getSelectedItem();
        }
        
        return new Route(id, name, description, listBanner, listRSS, listVideo, listBusStop);
    }

    public void resetForm() {
        tfId.setText("");
        tfName.setText("");
        tfDescription.setText("");
        
        btnDeleteRoute.setVisible(false);
    }

    public void refreshTable(List<Route> listEntity) {
        table.reload(listEntity);   
    }
    
    public EntityTable getTable() {
        return table;
    }
    
    public void loadRouteToEdit(Route route) {
        if(route.getId() != null) {
            tfId.setText(route.getId().toString());
            btnDeleteRoute.setVisible(true);
        }
        tfName.setText(route.getName());
        tfDescription.setText(route.getDescription());
        
        getCbListBanners().getSelectionModel().select(route.getBanners());
        getCbListRSSs().getSelectionModel().select(route.getRSSs());
        getCbListVideos().getSelectionModel().select(route.getVideos());
        getCbListBusStops().getSelectionModel().select(route.getBusStops());
    }
    
}
