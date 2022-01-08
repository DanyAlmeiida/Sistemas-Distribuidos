import manager.BalancerManager;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.UUID;

public class Balancer {
    public Balancer() {
        super();
    }

    public static void main(String[] args) {
        Registry r = null;

        try {
            r = LocateRegistry.createRegistry(2024);
        } catch (RemoteException var4) {
            var4.printStackTrace();
        }

        try {
            String address = "rmi://localhost:2024/balancer";
            String uuid = UUID.randomUUID().toString();
            BalancerManager replica_manager = new BalancerManager(uuid, address, r);
            r.rebind("balancer", replica_manager);
            System.out.println("Processor Replica Manager server ready");


        } catch (Exception var3) {
            System.out.println("Processor Replica Manager server main " + var3.getMessage());
        }

    }
}

