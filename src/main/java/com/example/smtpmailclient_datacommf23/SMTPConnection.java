package com.example.smtpmailclient_datacommf23;

import java.net.*;
import java.io.*;
import java.util.*;

/**
 * Open an SMTP connection to a mailserver and send one mail.
 *
 */
public class SMTPConnection {
    /* The socket to the server */
    private Socket connection;

    /* Streams for reading and writing the socket */

    private BufferedReader fromServer;
    private BufferedWriter toServer;

    private static final int SMTP_PORT = 2526;
    private static final String CRLF = "\r\n";

    /* Are we connected? Used in close() to determine what to do. */
    private boolean isConnected = false;

    /* Create an SMTPConnection object. Create the socket and the
       associated streams. Initialize SMTP connection. */
    public SMTPConnection(Envelope envelope) throws IOException {

        System.out.println(envelope.DestAddr.getHostName());
        connection = new Socket(envelope.DestAddr.getHostName(), SMTP_PORT);
        fromServer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        toServer =   new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));




        /* Fill in */
        String[] rc = fromServer.readLine().split(" ");
        String temp = rc[0];
        if(!temp.equals("220")){
         throw new IOException();
        }

        for(int i = 0; i < rc.length; i++){
            System.out.println(rc[i]);
        }

	/* Read a line from server and check that the reply code is 220.
	   If not, throw an IOException. */
        /* Fill in */

	/* SMTP handshake. We need the name of the local machine.
	   Send the appropriate SMTP handshake command. */
        String localhost = "127.0.0.1";


        toServer.flush();



        //System.out.println(fromServer.readLine());


        //sendCommand(servermsg, 220);

        isConnected = true;
    }

    /* Send the message. Write the correct SMTP-commands in the
       correct order. No checking for errors, just throw them to the
       caller. */
    public void send(Envelope envelope) throws IOException {
        /* Send all the necessary commands to send a message. Call
	   sendCommand() to do the dirty work. Do _not_ catch the
	   exception thrown from sendCommand(). */
        sendCommand("helo", 250);
        sendCommand("mail from: <\""+ envelope.Sender + "\">", 250);
        sendCommand("rcpt to: <\"" + envelope.DestHost + "\">", 354);
        sendCommand("data", 354);
        sendCommand(envelope.Message + "\n\r.", 250);
        close();
    }

    /* Close the connection. First, terminate on SMTP level, then
       close the socket. */
    public void close() {
        isConnected = false;
        try {
            sendCommand("quit", 221);
            connection.close();
        } catch (IOException e) {
            System.out.println("Unable to close connection: " + e);
            isConnected = true;
        }
    }

    /* Send an SMTP command to the server. Check that the reply code is
       what is is supposed to be according to RFC 821. */
    private void sendCommand(String command, int rc) throws IOException {
        /* Fill in */



        /* Write command to server and read reply from server. */
        /* Fill in */

        /* Fill in */
	/* Check that the server's reply code is the same as the parameter
	   rc. If not, throw an IOException. */
        /* Fill in */
    }

    /* Parse the reply line from the server. Returns the reply code. */
    private int parseReply(String reply) {
        return Integer.parseInt(reply);
    }

    /* Destructor. Closes the connection if something bad happens.
    protected void finalize() throws Throwable {
        if(isConnected) {
            close();
        }
        super.finalize();
    }*/
}