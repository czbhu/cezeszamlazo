package cezeszamlazo.views;

import invoice.MeasureOfUnit;
import java.awt.Dimension;
import java.awt.Toolkit;

/**
 * @author Tomy
 */
public class NewMeasureOfUnitView extends javax.swing.JDialog
{
    public static final int RET_OK = 1, RET_CANCEL = 0;
    private int returnStatus = RET_CANCEL;
    
    private String name;
    private String shortName;
    
    public NewMeasureOfUnitView()
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

        setModal(true);
        setLocation(x, y);
        
        setTitle("Új mennyiségi egység megadása");
    }
    
    public void Open()
    {
        setVisible(true);
    }
    
    private void Close(int status)
    {
        returnStatus = status;
        dispose();
        setVisible(false);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel_nemMeasureOfUnit = new javax.swing.JPanel();
        label_NewMeasureOfUnit = new javax.swing.JLabel();
        panel_measurOfUnit = new javax.swing.JPanel();
        label_Name = new javax.swing.JLabel();
        textField_Name = new javax.swing.JTextField();
        label_Short = new javax.swing.JLabel();
        textField_Short = new javax.swing.JTextField();
        button_Save = new customs.CustomButton();
        button_Back = new customs.CustomButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setName("Form"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(cezeszamlazo.App.class).getContext().getResourceMap(NewMeasureOfUnitView.class);
        panel_nemMeasureOfUnit.setBackground(resourceMap.getColor("panel_nemMeasureOfUnit.background")); // NOI18N
        panel_nemMeasureOfUnit.setName("panel_nemMeasureOfUnit"); // NOI18N

        label_NewMeasureOfUnit.setFont(resourceMap.getFont("label_NewMeasureOfUnit.font")); // NOI18N
        label_NewMeasureOfUnit.setForeground(resourceMap.getColor("label_NewMeasureOfUnit.foreground")); // NOI18N
        label_NewMeasureOfUnit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label_NewMeasureOfUnit.setText(resourceMap.getString("label_NewMeasureOfUnit.text")); // NOI18N
        label_NewMeasureOfUnit.setName("label_NewMeasureOfUnit"); // NOI18N

        panel_measurOfUnit.setBackground(resourceMap.getColor("panel_measurOfUnit.background")); // NOI18N
        panel_measurOfUnit.setName("panel_measurOfUnit"); // NOI18N

        label_Name.setText(resourceMap.getString("label_Name.text")); // NOI18N
        label_Name.setName("label_Name"); // NOI18N

        textField_Name.setText(resourceMap.getString("textField_Name.text")); // NOI18N
        textField_Name.setName("textField_Name"); // NOI18N

        label_Short.setText(resourceMap.getString("label_Short.text")); // NOI18N
        label_Short.setName("label_Short"); // NOI18N

        textField_Short.setText(resourceMap.getString("textField_Short.text")); // NOI18N
        textField_Short.setName("textField_Short"); // NOI18N

        button_Save.setText(resourceMap.getString("button_Save.text")); // NOI18N
        button_Save.setName("button_Save"); // NOI18N
        button_Save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_SaveActionPerformed(evt);
            }
        });

        button_Back.setText(resourceMap.getString("button_Back.text")); // NOI18N
        button_Back.setName("button_Back"); // NOI18N
        button_Back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_BackActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel_measurOfUnitLayout = new javax.swing.GroupLayout(panel_measurOfUnit);
        panel_measurOfUnit.setLayout(panel_measurOfUnitLayout);
        panel_measurOfUnitLayout.setHorizontalGroup(
            panel_measurOfUnitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_measurOfUnitLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_measurOfUnitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_measurOfUnitLayout.createSequentialGroup()
                        .addGroup(panel_measurOfUnitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(label_Short, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(label_Name, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panel_measurOfUnitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(textField_Name)
                            .addComponent(textField_Short)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_measurOfUnitLayout.createSequentialGroup()
                        .addGap(0, 246, Short.MAX_VALUE)
                        .addComponent(button_Save, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(button_Back, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        panel_measurOfUnitLayout.setVerticalGroup(
            panel_measurOfUnitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_measurOfUnitLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_measurOfUnitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label_Name)
                    .addComponent(textField_Name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panel_measurOfUnitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label_Short)
                    .addComponent(textField_Short, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_measurOfUnitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(button_Save, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(button_Back, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panel_nemMeasureOfUnitLayout = new javax.swing.GroupLayout(panel_nemMeasureOfUnit);
        panel_nemMeasureOfUnit.setLayout(panel_nemMeasureOfUnitLayout);
        panel_nemMeasureOfUnitLayout.setHorizontalGroup(
            panel_nemMeasureOfUnitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_nemMeasureOfUnitLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label_NewMeasureOfUnit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(panel_measurOfUnit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panel_nemMeasureOfUnitLayout.setVerticalGroup(
            panel_nemMeasureOfUnitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_nemMeasureOfUnitLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(label_NewMeasureOfUnit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel_measurOfUnit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel_nemMeasureOfUnit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel_nemMeasureOfUnit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void button_SaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_SaveActionPerformed
        name = textField_Name.getText();
        shortName = textField_Short.getText();
        
        MeasureOfUnit measureOfUnit = new MeasureOfUnit(name, shortName);
        measureOfUnit.Save();
        
        Close(RET_OK);
    }//GEN-LAST:event_button_SaveActionPerformed

    private void button_BackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_BackActionPerformed
        Close(RET_CANCEL);
    }//GEN-LAST:event_button_BackActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private customs.CustomButton button_Back;
    private customs.CustomButton button_Save;
    private javax.swing.JLabel label_Name;
    private javax.swing.JLabel label_NewMeasureOfUnit;
    private javax.swing.JLabel label_Short;
    private javax.swing.JPanel panel_measurOfUnit;
    private javax.swing.JPanel panel_nemMeasureOfUnit;
    private javax.swing.JTextField textField_Name;
    private javax.swing.JTextField textField_Short;
    // End of variables declaration//GEN-END:variables

     //GETTERS
    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    public int getReturnStatus() {
        return returnStatus;
    }
}