package com.estgv.models;

import java.io.Serializable;

public class Script implements Serializable {

    private static final long serialVersionUID = 1L;
    public Integer id;
    public String script;
    public String result;

    public Script(String script)
    {
        this.script = script;
    }

    public Script setId(Integer id){
        this.id=id;
        return this;
    }

    public String PrintInfo(){
        StringBuilder sb = new StringBuilder();
        sb.append("\n---------------- Script Log # "+ this.id + " -------------");
        sb.append("\n- Script: " + this.script);
        sb.append("\n- Result: " + (this.result == null ? "None" : this.result) );
        sb.append("\n------------ Script Log End # "+ this.id + " -------------\n");
        return sb.toString();
    }
}
