package com.estgv.models;

import java.io.Serializable;

public class Script implements Serializable {

    private static final long serialVersionUID = 1L;
    public Integer id;
    public Request request;
    public String result;

    public Script()
    {
    }
    public Script(Integer id ,Request request)
    {
        this.id = id;
        this.request = request;
    }
    public Script(Request request)
    {
        this.request = request;
    }
    public String PrintInfo(){
        StringBuilder sb = new StringBuilder();
        sb.append("\n---------------- Script Log # "+ this.id + " -------------");
        sb.append(this.request.PrintInfo());
        sb.append("\n- Result: " + (this.result == null ? "None" : this.result) );
        sb.append("\n------------ Script Log End # "+ this.id + " -------------\n");
        return sb.toString();
    }
}
