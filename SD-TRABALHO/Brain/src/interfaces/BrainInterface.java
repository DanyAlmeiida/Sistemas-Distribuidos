package interfaces;

import models.Script;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface BrainInterface extends Remote {

    Script get_result(String uuid) throws RemoteException;

    void set_result(Script script) throws RemoteException;

    List<String> getCompletedReq(String Px) throws RemoteException;
}
