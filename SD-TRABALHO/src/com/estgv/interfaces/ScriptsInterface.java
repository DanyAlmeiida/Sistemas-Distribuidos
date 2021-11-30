package com.estgv.interfaces;

import com.estgv.models.Script;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ScriptsInterface extends Remote
{
    void set_cpu_usage(double cpu_usage) throws RemoteException;

    String run(Script script) throws RemoteException;

    Script result(Integer scriptId) throws RemoteException;
}
