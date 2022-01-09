import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import interfaces.BalancerInterface;
import models.*;
import interfaces.ProcessorInterface;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import utils.ByteArrayUtils;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.UUID;

import static java.lang.System.exit;

public class Client {
    private String identification = UUID.randomUUID().toString();
    private static String martelada = "";
    public Client() throws JSchException, SftpException, IOException, NotBoundException, ClassNotFoundException {
        Integer option = -1;
        if(martelada.isEmpty()) {
             option = draw_menu();
        }
        else{
            option = 1;
        }
        switch (option)
        {
            case 1:
                ExecuteScript();
                break;
            case 2:
                new NotImplementedException();
                break;
            case 3:
                GetResult("");
                break;
            case 0:
                exit(0);
                break;
            default:
                System.out.println("Option not found...");
        }

    }
    private synchronized void ExecuteScript() throws JSchException, SftpException {
        String s = null;
        Script script = new Script();
        if(martelada.isEmpty()) {

            Scanner scanner = new Scanner(System.in);

            System.out.println("\nScript to Execute:");
            s = scanner.nextLine();
        }else{
            s = martelada;
        }
        Path p = create_temp_file(s);
        UploadFile(p);
        script.script = s;
        script.file = p.getFileName().toString();

        ProcessorInterface processorInterface = null;
        BalancerInterface processorReplicaManagerInterface = null;

        try{

            try {
                BalancerInterface remote =  (BalancerInterface)Naming.lookup("rmi://localhost:2024/balancer");
                ProcessorInfo processorInfo = remote.get();
                processorInterface = (ProcessorInterface) Naming.lookup(processorInfo.serverAddress);

            } catch (RemoteException ex)
            {
                System.out.println(ex.getMessage());
                BalancerInterface remote =  (BalancerInterface)Naming.lookup("rmi://localhost:2024/balancer");
                ProcessorInfo processorInfo = remote.get();
                processorInterface = (ProcessorInterface) Naming.lookup(processorInfo.serverAddress);
            }


            String id = processorInterface.run(script);
            System.out.println(id);
            wait(5000);
            GetResult(id);

        }
        catch(Exception e)
        {System.out.println(e.getMessage()); }
    }
    private void GetResult(String scriptUUID) throws IOException, NotBoundException, ClassNotFoundException {

        //regiom mc-brain
         DatagramSocket socket;
         InetAddress group;
         byte[] buf;

        socket = new DatagramSocket();
        group = InetAddress.getByName("224.0.0.1");



        BrainRequestDetail brainRequestDetail = new BrainRequestDetail();

        brainRequestDetail.SearchId = scriptUUID;
        brainRequestDetail.whoAmI = identification;
        brainRequestDetail.Type = "request-brain-script-by-uuid";

        buf = ByteArrayUtils.serializeObject(brainRequestDetail);

        DatagramPacket packet = new DatagramPacket(buf, buf.length, group, 6784);
        ResultModel<Script> response = null;

        socket.send(packet);
        while (true) {
            response = receive(socket);
            if(response.destination.equals(identification)) {
                    System.out.println(response.object.PrintInfo());
                    break;
            }
        }

        socket.close();


        //endregion


        //region get result from brain

        //endregion

    }


    public ResultModel<Script> receive(DatagramSocket socket ) throws IOException, ClassNotFoundException {
        byte[] data = new byte[100*100*10];
        DatagramPacket inPacket = new DatagramPacket(data, 0, data.length);
        socket.receive(inPacket);

        return  (ResultModel<Script>) ByteArrayUtils.deserializeBytes(inPacket.getData());
    }

    private Path create_temp_file(String script){
        String tmpdir = System.getProperty("java.io.tmpdir");
        Path temp = null;
        try {

            // Create an temporary file
            temp = Files.createTempFile(String.valueOf(UUID.randomUUID()), ".txt");

            Writer writer = null;
            try {
                writer = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(String.valueOf(temp.toAbsolutePath())), "utf-8"));
                writer.write(script);
            } catch (IOException ex) {
                // Report
            } finally {
                try {writer.close();} catch (Exception ex) {/*ignore*/}
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return temp;
    }
    private void UploadFile(Path path) throws JSchException, SftpException {


        SFTPClient sftpClient = new SFTPClient();
        sftpClient.upload(path);

    }

    public static void printMenu(String[] options){
        System.out.println("************** menu **************");
        for (String option : options){
            System.out.println(option);
        }
        System.out.print("Choose your option : ");
    }

    public Integer  draw_menu() {
        String[] options = {
                "1 - Execute Script",
                "2 - Credits",
                "3 - Get Result",
                "0 - Exit",
        };
        Scanner scanner = new Scanner(System.in);
        int option = -1;
        while (option != 0 && option != 1 && option != 2 && option != 3){
            printMenu(options);
            try {
                option = scanner.nextInt();
            }
            catch (InputMismatchException ex){
                System.out.println("Please enter an integer value between 1 and " + options.length);
                scanner.next();
            }
            catch (Exception ex){
                System.out.println("An unexpected error happened. Please try again");
                scanner.next();
            }

        }
        return option;
    }


    /**
     * Run this class as an application.
     */
    public static void main(String[] args)  {
        martelada = args[0];
        try {
            Client client = new Client();
        } catch (JSchException e) {
            e.printStackTrace();
        } catch (SftpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


}