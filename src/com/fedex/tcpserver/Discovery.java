package com.fedex.tcpserver;

import java.io.*;
import java.net.*;

/**
 * @author Federico Matteoni
 */
public class Discovery extends Thread {
    private DatagramSocket socket;
    private TCPSocket_server server;
    private GUI gui;
    
    public Discovery(DatagramSocket socketcomm, TCPSocket_server server, GUI gui) {
        this.socket = socketcomm;
        this.server = server;
        this.gui = gui;
    }
    
    @Override
    public void run() {
        gui.setOutputStatus("[Discovery] Avviato");
        do {
            try {
                byte[] recvBuf = new byte[15000];
                DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
                socket.receive(packet);
                if (new String(recvBuf, 0, packet.getLength()).equals("SocketTest_discovery")) {
                    gui.setOutputStatus("[Discovery] Ricevuto " + packet.getAddress().toString().split("/")[1]);
                    byte[] sendData = ("SocketTest&" + InetAddress.getLocalHost().toString()).getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, packet.getAddress(), 8889);
                    socket.send(sendPacket);
                }
            } catch (IOException ex) {
            }
        } while (!socket.isClosed());
        gui.setOutputStatus("[Discovery] Fermato");
    }
    //server.send(socketcomm.getInetAddress(), "SocketTest&" + InetAddress.getLocalHost().toString());
}
