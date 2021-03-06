package cezeszamlazo;

import cezeszamlazo.database.Query;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 * @author fejlesztes2
 */
public class CskKtListaDialog extends javax.swing.JDialog
{
    /**
     * A return status code - returned if Cancel button has been pressed
     */
    public static final int RET_CANCEL = 0;
    /**
     * A return status code - returned if OK button has been pressed
     */
    public static final int RET_OK = 1;
    public static final int MIND = 0, CSK = 1, KT = 2;

    private int row = -1;

    public CskKtListaDialog(int tipus)
    {
        initComponents();

        String title = "";

        switch (tipus)
        {
            case MIND:
                title = "CSK/KT kódok";
                break;

            case CSK:
                title = "CSK kódok";
                break;

            case KT:
                title = "KT kódok";
                break;
        }

        frissites(tipus);

        init(title);

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

        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        cskKtTable = new javax.swing.JTable();

        setName("Form"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(cezeszamlazo.App.class).getContext().getResourceMap(CskKtListaDialog.class);
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

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        cskKtTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "null"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        cskKtTable.setName("cskKtTable"); // NOI18N
        cskKtTable.getTableHeader().setReorderingAllowed(false);
        cskKtTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cskKtTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(cskKtTable);
        if (cskKtTable.getColumnModel().getColumnCount() > 0) {
            cskKtTable.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("cskKtTable.columnModel.title0")); // NOI18N
            cskKtTable.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("cskKtTable.columnModel.title1")); // NOI18N
            cskKtTable.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("cskKtTable.columnModel.title2")); // NOI18N
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(okButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(okButton))
                .addContainerGap())
        );

        getRootPane().setDefaultButton(okButton);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        row = cskKtTable.getSelectedRow();
        if (row >= 0) {
            doClose(RET_OK);
        } else {
            HibaDialog h = new HibaDialog("Nincs sor kiválasztva!", "", "Ok");
        }
    }//GEN-LAST:event_okButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        doClose(RET_CANCEL);
    }//GEN-LAST:event_cancelButtonActionPerformed

    /**
     * Closes the dialog
     */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        doClose(RET_CANCEL);
    }//GEN-LAST:event_closeDialog

    private void cskKtTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cskKtTableMouseClicked
        row = cskKtTable.getSelectedRow();
        
        if (row >= 0 && evt.getClickCount() == 2 && evt.getButton() == MouseEvent.BUTTON1)
        {
            doClose(RET_OK);
        }
    }//GEN-LAST:event_cskKtTableMouseClicked

    private void doClose(int retStatus)
    {
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JTable cskKtTable;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton okButton;
    // End of variables declaration//GEN-END:variables
    private int returnStatus = RET_CANCEL;

    private void init(String title)
    {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int locX = (screenSize.width - getWidth()) / 2;
        int locY = (screenSize.height - getHeight()) / 2;

        java.net.URL url = ClassLoader.getSystemResource("cezeszamlazo/resources/icon.png");
        java.awt.Image img = toolkit.createImage(url);
        setIconImage(img);

        setLocation(locX, locY);
        setTitle(title);

        setModal(true);
        setVisible(true);
    }

    private void frissites(int tipus)
    {
        DefaultTableModel model = (DefaultTableModel) cskKtTable.getModel();
        String[] header = {"Id", "Név", "CSK", "KT"};
        String whereString = (tipus == CSK ? "csk != ''" : (tipus == KT ? "kt != ''" : "1"));
        
        Query query = new Query.QueryBuilder()
            .select("id, nev, csk, kt ")
            .from("szamlazo_csk_kt")
            .where(whereString)
            .build();
        model.setDataVector(App.db.select(query.getQuery()), header);
        
        TableColumn col;
        int[] meret = {30, 200, 40, 40};
        
        for (int i = 0; i < meret.length; i++)
        {
            col = cskKtTable.getColumnModel().getColumn(i);
            col.setPreferredWidth(meret[i]);
        }
    }

    public String getCsk()
    {
        return String.valueOf(cskKtTable.getValueAt(row, 2));
    }

    public String getKt()
    {
        return String.valueOf(cskKtTable.getValueAt(row, 3));
    }
}