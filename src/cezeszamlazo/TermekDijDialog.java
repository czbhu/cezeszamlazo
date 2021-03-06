package cezeszamlazo;

import cezeszamlazo.views.UserMessage;
import invoice.ProductFee;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

public class TermekDijDialog extends javax.swing.JDialog
{
    private Map <String, String> productFeeMap = new HashMap<>();
    
    private boolean comboboxUpdate = false;
    
    /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;
    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;

    /** Creates new form TermekDijDialog */
    public TermekDijDialog()
    {
	initComponents();
	
        UpdateProductFeeTypeComboBox();
        
	init();

	// Close the dialog when Esc is pressed
	String cancelName = "cancel";
	InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
	inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), cancelName);
	
        ActionMap actionMap = getRootPane().getActionMap();
	actionMap.put(cancelName, new AbstractAction()
        {
            @Override
	    public void actionPerformed(ActionEvent e)
            {
		doClose(RET_CANCEL);
	    }
	});
    }
    
    public TermekDijDialog(ProductFee fee)
    {
	initComponents();
        
        UpdateProductFeeTypeComboBox();
	
	nev.setText(fee.getName());
	//szelesseg.setText(String.valueOf(fee.getSzelesseg()));
	//magassag.setText(String.valueOf(td.getMagassag()));
	//peldany.setText(String.valueOf(td.getPeldany()));
	//egysegSuly.setText(String.valueOf(td.getEgysegsuly()));
	
        /*productFeeMap.replace(td.getTipus(), String.valueOf(td.getTermekDij()));
        csk.setText(String.valueOf(td.getCsk()));
        kt.setText(String.valueOf(td.getKt()));
        
        for(int i = 0; i < comboBox_productFeeType.getItemCount(); i++)
        {
            String productFeeType = comboBox_productFeeType.getItemAt(i).toString();
            
            if(td.getTipus().equals(productFeeType))
            {
                comboBox_productFeeType.setSelectedIndex(i);
                break;
            }
        }
        
        termekdij.setText(productFeeMap.get(comboBox_productFeeType.getSelectedItem().toString()));
	
	boolean b = td.getOsszsuly() != 0.0;
	
	sulyMegadas.setSelected(b);
	osszSuly.setText(String.valueOf(td.getOsszsuly()));
	
	osszSuly.setEditable(b);
	osszSuly.setEnabled(b);
	szelesseg.setEditable(!b);
	szelesseg.setEnabled(!b);
	magassag.setEditable(!b);
	magassag.setEnabled(!b);
	peldany.setEditable(!b);
	peldany.setEnabled(!b);
	egysegSuly.setEditable(!b);
	egysegSuly.setEnabled(!b);
	
	szamolAr();
	
	init();*/

	// Close the dialog when Esc is pressed
	String cancelName = "cancel";
	InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
	inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), cancelName);
        
	ActionMap actionMap = getRootPane().getActionMap();
	actionMap.put(cancelName, new AbstractAction()
        {
            @Override
	    public void actionPerformed(ActionEvent e)
            {
		doClose(RET_CANCEL);
	    }
	});
    }
    
    /*public TermekDijDialog(String id)
    {
	initComponents();
        
        UpdateProductFeeTypeComboBox();
	
	TermekDij td = new TermekDij(id);
	
	nev.setText(td.getNev());
	szelesseg.setText(String.valueOf(td.getSzelesseg()));
	magassag.setText(String.valueOf(td.getMagassag()));
	peldany.setText(String.valueOf(td.getPeldany()));
	egysegSuly.setText(String.valueOf(td.getEgysegsuly()));
	termekdij.setText(String.valueOf(td.getTermekDij()));
	
	boolean b = td.getOsszsuly() != 0.0;
	
	sulyMegadas.setSelected(b);
	osszSuly.setText(String.valueOf(td.getOsszsuly()));
	
	osszSuly.setEditable(b);
	osszSuly.setEnabled(b);
	szelesseg.setEditable(!b);
	szelesseg.setEnabled(!b);
	magassag.setEditable(!b);
	magassag.setEnabled(!b);
	peldany.setEditable(!b);
	peldany.setEnabled(!b);
	egysegSuly.setEditable(!b);
	egysegSuly.setEnabled(!b);
	
	szamolAr();
	
	init();

	// Close the dialog when Esc is pressed
	String cancelName = "cancel";
	InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
	inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), cancelName);
	ActionMap actionMap = getRootPane().getActionMap();
	actionMap.put(cancelName, new AbstractAction() {

	    public void actionPerformed(ActionEvent e) {
		doClose(RET_CANCEL);
	    }
	});
    }*/
    
    private void UpdateProductFeeTypeComboBox()
    {
        comboboxUpdate = true;
        comboBox_productFeeType.removeAllItems();
        
        Object [][] productFeeTypes = ProductFee.getTypes();
        
        for(int i = 0; i < productFeeTypes.length; i++)
        {
            comboBox_productFeeType.addItem(new Label(productFeeTypes[i][1].toString(), productFeeTypes[i][0].toString()));
            productFeeMap.put(productFeeTypes[i][0].toString(), productFeeTypes[i][1].toString());
        }
        
        comboboxUpdate = false;
    }

    public int getReturnStatus()
    {
	return returnStatus;
    }
    
    private boolean HasCode()
    {
        boolean hasCode = true;
        
        if(csk.getText().isEmpty() && kt.getText().isEmpty())
        {
            hasCode = false;
        }

        return hasCode;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        termekdij = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        egysegSuly = new javax.swing.JTextField();
        peldany = new javax.swing.JTextField();
        szelesseg = new javax.swing.JTextField();
        nev = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        magassag = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        osszesenLabel = new javax.swing.JLabel();
        osszsulyLabel = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        osszesen = new javax.swing.JLabel();
        sulyMegadas = new javax.swing.JCheckBox();
        osszSuly = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        csk = new javax.swing.JTextField();
        kt = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        comboBox_productFeeType = new javax.swing.JComboBox();

        setName("Form"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(cezeszamlazo.App.class).getContext().getResourceMap(TermekDijDialog.class);
        okButton.setText(resourceMap.getString("okButton.text")); // NOI18N
        okButton.setName("okButton"); // NOI18N
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        cancelButton.setText(resourceMap.getString("cancelButton.text")); // NOI18N
        cancelButton.setName("cancelButton"); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        termekdij.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        termekdij.setText(resourceMap.getString("termekdij.text")); // NOI18N
        termekdij.setName("termekdij"); // NOI18N
        termekdij.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                termekdijKeyReleased(evt);
            }
        });

        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        egysegSuly.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        egysegSuly.setText(resourceMap.getString("egysegSuly.text")); // NOI18N
        egysegSuly.setName("egysegSuly"); // NOI18N
        egysegSuly.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                egysegSulyFocusGained(evt);
            }
        });
        egysegSuly.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                egysegSulyKeyReleased(evt);
            }
        });

        peldany.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        peldany.setText(resourceMap.getString("peldany.text")); // NOI18N
        peldany.setName("peldany"); // NOI18N
        peldany.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                peldanyFocusGained(evt);
            }
        });
        peldany.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                peldanyKeyReleased(evt);
            }
        });

        szelesseg.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        szelesseg.setText(resourceMap.getString("szelesseg.text")); // NOI18N
        szelesseg.setName("szelesseg"); // NOI18N
        szelesseg.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                szelessegFocusGained(evt);
            }
        });
        szelesseg.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                szelessegKeyReleased(evt);
            }
        });

        nev.setText(resourceMap.getString("nev.text")); // NOI18N
        nev.setName("nev"); // NOI18N

        jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        magassag.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        magassag.setText(resourceMap.getString("magassag.text")); // NOI18N
        magassag.setName("magassag"); // NOI18N
        magassag.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                magassagFocusGained(evt);
            }
        });
        magassag.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                magassagKeyReleased(evt);
            }
        });

        jLabel8.setText(resourceMap.getString("jLabel8.text")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N

        jLabel9.setText(resourceMap.getString("jLabel9.text")); // NOI18N
        jLabel9.setName("jLabel9"); // NOI18N

        jLabel10.setText(resourceMap.getString("jLabel10.text")); // NOI18N
        jLabel10.setName("jLabel10"); // NOI18N

        osszesenLabel.setFont(resourceMap.getFont("osszesenLabel.font")); // NOI18N
        osszesenLabel.setText(resourceMap.getString("osszesenLabel.text")); // NOI18N
        osszesenLabel.setName("osszesenLabel"); // NOI18N

        osszsulyLabel.setFont(resourceMap.getFont("osszsulyLabel.font")); // NOI18N
        osszsulyLabel.setText(resourceMap.getString("osszsulyLabel.text")); // NOI18N
        osszsulyLabel.setName("osszsulyLabel"); // NOI18N

        jLabel11.setText(resourceMap.getString("jLabel11.text")); // NOI18N
        jLabel11.setName("jLabel11"); // NOI18N

        osszesen.setFont(resourceMap.getFont("osszesen.font")); // NOI18N
        osszesen.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        osszesen.setText(resourceMap.getString("osszesen.text")); // NOI18N
        osszesen.setName("osszesen"); // NOI18N

        sulyMegadas.setText(resourceMap.getString("sulyMegadas.text")); // NOI18N
        sulyMegadas.setName("sulyMegadas"); // NOI18N
        sulyMegadas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sulyMegadasActionPerformed(evt);
            }
        });

        osszSuly.setEditable(false);
        osszSuly.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        osszSuly.setText(resourceMap.getString("osszSuly.text")); // NOI18N
        osszSuly.setEnabled(false);
        osszSuly.setName("osszSuly"); // NOI18N
        osszSuly.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                osszSulyFocusGained(evt);
            }
        });
        osszSuly.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                osszSulyKeyReleased(evt);
            }
        });

        jLabel12.setText(resourceMap.getString("jLabel12.text")); // NOI18N
        jLabel12.setName("jLabel12"); // NOI18N

        jLabel13.setText(resourceMap.getString("jLabel13.text")); // NOI18N
        jLabel13.setName("jLabel13"); // NOI18N

        csk.setText(resourceMap.getString("csk.text")); // NOI18N
        csk.setName("csk"); // NOI18N

        kt.setText(resourceMap.getString("kt.text")); // NOI18N
        kt.setName("kt"); // NOI18N

        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setMargin(new java.awt.Insets(2, 7, 2, 7));
        jButton1.setName("jButton1"); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText(resourceMap.getString("jButton2.text")); // NOI18N
        jButton2.setMargin(new java.awt.Insets(2, 7, 2, 7));
        jButton2.setName("jButton2"); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        comboBox_productFeeType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Termékdíj típusa" }));
        comboBox_productFeeType.setName("comboBox_productFeeType"); // NOI18N
        comboBox_productFeeType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboBox_productFeeTypeItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(osszesenLabel)
                            .addComponent(osszsulyLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(osszSuly, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(sulyMegadas))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(osszesen, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)
                                .addGap(104, 104, 104))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel12)
                            .addComponent(jLabel13))
                        .addGap(48, 48, 48)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nev, javax.swing.GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(peldany, 0, 1, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel10))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(szelesseg, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(magassag, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel8))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(kt)
                                    .addComponent(csk))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(termekdij)
                                .addGap(6, 6, 6)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(comboBox_productFeeType, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(egysegSuly)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel9)))))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cancelButton, okButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nev, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(szelesseg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(magassag, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(peldany, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(egysegSuly, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(termekdij, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel5)
                    .addComponent(comboBox_productFeeType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(csk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(kt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 6, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(osszsulyLabel)
                    .addComponent(jLabel11)
                    .addComponent(sulyMegadas)
                    .addComponent(osszSuly, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(osszesenLabel)
                    .addComponent(osszesen))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(okButton)
                    .addComponent(cancelButton))
                .addContainerGap())
        );

        getRootPane().setDefaultButton(okButton);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
	if(HasCode())
        {
            doClose(RET_OK);
        }
        else
        {
            UserMessage message = new UserMessage("Üres mező", "A folytatáshoz meg kell adni a CSK vagy A KT kódot!");
        }
    }//GEN-LAST:event_okButtonActionPerformed
    
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
	doClose(RET_CANCEL);
    }//GEN-LAST:event_cancelButtonActionPerformed

    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
	doClose(RET_CANCEL);
    }//GEN-LAST:event_closeDialog

    private void szelessegKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_szelessegKeyReleased
	szam(evt);
    }//GEN-LAST:event_szelessegKeyReleased

    private void magassagKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_magassagKeyReleased
	szam(evt);
    }//GEN-LAST:event_magassagKeyReleased

    private void peldanyKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_peldanyKeyReleased
	szam(evt);
    }//GEN-LAST:event_peldanyKeyReleased

    private void egysegSulyKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_egysegSulyKeyReleased
	szam(evt);
    }//GEN-LAST:event_egysegSulyKeyReleased

    private void termekdijKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_termekdijKeyReleased
	if(!termekdij.getText().isEmpty())
        {
            productFeeMap.replace(comboBox_productFeeType.getSelectedItem().toString(), termekdij.getText());
        }
        
        szam(evt);
    }//GEN-LAST:event_termekdijKeyReleased

    private void sulyMegadasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sulyMegadasActionPerformed
	if (sulyMegadas.isSelected())
        {
	    osszSuly.requestFocus();
	}
        
	osszSuly.setEditable(sulyMegadas.isSelected());
	osszSuly.setEnabled(sulyMegadas.isSelected());
	szelesseg.setEditable(!sulyMegadas.isSelected());
	szelesseg.setEnabled(!sulyMegadas.isSelected());
	magassag.setEditable(!sulyMegadas.isSelected());
	magassag.setEnabled(!sulyMegadas.isSelected());
	peldany.setEditable(!sulyMegadas.isSelected());
	peldany.setEnabled(!sulyMegadas.isSelected());
	egysegSuly.setEditable(!sulyMegadas.isSelected());
	egysegSuly.setEnabled(!sulyMegadas.isSelected());
    }//GEN-LAST:event_sulyMegadasActionPerformed

    private void osszSulyKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_osszSulyKeyReleased
	szam(evt);
    }//GEN-LAST:event_osszSulyKeyReleased

    private void peldanyFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_peldanyFocusGained
	if (peldany.getText().equalsIgnoreCase("0"))
        {
	    peldany.setSelectionStart(0);
	    peldany.setSelectionEnd(1);
	}
    }//GEN-LAST:event_peldanyFocusGained

    private void szelessegFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_szelessegFocusGained
	if (szelesseg.getText().equalsIgnoreCase("0"))
        {
	    szelesseg.setSelectionStart(0);
	    szelesseg.setSelectionEnd(1);
	}
    }//GEN-LAST:event_szelessegFocusGained

    private void magassagFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_magassagFocusGained
	if (magassag.getText().equalsIgnoreCase("0"))
        {
	    magassag.setSelectionStart(0);
	    magassag.setSelectionEnd(1);
	}
    }//GEN-LAST:event_magassagFocusGained

    private void egysegSulyFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_egysegSulyFocusGained
	if (egysegSuly.getText().equalsIgnoreCase("0"))
        {
	    egysegSuly.setSelectionStart(0);
	    egysegSuly.setSelectionEnd(1);
	}
    }//GEN-LAST:event_egysegSulyFocusGained

    private void osszSulyFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_osszSulyFocusGained
	if (osszSuly.getText().equalsIgnoreCase("0"))
        {
	    osszSuly.setSelectionStart(0);
	    osszSuly.setSelectionEnd(1);
	}
    }//GEN-LAST:event_osszSulyFocusGained

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        CskKtListaDialog c = new CskKtListaDialog(CskKtListaDialog.CSK);
        if (c.getReturnStatus() == CskKtListaDialog.RET_OK) {
            csk.setText(c.getCsk());
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        CskKtListaDialog c = new CskKtListaDialog(CskKtListaDialog.KT);
        if (c.getReturnStatus() == CskKtListaDialog.RET_OK) {
            kt.setText(c.getKt());
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void comboBox_productFeeTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboBox_productFeeTypeItemStateChanged
        if(!comboboxUpdate && evt.getStateChange() == ItemEvent.SELECTED)
        {
            Label l = (Label)comboBox_productFeeType.getSelectedItem();
            termekdij.setText(productFeeMap.get(comboBox_productFeeType.getSelectedItem().toString()));
            szamolAr();
        }
    }//GEN-LAST:event_comboBox_productFeeTypeItemStateChanged
    
    private void doClose(int retStatus)
    {
	returnStatus = retStatus;
	setVisible(false);
	dispose();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JComboBox comboBox_productFeeType;
    private javax.swing.JTextField csk;
    private javax.swing.JTextField egysegSuly;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField kt;
    private javax.swing.JTextField magassag;
    private javax.swing.JTextField nev;
    private javax.swing.JButton okButton;
    private javax.swing.JTextField osszSuly;
    private javax.swing.JLabel osszesen;
    private javax.swing.JLabel osszesenLabel;
    private javax.swing.JLabel osszsulyLabel;
    private javax.swing.JTextField peldany;
    private javax.swing.JCheckBox sulyMegadas;
    private javax.swing.JTextField szelesseg;
    private javax.swing.JTextField termekdij;
    // End of variables declaration//GEN-END:variables
    private int returnStatus = RET_CANCEL;
    
    private void init()
    {
	Toolkit toolkit = Toolkit.getDefaultToolkit();
	Dimension screenSize = toolkit.getScreenSize();
	int locX = (screenSize.width - getWidth()) / 2;
	int locY = (screenSize.height - getHeight()) / 2;

	java.net.URL url = ClassLoader.getSystemResource("cezeszamlazo/resources/icon.png");
	java.awt.Image img = toolkit.createImage(url);
	setIconImage(img);

	setLocation(locX, locY);
	setTitle("Termék díj számítás");
        
	setModal(true);
	setVisible(true);
    }
    
    private String csakszam(String text, int size, boolean tizedes)
    {
	String valid = "-0123456789";
	
        if (tizedes)
        {
	    valid += ".";
	}
        
	text = text.replace(",", ".");
	String result = "";
	
        for (int i = 0; i < text.length(); i++)
        {
	    if (valid.contains(text.substring(i, i + 1)))
            {
		result += text.substring(i, i + 1);
	    }
	}
        
	if (size != 0)
        {
	    if (result.length() > size)
            {
		result = result.substring(0, size);
	    }
	}
        
	return result;
    }
    
    private void szam(KeyEvent evt)
    {
	JTextField field = (JTextField) evt.getSource();
	int pos = field.getCaretPosition();
	field.setText(csakszam(field.getText(), 0, true));
	
        try
        {
	    field.setCaretPosition(pos);
	}
        catch (Exception ex)
        {
            
	}
        
	if (!field.getText().isEmpty())
        {
	    szamolAr();
	}
    }
    
    private void szamolAr()
    {
        String tipus = comboBox_productFeeType.getSelectedItem().toString();
	double sz = Double.parseDouble(szelesseg.getText().replace(",", ".")),
            m = Double.parseDouble(magassag.getText().replace(",", ".")),
            pld = Double.parseDouble(peldany.getText().replace(",", ".")),
            td = Double.parseDouble(termekdij.getText().replace(",", ".")),
            s = Double.parseDouble(egysegSuly.getText().replace(",", "."));
        
	/*TermekDij t = new TermekDij
        (
            tipus,
            "",
            sz,
            m,
            pld,
            s,
            td,
            (sulyMegadas.isSelected() ? Double.parseDouble(osszSuly.getText().replace(",", ".")) : 0.0),
            csk.getText(),
            kt.getText()
        );

	if (!sulyMegadas.isSelected())
        {
	    osszSuly.setText(String.valueOf(t.getSuly()).replace(".", ","));
	}
        
	osszesen.setText(String.valueOf(t.getOsszTermekDijNetto(false)).replace(".", ",") + " Ft");*/
    }
    
    /*public TermekDij getTermekDij()
    {
	return new TermekDij(
            nev.getText(),
            Double.parseDouble(szelesseg.getText().replace(",", ".")),
            Double.parseDouble(magassag.getText().replace(",", ".")),
            Double.parseDouble(peldany.getText().replace(",", ".")),
            Double.parseDouble(egysegSuly.getText().replace(",", ".")),
            Double.parseDouble(termekdij.getText().replace(",", ".")),
            (sulyMegadas.isSelected() ? Double.parseDouble(osszSuly.getText().replace(",", ".")) : 0.0),
            csk.getText(),
            kt.getText()
        );
    }*/
    
    /*public TermekDij getTermekDij()
    {
        Label tipus = (Label) comboBox_productFeeType.getSelectedItem();
        
	return new TermekDij
        (
            tipus.getName(),
            nev.getText(),
            Double.parseDouble(szelesseg.getText().replace(",", ".")),
            Double.parseDouble(magassag.getText().replace(",", ".")),
            Double.parseDouble(peldany.getText().replace(",", ".")),
            Double.parseDouble(egysegSuly.getText().replace(",", ".")),
            //Double.parseDouble(termekdij.getText().replace(",", ".")),
            Double.parseDouble(productFeeMap.get(comboBox_productFeeType.getSelectedItem().toString())),
            (sulyMegadas.isSelected() ? Double.parseDouble(osszSuly.getText().replace(",", ".")) : 0.0),
            csk.getText(),
            kt.getText()
        );
    }*/
}
