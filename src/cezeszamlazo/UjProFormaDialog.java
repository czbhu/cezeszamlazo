/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

 /*
 * NewOkCancelDialog.java
 *
 * Created on 2014.08.19., 14:34:36
 */
package cezeszamlazo;

import cezeszamlazo.controller.Functions;
import cezeszamlazo.controller.ProForma;
import cezeszamlazo.controller.ProFormaTermek;
import cezeszamlazo.controller.Szamla;
import cezeszamlazo.controller.SzamlaTermek;
import cezeszamlazo.controller.Vevo;
import cezeszamlazo.database.Query;
import cezeszamlazo.functions.SzamlaFunctions;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author pappadam
 */
public class UjProFormaDialog extends javax.swing.JDialog
{
    public enum SzamlaType      
    {                           
        UJ, MASOLAT, DEVIZA     
    }                           
    /**
     * A return status code - returned if Cancel button has been pressed
     */
    public static final int RET_CANCEL = 0;
    /**
     * A return status code - returned if OK button has been pressed
     */
    public static final int RET_OK = 1;
    
    private List termekek = new LinkedList<SzamlaTermek>();
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private String sorszam = "", penznem = "", vevoid = "0", helyesbitett = "", helyesbitettTeljesites = "";
    private int termekid = -1;
    private double kozeparfolyam = 1.0, scale = 1.0;
    private boolean deviza = false, b = true, bezar = false, modosit = true/*, storno = false*/;//comment
    
    private ProFormaTermek aktualis = new ProFormaTermek(0);
    private ProForma proForma;
    private FolyamatbanDialog folyamatbanDialog;
    private SzamlaType szamlaType;
    private Szamla szamla;

