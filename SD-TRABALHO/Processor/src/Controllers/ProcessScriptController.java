package Controllers;

import com.jcraft.jsch.JSchException;
import interfaces.BrainInterface;
import models.ResultModel;
import models.BrainRequestDetail;
import models.SFTPClient;
import models.Script;
import models.ScriptQueue;
import utils.ByteArrayUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

public class ProcessScriptController {

    public static void process(HashMap<String, ScriptQueue> scriptQueue, Script script, String serverId) throws JSchException, IOException {

        //region read-file-script to execute

        SFTPClient sftpClient = new SFTPClient();

        String sb = sftpClient.get_content(script.file);

        //endregion

        //region execute-script

        ProcessBuilder processBuilder = new ProcessBuilder("cmd","/c",sb);
        Process process = processBuilder.start();
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line + System.lineSeparator());
            }
            script.processed_by = serverId;
            script.result = output.toString();

            String brain_available = AvailableRMIAddress(serverId);
            if(brain_available.equals(""))
                throw new Exception("Brain not found");

            BrainInterface brainInterface = (BrainInterface) Naming.lookup(brain_available);
            brainInterface.set_result(script);

        } catch (NotBoundException | RemoteException | MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //endregion
    }

    public static String AvailableRMIAddress(String serverId){

        String AvailableRMIAddress = "";
        try {

            //regiom mc-brain
            DatagramSocket socket;
            InetAddress group;
            byte[] buf;

            socket = new DatagramSocket();
            group = InetAddress.getByName("224.0.0.1");


            BrainRequestDetail brainRequestDetail = new BrainRequestDetail();

            brainRequestDetail.whoAmI = serverId;
            brainRequestDetail.Type = "request-random-brain";

            buf = ByteArrayUtils.serializeObject(brainRequestDetail);

            DatagramPacket packet = new DatagramPacket(buf, buf.length, group, 6784);
            ResultModel<String> response = null;

            socket.send(packet);
            while (true) {
                response = receive(socket);
                if (response.destination.equals(serverId)) {
                    AvailableRMIAddress = response.object;
                    break;
                }
            }

            socket.close();


            //endregion
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return AvailableRMIAddress;
    }

    public static ResultModel<String> receive(DatagramSocket socket ) throws IOException, ClassNotFoundException {
        byte[] data = new byte[100*100*10];
        DatagramPacket inPacket = new DatagramPacket(data, 0, data.length);
        socket.receive(inPacket);

        return  (ResultModel<String>) ByteArrayUtils.deserializeBytes(inPacket.getData());
    }

    public static ArrayList<String> AvailableBrainsRMIAddressArrayList(String serverId){

        ArrayList<String> AvailableRMIAddress = null;
        try {

            //regiom mc-brain
            DatagramSocket socket;
            InetAddress group;
            byte[] buf;

            socket = new DatagramSocket();
            group = InetAddress.getByName("224.0.0.1");


            BrainRequestDetail brainRequestDetail = new BrainRequestDetail();

            brainRequestDetail.whoAmI = serverId;
            brainRequestDetail.Type = "request-all-brains";

            buf = ByteArrayUtils.serializeObject(brainRequestDetail);

            DatagramPacket packet = new DatagramPacket(buf, buf.length, group, 6784);
            ResultModel<ArrayList<String>> response = null;

            socket.send(packet);
            while (true) {
                response = receiveArrayList(socket);
                if (response.destination.equals(serverId)) {
                    AvailableRMIAddress = response.object;
                    break;
                }
            }

            socket.close();


            //endregion
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return AvailableRMIAddress;
    }

    public static ResultModel<ArrayList<String>> receiveArrayList(DatagramSocket socket ) throws IOException, ClassNotFoundException {
        byte[] data = new byte[100*100*10];
        DatagramPacket inPacket = new DatagramPacket(data, 0, data.length);
        socket.receive(inPacket);

        return  (ResultModel<ArrayList<String>>) ByteArrayUtils.deserializeBytes(inPacket.getData());

    }
}
