package com.estgv.interfaces;

import com.estgv.models.ResultModel;
import com.estgv.models.Script;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BrainInterface extends Remote {
    Script get_result(String uuid) throws RemoteException;
    void set_result(Script script) throws RemoteException;
}
