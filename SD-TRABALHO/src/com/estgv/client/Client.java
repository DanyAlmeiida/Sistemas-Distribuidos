package com.estgv.client;


import com.estgv.interfaces.ProcessorReplicaManagerInterface;
import com.estgv.interfaces.ScriptsInterface;
import com.estgv.models.ProcessorInfo;
import com.estgv.models.Script;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.UUID;

import static java.lang.System.exit;

public class Client {

    public Client() throws JSchException, SftpException {
        Integer option = draw_menu();
        switch (option)
        {
            case 1:
                ExecuteScript();
                break;
            case 2:
                new NotImplementedException();
                break;
            case 0:
                exit(0);
                break;
            default:
                System.out.println("Option not found...");
        }

    }
    private void ExecuteScript() throws JSchException, SftpException {
        Script script = new Script();
        Scanner scanner = new Scanner(System.in);

        System.out.println("\nScript to Execute:");
        String s = scanner.nextLine();
        Path p = create_temp_file(s);
        UploadFile(p);
        script.script = p.getFileName().toString();

        ScriptsInterface scriptsInterface = null;
        ProcessorReplicaManagerInterface processorReplicaManagerInterface = null;
        try{
            try {
                processorReplicaManagerInterface = (ProcessorReplicaManagerInterface) Naming.lookup("rmi://localhost:2024/processor_manager");
                ProcessorInfo processorInfo = processorReplicaManagerInterface.get();
                scriptsInterface = (ScriptsInterface) Naming.lookup(processorInfo.serverAddress);
            } catch (NotBoundException | RemoteException | MalformedURLException ex)
            {ex.printStackTrace(); }



            String id = scriptsInterface.run(script);
            System.out.println(id);
        }
        catch(Exception e)
        {System.out.println(e.getMessage()); }
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
    public static void main(String[] args) throws JSchException, SftpException {
        Client client = new Client();
    }


}
