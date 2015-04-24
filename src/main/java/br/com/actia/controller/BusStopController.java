package br.com.actia.controller;

import br.com.actia.action.AbstractAction;
import br.com.actia.action.BooleanExpression;
import br.com.actia.action.ConditionalAction;
import br.com.actia.action.TransactionalAction;
import br.com.actia.dao.BannerDAO;
import br.com.actia.dao.BannerDAOJPA;
import br.com.actia.dao.BusStopDAO;
import br.com.actia.dao.BusStopDAOJPA;
import br.com.actia.dao.ListPoiDAO;
import br.com.actia.dao.ListPoiDAOJPA;
import br.com.actia.dao.ListVideoDAO;
import br.com.actia.dao.ListVideoDAOJPA;
import br.com.actia.event.AbstractEvent;
import br.com.actia.event.AbstractEventListener;
import br.com.actia.event.CrudBannerEvent;
import br.com.actia.event.CrudBusStopEvent;
import br.com.actia.event.CrudListPoiEvent;
import br.com.actia.event.CrudListVideoEvent;
import br.com.actia.javascript.object.LatLong;
import br.com.actia.model.Banner;
import br.com.actia.model.BusStop;
import br.com.actia.model.ListPoi;
import br.com.actia.model.ListVideo;
import br.com.actia.ui.BusStopView;
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
    private final Pane parentPane;
    private final BusStopView view;
    private final Validator<BusStop> validador = new BusStopValidator();
    private BusStop busStop = null;
    private ResourceBundle rb;
    
    private ListPoiController listPoiController = null;
    private ListVideoController listVideoController = null;
    
    public BusStopController(AbstractController parent, Pane pane, ResourceBundle rb) {
        super(parent);
        loadPersistenceContext(((PersistenceController) getParentController()).getPersistenceContext());
        this.rb = rb;
        
        this.parentPane = pane;
        this.view = new BusStopView(this.rb);
        this.busStop = new BusStop();
        
        loadBannerList();
        loadListPoiList();
        loadListVideoList();
        
        registerAction(this.view.getBtnCancelBusStop(), new AbstractAction() {
            @Override
            protected void action() {
                closeView();
            }
        });
        
        registerAction(this.view.getBtnSaveBusStop(), 
                ConditionalAction.build()
                    .addConditional(new BooleanExpression() {
                        @Override
                        public boolean conditional() {
                            BusStop busStop = view.loadBusStopFromPanel();
                            String msg = validador.validate(busStop);
                            if (!"".equals(msg == null ? "" : msg)) {
                               // Dialog.showInfo("Validacão", msg, );
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
                                        }
                                    });
                                    fireEvent(new CrudBusStopEvent(busStop));
                                }
                            })));
        
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
            }
        });
        
        registerEventListener(CrudListPoiEvent.class, new AbstractEventListener<CrudListPoiEvent>() {
            @Override
            public void handleEvent(CrudListPoiEvent event) {
                loadListPoiList();
            }
        });
        
        registerEventListener(CrudBannerEvent.class,  new AbstractEventListener<CrudBannerEvent>() {
            @Override
            public void handleEvent(CrudBannerEvent event) {
                loadBannerList();
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
            this.view.setBusStop(this.busStop);
        }
    }
    
    void showNewListPoi() {
        if(listPoiController == null)
            listPoiController = new ListPoiController(this, parentPane, rb);
        
        listPoiController.showView();
        listPoiController.enableNewPoiButton(false);
    }
    
    void showNewListVideo() {
        if(listPoiController == null)
            listVideoController = new ListVideoController(this, parentPane, rb);
        
        listVideoController.showView();
    }

    private ObservableList<Banner> getBannerList() {
        BannerDAO bannerDAO = new BannerDAOJPA(this.getPersistenceContext());
        return FXCollections.observableArrayList(bannerDAO.getAll());
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

    private void loadBannerList() {
        ObservableList<Banner> lstBanner = getBannerList();
        this.view.getCbBanner().getItems().addAll(lstBanner);
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
        this.view.getTfId().setText(busStop.getId().toString());
        this.view.getTfName().setText(busStop.getName());
        this.view.getTfDescription().setText(busStop.getDescription());
        this.view.getTfLatitude().setText(String.valueOf(busStop.getLatitude()));
        this.view.getTfLongitude().setText(String.valueOf(busStop.getLongitude()));
        this.view.getTfRadius().setText(Float.toString(busStop.getRadius()));
        this.view.getCbBanner().getSelectionModel().select(busStop.getBanner());
        this.view.getCbListPois().getSelectionModel().select(busStop.getPois());
        this.view.getCbListVideos().getSelectionModel().select(busStop.getVideos());
    }
}
