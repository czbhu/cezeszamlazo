/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeszamlazo.controller;

import cezeszamlazo.App;
import cezeszamlazo.database.Query;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author pappadam
 */
public class Functions
{
    public static String betuvel(double osszeg)
    {
        String result = "";
        int num = (int) Math.floor(osszeg);
        int tizedes = (int) Math.round((osszeg - Math.floor(osszeg)) * 100);
        int ezres = num % 1000;
        num /= 1000;
        int szazezres = num % 1000;
        num /= 1000;
        int millios = num % 1000;
        num /= 1000;
        int milliardos = num % 1000;
        num /= 1000;
        if (milliardos != 0) {
            result += azaz(milliardos) + "millárd";
            if (millios != 0 || szazezres != 0 || ezres != 0) {
                result += "-";
            }
        }
        if (millios != 0) {
            result += azaz(millios) + "millió";
            if (szazezres != 0 || ezres != 0) {
                result += "-";
            }
        }
        if (szazezres != 0) {
            result += azaz(szazezres) + "ezer";
            if (ezres != 0 && osszeg > 2000) {
                result += "-";
            }
        }
        if (ezres != 0) {
            result += azaz(ezres);
        }
        if (osszeg < 1) {
            result = "nulla";
        }
        result = result.substring(0, 1).toUpperCase() + result.substring(1);
        if (tizedes != 0) {
            result += "\\" + tizedes;
        }
        return result;
    }

    public static String azaz(int osszeg)
    {
        String result = "";
        int sz, t, e;
        String[] egyes = {"", "egy", "kettő", "három", "négy", "öt", "hat", "hét", "nyolc", "kilenc"};
        String[] tizes1 = {"", "tizen", "huszon", "harminc", "negyven", "ötven", "hatvan", "hetven", "nyolcvan", "kilencven"};
        String[] tizes2 = {"", "tíz", "húsz", "harminc", "negyven", "ötven", "hatvan", "hetven", "nyolcvan", "kilencven"};

        sz = osszeg / 100;
        t = osszeg % 100 / 10;
        e = osszeg % 10;

        if (sz != 0) {
            result += egyes[sz] + "száz";
        }
        if (t != 0 && e != 0) {
            result += tizes1[t] + egyes[e];
        } else if (t != 0 && e == 0) {
            result += tizes2[t];
        } else if (t == 0 && e != 0) {
            result += egyes[e];
        }

        return result;
    }

    public static String dateFormat(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static String dateFormat(Date date) {
        return dateFormat(date, "yyyy-MM-dd");
    }

    public static String now() {
        return dateFormat(new Date());
    }

    public static String numberFormat(String num) {
        String result = "";
        double szam = Double.parseDouble(num);
        int elojel = 1;
        if (szam < 0) {
            elojel = -1;
            szam = Math.abs(szam);
        }
        NumberFormat formatter;
        formatter = new DecimalFormat("#,###.0000");
        result = formatter.format(szam);
        result = (elojel < 0 ? "-" : "") + (Math.floor(szam) == 0 ? "0" : "") + formatter.format(szam);
        return result;
    }

    public static double kerekit(double number, boolean isUtalas) {
        double result = number;

        if (!isUtalas) {
            int endOfNumber = (int) (Math.abs(number) % 10);
            switch (endOfNumber) {
                case 1:
                case 6: {
                    result = number - 1;
                    break;
                }
                case 2:
                case 7: {
                    result = number - 2;
                    break;
                }
                case 3:
                case 8: {
                    result = number + 2;
                    break;
                }
                case 4:
                case 9: {
                    result = number + 1;
                    break;
                }
                case 0:
                case 5: {
                    result = number;
                    break;
                }
                default:
                    result = number;
                    break;
            }
        }
//        System.out.println("number: " + number);
        return result;
    }

    public static String formatFieldName(String columnName) {
        String[] parts = columnName.split("_");

        String formatedField = parts[0];
        for (int i = 1; i < parts.length; i++) {
            parts[i] = parts[i].substring(0, 1).toUpperCase() + parts[i].substring(1);
            formatedField += parts[i];
        }

        return formatedField;
    }

    public static String md5(String input) throws NoSuchAlgorithmException {
        String result = input;
        if (input != null) {
            MessageDigest md = MessageDigest.getInstance("MD5"); //or "SHA-1"
            md.update(input.getBytes());
            BigInteger hash = new BigInteger(1, md.digest());
            result = hash.toString(16);
            while (result.length() < 32) { //40 for SHA-1
                result = "0" + result;
            }
        }
        return result;
    }

    public static String getStringFromObject(Object object) {
        try {
            return String.valueOf(object);
        } catch (Exception e) {
            return "";
        }

    }

    public static int getIntFromObject(Object object) {
        try {
            return Integer.valueOf(String.valueOf(object));
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
            return -1; 
        } catch (java.lang.NumberFormatException e){
            e.printStackTrace();
            return -1;
        }

    }

    /**
     * Szám formázása ezres tagolással, ha pedig valós akkor két tizedesjegyig.
     *
     * @param num a formázandó numerikus érték String-ként
     * @param tizedes a formázás egész vagy valós legyen (két tizedesig)
     * @return a formázott
     */
    public static String numberFormat(String num, boolean tizedes)
    {
        String result = "";
        double szam = 0;
        try {
            szam = Double.parseDouble(num.replace(",", "."));
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }
        NumberFormat formatter;
        if (tizedes) {
            if (szam - Math.floor(szam) != 0) {
                formatter = new DecimalFormat("#,###.00");
            } else {
                formatter = new DecimalFormat("#,###");
            }
        } else {
            formatter = new DecimalFormat("#,###");
        }
        if (szam == 0) {
            result = "0";
        } else {
            result = formatter.format(szam);
        }
        return (result.startsWith(",") ? "0" + result : result);
    }
    
    public static int GetLastProduct(String originalInvoiceNumber)
    {
        int lastProduct = 0;
        Query query = new Query.QueryBuilder()
                .select("id")
                .from("szamlazo_szamla_adatok")
                .where("szamla_sorszam LIKE '" + originalInvoiceNumber + "'")
                .build();
        Object [][] original = App.db.select(query.getQuery());
        
        for(int i = 0; i < original.length; i++)
        {      
            lastProduct++;
        }
        System.err.println("Az eredeti számla " + lastProduct + " termékből áll.");
        
        query = new Query.QueryBuilder()
                .select("id, szamla_sorszam")
                .from("szamlazo_szamla")
                .where("helyesbitett LIKE '" + originalInvoiceNumber + "'")
                .order("id ASC")
                .build();
        Object [][] modify = App.db.select(query.getQuery());
        
        for(Object [] obj: modify)
        {
            query = new Query.QueryBuilder()
                .select("id, modifiedproductID")
                .from("szamlazo_szamla_adatok")
                .where("szamla_sorszam LIKE '" + obj[1] + "'")
                .order("id ASC")
                .build();
            Object [][] select = App.db.select(query.getQuery());
            
            for(Object [] obj2 : select)
            {
                if(obj2[1].equals("0"))
                {
                    lastProduct++;
                }                
            }
        }
        
        System.err.println("A módosító számlával " + lastProduct + " termék lett.");
          
        if(lastProduct == 0)
        {
            lastProduct++;
        }
        
        return lastProduct;
    }
}
