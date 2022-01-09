package threads;

import interfaces.TrafficHandler;
import models.Heartbeat;
import models.JoinInfo;
import utils.ByteArrayUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;


public class Group implements Runnable {
        int port = 6789;
        public static List<JoinInfo> joinInfo = new ArrayList<JoinInfo>();
        public static MulticastSocket multicastSocket = null;
        public static InetAddress multicastGroup = null;
        public static byte[] buf = null;
        public static TrafficHandler trafficHandler;
        public Group(JoinInfo joinInfo, TrafficHandler trafficHandler ) throws GroupException {

            this.joinInfo.add(joinInfo);
            this.trafficHandler = trafficHandler;

            join();
        }

        public Group(JoinInfo joinInfo) throws GroupException {

            this.joinInfo.add(joinInfo);
            join();
        }

        public void join(){
            try {

                //region setup-group multicastsocket and join

                multicastGroup = InetAddress.getByName("224.0.0.3");
                multicastSocket = new MulticastSocket(port);
                multicastSocket.joinGroup(multicastGroup);

                //endregion


            }
            catch (SocketException e) {
                System.out.println("Socket: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Exception: " + e.getMessage());
            }
        }

        public void send(Heartbeat heartbeat) {

            // send the given message to all instances of Group using the same sequencer
            try {
                byte[] bytes = ByteArrayUtils.serializeObject(heartbeat);
                DatagramPacket messageOut = new DatagramPacket(bytes, bytes.length, multicastGroup, port);
                multicastSocket.send(messageOut);

            } catch (SocketException e) {
                System.out.println("Socket: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("IO: " + e.getMessage());
            }
        }

        public synchronized void leave(String address) {
            try {
                joinInfo.removeIf(x -> x.host.equals(address));
                multicastSocket.leaveGroup(multicastGroup);
            } catch (SocketException e) {
                System.out.println("Socket: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("IO: " + e.getMessage());
            }
        }

        public void run() {

            buf = new byte[1000*10*100];

            try {

                while (true) {
                    DatagramPacket msgPacket = new DatagramPacket(buf, buf.length);
                    multicastSocket.receive(msgPacket);
                    trafficHandler.handle((Heartbeat) ByteArrayUtils.deserializeBytes(msgPacket.getData()));
                }
            } catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }

        private void printTable(){
            System.out.println("############## MulticastGroup Join Info ##############");
            for (JoinInfo info: joinInfo
                 ) {
                System.out.println("# " + info.whoAmI + " - " + info.name + " - " + info.host);
            }
            System.out.println("######################################################");
        }



    public class GroupException extends Exception {

            public GroupException(String s) {
                super(s);
            }

        }

    }
