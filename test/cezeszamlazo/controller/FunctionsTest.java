/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeszamlazo.controller;

import java.util.Date;
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
public class FunctionsTest {
    
    public FunctionsTest() {
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
//     * Test of betuvel method, of class Functions.
//     */
//    @Test
//    public void testBetuvel() {
//        System.out.println("betuvel");
//        double osszeg = 0.0;
//        String expResult = "";
//        String result = Functions.betuvel(osszeg);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of azaz method, of class Functions.
//     */
//    @Test
//    public void testAzaz() {
//        System.out.println("azaz");
//        int osszeg = 0;
//        String expResult = "";
//        String result = Functions.azaz(osszeg);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of dateFormat method, of class Functions.
//     */
//    @Test
//    public void testDateFormat_Date_String() {
//        System.out.println("dateFormat");
//        Date date = null;
//        String format = "";
//        String expResult = "";
//        String result = Functions.dateFormat(date, format);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of dateFormat method, of class Functions.
//     */
//    @Test
//    public void testDateFormat_Date() {
//        System.out.println("dateFormat");
//        Date date = null;
//        String expResult = "";
//        String result = Functions.dateFormat(date);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of now method, of class Functions.
//     */
//    @Test
//    public void testNow() {
//        System.out.println("now");
//        String expResult = "";
//        String result = Functions.now();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of numberFormat method, of class Functions.
//     */
//    @Test
//    public void testNumberFormat() {
//        System.out.println("numberFormat");
//        String num = "";
//        String expResult = "";
//        String result = Functions.numberFormat(num);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of kerekit method, of class Functions.
//     */
//    @Test
//    public void testKerekit() {
//        System.out.println("kerekit");
//        double number = 0.0;
//        boolean isUtalas = false;
//        double expResult = 0.0;
//        double result = Functions.kerekit(number, isUtalas);
//        assertEquals(expResult, result, 0.0);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of formatFieldName method, of class Functions.
     */
    @Test
    public void testFormatFieldName() {
        System.out.println("formatFieldName");
        String columnName = "fizetesi_mod_kotelezo";
        String expResult = "fizetesiModKotelezo";
        String result = Functions.formatFieldName(columnName);
        System.out.println(result);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of betuvel method, of class Functions.
     */
    @Test
    public void testBetuvel() {
        System.out.println("betuvel");
        double osszeg = 0.0;
        String expResult = "";
        String result = Functions.betuvel(osszeg);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of azaz method, of class Functions.
     */
    @Test
    public void testAzaz() {
        System.out.println("azaz");
        int osszeg = 0;
        String expResult = "";
        String result = Functions.azaz(osszeg);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of dateFormat method, of class Functions.
     */
    @Test
    public void testDateFormat_Date_String() {
        System.out.println("dateFormat");
        Date date = null;
        String format = "";
        String expResult = "";
        String result = Functions.dateFormat(date, format);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of dateFormat method, of class Functions.
     */
    @Test
    public void testDateFormat_Date() {
        System.out.println("dateFormat");
        Date date = null;
        String expResult = "";
        String result = Functions.dateFormat(date);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of now method, of class Functions.
     */
    @Test
    public void testNow() {
        System.out.println("now");
        String expResult = "";
        String result = Functions.now();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of numberFormat method, of class Functions.
     */
    @Test
    public void testNumberFormat() {
        System.out.println("numberFormat");
        String num = "";
        String expResult = "";
        String result = Functions.numberFormat(num);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of kerekit method, of class Functions.
     */
    @Test
    public void testKerekit() {
        System.out.println("kerekit");
        double number = 0.0;
        boolean isUtalas = false;
        double expResult = 0.0;
        double result = Functions.kerekit(number, isUtalas);
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of md5 method, of class Functions.
     */
    @Test
    public void testMd5() throws Exception {
        System.out.println("md5");
        String input = "";
        String expResult = "";
        String result = Functions.md5(input);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
