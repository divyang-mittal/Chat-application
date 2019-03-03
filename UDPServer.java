import java.io.*;
import java.net.*;
class UDPServer
{
   public static void main(String args[]) throws Exception//main method of server;recieves message from one client and sends it to another client
   {
      DatagramSocket serverSocket = new DatagramSocket(7071);
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
         DatagramPacket sendPacket =new DatagramPacket(sendData, sendData.length,IPAddress,8081);
         serverSocket.send(sendPacket);
      }
   }
}