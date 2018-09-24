/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeszamlazo.kintlevoseg;

import cezeszamlazo.App;
import cezeszamlazo.controller.Functions;
import cezeszamlazo.database.Query;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author szekus
 */
public class KintlevosegLevelKapcsolattarto {

    private int id;
    private String name;
    private String email;
    private List<KintlevosegLevelKapcsolattarto> list;

    private KintlevosegLevelKapcsolattarto() {
    }

    public static KintlevosegLevelKapcsolattarto create() {
        KintlevosegLevelKapcsolattarto kapcsolattarto = new KintlevosegLevelKapcsolattarto();
        kapcsolattarto.list = new ArrayList<>();
        return kapcsolattarto;
    }

    public void setKapcsolattartoBySzamlaAzonosito(String azon) {
        Query query = new Query.QueryBuilder()
                .select("nev, email, id")
                .from("pixi_kapcsolattarto")
                .where("ugyfelid in (select id FROM pixi_ugyfel where nev like "
                        + "(SELECT nev from szamlazo_szamla where azon = '" + azon + "' ) ORDER BY id DESC)")
                .order("id DESC")
                .build();

        Object[][] selectFromKapcsolattarto = App.db.select(query.getQuery());

        if (selectFromKapcsolattarto.length != 0) {
            for (int i = 0; i < selectFromKapcsolattarto.length; i++) {
                KintlevosegLevelKapcsolattarto kapcsolattarto = create();
                kapcsolattarto.name = Functions.getStringFromObject(selectFromKapcsolattarto[i][0]);
                kapcsolattarto.email = Functions.getStringFromObject(selectFromKapcsolattarto[i][1]);
                kapcsolattarto.id = Functions.getIntFromObject(selectFromKapcsolattarto[i][2]);
                this.list.add(kapcsolattarto);
            }

        } else {
            query = new Query.QueryBuilder()
                    .select("DISTINCT nev, email ")
                    .from("szamlazo_szamla")
                    .where("azon = '" + azon + "'")
                    .build();
            Object[][] selectFromSzamlazoSzamla = App.db.select(query.getQuery());
            if (selectFromSzamlazoSzamla.length != 0) {
                KintlevosegLevelKapcsolattarto kapcsolattarto = create();
                kapcsolattarto.name = Functions.getStringFromObject(selectFromSzamlazoSzamla[0][0]);
                kapcsolattarto.email = Functions.getStringFromObject(selectFromSzamlazoSzamla[0][1]);
                kapcsolattarto.id = 0;
                this.list.add(kapcsolattarto);
            } else {
                KintlevosegLevelKapcsolattarto kapcsolattarto = create();
                kapcsolattarto.name = "";
                kapcsolattarto.email = "";
                kapcsolattarto.id = 0;
                this.list.add(kapcsolattarto);
            }
        }
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public List<KintlevosegLevelKapcsolattarto> getList() {
        return list;
    }
    
    

}
