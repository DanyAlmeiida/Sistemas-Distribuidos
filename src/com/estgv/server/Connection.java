package com.estgv.server;

import com.estgv.models.Request;
import com.estgv.models.ResultModel;
import com.estgv.models.Script;

import java.io.*;
import java.net.Socket;

public class Connection extends Thread {
    DataOutputStream out;
    DataInputStream ois;
    ObjectInputStream objectInputStream;
    ObjectOutputStream objectOutputStream;
    Socket clientSocket;
    ScriptsStorage storage = new ScriptsStorage();

    public Connection(Socket aClientSocket) {
        try {
            // inicializa variáveis
            clientSocket = aClientSocket;
            objectInputStream = new ObjectInputStream( clientSocket.getInputStream());
            objectOutputStream = new ObjectOutputStream ( clientSocket.getOutputStream());
            this.start(); //executa o método run numa thread separada
        } catch(IOException e) {
            System.out.println("Connection:"+e.getMessage());
        }
    }

    public ResultModel<Script> validate(Script script){
        if( script == null)
            return new ResultModel<Script>("Object Not Defined");

        if(script.request == null)
            return new ResultModel<Script>("Object Request Not Defined");

        if(script.request == null)
            return new ResultModel<Script>("Object Request Not Defined");

        Request request = (Request) script.request;

        if(script.request.Type == null)
            return new ResultModel<Script>("Object Request Type Not Defined");

        if(request.Type.equals("write"))
        {
            if(request.Variable == null || script.request.Variable.equals(""))
                return new ResultModel<Script>("Object Request Variable Not Defined");

            if(script.request.Value == null || script.request.Value.equals(""))
                return new ResultModel<Script>("Object Request Value Not Defined");
        }
        else if(request.Type.equals("read"))
        {
            if(request.Variable == null || script.request.Variable.equals(""))
                return new ResultModel<Script>("Object Request Variable Not Defined");

            if(script.request.Value != null && !script.request.Value.equals(""))
                return new ResultModel<Script>("Object Request Value Cant be Defined On Read Type Operations");
        }
        else
        {
            return new ResultModel<Script>("Object Request Type Must be defined with (read/write)");
        }

        return new ResultModel<>(script);
    }

    public ResultModel<Script> HandleRequestedScript(Script script) throws Exception {

        switch (script.request.Type) {
            case "read":
                script.result = script.request.Type + " " + script.request.Variable;
                script.result += " = " + ScriptsStorage.get(script.id).request.Value;
                return new ResultModel<>(script);
            case "write":
                script.id = ScriptsStorage.scriptsList.size();
                script.result = script.request.Type + " " + script.request.Variable;
                ScriptsStorage.add(script);
                return new ResultModel<>(script);
            default:
                return new ResultModel<>("Script Request Type not defined");
        }
    }
    public void run(){
        try {
            ResultModel<Script> resScriptValidation = validate((Script) objectInputStream.readObject());
            if(!resScriptValidation.success)
                objectOutputStream.writeObject(resScriptValidation);
            else
                objectOutputStream.writeObject(HandleRequestedScript(resScriptValidation.object));

        } catch(EOFException e) {
            System.out.println("EOF:" + e.getMessage());
        } catch(IOException e) {
            System.out.println("IO:" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            try {
                clientSocket.close();
            }catch (IOException e){
                /*close failed*/
            }
        }
    }
}
