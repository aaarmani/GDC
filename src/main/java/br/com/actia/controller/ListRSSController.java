package br.com.actia.controller;

import br.com.actia.action.AbstractAction;
import br.com.actia.action.BooleanExpression;
import br.com.actia.action.ConditionalAction;
import br.com.actia.action.TransactionalAction;
import br.com.actia.dao.ListRSSDAO;
import br.com.actia.dao.ListRSSDAOJPA;
import br.com.actia.dao.RSSDAO;
import br.com.actia.dao.RSSDAOJPA;
import br.com.actia.event.AbstractEventListener;
import br.com.actia.event.CrudListRSSEvent;
import br.com.actia.event.CrudRSSEvent;
import br.com.actia.model.ListRSS;
import br.com.actia.model.RSS;
import br.com.actia.ui.EntityListView;
import br.com.actia.ui.MainScreenView;
import br.com.actia.validation.ListRSSValidator;
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

public class ListRSSController extends PersistenceController {
    private final ResourceBundle rb;
    private final Pane parentPane;
    private MainScreenView mainScreenView;
    private final EntityListView<RSS, ListRSS> view;
    private Validator<ListRSS> validador = new ListRSSValidator();
    private RSSController RSSController = null;
    
    private RSSDAO RSSDAO = null;
    private Collection<RSS> listRSSAll = null;
    
    public ListRSSController(AbstractController parent, MainScreenView mainScreenView, ResourceBundle rb) {
        super(parent);
        loadPersistenceContext(((PersistenceController) getParentController()).getPersistenceContext());
        this.rb = rb;
        this.mainScreenView = mainScreenView;
        this.parentPane = mainScreenView.getPaneCenter();
        this.view = new EntityListView<RSS, ListRSS>(this.rb);
        this.view.setMaxHeight(parentPane.getHeight());
        this.view.setMaxWidth(parentPane.getWidth());
        this.view.setMinHeight(parentPane.getHeight());
        this.view.setMinWidth(parentPane.getWidth());
        
        this.RSSDAO = new RSSDAOJPA(getPersistenceContext());
        this.listRSSAll = (Collection<RSS>)this.RSSDAO.getAll();
        
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
                        ListRSS listRSS = loadListRSSFromView();

                        String msg = validador.validate(listRSS);
                        if (!"".equals(msg == null ? "" : msg)) {
                            // Dialog.showInfo("Validacão", msg, );
                             System.out.println(msg);
                             return false;
                        }
                        return true;
                    }
                })
                .addAction(TransactionalAction.build()
                    .persistenceCtxOwner(ListRSSController.this)
                    .addAction(new AbstractAction() {
                        ListRSS listRSS = null;
                        @Override
                        protected void action() {
                            listRSS = loadListRSSFromView();
                            ListRSSDAO listRSSDAO = new ListRSSDAOJPA(getPersistenceContext());
                            listRSS = listRSSDAO.save(listRSS);
                        }
                        
                         @Override
                        protected void posAction() {
                            //cleanUp();
                            resetForm();
                            refreshTable();
                            fireEvent(new CrudListRSSEvent(listRSS));
                            
                            if(parent instanceof RouteController){
                                closeView();
                            }
                        }
                    }))
        );
        
        registerAction(view.getBtnNewEntity(), new AbstractAction() {
            @Override
            protected void action() {
                showNewRSSController();
            }
        });
        
        view.getTable().setMouseEvent(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent t) {
                if (t.getClickCount() == 2) {
                    ListRSS listRSS = (ListRSS)view.getTable().getEntitySelected();
                    if (listRSS != null) {
                        loadListRSSToEdit(listRSS);
                    }
                }
            }
        });
        
        registerAction(this.view.getBtnDelete(),
                TransactionalAction.build()
                    .persistenceCtxOwner(ListRSSController.this)
                    .addAction(new AbstractAction() {
                        private ListRSS listRSS;
                        
                        @Override
                        protected void action() {
                            Integer id = Integer.parseInt(view.getTfId().getText());
                            if (id != null) {
                                ListRSSDAO listRSSDao = new ListRSSDAOJPA(getPersistenceContext());
                                listRSS = listRSSDao.findById(id);
                                if(listRSS != null) {
                                    listRSSDao.remove(listRSS);
                                }
                            }
                        }
                        @Override
                        protected void posAction() {
                            resetForm();
                            refreshTable();
                            fireEvent(new CrudListRSSEvent(listRSS));
                        }
                        @Override
                        protected void actionFailure(){
                            // TO DO
                        }
                    })
        );
        
        registerEventListener(CrudRSSEvent.class, new AbstractEventListener<CrudRSSEvent>() {
            @Override
            public void handleEvent(CrudRSSEvent event) {
                refreshListRSSsForSelection();
                resetForm();
                refreshTable();
            }
        });
        
        StackPane.setAlignment(view, Pos.CENTER);
        this.view.resetForm(this.listRSSAll);
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
        view.resetForm(this.listRSSAll);
        closeView();
        super.cleanUp();
    }

    private void loadEntityToList() {
        RSSDAO RSSDAO = new RSSDAOJPA(getPersistenceContext());
        this.view.getLsvEntity().getSourceItems().addAll((Collection<RSS>)RSSDAO.getAll());
        this.view.getLsvEntity().setSourceHeader(new Label(rb.getString("AvailableRsss")));
        this.view.getLsvEntity().setTargetHeader(new Label(rb.getString("SelectedRsss")));
    }
    
    private ListRSS loadListRSSFromView() {
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
        
        List<RSS> listRSS = null;
        if(!view.getLsvEntity().getTargetItems().isEmpty()) {
            listRSS = view.getLsvEntity().getTargetItems();
        }

        return new ListRSS(id, name, description, listRSS);
    }
    
    private void showNewRSSController() {
        if(RSSController == null)
            RSSController = new RSSController(this, mainScreenView, rb);
        
        RSSController.showView();
    }
    
    public void refreshTable() {
        refreshTable(null);
    }
    
    private void refreshTable(List<ListRSS> list) {
        //view.addTransition();
        if (list != null) {
            view.refreshTable(list);
            return;
        }
        
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                ListRSSDAO dao = new ListRSSDAOJPA(getPersistenceContext());
                view.refreshTable(dao.getAll());
            }
        });
    }
    
    private void loadListRSSToEdit(ListRSS listRSS) {
        this.view.getTfId().setText(listRSS.getId().toString());
        this.view.getTfName().setText(listRSS.getName());
        this.view.getTfDescription().setText(listRSS.getDescription());
        
        this.view.getLsvEntity().getSourceItems().clear();
        this.view.getLsvEntity().getSourceItems().addAll((Collection<RSS>)this.listRSSAll);
        this.view.getLsvEntity().getSourceItems().removeAll((Collection<RSS>)listRSS.getListRSS());
        
        this.view.getLsvEntity().getTargetItems().clear();
        this.view.getLsvEntity().getTargetItems().addAll((Collection<RSS>)listRSS.getListRSS());
        
        this.view.getBtnDelete().setVisible(true);
    }
    
    public void resetForm(){
        this.view.resetForm(this.listRSSAll);
    }
    
    public void refreshListRSSsForSelection(){
        this.RSSDAO = new RSSDAOJPA(getPersistenceContext());
        this.listRSSAll = (Collection<RSS>)this.RSSDAO.getAll();
    }
}
