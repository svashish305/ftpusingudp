/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.net.*;
import java.io.*;
import java.util.*;
/**
 *
 * @author shubhashish
 */
public class UDPclient extends Thread{

    DatagramSocket client;
    byte [] senddata;
    byte [] receivedata;
    String host;
    int mss,n,port;
    UDPclient()
    {
        try
        {
            client=new DatagramSocket();
        }
        catch(SocketException e)
        {
            System.err.println(e);
        }
    }
    public static void main(String [] args)
    {
        if(args[0].equals("start")&&args[1].equals("simple_ftp"))
        {
            Scanner sc=new Scanner(System.in);
            do
            {
                System.out.print("simple_ftp.>");
                String ip[]=sc.nextLine().split(" ");
                
            }while(!sc.nextLine().equals("bye"));
        }
    }
    public void run()
    {
        try
        {
            receivedata= new byte[mss+8];
            DatagramPacket received=new DatagramPacket(receivedata,receivedata.length);
            client.receive(received);
            if(check(received))
            {
                
            }
        }
        catch(IOException e)
        {
            System.out.println(e);
        }
    }
    boolean check(DatagramPacket p)
    {
        boolean res=false;
        
        return res;
    }
}