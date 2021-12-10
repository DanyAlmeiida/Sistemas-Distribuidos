import java.util.UUID;

public class Test {
    public static void main(String[] args)
    {
        Thread t = (new Thread() {
            public void run() {
                Replica.main(new String[0]);
                Processor.main(new String[]{String.valueOf(UUID.randomUUID()),"2025"});
                //Processor.main(new String[]{String.valueOf(UUID.randomUUID()),"2026"});
                //Processor.main(new String[]{String.valueOf(UUID.randomUUID()),"2027"});
                Brain.main(new String[]{String.valueOf(UUID.randomUUID()),"2030"});
            }
        });
        t.start();
    }
}
