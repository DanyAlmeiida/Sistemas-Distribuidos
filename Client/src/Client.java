import Models.Request;
import Models.Script;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    public static void main (String args[]) {
        Socket SocketServer = null;
        try{
            SocketServer = new Socket("localhost", 7896);
            ObjectInputStream in = new ObjectInputStream( SocketServer.getInputStream());
            ObjectOutputStream  out = new ObjectOutputStream ( SocketServer.getOutputStream());

            out.writeObject(new Script(new Request("write","1")));
            Script script = (Script) in.readObject();
            System.out.println("Received: "+ script.request.Type);

        }catch (UnknownHostException e){
            System.out.println("Sock:"+e.getMessage());
        }catch (EOFException e){
            System.out.println("IO:" + e.getMessage());
        }catch (IOException e){
            System.out.println("IO:"+e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if(SocketServer!=null)
                try {
                    SocketServer.close();
                }catch (IOException e){
                    System.out.println("close:"+e.getMessage());}
        }
    }


}
