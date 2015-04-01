package br.com.actia.controller;

import br.com.actia.action.AbstractAction;
import br.com.actia.javascript.object.LatLong;
import br.com.actia.model.BusStop;
import br.com.actia.ui.IncludeBusStopView;
import br.com.actia.validation.BusStopValidator;
import br.com.actia.validation.Validator;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public class IncludeBusStopController extends PersistenceController {
    private final Pane parentPane;
    private final IncludeBusStopView view;
    private final Validator<BusStop> validador = new BusStopValidator();
    private BusStop busStop = null;
    private boolean isVisible = false;
    
    public IncludeBusStopController(AbstractController parent, Pane pane) {
        super(parent);
        loadPersistenceContext(((PersistenceController) getParentController()).getPersistenceContext());
        
        this.parentPane = pane;
        this.view = new IncludeBusStopView();
        this.busStop = new BusStop();
        //this.view.resetForm();
        
        registerAction(this.view.getBtnCancelBusStop(), new AbstractAction() {
            @Override
            protected void action() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        closeView();
                    }
                });
            }
        });
        
        parentPane.getChildren().add(view);
        StackPane.setAlignment(view, Pos.BOTTOM_CENTER);
        this.view.resetForm();
        closeView();
    }
    
    public void showView() {
        this.view.setVisible(true);
    }
    
    public void closeView() {
        this.view.setVisible(false);
    }

    void setPosition(LatLong position) {
        if(position != null) {
            this.busStop.setLatitude(position.getLatitude());
            this.busStop.setLongitude(position.getLongitude());
            this.view.setBusStop(this.busStop);
        }
    }
}
