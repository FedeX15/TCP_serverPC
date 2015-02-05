package com.fedex.school.tcpserver;

import java.io.*;
import java.net.*;

/**
 * @author Federico Matteoni
 */
public class TCPSocket_server
{
    GUI gui;
    public int clientConnessi;
    
    public TCPSocket_server() {
        gui = new GUI(this);
        gui.setVisible(true);
        clientConnessi = 0;
    }
    
    /**
     * @param args Parametri da linea di comando
     */
    public static void main(String[] args) {
        TCPSocket_server server = new TCPSocket_server();
    }
    
    public void avviaServer() {
        try {
            ServerSocket socket = new ServerSocket(8888);
            gui.setServerStatus("SERVER ONLINE");
            String lastClient;
            Socket socketcomm = null;
            do {
                try {
                    socketcomm = socket.accept();
                    //socket.setSoTimeout(10000);
                    this.clientConnessi++;
                    gui.setClientStatus("Client " + socketcomm.getInetAddress().toString() + " connesso\t[" + this.clientConnessi + "]");
                    new TCPSocket_thread(socketcomm, this, gui).start();
                } catch (SocketTimeoutException timeout) {
                    if (this.clientConnessi == 0)
                    {
                        break;
                    }
                }
            } while (true);
            
            socket.close();
            gui.setServerStatus("SERVER OFFLINE");
        } catch (IOException ex) {}
    }
}
