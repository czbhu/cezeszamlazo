package cezeszamlazo.model;

import cezeszamlazo.App;
import cezeszamlazo.database.Query;

/**
 * @author Tomy
 */
public class LogModel
{
    public static final String TABLE = "szamlazo_logs";
    
    public static Object [][] getLogs()
    {
        String update = "UPDATE " + TABLE + " SET isNew = 0 WHERE isNew = 1";
        App.db.insert(update, null);
        
        Query query = new Query.QueryBuilder()
            .select("*")
            .from(TABLE)
            .order("id DESC")
            .build();
        Object [][] logs = App.db.select(query.getQuery());
        
        return logs;
    }
    
    public static void addLog(String className, String desc)
    {
        String classname = className + ".java";
        
        String query = "INSERT INTO " + TABLE
            + "(filename, description) "
            + "VALUES (?, ?)";
        
        Object [] values = new Object[2];
        values[0] = classname;
        values[1] = desc;
        
        App.db.insert(query, values);
    }
    
    public static boolean isNewLog()
    {
        Query query = new Query.QueryBuilder()
            .select("isNew")
            .from(TABLE)
            .where("isNew = 1")
            .build();
        Object [][] newLog = App.db.select(query.getQuery());
        
        return (newLog.length > 0);
    }
}