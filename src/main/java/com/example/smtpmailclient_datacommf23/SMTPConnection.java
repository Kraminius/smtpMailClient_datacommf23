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
    private PrintWriter toServer;

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
        toServer = new PrintWriter(connection.getOutputStream(), true);




        /* Fill in */

        if(rcListen() != 220){
         throw new IOException();
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
        sendCommand("HELO", 250);
        sendCommand("MAIL FROM: <\""+ envelope.Sender + "\">", 250);
        for(int i = 0; i < DestHost.length; i ++) {
            sendCommand("RCPT TO: <\"" + envelope.DestHost[i] + "\">", 250);
        }
        sendCommand("DATA", 354);
        sendCommand(envelope.Message + "\n\r.", 250);
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
        toServer.println(command);
        if(rcListen() != rc){
            throw new IOException();
        }
        else {
            System.out.println("everything works bby");
        }

        /* Write command to server and read reply from server. */
        /* Fill in */

        /* Fill in */
	/* Check that the server's reply code is the same as the parameter
	   rc. If not, throw an IOException. */
        /* Fill in */
    }

    private int rcListen() throws IOException {
        String[] temp = fromServer.readLine().split(" ");
        String rc = temp[0];
        return Integer.parseInt(rc);
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