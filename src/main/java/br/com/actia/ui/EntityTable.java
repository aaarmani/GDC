package br.com.actia.ui;

import java.util.List;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class EntityTable<T> extends VBox {
    
    private final EntityTableView table;
    
    public EntityTable(){
        table = new EntityTableView();
        this.getChildren().addAll(table);
        this.setPadding(new Insets(10, 0, 0, 10));//css
    }
    
    public void reload(List<T> listEntity) {
        table.reload(listEntity);
    }

    public void setMouseEvent(EventHandler<MouseEvent> event) {
        table.setOnMouseClicked(event);
    }
    
    public T getEntitySelected() {
        return (T) table.getSelectedItem();
    }
}
