package cezeszamlazo.views;

import cezeszamlazo.App;
import cezeszamlazo.controller.Functions;
import controller.City;
import controller.Country;
//import cezeszamlazo.model.Invoice;
//import cezeszamlazo.model.Supplier;
//import cezeszamlazo.nav.NAV;
//import cezeszamlazo.nav.invoice.AddressType;
//import cezeszamlazo.nav.response.queryTaxpayer.QueryTaxpayerResponseType;
import invoice.Invoice;
import invoice.Supplier;
import java.awt.Dimension;
import java.awt.Toolkit;
import nav.NAV;
import onlineInvoice.AddressType;
import onlineInvoice.response.queryTaxpayer.QueryTaxpayerResponseType;

/**
 * @author Tomy
 */

public class SupplierView extends javax.swing.JDialog
{
    public static final int RET_OK = 1, RET_CANCEL = 0;
    private int returnStatus = RET_CANCEL;
    
    private Invoice invoice;
    private Supplier supplier;
    
    public SupplierView()
    {
        initComponents();
        Init();
    }

    /*public SupplierView(Supplier supplier)
    {
        this.supplier = supplier;
        
        initComponents();
        
        setFields();
        
        Init();
    }*/
    
    public SupplierView(Invoice invoice)
    {
        this.invoice = invoice;
        supplier = invoice.getSupplier();
        
        initComponents();
        
        setFields();
        
        Init();
    }
    
    private void Init()
    {
        setModal(true);
        setTitle("Szállító adatok");
        
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;
        
        setLocation(x, y);
        
        setVisible(true);
    }
    
    private void Close(int status)
    {
        returnStatus = status;
        setVisible(false);
        dispose();
    }
    
