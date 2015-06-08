package br.com.actia.ui;

import br.com.actia.model.ListBusStop;
import java.util.ResourceBundle;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public class BusStopList2View extends VBox {
    private final int MAX_HEIGHT = 400;
    private final int MAX_WIDTH = 200;
    private final ResourceBundle rb;

    private TextField tfFilter;
    private ListView<ListBusStop> lstvBSList;
    
    public BusStopList2View(ResourceBundle rb) {
        this.rb = rb;
        this.setMaxHeight(MAX_HEIGHT);
        this.setMaxWidth(MAX_WIDTH);
        this.getStyleClass().add("transparentPanel");
        
        initializeComponents();
    }

    private void initializeComponents() {
        
        VBox head = buildHead();
        lstvBSList = new ListView<>();
        lstvBSList.getStyleClass().add("transparentPanel");
        this.getChildren().addAll(head, lstvBSList);
    }

    private VBox buildHead() {
        Label lb = new Label(rb.getString("Filter"));
        tfFilter = new TextField();
        Separator separator = new Separator();

        VBox vbox = new VBox();
        vbox.getChildren().addAll(lb, tfFilter, separator);
        vbox.setSpacing(5);
        return vbox;
    }

    public TextField getTfFilter() {
        return tfFilter;
    }

    public void setTfFilter(TextField tfFilter) {
        this.tfFilter = tfFilter;
    }

    public ListView<ListBusStop> getLstvBSList() {
        return lstvBSList;
    }

    public void setLstvBSList(ListView<ListBusStop> lstvBSList) {
        this.lstvBSList = lstvBSList;
    }
}
