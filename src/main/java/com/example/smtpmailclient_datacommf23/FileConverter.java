package com.example.smtpmailclient_datacommf23;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

public class FileConverter {

    private static FileConverter fileConverter;
    public FileConverter get(){
        if(fileConverter == null) fileConverter = new FileConverter();
        return fileConverter;
    }

    public static String getBase64(File file) {
        try{
            byte[] fileContent = Files.readAllBytes(file.toPath());
            String encodedString = Base64.getEncoder().encodeToString(fileContent);
            return encodedString;
        }catch(Exception e){
            System.out.println("Could not use this path");
        }
        return "";
    }

    public static String getType(File file){
        try{
            String mimeType = Files.probeContentType(file.toPath());
            return mimeType;
        }catch(Exception e){
            System.out.println("Could not use this path");
        }

        return "";
    }
}
