package cezeszamlazo.views;

import cezeszamlazo.PrinterGetSet;
import java.awt.Dimension;
import java.awt.Toolkit;

/**
 * @author Tomy
 */
public class PrinterSelect extends javax.swing.JDialog
{
    public static final int RET_OK = 0, RET_CANCEL = 1;
    private int returnStatus = RET_CANCEL;
    
    public PrinterSelect()
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
        setTitle("Nyomtató választó");
        setModal(true);
        
        PrinterGetSet fillprintercombobox = new PrinterGetSet();
        fillprintercombobox.fillPrinterCombobox(comboBox_Printers);
        
        setVisible(true);
    }
    
    private void Close(int status)
    {
        returnStatus = status;
        setVisible(false);
        dispose();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        comboBox_Printers = new customs.CustomCombobox();
        customButton1 = new customs.CustomButton();
        customButton2 = new customs.CustomButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setName("Form"); // NOI18N

        jPanel2.setName("jPanel2"); // NOI18N

        comboBox_Printers.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "nyomtatók" }));
        comboBox_Printers.setName("comboBox_Printers"); // NOI18N
        comboBox_Printers.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboBox_PrintersItemStateChanged(evt);
            }
        });

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(cezeszamlazo.App.class).getContext().getResourceMap(PrinterSelect.class);
        customButton1.setBackground(resourceMap.getColor("customButton1.background")); // NOI18N
        customButton1.setForeground(resourceMap.getColor("customButton1.foreground")); // NOI18N
        customButton1.setIcon(resourceMap.getIcon("customButton1.icon")); // NOI18N
        customButton1.setText(resourceMap.getString("customButton1.text")); // NOI18N
        customButton1.setName("customButton1"); // NOI18N
        customButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customButton1ActionPerformed(evt);
            }
        });

        customButton2.setIcon(resourceMap.getIcon("customButton2.icon")); // NOI18N
        customButton2.setText(resourceMap.getString("customButton2.text")); // NOI18N
        customButton2.setName("customButton2"); // NOI18N
        customButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 111, Short.MAX_VALUE)
                        .addComponent(customButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(customButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(comboBox_Printers, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(comboBox_Printers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(customButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(customButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void comboBox_PrintersItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboBox_PrintersItemStateChanged
        PrinterGetSet printer = new PrinterGetSet();
        printer.setPrinterName(String.valueOf(comboBox_Printers.getSelectedItem()));
    }//GEN-LAST:event_comboBox_PrintersItemStateChanged

    private void customButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customButton1ActionPerformed
        Close(RET_OK);
    }//GEN-LAST:event_customButton1ActionPerformed

    private void customButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customButton2ActionPerformed
        Close(RET_CANCEL);
    }//GEN-LAST:event_customButton2ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private customs.CustomCombobox comboBox_Printers;
    private customs.CustomButton customButton1;
    private customs.CustomButton customButton2;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables

    //GETERS
    public int getReturnStatus() {
        return returnStatus;
    }
}