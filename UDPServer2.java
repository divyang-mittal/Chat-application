import java.io.*;
import java.net.*;
class UDPServer2
{
   public static void main(String args[]) throws Exception
   {
      DatagramSocket serverSocket = new DatagramSocket(7070);
      while(true)
      {
         byte[] receiveData = new byte[1024];
         byte[] sendData = new byte[1024];
         DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
         serverSocket.receive(receivePacket);
         String sentence = new String( receivePacket.getData());
         System.out.println("RECEIVED: " + sentence);
         InetAddress IPAddress = receivePacket.getAddress();
         sendData = sentence.getBytes();
         DatagramPacket sendPacket =new DatagramPacket(sendData, sendData.length,IPAddress,8080);
         serverSocket.send(sendPacket);
      }
   }
}