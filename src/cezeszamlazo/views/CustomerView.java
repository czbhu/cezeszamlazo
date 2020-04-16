package cezeszamlazo.views;

import cezeszamlazo.App;
import cezeszamlazo.HibaDialog;
import controller.City;
import controller.Country;
import invoice.Address;
import invoice.Customer;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JTextField;
import nav.NAV;
import onlineInvoice.AddressType;
import onlineInvoice.response.queryTaxpayer.QueryTaxpayerResponseType;

/**
 * @author Tomy
 */
public class CustomerView extends javax.swing.JDialog
{
    public static final int RET_CANCEL = 0;
    public static final int RET_OK = 1;
    
    private int returnStatus = RET_CANCEL;
    
    private Customer customer;
    
    private boolean grouping = false;
    
    public CustomerView()
    {
        initComponents();
        this.customer = new Customer();
        
        Init();
    }
    
    public CustomerView(Customer customer)
    {
        initComponents();
        this.customer = customer;
        
        Init();
    }
    
    public CustomerView(Customer customer, boolean grouping)
    {
        initComponents();
        this.customer = customer;
        this.grouping = grouping;
        
        Init();
    }
    
    private void SetTitle()
    {
        String title;
        
        if(customer.getName().length() == 0)
        {
            title = "Új vevő!";
            //button_SaveAsNew.setEnabled(true);
            //button_SaveChanges.setEnabled(false);
        }
        else
        {
            title = "Vevő módosítás!";
            //button_SaveChanges.setEnabled(true);
            //button_SaveAsNew.setEnabled(false);
        }
        
        setTitle(title);
    }
    
    private void Init()
    {
        SetTitle();
        SetViewDatas();
        
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;
        
        setLocation(x, y);
    }
    
