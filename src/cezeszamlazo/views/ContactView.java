package cezeszamlazo.views;

import cezeszamlazo.HibaDialog;
import cezeszamlazo.Label;
import invoice.Contact;
import invoice.Customer;
import java.awt.Dimension;
import java.awt.Toolkit;

/**
 * @author Tomy
 */

public class ContactView extends javax.swing.JDialog
{
    public static final int RET_CANCEL = 0;
    public static final int RET_OK = 1;
    
    private int returnStatus = RET_CANCEL;
    
    private boolean comboboxUpdate = false;
    
    private Contact contact;
    
    private boolean grouping = false;
    
    public ContactView(int id)
    {
        initComponents();
        
        SetTitle(id);
        SetViewDatas();
        UpdateCustomerComboBox(contact.getCustomerId());
        
        Init();
    }
    
    public ContactView(int id, boolean grouping)
    {
        initComponents();
        this.grouping = grouping;
        button_ExitContactView.setEnabled(false);
        
        SetTitle(id);
        SetViewDatas();
        UpdateCustomerComboBox(contact.getCustomerId());
        
        Init();
    }

    private void SetTitle(int id)
    {
        String title = "";
        
        if(id == 0)
        {
            title = "Új kapcsolattartó";
            contact = new Contact();
        }
        else
        {
            title = "Kapcsolattartó módosítás";
            contact = new Contact(id);
        }
        
        setTitle(title);
    }
    
    private void Init()
    {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;
        
        setLocation(x, y);
        
    }
    
    public void Open(boolean isModal)
    {
        setModal(isModal);
        setVisible(true);
    }
    
    private void UpdateCustomerComboBox(int id)
    {
        int index = 0;
        
        comboboxUpdate = true;
        
        comboBox_Customer.removeAllItems();
        comboBox_Customer.addItem(new Label("0", "Új ügyfél"));
        
        Object [][] customers = Customer.getCustomers();
        
        for(int i = 0; i < customers.length; i++)
        {
            String customerID = customers[i][0].toString();
            String name = customers[i][1].toString();

            comboBox_Customer.addItem(new Label(customerID, name));

            if(Integer.parseInt(customerID) == id)
            {
                index = i+1;//azrét kell hozzáadni, mert 1 itemet előre beszúrtunk
            }
        }
        
        comboboxUpdate = false;
        
        if(id == 0)
        {
            for(int i = 0; i < comboBox_Customer.getItemCount(); i++)
            {
                Label customerLabel = (Label) comboBox_Customer.getItemAt(i);
                
                if(customerLabel.getId().equals("0"))
                {
                    comboBox_Customer.setSelectedIndex(i);
                    break;
                }
            }
        }  
        else
        {
            comboBox_Customer.setSelectedIndex(index);
        }
    }
    
    private void SetViewDatas()
    {
        for (int i = 0; i < comboBox_Customer.getItemCount(); i++)
        {
            Label customerLabel = (Label) comboBox_Customer.getItemAt(i);
            
            int id = Integer.parseInt(customerLabel.getId());
            
            if(id == contact.getCustomerId())
            {
                comboBox_Customer.setSelectedIndex(i);
            }
        }
        
        textField_ContactName.setText(contact.getName());
        textField_ContactTelNumber.setText(contact.getTelNumber());
        textField_ContactEmail.setText(contact.getEmail());
        checkBox_Newsletter.setSelected(contact.isNewsletter());
        checkBox_Finance.setSelected(contact.isFinance());
    }
    
    private void Close(int status)
    {
        returnStatus = status;
        
        if(!grouping)
        {
            setVisible(false);
            dispose();
        }
        else
        {
            setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        label_Customer = new javax.swing.JLabel();
        comboBox_Customer = new javax.swing.JComboBox();
        button_NewCustomer = new javax.swing.JButton();
        label_ContactName = new javax.swing.JLabel();
        textField_ContactName = new javax.swing.JTextField();
        label_ContactTelNumber = new javax.swing.JLabel();
        textField_ContactTelNumber = new javax.swing.JTextField();
        label_ContactEmail = new javax.swing.JLabel();
        textField_ContactEmail = new javax.swing.JTextField();
        checkBox_Newsletter = new javax.swing.JCheckBox();
        checkBox_Finance = new javax.swing.JCheckBox();
        button_SaveContact = new javax.swing.JButton();
        button_ExitContactView = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setName("Form"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(cezeszamlazo.App.class).getContext().getResourceMap(ContactView.class);
        label_Customer.setFont(resourceMap.getFont("label_Customer.font")); // NOI18N
        label_Customer.setText(resourceMap.getString("label_Customer.text")); // NOI18N
        label_Customer.setName("label_Customer"); // NOI18N

        comboBox_Customer.setName("comboBox_Customer"); // NOI18N
        comboBox_Customer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBox_CustomerActionPerformed(evt);
            }
        });