    private void LoadSupplier()
    {
        supplier.setName(textField_SupplierName.getText());
        
        supplier.setCountryCode(textField_CountryCode.getText());
        supplier.setCountry(textField_Country.getText());
        supplier.setPostalCode(textField_PostalCode.getText());
        supplier.setCity(textField_City.getText());
        supplier.setStreet(textField_Street.getText());
        supplier.setPublicPlace(textField_PublicPlace.getText());
        supplier.setHouseNumber(textField_HouseNumber.getText());
        
        supplier.setTaxNumber(textField_TaxNumber.getText());
        supplier.setEuTaxNumber(textField_EuTaxNumber.getText());
        supplier.setBankAccountNumber(textField_BankAccountNumber.getText());
        supplier.setComment(textField_Comment.getText());
        supplier.setInvoiceFooter(textField_InvoiceFooter.getText());
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel_SupplierDatas = new javax.swing.JPanel();
        panel_SupplierName = new javax.swing.JPanel();
        label_SupplierName = new javax.swing.JLabel();
        panel_SupplierNameText = new javax.swing.JPanel();
        textField_SupplierName = new javax.swing.JTextField();
        panel_SupplierAddress = new javax.swing.JPanel();
        label_SupplierAddress = new javax.swing.JLabel();
        panel_SupplierAddressDatas = new javax.swing.JPanel();
        label_CountryCode = new javax.swing.JLabel();
        textField_CountryCode = new javax.swing.JTextField();
        label_Country = new javax.swing.JLabel();
        textField_Country = new javax.swing.JTextField();
        label_PostalCode = new javax.swing.JLabel();
        textField_PostalCode = new javax.swing.JTextField();
        label_City = new javax.swing.JLabel();
        textField_City = new javax.swing.JTextField();
        label_Street = new javax.swing.JLabel();
        textField_Street = new javax.swing.JTextField();
        label_PublicPlace = new javax.swing.JLabel();
        textField_PublicPlace = new javax.swing.JTextField();
        label_houseNumber = new javax.swing.JLabel();
        textField_HouseNumber = new javax.swing.JTextField();
        panel_SupplierOther = new javax.swing.JPanel();
        label_SupplierOther = new javax.swing.JLabel();
        panel_OtherDatas = new javax.swing.JPanel();
        label_TaxNumber = new javax.swing.JLabel();
        textField_TaxNumber = new javax.swing.JTextField();
        label_EuTaxNumber = new javax.swing.JLabel();
        textField_EuTaxNumber = new javax.swing.JTextField();
        label_BankAccountNumber = new javax.swing.JLabel();
        textField_BankAccountNumber = new javax.swing.JTextField();
        label_Comment = new javax.swing.JLabel();
        textField_Comment = new javax.swing.JTextField();
        button_QueryTaxpayer = new customs.CustomButton();
        panel_SupplierInvoiceFooter = new javax.swing.JPanel();
        label_SupplierInvoiceFooter = new javax.swing.JLabel();
        panel_InvoiceFooter = new javax.swing.JPanel();
        scrollPane_InvoiceFooter = new javax.swing.JScrollPane();
        textField_InvoiceFooter = new javax.swing.JTextArea();
        button_SaveModification = new customs.CustomButton();
        button_SaveAsNew = new customs.CustomButton();
        button_ExitSupplierView = new customs.CustomButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setName("Form"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(cezeszamlazo.App.class).getContext().getResourceMap(SupplierView.class);
        panel_SupplierDatas.setBackground(resourceMap.getColor("panel_SupplierDatas.background")); // NOI18N
        panel_SupplierDatas.setName("panel_SupplierDatas"); // NOI18N

        panel_SupplierName.setBackground(resourceMap.getColor("panel_SupplierName.background")); // NOI18N
        panel_SupplierName.setName("panel_SupplierName"); // NOI18N

        label_SupplierName.setForeground(resourceMap.getColor("label_SupplierName.foreground")); // NOI18N
        label_SupplierName.setText(resourceMap.getString("label_SupplierName.text")); // NOI18N
        label_SupplierName.setName("label_SupplierName"); // NOI18N

        panel_SupplierNameText.setBackground(resourceMap.getColor("panel_SupplierNameText.background")); // NOI18N
        panel_SupplierNameText.setName("panel_SupplierNameText"); // NOI18N

        textField_SupplierName.setBackground(resourceMap.getColor("textField_CountryCode.background")); // NOI18N
        textField_SupplierName.setText(resourceMap.getString("textField_SupplierName.text")); // NOI18N
        textField_SupplierName.setName("textField_SupplierName"); // NOI18N

        javax.swing.GroupLayout panel_SupplierNameTextLayout = new javax.swing.GroupLayout(panel_SupplierNameText);
        panel_SupplierNameText.setLayout(panel_SupplierNameTextLayout);
        panel_SupplierNameTextLayout.setHorizontalGroup(
            panel_SupplierNameTextLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_SupplierNameTextLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(textField_SupplierName)
                .addContainerGap())
        );
        panel_SupplierNameTextLayout.setVerticalGroup(
            panel_SupplierNameTextLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_SupplierNameTextLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(textField_SupplierName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panel_SupplierNameLayout = new javax.swing.GroupLayout(panel_SupplierName);
        panel_SupplierName.setLayout(panel_SupplierNameLayout);
        panel_SupplierNameLayout.setHorizontalGroup(
            panel_SupplierNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_SupplierNameLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label_SupplierName)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(panel_SupplierNameText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panel_SupplierNameLayout.setVerticalGroup(
            panel_SupplierNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_SupplierNameLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label_SupplierName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel_SupplierNameText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panel_SupplierAddress.setBackground(resourceMap.getColor("panel_SupplierAddress.background")); // NOI18N
        panel_SupplierAddress.setName("panel_SupplierAddress"); // NOI18N

        label_SupplierAddress.setForeground(resourceMap.getColor("label_SupplierAddress.foreground")); // NOI18N
        label_SupplierAddress.setText(resourceMap.getString("label_SupplierAddress.text")); // NOI18N
        label_SupplierAddress.setName("label_SupplierAddress"); // NOI18N

        panel_SupplierAddressDatas.setBackground(resourceMap.getColor("panel_SupplierAddressDatas.background")); // NOI18N
        panel_SupplierAddressDatas.setName("panel_SupplierAddressDatas"); // NOI18N

        label_CountryCode.setText(resourceMap.getString("label_CountryCode.text")); // NOI18N
        label_CountryCode.setName("label_CountryCode"); // NOI18N

        textField_CountryCode.setBackground(resourceMap.getColor("textField_CountryCode.background")); // NOI18N
        textField_CountryCode.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textField_CountryCode.setText(resourceMap.getString("textField_CountryCode.text")); // NOI18N
        textField_CountryCode.setName("textField_CountryCode"); // NOI18N
        textField_CountryCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textField_CountryCodeKeyReleased(evt);
            }
        });

        label_Country.setText(resourceMap.getString("label_Country.text")); // NOI18N
        label_Country.setName("label_Country"); // NOI18N

        textField_Country.setBackground(resourceMap.getColor("textField_CountryCode.background")); // NOI18N
        textField_Country.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textField_Country.setText(resourceMap.getString("textField_Country.text")); // NOI18N
        textField_Country.setName("textField_Country"); // NOI18N
        textField_Country.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textField_CountryKeyReleased(evt);
            }
        });

        label_PostalCode.setText(resourceMap.getString("label_PostalCode.text")); // NOI18N
        label_PostalCode.setName("label_PostalCode"); // NOI18N

        textField_PostalCode.setBackground(resourceMap.getColor("textField_CountryCode.background")); // NOI18N
        textField_PostalCode.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textField_PostalCode.setText(resourceMap.getString("textField_PostalCode.text")); // NOI18N
        textField_PostalCode.setName("textField_PostalCode"); // NOI18N
        textField_PostalCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textField_PostalCodeKeyReleased(evt);
            }
        });

        label_City.setText(resourceMap.getString("label_City.text")); // NOI18N
        label_City.setName("label_City"); // NOI18N

        textField_City.setBackground(resourceMap.getColor("textField_CountryCode.background")); // NOI18N
        textField_City.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textField_City.setText(resourceMap.getString("textField_City.text")); // NOI18N
        textField_City.setName("textField_City"); // NOI18N

        label_Street.setText(resourceMap.getString("label_Street.text")); // NOI18N
        label_Street.setName("label_Street"); // NOI18N

        textField_Street.setBackground(resourceMap.getColor("textField_CountryCode.background")); // NOI18N
        textField_Street.setText(resourceMap.getString("textField_Street.text")); // NOI18N
        textField_Street.setName("textField_Street"); // NOI18N

        label_PublicPlace.setText(resourceMap.getString("label_PublicPlace.text")); // NOI18N
        label_PublicPlace.setName("label_PublicPlace"); // NOI18N

        textField_PublicPlace.setBackground(resourceMap.getColor("textField_CountryCode.background")); // NOI18N
        textField_PublicPlace.setText(resourceMap.getString("textField_PublicPlace.text")); // NOI18N
        textField_PublicPlace.setName("textField_PublicPlace"); // NOI18N

        label_houseNumber.setText(resourceMap.getString("label_houseNumber.text")); // NOI18N
        label_houseNumber.setName("label_houseNumber"); // NOI18N

        textField_HouseNumber.setBackground(resourceMap.getColor("textField_CountryCode.background")); // NOI18N
        textField_HouseNumber.setText(resourceMap.getString("textField_HouseNumber.text")); // NOI18N
        textField_HouseNumber.setName("textField_HouseNumber"); // NOI18N

        javax.swing.GroupLayout panel_SupplierAddressDatasLayout = new javax.swing.GroupLayout(panel_SupplierAddressDatas);
        panel_SupplierAddressDatas.setLayout(panel_SupplierAddressDatasLayout);
        panel_SupplierAddressDatasLayout.setHorizontalGroup(
            panel_SupplierAddressDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_SupplierAddressDatasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_SupplierAddressDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(label_houseNumber, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(label_PublicPlace, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(label_Street, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(label_PostalCode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(label_CountryCode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_SupplierAddressDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_SupplierAddressDatasLayout.createSequentialGroup()
                        .addGroup(panel_SupplierAddressDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(textField_CountryCode)
                            .addComponent(textField_PostalCode))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panel_SupplierAddressDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(label_Country, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(label_City, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel_SupplierAddressDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(textField_Country)
                            .addComponent(textField_City)))
                    .addComponent(textField_Street)
                    .addComponent(textField_PublicPlace)
                    .addComponent(textField_HouseNumber))
                .addContainerGap())
        );
        panel_SupplierAddressDatasLayout.setVerticalGroup(
            panel_SupplierAddressDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_SupplierAddressDatasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_SupplierAddressDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label_CountryCode)
                    .addComponent(textField_CountryCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textField_Country, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label_Country))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_SupplierAddressDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label_PostalCode)
                    .addComponent(textField_PostalCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label_City)
                    .addComponent(textField_City, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_SupplierAddressDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label_Street)
                    .addComponent(textField_Street, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_SupplierAddressDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label_PublicPlace)
                    .addComponent(textField_PublicPlace, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_SupplierAddressDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label_houseNumber)
                    .addComponent(textField_HouseNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout panel_SupplierAddressLayout = new javax.swing.GroupLayout(panel_SupplierAddress);
        panel_SupplierAddress.setLayout(panel_SupplierAddressLayout);
        panel_SupplierAddressLayout.setHorizontalGroup(
            panel_SupplierAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_SupplierAddressLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label_SupplierAddress)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(panel_SupplierAddressDatas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panel_SupplierAddressLayout.setVerticalGroup(
            panel_SupplierAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_SupplierAddressLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label_SupplierAddress)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel_SupplierAddressDatas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panel_SupplierOther.setBackground(resourceMap.getColor("panel_SupplierOther.background")); // NOI18N
        panel_SupplierOther.setName("panel_SupplierOther"); // NOI18N

        label_SupplierOther.setForeground(resourceMap.getColor("label_SupplierOther.foreground")); // NOI18N
        label_SupplierOther.setText(resourceMap.getString("label_SupplierOther.text")); // NOI18N
        label_SupplierOther.setName("label_SupplierOther"); // NOI18N

        panel_OtherDatas.setBackground(resourceMap.getColor("panel_OtherDatas.background")); // NOI18N
        panel_OtherDatas.setName("panel_OtherDatas"); // NOI18N

        label_TaxNumber.setText(resourceMap.getString("label_TaxNumber.text")); // NOI18N
        label_TaxNumber.setName("label_TaxNumber"); // NOI18N

        textField_TaxNumber.setBackground(resourceMap.getColor("textField_CountryCode.background")); // NOI18N
        textField_TaxNumber.setText(resourceMap.getString("textField_TaxNumber.text")); // NOI18N
        textField_TaxNumber.setName("textField_TaxNumber"); // NOI18N

        label_EuTaxNumber.setText(resourceMap.getString("label_EuTaxNumber.text")); // NOI18N
        label_EuTaxNumber.setName("label_EuTaxNumber"); // NOI18N

        textField_EuTaxNumber.setBackground(resourceMap.getColor("textField_CountryCode.background")); // NOI18N
        textField_EuTaxNumber.setText(resourceMap.getString("textField_EuTaxNumber.text")); // NOI18N
        textField_EuTaxNumber.setName("textField_EuTaxNumber"); // NOI18N

        label_BankAccountNumber.setText(resourceMap.getString("label_BankAccountNumber.text")); // NOI18N
        label_BankAccountNumber.setName("label_BankAccountNumber"); // NOI18N

        textField_BankAccountNumber.setBackground(resourceMap.getColor("textField_CountryCode.background")); // NOI18N
        textField_BankAccountNumber.setText(resourceMap.getString("textField_BankAccountNumber.text")); // NOI18N
        textField_BankAccountNumber.setName("textField_BankAccountNumber"); // NOI18N

        label_Comment.setText(resourceMap.getString("label_Comment.text")); // NOI18N
        label_Comment.setName("label_Comment"); // NOI18N

        textField_Comment.setBackground(resourceMap.getColor("textField_CountryCode.background")); // NOI18N
        textField_Comment.setText(resourceMap.getString("textField_Comment.text")); // NOI18N
        textField_Comment.setName("textField_Comment"); // NOI18N

        button_QueryTaxpayer.setText(resourceMap.getString("button_QueryTaxpayer.text")); // NOI18N
        button_QueryTaxpayer.setName("button_QueryTaxpayer"); // NOI18N
        button_QueryTaxpayer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_QueryTaxpayerActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel_OtherDatasLayout = new javax.swing.GroupLayout(panel_OtherDatas);
        panel_OtherDatas.setLayout(panel_OtherDatasLayout);
        panel_OtherDatasLayout.setHorizontalGroup(
            panel_OtherDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_OtherDatasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_OtherDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(label_Comment, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(label_EuTaxNumber, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(label_BankAccountNumber, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(label_TaxNumber, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_OtherDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_OtherDatasLayout.createSequentialGroup()
                        .addComponent(textField_TaxNumber)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(button_QueryTaxpayer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(textField_BankAccountNumber)
                    .addComponent(textField_EuTaxNumber)
                    .addComponent(textField_Comment))
                .addContainerGap())
        );
        panel_OtherDatasLayout.setVerticalGroup(
            panel_OtherDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_OtherDatasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_OtherDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label_TaxNumber)
                    .addComponent(textField_TaxNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(button_QueryTaxpayer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_OtherDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label_EuTaxNumber)
                    .addComponent(textField_EuTaxNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(panel_OtherDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label_BankAccountNumber)
                    .addComponent(textField_BankAccountNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_OtherDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label_Comment)
                    .addComponent(textField_Comment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panel_SupplierOtherLayout = new javax.swing.GroupLayout(panel_SupplierOther);
        panel_SupplierOther.setLayout(panel_SupplierOtherLayout);
        panel_SupplierOtherLayout.setHorizontalGroup(
            panel_SupplierOtherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_SupplierOtherLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label_SupplierOther)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(panel_OtherDatas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panel_SupplierOtherLayout.setVerticalGroup(
            panel_SupplierOtherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_SupplierOtherLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label_SupplierOther)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel_OtherDatas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panel_SupplierInvoiceFooter.setBackground(resourceMap.getColor("panel_SupplierInvoiceFooter.background")); // NOI18N
        panel_SupplierInvoiceFooter.setName("panel_SupplierInvoiceFooter"); // NOI18N

        label_SupplierInvoiceFooter.setForeground(resourceMap.getColor("label_SupplierInvoiceFooter.foreground")); // NOI18N
        label_SupplierInvoiceFooter.setText(resourceMap.getString("label_SupplierInvoiceFooter.text")); // NOI18N
        label_SupplierInvoiceFooter.setName("label_SupplierInvoiceFooter"); // NOI18N

        panel_InvoiceFooter.setBackground(resourceMap.getColor("panel_InvoiceFooter.background")); // NOI18N
        panel_InvoiceFooter.setName("panel_InvoiceFooter"); // NOI18N

        scrollPane_InvoiceFooter.setName("scrollPane_InvoiceFooter"); // NOI18N

        textField_InvoiceFooter.setBackground(resourceMap.getColor("textField_CountryCode.background")); // NOI18N
        textField_InvoiceFooter.setColumns(20);
        textField_InvoiceFooter.setRows(5);
        textField_InvoiceFooter.setName("textField_InvoiceFooter"); // NOI18N
        scrollPane_InvoiceFooter.setViewportView(textField_InvoiceFooter);

        javax.swing.GroupLayout panel_InvoiceFooterLayout = new javax.swing.GroupLayout(panel_InvoiceFooter);
        panel_InvoiceFooter.setLayout(panel_InvoiceFooterLayout);
        panel_InvoiceFooterLayout.setHorizontalGroup(
            panel_InvoiceFooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_InvoiceFooterLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollPane_InvoiceFooter)
                .addContainerGap())
        );
        panel_InvoiceFooterLayout.setVerticalGroup(
            panel_InvoiceFooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_InvoiceFooterLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollPane_InvoiceFooter, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panel_SupplierInvoiceFooterLayout = new javax.swing.GroupLayout(panel_SupplierInvoiceFooter);
        panel_SupplierInvoiceFooter.setLayout(panel_SupplierInvoiceFooterLayout);
        panel_SupplierInvoiceFooterLayout.setHorizontalGroup(
            panel_SupplierInvoiceFooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_SupplierInvoiceFooterLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label_SupplierInvoiceFooter)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(panel_InvoiceFooter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panel_SupplierInvoiceFooterLayout.setVerticalGroup(
            panel_SupplierInvoiceFooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_SupplierInvoiceFooterLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label_SupplierInvoiceFooter)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel_InvoiceFooter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        button_SaveModification.setText(resourceMap.getString("button_SaveModification.text")); // NOI18N
        button_SaveModification.setName("button_SaveModification"); // NOI18N
        button_SaveModification.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_SaveModificationActionPerformed(evt);
            }
        });

        button_SaveAsNew.setText(resourceMap.getString("button_SaveAsNew.text")); // NOI18N
        button_SaveAsNew.setName("button_SaveAsNew"); // NOI18N
        button_SaveAsNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_SaveAsNewActionPerformed(evt);
            }
        });

        button_ExitSupplierView.setText(resourceMap.getString("button_ExitSupplierView.text")); // NOI18N
        button_ExitSupplierView.setName("button_ExitSupplierView"); // NOI18N
        button_ExitSupplierView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_ExitSupplierViewActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel_SupplierDatasLayout = new javax.swing.GroupLayout(panel_SupplierDatas);
        panel_SupplierDatas.setLayout(panel_SupplierDatasLayout);
        panel_SupplierDatasLayout.setHorizontalGroup(
            panel_SupplierDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_SupplierDatasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_SupplierDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panel_SupplierAddress, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panel_SupplierName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panel_SupplierOther, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panel_SupplierInvoiceFooter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_SupplierDatasLayout.createSequentialGroup()
                        .addGap(0, 116, Short.MAX_VALUE)
                        .addComponent(button_SaveModification, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(button_SaveAsNew, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(button_ExitSupplierView, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        panel_SupplierDatasLayout.setVerticalGroup(
            panel_SupplierDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_SupplierDatasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel_SupplierName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel_SupplierAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel_SupplierOther, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel_SupplierInvoiceFooter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_SupplierDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(button_ExitSupplierView, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(button_SaveAsNew, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(button_SaveModification, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel_SupplierDatas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel_SupplierDatas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void textField_CountryCodeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textField_CountryCodeKeyReleased
        if(textField_CountryCode.getText().length() == 0)
        {
            textField_Country.setText("");
        }
        else if(textField_CountryCode.getText().length() == 2)
        {
            textField_CountryCode.setText(textField_CountryCode.getText().toUpperCase());
            
            String result = Country.getCountryByCountryCode(textField_CountryCode.getText());
            textField_Country.setText(result);
        }
    }//GEN-LAST:event_textField_CountryCodeKeyReleased

    private void textField_CountryKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textField_CountryKeyReleased
        if(textField_Country.getText().length() == 0)
        {
            textField_CountryCode.setText("");
        }
        else
        {
            String capital = textField_Country.getText().substring(0, 1).toUpperCase();
            String lower = textField_Country.getText().substring(1).toLowerCase();
            textField_Country.setText(capital + lower);
            String result = Country.getCountryCodeByCountry(textField_Country.getText());
            textField_CountryCode.setText(result);
        }
    }//GEN-LAST:event_textField_CountryKeyReleased

    private void textField_PostalCodeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textField_PostalCodeKeyReleased
        if(textField_PostalCode.getText().length() == 4)
        {
            String city = City.getCityByPostalCode(textField_PostalCode.getText());
            textField_City.setText(city);
        }
        else
        {
            textField_City.setText("");
        }
    }//GEN-LAST:event_textField_PostalCodeKeyReleased

    private void button_SaveModificationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_SaveModificationActionPerformed
        LoadSupplier();
        supplier.Update();
        
        Close(RET_OK);
    }//GEN-LAST:event_button_SaveModificationActionPerformed

    private void button_SaveAsNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_SaveAsNewActionPerformed
        LoadSupplier();
        supplier.Insert();
        
        Close(RET_OK);
    }//GEN-LAST:event_button_SaveAsNewActionPerformed

    private void button_ExitSupplierViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_ExitSupplierViewActionPerformed
        Close(RET_CANCEL);
    }//GEN-LAST:event_button_ExitSupplierViewActionPerformed

    private void button_QueryTaxpayerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_QueryTaxpayerActionPerformed
        QueryTaxpayerResponseType responseType = App.nav.getTaxpayer(textField_TaxNumber.getText());
        
        if(responseType.isTaxpayerValidity())
        {
            supplier.setName(responseType.getTaxpayerData().getTaxpayerName());
            
            AddressType address = responseType.getTaxpayerData().getTaxpayerAddress();
            
            supplier.setCountryCode(address.getCountryCode());
            supplier.setPostalCode(address.getPostalCode());
            supplier.setCity(address.getCity());
            supplier.setStreet(address.getStreetName());
            supplier.setPublicPlace(address.getPublicPlaceCategory());
            supplier.setHouseNumber(address.getNumber());
            //supplier.setBuilding(address.getBuilding())
            //supplier.setStaircase(address.getStaircase());
            //supplier.setFloor(address.getFloor());
            //supplier.setDoor(address.getDoor());
            //supplier.setLotNumber(address.getLotNumber());
            
            setFields();
        }
        else
        {
            UserMessage message = new UserMessage("Figyelem!", "Nem érvényes vagy nem létező adószám");
        }
    }//GEN-LAST:event_button_QueryTaxpayerActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private customs.CustomButton button_ExitSupplierView;
    private customs.CustomButton button_QueryTaxpayer;
    private customs.CustomButton button_SaveAsNew;
    private customs.CustomButton button_SaveModification;
    private javax.swing.JLabel label_BankAccountNumber;
    private javax.swing.JLabel label_City;
    private javax.swing.JLabel label_Comment;
    private javax.swing.JLabel label_Country;
    private javax.swing.JLabel label_CountryCode;
    private javax.swing.JLabel label_EuTaxNumber;
    private javax.swing.JLabel label_PostalCode;
    private javax.swing.JLabel label_PublicPlace;
    private javax.swing.JLabel label_Street;
    private javax.swing.JLabel label_SupplierAddress;
    private javax.swing.JLabel label_SupplierInvoiceFooter;
    private javax.swing.JLabel label_SupplierName;
    private javax.swing.JLabel label_SupplierOther;
    private javax.swing.JLabel label_TaxNumber;
    private javax.swing.JLabel label_houseNumber;
    private javax.swing.JPanel panel_InvoiceFooter;
    private javax.swing.JPanel panel_OtherDatas;
    private javax.swing.JPanel panel_SupplierAddress;
    private javax.swing.JPanel panel_SupplierAddressDatas;
    private javax.swing.JPanel panel_SupplierDatas;
    private javax.swing.JPanel panel_SupplierInvoiceFooter;
    private javax.swing.JPanel panel_SupplierName;
    private javax.swing.JPanel panel_SupplierNameText;
    private javax.swing.JPanel panel_SupplierOther;
    private javax.swing.JScrollPane scrollPane_InvoiceFooter;
    private javax.swing.JTextField textField_BankAccountNumber;
    private javax.swing.JTextField textField_City;
    private javax.swing.JTextField textField_Comment;
    private javax.swing.JTextField textField_Country;
    private javax.swing.JTextField textField_CountryCode;
    private javax.swing.JTextField textField_EuTaxNumber;
    private javax.swing.JTextField textField_HouseNumber;
    private javax.swing.JTextArea textField_InvoiceFooter;
    private javax.swing.JTextField textField_PostalCode;
    private javax.swing.JTextField textField_PublicPlace;
    private javax.swing.JTextField textField_Street;
    private javax.swing.JTextField textField_SupplierName;
    private javax.swing.JTextField textField_TaxNumber;
    // End of variables declaration//GEN-END:variables
    
    public int getReturnStatus()
    {
        return returnStatus;
    }

    public Supplier getSupplier()
    {
        return supplier;
    }
    
    //SETTERS
    private void setFields()
    {
        textField_SupplierName.setText(supplier.getName());
        
        textField_CountryCode.setText(supplier.getCountryCode());
        textField_Country.setText(supplier.getCountry());
        textField_PostalCode.setText(supplier.getPostalCode());
        textField_City.setText(supplier.getCity());
        textField_Street.setText(supplier.getStreet());
        textField_PublicPlace.setText(supplier.getPublicPlace());
        textField_HouseNumber.setText(supplier.getHouseNumber());
        
        textField_TaxNumber.setText(supplier.getTaxNumber());
        textField_EuTaxNumber.setText(supplier.getEuTaxNumber());
        textField_BankAccountNumber.setText(supplier.getBankAccountNumber());
        textField_Comment.setText(supplier.getComment());
        
        textField_InvoiceFooter.setText(supplier.getInvoiceFooter());
    }
}