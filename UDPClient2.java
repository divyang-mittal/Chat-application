import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
class SenderThread extends Thread implements ActionListener
{
   DatagramSocket clientSocket;
   JButton button;
   JTextArea areaEnter;
   JTextArea areaShow;
   public SenderThread(DatagramSocket client,JButton button,JTextArea areaEnter,JTextArea areaShow)
   {
      clientSocket=client;
      this.button=button;
      this.areaEnter=areaEnter;
      this.areaShow=areaShow;
      this.button.addActionListener(this);
   }
    public void actionPerformed(ActionEvent e){
      String text=areaEnter.getText();
      try{
         InetAddress ia = InetAddress.getByName("localhost");
         byte[] sendData = new byte[1024];
         sendData = text.getBytes();
         DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,ia, 7071);
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
   public RecieverThread(DatagramSocket client,JTextArea areaShow)
   {
      clientSocket=client;
      this.areaShow=areaShow;
   }
   public void run() 
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
               areaShow.append("Friend: "+modifiedSentence);
            }
         }
         catch (IOException ex) 
         {
            System.err.println(ex);
         }
      }
   }
}   


class UDPClient2
{
   JTextArea areaEnter;
   JTextArea areaShow; 
   JButton button; 
   UDPClient2()
   {
      JFrame f = new JFrame("User2");
      areaEnter = new JTextArea();
      areaShow = new JTextArea(); 
      button = new JButton("Send"); 
      areaEnter.setBounds(20,450,200,100);
      areaShow.setBounds(20,20,200,400);
      button.setBounds(100,580,150,50);

      JScrollPane paneEnter = new JScrollPane(areaEnter);
      JScrollPane paneShow = new JScrollPane(areaShow);
      paneEnter.setBounds(20,440,200,100);
      paneShow.setBounds(20,20,200,400);

      paneShow.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
      paneEnter.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);


      f.add(paneShow);
      f.add(paneEnter);
      f.add(button);
      f.setSize(500,700);
      f.setLayout(null);
      f.setVisible(true);
   }
   // public void actionPerformed(ActionEvent e){
   //    String text=areaEnter.getText();
   //    areaShow.append("\n");
   //    areaShow.append(areaEnter.getText());
   //    areaEnter.setText("");
   // }
   public static void main(String args[]) throws Exception
   {
      UDPClient2 client=new UDPClient2();
      DatagramSocket clientSocket = new DatagramSocket(8080);
      SenderThread sender = new SenderThread(clientSocket,client.button,client.areaEnter,client.areaShow);
      RecieverThread receiver = new RecieverThread(clientSocket,client.areaShow);
      sender.start();
      receiver.start();  
   }
}