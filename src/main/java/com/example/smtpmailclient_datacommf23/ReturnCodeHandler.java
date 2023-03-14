package com.example.smtpmailclient_datacommf23;

import com.example.smtpmailclient_datacommf23.Exceptions.SMTPException;

public interface ReturnCodeHandler {

    boolean checkForSuccess(int rc) throws SMTPException;
}
