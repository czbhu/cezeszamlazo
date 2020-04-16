package cezeszamlazo.views;

import cezeszamlazo.HibaDialog;
import invoice.Contact;
import invoice.Customer;
import java.awt.Dimension;
import java.awt.Toolkit;

/**
 * @author Tomy
 */
public class GroupingView extends javax.swing.JDialog
{
    public final static int CONTACT = 0, CUSTOMER = 1;
    
    public static final int RET_CANCEL = 0;
    public static final int RET_OK = 1;
    private int returnStatus = RET_CANCEL;
    
    private int type;
    private int current = 0;
    
    private Contact[] contacts = null;
    private Customer[] customers = null;
    
    private ContactView contactView = null;
    private CustomerView customerView = null;
    
    public GroupingView(int type, int [] ids)
    {
        initComponents();
        
        this.type = type;
        
        if(type == CONTACT)
        {
            contacts = Contact.getContacts(ids);
        }
        else if(type == CUSTOMER)
        {
            customers = Customer.getCustomers(ids);
        }
        
        Init();
    }
    
    private void Init()
    {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int x = (screenSize.width - getWidth()) / 2;
        int y = 0;
        
        Load();
        
        if(type == CONTACT)
        {
            y = screenSize.height / 2 - contactView.getHeight() / 2 - getHeight();
        }
        else if(type == CUSTOMER)
        {
            y = screenSize.height / 2 - customerView.getHeight() / 2 - getHeight();
        }
        
        setTitle("Csoportosítás");
        setLocation(x, y);
        setVisible(true);
    }
    
    private void Load()
    {
        if(type == CONTACT)
        {
            label_Count.setText((current + 1) + "/" + contacts.length);
            
            if(contactView == null)
            {
                contactView = new ContactView(contacts[current].getId(), true);
            }
            else
            {
                contactView.setContact(new Contact(contacts[current].getId()));
            }
            
            contactView.Open(false);
        }
        else if(type == CUSTOMER)
        {
            label_Count.setText((current + 1) + "/" + customers.length);
            
            if(customerView == null)
            {
                customerView = new CustomerView(new Customer(customers[current].getId()), true);
            }
            else
            {
                customerView.setCustomer(new Customer(customers[current].getId()));
            }
            
            customerView.Open(false);
        }
    }
    
    private void Close(int status)
    {
        if(type == CONTACT)
        {
            contactView.setVisible(false);
        }
        else if(type == CUSTOMER)
        {
            customerView.setVisible(false);
        }
        
        returnStatus = status;
        setVisible(false);
        dispose();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        button_Previous = new javax.swing.JButton();
        label_Count = new javax.swing.JLabel();
        button_Next = new javax.swing.JButton();
        button_Group = new javax.swing.JButton();
        button_ExitGroupingView = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setName("Form"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(cezeszamlazo.App.class).getContext().getResourceMap(GroupingView.class);
        button_Previous.setFont(resourceMap.getFont("button_Previous.font")); // NOI18N
        button_Previous.setText(resourceMap.getString("button_Previous.text")); // NOI18N
        button_Previous.setName("button_Previous"); // NOI18N
        button_Previous.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_PreviousActionPerformed(evt);
            }
        });

        label_Count.setFont(resourceMap.getFont("button_Next.font")); // NOI18N
        label_Count.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label_Count.setText(resourceMap.getString("label_Count.text")); // NOI18N
        label_Count.setName("label_Count"); // NOI18N

        button_Next.setFont(resourceMap.getFont("button_Next.font")); // NOI18N
        button_Next.setText(resourceMap.getString("button_Next.text")); // NOI18N
        button_Next.setName("button_Next"); // NOI18N
        button_Next.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_NextActionPerformed(evt);
            }
        });

        button_Group.setText(resourceMap.getString("button_Group.text")); // NOI18N
        button_Group.setName("button_Group"); // NOI18N
        button_Group.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_GroupActionPerformed(evt);
            }
        });

        button_ExitGroupingView.setText(resourceMap.getString("button_ExitGroupingView.text")); // NOI18N
        button_ExitGroupingView.setName("button_ExitGroupingView"); // NOI18N
        button_ExitGroupingView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_ExitGroupingViewActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 252, Short.MAX_VALUE)
                        .addComponent(button_Group)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(button_ExitGroupingView))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(button_Previous)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label_Count, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(button_Next)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(button_Next, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE)
                    .addComponent(button_Previous, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(label_Count, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(button_ExitGroupingView)
                    .addComponent(button_Group))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        Close(RET_CANCEL);
    }//GEN-LAST:event_formWindowClosed

    private void button_PreviousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_PreviousActionPerformed
        current--;
        
        if(current < 0)
        {
            if(type == CONTACT)
            {
                current = contacts.length - 1;
            }
            else if(type == CUSTOMER)
            {
                current = customers.length - 1;
            }
        }
        
        Load();
    }//GEN-LAST:event_button_PreviousActionPerformed

    private void button_NextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_NextActionPerformed
        current++;
        
        if(type == CONTACT)
        {
            if(current > contacts.length - 1)
            {
                current = 0;
            }
        }
        else if(type == CUSTOMER)
        {
            if(current > customers.length - 1)
            {
                current = 0;
            }
        }
        
        Load();
    }//GEN-LAST:event_button_NextActionPerformed

    private void button_ExitGroupingViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_ExitGroupingViewActionPerformed
        Close(RET_CANCEL);
    }//GEN-LAST:event_button_ExitGroupingViewActionPerformed

    private void button_GroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_GroupActionPerformed
        if(type == CONTACT)
        {
            Contact contact = contacts[current];
            
            String valid = contact.isValid();
            
            if(valid.isEmpty())
            {
                contact.Grouping(contacts);
                Close(RET_OK);
            }
            else
            {
                HibaDialog hd = new HibaDialog(valid, "", "OK");
            }
        }
        else if(type == CUSTOMER)
        {
            Customer customer = customers[current];
            
            String valid = customer.isValid();
            
            if(valid.isEmpty())
            {
                customer.Grouping(customers);
                Close(RET_OK);
            }
            else
            {
                HibaDialog hd = new HibaDialog(valid, "", "OK");
            }
        }
    }//GEN-LAST:event_button_GroupActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton button_ExitGroupingView;
    private javax.swing.JButton button_Group;
    private javax.swing.JButton button_Next;
    private javax.swing.JButton button_Previous;
    private javax.swing.JLabel label_Count;
    // End of variables declaration//GEN-END:variables

    //GETTERS
    public int getReturnStatus()
    {
        return returnStatus;
    }
}