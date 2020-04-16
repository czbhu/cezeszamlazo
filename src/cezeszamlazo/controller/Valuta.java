package cezeszamlazo.controller;

import cezeszamlazo.App;
import cezeszamlazo.database.Query;

public class Valuta
{
    private final static String TABLE = "szamlazo_valuta";
    
    private String valuta;
    private String valutaNev;
    private Double kozeparfolyam;

    public Valuta(String valuta, String valutaNev, Double kozeparfolyam)
    {
        this.valuta = valuta;
	this.valutaNev = valutaNev;
        this.kozeparfolyam = kozeparfolyam;
    }
    
    public Valuta(Object o)
    {
	if (o instanceof Valuta)
        {
	    Valuta v = (Valuta) o;
	    this.valuta = v.getValuta();
	    this.valutaNev = v.getValutaNev();
	    this.kozeparfolyam = v.getKozeparfolyam();
	}
    }

    public static Object[][] getCurrencies()
    {
        Query query = new Query.QueryBuilder()
            .select("currency, centralParity")
            .from(TABLE)
            .build();
        Object [][] currencies = App.db.select(query.getQuery());
        
        return currencies;
    }
    
    public String getValutaNev() {
	return valutaNev;
    }

    public Double getKozeparfolyam() {
        return kozeparfolyam;
    }

    public String getValuta() {
        return valuta;
    }
    
    @Override
    public String toString() {
        return valutaNev + " (" + valuta + ")";
    }
}