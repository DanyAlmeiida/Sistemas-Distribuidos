package com.estgv.replica;

import com.estgv.managers.ProcessorManager;
import com.estgv.processor.Processor;
import com.estgv.registry.RMIRegistry;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.UUID;

public class ProcessorReplicaManager {
    public ProcessorReplicaManager() {
    }

    public static void main(String[] args) {
        Registry r = null;

        try {
            r = LocateRegistry.createRegistry(2024);
        } catch (RemoteException var4) {
            var4.printStackTrace();
        }

        try {
            Remote replica_manager = new ProcessorManager();
            r.rebind("processor_manager", replica_manager);
            System.out.println("Processor Replica Manager server ready");

        } catch (Exception var3) {
            System.out.println("Processor Replica Manager server main " + var3.getMessage());
        }

    }

}
