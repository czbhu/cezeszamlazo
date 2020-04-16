package cezeszamlazo.views;

import cezeszamlazo.HibaDialog;
import cezeszamlazo.model.PopupTimer;
import cezeszamlazo.ugyfel.CheckBoxModelListener;
import cezeszamlazo.ugyfel.CheckBoxRenderer;
import cezeszamlazo.ugyfel.DefaultTableRender;
import invoice.Contact;
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
public class ContactsView extends javax.swing.JFrame
{
    private SelectedContactsView selectedContactsView = null;
    
    PopupTimer popupTimer;
    
    public ContactsView()
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
        setTitle("Kapcsolattartók");
    }
    
    public void Open()
    {
        UpdateContactsTable();
        setVisible(true);
    }
    
    public void UpdateContactsTable()
    {
        DefaultTableModel DTModel = (DefaultTableModel) table_Contacts.getModel();
        
        String search = textField_Search.getText();
        
        String [] header = {"Id", "Ügyfél", "Név", "Telefon", "Email", "Pénzügyes", "Kiválaszt"};
        
        Object [][] help = Contact.getContacts(search);
        Object [][] contacts = new Object[0][0];
        
        if(help.length > 0)
        {
            contacts = new Object[help.length][help[0].length + 1];
            
            for(int i = 0; i < contacts.length; i++)
            {
                contacts[i][0] = help[i][0];
                contacts[i][1] = help[i][1];
                contacts[i][2] = help[i][2];
                contacts[i][3] = help[i][3];
                contacts[i][4] = help[i][4];
                contacts[i][5] = false;
            }
        }
        
        DTModel.setDataVector(contacts, header);
        
        DefaultTableRender render = new DefaultTableRender();
        TableColumn col;
        CheckBoxRenderer checkBoxRenderer = new CheckBoxRenderer();
        int[] meret = {30, 200, 150, 100, 100};
        
        for (int i = 0; i < meret.length; i++)
        {
            col = table_Contacts.getColumnModel().getColumn(i);
            col.setPreferredWidth(meret[i]);
            col.setCellRenderer(render);
        }
        
        col = table_Contacts.getColumnModel().getColumn(5);
        col.setCellRenderer(checkBoxRenderer);

        DTModel.addTableModelListener(new CheckBoxModelListener(this));
    }
    
    private void OpenContactView(int row)
    {
        int id = 0;
        
        if(row >= 1)
        {
            id = Integer.parseInt(table_Contacts.getValueAt(row, 0).toString());
        }

        ContactView newContact = new ContactView(id);
        newContact.Open(true);
        
        if(newContact.getReturnStatus() == ContactView.RET_OK)
        {
            UpdateContactsTable();
        }
    }
    
    private Map<Integer, String> ProduceData()
    {
        Map<Integer, String> data = new HashMap<>();
        
        for(int i = 0; i < table_Contacts.getRowCount(); i++)
        {
            int id = Integer.parseInt(table_Contacts.getValueAt(i, 0).toString());
            String name = table_Contacts.getValueAt(i, 1).toString();
            data.put(id, name);
        }
        
        return data;
    }
    
    private String GenerateRegex()
    {
        String regex = "";

        regex += "^(";

        for (int i = 0; i < table_Contacts.getRowCount(); i++)
        {
            if ((boolean) table_Contacts.getValueAt(i, 6))
            {
                regex += String.valueOf(table_Contacts.getValueAt(i, 0)) + "|";
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
    
    public void SetSelected(ArrayList<Integer> ids)
    {
        table_Contacts.clearSelection();
        for (int i = 0; i < table_Contacts.getRowCount(); i++)
        {
            if (ids.contains(Integer.valueOf(String.valueOf(table_Contacts.getValueAt(i, 0)))))
            {
                table_Contacts.addRowSelectionInterval(i, i);
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PopupMenu_Contacts = new javax.swing.JPopupMenu();
        menuItem_Open = new javax.swing.JMenuItem();
        menuItem_Group = new javax.swing.JMenuItem();
        Separator = new javax.swing.JPopupMenu.Separator();
        menuItem_Delete = new javax.swing.JMenuItem();
        textField_Search = new javax.swing.JTextField();
        button_NewContact = new javax.swing.JButton();
        button_SelectedContacts = new javax.swing.JButton();
        scrollPane_Contacts = new javax.swing.JScrollPane();
        table_Contacts = new javax.swing.JTable();

        PopupMenu_Contacts.setName("PopupMenu_Contacts"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(cezeszamlazo.App.class).getContext().getResourceMap(ContactsView.class);
        menuItem_Open.setText(resourceMap.getString("menuItem_Open.text")); // NOI18N
        menuItem_Open.setName("menuItem_Open"); // NOI18N
        menuItem_Open.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItem_OpenActionPerformed(evt);
            }
        });
        PopupMenu_Contacts.add(menuItem_Open);

        menuItem_Group.setText(resourceMap.getString("menuItem_Group.text")); // NOI18N
        menuItem_Group.setName("menuItem_Group"); // NOI18N
        menuItem_Group.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItem_GroupActionPerformed(evt);
            }
        });
        PopupMenu_Contacts.add(menuItem_Group);

        Separator.setName("Separator"); // NOI18N
        PopupMenu_Contacts.add(Separator);

        menuItem_Delete.setText(resourceMap.getString("menuItem_Delete.text")); // NOI18N
        menuItem_Delete.setName("menuItem_Delete"); // NOI18N
        menuItem_Delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItem_DeleteActionPerformed(evt);
            }
        });
        PopupMenu_Contacts.add(menuItem_Delete);

        setName("Form"); // NOI18N

        textField_Search.setText(resourceMap.getString("textField_Search.text")); // NOI18N
        textField_Search.setName("textField_Search"); // NOI18N
        textField_Search.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textField_SearchKeyReleased(evt);
            }
        });

        button_NewContact.setText(resourceMap.getString("button_NewContact.text")); // NOI18N
        button_NewContact.setName("button_NewContact"); // NOI18N
        button_NewContact.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_NewContactActionPerformed(evt);
            }
        });

        button_SelectedContacts.setText(resourceMap.getString("button_SelectedContacts.text")); // NOI18N
        button_SelectedContacts.setName("button_SelectedContacts"); // NOI18N
        button_SelectedContacts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_SelectedContactsActionPerformed(evt);
            }
        });

        scrollPane_Contacts.setName("scrollPane_Contacts"); // NOI18N

        table_Contacts.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Ügyfél", "Név", "Telefon", "Email", "Pénzügyes", "Kiválaszt"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table_Contacts.setName("table_Contacts"); // NOI18N
        table_Contacts.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                table_ContactsMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                table_ContactsMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                table_ContactsMouseReleased(evt);
            }
        });
        scrollPane_Contacts.setViewportView(table_Contacts);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollPane_Contacts)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(textField_Search, javax.swing.GroupLayout.PREFERRED_SIZE, 492, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(button_NewContact)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(button_SelectedContacts)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textField_Search, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(button_NewContact)
                    .addComponent(button_SelectedContacts))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(scrollPane_Contacts, javax.swing.GroupLayout.DEFAULT_SIZE, 244, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void textField_SearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textField_SearchKeyReleased
        UpdateContactsTable();
    }//GEN-LAST:event_textField_SearchKeyReleased

    private void menuItem_OpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_OpenActionPerformed
        int row = table_Contacts.getSelectedRow();
        
        if(row >=0)
        {
            OpenContactView(row);
        }
    }//GEN-LAST:event_menuItem_OpenActionPerformed

    private void menuItem_GroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_GroupActionPerformed
        int [] rows = table_Contacts.getSelectedRows();
        int [] ids = new int[rows.length];
        
        for(int i = 0; i < rows.length; i++)
        {
            ids[i] = Integer.parseInt(table_Contacts.getValueAt(rows[i], 0).toString());
        }
        
        GroupingView contactGroup = new GroupingView(GroupingView.CONTACT, ids);
        
        if(contactGroup.getReturnStatus() == GroupingView.RET_OK)
        {
            UpdateContactsTable();
        }
    }//GEN-LAST:event_menuItem_GroupActionPerformed

    private void menuItem_DeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_DeleteActionPerformed
        HibaDialog hd = new HibaDialog("Biztosan törlöd a kapcsolattartót?", "Igen", "Nem");
        
        if(hd.getReturnStatus() == HibaDialog.RET_OK)
        {
            int row = table_Contacts.getSelectedRow();
            int id = Integer.parseInt(table_Contacts.getValueAt(row, 0).toString());
            
            Contact contact = new Contact(id);
            contact.Delete();
            
            UpdateContactsTable();
        }
    }//GEN-LAST:event_menuItem_DeleteActionPerformed

    private void table_ContactsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_ContactsMouseClicked
        int row = table_Contacts.getSelectedRow();
        
        if(row >=0 && evt.getButton() == MouseEvent.BUTTON1 && evt.getClickCount() == 2)
        {
            OpenContactView(row);
        }
    }//GEN-LAST:event_table_ContactsMouseClicked

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
        menuItem_Group.setEnabled(multiRow);
        menuItem_Delete.setEnabled(!multiRow);

        PopupMenu_Contacts.show(evt.getComponent(), evt.getX(), evt.getY());
    }
    
    private void table_ContactsMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_ContactsMouseReleased
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
    }//GEN-LAST:event_table_ContactsMouseReleased

    private void button_NewContactActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_NewContactActionPerformed
        OpenContactView(-100);
    }//GEN-LAST:event_button_NewContactActionPerformed

    private void button_SelectedContactsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_SelectedContactsActionPerformed
        if(selectedContactsView == null)
        {
            selectedContactsView = new SelectedContactsView(this);
            selectedContactsView.setX((int) (this.getLocation().getX() + this.getSize().getWidth()));
            selectedContactsView.setY((int) (this.getLocation().getY()));
            
            selectedContactsView.Fill(ProduceData());
        }
        
        if(selectedContactsView.isVisible())
        {
            selectedContactsView.setVisible(false);
        }
        else
        {
            selectedContactsView.SortTable(GenerateRegex());
            selectedContactsView.Open();
        }
    }//GEN-LAST:event_button_SelectedContactsActionPerformed

    private void table_ContactsMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_ContactsMousePressed
        popupTimer = new PopupTimer();
        popupTimer.Start();
    }//GEN-LAST:event_table_ContactsMousePressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPopupMenu PopupMenu_Contacts;
    private javax.swing.JPopupMenu.Separator Separator;
    private javax.swing.JButton button_NewContact;
    private javax.swing.JButton button_SelectedContacts;
    private javax.swing.JMenuItem menuItem_Delete;
    private javax.swing.JMenuItem menuItem_Group;
    private javax.swing.JMenuItem menuItem_Open;
    private javax.swing.JScrollPane scrollPane_Contacts;
    private javax.swing.JTable table_Contacts;
    private javax.swing.JTextField textField_Search;
    // End of variables declaration//GEN-END:variables
}