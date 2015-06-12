package com.fedex.tcpserver;

import java.io.*;
import java.net.*;

/**
 * @author Federico Matteoni
 */
public class TCPSocket_thread extends Thread
{
    private Socket socketcomm;
    private TCPSocket_server server;
    private GUI gui;
    
    public TCPSocket_thread (Socket socketcomm, TCPSocket_server server, GUI gui) {
        super(socketcomm.getInetAddress().toString());
        this.socketcomm = socketcomm;
        this.server = server;
        this.gui = gui;
    }    
    @Override
    public void run() {
        String input;
        do {
            try {
                InputStream instream = socketcomm.getInputStream();

                BufferedReader readerbuff = new BufferedReader(new InputStreamReader(instream));
                input = readerbuff.readLine();
                if (input == null) {
                    socketcomm.close();
                    server.clientConnessi--;
                    server.listaClient.remove(socketcomm.getInetAddress());
                    gui.updateLista(server.listaClient);
                    gui.setClientStatus("Client " + this.getName().split("/")[1] + " disconnesso (Connessi: " + server.clientConnessi + ")");
                } else if (input.equals("GetServerInfo")) {
                    gui.setOutputStatus("[" + this.getName().split("/")[1] + "] " + "<Request: ServerInfo>");
                    server.send(InetAddress.getByName(this.getName().split("/")[1]), "Server " + (server.avviato ? "online" : "offline") + "|" + server.clientConnessi + " client");
                } else if (input.equals("StreamCamera")) {
                    DatagramSocket streamsocket = new DatagramSocket(8890);
                    byte[] recvBuf = ("ready").getBytes();
                    DatagramPacket recvPacket = new DatagramPacket(recvBuf, recvBuf.length, InetAddress.getByName(this.getName().split("/")[1]), 8890);
                    StreamGUI streamgui = new StreamGUI();
                    streamgui.setVisible(true);
                    String txt;
                    do {
                        streamsocket.receive(recvPacket);
                        txt = new String(recvBuf, 0, recvPacket.getLength());
                        streamgui.setText(txt);
                    } while (!txt.equals("Close"));
                    streamgui.setVisible(false);
                    streamsocket.close();
                } else {
                    gui.setOutputStatus("[" + this.getName().split("/")[1] + "] " + input);
                }
            } catch (IOException ex) {
            }
        } while (!socketcomm.isClosed() && server.avviato);
    }
}
