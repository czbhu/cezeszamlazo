package cezeszamlazo;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class Updater {
    
    private int status = 0;
    
    public void update() {
        try {
            long startTime = System.currentTimeMillis();

            String updateUrl = "http://www.cezereklam.com/apps/cezeszamlazo";
            System.out.println("Csatlakozás: " + updateUrl);

            URL url = new URL(updateUrl);
            url.openConnection();
            InputStream reader = url.openStream();
            
            System.out.println(System.getProperty("user.dir") + "/CezeSzamlazo.jar");
            FileOutputStream writer = new FileOutputStream(System.getProperty("user.dir") + "/CezeSzamlazo.jar");
            byte[] buffer = new byte[153600];
            int totalBytesRead = 0;
            int bytesRead = 0;

            while ((bytesRead = reader.read(buffer)) > 0) {
                writer.write(buffer, 0, bytesRead);
                buffer = new byte[153600];
                totalBytesRead += bytesRead;
		status++;
            }

	    status = 100;
	    
            long endTime = System.currentTimeMillis();

            System.out.println("Kész. " + (new Integer(totalBytesRead).toString()) + " byte, (" + (new Long(endTime - startTime).toString()) + " ms).");
            writer.close();
            reader.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public int getStatus() {
	return status;
    }
}
