package br.com.actia.ui;

import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

/**
 * Constrói um <code>GridPane</code> com capacidade de organizar os elementos de forma tabular.
 *
 * <p>Cada linha é organizada com um <code>Label</code> na coluna1 e um <code>TextField</code> na coluna2.
 * 
 * @author YaW Tecnologia
 */
public class GridFormBuilder {
    
    private GridPane pane;
    
    private int row = 0;
    
    public GridFormBuilder() {
        this.pane = new GridPane();
        this.pane.getStyleClass().add("gridForm");
    }
    
    /*public GridFormBuilder addRow(Label label, TextField tf) {
        this.pane.add(label, 0, row);
        this.pane.add(tf, 1, row++);
        GridPane.setHalignment(label, HPos.RIGHT);
        return this;
    }
    
    public GridFormBuilder addRow(Label node1, TextField tf, Label label2, TextField tf2) {
        this.pane.add(node1, 0, row);
        this.pane.add(tf, 1, row);
        this.pane.add(label2, 2, row);
        this.pane.add(tf2, 3, row++);
        GridPane.setHalignment(node1, HPos.RIGHT);
        return this;
    }
    
    public GridFormBuilder addRow(Label label, TextField tf, Label label2, TextField tf2, Label label3, TextField tf3) {
        this.pane.add(label, 0, row);
        this.pane.add(tf, 1, row);
        this.pane.add(label2, 2, row);
        this.pane.add(tf2, 3, row);
        this.pane.add(label3, 4, row);
        this.pane.add(tf3, 5, row++);
        GridPane.setHalignment(label, HPos.RIGHT);
        return this;
    }*/
    
    public GridFormBuilder addRowGenerics(Node nd1, Node nd2) {
        this.pane.add(nd1, 0, row);
        this.pane.add(nd2, 1, row++);
        GridPane.setHalignment(nd1, HPos.RIGHT);
        return this;
    }
    
    public GridFormBuilder addRowGenerics(Node nd1, Node nd2, Node nd3, Node nd4) {
        this.pane.add(nd1, 0, row);
        this.pane.add(nd2, 1, row);
        this.pane.add(nd3, 2, row);
        this.pane.add(nd4, 3, row++);
        GridPane.setHalignment(nd1, HPos.RIGHT);
        GridPane.setHalignment(nd3, HPos.RIGHT);
        return this;
    }
    
    public GridFormBuilder addRowGenerics(Node nd1, Node nd2, Node nd3, Node nd4, Node nd5, Node nd6) {
        this.pane.add(nd1, 0, row);
        this.pane.add(nd2, 1, row);
        this.pane.add(nd3, 2, row);
        this.pane.add(nd4, 3, row);
        this.pane.add(nd5, 4, row);
        this.pane.add(nd6, 5, row++);
        GridPane.setHalignment(nd1, HPos.RIGHT);
        GridPane.setHalignment(nd3, HPos.RIGHT);
        GridPane.setHalignment(nd5, HPos.RIGHT);
        return this;
    }

    public GridPane build() {
        return this.pane;
    }
}
