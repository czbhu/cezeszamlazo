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

/**
 *
 * @author szekus
 */
public class ConnectLocalDBTest {
    
    public ConnectLocalDBTest() {
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
        LocalDB result = LocalDB.create();
        System.out.println(result);
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
    
}
