/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeszamlazo.database;

import java.util.regex.Pattern;
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
public class MyFileReaderTest {

    public MyFileReaderTest() {
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
     * Test of create method, of class MyFileReader.
     */
//    @Test
//    public void testCreate() {
//        System.out.println("create");
//        MyFileReader expResult = null;
//        MyFileReader result = MyFileReader.create();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
    /**
     * Test of readByLine method, of class MyFileReader.
     */
    @Test
    public void testReadByLine() throws Exception {
        System.out.println("readByLine");
        String fileUrl = "D:/szamlazo_demo_insert_1.sql";
        MyFileReader instance = MyFileReader.create(fileUrl);
        StringBuilder line = new StringBuilder(instance.readByLine());
//        line = line.replace("`", line);
        

        System.out.println();

//        instance.writeToFile("D:/szamlazo_demo_insert_2.sql", Pattern.compile("`").matcher(line).replaceAll(""));

    }

}
