/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeszamlazo.database;

import cezeszamlazo.App;
import cezeszamlazo.EncodeDecode;
import cezeszamlazo.Settings;
import cezeszamlazo.controller.Functions;
import static cezeszamlazo.interfaces.SharedValues.PASSWORD;
import static cezeszamlazo.interfaces.SharedValues.USERNAME;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author szekus
 */
public class rewriteDBDatasTest {

    public rewriteDBDatasTest() {
    }

    @BeforeClass
    public static void setUpClass() {

    }

    @AfterClass
    public static void tearDownClass() {
//        App.db.disconnect();
    }

    @Before
    public void setUp() {
        String clientUrl = "jdbc:mysql://localhost/szamlazo_" + Settings.getId()
                + "?useUnicode=true&characterEncoding=UTF-8";
        SzamlazoConnection clientConnection = new SzamlazoConnection(clientUrl, USERNAME, PASSWORD);
        App.db = new cezeszamlazo.Database(clientConnection);
        App.db.connect();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of formatFieldName method, of class Functions.
     */
    @Test
    public void testRewrite() {
        System.out.println("reWrite");
//        Query query = new Query.QueryBuilder()
//                .select("*")
//                .from("szamlazo_termek")
//                .where("1")
//                .order("id")
//                .build();
//
//        Query updateQuery = new Query.QueryBuilder()
//                .update("szamlazo_termek")
//                .set("nev = ?")
//                .where("id = ?")
//                .build();
//        Object[][] select = App.db.select(query.getQuery());
//        for (Object[] termekObject : select) {
//            Object[] object = new Object[2];
//
//            EncodeDecode.on = false;
//            object[0] = EncodeDecode.decode(String.valueOf(termekObject[1]));
//            object[1] = Integer.valueOf(String.valueOf(termekObject[0]));
////            System.out.println(EncodeDecode.flag);
//
////            EncodeDecode.flag = EncodeDecode.Flag.OFF;
//            System.out.println(object[0]);
////            System.out.println(object[1]);
//
//            App.db.insert(updateQuery.getQuery(), object, 2);
//        }

        RewriteDatabaseData rewriteDatabaseData = RewriteDatabaseData.create();
//        rewriteDatabaseData.rewrite("szamlazo_termek", "nev");

        List<MyColumn> columns = new ArrayList<>();

//        columns.add(new MyColumn(1, "nev"));
//        columns.add(new MyColumn(2, "leiras"));
//        rewriteDatabaseData.rewrite("szamlazo_termek_csoportok", columns);
//        columns.clear();
//        columns.add(new MyColumn(1, "verzio"));
//        columns.add(new MyColumn(2, "leiras"));
//        rewriteDatabaseData.rewrite("cr_versions", columns);
//        columns.clear();
//        Object[][] tables = rewriteDatabaseData.getAllTable("szamlazo_" + Settings.getId());
//
//        List<String> alterTableCommand = new ArrayList<>();
//        for (Object[] table : tables) {
//            alterTableCommand.add(rewriteDatabaseData.setTableCharacterSet(String.valueOf(table[0])));
//        }
//        for (String string : alterTableCommand) {
//            System.out.println(string + ";");
//        }
        Set<Integer> set = new TreeSet<Integer>();
        List<Integer> list = new ArrayList<>();

        for (int i = -3; i < 3; i++) {
            set.add(i);
            list.add(i);
        }
        for (Integer integer : list) {
            System.out.println(integer);
        }
        for (int i = 0; i < 3; i++) {
            set.remove(i);
            System.out.println(list);
            list.remove(i);
            System.out.println(list);
        }

        System.out.println(set + " " + list);
    }
}
