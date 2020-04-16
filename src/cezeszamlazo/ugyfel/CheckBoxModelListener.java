package cezeszamlazo.ugyfel;

import cezeszamlazo.App;
import cezeszamlazo.controller.Functions;
import cezeszamlazo.database.Query;
import cezeszamlazo.views.ContactsView;
import cezeszamlazo.views.CustomersView;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 * @author szekus,Tomy
 */
public class CheckBoxModelListener implements TableModelListener
{
    CustomersView customersView = null;
    ContactsView contactsView = null;

    public CheckBoxModelListener(CustomersView customersView)
    {
        this.customersView = customersView;
    }

    public CheckBoxModelListener(ContactsView view)
    {
        this.contactsView = view;
    }

    @Override
    public void tableChanged(TableModelEvent e)
    {
        int row = e.getFirstRow();
        int column = e.getColumn();
        
        if (column == 5)
        {
            TableModel model = (TableModel) e.getSource();
            String columnName = model.getColumnName(column);
            Boolean checked = (Boolean) model.getValueAt(row, column);

            System.out.println(model.getValueAt(row, 0) + " (cezeszamlazo.ugyfel/CheckBoxModelListener.java/tableChanged(TableModelEvent e))");

            Query query = new Query.QueryBuilder()
                .select("finance")
                .from("szamlazo_contact")
                .where("id = " + Functions.getIntFromObject(model.getValueAt(row, 0)))
                .build();
            Object[][] select = App.db.select(query.getQuery());

            String value = Functions.getStringFromObject(select[0][0]);
            String penzugyes;
            
            if (value.equals("1"))
            {
                penzugyes = "0";
            }
            else
            {
                penzugyes = "1";
            }

            String updateQuery = "UPDATE szamlazo_contact SET finance = " + penzugyes + " WHERE id = ?";
            Object[] object = new Object[1];
            object[0] = Functions.getIntFromObject(model.getValueAt(row, 0));
            App.db.insert(updateQuery, object);

            if (customersView != null)
            {
                customersView.UpdateCustomersTable();
            }
            
            if (contactsView != null)
            {
                contactsView.UpdateContactsTable();
            }
        }
    }
}