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
                } else if (input.equals("StartUDPStream")) {
                    DatagramSocket streamsocket = new DatagramSocket(8890);
                    byte[] recvBuf = new byte[1500];
                    DatagramPacket recvPacket = new DatagramPacket(recvBuf, recvBuf.length);
                    StreamGUI streamgui = new StreamGUI();
                    streamgui.setVisible(true);
                    streamgui.setBounds(this.gui.getX() + this.gui.getWidth()/2, this.gui.getY() + this.gui.getHeight()/2, streamgui.getWidth(), streamgui.getHeight());
                    String txt;
                    do {
                        streamsocket.receive(recvPacket);
                        txt = new String(recvBuf, 0, recvPacket.getLength());
                        streamgui.setText(txt);
                    } while (!txt.equals("Close"));
                    streamgui.setVisible(false);
                    streamsocket.close();
                } else if (input.startsWith("Play")) {
                    String str = input.split("&")[1];
                    int w = Integer.parseInt(str.split("-")[0]);
                    int h = Integer.parseInt(str.split("-")[1]);
                    DatagramSocket streamsocket = new DatagramSocket(8890);
                    byte[] recvBuf = new byte[1500];
                    DatagramPacket recvPacket = new DatagramPacket(recvBuf, recvBuf.length);
                    PlayGUI playgui = new PlayGUI(w/2, h/2);
                    playgui.setVisible(true);
                    playgui.setBounds(this.gui.getX() + this.gui.getWidth()/2, this.gui.getY() + this.gui.getHeight()/2, playgui.getWidth(), playgui.getHeight());
                    String txt;
                    int x;
                    do {
                        streamsocket.receive(recvPacket);
                        txt = new String(recvBuf, 0, recvPacket.getLength());
                        try {
                            x = Integer.parseInt(txt);
                            playgui.setOpponentPosition(x/2);
                        } catch (NumberFormatException ex) {
                        }
                    } while (!txt.equals("Close"));
                    playgui.setVisible(false);
                    streamsocket.close();
                } else {
                    gui.setOutputStatus("[" + this.getName().split("/")[1] + "] " + input);
                }
            } catch (IOException ex) {
            }
        } while (!socketcomm.isClosed() && server.avviato);
    }
}
