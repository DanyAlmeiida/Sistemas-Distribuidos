package managers;

import interfaces.BrainInterface;
import interfaces.BrainSearchInterface;
import models.BrainRequestDetail;
import models.JoinInfo;
import models.ResultModel;
import models.Script;
import threads.BrainGroup;

import java.io.IOException;
import java.net.InetAddress;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BrainManager extends UnicastRemoteObject implements BrainInterface {
    public ArrayList<Script> scriptsResultLst= new ArrayList<>();
    public  BrainGroup brainGroup;
    public BrainManager(String address_host, String name_host, String whoAmI) throws RemoteException, BrainGroup.GroupException {
       JoinInfo joinInfo = new JoinInfo(address_host,name_host,whoAmI);
        brainGroup = new BrainGroup(joinInfo, new BrainSearchInterface() {
           @Override
           public void buscaboby(String ScriptUUID, InetAddress rcvrAddress, int rcvrPort, String whoAmI) {
               try {
                   Script script = BrainManager.this.get_result(ScriptUUID);
                   if (script != null) {

                       brainGroup.send(new ResultModel<Script>(script,"result",whoAmI), rcvrAddress, rcvrPort);
                   }

               } catch (RemoteException e) {
                   e.printStackTrace();
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }

           @Override
           public List<String> buscaboby(String Px, String whoAmI) {
               return scriptsResultLst.stream().filter( x -> x.processed_by.equals(Px)).map( x -> x.uuid).collect(Collectors.toList());
           }
       });
        Thread t = new Thread(brainGroup);
        t.start();

        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            @Override
            public void run()
            {
                System.out.println("Leaving... OFFLINE!");
                brainGroup.leave(address_host);

            }
        });
    }

    @Override
    public Script get_result(String uuid) throws RemoteException {
        return scriptsResultLst.stream().filter(x -> x.uuid.equals(uuid)).findFirst().orElse(null);
    }

    @Override
    public void set_result(Script script) throws RemoteException {
        scriptsResultLst.add(script);
    }

    @Override
    public List<String> getCompletedReq(String Px) throws RemoteException {
        return scriptsResultLst.stream().filter( x -> x.processed_by.equals(Px)).map( x -> x.uuid).collect(Collectors.toList());
    }
}