    /**
     * Creates new form NewOkCancelDialog
     */
    public UjProFormaDialog()
    {
        initComponents();

        proForma = new ProForma();
        
        bezar = true;
        
        penznem = "Ft";
        kozeparfolyam = 1.0;
        deviza = false;
        this.szamlaType = szamlaType.UJ;
        
        penznemLabel1.setText(penznem);
        penznemLabel2.setText(penznem);
        penznemLabel3.setText(penznem);
        
        szamlaFejlec.setText("Díjbekérő");
        
        InvoiceGroupUpdate();
        SupplierComboBoxUpdate(0);
        VatUpdate();
        
        if(App.args.length > 0)
        {
            //kell-e??;
            System.err.println("Az app.args.length > 0");
        }

        init("Díjbekérő");

        // Close the dialog when Esc is pressed
        String cancelName = "cancel";
        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), cancelName);
        ActionMap actionMap = getRootPane().getActionMap();
        actionMap.put(cancelName, new AbstractAction()
        {
            public void actionPerformed(ActionEvent e)
            {
                doClose(RET_CANCEL);
            }
        });
    }
    
    public UjProFormaDialog(String penznem, boolean deviza, double kozeparfolyam)
    {
        initComponents();
        
        penznemLabel1.setText(penznem);
        penznemLabel2.setText(penznem);
        penznemLabel3.setText(penznem);
        
        this.penznem = penznem;
        this.deviza = deviza;
        this.kozeparfolyam = kozeparfolyam;
        
        if (deviza)
        {
            this.szamlaType = SzamlaType.DEVIZA;
        }
        else
        {
            this.szamlaType = SzamlaType.UJ;
        }
        
        szamlaFejlec.setText("Díjbekérő");
        
        InvoiceGroupUpdate();
        SupplierComboBoxUpdate(0);
        VatUpdate();
        
        init("Díjbekérő");
        
         // Close the dialog when Esc is pressed
        String cancelName = "cancel";
        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), cancelName);
        ActionMap actionMap = getRootPane().getActionMap();
        actionMap.put(cancelName, new AbstractAction()
        {
            public void actionPerformed(ActionEvent e)
            {
                doClose(RET_CANCEL);
            }
        });
    }

    public UjProFormaDialog(Szamla szamla)
    {
        initComponents();
        
        this.szamlaType = szamlaType.MASOLAT;
        this.szamla = szamla;
        this.penznem = (szamla.getValuta().equalsIgnoreCase("huf") || szamla.getValuta().equalsIgnoreCase("Ft") ? "Ft" : szamla.getValuta().toUpperCase());
        this.deviza = szamla.isDeviza();
        this.kozeparfolyam = szamla.getKozeparfolyam();
        
        penznemLabel1.setText(penznem);
        penznemLabel2.setText(penznem);
        penznemLabel3.setText(penznem);
        
        szamlaFejlec.setText("Díjbekérő");
        
        CustomerUpdate();
        InvoiceProductsUpdate();
        InvoiceGroupUpdate();        
        supplierComboBox.setSelectedItem(this.szamla.getSorszamasID());        
        VatUpdate();
        
        init("Díjbekérő");
        
         // Close the dialog when Esc is pressed
        String cancelName = "cancel";
        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), cancelName);
        ActionMap actionMap = getRootPane().getActionMap();
        actionMap.put(cancelName, new AbstractAction()
        {
            public void actionPerformed(ActionEvent e)
            {
                doClose(RET_CANCEL);
            }
        });
    }
    
    /**
     * @return the return status of this dialog - one of RET_OK or RET_CANCEL
     */
    public int getReturnStatus()
    {
        return returnStatus;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        osszegzoDialog = new javax.swing.JDialog();
        jPanel10 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        pay_payBack = new javax.swing.JLabel();
        say = new javax.swing.JLabel();
        summaryPay_payBack = new javax.swing.JLabel();
        summaryGrossText = new javax.swing.JLabel();
        summaryVatText = new javax.swing.JLabel();
        summaryNetText = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jPanel11 = new javax.swing.JPanel();
        nyomtatas = new javax.swing.JCheckBox();
        nyomtatasPeldany = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        elonezetLabel = new javax.swing.JLabel();
        pdfKeszites = new javax.swing.JCheckBox();
        mentesButton = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        szamlaTermekPopupMenu = new javax.swing.JPopupMenu();
        modositMenuItem = new javax.swing.JMenuItem();
        vtszMenuItem = new javax.swing.JMenuItem();
        torolMenuItem = new javax.swing.JMenuItem();
        supplierDialog = new javax.swing.JDialog();
        jLabel103 = new javax.swing.JLabel();
        supplierComment = new javax.swing.JTextField();
        jLabel105 = new javax.swing.JLabel();
        jLabel111 = new javax.swing.JLabel();
        jLabel104 = new javax.swing.JLabel();
        supplierTaxNumber = new javax.swing.JTextField();
        jLabel102 = new javax.swing.JLabel();
        supplierBankAccountNumber = new javax.swing.JTextField();
        jLabel101 = new javax.swing.JLabel();
        supplierPostalCode = new javax.swing.JTextField();
        supplierStreet = new javax.swing.JTextField();
        supplierCity = new javax.swing.JTextField();
        jLabel106 = new javax.swing.JLabel();
        supplierName = new javax.swing.JTextField();
        exitSupplierDatasDialog = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        supplierEUTaxNumber = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        supplierInvoiceFooter = new javax.swing.JTextArea();
        customerDialog = new javax.swing.JDialog();
        vevoOkButton1 = new javax.swing.JButton();
        vevoMentesUjButton1 = new javax.swing.JButton();
        vevoModositasMentes1 = new javax.swing.JButton();
        jPanel14 = new javax.swing.JPanel();
        jLabel65 = new javax.swing.JLabel();
        jLabel66 = new javax.swing.JLabel();
        jLabel67 = new javax.swing.JLabel();
        jLabel68 = new javax.swing.JLabel();
        jLabel69 = new javax.swing.JLabel();
        customerBankAccountNumberText = new javax.swing.JTextField();
        customerEUTaxNumberText = new javax.swing.JTextField();
        customerAppearsOnInvoiceCheckBox = new javax.swing.JCheckBox();
        customerTaxNumberText = new javax.swing.JTextField();
        customerMaturityText = new javax.swing.JTextField();
        customerPaymentMethodComboBox = new javax.swing.JComboBox();
        customerIsPaymentMethodRequiredCheckBox = new javax.swing.JCheckBox();
        jLabel70 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jLabel71 = new javax.swing.JLabel();
        jLabel72 = new javax.swing.JLabel();
        jLabel73 = new javax.swing.JLabel();
        jLabel74 = new javax.swing.JLabel();
        jLabel75 = new javax.swing.JLabel();
        jLabel76 = new javax.swing.JLabel();
        jLabel77 = new javax.swing.JLabel();
        customerPhoneNumberText = new javax.swing.JTextField();
        customerEmailText = new javax.swing.JTextField();
        customerCountryCode = new javax.swing.JTextField();
        customerStreetText = new javax.swing.JTextField();
        customerPostalCodeText = new javax.swing.JTextField();
        customerCityText = new javax.swing.JTextField();
        customerNameText = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        customerCountryName = new javax.swing.JTextField();
        szamlaFejlec = new javax.swing.JLabel();
        szamlaSorszam = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        szallitoAdatok = new javax.swing.JButton();
        supplierComboBox = new javax.swing.JComboBox();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        maturity = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        vevoChangeButton = new javax.swing.JButton();
        szamlaCsoport = new javax.swing.JComboBox();
        customerTextField = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        kelt = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel6 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        vtszTeszor = new javax.swing.JTextField();
        egysegar = new javax.swing.JTextField();
        mennyiseg = new javax.swing.JTextField();
        cikkszam = new javax.swing.JTextField();
        termek = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        penznemLabel2 = new javax.swing.JLabel();
        netto = new javax.swing.JTextField();
        brutto = new javax.swing.JTextField();
        penznemLabel3 = new javax.swing.JLabel();
        penznemLabel1 = new javax.swing.JLabel();
        vatComboBox = new javax.swing.JComboBox();
        jLabel20 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        hozzaadModosit = new javax.swing.JButton();
        kiurit = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        vtszTeszorTallozas = new javax.swing.JButton();
        mee = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        invoiceProductsTable = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        allNetText = new javax.swing.JLabel();
        allVatText = new javax.swing.JLabel();
        allGrossText = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        megjegyzesText = new javax.swing.JTextArea();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        lablecText = new javax.swing.JTextArea();
        jPanel13 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();

        osszegzoDialog.setModal(true);
        osszegzoDialog.setName("osszegzoDialog"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(cezeszamlazo.App.class).getContext().getResourceMap(UjProFormaDialog.class);
        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(resourceMap.getColor("jPanel10.border.border.lineColor")), resourceMap.getString("jPanel10.border.title"))); // NOI18N
        jPanel10.setName("jPanel10"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jLabel27.setText(resourceMap.getString("jLabel27.text")); // NOI18N
        jLabel27.setName("jLabel27"); // NOI18N

        jLabel29.setText(resourceMap.getString("jLabel29.text")); // NOI18N
        jLabel29.setName("jLabel29"); // NOI18N

        pay_payBack.setName("pay_payBack"); // NOI18N

        say.setName("say"); // NOI18N

        summaryPay_payBack.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        summaryPay_payBack.setName("summaryPay_payBack"); // NOI18N

        summaryGrossText.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        summaryGrossText.setName("summaryGrossText"); // NOI18N

        summaryVatText.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        summaryVatText.setName("summaryVatText"); // NOI18N

        summaryNetText.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        summaryNetText.setName("summaryNetText"); // NOI18N

        jSeparator3.setForeground(resourceMap.getColor("jSeparator3.foreground")); // NOI18N
        jSeparator3.setName("jSeparator3"); // NOI18N

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator3, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel29)
                            .addComponent(jLabel27)
                            .addComponent(jLabel2))
                        .addGap(60, 60, 60)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(summaryGrossText, javax.swing.GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE)
                            .addComponent(summaryVatText, javax.swing.GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE)
                            .addComponent(summaryNetText, javax.swing.GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE)))
                    .addComponent(say, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                        .addComponent(pay_payBack, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(summaryPay_payBack, javax.swing.GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(summaryNetText))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(summaryVatText))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(summaryGrossText))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pay_payBack)
                    .addComponent(summaryPay_payBack))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(say)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(resourceMap.getColor("jPanel11.border.border.lineColor")), resourceMap.getString("jPanel11.border.title"))); // NOI18N
        jPanel11.setName("jPanel11"); // NOI18N

        nyomtatas.setSelected(true);
        nyomtatas.setText(resourceMap.getString("nyomtatas.text")); // NOI18N
        nyomtatas.setName("nyomtatas"); // NOI18N
        nyomtatas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nyomtatasActionPerformed(evt);
            }
        });

        nyomtatasPeldany.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        nyomtatasPeldany.setText(resourceMap.getString("nyomtatasPeldany.text")); // NOI18N
        nyomtatasPeldany.setName("nyomtatasPeldany"); // NOI18N

        jLabel37.setText(resourceMap.getString("jLabel37.text")); // NOI18N
        jLabel37.setName("jLabel37"); // NOI18N

        elonezetLabel.setToolTipText(resourceMap.getString("elonezetLabel.toolTipText")); // NOI18N
        elonezetLabel.setBorder(javax.swing.BorderFactory.createLineBorder(resourceMap.getColor("elonezetLabel.border.lineColor"))); // NOI18N
        elonezetLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        elonezetLabel.setName("elonezetLabel"); // NOI18N
        elonezetLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                elonezetLabelMouseReleased(evt);
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                elonezetLabelMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                elonezetLabelMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                elonezetLabelMousePressed(evt);
            }
        });

        pdfKeszites.setText(resourceMap.getString("pdfKeszites.text")); // NOI18N
        pdfKeszites.setEnabled(false);
        pdfKeszites.setName("pdfKeszites"); // NOI18N

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(nyomtatas)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nyomtatasPeldany, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel37)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(elonezetLabel))
                    .addComponent(pdfKeszites))
                .addContainerGap(127, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nyomtatas)
                    .addComponent(nyomtatasPeldany, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel37)
                    .addComponent(elonezetLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pdfKeszites)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        mentesButton.setText(resourceMap.getString("mentesButton.text")); // NOI18N
        mentesButton.setName("mentesButton"); // NOI18N
        mentesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mentesButtonActionPerformed(evt);
            }
        });

        jButton5.setText(resourceMap.getString("jButton5.text")); // NOI18N
        jButton5.setName("jButton5"); // NOI18N
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout osszegzoDialogLayout = new javax.swing.GroupLayout(osszegzoDialog.getContentPane());
        osszegzoDialog.getContentPane().setLayout(osszegzoDialogLayout);
        osszegzoDialogLayout.setHorizontalGroup(
            osszegzoDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(osszegzoDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(osszegzoDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, osszegzoDialogLayout.createSequentialGroup()
                        .addComponent(mentesButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton5)))
                .addContainerGap())
        );
        osszegzoDialogLayout.setVerticalGroup(
            osszegzoDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(osszegzoDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(osszegzoDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mentesButton)
                    .addComponent(jButton5))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        szamlaTermekPopupMenu.setName("szamlaTermekPopupMenu"); // NOI18N

        modositMenuItem.setText(resourceMap.getString("modositMenuItem.text")); // NOI18N
        modositMenuItem.setName("modositMenuItem"); // NOI18N
        modositMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modositMenuItemActionPerformed(evt);
            }
        });
        szamlaTermekPopupMenu.add(modositMenuItem);

        vtszMenuItem.setText(resourceMap.getString("vtszMenuItem.text")); // NOI18N
        vtszMenuItem.setName("vtszMenuItem"); // NOI18N
        vtszMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vtszMenuItemActionPerformed(evt);
            }
        });
        szamlaTermekPopupMenu.add(vtszMenuItem);

        torolMenuItem.setText(resourceMap.getString("torolMenuItem.text")); // NOI18N
        torolMenuItem.setName("torolMenuItem"); // NOI18N
        torolMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                torolMenuItemActionPerformed(evt);
            }
        });
        szamlaTermekPopupMenu.add(torolMenuItem);

        supplierDialog.setMinimumSize(new java.awt.Dimension(400, 201));
        supplierDialog.setModal(true);
        supplierDialog.setName("supplierDialog"); // NOI18N

        jLabel103.setText(resourceMap.getString("jLabel103.text")); // NOI18N
        jLabel103.setName("jLabel103"); // NOI18N

        supplierComment.setName("supplierComment"); // NOI18N

        jLabel105.setText(resourceMap.getString("jLabel105.text")); // NOI18N
        jLabel105.setName("jLabel105"); // NOI18N

        jLabel111.setText(resourceMap.getString("jLabel111.text")); // NOI18N
        jLabel111.setName("jLabel111"); // NOI18N

        jLabel104.setText(resourceMap.getString("jLabel104.text")); // NOI18N
        jLabel104.setName("jLabel104"); // NOI18N

        supplierTaxNumber.setName("supplierTaxNumber"); // NOI18N

        jLabel102.setText(resourceMap.getString("jLabel102.text")); // NOI18N
        jLabel102.setName("jLabel102"); // NOI18N

        supplierBankAccountNumber.setName("supplierBankAccountNumber"); // NOI18N

        jLabel101.setText(resourceMap.getString("jLabel101.text")); // NOI18N
        jLabel101.setName("jLabel101"); // NOI18N

        supplierPostalCode.setName("supplierPostalCode"); // NOI18N
        supplierPostalCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                supplierPostalCodeKeyReleased(evt);
            }
        });

        supplierStreet.setName("supplierStreet"); // NOI18N

        supplierCity.setName("supplierCity"); // NOI18N

        jLabel106.setText(resourceMap.getString("jLabel106.text")); // NOI18N
        jLabel106.setName("jLabel106"); // NOI18N

        supplierName.setName("supplierName"); // NOI18N

        exitSupplierDatasDialog.setText(resourceMap.getString("exitSupplierDatasDialog.text")); // NOI18N
        exitSupplierDatasDialog.setName("exitSupplierDatasDialog"); // NOI18N
        exitSupplierDatasDialog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitSupplierDatasDialogActionPerformed(evt);
            }
        });

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        supplierEUTaxNumber.setName("supplierEUTaxNumber"); // NOI18N

        jLabel17.setText(resourceMap.getString("jLabel17.text")); // NOI18N
        jLabel17.setName("jLabel17"); // NOI18N

        jScrollPane4.setName("jScrollPane4"); // NOI18N

        supplierInvoiceFooter.setColumns(20);
        supplierInvoiceFooter.setFont(resourceMap.getFont("supplierInvoiceFooter.font")); // NOI18N
        supplierInvoiceFooter.setRows(5);
        supplierInvoiceFooter.setName("supplierInvoiceFooter"); // NOI18N
        supplierInvoiceFooter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                supplierInvoiceFooterKeyReleased(evt);
            }
        });
        jScrollPane4.setViewportView(supplierInvoiceFooter);

        javax.swing.GroupLayout supplierDialogLayout = new javax.swing.GroupLayout(supplierDialog.getContentPane());
        supplierDialog.getContentPane().setLayout(supplierDialogLayout);
        supplierDialogLayout.setHorizontalGroup(
            supplierDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(supplierDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(supplierDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(exitSupplierDatasDialog, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(supplierDialogLayout.createSequentialGroup()
                        .addGroup(supplierDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(supplierDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel104, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel103, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel102, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel101, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(25, 25, 25)
                        .addGroup(supplierDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(supplierStreet, javax.swing.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE)
                            .addGroup(supplierDialogLayout.createSequentialGroup()
                                .addComponent(supplierPostalCode, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel106)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(supplierCity, javax.swing.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE))
                            .addComponent(supplierName, javax.swing.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE)
                            .addComponent(supplierTaxNumber, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE)
                            .addComponent(supplierEUTaxNumber, javax.swing.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE)))
                    .addGroup(supplierDialogLayout.createSequentialGroup()
                        .addGroup(supplierDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(supplierDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel105, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel111))
                            .addComponent(jLabel17))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(supplierDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(supplierBankAccountNumber, javax.swing.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE)
                            .addComponent(supplierComment, javax.swing.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE)
                            .addComponent(jScrollPane4))))
                .addContainerGap())
        );
        supplierDialogLayout.setVerticalGroup(
            supplierDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(supplierDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(supplierDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel101)
                    .addComponent(supplierName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(supplierDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel102)
                    .addComponent(supplierPostalCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel106)
                    .addComponent(supplierCity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(supplierDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel103)
                    .addComponent(supplierStreet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(supplierDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel104)
                    .addComponent(supplierTaxNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(supplierDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(supplierEUTaxNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(supplierDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel105)
                    .addComponent(supplierBankAccountNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(supplierDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel111)
                    .addComponent(supplierComment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(supplierDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(supplierDialogLayout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 82, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(exitSupplierDatasDialog)
                .addContainerGap())
        );

        customerDialog.setModal(true);
        customerDialog.setName("customerDialog"); // NOI18N

        vevoOkButton1.setText(resourceMap.getString("vevoOkButton1.text")); // NOI18N
        vevoOkButton1.setName("vevoOkButton1"); // NOI18N
        vevoOkButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vevoOkButton1ActionPerformed(evt);
            }
        });

        vevoMentesUjButton1.setText(resourceMap.getString("vevoMentesUjButton1.text")); // NOI18N
        vevoMentesUjButton1.setName("vevoMentesUjButton1"); // NOI18N
        vevoMentesUjButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vevoMentesUjButton1ActionPerformed(evt);
            }
        });

        vevoModositasMentes1.setText(resourceMap.getString("vevoModositasMentes1.text")); // NOI18N
        vevoModositasMentes1.setName("vevoModositasMentes1"); // NOI18N
        vevoModositasMentes1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vevoModositasMentes1ActionPerformed(evt);
            }
        });

        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jPanel14.border.title"))); // NOI18N
        jPanel14.setName("jPanel14"); // NOI18N

        jLabel65.setText(resourceMap.getString("jLabel65.text")); // NOI18N
        jLabel65.setName("jLabel65"); // NOI18N

        jLabel66.setText(resourceMap.getString("jLabel66.text")); // NOI18N
        jLabel66.setName("jLabel66"); // NOI18N

        jLabel67.setText(resourceMap.getString("jLabel67.text")); // NOI18N
        jLabel67.setName("jLabel67"); // NOI18N

        jLabel68.setText(resourceMap.getString("jLabel68.text")); // NOI18N
        jLabel68.setName("jLabel68"); // NOI18N

        jLabel69.setText(resourceMap.getString("jLabel69.text")); // NOI18N
        jLabel69.setName("jLabel69"); // NOI18N

        customerBankAccountNumberText.setName("customerBankAccountNumberText"); // NOI18N

        customerEUTaxNumberText.setName("customerEUTaxNumberText"); // NOI18N

        customerAppearsOnInvoiceCheckBox.setText(resourceMap.getString("customerAppearsOnInvoiceCheckBox.text")); // NOI18N
        customerAppearsOnInvoiceCheckBox.setName("customerAppearsOnInvoiceCheckBox"); // NOI18N

        customerTaxNumberText.setName("customerTaxNumberText"); // NOI18N

        customerMaturityText.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        customerMaturityText.setText(resourceMap.getString("customerMaturityText.text")); // NOI18N
        customerMaturityText.setName("customerMaturityText"); // NOI18N
        customerMaturityText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                customerMaturityTextKeyReleased(evt);
            }
        });

        customerPaymentMethodComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Készpénz", "Átutalás", "Utánvét" }));
        customerPaymentMethodComboBox.setName("customerPaymentMethodComboBox"); // NOI18N
        customerPaymentMethodComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customerPaymentMethodComboBoxActionPerformed(evt);
            }
        });

        customerIsPaymentMethodRequiredCheckBox.setText(resourceMap.getString("customerIsPaymentMethodRequiredCheckBox.text")); // NOI18N
        customerIsPaymentMethodRequiredCheckBox.setName("customerIsPaymentMethodRequiredCheckBox"); // NOI18N
        customerIsPaymentMethodRequiredCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customerIsPaymentMethodRequiredCheckBoxActionPerformed(evt);
            }
        });

        jLabel70.setText(resourceMap.getString("jLabel70.text")); // NOI18N
        jLabel70.setName("jLabel70"); // NOI18N

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel69)
                    .addComponent(jLabel68)
                    .addComponent(jLabel67)
                    .addComponent(jLabel66)
                    .addComponent(jLabel65))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(customerPaymentMethodComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(customerIsPaymentMethodRequiredCheckBox))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(customerMaturityText, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel70))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(customerTaxNumberText, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
                            .addComponent(customerEUTaxNumberText, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
                            .addComponent(customerBankAccountNumberText, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(customerAppearsOnInvoiceCheckBox))))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel65)
                    .addComponent(customerPaymentMethodComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(customerIsPaymentMethodRequiredCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel66)
                    .addComponent(customerMaturityText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel70))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel67)
                    .addComponent(customerTaxNumberText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel68)
                    .addComponent(customerEUTaxNumberText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel69)
                    .addComponent(customerBankAccountNumberText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(customerAppearsOnInvoiceCheckBox))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel15.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jPanel15.border.title"))); // NOI18N
        jPanel15.setName("jPanel15"); // NOI18N

        jLabel71.setText(resourceMap.getString("jLabel71.text")); // NOI18N
        jLabel71.setName("jLabel71"); // NOI18N

        jLabel72.setText(resourceMap.getString("jLabel72.text")); // NOI18N
        jLabel72.setName("jLabel72"); // NOI18N

        jLabel73.setText(resourceMap.getString("jLabel73.text")); // NOI18N
        jLabel73.setName("jLabel73"); // NOI18N

        jLabel74.setText(resourceMap.getString("jLabel74.text")); // NOI18N
        jLabel74.setName("jLabel74"); // NOI18N

        jLabel75.setText(resourceMap.getString("jLabel75.text")); // NOI18N
        jLabel75.setName("jLabel75"); // NOI18N

        jLabel76.setText(resourceMap.getString("jLabel76.text")); // NOI18N
        jLabel76.setName("jLabel76"); // NOI18N

        jLabel77.setText(resourceMap.getString("jLabel77.text")); // NOI18N
        jLabel77.setName("jLabel77"); // NOI18N

        customerPhoneNumberText.setName("customerPhoneNumberText"); // NOI18N

        customerEmailText.setName("customerEmailText"); // NOI18N

        customerCountryCode.setName("customerCountryCode"); // NOI18N
        customerCountryCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                customerCountryCodeKeyReleased(evt);
            }
        });

        customerStreetText.setName("customerStreetText"); // NOI18N

        customerPostalCodeText.setName("customerPostalCodeText"); // NOI18N
        customerPostalCodeText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                customerPostalCodeTextKeyReleased(evt);
            }
        });

        customerCityText.setName("customerCityText"); // NOI18N

        customerNameText.setName("customerNameText"); // NOI18N
        customerNameText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                customerNameTextKeyReleased(evt);
            }
        });

        jLabel22.setText(resourceMap.getString("jLabel22.text")); // NOI18N
        jLabel22.setName("jLabel22"); // NOI18N

        customerCountryName.setName("customerCountryName"); // NOI18N
        customerCountryName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                customerCountryNameKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel75, javax.swing.GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE)
                    .addComponent(jLabel74, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel72, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel71, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel76, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel77, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(customerPhoneNumberText, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(customerStreetText, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel15Layout.createSequentialGroup()
                        .addComponent(customerPostalCodeText, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel73)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(customerCityText))
                    .addComponent(customerNameText, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(customerEmailText, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel15Layout.createSequentialGroup()
                        .addComponent(customerCountryCode, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel22)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(customerCountryName)))
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel71)
                    .addComponent(customerNameText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel72)
                    .addComponent(customerPostalCodeText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(customerCityText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel73))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel74)
                    .addComponent(customerStreetText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel75)
                    .addComponent(customerCountryCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22)
                    .addComponent(customerCountryName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel76)
                    .addComponent(customerPhoneNumberText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel77)
                    .addComponent(customerEmailText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout customerDialogLayout = new javax.swing.GroupLayout(customerDialog.getContentPane());
        customerDialog.getContentPane().setLayout(customerDialogLayout);
        customerDialogLayout.setHorizontalGroup(
            customerDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(customerDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(customerDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, customerDialogLayout.createSequentialGroup()
                        .addComponent(vevoModositasMentes1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(vevoMentesUjButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 103, Short.MAX_VALUE)
                        .addComponent(vevoOkButton1)))
                .addContainerGap())
        );
        customerDialogLayout.setVerticalGroup(
            customerDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(customerDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(customerDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(vevoOkButton1)
                    .addComponent(vevoMentesUjButton1)
                    .addComponent(vevoModositasMentes1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setName("Form"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        szamlaFejlec.setFont(resourceMap.getFont("szamlaFejlec.font")); // NOI18N
        szamlaFejlec.setText(resourceMap.getString("szamlaFejlec.text")); // NOI18N
        szamlaFejlec.setName("szamlaFejlec"); // NOI18N

        szamlaSorszam.setFont(resourceMap.getFont("szamlaSorszam.font")); // NOI18N
        szamlaSorszam.setText(resourceMap.getString("szamlaSorszam.text")); // NOI18N
        szamlaSorszam.setToolTipText(resourceMap.getString("szamlaSorszam.toolTipText")); // NOI18N
        szamlaSorszam.setName("szamlaSorszam"); // NOI18N

        jTabbedPane1.setName("jTabbedPane1"); // NOI18N

        jPanel1.setName("jPanel1"); // NOI18N

        szallitoAdatok.setText(resourceMap.getString("szallitoAdatok.text")); // NOI18N
        szallitoAdatok.setName("szallitoAdatok"); // NOI18N
        szallitoAdatok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                szallitoAdatokActionPerformed(evt);
            }
        });

        supplierComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "szállító" }));
        supplierComboBox.setName("supplierComboBox"); // NOI18N
        supplierComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supplierComboBoxActionPerformed(evt);
            }
        });

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Fejléc"));
        jPanel5.setName("jPanel5"); // NOI18N
        jPanel5.setPreferredSize(new java.awt.Dimension(575, 146));

        jLabel3.setFont(resourceMap.getFont("kelt.font")); // NOI18N
        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        maturity.setEditable(false);
        maturity.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        maturity.setEnabled(false);
        maturity.setName("maturity"); // NOI18N

        jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        jLabel8.setFont(resourceMap.getFont("kelt.font")); // NOI18N
        jLabel8.setText(resourceMap.getString("jLabel8.text")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N

        jLabel10.setIcon(resourceMap.getIcon("jLabel10.icon")); // NOI18N
        jLabel10.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel10.setName("jLabel10"); // NOI18N
        jLabel10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel10MouseClicked(evt);
            }
        });

        vevoChangeButton.setText(resourceMap.getString("vevoChangeButton.text")); // NOI18N
        vevoChangeButton.setMargin(new java.awt.Insets(2, 4, 2, 4));
        vevoChangeButton.setMaximumSize(new java.awt.Dimension(25, 23));
        vevoChangeButton.setMinimumSize(new java.awt.Dimension(25, 23));
        vevoChangeButton.setName("vevoChangeButton"); // NOI18N
        vevoChangeButton.setPreferredSize(new java.awt.Dimension(25, 23));
        vevoChangeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vevoChangeButtonActionPerformed(evt);
            }
        });

        szamlaCsoport.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "csoport" }));
        szamlaCsoport.setName("szamlaCsoport"); // NOI18N

        customerTextField.setName("customerTextField"); // NOI18N
        customerTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                customerTextFieldKeyReleased(evt);
            }
        });

        jButton3.setText(resourceMap.getString("jButton3.text")); // NOI18N
        jButton3.setName("jButton3"); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        kelt.setFont(resourceMap.getFont("kelt.font")); // NOI18N
        kelt.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        kelt.setText(resourceMap.getString("kelt.text")); // NOI18N
        kelt.setToolTipText(resourceMap.getString("kelt.toolTipText")); // NOI18N
        kelt.setName("kelt"); // NOI18N

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator1.setName("jSeparator1"); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(szamlaCsoport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(customerTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(vevoChangeButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(maturity, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                    .addComponent(kelt, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE))
                .addGap(6, 6, 6)
                .addComponent(jLabel10)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(customerTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton3)
                                    .addComponent(vevoChangeButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(kelt)))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(szamlaCsoport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                            .addGap(37, 37, 37)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(maturity)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Termék adatok"));
        jPanel6.setName("jPanel6"); // NOI18N

        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        jLabel12.setText(resourceMap.getString("jLabel12.text")); // NOI18N
        jLabel12.setName("jLabel12"); // NOI18N

        jLabel13.setText(resourceMap.getString("jLabel13.text")); // NOI18N
        jLabel13.setName("jLabel13"); // NOI18N

        jLabel14.setText(resourceMap.getString("jLabel14.text")); // NOI18N
        jLabel14.setName("jLabel14"); // NOI18N

        jLabel15.setText(resourceMap.getString("jLabel15.text")); // NOI18N
        jLabel15.setName("jLabel15"); // NOI18N

        vtszTeszor.setName("vtszTeszor"); // NOI18N

        egysegar.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        egysegar.setText("0");
        egysegar.setName("egysegar"); // NOI18N
        egysegar.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                egysegarFocusLost(evt);
            }
        });
        egysegar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                egysegarKeyReleased(evt);
            }
        });

        mennyiseg.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        mennyiseg.setText("1");
        mennyiseg.setName("mennyiseg"); // NOI18N
        mennyiseg.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                mennyisegFocusLost(evt);
            }
        });
        mennyiseg.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                mennyisegKeyReleased(evt);
            }
        });

        cikkszam.setName("cikkszam"); // NOI18N

        termek.setName("termek"); // NOI18N

        jLabel16.setText(resourceMap.getString("jLabel16.text")); // NOI18N
        jLabel16.setName("jLabel16"); // NOI18N

        penznemLabel2.setText(resourceMap.getString("penznemLabel2.text")); // NOI18N
        penznemLabel2.setName("penznemLabel2"); // NOI18N

        netto.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        netto.setText("0");
        netto.setName("netto"); // NOI18N
        netto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                nettoFocusLost(evt);
            }
        });
        netto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                nettoKeyReleased(evt);
            }
        });

        brutto.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        brutto.setText("0");
        brutto.setName("brutto"); // NOI18N
        brutto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                bruttoFocusLost(evt);
            }
        });
        brutto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                bruttoKeyReleased(evt);
            }
        });

        penznemLabel3.setText(resourceMap.getString("penznemLabel3.text")); // NOI18N
        penznemLabel3.setName("penznemLabel3"); // NOI18N

        penznemLabel1.setText(resourceMap.getString("penznemLabel1.text")); // NOI18N
        penznemLabel1.setName("penznemLabel1"); // NOI18N

        vatComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "afa" }));
        vatComboBox.setName("vatComboBox"); // NOI18N
        vatComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vatComboBoxActionPerformed(evt);
            }
        });

        jLabel20.setText(resourceMap.getString("jLabel20.text")); // NOI18N
        jLabel20.setName("jLabel20"); // NOI18N

        jLabel23.setText(resourceMap.getString("jLabel23.text")); // NOI18N
        jLabel23.setName("jLabel23"); // NOI18N

        jLabel24.setText(resourceMap.getString("jLabel24.text")); // NOI18N
        jLabel24.setName("jLabel24"); // NOI18N

        jLabel25.setText(resourceMap.getString("jLabel25.text")); // NOI18N
        jLabel25.setName("jLabel25"); // NOI18N

        hozzaadModosit.setFont(resourceMap.getFont("hozzaadModosit.font")); // NOI18N
        hozzaadModosit.setText(resourceMap.getString("hozzaadModosit.text")); // NOI18N
        hozzaadModosit.setName("hozzaadModosit"); // NOI18N
        hozzaadModosit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hozzaadModositActionPerformed(evt);
            }
        });

        kiurit.setText(resourceMap.getString("kiurit.text")); // NOI18N
        kiurit.setName("kiurit"); // NOI18N
        kiurit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kiuritActionPerformed(evt);
            }
        });

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator2.setName("jSeparator2"); // NOI18N

        vtszTeszorTallozas.setText(resourceMap.getString("vtszTeszorTallozas.text")); // NOI18N
        vtszTeszorTallozas.setMargin(new java.awt.Insets(2, 4, 2, 4));
        vtszTeszorTallozas.setName("vtszTeszorTallozas"); // NOI18N
        vtszTeszorTallozas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vtszTeszorTallozasActionPerformed(evt);
            }
        });

        mee.setName("mee"); // NOI18N
        mee.setText("db");

        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel31.setIcon(resourceMap.getIcon("jLabel31.icon")); // NOI18N
        jLabel31.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel31.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel31.setName("jLabel31"); // NOI18N
        jLabel31.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel31MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel31MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel31MouseExited(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(egysegar, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
                                    .addComponent(mennyiseg, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                                        .addComponent(jLabel16)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(mee, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE))
                                    .addComponent(penznemLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(vtszTeszor, javax.swing.GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(vtszTeszorTallozas))
                            .addComponent(cikkszam, javax.swing.GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel23, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(netto)
                                            .addComponent(brutto))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(penznemLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(penznemLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addComponent(kiurit)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(hozzaadModosit))))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(18, 18, Short.MAX_VALUE)
                                .addComponent(vatComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel20))))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(termek, javax.swing.GroupLayout.DEFAULT_SIZE, 447, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                        .addComponent(termek, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(netto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(penznemLabel2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(penznemLabel3)
                                    .addComponent(brutto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel12)
                                    .addComponent(cikkszam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel13)
                                    .addComponent(mennyiseg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel16)
                                    .addComponent(mee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel14)
                                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(egysegar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(penznemLabel1)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel15)
                                .addComponent(vtszTeszor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(vtszTeszorTallozas))
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(hozzaadModosit)
                                .addComponent(kiurit))))
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                            .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(vatComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel20)))))
                .addContainerGap())
        );

        jScrollPane3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Számla termékek"));
        jScrollPane3.setName("jScrollPane3"); // NOI18N

        invoiceProductsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Termék", "Cikkszám", "Mennyiség", "Mee.", "Egységár", "VTSZ/TESZOR", "Nettó ár", "ÁFA", "Áfa érték", "Bruttó ár"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, true, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        invoiceProductsTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        invoiceProductsTable.setName("invoiceProductsTable"); // NOI18N
        invoiceProductsTable.getTableHeader().setReorderingAllowed(false);
        invoiceProductsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                invoiceProductsTableMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                invoiceProductsTableMouseReleased(evt);
            }
        });
        jScrollPane3.setViewportView(invoiceProductsTable);

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Összesen"));
        jPanel7.setName("jPanel7"); // NOI18N

        allNetText.setName("allNetText"); // NOI18N

        allVatText.setName("allVatText"); // NOI18N

        allGrossText.setName("allGrossText"); // NOI18N

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(allNetText, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(allVatText, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(allGrossText, javax.swing.GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(allNetText, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(allVatText, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(allGrossText, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 585, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(szallitoAdatok)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(supplierComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 585, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(szallitoAdatok)
                    .addComponent(supplierComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(13, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Fejléc és termék adatok", jPanel1);

        jPanel2.setName("jPanel2"); // NOI18N

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Megjegyzés"));
        jPanel3.setName("jPanel3"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        megjegyzesText.setColumns(20);
        megjegyzesText.setLineWrap(true);
        megjegyzesText.setRows(5);
        megjegyzesText.setWrapStyleWord(true);
        megjegyzesText.setName("megjegyzesText"); // NOI18N
        jScrollPane1.setViewportView(megjegyzesText);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 555, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0))));
        jPanel4.setName("jPanel4"); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        lablecText.setColumns(20);
        lablecText.setLineWrap(true);
        lablecText.setRows(5);
        lablecText.setWrapStyleWord(true);
        lablecText.setName("lablecText"); // NOI18N
        jScrollPane2.setViewportView(lablecText);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 563, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Megjegyzés", jPanel2);

        jPanel13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel13.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel13.setName("jPanel13"); // NOI18N
        jPanel13.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel13MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel13MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanel13MouseExited(evt);
            }
        });

        jLabel19.setIcon(resourceMap.getIcon("jLabel19.icon")); // NOI18N
        jLabel19.setName("jLabel19"); // NOI18N

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel12.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel12.setName("jPanel12"); // NOI18N
        jPanel12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel12MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel12MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanel12MouseExited(evt);
            }
        });

        jLabel18.setIcon(resourceMap.getIcon("jLabel18.icon")); // NOI18N
        jLabel18.setText(resourceMap.getString("jLabel18.text")); // NOI18N
        jLabel18.setName("jLabel18"); // NOI18N

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel18)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(szamlaFejlec)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(szamlaSorszam))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(szamlaFejlec)
                    .addComponent(szamlaSorszam))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Closes the dialog
     */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        doClose(RET_CANCEL);
    }//GEN-LAST:event_closeDialog

