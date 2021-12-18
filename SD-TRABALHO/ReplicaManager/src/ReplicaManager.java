
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReplicaManager extends UnicastRemoteObject implements ReplicaInterface {
    private static final long serialVersionUID = 1L;
     List<ProcessorInfo> Replicas = Collections.synchronizedList(new ArrayList<ProcessorInfo>());
    final static String INET_ADDR = "224.0.0.3";
    InetAddress addr;

    public ReplicaManager() throws RemoteException {
        try {
            addr = InetAddress.getByName(INET_ADDR);

            Thread HeartbeatScanThread = (new Thread() {
                public void run() {
                    while (true) {
                        try {
                            sleep(1000);
                            for (ProcessorInfo p:
                                 Replicas) {
                                long diffSeconds = (new Date().getTime() -  p.lastHeartbeatDateTime.getTime())/1000;
                                if( diffSeconds > 30) {
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

        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private void Resume(String Px) throws MalformedURLException, NotBoundException, RemoteException {
        ProcessorInfo TargetProcessor = get();
        ProcessorInterface processorInterface = (ProcessorInterface) Naming.lookup(TargetProcessor.serverAddress);
        System.out.println("Redirecting processor " + Px + " to " + TargetProcessor.server_id);
        processorInterface.resume(Px);
    }

    @Override
    public InetAddress add(ProcessorInfo processorInfo) throws RemoteException {
        Replicas.add(processorInfo);
        return addr;
    }


    @Override
    public synchronized ProcessorInfo get() throws RemoteException {


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


    @Override
    public synchronized void set_cpu_usage(String processorObejctId, double cpu_usage) throws RemoteException {
        ProcessorInfo processorInfo = Replicas.stream()
                .filter((ProcessorInfo x) ->  x.server_id.equals(processorObejctId )).findFirst().orElse(null);
        if(processorInfo == null)
            return;
        else
            Replicas.stream().filter(x -> x.server_id.equals(processorObejctId)).findFirst().get().cpu_usage =cpu_usage;
    }



    @Override
    public synchronized   Boolean heartbeat(Heartbeat heartbeat) throws RemoteException {
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
            e.printStackTrace();
        }

        return true;
    }
}