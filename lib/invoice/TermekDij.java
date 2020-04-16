package invoice;

import database.Query;
import functions.NumericFunctions;
import java.io.Serializable;
import nav.NAV;

public class TermekDij implements Serializable
{
    public static final String TABLE = "productFee_types";
    
    private String tipus;
    private String nev;
    private double szelesseg;
    private double magassag;
    private double peldany;
    private double egysegsuly;
    private double termekDij;
    private double osszsuly;
    private double szorzo = 1.0;
    private String csk;
    private String kt;
    
    public TermekDij(String tipus, String nev, double szelesseg, double magassag, double peldany, double suly, double termekDij, double osszsuly, String csk, String kt)
    {
        this.tipus = tipus;
        this.nev = nev;
        this.szelesseg = szelesseg;
        this.magassag = magassag;
        this.peldany = peldany;
        this.egysegsuly = suly;
        this.termekDij = termekDij;
        this.osszsuly = osszsuly;
        this.csk = csk;
        this.kt = kt;
    }
    
    public TermekDij(String tipus, String nev, String szelesseg, String magassag, String peldany, String suly, String termekDij, String osszsuly)
    {
        this.tipus = tipus;
        this.nev = nev;
        this.szelesseg = Double.parseDouble(szelesseg.replace(",", "."));
        this.magassag = Double.parseDouble(magassag.replace(",", "."));
        this.peldany = Double.parseDouble(peldany.replace(",", "."));
        this.egysegsuly = Double.parseDouble(suly.replace(",", "."));
        this.termekDij = Double.parseDouble(termekDij.replace(",", "."));
        this.osszsuly = Double.parseDouble(osszsuly.replace(",", "."));
        this.csk = "";
        this.kt = "";
    }

    public TermekDij(TermekDij td)
    {
        this.tipus = td.getTipus();
        this.nev = td.getNev();
        this.szelesseg = td.getSzelesseg();
        this.magassag = td.getMagassag();
        this.peldany = td.getPeldany();
        this.egysegsuly = td.getEgysegsuly();
        this.termekDij = td.getTermekDij();
        this.osszsuly = td.getSzamoltSuly();
        this.csk = td.getCsk();
        this.kt = td.getKt();
    }
    
    public TermekDij(String id, String table)
    {
        Query query = new Query.QueryBuilder()
            .select("type, name, width, height, copy, unitWeight, productFee, totalWeight, csk, kt ")
            .from(table)
            .where("id = " + id)
            .build();
        Object[][] s = NAV.db.select(query.getQuery());
        
        this.tipus = s[0][0].toString();
        this.nev = String.valueOf(s[0][1]);
        this.szelesseg = Double.parseDouble(String.valueOf(s[0][2]));
        this.magassag = Double.parseDouble(String.valueOf(s[0][3]));
        this.peldany = Double.parseDouble(String.valueOf(s[0][4]));
        this.egysegsuly = Double.parseDouble(String.valueOf(s[0][5]));
        this.termekDij = Double.parseDouble(String.valueOf(s[0][6]));
        this.osszsuly = Double.parseDouble(String.valueOf(s[0][7]));
        this.csk = String.valueOf(s[0][8]);
        this.kt = String.valueOf(s[0][9]);
    }

    private TermekDij(TermekDijBuilder termekDijBuilder)
    {
        this.tipus = termekDijBuilder.tipus;
        this.nev = termekDijBuilder.nev;
        this.szelesseg = termekDijBuilder.szelesseg;
        this.magassag = termekDijBuilder.magassag;
        this.peldany = termekDijBuilder.peldany;
        this.egysegsuly = termekDijBuilder.egysegsuly;
        this.termekDij = termekDijBuilder.termekDij;
        this.osszsuly = termekDijBuilder.osszsuly;
        this.csk = termekDijBuilder.csk;
        this.kt = termekDijBuilder.kt;
    }
    
    public void Save(int productID, String productFee_table)
    {
        String query = "INSERT INTO " + productFee_table + " ("
            + "type, "
            + "productID, "
            + "name, "
            + "width, "
            + "height, "
            + "copy, "
            + "unitWeight, "
            + "totalWeight, "
            + "productFee, "
            + "csk, "
            + "kt) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Object [] values = new Object[11];
        
        values[0] = tipus;
        values[1] = productID;
        values[2] = nev;
        values[3] = szelesseg;
        values[4] = magassag;
        values[5] = peldany;
        values[6] = egysegsuly;
        values[7] = osszsuly;
        values[8] = termekDij;
        values[9] = csk;
        values[10] = kt;
        
        NAV.db.insert(query, values, 11);
    }
    
    public void Update(int productID, String productFee_table)
    {
        String query = "UPDATE " + productFee_table + " SET "
            + "type = ?, "
            + "name = ?, "
            + "width = ?, "
            + "height = ?, "
            + "copy = ?, "
            + "unitWeight = ?, "
            + "totalWeight = ?, "
            + "productFee = ?, "
            + "csk = ?, "
            + "kt = ? "
            + "WHERE productID = " + productID;
        
        Object [] values = new Object[10];
        
        values[0] = tipus;
        values[1] = nev;
        values[2] = szelesseg;
        values[3] = magassag;
        values[4] = peldany;
        values[5] = egysegsuly;
        values[6] = osszsuly;
        values[7] = termekDij;
        values[8] = csk;
        values[9] = kt;
        
        NAV.db.insert(query, values, values.length);
    }

