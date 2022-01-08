package threads;

import interfaces.ReceiveHeartbeatInterface;
import models.Heartbeat;
import utils.ByteArrayUtils;

import java.io.IOException;
import java.net.DatagramPacket;

public class ReceiveHearbeatThread implements Runnable {
    protected static Group group = null;
    protected  static ReceiveHeartbeatInterface receiveHeartbeatInterface;
    public ReceiveHearbeatThread(Group group, ReceiveHeartbeatInterface receiveHeartbeatInterface){
        this.group = group;
        this.receiveHeartbeatInterface = receiveHeartbeatInterface;
    }

    @Override
    public void run() {
        try {
            byte[] buf = new byte[1000*10*100];
            while (true) {
                DatagramPacket msgPacket = new DatagramPacket(buf, buf.length);
                group.multicastSocket.receive(msgPacket);
                Heartbeat heartbeat = (Heartbeat) ByteArrayUtils.deserializeBytes(msgPacket.getData());
                if(heartbeat.hearBeatType.equals("heartbeat") && heartbeat.sentBy.equals("PROCESSOR")) {
                    this.receiveHeartbeatInterface.hearbeat(heartbeat);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
