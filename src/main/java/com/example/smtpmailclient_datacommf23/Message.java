package com.example.smtpmailclient_datacommf23;

import java.util.*;
import java.text.*;

/* $Id: Message.java,v 1.5 1999/07/22 12:10:57 kangasha Exp $ */

/**
 * Mail message.
 *
 * @author Jussi Kangasharju
 */
public class Message {
    /* The headers and the body of the message. */
    public String Headers;
    public String Body;

    /* Sender and recipient. With these, we don't need to extract them
       from the headers. */
    private String From;
	private String[] recipientses;

    /* To make it look nicer */
    private static final String CRLF = "\r\n";

    /* Create the message object by inserting the required headers from
       RFC 822 (From, To, Date). */
    public Message(String from, String[] recipients, String subject, String text) {
	/* Remove whitespace */
	From = from.trim();
	recipientses = recipients;
	Headers = "From: " + From + CRLF;
	//Making more recepients
		for(int i = 0; i < recipientses.length; i++){
			Headers += "To: " + recipientses[i] + CRLF;}
	Headers += "Subject: " + subject.trim() + CRLF;

	/* A close approximation of the required format. Unfortunately
	   only GMT. */
	SimpleDateFormat format = 
	    new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'");
	String dateString = format.format(new Date());
	Headers += "Date: " + dateString + CRLF;
	Body = text;
    }

    /* Two functions to access the sender and recipient. */
    public String getFrom() {
	return From;
    }

	public String[] getRecipientses(){
		return recipientses;
	}

    /* Check whether the message is valid. In other words, check that
       both sender and recipient contain only one @-sign. */
    public boolean isValid() {
	int fromat = From.indexOf('@');

		//CHeck for valid for each recipient
		for(int i = 0; i < recipientses.length; i++){
			int toat = recipientses[i].indexOf('@');

			if(toat < 1 || (recipientses[i].length() - toat) <= 1) {
				System.out.println("Recipient address is invalid");
				return false;
			}

			if(toat != recipientses[i].lastIndexOf('@')) {
				System.out.println("Recipient address is invalid");
				return false;
			}
		}

	if(fromat < 1 || (From.length() - fromat) <= 1) {
	    System.out.println("Sender address is invalid");
	    return false;
	}

	if(fromat != From.lastIndexOf('@')) {
	    System.out.println("Sender address is invalid");
	    return false;
	}

	return true;
    }
    
    /* For printing the message. */
    public String toString() {
	String res;

	res = Headers + CRLF;
	res += Body;
	return res;
    }
}