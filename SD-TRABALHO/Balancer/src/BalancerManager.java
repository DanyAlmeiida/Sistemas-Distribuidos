import interfaces.ProcessorInterface;
import models.Heartbeat;
import models.JoinInfo;
import models.ProcessorInfo;
import threads.AfkProcessorScanThread;
import threads.Group;
import threads.ReceiveHearbeatThread;
import utils.ByteArrayUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.DecimalFormat;
import java.util.*;

public class BalancerManager extends UnicastRemoteObject implements BalancerInterface {
    private static final long serialVersionUID = 1L;
    List<ProcessorInfo> Replicas = Collections.synchronizedList(new ArrayList<>());
    public static Group group = null;

    public BalancerManager(String randomUUID, String address, Registry registry) throws RemoteException {
        super();
        try {
            group = new Group(new JoinInfo(address,randomUUID,"BALANCER"));

            synchronized (Replicas) {
            Thread AfkProcessorScanThread = new Thread(new AfkProcessorScanThread(Replicas,serverId -> this.Resume(serverId)));
            AfkProcessorScanThread.start();


            Thread receiveHearbeatThread = new Thread(new ReceiveHearbeatThread(group,heartbeat -> handle_heartbeat(heartbeat)));
            receiveHearbeatThread.start();

            Runtime.getRuntime().addShutdownHook(new Thread()
            {
                @Override
                public void run()
                {
                    try {
                        group.send(new Heartbeat(randomUUID,address,0.0,null,0L,"BALANCER","failed"));
                        UnicastRemoteObject.unexportObject(registry, false);
                        System.out.println("leaving");

                        group.leave(address);

                    } catch (NoSuchObjectException e) {
                        e.printStackTrace();
                    } catch (RemoteException remoteException) {
                        remoteException.printStackTrace();
                    }
                }
            });
        }

        }catch (Exception e){e.printStackTrace();}
    }



    private void Resume(String Px) {
        try
        {
            ProcessorInfo TargetProcessor = get();
            ProcessorInterface processorInterface = (ProcessorInterface) Naming.lookup(TargetProcessor.serverAddress);
            System.out.println("Redirecting processor " + Px + " to " + TargetProcessor.server_id);
            processorInterface.resume(Px);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public  ProcessorInfo get() throws RemoteException {


        ProcessorInfo min = null;
        for (ProcessorInfo  entry : Replicas) {
            System.out.println(entry.cpu_usage);
            if (min == null || min.cpu_usage > entry.cpu_usage) {
                min = entry;
            }
        }

        System.out.println("Picked Server [ " + min.server_id + " ] - " + new DecimalFormat("#%").format(min.cpu_usage));
        return min;
    }

    public void handle_heartbeat(Heartbeat heartbeat)   {
        try
        {
            if(Replicas.stream().filter( x -> x.server_id.equals(heartbeat.processorId)).count() == 0)
                Replicas.add(new ProcessorInfo(heartbeat.processorId, heartbeat.processorIP));

            for (ProcessorInfo processorInfo:
                 Replicas) {

                if (processorInfo.server_id.equals(heartbeat.processorId)) {
                    processorInfo.scriptQueue = heartbeat.scriptQueue;
                    processorInfo.cpu_usage = heartbeat.cpu_usage;
                    processorInfo.lastHeartbeatDateTime = new Date();
                }
            };

        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}