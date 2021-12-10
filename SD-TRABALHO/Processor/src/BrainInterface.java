import java.rmi.Remote;
import java.rmi.RemoteException;

interface BrainInterface extends Remote {

    Script get_result(String uuid) throws RemoteException;

    void set_result(Script script) throws RemoteException;

}