    private void SetViewDatas()
    {
        textField_CustomerName.setText(customer.getName().trim());
        textField_CountryCode.setText(customer.getAddress().getCountryCode().trim());
        
        Address address = customer.getAddress();
        textField_Country.setText(address.getCountry().trim());
        
        if(textField_Country.getText().isEmpty())
        {
            String result = Country.getCountryByCountryCode(textField_CountryCode.getText());
            textField_Country.setText(result);
        }
        
        textField_PostalCode.setText(address.getPostalCode().trim());
        textField_City.setText(address.getCity().trim());
        textField_Street.setText(address.getStreet().trim());
        textField_PublicPlace.setText(address.getPublicPlace().trim());
        textField_HouseNumber.setText(address.getHouseNumber().trim());
        
        textField_TelNumber.setText(customer.getTelNumber().trim());
        textField_Email.setText(customer.getEmail().trim());
        textField_TaxNumber.setText(customer.getTaxNumber().trim());
        textField_EuTaxNumber.setText(customer.getEuTaxNumber().trim());
        textField_BankAccountNumber.setText(customer.getBankAccountNumber().trim());
        checkBox_ShowInInvoice.setSelected(customer.getShowInInvoice());
        comboBox_PaymentMethod.setSelectedIndex(customer.getPaymentMethod());
        checkBox_PaymentMethodRequired.setSelected(customer.getPaymentMethodRequired());
        textField_MaturityDays.setText((customer.getMaturityDays() + "").trim());
        spinner_Discount.setValue(customer.getDiscount());
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
    
    private void LoadCustomer()
    {
        customer.setName(textField_CustomerName.getText().trim());
        customer.getAddress().setCountryCode(textField_CountryCode.getText().trim());
        customer.getAddress().setCountry(textField_Country.getText().trim());
        customer.getAddress().setPostalCode(textField_PostalCode.getText().trim());
        customer.getAddress().setCity(textField_City.getText().trim());
        customer.getAddress().setStreet(textField_Street.getText().trim());
        customer.getAddress().setPublicPlace(textField_PublicPlace.getText().trim());
        customer.getAddress().setHouseNumber(textField_HouseNumber.getText().trim());
        customer.setTelNumber(textField_TelNumber.getText().trim());
        customer.setTaxNumber(textField_TaxNumber.getText().trim());
        customer.setEuTaxNumber(textField_EuTaxNumber.getText().trim());
        customer.setBankAccountNumber(textField_BankAccountNumber.getText().trim());
        customer.setShowInInvoice(checkBox_ShowInInvoice.isSelected());
        customer.setPaymentMethod(comboBox_PaymentMethod.getSelectedIndex());
        customer.setPaymentMethodRequired(checkBox_PaymentMethodRequired.isSelected());
        customer.setMaturityDays(Integer.parseInt(textField_MaturityDays.getText().trim()));
        customer.setDiscount(Integer.parseInt(spinner_Discount.getValue().toString()));
    }
    
    public void Open(boolean isModal)
    {
        setModal(isModal);
        setVisible(true);
    }
    
    private boolean CheckFields()
    {
        boolean good = false;
        
        if(textField_CustomerName.getText().length() < 1)
        {
            HibaDialog hd = new HibaDialog("A név mező üresen maradt", "OK", "Vissza");
        }
        else if(textField_CountryCode.getText().isEmpty())
        {
            EmptyFieldChangeView efcv = new EmptyFieldChangeView("Az országkód mező üresen maradt! Cseréli erre: ", "HU", "Csere", "Vissza", textField_CountryCode);

            if(efcv.getReturnStatus() == EmptyFieldChangeView.RET_OK)
            {
                SetCountryByCountryCode(textField_Country, textField_CountryCode);
            }
        }
        else if(textField_PostalCode.getText().isEmpty())
        {
            EmptyFieldChangeView efcv = new EmptyFieldChangeView("Az irányítószám mező üresen maradt! Cseréli erre: ", "0000", "Csere", "Vissza", textField_PostalCode);

            if(efcv.getReturnStatus() == EmptyFieldChangeView.RET_OK)
            {
                textField_City.setText("-");
            }
        }
        else if(textField_Street.getText().isEmpty())
        {
            EmptyFieldChangeView efcv = new EmptyFieldChangeView("Az utca mező üresen maradt! Cseréli erre: ", "-", "Csere", "Vissza", textField_Street);
        }
        else if(textField_PublicPlace.getText().isEmpty())
        {
            EmptyFieldChangeView efcv = new EmptyFieldChangeView("A közterület mező üresen maradt! Cseréli erre: ", "-", "Csere", "Vissza", textField_PublicPlace);
        }
        else
        {
            good = true;
        }
        
        return good;
    }
    
    private void SetCountryByCountryCode(JTextField country, JTextField countryCode)
    {
        String resultCountry = Country.getCountryByCountryCode(countryCode.getText());
        
        country.setText(resultCountry);
    }
    
    private void SetCountryCodeByCountry(JTextField countryCode, JTextField country)
    {
        String resultCountryCode = Country.getCountryCodeByCountry(country.getText());
        
        countryCode.setText(resultCountryCode);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel_CustomerDatas = new javax.swing.JPanel();
        panel_CustomerName = new javax.swing.JPanel();
        label_CustomerName = new javax.swing.JLabel();
        panel_CustomerNameText = new javax.swing.JPanel();
        textField_CustomerName = new javax.swing.JTextField();
        panel_CustomerAddress = new javax.swing.JPanel();
        label_CustomerAddress = new javax.swing.JLabel();
        panel_CustomerAddressDatas = new javax.swing.JPanel();
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
        label_HouseNumber = new javax.swing.JLabel();
        textField_HouseNumber = new javax.swing.JTextField();
        panel_CustomerOther = new javax.swing.JPanel();
        label_CustomerOther = new javax.swing.JLabel();
        panel_OtherDatas = new javax.swing.JPanel();
        label_TelNumber = new javax.swing.JLabel();
        textField_TelNumber = new javax.swing.JTextField();
        label_Email = new javax.swing.JLabel();
        textField_Email = new javax.swing.JTextField();
        label_TaxNumber = new javax.swing.JLabel();
        textField_TaxNumber = new javax.swing.JTextField();
        button_SelectByTaxNumber = new customs.CustomButton();
        label_EuTaxNumber = new javax.swing.JLabel();
        textField_EuTaxNumber = new javax.swing.JTextField();
        label_BankAccountNumber = new javax.swing.JLabel();
        textField_BankAccountNumber = new javax.swing.JTextField();
        checkBox_ShowInInvoice = new javax.swing.JCheckBox();
        label_PaymentMethod = new javax.swing.JLabel();
        comboBox_PaymentMethod = new customs.CustomCombobox();
        checkBox_PaymentMethodRequired = new javax.swing.JCheckBox();
        label_MaturityDays = new javax.swing.JLabel();
        textField_MaturityDays = new javax.swing.JTextField();
        label_Day = new javax.swing.JLabel();
        label_Discount = new javax.swing.JLabel();
        spinner_Discount = new javax.swing.JSpinner();
        label_Discount2 = new javax.swing.JLabel();
        button_ExitNewCustomerView = new customs.CustomButton();
        button_SaveAsNew = new customs.CustomButton();
        button_SaveChanges = new customs.CustomButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setName("Form"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(cezeszamlazo.App.class).getContext().getResourceMap(CustomerView.class);
        panel_CustomerDatas.setBackground(resourceMap.getColor("panel_CustomerDatas.background")); // NOI18N
        panel_CustomerDatas.setName("panel_CustomerDatas"); // NOI18N

        panel_CustomerName.setBackground(resourceMap.getColor("panel_CustomerName.background")); // NOI18N
        panel_CustomerName.setName("panel_CustomerName"); // NOI18N

        label_CustomerName.setForeground(resourceMap.getColor("label_CustomerName.foreground")); // NOI18N
        label_CustomerName.setText(resourceMap.getString("label_CustomerName.text")); // NOI18N
        label_CustomerName.setName("label_CustomerName"); // NOI18N

        panel_CustomerNameText.setBackground(resourceMap.getColor("panel_CustomerNameText.background")); // NOI18N
        panel_CustomerNameText.setName("panel_CustomerNameText"); // NOI18N

        textField_CustomerName.setBackground(resourceMap.getColor("textField_CustomerName.background")); // NOI18N
        textField_CustomerName.setText(resourceMap.getString("textField_CustomerName.text")); // NOI18N
        textField_CustomerName.setName("textField_CustomerName"); // NOI18N

        javax.swing.GroupLayout panel_CustomerNameTextLayout = new javax.swing.GroupLayout(panel_CustomerNameText);
        panel_CustomerNameText.setLayout(panel_CustomerNameTextLayout);
        panel_CustomerNameTextLayout.setHorizontalGroup(
            panel_CustomerNameTextLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_CustomerNameTextLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(textField_CustomerName)
                .addContainerGap())
        );
        panel_CustomerNameTextLayout.setVerticalGroup(
            panel_CustomerNameTextLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_CustomerNameTextLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(textField_CustomerName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panel_CustomerNameLayout = new javax.swing.GroupLayout(panel_CustomerName);
        panel_CustomerName.setLayout(panel_CustomerNameLayout);
        panel_CustomerNameLayout.setHorizontalGroup(
            panel_CustomerNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_CustomerNameLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label_CustomerName)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(panel_CustomerNameText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panel_CustomerNameLayout.setVerticalGroup(
            panel_CustomerNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_CustomerNameLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label_CustomerName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel_CustomerNameText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panel_CustomerAddress.setBackground(resourceMap.getColor("panel_CustomerAddress.background")); // NOI18N
        panel_CustomerAddress.setName("panel_CustomerAddress"); // NOI18N

        label_CustomerAddress.setForeground(resourceMap.getColor("label_CustomerAddress.foreground")); // NOI18N
        label_CustomerAddress.setText(resourceMap.getString("label_CustomerAddress.text")); // NOI18N
        label_CustomerAddress.setName("label_CustomerAddress"); // NOI18N

        panel_CustomerAddressDatas.setBackground(resourceMap.getColor("panel_CustomerAddressDatas.background")); // NOI18N
        panel_CustomerAddressDatas.setName("panel_CustomerAddressDatas"); // NOI18N

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

        textField_Country.setBackground(resourceMap.getColor("textField_Country.background")); // NOI18N
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

        textField_PostalCode.setBackground(resourceMap.getColor("textField_PostalCode.background")); // NOI18N
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

        textField_City.setBackground(resourceMap.getColor("textField_City.background")); // NOI18N
        textField_City.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textField_City.setText(resourceMap.getString("textField_City.text")); // NOI18N
        textField_City.setName("textField_City"); // NOI18N

        label_Street.setText(resourceMap.getString("label_Street.text")); // NOI18N
        label_Street.setName("label_Street"); // NOI18N

        textField_Street.setBackground(resourceMap.getColor("textField_Street.background")); // NOI18N
        textField_Street.setText(resourceMap.getString("textField_Street.text")); // NOI18N
        textField_Street.setName("textField_Street"); // NOI18N

        label_PublicPlace.setText(resourceMap.getString("label_PublicPlace.text")); // NOI18N
        label_PublicPlace.setName("label_PublicPlace"); // NOI18N

        textField_PublicPlace.setBackground(resourceMap.getColor("textField_PublicPlace.background")); // NOI18N
        textField_PublicPlace.setText(resourceMap.getString("textField_PublicPlace.text")); // NOI18N
        textField_PublicPlace.setName("textField_PublicPlace"); // NOI18N

        label_HouseNumber.setText(resourceMap.getString("label_HouseNumber.text")); // NOI18N
        label_HouseNumber.setName("label_HouseNumber"); // NOI18N

        textField_HouseNumber.setBackground(resourceMap.getColor("textField_HouseNumber.background")); // NOI18N
        textField_HouseNumber.setText(resourceMap.getString("textField_HouseNumber.text")); // NOI18N
        textField_HouseNumber.setName("textField_HouseNumber"); // NOI18N

        javax.swing.GroupLayout panel_CustomerAddressDatasLayout = new javax.swing.GroupLayout(panel_CustomerAddressDatas);
        panel_CustomerAddressDatas.setLayout(panel_CustomerAddressDatasLayout);
        panel_CustomerAddressDatasLayout.setHorizontalGroup(
            panel_CustomerAddressDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_CustomerAddressDatasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_CustomerAddressDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(label_HouseNumber, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(label_PublicPlace, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(label_Street, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(label_PostalCode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(label_CountryCode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_CustomerAddressDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_CustomerAddressDatasLayout.createSequentialGroup()
                        .addGroup(panel_CustomerAddressDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(textField_CountryCode)
                            .addComponent(textField_PostalCode))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panel_CustomerAddressDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(label_Country, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(label_City, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel_CustomerAddressDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(textField_Country)
                            .addComponent(textField_City)))
                    .addComponent(textField_Street)
                    .addComponent(textField_PublicPlace)
                    .addComponent(textField_HouseNumber))
                .addContainerGap())
        );
        panel_CustomerAddressDatasLayout.setVerticalGroup(
            panel_CustomerAddressDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_CustomerAddressDatasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_CustomerAddressDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label_CountryCode)
                    .addComponent(textField_CountryCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textField_Country, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label_Country))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_CustomerAddressDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label_PostalCode)
                    .addComponent(textField_PostalCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label_City)
                    .addComponent(textField_City, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_CustomerAddressDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label_Street)
                    .addComponent(textField_Street, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_CustomerAddressDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label_PublicPlace)
                    .addComponent(textField_PublicPlace, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_CustomerAddressDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label_HouseNumber)
                    .addComponent(textField_HouseNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panel_CustomerAddressLayout = new javax.swing.GroupLayout(panel_CustomerAddress);
        panel_CustomerAddress.setLayout(panel_CustomerAddressLayout);
        panel_CustomerAddressLayout.setHorizontalGroup(
            panel_CustomerAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_CustomerAddressLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label_CustomerAddress)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(panel_CustomerAddressDatas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panel_CustomerAddressLayout.setVerticalGroup(
            panel_CustomerAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_CustomerAddressLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label_CustomerAddress)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel_CustomerAddressDatas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panel_CustomerOther.setBackground(resourceMap.getColor("panel_CustomerOther.background")); // NOI18N
        panel_CustomerOther.setName("panel_CustomerOther"); // NOI18N

        label_CustomerOther.setForeground(resourceMap.getColor("label_CustomerOther.foreground")); // NOI18N
        label_CustomerOther.setText(resourceMap.getString("label_CustomerOther.text")); // NOI18N
        label_CustomerOther.setName("label_CustomerOther"); // NOI18N

        panel_OtherDatas.setBackground(resourceMap.getColor("panel_OtherDatas.background")); // NOI18N
        panel_OtherDatas.setName("panel_OtherDatas"); // NOI18N

        label_TelNumber.setText(resourceMap.getString("label_TelNumber.text")); // NOI18N
        label_TelNumber.setName("label_TelNumber"); // NOI18N

        textField_TelNumber.setBackground(resourceMap.getColor("textField_TelNumber.background")); // NOI18N
        textField_TelNumber.setText(resourceMap.getString("textField_TelNumber.text")); // NOI18N
        textField_TelNumber.setName("textField_TelNumber"); // NOI18N

        label_Email.setText(resourceMap.getString("label_Email.text")); // NOI18N
        label_Email.setName("label_Email"); // NOI18N

        textField_Email.setBackground(resourceMap.getColor("textField_Email.background")); // NOI18N
        textField_Email.setText(resourceMap.getString("textField_Email.text")); // NOI18N
        textField_Email.setName("textField_Email"); // NOI18N

        label_TaxNumber.setText(resourceMap.getString("label_TaxNumber.text")); // NOI18N
        label_TaxNumber.setName("label_TaxNumber"); // NOI18N

        textField_TaxNumber.setBackground(resourceMap.getColor("textField_TaxNumber.background")); // NOI18N
        textField_TaxNumber.setText(resourceMap.getString("textField_TaxNumber.text")); // NOI18N
        textField_TaxNumber.setName("textField_TaxNumber"); // NOI18N

        button_SelectByTaxNumber.setText(resourceMap.getString("button_SelectByTaxNumber.text")); // NOI18N
        button_SelectByTaxNumber.setName("button_SelectByTaxNumber"); // NOI18N
        button_SelectByTaxNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_SelectByTaxNumberActionPerformed(evt);
            }
        });

        label_EuTaxNumber.setText(resourceMap.getString("label_EuTaxNumber.text")); // NOI18N
        label_EuTaxNumber.setName("label_EuTaxNumber"); // NOI18N

        textField_EuTaxNumber.setBackground(resourceMap.getColor("textField_EuTaxNumber.background")); // NOI18N
        textField_EuTaxNumber.setText(resourceMap.getString("textField_EuTaxNumber.text")); // NOI18N
        textField_EuTaxNumber.setName("textField_EuTaxNumber"); // NOI18N

        label_BankAccountNumber.setText(resourceMap.getString("label_BankAccountNumber.text")); // NOI18N
        label_BankAccountNumber.setName("label_BankAccountNumber"); // NOI18N

        textField_BankAccountNumber.setBackground(resourceMap.getColor("textField_BankAccountNumber.background")); // NOI18N
        textField_BankAccountNumber.setText(resourceMap.getString("textField_BankAccountNumber.text")); // NOI18N
        textField_BankAccountNumber.setName("textField_BankAccountNumber"); // NOI18N

        checkBox_ShowInInvoice.setText(resourceMap.getString("checkBox_ShowInInvoice.text")); // NOI18N
        checkBox_ShowInInvoice.setName("checkBox_ShowInInvoice"); // NOI18N
        checkBox_ShowInInvoice.setOpaque(false);

        label_PaymentMethod.setText(resourceMap.getString("label_PaymentMethod.text")); // NOI18N
        label_PaymentMethod.setName("label_PaymentMethod"); // NOI18N

        comboBox_PaymentMethod.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Készpénz", "Átutalás", "Utánvét" }));
        comboBox_PaymentMethod.setName("comboBox_PaymentMethod"); // NOI18N

        checkBox_PaymentMethodRequired.setText(resourceMap.getString("checkBox_PaymentMethodRequired.text")); // NOI18N
        checkBox_PaymentMethodRequired.setName("checkBox_PaymentMethodRequired"); // NOI18N
        checkBox_PaymentMethodRequired.setOpaque(false);

        label_MaturityDays.setText(resourceMap.getString("label_MaturityDays.text")); // NOI18N
        label_MaturityDays.setName("label_MaturityDays"); // NOI18N

        textField_MaturityDays.setBackground(resourceMap.getColor("textField_MaturityDays.background")); // NOI18N
        textField_MaturityDays.setText(resourceMap.getString("textField_MaturityDays.text")); // NOI18N
        textField_MaturityDays.setName("textField_MaturityDays"); // NOI18N
        textField_MaturityDays.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textField_MaturityDaysKeyReleased(evt);
            }
        });

        label_Day.setText(resourceMap.getString("label_Day.text")); // NOI18N
        label_Day.setName("label_Day"); // NOI18N

        label_Discount.setText(resourceMap.getString("label_Discount.text")); // NOI18N
        label_Discount.setName("label_Discount"); // NOI18N

        spinner_Discount.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        spinner_Discount.setName("spinner_Discount"); // NOI18N

        label_Discount2.setText(resourceMap.getString("label_Discount2.text")); // NOI18N
        label_Discount2.setName("label_Discount2"); // NOI18N

        javax.swing.GroupLayout panel_OtherDatasLayout = new javax.swing.GroupLayout(panel_OtherDatas);
        panel_OtherDatas.setLayout(panel_OtherDatasLayout);
        panel_OtherDatasLayout.setHorizontalGroup(
            panel_OtherDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_OtherDatasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_OtherDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(label_Discount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(label_MaturityDays, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(label_PaymentMethod, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(label_BankAccountNumber, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(label_EuTaxNumber, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(label_TaxNumber, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(label_Email, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(label_TelNumber, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_OtherDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(textField_TelNumber)
                    .addComponent(textField_Email)
                    .addGroup(panel_OtherDatasLayout.createSequentialGroup()
                        .addComponent(textField_TaxNumber)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(button_SelectByTaxNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(textField_EuTaxNumber)
                    .addGroup(panel_OtherDatasLayout.createSequentialGroup()
                        .addComponent(textField_BankAccountNumber)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(checkBox_ShowInInvoice))
                    .addGroup(panel_OtherDatasLayout.createSequentialGroup()
                        .addComponent(comboBox_PaymentMethod, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(checkBox_PaymentMethodRequired))
                    .addGroup(panel_OtherDatasLayout.createSequentialGroup()
                        .addComponent(textField_MaturityDays)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label_Day))
                    .addGroup(panel_OtherDatasLayout.createSequentialGroup()
                        .addComponent(spinner_Discount)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label_Discount2)))
                .addContainerGap())
        );
        panel_OtherDatasLayout.setVerticalGroup(
            panel_OtherDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_OtherDatasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_OtherDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label_TelNumber)
                    .addComponent(textField_TelNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_OtherDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label_Email)
                    .addComponent(textField_Email, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_OtherDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label_TaxNumber)
                    .addComponent(textField_TaxNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(button_SelectByTaxNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_OtherDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label_EuTaxNumber)
                    .addComponent(textField_EuTaxNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_OtherDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label_BankAccountNumber)
                    .addComponent(textField_BankAccountNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(checkBox_ShowInInvoice))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_OtherDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label_PaymentMethod)
                    .addComponent(comboBox_PaymentMethod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(checkBox_PaymentMethodRequired))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_OtherDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label_MaturityDays)
                    .addComponent(textField_MaturityDays, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label_Day))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_OtherDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label_Discount)
                    .addComponent(spinner_Discount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label_Discount2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panel_CustomerOtherLayout = new javax.swing.GroupLayout(panel_CustomerOther);
        panel_CustomerOther.setLayout(panel_CustomerOtherLayout);
        panel_CustomerOtherLayout.setHorizontalGroup(
            panel_CustomerOtherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_CustomerOtherLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label_CustomerOther)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(panel_OtherDatas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panel_CustomerOtherLayout.setVerticalGroup(
            panel_CustomerOtherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_CustomerOtherLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label_CustomerOther)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel_OtherDatas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        button_ExitNewCustomerView.setText(resourceMap.getString("button_ExitNewCustomerView.text")); // NOI18N
        button_ExitNewCustomerView.setName("button_ExitNewCustomerView"); // NOI18N
        button_ExitNewCustomerView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_ExitNewCustomerViewActionPerformed(evt);
            }
        });

        button_SaveAsNew.setText(resourceMap.getString("button_SaveAsNew.text")); // NOI18N
        button_SaveAsNew.setName("button_SaveAsNew"); // NOI18N
        button_SaveAsNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_SaveAsNewActionPerformed(evt);
            }
        });

        button_SaveChanges.setText(resourceMap.getString("button_SaveChanges.text")); // NOI18N
        button_SaveChanges.setName("button_SaveChanges"); // NOI18N
        button_SaveChanges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_SaveChangesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel_CustomerDatasLayout = new javax.swing.GroupLayout(panel_CustomerDatas);
        panel_CustomerDatas.setLayout(panel_CustomerDatasLayout);
        panel_CustomerDatasLayout.setHorizontalGroup(
            panel_CustomerDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_CustomerDatasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_CustomerDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panel_CustomerOther, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panel_CustomerName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panel_CustomerAddress, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_CustomerDatasLayout.createSequentialGroup()
                        .addGap(0, 26, Short.MAX_VALUE)
                        .addComponent(button_SaveChanges, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(button_SaveAsNew, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(button_ExitNewCustomerView, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        panel_CustomerDatasLayout.setVerticalGroup(
            panel_CustomerDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_CustomerDatasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel_CustomerName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel_CustomerAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel_CustomerOther, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_CustomerDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(button_ExitNewCustomerView, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(button_SaveAsNew, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(button_SaveChanges, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panel_CustomerDatas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel_CustomerDatas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        Close(RET_CANCEL);
    }//GEN-LAST:event_formWindowClosed

    private void button_SelectByTaxNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_SelectByTaxNumberActionPerformed
        String taxNumber = textField_TaxNumber.getText();
        
        QueryTaxpayerResponseType responseType = App.nav.getTaxpayer(taxNumber);
        
        System.err.println("responseType.isTaxpayerValidity(): " + responseType.isTaxpayerValidity());
        
        if(responseType.isTaxpayerValidity())
        {
            customer.setName(responseType.getTaxpayerData().getTaxpayerName());
            
            AddressType address = responseType.getTaxpayerData().getTaxpayerAddress();
            
            customer.getAddress().setCountryCode(address.getCountryCode());
            customer.getAddress().setPostalCode(address.getPostalCode());
            customer.getAddress().setCity(address.getCity());
            customer.getAddress().setStreet(address.getStreetName());
            customer.getAddress().setPublicPlace(address.getPublicPlaceCategory());
            customer.getAddress().setHouseNumber(address.getNumber());
            //customer.setBuilding(address.getBuilding())
            //customer.setStaircase(address.getStaircase());
            //customer.setFloor(address.getFloor());
            //customer.setDoor(address.getDoor());
            //customer.setLotNumber(address.getLotNumber());
            customer.setTaxNumber(taxNumber);
            
            SetViewDatas();
        }
        else
        {
            UserMessage message = new UserMessage("Figyelem!", "Nem érvényes vagy nem létező adószám");
        }
    }//GEN-LAST:event_button_SelectByTaxNumberActionPerformed

    private void button_SaveChangesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_SaveChangesActionPerformed
        if(CheckFields())
        {
            LoadCustomer();
            customer.Update();
            Close(RET_OK);
        }
    }//GEN-LAST:event_button_SaveChangesActionPerformed

    private void button_SaveAsNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_SaveAsNewActionPerformed
        if(CheckFields())
        {
            LoadCustomer();
            customer.Insert();
            Close(RET_OK);
        }
    }//GEN-LAST:event_button_SaveAsNewActionPerformed

    private void button_ExitNewCustomerViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_ExitNewCustomerViewActionPerformed
        Close(RET_CANCEL);
    }//GEN-LAST:event_button_ExitNewCustomerViewActionPerformed

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

    private void textField_MaturityDaysKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textField_MaturityDaysKeyReleased
        if(textField_MaturityDays.getText().isEmpty())
        {
            textField_MaturityDays.setText("0");
        }
    }//GEN-LAST:event_textField_MaturityDaysKeyReleased

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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private customs.CustomButton button_ExitNewCustomerView;
    private customs.CustomButton button_SaveAsNew;
    private customs.CustomButton button_SaveChanges;
    private customs.CustomButton button_SelectByTaxNumber;
    private javax.swing.JCheckBox checkBox_PaymentMethodRequired;
    private javax.swing.JCheckBox checkBox_ShowInInvoice;
    private customs.CustomCombobox comboBox_PaymentMethod;
    private javax.swing.JLabel label_BankAccountNumber;
    private javax.swing.JLabel label_City;
    private javax.swing.JLabel label_Country;
    private javax.swing.JLabel label_CountryCode;
    private javax.swing.JLabel label_CustomerAddress;
    private javax.swing.JLabel label_CustomerName;
    private javax.swing.JLabel label_CustomerOther;
    private javax.swing.JLabel label_Day;
    private javax.swing.JLabel label_Discount;
    private javax.swing.JLabel label_Discount2;
    private javax.swing.JLabel label_Email;
    private javax.swing.JLabel label_EuTaxNumber;
    private javax.swing.JLabel label_HouseNumber;
    private javax.swing.JLabel label_MaturityDays;
    private javax.swing.JLabel label_PaymentMethod;
    private javax.swing.JLabel label_PostalCode;
    private javax.swing.JLabel label_PublicPlace;
    private javax.swing.JLabel label_Street;
    private javax.swing.JLabel label_TaxNumber;
    private javax.swing.JLabel label_TelNumber;
    private javax.swing.JPanel panel_CustomerAddress;
    private javax.swing.JPanel panel_CustomerAddressDatas;
    private javax.swing.JPanel panel_CustomerDatas;
    private javax.swing.JPanel panel_CustomerName;
    private javax.swing.JPanel panel_CustomerNameText;
    private javax.swing.JPanel panel_CustomerOther;
    private javax.swing.JPanel panel_OtherDatas;
    private javax.swing.JSpinner spinner_Discount;
    private javax.swing.JTextField textField_BankAccountNumber;
    private javax.swing.JTextField textField_City;
    private javax.swing.JTextField textField_Country;
    private javax.swing.JTextField textField_CountryCode;
    private javax.swing.JTextField textField_CustomerName;
    private javax.swing.JTextField textField_Email;
    private javax.swing.JTextField textField_EuTaxNumber;
    private javax.swing.JTextField textField_HouseNumber;
    private javax.swing.JTextField textField_MaturityDays;
    private javax.swing.JTextField textField_PostalCode;
    private javax.swing.JTextField textField_PublicPlace;
    private javax.swing.JTextField textField_Street;
    private javax.swing.JTextField textField_TaxNumber;
    private javax.swing.JTextField textField_TelNumber;
    // End of variables declaration//GEN-END:variables

    //GETTERS
    public int getReturnStatus()
    {
        return returnStatus;
    }

    public Customer getCustomer()
    {
        return customer;
    }

    public int getMaturityDays()
    {
        String maturityDays = textField_MaturityDays.getText();
        
        if(maturityDays.isEmpty())
        {
            return 0;
        }
        else
        {
            return Integer.parseInt(maturityDays);
        }
    }
    
    //SETTERS

    public void setCustomer(Customer customer)
    {
        this.customer = customer;
        SetViewDatas();
    }

    public void setPaymentMethod(int paymentMethod)
    {
        comboBox_PaymentMethod.setSelectedIndex(paymentMethod);
    }
}