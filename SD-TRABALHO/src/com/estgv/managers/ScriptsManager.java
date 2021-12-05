package com.estgv.managers;

import com.estgv.client.SFTPClient;
import com.estgv.interfaces.ChargerInterface;
import com.estgv.interfaces.ProcessorReplicaManagerInterface;
import com.estgv.interfaces.ScriptsInterface;
import com.estgv.models.Script;
import com.estgv.models.ScriptQueue;
import com.estgv.processor.Processor;
import com.estgv.registry.RMIRegistry;
import com.estgv.replica.ProcessorReplicaManager;
import com.estgv.threads.ScriptRequestsThreads;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.sun.jmx.remote.internal.ArrayQueue;
import org.apache.commons.compress.utils.IOUtils;

import java.io.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;

public class ScriptsManager extends UnicastRemoteObject implements ScriptsInterface
{
    public double cpu_usage = 0.0;
    private static final long serialVersionUID = 1L;
    private ScriptQueue scriptQueue = new ScriptQueue();

    public ScriptsManager() throws RemoteException {

        //region Process-Pending-Scripts

        Thread tProcess = (new Thread() {
            public void run() {
                while (true) {
                    try {
                        System.out.println("Sleeping for 1000 seconds");
                        sleep(1000);
                        Script script = scriptQueue.get();
                        if (script != null) {
                            System.out.println("Executing " + script.script);
                            SFTPClient  sftpClient = new SFTPClient();

                            //region read-file-script to execute
                            String sb = sftpClient.get_content(script.script);
                            //endregion

                            ProcessBuilder processBuilder = new ProcessBuilder("cmd","/c",sb);
                            Process process = processBuilder.start();
                            StringBuilder output = new StringBuilder();
                            try (BufferedReader reader = new BufferedReader(
                                    new InputStreamReader(process.getInputStream()))) {

                                String line;
                                while ((line = reader.readLine()) != null) {
                                    output.append(line + "\n");
                                }

                                int exitVal = process.waitFor();
                                if (exitVal == 0) {
                                    System.out.println("Success!");
                                    System.out.println(output);
                                    System.exit(0);
                                } else {
                                    System.out.println("error");
                                }

                            }
                        }
                    } catch (InterruptedException | IOException e) {
                        e.printStackTrace();
                    } catch (JSchException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        tProcess.start();

        //endregion
    }

    @Override
    public void set_cpu_usage(double cpu_usage) throws RemoteException {
        this.cpu_usage = cpu_usage;
    }

    @Override
    public String run(Script script) throws RemoteException {
        Script _newS =  script.setUuid(UUID.randomUUID().toString());


        scriptQueue.put(_newS);

        return _newS.uuid;
    }

    @Override
    public Script result(Integer scriptId) throws RemoteException {
        return null;
    }

    @Override
    public Script get_pending_script() throws RemoteException {
        return (Script) scriptQueue.get();
    }

}
