package com.estgv.interfaces;

import com.estgv.models.ProcessorInfo;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ObjectRegistryInterface extends Remote
{
    void add(String var1, ProcessorInfo var2) throws RemoteException;

    ProcessorInfo resolve(String var1) throws RemoteException;

    String resolve() throws RemoteException;

    void set_cpu_usage(String processorObejctId, double cpu_usage) throws RemoteException ;

}
