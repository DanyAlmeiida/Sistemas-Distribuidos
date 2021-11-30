package com.estgv.threads;

import com.estgv.interfaces.ProcessorReplicaManagerInterface;
import com.estgv.interfaces.ScriptsInterface;
import com.estgv.models.Script;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class ScriptRequestsThreads extends Thread{
    public String serverAddress ;
    public void run()
    {
        try {
            ScriptsInterface scriptsInterface = (ScriptsInterface) Naming.lookup(serverAddress);
            Script script = scriptsInterface.pending().getLast();

        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
