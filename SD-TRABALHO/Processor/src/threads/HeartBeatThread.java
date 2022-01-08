package threads;

import models.Heartbeat;
import models.ScriptQueue;
import interfaces.HeartBeatInterface;

public class HeartBeatThread implements Runnable{
    protected static HeartBeatInterface heartBeatInterface;
    protected static Heartbeat heartbeat;
    protected static double  cpu_usage;
    public HeartBeatThread(String processorId,String processorIP, double cpu_usage, ScriptQueue scriptQueue, Long HearbeatDelay,
                           String sentBy, String hearBeatType, HeartBeatInterface heartBeatInterface){
        this.heartbeat = new Heartbeat(processorId,processorIP,cpu_usage,scriptQueue,HearbeatDelay,sentBy,hearBeatType);
        this.heartBeatInterface = heartBeatInterface;
        this.cpu_usage = cpu_usage;
    }
    public void set_queue(ScriptQueue scriptQueue)
    {
        this.heartbeat.scriptQueue = scriptQueue;
    }

    public void set_cpu_usage(double cpu_usage)
    {
        this.heartbeat.cpu_usage = cpu_usage;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(this.heartbeat.HearbeatDelay);
                heartBeatInterface.SendHeartbeat(heartbeat);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
