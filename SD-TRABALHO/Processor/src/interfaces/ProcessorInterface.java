package interfaces;

import models.Script;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ProcessorInterface extends Remote
{
    void set_cpu_usage(double cpu_usage) throws RemoteException;

    String run(Script script) throws RemoteException;

    Script get_pending_script() throws RemoteException;

    void resume(String Px) throws RemoteException;
}
