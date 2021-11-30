package com.estgv.client;


import com.estgv.interfaces.ObjectRegistryInterface;
import com.estgv.interfaces.ScriptsInterface;
import com.estgv.models.Script;
import org.apache.commons.net.ftp.FTPClient;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.InputMismatchException;
import java.util.Scanner;

import static java.lang.System.exit;

public class Client {

    public Client()
    {
        Integer option = draw_menu();
        switch (option)
        {
            case 1:
                UploadFile();
                break;
            case 2:
                ExecuteScript();
                break;
            case 3:
                new NotImplementedException();
                break;
            case 0:
                exit(0);
                break;
            default:
                System.out.println("Option not found...");
        }

    }
    private void ExecuteScript(){
        ScriptsInterface scriptsInterface = null;
        ObjectRegistryInterface objectRegistryInterface = null;
        try{
            try {
                objectRegistryInterface = (ObjectRegistryInterface) Naming.lookup("rmi://localhost:2023/registry");
                String s =  objectRegistryInterface.resolve();
                scriptsInterface = (ScriptsInterface) Naming.lookup(s);
            } catch (NotBoundException | RemoteException | MalformedURLException ex)
            {ex.printStackTrace(); }

            Script script = new Script();
            Scanner scanner = new Scanner(System.in);

            System.out.println("\nScript to Execute: ");
            script.script = scanner.next();

            String id = scriptsInterface.run(script);
            System.out.println(id);
        }
        catch(Exception e)
        {System.out.println(e.getMessage()); }
    }

    private void UploadFile(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("\nFile Path: ");
        Path path = Paths.get(scanner.next());

        FTPHelper ftpHelper = new FTPHelper();
        try {
            boolean res = ftpHelper.uploadFile(path);
            if(res == true)
                System.out.println("File Uploaded!");
            else
                System.out.println("Couldn't upload the file. Please try again...");

            ftpHelper.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void printMenu(String[] options){
        System.out.println("************** menu **************");
        for (String option : options){
            System.out.println(option);
        }
        System.out.print("Choose your option : ");
    }

    public Integer  draw_menu() {
        String[] options = {"1 - Upload File",
                "2 - Execute Script",
                "3 - Credits",
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
    public static void main(String[] args)
    {
        Client client = new Client();
    }


}
