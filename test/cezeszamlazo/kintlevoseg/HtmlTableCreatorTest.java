/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeszamlazo.kintlevoseg;

import java.util.ArrayList;
import java.util.Arrays;
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
public class HtmlTableCreatorTest {
    
    public HtmlTableCreatorTest() {
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
//     * Test of create method, of class HtmlTableCreator.
//     */
//    @Test
//    public void testCreate() {
//        System.out.println("create");
//        HtmlTableCreator expResult = null;
//        HtmlTableCreator result = HtmlTableCreator.create();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of setHtmlTableCell method, of class HtmlTableCreator.
//     */
//    @Test
//    public void testSetHtmlTableCell() {
//        System.out.println("setHtmlTableCell");
//        HtmlTableCell htmlTableCell = null;
//        HtmlTableCreator instance = null;
//        instance.setHtmlTableCell(htmlTableCell);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getHtmlTableCell method, of class HtmlTableCreator.
//     */
//    @Test
//    public void testGetHtmlTableCell() {
//        System.out.println("getHtmlTableCell");
//        HtmlTableCreator instance = null;
//        HtmlTableCell expResult = null;
//        HtmlTableCell result = instance.getHtmlTableCell();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
    /**
     * Test of generateTable method, of class HtmlTableCreator.
     */
    @Test
    public void testGenerateTable() {
//        System.out.println("generateTable");
        HtmlTableCreator instance = HtmlTableCreator.create();
        HtmlTableCell htmlTableCell = HtmlTableCell.create();
        htmlTableCell.setTh(Arrays.asList("asdsadsad", "asdasd", "asdsadsa"));
        htmlTableCell.setTd(Arrays.asList("{x}", "{y}", "{z}"));
        System.out.println(htmlTableCell);
        instance.setHtmlTableCell(htmlTableCell);
        
        String result = instance.generateTable();
        System.out.println(result);
        
    }
    
}
