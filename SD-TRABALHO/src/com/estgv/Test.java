package com.estgv;

import com.estgv.processor.Processor;
import com.estgv.registry.RMIRegistry;
import com.estgv.replica.ProcessorReplicaManager;

import java.util.UUID;

public class Test {

    public static void main(String[] args){

        Thread t = (new Thread() {
            public void run() {
                RMIRegistry.main(new String[0]);
                ProcessorReplicaManager.main(new String[0]);
                Processor.main(new String[]{String.valueOf(UUID.randomUUID()),"2025"});
                Processor.main(new String[]{String.valueOf(UUID.randomUUID()),"2026"});
                Processor.main(new String[]{String.valueOf(UUID.randomUUID()),"2027"});
            }
        });
        t.start();
    }
}