private void szallitoAdatokActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_szallitoAdatokActionPerformed
    //hiba
    //SzallitoAdatokDialog szad = new SzallitoAdatokDialog(proForma.getSzallito());
    //proForma.setSzallito(szad.getSzallito());
    nyit(supplierDialog, "Szállító adatok");
}//GEN-LAST:event_szallitoAdatokActionPerformed

private void supplierComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplierComboBoxActionPerformed
    if (b)
    {
        LoadSupplier();
        sorszamEllenorzes();
        Label label = (Label) supplierComboBox.getSelectedItem();
        Query query = new Query.QueryBuilder()
                .select("db, ev, elotag")
                .from("szamlazo_szamla_sorszam")
                .where("id = (SELECT sorszamid from szamlazo_ceg_adatok WHERE id = " + label.getId() + ")")
                .order("")
                .build();
        Object[][] s = App.db.select(query.getQuery());
        String ujSorszam = String.valueOf(s[0][1]) + "/" + (Integer.parseInt(String.valueOf(s[0][0])) + 1);
        if (!String.valueOf(s[0][2]).isEmpty())
        {
            ujSorszam = String.valueOf(s[0][2]) + " " + ujSorszam;
        }
        szamlaSorszam.setText(ujSorszam + (deviza ? "/V" : ""));
        if (!ujSorszam.matches(sorszam) && !proForma.getSorszam().isEmpty()) {
            HibaDialog hd = new HibaDialog("A sorszámozás megváltozott!\nAz új számla sorszám: " + ujSorszam, "Ok", "");
        }
        //proForma.getSorszam() = ujSorszam;
    }
}//GEN-LAST:event_supplierComboBoxActionPerformed

