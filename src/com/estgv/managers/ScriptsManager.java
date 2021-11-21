package com.estgv.managers;

import com.estgv.interfaces.ScriptsInterface;
import com.estgv.models.Script;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class ScriptsManager extends UnicastRemoteObject implements ScriptsInterface
{
    private static final long serialVersionUID = 1L;
    private ArrayList<Script> scriptArrayList = new ArrayList<Script>();

    public ScriptsManager() throws RemoteException {
    }

    @Override
    public Integer run(Script script) throws RemoteException {
        Script _newS = script.setId(scriptArrayList.size());
        this.scriptArrayList.add(_newS);
        return _newS.id;
    }

    @Override
    public Script result(Integer scriptId) throws RemoteException {
        return null;
    }
}
