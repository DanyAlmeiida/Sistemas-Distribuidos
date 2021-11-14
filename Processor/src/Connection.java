import Models.ResultModel;
import Models.Script;

import java.io.*;
import java.net.Socket;

public class Connection  extends Thread {
    DataOutputStream out;
    ObjectInputStream ois;
    Socket clientSocket;
    ScriptsStorage storage = new ScriptsStorage();

    public Connection (Socket aClientSocket) {
        try {
            // inicializa variáveis
            clientSocket = aClientSocket;
            ois = new ObjectInputStream( clientSocket.getInputStream());
            out = new DataOutputStream( clientSocket.getOutputStream());

            this.start(); //executa o método run numa thread separada

        } catch(IOException e) {
            System.out.println("Connection:"+e.getMessage());
        }
    }

    public ResultModel<Script> validate(Script script){
        if(script == null)
            return new ResultModel<Script>("Object Not Defined");

        if(script.request == null)
            return new ResultModel<Script>("Object Request Not Defined");

        if(script.request == null)
            return new ResultModel<Script>("Object Request Not Defined");

        if(script.request.Variable == null || script.request.Variable == "")
            return new ResultModel<Script>("Object Request Variable Not Defined");

        if(script.request.Value == null || script.request.Value == "")
            return new ResultModel<Script>("Object Request Value Not Defined");

        if(script.request.Type == null || script.request.Type == "")
            return new ResultModel<Script>("Object Request Type Not Defined");

        if(script.request.Type != "read" || script.request.Type != "write")
            return new ResultModel<Script>("Object Request Type Must be defined with (read/write)");

        return new ResultModel<Script>(script);
    }

    public void HandleRequestedScript(Script script) throws Exception {

        switch (script.request.Type) {
            case "read":
                out.writeUTF(String.valueOf(ScriptsStorage.get(script.id)));
                break;
            case "write":
                script.id = ScriptsStorage.scriptsList.size();
                ScriptsStorage.add(script);
                out.writeUTF(String.valueOf(script.id));
                break;
            default:
                throw new Exception("Script Request Type not defined");
        }
    }
    public void run(){
        try {

            ResultModel<Script> resScriptValidation = validate((Script) ois.readObject());
            if(!resScriptValidation.success)
                out.writeUTF(resScriptValidation.errors);

            HandleRequestedScript(resScriptValidation.object);

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
