package cezeszamlazo.views;

import cezeszamlazo.HibaDialog;
import cezeszamlazo.model.PopupTimer;
import cezeszamlazo.ugyfel.CheckBoxRenderer;
import cezeszamlazo.ugyfel.DefaultTableRender;
import invoice.Customer;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 * @author Tomy
 */

public class CustomersView extends javax.swing.JFrame
{
    private SelectedContactsView selectedCustomersView = null;
    
    private PopupTimer popupTimer;
    
    public CustomersView()
    {
        initComponents();
        
        Init();
    }
    
    private void Init()
    {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;
        
        setLocation(x, y);
        setTitle("Ügyfelek");
    }
    
    public void UpdateCustomersTable()
    {
        DefaultTableModel DTModel = (DefaultTableModel) table_Customers.getModel();
        
        String search = textField_Search.getText();
        
        Object [][] help = Customer.getCustomersObject(search);
        Object [][] customers = new Object[0][0];
        
        if(help.length > 0)
        {
            customers = new Object[help.length][help[0].length + 1];
            for(int i = 0; i < customers.length; i++)
            {
                customers[i][0] = help[i][0];
                customers[i][1] = help[i][1];
                customers[i][2] = help[i][2];
                customers[i][3] = help[i][3];
                customers[i][4] = help[i][4];
                customers[i][5] = false;
            }
        }
        
        String[] header = {"Id", "Név", "Cím", "Adószám", "Bankszámlaszám", "Kiválasztott"};
        
        DTModel.setDataVector(customers, header);
        
        DefaultTableRender render = new DefaultTableRender();
        TableColumn col;
        CheckBoxRenderer checkBoxRenderer = new CheckBoxRenderer();
        int[] meret = {30, 200, 150, 100, 100};
        
        for (int i = 0; i < meret.length; i++)
        {
            col = table_Customers.getColumnModel().getColumn(i);
            col.setPreferredWidth(meret[i]);
            col.setCellRenderer(render);
        }
        
        col = table_Customers.getColumnModel().getColumn(5);
        col.setCellRenderer(checkBoxRenderer);
    }
    
    private void OpenCustomerView(int row)
    {
        int customerID = 0;
        
        if(row >= 1)
        {
            customerID = Integer.parseInt(table_Customers.getValueAt(row, 0).toString());
        }
        
        CustomerView customerView = new CustomerView(new Customer(customerID));
        customerView.Open(true);
        
        if(customerView.getReturnStatus() == CustomerView.RET_OK)
        {
            UpdateCustomersTable();
        }
    }
    
    public void Open()
    {
        UpdateCustomersTable();
        setVisible(true);
    }
    
    public void SetSelected(ArrayList <Integer> ids)
    {
        table_Customers.clearSelection();
        
        for (int i = 0; i < table_Customers.getRowCount(); i++)
        {
            if (ids.contains(Integer.valueOf(String.valueOf(table_Customers.getValueAt(i, 0)))))
            {
                table_Customers.addRowSelectionInterval(i, i);
            }
        }
    }
    
    private Map<Integer, String> ProduceData()
    {
        Map<Integer, String> data = new HashMap<>();

        for (int i = 0; i < table_Customers.getRowCount(); i++)
        {
            int id = Integer.parseInt(table_Customers.getValueAt(i, 0).toString());
            String name = table_Customers.getValueAt(i, 1).toString();
            data.put(id, name);
        }

        return data;
    }
    
