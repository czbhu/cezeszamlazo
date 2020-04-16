package invoice;

import database.Query;
import java.util.Calendar;
import nav.NAV;

/**
 * @author Tomy
 */
public class InvoiceNumber
{
    public static final String TABLE = "szamlazo_szamla_sorszam";
    
    public static Object [][] getYear(int supplierId)
    {
        Query query = new Query.QueryBuilder()
            .select("id, year")
            .from(TABLE)
            .where("id = (SELECT serializationID from " + Supplier.TABLE + " WHERE id = " + supplierId + ")")
            .build();
        Object [][] year = NAV.db.select(query.getQuery());
        
        return year;
    }

    public static void UpdateYear(String id, int table)
    {
        Calendar c = Calendar.getInstance();
        
        String quantity = "";
        
        switch(table)
        {
            case Invoice.INVOICE:
                quantity = "invoiceQuantity";
                break;
            case Invoice.PROFORMA:
                quantity = "proFormaQuantity";
                break;
            case Invoice.SKETCH:
                quantity = "sketchQuantity";
                break;
        }
        
        String query = "UPDATE " + TABLE + " SET "
            + "year = ?, "
            +  quantity + " = 0 "
            + "WHERE id = '" + id + "'";
        
        Object [] o = new Object [1];
        o[0] = String.valueOf(c.get(Calendar.YEAR));
        
        NAV.db.insert(query, o, 1);
    }

    public static Object[][] getNumber(int type, int supplierID)
    {
        String selectText = "";
        
        switch(type)
        {
            case(Invoice.INVOICE):
                selectText = "invoiceQuantity,";
                break;
            case(Invoice.PROFORMA):
                selectText = "proFormaQuantity,";
                break;
            case(Invoice.SKETCH):
                selectText = "sketchQuantity,";
                break;
            /*case(Invoice.COMPLETION_CERTIFICATE):
                selectText = "invoiceQuantity, ";*/
        }
        
        Query query = new Query.QueryBuilder()
            .select(selectText + " year, prefix")
            .from(TABLE)
            .where("id = (SELECT serializationID from " + Supplier.TABLE + " WHERE id = " + supplierID + ")")
            .build();
        Object [][] number = NAV.db.select(query.getQuery());
        
        return number;
    }
}