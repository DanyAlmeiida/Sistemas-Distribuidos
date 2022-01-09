package interfaces;

import models.Heartbeat;

import java.rmi.RemoteException;

public interface TrafficHandler {
     void handle(Heartbeat heartbeat) throws RemoteException;
}