private void jLabel10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel10MouseClicked
    CalendarDialog cd = new CalendarDialog(null, maturity);
}//GEN-LAST:event_jLabel10MouseClicked

private void vevoChangeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vevoChangeButtonActionPerformed
    UgyfelListaDialog ugyfelListaDialog = new UgyfelListaDialog();
    if (ugyfelListaDialog.getReturnStatus() == 1)
    {
        vevoid = ugyfelListaDialog.getId();
        CustomerUpdate();
    }
}//GEN-LAST:event_vevoChangeButtonActionPerformed

private void customerTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_customerTextFieldKeyReleased
    //vevoNevText.setText(vevo.getText());
}//GEN-LAST:event_customerTextFieldKeyReleased

private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
    nyit(customerDialog, "Vevő adatok");
}//GEN-LAST:event_jButton3ActionPerformed

private void egysegarFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_egysegarFocusLost
    JTextField field = (JTextField) evt.getSource();
    if (field.getText().isEmpty()) {
        field.setText("0");
        szamolMennyisegAlapjan();
    }
}//GEN-LAST:event_egysegarFocusLost

private void egysegarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_egysegarKeyReleased
    csakszam(evt, true);
    if (!egysegar.getText().isEmpty()) {
        szamolMennyisegAlapjan();
    }
}//GEN-LAST:event_egysegarKeyReleased

private void mennyisegFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_mennyisegFocusLost
    JTextField field = (JTextField) evt.getSource();
    if (field.getText().isEmpty()) {
        field.setText("0");
        szamolMennyisegAlapjan();
    }
}//GEN-LAST:event_mennyisegFocusLost

private void mennyisegKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_mennyisegKeyReleased
    csakszam(evt, true);
    if (!mennyiseg.getText().isEmpty()) {
        szamolMennyisegAlapjan();
    }
}//GEN-LAST:event_mennyisegKeyReleased

private void nettoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nettoFocusLost
    JTextField field = (JTextField) evt.getSource();
    if (field.getText().isEmpty()) {
        field.setText("0");
        szamolNettoAlapjan();
    }
}//GEN-LAST:event_nettoFocusLost

private void nettoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nettoKeyReleased
    csakszam(evt, true);
    if (!netto.getText().isEmpty()) {
        szamolNettoAlapjan();
    }
}//GEN-LAST:event_nettoKeyReleased

private void bruttoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_bruttoFocusLost
    JTextField field = (JTextField) evt.getSource();
    if (field.getText().isEmpty()) {
        field.setText("0");
        szamolBruttoAlapjan();
    }
}//GEN-LAST:event_bruttoFocusLost

private void bruttoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bruttoKeyReleased
    csakszam(evt, deviza);
    if (!brutto.getText().isEmpty()) {
        szamolBruttoAlapjan();
    }
}//GEN-LAST:event_bruttoKeyReleased

private void vatComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vatComboBoxActionPerformed
    if (b) {
        szamolMennyisegAlapjan();
    }
}//GEN-LAST:event_vatComboBoxActionPerformed

private void hozzaadModositActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hozzaadModositActionPerformed
    if (termek.getText().isEmpty())
    {
        HibaDialog hd = new HibaDialog("Nincs megadva termék név!", "Ok", "");
    }
    else if (Double.parseDouble(mennyiseg.getText()) == 0)
    {
        HibaDialog hd = new HibaDialog("Nulla a mennyiség!", "Ok", "");
    }
    /*else if (vtszTeszor.getText().isEmpty())
    {
        HibaDialog hd = new HibaDialog("Nincs megadva VTSZ/TESZOR szám!", "Ok", "");
    }*/
    else
    {
        Label l = (Label) vatComboBox.getSelectedItem();
        ProFormaTermek szt
                = new ProFormaTermek(
                        0,
                        0,
                        termek.getText(),
                        cikkszam.getText(),
                        Double.parseDouble(mennyiseg.getText().replace(",", ".")),
                        mee.getText(),
                        Double.parseDouble(egysegar.getText().replace(",", ".")),
                        0,
                        Integer.parseInt(l.getId()),
                        vtszTeszor.getText());
        termekek.add(szt);
        szamlaTermekekFrissites();
    }
}//GEN-LAST:event_hozzaadModositActionPerformed

private void kiuritActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kiuritActionPerformed
    kiurit();
}//GEN-LAST:event_kiuritActionPerformed

private void vtszTeszorTallozasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vtszTeszorTallozasActionPerformed
    VtszTeszorListaDialog v = new VtszTeszorListaDialog();
    if (v.getReturnStatus() == 1) {
        vtszTeszor.setText(v.getVtszTeszor());
        Label l;
        int j = 0;
        for (int i = 0; i < vatComboBox.getItemCount(); i++) {
            l = (Label) vatComboBox.getItemAt(i);
            if (l.getName().equalsIgnoreCase(v.getAfa())) {
                j = i;
                break;
            }
        }
        vatComboBox.setSelectedIndex(j);
    }
}//GEN-LAST:event_vtszTeszorTallozasActionPerformed

private void jLabel31MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel31MouseClicked
    TermekListaDialog t = new TermekListaDialog();
    if (t.getReturnStatus() == 1)
    {
        termek.setText(t.getTermek());
        mee.setText(t.getMee());
        egysegar.setText(t.getEgysegar());
        cikkszam.setText(t.getCikkszam());
        vtszTeszor.setText(t.getVtszTeszor());
        for (int i = 0; i < vatComboBox.getItemCount(); i++)
        {
            Label l = (Label) vatComboBox.getItemAt(i);
            if (l.getId().equalsIgnoreCase(t.getAfa()))
            {
                vatComboBox.setSelectedIndex(i);
                break;
            }
        }
    }
}//GEN-LAST:event_jLabel31MouseClicked

