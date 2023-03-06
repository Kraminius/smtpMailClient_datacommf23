package com.example.smtpmailclient_datacommf23;

import com.example.smtpmailclient_datacommf23.exceptions.*;

import java.net.*;
import java.io.*;

/**
 * Open an SMTP connection to a mailserver and send one mail.
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
        sendCommand("HELO", rc -> rc == 250);
        sendCommand("MAIL FROM: <\"" + envelope.Sender + "\">", rc -> {
            if (rc == 511) {
                throw new BadEmail(envelope.Sender, "The from email is bad.");
            }
            if (rc == 512) {
                throw new DomainNotFound(envelope.Sender, "Error in domain name.");
            }
            return rc == 250;
        });
        sendCommand("RCPT TO: <\"" + envelope.DestHost + "\">", rc -> {
            if (rc == 511) {
                throw new BadEmail(envelope.DestHost, "The To email is bad.");
            }
            if (rc == 512) {
                throw new DomainNotFound(envelope.DestHost, "Error in domain name.");
            }
            return rc == 250;
        });
        sendCommand("DATA", rc -> rc == 354);
        sendCommand(envelope.Message + "\n\r.", rc -> rc == 250);
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

    private int rcListen() throws IOException {
        String[] temp = fromServer.readLine().split(" ");
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
                throw new ServerUnavaliable();
            }
            case 422 -> {
                throw new RecipientMailboxFull();
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

    /* Destructor. Closes the connection if something bad happens.
    protected void finalize() throws Throwable {
        if(isConnected) {
            close();
        }
        super.finalize();
    }*/
}