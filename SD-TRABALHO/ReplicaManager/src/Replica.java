import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Replica {
    public Replica() {
    }

    public static void main(String[] args) {
        Registry r = null;

        try {
            r = LocateRegistry.createRegistry(2024);
        } catch (RemoteException var4) {
            var4.printStackTrace();
        }

        try {
            ReplicaManager replica_manager = new ReplicaManager();
            r.rebind("processor_manager", replica_manager);
            System.out.println("Processor Replica Manager server ready");

        } catch (Exception var3) {
            System.out.println("Processor Replica Manager server main " + var3.getMessage());
        }

    }
}

