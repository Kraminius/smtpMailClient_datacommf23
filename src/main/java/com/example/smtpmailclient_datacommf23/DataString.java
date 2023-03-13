package com.example.smtpmailclient_datacommf23;

import java.io.File;

public class DataString {
    public static String getAttachString(File file, Message message){
        String base64 = FileConverter.getBase64(file);
        String type = FileConverter.getType(file);
        String name = file.getName();
        String data =
                        "MIME-Version: 1.0 \n" +
                        "Content-Type: multipart/mixed; boundary=\"123145\"\n" +
                        message.Headers + "\n\n" +

                        "--123145\n" +
                        "Content-Type: text/plain; charset=\"UTF-8\"\n" +
                        "Content-Transfer-Encoding: 7bit\n\n" +

                        message.Body + "\n \n" +

                        "--123145\n" +
                        "Content-Type: " + type + "\n" +
                        "Content-Transfer-Encoding: base64\n" +
                        "Content-Disposition: attachment; filename=" + name + "\n\n" +

                        base64 + "\n\n" +

                        "--123145--";

        return data;
    }
}
