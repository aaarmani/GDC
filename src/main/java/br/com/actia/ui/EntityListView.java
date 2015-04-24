package br.com.actia.ui;

import br.com.actia.model.ListPoi;
import br.com.actia.model.Poi;
import java.util.ArrayList;
import java.util.Collection;
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
import org.controlsfx.control.ListSelectionView;

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public class EntityListView<T, K> extends VBox {
    private final ResourceBundle rb;
    private ListSelectionView<T> lsvEntity;
    private Button btnCancel;
    private Button btnSave;
    private Button btnNewEntity;
    private TextField tfId;
    private TextField tfName;
    private TextField tfDescription;
    private EntityTable table;
    
    public EntityListView(ResourceBundle rb) {
        this.rb = rb;
        this.getStyleClass().add("whitePanel");
        initializeComponents();
    }

    private void initializeComponents() {
        VBox vbHead = buildHead();
        GridPane gpInputs = buildInputs();
        lsvEntity = buildList();
        HBox hbButtons = buildButtons();
        table = new EntityTable();
        this.getChildren().addAll(vbHead, gpInputs, lsvEntity, hbButtons, table);
    }
    
    private VBox buildHead() {
        Label lblTitle = new Label(rb.getString("LSTTitle"));
        lblTitle.getStyleClass().add("titleLabel");
        Separator separator = new Separator();
        return new VBox(lblTitle, separator);
    }

    private GridPane buildInputs() {
        tfId = new TextField();

        Label lblName = new Label(rb.getString("Name"));
        tfName = new TextField();
        tfName.setMinWidth(180);
        tfName.setMaxWidth(180);

        Label lblDesc = new Label(rb.getString("Description"));
        tfDescription = new TextField();
        tfDescription.setMinWidth(180);
        tfDescription.setMaxWidth(180);
        
        btnNewEntity = new Button("+");
        btnNewEntity.setId("btnNewEntity");
        btnNewEntity.getStyleClass().add("flatButton");
        
        GridFormBuilder grid = new GridFormBuilder();
        grid.addRowGenerics(lblName, tfName, lblDesc, tfDescription, btnNewEntity, null);
        
        return grid.build();
    }
    
    private ListSelectionView<T> buildList() {
        ListSelectionView<T> lsv = new ListSelectionView<T>();
        return lsv;
    }

    private HBox buildButtons() {
        btnCancel = new Button(rb.getString("Cancel"));
        btnCancel.setId("btnCancelList");
        btnCancel.setCancelButton(true);
        btnCancel.getStyleClass().add("flatButton");
        
        btnSave = new Button(rb.getString("Save"));
        btnSave.setId("btnSalveList");
        btnSave.setDefaultButton(true);
        btnSave.getStyleClass().add("flatButton");
        
        HBox hbox = new HBox();
        hbox.getChildren().addAll(btnCancel, btnSave);
        hbox.getStyleClass().add("buttonBar");
        hbox.setAlignment(Pos.CENTER_RIGHT);
        
        return hbox;
    }

    public void resetForm(Collection<T> listItemsAll) {
        tfId.setText("");
        tfName.setText("");
        tfDescription.setText("");
        
        this.lsvEntity.getSourceItems().clear();
        this.lsvEntity.getSourceItems().addAll((Collection<T>)listItemsAll);
        
        this.lsvEntity.getTargetItems().clear();
    }

    public ListSelectionView<T> getLsvEntity() {
        return lsvEntity;
    }

    public void setLsvEntity(ListSelectionView<T> lsvEntity) {
        this.lsvEntity = lsvEntity;
    }

    public Button getBtnCancel() {
        return btnCancel;
    }

    public void setBtnCancel(Button btnCancel) {
        this.btnCancel = btnCancel;
    }

    public Button getBtnSave() {
        return btnSave;
    }

    public void setBtnSave(Button btnSave) {
        this.btnSave = btnSave;
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

    public Button getBtnNewEntity() {
        return btnNewEntity;
    }

    public void setBtnNewEntity(Button btnNewEntity) {
        this.btnNewEntity = btnNewEntity;
    }

    public EntityTable getTable() {
        return table;
    }

    public void setTable(EntityTable table) {
        this.table = table;
    }
    
    public void refreshTable(List<K> list) {
        table.reload(new ArrayList<>(list));        
    }
}
