package com.estgv.client;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;
import java.nio.file.Path;

public class FTPHelper {
     FTPClient ftp;

    public  FTPHelper() {
        try
        {
            ftp = new FTPClient();
            ftp.connect("localhost");
            ftp.login("sd-user","12345");
            ftp.setFileTransferMode(ftp.BINARY_FILE_TYPE);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    public void execute_command(String command){
        //ftp.sendCommand("");
    }
    public boolean uploadFile(Path filePath)
            throws Exception {
        boolean res = false;
        try(InputStream input = new FileInputStream(filePath.toFile().getAbsolutePath())){
            ftp.changeWorkingDirectory("tpsd");
            res = this.ftp.storeFile(String.valueOf(filePath.getFileName()), input);
            int reply = ftp.getReplyCode();

            if(!FTPReply.isPositiveCompletion(reply)) {
                System.out.println("upload failed!");
            }
        }catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
        return res;
    }

    public void disconnect() throws IOException {
        ftp.logout();
        ftp.disconnect();
    }
}
