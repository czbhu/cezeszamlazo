package cezeszamlazo.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author szekus
 */
public class CreateDB
{
    public static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    public static final String JDBC_URL = "jdbc:derby:testdb;create=true";

    public static void create() throws ClassNotFoundException, SQLException
    {
        Class.forName(DRIVER);
        Connection connection = DriverManager.getConnection(JDBC_URL);
//        connection.createStatement().execute("create table test(id INTEGER, value varchar(20))");

        Statement sta = connection.createStatement();
        sta.execute("INSERT INTO test values(1,'1')");
        ResultSet res = sta.executeQuery("SELECT * FROM test");
        System.out.println("List of test: ");
        
        while (res.next())
        {
            System.out.println("  " + res.getInt("id") + ", " + res.getString("value"));
        }
        
        res.close();

        sta.close();
//        connection.createStatement().execute("insert into test values(1,'1')");
    }

//    public static Database readDatabaseFromXML(String fileName) throws DdlUtilsException {
//        return new DatabaseIO().read(fileName);
//    }
}