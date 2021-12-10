import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.DecimalFormat;

public class ResourcesThread  extends Thread{
    private String processorId = "";
    private double cpu_mean_usage;
    DecimalFormat df = new DecimalFormat("#%");

    public ResourcesThread(String processorId){
        this.processorId = processorId;
    }

    public void run(){
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        int it = 0, it_max = 9;
        while(it <= it_max)
        {
            double x = osBean.getSystemCpuLoad();
            cpu_mean_usage += x;
            //    System.out.println(it + " = " + df.format(x));

            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(it == it_max)
            {
                cpu_mean_usage /= it_max+1;
                try {
                    setCpu_mean_usage(cpu_mean_usage);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (NotBoundException e) {
                    e.printStackTrace();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                it = 0;cpu_mean_usage = 0;
            }
            else
            {
                it++;
            }

        }
    }

    public void setCpu_mean_usage(double cpu_mean_usage) throws MalformedURLException, NotBoundException, RemoteException {
        ReplicaInterface replicaManagerInterface = (ReplicaInterface) Naming.lookup("rmi://localhost:2024/processor_manager");
        replicaManagerInterface.set_cpu_usage(processorId,cpu_mean_usage);

        System.out.println("\nPROCESSOR [ " + processorId + " ] CPU USAGE:" + df.format(cpu_mean_usage));
    }
}