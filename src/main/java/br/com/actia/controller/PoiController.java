package br.com.actia.controller;

import br.com.actia.action.AbstractAction;
import br.com.actia.action.BooleanExpression;
import br.com.actia.action.ConditionalAction;
import br.com.actia.action.TransactionalAction;
import br.com.actia.dao.PoiDAO;
import br.com.actia.dao.PoiDAOJPA;
import br.com.actia.dao.PoiTypeDAO;
import br.com.actia.dao.PoiTypeDAOJPA;
import br.com.actia.event.IncludePoiEvent;
import br.com.actia.javascript.object.LatLong;
import br.com.actia.model.Poi;
import br.com.actia.model.PoiType;
import br.com.actia.ui.Dialog;
import br.com.actia.ui.PoiView;
import br.com.actia.validation.PoiValidator;
import br.com.actia.validation.Validator;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public class PoiController extends PersistenceController {
    private Pane parentPane;
    private PoiView view;
    private Validator<Poi> validador = new PoiValidator();
    private Poi poi;
    private final ResourceBundle rb;

    public PoiController(AbstractController parent, Pane pane, ResourceBundle rb) {
        super(parent);
        loadPersistenceContext(((PersistenceController) getParentController()).getPersistenceContext());
        this.rb = rb;
        
        this.parentPane = pane;
        this.view = new PoiView(this.rb);
        this.view.resetForm();
        this.poi = new Poi();
        
        ObservableList<PoiType> lstPoiTypes = getPoiTypeList();
        this.view.getCbPoiType().getItems().addAll(lstPoiTypes);
        
        registerAction(this.view.getBtnCancelPOI(), new AbstractAction() {
            @Override
            protected void action() {
                closeView();
            }
        });
        
        registerAction(this.view.getBtnSavePOI(), 
                ConditionalAction.build()
                    .addConditional(new BooleanExpression() {
                        @Override
                        public boolean conditional() {
                            Poi poi = view.loadPoiFromPanel();
                            String msg = validador.validate(poi);
                            if (!"".equals(msg == null ? "" : msg)) {
                                //Dialog.showInfo("Validacão", msg, parentPane.);
                                System.out.println(msg);
                                return false;
                            }
                                                
                            return true;
                        }
                    })
                    .addAction(TransactionalAction.build()
                            .persistenceCtxOwner(PoiController.this)
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
    
    public void showView(String poiName) {
        
        if(poiName != null) {
            PoiDAOJPA poiDAOJPA = new PoiDAOJPA(getPersistenceContext());

            List<Poi> lstPoi = poiDAOJPA.getPoiByName(poiName);

            for(Poi poi : lstPoi) {
                if(poi.getName().equals(poiName)) {
                    refreshForm(poi);
                    break;
                }
            }
        }

        view.setVisible(true);
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
        super.cleanUp();
        view.resetForm();
    }
    
    /**
     * Pega os tipos de Pontos de Interesse cadastrados na base de dados
     * @return observableArrayList com lista de PoiTypes cadastrados no banco
     */
    private ObservableList<PoiType> getPoiTypeList() {
        PoiTypeDAO poiTypeDAO = new PoiTypeDAOJPA(this.getPersistenceContext());
        return FXCollections.observableArrayList(poiTypeDAO.getAll());
    }

    private void refreshForm(Poi poi) {
        this.view.getTfId().setText(poi.getId().toString());
        this.view.getTfName().setText(poi.getName());
        this.view.getTfLatitude().setText(String.valueOf(poi.getLatitude()));
        this.view.getTfLongitude().setText(String.valueOf(poi.getLongitude()));
        this.poi = poi;
    }
}
