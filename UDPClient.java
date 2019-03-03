import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
class SenderThread extends Thread implements ActionListener// thread to recieve the message from the server
{
   DatagramSocket clientSocket;
   JButton button;
   JTextArea areaEnter;
   JTextArea areaShow;
   public SenderThread(DatagramSocket client,JButton button,JTextArea areaEnter,JTextArea areaShow)//constructor to initialize various variables
   {
      clientSocket=client;
      this.button=button;
      this.areaEnter=areaEnter;
      this.areaShow=areaShow;
      this.button.addActionListener(this);
   }
    public void actionPerformed(ActionEvent e){ //to send the message when submit button is hit  
      String text=areaEnter.getText();
      try{
         InetAddress ia = InetAddress.getByName("localhost");
         byte[] sendData = new byte[1024];
         sendData = text.getBytes();
         DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,ia, 7070);
         clientSocket.send(sendPacket);
         areaShow.append("\n");
         areaShow.append("You: "+areaEnter.getText());
         areaEnter.setText("");
      }
      catch (IOException ex) 
         {
            System.err.println(ex);
         }
   }
}  

class RecieverThread extends Thread 
{
   DatagramSocket clientSocket;
   JTextArea areaShow;
   public RecieverThread(DatagramSocket client,JTextArea areaShow)//constructor to initialize variables
   {
      clientSocket=client;
      this.areaShow=areaShow;
   }
   public void run() //thread program to continuously recieve messages from server
   {          
      String host = "localhost";   
      while (true) 
      {
         try
         {
            InetAddress ia = InetAddress.getByName(host);
            while(true)
            {
               byte[] receiveData = new byte[1024];
               DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
               clientSocket.receive(receivePacket);
               String modifiedSentence = new String(receivePacket.getData());      
               areaShow.append("\n");
               areaShow.append("Friend "+modifiedSentence);
            }
         }
         catch (IOException ex) 
         {
            System.err.println(ex);
         }
      }
   }
}   


class UDPClient
{
   JTextArea areaEnter;
   JTextArea areaShow; 
   JButton button; 
   UDPClient() //constructor to setup the user interface
   {
      JFrame f = new JFrame("User1");
      areaEnter = new JTextArea();
      areaShow = new JTextArea(); 
      button = new JButton("Send"); 

      areaShow.setEditable(false);
      
      JScrollPane paneEnter = new JScrollPane(areaEnter);
      JScrollPane paneShow = new JScrollPane(areaShow);
      
      paneEnter.setBounds(40,420,300,100);
      paneShow.setBounds(40,20,300,400);
      button.setBounds(140,550,100,50);
      
      f.getContentPane().setBackground(Color.black);


      paneShow.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
      paneEnter.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

      f.add(paneShow);
      f.add(paneEnter);
      f.add(button);
      f.setSize(400,650);
      f.setLayout(null);
      f.setVisible(true);
   }
   public static void main(String args[]) throws Exception//main program
   //creates two threads one to recieve message and one to send it
   {
      UDPClient client=new UDPClient();
      DatagramSocket clientSocket = new DatagramSocket(8081);
      SenderThread sender = new SenderThread(clientSocket,client.button,client.areaEnter,client.areaShow);
      RecieverThread receiver = new RecieverThread(clientSocket,client.areaShow);
      sender.start();
      receiver.start();  
   }
}