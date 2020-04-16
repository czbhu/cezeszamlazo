package cezeszamlazo.functions;

/**
 * @author Tomy
 */
public class KeyPressed
{
    public static volatile boolean pressed = false;
    
    public static boolean Pressed()
    {
        synchronized (KeyPressed.class)
        {
            return pressed;
        }
    }
}