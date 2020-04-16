package cezeszamlazo;

import java.awt.Dimension;
import java.awt.Toolkit;
import szamlazo.pdf.SzamlaLablec;

/**
 *
 * @author szekus
 */
public class SzamlaLablecDialog extends javax.swing.JDialog
{
    public SzamlaLablecDialog()
    {
        initComponents();
        init();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        lablecHunTextArea = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setName("Form"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        lablecHunTextArea.setColumns(20);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(cezeszamlazo.App.class).getContext().getResourceMap(SzamlaLablecDialog.class);
        lablecHunTextArea.setFont(resourceMap.getFont("lablecHunTextArea.font")); // NOI18N
        lablecHunTextArea.setRows(5);
        lablecHunTextArea.setName("lablecHunTextArea"); // NOI18N
        lablecHunTextArea.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                lablecHunTextAreaKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(lablecHunTextArea);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lablecHunTextAreaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lablecHunTextAreaKeyReleased
        SzamlaLablec.save(lablecHunTextArea.getText());
    }//GEN-LAST:event_lablecHunTextAreaKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea lablecHunTextArea;
    // End of variables declaration//GEN-END:variables

    private void init()
    {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int locX = (screenSize.width - getWidth()) / 2;
        int locY = (screenSize.height - getHeight()) / 2;

        java.net.URL url = ClassLoader.getSystemResource("cezeszamlazo/resources/icon.png");
        java.awt.Image img = toolkit.createImage(url);
        setIconImage(img);

        lablecHunTextArea.setText(SzamlaLablec.getLablec());
        
        setLocation(locX, locY);
        setTitle("Számla Lábléc");

        setModal(true);
        setVisible(true);
    }
}