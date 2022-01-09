import java.util.UUID;

public class Brain2Test {
    public static void main(String[] args)
    {


        Thread x = new Thread(){
            @Override
            public void run() {
                super.run();
                Brain.main(new String[]{String.valueOf(UUID.randomUUID()),"2040"});
            }
        };
        x.start();

    }
    public void await() throws InterruptedException {

    }
}
