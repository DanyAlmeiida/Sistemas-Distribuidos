import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.UUID;

public class Test {
    public static void main(String[] args)
    {
        Thread t = (new Thread() {
            public void run() {
                Replica.main(new String[0]);
                Processor.main(new String[]{String.valueOf(UUID.randomUUID()),"2025"});
                Processor.main(new String[]{String.valueOf(UUID.randomUUID()),"2026"});
                Processor.main(new String[]{String.valueOf(UUID.randomUUID()),"2027"});
                Brain.main(new String[]{String.valueOf(UUID.randomUUID()),"2030"});
                try {
                    sleep(30000);
                    Client.main(new String[]{"@echo Dany Almeida 123456131312312312"});
                    Client.main(new String[]{"@echo Dany Almeida 123456131312312312"});
                    Client.main(new String[]{"@echo Dany Almeida 123456131312312312"});



                } catch (com.jcraft.jsch.JSchException e) {
                    e.printStackTrace();
                } catch (com.jcraft.jsch.SftpException e) {
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
        });
        t.start();
    }
}
