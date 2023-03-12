package com.example.smtpmailclient_datacommf23;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;


public class Import {

    private static Import imp;
    private Desktop desktop;
    final JFileChooser fileChooser = new JFileChooser();
    File importedFile;

    public static Import get(){
        if(imp == null) imp = new Import();
        return imp;
    }
    public void clear(){
        importedFile = null;
    }
    public File getFile(){
        return importedFile;
    }
    public String importPressed(){
        int response = fileChooser.showOpenDialog(null); //Will select file to open
        System.out.println("Response: " + response);

        if(response == JFileChooser.APPROVE_OPTION){ //If it returns a file, and doesn't click cancel or something else.
            File file = new File(fileChooser.getSelectedFile().getAbsolutePath()); //Gets the name of the file
            importedFile = fileChooser.getSelectedFile().getAbsoluteFile();
            return file.getAbsolutePath();
        }
        else{
            return "";
        }
    }
    private void openFile(File file) {
        try {
            desktop.open(file);
        } catch (IOException ex) {

        }
    }

}
