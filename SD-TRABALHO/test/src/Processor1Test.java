import java.util.UUID;

public class Processor1Test {
    public static void main(String[] args)
    {


        Thread x = new Thread(){
            @Override
            public void run() {
                super.run();
                Processor.main(new String[]{String.valueOf(UUID.randomUUID()),"2025"});
            }
        };
        x.start();

    }
    public void await() throws InterruptedException {

    }
}
