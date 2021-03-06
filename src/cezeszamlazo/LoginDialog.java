package cezeszamlazo;

import cezeszamlazo.controller.Functions;
import cezeszamlazo.database.Query;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import java.security.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginDialog extends javax.swing.JDialog
{
    /**
     * A return status code - returned if Cancel button has been pressed
     */
    public static final int RET_CANCEL = 0;
    /**
     * A return status code - returned if OK button has been pressed
     */
    public static final int RET_OK = 1;
//    private Serial main;

    public LoginDialog()
    {
        initComponents();

//	main = new Serial();
//	main.initialize();
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;

        java.net.URL url = ClassLoader.getSystemResource("cezeszamlazo/resources/lock.png");
        java.awt.Image img = toolkit.createImage(url);
        setIconImage(img);

        setLocation(x, y);
        setTitle("Bejelentkezés");

        setModal(true);
        setVisible(true);
//	LoginThread lt = new LoginThread();

        // Close the dialog when Esc is pressed
        String cancelName = "cancel";
        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), cancelName);
        
        ActionMap actionMap = getRootPane().getActionMap();
        actionMap.put(cancelName, new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                doClose(RET_CANCEL);
            }
        });
    }

    /**
     * @return the return status of this dialog - one of RET_OK or RET_CANCEL
     */
    public int getReturnStatus() {
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

        jLabel1 = new javax.swing.JLabel();
        usernev = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jelszo = new javax.swing.JPasswordField();
        cancel = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        ok = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();

        setName("Form"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(cezeszamlazo.App.class).getContext().getResourceMap(LoginDialog.class);
        jLabel1.setFont(resourceMap.getFont("jLabel1.font")); // NOI18N
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        usernev.setFont(resourceMap.getFont("usernev.font")); // NOI18N
        usernev.setName("usernev"); // NOI18N
        usernev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usernevActionPerformed(evt);
            }
        });

        jLabel2.setFont(resourceMap.getFont("jLabel1.font")); // NOI18N
        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jelszo.setFont(resourceMap.getFont("jelszo.font")); // NOI18N
        jelszo.setName("jelszo"); // NOI18N
        jelszo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jelszoActionPerformed(evt);
            }
        });

        cancel.setBorder(javax.swing.BorderFactory.createLineBorder(resourceMap.getColor("ok.border.lineColor"))); // NOI18N
        cancel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cancel.setName("cancel"); // NOI18N
        cancel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cancelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                cancelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                cancelMouseExited(evt);
            }
        });

        jLabel3.setIcon(resourceMap.getIcon("jLabel3.icon")); // NOI18N
        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        javax.swing.GroupLayout cancelLayout = new javax.swing.GroupLayout(cancel);
        cancel.setLayout(cancelLayout);
        cancelLayout.setHorizontalGroup(
            cancelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cancelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        cancelLayout.setVerticalGroup(
            cancelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
        );

        ok.setBorder(javax.swing.BorderFactory.createLineBorder(resourceMap.getColor("ok.border.lineColor"))); // NOI18N
        ok.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ok.setName("ok"); // NOI18N
        ok.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                okMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                okMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                okMouseExited(evt);
            }
        });

        jLabel4.setIcon(resourceMap.getIcon("jLabel4.icon")); // NOI18N
        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        javax.swing.GroupLayout okLayout = new javax.swing.GroupLayout(ok);
        ok.setLayout(okLayout);
        okLayout.setHorizontalGroup(
            okLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(okLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        okLayout.setVerticalGroup(
            okLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(ok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(usernev, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                    .addComponent(jelszo, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(usernev, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jelszo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ok, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cancel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        doClose(RET_CANCEL);
    }//GEN-LAST:event_closeDialog

    private void cancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cancelMouseClicked
        doClose(RET_CANCEL);
    }//GEN-LAST:event_cancelMouseClicked

    private void okMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_okMouseClicked
        login();
    }//GEN-LAST:event_okMouseClicked

    private void okMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_okMouseEntered
        ok.setBackground(Color.decode("#abd043"));
    }//GEN-LAST:event_okMouseEntered

    private void okMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_okMouseExited
        ok.setBackground(Color.decode("#f0f0f0"));
    }//GEN-LAST:event_okMouseExited

    private void cancelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cancelMouseEntered
        cancel.setBackground(Color.decode("#d24343"));
    }//GEN-LAST:event_cancelMouseEntered

    private void cancelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cancelMouseExited
        cancel.setBackground(Color.decode("#f0f0f0"));
    }//GEN-LAST:event_cancelMouseExited

    private void jelszoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jelszoActionPerformed
        login();
    }//GEN-LAST:event_jelszoActionPerformed

    private void usernevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usernevActionPerformed
        login();
    }//GEN-LAST:event_usernevActionPerformed

    private void doClose(int retStatus) {
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel cancel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPasswordField jelszo;
    private javax.swing.JPanel ok;
    private javax.swing.JTextField usernev;
    // End of variables declaration//GEN-END:variables
    
    private int returnStatus = RET_CANCEL;
//    class LoginThread extends Thread {
//	
//	public LoginThread() {
//	    start();
//	}
//	
//	@Override
//	public void run() {
//	    while (true) {
//		System.out.println(main.getData());
//	    }
//	}
//    }

    private void login()
    {
        if (usernev.getText().isEmpty())
        {
            HibaDialog hd = new HibaDialog(this, "A felhasználónév mező üres!", "Ok", "");
        }
        else if (new String(jelszo.getPassword()).isEmpty())
        {
            HibaDialog hd = new HibaDialog(this, "A jelszó mező üres!", "Ok", "");
        }
        else
        {
            try
            {
                String usernameText = usernev.getText();
                String jelszoText = new String(jelszo.getPassword());
                
                jelszoText = Functions.md5(jelszoText);
                
                Query query = new Query.QueryBuilder()
                    //.select("id, nev, usernev, jelszo, csoport, ceg")
                    .select("id, name, username, password, `group`, companyIDs, defaultCompanyID")
                    .from("szamlazo_users")
                    .where("username = '" + usernameText + "' && password = ('" + jelszoText + "')")
                    .build();
                Object[][] select = App.db.select(query.getQuery());
                
                if (select.length != 0)
                {
                    App.user = new User(select);
                    doClose(RET_OK);
                }
                else
                {
                    HibaDialog hd = new HibaDialog(this, "Hibás adatok!", "Ok", "");
                }
            }
            catch (NoSuchAlgorithmException ex)
            {
                Logger.getLogger(LoginDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}