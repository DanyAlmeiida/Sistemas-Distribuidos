package Models;

import java.io.Serializable;

public class Script implements Serializable {

    private static final long serialVersionUID = 1L;
    public Integer id;
    public Request request;
    public String result;

    public Script(Request request)
    {
        this.request = request;
    }
}
