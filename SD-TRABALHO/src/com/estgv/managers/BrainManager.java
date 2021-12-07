package com.estgv.managers;

import com.estgv.interfaces.BrainInterface;
import com.estgv.models.ResultModel;
import com.estgv.models.Script;

import java.rmi.RemoteException;
import java.rmi.server.UID;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class BrainManager extends UnicastRemoteObject implements BrainInterface {
    public ArrayList<Script> scriptsResultLst= new ArrayList<>();
    public BrainManager() throws RemoteException {
    }

    @Override
    public Script get_result(String uuid) throws RemoteException {
        return scriptsResultLst.stream().filter(x -> x.uuid.equals(uuid)).findFirst().orElse(null);
    }

    @Override
    public void set_result(Script script) throws RemoteException {
        scriptsResultLst.add(script);
    }
}
