package com.estgv.models;

import java.io.Serializable;

public class ProcessorInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    public String server_id, serverAddress;
    public double cpu_usage = 0.0;

    public ProcessorInfo(String server_id, String serverAddress, double cpu_usage){
        this.server_id = server_id;
        this.serverAddress = serverAddress;
        this.cpu_usage = cpu_usage;
    }
}
