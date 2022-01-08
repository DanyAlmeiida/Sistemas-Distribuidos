import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Processor {
    public static Registry r = null;
    public static ProcessorManager processorManager;

    public Processor(String uuid, Integer port)
    {
        try{
            r= LocateRegistry.createRegistry(port);
        }catch (RemoteException e){
            e.printStackTrace();
        }

        try{
            String address = "rmi://localhost:" + port + "/scripts";

            processorManager = new ProcessorManager(uuid,address);
            r.rebind("scripts", processorManager);
            System.out.println("server ready");


        }catch(Exception e) {
            System.out.println("server main " + e.getMessage());
        }
    }

    /**
     * Run this class as an application.
     */
    public static void main(String[] args)
    {
        Processor server = new Processor(args[0],Integer.parseInt(args[1]));
    }
}