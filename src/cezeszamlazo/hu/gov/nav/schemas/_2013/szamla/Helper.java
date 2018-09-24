/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeszamlazo.hu.gov.nav.schemas._2013.szamla;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * @author szekus
 */
public class Helper {
    
    public static XMLGregorianCalendar convertStringToXMLDate(String date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        String date = dateFormat.format(new Date());
        XMLGregorianCalendar calendar = null;
        try {
            calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(
                    date);
        } catch (DatatypeConfigurationException ex) {
            Logger.getLogger(Helper.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return calendar;
    }
}
