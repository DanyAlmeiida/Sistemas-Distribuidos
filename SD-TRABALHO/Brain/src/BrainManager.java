import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class BrainManager extends UnicastRemoteObject implements BrainInterface {
    public ArrayList<Script> scriptsResultLst= new ArrayList<>();
    public BrainManager() throws RemoteException {
    }

    @Override
    public Script get_result(String uuid) throws RemoteException {
        return scriptsResultLst.stream().filter(x -> x.uuid.equals(uuid)).findFirst().orElse(null);
    }

    @Override
    public void set_result(Script script) throws RemoteException {
        scriptsResultLst.add(script);
    }
}
