package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ScriptQueue implements Serializable {
    private LinkedList<Script> list = new LinkedList<Script>();



    /**
     * Puts object in queue.
     */
    public void put(Script o) {
        list.addLast(o);
    }

    /**
     * Returns an element (object) from queue.
     *
     * @return element from queue or <code>null</code> if queue is empty
     */
    public Script get() {
        if (list.isEmpty()) {
            return null;
        }
        return list.removeFirst();
    }

    public void removeByScriptId(String scriptId){
        if(list.isEmpty()){return;}
        else{
            for(Script script: list)
            {
                if(script.uuid == scriptId)
                {
                    list.remove(script);
                    break;
                }
            }
        }
    }
    /**
     * Returns all elements from the queue and clears it.
     */
    public List<Script> getAll() {
        List<Script> myAL = new ArrayList<>();

        for (Script alObject : list)
            myAL.add(alObject);

        return myAL;
    }


    /**
     * Peeks an element in the queue. Returned elements is not removed from the queue.
     */
    public Script peek() {
        return list.getFirst();
    }

    /**
     * Returns <code>true</code> if queue is empty, otherwise <code>false</code>
     */
    public boolean isEmpty() {
        return list.isEmpty();
    }

    /**
     * Returns queue size.
     */
    public int size() {
        return list.size();
    }
}
