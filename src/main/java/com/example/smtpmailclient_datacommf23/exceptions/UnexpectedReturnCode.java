package com.example.smtpmailclient_datacommf23.exceptions;

public class UnexpectedReturnCode extends SMTPException {

    public UnexpectedReturnCode(int errorCode) {
        super("UnexpectedReturnCode errorCode = " + errorCode);

    }

}
