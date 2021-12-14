import java.io.Serializable;

public class ProcessorInfo  implements Serializable {
    private static final long serialVersionUID = 1L;
    public String server_id, serverAddress;
    public double cpu_usage = 0.0;
    public Object[] scriptArrayList;

    public ProcessorInfo(String server_id, String serverAddress){
        this.server_id = server_id;
        this.serverAddress = serverAddress;
    }
}
