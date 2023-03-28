package ChatSystem;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mamadou
 */
public class TCPClient {
    static BufferedWriter bw; 
    static BufferedReader br;

  public static void main(String[] args) {

    Socket s;

    try {

      System.out.println("Connection en cours");

      s = new Socket(InetAddress.getLocalHost(), ChatSystem.PORT);

      System.out.println("Connection réussie !");

      bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
      br = new BufferedReader(new InputStreamReader(s.getInputStream()));

     

    } catch (UnknownHostException ex) {

      ex.printStackTrace();
      System.out.println("Error obtaining the adress" + ex);

    } catch (IOException ex) {

      ex.printStackTrace();
      System.out.println("Error creating the socket" + ex);

    }

  }

}
