import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.UUID;

public class BalancerTest {
    public static void main(String[] args)
    {
        Thread t = (new Thread(() -> Balancer.main(new String[0])));
        t.start();

    }

}
