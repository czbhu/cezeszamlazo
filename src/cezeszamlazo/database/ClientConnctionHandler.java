package cezeszamlazo.database;

import cezeszamlazo.interfaces.SharedValues;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class ClientConnctionHandler implements SharedValues{

    private static SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    protected Connection conn;
    
    protected final String DBPREFIX = "hid_";

    /**
     * Alapértelmezett konstruktor.
     */
    public ClientConnctionHandler() {
    }

    /**
     * Létrehozza a kapcsolatot.
     *
     * @return a létrehozott kapocsalat
     * @throws SQLException ha adatbázis hiba történik SQLException hibát dob
     */
    public Connection getConnection(String dbName) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");

        String url = "";

        url = "jdbc:mysql://"+MYSQLHOST+"/"+this.DBPREFIX+ dbName + "?&useUnicode=true&characterEncoding=utf8&characterSetResults=utf8";
        

        conn = DriverManager.getConnection(
                url, USERNAME, PASSWORD);
        System.out.println(f.format(Calendar.getInstance().getTime()) + " - URL: " + url);
        System.out.println(f.format(Calendar.getInstance().getTime()) + " - Connection: " + conn);
        return conn;
    }
}