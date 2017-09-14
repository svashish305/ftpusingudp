/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package arq;
import java.net.*;
import java.io.*;
import java.util.*;
/**
 *
 * @author shubhashish
 */
public class simple_ftp_server {
    DatagramSocket serverSocket;
    static int portn;
    static InetAddress ip;
    String filename;
    FileOutputStream fos ;
    int recvack=0;
    boolean download=true;
    public static void main(String[] args)
    { 
        //String[] arg=new String[3]
        System.out.println("Waiting for packets");
        simple_ftp_server sfs=new simple_ftp_server();
        sfs.filename=args[1];  
        int len=0,currenttot=0;
     try
     {
      int portno=Integer.parseInt(args[0]);
      File f=new File(args[1]);
      float p=Float.parseFloat(args[2]);
      sfs.serverSocket = new DatagramSocket(portno); 
      byte[] receiveData;
      byte[] sendData  = new byte[10000000];
      float r;
      Random rs=new Random();
       FileOutputStream stream = new FileOutputStream(f);
      BufferedOutputStream bos=new BufferedOutputStream(stream);
      one:while(sfs.download) 
        { 
          receiveData = new byte[2048]; 
          DatagramPacket receivePacket =new DatagramPacket(receiveData, receiveData.length);
          sfs.serverSocket.receive(receivePacket);
          byte [] db=new byte[receivePacket.getLength()-64];
          System.arraycopy(receiveData,64, db,0,db.length);
//          System.out.println(receivePacket.getLength());
          byte [] received=receivePacket.getData();
//          System.out.println("Data length of received packet=="+received.length);
          int seqno=sfs.bintodeci(received,32);
          String chck=new String(Arrays.copyOfRange(received, 32, 48));
          String type=new String(Arrays.copyOfRange(received,48,64));
          byte [] data=Arrays.copyOfRange(received, 64,received.length);
//          System.out.println("Bytes of actual data transferred"+data.length);
          r=rs.nextFloat();
          //System.out.println(r);
          if(r<=p)
          {
              System.out.println("Packet loss, Sequence number="+seqno);
              continue one;
          }
          sfs.ip=receivePacket.getAddress();
          sfs.portn=receivePacket.getPort();
          //System.out.println("Packet received from"+sfs.ip);
          String temp=sfs.checksum(data);
          if(temp.equals(chck))
          {
              //System.out.println("checksum validated");
                if(sfs.recvack==seqno)
                {
                    sfs.recvack++;
                    //System.out.println("Packet is valid");
                   
                    if(type.equals("0101010101010101"))
                    {
                        stream.write(db);
//                        System.arraycopy(data,0,sendData,currenttot,data1.length());
//                        currenttot+=data1.length();
                        DatagramPacket ack=sfs.generate(sfs.recvack);
                        sfs.serverSocket.send(ack);
                        //System.out.println("Acknowledgement sent for sequence no::"+sfs.recvack); 
                    }
                    else
                        sfs.download=false;
                }
           }
        }
      bos.flush();
      bos.close();
      System.out.println(sfs.filename+" has been downloaded");
     }
     catch(SocketException s)
     {
         System.err.println("Port occupied!!");
     }
     catch(IOException ioe)
     {
         System.err.println(ioe);
     }
    }
    String checksum(byte [] b)
    {
       byte sum1=0,sum2=0;
       //System.out.println(b.length+" size of byte received");
       for(int i=0;i<b.length;i=i+2)
       {
           sum1+=b[i];
           if((i+1)<b.length)
            sum2+=b[i+1];
       }
       String res=Byte.toString(sum1),res1=Byte.toString(sum2);
       for(int i=res.length();i<8;i++)
           res="0"+res;
       for(int i=res1.length();i<8;i++)
           res1="0"+res1;
       return res+res1;
    }
    DatagramPacket generate(int seq)
    {
        DatagramPacket p=null;
        String sequ=getseq(seq);
        sequ+="00000000000000001010101010101010";
        byte []send=sequ.getBytes();
        try
        {
            p=new DatagramPacket(send,send.length,ip,portn);
        }
        catch(Exception e)
        {
            System.err.println(e);
        }
        return p;
    }
    String getseq(int n)
    {
        String temp=Integer.toBinaryString(n);
        for(int i=temp.length();i<32;i++)
            temp="0"+temp;
        return temp;
    }
   
    int bintodeci(byte [] st,int n){
     String str=new String(Arrays.copyOfRange(st, 0, 32)); 
     //System.out.println(str);
    double j=0;
    for(int i=0;i<str.length();i++){
        if(str.charAt(i)== '1'){
         j=j+ Math.pow(2,str.length()-1-i);
     }

    }
    //System.out.println("bintodeci"+(int)j);
    return (int) j;
}

}
