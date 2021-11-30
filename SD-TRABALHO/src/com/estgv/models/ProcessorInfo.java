package com.estgv.models;

import java.io.Serializable;

public class ProcessorInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    public String serverAddress;
    public double cpu_usage;

    public ProcessorInfo(String serverAddress, double cpu_usage){
        this.serverAddress = serverAddress;
        this.cpu_usage = cpu_usage;
    }
}
