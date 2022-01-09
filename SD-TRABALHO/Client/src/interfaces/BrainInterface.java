package interfaces;

import models.Script;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BrainInterface extends Remote {
    Script get_result(String uuid) throws RemoteException;
}
