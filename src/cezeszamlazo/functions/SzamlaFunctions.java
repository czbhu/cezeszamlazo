/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeszamlazo.functions;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 *
 * @author szekus
 */
public class SzamlaFunctions {

    public static String numberFormat(String num, boolean tizedes) {
        String result = "";
        double szam = Double.parseDouble(num);
        int elojel = 1;
        if (szam < 0) {
            elojel = -1;
            szam = Math.abs(szam);
        }
        NumberFormat formatter;
        if (tizedes) {
            formatter = new DecimalFormat("#,###.00");
            result = (elojel < 0 ? "-" : "") + (Math.floor(szam) == 0 ? "0" : "") + formatter.format(szam);
        } else {
            formatter = new DecimalFormat("#,###");
            result = (elojel < 0 ? "-" : "") + formatter.format(szam);
        }
        return result;
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

    public static String dateFormat(String date) {
        String[] temp = date.split("-");
        return temp[0] + "." + temp[1] + "." + temp[2] + ".";
    }

    public static String dateFormatEn(String date) {
        String[] temp = date.split("-");
        return temp[2] + "-" + temp[1] + "-" + temp[0];
    }

    public static String azaz(int osszeg) {
        String result = "";
        int sz, t, e;
        String[] egyes = {"", "egy", "kettő", "három", "négy", "öt", "hat", "hét", "nyolc", "kilenc"};
        String[] tizes1 = {"", "tizen", "huszon", "harminc", "negyven", "ötven", "hatvan", "hetven", "nyolcvan", "kilencven"};
        String[] tizes2 = {"", "tíz", "húsz", "harminc", "negyven", "ötven", "hatvan", "hetven", "nyolcvan", "kilencven"};

        sz = osszeg / 100;
        t = osszeg % 100 / 10;
        e = osszeg % 10;

        if (sz < 0) {
            sz = sz * -1;
        }
        if (t < 0) {
            t = t * -1;
        }
        if (e < 0) {
            e = e * -1;
        }

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
    
    public static String betuvel(double osszeg) {
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
            result += SzamlaFunctions.azaz(milliardos) + "millárd";
            if (millios != 0 || szazezres != 0 || ezres != 0) {
                result += "-";
            }
        }
        if (millios != 0) {
            result += SzamlaFunctions.azaz(millios) + "millió";
            if (szazezres != 0 || ezres != 0) {
                result += "-";
            }
        }
        if (szazezres != 0) {
            result += SzamlaFunctions.azaz(szazezres) + "ezer";
            if (ezres != 0 && osszeg > 2000) {
                result += "-";
            }
        }
        if (ezres != 0) {
            result += SzamlaFunctions.azaz(ezres);
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
    
     public static String csakszam(String text, int size, boolean tizedes) {
        String valid = "+-0123456789";
        if (tizedes) {
            valid += ".";
        }
        text = text.replace(",", ".");
        String result = "";
        for (int i = 0; i < text.length(); i++) {
            if (valid.contains(text.substring(i, i + 1))) {
                result += text.substring(i, i + 1);
            }
        }
        if (size != 0) {
            if (result.length() > size) {
                result = result.substring(0, size);
            }
        }
        return result;
    }

}
