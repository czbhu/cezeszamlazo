/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeszamlazo.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author szekus
 */
public class MyFileReader {

    private String fileUrl;

    private MyFileReader(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public static MyFileReader create(String fileUrl) {
        return new MyFileReader(fileUrl);
    }

    public String readByLine() throws FileNotFoundException, IOException {
        String result = "";
        try (BufferedReader br = new BufferedReader(new FileReader(fileUrl))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            result = sb.toString();
        }

        return result;
    }

    public void writeToFile(String writeFileUrl, String line) throws IOException {
        List<String> lines = Arrays.asList(line);

        File file = new File(writeFileUrl);
        if (!file.exists()) {
            file.createNewFile();
        }

        Path path = Paths.get(writeFileUrl);
        Files.write(path, lines, Charset.forName("UTF-8"), StandardOpenOption.WRITE);
    }

}
