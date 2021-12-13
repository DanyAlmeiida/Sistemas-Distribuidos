import java.io.IOException;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class ReplicaManager extends UnicastRemoteObject implements ReplicaInterface {

    private static final long serialVersionUID = 1L;
    static ArrayList<ProcessorInfo> Replicas = new ArrayList<>();
    final static String INET_ADDR = "224.0.0.3";
    InetAddress addr;

    public ReplicaManager() throws RemoteException {
        try {
            addr = InetAddress.getByName(INET_ADDR);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    @Override
    public InetAddress add(ProcessorInfo processorInfo) throws RemoteException {
        Replicas.add(processorInfo);
        return addr;
    }


    @Override
    public ProcessorInfo get() throws RemoteException {

        for(ProcessorInfo info : Replicas){
            System.out.println(info.server_id + " - " +  new DecimalFormat("#%").format(info.cpu_usage));
        }

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
    public void set_cpu_usage(String processorObejctId, double cpu_usage) throws RemoteException {
        ProcessorInfo processorInfo = Replicas.stream()
                .filter((ProcessorInfo x) ->  x.server_id.equals(processorObejctId )).findFirst().orElse(null);
        if(processorInfo == null)
            return;
        else
            Replicas.stream().filter(x -> x.server_id.equals(processorObejctId)).findFirst().get().cpu_usage =cpu_usage;
    }

    @Override
    public double get_cpu_usage(String processorObejctId) throws RemoteException {
        return Replicas.stream()
                .filter((ProcessorInfo x) ->  x.server_id.equals(processorObejctId )).findFirst().orElse(null).cpu_usage;
    }

    @Override
    public Boolean heartbeat(Heartbeat heartbeat) throws RemoteException {
        Boolean res = true;

        try
        {
            if(heartbeat == null)
                throw new Exception("Heartbeat empty!");


            for (ProcessorInfo processorInfo : Replicas){
                if(processorInfo.server_id.equals(heartbeat.processorId)){
                    System.out.println(heartbeat.scriptArrayList.length);
                  processorInfo.scriptArrayList = heartbeat.scriptArrayList;
                }
            }
        }catch (Exception e)
        {
            res = false;
            e.printStackTrace();
        }
        return res;
    }
}