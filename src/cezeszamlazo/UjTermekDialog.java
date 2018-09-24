/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

 /*
 * UjTermekDialog.java
 *
 * Created on 2012.05.21., 12:47:45
 */
package cezeszamlazo;

import cezeszamlazo.database.Query;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

/**
 *
 * @author adam.papp
 */
public class UjTermekDialog extends javax.swing.JDialog {

    /**
     * A return status code - returned if Cancel button has been pressed
     */
    public static final int RET_CANCEL = 0;
    /**
     * A return status code - returned if OK button has been pressed
     */
    public static final int RET_OK = 1;

    private String id = "0";

    /**
     * Creates new form UjTermekDialog
     */
    public UjTermekDialog() {
        initComponents();

        csoportFrissites(csoport1, "1. csoport", "0");
        csoportFrissites(csoport2, "2. csoport", "0");
        csoportFrissites(csoport3, "3. csoport", "0");

        afaFrissites("");

        init("Új termék");

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

    public UjTermekDialog(String id) {
        initComponents();

        this.id = id;
        Query query = new Query.QueryBuilder()
                .select("nev, "
                        + "cikkszam, "
                        + "mennyisegi_egyseg, "
                        + "vtsz_teszor, "
                        + "suly, "
                        + "netto_ar, "
                        + "csoportok, "
                        + "afaid ")
                .from("szamlazo_termek")
                .where("id = " + id)
                .order("")
                .build();

        Object[][] s = App.db.select(query.getQuery());

        nev.setText(String.valueOf(s[0][0]));
        cikkszam.setText(String.valueOf(s[0][1]));
        mee.setText(String.valueOf(s[0][2]));
        vtszTeszor.setText(String.valueOf(s[0][3]));
        suly.setText(String.valueOf(s[0][4]));
        netto.setText(String.valueOf(s[0][5]));

        String[] temp = String.valueOf(s[0][6]).split(";");
        csoportFrissites(csoport1, "1. csoport", temp[0]);
        csoportFrissites(csoport2, "2. csoport", temp[1]);
        csoportFrissites(csoport3, "3. csoport", temp[2]);

        afaFrissites(String.valueOf(s[0][7]));

        init("Termék módosítása");

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
        nev = new javax.swing.JTextField();
        afa = new javax.swing.JComboBox();
        jLabel22 = new javax.swing.JLabel();
        mee = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        cikkszam = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        netto = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        csoport3 = new javax.swing.JComboBox();
        jLabel24 = new javax.swing.JLabel();
        csoport2 = new javax.swing.JComboBox();
        jLabel23 = new javax.swing.JLabel();
        csoport1 = new javax.swing.JComboBox();
        suly = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        vtszTeszor = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setName("Form"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(cezeszamlazo.App.class).getContext().getResourceMap(UjTermekDialog.class);
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

        nev.setName("nev"); // NOI18N

        afa.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "25", "5", "0" }));
        afa.setName("afa"); // NOI18N

        jLabel22.setText(resourceMap.getString("jLabel22.text")); // NOI18N
        jLabel22.setName("jLabel22"); // NOI18N

        mee.setText(resourceMap.getString("mee.text")); // NOI18N
        mee.setName("mee"); // NOI18N

        jLabel21.setText(resourceMap.getString("jLabel21.text")); // NOI18N
        jLabel21.setName("jLabel21"); // NOI18N

        cikkszam.setName("cikkszam"); // NOI18N

        jLabel20.setText(resourceMap.getString("jLabel20.text")); // NOI18N
        jLabel20.setName("jLabel20"); // NOI18N

        netto.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        netto.setText(resourceMap.getString("netto.text")); // NOI18N
        netto.setName("netto"); // NOI18N

        jLabel19.setText(resourceMap.getString("jLabel19.text")); // NOI18N
        jLabel19.setName("jLabel19"); // NOI18N

        jLabel25.setText(resourceMap.getString("jLabel25.text")); // NOI18N
        jLabel25.setName("jLabel25"); // NOI18N

        jLabel18.setText(resourceMap.getString("jLabel18.text")); // NOI18N
        jLabel18.setName("jLabel18"); // NOI18N

        jLabel17.setText(resourceMap.getString("jLabel17.text")); // NOI18N
        jLabel17.setName("jLabel17"); // NOI18N

