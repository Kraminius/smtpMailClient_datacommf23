package com.example.smtpmailclient_datacommf23;

import com.example.smtpmailclient_datacommf23.exceptions.SMTPException;

public interface  ReturnCodeHandler {

     boolean checkForSuccess(int rc) throws SMTPException;

}