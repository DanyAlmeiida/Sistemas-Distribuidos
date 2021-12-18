import java.io.*;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Optional;


public class Group implements Runnable {
        public static String host = null;
        public static String senderName = null;
        public static MulticastSocket s = null;
        public static InetAddress group = null;
        public static byte[] buf = null;
        public static Registry registry = null;
        public static ReplicaInterface server;
        public static long lastSeqReceived1 = 0;
        public static int count = 0;
        public static MsgHandler handles = null;
        public static Heartbeat heartbeat;
        //public static History hist;

        public Group(String processorId, String processorIp, MsgHandler handler) throws GroupException {

            this.host = processorIp;
            this.senderName = processorId;
            handles = handler;
          //  hist = new History();
            try {

                int port = 6789;
                registry = LocateRegistry.getRegistry(2024);
                server = (ReplicaInterface) registry.lookup("processor_manager");
                InetAddress INET_ADDR = server.add(new ProcessorInfo(processorId,processorIp));
                group = INET_ADDR;
                s = new MulticastSocket(port);
                // s.setTimeToLive(1);
                s.joinGroup(group);

            } catch (SocketException e) {
                System.out.println("Socket: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Exception: " + e.getMessage());
            }
        }
    public static <T> Optional<byte[]> objectToBytes(T obj) {
        byte[] bytes = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream sOut;
        try {
            sOut = new ObjectOutputStream(out);
            sOut.writeObject(obj);
            sOut.flush();
            bytes = out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(bytes);
    }
        public void send(Heartbeat heartbeat) throws GroupException {

            // send the given message to all instances of Group using the same sequencer
            try {
                server.heartbeat(heartbeat);
                byte[] bytes = ByteArrayUtils.serializeObject(heartbeat);

                DatagramPacket messageOut = new DatagramPacket(bytes, bytes.length, group, 6789);
                s.send(messageOut);
            } catch (SocketException e) {
                System.out.println("Socket: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("IO: " + e.getMessage());
            }
        }

        public void leave() {
            try {
                //server.leave(senderName);
                s.leaveGroup(group);
                // leave group
            } catch (SocketException e) {
                System.out.println("Socket: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("IO: " + e.getMessage());
            }
        }

        public void run() {
            // repeatedly: listen to MulticastSocket created in constructor, and on receipt
            // of a datagram call "handle" on the instance
            // of Group.MsgHandler which was supplied to the constructor
            buf = new byte[1000*10*100];

            try {

                while (true) {
                    DatagramPacket msgPacket = new DatagramPacket(buf, buf.length);
                    s.receive(msgPacket);
                    Heartbeat tes = (Heartbeat) ByteArrayUtils.deserializeBytes(msgPacket.getData());
                    handles.handle(tes);
                }
            } catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }

        public interface MsgHandler {
            public void handle(Heartbeat heartbeat);
        }

        public class GroupException extends Exception {

            public GroupException(String s) {
                super(s);
            }

        }

        public class HeartBeater extends Thread {
            // This thread sends heartbeat messages when required
            public void HeartBeaters() throws Exception {
                server.heartbeat(heartbeat);
            }

        }



    }
