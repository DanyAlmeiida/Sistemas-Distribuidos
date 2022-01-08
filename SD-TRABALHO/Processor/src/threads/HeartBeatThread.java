package threads;

import models.Heartbeat;
import models.ScriptQueue;
import interfaces.HeartBeatInterface;

public class HeartBeatThread implements Runnable{
    protected static HeartBeatInterface heartBeatInterface;
    protected static Heartbeat heartbeat;
    public HeartBeatThread(String processorId,String processorIP, double cpu_usage, ScriptQueue scriptQueue, Long HearbeatDelay,
                           String sentBy, String hearBeatType, HeartBeatInterface heartBeatInterface){
        this.heartbeat = new Heartbeat(processorId,processorIP,cpu_usage,scriptQueue,HearbeatDelay,sentBy,hearBeatType);
        this.heartBeatInterface = heartBeatInterface;
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
