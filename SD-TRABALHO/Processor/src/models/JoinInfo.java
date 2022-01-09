package models;

import java.io.Serializable;

public class JoinInfo implements Serializable {
    public String host;
    public String name;
    public String whoAmI;

    public JoinInfo(String host, String name, String whoAmI){
        this.host = host;
        this.name = name;
        this.whoAmI = whoAmI;
    }
}
