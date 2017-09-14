/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;
import java.net.*;
/**
 *
 * @author shubhashish
 */
public class simple_ftp_client {
    String hostname="127.0.0.1";
    int port,n,mss,count=0,buffer=0,seq,ack=0;
    DatagramSocket clientsocket;
    File name;
    byte [][] fs;
    InetAddress ip;
    boolean done=false;
    long rtt=0;
    simple_ftp_client(String host,int a,String f,int b,int c)
    {
        hostname=host;
        port=a;
        n=b;
        mss=c;
        try
        {
            clientsocket=new DatagramSocket();
            name=new File(f);
            ip=InetAddress.getByName(hostname);
        }
        catch(SocketException s)
        {
            System.err.println(s);
        }
        catch(UnknownHostException h)
        {
            System.err.println(h);
        }
    }
    public static void main(String []args)
    {
//        String arg[]=new String[5];
//        arg[0]="192.168.1.117";
//        arg[1]="7735";
//        arg[2]="02.overview.pdf";
//        arg[3]="64";
//        arg[4]="100";
        long avgdelay=0;
       simple_ftp_client sfc=new simple_ftp_client(args[0],Integer.parseInt(args[1]),args[2],Integer.parseInt(args[3]),Integer.parseInt(args[4]));
       System.out.println("Preparing file for transfer");
       sfc.filesplit();
       System.out.println("Sending File...Please wait");
       sfc.rtt=sfc.execute();
       System.out.println("Avg. delay="+(sfc.rtt-avgdelay));
       System.out.println("File transfer complete!!");
    }
    long execute()
    {
        long strt,end;                      //task1
        strt=System.currentTimeMillis();        //task1
        while((count*mss)<(int)name.length())
        {
            while(buffer<n&&fs.length>count)
            {
                
                DatagramPacket send=rdt_send(count);
                try
                {
                    clientsocket.send(send);
                    //System.out.println("Packet sent with seq no!!"+count);
                    buffer++;
                    count++;
                }
                catch(IOException e)
                {
                    System.err.println(e);
                }
            }
            byte [] receivedata=new byte[1024];
            DatagramPacket receive=new DatagramPacket(receivedata,receivedata.length);
            done=false;
            try
            {
                clientsocket.setSoTimeout(100);
                one:while(!done)
                {
                    clientsocket.receive(receive);
//                    System.out.println("Packet received!!");
                    String unpack=new String(receive.getData());
                    if(unpack.substring(48,64).equals("1010101010101010"))
                    {
//                        System.out.println("Packet is acknowledgement");
                        String seqtemp=unpack.substring(0,32);
                        int seqn=bintodeci(seqtemp);
                        ack=seqn;
//                        System.out.println("ack::"+ack);
                        if(ack==count)
                        {
//                            System.out.println("All packets are acknowledged now");
                            done=true;
                            count=ack;
                            buffer=0;
                            break one;
                        }
                    }
                }
            }
            catch(SocketTimeoutException sto)
            {
                System.out.println("Timeout, Sequence number="+(ack));
                buffer=count-ack;
                count=ack;
            }
            catch(IOException ioe)
            {
                System.err.println(ioe);
            }
        }
        end=System.currentTimeMillis();                 //task1
        endfile();
        return end-strt;                                //task1
        
    }
    void filesplit()
    {
        int i=(int)name.length()/mss;
        int j;
        System.out.println("File Size::"+name.length()+"bytes");
        fs=new byte[i+1][mss];
        try
        {
            byte [] bytearray  = new byte [(int)name.length()];
            FileInputStream fin = new FileInputStream(name);
            BufferedInputStream bin = new BufferedInputStream(fin);
            bin.read(bytearray,0,bytearray.length);
            for(j=0;j<bytearray.length;j++)
            {
              fs[j/mss][j%mss]=bytearray[j];
              //System.out.println(j);
            }
            i=j/mss;
            while(j%mss!=0)
            {
                j++;
                fs[i][j%mss]=0;
                //System.out.println(j);
            }
        }
        catch(FileNotFoundException e)
        {
            System.err.println();
        }
        catch(IOException er)
        {
            System.err.println(er);
        }
    }
   String getseq(int n)
    {
        String temp=Integer.toBinaryString(n);
        for(int i=temp.length();i<32;i++)
            temp="0"+temp;
        return temp;
    }
    DatagramPacket rdt_send(int c)
    {
       DatagramPacket p;
       String sequ=getseq(c);
       String chcksum=checksum(fs[c]);
       String type="0101010101010101";
       String header=sequ+chcksum+type;
       byte[] senddata;
       senddata=header.getBytes();
       byte[] pack= new byte[mss+senddata.length];
       for(int i=0;i<pack.length;i++)
       {
           if(i<senddata.length)
               pack[i]=senddata[i];
           else
               pack[i]=fs[c][i-senddata.length];
       }
       p= new DatagramPacket(pack,pack.length,ip,port);
       return p;
    }
    String checksum(byte [] b)
    {
       byte sum1=0,sum2=0;
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
    int bintodeci(String str)
    {
        double j=0;
        for(int i=0;i<str.length();i++)
        {
            if(str.charAt(i)== '1')
            {
                j=j+ Math.pow(2,str.length()-1-i);
            }
        }
        return (int) j;
    }
    void endfile()
    {
        try
        {
           String temp=Integer.toBinaryString(count);
            for(int i=temp.length();i<32;i++)
                temp="0"+temp;
            byte emp[]=new byte[mss];
            String check=checksum(emp);
            String eof=temp+check+"0000000000000000"+(new String(emp));
            byte b[]=eof.getBytes();
            DatagramPacket p=new DatagramPacket(b,b.length,ip,7735);
            clientsocket.send(p);
            //System.out.println("EOF sent");
        }
        catch(IOException ioe){
            System.err.print(ioe);
        }
    }
}
