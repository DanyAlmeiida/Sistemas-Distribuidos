package com.estgv.processor;

import com.estgv.interfaces.ObjectRegistryInterface;
import com.estgv.interfaces.ProcessorReplicaManagerInterface;
import com.estgv.managers.ScriptsManager;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import com.estgv.models.ProcessorInfo;
import com.estgv.threads.ResourcesThread;
import com.estgv.threads.ScriptRequestsThreads;


public class Processor {
    public static Registry r = null;
    public static Integer port;
    public static ScriptsManager scriptsManager;

    public Processor(String uuid, Integer port)
    {
        try{
            r= LocateRegistry.createRegistry(port);
        }catch (RemoteException e){
            e.printStackTrace();
        }

        try{
            String address = "rmi://localhost:" + port + "/scripts";
            ObjectRegistryInterface objectRegistryInterface = (ObjectRegistryInterface)Naming.lookup("rmi://localhost:2023/registry");
            objectRegistryInterface.add(uuid,address);

            ProcessorReplicaManagerInterface processorReplicaManagerInterface = (ProcessorReplicaManagerInterface)Naming.lookup("rmi://localhost:2024/processor_manager");
            processorReplicaManagerInterface.add(new ProcessorInfo(
                    uuid,
                    address ,
                    0.0
            ));

            scriptsManager = new ScriptsManager(uuid);
            r.rebind("scripts", scriptsManager );
            System.out.println("server ready");


            ResourcesThread  resourcesThread = new ResourcesThread(uuid);
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
        Processor server = new Processor(args[0],Integer.parseInt(args[1]));
    }
}
