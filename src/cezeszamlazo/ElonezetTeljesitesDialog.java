package cezeszamlazo;

import cezeszamlazo.controller.Functions;
import com.itextpdf.text.Document;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import cezeszamlazo.controller.Szamla;
import cezeszamlazo.controller.SzamlaTermek;
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
import cezeszamlazo.controller.TermekDij;
import cezeszamlazo.database.Query;
import cezeszamlazo.functions.ElonezetFunctions;
import org.apache.commons.io.IOUtils;
import szamlazo.pdf.DrawString;
import szamlazo.pdf.SzamlaLablec;

public class ElonezetTeljesitesDialog extends javax.swing.JDialog {

    private ElonezetFunctions elonezetFunctions;

    public static int ELONEZET = 0, NYOMTATAS = 1, PDF = 2;
    private static int N = 25;
    /**
     * A return status code - returned if Cancel button has been pressed
     */
    public static final int RET_CANCEL = 0;
    /**
     * A return status code - returned if OK button has been pressed
     */
    public static final int RET_OK = 1;
    private static final int W = 595, H = 842;
    private double scale = 1.4;
    private Szamla szla;
    private int peldany = 1, osszPeldany = 3, count = 0;
    private double osszCsom = 0.0, osszRekl = 0.0;
    private double[] oNetto = {0, 0, 0, 0},
            oAfaErtek = {0, 0, 0, 0},
            oBrutto = {0, 0, 0, 0};
    private boolean elonezet = true;
    private List<SzamlaTermek> szamlaTermekek = new LinkedList<SzamlaTermek>();
    private List<Component> pages = new LinkedList<Component>();
    // print
    private Font sansBoldItalic16 = new Font(Font.SANS_SERIF, Font.BOLD | Font.ITALIC, 16),
            sansBold11 = new Font(Font.SANS_SERIF, Font.BOLD, 11),
            sansPlain10 = new Font(Font.SANS_SERIF, Font.PLAIN, 10),
            sansBold10 = new Font(Font.SANS_SERIF, Font.BOLD, 10),
            sansBold12 = new Font(Font.SANS_SERIF, Font.BOLD, 12),
            sansBoldItalic12 = new Font(Font.SANS_SERIF, Font.BOLD | Font.ITALIC, 12),
            sansPlain9 = new Font(Font.SANS_SERIF, Font.PLAIN, 9),
            sansPlain8 = new Font(Font.SANS_SERIF, Font.PLAIN, 8),
            sansPlain12 = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
    private int mx = 0, my = 0;

    public ElonezetTeljesitesDialog(String azon, int osszPeldany, int tipus) {
        this(new Szamla(azon), osszPeldany, tipus);
    }

