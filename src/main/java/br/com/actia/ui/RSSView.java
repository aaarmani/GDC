package br.com.actia.ui;

import br.com.actia.model.RSS;
import br.com.actia.validation.MaskTextField;
import java.io.File;
import java.util.ResourceBundle;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.List;

public class RSSView extends VBox {
    private final int MAX_HEIGHT = 600;
    private final int VIEWER_SIZE = 100;
    
    private TextField tfId;
    private MaskTextField tfName;
    private TextField tfFeedPath;
    private Button btnPreviewFeed;
    private Button btnCancelRSS;
    private Button btnSaveRSS;
    private Button btnDeleteRSS;
    private HBox feedView;
    private ImageView feedImageView;
    private VBox feedContentView;
    private Label feedTitleText;
    private Label feedContentText;
    private Label feedDateText;
    private Button btnPlay;
    private ResourceBundle rb;
    private EntityTable<RSS> table;
    
    public RSSView(ResourceBundle rb) {
        this.rb = rb;
        this.setMaxHeight(MAX_HEIGHT);
        this.getStyleClass().add("whitePanel");
        initializeComponents();
    }

    private void initializeComponents() {
        VBox head = buildHead();
        GridPane chooser = buildChooser();
        HBox buttons = buildButtons();
        VBox viewer = buildViewer();
        table = new EntityTable();
        
        this.getChildren().addAll(head, chooser, buttons, viewer, table);
    }

    private VBox buildHead() {
        Label lblTitle = new Label(rb.getString("RSNewRSS"));
        lblTitle.getStyleClass().add("titleLabel");
        Separator separator = new Separator();
        return new VBox(lblTitle, separator);
    }

    private GridPane buildChooser() {
        tfId = new TextField();
        
        Label lblName = new Label(rb.getString("Name"));
        tfName = new MaskTextField();
        tfName.setMaskCompleteWord(MaskTextField.FIELD_NAME);
        
        Label lblFeed = new Label(rb.getString("RSChooseFeed"));
        tfFeedPath = new TextField();
        btnPreviewFeed = new Button(rb.getString("Preview"));
        btnPreviewFeed.setId("btnPreviewFeed");
        btnPreviewFeed.getStyleClass().add("flatButton");
        
        GridFormBuilder grid = new GridFormBuilder();
        grid.addRowGenerics(lblName, tfName);
        grid.addRowGenerics(lblFeed, tfFeedPath, btnPreviewFeed, null);
        
        return grid.build();
    }

    private HBox buildButtons() {
        btnCancelRSS = new Button(rb.getString("Cancel"));
        btnCancelRSS.setId("btnCancelRSS");
        btnCancelRSS.setCancelButton(true);
        btnCancelRSS.getStyleClass().add("flatButton");
        
        btnSaveRSS = new Button(rb.getString("Save"));
        btnSaveRSS.setId("btnSalveRSS");
        btnSaveRSS.setDefaultButton(true);
        btnSaveRSS.getStyleClass().add("flatButton");
        
        btnDeleteRSS = new Button(rb.getString("Delete"));
        btnDeleteRSS.setId("btnDeleteRSS");
        btnDeleteRSS.getStyleClass().add("flatButton");
        btnDeleteRSS.setVisible(false);
        
        HBox hbox = new HBox();
        hbox.getChildren().addAll(btnDeleteRSS, btnCancelRSS, btnSaveRSS);
        hbox.getStyleClass().add("buttonBar");
        hbox.setAlignment(Pos.CENTER_RIGHT);
        
        return hbox;
    }
    
    private VBox buildViewer() {
        Separator separator = new Separator();
        
        feedView = new HBox();
        feedView.setPrefWidth(VIEWER_SIZE);
        feedView.setPrefHeight(VIEWER_SIZE);
        feedView.getStyleClass().add("feedView");
        
        feedImageView = new ImageView();
        //feedImageView.setFitWidth(VIEWER_SIZE);
        //feedImageView.setFitHeight(VIEWER_SIZE);

        feedImageView.setPreserveRatio(true);
        feedImageView.setCache(true);
        /*feedImageView.setSmooth(true);
        feedImageView.getStyleClass().add("Img");*/
        feedImageView.getStyleClass().add("feedImageView");
        // feedImageView.getProperties().
        
        feedContentView = new VBox();
        feedTitleText = new Label();
        feedContentText = new Label();
        feedDateText = new Label();
        
        feedTitleText.getStyleClass().add("feedTitleText");
        feedContentText.getStyleClass().add("feedContentText");
        feedDateText.getStyleClass().add("feedDateText");
        
        feedContentView.getChildren().add(feedTitleText);
        feedContentView.getChildren().add(feedContentText);
        feedContentView.getChildren().add(feedDateText);
        
        feedView.getChildren().add(feedImageView);
        feedView.getChildren().add(feedContentView);
        
        btnPlay = new Button(rb.getString("Play"));
        btnPlay.setId("btnPlayFeed");
        btnPlay.getStyleClass().add("flatButton");
        setBtToPlay();
        
        VBox vbox = new VBox(separator, feedView, btnPlay);
        vbox.getStyleClass().add("viewPane");
        
        return vbox;
    }

