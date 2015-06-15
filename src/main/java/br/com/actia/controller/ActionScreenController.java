package br.com.actia.controller;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.Node;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.TaskProgressView;
import service.DownloadFileTask;


/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public class ActionScreenController extends PersistenceController {
    private final ResourceBundle rb;
    private final TaskProgressView<DownloadFileTask> view;
    private final PopOver popOverView;
    private final Node ownerNode;
    private List<DownloadFileTask> lstDownloadTask;

    public ActionScreenController(AbstractController parent, Node ownerNode, ResourceBundle rb) {
        super(parent);
        loadPersistenceContext(((PersistenceController) getParentController()).getPersistenceContext());
        this.rb = rb;
        this.ownerNode = ownerNode;
        this.view = new TaskProgressView<>();
        this.popOverView = new PopOver(view);
        //this.popOverView.setArrowLocation(PopOver.ArrowLocation.RIGHT_BOTTOM);
        
        lstDownloadTask =  new LinkedList<>();
    }

    //Add New Download Task
    public void startDownload(File orginFile, File destFile) {
        DownloadFileTask dfTask = new DownloadFileTask(rb, orginFile, destFile);
        view.getTasks().add(dfTask);
        lstDownloadTask.add(dfTask);
        
        Thread thread = new Thread(dfTask);
        thread.setDaemon(true);
        thread.start();
    }

    public void showView() {
        if(!popOverView.isShowing())
            popOverView.show(ownerNode);
        else
            popOverView.hide();
    }
        
    public void closeView() {
        popOverView.hide();
    }
    
    @Override
    public void cleanUp() {
        closeView();
        super.cleanUp();
    }
}
