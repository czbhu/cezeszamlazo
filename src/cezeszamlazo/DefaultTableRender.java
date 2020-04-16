package cezeszamlazo;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

class DefaultTableRender extends JLabel implements TableCellRenderer
{
    private int[] cols = {};
    private String valuta = "Ft";

    public DefaultTableRender()
    {
        setOpaque(true);
    }
    
    public DefaultTableRender(int[] cols)
    {
        this.cols = cols;
	setOpaque(true);
    }
    
    public DefaultTableRender(int[] cols, String valuta)
    {
        this.cols = cols;
	this.valuta = valuta;
	setOpaque(true);
    }

    @Override
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
        
	setFont(table.getFont());
        
	if (cols.length != 0)
        {
	    for (int i = 0; i < cols.length; i++)
            {
		if (column == cols[i])
                {
		    setHorizontalAlignment(RIGHT);
		    setText(EncodeDecode.numberFormat(String.valueOf(value), !(valuta.equalsIgnoreCase("Ft") || valuta.equalsIgnoreCase("huf"))) + " " + valuta);
		    break;
		}
                else
                {
		    setHorizontalAlignment(LEFT);
		    setText(String.valueOf(value));
		}
	    }
	}
        else
        {
	    setHorizontalAlignment(LEFT);
	    setText(String.valueOf(value));
	}
        
        return this;
    }  
}