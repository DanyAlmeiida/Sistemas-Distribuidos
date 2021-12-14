import com.jcraft.jsch.JSchException;

import java.io.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.UUID;

public class ProcessorManager extends UnicastRemoteObject implements ProcessorInterface
{
    private String uuid = "";
    public double cpu_usage = 0.0;
    private static final long serialVersionUID = 1L;
    private ScriptQueue scriptQueue = new ScriptQueue();
    private Group group;
    private Long delayHearbeat = (long)(Math.random() * 5000);

    public ProcessorManager(String id_Server, String processor_ip) throws RemoteException {
        this.uuid = id_Server;

        //region thread-group-hearbeat

        try {
            group = new Group(uuid,processor_ip,new MsgHandlers());

            Thread tProcessGroup = (new Thread() {
                public void run() {
                    while (true) {
                        try {
                            sleep(delayHearbeat);
                            group.send(new Heartbeat(uuid,cpu_usage,scriptQueue.getAll(),delayHearbeat));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            tProcessGroup.start();
        } catch (Group.GroupException e) {
            e.printStackTrace();
        }

        //endregion

        //region thread-group-listenner

        Thread thread = new Thread(group);
        thread.start();

        //endregion

        //region Process-Pending-Scripts

        Thread tProcess = (new Thread() {
            public void run() {
                while (true) {
                    try {
                        System.out.println("Sleeping for 1000 seconds");
                        sleep(1000);
                        Script script = scriptQueue.get();
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
        });
        tProcess.start();

        //endregion
    }

    private void process_script(Script script) throws IOException, JSchException {

        //region read-file-script to execute

        SFTPClient  sftpClient = new SFTPClient();

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
            script.processed_by = uuid;
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

    @Override
    public void set_cpu_usage(double cpu_usage) throws RemoteException {
        this.cpu_usage = cpu_usage;
    }

    @Override
    public String run(Script script) throws RemoteException {
        Script _newS =  script.setUuid(UUID.randomUUID().toString());

        if(this.cpu_usage <  0.005)
        {
            try {
                process_script(script);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSchException e) {
                e.printStackTrace();
            }
        }
        else
        {
            scriptQueue.put(_newS);
        }

        return _newS.uuid;
    }


    @Override
    public Script get_pending_script() throws RemoteException {
        return (Script) scriptQueue.get();
    }
    public class MsgHandlers implements Group.MsgHandler{
        MsgHandlers(){

        }

        @Override
        public void handle(Heartbeat heartbeat) {
            System.out.println(heartbeat.processorId + " with " + heartbeat.scriptArrayList.length + " in list");
        }

    }


}