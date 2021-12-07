package com.estgv.managers;

import com.estgv.client.SFTPClient;
import com.estgv.interfaces.BrainInterface;
import com.estgv.interfaces.ProcessorReplicaManagerInterface;
import com.estgv.interfaces.ScriptsInterface;
import com.estgv.models.ProcessorInfo;
import com.estgv.models.Script;
import com.estgv.models.ScriptQueue;
import com.jcraft.jsch.JSchException;

import java.io.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.UUID;

public class ScriptsManager extends UnicastRemoteObject implements ScriptsInterface
{
    private String uuid = "";
    public double cpu_usage = 0.0;
    private static final long serialVersionUID = 1L;
    private ScriptQueue scriptQueue = new ScriptQueue();

    public ScriptsManager(String id_Server) throws RemoteException {
        this.uuid = id_Server;
        //region Process-Pending-Scripts

        Thread tProcess = (new Thread() {
            public void run() {
                while (true) {
                    try {
                        System.out.println("Sleeping for 1000 seconds");
                        sleep(1000);
                        Script script = scriptQueue.get();
                        if (script != null) {
                            process_script(script);
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
    private void process_script(Script script) throws IOException, JSchException {
        SFTPClient  sftpClient = new SFTPClient();

        //region read-file-script to execute
        String sb = sftpClient.get_content(script.file);
        //endregion

        ProcessBuilder processBuilder = new ProcessBuilder("cmd","/c",sb);
        Process process = processBuilder.start();
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line + System.lineSeparator());
            }
            script.processed_by = uuid;
            script.result = output.toString();
            BrainInterface brainInterface = (BrainInterface) Naming.lookup("rmi://localhost:2030/resultmodels");
            brainInterface.set_result(script);

        } catch (NotBoundException | RemoteException | MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void set_cpu_usage(double cpu_usage) throws RemoteException {
        this.cpu_usage = cpu_usage;
    }

    @Override
    public String run(Script script) throws RemoteException {
        Script _newS =  script.setUuid(UUID.randomUUID().toString());

        if(this.cpu_usage <  0.05)
        {
            try {
                process_script(script);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSchException e) {
                e.printStackTrace();
            }

        }else
        {
            scriptQueue.put(_newS);

        }

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
