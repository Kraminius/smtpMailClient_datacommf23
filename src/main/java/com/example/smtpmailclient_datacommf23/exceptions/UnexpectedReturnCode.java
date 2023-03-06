package com.example.smtpmailclient_datacommf23.exceptions;

public class UnexpectedReturnCode extends SMTPException {

    private final int errorcode;
    public UnexpectedReturnCode(int errorCode) {
        super( "UnexpectedReturnCode{" +
                "errorCode=" + errorCode +
                '}');
        this.errorcode = errorCode;

    }

}
