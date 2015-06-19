package br.com.actia.ui;

import br.com.actia.model.RSS;
import br.com.actia.model.Route;
import br.com.actia.validation.MaskTextField;
import java.io.File;
import java.util.ArrayList;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;

public class FileGeneratorView extends VBox {
    private final int MAX_HEIGHT = 600;
    
    private ComboBox<Route> cbRoute;
    private Button btnNewRoute;
    private Button btnCancelFileGenerator;
    private Button btnGenerateFileGenerator;
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
        HBox buttons = buildButtons();
        
        this.getChildren().addAll(head, inputs, buttons);
    }

    private VBox buildHead() {
        Label lblTitle = new Label(rb.getString("FGGenerateFiles"));
        lblTitle.getStyleClass().add("titleLabel");
        Separator separator = new Separator();
        return new VBox(lblTitle, separator);
    }

    private GridPane buildInputs() {
        Label lblRoute = new Label(rb.getString("Route"));
        cbRoute = new ComboBox<Route>();
        cbRoute.setPromptText(rb.getString("ChooseRoute"));
        
        Label lblNewRoute = new Label(rb.getString("NewRoute"));
        btnNewRoute = new Button("+");
        btnNewRoute.tooltipProperty().set(new Tooltip(rb.getString("CreateNewVideoList")));
        btnNewRoute.getStyleClass().add("flatButton");
        btnNewRoute.setId("newRouteFileGenerator");
        
        GridFormBuilder grid = new GridFormBuilder();
        grid.addRowGenerics(lblRoute, cbRoute, btnNewRoute, lblNewRoute);
        
        return grid.build();
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
    
    public Button getBtnNewRoute() {
        return btnNewRoute;
    }

    public void setBtnNewRoute(Button btnNewRoute) {
        this.btnNewRoute = btnNewRoute;
    }

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
    
    public ComboBox<Route> getCbRoute() {
        return cbRoute;
    }

    public void setCbRoute(ComboBox<Route> cbRoute) {
        this.cbRoute = cbRoute;
    }
    
    public void resetForm() {
        // TO DO
    }
    
    public ArrayList<Route> loadRoutesToGenerate() {
        ArrayList<Route> routesToGenerate = new ArrayList<Route>();
        
        Route route = null;
        if(!cbRoute.getSelectionModel().isEmpty()) {
            route = cbRoute.getSelectionModel().getSelectedItem();
        }
        
        routesToGenerate.add(route);
        
        return routesToGenerate;
    }
}
