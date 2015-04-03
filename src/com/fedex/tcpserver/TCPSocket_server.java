package com.fedex.tcpserver;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * @author Federico Matteoni
 */
public class TCPSocket_server
{
    GUI gui;
    public int clientConnessi;
    public HashMap<InetAddress, Socket> listaClient;
    public boolean avviato;
    private ServerSocket socket;
    
    public TCPSocket_server() {
        gui = new GUI(this);
        gui.setVisible(true);
        clientConnessi = 0;
        listaClient = new HashMap<>();
        gui.updateLista(listaClient);
        avviato = false;
    }
    
    /**
     * @param args Parametri da linea di comando
     */
    public static void main(String[] args) {
        TCPSocket_server server = new TCPSocket_server();
    }
    
    public void avviaServer() {
        avviato = true;
        try {
            socket = new ServerSocket(8888);
            gui.setServerStatus("SERVER ONLINE [" + socket.getLocalSocketAddress().toString() + "]");
            Socket socketcomm = null;
            do {
                try {
                    socketcomm = socket.accept();
                    this.listaClient.put(socketcomm.getInetAddress(), socketcomm);
                    gui.updateLista(listaClient);
                    this.clientConnessi++;
                    gui.setClientStatus("Client " + socketcomm.getInetAddress().toString() + " connesso\t[" + this.clientConnessi + "]");
                    new TCPSocket_thread(socketcomm, this, gui).start();
                } catch (SocketTimeoutException timeout) {
                    if (this.clientConnessi == 0) {
                        break;
                    }
                }
            } while (avviato);
        } catch (IOException ex) {}
    }
    
    public void chiudiServer() {
        try {
            avviato = false;
            listaClient = new HashMap<>();
            clientConnessi = 0;
            gui.updateLista(listaClient);
            socket.close();
            gui.setServerStatus("SERVER OFFLINE");
        } catch (IOException ex) {}
    }
    
    public void send(InetAddress client, String text) {
        try {
            OutputStream theoutstream = listaClient.get(client).getOutputStream();
            DataOutputStream outstream = new DataOutputStream(theoutstream);
            outstream.writeBytes(text + "\n");
        } catch (NullPointerException ex) {
        } catch (IOException ex) {
        }
    }
}
