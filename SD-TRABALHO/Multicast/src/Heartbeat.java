import java.io.Serializable;
import java.util.ArrayList;

public class Heartbeat implements Serializable {
    public String processorId;
    public double cpu_usage;
    public Object[] scriptArrayList;
    public Long HearbeatDelay;


    public Heartbeat(String processorId, double cpu_usage, Object[] scriptArrayList, Long HearbeatDelay){
        this.processorId = processorId;
        this.cpu_usage = cpu_usage;
        this.scriptArrayList = scriptArrayList;
        this.HearbeatDelay = HearbeatDelay;
    }
}
