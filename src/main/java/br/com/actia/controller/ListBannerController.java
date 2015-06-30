package br.com.actia.controller;

import br.com.actia.action.AbstractAction;
import br.com.actia.action.BooleanExpression;
import br.com.actia.action.ConditionalAction;
import br.com.actia.action.TransactionalAction;
import br.com.actia.dao.ListBannerDAO;
import br.com.actia.dao.ListBannerDAOJPA;
import br.com.actia.dao.BannerDAO;
import br.com.actia.dao.BannerDAOJPA;
import br.com.actia.event.AbstractEventListener;
import br.com.actia.event.CrudListBannerEvent;
import br.com.actia.event.CrudBannerEvent;
import br.com.actia.model.ListBanner;
import br.com.actia.model.Banner;
import br.com.actia.ui.EntityListView;
import br.com.actia.ui.MainScreenView;
import br.com.actia.validation.ListBannerValidator;
import br.com.actia.validation.Validator;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.application.Platform;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;

public class ListBannerController extends PersistenceController {
    private final ResourceBundle rb;
    private final Pane parentPane;
    private MainScreenView mainScreenView;
    private final EntityListView<Banner, ListBanner> view;
    private Validator<ListBanner> validador = new ListBannerValidator();
    private BannerController bannerController = null;
    
    private BannerDAO bannerDAO = null;
    private Collection<Banner> listBannerAll = null;
    
