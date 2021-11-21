package com.estgv.client;


import com.estgv.interfaces.ObjectRegistryInterface;
import com.estgv.interfaces.ScriptsInterface;
import com.estgv.models.Script;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Client {

    public Client()
    {
        ScriptsInterface scriptsInterface = null;
        ObjectRegistryInterface objectRegistryInterface = null;
        try{
            try {
                scriptsInterface = (ScriptsInterface) Naming.lookup("rmi://localhost:2022/scripts");
                objectRegistryInterface = (ObjectRegistryInterface) Naming.lookup("rmi://localhost:2023/registry");
            } catch (NotBoundException | RemoteException | MalformedURLException ex)
            {ex.printStackTrace(); }

            Script script = new Script("cls");
            Integer id = scriptsInterface.run(script);
            System.out.println(id.toString());
        }
        catch(Exception e)
        {System.out.println(e.getMessage()); }
    }


    /**
     * Run this class as an application.
     */
    public static void main(String[] args)
    {
        Client client = new Client();
    }


}
