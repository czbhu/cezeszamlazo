/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeszamlazo.kintlevoseg;

import cezeszamlazo.kintlevoseg.KintlevosegLevelAttributum;
import cezeszamlazo.App;
import cezeszamlazo.Settings;
import cezeszamlazo.database.SzamlazoConnection;
import static cezeszamlazo.interfaces.SharedValues.PASSWORD;
import static cezeszamlazo.interfaces.SharedValues.USERNAME;
import java.util.List;
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
public class KintlevosegLevelAttributumTest {

    public KintlevosegLevelAttributumTest() {
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

    

    /**
     * Test of getAll method, of class KintlevosegLevelAttributum.
     */
    @Test
    public void testGetAll() {
        System.out.println("getAll");
        KintlevosegLevelAttributum instance = new KintlevosegLevelAttributum();
        List<KintlevosegLevelAttributum> result = instance.getAll();
        
        for (KintlevosegLevelAttributum kintlevosegLevelAttributum : result) {
            System.out.println(kintlevosegLevelAttributum);
        }
        // TODO review the generated test code and remove the default call to fail.
    }

}
