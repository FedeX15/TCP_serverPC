package com.fedex.tcpserver;

import java.io.*;
import java.net.*;
import static java.net.InetAddress.getLocalHost;
import java.util.*;
import javax.sound.midi.SysexMessage;

/**
 * @author Federico Matteoni
 */
public class TCPSocket_server {
    GUI gui;
    public int clientConnessi;
    public HashMap<InetAddress, Socket> listaClient;
    public boolean avviato;
    private ServerSocket socket;
    private DatagramSocket discoverySocket;
    static public long startTime;
    
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
        TCPSocket_server.startTime = System.nanoTime();
        gui.setOutputStatus("[" + (System.nanoTime() - TCPSocket_server.startTime)/1000000 + " Server] Launched");
        try {
            socket = new ServerSocket(8888);
            discoverySocket = new DatagramSocket(8889);
            discoverySocket.setBroadcast(true);
            new Discovery(discoverySocket, this, gui).start();
            gui.setServerStatus("SERVER ONLINE [" + InetAddress.getLocalHost().toString().split("/")[1] + "]");
            Socket socketcomm = null;
            do {
                try {
                    socketcomm = socket.accept();
                    this.listaClient.put(socketcomm.getInetAddress(), socketcomm);
                    gui.updateLista(listaClient);
                    this.clientConnessi++;
                    gui.setClientStatus((System.nanoTime() - TCPSocket_server.startTime)/1000000 + " Client " + socketcomm.getInetAddress().toString().split("/")[1] + " connected (" + this.clientConnessi + " connected)");
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
            discoverySocket.close();
            gui.setServerStatus("SERVER OFFLINE");
            gui.setOutputStatus("[Server] Stopped");
        } catch (IOException ex) {}
    }
    
    public void send(InetAddress client, String text) {
        try {
            OutputStream theoutstream = listaClient.get(client).getOutputStream();
            DataOutputStream outstream = new DataOutputStream(theoutstream);
            outstream.writeBytes(text + "\n");
        } catch (NullPointerException ex) {
        } catch (IOException ex) {}
    }
}
