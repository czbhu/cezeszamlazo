package cezeszamlazo;

import cezeszamlazo.database.Query;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author adam.papp
 */
public class VtszTeszorFrame extends javax.swing.JFrame
{
    public VtszTeszorFrame()
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        kereses = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        vtszTable = new javax.swing.JTable();

        setName("Form"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(cezeszamlazo.App.class).getContext().getResourceMap(VtszTeszorFrame.class);
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(resourceMap.getColor("jPanel1.border.lineColor"))); // NOI18N
        jPanel1.setName("jPanel1"); // NOI18N

        jLabel1.setFont(resourceMap.getFont("jLabel1.font")); // NOI18N
        jLabel1.setIcon(resourceMap.getIcon("jLabel1.icon")); // NOI18N
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setEnabled(false);
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

        vtszTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        vtszTable.setName("vtszTable"); // NOI18N
        vtszTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(vtszTable);
        if (vtszTable.getColumnModel().getColumnCount() > 0) {
            vtszTable.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("vtszTable.columnModel.title0")); // NOI18N
            vtszTable.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("vtszTable.columnModel.title1")); // NOI18N
            vtszTable.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("vtszTable.columnModel.title2")); // NOI18N
            vtszTable.getColumnModel().getColumn(3).setHeaderValue(resourceMap.getString("vtszTable.columnModel.title3")); // NOI18N
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(kereses, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(kereses)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void keresesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_keresesKeyReleased
        frissites();
    }//GEN-LAST:event_keresesKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField kereses;
    private javax.swing.JTable vtszTable;
    // End of variables declaration//GEN-END:variables

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
        setTitle("VTSZ/TESZOR sablonok");
    }

    private void frissites()
    {
        String keresText = EncodeDecode.encode(kereses.getText().replace("'", "\\'"));
        String whereText = "v.nev LIKE '%" + keresText + "%' || v.szam LIKE '%" + keresText + "%' || a.vatAmount LIKE '%" + keresText + "%'";
        DefaultTableModel model = (DefaultTableModel) vtszTable.getModel();
        String[] header = {"Id", "Név", "VTSZ/TESZOR", "ÁFA"};
        
        Query query = new Query.QueryBuilder()
            .select("v.id, "
                + "v.nev, "
                + "v.szam, "
                + "CONCAT(a.vatAmount, '%') ")
            .from("szamlazo_vtsz_sablon v, szamlazo_vats a ")
            .where("a.id = v.afaid && (" + whereText + ") ")
            .order("nev")
            .build();
        model.setDataVector(App.db.select(query.getQuery()), header);
        
        int[] meret = {30, 150, 50, 50};
        DefaultTableRender render = new DefaultTableRender();
        TableColumn col;
        
        for (int i = 0; i < meret.length; i++)
        {
            col = vtszTable.getColumnModel().getColumn(i);
            col.setCellRenderer(render);
            col.setPreferredWidth(meret[i]);
        }
    }

    public void nyit()
    {
        frissites();
        setVisible(true);
    }
}