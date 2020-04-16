package customs;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JComboBox;

/**
 * @author Tomy
 */

public class CustomCombobox extends JComboBox
{
    public CustomCombobox()
    {
        super();
        
        try
        {
            setPrototypeDisplayValue("xxxx");
            setRenderer(new Colorer());
            setBorder(null);
            Dimension dim = new Dimension(20, 20);
            setPreferredSize(dim);
            setBackground(new Color(0, 43, 64));
            setForeground(Color.WHITE);
        }
        catch (Exception e)
        {
            System.err.print(e);
        }
    }
}