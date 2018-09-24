/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeszamlazo.database;

import java.sql.Connection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import cezeszamlazo.databasemodel.*;

/**
 *
 * @author szekus
 */
public class LocalDBTest {
    
    public LocalDBTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of create method, of class LocalDB.
     */
    @Test
    public void testCreate() throws Exception {
        System.out.println("create");
//        LocalDB expResult = null;
        LocalDB localDB = LocalDB.create();
        System.out.println(localDB);
        
        Object[][] select = localDB.select("SELECT * FROM szamlazo_users");
        DataObject dataObject = DataObject.create(select);
        
        dataObject.printValues();
        
//        for (int i = 0; i < select[0].length; i++) {
//            System.out.println(select[2][1]);
////            for (int j = 0; j < select.length; j++) {
////                System.out.println(select[0][j]);
////            }
//            
//        }
        
        // TODO review the generated test code and remove the default call to fail.
    }

//    /**
//     * Test of getConnection method, of class LocalDB.
//     */
//    @Test
//    public void testGetConnection() {
//        System.out.println("getConnection");
//        LocalDB instance = null;
//        Connection expResult = null;
//        Connection result = instance.getConnection();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of select method, of class LocalDB.
//     */
//    @Test
//    public void testSelect() {
//        System.out.println("select");
//        String query = "";
//        LocalDB instance = null;
//        Object[][] expResult = null;
//        Object[][] result = instance.select(query);
//        assertArrayEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of insert method, of class LocalDB.
//     */
//    @Test
//    public void testInsert_3args() {
//        System.out.println("insert");
//        String query = "";
//        Object[] o = null;
//        int cols = 0;
//        LocalDB instance = null;
//        instance.insert(query, o, cols);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of insert method, of class LocalDB.
//     */
//    @Test
//    public void testInsert_4args() {
//        System.out.println("insert");
//        String query = "";
//        Object[] o = null;
//        int cols = 0;
//        boolean d = false;
//        LocalDB instance = null;
//        instance.insert(query, o, cols, d);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of delete method, of class LocalDB.
//     */
//    @Test
//    public void testDelete() {
//        System.out.println("delete");
//        String query = "";
//        LocalDB instance = null;
//        instance.delete(query);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of rowNum method, of class LocalDB.
//     */
//    @Test
//    public void testRowNum() {
//        System.out.println("rowNum");
//        String query = "";
//        LocalDB instance = null;
//        int expResult = 0;
//        int result = instance.rowNum(query);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of insertSync method, of class LocalDB.
//     */
//    @Test
//    public void testInsertSync() {
//        System.out.println("insertSync");
//        String query = "";
//        Object[] o = null;
//        int cols = 0;
//        boolean d = false;
//        LocalDB instance = null;
//        instance.insertSync(query, o, cols, d);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
    
}
