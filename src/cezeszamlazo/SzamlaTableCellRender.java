package cezeszamlazo;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class SzamlaTableCellRender extends JLabel implements TableCellRenderer {

    public SzamlaTableCellRender() {
        setOpaque(true);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        String penznem = "";

        if (String.valueOf(table.getValueAt(row, 16)).matches("huf")) {
            penznem = "Ft";
        } else if (String.valueOf(table.getValueAt(row, 16)).matches("eur")) {
            penznem = "€";
        } else if (String.valueOf(table.getValueAt(row, 16)).matches("usd")) {
            penznem = "$";
        }

        if (isSelected) {
            setBackground(Color.decode("#3399FF"));
            setForeground(Color.white);
        } else {

            if (row % 2 == 1) {
                setBackground(Color.decode("#F5F5F5"));
                setForeground(Color.black);
            } else {
                setBackground(Color.white);
                setForeground(Color.black);
            }
            if (String.valueOf(table.getValueAt(row, 14)).matches("2") || String.valueOf(table.getValueAt(row, 14)).matches("3")) {
                setBackground(Color.decode("#F79D9D"));
                setForeground(Color.decode("#B30000"));
            }
            if (String.valueOf(table.getValueAt(row, 14)).matches("0")) {
                setBackground(Color.decode("#D3ADDA"));
                setForeground(Color.decode("#532652"));
            }
            if (String.valueOf(table.getValueAt(row, 14)).matches("0")) {
                if (String.valueOf(table.getValueAt(row, 13)).matches("0")) {
//                    setBackground(Color.YELLOW);
//                    setForeground(Color.BLACK);
                } else if (String.valueOf(table.getValueAt(row, 13)).matches("1")) {
                    setBackground(Color.decode("#CECEF6"));
                    setForeground(Color.BLACK);
                } else if (String.valueOf(table.getValueAt(row, 13)).matches("2")) {
                    setBackground(Color.YELLOW);
                    setForeground(Color.BLACK);
                }

            }
        }
        setFont(table.getFont());
        if (column == 14) {
            switch (Integer.parseInt(String.valueOf(value))) {
                case 0:
                    setText("Kintlévőség");
                    break;
                case 1:
                    setText("Normál");
                    break;
                case 2:
                    setText("Helyesbítő, stornó");
                    break;
                case 3:
                    setText("Helyesbített, stornírozott");
                    break;
            }
            setHorizontalAlignment(LEFT);
        } else if ((column >= 3 && column <= 7) || column == 12) {
            setText(EncodeDecode.numberFormat(String.valueOf(value), (penznem.matches("Ft") ? false : true)) + " " + penznem);
            setHorizontalAlignment(RIGHT);
        } else if (column == 0 || column == 11) {
            setText(String.valueOf(value));
            setHorizontalAlignment(RIGHT);
        } else if (column == 13) {
            switch (Integer.parseInt(String.valueOf(value))) {
                case 0:
                    setText("Készpénz");
                    break;
                case 1:
                    setText("Átutalás");
                    break;
                case 2:
                    setText("Utánvét");
                    break;
            }
            setHorizontalAlignment(LEFT);
        } else if (String.valueOf(value).length() == 10 && String.valueOf(value).split("-").length == 3) {
            setText(String.valueOf(value));
            setHorizontalAlignment(CENTER);
        } else {
            setText(String.valueOf(value));
            setHorizontalAlignment(LEFT);
        }
        return this;
    }
}
