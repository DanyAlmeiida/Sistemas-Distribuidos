package com.estgv.models;

import java.io.Serializable;

public class Script implements Serializable {

    private static final long serialVersionUID = 1L;
    public String uuid;
    public String script; //ex: put text1.txt
    public byte[] file;
    public String result;

    public Script()
    {
    }

    public Script setUuid(String uuid){
        this.uuid = uuid;
        return this;
    }

    public String PrintInfo(){
        StringBuilder sb = new StringBuilder();
        sb.append("\n---------------- Script Log # "+ this.uuid + " -------------");
        sb.append("\n- Script: " + this.script);
        sb.append("\n- Result: " + (this.result == null ? "None" : this.result) );
        sb.append("\n------------ Script Log End # "+ this.uuid + " -------------\n");
        return sb.toString();
    }
}
