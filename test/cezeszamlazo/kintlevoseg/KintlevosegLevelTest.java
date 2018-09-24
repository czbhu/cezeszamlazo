/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeszamlazo.kintlevoseg;

import cezeszamlazo.App;
import cezeszamlazo.database.SzamlazoConnection;
import static cezeszamlazo.interfaces.SharedValues.PASSWORD;
import static cezeszamlazo.interfaces.SharedValues.USERNAME;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author szekus
 */
public class KintlevosegLevelTest {

    public KintlevosegLevelTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        String clientUrl = "jdbc:mysql://localhost/cezetesztdb"
                + "?useUnicode=true&characterEncoding=UTF-8";
        SzamlazoConnection clientConnection = new SzamlazoConnection(clientUrl, USERNAME, PASSWORD);
        App.db = new cezeszamlazo.Database(clientConnection);
        App.db.connect();
    }

    @After
    public void tearDown() {
    }

//    /**
//     * Test of create method, of class KintlevosegLevel.
//     */
//    @Test
//    public void testCreate() {
//        System.out.println("create");
//        KintlevosegLevel expResult = null;
//        KintlevosegLevel result = KintlevosegLevel.create();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getHtmlString method, of class KintlevosegLevel.
//     */
//    @Test
//    public void testGetHtmlString() {
//        System.out.println("getHtmlString");
//        KintlevosegLevel instance = null;
//        StringBuilder expResult = null;
//        StringBuilder result = instance.getHtmlString();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of setHtmlString method, of class KintlevosegLevel.
//     */
//    @Test
//    public void testSetHtmlString() {
//        System.out.println("setHtmlString");
//        StringBuilder htmlString = null;
//        KintlevosegLevel instance = null;
//        instance.setHtmlString(htmlString);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of save method, of class KintlevosegLevel.
//     */
//    @Test
//    public void testSave() {
//        System.out.println("save");
//        KintlevosegLevel instance = null;
//        instance.save();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getHtmlStringFromDb method, of class KintlevosegLevel.
//     */
//    @Test
//    public void testGetHtmlStringFromDb() {
//        System.out.println("getHtmlStringFromDb");
//        KintlevosegLevel instance = null;
//        StringBuilder expResult = null;
//        StringBuilder result = instance.getHtmlStringFromDb();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//    @Test
//    public void testFunction() {
//        Object[][] select = App.db.select("SELECT * from szamlazo_kintlevoseg_level_attributumok");
//
//        for (int i = 0; i < select.length; i++) {
//            String name = String.valueOf(select[i][3]);
//            Object[] updateObject = new Object[2];
//
//            updateObject[0] = "{" + name + "}";
//            updateObject[1] = Integer.valueOf(String.valueOf(select[i][0]));
//
////            System.out.println(updateObject[0] + " => " + updateObject[1]);
//            App.db.insert("UPDATE szamlazo_kintlevoseg_level_attributumok SET name = ? WHERE id = ?", updateObject, 2);
//        }
//
//    }
    @Test
    public void testFunction2() {
        try {
            KintlevosegLevel kintlevosegLevel = KintlevosegLevel.create(KintlevosegLevel.Type.PDF);

            String[] azonositok = new String[]{"132379036769"};
//            System.out.println("App.user.getNev():" + App.user.getNev());
            kintlevosegLevel.createPDF(azonositok, true);
//            kintlevosegLevel.createEmail(azonositok, true);
//            kintlevosegLevel.reWriteHtml(azonositok);
        } catch (KintlevosegLevelException ex) {
            System.out.println("exception: ");
            ex.printStackTrace();
        }

    }

}
