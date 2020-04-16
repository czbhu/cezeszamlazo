package cezeszamlazo.views;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 * @author Tomy
 */

public class SelectedContactsView extends javax.swing.JDialog
{
    private int x = 0;
    private int y = 0;
    
    private TableRowSorter<TableModel> sorter;
    private ContactsView contactsView = null;
    private CustomersView customersView = null;
    
    public SelectedContactsView(ContactsView contactsView)
    {
        initComponents();
        this.contactsView = contactsView; 
    }
    
    public SelectedContactsView(CustomersView customersView)
    {
        initComponents();
        this.customersView = customersView; 
    }
    
    public void Open()
    {
        if(x == 0 && y == 0)
        {
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Dimension screenSize = toolkit.getScreenSize();
            x = (screenSize.width - getWidth()) / 2;
            y = (screenSize.height - getHeight()) / 2;
            setTitle("Ügyfelek");
        }
        else
        {
            setTitle("Kiválasztott ügyfelek");
        }
        
        setLocation(x, y);
        setVisible(true);
    }
    
    public void Fill(Map <Integer, String> data)
    {
        DefaultTableModel DTModel = (DefaultTableModel) table_SelectedContacts.getModel();
        
        DTModel.setRowCount(0);
        
        for(int i: data.keySet())
        {
            DTModel.addRow(new Object [] {i, data.get(i)});
        }
    }
    
    public void SortTable(String regex)
    {
        sorter = new TableRowSorter<>(((DefaultTableModel) table_SelectedContacts.getModel()));
        sorter.setRowFilter(RowFilter.regexFilter("^(Nothing matches to this pattern)$"));
        table_SelectedContacts.setRowSorter(sorter);
        
        sorter.setRowFilter(RowFilter.regexFilter(regex));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrollPane_SelectedContacts = new javax.swing.JScrollPane();
        table_SelectedContacts = new javax.swing.JTable();
        button_SelectSelected = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setName("Form"); // NOI18N

        scrollPane_SelectedContacts.setName("scrollPane_SelectedContacts"); // NOI18N

        table_SelectedContacts.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Név"
            }
        ));
        table_SelectedContacts.setName("table_SelectedContacts"); // NOI18N
        scrollPane_SelectedContacts.setViewportView(table_SelectedContacts);

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(cezeszamlazo.App.class).getContext().getResourceMap(SelectedContactsView.class);
        button_SelectSelected.setText(resourceMap.getString("button_SelectSelected.text")); // NOI18N
        button_SelectSelected.setName("button_SelectSelected"); // NOI18N
        button_SelectSelected.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_SelectSelectedActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollPane_SelectedContacts, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .addComponent(button_SelectSelected, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollPane_SelectedContacts, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(button_SelectSelected)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void button_SelectSelectedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_SelectSelectedActionPerformed
        ArrayList<Integer> ids = new ArrayList<>();
    	
    	for(int i = 0; i< table_SelectedContacts.getRowCount(); i++)
        {
            ids.add(Integer.parseInt(String.valueOf(table_SelectedContacts.getValueAt(i, 0))));
    	}

        if(customersView != null)
        {
            ((CustomersView) customersView).SetSelected(ids);
        }
        
        if(contactsView != null)
        {
            ((ContactsView) contactsView).SetSelected(ids);
        }
    }//GEN-LAST:event_button_SelectSelectedActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton button_SelectSelected;
    private javax.swing.JScrollPane scrollPane_SelectedContacts;
    private javax.swing.JTable table_SelectedContacts;
    // End of variables declaration//GEN-END:variables

    //SETTERS
    public void setX(int x)
    {
        this.x = x;
    }

    public void setY(int y)
    {
        this.y = y;
    }
}