package com.example.smtpmailclient_datacommf23.Exceptions;

public class SMTPException extends Exception {

    public SMTPException(){
    }

    public SMTPException(String message){
        super(message);
    }

    public SMTPException(String message, Throwable cause){
        super(message, cause);
    }

    public SMTPException(Throwable cause){
        super(cause);
    }

}
