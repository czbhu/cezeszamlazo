/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeszamlazo.database;

import cezeszamlazo.App;
import cezeszamlazo.EncodeDecode;
import cezeszamlazo.Settings;
import java.util.List;

/**
 *
 * @author szekus
 */
public class RewriteDatabaseData {

    private RewriteDatabaseData() {
    }

    public static RewriteDatabaseData create() {
        return new RewriteDatabaseData();
    }

    public void rewrite(String databaseName, List<MyColumn> column) {
        for (MyColumn myColumn : column) {
            rewrite(databaseName, myColumn);
        }

    }

    public Object[][] getAllTable(String databaseName) {

        Query query = new Query.QueryBuilder()
                .select("DISTINCT TABLE_NAME")
                .from("INFORMATION_SCHEMA.COLUMNS")
                .where("TABLE_SCHEMA = '" + databaseName + "'")
                .build();

        Object[][] select = App.db.select(query.getQuery());
        for (Object[] objects : select) {
            System.out.println(objects[0]);
        }
        return select;
//alter table szamlazo_versions convert to character set utf8 collate utf8_hungarian_ci;
    }

    public String setTableCharacterSet(String tableName) {
        String queryString = "";
        queryString = "alter table " + tableName
                + " convert to character set utf8 collate utf8_hungarian_ci";
        App.db.select(queryString);
        return queryString;
    }

    public void rewrite(String databaseName, MyColumn column) {

        Query query = new Query.QueryBuilder()
                .select("*")
                .from(databaseName)
                .where("1")
                .order("id")
                .build();

        Query updateQuery = new Query.QueryBuilder()
                .update(databaseName)
                .set(column.getColumnName() + " = ? ")
                .where("id = ?")
                .build();

//        String alterQueryString = "ALTER TABLE t1 MODIFY 
//    col1 VARCHAR
//        (5)
//      CHARACTER SET latin1 COLLATE latin1_swedish_ci
//        " ;
//        Query alterQuery = new Query.QueryBuilder().query(alterQueryString);
        Object[][] select = App.db.select(query.getQuery());
        System.out.println(updateQuery);
        for (Object[] rewriteObject : select) {
            Object[] object = new Object[2];
            EncodeDecode.on = false;
            object[0] = EncodeDecode.decode(String.valueOf(rewriteObject[column.getColumnNumber()]));
            object[1] = Integer.valueOf(String.valueOf(rewriteObject[0]));
            App.db.insert(updateQuery.getQuery(), object, 2);
            EncodeDecode.on = true;
        }
    }
}
