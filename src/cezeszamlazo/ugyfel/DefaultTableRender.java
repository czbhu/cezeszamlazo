package cezeszamlazo.ugyfel;

import cezeszamlazo.controller.Functions;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * Általános táblázat formázására hosznált osztály.
 *
 * @author Fejlesztes
 */
public class DefaultTableRender extends JLabel implements TableCellRenderer
{
    protected boolean showColor = false;
    protected boolean userTable = false;

    /**
     * azon oszopok azonosítói melyeket számformátummá kell alakítani
     */
    private int[] cols = {};
    /**
     * az oszlopokhoz tartozó címkék ("Ft", "db", "", stb..)
     */
    private String[] colLabels = {};

    /**
     * alap konstruktor
     */
    public DefaultTableRender()
    {
        setOpaque(true);
    }

    /**
     * bővített konstruktor ha van formázandó oszlop
     *
     * @param cols a formázandó oszlopok azonosítói
     * @param colLabels a az oszlopokhoz tartozó címkék
     */
    public DefaultTableRender(int[] cols, String[] colLabels)
    {
        this.cols = cols;
        this.colLabels = colLabels;
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        if (isSelected)
        {
            // ha a sor kijelölt akkor kiemelt színt kap
            setBackground(Color.decode("#3399FF"));
            setForeground(Color.white);
        }
        else
        {
            if (this.isShowColor())
            {
                if (column == 0)
                {
                    //int ajanlatkeresId = Integer.parseInt(String.valueOf(table.getValueAt(row, column)));
                    //String color = PixiRendszer.getColorByAjalkeresId(ajanlatkeresId);
                    String color = "";
                    
                    if(color.isEmpty())
                    {
                        if(row % 2 == 1)
                        {
                            setBackground(Color.decode("#F5F5F5"));
                        }
                        else
                        {
                            setBackground(Color.white);
                        }
                    }
                    else
                    {
                        setBackground(Color.decode(color));
                    }
                }
            }
            else if(isUserTable())
            {
                if (column == 0)
                {
                    //int userId = Integer.parseInt(String.valueOf(table.getValueAt(row, column)));
                    //String color = PixiRendszer.getColorByUserId(userId);
                    String color = "";
                    
                    if (color.isEmpty())
                    {
                        if (row % 2 == 1)
                        {
                            setBackground(Color.decode("#F5F5F5"));
                        }
                        else
                        {
                            setBackground(Color.white);
                        }
                    }
                    else
                    {
                        setBackground(Color.decode(color));
                    }
                }
            }
            else
            {
                //egyébként felváltva soronként eltérő szín
                if (row % 2 == 1)
                {
                    setBackground(Color.decode("#F5F5F5"));
                }
                else
                {
                    setBackground(Color.white);
                }
            }

            // a betűszín egységesen fekete
            setForeground(Color.black);
        }
        
        // font beállítása
        setFont(table.getFont());
        
        if (cols.length != 0)
        {
            // ha vannak formázandó oszlopok
            for (int i = 0; i < cols.length; i++)
            {
                if (column == cols[i])
                {
                    // ha az aktuális oszlop pont formázandó
                    // akkor jobbra igazítjuk és 
                    setHorizontalAlignment(RIGHT);
                    setText(Functions.numberFormat(String.valueOf(value), true) + (colLabels[i].isEmpty() ? "" : " " + colLabels[i]));
                    break;
                }
                else
                {
                    // egyébként balra igazítva beállítjuk a szöveget
                    setHorizontalAlignment(LEFT);
                    setText(String.valueOf(value));
                }
            }
        }
        else
        {
            // egyébként balra igazítva beállítjuk a szöveget
            setHorizontalAlignment(LEFT);
            setText(String.valueOf(value));
        }
        
        return this;
    }

    public void setShowColor(boolean showColor) {
        this.showColor = showColor;
    }

    public boolean isShowColor() {
        return showColor;
    }

    public void setUserTable(boolean userTable) {
        this.userTable = userTable;
    }

    public boolean isUserTable() {
        return userTable;
    }
}