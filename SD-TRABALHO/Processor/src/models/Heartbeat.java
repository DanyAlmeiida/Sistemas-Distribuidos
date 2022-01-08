package models;

import java.io.Serializable;

public class Heartbeat implements Serializable {
    public String processorId;
    public String processorIP;
    public double cpu_usage;
    public ScriptQueue scriptQueue;
    public Long HearbeatDelay;
    public String sentBy;
    public String hearBeatType;

    public Heartbeat(String processorId,String processorIP, double cpu_usage, ScriptQueue scriptQueue, Long HearbeatDelay, String sentBy, String hearBeatType){
        this.processorId = processorId;
        this.processorIP = processorIP;
        this.cpu_usage = cpu_usage;
        this.scriptQueue = scriptQueue;
        this.HearbeatDelay = HearbeatDelay;
        this.sentBy = sentBy;
        this.hearBeatType = hearBeatType;
    }
}
