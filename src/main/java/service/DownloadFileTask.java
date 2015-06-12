package service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ResourceBundle;
import javafx.concurrent.Task;

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
  */
public class DownloadFileTask extends Task<Integer> {
    private final ResourceBundle rb;
    private final File origFile;
    private final File destFile;
    
    public DownloadFileTask(ResourceBundle rb, File origFile, File destFile) {
        this.rb = rb;
        this.origFile = origFile;
        this.destFile = destFile;
    }
    
    @Override
    protected Integer call() throws Exception {
        updateTitle(this.origFile.getName());
        updateMessage(rb.getString("FILE_CHECK"));
        try {
            filesIsOK();
            diskIsOK();
            createDestFile();
            
            //Start Thread to copy file
        } catch(Exception e) {
            System.out.println(e.getMessage());
            updateMessage(e.getMessage());
            this.cancel();
        }
       
        updateMessage(rb.getString("FILE_COPY"));
        
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                copyFile();
            }
        });
        thread.setDaemon(true);
        thread.start();
        
        double orgiSize = origFile.length();
        double destSize;
        long cnt = 0;
        do {
            destSize = destFile.length();
            updateProgress((100*(destSize/orgiSize)), 100);
            Thread.sleep(20);
            cnt++;
        } while(orgiSize > destSize);

        updateMessage(rb.getString("FILE_CPSUCCESS"));
        Thread.sleep(2000);

        return null;
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
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        
        try {
            inChannel = new FileInputStream(origFile).getChannel();
            outChannel = new FileOutputStream(destFile).getChannel();

            inChannel.transferTo(0, inChannel.size(), outChannel);
            
            //Banner indicando OK
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
}