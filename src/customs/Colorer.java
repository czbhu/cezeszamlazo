package customs;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.border.MatteBorder;

/**
 * @author Tomy
 */
public class Colorer<String> extends JLabel implements ListCellRenderer
{
    public Colorer()
    {
        super();
        setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(JList jlist, Object e, int i, boolean isSelected, boolean bln1)
    {
        setBorder(new MatteBorder(0, 1, 0, 0, new Color(0, 43, 64)));
        setText(e.toString());
        Dimension dim = new Dimension(20, 20);
        setPreferredSize(dim);
        setBackground(new Color(0, 43, 64));

        if (isSelected)
        {
            setBackground(new Color(0, 87, 124));
            setForeground(Color.WHITE);
        }

        return this;
    }
}
