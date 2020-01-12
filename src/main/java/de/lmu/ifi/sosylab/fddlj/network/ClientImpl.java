package de.lmu.ifi.sosylab.fddlj.network;

import de.lmu.ifi.sosylab.fddlj.model.Model;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientImpl {

  public static final int PORT = 43200;
  private InetAddress serverAddress;
  private Model model;
  private Socket connection;
  private BufferedReader in;
  private ObjectOutputStream out;
  private boolean connectionEstablished;
  private boolean running;


  public ClientImpl(InetAddress inetAddress, Model model) {
    this.serverAddress = inetAddress;
    this.model = model;
  }

  public void startClient() {
    this.running = true;

    Thread connectorThread = new Thread(this::communicateWithServer);
  }

  private void communicateWithServer() {
    try {
      this.connection = new Socket(this.serverAddress, PORT);
      this.out = new ObjectOutputStream(connection.getOutputStream());
      this.out.flush();
      this.in = new BufferedReader(
                      new InputStreamReader(this.connection.getInputStream(), StandardCharsets.UTF_8));

      this.connectionEstablished = true;
    } catch (@SuppressWarnings("unused") IOException e) {
      // chessController.handleClientConnectionFailed();
      terminate();
    }

    while (running) {
      String receivedLine;
      try {
        receivedLine = in.readLine();
        processReceivedLine(receivedLine);
      } catch (IOException e) {
        terminate();
      }
    }
  }

  private void processReceivedLine(String receivedLine) {

    System.out.println("Received Line: " + receivedLine);

    /**
     * TODO Parse String to json
     */

    // this.model.placeDisk();
  }

  /**
   * Terminate the client by closing with the connection to the server.
   */
  public void terminate() {
    this.running = false;
    this.connectionEstablished = false;
    try {
      if (connection != null) {
        connection.close();
      }
      if (in != null) {
        in.close();
      }
      if (out != null) {
        out.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
