package managers;

import interfaces.BalancerInterface;
import interfaces.ProcessorInterface;
import models.Heartbeat;
import models.JoinInfo;
import models.ProcessorInfo;
import threads.Group;
import utils.ByteArrayUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class BalancerManager extends UnicastRemoteObject implements BalancerInterface {
    private static final long serialVersionUID = 1L;
    List<ProcessorInfo> Replicas = Collections.synchronizedList(new ArrayList<ProcessorInfo>());
    public static Group group = null;
    Boolean NoHeartBeatFromProcessors  = true;

    public BalancerManager(String randomUUID, String address, Registry registry) throws RemoteException {
        super();
        try {
            group = new Group(new JoinInfo(address,randomUUID,"BALANCER"));
        synchronized (Replicas) {
            Thread HeartbeatScanThread = (new Thread() {
                public void run() {
                    while (true) {
                        try {
                            System.out.println(Replicas.size() + " processadores");
                            sleep(1000);
                            for (ProcessorInfo p :
                                    Replicas) {
                                long diffSeconds = (new Date().getTime() - p.lastHeartbeatDateTime.getTime()) / 1000;
                                if (diffSeconds > 30 ) {
                                    ProcessorInfo aux = p;
                                    System.out.println("Removing processor " + aux.server_id + ". No Heartbeat founded after 30s...");
                                    Replicas.remove(p);
                                    Resume(aux.server_id);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            HeartbeatScanThread.start();


            Thread GroupT = (new Thread(){
             @Override
             public void run() {
                 try {
                 byte[] buf = new byte[1000*10*100];
                 while (true) {
                    DatagramPacket msgPacket = new DatagramPacket(buf, buf.length);
                    group.multicastSocket.receive(msgPacket);
                    Heartbeat heartbeat = (Heartbeat) ByteArrayUtils.deserializeBytes(msgPacket.getData());
                    if(heartbeat.hearBeatType.equals("heartbeat") && heartbeat.sentBy.equals("PROCESSOR")) {
                        handle(heartbeat);
                    }
                 }
                 } catch (IOException | ClassNotFoundException e) {
                     e.printStackTrace();
                 }
             }
         });
            Remote teste = this;
         GroupT.start();
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



    private void Resume(String Px) throws MalformedURLException, NotBoundException, RemoteException {
        ProcessorInfo TargetProcessor = get();
        ProcessorInterface processorInterface = (ProcessorInterface) Naming.lookup(TargetProcessor.serverAddress);
        System.out.println("Redirecting processor " + Px + " to " + TargetProcessor.server_id);
        processorInterface.resume(Px);
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

    public void handle(Heartbeat heartbeat)   {
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