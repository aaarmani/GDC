package br.com.actia.ui;

import java.util.ResourceBundle;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public class UserOptionsView extends VBox {
    private final ResourceBundle rb;
    private Button btnLogInOut;
    private Button btnUserEdit;
    private Button btnNewCompany;
    private Label lblUserName;
    private FontAwesome fontAwesome;
    
    public UserOptionsView(ResourceBundle rb) {
        this.rb = rb;
        this.getStyleClass().add("userMenu");
        
        fontAwesome = new FontAwesome();
        
         VBox head = buildHead();
         VBox options = buildOptions();
         VBox footer = buildFooter();
        this.getChildren().addAll(head, options, footer);
    }
    
    private VBox buildHead() {
        lblUserName = new Label(rb.getString("USER_TITLE"));
        lblUserName.getStyleClass().add("textWhite");
        Separator separator = new Separator();
        
        VBox vbox = new VBox();
        vbox.getChildren().addAll(lblUserName);
        
        return vbox;
    }
    
    private VBox buildOptions() {
        btnUserEdit = newMenuItem(rb.getString("USER_EDIT"), "USER");
        btnNewCompany = newMenuItem(rb.getString("USER_COMPANY"), "SUITCASE");
        
        VBox vbox = new VBox();
        vbox.getChildren().addAll(btnUserEdit, btnNewCompany);
        return vbox;
    }
    
    private VBox buildFooter() {
        Separator separator = new Separator();
        btnLogInOut = newMenuItem(rb.getString("USER_LOGOUT"), "SIGN_OUT");
        
        VBox vbox = new VBox();
        vbox.getChildren().addAll(separator, btnLogInOut);
        return vbox;
    }
    
    private Button newMenuItem(String item, String strImage) {
        Button btn = new Button(item, fontAwesome.create(strImage));
        btn.setMinSize(150, 40);
        btn.getStyleClass().add("flatButton");
        btn.setId(item);
        btn.setAlignment(Pos.CENTER_LEFT);
        return btn;
    }

    public Button getBtnLogInOut() {
        return btnLogInOut;
    }

    public void setBtnLogInOut(Button btnLogInOut) {
        this.btnLogInOut = btnLogInOut;
    }

    public Button getBtnUserEdit() {
        return btnUserEdit;
    }

    public void setBtnUserEdit(Button btnUserEdit) {
        this.btnUserEdit = btnUserEdit;
    }

    public Button getBtnNewCompany() {
        return btnNewCompany;
    }

    public void setBtnNewCompany(Button btnNewCompany) {
        this.btnNewCompany = btnNewCompany;
    }

    public Label getLblUserName() {
        return lblUserName;
    }

    public void setLblUserName(Label lblUserName) {
        this.lblUserName = lblUserName;
    }
    
    
}
