import Models.ResultModel;
import Models.Script;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class ObjectRegistry extends UnicastRemoteObject implements ObjectRegistryInterface {
    private static final long serialVersionUID = 1L;
    static HashMap<String, String> hash = new HashMap();

    protected ObjectRegistry() throws RemoteException {
    }

    public void addObject(String objectID, String address) throws RemoteException {
        hash.put(objectID, address);
    }

    public String resolve(String objectID) throws RemoteException {
        return (String)hash.get(objectID);
    }
}

