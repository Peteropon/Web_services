package client;

import java.io.*;

import static client.HTTPServer.WEB_ROOT;

public class Feature {

    static final String REVERSE ="Reverse.html";
    private File htmlFile;

    public void createHTML(String param) {

        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n<html><head><title>Reverse Writing </title></head>\n")
                .append("<body>").append(" <bdo dir=\"rtl\">").append(param).append("</bdo> \n")
                .append("</body></html>");
        System.out.println("Creating html file");
        WriteToFile(html.toString());
    }

    private void WriteToFile(String content) {
        File file = new File(WEB_ROOT, REVERSE);
        if (file.exists()){
            File newFile = new File(WEB_ROOT, REVERSE + "backup");
            file.renameTo(newFile);
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            OutputStream out = new FileOutputStream(file.getAbsoluteFile());
            Writer writer = new OutputStreamWriter(out);
            writer.write(content);
            writer.close();
            System.out.println("html file created successfully");
        }catch (IOException e){
            e.getMessage();
        }

        htmlFile = file;
    }

    public File getHtmlFile() {
        return htmlFile;
    }
}
