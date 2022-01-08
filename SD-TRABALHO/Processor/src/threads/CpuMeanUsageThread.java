package threads;

import com.sun.management.OperatingSystemMXBean;
import interfaces.CpuMeanUsageInterface;

import java.lang.management.ManagementFactory;

public class CpuMeanUsageThread implements Runnable {
    protected static CpuMeanUsageInterface cpuMeanUsageInterface;

    public CpuMeanUsageThread(CpuMeanUsageInterface cpuMeanUsageInterface)
    {
        this.cpuMeanUsageInterface = cpuMeanUsageInterface;
    }


    @Override
    public void run() {
        double cpu_mean_usage = 0.0;
        while (true) {
            OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
            int it = 0, it_max = 9;
            while(it <= it_max)
            {
                double x = osBean.getSystemCpuLoad();
                cpu_mean_usage += x;

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(it == it_max)
                {
                    cpu_mean_usage /= it_max+1;
                    double aux_cpusage=cpu_mean_usage;
                    System.out.println("Usage: " + aux_cpusage);
                    cpuMeanUsageInterface.set_cpu_usage(aux_cpusage);
                    it = 0;
                    cpu_mean_usage = 0;
                }
                else
                {
                    it++;
                }

            }
        }
    }
}
