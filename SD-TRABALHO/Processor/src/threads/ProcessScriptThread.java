package threads;

import models.SFTPClient;
import models.Script;
import models.ScriptQueue;
import com.jcraft.jsch.JSchException;
import interfaces.BrainInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;

public class ProcessScriptThread implements Runnable{
    protected static HashMap<String, ScriptQueue> scriptQueue  = null;
    protected static String serverId;
    public ProcessScriptThread(HashMap<String, ScriptQueue> script, String serverId){
        this.scriptQueue = script;
        this.serverId = serverId;
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println("Sleeping for 1000 seconds");
                Thread.sleep(1000);
                Script script = scriptQueue.get(this.serverId).get();
                if (script != null) {
                    process_script(script);
                }
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            } catch (JSchException e) {
                e.printStackTrace();
            }
        }
    }

    public void process_script(Script script) throws JSchException, IOException {

        //region read-file-script to execute

        SFTPClient sftpClient = new SFTPClient();

        String sb = sftpClient.get_content(script.file);

        //endregion

        //region execute-script

        ProcessBuilder processBuilder = new ProcessBuilder("cmd","/c",sb);
        Process process = processBuilder.start();
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line + System.lineSeparator());
            }
            script.processed_by = this.serverId;
            script.result = output.toString();
            BrainInterface brainInterface = (BrainInterface) Naming.lookup("rmi://localhost:2030/resultmodels");
            brainInterface.set_result(script);

        } catch (NotBoundException | RemoteException | MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //endregion
    }
}
