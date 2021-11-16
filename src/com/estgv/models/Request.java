package com.estgv.models;

import java.io.Serializable;

public class Request implements Serializable {
    private static final long serialVersionUID = 1L;
    public String Type;
    public String Variable;
    public String Value;


    /*
    * Method to write a variable
    * */
    public Request(String variable, String value){
        this.Type = "write";
        this.Variable = variable;
        this.Value = value;
    }

    /*
    * Method to read variable
    * */
    public Request(String variable){
        this.Type = "read";
        this.Variable = variable;
        this.Value = null;
    }

    public String PrintInfo(){
        StringBuilder sb = new StringBuilder();
        sb.append("\n- Request: ");
        sb.append("\n-  » Type: " + (this.Type == null ? "Undefined" : this.Type));
        sb.append("\n-  » Variable: " + (this.Variable == null ? "None" : this.Variable ));
        sb.append("\n-  » Value: " + (this.Value == null ? "None" : this.Value));
        return sb.toString();
    }
}
