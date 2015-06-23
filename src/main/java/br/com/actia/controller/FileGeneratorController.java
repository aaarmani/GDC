package br.com.actia.controller;

import br.com.actia.action.AbstractAction;
import br.com.actia.action.BooleanExpression;
import br.com.actia.action.ConditionalAction;
import br.com.actia.action.TransactionalAction;
import br.com.actia.dao.RouteDAO;
import br.com.actia.dao.RouteDAOJPA;
import br.com.actia.event.AbstractEventListener;
import br.com.actia.event.CrudRouteEvent;
import br.com.actia.gson.RouteConverter;
import br.com.actia.model.Route;
import br.com.actia.ui.AutoCompleteComboBoxListener;
import br.com.actia.ui.FileGeneratorView;
import br.com.actia.ui.MainScreenView;
import br.com.actia.validation.RoutesToGenerateValidator;
import br.com.actia.validation.Validator;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class FileGeneratorController extends PersistenceController {
    private FileGeneratorView view;
    private final Validator<ObservableList<Route>> validador = new RoutesToGenerateValidator();
    private final Pane parentPane;
    private MainScreenView mainScreenView;
    private ResourceBundle rb;
    private RouteController routeController = null;
    
    private ObservableList<Route> obsRoutesSelecteds;
    
    public FileGeneratorController(AbstractController parent, MainScreenView mainScreenView, ResourceBundle rb) {
        super(parent);
        loadPersistenceContext(((PersistenceController) getParentController()).getPersistenceContext());
        this.rb = rb;
        this.mainScreenView = mainScreenView;
        this.parentPane = mainScreenView.getPaneCenter();
        this.view = new FileGeneratorView(rb);
        this.view.setMaxHeight(parentPane.getHeight());
        this.view.setMaxWidth(parentPane.getWidth());
        this.view.setMinHeight(parentPane.getHeight());
        this.view.setMinWidth(parentPane.getWidth());
        
        loadRouteList();
        
        obsRoutesSelecteds = FXCollections.observableArrayList();
        this.view.getLstvRoutesToGenerate().setItems(obsRoutesSelecteds);
        
        registerAction(this.view.getBtnCancelFileGenerator(), new AbstractAction() {
            @Override
            protected void action() {
                closeView();
                resetForm();
            }
        });
        
        registerAction(this.view.getBtnGenerateFileGenerator(), 
                ConditionalAction.build()
                    .addConditional(new BooleanExpression() {
                        @Override
                        public boolean conditional() {
                            ObservableList<Route> routes = obsRoutesSelecteds;
                            String msg = validador.validate(routes);
                            if (!"".equals(msg == null ? "" : msg)) {
                               // Dialog.showInfo("Validacão", msg, );
                                System.out.println(msg);
                                return false;
                            }
                                                
                            return true;
                        }
                    })
                    .addAction(TransactionalAction.build()
                            .persistenceCtxOwner(FileGeneratorController.this)
                            .addAction(new AbstractAction() {
                                private ObservableList<Route> routes;

                                @Override
                                protected void action() {
                                    routes = obsRoutesSelecteds;
                                    RouteConverter routeConverter = null;
                                    for(Route r : routes){
                                        routeConverter = new RouteConverter(r);
                                        routeConverter.generateAllFromRoute();
                                    }
                                }

                                @Override
                                protected void posAction() {
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            resetForm();
                                        }
                                    });
                                }
                            })));
        
        registerAction(view.getBtnAddRoute(), new AbstractAction() {
            @Override
            protected void action() {
                putSelectedRouteInList();
            }
        });
        
        registerEventListener(CrudRouteEvent.class, new AbstractEventListener<CrudRouteEvent>() {
            @Override
            public void handleEvent(CrudRouteEvent event) {
                loadRouteList();
            }
        });
        
        this.view.getLstvRoutesToGenerate().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Route selectedRt = view.getLstvRoutesToGenerate().getSelectionModel().getSelectedItem();
                if(obsRoutesSelecteds.contains(selectedRt)) {
                    obsRoutesSelecteds.remove(selectedRt);
                    view.getLstvRoutesToGenerate().getSelectionModel().clearSelection();
                }
            }
            
        });
        
        StackPane.setAlignment(view, Pos.CENTER);
        this.resetForm();
    }
    
    public void showView() {
        parentPane.getChildren().add(view);
    }
    
    public void closeView() {
        parentPane.getChildren().remove(view);
    }
    
    @Override
    protected void cleanUp() {
        this.resetForm();
        
        closeView();
        super.cleanUp(); //To change body of generated methods, choose Tools | Templates.
    }
    
    private void putSelectedRouteInList() {
         Route selectedRoute = this.view.getCbRoute().getSelectionModel().getSelectedItem();
         if(selectedRoute != null && !obsRoutesSelecteds.contains(selectedRoute)) {
             obsRoutesSelecteds.add(selectedRoute);
         }
    }
    
    private void loadRouteList() {
        ObservableList<Route> lstRoute = getRouteList();
        this.view.getCbRoute().getItems().clear();
        this.view.getCbRoute().getItems().addAll(lstRoute);
    }
    
    private ObservableList<Route> getRouteList() {
        RouteDAO routeDAO = new RouteDAOJPA(this.getPersistenceContext());
        return FXCollections.observableArrayList(routeDAO.getAll());
    }
    
    private void resetForm() {
        this.view.getCbRoute().getSelectionModel().clearSelection();
        obsRoutesSelecteds.clear();
    }
}