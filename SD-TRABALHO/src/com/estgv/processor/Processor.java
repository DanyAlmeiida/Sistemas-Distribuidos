package com.estgv.processor;

import com.estgv.managers.ScriptsManager;
import com.estgv.registry.ObjectRegistry;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Processor {
    public static Registry r = null;
    public static ScriptsManager scriptsManager;

    public Processor()
    {
        try{
            r= LocateRegistry.createRegistry(2022);
        }catch (RemoteException e){
            e.printStackTrace();
        }

        try{
            scriptsManager = new ScriptsManager();
            r.rebind("scripts", scriptsManager );
            System.out.println("server ready");

        }catch(Exception e) {
            System.out.println("server main " + e.getMessage());
        }

        try {
            r = LocateRegistry.createRegistry(2023);
            r.rebind("registry", new ObjectRegistry());
        } catch (RemoteException remoteException) {
            remoteException.printStackTrace();
        }

    }

    /**
     * Run this class as an application.
     */
    public static void main(String[] args)
    {
        Processor server = new Processor();
    }
}