    private String GenerateRegex()
    {
        String regex = "";

        regex += "^(";

        for (int i = 0; i < table_Customers.getRowCount(); i++)
        {
            if ((boolean) table_Customers.getValueAt(i, 5))
            {
                regex += String.valueOf(table_Customers.getValueAt(i, 0)) + "|";
            }
        }

        regex = regex.substring(0, regex.length() - 1);
        regex += ")$";

        if (regex.equals("^)$"))
        {
            return "^(Nothing matches to this pattern)$";
        }

        return regex;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PopupMenu_Customers = new javax.swing.JPopupMenu();
        menuItem_Open = new javax.swing.JMenuItem();
        menuItem_Grouping = new javax.swing.JMenuItem();
        Separator = new javax.swing.JPopupMenu.Separator();
        menuItem_Delete = new javax.swing.JMenuItem();
        textField_Search = new javax.swing.JTextField();
        button_NewCustomer = new javax.swing.JButton();
        button_SelectedCustomers = new javax.swing.JButton();
        scrollPane_Customers = new javax.swing.JScrollPane();
        table_Customers = new javax.swing.JTable();

        PopupMenu_Customers.setName("PopupMenu_Customers"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(cezeszamlazo.App.class).getContext().getResourceMap(CustomersView.class);
        menuItem_Open.setText(resourceMap.getString("menuItem_Open.text")); // NOI18N
        menuItem_Open.setName("menuItem_Open"); // NOI18N
        menuItem_Open.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItem_OpenActionPerformed(evt);
            }
        });
        PopupMenu_Customers.add(menuItem_Open);

        menuItem_Grouping.setText(resourceMap.getString("menuItem_Grouping.text")); // NOI18N
        menuItem_Grouping.setName("menuItem_Grouping"); // NOI18N
        menuItem_Grouping.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItem_GroupingActionPerformed(evt);
            }
        });
        PopupMenu_Customers.add(menuItem_Grouping);

        Separator.setName("Separator"); // NOI18N
        PopupMenu_Customers.add(Separator);

        menuItem_Delete.setText(resourceMap.getString("menuItem_Delete.text")); // NOI18N
        menuItem_Delete.setName("menuItem_Delete"); // NOI18N
        menuItem_Delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItem_DeleteActionPerformed(evt);
            }
        });
        PopupMenu_Customers.add(menuItem_Delete);

        setName("Form"); // NOI18N

        textField_Search.setText(resourceMap.getString("textField_Search.text")); // NOI18N
        textField_Search.setName("textField_Search"); // NOI18N
        textField_Search.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textField_SearchKeyReleased(evt);
            }
        });

        button_NewCustomer.setText(resourceMap.getString("button_NewCustomer.text")); // NOI18N
        button_NewCustomer.setName("button_NewCustomer"); // NOI18N
        button_NewCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_NewCustomerActionPerformed(evt);
            }
        });

        button_SelectedCustomers.setText(resourceMap.getString("button_SelectedCustomers.text")); // NOI18N
        button_SelectedCustomers.setName("button_SelectedCustomers"); // NOI18N
        button_SelectedCustomers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_SelectedCustomersActionPerformed(evt);
            }
        });

        scrollPane_Customers.setName("scrollPane_Customers"); // NOI18N

        table_Customers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Név", "Cím", "Adószám", "Bankszámlaszám", "Kiválasztott"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table_Customers.setName("table_Customers"); // NOI18N
        table_Customers.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                table_CustomersMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                table_CustomersMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                table_CustomersMouseReleased(evt);
            }
        });
        scrollPane_Customers.setViewportView(table_Customers);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollPane_Customers, javax.swing.GroupLayout.DEFAULT_SIZE, 760, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(textField_Search)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(button_NewCustomer)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(button_SelectedCustomers)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textField_Search, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(button_NewCustomer)
                    .addComponent(button_SelectedCustomers))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(scrollPane_Customers, javax.swing.GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void textField_SearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textField_SearchKeyReleased
        UpdateCustomersTable();
    }//GEN-LAST:event_textField_SearchKeyReleased

    private void button_NewCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_NewCustomerActionPerformed
        OpenCustomerView(-100);
    }//GEN-LAST:event_button_NewCustomerActionPerformed

    private void button_SelectedCustomersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_SelectedCustomersActionPerformed
        if(selectedCustomersView == null)
        {
            selectedCustomersView = new SelectedContactsView(this);
            selectedCustomersView.Fill(ProduceData());
            selectedCustomersView.setX((int) (this.getLocation().getX() + this.getSize().getWidth()));
            selectedCustomersView.setY((int) (this.getLocation().getY()));
        }
        
        if(selectedCustomersView.isVisible())
        {
            selectedCustomersView.setVisible(false);
        }
        else
        {
            selectedCustomersView.SortTable(GenerateRegex());
            selectedCustomersView.Open();
        }
    }//GEN-LAST:event_button_SelectedCustomersActionPerformed

    private void table_CustomersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_CustomersMouseClicked
        int row = table_Customers.getSelectedRow();
        
        if(row >=0 && evt.getButton() == MouseEvent.BUTTON1 && evt.getClickCount() == 2)
        {
            OpenCustomerView(row);
        }
    }//GEN-LAST:event_table_CustomersMouseClicked

    private void ShowPopupMenu(MouseEvent evt)
    {
        JTable source = (JTable) evt.getSource();
        int row = source.rowAtPoint(evt.getPoint());
        int column = source.columnAtPoint(evt.getPoint());

        if(!source.isRowSelected(row))
        {
            source.changeSelection(row, column, false, false);
        }

        boolean multiRow = source.getSelectedRows().length > 1;

        menuItem_Open.setEnabled(!multiRow);
        menuItem_Grouping.setEnabled(multiRow);
        menuItem_Delete.setEnabled(!multiRow);

        PopupMenu_Customers.show(evt.getComponent(), evt.getX(), evt.getY());
    }
    
    private void table_CustomersMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_CustomersMouseReleased
        if(evt.isPopupTrigger())
        {
            ShowPopupMenu(evt);
        }
        else
        {
            if(popupTimer.Stop())
            {
                ShowPopupMenu(evt);
                popupTimer.setStart(0);
            }
        }
    }//GEN-LAST:event_table_CustomersMouseReleased

    private void menuItem_OpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_OpenActionPerformed
        int row = table_Customers.getSelectedRow();
        
        if(row >=0)
        {
            OpenCustomerView(row);
        }
    }//GEN-LAST:event_menuItem_OpenActionPerformed

    private void menuItem_GroupingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_GroupingActionPerformed
        int [] rows = table_Customers.getSelectedRows();
        int [] ids = new int[rows.length];
        
        for(int i = 0; i < rows.length; i++)
        {
            ids[i] = Integer.parseInt(table_Customers.getValueAt(rows[i], 0).toString());
        }
        
        GroupingView grouping = new GroupingView(GroupingView.CUSTOMER, ids);
        
        if(grouping.getReturnStatus() == GroupingView.RET_OK)
        {
            UpdateCustomersTable();
        }
    }//GEN-LAST:event_menuItem_GroupingActionPerformed

    private void menuItem_DeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_DeleteActionPerformed
        HibaDialog hd = new HibaDialog("Biztosan törlöd az ügyfelet?", "Igen", "Nem");
        
        if(hd.getReturnStatus() == HibaDialog.RET_OK)
        {
            int row = table_Customers.getSelectedRow();
            int customerID = Integer.parseInt(table_Customers.getValueAt(row, 0).toString());
            
            Customer customer = new Customer(customerID);
            customer.Delete();
            UpdateCustomersTable();
        }
    }//GEN-LAST:event_menuItem_DeleteActionPerformed

    private void table_CustomersMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_CustomersMousePressed
        popupTimer = new PopupTimer();
        popupTimer.Start();
    }//GEN-LAST:event_table_CustomersMousePressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPopupMenu PopupMenu_Customers;
    private javax.swing.JPopupMenu.Separator Separator;
    private javax.swing.JButton button_NewCustomer;
    private javax.swing.JButton button_SelectedCustomers;
    private javax.swing.JMenuItem menuItem_Delete;
    private javax.swing.JMenuItem menuItem_Grouping;
    private javax.swing.JMenuItem menuItem_Open;
    private javax.swing.JScrollPane scrollPane_Customers;
    private javax.swing.JTable table_Customers;
    private javax.swing.JTextField textField_Search;
    // End of variables declaration//GEN-END:variables
}