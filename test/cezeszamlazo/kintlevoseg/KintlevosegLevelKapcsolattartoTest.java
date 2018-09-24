/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeszamlazo.kintlevoseg;

import cezeszamlazo.App;
import cezeszamlazo.database.SzamlazoConnection;
import static cezeszamlazo.interfaces.SharedValues.*;
import java.util.ArrayList;
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
public class KintlevosegLevelKapcsolattartoTest {

    public KintlevosegLevelKapcsolattartoTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        SzamlazoConnection clientConnection = new SzamlazoConnection(URL, USERNAME, PASSWORD);
        App.db = new cezeszamlazo.Database(clientConnection);
        App.db.connect();
    }

    @After
    public void tearDown() {
    }

//    /**
//     * Test of create method, of class KintlevosegLevelKapcsolattarto.
//     */
//    @Test
//    public void testCreate() {
//        System.out.println("create");
//        KintlevosegLevelKapcsolattarto expResult = null;
//        KintlevosegLevelKapcsolattarto result = KintlevosegLevelKapcsolattarto.create();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
    /**
     * Test of setKapcsolattartoBySzamlaAzonosito method, of class
     * KintlevosegLevelKapcsolattarto.
     */
    @Test
    public void testSetKapcsolattartoBySzamlaAzonosito() {
//        System.out.println("setKapcsolattartoBySzamlaAzonosito");
        String azon = "145381617694sdf";
        KintlevosegLevelKapcsolattarto instance = KintlevosegLevelKapcsolattarto.create();
        instance.setKapcsolattartoBySzamlaAzonosito(azon);

//        System.out.println(instance.getName());
//        System.out.println(instance.getEmail());
        // TODO review the generated test code and remove the default call to fail.
    }

    @Test
    public void function_1() {
        String[] azonositok = new String[]{
//            "151058170044",
//            "151033091934",
//            "150911378366",
//            "150883564761",
            "150644425264",
            "150644395941",
            "150644329926",
//            "150644288854",
//            "150644274626",
//            "150635583664",
//            "149734473735"
        };

        String[] names = new String[]{
//            "NI Hungary Kft.",
//            "Bloom Event Kft",
//            "POCC Hungary Kft.",
//            "Ceze Kft.",
            "Bloom Event Kft",
            "Bloom Event Kft",
            "Bloom Event Kft",
//            "Bloom Event Kft.",
//            "Bloom Event Kft.",
//            "Bloom Event Kft.",
//            "Józsa Autócentrum Kft"
        };

        List<KintlevosegKapcsolattartokGroup> list = new ArrayList<>();
       
        int count = 0;
        for (int i = 0; i < azonositok.length; i++) {
            String azon = azonositok[i];
            String name = names[i];
            KintlevosegKapcsolattartokGroup group = KintlevosegKapcsolattartokGroup.create();
            group.setName(name);
            if (list.isEmpty()) {
                list.add(group);
                list.get(0).addToAzonositok(azon);
            } else {
                for (int j = 0; j < list.size(); j++) {
                    if (list.get(j).equals(group)) {
                        list.get(j).addToAzonositok(azon);
                    } else {
                        if (!list.contains(group)) {
                            System.out.println("LOL: " + count++);
                            group.addToAzonositok(azon);
                            list.add(group);
                        }

                    }
                }

            }

        }
        System.out.println(list.size());
//        for (KintlevosegKapcsolattartokGroup kintlevosegKapcsolattartokGroup : list) {
//            System.out.println(kintlevosegKapcsolattartokGroup);
//        }

    }

}
