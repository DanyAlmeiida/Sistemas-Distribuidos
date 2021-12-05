package com.estgv.threads;

import com.estgv.interfaces.ProcessorReplicaManagerInterface;
import com.estgv.interfaces.ScriptsInterface;
import com.estgv.models.Script;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Queue;

public class ScriptRequestsThreads extends Thread{
    public String serverAddress ;
    public void run()
    {
        try {
            ScriptsInterface scriptsInterface = (ScriptsInterface) Naming.lookup(serverAddress);
            Script script = scriptsInterface.get_pending_script();
            System.out.println("Sleeping for 200 seconds");
            sleep(200);
            if( script != null) {
                System.out.println("Executing " + script.script);

                ProcessBuilder processBuilder = new ProcessBuilder();

                //contacts the charger

                processBuilder.command(script.script);

                Process process = processBuilder.start();
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(process.getInputStream()))) {

                    String line;

                    while ((line = ((BufferedReader) reader).readLine()) != null) {
                        System.out.println(line);
                    }

                }
            }
        } catch (InterruptedException | IOException | NotBoundException e) {
            e.printStackTrace();
        }
    }
}
