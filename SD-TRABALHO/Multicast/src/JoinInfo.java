import java.io.Serializable;
import java.net.InetAddress;

public class JoinInfo implements Serializable
{
    public InetAddress addr;
    public long sequence;
    public JoinInfo(InetAddress addr, long sequence)
    {
        this.addr = addr;
        this.sequence = sequence;
    }
}
