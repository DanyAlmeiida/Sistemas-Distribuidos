package com.estgv.registry;

import com.estgv.interfaces.ObjectRegistryInterface;
import com.estgv.models.ProcessorInfo;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class ObjectRegistry extends UnicastRemoteObject implements ObjectRegistryInterface {
    private static final long serialVersionUID = 1L;
    static HashMap<String, String> hash = new HashMap();

    public ObjectRegistry() throws RemoteException {
    }

    public void add(String objectID, String address) throws RemoteException {
        hash.put(objectID, address);
    }

    public String resolve(String objectID) throws RemoteException {
        return  hash.get(objectID);
    }

}
