import java.rmi.RemoteException;

public interface TrafficHandler {
     void handle(Heartbeat heartbeat) throws RemoteException;
}
