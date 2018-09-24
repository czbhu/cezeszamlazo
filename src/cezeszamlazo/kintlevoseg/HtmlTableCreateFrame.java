/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeszamlazo.kintlevoseg;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author szekus
 */
public class HtmlTableCreateFrame extends javax.swing.JFrame {

    private String htmlTableString;

    public String getHtmlTableString() {
        return htmlTableString;
    }

    /**
     * A return status code - returned if Cancel button has been pressed
     */
    public static final int RET_CANCEL = 0;
    /**
     * A return status code - returned if OK button has been pressed
     */
    public static final int RET_OK = 1;

    /**
     * @return the return status of this dialog - one of RET_OK or RET_CANCEL
     */
    public int getReturnStatus() {
        return returnStatus;
    }
    
     private void doClose(int retStatus) {
	returnStatus = retStatus;
	setVisible(false);
	dispose();
    }
     
     private int returnStatus = RET_CANCEL;

    /**
     * Creates new form HtmlTableCreateFrame
     */
    public HtmlTableCreateFrame() {
        initComponents();

        KintlevosegLevelAttributum kintlevosegLevelAttributum = new KintlevosegLevelAttributum();
        List<String> allName = kintlevosegLevelAttributum.getAllName();
        DefaultTableModel model = (DefaultTableModel) trTdTable.getModel();
        String[] header = new String[]{"érték", "oszlop neve", "kiválaszt"};

        Object[][] dataVector = new Object[allName.size()][3];
        for (int i = 0; i < allName.size(); i++) {
            Object cell = allName.get(i);
            dataVector[i][0] = cell;
            dataVector[i][1] = cell;
            dataVector[i][2] = false;
        }
        model.setDataVector(dataVector, header);
        TableColumn col;
//        SzamlaTableCellRender render = new SzamlaTableCellRender();
        int[] meret = {200, 200, 200};
        for (int i = 0;
                i < header.length; i++) {
            try {
                col = trTdTable.getColumnModel().getColumn(i);
//                col.setCellRenderer(render);
                col.setPreferredWidth(meret[i]);
            } catch (Exception ex) {
            }
        }

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
        jScrollPane1 = new javax.swing.JScrollPane();
        trTdTable = new javax.swing.JTable();
        addButton = new javax.swing.JButton();
        closeButton = new javax.swing.JButton();
        previewButton = new javax.swing.JButton();

        setName("Form"); // NOI18N

        jPanel1.setName("jPanel1"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        trTdTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "null"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        trTdTable.setName("trTdTable"); // NOI18N
        jScrollPane1.setViewportView(trTdTable);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(cezeszamlazo.App.class).getContext().getResourceMap(HtmlTableCreateFrame.class);
        if (trTdTable.getColumnModel().getColumnCount() > 0) {
            trTdTable.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("trTdTable.columnModel.title0")); // NOI18N
            trTdTable.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("trTdTable.columnModel.title1")); // NOI18N
        }

        addButton.setText(resourceMap.getString("addButton.text")); // NOI18N
        addButton.setName("addButton"); // NOI18N
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        closeButton.setText(resourceMap.getString("closeButton.text")); // NOI18N
        closeButton.setName("closeButton"); // NOI18N

        previewButton.setText(resourceMap.getString("previewButton.text")); // NOI18N
        previewButton.setName("previewButton"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(previewButton)
                        .addGap(113, 113, 113)
                        .addComponent(addButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(closeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(46, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addButton)
                    .addComponent(closeButton)
                    .addComponent(previewButton)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        List<String> th = new ArrayList<>();
        List<String> td = new ArrayList<>();
        for (int i = 0; i < trTdTable.getModel().getRowCount(); i++) {
            if ((Boolean) trTdTable.getModel().getValueAt(i, 2)) {
                String thString = (String) trTdTable.getModel().getValueAt(i, 1);
                String tdString = (String) trTdTable.getModel().getValueAt(i, 0);
                th.add(thString);
                td.add(tdString);
            }
        }
        HtmlTableCell htmlTableCell = HtmlTableCell.create();
        htmlTableCell.setTh(th);
        htmlTableCell.setTd(td);

        HtmlTableCreator htmlTableCreator = HtmlTableCreator.create();
        htmlTableCreator.setHtmlTableCell(htmlTableCell);
        htmlTableString = htmlTableCreator.generateTable();
        doClose(RET_OK);

    }//GEN-LAST:event_addButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JButton closeButton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton previewButton;
    private javax.swing.JTable trTdTable;
    // End of variables declaration//GEN-END:variables

    private void init() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;

        setLocation(x, y);
        setTitle("Tábla készítése");
        setVisible(true);
    }
}