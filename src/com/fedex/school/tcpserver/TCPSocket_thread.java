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

                gui.setOutputStatus(this.getName() + "_>" + input);

                /*OutputStream theoutstream = socketcomm.getOutputStream();
                DataOutputStream outstream = new DataOutputStream(theoutstream);
                try {
                    outstream.writeBytes(input.toUpperCase() + "\n");
                } catch (NullPointerException ex) {
                    socketcomm.close();
                    server.clientConnessi--;
                    gui.setClientStatus("Client " + this.getName() + " disconnesso\t[" + server.clientConnessi + "]");
                    break;
                }*/

                if (input.equals("fine")) {
                    socketcomm.close();
                    server.clientConnessi--;
                    gui.setClientStatus("Client " + this.getName() + " disconnesso\t[" + server.clientConnessi + "]");
                    break;
                }
            } catch (IOException ex) {}
        } while (true);
    }
}