        button_NewCustomer.setText(resourceMap.getString("button_NewCustomer.text")); // NOI18N
        button_NewCustomer.setName("button_NewCustomer"); // NOI18N
        button_NewCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_NewCustomerActionPerformed(evt);
            }
        });

        label_ContactName.setText(resourceMap.getString("label_ContactName.text")); // NOI18N
        label_ContactName.setName("label_ContactName"); // NOI18N

        textField_ContactName.setText(resourceMap.getString("textField_ContactName.text")); // NOI18N
        textField_ContactName.setName("textField_ContactName"); // NOI18N

        label_ContactTelNumber.setText(resourceMap.getString("label_ContactTelNumber.text")); // NOI18N
        label_ContactTelNumber.setName("label_ContactTelNumber"); // NOI18N

        textField_ContactTelNumber.setText(resourceMap.getString("textField_ContactTelNumber.text")); // NOI18N
        textField_ContactTelNumber.setName("textField_ContactTelNumber"); // NOI18N

        label_ContactEmail.setText(resourceMap.getString("label_ContactEmail.text")); // NOI18N
        label_ContactEmail.setName("label_ContactEmail"); // NOI18N

        textField_ContactEmail.setText(resourceMap.getString("textField_ContactEmail.text")); // NOI18N
        textField_ContactEmail.setName("textField_ContactEmail"); // NOI18N

        checkBox_Newsletter.setText(resourceMap.getString("checkBox_Newsletter.text")); // NOI18N
        checkBox_Newsletter.setName("checkBox_Newsletter"); // NOI18N

        checkBox_Finance.setText(resourceMap.getString("checkBox_Finance.text")); // NOI18N
        checkBox_Finance.setName("checkBox_Finance"); // NOI18N

        button_SaveContact.setText(resourceMap.getString("button_SaveContact.text")); // NOI18N
        button_SaveContact.setName("button_SaveContact"); // NOI18N
        button_SaveContact.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_SaveContactActionPerformed(evt);
            }
        });

        button_ExitContactView.setText(resourceMap.getString("button_ExitContactView.text")); // NOI18N
        button_ExitContactView.setName("button_ExitContactView"); // NOI18N
        button_ExitContactView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_ExitContactViewActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(label_ContactTelNumber, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(label_Customer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(label_ContactName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(label_ContactEmail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(textField_ContactName)
                    .addComponent(textField_ContactTelNumber)
                    .addComponent(textField_ContactEmail)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(checkBox_Finance)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(button_SaveContact))
                            .addComponent(checkBox_Newsletter))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(button_ExitContactView, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(comboBox_Customer, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(button_NewCustomer, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label_Customer)
                    .addComponent(comboBox_Customer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(button_NewCustomer))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label_ContactName)
                    .addComponent(textField_ContactName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label_ContactTelNumber)
                    .addComponent(textField_ContactTelNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label_ContactEmail)
                    .addComponent(textField_ContactEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(checkBox_Newsletter)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(checkBox_Finance)
                        .addContainerGap(33, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(button_SaveContact)
                            .addComponent(button_ExitContactView))
                        .addContainerGap())))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void button_NewCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_NewCustomerActionPerformed
        CustomerView customerView = new CustomerView();
        customerView.setCustomer(new Customer(contact.getCustomerId()));
        customerView.Open(true);
        
        if(customerView.getReturnStatus() == CustomerView.RET_OK)
        {
            UpdateCustomerComboBox(customerView.getCustomer().getId());
            
            for(int i = 0; i < comboBox_Customer.getItemCount(); i++)
            {
                Label customerLabel = (Label) comboBox_Customer.getItemAt(i);
                
                if(customerLabel.getId().equals(customerView.getCustomer().getId()))
                {
                    comboBox_Customer.setSelectedIndex(i);
                    break;
                }
            }
        }
    }//GEN-LAST:event_button_NewCustomerActionPerformed

    private void button_SaveContactActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_SaveContactActionPerformed
        if(textField_ContactName.getText().length() != 0)
        {
            if(comboBox_Customer.getSelectedIndex() != 0)
            {
                contact.setName(textField_ContactName.getText());
                contact.setTelNumber(textField_ContactTelNumber.getText());
                contact.setEmail(textField_ContactEmail.getText());
                contact.setNewsletter(checkBox_Newsletter.isSelected());
                contact.setFinance(checkBox_Finance.isSelected());

                contact.Save();

                Close(RET_OK);
            }
            else
            {
                HibaDialog hd = new HibaDialog("Válasszon ki egy ügyfelet!", "OK", "Vissza");
            }
        }
        else
        {
            HibaDialog hd = new HibaDialog("A kapcsolattartó neve nem maradhat üresen!", "OK", "Vissza");
        }
    }//GEN-LAST:event_button_SaveContactActionPerformed

    private void button_ExitContactViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_ExitContactViewActionPerformed
        Close(RET_CANCEL);
    }//GEN-LAST:event_button_ExitContactViewActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        Close(RET_CANCEL);
    }//GEN-LAST:event_formWindowClosed

    private void comboBox_CustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBox_CustomerActionPerformed
        if(!comboboxUpdate)
        {
            Label customerLabel = (Label)comboBox_Customer.getSelectedItem();
            contact.setCustomerId(Integer.parseInt(customerLabel.getId()));
        }
    }//GEN-LAST:event_comboBox_CustomerActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        Close(RET_CANCEL);
    }//GEN-LAST:event_formWindowClosing

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton button_ExitContactView;
    private javax.swing.JButton button_NewCustomer;
    private javax.swing.JButton button_SaveContact;
    private javax.swing.JCheckBox checkBox_Finance;
    private javax.swing.JCheckBox checkBox_Newsletter;
    private javax.swing.JComboBox comboBox_Customer;
    private javax.swing.JLabel label_ContactEmail;
    private javax.swing.JLabel label_ContactName;
    private javax.swing.JLabel label_ContactTelNumber;
    private javax.swing.JLabel label_Customer;
    private javax.swing.JTextField textField_ContactEmail;
    private javax.swing.JTextField textField_ContactName;
    private javax.swing.JTextField textField_ContactTelNumber;
    // End of variables declaration//GEN-END:variables

    //GETTERS
    public int getReturnStatus()
    {
        return returnStatus;
    }
    
    //SETTERS
    public void setContact(Contact contact)
    {
        this.contact = contact;
        SetViewDatas();
    }
}