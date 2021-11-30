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
    static HashMap<String, ProcessorInfo> hash = new HashMap();

    public ObjectRegistry() throws RemoteException {
    }

    public void add(String objectID, ProcessorInfo processorInfo) throws RemoteException {
        hash.put(objectID, processorInfo);
    }

    public ProcessorInfo resolve(String objectID) throws RemoteException {
        return  hash.get(objectID);
    }

    public String resolve() throws RemoteException {
        Map.Entry<String, ProcessorInfo> min = null;
        for (Map.Entry<String, ProcessorInfo> entry : hash.entrySet()) {
            if (min == null || min.getValue().cpu_usage > entry.getValue().cpu_usage) {
                min = entry;
            }
        }

        return min.getValue().serverAddress;
    }


    @Override
    public void set_cpu_usage(String processorObejctId, double cpu_usage) throws RemoteException {
        ProcessorInfo processorInfo = hash.get(processorObejctId);
        processorInfo.cpu_usage = cpu_usage;
        hash.replace(processorObejctId, processorInfo);
    }
}