    public Integer getRSSId() {
        try {
            return Integer.parseInt(tfId.getText());
        } catch (Exception nex) {
            return null;
        }
    }
    
    public TextField getTfId() {
        return tfId;
    }

    public void setTfId(TextField tfId) {
        this.tfId = tfId;
    }

    public MaskTextField getTfName() {
        return tfName;
    }

    public void setTfName(MaskTextField tfName) {
        this.tfName = tfName;
    }
    
    public TextField getTfFeedPath() {
        return tfFeedPath;
    }

    public void setTfFeedPath(TextField tfFeedPath) {
        this.tfFeedPath = tfFeedPath;
    }

    public Button getBtnPreviewFeed() {
        return btnPreviewFeed;
    }

    public void setBtnPreviewFeed(Button btnPreviewFeed) {
        this.btnPreviewFeed = btnPreviewFeed;
    }

    public Button getBtnCancelRSS() {
        return btnCancelRSS;
    }

    public void setBtnCancelRSS(Button btnCancelRSS) {
        this.btnCancelRSS = btnCancelRSS;
    }

    public Button getBtnSaveRSS() {
        return btnSaveRSS;
    }

    public void setBtnSaveRSS(Button btnSaveRSS) {
        this.btnSaveRSS = btnSaveRSS;
    }

    public HBox getFeedView() {
        return feedView;
    }
    
    public void setFeedView(HBox feedView) {
        this.feedView = feedView;
    }
    
    public ImageView getFeedImageView() {
        return feedImageView;
    }
    
    public void setFeedImageView(ImageView feedImageView) {
        this.feedImageView = feedImageView;
    }
    
    public VBox getFeedContentView() {
        return feedContentView;
    }
    
    public void setFeedContentView(VBox feedContentView) {
        this.feedContentView = feedContentView;
    }
    
    public Label getFeedTitleText() {
        return feedTitleText;
    }
    
    public void setFeedTitleText(Label feedTitleText) {
        this.feedTitleText = feedTitleText;
    }
    
    public Label getFeedContentText() {
        return feedContentText;
    }
    
    public void setFeedContentText(Label feedContentText) {
        this.feedContentText = feedContentText;
    }
    
    public Label getFeedDateText() {
        return feedDateText;
    }
    
    public void setFeedDateText(Label feedDateText) {
        this.feedDateText = feedDateText;
    }
    
    public Button getBtnPlay() {
        return btnPlay;
    }

    public void setBtnPlay(Button btnPlay) {
        this.btnPlay = btnPlay;
    }

    public Button getBtnDeleteRSS() {
        return btnDeleteRSS;
    }

    public void setBtnDeleteRSS(Button btnDeleteRSS) {
        this.btnDeleteRSS = btnDeleteRSS;
    }
    
    public RSS loadRSSFromPanel() {
        Integer id = null;
        if(!tfId.getText().trim().isEmpty()) {
            id = Integer.valueOf(tfId.getText());
        }
        
        String name = null;
        if(!tfName.getText().trim().isEmpty()) {
            name = tfName.getText();
        }
        
        String feedPath = null;
        if(!tfFeedPath.getText().trim().isEmpty()) {
            feedPath = tfFeedPath.getText();
        }
        
        return new RSS(id, name, feedPath);
    }

    public void resetForm() {
        tfId.setText("");
        tfName.setText("");
        tfFeedPath.setText("");
        feedView.setVisible(false);
        btnDeleteRSS.setVisible(false);
    }

    public void setBtToPlay() {
        btnPlay.setText(rb.getString("Play"));
    }

    public void setBtToStop() {
        btnPlay.setText(rb.getString("Stop"));
    }
    
    public void refreshTable(List<RSS> listEntity) {
        table.reload(listEntity);   
    }
    
    public EntityTable getTable() {
        return table;
    }
    
    public void loadRSSToEdit(RSS RSS) {
        if(RSS.getId() != null) {
            tfId.setText(RSS.getId().toString());
            btnDeleteRSS.setVisible(true);
        }
        tfName.setText(RSS.getName());
        tfFeedPath.setText(RSS.getPath());
    }
    
}
