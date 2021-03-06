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

public class VtszTeszorListaDialog extends javax.swing.JDialog {

    /**
     * A return status code - returned if Cancel button has been pressed
     */
    public static final int RET_CANCEL = 0;
    /**
     * A return status code - returned if OK button has been pressed
     */
    public static final int RET_OK = 1;
    private String vtszTeszor = "", afa = "0";

    public VtszTeszorListaDialog()
    {
        initComponents();

        frissites();
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

        kereses = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        listaTable = new javax.swing.JTable();

        setName("Form"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(cezeszamlazo.App.class).getContext().getResourceMap(VtszTeszorListaDialog.class);
        kereses.setText(resourceMap.getString("kereses.text")); // NOI18N
        kereses.setName("kereses"); // NOI18N
        kereses.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                keresesKeyReleased(evt);
            }
        });

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        listaTable.setModel(new javax.swing.table.DefaultTableModel(
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
        listaTable.setName("listaTable"); // NOI18N
        listaTable.getTableHeader().setReorderingAllowed(false);
        listaTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listaTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(listaTable);
        if (listaTable.getColumnModel().getColumnCount() > 0) {
            listaTable.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("listaTable.columnModel.title0")); // NOI18N
            listaTable.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("listaTable.columnModel.title1")); // NOI18N
            listaTable.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("listaTable.columnModel.title2")); // NOI18N
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
                    .addComponent(kereses, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(kereses, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE)
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

    private void listaTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listaTableMouseClicked
        int[] rows = listaTable.getSelectedRows();
        
        if (rows.length == 1 && evt.getClickCount() == 2 && evt.getButton() == MouseEvent.BUTTON1)
        {
            vtszTeszor = String.valueOf(listaTable.getValueAt(rows[0], 1));
            afa = String.valueOf(listaTable.getValueAt(rows[0], 2));
            doClose(RET_OK);
        }
    }//GEN-LAST:event_listaTableMouseClicked

    private void keresesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_keresesKeyReleased
        frissites();
    }//GEN-LAST:event_keresesKeyReleased

    private void doClose(int retStatus)
    {
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField kereses;
    private javax.swing.JTable listaTable;
    // End of variables declaration//GEN-END:variables
    private int returnStatus = RET_CANCEL;

    private void frissites()
    {
        DefaultTableModel model = (DefaultTableModel) listaTable.getModel();
        String[] header = {"Név", "VTSZ/TESZOR", "ÁFA"};
        String keresText = kereses.getText().replace("'", "\\\'"),
            whereText = "v.nev LIKE '%" + keresText + "%' || v.szam LIKE '%" + keresText + "%' || v.afa LIKE '%" + keresText + "%'";
        
        Query query = new Query.QueryBuilder()
            .select("v.nev, v.szam, (SELECT vatAmount from szamlazo_vats WHERE id = v.afaid) ")
            .from("szamlazo_vtsz_sablon v ")
            .where(whereText)
            .build();
        model.setDataVector(App.db.select(query.getQuery()), header);
        
        TableColumn col;
        int[] meret = {270, 100, 100};
        
        for (int i = 0; i < meret.length; i++)
        {
            col = listaTable.getColumnModel().getColumn(i);
            col.setPreferredWidth(meret[i]);
        }
    }

    public String getVtszTeszor() {
        return vtszTeszor;
    }

    public String getAfa() {
        return afa;
    }

    private void init()
    {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;

        java.net.URL url = ClassLoader.getSystemResource("cezeszamlazo/resources/vtsz.png");
        java.awt.Image img = toolkit.createImage(url);
        setIconImage(img);

        setLocation(x, y);
        setTitle("VTSZ/TESZOR lista");
        setModal(true);
        setVisible(true);
    }
}