package br.com.actia.controller;

import br.com.actia.ui.UserOptionsView;
import java.util.ResourceBundle;
import javafx.scene.Node;
import org.controlsfx.control.PopOver;

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public class UserOptionsController extends AbstractController {
    private final ResourceBundle rb;
    private final Node ownerNode;
    private final UserOptionsView view;
    private final PopOver popOverView;
    

    public UserOptionsController(AbstractController parent, Node ownerNode, ResourceBundle rb) {
        super(parent);
        this.rb = rb;
        this.ownerNode = ownerNode;
        this.view = new UserOptionsView(rb);
        this.popOverView = new PopOver(view);
        this.popOverView.setArrowLocation(PopOver.ArrowLocation.RIGHT_TOP);
    }
    
    public void showView() {
        if(!popOverView.isShowing())
            popOverView.show(ownerNode);
        else
            popOverView.hide();
    }
    
    public void closeView() {
        popOverView.hide();
    }
    
    @Override
    public void cleanUp() {
        closeView();
    }
}
