/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeszamlazo;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author czbhu
 */
public class TimeStamp {
    
    public String getTimeStamp() {
        //String timeStamp = "dadada";
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        return timeStamp;
    }
}
