import managers.BrainManager;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Brain {
    public static Registry r = null;
    public static Integer port;
    public static BrainManager brainManager;

    public Brain(String uuid, Integer port)
    {
        try{
            r= LocateRegistry.createRegistry(port);
        }catch (RemoteException e){
            e.printStackTrace();
        }

        try{
            String address = "rmi://localhost:" + port + "/resultmodels";

            brainManager = new BrainManager(address,uuid,"BRAIN");
            r.rebind("resultmodels", brainManager );
            System.out.println("brain server ready");

        }catch(Exception e) {
            System.out.println("brain server  " + e.getMessage());
        }
    }

    /**
     * Run this class as an application.
     */
    public static void main(String[] args)
    {
        Brain server = new Brain(args[0],Integer.parseInt(args[1]));
    }
}
