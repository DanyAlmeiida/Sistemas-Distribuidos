package com.estgv.managers;

import com.estgv.interfaces.ChargerInterface;
import com.estgv.interfaces.ScriptsInterface;
import com.estgv.models.Script;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.UUID;

public class ScriptsManager extends UnicastRemoteObject implements ScriptsInterface
{
    public double cpu_usage = 0.0;
    private static final long serialVersionUID = 1L;
    private ArrayList<Script> scriptArrayList = new ArrayList<Script>();

    public ScriptsManager() throws RemoteException {
    }

    private void Charge(Script script) throws  RemoteException {

    }

    private Script Read(String scriptId) throws RemoteException {
        Script rscript = null;


        return rscript;
    }


    @Override
    public void set_cpu_usage(double cpu_usage) throws RemoteException {
        this.cpu_usage = cpu_usage;
    }

    @Override
    public String run(Script script) throws RemoteException {
        System.out.println("REQUEST FROM CLIENT - CPU USAGE : " +  cpu_usage);
        Script _newS = script.setUuid(UUID.randomUUID().toString());
        this.scriptArrayList.add(_newS);

        Charge(script);
        Script chargerInfo = Read(script.uuid);

        return _newS.uuid;
    }

    @Override
    public Script result(Integer scriptId) throws RemoteException {
        return null;
    }
}
