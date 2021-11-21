package com.estgv.models;

import java.io.Serializable;

public class ResultModel<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    public T object;
    public String errors;
    public boolean success;

    public ResultModel(T object){
        this.object = object;
        this.errors = "";
        this.success = true;
    }

    public ResultModel(String errors){
        this.object = null;
        this.errors = errors;
        this.success = false;
    }
}