    public ElonezetTeljesitesDialog(Szamla szla, int osszPeldany, int tipus) {
        initComponents();

        this.elonezetFunctions = new ElonezetFunctions();

        this.szla = szla;

        this.osszPeldany = osszPeldany;
        //this.elonezet = !nyomtat;

//        System.out.println("---------");
//        System.out.println("ÁTVÁLLAL: " + szla.isAtvallal());
//        System.out.println("---------");
        elonezet = false;

        PrinterJob printerJob = PrinterJob.getPrinterJob();
        // oldalak számítás kezdés

        int osszPage = 0;
        count = 0;

        for (SzamlaTermek szt : szla.getTermekek()) {
            //szt.szorozMennyiseg(szla.getTipus() == 2 ? -1 : 1);
            szamlaTermekek.add(szt);
            if (szt.getTermekDij() != null && !szla.isAtvallal()) {
                TermekDij td = szt.getTermekDij();
                if (td.getTermekDij() == 20) {
                    osszCsom += td.getOsszTermekDijNetto(szla.isDeviza());
                } else {
                    osszRekl += td.getOsszTermekDijNetto(szla.isDeviza());
                }
                if (!szla.isAtvallal()) {
                    oNetto[3] += td.getOsszTermekDijNetto(szla.isDeviza());
                    oAfaErtek[3] += td.getOsszTermekDijAfaErtek(szla.isDeviza());
                    oBrutto[3] += td.getOsszTermekDijBrutto(szla.isDeviza());
                    szamlaTermekek.add(new SzamlaTermek(
                            "Környezetvédelmi termékdíj (" + (td.getTermekDij() == 20 ? "csomagolószer" : "reklámpapír") + ")",
                            "",
                            "kg",
                            "27",
                            "",
                            String.valueOf(td.getTermekDij()),
                            String.valueOf(td.getSuly()),
                            ""));
                }
            }
            switch ((int) szt.getAfa()) {
                case 0:
                    oNetto[0] += szt.getNetto(szla.isDeviza());
                    oAfaErtek[0] += szt.getAfaErtek(szla.isDeviza());
                    oBrutto[0] += szt.getNetto(szla.isDeviza()) + szt.getAfaErtek(szla.isDeviza());
                    break;
                case 5:
                    oNetto[1] += szt.getNetto(szla.isDeviza());
                    oAfaErtek[1] += szt.getAfaErtek(szla.isDeviza());
                    oBrutto[1] += szt.getNetto(szla.isDeviza()) + szt.getAfaErtek(szla.isDeviza());
                    break;
                case 25:
                    oNetto[2] += szt.getNetto(szla.isDeviza());
                    oAfaErtek[2] += szt.getAfaErtek(szla.isDeviza());
                    oBrutto[2] += szt.getNetto(szla.isDeviza()) + szt.getAfaErtek(szla.isDeviza());
                    break;
                case 27:
                    oNetto[3] += szt.getNetto(szla.isDeviza());
                    oAfaErtek[3] += szt.getAfaErtek(szla.isDeviza());
                    oBrutto[3] += szt.getNetto(szla.isDeviza()) + szt.getAfaErtek(szla.isDeviza());
                    break;
            }
        }

        N = N - (szla.getTipus() == 2 ? (1 + (szla.isDeviza() ? 1 : 0)) : 0);

        osszPage = (int) Math.ceil(szamlaTermekek.size() / (N * 1.0));
        if ((osszPage * N) - szamlaTermekek.size() < N / 2) {
            // ha az utolsó oldalon N/2-nél több termék van!
            osszPage += 1;
        }
        //System.out.println("OSSZPAGE: " + osszPage + "  N: " + N);
        // oldalak számítása vége
        Book book = new Book();

        Dimension A4 = new Dimension(W, H);
        Dimension d = new Dimension((int) Math.round(A4.width * scale + (elonezet ? 20 * scale : 0)), (int) Math.round(A4.height * scale + (elonezet ? 20 * scale : 0)));

        pages.removeAll(pages);
        for (int i = 0; i < osszPage; i++) {
            Component c = new Page(i + 1);
            c.setSize(W - (elonezet ? 0 : 60), H - (elonezet ? 0 : 50));
            pages.add(c);
            PagePrintable printable = new PagePrintable(c);
            PageFormat pageFormat = printerJob.defaultPage();
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
        printerJob.setPageable(book);

        // NYOMTATÁS
        if (tipus == NYOMTATAS) {
            scale = 1.0;
            for (peldany = 1; peldany <= osszPeldany; peldany++) {
                try {
                    String printJobName = szla.getTeljesitesIgazolasModel().getTIGSorszam();
                    printerJob.setJobName(printJobName);
                    printerJob.print();
                } catch (Exception PrintException) {
                    PrintException.printStackTrace();
                }
            }
        } else if (tipus == ELONEZET) {
            elonezetFrissites();

            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            Insets scnMax = Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration());
            int taskBarSize = scnMax.bottom;
            dim.height = dim.height - taskBarSize;

            this.setSize(dim);

            this.setTitle("Előnézet");
            this.setModal(true);
            this.setVisible(true);
        } else if (tipus == PDF) {
            elonezetFrissites();

            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            Insets scnMax = Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration());
            int taskBarSize = scnMax.bottom;
            dim.height = dim.height - taskBarSize;

            this.setSize(dim);

            this.szla.setNyomtatva(1);
            this.setVisible(true);
            printToPDF();
            doClose(RET_OK);
        }
    }

    private void elonezetFrissites() {
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.removeAll();
        for (Component c : pages) {
            Elonezet e = new Elonezet(c);
            panel.add(e);
        }
        jScrollPane1.setViewportView(panel);
        jScrollPane1.validate();
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
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(cezeszamlazo.App.class).getContext().getResourceMap(ElonezetTeljesitesDialog.class);
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
        jScrollPane1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jScrollPane1MouseDragged(evt);
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

    /**
     * Closes the dialog
     */
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

    private void jScrollPane1MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane1MouseDragged
        // TODO add your handling code here:
    }//GEN-LAST:event_jScrollPane1MouseDragged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        printToPDF();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void doClose(int retStatus) {
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

    private int header(Graphics2D g2d, int page) {
        GlyphVector msg;
        g2d.setColor(Color.decode("#eeeeee"));
        g2d.fillRoundRect(0, 0, mx, 60, 5, 5);
        g2d.setColor(Color.BLACK);
        g2d.drawRoundRect(0, 0, mx, 60, 5, 5);
        g2d.setFont(sansBoldItalic16);
        String str = "";
        if (szla.getTipus() == 2) {
            str = "";
            if (szla.isDeviza()) {
                str += " / Cancelled Invoice";
            }
            if (szla.getNyomtatva() == 1) {
                str += " (Minta)";
            }
            msg = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), "Teljesítés Igazolás" + str);
            g2d.drawGlyphVector(msg, 5, 15);
        } else {
            str = "";
            if (szla.isDeviza()) {
                str += " / Invoice";
            }
            if (szla.getNyomtatva() == 1) {
//                str += " (Az eredetivel mindenben megegyező hiteles másolat.)";
            }
            //g2d.drawString("Számla" + str, 5, 15);
            msg = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), "Teljesítés Igazolás" + str);
            g2d.drawGlyphVector(msg, 5, 15);
        }
        FontMetrics fm = g2d.getFontMetrics(sansBoldItalic16);
        str = "";
        if (szla.isDeviza()) {
            str = "/V";
        }
        g2d.drawString(szla.getTeljesitesIgazolasModel().getTIGSorszam() + str, mx - fm.stringWidth(szla.getTeljesitesIgazolasModel().getTIGSorszam() + str) - 5, 55);
        g2d.setFont(sansBold11);
        if (szla.getNyomtatva() == 0) {
            if (peldany == 1) {
                g2d.drawString("1. - Eredeti - példány", 5, 40);
            } else {
                g2d.drawString(peldany + ". - példány", 5, 40);
            }
            g2d.setFont(sansPlain10);
            g2d.drawString("Ez a számla összesen " + osszPeldany + " példányban került kinyomtatásra.", 5, 55);
        }
        g2d.setFont(sansPlain9);
        fm = g2d.getFontMetrics(sansPlain9);
        if (page != 1) {
            str = "folytatás a(z) " + (page - 1) + ". oldalról";
            g2d.drawString(str, mx - 5 - fm.stringWidth(str), 15);
        }
        g2d.setFont(sansPlain10);
        if (szla.isDeviza()) {
            str = " / Invoice No.";
        } else {
            str = "";
        }
        g2d.drawString("Sorszám" + str, 350 - (str.isEmpty() ? 0 : 70), 55);
        g2d.setFont(sansBold12);
        if (szla.isDeviza()) {
            str = " / Sold by";
        }
        g2d.drawString("Szállító" + str, 5, 78);
        if (szla.isDeviza()) {
            str = " / Client";
        }
        msg = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), "Vevő" + str);
        g2d.drawGlyphVector(msg, mx / 2 + 5, 78);
        g2d.setFont(sansBoldItalic12);
        fm = g2d.getFontMetrics();
        g2d.drawString(szla.getSzallito().getNev(), 5, 90);

        int p = 0;
        if (fm.stringWidth(szla.getVevo().getNev()) > mx / 2 - 10) {
            String[] reszek = szla.getVevo().getNev().split(" ");
            String temp = "";
            int LineWidth = mx / 2 - 10,
                    SpaceLeft = LineWidth,
                    SpaceWidth = fm.stringWidth(" "),
                    j = 0, pluszSor = 0;
            for (String s : reszek) {
                if ((fm.stringWidth(s) + SpaceWidth) > SpaceLeft) {
                    msg = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), temp);
                    g2d.drawGlyphVector(msg, mx / 2 + 5, 90 + (j + pluszSor) * 10);
                    SpaceLeft = LineWidth - fm.stringWidth(s) - SpaceWidth;
                    temp = s + " ";
                    pluszSor++;
                    p += 10;
                } else {
                    SpaceLeft = SpaceLeft - (fm.stringWidth(s) + SpaceWidth);
                    temp += s + " ";
                }
            }
            msg = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), temp);
            g2d.drawGlyphVector(msg, mx / 2 + 5, 90 + (j + pluszSor) * 10);
            p += 10;
        } else {
            msg = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), szla.getVevo().getNev());
            g2d.drawGlyphVector(msg, mx / 2 + 5, 90);
            p += 10;
        }

        g2d.setFont(sansPlain8);
        fm = g2d.getFontMetrics();
        g2d.drawString(szla.getSzallito().getIrsz() + " " + szla.getSzallito().getVaros(), 5, 90 + 1 * 10);
        g2d.drawString(szla.getSzallito().getAddress(), 5, 90 + 2 * 10);
        str = ": ";
        if (szla.isDeviza()) {
            str = " / Tax Number: ";
        }
        g2d.drawString("Adószám" + str + szla.getSzallito().getAdoszam(), 5, 90 + 3 * 10);
        str = ": ";
        if (szla.isDeviza()) {
            str += " / Bank Account Number: ";
        }
        g2d.drawString("Bankszámlaszám" + str, 5, 90 + 4 * 10);
        g2d.drawString(szla.getSzallito().getBankszamlaszam(), 5, 90 + 5 * 10);
        g2d.drawString(szla.getSzallito().getMegjegyzes(), 5, 90 + 6 * 10);

        msg = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), (!szla.getVevo().getIrsz().equalsIgnoreCase("0") && !szla.getVevo().getIrsz().isEmpty() ? szla.getVevo().getIrsz() + ", " : "") + szla.getVevo().getVaros());
        g2d.drawGlyphVector(msg, mx / 2 + 5, 90 + p);
        p += 10;

        if (fm.stringWidth(szla.getVevo().getAddress()) > mx / 2 - 10) {
            String[] reszek = szla.getVevo().getAddress().split(" ");
            String temp = "";
            int LineWidth = mx / 2 - 10,
                    SpaceLeft = LineWidth,
                    SpaceWidth = fm.stringWidth(" "),
                    j = 0, pluszSor = 0;
            for (String s : reszek) {
                if ((fm.stringWidth(s) + SpaceWidth) > SpaceLeft) {
                    msg = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), temp);
                    g2d.drawGlyphVector(msg, mx / 2 + 5, 90 + p + (j + pluszSor) * 10);
                    SpaceLeft = LineWidth - fm.stringWidth(s) - SpaceWidth;
                    temp = s + " ";
                    pluszSor++;
                } else {
                    SpaceLeft = SpaceLeft - (fm.stringWidth(s) + SpaceWidth);
                    temp += s + " ";
                }
            }
            msg = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), temp);
            g2d.drawGlyphVector(msg, mx / 2 + 5, 90 + p + (j + pluszSor) * 10);
            p += 10 * (pluszSor + 1);
        } else {
            msg = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), szla.getVevo().getAddress());
            g2d.drawGlyphVector(msg, mx / 2 + 5, 90 + p);
            p += 10;
        }
        if (!szla.getVevo().getAdoszam().isEmpty()) {
            g2d.drawString("Adószám: " + szla.getVevo().getAdoszam(), mx / 2 + 5, 90 + p);
            p += 10;
        }
        if (!szla.getVevo().getEuAdoszam().isEmpty()) {
            g2d.drawString("Eu-adószám: " + szla.getVevo().getEuAdoszam(), mx / 2 + 5, 90 + p);
            p += 10;
        }
        if (szla.getVevo().isSzamlanMegjelenik()) {
            g2d.drawString("Bankszámlaszám: " + szla.getVevo().getBankszamlaszam(), mx / 2 + 5, 90 + p);
        }
        g2d.drawLine(0, 65, mx, 65);
        g2d.drawLine(0, 165, mx, 165);
        g2d.drawLine(mx / 2, 65, mx / 2, 165);
        g2d.drawLine(0, 65, 0, 165);
        g2d.drawLine(mx, 65, mx, 165);
        int cs = 0;
        if (szla.isDeviza()) {
            cs = 10;
        }
        g2d.setColor(Color.decode("#eeeeee"));
        g2d.fillRoundRect(0, 170, mx, 15 + cs, 5, 5);
        g2d.setColor(Color.BLACK);
        g2d.drawRoundRect(0, 170, mx, 15 + cs, 5, 5);
        g2d.setFont(sansBold10);
        String[] fejlecReszek = {"Teljesítés időpontja", "Teljesítés Igazolás Kelte"};
        String[] fejlec1ReszekAngol = {"Date of fulfilment", "Date of fulfilmen certificatet"};
        str = "";
        if (szla.isDeviza()) {
            str = "/";
        }
        if (szla.isDeviza()) {
            g2d.drawString(fejlec1ReszekAngol[0], 5 + 0 * mx / 4, 192);
        } else {
            msg = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), fejlecReszek[0] + str);
            g2d.drawGlyphVector(msg, 5 + 0 * mx / 4, 182);
        }
        if (szla.isDeviza()) {
            g2d.drawString(fejlec1ReszekAngol[1], 5 + 3 * mx / 4, 192);
        } else {
            msg = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), fejlecReszek[1] + str);
            g2d.drawGlyphVector(msg, 5 + 3 * mx / 4, 182);
        }

        str = "";
