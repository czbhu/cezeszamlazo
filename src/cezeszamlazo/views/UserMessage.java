package cezeszamlazo.views;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Toolkit;

/**
 * @author Tomy
 */
public class UserMessage extends javax.swing.JDialog
{
    private String messageTitle;
    private String description;
    
    public UserMessage(String messageTitle, String description)
    {
        initComponents();
        
        this.messageTitle = messageTitle;
        this.description = description;
        
        Init();
    }
    
    private void Init()
    {
        SetTitle();
        SetDescription();
        
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;
        
        setLocation(x, y);
        setModal(true);
        setVisible(true);
    }
    
    private void SetTitle()
    {
        label_MessageTitle.setText(messageTitle);
    }
    
    private void SetDescription()
    {
        textArea_Description.setText(description);
        
        FontMetrics metrics = textArea_Description.getFontMetrics(textArea_Description.getFont());

        String [] rows = description.split("\n");
        int maxWidth = 0;
        
        for(int i = 0; i < rows.length; i++)
        {
            int rowWidth = metrics.stringWidth(rows[i]);
            
            if(rowWidth > maxWidth)
            {
                maxWidth = rowWidth;
            }
        }
        
        int lineWidth = maxWidth;
        int lineHeight = metrics.getHeight() * rows.length + 4;
        
        Dimension size = new Dimension(lineWidth + 40, lineHeight);
        
        this.setSize(new Dimension(lineWidth + 40, 105 + lineHeight));
        
        panel_Description.setPreferredSize(size);
        
        textArea_Description.setPreferredSize(size);
    }
    
    private void Close()
    {
        setVisible(false);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        label_MessageTitle = new javax.swing.JLabel();
        panel_Description = new javax.swing.JPanel();
        textArea_Description = new javax.swing.JTextArea();
        button_OK = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setName("Form"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(cezeszamlazo.App.class).getContext().getResourceMap(UserMessage.class);
        label_MessageTitle.setFont(resourceMap.getFont("label_MessageTitle.font")); // NOI18N
        label_MessageTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label_MessageTitle.setText(resourceMap.getString("label_MessageTitle.text")); // NOI18N
        label_MessageTitle.setName("label_MessageTitle"); // NOI18N

        panel_Description.setBackground(resourceMap.getColor("panel_Description.background")); // NOI18N
        panel_Description.setName("panel_Description"); // NOI18N

        textArea_Description.setBackground(resourceMap.getColor("textArea_Description.background")); // NOI18N
        textArea_Description.setColumns(20);
        textArea_Description.setFont(resourceMap.getFont("textArea_Description.font")); // NOI18N
        textArea_Description.setRows(5);
        textArea_Description.setText(resourceMap.getString("textArea_Description.text")); // NOI18N
        textArea_Description.setEnabled(false);
        textArea_Description.setName("textArea_Description"); // NOI18N

        javax.swing.GroupLayout panel_DescriptionLayout = new javax.swing.GroupLayout(panel_Description);
        panel_Description.setLayout(panel_DescriptionLayout);
        panel_DescriptionLayout.setHorizontalGroup(
            panel_DescriptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(textArea_Description, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        panel_DescriptionLayout.setVerticalGroup(
            panel_DescriptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(textArea_Description, javax.swing.GroupLayout.PREFERRED_SIZE, 18, Short.MAX_VALUE)
        );

        button_OK.setText(resourceMap.getString("button_OK.text")); // NOI18N
        button_OK.setName("button_OK"); // NOI18N
        button_OK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_OKActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label_MessageTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(button_OK, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(panel_Description, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label_MessageTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel_Description, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(button_OK)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void button_OKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_OKActionPerformed
        Close();
    }//GEN-LAST:event_button_OKActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton button_OK;
    private javax.swing.JLabel label_MessageTitle;
    private javax.swing.JPanel panel_Description;
    private javax.swing.JTextArea textArea_Description;
    // End of variables declaration//GEN-END:variables

    //SETTERS
    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    //GETTERS
    public String getMessageTitle() {
        return messageTitle;
    }

    public String getDescription() {
        return description;
    }
}