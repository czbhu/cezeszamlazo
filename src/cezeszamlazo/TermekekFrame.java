package cezeszamlazo;

import cezeszamlazo.database.Query;
import cezeszamlazo.model.PopupTimer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class TermekekFrame extends javax.swing.JFrame
{
    private PopupTimer popupTimer;
    
    public TermekekFrame()
    {
        initComponents();

        init();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        termekekPopupMenu = new javax.swing.JPopupMenu();
        megnyitasMenuItem = new javax.swing.JMenuItem();
        torlesMenuItem = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        kereses = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        termekekTable = new javax.swing.JTable();

        termekekPopupMenu.setName("termekekPopupMenu"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(cezeszamlazo.App.class).getContext().getResourceMap(TermekekFrame.class);
        megnyitasMenuItem.setText(resourceMap.getString("megnyitasMenuItem.text")); // NOI18N
        megnyitasMenuItem.setName("megnyitasMenuItem"); // NOI18N
        megnyitasMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                megnyitasMenuItemActionPerformed(evt);
            }
        });
        termekekPopupMenu.add(megnyitasMenuItem);

        torlesMenuItem.setText(resourceMap.getString("torlesMenuItem.text")); // NOI18N
        torlesMenuItem.setName("torlesMenuItem"); // NOI18N
        torlesMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                torlesMenuItemActionPerformed(evt);
            }
        });
        termekekPopupMenu.add(torlesMenuItem);

        setName("Form"); // NOI18N

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(resourceMap.getColor("jPanel1.border.lineColor"))); // NOI18N
        jPanel1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanel1MouseExited(evt);
            }
        });

        jLabel1.setFont(resourceMap.getFont("jLabel1.font")); // NOI18N
        jLabel1.setIcon(resourceMap.getIcon("jLabel1.icon")); // NOI18N
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
        );

        kereses.setFont(resourceMap.getFont("kereses.font")); // NOI18N
        kereses.setText(resourceMap.getString("kereses.text")); // NOI18N
        kereses.setName("kereses"); // NOI18N
        kereses.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                keresesKeyReleased(evt);
            }
        });

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        termekekTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "null", "null", "null"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        termekekTable.setName("termekekTable"); // NOI18N
        termekekTable.getTableHeader().setReorderingAllowed(false);
        termekekTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                termekekTableMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                termekekTableMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                termekekTableMouseReleased(evt);
            }
        });
        termekekTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                termekekTableKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(termekekTable);
        if (termekekTable.getColumnModel().getColumnCount() > 0) {
            termekekTable.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("termekekTable.columnModel.title0")); // NOI18N
            termekekTable.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("termekekTable.columnModel.title1")); // NOI18N
            termekekTable.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("termekekTable.columnModel.title2")); // NOI18N
            termekekTable.getColumnModel().getColumn(3).setHeaderValue(resourceMap.getString("termekekTable.columnModel.title3")); // NOI18N
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(kereses, javax.swing.GroupLayout.DEFAULT_SIZE, 377, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(kereses, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jPanel1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseEntered
        jPanel1.setBackground(Color.decode("#abd043"));
    }//GEN-LAST:event_jPanel1MouseEntered

    private void jPanel1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseExited
        jPanel1.setBackground(Color.decode("#f0f0f0"));
    }//GEN-LAST:event_jPanel1MouseExited

    private void keresesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_keresesKeyReleased
        frissites();
    }//GEN-LAST:event_keresesKeyReleased

    private void jPanel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseClicked
        UjTermekDialog t = new UjTermekDialog();
        if (t.getReturnStatus() == 1) {
            frissites();
        }
    }//GEN-LAST:event_jPanel1MouseClicked

    private void termekekTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_termekekTableMouseClicked
        int row = termekekTable.getSelectedRow();
        if (row >= 0 && evt.getClickCount() == 2 && evt.getButton() == MouseEvent.BUTTON1) {
            UjTermekDialog t = new UjTermekDialog(String.valueOf(termekekTable.getValueAt(row, 0)));
            if (t.getReturnStatus() == 1) {
                frissites();
            }
        }
    }//GEN-LAST:event_termekekTableMouseClicked

    private void termekekTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_termekekTableKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
            torol();
        }
    }//GEN-LAST:event_termekekTableKeyReleased

    private void megnyitasMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_megnyitasMenuItemActionPerformed
        int row = termekekTable.getSelectedRow();
        if (row >= 0) {
            UjTermekDialog t = new UjTermekDialog(String.valueOf(termekekTable.getValueAt(row, 0)));
            if (t.getReturnStatus() == 1) {
                frissites();
            }
        }
    }//GEN-LAST:event_megnyitasMenuItemActionPerformed

    private void torlesMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_torlesMenuItemActionPerformed
        torol();
    }//GEN-LAST:event_torlesMenuItemActionPerformed

    private void ShowPopupMenu(MouseEvent evt)
    {
        JTable source = (JTable) evt.getSource();
        int row = source.rowAtPoint(evt.getPoint());
        int column = source.columnAtPoint(evt.getPoint());

        if (!source.isRowSelected(row)) {
            source.changeSelection(row, column, false, false);
        }

        megnyitasMenuItem.setVisible(source.getSelectedRows().length == 1);

        termekekPopupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
    }
    
    private void termekekTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_termekekTableMouseReleased
        if (evt.isPopupTrigger())
        {
            ShowPopupMenu(evt);
        }
        else
        {
            if(popupTimer.Stop())
            {
                ShowPopupMenu(evt);
                popupTimer.setStart(0);
            }
        }
    }//GEN-LAST:event_termekekTableMouseReleased

    private void termekekTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_termekekTableMousePressed
        popupTimer = new PopupTimer();
        popupTimer.Start();
    }//GEN-LAST:event_termekekTableMousePressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField kereses;
    private javax.swing.JMenuItem megnyitasMenuItem;
    private javax.swing.JPopupMenu termekekPopupMenu;
    private javax.swing.JTable termekekTable;
    private javax.swing.JMenuItem torlesMenuItem;
    // End of variables declaration//GEN-END:variables

    private void init()
    {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;

        java.net.URL url = ClassLoader.getSystemResource("cezeszamlazo/resources/termek.png");
        java.awt.Image img = toolkit.createImage(url);
        setIconImage(img);

        setLocation(x, y);
        setTitle("Termékek");
    }

    private void frissites()
    {
        DefaultTableModel model = (DefaultTableModel) termekekTable.getModel();
        String[] header = {"Id", "Név", "Cikkszám", "ÁFA", "VTSZ/TESZOR", "Nettó", "Bruttó"};
        int[] meret = {50, 200, 100, 50, 80, 70, 70};
        String keresText = EncodeDecode.encode(kereses.getText().replace("'", "\\'"));
        String whereText = "t.nev LIKE '%" + keresText + "%' || t.cikkszam LIKE '%" + keresText + "%' || t.vtsz_teszor LIKE '%" + keresText + "%' || t.id = '" + keresText + "'";
        
        Query query = new Query.QueryBuilder()
            .select("t.id, "
                + "t.nev, "
                + "t.cikkszam, "
                + "a.vatAmount, "
                + "t.vtsz_teszor, "
                + "t.netto_ar, "
                + "ROUND(t.netto_ar + t.netto_ar * (a.vatAmount / 100)) ")
            .from("szamlazo_termek t, szamlazo_vats a ")
            .where("(" + whereText + ") && t.afaid = a.id ")
            .order("nev ASC")
            .build();
        model.setDataVector(App.db.select(query.getQuery()), header);

        TableColumn col;
        DefaultTableRender render = new DefaultTableRender(new int[]{5, 6});
        
        for (int i = 0; i < meret.length; i++)
        {
            col = termekekTable.getColumnModel().getColumn(i);
            col.setPreferredWidth(meret[i]);
            col.setCellRenderer(render);
        }
    }

    private void torol()
    {
        int[] rows = termekekTable.getSelectedRows();
        
        if (rows.length > 0)
        {
            HibaDialog h = new HibaDialog(this, "Biztosan törlöd a kiválaszott " + (rows.length == 1 ? "sort" : "sorokat") + "?", "Igen", "Nem");
            
            if (h.getReturnStatus() == 1)
            {
                String keres = "(";
                
                for (int i = 0; i < rows.length; i++)
                {
                    keres += String.valueOf(termekekTable.getValueAt(rows[i], 0)) + ", ";
                }
                
                keres += "0)";
                App.db.delete("DELETE FROM szamlazo_termek WHERE id IN " + keres);
                frissites();
            }
        }
        else
        {
            HibaDialog h = new HibaDialog(this, "Nincs sor kiválasztva!", "Ok", "");
        }
    }

    public void nyit()
    {
        frissites();
        setVisible(true);
    }
}