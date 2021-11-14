import Models.Script;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ObjectRegistryInterface extends Remote {

    void addObject(String var1, Script var2) throws RemoteException;

    Script resolve(String var1) throws RemoteException;
}


