import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Heartbeat implements Serializable {
    public String processorId;
    public String processorIP;
    public double cpu_usage;
    public ScriptQueue scriptQueue;
    public Long HearbeatDelay;


    public Heartbeat(String processorId, double cpu_usage, ScriptQueue scriptQueue, Long HearbeatDelay){
        this.processorId = processorId;
        this.cpu_usage = cpu_usage;
        this.scriptQueue = scriptQueue;
        this.HearbeatDelay = HearbeatDelay;
    }
}
