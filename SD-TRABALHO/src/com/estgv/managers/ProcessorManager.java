package com.estgv.managers;

import com.estgv.interfaces.ProcessorReplicaManagerInterface;
import com.estgv.models.ProcessorInfo;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.UUID;

public class ProcessorManager  extends UnicastRemoteObject implements ProcessorReplicaManagerInterface {

    private static final long serialVersionUID = 1L;
    static ArrayList<ProcessorInfo> Replicas = new ArrayList<>();

    public ProcessorManager() throws RemoteException{

    }

    @Override
    public void add(ProcessorInfo processorInfo) throws RemoteException {
        Replicas.add(processorInfo);
    }


    @Override
    public ProcessorInfo get() throws RemoteException {

        for(ProcessorInfo info : Replicas){
            System.out.println(info.server_id + " - " +  new DecimalFormat("#%").format(info.cpu_usage));
        }

        ProcessorInfo min = null;
        for (ProcessorInfo  entry : Replicas) {
            System.out.println(entry.cpu_usage);
            if (min == null || min.cpu_usage > entry.cpu_usage) {
                min = entry;
            }
        }

        System.out.println("Picked Server [ " + min.server_id + " ] - " + new DecimalFormat("#%").format(min.cpu_usage));
        return min;
    }


    @Override
    public void set_cpu_usage(String processorObejctId, double cpu_usage) throws RemoteException {
        ProcessorInfo processorInfo = Replicas.stream()
                .filter((ProcessorInfo x) ->  x.server_id.equals(processorObejctId )).findFirst().orElse(null);
        if(processorInfo == null)
            return;
        else
            Replicas.stream().filter(x -> x.server_id.equals(processorObejctId)).findFirst().get().cpu_usage =cpu_usage;
    }

    @Override
    public double get_cpu_usage(String processorObejctId) throws RemoteException {
        return Replicas.stream()
                .filter((ProcessorInfo x) ->  x.server_id.equals(processorObejctId )).findFirst().orElse(null).cpu_usage;
    }
}