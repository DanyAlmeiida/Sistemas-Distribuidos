package com.estgv.interfaces;

import com.estgv.models.ProcessorInfo;
import com.estgv.processor.Processor;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ProcessorReplicaManagerInterface extends Remote {

    void add(ProcessorInfo processorInfo) throws RemoteException;
    ProcessorInfo get() throws RemoteException;

    void set_cpu_usage(String processorObejctId, double cpu_usage) throws RemoteException ;

    double get_cpu_usage(String processorObejctId) throws RemoteException;
}
