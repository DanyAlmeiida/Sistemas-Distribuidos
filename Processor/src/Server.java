import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
    public static void main (String args[]) {
        try{
            ServerSocket listenSocket = new ServerSocket(7896);
            System.out.println("started");
            while(true) {
                Socket clientSocket = listenSocket.accept(); // bloqueia à espera de uma conexão
                Connection c = new Connection(clientSocket); // processa o pedido
            }
        } catch(IOException e) {
            System.out.println("Listen :"+e.getMessage());
        }
    }
}
