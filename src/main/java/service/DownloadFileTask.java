package service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import org.controlsfx.control.Notifications;

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
  */
public class DownloadFileTask extends Task<Integer> {
    private final ResourceBundle rb;
    private final File origFile;
    private final File destFile;
    
    private FileChannel inChannel = null;
    private FileChannel outChannel = null;
    
    public DownloadFileTask(ResourceBundle rb, File origFile, File destFile) {
        this.rb = rb;
        this.origFile = origFile;
        this.destFile = destFile;
    }
    
    @Override
    protected Integer call() throws Exception {
        updateTitle(this.origFile.getName());
        updateMessage(rb.getString("FILE_CHECK"));
        
        if(filesEquals()){
            return 0;
        }
        
        try {
            filesIsOK();
            diskIsOK();
            createDestFile();
        } catch(Exception e) {
            System.out.println(e.getMessage());
            updateMessage(e.getMessage());
            return 1;
        }
       
        updateMessage(rb.getString("FILE_COPY"));
        //Start Thread to copy file
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                copyFile();
            }
        });
        thread.setDaemon(true);
        thread.start();
        
        //Update download file progress
        double orgiSize = origFile.length();
        double destSize = 0;
        do {
            destSize = destFile.length();
            updateProgress((100*(destSize/orgiSize)), 100);
            if(thread.getState() == Thread.State.TERMINATED) {
                Thread.sleep(20);
                destSize = destFile.length();
                updateProgress((100*(destSize/orgiSize)), 100);
                break;
            }
            
            Thread.sleep(20);
        } while(orgiSize > destSize);

        //File copy OK
        if(destSize >= orgiSize) {
            final String msg = rb.getString("FILE_CPSUCCESS") + " " + origFile.getName();
            
            updateMessage(msg);
            Platform.runLater(new Runnable() {
            @Override
            public void run() {
                    Notifications.create().title(rb.getString("FILE_COPIED")).text(msg).position(Pos.TOP_RIGHT).showConfirm();
                }
            });
        }
        //File copy ERROR
        else {
            final String msg = rb.getString("FILE_CPERROR") + " " + origFile.getName();
            
            updateMessage(msg);
            Platform.runLater(new Runnable() {
            @Override
            public void run() {
                    Notifications.create().title(rb.getString("FILE_COPIED")).text(msg).position(Pos.TOP_RIGHT).showError();
                }
            });
        }
        
        Thread.sleep(2000);
        return 0;
    }

    private void filesIsOK() throws InterruptedException {
        if(origFile == null || destFile == null) {
            throw new IllegalArgumentException(rb.getString("ERR_NULL"));
        }
        
        if(!origFile.exists() || !origFile.isFile()) {
            throw new IllegalArgumentException(rb.getString("ERR_NEXIST"));
        }
        
        if(!origFile.canRead()) {
            throw new IllegalArgumentException(rb.getString("ERR_PERMISSIONS"));
        }
    }

    private void diskIsOK() throws InterruptedException, IOException {
        File disk = destFile.getParentFile();
        
        if(disk != null && !disk.exists()) {
            disk.mkdir();
            disk.createNewFile();
        }
        
        if(disk == null || !disk.exists() || !disk.isDirectory()) {
            throw new IllegalArgumentException(rb.getString("ERR_FILE_DISK"));
        }
        
        long origFileSize = origFile.length();
        long diskFileFreeSpace = disk.getFreeSpace();
        
        if(origFileSize >= diskFileFreeSpace) {
            throw new IllegalArgumentException(rb.getString("ERR_DISK_SIZE"));
        }
    }

    private void createDestFile() throws IOException {
        if(destFile.exists()) {
            throw new IllegalArgumentException(rb.getString("ERR_FILE_EXIST"));
        }
        
        destFile.createNewFile();
    }
    
    private void copyFile() {
        this.inChannel = null;
        this.outChannel = null;
        
        try {
            inChannel = new FileInputStream(origFile).getChannel();
            outChannel = new FileOutputStream(destFile).getChannel();

            inChannel.transferTo(0, inChannel.size(), outChannel);
        } catch(Exception e) {
  
        } finally {
            try {
                if(inChannel != null)
                    inChannel.close();
                if(outChannel != null)
                    outChannel.close();
            } catch(IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    @Override
    protected void cancelled() {
        // super.cancelled(); //To change body of generated methods, choose Tools | Templates.
        
        try {
            if(inChannel != null) {
                inChannel.close();
            }
            if(outChannel != null) {
                outChannel.close();
            }
            destFile.delete();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private boolean filesEquals() {
        return origFile.getAbsolutePath().equalsIgnoreCase(destFile.getAbsolutePath());
    }

    
}