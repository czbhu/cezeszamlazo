package cezeszamlazo.controller;

import cezeszamlazo.App;
import cezeszamlazo.database.Query;

/**
 * @author Tomy
 */
public class Takeover
{
    public final static String TABLE = "szamlazo_takeover_types";
    
    public static Object[][] getTakeoverTypes()
    {
        Query query = new Query.QueryBuilder()
            .select("value, text")
            .from(TABLE)
            .build();
        Object [][] takeoverTypes = App.db.select(query.getQuery());
        
        return takeoverTypes;
    }

    public static String[] getTakeoverTooltips()
    {
        Query query = new Query.QueryBuilder()
            .select("text")
            .from(TABLE)
            .build();
        Object [][] takeoverTypes = App.db.select(query.getQuery());
        
        String [] tooltip = new String[takeoverTypes.length];
        
        for(int i = 0; i < takeoverTypes.length; i++)
        {
            tooltip[i] = takeoverTypes[i][0].toString();
        }
        
        return tooltip;
    }
}