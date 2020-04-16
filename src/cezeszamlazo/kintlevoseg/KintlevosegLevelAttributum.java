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
public class KintlevosegLevelAttributum
{
    private int id;
    private String name;
    private String refTable;
    private String refAttr;
    private String sqlCommand = "";
    private String value;

    public KintlevosegLevelAttributum() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRefAttr() {
        return refAttr;
    }

    public String getRefTable() {
        return refTable;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRefAttr(String refAttr) {
        this.refAttr = refAttr;
    }

    public void setRefTable(String refTable) {
        this.refTable = refTable;
    }

    public void setSqlCommand(String sqlCommand) {
        this.sqlCommand = sqlCommand;
    }

    public String getSqlCommand() {
        return sqlCommand;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public List<KintlevosegLevelAttributum> getAll() {
        List<KintlevosegLevelAttributum> list = new ArrayList<>();
        Query query = new Query.QueryBuilder()
                .select("*")
                .from("szamlazo_kintlevoseg_level_attributumok")
                .where("1=1")
                .build();
        Object[][] select = App.db.select(query.getQuery());

        for (int i = 0; i < select.length; i++) {
            KintlevosegLevelAttributum kintlevosegLevelAttributum = new KintlevosegLevelAttributum();
            kintlevosegLevelAttributum.setId(Functions.getIntFromObject(select[i][0]));
            kintlevosegLevelAttributum.setName(Functions.getStringFromObject(select[i][1]));
            kintlevosegLevelAttributum.setRefTable(Functions.getStringFromObject(select[i][2]));
            kintlevosegLevelAttributum.setRefAttr(Functions.getStringFromObject(select[i][3]));
            kintlevosegLevelAttributum.setSqlCommand(Functions.getStringFromObject(select[i][4]));
            list.add(kintlevosegLevelAttributum);
        }

        return list;
    }

    public KintlevosegLevelAttributum getHtmlAttribute(String attr)
    {
        Query query = new Query.QueryBuilder()
            .select("*")
            .from("szamlazo_kintlevoseg_level_attributumok2")
            .where("ref_attr = '" + attr + "'")
            .build();
        Object[][] select = App.db.select(query.getQuery());
        try
        {
            KintlevosegLevelAttributum kintlevosegLevelAttributum = new KintlevosegLevelAttributum();
            kintlevosegLevelAttributum.setId(Functions.getIntFromObject(select[0][0]));
            kintlevosegLevelAttributum.setName(Functions.getStringFromObject(select[0][1]));
            kintlevosegLevelAttributum.setRefTable(Functions.getStringFromObject(select[0][2]));
            kintlevosegLevelAttributum.setRefAttr(Functions.getStringFromObject(select[0][3]));
            kintlevosegLevelAttributum.setSqlCommand(Functions.getStringFromObject(select[0][4]));
            return kintlevosegLevelAttributum;
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            e.printStackTrace();
            //System.out.println(attr);
        }
        return null;
    }

    @Override
    public String toString()
    {
        return "id: " + id + "; name: " + name + "; ref_table: " + refTable + "; ref_attr: " + refAttr;
    }

    public List<String> getAllName()
    {
        List<String> list = new ArrayList<>();
        Query query = new Query.QueryBuilder()
            .select("name")
            .from("szamlazo_kintlevoseg_level_attributumok")
            .where("1=1")
            .order("name")
            .build();
        Object[][] select = App.db.select(query.getQuery());

        for (int i = 0; i < select.length; i++) {
            list.add(Functions.getStringFromObject(select[i][0]));
        }

        return list;
    }

//    public String createSelect(List<KintlevosegLevelAttributum> list) {
//        String select = "";
//
//        int i = 0;
//        for (KintlevosegLevelAttributum kintlevosegLevelAttributum : list) {
//            if (kintlevosegLevelAttributum.getSqlCommand() != "") {
//                select += kintlevosegLevelAttributum.getSqlCommand();
//            } else {
//                select += "sz." + kintlevosegLevelAttributum.getRefAttr();
//            }
//            
//            if (i != list.size()-1) {
//                select += ",";
//            }
//            i++;
//        }
//
//        return select;
//    }
}