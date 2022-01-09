package threads;


import interfaces.BrainSearchInterface;
import models.BrainRequestDetail;
import models.JoinInfo;
import models.ResultModel;
import models.Script;
import utils.ByteArrayUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class BrainGroup implements Runnable {
    int port = 6784;
    public  static List<JoinInfo> joinInfo = new ArrayList<JoinInfo>();
    public static MulticastSocket multicastSocket = null;
    public static InetAddress multicastGroup = null;
    public static byte[] buf = null;
    public static BrainSearchInterface brainSearchInterface = null;

    public BrainGroup()   throws GroupException {
        try
        {

            //region setup-group multicastsocket and join

            multicastGroup = InetAddress.getByName("224.0.0.1");
            multicastSocket = new MulticastSocket(port);
            multicastSocket.joinGroup(multicastGroup);

            //endregion

        }catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    public BrainGroup(JoinInfo joinInfo, BrainSearchInterface brainSearchInterface)   throws GroupException {
        try
        {
            this.brainSearchInterface = brainSearchInterface;
            this.joinInfo.add(joinInfo);

            //region setup-group multicastsocket and join

            multicastGroup = InetAddress.getByName("224.0.0.1");
            multicastSocket = new MulticastSocket(port);
            multicastSocket.joinGroup(multicastGroup);

            //endregion

        }catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }


    public void send(JoinInfo choosenBrainInformation, InetAddress rcvrAddress, int rcvrPort) throws IOException
    {
        byte[] data = ByteArrayUtils.serializeObject(choosenBrainInformation);
        DatagramPacket outPacket = new DatagramPacket(data, 0, data.length, rcvrAddress, rcvrPort);

        multicastSocket.send(outPacket);
    }

    public void send(ArrayList<JoinInfo> choosenBrainInformation, InetAddress rcvrAddress, int rcvrPort) throws IOException
    {
        byte[] data = ByteArrayUtils.serializeObject(choosenBrainInformation);
        DatagramPacket outPacket = new DatagramPacket(data, 0, data.length, rcvrAddress, rcvrPort);

        multicastSocket.send(outPacket);
    }

    public void send(BrainRequestDetail script, InetAddress rcvrAddress, int rcvrPort) throws IOException
    {
        byte[] data = ByteArrayUtils.serializeObject(script);
        DatagramPacket outPacket = new DatagramPacket(data, 0, data.length, rcvrAddress, rcvrPort);

        multicastSocket.send(outPacket);
    }
    public void send(ResultModel<Script> script, InetAddress rcvrAddress, int rcvrPort) throws IOException
    {
        byte[] data = ByteArrayUtils.serializeObject(script);
        DatagramPacket outPacket = new DatagramPacket(data, 0, data.length, rcvrAddress, rcvrPort);

        multicastSocket.send(outPacket);
    }

    public void sendAddress(ResultModel<String> address, InetAddress rcvrAddress, int rcvrPort) throws IOException
    {
        byte[] data = ByteArrayUtils.serializeObject(address);
        DatagramPacket outPacket = new DatagramPacket(data, 0, data.length, rcvrAddress, rcvrPort);

        multicastSocket.send(outPacket);
    }
    public void sendAvailableBrains(ResultModel<List<String>> address, InetAddress rcvrAddress, int rcvrPort) throws IOException
    {
        byte[] data = ByteArrayUtils.serializeObject(address);
        DatagramPacket outPacket = new DatagramPacket(data, 0, data.length, rcvrAddress, rcvrPort);

        multicastSocket.send(outPacket);
    }

    public void leave(String address) {
        try {
            joinInfo.removeIf(x -> x.host.equals(address));
            multicastSocket.leaveGroup(multicastGroup);
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }
    }
    @Override
    public void run() {
        InetAddress senderAddress;
        int senderPort;
        buf = new byte[1000*10*100];

        try {

            while (true) {
                DatagramPacket msgPacket = new DatagramPacket(buf, buf.length);
                multicastSocket.receive(msgPacket);
                senderAddress = msgPacket.getAddress();
                senderPort = msgPacket.getPort();
                BrainRequestDetail received = (BrainRequestDetail) ByteArrayUtils.deserializeBytes(msgPacket.getData());
                if(received.Type.equals("request-brain-script-by-uuid") && !received.SearchId.equals(""))
                {
                    brainSearchInterface.buscaboby(received.SearchId,senderAddress,senderPort,received.whoAmI);
                }
                if(received.Type.equals("request-all-brains"))
                {
                    this.sendAvailableBrains(new ResultModel<List<String>>(joinInfo.stream().map(x -> x.host).collect(Collectors.toList())
                            ,"result", received.whoAmI),senderAddress,senderPort);
                }
                if(received.Type.equals("request-random-brain"))
                {
                    Random randomGenerator = new Random();
                    int index = randomGenerator.nextInt(joinInfo.size());
                    this.sendAddress(new ResultModel<String>(joinInfo.get(index).host,"result", received.whoAmI),senderAddress,senderPort);
                }


            }
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
    public class GroupException extends Exception {

        public GroupException(String s) {
            super(s);
        }

    }

}


