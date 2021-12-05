package com.estgv;

import com.estgv.client.SFTPClient;
import com.estgv.processor.Processor;
import com.estgv.registry.RMIRegistry;
import com.estgv.replica.ProcessorReplicaManager;
import com.jcraft.jsch.*;
import org.apache.commons.net.ftp.FTPClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.UUID;

public class Test {

    public static void main(String[] args){

        Thread t = (new Thread() {
            public void run() {
                RMIRegistry.main(new String[0]);
                ProcessorReplicaManager.main(new String[0]);
                Processor.main(new String[]{String.valueOf(UUID.randomUUID()),"2025"});
                //Processor.main(new String[]{String.valueOf(UUID.randomUUID()),"2026"});
                //Processor.main(new String[]{String.valueOf(UUID.randomUUID()),"2027"});
            }
        });
        t.start();

        /*try {
            remoteLs();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }
    public static void remoteLs() throws JSchException, IOException {
        JSch js = new JSch();
        Session s = js.getSession("tester", "192.168.1.93", 22);
        s.setPassword("password");
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        s.setConfig(config);
        s.connect();

        Channel c = s.openChannel("exec");
        ChannelExec ce = (ChannelExec) c;

        ce.setCommand("ls -l");
        ce.setErrStream(System.err);

        ce.connect();

        BufferedReader reader = new BufferedReader(new InputStreamReader(ce.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }

        ce.disconnect();
        s.disconnect();
        while (ce.getExitStatus() == -1){
            try{Thread.sleep(1000);}catch(Exception e){System.out.println(e);}
        }
        System.out.println("Exit code: " + ce.getExitStatus());

    }
}
