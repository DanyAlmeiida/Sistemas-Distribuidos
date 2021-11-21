package com.estgv.registry;

import com.estgv.interfaces.ObjectRegistryInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class ObjectRegistry extends UnicastRemoteObject implements ObjectRegistryInterface {
    private static final long serialVersionUID = 1L;
    static HashMap<String, String> hash = new HashMap();

    public ObjectRegistry() throws RemoteException {
    }

    public void add(String objectID, String serverAddress) throws RemoteException {
        hash.put(objectID, serverAddress);
    }

    public String resolve(String objectID) throws RemoteException {
        return (String)hash.get(objectID);
    }
}
