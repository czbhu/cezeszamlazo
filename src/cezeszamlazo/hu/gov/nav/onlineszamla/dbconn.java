/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeszamlazo.hu.gov.nav.onlineszamla;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author szekus
 */
public class dbconn
{
    Connection conn;
    
    public Connection getConnection()
    {
        try
        {
            //conn = DriverManager.getConnection("jdbc:mysql://phpmyadmin2.ceze.eu/minta_tabla_szamlazo","szamla_demo","demo");                    
            conn = DriverManager.getConnection("jdbc:mysql://phpmyadmin2.ceze.eu/cezetesztdb","cezeteszt","pO8x3ozJQ4AR");                       
        }
        catch (SQLException ex)
        {
            System.out.print("Adatbázis kapcs. hiba!");
        }
        return conn;
    }
}
