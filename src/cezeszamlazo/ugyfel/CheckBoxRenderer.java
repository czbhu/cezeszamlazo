package cezeszamlazo.ugyfel;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class CheckBoxRenderer extends JCheckBox implements TableCellRenderer
{
    public CheckBoxRenderer()
    {
        setHorizontalAlignment(JLabel.CENTER);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        if (isSelected)
        {
            setBackground(Color.decode("#3399FF"));
            setForeground(Color.white);
        }
        else
        {
            if (row % 2 == 1)
            {
                setBackground(Color.decode("#F5F5F5"));
                setForeground(Color.black);
            }
            else
            {
                setBackground(Color.white);
                setForeground(Color.black);
            }
        }
        
        //System.out.println("value: "+ value);

        setSelected(String.valueOf(value).equals("true"));

        //System.out.println(row);
        //System.out.println(String.valueOf(value));
        //setSelected(String.valueOf(value).equals("1"));
        //System.out.println(isSelected);
        
        return this;
    }
}