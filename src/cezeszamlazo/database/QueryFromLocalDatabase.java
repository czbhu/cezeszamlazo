package cezeszamlazo.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author szekus
 */
public class QueryFromLocalDatabase
{
    private QueryFromLocalDatabase()
    {}

    public static QueryFromLocalDatabase create() {
        return new QueryFromLocalDatabase();
    }

    public static void getValues()
    {
        try
        {
            Connection connection = LocalDB.create().getConnection();
            Statement sta = connection.createStatement(); 
            ResultSet res = sta.executeQuery(
                    "SELECT * FROM szamlazo_afa");
            
            while (res.next())
            {
                System.out.println(
                        "  " + res.getInt("id")
                        + ", " + res.getString("megnevezes")
                        + ", " + res.getString("afa"));
            }
            
            res.close();
            sta.close();
            connection.close();
        }
        catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(QueryFromLocalDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}