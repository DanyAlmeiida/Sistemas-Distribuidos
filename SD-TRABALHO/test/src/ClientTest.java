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
                    Client.main(new String[]{"@echo Dany Almeida 123456131312312312"});
                    Thread.sleep(500);
                    Client.main(new String[]{"@echo Dany Almeida 123456131312312312"});
                    Thread.sleep(500);
                    Client.main(new String[]{"@echo Dany Almeida 123456131312312312"});
                    Thread.sleep(500);
                    Client.main(new String[]{"@echo Dany Almeida 123456131312312312"});
                    Thread.sleep(500);
                    Client.main(new String[]{"@echo Dany Almeida 123456131312312312"});
                    Thread.sleep(500);
                    Client.main(new String[]{"@echo Dany Almeida 123456131312312312"});
                    Thread.sleep(500);
                    Client.main(new String[]{"@echo Dany Almeida 123456131312312312"});
                    Thread.sleep(500);
                    Client.main(new String[]{"@echo Dany Almeida 123456131312312312"});
                    Thread.sleep(500);
                    Client.main(new String[]{"@echo Dany Almeida 123456131312312312"});
                    Thread.sleep(500);
                    Client.main(new String[]{"@echo Dany Almeida 123456131312312312"});
                    Thread.sleep(500);
                    Client.main(new String[]{"@echo Dany Almeida 123456131312312312"});
                    Thread.sleep(500);
                    Client.main(new String[]{"@echo Dany Almeida 123456131312312312"});
                    Thread.sleep(500);
                    Client.main(new String[]{"@echo Dany Almeida 123456131312312312"});
                    Thread.sleep(500);
                    Client.main(new String[]{"@echo Dany Almeida 123456131312312312"});
                    Thread.sleep(500);
                    Client.main(new String[]{"@echo Dany Almeida 123456131312312312"});
                    Thread.sleep(500);
                    Client.main(new String[]{"@echo Dany Almeida 123456131312312312"});
                    Thread.sleep(500);

                } catch (JSchException e) {
                    e.printStackTrace();
                } catch (SftpException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (NotBoundException e) {
                    e.printStackTrace();
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        x.start();

    }
    public void await() throws InterruptedException {

    }
}
