package com.example.smtpmailclient_datacommf23;

import java.io.*;
import java.net.*;
import java.util.*;

/* $Id: Envelope.java,v 1.8 1999/09/06 16:43:20 kangasha Exp $ */

/**
 * SMTP envelope for one mail message.
 *
 * @author Jussi Kangasharju
 */
public class Envelope {
    /* SMTP-sender of the message (in this case, contents of From-header. */
    public String Sender;

    /* SMTP-recipient, or contents of To-header. */
    public String[] Recipient;

    /* Target MX-host */
    public String DestHost;
    public InetAddress DestAddr;

    /* The actual message */
    public Message Message;
	public String attachment;
	public boolean hasFile = false;
    /* Create the envelope. */
    public Envelope(Message message, String localServer, File file) throws UnknownHostException {
		/* Get sender and recipient. */
		Sender = message.getFrom();
		Recipient = message.getRecipientses();

	/* Get message. We must escape the message to make sure that
	   there are no single periods on a line. This would mess up
	   sending the mail. */
		Message = escapeMessage(message);

		/* Take the name of the local mailserver and map it into an
		 * InetAddress */
		DestHost = localServer;
		try {
			DestAddr = InetAddress.getByName(DestHost);
		} catch (UnknownHostException e) {
			System.out.println("Unknown host: " + DestHost);
			System.out.println(e);
			throw e;
		}

		if(file != null){
			hasFile = true;

			String type = FileConverter.getType(file);
			String base64 = FileConverter.getBase64(file);
			String name = file.getName();
			attachment = "--boundary  '\n'" +
					"Content-Type: " +  type + "; name=" + name + "\n" +
					"Content-Disposition: inline; filename=" + name + "\n" +
					"Content-Transfer-Encoding: base64\n" +
					"Content-ID: <0123456789>\n" +
					"Content-Location: " + name + "\n" +
					base64 + "\n" +
					"--boundary";

			/*
			--boundary
				Content-Type: image/png; name="sig.png"
				Content-Disposition: inline; filename="sig.png"
				Content-Transfer-Encoding: base64
				Content-ID: <0123456789>
				Content-Location: sig.png

				base64 data

			--boundary
			 */
		}


		return;
    }

    /* Escape the message by doubling all periods at the beginning of
       a line. */
    private Message escapeMessage(Message message) {
	String escapedBody = "";
	String token;
	StringTokenizer parser = new StringTokenizer(message.Body, "\n", true);

	while(parser.hasMoreTokens()) {
	    token = parser.nextToken();
	    if(token.startsWith(".")) {
		token = "." + token;
	    }
	    escapedBody += token;
	}
	message.Body = escapedBody;
	return message;
    }

    /* For printing the envelope. Only for debug. */
    public String toString() {
	String res = "Sender: " + Sender + '\n';
		for(int i = 0; i < Recipient.length; i++) {
			res += "Recipient: " + Recipient[i] + '\n';
		}
	res += "MX-host: " + DestHost + ", address: " + DestAddr + '\n';
	res += "Message:" + '\n';
	res += Message.toString();
	
	return res;
    }
}