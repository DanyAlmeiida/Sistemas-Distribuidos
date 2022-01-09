import Controllers.ProcessScriptController;
import com.jcraft.jsch.JSchException;
import interfaces.ProcessorInterface;
import managers.BalancerManager;
import models.Script;
import models.Heartbeat;
import models.JoinInfo;
import models.ScriptQueue;
import threads.Group;
import interfaces.BrainInterface;
import threads.CpuMeanUsageThread;
import threads.HeartBeatThread;
import threads.ProcessScriptThread;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ProcessorManager extends UnicastRemoteObject implements ProcessorInterface
{
    private String uuid = "", processor_ip;
    public double cpu_usage = 0.0;
    private static final long serialVersionUID = 1L;
    private HashMap<String, ScriptQueue> scriptQueue = new HashMap<String, ScriptQueue>();
    private Group multicastGroup;
    private Long delayHearbeat = (long)(Math.random() * 5000);
    private Boolean IsToSendHearbeat = false;
    private HeartBeatThread tProcessGroupHearBeat;
    private ProcessScriptThread tProcessPendingScripts;
    public ProcessorManager(String id_Server, String processor_ip) throws RemoteException {

        this.uuid = id_Server;
        this.processor_ip = processor_ip;

        try {

            scriptQueue.put(id_Server,new ScriptQueue());

            tProcessGroupHearBeat = new HeartBeatThread(uuid,processor_ip, this.cpu_usage,
                    scriptQueue.get(this.uuid), delayHearbeat, "PROCESSOR", "heartbeat",
                    heartbeat -> multicastGroup.send(heartbeat));

            Thread tMeanCPU = new Thread(new CpuMeanUsageThread(cpu_usage_mean ->
            {this.cpu_usage = cpu_usage_mean; tProcessGroupHearBeat.set_cpu_usage(this.cpu_usage); } ));
            tMeanCPU.start();

            JoinInfo joinInfo = new JoinInfo(processor_ip,uuid,"PROCESSOR");
            multicastGroup = new Group(joinInfo, this::handle);

            Thread thread = new Thread(multicastGroup);
            thread.start();

            Thread thread1 = new Thread(tProcessGroupHearBeat);
            thread1.start();

            tProcessPendingScripts = new ProcessScriptThread(scriptQueue,uuid);
            Thread tProcessPending = new Thread(tProcessPendingScripts);
            tProcessPending.start();

        } catch (Group.GroupException e) {
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

        if(this.cpu_usage >  0.005)
        {
           try {
               ProcessScriptController.process(scriptQueue,script,uuid);
            } catch (JSchException | IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            scriptQueue.get(this.uuid).put(_newS);
            tProcessGroupHearBeat.set_queue(scriptQueue.get(this.uuid));
            tProcessPendingScripts.set_queue(scriptQueue);
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
        this.scriptQueue.remove(Px);
    }

    private void handle(Heartbeat heartbeat) {
        switch (heartbeat.hearBeatType)
        {
            case "heartbeat":
                if(!heartbeat.processorId.equals(this.uuid) && heartbeat.sentBy.equals("PROCESSOR"))
                    scriptQueue.put(heartbeat.processorId, heartbeat.scriptQueue);
                break;
            case "failed":
                if(heartbeat.sentBy.equals("BALANCER"))
                {
                    Registry r = null;

                    try {
                        r = LocateRegistry.createRegistry(2024);
                    } catch (RemoteException var4) {
                       return;
                    }

                    try {
                        String address = "rmi://localhost:2024/balancer";
                        BalancerManager replica_manager = new BalancerManager(UUID.randomUUID().toString(), address,r);
                        r.rebind("balancer", replica_manager);
                        System.out.println("Processor Replica Manager server ready");

                    } catch (Exception var3) {
                        System.out.println("Processor Replica Manager server main " + var3.getMessage());
                    }
                }
                break;
            default:
                System.out.println("Hearbeat type unknow");
        }
    }
}