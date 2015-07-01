package br.com.actia.controller;

import br.com.actia.action.AbstractAction;
import br.com.actia.action.BooleanExpression;
import br.com.actia.action.ConditionalAction;
import br.com.actia.action.TransactionalAction;
import br.com.actia.dao.ListBannerDAO;
import br.com.actia.dao.ListBannerDAOJPA;
import br.com.actia.dao.ListBusStopDAO;
import br.com.actia.dao.ListBusStopDAOJPA;
import br.com.actia.dao.ListRSSDAO;
import br.com.actia.dao.ListRSSDAOJPA;
import br.com.actia.dao.ListVideoDAO;
import br.com.actia.dao.ListVideoDAOJPA;
import br.com.actia.dao.RouteDAO;
import br.com.actia.dao.RouteDAOJPA;
import br.com.actia.event.AbstractEventListener;
import br.com.actia.event.CrudListBannerEvent;
import br.com.actia.event.CrudListBusStopEvent;
import br.com.actia.event.CrudListRSSEvent;
import br.com.actia.event.CrudListVideoEvent;
import br.com.actia.event.CrudRouteEvent;
import br.com.actia.model.ListBanner;
import br.com.actia.model.ListBusStop;
import br.com.actia.model.ListRSS;
import br.com.actia.model.ListVideo;
import br.com.actia.model.Route;
import br.com.actia.ui.Dialog;
import br.com.actia.ui.MainScreenView;
import br.com.actia.ui.RouteView;
import br.com.actia.validation.RouteValidator;
import br.com.actia.validation.Validator;
import java.util.ResourceBundle;
import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.util.List;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class RouteController extends PersistenceController {
    private RouteView view;
    private final Validator<Route> validador = new RouteValidator();
    private final Pane parentPane;
    private MainScreenView mainScreenView;
    private ResourceBundle rb;
    
    private ListBannerController listBannerController = null;
    private ListRSSController listRSSController = null;
    private ListVideoController listVideoController = null;
    private ListBusStopController listBusStopController = null;
    
    private GoogleMapController googleMapController = null;
    
    public RouteController(AbstractController parent, MainScreenView mainScreenView, ResourceBundle rb) {
        super(parent);
        loadPersistenceContext(((PersistenceController) getParentController()).getPersistenceContext());
        this.rb = rb;
        this.mainScreenView = mainScreenView;
        this.parentPane = mainScreenView.getPaneCenter();
        this.view = new RouteView(rb);
        this.view.setMaxHeight(parentPane.getHeight());
        this.view.setMaxWidth(parentPane.getWidth());
        this.view.setMinHeight(parentPane.getHeight());
        this.view.setMinWidth(parentPane.getWidth());
        
        loadListBannerList();
        loadListRSSList();
        loadListVideoList();
        loadListBusStopList();
        
        registerAction(this.view.getBtnCancelRoute(), new AbstractAction() {
            @Override
            protected void action() {
                closeView();
            }
        });
        
        registerAction(this.view.getBtnSaveRoute(),
            ConditionalAction.build()
                .addConditional(new BooleanExpression() {
                    @Override
                    public boolean conditional() {
                        Route route = view.loadRouteFromPanel();
                        String msg = validador.validate(route, rb);
                        if (!"".equals(msg == null ? "" : msg)) {
                            Dialog.showError(rb.getString("VALIDATION"), msg);
                            return false;
                        }

                        return true;
                    }
                })
                .addAction(TransactionalAction.build()
                            .persistenceCtxOwner(RouteController.this)
                            .addAction(new AbstractAction() {
                                private Route route;

                                @Override
                                protected void action() {
                                    route = view.loadRouteFromPanel();
                                    RouteDAO routeDao = new RouteDAOJPA(getPersistenceContext());
                                    route = routeDao.save(route);
                                }

                                @Override
                                protected void posAction() {
                                    view.resetForm();
                                    reloadAllLists();
                                    refreshTable();
                                    //cleanUp();
                                    //MOVER ARQUIVO PARA PASTA DO SISTEMA
                                    fireEvent(new CrudRouteEvent(route));
                                    
                                    /*
                                    if(parent instanceof FileGeneratorController){
                                        closeView();
                                    }
                                    */
                                }
                            }))
        );
        
        registerAction(this.view.getBtnDeleteRoute(),
            TransactionalAction.build()
                .persistenceCtxOwner(RouteController.this)
                .addAction(new AbstractAction() {
                    private Route route;

                    @Override
                    protected void action() {
                        Integer id = view.getRouteId();
                        if (id != null) {
                            RouteDAO routeDao = new RouteDAOJPA(getPersistenceContext());
                            route = routeDao.findById(id);
                            if (route != null) {
                                routeDao.remove(route);
                            }
                        }
                    }
                    @Override
                    protected void posAction() {
                        view.resetForm();
                        refreshTable();
                        reloadAllLists();
                        fireEvent(new CrudRouteEvent(route));
                    }
                    @Override
                    protected void actionFailure(){

                    }
                })
        );
        
        registerAction(this.view.getBtnNewListBanner(), new AbstractAction() {
            @Override
            protected void action() {
                showNewListBanner();
            }
            @Override
            protected void posAction() {
                loadListBannerList();
            }
        });
        
        registerAction(this.view.getBtnNewListRSS(), new AbstractAction() {
            @Override
            protected void action() {
                showNewListRSS();
            }
            @Override
            protected void posAction() {
                loadListRSSList();
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
        
        registerAction(this.view.getBtnNewListBusStop(), new AbstractAction() {
            @Override
            protected void action() {
                showGoogleMapControllerAndNewListBusStop();
            }
            @Override
            protected void posAction() {
                loadListBusStopList();
            }
        });
        
        registerEventListener(CrudListBannerEvent.class, new AbstractEventListener<CrudListBannerEvent>() {
            @Override
            public void handleEvent(CrudListBannerEvent event) {
                loadListBannerList();
                System.out.println("EVENTO: CrudListBannerEvent");
            }
        });
        
        registerEventListener(CrudListRSSEvent.class, new AbstractEventListener<CrudListRSSEvent>() {
            @Override
            public void handleEvent(CrudListRSSEvent event) {
                loadListRSSList();
                System.out.println("EVENTO: CrudListRSSEvent");
            }
        });
        
        registerEventListener(CrudListVideoEvent.class, new AbstractEventListener<CrudListVideoEvent>() {
            @Override
            public void handleEvent(CrudListVideoEvent event) {
                loadListVideoList();
                System.out.println("EVENTO: CrudListVideoEvent");
            }
        });
        
        registerEventListener(CrudListBusStopEvent.class, new AbstractEventListener<CrudListBusStopEvent>() {
            @Override
            public void handleEvent(CrudListBusStopEvent event) {
                loadListBusStopList();
                System.out.println("EVENTO: CrudListBusStopEvent");
            }
        });
        
        view.getTable().setMouseEvent(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent t) {
                if (t.getClickCount() == 2) {
                    Route route = (Route)view.getTable().getEntitySelected();
                    if (route != null) {
                        view.loadRouteToEdit(route);
                    }
                }
            }
        });
        
        StackPane.setAlignment(view, Pos.CENTER);
        this.view.resetForm();
        this.refreshTable();
    }
    
    public void showView() {
        parentPane.getChildren().add(view);
    }
    
    public void closeView() {
        parentPane.getChildren().remove(view);
    }
    
    void showNewListBanner() {
        if(listBannerController == null)
            listBannerController = new ListBannerController(this, mainScreenView, rb);
        
        listBannerController.showView();
    }
    
    void showNewListRSS() {
        if(listRSSController == null)
            listRSSController = new ListRSSController(this, mainScreenView, rb);
        
        listRSSController.showView();
    }
    
    void showNewListVideo() {
        if(listVideoController == null)
            listVideoController = new ListVideoController(this, mainScreenView, rb);
        
        listVideoController.showView();
    }
    
    void showGoogleMapControllerAndNewListBusStop() {
        if(googleMapController == null)
            googleMapController = new GoogleMapController(this, mainScreenView, rb);

        googleMapController.showView();
        googleMapController.showBusStopListController(null);
    }
    
    private ObservableList<ListBanner> getListBannerList() {
        ListBannerDAO listBannerDAO = new ListBannerDAOJPA(getPersistenceContext());
        return FXCollections.observableArrayList(listBannerDAO.getAll());
    }
    
    private ObservableList<ListRSS> getListRSSList() {
        ListRSSDAO listRSSDAO = new ListRSSDAOJPA(getPersistenceContext());
        return FXCollections.observableArrayList(listRSSDAO.getAll());
    }
    
    private ObservableList<ListVideo> getListVideoList() {
        ListVideoDAO listVideoDAO = new ListVideoDAOJPA(getPersistenceContext());
        return FXCollections.observableArrayList(listVideoDAO.getAll());
    }
    
    private ObservableList<ListBusStop> getListBusStopList() {
        ListBusStopDAO listBusStopDAO = new ListBusStopDAOJPA(getPersistenceContext());
        return FXCollections.observableArrayList(listBusStopDAO.getAll());
    }
    
    @Override
    protected void cleanUp() {
        view.resetForm();
        reloadAllLists();
        
        // this.view.getBtnPlay().setVisible(false);
        // this.view.getFeedView().setVisible(false);
        
        closeView();
        super.cleanUp(); //To change body of generated methods, choose Tools | Templates.
    }
    
    private void reloadAllLists() {
        loadListBannerList();
        loadListRSSList();
        loadListVideoList();
        loadListBusStopList();
    }
    
    private void loadListBannerList() {
        ObservableList<ListBanner> lstBanners = getListBannerList();
        this.view.getCbListBanners().getItems().clear();
        this.view.getCbListBanners().getItems().addAll(lstBanners);
    }
    
    private void loadListRSSList() {
        ObservableList<ListRSS> lstRSSs = getListRSSList();
        this.view.getCbListRSSs().getItems().clear();
        this.view.getCbListRSSs().getItems().addAll(lstRSSs);
    }
    
    private void loadListVideoList() {
        ObservableList<ListVideo> lstVideos = getListVideoList();
        this.view.getCbListVideos().getItems().clear();
        this.view.getCbListVideos().getItems().addAll(lstVideos);
    }
    
    private void loadListBusStopList() {
        ObservableList<ListBusStop> lstBusStops = getListBusStopList();
        this.view.getCbListBusStops().getItems().clear();
        this.view.getCbListBusStops().getItems().addAll(lstBusStops);
    }
    
    private void refreshTable() {
        refreshTable(null);
    }
    
    private void refreshTable(List<Route> list) {
        //view.addTransition();
        if (list != null) {
            view.refreshTable(list);
            return;
        }
        
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                RouteDAO dao = new RouteDAOJPA(getPersistenceContext());
                view.refreshTable(dao.getAll());
            }
        });
    }
}