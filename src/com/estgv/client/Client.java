package com.estgv.client;

import com.estgv.models.Request;
import com.estgv.models.ResultModel;
import com.estgv.models.Script;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    public final static String SERVER_HOSTNAME = "localhost";
    public final static int COMM_PORT = 5050;  // socket port for client comms

    private Socket socket;
    private ResultModel<Script> payload;

    /** Default constructor. */
    public Client()
    {
        try
        {
            this.socket = new Socket(SERVER_HOSTNAME, COMM_PORT);
            InputStream iStream = this.socket.getInputStream();
            OutputStream oStream = this.socket.getOutputStream();

            ObjectOutputStream oos = new ObjectOutputStream(oStream);
            oos.writeObject(new Script(0,new Request("A")));

            ObjectInputStream oiStream = new ObjectInputStream(iStream);
            this.payload = (ResultModel<Script>) oiStream.readObject();



        }
        catch (UnknownHostException uhe)
        {
            System.out.println("Don't know about host: " + SERVER_HOSTNAME);
            System.exit(1);
        }
        catch (IOException ioe)
        {
            System.out.println("Couldn't get I/O for the connection to: " +
                    SERVER_HOSTNAME + ":" + COMM_PORT);
            System.exit(1);
        }
        catch(ClassNotFoundException cne)
        {
            System.out.println("Wanted class TcpPayload, but got class " + cne);
        }
        if(this.payload.success == true)
            System.out.println(this.payload.object.PrintInfo());
        else
            System.out.println(this.payload.errors);
    }

    /**
     * Run this class as an application.
     */
    public static void main(String[] args)
    {
        Client client = new Client();
    }


}
