package cezeszamlazo;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class EncodeDecode
{
    public static boolean on = true;

    public static String encode(String text)
    {
        if (!EncodeDecode.on)
        {
            return text;
        }
        
        if (EncodeDecode.on == false)
        {
            return text;
        }
        
        /*text = text.replace("Á", "&Aacute;");
        text = text.replace("á", "&aacute;");
        text = text.replace("É", "&Eacute;");
        text = text.replace("é", "&eacute;");
        text = text.replace("Í", "&Iacute;");
        text = text.replace("í", "&iacute;");
        text = text.replace("Ó", "&Oacute;");
        text = text.replace("ó", "&oacute;");
        text = text.replace("Ö", "&Ouml;");
        text = text.replace("ö", "&ouml;");
        text = text.replace("Ő", "&#336");
        text = text.replace("ő", "&#337");
        text = text.replace("Ú", "&Uacute;");
        text = text.replace("ú", "&uacute;");
        text = text.replace("Ü", "&Uuml;");
        text = text.replace("ü", "&uuml;");
        text = text.replace("Ű", "&#368");
        text = text.replace("ű", "&#369");*/
        
        return text;
    }

    public static String decode(String text)
    {
        if (text == null)
        {
            return "";
        }
        
        if (EncodeDecode.on == false)
        {
            return text;
        }
        
        text = text.replace("&Aacute;", "Á");
        text = text.replace("&aacute;", "á");
        text = text.replace("&Eacute;", "É");
        text = text.replace("&eacute;", "é");
        text = text.replace("&Iacute;", "Í");
        text = text.replace("&iacute;", "í");
        text = text.replace("&Oacute;", "Ó");
        text = text.replace("&oacute;", "ó");
        text = text.replace("&Ouml;", "Ö");
        text = text.replace("&ouml;", "ö");
        text = text.replace("&#336", "Ő");
        text = text.replace("&#337", "ő");
        text = text.replace("&Uacute;", "Ú");
        text = text.replace("&uacute;", "ú");
        text = text.replace("&Uuml;", "Ü");
        text = text.replace("&uuml;", "ü");
        text = text.replace("&#368", "Ű");
        text = text.replace("&#369", "ű");
        
        return text;
    }

    public static String numberFormat(String num, boolean tizedes)
    {
        String result = "";
        double szam = Double.parseDouble(num);
        NumberFormat formatter;
        
        if (tizedes)
        {
            if (szam - Math.floor(szam) != 0)
            {
                formatter = new DecimalFormat("#,##0.00");
            }
            else
            {
                formatter = new DecimalFormat("#,###.00");
            }
        }
        else
        {
            formatter = new DecimalFormat("#,###");
        }
        
        if (szam == 0)
        {
            result = "0";
        }
        else
        {
            result = formatter.format(szam);
        }
        
        return result;
    }
    
    public static String numberFormat(double num, boolean tizedes, int paymentMethod)
    {
        String result;
        double szam = num;
        NumberFormat formatter;
        
        if (tizedes)
        {
            if (szam - Math.floor(szam) != 0)
            {
                formatter = new DecimalFormat("#,##0.00");
            }
            else
            {
                formatter = new DecimalFormat("#,###.00");
            }
        }
        else
        {
            formatter = new DecimalFormat("#,###");
        }
        
        if (szam == 0)
        {
            result = "0";
        }
        else
        {
            if(paymentMethod != 1)
            {
                szam = Round(szam, tizedes, paymentMethod);
            }
            
            result = formatter.format(szam);
        }
        
        result = result.replace(",", ".");
        
        return result;
    }
    
    private static double Round(double value, boolean foreignCurrency, int paymentMethod)
    {
        double result;
        
        if(foreignCurrency)
        {
            result = ((int)Math.round(value * 100)) / 100.0;
        }
        else
        {
            if (paymentMethod != 1)
            {
                value = (int)Math.round(value);
                int endOfNumber = (int) (Math.abs(value) % 10);

                switch (endOfNumber)
                {
                    case 1:
                    case 6:
                    {
                        value = value - 1;
                        break;
                    }
                    case 2:
                    case 7:
                    {
                        value = value - 2;
                        break;
                    }
                    case 3:
                    case 8:
                    {
                        value = value + 2;
                        break;
                    }
                    case 4:
                    case 9:
                    {
                        value = value + 1;
                        break;
                    }
                    case 0:
                    case 5:
                    {
                        result = value;
                        break;
                    }
                    default:
                        value = value;
                        break;
                }
            }
            result = value;
        }
        
        return result;
    }

    public static String csakszam(String text, int size, boolean tizedes)
    {
        String valid = "0123456789";
        
        if (tizedes)
        {
            valid += ".";
        }
        
        text = text.replace(",", ".");
        String result = "";
        
        for (int i = 0; i < text.length(); i++)
        {
            if (valid.contains(text.substring(i, i + 1)))
            {
                result += text.substring(i, i + 1);
            }
        }
        
        if (size != 0)
        {
            if (result.length() > size)
            {
                result = result.substring(0, size);
            }
        }
        
        return result;
    }
}