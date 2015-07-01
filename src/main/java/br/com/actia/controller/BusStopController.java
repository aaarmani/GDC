package br.com.actia.controller;

import br.com.actia.action.AbstractAction;
import br.com.actia.action.BooleanExpression;
import br.com.actia.action.ConditionalAction;
import br.com.actia.action.TransactionalAction;
import br.com.actia.dao.IndicationDAO;
import br.com.actia.dao.IndicationDAOJPA;
import br.com.actia.dao.BusStopDAO;
import br.com.actia.dao.BusStopDAOJPA;
import br.com.actia.dao.ListPoiDAO;
import br.com.actia.dao.ListPoiDAOJPA;
import br.com.actia.dao.ListVideoDAO;
import br.com.actia.dao.ListVideoDAOJPA;
import br.com.actia.event.AbstractEventListener;
import br.com.actia.event.BusStopDeleteEvent;
import br.com.actia.event.CrudIndicationEvent;
import br.com.actia.event.BusStopNewEvent;
import br.com.actia.event.CrudListPoiEvent;
import br.com.actia.event.CrudListVideoEvent;
import br.com.actia.javascript.object.LatLong;
import br.com.actia.model.Indication;
import br.com.actia.model.BusStop;
import br.com.actia.model.ListPoi;
import br.com.actia.model.ListVideo;
import br.com.actia.ui.BusStopView;
import br.com.actia.ui.Dialog;
import br.com.actia.ui.MainScreenView;
import br.com.actia.validation.BusStopValidator;
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
public class BusStopController extends PersistenceController {
    private final int DEFAULT_RADIUS = 40;
    private final Pane parentPane;
    private MainScreenView mainScreenView;
    private final BusStopView view;
    private final Validator<BusStop> validador = new BusStopValidator();
    private BusStop busStop = null;
    private ResourceBundle rb;
    
    private ListPoiController listPoiController = null;
    private ListVideoController listVideoController = null;
    
