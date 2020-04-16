package functions;

/**
 * @author Tomy
 */
public class NumericFunctions
{
    public static double Format(double number, int exponent)
    {
        int whole = (int)Math.round(number * Math.pow(10, exponent));
        
        double result = whole / Math.pow(10.0, exponent);
        
        return result;      
    }
    
    public static int Round(double value)
    {
        return (int) Math.round(value);
    }
}