/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeszamlazo.kintlevoseg;

import cezeszamlazo.HibaDialog;
import cezeszamlazo.controller.Functions;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DropMode;
import javax.swing.TransferHandler;
import javax.swing.table.TableColumn;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author szekus
 */
public class HtmlTableCreateDialog extends javax.swing.JDialog {

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
//        dispose();
    }

    private int returnStatus = RET_CANCEL;

    public HtmlTableCreateDialog() {
        initComponents();

        KintlevosegLevelAttributum kintlevosegLevelAttributum = new KintlevosegLevelAttributum();
        List<String> allName = kintlevosegLevelAttributum.getAllName();
        DefaultTableModel model = (DefaultTableModel) trTdTable.getModel();
        String[] header = new String[]{"érték", "oszlop neve", "kiválaszt", "sorszám"};

        Object[][] dataVector = new Object[allName.size()][header.length];
        for (int i = 0; i < allName.size(); i++) {
            Object cell = allName.get(i);
            dataVector[i][0] = cell;
            dataVector[i][1] = cell;
            dataVector[i][2] = false;
            dataVector[i][3] = 0;
        }
        model.setDataVector(dataVector, header);
        TableColumn col;
//        SzamlaTableCellRender render = new SzamlaTableCellRender();
        int[] meret = {200, 200, 200, 200};
        for (int i = 0;
                i < header.length; i++) {
            try {
                col = trTdTable.getColumnModel().getColumn(i);
//                col.setCellRenderer(render);
                col.setPreferredWidth(meret[i]);
            } catch (Exception ex) {
            }
        }
        trTdTable.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {

                    int row = trTdTable.getSelectedRow();
                    int column = trTdTable.getSelectedColumn();

                    // resul is the new value to insert in the DB
                    String resul = trTdTable.getValueAt(row, column).toString();
                    // id is the primary key of my DB
                    String id = trTdTable.getValueAt(row, 0).toString();

                    // update is my method to update. Update needs the id for
                    // the where clausule. resul is the value that will receive
                    // the cell and you need column to tell what to update.
                }
            }
        });
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

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setName("Form"); // NOI18N

        jPanel1.setName("jPanel1"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        trTdTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "null", "null"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, true
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
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(cezeszamlazo.App.class).getContext().getResourceMap(HtmlTableCreateDialog.class);
        if (trTdTable.getColumnModel().getColumnCount() > 0) {
            trTdTable.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("trTdTable.columnModel.title0")); // NOI18N
            trTdTable.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("trTdTable.columnModel.title1")); // NOI18N
            trTdTable.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("trTdTable.columnModel.title2")); // NOI18N
            trTdTable.getColumnModel().getColumn(3).setHeaderValue(resourceMap.getString("trTdTable.columnModel.title3")); // NOI18N
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
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });

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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

    private boolean isDifferentOrder() throws KintlevosegLevelException {
        List<Integer> listNumber = new ArrayList<>();
        for (int i = 0; i < trTdTable.getModel().getRowCount(); i++) {
            if ((Boolean) trTdTable.getModel().getValueAt(i, 2)) {
                System.out.println("sor: " + trTdTable.getModel().getValueAt(i, 1));
                int number = Functions.getIntFromObject(trTdTable.getModel().getValueAt(i, 3));
                System.out.println("number: " + number);
                if (number == 0) {
                    throw new KintlevosegLevelException("Kiválasztott sornál a sorszám nem lehet 0!");
                } else {
                    listNumber.add(number);
                }

            }
        }
        Set<Integer> set = new HashSet<>(listNumber);
        if (set.size() < listNumber.size()) {
            throw new KintlevosegLevelException("Nem lehet azonos sorszám!");
        }

        return true;
    }
    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        try {
            if (this.isDifferentOrder()) {
                List<String> th = new ArrayList<>();
                List<String> td = new ArrayList<>();
                int selectedRows = 0;
                for (int i = 0; i < trTdTable.getModel().getRowCount(); i++) {
                    if ((Boolean) trTdTable.getModel().getValueAt(i, 2)) {
                        selectedRows++;
                    }
                }
                for (int i = 0; i < selectedRows; i++) {
                    th.add("");
                    td.add("");
                }

                for (int i = 0; i < trTdTable.getModel().getRowCount(); i++) {
                    if ((Boolean) trTdTable.getModel().getValueAt(i, 2)) {
                        String thString = (String) trTdTable.getModel().getValueAt(i, 1);
                        String tdString = (String) trTdTable.getModel().getValueAt(i, 0);
                        int number = Functions.getIntFromObject(trTdTable.getModel().getValueAt(i, 3));
                        th.set(number - 1, thString);
                        td.set(number - 1, tdString);
                    }
                }
                HtmlTableCell htmlTableCell = HtmlTableCell.create();
                htmlTableCell.setTh(th);
                htmlTableCell.setTd(td);

                HtmlTableCreator htmlTableCreator = HtmlTableCreator.create();
                htmlTableCreator.setHtmlTableCell(htmlTableCell);
                htmlTableString = htmlTableCreator.generateTable();
                doClose(RET_OK);
            }
        } catch (KintlevosegLevelException ex) {
            HibaDialog dialog = new HibaDialog(ex.getMsg(), "OK", "");
        }

    }//GEN-LAST:event_addButtonActionPerformed

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        doClose(RET_CANCEL);
    }//GEN-LAST:event_closeButtonActionPerformed

    private void init() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;

        setLocation(x, y);
        setTitle("Tábla készítése");
        setModal(true);
//        setVisible(true);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JButton closeButton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton previewButton;
    private javax.swing.JTable trTdTable;
    // End of variables declaration//GEN-END:variables
}
