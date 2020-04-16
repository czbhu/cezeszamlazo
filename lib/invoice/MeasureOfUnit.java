package invoice;

import database.Query;
import nav.NAV;


/**
 * @author Tomy
 */
public class MeasureOfUnit
{
    public final static String TABLE = "szamlazo_measureOfUnit";
    
    String type;
    String name;
    String shortName;
    
    public MeasureOfUnit(String shortName)
    {
        this.shortName = shortName;
        getMeasureOfUnitByShortName();
    }
    
    public MeasureOfUnit(String name, String shortName)
    {
        this.name = name;
        this.shortName = shortName;
    }
    
    public void Save()
    {
        String query = "INSERT INTO " + TABLE + " ("
            + "type, "
            + "name, "
            + "shortName) "
            + "VALUES (?, ?, ?)";
        
        Object [] values = new Object[3];
        
        values[0] = "OWN";
        values[1] = name;
        values[2] = shortName;
        
        NAV.db.insert(query, values, values.length);
    }
    
    private void getMeasureOfUnitByShortName()
    {
        try
        {
            Query query = new Query.QueryBuilder()
                .select("type, name")
                .from(TABLE)
                .where("shortName = '" + shortName + "'")
                .build();
            Object [][] res = NAV.db.select(query.getQuery());

            type = res[0][0].toString();
            name = res[0][1].toString();
        }
        catch(ArrayIndexOutOfBoundsException ex)
        {
            String insert = "INSERT INTO " + TABLE + " (type, name, shortName)"
                    + "VALUES (?, ?, ?)";
            
            Object [] o = new Object[3];
            
            o[0] = "OWN";
            o[1] = shortName;
            o[2] = shortName;
            
            NAV.db.insert(insert, o, o.length);
            
            getMeasureOfUnitByShortName();
        }
    }
    
    public static Object[][] getMeasureOfUnits()
    {
        Query query = new Query.QueryBuilder()
            .select("shortName")
            .from(TABLE)
            .order("shortName")
            .build();
        Object [][] measureOfUnits = NAV.db.select(query.getQuery());
        
        return measureOfUnits;
    }
    
    public static String[] getTooltips()
    {
        Query query = new Query.QueryBuilder()
            .select("name")
            .from(TABLE)
            .order("shortName")
            .build();
        Object [][] vats = NAV.db.select(query.getQuery());
        
        String [] tooltip = new String[vats.length];
        
        for(int i = 0; i < vats.length; i++)
        {
            tooltip[i] = vats[i][0].toString();
        }
        
        return tooltip;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }
}