import Models.Request;
import Models.Script;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Client {

    public static ScriptsInterface lookUpScriptsInterface() {

        try
        {
            return (ScriptsInterface) Naming.lookup("rmi://localhost:2022/scriptsInterface");
        }
        catch (NotBoundException | RemoteException | MalformedURLException ex)
        {
            ex.printStackTrace();
        }
    }
    public static void main(String[] args){

        ScriptsInterface scriptsInterface = lookUpScriptsInterface();

        try
        {
            Script script = new Script(new Request("A","1"));
            scriptsInterface.RunScript(script);
        }
        catch(RemoteException e)
        {System.out.println(e.getMessage()); }
        catch(Exception e)
        {e.printStackTrace();}
    }


}
