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

    public ClientConnctionHandler()
    {}

    /**
     * Létrehozza a kapcsolatot.
     *
     * @param dbName az adtbázis neves
     * @return a létrehozott kapocsalat
     * @throws SQLException ha adatbázis hiba történik SQLException hibát dob
     * @throws java.lang.ClassNotFoundException
     */
    public Connection getConnection(String dbName) throws SQLException, ClassNotFoundException
    {
        Class.forName("com.mysql.jdbc.Driver");

        String url = "jdbc:mysql://"+MYSQLHOST+"/"+this.DBPREFIX+ dbName + "?&useUnicode=true&characterEncoding=utf8&characterSetResults=utf8";

        conn = DriverManager.getConnection(url, USERNAME, PASSWORD);
        
        System.out.println(f.format(Calendar.getInstance().getTime()) + " - URL: " + url + " (ClientConnectionHandler.java/getConnection)");
        System.out.println(f.format(Calendar.getInstance().getTime()) + " - Connection: " + conn + " (ClientConnectionHandler.java/getConnection)");
        
        return conn;
    }
}