package com.fedex.school.tcpserver;

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
                    gui.setClientStatus("Client " + this.getName() + " disconnesso\t[" + server.clientConnessi + "]");
                } else {
                    gui.setOutputStatus(this.getName() + "_>" + input);
                }
                sleep(1000);
            } catch (IOException ex) {
            } catch (InterruptedException ex) {
            }
        } while (!socketcomm.isClosed());
    }
}
