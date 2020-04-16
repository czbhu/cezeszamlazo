package cezeszamlazo.functions;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 *
 * @author szekus
 */
public class ElonezetFunctions
{
    public ElonezetFunctions()
    {}

    public String numberFormat(String num, boolean tizedes)
    {
        String result = "";
        double szam = Double.parseDouble(num);
        int elojel = 1;
        NumberFormat formatter;
        
        if (szam < 0)
        {
            elojel = -1;
            szam = Math.abs(szam);
        }
        
        if (tizedes)
        {
            formatter = new DecimalFormat("#,###.00");
            result = (elojel < 0 ? "-" : "") + (Math.floor(szam) == 0 ? "0" : "") + formatter.format(szam);
        }
        else
        {
            formatter = new DecimalFormat("#,###");
            result = (elojel < 0 ? "-" : "") + formatter.format(szam);
        }
        
        return result;
    }
}
