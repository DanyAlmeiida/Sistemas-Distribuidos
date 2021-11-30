package com.estgv.managers;

import com.estgv.interfaces.ChargerInterface;
import com.estgv.interfaces.ProcessorReplicaManagerInterface;
import com.estgv.interfaces.ScriptsInterface;
import com.estgv.models.Script;
import com.estgv.processor.Processor;
import com.estgv.registry.RMIRegistry;
import com.estgv.replica.ProcessorReplicaManager;
import com.sun.jmx.remote.internal.ArrayQueue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
    private LinkedList<Script> pendingScripts = new LinkedList<Script>();

    public ScriptsManager() throws RemoteException {
    }

    private void Charge(Script script) throws  RemoteException {

    }

    private Script Read(String scriptId) throws RemoteException {
        Script rscript = null;


        return rscript;
    }


    @Override
    public void set_cpu_usage(double cpu_usage) throws RemoteException {
        this.cpu_usage = cpu_usage;
    }

    @Override
    public String run(Script script) throws RemoteException {
        Script _newS =  script.setUuid(UUID.randomUUID().toString());
        Thread t = (new Thread() {
            public void run() {
                pendingScripts.add(script);
            }
        });
        t.start();

        pendingScripts.add(_newS);

        return _newS.uuid;
    }

    @Override
    public Script result(Integer scriptId) throws RemoteException {
        return null;
    }

    //region pendingScripts

    @Override //returns a list of pending scripts
    public LinkedList<Script> pending() throws RemoteException {
        return pendingScripts;
    }

    //endregion
}
