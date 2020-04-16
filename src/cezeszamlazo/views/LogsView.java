package cezeszamlazo.views;

import cezeszamlazo.model.LogModel;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 * @author Tomy
 */
public class LogsView extends javax.swing.JDialog
{
    public LogsView()
    {
        initComponents();
        
        Init();
    }
    
    public void Open()
    {
        UpdateLogTable();
        setVisible(true);
    }
    
    private void Init()
    {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;
        
        setLocation(x, y);
    }
    
    private void UpdateLogTable()
    {
        DefaultTableModel tableModel = (DefaultTableModel)table_Logs.getModel();
        
        Object [][] values = LogModel.getLogs();
        Object [] header = {"Id", "Dátum", "Fájlnév", "Leírás"};
        Integer [] width = {10, 80, 150, 600};
        tableModel.setDataVector(values, header);
        
        TableColumn column;
        
        for(int i = 0; i < header.length; i++)
        {
            column = table_Logs.getColumnModel().getColumn(i);
            column.setMinWidth(width[i]);
            column.setPreferredWidth(width[i]);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrollPane_Logs = new javax.swing.JScrollPane();
        table_Logs = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setName("Form"); // NOI18N

        scrollPane_Logs.setName("scrollPane_Logs"); // NOI18N

        table_Logs.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Dátum", "Fájl", "Leírás"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table_Logs.setName("table_Logs"); // NOI18N
        table_Logs.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                table_LogsMouseClicked(evt);
            }
        });
        scrollPane_Logs.setViewportView(table_Logs);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollPane_Logs, javax.swing.GroupLayout.DEFAULT_SIZE, 980, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollPane_Logs, javax.swing.GroupLayout.DEFAULT_SIZE, 532, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void table_LogsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_LogsMouseClicked
        int row = table_Logs.getSelectedRow();
        
        if(evt.getButton() == MouseEvent.BUTTON1 && evt.getClickCount() == 2)
        {
            LogView view = new LogView(table_Logs.getValueAt(row, 3).toString());
        }
    }//GEN-LAST:event_table_LogsMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane scrollPane_Logs;
    private javax.swing.JTable table_Logs;
    // End of variables declaration//GEN-END:variables
}