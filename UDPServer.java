import java.io.*; 
import java.net.*; 
  
class UDPServer { 
  public static void main(String args[]) throws Exception 
    { 
     try
     { 
      DatagramSocket serverSocket = new DatagramSocket(10000); 
  
      byte[] receiveData = new byte[1024]; 
      byte[] sendData  = new byte[1024]; 
  
      while(true) 
        { 
  
          receiveData = new byte[1024]; 

          DatagramPacket receivePacket = 
             new DatagramPacket(receiveData, receiveData.length); 

          System.out.println ("Waiting for datagram packet");

          serverSocket.receive(receivePacket); 

          String sentence = new String(receivePacket.getData()); 
          System.out.println(bintodeci(sentence.substring(0, 64))+"::Length of file");
          System.out.println("MSS::"+bintodeci(sentence.substring(64)));
          InetAddress IPAddress = receivePacket.getAddress(); 
  
          int port = receivePacket.getPort(); 
  
          System.out.println ("From: " + IPAddress + ":" + port);
          System.out.println ("Message: " + sentence);

          sentence=sentence.toUpperCase();
          sendData = sentence.getBytes(); 
  
          DatagramPacket sendPacket = 
             new DatagramPacket(sendData, sendData.length, IPAddress, 
                               port); 
  
          serverSocket.send(sendPacket); 

          System.out.println("packet sent");
        } 

     }
      catch (SocketException ex) {
        System.out.println("UDP Port 9876 is occupied.");
        System.exit(1);
      }

    } 
  static int bintodeci(String str){
    double j=0;
    for(int i=0;i<str.length();i++){
        if(str.charAt(i)== '1'){
         j=j+ Math.pow(2,str.length()-1-i);
     }

    }
    return (int) j;
}
}  
