package com.estgv.charger;
import com.estgv.managers.ChargerManager;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Charger{
    public static Registry r = null;
    public static ChargerManager chargerManager;
    public Charger(){
        try{
            r = LocateRegistry.createRegistry(5000);
        }catch(RemoteException a){
            a.printStackTrace();
        }

        try{
            chargerManager = new ChargerManager();
            r.rebind("charger", chargerManager);

            System.out.println("Charger ready");
        }catch(Exception e) {
            System.out.println("Charger main " + e.getMessage());
        }
    }
    public static void main(String[] args){
        Charger charger = new Charger();
    }

}


