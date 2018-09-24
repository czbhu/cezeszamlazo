/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeszamlazo.model;

import cezeszamlazo.App;
import cezeszamlazo.database.Query;
import java.io.Serializable;

/**
 *
 * @author szekus
 */
public class TeljesitesIgazolasModel extends Model implements Serializable {

    private String TABLE_NAME = "szamlazo_teljesites_igazolas";

    private int id;
    private String TIGSorszam;
    private String szamlaSorszam;
    private String teljesitesIgazolasKelte;

    public TeljesitesIgazolasModel() {
        this.values = new Object[3];

    }

    public void setTeljesitestIgazolasByTIGSorszam(String TIGSorszam) {
        String query = "SELECT * FROM " + this.TABLE_NAME + " WHERE TIGSorszam = '" + TIGSorszam + "'";
        selectAllByQuery(query);
    }

    public void setTeljesitestIgazolasBySzamlaSorszam(String szamlaSorszam) {
        String query = "SELECT * FROM " + this.TABLE_NAME + " WHERE szamla_sorszam = '" + szamlaSorszam + "'";
        selectAllByQuery(query);
    }

    @Override
    protected void selectAllByQuery(String query) {
        this.select = App.db.select(query);
        this.id = Integer.parseInt(String.valueOf(select[0][0]));
        this.TIGSorszam = String.valueOf(select[0][1]);
        this.szamlaSorszam = String.valueOf(select[0][2]);
        this.teljesitesIgazolasKelte = String.valueOf(select[0][3]);
    }

    @Override
    public void save() {
        String query = "";
        if (!this.isExistTeljesitesIgazolas()) {
            query = "INSERT INTO " + this.TABLE_NAME
                    + " (id, tig_sorszam, szamla_sorszam, teljesites_igazolas_kelte)"
                    + "VALUES (null,?,?,?)";
            this.setValues();
            App.db.insert(query, this.values, this.values.length);
        } else {
            Object[] updateObject = new Object[2];
            updateObject[0] = this.TIGSorszam;
            updateObject[1] = this.teljesitesIgazolasKelte;
            query = "UPDATE " + this.TABLE_NAME
                    + " SET tig_sorszam = ?, "
                    + "teljesites_igazolas_kelte = ? "
                    + "WHERE szamla_sorszam = '" + this.szamlaSorszam + "'";
            App.db.insert(query, updateObject, 2);
        }

    }

    public boolean isExistTeljesitesIgazolas() {
        Query query = new Query.QueryBuilder()
                .select("*")
                .from(this.TABLE_NAME)
                .where("szamla_sorszam = '" + this.szamlaSorszam + "'")
                .order("")
                .build();
        Object[][] select = App.db.select(query.getQuery());
        if (select.length > 0) {
            return true;
        }
        return false;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSzamlaSorszam(String szamlaSorszam) {
        this.szamlaSorszam = szamlaSorszam;
    }

    public void setTIGSorszam(String TIGSorszam) {
        this.TIGSorszam = TIGSorszam;
    }

    public void setTeljesitesIgazolasKelte(String teljesitesIgazolasKelte) {
        this.teljesitesIgazolasKelte = teljesitesIgazolasKelte;
    }

    public int getId() {
        return id;
    }

    public String getSzamlaSorszam() {
        return szamlaSorszam;
    }

    public String getTIGSorszam() {
        return TIGSorszam;
    }

    public String getTeljesitesIgazolasKelte() {
        return teljesitesIgazolasKelte;
    }

    @Override
    void setValues() {
//        this.values[0][0] = this.id;
        this.values[0] = this.TIGSorszam;
        this.values[1] = this.szamlaSorszam;
        this.values[2] = this.teljesitesIgazolasKelte;
    }
}
