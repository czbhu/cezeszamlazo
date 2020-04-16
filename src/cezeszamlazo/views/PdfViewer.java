package cezeszamlazo.views;

import javax.swing.JPanel;
import org.icepdf.ri.common.ComponentKeyBinding;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;

/**
 * @author Tomy
 */
public class PdfViewer extends javax.swing.JFrame
{
    public PdfViewer()
    {
        initComponents();
    }
    
    public void openPDF(String file)
    {
        setVisible(true);
        setExtendedState(MAXIMIZED_BOTH);
        
        try
        {
            SwingController control = new SwingController();
            SwingViewBuilder factory = new SwingViewBuilder(control);
            JPanel viewerPanel = factory.buildViewerPanel();
            ComponentKeyBinding.install(control, viewerPanel);
            control.getDocumentViewController().setAnnotationCallback(
                new org.icepdf.ri.common.MyAnnotationCallback(
                    control.getDocumentViewController()));
            control.openDocument(file);
            scrollPane_pdf.setViewportView(viewerPanel);
        }
        catch(Exception ex)
        {
        
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrollPane_pdf = new javax.swing.JScrollPane();

        setName("Form"); // NOI18N

        scrollPane_pdf.setName("scrollPane_pdf"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPane_pdf, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPane_pdf, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane scrollPane_pdf;
    // End of variables declaration//GEN-END:variables
}