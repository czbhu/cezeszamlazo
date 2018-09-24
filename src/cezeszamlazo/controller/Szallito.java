package cezeszamlazo.controller;

import cezeszamlazo.App;
import cezeszamlazo.database.Query;
import java.io.Serializable;

public class Szallito implements Serializable {

    public static final String TABLE = "szamlazo_ceg_adatok";
    private int id;
    private String nev;
    private String irsz;
    private String varos;
    private String utca;
    private String kozterulet;
    private String hazszam;
    private String adoszam;
    private String bankszamlaszam;
    private String megjegyzes;
    private int sorszamid;
    private boolean deviza;
    private String szamlaLablec;

    public Szallito(int id) {
        if (id != 0) {
            Query query = new Query.QueryBuilder()
                    .select("nev, "
                            + "irsz, "
                            + "varos, "
                            + "utca, "
                            + "kozterulet, "
                            + "hazszam, "
                            + "adoszam, "
                            + "bankszamlaszam, "
                            + "megjegyzes, "
                            + "sorszamid, "
                            + "deviza, "
                            + "szamla_lablec ")
                    .from(TABLE)
                    .where("id = " + id)
                    .build();
            Object[][] select = App.db.select(query.getQuery());
            if (select.length != 0) {
                this.id = id;
                nev = String.valueOf(select[0][0]);
                irsz = String.valueOf(select[0][1]);
                varos = String.valueOf(select[0][2]);
                utca = String.valueOf(select[0][3]);    
                kozterulet = String.valueOf(select[0][4]);
                hazszam = String.valueOf(select[0][5]);         
                adoszam = String.valueOf(select[0][6]);
                bankszamlaszam = String.valueOf(select[0][7]);
                megjegyzes = String.valueOf(select[0][8]);
                sorszamid = Integer.parseInt(String.valueOf(select[0][9]));
                deviza = String.valueOf(select[0][10]).equalsIgnoreCase("1");
                szamlaLablec = String.valueOf(select[0][11]);
            } else {
                init();
            }

        } else {
            init();
        }
    }

    public Szallito(String nev, String irsz, String varos, String utca, String kozterulet, String hazszam, String adoszam, String bankszamlaszam, String megjegyzes, String szamlaLablec) {
        this.nev = nev;
        this.irsz = irsz;
        this.varos = varos;
        this.utca = utca;
        this.kozterulet = kozterulet;
        this.hazszam = hazszam;
        this.adoszam = adoszam;
        this.bankszamlaszam = bankszamlaszam;
        this.megjegyzes = megjegyzes;
        this.szamlaLablec = szamlaLablec;
    }

    public Szallito(String nev, String irsz, String varos, String utca,String kozterulet, String hazszam, String adoszam, String bankszamlaszam, String megjegyzes) {
        this.nev = nev;
        this.irsz = irsz;
        this.varos = varos;
        this.utca = utca;
        this.kozterulet = kozterulet;
        this.hazszam = hazszam;
        this.adoszam = adoszam;
        this.bankszamlaszam = bankszamlaszam;
        this.megjegyzes = megjegyzes;
    }

    private void init() {
        id = 0;
        nev = "";
        irsz = "";
        varos = "";
        utca = "";
        kozterulet = "";
        hazszam = "";
        adoszam = "";
        bankszamlaszam = "";
        megjegyzes = "";
        sorszamid = 0;
        deviza = false;
        szamlaLablec = "";
    }

    public String getAdoszam() {
        return adoszam;
    }

    public void setAdoszam(String adoszam) {
        this.adoszam = adoszam;
    }

    public String getBankszamlaszam() {
        return bankszamlaszam;
    }

    public void setBankszamlaszam(String bankszamlaszam) {
        this.bankszamlaszam = bankszamlaszam;
    }

    public boolean isDeviza() {
        return deviza;
    }

    public void setDeviza(boolean deviza) {
        this.deviza = deviza;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIrsz() {
        return irsz;
    }

    public void setIrsz(String irsz) {
        this.irsz = irsz;
    }

    public String getMegjegyzes() {
        return megjegyzes;
    }

    public void setMegjegyzes(String megjegyzes) {
        this.megjegyzes = megjegyzes;
    }

    public String getNev() {
        return nev;
    }

    public void setNev(String nev) {
        this.nev = nev;
    }

    public int getSorszamid() {
        return sorszamid;
    }

    public void setSorszamid(int sorszamid) {
        this.sorszamid = sorszamid;
    }

    public String getUtca() {
        return utca;
    }

    public void setUtca(String utca) {
        this.utca = utca;
    }
    
    public String getKozterulet() {
        return kozterulet;
    }

    public void setKozterulet(String kozterulet) {
        this.kozterulet = kozterulet;
    }
    
    public String getHazszam() {
        return hazszam;
    }

    public void setHazszam(String hazszam) {
        this.hazszam = hazszam;
    }

    public String getVaros() {
        return varos;
    }

    public String getAddress() {
        return utca + " " + kozterulet + " " + hazszam;
    }
    
    public void setVaros(String varos) {
        this.varos = varos;
    }

    public String getSzamlaLablec() {
        if (szamlaLablec == null) {
            szamlaLablec = "";
        }
        return szamlaLablec;
    }

    public void setSzamlaLablec(String szamlaLablec) {
        this.szamlaLablec = szamlaLablec;
    }

    @Override
    public String toString() {
        return nev + "\n"
                + (!irsz.isEmpty() && !irsz.equalsIgnoreCase("0") ? irsz + ", " : "")
                + (varos.isEmpty() ? "" : varos + "\n")
                + (utca.isEmpty() ? "" : utca + "\n")
                + (kozterulet.isEmpty() ? "" : kozterulet + "\n")
                + (hazszam.isEmpty() ? "" : hazszam + "\n")
                + (adoszam.isEmpty() ? "" : adoszam + "\n")
                + (bankszamlaszam.isEmpty() ? "" : bankszamlaszam + "\n")
                + (megjegyzes.isEmpty() ? "" : megjegyzes);
    }
}