//        if (szla.isDeviza()) {
//            if (szla.getVevo().getFizetesiMod() == 0) {
//                str = " / Cash";
//            } else if (szla.getVevo().getFizetesiMod() == 1) {
//                str = " / Bank Transfer";
//            } else {
//                str = " / C.O.D.";
//            }
//        }
//        if (szla.getVevo().getFizetesiMod() == 0) {
//            str = "Készpénz" + str;
//        } else if (szla.getVevo().getFizetesiMod() == 1) {
//            str = "Átutalás" + str;
//        } else {
//            str = "Utánvét" + str;
//        }
//        g2d.drawString(str, 5 + 0 * mx / 4, 200 + cs);
        g2d.setFont(sansPlain10);
        if (!szla.isDeviza()) {
            g2d.drawString(elonezetFunctions.dateFormat(szla.getTeljesites()), 5 + 0 * mx / 4, 200 + cs);
            g2d.drawString("Debrecen, " + elonezetFunctions.dateFormat(szla.getTeljesitesIgazolasDatuma()), 5 + 3 * mx / 4, 200 + cs);
        } else {
            g2d.drawString(elonezetFunctions.dateFormatEn(szla.getTeljesites()), 5 + 0 * mx / 4, 200 + cs);
            g2d.drawString("Debrecen, " + elonezetFunctions.dateFormatEn(szla.getTeljesitesIgazolasDatuma()), 5 + 3 * mx / 4, 200 + cs);
        }

        if (szla.getTipus() == 2) {
            g2d.setFont(sansBold11);
            fm = g2d.getFontMetrics();
            String s = "Az okirat a következő számlához tartozik: " + szla.getHelyesbitett()
                    + " (Teljesítés időpontja: "
                    + elonezetFunctions.dateFormat(szla.getHelyesbitettTeljesites()) + ")";
            msg = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), s);
            g2d.drawGlyphVector(msg, mx / 2 - fm.stringWidth(s) / 2, 220 + cs);
            g2d.drawLine(0, 205 + cs, mx, 205 + cs);
            if (szla.isDeviza()) {
                s = "Cancelled invoice of invoice " + szla.getHelyesbitett()
                        + "/V (Date of fulfilment: "
                        + elonezetFunctions.dateFormatEn(szla.getHelyesbitettTeljesites()) + ")";
                g2d.drawString(s, mx / 2 - fm.stringWidth(s) / 2, 235 + cs);
                cs += 15;
            }
            g2d.drawLine(0, 225 + cs, mx, 225 + cs);
            cs += 20;
        }
        return 0;
    }

    private void footer(Graphics2D g2d, int page) {
        GlyphVector msg;
        //        String lablec = SzamlaLablec.getLablecWithCegnev(szla.getSzallito().getSzamlaLablec());
        DrawString drawString = new DrawString(szla.getSzallito().getSzamlaLablec(), 80);
        if (page == pages.size()) {
            g2d.setFont(sansPlain10);
            drawString.drawTheString(g2d, my);
        }
        g2d.setFont(sansPlain8);
        if (!szla.isDeviza()) {
            g2d.drawLine(0, my - 45, mx, my - 45);
            g2d.drawString("Ez a számla a Ceze Kft. rendszerével készült. http://ceze.hu", 5, my - 35);
            g2d.drawString("A számla a többször módosított 24/1995 (XI.22) PM rendeletnek megfelel.", 5, my - 25);
            msg = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), "A bizonylatot nyomtatta: " + App.user.getNev());
            g2d.drawGlyphVector(msg, 5, my - 15);
        } else {
            g2d.drawLine(0, my - 55, mx, my - 55);
            g2d.drawString("Ez a számla a Ceze Kft. rendszerével készült. http://ceze.hu / This invoice was prepared using the program of Ceze Kft. http://ceze.hu", 5, my - 45);
            g2d.drawString("A számla a többször módosított 24/1995 (XI.22) PM rendeletnek megfelel. /", 5, my - 35);
            g2d.drawString("The invoice is in compliance with the PM Decree 24/1995 (XI.22) amended with the PM Decree 34/1999 (XII.26).", 5, my - 25);
            msg = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), "A bizonylatot nyomtatta: " + App.user.getNev());
            g2d.drawGlyphVector(msg, 5, my - 15);
        }
        g2d.drawString(page + ". oldal", mx - 40, my - 15);