    public double getOsszsuly()
    {
        return osszsuly;
    }

    public void setOsszsuly(double osszsuly) {
        this.osszsuly = osszsuly;
    }

    public double getMagassag() {
        return magassag;
    }

    public void setMagassag(double magassag) {
        this.magassag = magassag;
    }

    public String getNev() {
        return nev;
    }

    public void setNev(String nev) {
        this.nev = nev;
    }

    public double getPeldany() {
        return peldany;
    }

    public void setPeldany(double peldany) {
        this.peldany = peldany;
    }

    public double getSzelesseg() {
        return szelesseg;
    }

    public void setSzelesseg(double szelesseg) {
        this.szelesseg = szelesseg;
    }

    public double getTermekDij() {
        return termekDij;
    }

    public void setTermekDij(double termekDij) {
        this.termekDij = termekDij;
    }

    public double getEgysegsuly() {
        return egysegsuly;
    }

    public void setEgysegsuly(double suly) {
        this.egysegsuly = suly;
    }

    public String getCsk() {
        return csk;
    }

    public void setCsk(String csk) {
        this.csk = csk;
    }

    public String getKt() {
        return kt;
    }

    public void setKt(String kt) {
        this.kt = kt;
    }

    public void setSzorzo(double szorzo){
        this.szorzo = szorzo;
    }

    public String getTipus() {
        return tipus;
    }

    public void setTipus(String tipus) {
        this.tipus = tipus;
    }

    public int getOsszTermekDijNetto(boolean deviza){
        double netto = getSuly() * termekDij;
        return NumericFunctions.Round(deviza ? Math.round(netto * 100.0) / 100.0 : Math.round(netto));
    }

    public int getOsszTermekDijAfaErtek(boolean deviza)
    {
        double afaErtek = getOsszTermekDijNetto(deviza) * 0.27;
        return NumericFunctions.Round(deviza ? Math.round(afaErtek * 100.0) / 100.0 : Math.round(afaErtek));
    }

    public int getOsszTermekDijBrutto(boolean deviza)
    {
        return NumericFunctions.Round(getOsszTermekDijNetto(deviza) + getOsszTermekDijAfaErtek(deviza));
    }

    private double getSzamoltSuly() {
        return Math.round(szelesseg / 1000.0 * magassag / 1000.0 * egysegsuly / 1000.0 * peldany * 1000000.0) / 1000000.0;
    }

    public double getSuly() {
        return (this.getOsszsuly() != 0 ? this.getOsszsuly() : this.getSzamoltSuly()) * szorzo;
    }
    
    public static Object[][] getProductFeeTypes()
    {
        Query query = new Query.QueryBuilder()
            .select("type, productFee")
            .from(TABLE)
            .build();
        Object [][] productFeeTypes = NAV.db.select(query.getQuery());
        
        return productFeeTypes;
    }

    @Override
    public String toString() {
        return "Név:\t\t" + this.nev + "\nSzélesség:\t\t" + this.szelesseg + "\nMagasság:\t\t" + this.magassag
                + "\nPéldány:\t\t" + this.peldany + "\nSúly:\t\t\t" + this.egysegsuly + "\nTermék díj:\t\t" + this.termekDij
                + "\nÖssz.súly:\t\t" + this.getSzamoltSuly() + "\nÖssz.termékdíj (true):\t\t" + this.getOsszTermekDijNetto(true);
    }

    public static class TermekDijBuilder
    {
        public String tipus;
        public String nev;
        public double szelesseg;
        public double magassag;
        public double peldany;
        public double egysegsuly;
        public double termekDij;
        public double osszsuly;
        public double szorzo = 1.0;
        public String csk;
        public String kt;

        public TermekDijBuilder tipus(String tipus)
        {
            this.tipus = tipus;
            return this;
        }
        
        public TermekDijBuilder nev(String nev) {
            this.nev = nev;
            return this;
        }

        public TermekDijBuilder szelesseg(double szelesseg) {
            this.szelesseg = szelesseg;
            return this;
        }

        public TermekDijBuilder magassag(double magassag) {
            this.magassag = magassag;
            return this;
        }

        public TermekDijBuilder peldany(double peldany) {
            this.peldany = peldany;
            return this;
        }

        public TermekDijBuilder egysegsuly(double egysegsuly) {
            this.egysegsuly = egysegsuly;
            return this;
        }

        public TermekDijBuilder termekDij(double termekDij) {
            this.termekDij = termekDij;
            return this;
        }

        public TermekDijBuilder osszsuly(double osszsuly) {
            this.osszsuly = osszsuly;
            return this;
        }

        public TermekDijBuilder szorzo(double szorzo) {
            this.szorzo = szorzo;
            return this;
        }

        public TermekDijBuilder csk(String csk) {
            this.csk = csk;
            return this;
        }

        public TermekDijBuilder kt(String kt) {
            this.kt = kt;
            return this;
        }

        public TermekDij build() {
            return new TermekDij(this);
        }
    }
}