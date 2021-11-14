package Models;

public class ResultModel<T> {
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
