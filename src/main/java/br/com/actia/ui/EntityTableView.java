package br.com.actia.ui;

import java.util.List;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


public class EntityTableView<T> extends TableView<T> {

    private final ObservableList<T> listEntity;

    public EntityTableView() {
        TableColumn<T, String> nameCol = new TableColumn<>("Name");
        nameCol.prefWidthProperty().bind(this.widthProperty().multiply(0.30));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<T, String> descriptionCol = new TableColumn<>("Description");
        descriptionCol.prefWidthProperty().bind(this.widthProperty().multiply(0.70));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));

        listEntity = FXCollections.observableArrayList();
        setItems(listEntity);
        
        getColumns().addAll(nameCol, descriptionCol);

    }

    public void reload(final List<T> listEntity) {
        this.listEntity.clear();
        Platform.runLater(() -> {
            for (T l: listEntity) {
                //T item = new T(l);
                EntityTableView.this.listEntity.add(l);
            }
        });
    }

    public T getSelectedItem() {
        T item = getSelectionModel().getSelectedItem();
        if (item != null) {
            return item;
        }
        return null;
    }
}
