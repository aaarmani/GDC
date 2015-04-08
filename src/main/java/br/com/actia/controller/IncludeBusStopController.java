package br.com.actia.controller;

import br.com.actia.action.AbstractAction;
import br.com.actia.action.BooleanExpression;
import br.com.actia.action.ConditionalAction;
import br.com.actia.action.TransactionalAction;
import br.com.actia.dao.BannerDAO;
import br.com.actia.dao.BannerDAOJPA;
import br.com.actia.dao.BusStopDAO;
import br.com.actia.dao.BusStopDAOJPA;
import br.com.actia.javascript.object.LatLong;
import br.com.actia.model.Banner;
import br.com.actia.model.BusStop;
import br.com.actia.ui.IncludeBusStopView;
import br.com.actia.validation.BusStopValidator;
import br.com.actia.validation.Validator;
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
public class IncludeBusStopController extends PersistenceController {
    private final Pane parentPane;
    private final IncludeBusStopView view;
    private final Validator<BusStop> validador = new BusStopValidator();
    private BusStop busStop = null;
    private boolean isVisible = false;
    private ResourceBundle rb;
    
    public IncludeBusStopController(AbstractController parent, Pane pane, ResourceBundle rb) {
        super(parent);
        loadPersistenceContext(((PersistenceController) getParentController()).getPersistenceContext());
        this.rb = rb;
        
        this.parentPane = pane;
        this.view = new IncludeBusStopView(this.rb);
        this.busStop = new BusStop();
        //this.view.resetForm();
        
        ObservableList<Banner> lstBanner = getBannerList();
        this.view.getCbBanner().getItems().addAll(lstBanner);
        
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
                    .addAction(
                        TransactionalAction.build()
                            .persistenceCtxOwner(IncludeBusStopController.this)
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
                                    //fireEvent(new IncludeBusStopEvent(busStop));
                                }
                            })));
        
        registerAction(this.view.getBtnNewListPoi(), new AbstractAction() {
            @Override
            protected void action() {
                showNewBanner();
            }
        });
        
        registerAction(this.view.getBtnNewListVideo(), new AbstractAction() {
            @Override
            protected void action() {
                showNewVideo();
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
    
    void showNewBanner() {
        /*BannerController bannerCtrl = new BannerController(this, this.parentPane);
        bannerCtrl.showView();*/
    }
    
    void showNewVideo() {
        VideoController videoController = new VideoController(this, parentPane, this.rb);
        videoController.showView();
    }

    private ObservableList<Banner> getBannerList() {
        BannerDAO bannerDAO = new BannerDAOJPA(this.getPersistenceContext());
        return FXCollections.observableArrayList(bannerDAO.getAll());
    }
    
    @Override
    protected void cleanUp() {
        super.cleanUp(); //To change body of generated methods, choose Tools | Templates.
        view.resetForm();
    }
}
