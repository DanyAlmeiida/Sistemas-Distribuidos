package threads;

import models.ProcessorInfo;
import interfaces.AfkProcessorScanInterface;

import java.util.Date;
import java.util.List;

public class AfkProcessorScanThread implements Runnable{
    protected static List<ProcessorInfo> replicas = null;
    protected static AfkProcessorScanInterface afkProcessorScanInterface;
    public AfkProcessorScanThread(List<ProcessorInfo> replicas, AfkProcessorScanInterface afkProcessorScanInterface){
        this.replicas = replicas;
        this.afkProcessorScanInterface = afkProcessorScanInterface;
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println(this.replicas.size() + " processadores");
                Thread.sleep(1000);
                for (ProcessorInfo p :
                        this.replicas) {
                    long diffSeconds = (new Date().getTime() - p.lastHeartbeatDateTime.getTime()) / 1000;
                    if (diffSeconds > 30 ) {
                        ProcessorInfo aux = p;
                        System.out.println("Removing processor " + aux.server_id + ". No Heartbeat founded after 30s...");
                        this.replicas.remove(p);
                        this.afkProcessorScanInterface.resume(aux.server_id);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
