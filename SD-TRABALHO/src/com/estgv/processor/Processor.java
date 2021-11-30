package com.estgv.processor;

import com.estgv.interfaces.ObjectRegistryInterface;
import com.estgv.managers.ScriptsManager;
import com.estgv.models.ProcessorInfo;
import com.estgv.registry.ObjectRegistry;

import java.lang.management.ManagementFactory;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.UUID;

import com.estgv.threads.ResourcesThread;
import com.sun.management.OperatingSystemMXBean;

public class Processor {
    public static Registry r = null;
    public static Integer port;
    public static ScriptsManager scriptsManager;

    public Processor(Integer port)
    {

        try {
            r = LocateRegistry.createRegistry(2023);
            r.rebind("registry", new ObjectRegistry());
        } catch (RemoteException remoteException) {
            remoteException.printStackTrace();
        }

        try{
            r= LocateRegistry.createRegistry(port);
        }catch (RemoteException e){
            e.printStackTrace();
        }

        try{
            String pid = UUID.randomUUID().toString();
            String rebind = "scripts";
            ObjectRegistryInterface objectRegistryInterface = (ObjectRegistryInterface) Naming.lookup("rmi://localhost:2023/registry");
            objectRegistryInterface.add(pid,new ProcessorInfo("rmi://localhost:"+port+"/" + rebind,0.0));

            scriptsManager = new ScriptsManager();
            r.rebind("scripts", scriptsManager );
            System.out.println("server ready");


            ResourcesThread  resourcesThread = new ResourcesThread(pid);
            resourcesThread.start();


        }catch(Exception e) {
            System.out.println("server main " + e.getMessage());
        }






    }

    /**
     * Run this class as an application.
     */
    public static void main(String[] args)
    {
        Processor server = new Processor(2022);
    }
}
