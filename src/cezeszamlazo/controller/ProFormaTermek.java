/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeszamlazo.controller;

import cezeszamlazo.App;
import cezeszamlazo.database.Query;
import java.util.LinkedList;

/**
 *
 * @author pappadam
 */
public class ProFormaTermek {

    /**
     * az adatbázis tábla neve
     */
    public static final String TABLE = "szamlazo_pro_forma_adatok";
    /**
     * az adatbázis táblában lévő mezők
     */
    private int id;
    private int proFormaId;
    private String termek;
    private String termekKod;
    private double mennyiseg;
    private String mennyisegiEgyseg;
    private double egysegar;
    private int afa;
    private int engedmenyFelar;
    private String vtszTeszor;

    public ProFormaTermek() {
    }

    public ProFormaTermek(int id) {
        if (id != 0) {
        } else {
            init();
        }
    }

    public ProFormaTermek(int id, int proFormaId, String termek, String termekKod, double mennyiseg, String mennyisegiEgyseg, double egysegar, int afa, int engedmenyFelar, String vtszTeszor) {
        this.id = id;
        this.proFormaId = proFormaId;
        this.termek = termek;
        this.termekKod = termekKod;
        this.mennyiseg = mennyiseg;
        this.mennyisegiEgyseg = mennyisegiEgyseg;
        this.egysegar = egysegar;
        this.afa = afa;
        this.engedmenyFelar = engedmenyFelar;
        this.vtszTeszor = vtszTeszor;
    }

    private void init() {
        this.id = 0;
        this.proFormaId = 0;
        this.termek = "";
        this.termekKod = "";
        this.mennyiseg = 0.0;
        this.mennyisegiEgyseg = "db";
        this.egysegar = 0.0;
        this.afa = 27;
        this.engedmenyFelar = 0;
        this.vtszTeszor = "";
    }

    public static LinkedList<ProFormaTermek> getTermekek(int proFromaId) {
        LinkedList<ProFormaTermek> termekek = new LinkedList<ProFormaTermek>();

        Query query = new Query.QueryBuilder()
                .select("id, "
                        + "proFormaId, "
                        + "termek, "
                        + "termekKod, "
                        + "mennyiseg, "
                        + "mennyisegiEgyseg, "
                        + "egysegar, "
                        + "afa, "
                        + "engedmenyFelar, "
                        + "vtszTeszor ")
                .from(TABLE)
                .where("pro_forma_id = " + proFromaId)
                .build();
        Object[][] select = App.db.select(query.getQuery());
        for (int i = 0; i < select.length; i++) {
            termekek.add(new ProFormaTermek(
                    Integer.parseInt(String.valueOf(select[i][0])),
                    Integer.parseInt(String.valueOf(select[i][1])),
                    String.valueOf(select[i][2]),
                    String.valueOf(select[i][3]),
                    Double.parseDouble(String.valueOf(select[i][4])),
                    String.valueOf(select[i][5]),
                    Double.parseDouble(String.valueOf(select[i][6])),
                    Integer.parseInt(String.valueOf(select[i][7])),
                    Integer.parseInt(String.valueOf(select[i][8])),
                    String.valueOf(select[i][9])));
        }

        return termekek;
    }

    /**
     * GET / SET metódusok
     */
    public int getAfa() {
        return afa;
    }

    public void setAfa(int afa) {
        this.afa = afa;
    }

    public double getEgysegar() {
        return egysegar;
    }

    public void setEgysegar(double egysegar) {
        this.egysegar = egysegar;
    }

    public int getEngedmenyFelar() {
        return engedmenyFelar;
    }

    public void setEngedmenyFelar(int engedmenyFelar) {
        this.engedmenyFelar = engedmenyFelar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getMennyiseg() {
        return mennyiseg;
    }

    public void setMennyiseg(double mennyiseg) {
        this.mennyiseg = mennyiseg;
    }

    public String getMennyisegiEgyseg() {
        return mennyisegiEgyseg;
    }

    public void setMennyisegiEgyseg(String mennyisegiEgyseg) {
        this.mennyisegiEgyseg = mennyisegiEgyseg;
    }

    public int getProFormaId() {
        return proFormaId;
    }

    public void setProFormaId(int proFormaId) {
        this.proFormaId = proFormaId;
    }

    public String getTermek() {
        return termek;
    }

    public void setTermek(String termek) {
        this.termek = termek;
    }

    public String getTermekKod() {
        return termekKod;
    }

    public void setTermekKod(String termekKod) {
        this.termekKod = termekKod;
    }

    public String getVtszTeszor() {
        return vtszTeszor;
    }

    public void setVtszTeszor(String vtszTeszor) {
        this.vtszTeszor = vtszTeszor;
    }

    public double getNetto() {
        return egysegar * mennyiseg;
    }

    public double getAfaErtek() {
        return getNetto() * (1.0 - afa / 100.0);
    }

    public double getBrutto() {
        return getNetto() + getAfaErtek();
    }

    public void mentes(int proFormaId) {
        this.proFormaId = proFormaId;
        Object[] o = new Object[9];
        o[0] = this.proFormaId;
        o[1] = termek;
        o[2] = termekKod;
        o[3] = mennyiseg;
        o[4] = mennyisegiEgyseg;
        o[5] = egysegar;
        o[6] = afa;
        o[7] = engedmenyFelar;
        o[8] = vtszTeszor;
        if (id == 0) {
            App.db.select("INSERT INTO " + TABLE + " (pro_forma_id, termek, termek_kod, mennyiseg, mennyisegi_egyseg, egysegar, afa, engedmeny_felar, vtsz_teszor) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
        } else {
            // NINCS UPDATE
        }
    }
}
