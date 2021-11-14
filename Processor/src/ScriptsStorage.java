import Models.Script;

import java.util.HashMap;

public class ScriptsStorage {
    static HashMap<Integer, Script> scriptsList = new HashMap<>();

    public static Integer add(Script script){
        scriptsList.put(script.id, script);
        return script.id;
    }

    public static Script get(int scriptId)
    {
        return scriptsList.get(scriptId);
    }
}
