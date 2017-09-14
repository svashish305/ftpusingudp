

import java.io.*;
import java.net.*;
import java.util.*;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.*;
/**
 *
 * @author shubhashish
 */
public class practice {
    byte [][] fs;
    File name=new File("F:\\NetBeansProjects\\arq\\src\\1.txt");
    int mss=100;
    int count=0;
    public static void main(String []args)
    {
        Scanner sc=new Scanner(System.in);
        //System.out.println(Integer.toBinaryString(Integer.parseInt(args[0])));
        practice p=new practice();
        System.out.println("Enter a message");
        String in=sc.nextLine();
       
        String temp1=in.trim();
//        String temp=new String(temp2);
//        byte [] temp1=temp.getBytes();
//        System.out.println(p.checksum(temp1));
//        if(in.equals(temp))
//        {
            System.out.println(temp1.length()+"\n"+temp1);
       // }
        //p.filesplit();
    }
    void filesplit()
    {
        int i=(int)name.length()/mss;
        int j=0;
        System.out.println(i);
        fs=new byte[i+1][mss];
        try
        {
            byte [] bytearray  = new byte [(int)name.length()];
            System.out.println(name.length());
            FileInputStream fin = new FileInputStream(name);
            BufferedInputStream bin = new BufferedInputStream(fin);
            bin.read(bytearray,0,bytearray.length);
            for(j=0;j<bytearray.length;j++)
            {
              fs[j/mss][j%mss]=bytearray[j];
              System.out.println(j);
            }
//            i=j/mss;
//            while(j%mss!=0)
//            {
//                j++;
//                fs[i][j%mss]=0;
//                System.out.println(j);
//            }
            for(int k=0;k<i/mss;k++)
            {
                rdt_send(k);
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
    void rdt_send(int c)
    {
       DatagramPacket p=null;
       String sequ=getseq(c);
       String chcksum=checksum(fs[c]);
       String type="0101010101010101";
       String header=sequ+chcksum+type;
       byte[] senddata;
       senddata=header.getBytes();
       System.out.println(senddata.length);
       byte[] pack= new byte[fs[c].length+senddata.length];
       for(int i=0;i<pack.length;i++)
       {
           if(i<senddata.length)
               pack[i]=senddata[i];
           else
               pack[i]=fs[c][i-senddata.length];
       }
//       p= new DatagramPacket(pack,pack.length,ip,7735);
 //     count++;
//       return p;
    }
    String checksum(byte [] b)
    {
       byte sum1=b[0],sum2=0;
       for(int i=1;i<b.length/2;i=i*2)
       {
           sum1+=b[i];
           sum2+=b[i];
       }
       String res=Byte.toString(sum1),res1=Byte.toString(sum2);
       for(int i=res.length();i<8;i++)
           res="0"+res;
       for(int i=res1.length();i<8;i++)
           res1="0"+res1;
       return res+res1;
    }
    int bintodeci(String str){
    double j=0;
    for(int i=0;i<str.length();i++){
        if(str.charAt(i)== '1'){
         j=j+ Math.pow(2,str.length()-1-i);
     }

    }
    return (int) j;
}
    
}
