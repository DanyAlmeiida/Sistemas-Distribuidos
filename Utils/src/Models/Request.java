package Models;

import java.io.Serializable;

public class Request implements Serializable {
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
    }
}
