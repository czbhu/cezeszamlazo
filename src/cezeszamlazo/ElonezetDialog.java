package cezeszamlazo;

import cezeszamlazo.controller.Functions;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.font.GlyphVector;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileOutputStream;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import cezeszamlazo.database.Query;
import cezeszamlazo.functions.ElonezetFunctions;
import invoice.Invoice;
import invoice.InvoiceProduct;
import invoice.MeasureOfUnit;
import invoice.ProductFee;
import java.util.ArrayList;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import org.apache.commons.io.IOUtils;
import szamlazo.pdf.DrawString;
import szamlazo.pdf.SzamlaLablec;

public class ElonezetDialog extends javax.swing.JDialog
{
    private ElonezetFunctions elonezetFunctions;
    private Functions functions;
    public static int ELONEZET = 0, NYOMTATAS = 1, PDF = 2, ATTACMENT = -1;
    private static int N = 25;

    public static final int RET_CANCEL = 0, RET_OK = 1;
    
    private static final int W = 595, H = 842;
    private double scale = 1.4;
    private Invoice invoice;
    private int peldany = 1, osszPeldany = 3, count = 0;//unused
    private int osszCsom = 0, osszRekl = 0;
    private double[] 
        oNetto = {0, 0, 0, 0, 0, 0, 0, 0},
        oAfaErtek = {0, 0, 0, 0, 0, 0, 0, 0},
        oBrutto = {0, 0, 0, 0, 0, 0, 0, 0};
    private boolean elonezet = true;
    
    private List<InvoiceProduct> invoiceProducts = new LinkedList<>();
    private List<Component> pages = new LinkedList<>();

    private Font 
        sansBoldItalic16 = new Font(Font.SANS_SERIF, Font.BOLD | Font.ITALIC, 16),
        sansBold11 = new Font(Font.SANS_SERIF, Font.BOLD, 11),
        sansPlain10 = new Font(Font.SANS_SERIF, Font.PLAIN, 10),
        sansBold10 = new Font(Font.SANS_SERIF, Font.BOLD, 10),
        sansBold12 = new Font(Font.SANS_SERIF, Font.BOLD, 12),
        sansBoldItalic12 = new Font(Font.SANS_SERIF, Font.BOLD | Font.ITALIC, 12),
        sansPlain9 = new Font(Font.SANS_SERIF, Font.PLAIN, 9),
        sansPlain8 = new Font(Font.SANS_SERIF, Font.PLAIN, 8),
        sansPlain12 = new Font(Font.SANS_SERIF, Font.PLAIN, 12);//unused
    private int mx = 0, my = 0;

    public ElonezetDialog(Invoice invoice, int previewType)
    {
        this(invoice, 1, previewType);
    }
    
    public ElonezetDialog(Invoice invoice, int osszPeldany, int tipus)
    {
        initComponents();
        
        this.elonezetFunctions = new ElonezetFunctions();
        this.functions = new Functions();
        this.invoice = invoice;
        this.osszPeldany = osszPeldany;
        
        elonezet = false;
        
        PrinterJob pj = PrinterJob.getPrinterJob();
        
        // oldalak számításának kezdete
        int osszPage;
        count = 0;

        invoiceProducts = new ArrayList<>();
        
        for(int i = 0; i < invoice.getProducts().Size(); i++)
        {
            InvoiceProduct product = invoice.getProducts().Get(i);
            invoiceProducts.add(product);

            for(ProductFee fee : product.getProductFees())
            {
                switch(fee.getType())
                {
                    case ADVERTISING:
                        osszRekl += fee.getTotalNet(invoice.isForeignCurrency());
                        break;
                    case PACKAGING:
                        osszCsom += fee.getTotalNet(invoice.isForeignCurrency());
                        break;
                }
                
                if (invoice.getTakeoverType().isEmpty())
                {
                    oNetto[3] += fee.getTotalNet(invoice.isForeignCurrency());
                    oAfaErtek[3] += fee.getTotalVat(invoice.isForeignCurrency());
                    oBrutto[3] += fee.getTotalGross(invoice.isForeignCurrency());
                    
                    MeasureOfUnit mou = new MeasureOfUnit("kg");
                    
                    InvoiceProduct prod = new InvoiceProduct(fee.getName(), "", fee.getWeight(), mou, 0, "", fee.getKgPrice(), "27%", 27);
                    invoiceProducts.add(prod);
                }
            }
            
            switch (product.getVatLabel())
            {
                case "AAM":
                    oNetto[0] += product.getNetPrice(invoice.isForeignCurrency());
                    oAfaErtek[0] += product.getVatAmount(invoice.isForeignCurrency());
                    oBrutto[0] += product.getGrossPrice(invoice.isForeignCurrency());
                    break;
                case "5%":
                    oNetto[1] += product.getNetPrice(invoice.isForeignCurrency());
                    oAfaErtek[1] += product.getVatAmount(invoice.isForeignCurrency());
                    oBrutto[1] += product.getGrossPrice(invoice.isForeignCurrency());
                    break;
                case "10%":
                    oNetto[2] += product.getNetPrice(invoice.isForeignCurrency());
                    oAfaErtek[2] += product.getVatAmount(invoice.isForeignCurrency());
                    oBrutto[2] += product.getGrossPrice(invoice.isForeignCurrency());
                    break;
                case "27%":
                    oNetto[3] += product.getNetPrice(invoice.isForeignCurrency());
                    oAfaErtek[3] += product.getVatAmount(invoice.isForeignCurrency());
                    oBrutto[3] += product.getGrossPrice(invoice.isForeignCurrency());
                    break;
                case "Az Áfa törvény hatályán kívüli":
                    oNetto[4] += product.getNetPrice(invoice.isForeignCurrency());
                    oAfaErtek[4] += product.getVatAmount(invoice.isForeignCurrency());
                    oBrutto[4] += product.getGrossPrice(invoice.isForeignCurrency());
                    break;
                case "Belföldi fordított adózás":
                    oNetto[5] += product.getNetPrice(invoice.isForeignCurrency());
                    oAfaErtek[5] += product.getVatAmount(invoice.isForeignCurrency());
                    oBrutto[5] += product.getGrossPrice(invoice.isForeignCurrency());
                    break;
                case "Áthárított adót tartalmazó különbözet szerinti adózás":
                    oNetto[6] += product.getNetPrice(invoice.isForeignCurrency());
                    oAfaErtek[6] += product.getVatAmount(invoice.isForeignCurrency());
                    oBrutto[6] += product.getGrossPrice(invoice.isForeignCurrency());
                    break;
                case "Áthárított adót nem tartalmazó különbözet szerinti adózás":
                    oNetto[7] += product.getNetPrice(invoice.isForeignCurrency());
                    oAfaErtek[7] += product.getVatAmount(invoice.isForeignCurrency());
                    oBrutto[7] += product.getGrossPrice(invoice.isForeignCurrency());
                    break;
            }
        }

        N = N - (invoice.getInvoiceType() == Invoice.InvoiceType.STORNO ? (1 + (invoice.isForeignCurrency() ? 1 : 0)) : 0);

        osszPage = (int) Math.ceil(invoiceProducts.size() / (N * 1.0));
        
        if ((osszPage * N) - invoiceProducts.size() < N / 2)
        {
            // ha az utolsó oldalon N/2-nél több termék van!
            osszPage += 1;
        }
        //System.out.println("OSSZPAGE: " + osszPage + "  N: " + N + " ElonezetDialog.java/ElonezetDialog(Invoice invoice, int osszPeldany, int tipus)");
        // oldalak számításának vége
        
        Book book = new Book();

        Dimension A4 = new Dimension(W, H);
        //unused
        Dimension d = new Dimension((int) Math.round(A4.width * scale + (elonezet ? 20 * scale : 0)), (int) Math.round(A4.height * scale + (elonezet ? 20 * scale : 0)));

        pages.removeAll(pages);
        
        for (int i = 0; i < osszPage; i++)
        {
            Component c = new Page(i + 1);
            c.setSize(W - (elonezet ? 0 : 60), H - (elonezet ? 0 : 50));
            pages.add(c);
            
            PagePrintable printable = new PagePrintable(c);
            PageFormat pageFormat = pj.defaultPage();
            Paper paper = new Paper();
            //A4 borderless (setting may need adjustment)
            paper.setSize(W, H);
            //will make part of page disappear
            //paper.setImageableArea(30, 30, W, H);
            paper.setImageableArea((elonezet ? 0 : 30), (elonezet ? 0 : 30), W - (elonezet ? 0 : 60), H - (elonezet ? 0 : 60));
            //works on our printers
            //paper.setImageableArea(5, 5, 565, 812);
            pageFormat.setPaper(paper);
            book.append(printable, pageFormat);
        }

        // Print the Book.
        pj.setPageable(book);

        if (tipus == NYOMTATAS)
        {
            scale = 1.0;

            for (peldany = 1; peldany <= osszPeldany; peldany++)
            {
                try
                {
                    PrinterGetSet printer = new PrinterGetSet();
                    String printerLabel = printer.getPrinterName();
                    pj.setPrintService(setPrinter(printerLabel));
                    System.out.println("printlabel:" + printerLabel);
                    pj.print();
                }
                catch (Exception PrintException)
                {
                    PrintException.printStackTrace();
                }
            }
        }
        else if (tipus == ELONEZET)
        {
            elonezetFrissites();

            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            Insets scnMax = Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration());
            int taskBarSize = scnMax.bottom;
            dim.height = dim.height - taskBarSize;

            this.setSize(dim);

            this.setTitle("Előnézet");
            this.setModal(true);
            this.setVisible(true);
        }
        else if (tipus == PDF)
        {
            elonezetFrissites();

            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            Insets scnMax = Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration());
            int taskBarSize = scnMax.bottom;
            dim.height = dim.height - taskBarSize;

