package com.estgv.registry;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIRegistry {
    public RMIRegistry() {
    }

    public static void main(String[] args) {
        Registry r = null;

        try {
            r = LocateRegistry.createRegistry(2023);
        } catch (RemoteException var4) {
            var4.printStackTrace();
        }

        try {
            ObjectRegistry objectList = new ObjectRegistry();
            r.rebind("registry", objectList);
            System.out.println("Object server ready");
        } catch (Exception var3) {
            System.out.println("Object server main " + var3.getMessage());
        }

    }
}