        csoport3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "3. csoport" }));
        csoport3.setName("csoport3"); // NOI18N

        jLabel24.setText(resourceMap.getString("jLabel24.text")); // NOI18N
        jLabel24.setName("jLabel24"); // NOI18N

        csoport2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "2. csoport" }));
        csoport2.setName("csoport2"); // NOI18N

        jLabel23.setText(resourceMap.getString("jLabel23.text")); // NOI18N
        jLabel23.setName("jLabel23"); // NOI18N

        csoport1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1. csoport" }));
        csoport1.setName("csoport1"); // NOI18N

        suly.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        suly.setText(resourceMap.getString("suly.text")); // NOI18N
        suly.setName("suly"); // NOI18N

        jLabel27.setText(resourceMap.getString("jLabel27.text")); // NOI18N
        jLabel27.setName("jLabel27"); // NOI18N

        vtszTeszor.setName("vtszTeszor"); // NOI18N

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jButton1.setFont(resourceMap.getFont("jButton1.font")); // NOI18N
        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setMargin(new java.awt.Insets(2, 7, 2, 7));
        jButton1.setName("jButton1"); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel20)
                            .addComponent(jLabel19)
                            .addComponent(jLabel17)
                            .addComponent(jLabel18))
                        .addGap(28, 28, 28)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(mee, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(afa, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel21)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(suly, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel25))
                                    .addComponent(jLabel27)))
                            .addComponent(cikkszam, javax.swing.GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE)
                            .addComponent(nev, javax.swing.GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel22)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(vtszTeszor, javax.swing.GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel23)
                            .addComponent(jLabel24))
                        .addGap(22, 22, 22)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(netto, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel1))
                            .addComponent(csoport2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(csoport1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(csoport3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton)))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cancelButton, okButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(nev, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(cikkszam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(mee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25)
                    .addComponent(jLabel21)
                    .addComponent(suly, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(afa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(vtszTeszor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(csoport1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(csoport2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(csoport3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(netto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(okButton))
                .addContainerGap())
        );

        getRootPane().setDefaultButton(okButton);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        if (nev.getText().isEmpty()) {
            HibaDialog h = new HibaDialog("Nincs megadva név!", "Ok", "");
            nev.requestFocus();
        } else if (vtszTeszor.getText().isEmpty()) {
            HibaDialog h = new HibaDialog("Nincs megadva VTSZ/TESZOR!", "Ok", "");
            vtszTeszor.requestFocus();
        } else {
            Object[] o = new Object[8];
            o[0] = nev.getText();
            o[1] = cikkszam.getText();
            o[2] = mee.getText();
            o[3] = suly.getText();
            Label l = (Label) afa.getSelectedItem();
            o[4] = l.getId();
            o[5] = vtszTeszor.getText();
            String csop = "";
            l = (Label) csoport1.getSelectedItem();
            csop += l.getId() + ";";
            l = (Label) csoport2.getSelectedItem();
            csop += l.getId() + ";";
            l = (Label) csoport3.getSelectedItem();
            csop += l.getId() + ";";
            o[6] = csop;
            o[7] = netto.getText();
            if (id.equalsIgnoreCase("0")) {
                App.db.insert("INSERT INTO szamlazo_termek (nev, cikkszam, mennyisegi_egyseg, suly, afaid, vtsz_teszor, csoportok, netto_ar) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)", o, o.length);
            } else {
                App.db.insert("UPDATE szamlazo_termek SET nev = ?, cikkszam = ?, mennyisegi_egyseg = ?, suly = ?, afaid = ?, vtsz_teszor = ?, csoportok = ?, netto_ar = ? WHERE id = " + id, o, o.length);
            }
            doClose(RET_OK);
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

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        VtszTeszorListaDialog v = new VtszTeszorListaDialog();
        if (v.getReturnStatus() == 1) {
            vtszTeszor.setText(v.getVtszTeszor());
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void doClose(int retStatus) {
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox afa;
    private javax.swing.JButton cancelButton;
    private javax.swing.JTextField cikkszam;
    private javax.swing.JComboBox csoport1;
    private javax.swing.JComboBox csoport2;
    private javax.swing.JComboBox csoport3;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JTextField mee;
    private javax.swing.JTextField netto;
    private javax.swing.JTextField nev;
    private javax.swing.JButton okButton;
    private javax.swing.JTextField suly;
    private javax.swing.JTextField vtszTeszor;
    // End of variables declaration//GEN-END:variables
    private int returnStatus = RET_CANCEL;

    private void init(String title) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;

        java.net.URL url = ClassLoader.getSystemResource("cezeszamlazo/resources/uj_termek_16.png");
        java.awt.Image img = toolkit.createImage(url);
        setIconImage(img);

        setLocation(x, y);
        setTitle(title);

        setModal(true);
        setVisible(true);
    }

    private void csoportFrissites(JComboBox csoport, String nev, String csid) {
        Query query = new Query.QueryBuilder()
                .select("id, nev ")
                .from("szamlazo_termek_csoportok")
                .order("nev")
                .build();
        Object[][] s = App.db.select(query.getQuery());
        int j = 0;
        csoport.removeAll();
        csoport.removeAllItems();
        csoport.addItem(new Label("0", nev));
        for (int i = 0; i < s.length; i++) {
            String temp = String.valueOf(s[i][0]);
            if (temp.equalsIgnoreCase(csid)) {
                j = i + 1;
            }
            csoport.addItem(new Label(temp, String.valueOf(s[i][1])));
        }
        if (j != 0) {
            csoport.setSelectedIndex(j);
        }
    }

    private void afaFrissites(String aid) {
        int j = 0;
        Query query = new Query.QueryBuilder()
                .select("id, afa ")
                .from("szamlazo_afa")
                .order("afa DESC")
                .build();
        Object[][] s = App.db.select(query.getQuery());
        afa.removeAll();
        afa.removeAllItems();
        for (int i = 0; i < s.length; i++) {
            String temp = String.valueOf(s[i][0]);
            if (temp.equalsIgnoreCase(aid)) {
                j = i;
            }
            afa.addItem(new Label(temp, String.valueOf(s[i][1])));
        }
        afa.setSelectedIndex(j);
    }
}
