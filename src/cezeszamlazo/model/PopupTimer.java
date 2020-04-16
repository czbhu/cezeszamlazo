package cezeszamlazo.model;

/**
 * @author Tomy
 */
public class PopupTimer
{
    long start = 0;
    long end = 0;
    long delay = 0;
    
    public PopupTimer()
    {
    
    }
    
    public void Start()
    {
        start = System.currentTimeMillis();
    }
    
    public boolean Stop()
    {
        end = System.currentTimeMillis();
        
        Calculate();
        
        return (delay >= 1500 && start != 0);
    }
    
    private void Calculate()
    {
        delay = end - start;
    }

    //SETTERS
    public void setStart(long start)
    {
        this.start = start;
    }
}
