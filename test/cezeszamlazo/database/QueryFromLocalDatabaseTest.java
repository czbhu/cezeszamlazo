/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeszamlazo.database;

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
public class QueryFromLocalDatabaseTest {

    public QueryFromLocalDatabaseTest() {
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

//    /**
//     * Test of create method, of class QueryFromLocalDatabase.
//     */
//    @Test
//    public void testCreate() {
//        System.out.println("create");
//        QueryFromLocalDatabase expResult = null;
//        QueryFromLocalDatabase result = QueryFromLocalDatabase.create();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
    /**
     * Test of getValues method, of class QueryFromLocalDatabase.
     */
    @Test
    public void testGetValues() {
        System.out.println("getValues");
        QueryFromLocalDatabase queryFromLocalDatabase = QueryFromLocalDatabase.create();

        queryFromLocalDatabase.getValues();
    }

}
