package invoice;

import database.Query;
import nav.NAV;

/**
 * @author adam.papp, Tomy
 */
public class Afa
{
    public final static String TABLE = "szamlazo_vats";
    
    /**
     * ÁFA értéke.
     */
    private double afa;
    /**
     * Adott ÁFÁ-hoz tartozó nettó.
     */
    private double netto;

    public Afa(double afa, double netto)
    {
	this.afa = afa;
	this.netto = netto;
    }
    
    public Object[][] getVats()
    {
        Query query = new Query.QueryBuilder()
            .select("name, vatAmount")
            .from(TABLE)
            .order("vatAmount DESC")
            .build();
        Object [][] vats = NAV.db.select(query.getQuery());
        
        return vats;
    }
    
    public String[] getVatTooltips()
    {
        Query query = new Query.QueryBuilder()
            .select("name")
            .from(TABLE)
            .build();
        Object [][] vats = NAV.db.select(query.getQuery());
        
        String [] tooltip = new String[vats.length];
        
        for(int i = 0; i < vats.length; i++)
        {
            tooltip[i] = vats[i][0].toString();
        }
        
        return tooltip;
    }

    public double getAfa() {
	return afa;
    }

    public void setAfa(double afa) {
	this.afa = afa;
    }

    public double getAfaErtek(boolean deviza) {
	return (deviza ? Math.round(netto * afa) / 100.0 : Math.round(netto * afa / 100.0));
    }

    public double getNetto(boolean deviza) {
	return (deviza ? netto : Math.round(netto));
    }

    public void setNetto(double netto) {
	this.netto = netto;
    }
    
    public void addNetto(double netto) {
	this.netto += netto;
    }
    
    public double getBrutto(boolean deviza) {
	return getNetto(deviza) + getAfaErtek(deviza);
    }

    @Override
    public String toString() {
	return this.afa + "%\t" + netto + "\t" + getAfaErtek(true) + "\t" + getBrutto(true) + "\n";
    }    
}