private void jLabel31MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel31MouseEntered
    jLabel31.setBackground(Color.decode("#abd043"));
}//GEN-LAST:event_jLabel31MouseEntered

private void jLabel31MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel31MouseExited
    jLabel31.setBackground(Color.decode("#f0f0f0"));
}//GEN-LAST:event_jLabel31MouseExited

private void invoiceProductsTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_invoiceProductsTableMouseClicked
    if (evt.getClickCount() == 2 && evt.getButton() == MouseEvent.BUTTON1 && modosit) {
        int[] rows = invoiceProductsTable.getSelectedRows();
        termekid = rows[0];
        aktualis = (ProFormaTermek) proForma.getTermekek().get(termekid);
        termek.setText(aktualis.getTermek());
        cikkszam.setText(aktualis.getTermekKod());
        mennyiseg.setText(aktualis.getMennyiseg() + "");
        mee.setText(aktualis.getMennyisegiEgyseg());
        egysegar.setText(aktualis.getEgysegar() + "");
        vtszTeszor.setText(aktualis.getVtszTeszor());
        Label l;
        int j = 0;
        for (int i = 0; i < vatComboBox.getItemCount(); i++) {
            l = (Label) vatComboBox.getItemAt(i);
            if (Double.parseDouble(l.getName()) == aktualis.getAfa()) {
                j = i;
                break;
            }
        }
        vatComboBox.setSelectedIndex(j);
        hozzaadModosit.setText("Módosít");
    }
}//GEN-LAST:event_invoiceProductsTableMouseClicked

private void invoiceProductsTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_invoiceProductsTableMouseReleased
    if (evt.isPopupTrigger() && modosit)
    {
        JTable source = (JTable) evt.getSource();
        int row = source.rowAtPoint(evt.getPoint());
        int column = source.columnAtPoint(evt.getPoint());

        if (!source.isRowSelected(row)) {
            source.changeSelection(row, column, false, false);
        }

        int[] rows = invoiceProductsTable.getSelectedRows();

        modositMenuItem.setVisible(rows.length == 1);

        szamlaTermekPopupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
    }
}//GEN-LAST:event_invoiceProductsTableMouseReleased

private void jPanel13MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel13MouseClicked
    doClose(RET_CANCEL);
}//GEN-LAST:event_jPanel13MouseClicked

private void jPanel13MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel13MouseEntered
    jPanel13.setBackground(Color.decode("#d24343"));
}//GEN-LAST:event_jPanel13MouseEntered

private void jPanel13MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel13MouseExited
    jPanel13.setBackground(Color.decode("#f0f0f0"));
}//GEN-LAST:event_jPanel13MouseExited

private void jPanel12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel12MouseClicked
    boolean v = false;
    int j = 0;
    for (int i = 0; i < proForma.getTermekek().size(); i++) {
        ProFormaTermek pft = (ProFormaTermek) proForma.getTermekek().get(i);
        if (pft.getVtszTeszor().isEmpty()) {
            v = true;
            j = i + 1;
            break;
        }
    }
    if (v) {
        HibaDialog h = new HibaDialog("A(z) " + j + ". termékhez nincs megadva VTSZ/TESZOR szám!", "Ok", "");
    } else if (proForma.getTermekek().isEmpty()) {
        HibaDialog h = new HibaDialog("Nincs termék hozzáadva a számlához!", "Ok", "");
    } else if (customerTextField.getText().isEmpty()) {
        HibaDialog h = new HibaDialog("Nincs vevő megadva!", "Ok", "");
    } else {
        nyit(osszegzoDialog, "Összegző");
    }
}//GEN-LAST:event_jPanel12MouseClicked

private void jPanel12MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel12MouseEntered
    jPanel12.setBackground(Color.decode("#abd043"));
}//GEN-LAST:event_jPanel12MouseEntered

private void jPanel12MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel12MouseExited
    jPanel12.setBackground(Color.decode("#f0f0f0"));
}//GEN-LAST:event_jPanel12MouseExited

private void nyomtatasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nyomtatasActionPerformed
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    if (nyomtatas.isSelected()) {
        //java.net.URL url = ClassLoader.getSystemResource("cezeszamlazo/resources/print_20.png");
        //java.awt.Image img = toolkit.createImage(url);
        mentesButton.setText("Mentés és nyomtatás");
        //mentesLabel.setIcon((Icon) img);
    } else {
        //java.net.URL url = ClassLoader.getSystemResource("cezeszamlazo/resources/checkmark_20.png");
        //java.awt.Image img = toolkit.createImage(url);
        mentesButton.setText("Mentés");
        //mentesLabel.setIcon((Icon) img);
    }
    nyomtatasPeldany.setEnabled(nyomtatas.isSelected());
    nyomtatasPeldany.setEditable(nyomtatas.isSelected());
}//GEN-LAST:event_nyomtatasActionPerformed

private void elonezetLabelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elonezetLabelMouseReleased
}//GEN-LAST:event_elonezetLabelMouseReleased

private void elonezetLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elonezetLabelMouseClicked
    elonezet(ElonezetDialog.ELONEZET);
}//GEN-LAST:event_elonezetLabelMouseClicked

private void elonezetLabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elonezetLabelMouseExited
}//GEN-LAST:event_elonezetLabelMouseExited

private void elonezetLabelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elonezetLabelMousePressed
}//GEN-LAST:event_elonezetLabelMousePressed

private void mentesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mentesButtonActionPerformed
    folyamatbanDialog = new FolyamatbanDialog("A mentés folyamatban. Kis türelmet...");
    SzamlaThread sz = new SzamlaThread();
    folyamatbanDialog.setVisible(true);
}//GEN-LAST:event_mentesButtonActionPerformed

private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
    osszegzoDialog.setVisible(false);
}//GEN-LAST:event_jButton5ActionPerformed

private void modositMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modositMenuItemActionPerformed
    int[] rows = invoiceProductsTable.getSelectedRows();
    termekid = rows[0];
    aktualis = (ProFormaTermek) proForma.getTermekek().get(termekid);
    termek.setText(aktualis.getTermek());
    cikkszam.setText(aktualis.getTermekKod());
    mennyiseg.setText(aktualis.getMennyiseg() + "");
    mee.setText(aktualis.getMennyisegiEgyseg());
    egysegar.setText(aktualis.getEgysegar() + "");
    vtszTeszor.setText(aktualis.getVtszTeszor());
    Label l;
    int j = 0;
    for (int i = 0; i < vatComboBox.getItemCount(); i++) {
        l = (Label) vatComboBox.getItemAt(i);
        if (Double.parseDouble(l.getName()) == aktualis.getAfa()) {
            j = i;
            break;
        }
    }
    vatComboBox.setSelectedIndex(j);
    hozzaadModosit.setText("Módosít");
}//GEN-LAST:event_modositMenuItemActionPerformed

private void vtszMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vtszMenuItemActionPerformed
    VtszTeszorListaDialog v = new VtszTeszorListaDialog();
    if (v.getReturnStatus() == 1) {
        int[] rows = invoiceProductsTable.getSelectedRows();
        for (int i = 0; i < rows.length; i++) {
            proForma.getTermekek().get(rows[i]).setVtszTeszor(v.getVtszTeszor());
            proForma.getTermekek().get(rows[i]).setAfa(Integer.parseInt(v.getAfa()));
        }
        szamlaTermekekFrissites();
    }
}//GEN-LAST:event_vtszMenuItemActionPerformed