//        if (!szla.isDeviza()) {
//            if (page == pages.size()) {
//                g2d.setFont(sansPlain10);
//                g2d.drawString("A 2014. évi XXII. tv 3.§. (3) bek. szerint kinyilatkozzuk, hogy reklámadó fizetési kötelezettség cégünket nem terheli, ", 5, my - 70);
//                g2d.drawString("mivel e tevékenység vonatkozásában az adóalapunk a 0,5 milliárd forintot nem éri el.", 5, my - 60);
//                g2d.drawString("A számla kiegyenlítéséig a szállított áru a Ceze Kft. tulajdonát képezi.", 5, my - 50);
//            }
//            g2d.setFont(sansPlain8);
//            g2d.drawLine(0, my - 45, mx, my - 45);
//            g2d.drawString("Ez a számla a Ceze Kft. rendszerével készült. http://ceze.hu", 5, my - 35);
//            g2d.drawString("A számla a többször módosított 24/1995 (XI.22) PM rendeletnek megfelel.", 5, my - 25);
//            msg = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), "A bizonylatot nyomtatta: " + App.user.getNev());
//            g2d.drawGlyphVector(msg, 5, my - 15);
//        } else {
//            if (page == pages.size()) {
//                g2d.setFont(sansPlain10);
//                g2d.drawString("A 2014. évi XXII. tv 3.§. (3) bek. szerint kinyilatkozzuk, hogy reklámadó fizetési kötelezettség cégünket nem terheli, ", 5, my - 70);
//                g2d.drawString("mivel e tevékenység vonatkozásában az adóalapunk a 0,5 milliárd forintot nem éri el.", 5, my - 60);
//                g2d.drawString("A számla kiegyenlítéséig a szállított áru a Ceze Kft. tulajdonát képezi.", 5, my - 50);
//            }
//            g2d.setFont(sansPlain8);
//            g2d.drawLine(0, my - 55, mx, my - 55);
//            g2d.drawString("Ez a számla a Ceze Kft. rendszerével készült. http://ceze.hu / This invoice was prepared using the program of Ceze Kft. http://ceze.hu", 5, my - 45);
//            g2d.drawString("A számla a többször módosított 24/1995 (XI.22) PM rendeletnek megfelel. /", 5, my - 35);
//            g2d.drawString("The invoice is in compliance with the PM Decree 24/1995 (XI.22) amended with the PM Decree 34/1999 (XII.26).", 5, my - 25);
//            msg = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), "A bizonylatot nyomtatta: " + App.user.getNev());
//            g2d.drawGlyphVector(msg, 5, my - 15);
//        }
    }

    public void content(Graphics2D g2d, int page) {
        FontMetrics fm;
        String str;
        GlyphVector msg;
//	msg = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), "árvíztűrő tükörfúrógép - ÁRVÍZTŰRŐ TÜKÖRFÚRÓGÉP");
//	g2d.drawGlyphVector(msg, 0, 0);

        if (elonezet) {
            g2d.translate(10 * scale, 10 * scale);
        }

        g2d.setColor(Color.black);
        /* Now we perform our rendering */

        int sor = 10, j = 0, cs = (szla.getTipus() == 2 ? 20 + (szla.isDeviza() ? 25 : 0) : 0), c = 0,
                n = (N * page < szamlaTermekek.size() ? N * page : szamlaTermekek.size());

        if (n <= szamlaTermekek.size()) {
            g2d.setColor(Color.decode("#eeeeee"));
            if (szla.isDeviza()) {
                g2d.fillRoundRect(0, 210 + cs, mx, 25, 5, 5);
            } else {
                g2d.fillRoundRect(0, 210 + cs, mx, 15, 5, 5);
            }
            g2d.setColor(Color.BLACK);
            if (szla.isDeviza()) {
                g2d.drawRoundRect(0, 210 + cs, mx, 25, 5, 5);
            } else {
                g2d.drawRoundRect(0, 210 + cs, mx, 15, 5, 5);
            }
            g2d.setFont(sansBold10);
            String[] fejlec2 = {"Megnevezés", "VTSZ/TESZOR", "Mennyiség Mee", "Egységár", "Nettó", "ÁFA", "ÁFA Érték", "Bruttó"};
            String[] fejlec2d = {"Description", "VTSZ/TESZOR Nr.", "Amount Unit", "Unit price", "Net", "VAT", "VAT price", "Gross"};
            fm = g2d.getFontMetrics();
            int[] foMeret = {90, 70, 80, 65, 65, 30, 65, 65},
                    ossz = new int[9];
            ossz[0] = 0;
            for (int i = 0; i < 8; i++) {
                if (i > 0) {
                    g2d.drawString(fejlec2[i] + (szla.isDeviza() ? "/" : ""), ossz[i] + foMeret[i] - fm.stringWidth(fejlec2[i] + (szla.isDeviza() ? "/" : "")), 220 + cs);
                    if (szla.isDeviza()) {
                        g2d.drawString(fejlec2d[i], ossz[i] + foMeret[i] - fm.stringWidth(fejlec2d[i]), 230 + cs);
                    }
                } else {
                    g2d.drawString(fejlec2[i], 5 + ossz[i], 220 + cs);
                    if (szla.isDeviza()) {
                        g2d.drawString(fejlec2d[i], 5 + ossz[i], 230 + cs);
                    }
                }
                ossz[i + 1] = ossz[i] + foMeret[i];
            }
            g2d.setFont(sansPlain8);
            if (szla.isDeviza()) {
                cs += 10;
            }
            j = 0;
            sor = 10;
            fm = g2d.getFontMetrics();
            for (c = N * (page - 1); c < n; c++) {
                count = c;
                SzamlaTermek szt = szamlaTermekek.get(c);
                int pluszSor = 0;
                str = szt.getNev();
                if (szt.getTermekDij() != null) {
                    str += " - " + EncodeDecode.numberFormat(String.valueOf(szt.getTermekDij().getSuly()), true) + " kg";
                }
                msg = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), str);
                g2d.drawGlyphVector(msg, 5, 240 + j * sor + cs);
                str = "";
                j++;
                if (szla.isAtvallal() && szt.getTermekDij() != null) {
                    if (!szt.getTermekDij().getCsk().isEmpty()) {
                        str = "CSK: " + szt.getTermekDij().getCsk();
                    } else if (!szt.getTermekDij().getKt().isEmpty()) {
                        str = "KT: " + szt.getTermekDij().getKt();
                    }
                    g2d.drawString(str, 5, 240 + j * sor + cs);
                } else {
                    System.out.println("");
                    System.out.println("EE!");
                    System.out.println("");
                }
                g2d.drawString(szt.getVtszTeszor(), ossz[2] - fm.stringWidth(szt.getVtszTeszor()), 240 + j * sor + cs);
                msg = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), elonezetFunctions.numberFormat(String.valueOf(szt.getMennyiseg()), true) + " " + szt.getMee());
                g2d.drawGlyphVector(msg, ossz[3] - fm.stringWidth(elonezetFunctions.numberFormat(String.valueOf(szt.getMennyiseg()), true) + " " + szt.getMee()), 240 + j * sor + cs);
                if (szla.isDeviza() && szla.getKozeparfolyam() != 1.0) {
                    g2d.drawString(elonezetFunctions.numberFormat(String.valueOf(szt.getEgysegar())) + " " + szla.getValuta(), ossz[4] - fm.stringWidth(elonezetFunctions.numberFormat(String.valueOf(szt.getEgysegar())) + " " + szla.getValuta()), 240 + j * sor + cs);
                } else {
                    g2d.drawString(elonezetFunctions.numberFormat(String.valueOf(szt.getEgysegar()), true) + " " + szla.getValuta(), ossz[4] - fm.stringWidth(elonezetFunctions.numberFormat(String.valueOf(szt.getEgysegar()), true) + " " + szla.getValuta()), 240 + j * sor + cs);
                }
                str = elonezetFunctions.numberFormat(String.valueOf(szt.getNetto(szla.isDeviza())), szla.isDeviza() && szla.getKozeparfolyam() != 1.0) + " " + szla.getValuta();
                g2d.drawString(str, ossz[5] - fm.stringWidth(str), 240 + j * sor + cs);
                g2d.drawString(((int) szt.getAfa()) + "%", ossz[6] - fm.stringWidth(((int) szt.getAfa()) + "%"), 240 + j * sor + cs);
                str = elonezetFunctions.numberFormat(String.valueOf(szt.getAfaErtek(szla.isDeviza() && szla.getKozeparfolyam() != 1.0)), szla.isDeviza() && szla.getKozeparfolyam() != 1.0) + " " + szla.getValuta();
                g2d.drawString(str, ossz[7] - fm.stringWidth(str), 240 + j * sor + cs);
                str = elonezetFunctions.numberFormat(String.valueOf(szt.getBrutto(szla.isDeviza() && szla.getKozeparfolyam() != 1.0)), szla.isDeviza() && szla.getKozeparfolyam() != 1.0) + " " + szla.getValuta();
                g2d.drawString(str, mx - 5 - fm.stringWidth(str), 240 + j * sor + cs);
                j++;
                j += pluszSor;
            }
        }
        if (n == szamlaTermekek.size() && page == pages.size()) {

            g2d.drawLine(0, 240 + j * sor - sor / 2 + cs, mx, 240 + j * sor - sor / 2 + cs);
            int m = 0;

            m = 250 + j * sor + cs;
            g2d.setColor(Color.decode("#eeeeee"));
            if (szla.isDeviza()) {
                g2d.fillRoundRect(200, m, mx - 200, 25, 5, 5);
            } else {
                g2d.fillRoundRect(200, m, mx - 200, 15, 5, 5);
            }
            g2d.setColor(Color.BLACK);
            if (szla.isDeviza()) {
                g2d.drawRoundRect(200, m, mx - 200, 25, 5, 5);
            } else {
                g2d.drawRoundRect(200, m, mx - 200, 15, 5, 5);
            }
            g2d.setFont(sansBold10);
            String[] fejlec3 = {"ÁFA", "Nettó ár", "ÁFA érték", "Bruttó ár"};
            String[] fejlec3d = {"VAT", "Net", "VAT price", "Gross"};
            int[] meret2 = new int[5];
            for (int i = 0; i < 5; i++) {
                meret2[i] = i * (mx - 150) / 4 + 150 - 5;
            }
            fm = g2d.getFontMetrics();
            for (int i = 0; i < 4; i++) {
                g2d.drawString(fejlec3[i] + (szla.isDeviza() ? "/" : ""), meret2[i + 1] - fm.stringWidth(fejlec3[i] + (szla.isDeviza() ? "/" : "")), m + 10);
                if (szla.isDeviza()) {
                    g2d.drawString(fejlec3d[i], meret2[i + 1] - fm.stringWidth(fejlec3d[i]), m + 20);
                }
            }
            g2d.setFont(sansPlain10);
            if (szla.isDeviza()) {
                m += 10;
            }
            fm = g2d.getFontMetrics();
            j = 3;
            for (int i = 0; i < oNetto.length; i++) {
                if (oNetto[i] != 0) {
                    int afa = (i == 0 ? 0 : (i == 1 ? 5 : (i == 2 ? 25 : 27)));
                    double nn = 0.0;
                    boolean isUtalas = ((szla.getVevo().getFizetesiMod() == 1) ? true : false);
                    if (isUtalas) {
                        nn = oNetto[i];
                    } else {
                        nn = (szla.isDeviza() && szla.getKozeparfolyam() != 1.0 ? oNetto[i] : Math.round(oNetto[i]));
                    }

                    double aa = (szla.isDeviza() && szla.getKozeparfolyam() != 1.0 ? oAfaErtek[i] : Math.round(oAfaErtek[i]));
                    double bb = (szla.isDeviza() && szla.getKozeparfolyam() != 1.0 ? oBrutto[i] : Math.round(oBrutto[i]));
                    g2d.drawString(afa + "%", meret2[1] - fm.stringWidth(afa + "%"), m + j * sor);
                    g2d.drawString(elonezetFunctions.numberFormat(String.valueOf(nn), szla.isDeviza() && szla.getKozeparfolyam() != 1.0) + " " + szla.getValuta(), meret2[2] - fm.stringWidth(elonezetFunctions.numberFormat(String.valueOf(nn), szla.isDeviza() && szla.getKozeparfolyam() != 1.0) + " " + szla.getValuta()), m + j * sor);
                    g2d.drawString(elonezetFunctions.numberFormat(String.valueOf(aa), szla.isDeviza() && szla.getKozeparfolyam() != 1.0) + " " + szla.getValuta(), meret2[3] - fm.stringWidth(elonezetFunctions.numberFormat(String.valueOf(aa), szla.isDeviza() && szla.getKozeparfolyam() != 1.0) + " " + szla.getValuta()), m + j * sor);
                    g2d.drawString(elonezetFunctions.numberFormat(String.valueOf(bb), szla.isDeviza() && szla.getKozeparfolyam() != 1.0) + " " + szla.getValuta(), meret2[4] - fm.stringWidth(elonezetFunctions.numberFormat(String.valueOf(bb), szla.isDeviza() && szla.getKozeparfolyam() != 1.0) + " " + szla.getValuta()), m + j * sor);
                    j++;
                }
            }
            g2d.drawLine(200, m + j * sor, mx, m + j * sor);
            j++;
            g2d.setFont(sansBold11);
            fm = g2d.getFontMetrics();
            g2d.drawString("Összesen", meret2[1] - fm.stringWidth("Összesen"), m + j * sor);
            g2d.drawString(elonezetFunctions.numberFormat(String.valueOf(szla.isDeviza() && szla.getKozeparfolyam() != 1.0 ? oBrutto[0] + oBrutto[1] + oBrutto[2] + oBrutto[3] : Math.round(oBrutto[0] + oBrutto[1] + oBrutto[2] + oBrutto[3])), szla.isDeviza() && szla.getKozeparfolyam() != 1.0) + " " + szla.getValuta(),
                    meret2[4] - fm.stringWidth(elonezetFunctions.numberFormat(String.valueOf(szla.isDeviza() && szla.getKozeparfolyam() != 1.0 ? oBrutto[0] + oBrutto[1] + oBrutto[2] + oBrutto[3] : Math.round(oBrutto[0] + oBrutto[1] + oBrutto[2] + oBrutto[3])), szla.isDeviza() && szla.getKozeparfolyam() != 1.0) + " " + szla.getValuta()),
                    m + j * sor);
            g2d.setFont(sansPlain10);
            fm = g2d.getFontMetrics();

            double nn = 0.0;
            boolean isUtalas = ((szla.getVevo().getFizetesiMod() == 1) ? true : false);
            if (isUtalas) {
                nn = oNetto[0] + oNetto[1] + oNetto[2] + oNetto[3];
            } else {
                nn = (szla.isDeviza() && szla.getKozeparfolyam() != 1.0
                        ? oNetto[0] + oNetto[1] + oNetto[2] + oNetto[3]
                        : Math.round(oNetto[0]) + Math.round(oNetto[1]) + Math.round(oNetto[2]) + Math.round(oNetto[3]));
            }
            double aa = (szla.isDeviza() && szla.getKozeparfolyam() != 1.0
                    ? oAfaErtek[0] + oAfaErtek[1] + oAfaErtek[2] + oAfaErtek[3]
                    : Math.round(oAfaErtek[0]) + Math.round(oAfaErtek[1]) + Math.round(oAfaErtek[2]) + Math.round(oAfaErtek[3]));
            g2d.drawString(elonezetFunctions.numberFormat(String.valueOf(nn), szla.isDeviza() && szla.getKozeparfolyam() != 1.0) + " " + szla.getValuta(),
                    meret2[2] - fm.stringWidth(elonezetFunctions.numberFormat(String.valueOf(nn), szla.isDeviza() && szla.getKozeparfolyam() != 1.0) + " " + szla.getValuta()),
                    m + j * sor);
            g2d.drawString(elonezetFunctions.numberFormat(String.valueOf(aa), szla.isDeviza() && szla.getKozeparfolyam() != 1.0) + " " + szla.getValuta(),
                    meret2[3] - fm.stringWidth(elonezetFunctions.numberFormat(String.valueOf(aa), szla.isDeviza() && szla.getKozeparfolyam() != 1.0) + " " + szla.getValuta()),
                    m + j * sor);
            j += 2;

            g2d.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));

            fm = g2d.getFontMetrics();
            if (szla.getTipus() == 2) {
                str = ": ";
                if (szla.isDeviza()) {
                    str = " / Refund: ";
                }
                msg = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), "Visszatérítend\u0151 összeg" + str + elonezetFunctions.numberFormat(String.valueOf(String.valueOf(szla.isDeviza() && szla.getKozeparfolyam() != 1.0 ? Math.abs(oBrutto[0] + oBrutto[1] + oBrutto[2] + oBrutto[3]) : Math.abs(Math.round(oBrutto[0] + oBrutto[1] + oBrutto[2] + oBrutto[3])))), szla.isDeviza() && szla.getKozeparfolyam() != 1.0) + " " + szla.getValuta() + ",");
                g2d.drawGlyphVector(msg, mx - fm.stringWidth("Visszatérítend\u0151 összeg" + str + elonezetFunctions.numberFormat(String.valueOf(szla.isDeviza() && szla.getKozeparfolyam() != 1.0 ? Math.abs(oBrutto[0] + oBrutto[1] + oBrutto[2] + oBrutto[3]) : Math.abs(Math.round(oBrutto[0] + oBrutto[1] + oBrutto[2] + oBrutto[3]))), szla.isDeviza() && szla.getKozeparfolyam() != 1.0) + " " + szla.getValuta() + ","), m + j * sor);
            } else {
                str = ": ";
                if (szla.isDeviza()) {
                    str = " / Total: ";
                }
                isUtalas = ((szla.getVevo().getFizetesiMod() == 1) ? true : false);
                double ossz = Functions.kerekit(oBrutto[0] + oBrutto[1] + oBrutto[2] + oBrutto[3], isUtalas);
                msg = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), "Fizetend\u0151 összeg" + str + elonezetFunctions.numberFormat(String.valueOf(szla.isDeviza() && szla.getKozeparfolyam() != 1.0 ? ossz : Math.round(ossz)), szla.isDeviza() && szla.getKozeparfolyam() != 1.0) + " " + szla.getValuta() + ",");
                g2d.drawGlyphVector(msg, mx - fm.stringWidth("Fizetend\u0151 összeg" + str + elonezetFunctions.numberFormat(String.valueOf(szla.isDeviza() && szla.getKozeparfolyam() != 1.0 ? ossz : Math.round(ossz)), szla.isDeviza() && szla.getKozeparfolyam() != 1.0) + " " + szla.getValuta() + ","), m + j * sor);
            }
            g2d.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
            j += 2;
            Query query = new Query.QueryBuilder()
                    .select("valuta_nev")
                    .from("szamlazo_valuta")
                    .where(" valuta = '" + (szla.getValuta().equalsIgnoreCase("Ft") ? "HUF" : szla.getValuta()) + "'")
                    .build();
            Object[][] sl = App.db.select(query.getQuery());
            str = String.valueOf(sl[0][0]);
            if (szla.isDeviza() && szla.getKozeparfolyam() != 1.0) {
                msg = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), "azaz " + elonezetFunctions.betuvel(Math.abs(Math.round((oBrutto[0] + oBrutto[1] + oBrutto[2] + oBrutto[3]) * 100.0) / 100.0)) + " " + str + ".");
                g2d.drawGlyphVector(msg, 5, m + j * sor);
            } else {
                msg = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), "azaz " + elonezetFunctions.betuvel(Math.round(Math.abs(oBrutto[0] + oBrutto[1] + oBrutto[2] + oBrutto[3]))) + " " + str + ".");
                g2d.drawGlyphVector(msg, 5, m + j * sor);
            }
            if (szla.isDeviza()) {
                j++;
                g2d.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
                g2d.drawString("(Árfolyam / Exchange rate: " + elonezetFunctions.numberFormat(String.valueOf(szla.getKozeparfolyam())) + " Ft/" + szla.getValuta() + ")", 5, m + j * sor + 5);
            }
            j += 2;
            g2d.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
            if (osszCsom != 0.0) {
                g2d.drawString("A csomagolószer termékdíj összege a bruttó árból: "
                        + elonezetFunctions.numberFormat(String.valueOf(osszCsom), false) + " Ft", 5, m + j * sor + 5);
                sor++;
            }
            if (osszRekl != 0.0) {
                g2d.drawString("A reklámpapír termékdíj összege a bruttó árból: "
                        + elonezetFunctions.numberFormat(String.valueOf(osszRekl), false) + " Ft", 5, m + j * sor + 5);
                sor++;
            }
            sor++;
            g2d.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
            if (!szla.getMegjegyzes().isEmpty()) {
                g2d.setFont(sansPlain10);
                szla.setMegjegyzes(szla.getMegjegyzes().replace("\t", "  "));
                g2d.drawString("Megjegyzés:", 5, m + j * sor);
                j += 2;
                String[] temp = szla.getMegjegyzes().split("\\n");
                g2d.setFont(sansPlain8);
                fm = g2d.getFontMetrics();
                for (String s : temp) {
                    if (fm.stringWidth(s) < mx - 10) {
                        msg = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), s);
                        g2d.drawGlyphVector(msg, 5, m + j * sor);
                    } else {
                        String[] reszek = s.split(" ");
                        int LineWidth = mx - 10;
                        int SpaceLeft = LineWidth;
                        int SpaceWidth = fm.stringWidth(" ");
                        String r = "";
                        for (String s2 : reszek) {
                            if ((fm.stringWidth(s2) + SpaceWidth) > SpaceLeft) {
                                msg = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), r);
                                g2d.drawGlyphVector(msg, 5, m + j * sor);
                                SpaceLeft = LineWidth - fm.stringWidth(s2) - SpaceWidth;
                                r = s2 + " ";
                                j++;
                            } else {
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
            if (!szla.getLablec().isEmpty()) {
                g2d.setFont(sansPlain10);

//                String[] temp = szla.getLablecWithCegnev().split("\\n");
                String[] temp = this.getLablec().split("\n");

                g2d.setFont(sansPlain8);
                fm = g2d.getFontMetrics();
                for (String tempString : temp) {
                    if (fm.stringWidth(tempString) < mx - 10) {
                        msg = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), tempString);
                        g2d.drawGlyphVector(msg, 5, m + j * sor);
                    } else {
                        String[] reszek = tempString.split(" ");
                        int LineWidth = mx - 10;
                        int SpaceLeft = LineWidth;
                        int SpaceWidth = fm.stringWidth(" ");
                        String reszekString = "";
                        for (String s2 : reszek) {
                            if ((fm.stringWidth(s2) + SpaceWidth) > SpaceLeft) {
                                msg = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), reszekString);
                                g2d.drawGlyphVector(msg, 5, m + j * sor);
                                SpaceLeft = LineWidth - fm.stringWidth(s2) - SpaceWidth;
                                reszekString = s2 + " ";
                                j++;
                            } else {
                                SpaceLeft = SpaceLeft - (fm.stringWidth(s2) + SpaceWidth);
                                reszekString += s2 + " ";
                            }
                        }
                        msg = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), reszekString);
                        g2d.drawGlyphVector(msg, 5, m + j * sor);
                        msg = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), "Szállító:");
                        g2d.drawGlyphVector(msg, 10, (m + j * sor) + 50);
                        msg = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), "Vevő:");
                        g2d.drawGlyphVector(msg, 400, (m + j * sor) + 50);
                    }
                    j++;
                }
            }
        }
    }

    private void printToPDF() {
        File file = new File("teljesites_igazolas_" + szla.getSorszam().replace(" ", "_").replace("/", "_") + ".pdf");
        try {
            JFileChooser chooser = new JFileChooser();
            chooser.setSelectedFile(file);
            chooser.showOpenDialog(null);
            File curFile = chooser.getSelectedFile();
            if (curFile != null) {
                scale = 1.0;
                Document document = new Document(PageSize.A4, 20, 20, 20, 20);
                try {
                    PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(curFile.getAbsoluteFile()));
                    document.open();
                    PageFormat pf = new PageFormat();
                    Paper paper = new Paper();
                    paper.setSize(mx, my);
                    paper.setImageableArea(0, 0, mx, my);
                    pf.setPaper(paper);

                    float pageImageableWidth = (float) pf.getImageableWidth(),
                            pageImageableHeight = (float) pf.getImageableHeight();
                    
                    byte[] rBytes = IOUtils.toByteArray(Thread.currentThread().getContextClassLoader()
                            .getResourceAsStream("/cezeszamlazo/resources/fonts/arial.ttf"));

                    BaseFont baseFont = BaseFont.createFont("arial.ttf", BaseFont.IDENTITY_H, true, false, rBytes, null);
                    
                    
//                    com.itextpdf.text.Font font2
//                            = FontFactory.getFont("c:\\windows\\fonts\\arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 10);

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
                        this.szla.getTeljesitesIgazolasModel().save();
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
    }

    class Elonezet extends JPanel {

        public final Dimension A4 = new Dimension(W, H);
        private Component c;

        public Elonezet(Component c) {
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
        public void paintComponent(Graphics g) {
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
            if (szla != null) {
                c.paint(g2d);
            } else {
                // Hiba!!
                //paint(g2d, pf, scale);
            }
        }
    }

    class Page extends JComponent implements Printable {

        private int page = 0;

        public Page(int page) {
            this.page = page;
        }

        public void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            mx = getWidth();
            my = getHeight();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.scale(scale, scale);
            content(g2d, page);
            footer(g2d, page);
            header(g2d, page);
        }

        public int print(Graphics g, PageFormat pageFormat, int pageIndex) {
            if (pageIndex != 0) {
                return NO_SUCH_PAGE;
            }
            paintComponent(g);
            return PAGE_EXISTS;
        }
    }

    class PagePrintable implements Printable {

        private Component mComponent;

        public PagePrintable(Component c) {
            mComponent = c;
        }

        public int print(Graphics g, PageFormat pageFormat, int pageIndex) {
            Graphics2D g2 = (Graphics2D) g;
            g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
            mComponent.paint(g2);
            return PAGE_EXISTS;
        }
    }

    private String getLablec() {
        return "A Szállító a fenti felsorolt szolgáltatásokat "
                + "a megrendelésnek megfelelően, határidőre teljesítette. "
                + "A teljesítés kapcsán az elvégzett munka ellenértékéről "
                + "számla kiállítására és benyújtására jogosult a megrendelő "
                + "felé a megrendelésben rögzített feltételekkel.";

    }
}
