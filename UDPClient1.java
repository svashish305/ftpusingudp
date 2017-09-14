import java.io.*; 
import java.net.*; 
  
public class UDPClient1 { 
    public static void main(String args[]) throws Exception 
    { 
     try {
        String serverHostname = new String ("127.0.0.1");

        if (args.length > 0)
           serverHostname = args[0];
  
      BufferedReader inFromUser = 
        new BufferedReader(new InputStreamReader(System.in)); 
  
      DatagramSocket clientSocket = new DatagramSocket(); 
  
      InetAddress IPAddress = InetAddress.getByName(serverHostname); 
      System.out.println ("Attemping to connect to " + IPAddress + 
                          ") via UDP port 10000");
  
      byte[] sendData = new byte[1024]; 
      byte[] receiveData = new byte[1024]; 
  
      System.out.print("Enter Message: ");
      String sentence=inFromUser.readLine();
      
      sendData = sentence.getBytes();         

      System.out.println ("Sending data to " + sendData.length + 
                          " bytes to server.");
      DatagramPacket sendPacket = 
         new DatagramPacket(sendData, sendData.length, IPAddress, 10000); 
  
      clientSocket.send(sendPacket); 
  
      DatagramPacket receivePacket = 
         new DatagramPacket(receiveData, receiveData.length); 
  
      System.out.println ("Waiting for return packet");
      clientSocket.setSoTimeout(100000);

      try {
           clientSocket.receive(receivePacket); 
           String modifiedSentence = 
               new String(receivePacket.getData()); 
  
           InetAddress returnIPAddress = receivePacket.getAddress();
     
           int port = receivePacket.getPort();

           System.out.println ("From server at: " + returnIPAddress + 
                               ":" + port);
           System.out.println("Message: " + modifiedSentence); 

          }
      catch (SocketTimeoutException ste)
          {
           System.out.println ("Timeout Occurred: Packet assumed lost");
      }
  
      clientSocket.close(); 
     }
   catch (UnknownHostException ex) { 
     System.err.println(ex);
    }
   catch (IOException ex) {
     System.err.println(ex);
    }
  } 
    static String getFilecredentials(String a)
    {
        try
        {
            String res="";
            File f=new File(a);
            System.out.println("File length="+f.length());
            res+=get64long(f.length());
            res+=getseq(500);
            return res;
        }
        catch(Exception fw)
        {
            System.out.println(fw);
        }
        return null;
    }
    static String get64long(long x)
    {
        String temp=Long.toBinaryString(x);
        for(int i=temp.length();i<64;i++)
            temp="0"+temp;
        return temp;
    }
     static String getseq(int n)
    {
        String temp=Integer.toBinaryString(n);
        for(int i=temp.length();i<32;i++)
            temp="0"+temp;
        return temp;
    }
} 
