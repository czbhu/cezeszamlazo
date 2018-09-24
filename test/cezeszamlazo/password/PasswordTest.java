/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeszamlazo.password;

import java.util.logging.Level;
import java.util.logging.Logger;
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
public class PasswordTest {

    public PasswordTest() {
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
     * Test of createPasswordString method, of class Password.
     */
    @Test
    public void testCreate() {
        try {
            System.out.println("create");
//        Password expResult = null;

            String passwordString_1 = "123AA1";
            String passwordString_2 = "123AA1";

            Password password = Password.create();
            Password password2 = Password.create();

            PasswordRules passwordRules = new PasswordRules.PasswordRulesBuilder()
                    .compare("BUG")
                    .empty("NEM LEHET ÜRES")
                    .minLength(4)
                    .maxLength(15, "TÚL HOSSZÓ")
                    .minNumberOfNumber(1, "")
                    .minUpperCaseCharacter(0, "")
                    .build();
            System.out.println(password.generatePassword(passwordRules));
            password = password.createPasswordString(passwordString_1, passwordRules);
//            password2 = password.createPasswordString(passwordString_2, passwordRules);

//            password.comparePassword(passwordString_2);
//        System.out.println(password.getPasswordRules());
//        assertEquals(expResult, result);
// TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
        } catch (PasswordRulesException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Test of createPasswordString method, of class Password.
     */
    @Test
    public void testGenerate() {
//        System.out.println("generate");
//        Password expResult = null;
        Password password = Password.create();

//        System.out.println(password.generatePassword());
//        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

}
