import java.io.Serializable;
import java.util.Date;

public class ProcessorInfo  implements Serializable {
    private static final long serialVersionUID = 1L;
    public String server_id, serverAddress;
    public double cpu_usage = 0.0;
    public ScriptQueue scriptQueue = new ScriptQueue();
    public Date lastHeartbeatDateTime;

    public ProcessorInfo(String server_id, String serverAddress){
        this.server_id = server_id;
        this.serverAddress = serverAddress;
    }
}
