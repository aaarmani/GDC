package br.com.actia.ui;

import br.com.actia.model.Route;
import java.io.File;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import org.controlsfx.control.ListSelectionView;

public class FileGeneratorView extends VBox {
    private final int MAX_HEIGHT = 600;
    private final int MAX_HEIGHT_LIST_VIEW = 50;
    
    private TextField tfDirectoryPath;
    private Button btnChooseDirectory;
    
    //private ComboBox<Route> cbRoute;
    //private Button btnAddRoute;
    private Button btnCancelFileGenerator;
    private Button btnGenerateFileGenerator;
    //private ListView<Route> lstvRoutesToGenerate;

    private ListSelectionView<Route> lsvRoutesToGenerate;
    
    private ResourceBundle rb;
    
    public FileGeneratorView(ResourceBundle rb) {
        this.rb = rb;
        this.setMaxHeight(MAX_HEIGHT);
        this.getStyleClass().add("whitePanel");
        initializeComponents();
    }

    private void initializeComponents() {
        VBox head = buildHead();
        GridPane inputs = buildInputs();
        // lstvRoutesToGenerate = buildListViewRoutesToGenerate();
        lsvRoutesToGenerate = buildListRoutesToGenerate();
        HBox buttons = buildButtons();
        
        this.getChildren().addAll(head, inputs, lsvRoutesToGenerate, buttons);
    }

    private VBox buildHead() {
        Label lblTitle = new Label(rb.getString("FGGenerateFiles"));
        lblTitle.getStyleClass().add("titleLabel");
        Separator separator = new Separator();
        return new VBox(lblTitle, separator);
    }

    private GridPane buildInputs() {
        Label lblDirectoryPath = new Label(rb.getString("FGChooseDirectory"));
        btnChooseDirectory = new Button(rb.getString("Search"));
        btnChooseDirectory.setId("btnChooseDirectory");
        btnChooseDirectory.getStyleClass().add("flatButton");
        
        tfDirectoryPath = new TextField();
        
        /*
        Label lblRoute = new Label(rb.getString("Route"));
        cbRoute = new ComboBox<Route>();
        cbRoute.setPromptText(rb.getString("ChooseRoute"));
        // TRATAR: new AutoCompleteComboBoxListener<Route>(cbRoute);
                
        Label lblAddRoute = new Label(rb.getString("AddRoute"));
        btnAddRoute = new Button("+");
        btnAddRoute.tooltipProperty().set(new Tooltip(rb.getString("AddRouteToList")));
        btnAddRoute.getStyleClass().add("flatButton");
        btnAddRoute.setId("addRouteFileGenerator");
                
        Label lblRoutesSelected = new Label(rb.getString("SelectedRoutes"));
        */
        
        GridFormBuilder grid = new GridFormBuilder();
        grid.addRowGenerics(lblDirectoryPath, tfDirectoryPath, btnChooseDirectory, null);
        // grid.addRowGenerics(lblRoute, cbRoute, btnAddRoute, lblAddRoute);
        // grid.addRowGenerics(lblRoutesSelected, null);
        
        return grid.build();
    }

    /*
    private ListView<Route> buildListViewRoutesToGenerate() {
        ListView<Route> lstv = new ListView<>();
        lstv.setId("ListRoutesToGenerate");
        lstv.setOrientation(Orientation.HORIZONTAL);
        lstv.setMaxHeight(MAX_HEIGHT_LIST_VIEW);
        
        return lstv;
    }
    */
    
    private ListSelectionView<Route> buildListRoutesToGenerate() {
        ListSelectionView<Route> lsv = new ListSelectionView<Route>();
        lsv.setSourceHeader(new Label(rb.getString("AvailableRoutes")));
        lsv.setTargetHeader(new Label(rb.getString("SelectedRoutes")));
        
        return lsv;
    }
    
    private HBox buildButtons() {
        btnGenerateFileGenerator = new Button(rb.getString("Generate"));
        btnGenerateFileGenerator.setId("btnGenerateFileGenerator");
        btnGenerateFileGenerator.getStyleClass().add("flatButton");
        
        btnCancelFileGenerator = new Button(rb.getString("Cancel"));
        btnCancelFileGenerator.setId("btnCancelFileGenerator");
        btnCancelFileGenerator.setCancelButton(true);
        btnCancelFileGenerator.getStyleClass().add("flatButton");
        
        HBox hbox = new HBox();
        hbox.getChildren().addAll(btnGenerateFileGenerator, btnCancelFileGenerator);
        hbox.getStyleClass().add("buttonBar");
        hbox.setAlignment(Pos.CENTER_RIGHT);
        
        return hbox;
    }
    
    /*
    public Button getBtnAddRoute() {
        return btnAddRoute;
    }

    public void setBtnAddRoute(Button btnNewRoute) {
        this.btnAddRoute = btnNewRoute;
    }
    */
    
    public Button getBtnCancelFileGenerator() {
        return btnCancelFileGenerator;
    }

    public void setBtnCancelFileGenerator(Button btnCancelFileGenerator) {
        this.btnCancelFileGenerator = btnCancelFileGenerator;
    }
    
    public Button getBtnGenerateFileGenerator() {
        return btnGenerateFileGenerator;
    }

    public void setBtnGenerateFileGenerator(Button btnGenerateFileGenerator) {
        this.btnGenerateFileGenerator = btnGenerateFileGenerator;
    }
    
    public Button getBtnChooseDirectory() {
        return btnChooseDirectory;
    }

    public void setBtnChooseDirectory(Button btnChooseDirectory) {
        this.btnChooseDirectory = btnChooseDirectory;
    }
    
    public TextField getTfDirectoryPath() {
        return tfDirectoryPath;
    }

    public void setTfDirectoryPath(TextField tfDirectoryPath) {
        this.tfDirectoryPath = tfDirectoryPath;
    }
    
    /*
    public ComboBox<Route> getCbRoute() {
        return cbRoute;
    }

    public void setCbRoute(ComboBox<Route> cbRoute) {
        this.cbRoute = cbRoute;
    }
    
    public ListView<Route> getLstvRoutesToGenerate() {
        return lstvRoutesToGenerate;
    }
    
    public void setLstvRoutesToGenerate(ListView<Route> lstvRoutesToGenerate) {
        this.lstvRoutesToGenerate = lstvRoutesToGenerate;
    }

    public ListSelectionView<Route> getLsvEntity() {
        return lsvEntity;
    }
    */
    
    public ListSelectionView<Route> getLsvRoutesToGenerate(){
        return this.lsvRoutesToGenerate;
    }
    
//    public VBox transparentPane() {
//        VBox transparentPane = new VBox();
//        
//        transparentPane.setPrefHeight(1000);
//        transparentPane.setPrefWidth(1000);
//        
//        // transparentPane.setMaxHeight(200);
//        transparentPane.getStyleClass().add("transparentPanel");
//        
//        return transparentPane;
//    }
}