private void torolMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_torolMenuItemActionPerformed
    int[] rows = invoiceProductsTable.getSelectedRows();
    for (int i = rows.length - 1; i >= 0; i--) {
        proForma.getTermekek().remove(rows[i]);
    }
    szamlaTermekekFrissites();
}//GEN-LAST:event_torolMenuItemActionPerformed

    private void supplierPostalCodeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_supplierPostalCodeKeyReleased
        if (supplierPostalCode.getText().length() == 4) {
            Query query = new Query.QueryBuilder()
            .select("CONCAT(varos, IF(v = 1, CONCAT('-', varosresz), ''))")
            .from("varosok")
            .where("irsz = '" + supplierPostalCode.getText() + "'")
            .build();
            Object[][] s = App.db.select(query.getQuery());
            if (s.length == 1) {
                supplierCity.setText(String.valueOf(s[0][0]));
            }
        }
    }//GEN-LAST:event_supplierPostalCodeKeyReleased

    private void exitSupplierDatasDialogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitSupplierDatasDialogActionPerformed
        supplierDialog.setVisible(false);
    }//GEN-LAST:event_exitSupplierDatasDialogActionPerformed

    private void supplierInvoiceFooterKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_supplierInvoiceFooterKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_supplierInvoiceFooterKeyReleased

    private void vevoOkButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vevoOkButton1ActionPerformed
        maturityUpdate();
        customerDialog.setVisible(false);
    }//GEN-LAST:event_vevoOkButton1ActionPerformed

    private void vevoMentesUjButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vevoMentesUjButton1ActionPerformed
        Object[] o = new Object[14];
        o[0] = customerNameText.getText();
        o[1] = customerPostalCodeText.getText();
        o[2] = customerCityText.getText();
        o[3] = customerStreetText.getText();
        o[4] = customerCountryCode.getText();
        o[5] = customerPhoneNumberText.getText();
        o[6] = customerEmailText.getText();
        o[7] = customerPaymentMethodComboBox.getSelectedIndex();
        o[8] = (customerIsPaymentMethodRequiredCheckBox.isSelected() ? 1 : 0);
        o[9] = customerMaturityText.getText();
        o[10] = customerTaxNumberText.getText();
        o[11] = customerEUTaxNumberText.getText();
        o[12] = customerBankAccountNumberText.getText();
        o[13] = (customerAppearsOnInvoiceCheckBox.isSelected() ? 1 : 0);
        //        App.db.insert("INSERT INTO szamlazo_ugyfel (nev, irsz, varos, utca, orszag, telefon, email, fizetesi_mod, fizetesi_mod_kotelezo, esedekesseg, adoszam, eu_adoszam, bankszamlaszam, szamlan_megjelenik) VALUES " + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", o, 14);
        App.db.insert("INSERT INTO pixi_ugyfel (nev, irsz, varos, utca, orszag, telefon, email, fizetesi_mod, fizetesi_mod_kotelezo, esedekesseg, adoszam, eu_adoszam, bankszamlaszam, szamlan_megjelenik) VALUES " + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", o, 14);
        Query query = new Query.QueryBuilder()
        .select("id")
        .from("pixi_ugyfel")
        //.from("szamlazo_ugyfel")
        .where("1")
        .order("id DESC LIMIT 1")
        .build();
        Object[][] select = App.db.select(query.getQuery());
        vevoid = String.valueOf(String.valueOf(select[0][0]));
        customerDialog.setVisible(false);
    }//GEN-LAST:event_vevoMentesUjButton1ActionPerformed

    private void vevoModositasMentes1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vevoModositasMentes1ActionPerformed
        if (!vevoid.equalsIgnoreCase("0")) {
            Object[] o = new Object[15];
            o[0] = customerNameText.getText();
            o[1] = customerPostalCodeText.getText();
            o[2] = customerCityText.getText();
            o[3] = customerStreetText.getText();
            o[4] = customerCountryCode.getText();
            o[5] = customerPhoneNumberText.getText();
            o[6] = customerEmailText.getText();
            o[7] = customerPaymentMethodComboBox.getSelectedIndex();
            o[8] = (customerIsPaymentMethodRequiredCheckBox.isSelected() ? 1 : 0);
            o[9] = customerMaturityText.getText();
            o[10] = customerTaxNumberText.getText();
            o[11] = customerEUTaxNumberText.getText();
            o[12] = customerBankAccountNumberText.getText();
            o[13] = (customerAppearsOnInvoiceCheckBox.isSelected() ? 1 : 0);
            o[14] = vevoid;
            String query = "UPDATE pixi_ugyfel  SET nev = ?, irsz = ?, varos = ?, "
            + "utca = ? , orszag= ? , telefon = ?, "
            + "email = ?, fizetesi_mod = ?, fizetesi_mod_kotelezo = ?, "
            + "esedekesseg = ?, adoszam = ?, "
            + "eu_adoszam = ?, bankszamlaszam = ?, szamlan_megjelenik = ? "
            + "WHERE id = ?";
            //            System.out.println("query: " + query);
            App.db.insert(query, o, 15);
        }
        customerDialog.setVisible(false);
    }//GEN-LAST:event_vevoModositasMentes1ActionPerformed

    private void customerMaturityTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_customerMaturityTextKeyReleased
        String e = SzamlaFunctions.csakszam(customerMaturityText.getText(), 0, false);
        customerMaturityText.setText(e);
    }//GEN-LAST:event_customerMaturityTextKeyReleased

    private void customerPaymentMethodComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customerPaymentMethodComboBoxActionPerformed
       // fizetesiMod.setSelectedIndex(vevoFizetesiModComboBox.getSelectedIndex());
        if (customerPaymentMethodComboBox.getSelectedIndex() == 1 && customerMaturityText.getText().matches("0")) {
            Properties prop = new Properties();
            try {
                prop.load(new FileInputStream("dat/beallitasok.properties"));
                customerMaturityText.setText(prop.getProperty("alapEsedekesseg"));
            } catch (IOException ex) {
                System.out.println("IOException váltódott ki!");
                ex.printStackTrace();
            }
        }
        if (customerPaymentMethodComboBox.getSelectedIndex() == 0) {
            customerMaturityText.setText("0");
        }
        maturityUpdate();
    }//GEN-LAST:event_customerPaymentMethodComboBoxActionPerformed

    private void customerIsPaymentMethodRequiredCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customerIsPaymentMethodRequiredCheckBoxActionPerformed
        //fizetesiMod.setEnabled(!vevoFizetesiModKotelezoCheckBox.isSelected());
    }//GEN-LAST:event_customerIsPaymentMethodRequiredCheckBoxActionPerformed

    private void customerCountryCodeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_customerCountryCodeKeyReleased
        if (customerCountryCode.getText().length() == 2) {
            Query query = new Query.QueryBuilder()
            .select("id, countryName, countryCode ")
            .from("countries")
            .where("countryCode = '" + customerCountryCode.getText() + "'")
            .build();
            Object[][] o = App.db.select(query.getQuery());
            if (o.length != 0)
            {
                customerCountryName.setText(String.valueOf(o[0][1]));
            }
            System.out.println(o[0][0]);
        }
    }//GEN-LAST:event_customerCountryCodeKeyReleased

    private void customerPostalCodeTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_customerPostalCodeTextKeyReleased
        if (customerPostalCodeText.getText().length() == 4) {
            Query query = new Query.QueryBuilder()
            .select("id, CONCAT(varos, IF(v = 1, '-', ''), varosresz) ")
            .from("varosok")
            .where("irsz = '" + customerPostalCodeText.getText() + "'")
            .build();
            Object[][] o = App.db.select(query.getQuery());
            if (o.length != 0) {
                customerCityText.setText(String.valueOf(o[0][1]));
            }
        }
    }//GEN-LAST:event_customerPostalCodeTextKeyReleased

    private void customerNameTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_customerNameTextKeyReleased
        customerTextField.setText(customerNameText.getText());
    }//GEN-LAST:event_customerNameTextKeyReleased

    private void customerCountryNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_customerCountryNameKeyReleased
        if (customerCountryName.getText().length() >= 2) {
            Query query = new Query.QueryBuilder()
            .select("id, countryName, countryCode ")
            .from("countries")
            .where("countryName = '" + customerCountryName.getText() + "'")
            .build();
            Object[][] o = App.db.select(query.getQuery());
            if (o.length != 0)
            {
                customerCountryCode.setText(String.valueOf(o[0][2]));
            }
        }
    }//GEN-LAST:event_customerCountryNameKeyReleased

    private void doClose(int retStatus) {
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel allGrossText;
    private javax.swing.JLabel allNetText;
    private javax.swing.JLabel allVatText;
    private javax.swing.JTextField brutto;
    private javax.swing.JTextField cikkszam;
    private javax.swing.JCheckBox customerAppearsOnInvoiceCheckBox;
    private javax.swing.JTextField customerBankAccountNumberText;
    private javax.swing.JTextField customerCityText;
    private javax.swing.JTextField customerCountryCode;
    private javax.swing.JTextField customerCountryName;
    private javax.swing.JDialog customerDialog;
    private javax.swing.JTextField customerEUTaxNumberText;
    private javax.swing.JTextField customerEmailText;
    private javax.swing.JCheckBox customerIsPaymentMethodRequiredCheckBox;
    private javax.swing.JTextField customerMaturityText;
    private javax.swing.JTextField customerNameText;
    private javax.swing.JComboBox customerPaymentMethodComboBox;
    private javax.swing.JTextField customerPhoneNumberText;
    private javax.swing.JTextField customerPostalCodeText;
    private javax.swing.JTextField customerStreetText;
    private javax.swing.JTextField customerTaxNumberText;
    private javax.swing.JTextField customerTextField;
    private javax.swing.JTextField egysegar;
    private javax.swing.JLabel elonezetLabel;
    private javax.swing.JButton exitSupplierDatasDialog;
    private javax.swing.JButton hozzaadModosit;
    private javax.swing.JTable invoiceProductsTable;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel101;
    private javax.swing.JLabel jLabel102;
    private javax.swing.JLabel jLabel103;
    private javax.swing.JLabel jLabel104;
    private javax.swing.JLabel jLabel105;
    private javax.swing.JLabel jLabel106;
    private javax.swing.JLabel jLabel111;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel kelt;
    private javax.swing.JButton kiurit;
    private javax.swing.JTextArea lablecText;
    private javax.swing.JTextField maturity;
    private javax.swing.JTextField mee;
    private javax.swing.JTextArea megjegyzesText;
    private javax.swing.JTextField mennyiseg;
    private javax.swing.JButton mentesButton;
    private javax.swing.JMenuItem modositMenuItem;
    private javax.swing.JTextField netto;
    private javax.swing.JCheckBox nyomtatas;
    private javax.swing.JTextField nyomtatasPeldany;
    private javax.swing.JDialog osszegzoDialog;
    private javax.swing.JLabel pay_payBack;
    private javax.swing.JCheckBox pdfKeszites;
    private javax.swing.JLabel penznemLabel1;
    private javax.swing.JLabel penznemLabel2;
    private javax.swing.JLabel penznemLabel3;
    private javax.swing.JLabel say;
    private javax.swing.JLabel summaryGrossText;
    private javax.swing.JLabel summaryNetText;
    private javax.swing.JLabel summaryPay_payBack;
    private javax.swing.JLabel summaryVatText;
    private javax.swing.JTextField supplierBankAccountNumber;
    private javax.swing.JTextField supplierCity;
    private javax.swing.JComboBox supplierComboBox;
    private javax.swing.JTextField supplierComment;
    private javax.swing.JDialog supplierDialog;
    private javax.swing.JTextField supplierEUTaxNumber;
    private javax.swing.JTextArea supplierInvoiceFooter;
    private javax.swing.JTextField supplierName;
    private javax.swing.JTextField supplierPostalCode;
    private javax.swing.JTextField supplierStreet;
    private javax.swing.JTextField supplierTaxNumber;
    private javax.swing.JButton szallitoAdatok;
    private javax.swing.JComboBox szamlaCsoport;
    private javax.swing.JLabel szamlaFejlec;
    private javax.swing.JLabel szamlaSorszam;
    private javax.swing.JPopupMenu szamlaTermekPopupMenu;
    private javax.swing.JTextField termek;
    private javax.swing.JMenuItem torolMenuItem;
    private javax.swing.JComboBox vatComboBox;
    private javax.swing.JButton vevoChangeButton;
    private javax.swing.JButton vevoMentesUjButton1;
    private javax.swing.JButton vevoModositasMentes1;
    private javax.swing.JButton vevoOkButton1;
    private javax.swing.JMenuItem vtszMenuItem;
    private javax.swing.JTextField vtszTeszor;
    private javax.swing.JButton vtszTeszorTallozas;
    // End of variables declaration//GEN-END:variables
    private int returnStatus = RET_CANCEL;

    private void InvoiceGroupUpdate()
    {
        Query query = new Query.QueryBuilder()
                .select("id, nev")
                .from("szamlazo_szamla_csoportok")
                .where("1")
                .order("nev")
                .build();
        Object [][] select = App.db.select(query.getQuery());
        szamlaCsoport.removeAllItems();
        
        for(Object [] obj : select)
        {
            szamlaCsoport.addItem(new Label(obj[0].toString(), obj[1].toString()));
        }
    }
    
    private void SupplierComboBoxUpdate(int selectedSupplierId)
    {
        String [] id = App.user.getCeg().split(";");
        int defaultCompanyId = -1;
        String queryString = "";
        Object [][] select = null;
        
        if(selectedSupplierId == 0)
        {
            queryString = "SELECT sorszamid FROM szamlazo_ceg_adatok AS szca "
                    + "WHERE szca.id ="
                        + "(SELECT alapertelmezett_ceg "
                        + "FROM szamlazo_users "
                        + "WHERE id = " +App.user.getId()+ " )";
            select = App.db.select(queryString);
            defaultCompanyId = Integer.valueOf(select[0][0].toString());
        }
        
        String keres = "";
        for(int i = 0; i < id.length; i++)
        {
            keres += "a.id = " + id[i] + "||";
        }
        keres += "0";
        
        b = false;
        
        Query query = new Query.QueryBuilder()
                .select("a.id, "
                        + "CONCAT(IF(s.elotag != '', "
                        + "CONCAT(s.elotag, ' - '), ''), "
                        + "s.megnevezes, "
                        + "IF(a.deviza = 1, ' (deviza adatokkal)', '')), "
                        + "a.deviza, "
                        + "a.sorszamid ")
                .from("szamlazo_ceg_adatok a, szamlazo_szamla_sorszam s ")
                .where("a.sorszamid = s.id && (" + keres + ") ")
                .build();
        select = App.db.select(query.getQuery());
        
        supplierComboBox.removeAllItems();
        
        int i = 0;
        int selectedIndex = 0;
        int invoiceNumberId = -1;
        int devizaNumber = -1;
        
        for(Object[] obj : select)
        {
            invoiceNumberId = Integer.valueOf(obj[3].toString());
            devizaNumber = Integer.valueOf(obj[2].toString());
            
            if(this.deviza)
            {
                if(devizaNumber == 1 && invoiceNumberId == selectedSupplierId)
                {
                    selectedIndex = i;
                }
            }
            else if(selectedSupplierId == 0 && devizaNumber != 1)
            {
                if(invoiceNumberId == defaultCompanyId)
                {
                    selectedIndex = i;
                }
            }
            else if(invoiceNumberId == selectedSupplierId && devizaNumber != 1)
            {
                selectedIndex = i;
            }
            
            String str = obj[1].toString();
            if(str.length() > 40)
            {
                str = str.substring(0, 37) + "...";
            }
            
            supplierComboBox.addItem(new Label(obj[0].toString(), str));
            i++;
        }
        
        b = true;
        supplierComboBox.setSelectedIndex(selectedIndex);
    }
    
    private void VatUpdate()
    {
        b = false;
        Query query = new Query.QueryBuilder()
                .select("id, megnevezes")
                .from("szamlazo_afa")
                .where("1")
                .build();
        Object [][] select = App.db.select(query.getQuery());
        vatComboBox.removeAllItems();
        
        for(Object [] obj : select)
        {
            vatComboBox.addItem(new Label (obj[0].toString(), obj[1].toString()));
        }
        b = true;
    }
    
    private void CustomerUpdate()
    {
        Vevo vevo = new Vevo(Integer.valueOf(vevoid));
        if(szamla == null)
        {
            SetCustomerDialogFields(vevo);
        }
        else
        {
            SetCustomerDialogFields();
        }
    }
    
    private void SetCustomerDialogFields(Vevo customer)
    {
        customerTextField.setText(customer.getNev());
        customerNameText.setText(customer.getNev());
        customerPostalCodeText.setText(customer.getIrsz());
        customerCityText.setText(customer.getVaros());
        customerStreetText.setText(customer.getAddress());
        customerCountryName.setText(customer.getOrszag());
        customerPhoneNumberText.setText(customer.getTelefon());
        customerEmailText.setText(customer.getEmail());
        customerPaymentMethodComboBox.setSelectedIndex(customer.getFizetesiMod());
        customerIsPaymentMethodRequiredCheckBox.setSelected(customer.isFizetesiModKotelezo());
        customerTaxNumberText.setText(customer.getAdoszam());
        customerEUTaxNumberText.setText(customer.getEuAdoszam());
        customerBankAccountNumberText.setText(customer.getBankszamlaszam());
        customerAppearsOnInvoiceCheckBox.setSelected(customer.isSzamlanMegjelenik());
        
        if(customer.getFizetesiMod() == 0 && customer.getEsedekesseg().equalsIgnoreCase("0"))
        {
            Properties prop = new Properties();
            try
            {
                prop.load(new FileInputStream("dat/beallitasok.properties"));
                customerMaturityText.setText(prop.getProperty("alapEsedekesseg"));
            }
            catch(IOException ex)
            {
                System.out.println("IOEXception in SetCustomerDialogFields(Vevo customer) method");
                ex.printStackTrace();
                customerMaturityText.setText("0");
            }
        }
        else
        {
            customerMaturityText.setText(customer.getEsedekesseg());
        }
    }
    
    private void SetCustomerDialogFields()
    {
        customerTextField.setText(szamla.getVevo().getNev());
        customerNameText.setText(szamla.getVevo().getNev());
        customerPostalCodeText.setText(szamla.getVevo().getIrsz());
        customerCityText.setText(szamla.getVevo().getVaros());
        customerStreetText.setText(szamla.getVevo().getAddress());
        customerCountryName.setText(szamla.getVevo().getOrszag());
        customerPhoneNumberText.setText(szamla.getVevo().getTelefon());
        customerEmailText.setText(szamla.getVevo().getEmail());
        customerPaymentMethodComboBox.setSelectedIndex(szamla.getVevo().getFizetesiMod());
        customerIsPaymentMethodRequiredCheckBox.setSelected(szamla.getVevo().isFizetesiModKotelezo());
        customerTaxNumberText.setText(szamla.getVevo().getAdoszam());
        customerEUTaxNumberText.setText(szamla.getVevo().getEuAdoszam());
        customerBankAccountNumberText.setText(szamla.getVevo().getBankszamlaszam());
        customerAppearsOnInvoiceCheckBox.setSelected(szamla.getVevo().isSzamlanMegjelenik());
        
        if(szamla.getVevo().getFizetesiMod() == 0 && szamla.getVevo().getEsedekesseg().equalsIgnoreCase("0"))
        {
            Properties prop = new Properties();
            try
            {
                prop.load(new FileInputStream("dat/beallitasok.properties"));
                customerMaturityText.setText(prop.getProperty("alapEsedekesseg"));
            }
            catch(IOException ex)
            {
                System.out.println("IOEXception in SetCustomerDialogFields(Vevo customer) method");
                ex.printStackTrace();
                customerMaturityText.setText("0");
            }
        }
        else
        {
            customerMaturityText.setText(szamla.getVevo().getEsedekesseg());
        }
    }
    
    private void InvoiceProductsUpdate()
    {
        DefaultTableModel model = (DefaultTableModel) invoiceProductsTable.getModel();
        for(int i = 0; i < invoiceProductsTable.getRowCount(); i++)
        {
            model.removeRow(i);
        }
        
        Object [] row = new Object[10];
        double allNet = 0.0f, allVat = 0.0f, allGross = 0.0f;
        
        for(int i = 0; i < termekek.size(); i++)
        {
            SzamlaTermek szt = (SzamlaTermek) termekek.get(i);
            row[0] = szt.getNev();
            row[1] = szt.getCikkszam();
            row[2] = szt.getMennyiseg();
            row[3] = szt.getMee();
            row[4] = String.format(java.util.Locale.US, "%.2f", szt.getEgysegar());
            row[5] = szt.getVtszTeszor();
            row[6] = String.format(java.util.Locale.US, "%.2f", szt.getNetto(deviza));
            row[7] = szt.getAfaLabel();
            allNet += szt.getNetto(deviza);
            allVat += szt.getAfaErtek(deviza);
            allGross += szt.getBrutto(deviza);
            
            row[8] = String.format(java.util.Locale.US, "%.2f", szt.getAfaErtek(deviza));
            row[9] = String.format(java.util.Locale.US, "%.2f", szt.getBrutto(deviza));
            model.addRow(row);
        }
        
        allNetText.setText(EncodeDecode.numberFormat(String.valueOf(allNet), deviza) + " " + penznem);
        allVatText.setText(EncodeDecode.numberFormat(String.valueOf(allVat), deviza) + " " + penznem);
        allGrossText.setText(EncodeDecode.numberFormat(String.valueOf(allGross), deviza) + " " + penznem);
        summaryNetText.setText(EncodeDecode.numberFormat(String.valueOf(allNet), deviza) + " " + penznem);
        summaryVatText.setText(EncodeDecode.numberFormat(String.valueOf(allVat), deviza) + " " + penznem);
        summaryGrossText.setText(EncodeDecode.numberFormat(String.valueOf(allGross), deviza) + " " + penznem);
        
        if(allGross < 0)
        {
            pay_payBack.setText("Visszatérítendő");
        }
        
        summaryPay_payBack.setText(EncodeDecode.numberFormat(String.valueOf(allGross), deviza) + " " + penznem);
        say.setText("azaz " + SzamlaFunctions.betuvel(allGross) + " " + penznem);
    }
   
    private void init(String title)
    {
        szallitoFrissites();

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;

        java.net.URL url = ClassLoader.getSystemResource("cezeszamlazo/resources/uj-szamla.png");
        java.awt.Image img = toolkit.createImage(url);

        setIconImage(img);

        customerDialog.setSize((int) Math.round(customerDialog.getPreferredSize().getWidth()), (int) Math.round(customerDialog.getPreferredSize().getHeight()) + 35);
        customerDialog.setIconImage(img);
        
        kelt.setText(Functions.now());
        maturity.setText(Functions.now());

        setLocation(x, y);
        setTitle(title);
        setModal(true);
        setVisible(true);
    }

    private void elonezet(int tipus) {
        /*
        Szallito szall = new Szallito(
        szallitoNev.getText(),
        szallitoIrsz.getText(),
        szallitoVaros.getText(),
        szallitoUtca.getText(),
        szallitoAdoszam.getText(),
        szallitoBankszamlaszam.getText(),
        szallitoMegjegyzes.getText());
        Vevo v = new Vevo(
        vevo.getText(),
        vevoIrszText.getText(),
        vevoVarosText.getText(),
        vevoUtcaText.getText(),
        vevoOrszagText.getText(),
        vevoTelefonText.getText(),
        vevoEmailText.getText(),
        vevoFizetesiModComboBox.getSelectedIndex(),
        vevoFizetesiModKotelezoCheckBox.isSelected(),
        vevoEsedekessegText.getText(),
        vevoAdoszamText.getText(),
        vevoEuAdoszamText.getText(),
        vevoBankszamlaszamText.getText(),
        vevoSzamlanMegjelenikCheckBox.isSelected());
        Label label = (Label) szamlaCsoport.getSelectedItem();
        Szamla sz = new Szamla(
        szall,
        v,
        proForma.getSorszam(),
        kelt.getText(),
        teljesites.getText(),
        esedekesseg.getText(),
        label.getId(),
        penznem,
        kozeparfolyam,
        helyesbitett,
        helyesbitettTeljesites,
        deviza,
        megjegyzes.getText(),
        lablec.getText(),
        termekek,
        termekdijAtvallalas.isSelected());
        sz.setTipus(szla != null ? this.szla.getTipus() : 0);
        sz.setHelyesbitettTeljesites(szla != null ? this.szla.getTeljesites() : "");
        sz.setHelyesbitett(szla != null ? this.szla.getSorszam() : "");
         */
        //ElonezetDialog elonezet = new ElonezetDialog(sz, Integer.parseInt(nyomtatasPeldany.getText()), tipus);
    }

    private void szamlaTermekekFrissites()
    {
        DefaultTableModel model = (DefaultTableModel) invoiceProductsTable.getModel();
        for (int i = invoiceProductsTable.getRowCount() - 1; i >= 0; i--)
        {
            model.removeRow(i);
        }
        Object[] row = new Object[10];
        double oN = 0.0, oA = 0.0, oB = 0.0;
        for (int i = 0; i < termekek.size(); i++)
        {
            ProFormaTermek szt = (ProFormaTermek) proForma.getTermekek().get(i);
            row[0] = szt.getTermek();
            row[1] = szt.getTermekKod();
            row[2] = szt.getMennyiseg();
            row[3] = szt.getMennyisegiEgyseg();
            row[4] = szt.getEgysegar();
            row[5] = szt.getVtszTeszor();
            row[6] = szt.getNetto();
            row[7] = szt.getAfa();
            row[8] = szt.getAfaErtek();
            row[9] = szt.getBrutto();
            model.addRow(row);
        }
        allNetText.setText(EncodeDecode.numberFormat(String.valueOf(oN), deviza) + " " + penznem);
        allVatText.setText(EncodeDecode.numberFormat(String.valueOf(oA), deviza) + " " + penznem);
        allGrossText.setText(EncodeDecode.numberFormat(String.valueOf(oB), deviza) + " " + penznem);
        summaryNetText.setText(EncodeDecode.numberFormat(String.valueOf(oN), deviza) + " " + penznem);
        summaryVatText.setText(EncodeDecode.numberFormat(String.valueOf(oA), deviza) + " " + penznem);
        summaryGrossText.setText(EncodeDecode.numberFormat(String.valueOf(oB), deviza) + " " + penznem);

        summaryPay_payBack.setText(EncodeDecode.numberFormat(String.valueOf(oB), deviza) + " " + penznem);
        say.setText("azaz " + Functions.betuvel(oB) + " " + penznem);
        if (!modosit) {
            invoiceProductsTable.setEnabled(false);
            hozzaadModosit.setEnabled(false);
        }
        
        invoiceProductsTable.setEnabled(true);
    }

    private void szamlaCsoportFrissites() {
        Query query = new Query.QueryBuilder()
                .select("id, nev")
                .from("szamlazo_szamla_csoportok")
                .where("1")
                .order("nev")
                .build();
        Object[][] s = App.db.select(query.getQuery());
        szamlaCsoport.removeAllItems();
        for (int i = 0; i < s.length; i++) {
            szamlaCsoport.addItem(new Label(String.valueOf(s[i][0]), String.valueOf(s[i][1])));
        }
    }

    private void szallitoFrissites() {
        String[] id = App.user.getCeg().split(";");
        String keres = "";
        for (int i = 0; i < id.length; i++) {
            keres += "a.id = " + id[i] + " || ";
        }
        keres += "0";
        b = false;
        Query query = new Query.QueryBuilder()
                .select("a.id, "
                        + "CONCAT(IF(s.elotag != '', CONCAT(s.elotag, ' - '), ''), s.megnevezes, "
                        + "IF(a.deviza = 1, ' (deviza adatokkal)', '')), "
                        + "a.deviza ")
                .from("szamlazo_ceg_adatok a, szamlazo_szamla_sorszam s ")
                .where("a.sorszamid = s.id && (" + keres + ") ")
                .order("a.id")
                .build();
        Object[][] o = App.db.select(query.getQuery());
        supplierComboBox.removeAllItems();
        int i = 0, j = 0;
        for (Object[] obj : o) {
            if (String.valueOf(obj[2]).equalsIgnoreCase("1")) {
                j = i;
            }
            String s = String.valueOf(obj[1]);
            if (s.length() < 40) {
                supplierComboBox.addItem(new Label(String.valueOf(obj[0]), s));
            } else {
                supplierComboBox.addItem(new Label(String.valueOf(obj[0]), s.substring(0, 37) + "..."));
            }
            i++;
        }
        b = true;
        supplierComboBox.setSelectedIndex(j);
    }

    private void afaFrissites() {
        b = false;
        Object[][] s = App.db.select("SELECT afa FROM szamlazo_afa ORDER BY afa DESC");
        vatComboBox.removeAllItems();
        for (int i = 0; i < s.length; i++) {
            vatComboBox.addItem(new Label(String.valueOf(s[i][0]), String.valueOf(s[i][0])));
        }
        b = true;
    }
    
    private void maturityUpdate()
    {
        Calendar c = Calendar.getInstance();
        String [] d = kelt.getText().split("-");
        c.set(Integer.parseInt(d[0]), Integer.parseInt(d[1])-1, Integer.parseInt(d[2]));
        if(!customerMaturityText.getText().matches("0") && !customerMaturityText.getText().isEmpty())
        {
            c.add(Calendar.DATE, Integer.parseInt(customerMaturityText.getText()));
            maturity.setText(format.format(c.getTime()));
        }
        else if(customerMaturityText.getText().matches("0"))
        {
            c.add(Calendar.DATE, 0);
            maturity.setText(format.format(c.getTime()));
        }
    }

    private void sorszamEllenorzes()
    {
        Calendar calendar = Calendar.getInstance();
        Label label = (Label) supplierComboBox.getSelectedItem();
        Query query = new Query.QueryBuilder()
                .select("id, ev ")
                .from("szamlazo_szamla_sorszam")
                .where("id = (SELECT sorszamid from szamlazo_ceg_adatok WHERE id = " + label.getId() + ")")
                .order("")
                .build();
        Object[][] select = App.db.select(query.getQuery());
        if (select.length != 0 && Integer.parseInt(String.valueOf(select[0][1])) < calendar.get(Calendar.YEAR)) {

            Object[] o = new Object[1];
            o[0] = String.valueOf(calendar.get(Calendar.YEAR));
            App.db.insert("UPDATE szamlazo_szamla_sorszam SET ev = ?, db = 0 WHERE id = " + String.valueOf(select[0][0]), o, 1);
        } else {
            query = new Query.QueryBuilder()
                    .select("kelt")
                    .from("szamlazo_szamla")
                    .where("sorszamozasid = (SELECT sorszamid from szamlazo_ceg_adatok WHERE id = " + label.getId() + ")")
                    .order("id DESC LIMIT 1")
                    .build();
            select = App.db.select(query.getQuery());
            if (select.length != 0) {
                String[] datum = String.valueOf(select[0][0]).split("-");
                System.out.println(String.valueOf(select[0][0]));
                int e = Integer.parseInt(datum[0]),
                        h = Integer.parseInt(datum[1]),
                        n = Integer.parseInt(datum[2]);
                if (calendar.get(Calendar.YEAR) < e
                        || (calendar.get(Calendar.YEAR) == e && calendar.get(Calendar.MONTH) + 1 < h)
                        || (calendar.get(Calendar.YEAR) == e && calendar.get(Calendar.MONTH) + 1 == h && calendar.get(Calendar.DATE) < n)) {
                    // valami nem stimmel
                    HibaDialog hd = new HibaDialog("A legutóbbi számla kelte nagyobb, mint a beállított rendszeridő!", "Ok", "");
                }
            }
        }
    }
    
    public void LoadSupplier()
    {
        Label supplierLabel = (Label) supplierComboBox.getSelectedItem();
        Query query = new Query.QueryBuilder()
                .select("nev, irsz, varos, utca, adoszam, bankszamlaszam, megjegyzes, szamla_lablec")
                .from("szamlazo_ceg_adatok")
                .where("id = " + supplierLabel.getId())
                .build();
        Object [][] supplierInfos = App.db.select(query.getQuery());
        supplierName.setText(String.valueOf(supplierInfos[0][0]));
        supplierPostalCode.setText(String.valueOf(supplierInfos[0][1]));
        supplierCity.setText(String.valueOf(supplierInfos[0][2]));
        supplierStreet.setText(String.valueOf(supplierInfos[0][3]));
        supplierTaxNumber.setText(String.valueOf(supplierInfos[0][4]));
        supplierBankAccountNumber.setText(String.valueOf(supplierInfos[0][5]));
        supplierComment.setText(String.valueOf(supplierInfos[0][6]));
        supplierInvoiceFooter.setText(String.valueOf(supplierInfos[0][7]));
    }

    private void kiurit() {
        termek.setText("");
        cikkszam.setText("");
        mennyiseg.setText("1");
        mee.setText("db");
        egysegar.setText("0");
        vtszTeszor.setText("");
        netto.setText("0");
        brutto.setText("0");
        b = false;
        vatComboBox.setSelectedIndex(0);
        termekid = -1;
        hozzaadModosit.setText("Hozzáad");
        aktualis = new ProFormaTermek(0);
        b = true;
    }

    private void csakszam(KeyEvent evt, boolean tizedes) {
        JTextField field = (JTextField) evt.getSource();
        int pos = field.getCaretPosition();
        field.setText(csakszam(field.getText(), 0, tizedes));
        try {
            field.setCaretPosition(pos);
        } catch (Exception ex) {
        }
    }

    private void szamolMennyisegAlapjan()
    {
        double m = Double.parseDouble(mennyiseg.getText()),
                e = Double.parseDouble(egysegar.getText()),
                a = 0.0;
        Label l = (Label) vatComboBox.getSelectedItem();
        a = Double.parseDouble(l.getId()) + 100;
        if (deviza) {
            double net = Math.round(m * e * 10000.0) / 10000.0,
                    bru = Math.round(net * a) / 100.0;
            netto.setText(net + "");
            brutto.setText(bru + "");
        } else {
            double net = Math.round(m * e * 100.0) / 100.0;
            int bru = (int) Math.round(net * a / 100.0);
            netto.setText(net + "");
            brutto.setText(bru + "");
        }
    }

    private void szamolNettoAlapjan() {
        double m = Double.parseDouble(mennyiseg.getText()),
                e = 0.0,
                a = 0.0;
        Label l = (Label) vatComboBox.getSelectedItem();
        a = Double.parseDouble(l.getId()) + 100;
        if (deviza) {
            double net = Double.parseDouble(netto.getText().replace(",", ".")),
                    bru = (int) Math.round(net * a) / 100.0;
            e = Math.round(net / m * 10000.0) / 10000.0;
            brutto.setText(bru + "");
        } else {
            double net = Double.parseDouble(netto.getText().replace(",", ".")),
                    bru = (int) Math.round(net * a / 100.0);
            e = Math.round(net / m * 100.0) / 100.0;
            brutto.setText(bru + "");
        }
        egysegar.setText(e + "");
    }

    private void szamolBruttoAlapjan() {
        double m = Double.parseDouble(mennyiseg.getText()),
                e = 0.0,
                a = 0.0;
        Label l = (Label) vatComboBox.getSelectedItem();
        a = Double.parseDouble(l.getId()) + 100;
        if (deviza) {
            double net = 0.0,
                    bru = Double.parseDouble(brutto.getText());
            net = Math.round(bru * 100.0 / a * 100.0) / 100.0;
            e = Math.round(net / m * 10000.0) / 10000.0;
            netto.setText(net + "");
        } else {
            double net = 0;
            int bru = Integer.parseInt(brutto.getText());
            net = Math.round(bru * 100.0 / a * 100.0) / 100.0;
            e = Math.round(net / m * 100.0) / 100.0;
            netto.setText(net + "");
        }
        egysegar.setText(e + "");
    }

    private String csakszam(String text, int size, boolean tizedes) {
        String valid = "+-0123456789";
        if (tizedes) {
            valid += ".";
        }
        text = text.replace(",", ".");
        String result = "";
        for (int i = 0; i < text.length(); i++) {
            if (valid.contains(text.substring(i, i + 1))) {
                result += text.substring(i, i + 1);
            }
        }
        if (size != 0) {
            if (result.length() > size) {
                result = result.substring(0, size);
            }
        }
        return result;
    }

    private void nyit(Object dialog, String title) {
        if (dialog instanceof JDialog) {
            JDialog d = (JDialog) dialog;
            Dimension appSize = getSize();
            Point appLocation = getLocation();
            int x = (appSize.width - d.getWidth()) / 2 + appLocation.x;
            int y = (appSize.height - d.getHeight()) / 2 + appLocation.y;
            d.setLocation(x, y);
            d.setTitle(title);
            d.setVisible(true);
        } else if (dialog instanceof JFrame) {
            JFrame f = (JFrame) dialog;
            Dimension appSize = getSize();
            Point appLocation = getLocation();
            int x = (appSize.width - f.getWidth()) / 2 + appLocation.x;
            int y = (appSize.height - f.getHeight()) / 2 + appLocation.y;
            f.setLocation(x, y);
            f.setTitle(title);
            f.setVisible(true);
        }
    }

    private String numberFormat(String num, boolean tizedes) {
        String result = "";
        double szam = Double.parseDouble(num);
        int elojel = 1;
        if (szam < 0) {
            elojel = -1;
            szam = Math.abs(szam);
        }
        NumberFormat formatter;
        if (tizedes) {
            formatter = new DecimalFormat("#,###.00");
            result = (elojel < 0 ? "-" : "") + (Math.floor(szam) == 0 ? "0" : "") + formatter.format(szam);
        } else {
            formatter = new DecimalFormat("#,###");
            result = (elojel < 0 ? "-" : "") + formatter.format(szam);
        }
        return result;
    }

    private void folyamatbanNyit() {
        mentesButton.setEnabled(false);
        folyamatbanDialog.setSize(folyamatbanDialog.getPreferredSize());
        folyamatbanDialog.setLocation(getLocationOnScreen().x + (getSize().width - folyamatbanDialog.getSize().width) / 2, getLocationOnScreen().y + (getSize().height - folyamatbanDialog.getSize().height) / 2);
        folyamatbanDialog.setVisible(true);
    }

    private void init() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;

        java.net.URL url = ClassLoader.getSystemResource("cezeszamlazo/resources/icon.png");
        java.awt.Image img = toolkit.createImage(url);

        setIconImage(img);

        setLocation(x, y);
        setTitle("Szállító adatok");
        setModal(true);
        setVisible(true);
    }

    class SzamlaThread extends Thread {

        public SzamlaThread() {
            start();
        }

        @Override
        public void run() {
//            mentesButton.setEnabled(false);
//            String ssz = proForma.getSorszam();
//            Calendar now = Calendar.getInstance();
//            Label label = (Label) szallitoComboBox.getSelectedItem();
//            Object[][] select = App.db.select("SELECT db, ev, elotag FROM szamlazo_szamla_sorszam WHERE id = (SELECT sorszamid from szamlazo_ceg_adatok WHERE id = " + label.getId() + ")", 3);
//            App.db.insert("UPDATE szamlazo_szamla_sorszam SET db = db + 1 WHERE id = (SELECT sorszamid from szamlazo_ceg_adatok WHERE id = " + label.getId() + ")", null, 0);
//            proForma.getSorszam() = now.get(Calendar.YEAR) + "/" + (Integer.parseInt(String.valueOf(select[0][0])) + 1);
//            if (!String.valueOf(select[0][2]).isEmpty()) {
//                proForma.getSorszam() = String.valueOf(select[0][2]) + " " + sorszam;
//            }
//            if (ssz.matches(sorszam)) {
//                szamlaMentes();
//                folyamatbanDialog.setVisible(false);
//                osszegzoDialog.setVisible(false);
//                doClose(RET_OK);
//            } else {
//                if (!deviza) {
//                    HibaDialog h = new HibaDialog("A számla sorszáma időközben megváltozott. Új sorszám: " + sorszam, "Ok", "");
//                } else {
//                    HibaDialog h = new HibaDialog("A számla sorszáma időközben megváltozott. Új sorszám: " + sorszam + "/V", "Ok", "");
//                }
//            }
//            folyamatbanDialog.setVisible(false);
        }
    }
}