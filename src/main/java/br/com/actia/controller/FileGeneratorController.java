package br.com.actia.controller;

import br.com.actia.action.AbstractAction;
import br.com.actia.action.BooleanExpression;
import br.com.actia.action.ConditionalAction;
import br.com.actia.action.TransactionalAction;
import br.com.actia.dao.RouteDAO;
import br.com.actia.dao.RouteDAOJPA;
import br.com.actia.event.AbstractEventListener;
import br.com.actia.event.CopyFileEvent;
import br.com.actia.event.CrudRouteEvent;
import br.com.actia.gson.RouteConverter;
import br.com.actia.model.Route;
import br.com.actia.ui.AutoCompleteComboBoxListener;
import br.com.actia.ui.FileGeneratorView;
import br.com.actia.ui.MainScreenView;
import br.com.actia.validation.RoutesToGenerateValidator;
import br.com.actia.validation.Validator;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import service.FileToCopy;
import service.MediaExporter;

public class FileGeneratorController extends PersistenceController {
    private FileGeneratorView view;
    private final Validator<List<Route>> validador = new RoutesToGenerateValidator();
    private final Pane parentPane;
    private MainScreenView mainScreenView;
    private ResourceBundle rb;
    private RouteController routeController = null;
    
    private File directoryFile;
    
    // private ObservableList<Route> obsRoutesSelecteds;
    
    private RouteDAO routeDAO = null;
    private Collection<Route> listRouteAll = null;
    
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
        
        /*
        loadRouteList();
        
        obsRoutesSelecteds = FXCollections.observableArrayList();
        this.view.getLstvRoutesToGenerate().setItems(obsRoutesSelecteds);
        */
        
        this.routeDAO = new RouteDAOJPA(getPersistenceContext());
        this.listRouteAll = (Collection<Route>)this.routeDAO.getAll();
        
        loadRouteToList();
        
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
                            // ObservableList<Route> routes = obsRoutesSelecteds;
                            List<Route> routes = null;
                            if(!view.getLsvRoutesToGenerate().getTargetItems().isEmpty()) {
                                routes = view.getLsvRoutesToGenerate().getTargetItems();
                            }
                            String msg = validador.validate(routes, rb);
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
                                // private ObservableList<Route> routes;

                                @Override
                                protected void action() {
//                                    VBox transparentPane = view.transparentPane();
//                                    
//                                    BorderPane parent = (BorderPane)parentPane.getParent();
//                                    if(!parent.getChildren().contains(transparentPane)){
//                                        parent.getChildren().add(transparentPane);
//                                    }
                                    
                                    // routes = obsRoutesSelecteds;
                                    
                                    List<Route> routes = null;
                                    if(!view.getLsvRoutesToGenerate().getTargetItems().isEmpty()) {
                                        routes = view.getLsvRoutesToGenerate().getTargetItems();
                                    }
                                    
                                    RouteConverter routeConverter = new RouteConverter();
                                    routeConverter.setPath(view.getTfDirectoryPath().getText());
                                    routeConverter.buildFolderStructure();
                                    
                                    for(Route r : routes){
                                        routeConverter.setRoute(r);
                                        routeConverter.setFilePath();
                                        routeConverter.generateAllFromRoute();
                                    }
                                    
                                    MediaExporter mediaExporter = new MediaExporter(routes);
                                    mediaExporter.setDestinationFolder(view.getTfDirectoryPath().getText());
                                    ArrayList<FileToCopy> filesToCopy = mediaExporter.exportAndBuild();
                                    
                                    for(FileToCopy fileToCopy : filesToCopy) {
                                        fireEvent(new CopyFileEvent(fileToCopy));
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
        
        registerAction(this.view.getBtnChooseDirectory(), new AbstractAction() {
            @Override
            protected void action() {
                chooseDirectory();
            }
            
            @Override
            protected void posAction() { 
                showDirectoryPath();
            }
        });
        
        /*
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
        */
        
        StackPane.setAlignment(view, Pos.CENTER);
        this.resetForm();
    }
    
    public void showView() {
        parentPane.getChildren().add(view);
    }
    
    public void closeView() {
        parentPane.getChildren().remove(view);
    }
    
    private void chooseDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Selecione um diretório");
        directoryFile = directoryChooser.showDialog(null);
    }
    
    private void showDirectoryPath() {
        if(directoryFile != null) {
            view.getTfDirectoryPath().setText(directoryFile.getAbsolutePath());
        }
    }
    
    @Override
    protected void cleanUp() {
        this.resetForm();
        
        closeView();
        super.cleanUp(); //To change body of generated methods, choose Tools | Templates.
    }
    
    /*
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
    */
    
    private void loadRouteToList() {
        RouteDAO routeDAO = new RouteDAOJPA(getPersistenceContext());
        this.view.getLsvRoutesToGenerate().getSourceItems().addAll((Collection<Route>)routeDAO.getAll());
    }
    
    private void resetForm() {
        this.view.getTfDirectoryPath().setText("");
        // this.view.getCbRoute().getSelectionModel().clearSelection();
        // obsRoutesSelecteds.clear();
        
        this.view.getLsvRoutesToGenerate().getSourceItems().clear();
        this.view.getLsvRoutesToGenerate().getSourceItems().addAll((Collection<Route>)listRouteAll);
        
        this.view.getLsvRoutesToGenerate().getTargetItems().clear();
    }
}