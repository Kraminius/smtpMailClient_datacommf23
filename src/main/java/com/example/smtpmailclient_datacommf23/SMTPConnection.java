package com.example.smtpmailclient_datacommf23;

import com.example.smtpmailclient_datacommf23.Exceptions.*;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.net.*;
import java.io.*;
import java.util.*;

/**
 * Open an SMTP connection to a mailserver and send one mail.
 */
public class SMTPConnection {
    /* The socket to the server */
    private Socket connection;

    /* Streams for reading and writing the socket */

    private BufferedReader fromServer;
    private PrintWriter toServer;

    private static final int SMTP_PORT = 587;
    private static final String CRLF = "\r\n";

    /* Are we connected? Used in close() to determine what to do. */
    private boolean isConnected = false;

    /* Create an SMTPConnection object. Create the socket and the
       associated streams. Initialize SMTP connection. */
    public SMTPConnection(Envelope envelope) throws IOException, SMTPException {

        System.out.println(envelope.DestAddr.getHostName());
        connection = new Socket(envelope.DestAddr.getHostName(), SMTP_PORT);
        fromServer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        toServer = new PrintWriter(connection.getOutputStream(), true);

         /* Read a line from server and check that the reply code is 220.
	    If not, throw an IOException. */
        handleReturnCode(rc -> rc == 220);

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
    public void send(Envelope envelope) throws IOException, SMTPException {
        /* Send all the necessary commands to send a message. Call
	   sendCommand() to do the dirty work. Do _not_ catch the
	   exception thrown from sendCommand(). */
        sendCommand("EHLO myHostname", rc -> rc == 250);
        sendCommand("STARTTLS", rc -> rc == 220);
        createTLSSocket(envelope);
        sendCommand("AUTH LOGIN", rc -> rc == 334);
        sendCommand("ZGF0YWNvbW1nMDk=", rc -> rc == 334);
        sendCommand("eXR4bHNlcWZkeWxqcmhwaA==", rc -> rc == 235);
        sendCommand("MAIL FROM: <\"" + envelope.Sender + "\">", rc -> {
            if (rc == 511) {
                throw new BadEmail(envelope.Sender, "The from email is bad.");
            }
            if (rc == 512) {
                throw new DomainNotFound(envelope.Sender, "Error in domain name.");
            }
            return rc == 250;
        });
        for (int i = 0; i < envelope.Recipient.length; i++) {
            final String recipient = envelope.Recipient[i];
            sendCommand("RCPT TO: <\"" + recipient + "\">", rc -> {
                if (rc == 511) {
                    throw new BadEmail(recipient, "The To email is bad.");
                }
                if (rc == 512) {
                    throw new DomainNotFound(recipient, "Error in domain name.");
                }
                return rc == 250;
            });
            System.out.println("Sending mail to: " + recipient);
            }
        sendCommand("DATA", rc -> rc == 354);

        if (envelope.file == null) sendCommand(envelope.Message + "\r\n.", rc -> rc == 250);
        else sendCommand(DataString.getAttachString(envelope.file, envelope.Message) + "\r\n.", rc -> rc == 250);
    }

    public void createTLSSocket(Envelope envelope) throws IOException {
        SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        SSLSocket sslsocket = (SSLSocket) sslSocketFactory.createSocket(connection, envelope.DestAddr.getHostName(), SMTP_PORT, true);

        fromServer = new BufferedReader(new InputStreamReader(sslsocket.getInputStream()));
        toServer = new PrintWriter(sslsocket.getOutputStream(), true);


        /*BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                sslsocket.getOutputStream()));
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                sslsocket.getInputStream()));*/

    }

    /* Close the connection. First, terminate on SMTP level, then
       close the socket. */
    public void close() {
        isConnected = false;
        try {
            sendCommand("quit", rc -> rc == 221);
            connection.close();
        } catch (IOException | SMTPException e) {
            System.out.println("Unable to close connection: " + e);
            isConnected = true;
        }
    }

    public void sendCommandTLS() {

    }

    /* Send an SMTP command to the server. Check that the reply code is
       what it is supposed to be according to RFC 821. */
    private void sendCommand(String command, ReturnCodeHandler rch) throws IOException, SMTPException {
        /* Write command to server and read reply from server. */
        toServer.println(command);
        /* Check that the server's reply code is the same as the parameter
	   rc. If not, throw an IOException. */
        handleReturnCode(rch);

        System.out.println("everything works bby");
    }

    /* Write command to server and read reply from server. */
    /* Fill in */

    /* Fill in */
	/* Check that the server's reply code is the same as the parameter
	   rc. If not, throw an IOException. */
    /* Fill in */


    private int rcListen() throws IOException {
        String[] temp = fromServer.readLine().split("[ -]");
        for (int i = 0; i < temp.length; i++) {
            System.out.print(temp[i]);
            System.out.print(" ");
        }
        while (fromServer.ready()) {
            temp = fromServer.readLine().split("[ -]");
            for (int i = 0; i < temp.length; i++) {
                System.out.print(temp[i]);
                System.out.print(" ");
            }
        }

        String rc = temp[0];

        System.out.println();
        return Integer.parseInt(rc);
    }

    private int rcListenLine() throws IOException {
        String[] temp = fromServer.readLine().split("-");
        String rc = temp[0];
        return Integer.parseInt(rc);
    }

    private void handleReturnCode(ReturnCodeHandler rch) throws IOException, SMTPException {
        int rc = rcListen();
        if (rch.checkForSuccess(rc)) return;

        switch (rc) {
            case 420 -> {
                throw new TimeOut();
            }
            case 421 -> {
                throw new ServerUnavaliable("Server is unavailable, try again later");
            }
            case 422 -> {
                throw new RecipientMailBoxFull("Recipient mail box is full. Contact recipient to resolve issue.");
            }
            case 101 -> {
                throw new IOException("Unable to connect to server");
            }
        }
        throw new UnexpectedReturnCode(rc);
    }

    /* Parse the reply line from the server. Returns the reply code. */
    private int parseReply(String reply) {
        return Integer.parseInt(reply);
    }
}

    /* Destructor. Closes the connection if something bad happens.
    protected void finalize() throws Throwable {
        if(isConnected) {
            close();
        }
        super.finalize();
    }*/
