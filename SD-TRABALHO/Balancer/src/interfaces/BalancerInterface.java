package interfaces;

import models.ProcessorInfo;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BalancerInterface extends Remote {
    ProcessorInfo get() throws RemoteException;
}
