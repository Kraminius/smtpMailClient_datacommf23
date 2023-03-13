package com.example.smtpmailclient_datacommf23;

import java.io.File;

public class DataString {
    public static String getAttachString(File file, Message message){
        String base64 = FileConverter.getBase64(file);
        String type = FileConverter.getType(file);
        String name = file.getName();
        String data =
                        "Content-Type: multipart/mixed; boundary=\"000000000000cf026105f65074cc\"\n" +
                        "\n" +
                        "--000000000000cf026105f65074cc\n" +
                        "Content-Type: multipart/alternative; boundary=\"000000000000cf025f05f65074ca\"\n" +
                        "\n" +
                        "--000000000000cf025f05f65074ca\n" +
                        "Content-Type: text/plain; charset=\"UTF-8\"\n" +
                        "--000000000000cf025f05f65074ca\n" +
                        "Content-Type: text/html; charset=\"UTF-8\"\n" +
                        message + "\n" +
                        "--000000000000cf025f05f65074ca--\n" +
                        "--000000000000cf026105f65074cc\n" +
                        "Content-Type:" + type + "; name=" + name + "\n" +
                        "Content-Disposition: attachment; filename=" + name + "\n" +
                        "Content-Transfer-Encoding: base64\n" +
                        base64 + "\n" +
                        "--000000000000cf026105f65074cc--";

        return data;
    }

}
