package br.com.actia.ui;

import br.com.actia.model.BusStop;
import br.com.actia.model.ListBusStop;
import br.com.actia.validation.MaskTextField;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
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
public class BusStopListView extends VBox {
    private final int MAX_HEIGHT = 200;
    private final ResourceBundle rb;
    
    private TextField tfId;
    private MaskTextField tfName;
    private HBox HbLabels;
    private Button btnCancel;
    private Button btnDelete;
    private Button btnSave;

    public BusStopListView(ResourceBundle rb) {
        this.rb = rb;
        this.setMaxHeight(MAX_HEIGHT);
        this.getStyleClass().add("transparentPanel");
        
        initializeComponents();
    }

    private void initializeComponents() {
        VBox head = buildHead();
        GridPane inputs = buildInputs();
        HBox buttons = buildButtons();
        
        this.getChildren().addAll(head, inputs, HbLabels, buttons);
    }
    
    private VBox buildHead() {
        Label lblTitle = new Label(rb.getString("NewBusStopList"));
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
        
        Label lblBusStops = new Label(rb.getString("BusStops"));
        HbLabels = buildHbLabels();
        
        GridFormBuilder grid = new GridFormBuilder();
        grid.addRowGenerics(lblName, tfName);
        grid.addRowGenerics(lblBusStops, HbLabels);

        return grid.build();
    }

    private HBox buildHbLabels() {
        HBox hb = new HBox();
        hb.setSpacing(5);
        return hb;
    }
    
    private HBox buildButtons() {
        btnDelete = new Button(rb.getString("Delete"));
        btnDelete.setId("deleteBSL");
        btnDelete.getStyleClass().add("flatButton");
                
        btnCancel = new Button(rb.getString("Cancel"));
        btnCancel.setId("cancelBSL");
        btnCancel.setCancelButton(true);
        btnCancel.getStyleClass().add("flatButton");
        
        btnSave = new Button(rb.getString("Save"));
        btnSave.setId("salveBSL");
        btnSave.setDefaultButton(true);
        btnSave.getStyleClass().add("flatButton");
        
        HBox hbox = new HBox();
        hbox.getChildren().addAll(btnDelete, btnCancel, btnSave);
        hbox.getStyleClass().add("buttonBar");
        hbox.setAlignment(Pos.CENTER_RIGHT);
        
        return hbox;
    }
    
    public void resetForm() {
        tfId.setText("");
        tfName.setText("");
    }
    
    public ListBusStop loadBusStopFromPanel() {
        Integer id = null;
        if(!tfId.getText().trim().isEmpty()) {
            id = Integer.valueOf(tfId.getText());
        }
        
        String name = null;
        if (!tfName.getText().trim().isEmpty()) {
            name = tfName.getText().trim();
        }
        
        List<BusStop> lstBusStop = new ArrayList<>();
        
        return new ListBusStop(id, name, lstBusStop);
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

    public Button getBtnCancel() {
        return btnCancel;
    }

    public void setBtnCancel(Button btnCancel) {
        this.btnCancel = btnCancel;
    }

    public Button getBtnDelete() {
        return btnDelete;
    }

    public void setBtnDelete(Button btnDelete) {
        this.btnDelete = btnDelete;
    }

    public Button getBtnSave() {
        return btnSave;
    }

    public void setBtnSave(Button btnSave) {
        this.btnSave = btnSave;
    }

    public HBox getHbLabels() {
        return HbLabels;
    }

    public void setHbLabels(HBox HbLabels) {
        this.HbLabels = HbLabels;
    }
}