    public ListBannerController(AbstractController parent, MainScreenView mainScreenView, ResourceBundle rb) {
        super(parent);
        loadPersistenceContext(((PersistenceController) getParentController()).getPersistenceContext());
        this.rb = rb;
        this.mainScreenView = mainScreenView;
        this.parentPane = mainScreenView.getPaneCenter();
        this.view = new EntityListView<Banner, ListBanner>(this.rb);
        this.view.setMaxHeight(parentPane.getHeight());
        this.view.setMaxWidth(parentPane.getWidth());
        this.view.setMinHeight(parentPane.getHeight());
        this.view.setMinWidth(parentPane.getWidth());
        
        this.bannerDAO = new BannerDAOJPA(getPersistenceContext());
        this.listBannerAll = (Collection<Banner>)this.bannerDAO.getAll();
        
        loadEntityToList();
        
        registerAction(this.view.getBtnCancel(), new AbstractAction() {
            @Override
            protected void action() {
                closeView();
            }
        });
        
        registerAction(this.view.getBtnSave(),
            ConditionalAction.build()
                .addConditional(new BooleanExpression() {
                    @Override
                    public boolean conditional() {
                        ListBanner listBanner = loadListBannerFromView();

                        String msg = validador.validate(listBanner);
                        if (!"".equals(msg == null ? "" : msg)) {
                            // Dialog.showInfo("Validacão", msg, );
                             System.out.println(msg);
                             return false;
                        }
                        return true;
                    }
                })
                .addAction(TransactionalAction.build()
                    .persistenceCtxOwner(ListBannerController.this)
                    .addAction(new AbstractAction() {
                        ListBanner listBanner = null;
                        @Override
                        protected void action() {
                            listBanner = loadListBannerFromView();
                            ListBannerDAO listBannerDAO = new ListBannerDAOJPA(getPersistenceContext());
                            listBanner = listBannerDAO.save(listBanner);
                        }
                        
                         @Override
                        protected void posAction() {
                            //cleanUp();
                            resetForm();
                            refreshTable();
                            fireEvent(new CrudListBannerEvent(listBanner));
                            
                            if(parent instanceof RouteController){
                                closeView();
                            }
                        }
                    }))
        );
        
        registerAction(view.getBtnNewEntity(), new AbstractAction() {
            @Override
            protected void action() {
                showNewBannerController();
            }
        });
        
        view.getTable().setMouseEvent(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent t) {
                if (t.getClickCount() == 2) {
                    ListBanner listBanner = (ListBanner)view.getTable().getEntitySelected();
                    if (listBanner != null) {
                        loadListBannerToEdit(listBanner);
                    }
                }
            }
        });
        
        registerAction(this.view.getBtnDelete(),
                TransactionalAction.build()
                    .persistenceCtxOwner(ListBannerController.this)
                    .addAction(new AbstractAction() {
                        private ListBanner listBanner;
                        
                        @Override
                        protected void action() {
                            Integer id = Integer.parseInt(view.getTfId().getText());
                            if (id != null) {
                                ListBannerDAO listBannerDao = new ListBannerDAOJPA(getPersistenceContext());
                                listBanner = listBannerDao.findById(id);
                                if(listBanner != null) {
                                    listBannerDao.remove(listBanner);
                                }
                            }
                        }
                        @Override
                        protected void posAction() {
                            resetForm();
                            refreshTable();
                            fireEvent(new CrudListBannerEvent(listBanner));
                        }
                        @Override
                        protected void actionFailure(){
                            // TO DO
                        }
                    })
        );
        
        registerEventListener(CrudBannerEvent.class, new AbstractEventListener<CrudBannerEvent>() {
            @Override
            public void handleEvent(CrudBannerEvent event) {
                refreshListBannersForSelection();
                resetForm();
                refreshTable();
            }
        });
        
        StackPane.setAlignment(view, Pos.CENTER);
        this.view.resetForm(this.listBannerAll);
        this.refreshTable();
    }
    
    public void showView() {
        parentPane.getChildren().add(view);
    }
    
    public void closeView() {
        parentPane.getChildren().remove(view);
    }
    
    @Override
    protected void cleanUp() {
        view.resetForm(this.listBannerAll);
        closeView();
        super.cleanUp();
    }

    private void loadEntityToList() {
        BannerDAO bannerDAO = new BannerDAOJPA(getPersistenceContext());
        this.view.getLsvEntity().getSourceItems().addAll((Collection<Banner>)bannerDAO.getAll());
        this.view.getLsvEntity().setSourceHeader(new Label(rb.getString("AvailableBanners")));
        this.view.getLsvEntity().setTargetHeader(new Label(rb.getString("SelectedBanners")));
    }
    
    private ListBanner loadListBannerFromView() {
        Integer id = null;
        if(!view.getTfId().getText().trim().isEmpty()) {
            id = Integer.valueOf(view.getTfId().getText());
        }
        
        String name = null;
        if(!view.getTfName().getText().trim().isEmpty()) {
            name = view.getTfName().getText();
        }
        
        String description = null;
        if(!view.getTfDescription().getText().trim().isEmpty()) {
            description = view.getTfDescription().getText();
        }
        
        List<Banner> listBanner = null;
        if(!view.getLsvEntity().getTargetItems().isEmpty()) {
            listBanner = view.getLsvEntity().getTargetItems();
        }

        return new ListBanner(id, name, description, listBanner);
    }
    
    private void showNewBannerController() {
        if(bannerController == null)
            bannerController = new BannerController(this, mainScreenView, rb);
        
        bannerController.showView();
    }
    
    public void refreshTable() {
        refreshTable(null);
    }
    
    private void refreshTable(List<ListBanner> list) {
        //view.addTransition();
        if (list != null) {
            view.refreshTable(list);
            return;
        }
        
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                ListBannerDAO dao = new ListBannerDAOJPA(getPersistenceContext());
                view.refreshTable(dao.getAll());
            }
        });
    }
    
    private void loadListBannerToEdit(ListBanner listBanner) {
        this.view.getTfId().setText(listBanner.getId().toString());
        this.view.getTfName().setText(listBanner.getName());
        this.view.getTfDescription().setText(listBanner.getDescription());
        
        this.view.getLsvEntity().getSourceItems().clear();
        this.view.getLsvEntity().getSourceItems().addAll((Collection<Banner>)this.listBannerAll);
        this.view.getLsvEntity().getSourceItems().removeAll((Collection<Banner>)listBanner.getListBanner());
        
        this.view.getLsvEntity().getTargetItems().clear();
        this.view.getLsvEntity().getTargetItems().addAll((Collection<Banner>)listBanner.getListBanner());
        
        this.view.getBtnDelete().setVisible(true);
    }
    
    public void resetForm(){
        this.view.resetForm(this.listBannerAll);
    }
    
    public void refreshListBannersForSelection(){
        this.bannerDAO = new BannerDAOJPA(getPersistenceContext());
        this.listBannerAll = (Collection<Banner>)this.bannerDAO.getAll();
    }
}
