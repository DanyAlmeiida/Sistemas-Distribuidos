package models;

import java.io.Serializable;

public class Script implements Serializable {

    private static final long serialVersionUID = 1L;
    public String uuid;
    public String script; //ex: put text1.txt
    public String processed_by;
    public String file;
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
        sb.append("\n---------------- models.Script Log # "+ this.uuid + " -------------");
        sb.append("\n- models.Script: " + this.script);
        sb.append("\n- Result: " + (this.result == null ? "None" : this.result) );
        sb.append("\n- SftpFile: " + (this.file == null ? "None" : this.file) );
        sb.append("\n- ProcessorUUID: " + (this.processed_by == null ? "None" : this.processed_by) );
        sb.append("\n------------ models.Script Log End # "+ this.uuid + " -------------\n");
        return sb.toString();
    }
}
