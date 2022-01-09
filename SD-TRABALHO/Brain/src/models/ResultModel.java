package models;

import java.io.Serializable;

public class ResultModel<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    public T object;
    public String msg;
    public boolean success;
    public String destination;

    public ResultModel(T object, String destination){
        this.object = object;
        this.msg = "";
        this.success = true;
        this.destination = destination;
    }
    public ResultModel(T object, String msg, String destination){
        this.object = object;
        this.msg = msg;
        this.success = true;
        this.destination = destination;
    }


    public ResultModel(String errors){
        this.object = null;
        this.msg = errors;
        this.success = false;
        this.destination = "";
    }
}
