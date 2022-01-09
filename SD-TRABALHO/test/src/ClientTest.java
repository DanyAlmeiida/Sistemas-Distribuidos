import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.UUID;

public class ClientTest {
    public static void main(String[] args)
    {


        Thread x = new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Client.main(new String[]{"@echo Dany Almeida 1"});
                    Client.main(new String[]{"@echo Dany Almeida 2"});
                    Client.main(new String[]{"@echo Dany Almeida 3"});
                    Thread.sleep(500);


              } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        x.start();

    }

}
