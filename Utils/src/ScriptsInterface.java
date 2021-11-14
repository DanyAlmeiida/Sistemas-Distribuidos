import Models.ResultModel;
import Models.Script;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ScriptsInterface extends Remote
{
    public ResultModel<Integer> RunScript(Script x) throws RemoteException;
    public ResultModel<String> GetResult(Integer id) throws RemoteException;
}
