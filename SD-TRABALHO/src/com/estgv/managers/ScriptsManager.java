package com.estgv.managers;

import com.estgv.interfaces.ChargerInterface;
import com.estgv.interfaces.ScriptsInterface;
import com.estgv.models.Script;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class ScriptsManager extends UnicastRemoteObject implements ScriptsInterface
{
    private static final long serialVersionUID = 1L;
    private ArrayList<Script> scriptArrayList = new ArrayList<Script>();

    public ScriptsManager() throws RemoteException {
    }

    private void Charge(Script script) throws  RemoteException {
        try{
            ChargerInterface chargerInterface = (ChargerInterface)Naming.lookup("rmi://localhost:5000/charger");

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(script);

            chargerInterface.upload("RequestScript_N" + script.id + ".txt",bos.toByteArray());
        }catch (Exception e) { e.printStackTrace(); }

    }

    private Script Read(Integer scriptId) throws RemoteException {
        Script rscript = null;

        try{
            ChargerInterface chargerInterface = (ChargerInterface)Naming.lookup("rmi://localhost:5000/charger");


            byte[] data = chargerInterface.download("RequestScript_N" + scriptId.toString() + ".txt");

            ByteArrayInputStream in = new ByteArrayInputStream(data);
            ObjectInputStream is = new ObjectInputStream(in);

            rscript = (Script)is.readObject();
        }catch (Exception e) { e.printStackTrace(); }
        return rscript;
    }

    @Override
    public Integer run(Script script) throws RemoteException {
        Script _newS = script.setId(scriptArrayList.size());
        this.scriptArrayList.add(_newS);

        Charge(script);
        Script chargerInfo = Read(script.id);

        return chargerInfo.id;
    }

    @Override
    public Script result(Integer scriptId) throws RemoteException {
        return null;
    }
}
