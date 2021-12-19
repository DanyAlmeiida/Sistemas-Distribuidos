import com.jcraft.jsch.JSchException;
import com.sun.management.OperatingSystemMXBean;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ProcessorManager extends UnicastRemoteObject implements ProcessorInterface
{
    private String uuid = "";
    public double cpu_usage = 0.0;
    private static final long serialVersionUID = 1L;
    private HashMap<String,ScriptQueue> scriptQueue = new HashMap<String, ScriptQueue>();
    private Group group;
    private Long delayHearbeat = (long)(Math.random() * 5000);

    public ProcessorManager(String id_Server, String processor_ip) throws RemoteException {
        this.uuid = id_Server;
        scriptQueue.put(id_Server,new ScriptQueue());

        Thread tMeanCPU = (new Thread() {
            DecimalFormat df = new DecimalFormat("#%");
            private double cpu_mean_usage;
            public void run() {
                while (true) {
                    OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
                    int it = 0, it_max = 9;
                    while(it <= it_max)
                    {
                        double x = osBean.getSystemCpuLoad();
                        cpu_mean_usage += x;

                        try {
                            sleep(1000);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if(it == it_max)
                        {
                            cpu_mean_usage /= it_max+1;
                            cpu_usage = cpu_mean_usage;
                            System.out.println("\nPROCESSOR [ " + id_Server + " ] CPU USAGE:" + df.format(cpu_usage));

                            it = 0;
                            cpu_mean_usage = 0;
                        }
                        else
                        {
                            it++;
                        }

                    }
                }
            }
        });
        tMeanCPU.start();

        try {
            group = new Group(uuid,processor_ip,new MsgHandlers());

            Thread tProcessGroup = (new Thread() {
                public void run() {
                    while (true) {
                        try {
                            sleep(delayHearbeat);
                            Heartbeat hb =  new Heartbeat(uuid,cpu_usage,scriptQueue.get(id_Server),delayHearbeat);
                            hb.processorIP = processor_ip;
                            group.send(hb);
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
                        Script script = scriptQueue.get(id_Server).get();
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
        //tProcess.start();

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
           /* try {
                //process_script(script);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSchException e) {
                e.printStackTrace();
            }*/
        }
        else
        {
            scriptQueue.get(this.uuid).put(_newS);
        }

        return _newS.uuid;
    }


    @Override
    public Script get_pending_script() throws RemoteException {
        return (Script) scriptQueue.get(this.uuid).get();
    }

    @Override
    public void resume(String Px) throws RemoteException {
        List<Script> lstScripts = this.getRequests(Px);
        BrainInterface brainInterface = null;
        try {
            brainInterface = (BrainInterface) Naming.lookup("rmi://localhost:2030/resultmodels");
            List<String> scriptsCompletedUUIDS = brainInterface.getCompletedReq(Px);
            scriptQueue.get(Px).getAll().removeIf( script -> scriptsCompletedUUIDS.contains(script.uuid));
            this.ExecuteUnfinishedReq(Px);
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private List<Script> getRequests(String Px)  { return scriptQueue.get(Px).getAll(); }

    private void ExecuteUnfinishedReq(String Px){
        for (Script script:
                scriptQueue.get(Px).getAll()) {
            this.scriptQueue.get(this.uuid).put(script);
        }
    }

    public class MsgHandlers implements Group.MsgHandler{
        MsgHandlers(){

        }

        @Override
        public void handle(Heartbeat heartbeat) {
            System.out.println("Inseri " + heartbeat.scriptQueue.size() + " pedidos no processador " + uuid + " vindos do processador " + heartbeat.processorId);
            scriptQueue.put(heartbeat.processorId,heartbeat.scriptQueue);
        }

    }


}