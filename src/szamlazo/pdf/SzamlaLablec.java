/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package szamlazo.pdf;

import cezeszamlazo.App;
import cezeszamlazo.database.Query;

/**
 *
 * @author szekus
 */
public class SzamlaLablec
{
    private static String tableName = "szamlazo_szamla_lablec";
    private static String magicWord = "$CEGNEV";

    public static String getLablecWithCegnev(String cegNev)
    {
        String result = "";
        
        Query query = new Query.QueryBuilder()
            .select("lablec_hun")
            .from(SzamlaLablec.tableName)
            .where("id = 1")
            .order("")
            .build();
        Object[][] select = App.db.select(query.getQuery());
        
        result = String.valueOf(select[0][0]);
        result = result.replace(SzamlaLablec.magicWord, cegNev);
        result = result.replace("\n", "").replace("\r", "");//remove new lines
        if (result.equals("0")) {
            result = "";
        }
        return result;
    }

    public static String getLablec()
    {
        String result = "";
        Query query = new Query.QueryBuilder()
            .select("lablec_hun")
            .from(SzamlaLablec.tableName)
            .where("id = 1")
            .build();
        Object[][] select = App.db.select(query.getQuery());
        result = String.valueOf(select[0][0]);
        if (result.equals("0")) {
            result = "";
        }
        System.out.println("result: " + result);
        return result;
    }

    public static void save(String text)
    {
        Object[] o = new Object[1];
        o[0] = text;
        App.db.insert("UPDATE " + SzamlaLablec.tableName + " SET lablec_hun = ? WHERE id = 1", o);
    }

}
