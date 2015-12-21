package br.com.actia.controller;

import br.com.actia.action.AbstractAction;
import br.com.actia.action.BooleanExpression;
import br.com.actia.action.ConditionalAction;
import br.com.actia.action.TransactionalAction;
import br.com.actia.dao.RouteDAO;
import br.com.actia.dao.RouteDAOJPA;
import br.com.actia.event.CopyFileEvent;
import br.com.actia.gson.ListVideoConverter;
import br.com.actia.gson.RouteConverter;
import br.com.actia.model.BusStop;
import br.com.actia.model.ListVideo;
import br.com.actia.model.Route;
import br.com.actia.ui.Dialog;
import br.com.actia.ui.FileGeneratorView;
import br.com.actia.ui.MainScreenView;
import br.com.actia.validation.RoutesToGenerateValidator;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import service.FileToCopy;
import service.MediaExporter;

public class FileGeneratorController extends PersistenceController {
    private FileGeneratorView view;
    private final RoutesToGenerateValidator validador = new RoutesToGenerateValidator();
    private final Pane parentPane;
    private MainScreenView mainScreenView;
    private ResourceBundle rb;
    private RouteController routeController = null;
    
    private File directoryFile;
    
    // private ObservableList<Route> obsRoutesSelecteds;
    
    private RouteDAO routeDAO = null;
    private List<Route> listRouteAll = null;
    
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
        this.listRouteAll = this.routeDAO.getAll();
        
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
                            List<Route> routes = null;
                            if(!view.getLsvRoutesToGenerate().getTargetItems().isEmpty()) {
                                routes = view.getLsvRoutesToGenerate().getTargetItems();
                            }
                            String msg = validador.validate(routes, rb);
                            if (!"".equals(msg == null ? "" : msg)) {
                                Dialog.showError(rb.getString("VALIDATION"), msg);
                                return false;
                            }
                            
                            if(view.getTfDirectoryPath().getText().isEmpty()) {
                                msg += rb.getString("FGChooseDirectory");
                                Dialog.showError(rb.getString("VALIDATION"), msg);
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
                                    
                                    Map<String, ListVideo> mapListVideo = new HashMap<String, ListVideo>();
                                    
                                    for(Route r : routes){
                                        routeConverter.setRoute(r);
                                        routeConverter.setFilePath();
                                        routeConverter.generateAllFromRoute();
                                        
                                        for(BusStop b : r.getBusStops().getListBusStop()) {
                                            if(b == null)
                                                continue;
                                            
                                            ListVideo l = b.getVideos();
                                            if(l != null)
                                                mapListVideo.put(l.getName(), l);
                                        }
                                    }
                                    
                                    //Build BusStop's ListVideos
                                    Iterator it = mapListVideo.keySet().iterator();
                                    while(it.hasNext()) {
                                        ListVideo lv = mapListVideo.get(it.next());
                                        ListVideoConverter listVideoConverter = new ListVideoConverter(lv, view.getTfDirectoryPath().getText());
                                        listVideoConverter.generate();
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
        this.view.getLsvRoutesToGenerate().getSourceItems().addAll(routeDAO.getAll());
    }
    
    private void resetForm() {
        this.view.getTfDirectoryPath().setText("");
        // this.view.getCbRoute().getSelectionModel().clearSelection();
        // obsRoutesSelecteds.clear();
        
        this.view.getLsvRoutesToGenerate().getSourceItems().clear();
        this.view.getLsvRoutesToGenerate().getSourceItems().addAll(listRouteAll);
        
        this.view.getLsvRoutesToGenerate().getTargetItems().clear();
    }
}