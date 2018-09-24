package cezeszamlazo.controller;

import cezeszamlazo.App;
import cezeszamlazo.database.Query;
import java.io.Serializable;

/**
 *
 * @author Papp Ádám - Ceze Reklam
 */
public class SzamlaTermek implements Serializable {

    private String nev;
    private String cikkszam;
    private String mee;
    private double afa;
    private String afaLabel;
    private String vtszTeszor;
    private double egysegar;
    private double mennyiseg;
    private TermekDij termekDij;

    public SzamlaTermek(String nev, String cikkszam, String mee, String afa, String vtszTeszor, String egysegar, String mennyiseg, String afaLabel) {
        this.nev = nev;
        this.cikkszam = cikkszam;
        this.mee = mee;
        this.afa = Double.parseDouble(afa.replace(",", "."));
        this.vtszTeszor = vtszTeszor;
        this.egysegar = Double.parseDouble(egysegar.replace(",", "."));
        this.mennyiseg = Double.parseDouble(mennyiseg.replace(",", "."));
        this.termekDij = null;
        this.afaLabel = afaLabel;
    }

    public SzamlaTermek(String id) {
        Query query = new Query.QueryBuilder()
                .select("termek, termek_kod, mennyiseg, mennyisegi_egyseg, egysegar, afa, vtsz_teszor, afa_label ")
                .from("szamlazo_szamla_adatok")
                .where("id = " + id)
                .build();
        Object[][] select = App.db.select(query.getQuery());
        this.nev = String.valueOf(select[0][0]);
        this.cikkszam = String.valueOf(select[0][1]);
        this.mennyiseg = Double.parseDouble(String.valueOf(select[0][2]));
        this.mee = String.valueOf(select[0][3]);
        this.egysegar = Double.parseDouble(String.valueOf(select[0][4]));
        this.afa = Double.parseDouble(String.valueOf(select[0][5]));
        this.vtszTeszor = String.valueOf(select[0][6]);
        this.afaLabel = String.valueOf(select[0][7]);
        query = new Query.QueryBuilder()
                .select("id")
                .from("szamlazo_szamla_termekdij")
                .where("termekid = " + id)
                .build();
        select = App.db.select(query.getQuery());
        if (select.length != 0) {
            this.termekDij = new TermekDij(String.valueOf(select[0][0]));
        } else {
            this.termekDij = null;
        }
    }

    public TermekDij getTermekDij() {
        return termekDij;
    }

    public void setTermekDij(TermekDij termekDij)
    {
        this.termekDij = termekDij;
    }

    public double getAfa() {
        return afa;
    }
    
    public String getAfaLabel() {
        return afaLabel;
    }

    public String getCikkszam() {
        return cikkszam;
    }

    public double getEgysegar() {
        return egysegar;
    }

    public String getMee() {
        return mee;
    }

    public double getMennyiseg() {
        return mennyiseg;
    }

    public double getNetto(boolean deviza) {
        double netto = egysegar * mennyiseg;
        return (deviza ? Math.round(netto * 100.0) / 100.0 : Math.round(netto * 100.0) / 100.0);
    }

    public double getAfaErtek(boolean deviza) {
        double afaErtek = getNetto(deviza) * (afa / 100.0);
        return (deviza ? Math.round(afaErtek * 10000.0) / 10000.0 : Math.round(afaErtek * 100.0) / 100.0);
    }

    public double getBrutto(boolean deviza) {
        return (deviza ? getNetto(deviza) + getAfaErtek(deviza) : Math.round(getNetto(deviza)) + Math.round(getAfaErtek(deviza)));
    }

    public String getNev() {
        return nev;
    }

    public String getVtszTeszor() {
        return vtszTeszor;
    }

    public void setAfa(double afa) {
        this.afa = afa;
    }
    
    public void setAfaLabel(String afaLabel) {
        this.afaLabel = afaLabel;
    }

    public void setVtszTeszor(String vtszTeszor) {
        this.vtszTeszor = vtszTeszor;
    }

    public void szorozMennyiseg(double m) {
        this.mennyiseg *= m;
    }

    @Override
    public String toString() {
        return nev + "\t" + cikkszam + "\t" + vtszTeszor + "\t" + mennyiseg + " " + mee + "\t" + egysegar + "\t" + getNetto(true) + "\t" + afaLabel + "\t" + getAfaErtek(true) + "\t" + getBrutto(true) + "\n";
    }

}
