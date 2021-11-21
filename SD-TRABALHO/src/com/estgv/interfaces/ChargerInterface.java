package com.estgv.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChargerInterface extends Remote {
    public byte[] download(String fileName) throws
            RemoteException;

    public void upload(String fileName, byte[] buffer) throws
            RemoteException;
}
