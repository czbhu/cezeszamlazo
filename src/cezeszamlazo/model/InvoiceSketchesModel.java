package cezeszamlazo.model;

import cezeszamlazo.App;
import cezeszamlazo.database.Query;

/**
 * @author Tomy
 */

public class InvoiceSketchesModel
{
    private static final String sketchesTable = "szamlazo_sketches";
    private static final String productsTable = "szamlazo_sketches_products";
    
    public Object [][] getSketches()
    {
        Query query = new Query.QueryBuilder()
            .select("*")
            .from(sketchesTable)
            .build();
        Object [][] sketches = App.db.select(query.getQuery());
        
        for(int i = sketches.length; i < 0; i--)
        {
            if(sketches[i][2].toString().equals("0") && sketches[i][3].equals(""))
            {
                SketchUpdate(sketches[i][1].toString());
            }
        }
        
        return sketches;
    }
    
    private void SketchUpdate(String indentifier)
    {
        Query query = new Query.QueryBuilder()
            .select("serializationID, currency")
            .from("szamlazo_suppliers")
            .where("id = (SELECT defaultCompanyID FROM szamlazo_users WHERE id = " + App.user.getId() + ")")
            .build();
        Object [][] serializationIDs = App.db.select(query.getQuery());

        int serializationID = Integer.parseInt(serializationIDs[0][0].toString());
        String currency = serializationIDs[0][1].toString();
        
        query = new Query.QueryBuilder()
            .select("centralParity")
            .from("szamlazo_valuta")
            .where("currency = " + currency)
            .build();
        Object [][] centralP = App.db.select(query.getQuery());
        
        double centralParity = Double.parseDouble(centralP[0][0].toString());

        query = new Query.QueryBuilder()
            .select("sketchQuantity, year, prefix")
            .from("szamlazo_szamla_sorszam")
            .where("id = " + serializationID + "")
            .build();
        Object [][] s = App.db.select(query.getQuery());

        String invoiceNumber = String.valueOf(s[0][1]) + "/" + (Integer.parseInt(String.valueOf(s[0][0])) + 1);

        if (!String.valueOf(s[0][2]).isEmpty())
        {
            invoiceNumber = String.valueOf(s[0][2]) + " " + invoiceNumber;
        }

        String update = "UPDATE " + sketchesTable + " SET invoiceNumber = " + invoiceNumber + ", serializationID = " + serializationID + ", centralParity = " + centralParity + " WHERE indentifier = " + indentifier;
        App.db.insert(update, null);

        update = "UPDATE " + productsTable + " SET invoiceNumber = " + invoiceNumber + ", serializationID = " + serializationID + ", centralParity = " + centralParity + " WHERE indentifier = " + indentifier;
        App.db.insert(update, null);
        
        update = "UPDATE szamlazo_szamla_sorszam SET sketchQuantity = sketchQuantity + 1 WHERE id = " + serializationID;
        App.db.insert(update, null);
    }

    public Object[][] getProducts(String indentifier)
    {
        Query query = new Query.QueryBuilder()
            .select("*")
            .from(productsTable)
            .where("indentifier = " + indentifier)
            .build();
        Object [][] products = App.db.select(query.getQuery());
        
        return products;
    }
}