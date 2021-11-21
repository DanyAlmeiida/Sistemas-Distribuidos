package com.estgv.managers;

import com.estgv.interfaces.ChargerInterface;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ChargerManager extends UnicastRemoteObject implements ChargerInterface {

    private String name;

    public ChargerManager() throws RemoteException {
        super();

    }
    public ChargerManager(String s) throws RemoteException {
        super();
        name = s;
    }

    public byte[] download(String fileName){

        try {
            File file = new File(fileName);
            byte buffer[] = new byte[(int)file.length()];
            BufferedInputStream input = new
                    BufferedInputStream(new FileInputStream(fileName));
            input.read(buffer,0,buffer.length);
            input.close();
            return(buffer);
        } catch(Exception e){
            System.out.println("FileImpl: "+e.getMessage());
            e.printStackTrace();
            return(null);
        }
    }

    public void upload(String fileName, byte[] buffer){

        try {
            File file = new File(fileName);
            BufferedOutputStream output = new
                    BufferedOutputStream(new FileOutputStream(fileName));
            output.write(buffer,0,buffer.length);
            output.flush();
            output.close();

        } catch(Exception e) {
            System.out.println("FileImpl: "+e.getMessage());
            e.printStackTrace();

        }
    }


}