            this.setSize(dim);

            this.invoice.setPrinted(true);
            this.setVisible(true);
            printToPDF();
            doClose(RET_OK);
        }
        else if (tipus == ATTACMENT)
        {
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            Insets scnMax = Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration());
            int taskBarSize = scnMax.bottom;
            dim.height = dim.height - taskBarSize;

            this.setSize(dim);
            createPDF();
            doClose(RET_OK);
        }
    }

    private PrintService setPrinter(String printerName)
    {
        PrintService[] printServices = PrinterJob.lookupPrintServices();
        DocPrintJob docprintjob = null;
        
        for (PrintService printer : printServices)
        {
            if (printer.getName().equalsIgnoreCase(printerName))
            {
                docprintjob = printer.createPrintJob();
            }
        }
        
        return docprintjob.getPrintService();
    }
    
    private void elonezetFrissites()
    {
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.removeAll();
        
        for (Component c : pages)
        {
            Elonezet e = new Elonezet(c);
            panel.add(e);
        }
        
        jScrollPane1.setViewportView(panel);
        jScrollPane1.validate();
    }

    public int getReturnStatus()
    {
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

        jSlider1 = new javax.swing.JSlider();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        panel = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();

        setName("Form"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jSlider1.setMajorTickSpacing(2);
        jSlider1.setMaximum(50);
        jSlider1.setMinimum(8);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(cezeszamlazo.App.class).getContext().getResourceMap(ElonezetDialog.class);
        jSlider1.setToolTipText(resourceMap.getString("jSlider1.toolTipText")); // NOI18N
        jSlider1.setValue(8);
        jSlider1.setName("jSlider1"); // NOI18N
        jSlider1.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                jSlider1MouseWheelMoved(evt);
            }
        });
        jSlider1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider1StateChanged(evt);
            }
        });

        jLabel1.setFont(resourceMap.getFont("jLabel1.font")); // NOI18N
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jLabel2.setFont(resourceMap.getFont("jLabel1.font")); // NOI18N
        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N
        jScrollPane1.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                jScrollPane1MouseWheelMoved(evt);
            }
        });

        panel.setName("panel"); // NOI18N
        jScrollPane1.setViewportView(panel);

        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
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
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 280, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addContainerGap())
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 640, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSlider1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        doClose(RET_CANCEL);
    }//GEN-LAST:event_closeDialog

    private void jSlider1MouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_jSlider1MouseWheelMoved
        int irany = evt.getWheelRotation();
        jSlider1.setValue(jSlider1.getValue() - irany);
    }//GEN-LAST:event_jSlider1MouseWheelMoved

    private void jSlider1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider1StateChanged
        scale = (double) jSlider1.getValue() / 10.0;
        elonezetFrissites();
    }//GEN-LAST:event_jSlider1StateChanged

    private void jScrollPane1MouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_jScrollPane1MouseWheelMoved
        if (evt.isShiftDown()) {
            jScrollPane1.getHorizontalScrollBar().setValue(jScrollPane1.getHorizontalScrollBar().getValue() + evt.getWheelRotation() * 10);
        } else {
            jScrollPane1.getVerticalScrollBar().setValue(jScrollPane1.getVerticalScrollBar().getValue() + evt.getWheelRotation() * 10);
        }
    }//GEN-LAST:event_jScrollPane1MouseWheelMoved

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        printToPDF();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void doClose(int retStatus)
    {
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JPanel panel;
    // End of variables declaration//GEN-END:variables
    private int returnStatus = RET_CANCEL;

    private int header(Graphics2D g2d, int page)
    {
        GlyphVector msg;
        g2d.setColor(Color.decode("#eeeeee"));
        g2d.fillRoundRect(0, 0, mx, 60, 5, 5);
        g2d.setColor(Color.BLACK);
        g2d.drawRoundRect(0, 0, mx, 60, 5, 5);
        g2d.setFont(sansBoldItalic16);
        String str = "";
        
        if (invoice.getInvoiceType() == Invoice.InvoiceType.STORNO)
        {
            str = "";
            
            if (invoice.isForeignCurrency())
            {
                str += " / Cancelled Invoice";
            }
            
            msg = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), "Számlával egy tekintet alá eső okirat" + str);
            g2d.drawGlyphVector(msg, 5, 15);
        }
        else
        {
            str = "";
            if (invoice.isForeignCurrency())
            {
                str += " / Invoice";
            }
            
            String title = "";
            
            switch (invoice.getType())
            {
                case Invoice.PROFORMA:
                    title = "Dijbekérő";
                    break;
                case Invoice.INVOICE:
                    switch(invoice.getInvoiceType())
                    {
                        case NEW:
                        case COPY:
                        case ORIGINAL:
                            title = "Számla";
                            break;
                        case CORRECTION:
                        case MODIFICATION:
                        case STORNO:
                            title = "Számlával egy tekintet alá eső okirat";
                    }
                    //title = "Számla";
                    break;
                case Invoice.COMPLETION_CERTIFICATE:
                    title = "Teljesítés igazolás";
                    break;
                default:
                    break;
            }
            
            msg = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), title + str);
            g2d.drawGlyphVector(msg, 5, 15);
        }
        
        FontMetrics fm = g2d.getFontMetrics(sansBoldItalic16);
        str = "";
        
        if (invoice.isForeignCurrency())
        {
            str = "/V";
        }
        
        g2d.drawString(invoice.getInvoiceNumber() + str, mx - fm.stringWidth(invoice.getInvoiceNumber() + str) - 5, 55);
        g2d.setFont(sansBold11);
        
        g2d.setFont(sansPlain9);
        fm = g2d.getFontMetrics(sansPlain9);
        
        if (page != 1)
        {
            str = "folytatás a(z) " + (page - 1) + ". oldalról";
            g2d.drawString(str, mx - 5 - fm.stringWidth(str), 15);
        }
        
        g2d.setFont(sansPlain10);
        
        if (invoice.isForeignCurrency())
        {
            str = " / Invoice No.";
        }
        else
        {
            str = "";
        }
        
        g2d.drawString("Sorszám" + str, 390 - (str.isEmpty() ? 0 : 70), 55);
        g2d.setFont(sansBold12);
        
        if (invoice.isForeignCurrency())
        {
            str = " / Sold by";
        }
        
        g2d.drawString("Szállító" + str, 5, 78);
        
        if (invoice.isForeignCurrency())
        {
            str = " / Client";
        }
        
        msg = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), "Vevő" + str);
        g2d.drawGlyphVector(msg, mx / 2 + 5, 78);
        g2d.setFont(sansBoldItalic12);
        fm = g2d.getFontMetrics();
        g2d.drawString(invoice.getSupplier().getName(), 5, 90);

        int p = 0;
        
        if (fm.stringWidth(invoice.getCustomer().getName()) > mx / 2 - 10)
        {
            String[] reszek = invoice.getCustomer().getName().split(" ");
            String temp = "";
            int LineWidth = mx / 2 - 10, SpaceLeft = LineWidth, SpaceWidth = fm.stringWidth(" "), j = 0, pluszSor = 0;
            
            for (String s : reszek)
            {
                if ((fm.stringWidth(s) + SpaceWidth) > SpaceLeft)
                {
                    msg = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), temp);
                    g2d.drawGlyphVector(msg, mx / 2 + 5, 90 + (j + pluszSor) * 10);
                    SpaceLeft = LineWidth - fm.stringWidth(s) - SpaceWidth;
                    temp = s + " ";
                    pluszSor++;
                    p += 10;
                }
                else
                {
                    SpaceLeft = SpaceLeft - (fm.stringWidth(s) + SpaceWidth);
                    temp += s + " ";
                }
            }
            
            msg = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), temp);
            g2d.drawGlyphVector(msg, mx / 2 + 5, 90 + (j + pluszSor) * 10);
            p += 10;
        }
        else
        {
            msg = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), invoice.getCustomer().getName());
            g2d.drawGlyphVector(msg, mx / 2 + 5, 90);
            p += 10;
        }

        g2d.setFont(sansPlain8);
        fm = g2d.getFontMetrics();
        
        int k = 1;
        g2d.drawString(invoice.getSupplier().getPostalCode() + " " + invoice.getSupplier().getCity(), 5, 90 + k++ * 10);
        g2d.drawString(invoice.getSupplier().getAddress(), 5, 90 + k++ * 10);
        str = ": ";
        
        if (invoice.isForeignCurrency())
        {
            str = " / Tax Number: ";
        }
        
        g2d.drawString("Adószám" + str + invoice.getSupplier().getTaxNumber(), 5, 90 + k++ * 10);
        str = ": ";

        if(!invoice.getSupplier().getEuTaxNumber().isEmpty())
        {
            if(invoice.isForeignCurrency())
            {
                str = " / Eu Tax Number: ";
            }
            
            g2d.drawString("EU-adószám" + str + invoice.getSupplier().getEuTaxNumber(), 5, 90 + k++ * 10);
            str = ": ";
        }
        
        if (invoice.isForeignCurrency())
        {
            str += " / Bank Account Number: ";
        }
        
        g2d.drawString("Bankszámlaszám" + str, 5, 90 + k++ * 10);
        g2d.drawString(invoice.getSupplier().getBankAccountNumber(), 5, 90 + k++ * 10);
        g2d.drawString(invoice.getSupplier().getComment(), 5, 90 + k++ * 10);
        
        msg = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), (!invoice.getCustomer().getAddress().getPostalCode().equalsIgnoreCase("0") && !invoice.getCustomer().getAddress().getPostalCode().isEmpty() ? invoice.getCustomer().getAddress().getPostalCode() + ", " : "") + invoice.getCustomer().getAddress().getCity());
        g2d.drawGlyphVector(msg, mx / 2 + 5, 90 + p);
        p += 10;

        if (fm.stringWidth(invoice.getCustomer().getAddressStr()) > mx / 2 - 10)
        {
            String[] reszek = invoice.getCustomer().getAddressStr().split(" ");
            String temp = "";
            int LineWidth = mx / 2 - 10,
                SpaceLeft = LineWidth,
                SpaceWidth = fm.stringWidth(" "),
                j = 0, pluszSor = 0;
            
            for (String s : reszek)
            {
                if ((fm.stringWidth(s) + SpaceWidth) > SpaceLeft)
                {
                    msg = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), temp);
                    g2d.drawGlyphVector(msg, mx / 2 + 5, 90 + p + (j + pluszSor) * 10);
                    SpaceLeft = LineWidth - fm.stringWidth(s) - SpaceWidth;
                    temp = s + " ";
                    pluszSor++;
                }
                else
                {
                    SpaceLeft = SpaceLeft - (fm.stringWidth(s) + SpaceWidth);
                    temp += s + " ";
                }
            }
            
            msg = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), temp);
            g2d.drawGlyphVector(msg, mx / 2 + 5, 90 + p + (j + pluszSor) * 10);
            p += 10 * (pluszSor + 1);
        }
        else
        {
            msg = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), invoice.getCustomer().getAddressStr());
            g2d.drawGlyphVector(msg, mx / 2 + 5, 90 + p);
            p += 10;
        }
        
        if (!invoice.getCustomer().getTaxNumber().isEmpty())
        {
            g2d.drawString("Adószám: " + invoice.getCustomer().getTaxNumber(), mx / 2 + 5, 90 + p);
            p += 10;
        }
        
        if (!invoice.getCustomer().getEuTaxNumber().isEmpty())
        {
            g2d.drawString("EU-adószám: " + invoice.getCustomer().getEuTaxNumber(), mx / 2 + 5, 90 + p);
            p += 10;
        }
        
        if (invoice.getCustomer().getShowInInvoice())
        {
            g2d.drawString("Bankszámlaszám: " + invoice.getCustomer().getBankAccountNumber(), mx / 2 + 5, 90 + p);
        }
        
        g2d.drawLine(0, 65, mx, 65);
        g2d.drawLine(0, 165, mx, 165);
        g2d.drawLine(mx / 2, 65, mx / 2, 165);
        g2d.drawLine(0, 65, 0, 165);
        g2d.drawLine(mx, 65, mx, 165);
        int cs = 0;
        
        if (invoice.isForeignCurrency())
        {
            cs = 10;
        }
        
        g2d.setColor(Color.decode("#eeeeee"));
        g2d.fillRoundRect(0, 170, mx, 15 + cs, 5, 5);
        g2d.setColor(Color.BLACK);
        g2d.drawRoundRect(0, 170, mx, 15 + cs, 5, 5);
        g2d.setFont(sansBold10);
        String[] fejlec1 = {"Fizetési mód", "Teljesítés időpontja", "Számla kelte", "Esedékesség"};
        String[] fejlec1d = {"Method of payment", "Date of fulfilment", "Date of invoice", "Due"};
        int i = 0;
        str = "";
        
        if (invoice.isForeignCurrency())
        {
            str = "/";
        }
        
        for (i = 0; i < 4; i++)
        {
            msg = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), fejlec1[i] + str);
            g2d.drawGlyphVector(msg, 5 + i * mx / 4, 182);
            
            if (invoice.isForeignCurrency())
            {
                g2d.drawString(fejlec1d[i], 5 + i * mx / 4, 192);
            }
        }
        
        str = "";
        
        switch (invoice.getCustomer().getPaymentMethod())
        {
            case 0:
                str = "Készpénz" + (invoice.isForeignCurrency() ? " / Cash" : "");
                break;
            case 1:
                str = "Átutalás" + (invoice.isForeignCurrency() ? " / Bank Transfer" : "");
                break;
            default:
                str = "Utánvét" + (invoice.isForeignCurrency() ? " / C.O.D." : "");
                break;
        }
        
        g2d.drawString(str, 5 + 0 * mx / 4, 200 + cs);
        g2d.setFont(sansPlain10);
        
        if (!invoice.isForeignCurrency())
        {
            g2d.drawString(functions.dateFormat(invoice.getCompletionDate()), 5 + 1 * mx / 4, 200 + cs);
            g2d.drawString(functions.dateFormat(invoice.getIssueDate()), 5 + 2 * mx / 4, 200 + cs);
            g2d.drawString(functions.dateFormat(invoice.getMaturityDate()), 5 + 3 * mx / 4, 200 + cs);
        }
        else
        {
            g2d.drawString(functions.dateFormatEn(invoice.getCompletionDate()), 5 + 1 * mx / 4, 200 + cs);
            g2d.drawString(functions.dateFormatEn(invoice.getIssueDate()), 5 + 2 * mx / 4, 200 + cs);
            g2d.drawString(functions.dateFormatEn(invoice.getMaturityDate()), 5 + 3 * mx / 4, 200 + cs);
        }

        if (invoice.getInvoiceType() == Invoice.InvoiceType.STORNO)
        {
            g2d.setFont(sansBold11);
            fm = g2d.getFontMetrics();
            String s = "Az okirat a következő számlához tartozik: " + invoice.getCorrectedInvoice()
                    + " (Teljesítés időpontja: "
                    + functions.dateFormat(invoice.getCompletionDate()) + ")";
            msg = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), s);
            g2d.drawGlyphVector(msg, mx / 2 - fm.stringWidth(s) / 2, 220 + cs);
            g2d.drawLine(0, 205 + cs, mx, 205 + cs);
            
            if (invoice.isForeignCurrency())
            {
                s = "Cancelled invoice of invoice " + invoice.getCorrectedInvoice()
                        + "/V (Date of fulfilment: "
                        + functions.dateFormatEn(invoice.getCompletionDate()) + ")";
                g2d.drawString(s, mx / 2 - fm.stringWidth(s) / 2, 235 + cs);
                cs += 15;
            }
            
            g2d.drawLine(0, 225 + cs, mx, 225 + cs);
            cs += 20;
        }
        return 0;
    }

    private void footer(Graphics2D g2d, int page)
    {
        GlyphVector msg;
        String lablec = SzamlaLablec.getLablecWithCegnev(invoice.getSupplier().getInvoiceFooter());
        System.out.println(invoice.getSupplier().getInvoiceFooter());
        DrawString drawString = new DrawString(invoice.getSupplier().getInvoiceFooter(), 80);
        
        if (page == pages.size())
        {
            g2d.setFont(sansPlain10);
            drawString.drawTheString(g2d, my);
        }
        g2d.setFont(sansPlain8);

        if (!invoice.isForeignCurrency())
        {
            g2d.drawLine(0, my - 35, mx, my - 35);
            g2d.drawString("Ez a számla a Ceze Kft. rendszerével készült. http://ceze.hu", 5, my - 28);
            g2d.drawString("A számla a többször módosított 24/1995 (XI.22) PM rendeletnek megfelel.", 5, my - 20);
            //msg = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), "A bizonylatot nyomtatta: " + App.user.getNev());
            //msg = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), "A bizonylatot nyomtatta: ");
            //g2d.drawGlyphVector(msg, 5, my - 15);
        }
        else
        {
            g2d.drawLine(0, my - 35, mx, my - 35);
            g2d.drawString("Ez a számla a Ceze Kft. rendszerével készült. http://ceze.hu / This invoice was prepared using the program of Ceze Kft. http://ceze.hu", 5, my - 28);
            g2d.drawString("A számla a többször módosított 24/1995 (XI.22) PM rendeletnek megfelel. /", 5, my - 20);
            g2d.drawString("The invoice is in compliance with the PM Decree 24/1995 (XI.22) amended with the PM Decree 34/1999 (XII.26).", 5, my - 15);
            //msg = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), "A bizonylatot nyomtatta: " + App.user.getNev());
            //g2d.drawGlyphVector(msg, 5, my - 15);
        }
        
        g2d.drawString(page + ". oldal", mx - 40, my - 15);

//        if (!invoice.getForeignCurrency())
//        {
//            if (page == pages.size())
//            {
//                g2d.setFont(sansPlain10);
//                //g2d.drawString("A 2014. évi XXII. tv 3.§. (3) bek. szerint kinyilatkozzuk, hogy reklámadó fizetési kötelezettség cégünket nem terheli, ", 5, my - 70);
//                //g2d.drawString("mivel e tevékenység vonatkozásában az adóalapunk a 0,5 milliárd forintot nem éri el.", 5, my - 60);
//                //g2d.drawString("A számla kiegyenlítéséig a szállított áru a Ceze Kft. tulajdonát képezi.", 5, my - 50);
//            }
//            
//            g2d.setFont(sansPlain8);
//            g2d.drawLine(0, my - 45, mx, my - 45);
//            g2d.drawString("Ez a számla a Ceze Kft. rendszerével készült. http://ceze.hu", 5, my - 35);
//            g2d.drawString("A számla a többször módosított 24/1995 (XI.22) PM rendeletnek megfelel.", 5, my - 25);
//            msg = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), "A bizonylatot nyomtatta: " + App.user.getNev());
//            g2d.drawGlyphVector(msg, 5, my - 15);
//        }
//        else
//        {
//            if (page == pages.size())
//            {
//                g2d.setFont(sansPlain10);
//                //g2d.drawString("A 2014. évi XXII. tv 3.§. (3) bek. szerint kinyilatkozzuk, hogy reklámadó fizetési kötelezettség cégünket nem terheli, ", 5, my - 70);
//                //g2d.drawString("mivel e tevékenység vonatkozásában az adóalapunk a 0,5 milliárd forintot nem éri el.", 5, my - 60);
//                //g2d.drawString("A számla kiegyenlítéséig a szállított áru a Ceze Kft. tulajdonát képezi.", 5, my - 50);
//            }
//            
//            g2d.setFont(sansPlain8);
//            g2d.drawLine(0, my - 55, mx, my - 55);
//            g2d.drawString("Ez a számla a Ceze Kft. rendszerével készült. http://ceze.hu / This invoice was prepared using the program of Ceze Kft. http://ceze.hu", 5, my - 45);
//            g2d.drawString("A számla a többször módosított 24/1995 (XI.22) PM rendeletnek megfelel. /", 5, my - 35);
//            g2d.drawString("The invoice is in compliance with the PM Decree 24/1995 (XI.22) amended with the PM Decree 34/1999 (XII.26).", 5, my - 25);
//            msg = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), "A bizonylatot nyomtatta: " + App.user.getNev());
//            g2d.drawGlyphVector(msg, 5, my - 15);
//        }

    }

    public void content(Graphics2D g2d, int page)
    {
        FontMetrics fm;
        String str;
        GlyphVector msg;
        //msg = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), "árvíztűrő tükörfúrógép - ÁRVÍZTŰRŐ TÜKÖRFÚRÓGÉP");
        //g2d.drawGlyphVector(msg, 0, 0);

        if (elonezet)
        {
            g2d.translate(10 * scale, 10 * scale);
        }

        g2d.setColor(Color.black);
        /* Now we perform our rendering */

        int sor = 10, j = 0, cs = (invoice.getInvoiceType() == Invoice.InvoiceType.STORNO ? 20 + (invoice.isForeignCurrency() ? 25 : 0) : 0), c = 0,
            n = (N * page < invoiceProducts.size() ? N * page : invoiceProducts.size());

        if (n <= invoiceProducts.size())
        {
            g2d.setColor(Color.decode("#eeeeee"));
            
            if (invoice.isForeignCurrency())
            {
                g2d.fillRoundRect(0, 210 + cs, mx, 25, 5, 5);
            }
            else
            {
                g2d.fillRoundRect(0, 210 + cs, mx, 15, 5, 5);
            }
            
            g2d.setColor(Color.BLACK);
            
            if (invoice.isForeignCurrency())
            {
                g2d.drawRoundRect(0, 210 + cs, mx, 25, 5, 5);
            }
            else
            {
                g2d.drawRoundRect(0, 210 + cs, mx, 15, 5, 5);
            }
            
            g2d.setFont(sansBold10);
            String[] fejlec2 = {"Megnevezés", "VTSZ/TESZOR", "Mennyiség Mee", "Egységár", "ÁFA", "ÁFA Érték", "Nettó", "Bruttó"};
            String[] fejlec2d = {"Description", "VTSZ/TESZOR Nr.", "Amount Unit", "Unit price", "Net", "VAT", "VAT price", "Gross"};
            fm = g2d.getFontMetrics();
            int[] foMeret = {90, 60, 85, 65, 40, 70, 55, 65};
            int[] ossz = new int[9];
            
            ossz[0] = 0;
            
            for (int i = 0; i < 8; i++)
            {
                if (i > 0)
                {
                    g2d.drawString(fejlec2[i] + (invoice.isForeignCurrency() ? "/" : ""), ossz[i] + foMeret[i] - fm.stringWidth(fejlec2[i] + (invoice.isForeignCurrency() ? "/" : "")), 220 + cs);
                    
                    if (invoice.isForeignCurrency())
                    {
                        g2d.drawString(fejlec2d[i], ossz[i] + foMeret[i] - fm.stringWidth(fejlec2d[i]), 230 + cs);
                    }
                }
                else
                {
                    g2d.drawString(fejlec2[i], 5 + ossz[i], 220 + cs);
                    
                    if (invoice.isForeignCurrency())
                    {
                        g2d.drawString(fejlec2d[i], 5 + ossz[i], 230 + cs);
                    }
                }
                
                ossz[i + 1] = ossz[i] + foMeret[i];
            }
            
            g2d.setFont(sansPlain8);
            
            if (invoice.isForeignCurrency())
            {
                cs += 10;
            }
            
            j = 0;
            sor = 10;
            fm = g2d.getFontMetrics();
            
            for (c = N * (page - 1); c < n; c++)
            {
                count = c;

                InvoiceProduct product = invoiceProducts.get(c);
                int pluszSor = 0;
                str = product.getName();
                
                for(ProductFee fee : product.getProductFees())
                {
                    str += " - " + EncodeDecode.numberFormat(String.valueOf(fee.getWeight()), true) + " kg";
                }
                
                msg = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), str);
                g2d.drawGlyphVector(msg, 5, 240 + j * sor + cs);
                str = "";
                j++;
                
                for(ProductFee fee : product.getProductFees())
                {
                    switch(fee.getType())
                    {
                        case ADVERTISING:
                            str = "KT: " + fee.getCode();
                            break;
                        case PACKAGING:
                            str = "CSK: " + fee.getCode();
                            break;
                    }
                    
                    g2d.drawString(str, 5, 240 + j * sor + cs);
                }
                
                //g2d.drawString(szt.getVtszTeszor(), ossz[2] - fm.stringWidth(szt.getVtszTeszor()), 240 + j * sor + cs);
                g2d.drawString(product.getVtszTeszor(), ossz[2] - fm.stringWidth(product.getVtszTeszor())-20, 240 + j * sor + cs);

                msg = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), elonezetFunctions.numberFormat(String.valueOf(product.getQuantity()), true) + " " + product.getMeasureOfUnit().getShortName());
                //g2d.drawGlyphVector(msg, ossz[3] - fm.stringWidth(elonezetFunctions.numberFormat(String.valueOf(szt.getMennyiseg()), true) + " " + szt.getMee()), 240 + j * sor + cs);
                g2d.drawGlyphVector(msg, ossz[3] - fm.stringWidth(elonezetFunctions.numberFormat(String.valueOf(product.getQuantity()), true) + " " + product.getMeasureOfUnit().getShortName())-18, 240 + j * sor + cs);
                
                if (invoice.isForeignCurrency() && invoice.getCentralParity() != 1.0)
                {
                    g2d.drawString(functions.numberFormat(String.valueOf(product.getUnitPrice())) + " " + invoice.getCurrency(), ossz[4] - fm.stringWidth(functions.numberFormat(String.valueOf(product.getUnitPrice())) + " " + invoice.getCurrency()), 240 + j * sor + cs);
                }
                else
                {
                    g2d.drawString(elonezetFunctions.numberFormat(String.valueOf(product.getUnitPrice()), true) + " " + invoice.getCurrency(), ossz[4] - fm.stringWidth(elonezetFunctions.numberFormat(String.valueOf(product.getUnitPrice()), true) + " " + invoice.getCurrency()), 240 + j * sor + cs);
                }
                
                g2d.drawString(product.getVatLabel(), ossz[5] - fm.stringWidth(String.valueOf((int) product.getVatPercent() - 35)), 230 + j * sor + cs);
                
                str = elonezetFunctions.numberFormat(String.valueOf(product.getVatAmount(invoice.isForeignCurrency())), invoice.isForeignCurrency() && invoice.getCentralParity() != 1.0) + " " + invoice.getCurrency();
                g2d.drawString(str, ossz[6] - fm.stringWidth(str), 240 + j * sor + cs);
                
                str = elonezetFunctions.numberFormat(String.valueOf(product.getNetPrice(invoice.isForeignCurrency())), invoice.isForeignCurrency() && invoice.getCentralParity() != 1.0) + " " + invoice.getCurrency();
                g2d.drawString(str, ossz[7] - fm.stringWidth(str), 240 + j * sor + cs);

                str = elonezetFunctions.numberFormat(String.valueOf(product.getGrossPrice(invoice.isForeignCurrency())), invoice.isForeignCurrency() && invoice.getCentralParity() != 1.0) + " " + invoice.getCurrency();
                g2d.drawString(str, mx - 5 - fm.stringWidth(str), 240 + j * sor + cs);
                j++;
                j += pluszSor;                
            }
        }
        
        if (n == invoiceProducts.size() && page == pages.size())
        {
            g2d.drawLine(0, 240 + j * sor - sor / 2 + cs, mx, 240 + j * sor - sor / 2 + cs);
            int m = 0;

            m = 250 + j * sor + cs;
            g2d.setColor(Color.decode("#eeeeee"));
            
            if (invoice.isForeignCurrency())
            {
                g2d.fillRoundRect(200, m, mx - 200, 25, 5, 5);
            }
            else
            {
                g2d.fillRoundRect(200, m, mx - 200, 15, 5, 5);
            }
            
            g2d.setColor(Color.BLACK);
            
            if (invoice.isForeignCurrency())
            {
                g2d.drawRoundRect(200, m, mx - 200, 25, 5, 5);
            }
            else
            {
                g2d.drawRoundRect(200, m, mx - 200, 15, 5, 5);
            }
            
            g2d.setFont(sansBold10);
            String[] fejlec3 = {"ÁFA", "Nettó ár", "ÁFA érték", "Bruttó ár"};
            String[] fejlec3d = {"VAT", "Net", "VAT price", "Gross"};
            int[] meret2 = new int[5];
            
            for (int i = 0; i < 5; i++)
            {
                meret2[i] = i * (mx - 150) / 4 + 150 - 5;
            }
            
            fm = g2d.getFontMetrics();
            
            for (int i = 0; i < 4; i++)
            {
                g2d.drawString(fejlec3[i] + (invoice.isForeignCurrency() ? "/" : ""), meret2[i + 1] - fm.stringWidth(fejlec3[i] + (invoice.isForeignCurrency() ? "/" : "")), m + 10);
                
                if (invoice.isForeignCurrency())
                {
                    g2d.drawString(fejlec3d[i], meret2[i + 1] - fm.stringWidth(fejlec3d[i]), m + 20);
                }
            }
            
            g2d.setFont(sansPlain10);
            
            if (invoice.isForeignCurrency())
            {
                m += 10;
            }
            
            fm = g2d.getFontMetrics();
            j = 3;
            
            for (int i = 0; i < oNetto.length; i++)
            {
                if (oNetto[i] != 0)
                {
                    String afa = "";
                    
                    //Ezt itt át kell nézni, hogy DB-ből vegye az értékeket!!!
                    switch(i)
                    {
                        case 0:
                            afa = "AAM";
                            break;
                        case 1:
                            afa = "5%";
                           break;
                        case 2:
                            afa = "10%";
                           break;
                        case 3:
                            afa = "27%";
                            break;
                        case 4:
                            afa = "Az Áfa törvény hatályán kívüli";
                            break;
                        case 5:
                            afa = "Belföldi fordított adózás";
                            break;
                        case 6:
                            afa = "Áthárított adót tartalmazó különbözet szerinti adózás";
                            break;
                        case 7:
                            afa = "Áthárított adót nem tartalmazó különbözet szerinti adózás";
                            break;
                    }
                    
                    double nn;
                    boolean isUtalas = ((invoice.getCustomer().getPaymentMethod() == 1));
                    
                    if (isUtalas)
                    {
                        nn = oNetto[i];
                    }
                    else
                    {
                        nn = (invoice.isForeignCurrency() && invoice.getCentralParity() != 1.0 ? oNetto[i] : Math.round(oNetto[i]));
                    }

                    double aa = (invoice.isForeignCurrency() && invoice.getCentralParity() != 1.0 ? oAfaErtek[i] : Math.round(oAfaErtek[i]));
                    double bb = (invoice.isForeignCurrency() && invoice.getCentralParity() != 1.0 ? oBrutto[i] : Math.round(oBrutto[i]));
                    
                    g2d.drawString(afa , meret2[1] - fm.stringWidth(afa + "%")+30, m + j * sor);
                    g2d.drawString(elonezetFunctions.numberFormat(String.valueOf(nn), invoice.isForeignCurrency() && invoice.getCentralParity() != 1.0) + " " + invoice.getCurrency(), meret2[2] - fm.stringWidth(elonezetFunctions.numberFormat(String.valueOf(nn), invoice.isForeignCurrency() && invoice.getCentralParity() != 1.0) + " " + invoice.getCurrency()), m + j * sor);
                    g2d.drawString(elonezetFunctions.numberFormat(String.valueOf(aa), invoice.isForeignCurrency() && invoice.getCentralParity() != 1.0) + " " + invoice.getCurrency(), meret2[3] - fm.stringWidth(elonezetFunctions.numberFormat(String.valueOf(aa), invoice.isForeignCurrency() && invoice.getCentralParity() != 1.0) + " " + invoice.getCurrency()), m + j * sor);
                    g2d.drawString(elonezetFunctions.numberFormat(String.valueOf(bb), invoice.isForeignCurrency() && invoice.getCentralParity() != 1.0) + " " + invoice.getCurrency(), meret2[4] - fm.stringWidth(elonezetFunctions.numberFormat(String.valueOf(bb), invoice.isForeignCurrency() && invoice.getCentralParity() != 1.0) + " " + invoice.getCurrency()), m + j * sor);
                    j++;
                }
            }
            
            g2d.drawLine(200, m + j * sor, mx, m + j * sor);
            j++;
            g2d.setFont(sansBold11);
            fm = g2d.getFontMetrics();
            g2d.drawString("Összesen", meret2[1] - fm.stringWidth("Összesen"), m + j * sor);
            g2d.drawString(elonezetFunctions.numberFormat(String.valueOf(invoice.isForeignCurrency() && invoice.getCentralParity() != 1.0 ? oBrutto[0] + oBrutto[1] + oBrutto[2] + oBrutto[3] + oBrutto[4] + oBrutto[5] + oBrutto[6] + oBrutto[7] : Math.round(oBrutto[0] + oBrutto[1] + oBrutto[2] + oBrutto[3] + oBrutto[4] + oBrutto[5] + oBrutto[6] + oBrutto[7])), invoice.isForeignCurrency() && invoice.getCentralParity() != 1.0) + " " + invoice.getCurrency(),
                meret2[4] - fm.stringWidth(elonezetFunctions.numberFormat(String.valueOf(invoice.isForeignCurrency() && invoice.getCentralParity() != 1.0 ? oBrutto[0] + oBrutto[1] + oBrutto[2] + oBrutto[3] + oBrutto[4] + oBrutto[5] + oBrutto[6] + oBrutto[7] : Math.round(oBrutto[0] + oBrutto[1] + oBrutto[2] + oBrutto[3] + oBrutto[4] + oBrutto[5] + oBrutto[6] + oBrutto[7])), invoice.isForeignCurrency() && invoice.getCentralParity() != 1.0) + " " + invoice.getCurrency()),
                m + j * sor);
            g2d.setFont(sansPlain10);
            fm = g2d.getFontMetrics();

            double nn;
            boolean isUtalas = ((invoice.getCustomer().getPaymentMethod() == 1));
            
            if (isUtalas)
            {
                nn = oNetto[0] + oNetto[1] + oNetto[2] + oNetto[3] + oNetto[4] + oNetto[5] + oNetto[6] + oNetto[7];
            }
            else
            {
                nn = (invoice.isForeignCurrency() && invoice.getCentralParity() != 1.0
                    ? oNetto[0] + oNetto[1] + oNetto[2] + oNetto[3] + oNetto[4] + oNetto[5] + oNetto[6] + oNetto[7]
                    : Math.round(oNetto[0]) + Math.round(oNetto[1]) + Math.round(oNetto[2]) + Math.round(oNetto[3]) + Math.round(oNetto[4]) + Math.round(oNetto[5]) + Math.round(oNetto[6]) + Math.round(oNetto[7]));
            }
            
            double aa = (invoice.isForeignCurrency() && invoice.getCentralParity() != 1.0
                    ? oAfaErtek[0] + oAfaErtek[1] + oAfaErtek[2] + oAfaErtek[3] + oAfaErtek[4] + oAfaErtek[5] + oAfaErtek[6] + oAfaErtek[7]
                    : Math.round(oAfaErtek[0]) + Math.round(oAfaErtek[1]) + Math.round(oAfaErtek[2]) + Math.round(oAfaErtek[3]) + Math.round(oAfaErtek[4]) + Math.round(oAfaErtek[5]) + Math.round(oAfaErtek[6]) + Math.round(oAfaErtek[7]));
            g2d.drawString(elonezetFunctions.numberFormat(String.valueOf(nn), invoice.isForeignCurrency() && invoice.getCentralParity() != 1.0) + " " + invoice.getCurrency(),
                    meret2[2] - fm.stringWidth(elonezetFunctions.numberFormat(String.valueOf(nn), invoice.isForeignCurrency() && invoice.getCentralParity() != 1.0) + " " + invoice.getCurrency()),
                    m + j * sor);
            g2d.drawString(elonezetFunctions.numberFormat(String.valueOf(aa), invoice.isForeignCurrency() && invoice.getCentralParity() != 1.0) + " " + invoice.getCurrency(),
                    meret2[3] - fm.stringWidth(elonezetFunctions.numberFormat(String.valueOf(aa), invoice.isForeignCurrency() && invoice.getCentralParity() != 1.0) + " " + invoice.getCurrency()),
                    m + j * sor);
            j += 2;

            //áfa érték forintban, deviza számla esetén
            g2d.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 8));
            
            if(invoice.isForeignCurrency())
            {
                g2d.drawString(elonezetFunctions.numberFormat(String.valueOf(aa * invoice.getCentralParity()), invoice.isForeignCurrency() && invoice.getCentralParity() != 1.0) + " HUF", 
                    meret2[3] - fm.stringWidth(elonezetFunctions.numberFormat(String.valueOf(aa), invoice.isForeignCurrency() && invoice.getCentralParity() != 1.0) + " " + invoice.getCurrency()),
                    (m + j * sor) - 13);
            }
            
            g2d.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));

            fm = g2d.getFontMetrics();
            
            if (invoice.getInvoiceType() == Invoice.InvoiceType.STORNO)
            {
                str = ": ";
                
                if (invoice.isForeignCurrency())
                {
                    str = " / Refund: ";
                }
                
                msg = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), "Visszatérítend\u0151 összeg" + str + elonezetFunctions.numberFormat(String.valueOf(String.valueOf(invoice.isForeignCurrency() && invoice.getCentralParity() != 1.0 ? Math.abs(oBrutto[0] + oBrutto[1] + oBrutto[2] + oBrutto[3]) : Math.abs(Math.round(oBrutto[0] + oBrutto[1] + oBrutto[2] + oBrutto[3])))), invoice.isForeignCurrency() && invoice.getCentralParity() != 1.0) + " " + invoice.getCurrency() + ",");
                g2d.drawGlyphVector(msg, mx - fm.stringWidth("Visszatérítend\u0151 összeg" + str + elonezetFunctions.numberFormat(String.valueOf(invoice.isForeignCurrency() && invoice.getCentralParity() != 1.0 ? Math.abs(oBrutto[0] + oBrutto[1] + oBrutto[2] + oBrutto[3]) : Math.abs(Math.round(oBrutto[0] + oBrutto[1] + oBrutto[2] + oBrutto[3]))), invoice.isForeignCurrency() && invoice.getCentralParity() != 1.0) + " " + invoice.getCurrency() + ","), m + j * sor);
            }
            else
            {
                str = ": ";
                
                if (invoice.isForeignCurrency())
                {
                    str = " / Total: ";
                }
                
                isUtalas = ((invoice.getPaymentMethod() == 1));
                
                System.err.println("oAfaErtek: " + (oAfaErtek[0] + oAfaErtek[1] + oAfaErtek[2] + oAfaErtek[3] + oAfaErtek[4] + oAfaErtek[5] + oAfaErtek[6] + oAfaErtek[7]));
                System.err.println("Invoice.getTotalVat(): " + invoice.getTotalVat());
                
                //System.err.println("oNetto: " + (oNetto[0] + oNetto[1] + oNetto[2] + oNetto[3] + oNetto[4] + oNetto[5] + oNetto[6] + oNetto[7]));
                //System.err.println("Invoice.getTotalNet(): " + invoice.getTotalNet());
                
                //System.err.println("oBrutto: " + (oBrutto[0] + oBrutto[1] + oBrutto[2] + oBrutto[3] + oBrutto[4] + oBrutto[5] + oBrutto[6] + oBrutto[7]));
                //System.err.println("Invoice.getTotal(): " + invoice.getTotal());
                
                int ossz = Functions.kerekit(invoice.getTotal(), isUtalas);
                //double ossz = Functions.kerekit(oBrutto[0] + oBrutto[1] + oBrutto[2] + oBrutto[3] + oBrutto[4] + oBrutto[5] + oBrutto[6] + oBrutto[7], isUtalas);
                msg = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), "Fizetend\u0151 összeg" + str + elonezetFunctions.numberFormat((invoice.isForeignCurrency() ? String.valueOf(invoice.getTotal()) : String.valueOf(ossz)), invoice.isForeignCurrency()) + " " + invoice.getCurrency() + ",");
                //msg = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), "Fizetend\u0151 összeg" + str + elonezetFunctions.numberFormat(String.valueOf(invoice.getForeignCurrency() && invoice.getCentralParity() != 1.0 ? ossz : Math.round(ossz)), invoice.getForeignCurrency() && invoice.getCentralParity() != 1.0) + " " + invoice.getCurrency() + ",");
                g2d.drawGlyphVector(msg, mx - fm.stringWidth("Fizetend\u0151 összeg" + str + elonezetFunctions.numberFormat(String.valueOf(ossz), invoice.isForeignCurrency()) + " " + invoice.getCurrency() + ","), m + j * sor);
            }
            
            g2d.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
            j += 2;

            Query query = new Query.QueryBuilder()
                .select("currencyName")
                .from("szamlazo_valuta")
                .where("currency = '" + (invoice.getCurrency().equalsIgnoreCase("Ft") ? "HUF" : invoice.getCurrency()) + "'")
                .build();
            Object[][] sl = App.db.select(query.getQuery());
            
            str = String.valueOf(sl[0][0]);
            
            /*if (invoice.getForeignCurrency() && invoice.getCentralParity() != 1.0)
            {*/
                //msg = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), "azaz " + elonezetFunctions.betuvel((invoice.getForeignCurrency() ? invoice.getRefund() : Functions.kerekit(invoice.getRefund(), isUtalas))) + " " + str + ".");
                msg = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), "azaz " + functions.betuvel((invoice.isForeignCurrency() ? invoice.getRefund() : Functions.kerekit(invoice.getRefund(), isUtalas))) + " " + str + ".");
                g2d.drawGlyphVector(msg, 5, m + j * sor);
            /*}
            else
            {
                msg = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), "azaz " + elonezetFunctions.betuvel(Math.round(Math.abs(oBrutto[0] + oBrutto[1] + oBrutto[2] + oBrutto[3] + oBrutto[4] + oBrutto[5] + oBrutto[6] + oBrutto[7]))) + " " + str + ".");
                g2d.drawGlyphVector(msg, 5, m + j * sor);
            }*/
            
            if (invoice.isForeignCurrency())
            {
                j++;
                g2d.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
                g2d.drawString("(Árfolyam / Exchange rate: " + functions.numberFormat(String.valueOf(invoice.getCentralParity())) + " Ft/" + invoice.getCurrency() + ")", 5, m + j * sor + 5);
            }
            
            j += 2;
            g2d.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
            
            if(invoice.getTakeoverType().isEmpty())
            {
                if (osszCsom != 0.0)
                {
                    g2d.drawString("A csomagolószer termékdíj összege a bruttó árból: "
                            + elonezetFunctions.numberFormat(String.valueOf(osszCsom), false) + " Ft", 5, m + j * sor + 5);
                    sor++;
                }

                if (osszRekl != 0.0)
                {
                    g2d.drawString("A reklámpapír termékdíj összege a bruttó árból: "
                            + elonezetFunctions.numberFormat(String.valueOf(osszRekl), false) + " Ft", 5, m + j * sor + 5);
                    sor++;
                }
            }
            
            sor++;
            g2d.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
            
            if (!invoice.getComment().isEmpty())
            {
                g2d.setFont(sansPlain10);
                invoice.setComment(invoice.getComment().replace("\t", "  "));
                g2d.drawString("Megjegyzés:", 5, m + j * sor);
                j += 2;
                String[] temp = invoice.getComment().split("\\n");
                g2d.setFont(sansPlain8);
                fm = g2d.getFontMetrics();
                
                for (String s : temp)
                {
                    if (fm.stringWidth(s) < mx - 10)
                    {
                        msg = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), s);
                        g2d.drawGlyphVector(msg, 5, m + j * sor);
                    }
                    else
                    {
                        String[] reszek = s.split(" ");
                        int LineWidth = mx - 10;
                        int SpaceLeft = LineWidth;
                        int SpaceWidth = fm.stringWidth(" ");
                        String r = "";
                        
                        for (String s2 : reszek)
                        {
                            if ((fm.stringWidth(s2) + SpaceWidth) > SpaceLeft)
                            {
                                msg = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), r);
                                g2d.drawGlyphVector(msg, 5, m + j * sor);
                                SpaceLeft = LineWidth - fm.stringWidth(s2) - SpaceWidth;
                                r = s2 + " ";
                                j++;
                            }
                            else
                            {
                                SpaceLeft = SpaceLeft - (fm.stringWidth(s2) + SpaceWidth);
                                r += s2 + " ";
                            }
                        }
                        
                        msg = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), r);
                        g2d.drawGlyphVector(msg, 5, m + j * sor);
                    }
                    
                    j++;
                }
            }
            
            j += 2;
            
            if (!invoice.getFooter().isEmpty())
            {
                g2d.setFont(sansPlain10);
                String[] temp = invoice.getFooter().split("\\n");
                g2d.setFont(sansPlain8);
                fm = g2d.getFontMetrics();
                for (String s : temp)
                {
                    if (fm.stringWidth(s) < mx - 10)
                    {
                        msg = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), s);
                        g2d.drawGlyphVector(msg, 5, m + j * sor);
                    }
                    else
                    {
                        String[] reszek = s.split(" ");
                        int LineWidth = mx - 10;
                        int SpaceLeft = LineWidth;
                        int SpaceWidth = fm.stringWidth(" ");
                        String r = "";
                        
                        for (String s2 : reszek)
                        {
                            if ((fm.stringWidth(s2) + SpaceWidth) > SpaceLeft)
                            {
                                msg = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), r);
                                g2d.drawGlyphVector(msg, 5, m + j * sor);
                                SpaceLeft = LineWidth - fm.stringWidth(s2) - SpaceWidth;
                                r = s2 + " ";
                                j++;
                            }
                            else
                            {
                                SpaceLeft = SpaceLeft - (fm.stringWidth(s2) + SpaceWidth);
                                r += s2 + " ";
                            }
                        }
                        
                        msg = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), r);
                        g2d.drawGlyphVector(msg, 5, m + j * sor);
                    }
                    j++;
                }
            }
        }
    }
    
    public String createPDF()
    {
        File kintlevosegDir = new File("dokumentumok/csatolmanyok/kintlevoseg");

        if (!kintlevosegDir.exists()) {
            try {
                kintlevosegDir.mkdirs();
            } catch (SecurityException se) {
                se.printStackTrace();
            }
        }
        File file = new File(kintlevosegDir + "/szamla_" + invoice.getInvoiceNumber().replace(" ", "_").replace("/", "_") + ".pdf");
        try
        {
            if (file != null)
            {
                scale = 1.0;
                Document document = new Document(PageSize.A4, 20, 20, 20, 20);
                try {
                    PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file.getAbsoluteFile()));
                    document.open();
                    PageFormat pf = new PageFormat();
                    Paper paper = new Paper();
                    paper.setSize(mx, my);
                    paper.setImageableArea(0, 0, mx, my);
                    pf.setPaper(paper);

                    float pageImageableWidth = (float) pf.getImageableWidth(),
                            pageImageableHeight = (float) pf.getImageableHeight();

//                    InputStream inputStream = this.getClass().getResourceAsStream("/cezeszamlazo/resources/fonts/arial.ttf");
//                    byte[] rBytes = RandomAccessFileOrArray.InputStreamToArray(inputStream);
                    byte[] rBytes = IOUtils.toByteArray(Thread.currentThread().getContextClassLoader()
                            .getResourceAsStream("cezeszamlazo/resources/fonts/arial.ttf"));

                    BaseFont baseFont = BaseFont.createFont("arial.ttf", BaseFont.IDENTITY_H, true, false, rBytes, null);

//                    com.itextpdf.text.Font font2(baseFont)
//                            //                            = FontFactory.getFont("c:\\windows\\fonts\\arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 10);
                    PdfContentByte contentByte = writer.getDirectContent();
                    boolean first = true;
                    for (Component p : pages) {
                        PdfTemplate template = contentByte.createTemplate(pageImageableWidth, pageImageableHeight);
                        Graphics2D g2 = template.createGraphics(pageImageableWidth, pageImageableHeight);
                        p.print(g2);
                        g2.dispose();
                        if (first) {
                            first = false;
                        } else {
                            document.newPage();
                        }
                        template.setFontAndSize(baseFont, 12);
                        contentByte.addTemplate(template, 30, 20);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (document.isOpen()) {
                        document.close();
                    }
                }
            }
        } catch (Exception ex) {
            HibaDialog h = new HibaDialog("Sikertelen mentés!", "Ok", "");
        }
        return file.getAbsolutePath();
    }

    public void printToPDF()
    {
        File file = new File("szamla_" + invoice.getInvoiceNumber().replace(" ", "_").replace("/", "_") + ".pdf");
        
        try
        {
            JFileChooser chooser = new JFileChooser();
            chooser.setSelectedFile(file);
            chooser.showOpenDialog(null);
            File curFile = chooser.getSelectedFile();
            
            if (curFile != null)
            {
                scale = 1.0;
                Document document = new Document(PageSize.A4, 20, 20, 20, 20);
                
                try
                {
                    PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(curFile.getAbsoluteFile()));
                    document.open();
                    PageFormat pf = new PageFormat();
                    Paper paper = new Paper();
                    paper.setSize(mx, my);
                    paper.setImageableArea(0, 0, mx, my);
                    pf.setPaper(paper);

                    float pageImageableWidth = (float) pf.getImageableWidth(),
                            pageImageableHeight = (float) pf.getImageableHeight();

//                    InputStream inputStream = this.getClass().getResourceAsStream("/cezeszamlazo/resources/fonts/arial.ttf");
//                    byte[] rBytes = RandomAccessFileOrArray.InputStreamToArray(inputStream);
                    byte[] rBytes = IOUtils.toByteArray(Thread.currentThread().getContextClassLoader()
                            .getResourceAsStream("cezeszamlazo/resources/fonts/arial.ttf"));

                    BaseFont baseFont = BaseFont.createFont("arial.ttf", BaseFont.IDENTITY_H, true, false, rBytes, null);

//                    com.itextpdf.text.Font font2(baseFont)
//                            //                            = FontFactory.getFont("c:\\windows\\fonts\\arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 10);
                    PdfContentByte contentByte = writer.getDirectContent();
                    boolean first = true;
                    
                    for (Component p : pages)
                    {
                        PdfTemplate template = contentByte.createTemplate(pageImageableWidth, pageImageableHeight);
                        Graphics2D g2 = template.createGraphics(pageImageableWidth, pageImageableHeight);
                        p.print(g2);
                        g2.dispose();
                        
                        if (first)
                        {
                            first = false;
                        }
                        else
                        {
                            document.newPage();
                        }
                        
                        template.setFontAndSize(baseFont, 12);
                        contentByte.addTemplate(template, 30, 20);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    if (document.isOpen())
                    {
                        document.close();
                    }
                }
            }
        }
        catch (Exception ex)
        {
            HibaDialog h = new HibaDialog("Sikertelen mentés!", "Ok", "");
        }
    }

    class Elonezet extends JPanel
    {
        public final Dimension A4 = new Dimension(W, H);
        private Component c;

        public Elonezet(Component c)
        {
            this.c = c;
            //Dimension size = new Dimension((int) Math.round(A4.width * scale), (int) Math.round(A4.height * scale));
            Dimension size = new Dimension((int) Math.round(A4.width * scale + (elonezet ? 20 * scale : 0)), (int) Math.round(A4.height * scale + (elonezet ? 20 * scale : 0)));

            setPreferredSize(size);
            setMaximumSize(size);
            setMinimumSize(size);
            setSize(size);
            setLayout(null);
        }

        @Override
        public void paintComponent(Graphics g)
        {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setBackground(Color.WHITE);
            g2d.clearRect(0, 0, getWidth(), getHeight());

            /*
             g2d.drawLine(0, 0, mx, 0);
             g2d.drawLine(mx, 0, mx, my);
             g2d.drawLine(mx, my, 0, my);
             g2d.drawLine(0, my, 0, 0);
             g2d.drawLine(0, 0, mx, my);
             g2d.drawLine(mx, 0, 0, my);
             */
            if (invoice != null)
            {
                c.paint(g2d);
            }
            else
            {
                // Hiba!!
                //paint(g2d, pf, scale);
            }
        }
    }

    class Page extends JComponent implements Printable
    {
        private int page = 0;

        public Page(int page)
        {
            this.page = page;
        }

        @Override
        public void paintComponent(Graphics g)
        {
            Graphics2D g2d = (Graphics2D) g;
            mx = getWidth();
            my = getHeight();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.scale(scale, scale);
            content(g2d, page);
            footer(g2d, page);
            header(g2d, page);
        }

        @Override
        public int print(Graphics g, PageFormat pageFormat, int pageIndex)
        {
            if (pageIndex != 0)
            {
                return NO_SUCH_PAGE;
            }
            
            paintComponent(g);
            return PAGE_EXISTS;
        }
    }

    class PagePrintable implements Printable
    {
        private Component mComponent;

        public PagePrintable(Component c)
        {
            mComponent = c;
        }

        @Override
        public int print(Graphics g, PageFormat pageFormat, int pageIndex)
        {
            Graphics2D g2 = (Graphics2D) g;
            g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
            mComponent.paint(g2);
            return PAGE_EXISTS;
        }
    }
}