    public BusStopController(AbstractController parent, MainScreenView mainScreenView, Pane pane, ResourceBundle rb) {
        super(parent);
        loadPersistenceContext(((PersistenceController) getParentController()).getPersistenceContext());
        this.rb = rb;
        this.mainScreenView = mainScreenView;
        this.parentPane = pane;
        this.view = new BusStopView(this.rb);
        this.busStop = new BusStop();
        
        loadIndicationList();
        loadListPoiList();
        loadListVideoList();
        
        registerAction(this.view.getBtnCancelBusStop(), new AbstractAction() {
            @Override
            protected void action() {
                closeView();
                refreshForm(null);
            }
        });
        
        registerAction(this.view.getBtnSaveBusStop(), 
                ConditionalAction.build()
                    .addConditional(new BooleanExpression() {
                        @Override
                        public boolean conditional() {
                            BusStop busStop = view.loadBusStopFromPanel();
                            String msg = validador.validate(busStop, rb);
                            if (!"".equals(msg == null ? "" : msg)) {
                                Dialog.showError(rb.getString("VALIDATION"), msg);
                                System.out.println(msg);
                                return false;
                            }
                                                
                            return true;
                        }
                    })
                    .addAction(TransactionalAction.build()
                            .persistenceCtxOwner(BusStopController.this)
                            .addAction(new AbstractAction() {
                                private BusStop busStop;

                                @Override
                                protected void action() {
                                    busStop = view.loadBusStopFromPanel();
                                    BusStopDAO dao = new BusStopDAOJPA(getPersistenceContext());
                                    busStop = dao.save(busStop);
                                }

                                @Override
                                protected void posAction() {
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            closeView();
                                            refreshForm(null);
                                        }
                                    });
                                    fireEvent(new BusStopNewEvent(busStop));
                                }
                            })));
        
        registerAction(this.view.getBtnDeleteBusStop(),
            TransactionalAction.build()
                    .persistenceCtxOwner(BusStopController.this)
                    .addAction(new AbstractAction() {
                        private BusStop busStop;
                        
                        @Override
                        protected void action() {
                            Integer id = Integer.valueOf(view.getTfId().getText());
                            if (id != null) {
                                BusStopDAO busStopDAO = new BusStopDAOJPA(getPersistenceContext());
                                busStop = busStopDAO.findById(id);
                                if (busStop != null) { 
                                    busStopDAO.remove(busStop);
                                }
                            }
                        }
                        
                        @Override
                        public void posAction() {
                            refreshForm(null);
                            closeView();
                            fireEvent(new BusStopDeleteEvent(busStop));
                        }
                    }));
        
        registerAction(this.view.getBtnNewListPoi(), new AbstractAction() {
            @Override
            protected void action() {
                showNewListPoi();
            }
            @Override
            protected void posAction() {
                loadListPoiList();
            }
        });
        
        registerAction(this.view.getBtnNewListVideo(), new AbstractAction() {
            @Override
            protected void action() {
                showNewListVideo();
            }
            @Override
            protected void posAction() {
                loadListVideoList();
            }
        });
        
        registerEventListener(CrudListVideoEvent.class, new AbstractEventListener<CrudListVideoEvent>() {
            @Override
            public void handleEvent(CrudListVideoEvent event) {
                loadListVideoList();
                System.out.println("EVENTO: CrudListVideoEvent");
            }
        });
        
        registerEventListener(CrudListPoiEvent.class, new AbstractEventListener<CrudListPoiEvent>() {
            @Override
            public void handleEvent(CrudListPoiEvent event) {
                loadListPoiList();
                System.out.println("EVENTO: CrudListPOIEvent");
            }
        });
        
        registerEventListener(CrudIndicationEvent.class,  new AbstractEventListener<CrudIndicationEvent>() {
            @Override
            public void handleEvent(CrudIndicationEvent event) {
                loadIndicationList();
                System.out.println("EVENTO: CrudListIndicationEvent");
            }
        });
        
        StackPane.setAlignment(view, Pos.BOTTOM_CENTER);
        this.view.resetForm();
    }
    
    public void showView(String busStopName) {
        if(busStopName != null) {
            BusStopDAO busStopDAOJPA = new BusStopDAOJPA(getPersistenceContext());

            List<BusStop> lstBusStop = busStopDAOJPA.getBusStopByName(busStopName);

            for(BusStop busStop : lstBusStop) {
                if(busStop.getName().equals(busStopName)) {
                    refreshForm(busStop);
                    break;
                }
            }
        }
        else {
            refreshForm(null);
        }
        
        if(!parentPane.getChildren().contains(view))
            parentPane.getChildren().add(view);
    }
    
    public void closeView() {
        this.parentPane.getChildren().remove(view);
    }

    void setPosition(LatLong position) {
        if(position != null) {
            this.busStop.setLatitude(position.getLatitude());
            this.busStop.setLongitude(position.getLongitude());
            this.busStop.setRadius(DEFAULT_RADIUS);
            this.view.setBusStop(this.busStop);
        }
    }
    
    void showNewListPoi() {
        if(listPoiController == null)
            listPoiController = new ListPoiController(this, mainScreenView, rb);
        
        listPoiController.showView();
        listPoiController.enableNewPoiButton(false);
    }
    
    void showNewListVideo() {
        if(listVideoController == null)
            listVideoController = new ListVideoController(this, mainScreenView, rb);
        
        listVideoController.showView();
    }

    private ObservableList<Indication> getIndicationList() {
        IndicationDAO indicationDAO = new IndicationDAOJPA(this.getPersistenceContext());
        return FXCollections.observableArrayList(indicationDAO.getAll());
    }

    private ObservableList<ListPoi> getListPoiList() {
        ListPoiDAO listPoiDAO = new ListPoiDAOJPA(getPersistenceContext());
        return FXCollections.observableArrayList(listPoiDAO.getAll());
    }
    
    private ObservableList<ListVideo> getListVideoList() {
        ListVideoDAO listVideoDAO = new ListVideoDAOJPA(getPersistenceContext());
        return FXCollections.observableArrayList(listVideoDAO.getAll());
    }
    
    @Override
    protected void cleanUp() {
        view.resetForm();
        closeView();
        super.cleanUp(); //To change body of generated methods, choose Tools | Templates.
    }

    private void loadIndicationList() {
        ObservableList<Indication> lstIndication = getIndicationList();
        this.view.getCbIndication().getItems().addAll(lstIndication);
    }

    private void loadListPoiList() {
        ObservableList<ListPoi> lstPois = getListPoiList();
        this.view.getCbListPois().getItems().clear();
        this.view.getCbListPois().getItems().addAll(lstPois);
    }

    private void loadListVideoList() {
        ObservableList<ListVideo> lstVideo = getListVideoList();
        this.view.getCbListVideos().getItems().clear();
        this.view.getCbListVideos().getItems().addAll(lstVideo);
    }

    private void refreshForm(BusStop busStop) {
        if(busStop != null)
        {
            this.view.getTfId().setText(busStop.getId().toString());
            this.view.getTfName().setText(busStop.getName());
            this.view.getTfDescription().setText(busStop.getDescription());
            this.view.getTfLatitude().setText(String.valueOf(busStop.getLatitude()));
            this.view.getTfLongitude().setText(String.valueOf(busStop.getLongitude()));
            this.view.getTfRadius().setText(Float.toString(busStop.getRadius()));
            this.view.getCbIndication().getSelectionModel().select(busStop.getIndication());
            this.view.getCbListPois().getSelectionModel().select(busStop.getPois());
            this.view.getCbListVideos().getSelectionModel().select(busStop.getVideos());
            
            this.view.getBtnDeleteBusStop().setVisible(true);
        }
        else {
            this.view.getTfId().clear();
            this.view.getTfName().clear();
            this.view.getTfDescription().clear();
            this.view.getTfLatitude().clear();
            this.view.getTfLongitude().clear();
            this.view.getTfRadius().setText(String.valueOf(DEFAULT_RADIUS));//clear();
            this.view.getCbIndication().getSelectionModel().clearSelection();
            this.view.getCbListPois().getSelectionModel().clearSelection();
            this.view.getCbListVideos().getSelectionModel().clearSelection();
            
            this.view.getBtnDeleteBusStop().setVisible(false);
        }
    }
}
