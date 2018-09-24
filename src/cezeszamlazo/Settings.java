/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeszamlazo;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author Fejlesztés
 */
public class Settings {

    private static final String propertiesDir = "dat";
    private static final String propertiesFile = "settings.properties";
    
   public static String getId() {
       return Settings.get("id");
   }

    public static String get(String name) {
        try {
            check();
            Properties prop = new Properties();
            prop.load(new FileInputStream(getPath()));

            return prop.getProperty(name);
        } catch (IOException ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public static void set(String key, String value) {
        try {
            check();
            FileInputStream in = new FileInputStream(getPath());
            Properties props = new Properties();
            props.load(in);
            in.close();

            FileOutputStream out = new FileOutputStream(getPath());
            props.setProperty(key, value);
            props.store(out, null);
            out.close();
        } catch (ArrayIndexOutOfBoundsException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void check() {
        try {
            File f = new File(propertiesDir);
            if (!f.exists()) {
                System.out.println("MAKEDIR: " + propertiesDir);
                f.mkdir();
            }
            f = new File(getPath());
            if (!f.exists()) {
                System.out.println("CREATENEWFILE: " + getPath());
                f.createNewFile();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static String getPath() {
        return propertiesDir + "/" + propertiesFile;
    }

}
