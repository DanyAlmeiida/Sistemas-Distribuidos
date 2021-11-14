import Models.ResultModel;
import Models.Script;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class ScriptsManager extends UnicastRemoteObject implements ScriptsInterface
{
    private static final long serialVersionUID = 1L;
    private ArrayList<Script> scriptsList = new ArrayList<Script>();

    protected ScriptsManager() throws RemoteException {
    }


    @Override
    public ResultModel<Integer> RunScript(Script x) throws RemoteException {
        try
        {
            x.id=scriptsList.size();
            scriptsList.add(x);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return new ResultModel<>(x.id);
    }

    @Override
    public Integer getId() throws RemoteException {
        return null;
    }

    @Override
    public ResultModel<String> GetResult(Integer id) throws RemoteException {
        throw new RemoteException("Not Implemented");
    }
}
