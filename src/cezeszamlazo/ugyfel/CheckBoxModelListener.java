/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeszamlazo.ugyfel;

import cezeszamlazo.App;
import cezeszamlazo.controller.Functions;
import cezeszamlazo.database.Query;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 *
 * @author szekus
 */
public class CheckBoxModelListener implements TableModelListener {

    UgyfelekFrame ugyfelekFrame = null;
    KapcsolattartokFrame kapcsolattartokFrame = null;

    public CheckBoxModelListener(UgyfelekFrame ugyfelekFrame) {
        this.ugyfelekFrame = ugyfelekFrame;
    }

    public CheckBoxModelListener(KapcsolattartokFrame kapcsolattartokFrame) {
        this.kapcsolattartokFrame = kapcsolattartokFrame;
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        int row = e.getFirstRow();
        int column = e.getColumn();
        if (column == 5) {
            
            TableModel model = (TableModel) e.getSource();
            String columnName = model.getColumnName(column);
            Boolean checked = (Boolean) model.getValueAt(row, column);

            System.out.println(model.getValueAt(row, 0));

            Query query = new Query.QueryBuilder()
                    .select("penzugyes")
                    .from("pixi_kapcsolattarto")
                    .where("id = " + Functions.getIntFromObject(model.getValueAt(row, 0)))
                    .order("")
                    .build();
            Object[][] select = App.db.select(query.getQuery());

            String value = Functions.getStringFromObject(select[0][0]);
            String penzugyes = "1";
            if (value.equals("1")) {
                penzugyes = "0";
            } else {
                penzugyes = "1";
            }

            String updateQuery = "UPDATE pixi_kapcsolattarto SET penzugyes = " + penzugyes + " WHERE id = ?";
            Object[] object = new Object[1];
            object[0] = Functions.getIntFromObject(model.getValueAt(row, 0));
            App.db.insert(updateQuery, object, 1);

            if (ugyfelekFrame != null) {
                ugyfelekFrame.frissites();
            }
            if (kapcsolattartokFrame != null) {
                kapcsolattartokFrame.frissites();
            }
        }
    }

}
