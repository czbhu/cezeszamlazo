/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeszamlazo.ugyfel;

import cezeszamlazo.App;
import cezeszamlazo.EncodeDecode;
import cezeszamlazo.HibaDialog;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author pappadam
 */
public class UgyfelTallozasDialog extends javax.swing.JDialog {

    /**
     * A return status code - returned if Cancel button has been pressed
     */
    public static final int RET_CANCEL = 0;
    /**
     * A return status code - returned if OK button has been pressed
     */
    public static final int RET_OK = 1;
    
    /**
     * a kiválasztott ügyfél azonosítója
     */
    private int selectedUgyfelId = 0;
    /**
     * a kiválasztott ügyfél
     */
    private String selectedUgyfel = "";
    /**
     * a kiválasztott ügyfél név
     */
    private String selectedUgyfelNev = "";

    /**
     * Creates new form UgyfelTallozasDialog
     */
    public UgyfelTallozasDialog() {
        initComponents();

        frissites();

        init();
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

        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        keresesText = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        okButton.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        okButton.setText("Hozzáad");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        cancelButton.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cancelButton.setText("Vissza");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        keresesText.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        keresesText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                keresesTextKeyReleased(evt);
            }
        });

        table.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id", "nev", "cim"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table.setRowHeight(25);
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(table);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(okButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton))
                    .addComponent(keresesText, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(keresesText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE)
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
        kivalaszt();
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

    private void keresesTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_keresesTextKeyReleased
        frissites();
    }//GEN-LAST:event_keresesTextKeyReleased

    private void tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMouseClicked
        if (evt.getClickCount() == 2 && evt.getButton() == MouseEvent.BUTTON1) {
            kivalaszt();
        }
    }//GEN-LAST:event_tableMouseClicked

    private void doClose(int retStatus) {
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField keresesText;
    private javax.swing.JButton okButton;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables

    private int returnStatus = RET_CANCEL;

    private void frissites() {

        // a szórészletekre keresés        
        String w = "1";
        String k = EncodeDecode.decode(keresesText.getText());
        if (!k.isEmpty()) {
            String[] t = k.split(" ");
            String[] mezo = new String[]{"nev", "irsz", "varos", "utca", "id"};
            for (int i = 0; i < t.length; i++) {
                w += " && (";
                for (int j = 0; j < mezo.length; j++) {
                    w += mezo[j] + " LIKE '%" + t[i] + "%' || ";
                }
                w += "0)";
            }
        }

        DefaultTableModel model = (DefaultTableModel) table.getModel();
        String[] header = {"Id", "Ügyfél", "Cím"};

        model.setDataVector(App.db.select("SELECT "
                + "id, "
                + "nev,"
                + "CONCAT(irsz, ' ', varos, ', ', utca) "
                + "FROM " + Ugyfel.TABLE + " "
                + "WHERE " + w + " "
                + "ORDER BY nev ASC"), header);
        DefaultTableRender render = new DefaultTableRender();
        TableColumn col;
        int[] meret = {30, 125, 225};
        for (int i = 0; i < meret.length; i++) {
            col = table.getColumnModel().getColumn(i);
            col.setPreferredWidth(meret[i]);
            col.setCellRenderer(render);
        }
    }

    private void init() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;

//        setIconImage(PixiRendszer.img);
        setLocation(x, y);
        setTitle("Ügyfél tallózás");
        setModal(true);
        setVisible(true);
    }

    private void kivalaszt() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            selectedUgyfelId = Integer.parseInt(String.valueOf(table.getValueAt(row, 0)));
            selectedUgyfel = String.valueOf(table.getValueAt(row, 1)) + "\n" + String.valueOf(table.getValueAt(row, 2));
            selectedUgyfelNev = String.valueOf(table.getValueAt(row, 1));
            doClose(RET_OK);
        } else {
            HibaDialog h = new HibaDialog("Nincs sor kiválasztva!", "", "Ok");
        }
    }

    public String getSelectedUgyfelNev() {
        return selectedUgyfelNev;
    }
    
    public String getSelectedUgyfel() {
        return selectedUgyfel;
    }
    
    public int getSelectedUgyfelId() {
        return selectedUgyfelId;
    }
    
}