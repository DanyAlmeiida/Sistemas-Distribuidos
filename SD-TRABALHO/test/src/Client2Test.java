public class Client2Test {
    public static void main(String[] args)
    {


        Thread x = new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Client.main(new String[]{"@echo Tiago  1"});
                    Client.main(new String[]{"@echo Tiago  2"});
                    Client.main(new String[]{"@echo Tiago  3"});
                    Thread.sleep(500);


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        x.start();

    }

}
