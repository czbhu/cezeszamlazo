/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeszamlazo.functions;

import java.text.DecimalFormat;

/**
 *
 * @author Tomy
 */
public class Formatter {
    public static String currencyFormat(int text)
    {
        String pattern = "###,###.###";
        DecimalFormat myFormatter = new DecimalFormat(pattern);
        String output = myFormatter.format(text);
        
        return output;
    }
    
    public static String currencyFormat(double text)
    {
        String pattern = "###,###.###";
        DecimalFormat myFormatter = new DecimalFormat(pattern);
        String output = myFormatter.format(text);
        
        return output;
    }
}
