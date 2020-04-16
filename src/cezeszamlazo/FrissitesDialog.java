package cezeszamlazo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

public class FrissitesDialog extends javax.swing.JDialog
{
    /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;
    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;
    
    private int returnStatus = RET_CANCEL;

    public FrissitesDialog(String verzio, String leiras)
    {
	initComponents();

	this.verzio.setText("Elérhető az új " + verzio + " verzió!");
	this.leiras.setText(leiras);

	Toolkit toolkit = Toolkit.getDefaultToolkit();
	Dimension screenSize = toolkit.getScreenSize();
	int x = (screenSize.width - getWidth()) / 2;
	int y = (screenSize.height - getHeight()) / 2;

	java.net.URL url = ClassLoader.getSystemResource("cezeszamlazo/resources/refresh.png");
	java.awt.Image img = toolkit.createImage(url);
	setIconImage(img);

	setLocation(x, y);
	setTitle("Frissítés " + verzio + " verzióra");

	setModal(true);
	setVisible(true);
    }

    /** @return the return status of this dialog - one of RET_OK or RET_CANCEL */
    public int getReturnStatus() {
	return returnStatus;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        verzio = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        leiras = new javax.swing.JTextArea();
        cancel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        ok = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();

        setName("Form"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(cezeszamlazo.App.class).getContext().getResourceMap(FrissitesDialog.class);
        verzio.setFont(resourceMap.getFont("verzio.font")); // NOI18N
        verzio.setIcon(resourceMap.getIcon("verzio.icon")); // NOI18N
        verzio.setText(resourceMap.getString("verzio.text")); // NOI18N
        verzio.setName("verzio"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        leiras.setColumns(20);
        leiras.setEditable(false);
        leiras.setFont(resourceMap.getFont("leiras.font")); // NOI18N
        leiras.setLineWrap(true);
        leiras.setRows(5);
        leiras.setWrapStyleWord(true);
        leiras.setDisabledTextColor(resourceMap.getColor("leiras.disabledTextColor")); // NOI18N
        leiras.setEnabled(false);
        leiras.setName("leiras"); // NOI18N
        jScrollPane1.setViewportView(leiras);

        cancel.setBorder(javax.swing.BorderFactory.createLineBorder(resourceMap.getColor("ok.border.lineColor"))); // NOI18N
        cancel.setToolTipText(resourceMap.getString("cancel.toolTipText")); // NOI18N
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

        jLabel1.setIcon(resourceMap.getIcon("jLabel1.icon")); // NOI18N
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        javax.swing.GroupLayout cancelLayout = new javax.swing.GroupLayout(cancel);
        cancel.setLayout(cancelLayout);
        cancelLayout.setHorizontalGroup(
            cancelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cancelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        cancelLayout.setVerticalGroup(
            cancelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
        );

        ok.setBorder(javax.swing.BorderFactory.createLineBorder(resourceMap.getColor("ok.border.lineColor"))); // NOI18N
        ok.setToolTipText(resourceMap.getString("ok.toolTipText")); // NOI18N
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

        jLabel2.setIcon(resourceMap.getIcon("jLabel2.icon")); // NOI18N
        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        javax.swing.GroupLayout okLayout = new javax.swing.GroupLayout(ok);
        ok.setLayout(okLayout);
        okLayout.setHorizontalGroup(
            okLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(okLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        okLayout.setVerticalGroup(
            okLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .addComponent(verzio, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(ok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(verzio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ok, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
	doClose(RET_CANCEL);
    }//GEN-LAST:event_closeDialog

    private void cancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cancelMouseClicked
	doClose(RET_CANCEL);
    }//GEN-LAST:event_cancelMouseClicked

    private void okMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_okMouseClicked
	UpdateDialog u = new UpdateDialog();
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

    private void doClose(int retStatus) {
	returnStatus = retStatus;
	setVisible(false);
	dispose();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel cancel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea leiras;
    private javax.swing.JPanel ok;
    private javax.swing.JLabel verzio;
    // End of variables declaration//GEN-END:variables
}