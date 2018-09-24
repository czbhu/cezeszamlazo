package cezeszamlazo;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

class CalendarTableRender extends JLabel implements TableCellRenderer {
    
    public CalendarTableRender() {
        setOpaque(true);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        
        if (isSelected) {
            setBackground(Color.decode("#6ccded"));
            setForeground(Color.white);
        } else {
            if (row % 2 == 1) {
                setBackground(Color.decode("#F5F5F5"));
                setForeground(Color.black);
            } else {
                setBackground(Color.white);
                setForeground(Color.black);
            }
        }
	setFont(table.getFont());
	setHorizontalAlignment(CENTER);
	setText(String.valueOf(value));
        return this;
    }
    
}