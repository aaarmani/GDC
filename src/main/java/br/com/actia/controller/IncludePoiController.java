package br.com.actia.controller;

import br.com.actia.action.AbstractAction;
import br.com.actia.action.BooleanExpression;
import br.com.actia.action.ConditionalAction;
import br.com.actia.action.TransactionalAction;
import br.com.actia.dao.PoiDAO;
import br.com.actia.dao.PoiDAOJPA;
import br.com.actia.dao.PoiTypeDAOJPA;
import br.com.actia.event.IncludePoiEvent;
import br.com.actia.javascript.object.LatLong;
import br.com.actia.model.Poi;
import br.com.actia.model.PoiType;
import br.com.actia.ui.IncludePoiView;
import br.com.actia.validation.PoiValidator;
import br.com.actia.validation.Validator;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javax.swing.DefaultListCellRenderer;
import javax.validation.groups.Default;

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public class IncludePoiController extends PersistenceController {
    private Pane parentPane;
    private IncludePoiView view;
    private Validator<Poi> validador = new PoiValidator();
    private Poi poi;

    public IncludePoiController(AbstractController parent, Pane pane) {
        super(parent);
        loadPersistenceContext(((PersistenceController) getParentController()).getPersistenceContext());
        
        this.parentPane = pane;
        this.view = new IncludePoiView();
        this.view.resetForm();
        this.poi = new Poi();
        
        ObservableList<PoiType> lst = getPoiTypeList();
        this.view.setTypeList(lst);
        
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
        
        registerAction(this.view.getBtnSaveBusStop(), 
                ConditionalAction.build()
                    .addConditional(new BooleanExpression() {
                        @Override
                        public boolean conditional() {
                            Poi poi = view.loadPoiFromPanel();
                            String msg = validador.validate(poi);
                            if (!"".equals(msg == null ? "" : msg)) {
                               // Dialog.showInfo("Validacão", msg, );
                                System.out.println(msg);
                                return false;
                            }
                                                
                            return true;
                        }
                    })
                    .addAction(
                        TransactionalAction.build()
                            .persistenceCtxOwner(IncludePoiController.this)
                            .addAction(new AbstractAction() {
                                private Poi poi;

                                @Override
                                protected void action() {
                                    poi = view.loadPoiFromPanel();
                                    PoiDAO dao = new PoiDAOJPA(getPersistenceContext());
                                    poi = dao.save(poi);
                                }

                                @Override
                                protected void posAction() {
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            closeView();
                                        }
                                    });
                                    fireEvent(new IncludePoiEvent(poi));
                                }
                            })));
        
        parentPane.getChildren().add(view);
        StackPane.setAlignment(view, Pos.BOTTOM_CENTER);
        this.view.resetForm();
        closeView();
    }
    
    public void showView() {
        view.setVisible(true);
    }
    
    public void closeView() {
        view.setVisible(false);
    }

    void setPosition(LatLong position) {
        if(position != null) {
            this.poi.setLatitude(position.getLatitude());
            this.poi.setLongitude(position.getLongitude());
            this.view.setPoi(this.poi);
        }
    }
    
    @Override
    protected void cleanUp() {
        view.resetForm();
        
        super.cleanUp();
    }

    /**
     * Pega os tipos de Pontos de Interesse cadastrados na base de dados
     * @return observableArrayList com lista de PoiTypes cadastrados no banco
     */
    private ObservableList<PoiType> getPoiTypeList() {
        PoiTypeDAOJPA poiTypeDAOJPA = new PoiTypeDAOJPA(this.getPersistenceContext());
        return FXCollections.observableArrayList(poiTypeDAOJPA.getAll());
    }
}
