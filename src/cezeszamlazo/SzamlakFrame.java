package cezeszamlazo;

import java.awt.Color;
import cezeszamlazo.controller.Functions;
import cezeszamlazo.database.Query;
import cezeszamlazo.functions.KeyPressed;
import cezeszamlazo.kintlevoseg.KintlevosegLevel;
import cezeszamlazo.kintlevoseg.KintlevosegLevelException;
import cezeszamlazo.kintlevoseg.KintlevosegKapcsolattartokGroup;
import cezeszamlazo.model.HttpClientFactory;
import cezeszamlazo.model.PopupTimer;
import cezeszamlazo.views.PdfViewer;
import cezeszamlazo.views.PrinterSelect;
import cezeszamlazo.views.ResizeableInvoiceView;
import com.itextpdf.text.pdf.codec.Base64;
import invoice.Invoice;
import invoice.Invoice.InvoiceType;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class SzamlakFrame extends javax.swing.JFrame
{
    public enum SzamlaType
    {
        UJ, MASOLAT, STORNO, HELYESBITO, DEVIZA, MODOSIT
    }
    
    PopupTimer popupTimer;
    
    String keres = "1";
    boolean b = true;
    boolean comboboxUpdate = false;
    boolean isKintlevoseg;
    String nyomtatasAzon = "";
    FolyamatbanDialog folyamatbanDialog;
    boolean firstInitEasyCombobox = false;

    int sorszamozasItemStateChangeCounter = 0;

    FrissitesThread frissitesThread = new FrissitesThread();
    
    public SzamlakFrame()
    {
        initComponents();
        init();
        
        String workingDir = System.getProperty("user.dir");
        //System.err.println(workingDir + " SzamlakFrame/SzamlakFrame()");
        File [] folders = {new File(workingDir + "/dokumentumok/csatolmanyok/kintlevoseg/"), new File(workingDir)};
        deletePDFs(folders);
    }
    
    private boolean GLSAvailable()
    {
        Query query = new Query.QueryBuilder()
            .select("needGLS")
            .from("szamlazo_users")
            .where("id = " + App.user.getId())
            .build();
        Object [][] glsAvailable = App.db.select(query.getQuery());
        
        boolean res = glsAvailable[0][0].toString().equals("1");
        
        return res;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        szamlaPopupMenu = new javax.swing.JPopupMenu();
        masolatMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        modositoMenuItem = new javax.swing.JMenuItem();
        helyesbitoMenuItem = new javax.swing.JMenuItem();
        stornoMenuItem = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        statusRefreshMenuItem = new javax.swing.JMenuItem();
        kifizetesMenuItem = new javax.swing.JMenuItem();
        nyomtatasMenuItem = new javax.swing.JMenuItem();
        menuItem_GLS = new javax.swing.JMenuItem();
        szamlaKuldesEmailMenuItem = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        pdfKeszitesMenuItem = new javax.swing.JMenuItem();
        csvKeszitesEgyszeruMenuItem = new javax.swing.JMenuItem();
        csvKeszitesReszletesMenuItem = new javax.swing.JMenuItem();
        kijeloltekTermekdijKimutatas = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        kintlevosegLevelEmailMenuItem = new javax.swing.JMenuItem();
        kintlevosegLevelPDFMenuItem = new javax.swing.JMenuItem();
        teljesitesIgazolasNyomtatasaMenuItem = new javax.swing.JMenuItem();
        nyomtatasDialog = new javax.swing.JDialog();
        nyomtatasLabel = new javax.swing.JLabel();
        nyomtatasPeldany = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        printingPanel = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        printerComboBox = new javax.swing.JComboBox<>();
        reszletesKeresesDialog = new javax.swing.JDialog();
        jPanel5 = new javax.swing.JPanel();
        normal = new javax.swing.JCheckBox();
        helyesbito = new javax.swing.JCheckBox();
        helyesbitett = new javax.swing.JCheckBox();
        jPanel7 = new javax.swing.JPanel();
        keltTol = new javax.swing.JTextField();
        keltTolCal = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        keltIg = new javax.swing.JTextField();
        keltIgCal = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        teljesitesIgCal = new javax.swing.JLabel();
        teljesitesIg = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        teljesitesTolCal = new javax.swing.JLabel();
        teljesitesTol = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        esedekessegIgCal = new javax.swing.JLabel();
        esedekessegIg = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        esedekessegTolCal = new javax.swing.JLabel();
        esedekessegTol = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        kifizetesIgCal = new javax.swing.JLabel();
        kifizetesIg = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        kifizetesTolCal = new javax.swing.JLabel();
        kifizetesTol = new javax.swing.JTextField();
        DetailedSearchButton = new javax.swing.JButton();
        DetailedSearchToDefaultButton = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        keszpenzCheckBox = new javax.swing.JCheckBox();
        atutalasCheckBox = new javax.swing.JCheckBox();
        utanvetCheckBox = new javax.swing.JCheckBox();
        sorszamozas = new javax.swing.JComboBox();
        kereses = new javax.swing.JTextField();
        szamlaScrollPane = new javax.swing.JScrollPane();
        table_Invoices = new javax.swing.JTable();
        sumScrollPane = new javax.swing.JScrollPane();
        sumTable = new javax.swing.JTable();
        nemNyomtatottCheckBox = new javax.swing.JCheckBox();
        jPanel1 = new javax.swing.JPanel();
        ReszletesKereses = new javax.swing.JLabel();
        EasySearchComboBox = new javax.swing.JComboBox();
        comboBox_Sort = new javax.swing.JComboBox<>();

        szamlaPopupMenu.setName("szamlaPopupMenu"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(cezeszamlazo.App.class).getContext().getResourceMap(SzamlakFrame.class);
        masolatMenuItem.setText(resourceMap.getString("masolatMenuItem.text")); // NOI18N
        masolatMenuItem.setName("masolatMenuItem"); // NOI18N
        masolatMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                masolatMenuItemActionPerformed(evt);
            }
        });
        szamlaPopupMenu.add(masolatMenuItem);

        jSeparator1.setName("jSeparator1"); // NOI18N
        szamlaPopupMenu.add(jSeparator1);

        modositoMenuItem.setText(resourceMap.getString("modositoMenuItem.text")); // NOI18N
        modositoMenuItem.setEnabled(false);
        modositoMenuItem.setName("modositoMenuItem"); // NOI18N
        modositoMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modositoMenuItemActionPerformed(evt);
            }
        });
        szamlaPopupMenu.add(modositoMenuItem);

        helyesbitoMenuItem.setText(resourceMap.getString("helyesbitoMenuItem.text")); // NOI18N
        helyesbitoMenuItem.setEnabled(false);
        helyesbitoMenuItem.setName("helyesbitoMenuItem"); // NOI18N
        helyesbitoMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helyesbitoMenuItemActionPerformed(evt);
            }
        });
        szamlaPopupMenu.add(helyesbitoMenuItem);

        stornoMenuItem.setText(resourceMap.getString("stornoMenuItem.text")); // NOI18N
        stornoMenuItem.setName("stornoMenuItem"); // NOI18N
        stornoMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stornoMenuItemActionPerformed(evt);
            }
        });
        szamlaPopupMenu.add(stornoMenuItem);

        jSeparator2.setName("jSeparator2"); // NOI18N
        szamlaPopupMenu.add(jSeparator2);

        statusRefreshMenuItem.setText(resourceMap.getString("statusRefreshMenuItem.text")); // NOI18N
        statusRefreshMenuItem.setName("statusRefreshMenuItem"); // NOI18N
        statusRefreshMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statusRefreshMenuItemActionPerformed(evt);
            }
        });
        szamlaPopupMenu.add(statusRefreshMenuItem);

        kifizetesMenuItem.setText(resourceMap.getString("kifizetesMenuItem.text")); // NOI18N
        kifizetesMenuItem.setName("kifizetesMenuItem"); // NOI18N
        kifizetesMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kifizetesMenuItemActionPerformed(evt);
            }
        });
        szamlaPopupMenu.add(kifizetesMenuItem);

        nyomtatasMenuItem.setText(resourceMap.getString("nyomtatasMenuItem.text")); // NOI18N
        nyomtatasMenuItem.setName("nyomtatasMenuItem"); // NOI18N
        nyomtatasMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nyomtatasMenuItemActionPerformed(evt);
            }
        });
        szamlaPopupMenu.add(nyomtatasMenuItem);

        menuItem_GLS.setText(resourceMap.getString("menuItem_GLS.text")); // NOI18N
        menuItem_GLS.setName("menuItem_GLS"); // NOI18N
        menuItem_GLS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItem_GLSActionPerformed(evt);
            }
        });
        szamlaPopupMenu.add(menuItem_GLS);

        szamlaKuldesEmailMenuItem.setText(resourceMap.getString("szamlaKuldesEmailMenuItem.text")); // NOI18N
        szamlaKuldesEmailMenuItem.setName("szamlaKuldesEmailMenuItem"); // NOI18N
        szamlaKuldesEmailMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                szamlaKuldesEmailMenuItemActionPerformed(evt);
            }
        });
        szamlaPopupMenu.add(szamlaKuldesEmailMenuItem);

        jSeparator3.setName("jSeparator3"); // NOI18N
        szamlaPopupMenu.add(jSeparator3);

        pdfKeszitesMenuItem.setText(resourceMap.getString("pdfKeszitesMenuItem.text")); // NOI18N
        pdfKeszitesMenuItem.setName("pdfKeszitesMenuItem"); // NOI18N
        pdfKeszitesMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pdfKeszitesMenuItemActionPerformed(evt);
            }
        });
        szamlaPopupMenu.add(pdfKeszitesMenuItem);

        csvKeszitesEgyszeruMenuItem.setText(resourceMap.getString("csvKeszitesEgyszeruMenuItem.text")); // NOI18N
        csvKeszitesEgyszeruMenuItem.setName("csvKeszitesEgyszeruMenuItem"); // NOI18N
        csvKeszitesEgyszeruMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                csvKeszitesEgyszeruMenuItemActionPerformed(evt);
            }
        });
        szamlaPopupMenu.add(csvKeszitesEgyszeruMenuItem);

        csvKeszitesReszletesMenuItem.setText(resourceMap.getString("csvKeszitesReszletesMenuItem.text")); // NOI18N
        csvKeszitesReszletesMenuItem.setName("csvKeszitesReszletesMenuItem"); // NOI18N
        csvKeszitesReszletesMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                csvKeszitesReszletesMenuItemActionPerformed(evt);
            }
        });
        szamlaPopupMenu.add(csvKeszitesReszletesMenuItem);

        kijeloltekTermekdijKimutatas.setText(resourceMap.getString("kijeloltekTermekdijKimutatas.text")); // NOI18N
        kijeloltekTermekdijKimutatas.setName("kijeloltekTermekdijKimutatas"); // NOI18N
        kijeloltekTermekdijKimutatas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kijeloltekTermekdijKimutatasActionPerformed(evt);
            }
        });
        szamlaPopupMenu.add(kijeloltekTermekdijKimutatas);

        jSeparator4.setName("jSeparator4"); // NOI18N
        szamlaPopupMenu.add(jSeparator4);

        kintlevosegLevelEmailMenuItem.setText(resourceMap.getString("kintlevosegLevelEmailMenuItem.text")); // NOI18N
        kintlevosegLevelEmailMenuItem.setName("kintlevosegLevelEmailMenuItem"); // NOI18N
        kintlevosegLevelEmailMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kintlevosegLevelEmailMenuItemActionPerformed(evt);
            }
        });
        szamlaPopupMenu.add(kintlevosegLevelEmailMenuItem);

        kintlevosegLevelPDFMenuItem.setText(resourceMap.getString("kintlevosegLevelPDFMenuItem.text")); // NOI18N
        kintlevosegLevelPDFMenuItem.setName("kintlevosegLevelPDFMenuItem"); // NOI18N
        kintlevosegLevelPDFMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kintlevosegLevelPDFMenuItemActionPerformed(evt);
            }
        });
        szamlaPopupMenu.add(kintlevosegLevelPDFMenuItem);

        teljesitesIgazolasNyomtatasaMenuItem.setText(resourceMap.getString("teljesitesIgazolasNyomtatasaMenuItem.text")); // NOI18N
        teljesitesIgazolasNyomtatasaMenuItem.setName("teljesitesIgazolasNyomtatasaMenuItem"); // NOI18N
        teljesitesIgazolasNyomtatasaMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                teljesitesIgazolasNyomtatasaMenuItemActionPerformed(evt);
            }
        });
        szamlaPopupMenu.add(teljesitesIgazolasNyomtatasaMenuItem);

        nyomtatasDialog.setModal(true);
        nyomtatasDialog.setName("nyomtatasDialog"); // NOI18N

        nyomtatasLabel.setFont(resourceMap.getFont("nyomtatasLabel.font")); // NOI18N
        nyomtatasLabel.setText(resourceMap.getString("nyomtatasLabel.text")); // NOI18N
        nyomtatasLabel.setName("nyomtatasLabel"); // NOI18N

        nyomtatasPeldany.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        nyomtatasPeldany.setText(resourceMap.getString("nyomtatasPeldany.text")); // NOI18N
        nyomtatasPeldany.setName("nyomtatasPeldany"); // NOI18N

        jLabel3.setFont(resourceMap.getFont("jLabel3.font")); // NOI18N
        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        jPanel2.setBackground(resourceMap.getColor("jPanel2.background")); // NOI18N
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(resourceMap.getColor("jPanel2.border.lineColor"))); // NOI18N
        jPanel2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel2.setName("jPanel2"); // NOI18N
        jPanel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel2MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanel2MouseExited(evt);
            }
        });

        jLabel4.setIcon(resourceMap.getIcon("jLabel4.icon")); // NOI18N
        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
        );

        printingPanel.setBorder(javax.swing.BorderFactory.createLineBorder(null));
        printingPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        printingPanel.setName("printingPanel"); // NOI18N
        printingPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                printingPanelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                printingPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                printingPanelMouseExited(evt);
            }
        });

        jLabel5.setIcon(resourceMap.getIcon("jLabel5.icon")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        javax.swing.GroupLayout printingPanelLayout = new javax.swing.GroupLayout(printingPanel);
        printingPanel.setLayout(printingPanelLayout);
        printingPanelLayout.setHorizontalGroup(
            printingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(printingPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        printingPanelLayout.setVerticalGroup(
            printingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(resourceMap.getColor("jPanel4.border.lineColor"))); // NOI18N
        jPanel4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel4.setName("jPanel4"); // NOI18N
        jPanel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel4MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel4MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanel4MouseExited(evt);
            }
        });

        jLabel6.setIcon(resourceMap.getIcon("jLabel6.icon")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
        );

        printerComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        printerComboBox.setName("printerComboBox"); // NOI18N
        printerComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                printerComboBoxItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout nyomtatasDialogLayout = new javax.swing.GroupLayout(nyomtatasDialog.getContentPane());
        nyomtatasDialog.getContentPane().setLayout(nyomtatasDialogLayout);
        nyomtatasDialogLayout.setHorizontalGroup(
            nyomtatasDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(nyomtatasDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(nyomtatasDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(nyomtatasDialogLayout.createSequentialGroup()
                        .addComponent(nyomtatasLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nyomtatasPeldany, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, nyomtatasDialogLayout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(printerComboBox, 0, 222, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(printingPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        nyomtatasDialogLayout.setVerticalGroup(
            nyomtatasDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(nyomtatasDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(nyomtatasDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nyomtatasLabel)
                    .addComponent(nyomtatasPeldany, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(nyomtatasDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(printingPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(printerComboBox))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        reszletesKeresesDialog.setName("reszletesKeresesDialog"); // NOI18N

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(resourceMap.getColor("jPanel5.border.border.lineColor")), resourceMap.getString("jPanel5.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, resourceMap.getFont("jPanel5.border.titleFont"))); // NOI18N
        jPanel5.setName("jPanel5"); // NOI18N
        jPanel5.setPreferredSize(new java.awt.Dimension(575, 146));

        normal.setSelected(true);
        normal.setText(resourceMap.getString("normal.text")); // NOI18N
        normal.setName("normal"); // NOI18N

        helyesbito.setSelected(true);
        helyesbito.setText(resourceMap.getString("helyesbito.text")); // NOI18N
        helyesbito.setName("helyesbito"); // NOI18N

        helyesbitett.setSelected(true);
        helyesbitett.setText(resourceMap.getString("helyesbitett.text")); // NOI18N
        helyesbitett.setName("helyesbitett"); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(normal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(helyesbito)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(helyesbitett)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(normal)
                    .addComponent(helyesbito)
                    .addComponent(helyesbitett))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(resourceMap.getColor("jPanel7.border.border.lineColor")), resourceMap.getString("jPanel7.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, resourceMap.getFont("jPanel7.border.titleFont"))); // NOI18N
        jPanel7.setName("jPanel7"); // NOI18N
        jPanel7.setPreferredSize(new java.awt.Dimension(575, 146));

        keltTol.setBackground(resourceMap.getColor("keltTol.background")); // NOI18N
        keltTol.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        keltTol.setText(resourceMap.getString("keltTol.text")); // NOI18N
        keltTol.setName("keltTol"); // NOI18N

        keltTolCal.setIcon(resourceMap.getIcon("keltTolCal.icon")); // NOI18N
        keltTolCal.setText(resourceMap.getString("keltTolCal.text")); // NOI18N
        keltTolCal.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        keltTolCal.setName("keltTolCal"); // NOI18N
        keltTolCal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                keltTolCalMouseClicked(evt);
            }
        });

        jLabel7.setIcon(resourceMap.getIcon("jLabel7.icon")); // NOI18N
        jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        keltIg.setEditable(false);
        keltIg.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        keltIg.setText(resourceMap.getString("keltIg.text")); // NOI18N
        keltIg.setName("keltIg"); // NOI18N

        keltIgCal.setIcon(resourceMap.getIcon("keltIgCal.icon")); // NOI18N
        keltIgCal.setText(resourceMap.getString("keltIgCal.text")); // NOI18N
        keltIgCal.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        keltIgCal.setName("keltIgCal"); // NOI18N
        keltIgCal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                keltIgCalMouseClicked(evt);
            }
        });

        jLabel9.setText(resourceMap.getString("jLabel9.text")); // NOI18N
        jLabel9.setName("jLabel9"); // NOI18N

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(keltTol, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(keltTolCal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(keltIg, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(keltIgCal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addContainerGap(40, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(keltIg, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(keltIgCal, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(keltTol, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(keltTolCal, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel7)))
                .addContainerGap())
        );

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(resourceMap.getColor("jPanel8.border.border.lineColor")), resourceMap.getString("jPanel8.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, resourceMap.getFont("jPanel8.border.titleFont"))); // NOI18N
        jPanel8.setName("jPanel8"); // NOI18N
        jPanel8.setPreferredSize(new java.awt.Dimension(575, 146));

        jLabel10.setText(resourceMap.getString("jLabel10.text")); // NOI18N
        jLabel10.setName("jLabel10"); // NOI18N

        teljesitesIgCal.setIcon(resourceMap.getIcon("teljesitesIgCal.icon")); // NOI18N
        teljesitesIgCal.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        teljesitesIgCal.setName("teljesitesIgCal"); // NOI18N
        teljesitesIgCal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                teljesitesIgCalMouseClicked(evt);
            }
        });

        teljesitesIg.setEditable(false);
        teljesitesIg.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        teljesitesIg.setName("teljesitesIg"); // NOI18N

        jLabel12.setText(resourceMap.getString("jLabel12.text")); // NOI18N
        jLabel12.setName("jLabel12"); // NOI18N

        teljesitesTolCal.setIcon(resourceMap.getIcon("teljesitesTolCal.icon")); // NOI18N
        teljesitesTolCal.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        teljesitesTolCal.setName("teljesitesTolCal"); // NOI18N
        teljesitesTolCal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                teljesitesTolCalMouseClicked(evt);
            }
        });

        teljesitesTol.setEditable(false);
        teljesitesTol.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        teljesitesTol.setText(resourceMap.getString("teljesitesTol.text")); // NOI18N
        teljesitesTol.setEnabled(true);
        teljesitesTol.setName("teljesitesTol"); // NOI18N

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(teljesitesTol, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(teljesitesTolCal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(teljesitesIg, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(teljesitesIgCal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addContainerGap(36, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(teljesitesIg, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(teljesitesIgCal, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(teljesitesTol, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(teljesitesTolCal, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel12)))
                .addContainerGap())
        );

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(resourceMap.getColor("jPanel9.border.border.lineColor")), resourceMap.getString("jPanel9.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, resourceMap.getFont("jPanel9.border.titleFont"))); // NOI18N
        jPanel9.setName("jPanel9"); // NOI18N
        jPanel9.setPreferredSize(new java.awt.Dimension(575, 146));

        jLabel14.setText(resourceMap.getString("jLabel14.text")); // NOI18N
        jLabel14.setName("jLabel14"); // NOI18N

        esedekessegIgCal.setIcon(resourceMap.getIcon("esedekessegIgCal.icon")); // NOI18N
        esedekessegIgCal.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        esedekessegIgCal.setName("esedekessegIgCal"); // NOI18N
        esedekessegIgCal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                esedekessegIgCalMouseClicked(evt);
            }
        });

        esedekessegIg.setEditable(false);
        esedekessegIg.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        esedekessegIg.setName("esedekessegIg"); // NOI18N

        jLabel16.setText(resourceMap.getString("jLabel16.text")); // NOI18N
        jLabel16.setName("jLabel16"); // NOI18N

        esedekessegTolCal.setIcon(resourceMap.getIcon("esedekessegTolCal.icon")); // NOI18N
        esedekessegTolCal.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        esedekessegTolCal.setName("esedekessegTolCal"); // NOI18N
        esedekessegTolCal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                esedekessegTolCalMouseClicked(evt);
            }
        });

        esedekessegTol.setEditable(false);
        esedekessegTol.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        esedekessegTol.setText(resourceMap.getString("esedekessegTol.text")); // NOI18N
        esedekessegTol.setName("esedekessegTol"); // NOI18N

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(esedekessegTol, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(esedekessegTolCal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(esedekessegIg, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(esedekessegIgCal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14)
                .addContainerGap(17, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(esedekessegIg, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(esedekessegIgCal, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(esedekessegTol, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(esedekessegTolCal, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel16)))
                .addContainerGap())
        );

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(resourceMap.getColor("jPanel10.border.border.lineColor")), resourceMap.getString("jPanel10.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, resourceMap.getFont("jPanel10.border.titleFont"))); // NOI18N
        jPanel10.setName("jPanel10"); // NOI18N
        jPanel10.setPreferredSize(new java.awt.Dimension(575, 146));

        jLabel18.setText(resourceMap.getString("jLabel18.text")); // NOI18N
        jLabel18.setName("jLabel18"); // NOI18N

        kifizetesIgCal.setIcon(resourceMap.getIcon("kifizetesIgCal.icon")); // NOI18N
        kifizetesIgCal.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        kifizetesIgCal.setName("kifizetesIgCal"); // NOI18N
        kifizetesIgCal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                kifizetesIgCalMouseClicked(evt);
            }
        });

        kifizetesIg.setEditable(false);
        kifizetesIg.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        kifizetesIg.setName("kifizetesIg"); // NOI18N

        jLabel20.setText(resourceMap.getString("jLabel20.text")); // NOI18N
        jLabel20.setName("jLabel20"); // NOI18N

        kifizetesTolCal.setIcon(resourceMap.getIcon("kifizetesTolCal.icon")); // NOI18N
        kifizetesTolCal.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        kifizetesTolCal.setName("kifizetesTolCal"); // NOI18N
        kifizetesTolCal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                kifizetesTolCalMouseClicked(evt);
            }
        });

        kifizetesTol.setEditable(false);
        kifizetesTol.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        kifizetesTol.setText(resourceMap.getString("kifizetesTol.text")); // NOI18N
        kifizetesTol.setName("kifizetesTol"); // NOI18N

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(kifizetesTol, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(kifizetesTolCal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(kifizetesIg, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(kifizetesIgCal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel18)
                .addContainerGap(40, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(kifizetesIg, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(kifizetesIgCal, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(kifizetesTol, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(kifizetesTolCal, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel20)))
                .addContainerGap())
        );

        DetailedSearchButton.setIcon(resourceMap.getIcon("DetailedSearchButton.icon")); // NOI18N
        DetailedSearchButton.setText(resourceMap.getString("DetailedSearchButton.text")); // NOI18N
        DetailedSearchButton.setName("DetailedSearchButton"); // NOI18N
        DetailedSearchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DetailedSearchButtonActionPerformed(evt);
            }
        });

        DetailedSearchToDefaultButton.setText(resourceMap.getString("DetailedSearchToDefaultButton.text")); // NOI18N
        DetailedSearchToDefaultButton.setName("DetailedSearchToDefaultButton"); // NOI18N
        DetailedSearchToDefaultButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DetailedSearchToDefaultButtonActionPerformed(evt);
            }
        });

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(resourceMap.getColor("jPanel11.border.border.lineColor")), resourceMap.getString("jPanel11.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, resourceMap.getFont("jPanel11.border.titleFont"))); // NOI18N
        jPanel11.setName("jPanel11"); // NOI18N
        jPanel11.setPreferredSize(new java.awt.Dimension(575, 146));

        keszpenzCheckBox.setSelected(true);
        keszpenzCheckBox.setText(resourceMap.getString("keszpenzCheckBox.text")); // NOI18N
        keszpenzCheckBox.setName("keszpenzCheckBox"); // NOI18N

        atutalasCheckBox.setSelected(true);
        atutalasCheckBox.setText(resourceMap.getString("atutalasCheckBox.text")); // NOI18N
        atutalasCheckBox.setName("atutalasCheckBox"); // NOI18N

        utanvetCheckBox.setSelected(true);
        utanvetCheckBox.setText(resourceMap.getString("utanvetCheckBox.text")); // NOI18N
        utanvetCheckBox.setName("utanvetCheckBox"); // NOI18N

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(keszpenzCheckBox)
                .addGap(18, 18, 18)
                .addComponent(atutalasCheckBox)
                .addGap(18, 18, 18)
                .addComponent(utanvetCheckBox)
                .addContainerGap(138, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(keszpenzCheckBox)
                    .addComponent(atutalasCheckBox)
                    .addComponent(utanvetCheckBox))
                .addContainerGap(8, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout reszletesKeresesDialogLayout = new javax.swing.GroupLayout(reszletesKeresesDialog.getContentPane());
        reszletesKeresesDialog.getContentPane().setLayout(reszletesKeresesDialogLayout);
        reszletesKeresesDialogLayout.setHorizontalGroup(
            reszletesKeresesDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, reszletesKeresesDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(reszletesKeresesDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 387, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 387, Short.MAX_VALUE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 387, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, 387, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 387, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, 387, Short.MAX_VALUE)
                    .addGroup(reszletesKeresesDialogLayout.createSequentialGroup()
                        .addComponent(DetailedSearchToDefaultButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(DetailedSearchButton)))
                .addContainerGap())
        );
        reszletesKeresesDialogLayout.setVerticalGroup(
            reszletesKeresesDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(reszletesKeresesDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(reszletesKeresesDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(DetailedSearchButton)
                    .addComponent(DetailedSearchToDefaultButton))
                .addContainerGap())
        );

        jPanel7.getAccessibleContext().setAccessibleName(resourceMap.getString("jPanel7.AccessibleContext.accessibleName")); // NOI18N

        setName("Form"); // NOI18N

        sorszamozas.setFont(resourceMap.getFont("sorszamozas.font")); // NOI18N
        sorszamozas.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "sorszámozás" }));
        sorszamozas.setName("sorszamozas"); // NOI18N
        sorszamozas.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                sorszamozasItemStateChanged(evt);
            }
        });

        kereses.setName("kereses"); // NOI18N
        kereses.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                keresesKeyReleased(evt);
            }
        });

        szamlaScrollPane.setName("szamlaScrollPane"); // NOI18N

        table_Invoices.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sorszám", "Vevő", "Kelt", "Nettó", "Áfa érték", "Bruttó", "Fizetendő", "Fennmaradó", "Esedékesség", "Teljesítés", "Kifizetés", "Késedelem", "Becsült kamat", "Fizetési mód", "Megjegyzés", "Csoport", "Valuta", "Középárfolyam", "Azonosító", "Teljesítés igazolás sorszáma", "Teljesítés igazolás kelte", "NAV státusz"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, true, false, false, false, false, false, false, false, false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table_Invoices.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        table_Invoices.setName("table_Invoices"); // NOI18N
        table_Invoices.setRowHeight(20);
        table_Invoices.getTableHeader().setReorderingAllowed(false);
        table_Invoices.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                table_InvoicesMouseDragged(evt);
            }
        });
        table_Invoices.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                table_InvoicesMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                table_InvoicesMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                table_InvoicesMouseReleased(evt);
            }
        });
        table_Invoices.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                table_InvoicesComponentResized(evt);
            }
        });
        table_Invoices.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                table_InvoicesKeyReleased(evt);
            }
        });
        szamlaScrollPane.setViewportView(table_Invoices);

        sumScrollPane.setName("sumScrollPane"); // NOI18N

        sumTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        sumTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        sumTable.setName("sumTable"); // NOI18N
        sumTable.setRowHeight(20);
        sumTable.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                sumTableComponentResized(evt);
            }
        });
        sumScrollPane.setViewportView(sumTable);

        nemNyomtatottCheckBox.setText(resourceMap.getString("nemNyomtatottCheckBox.text")); // NOI18N
        nemNyomtatottCheckBox.setName("nemNyomtatottCheckBox"); // NOI18N
        nemNyomtatottCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nemNyomtatottCheckBoxActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(resourceMap.getColor("jPanel1.border.lineColor"))); // NOI18N
        jPanel1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanel1MouseExited(evt);
            }
        });

        ReszletesKereses.setFont(resourceMap.getFont("ReszletesKereses.font")); // NOI18N
        ReszletesKereses.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ReszletesKereses.setIcon(resourceMap.getIcon("ReszletesKereses.icon")); // NOI18N
        ReszletesKereses.setText(resourceMap.getString("ReszletesKereses.text")); // NOI18N
        ReszletesKereses.setName("ReszletesKereses"); // NOI18N
        ReszletesKereses.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ReszletesKeresesMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ReszletesKeresesMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ReszletesKeresesMouseExited(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(ReszletesKereses, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ReszletesKereses, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        EasySearchComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Összes", "Kintlévőség", "Határidőn túli kintlévőség" }));
        EasySearchComboBox.setName("EasySearchComboBox"); // NOI18N
        EasySearchComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                EasySearchComboBoxItemStateChanged(evt);
            }
        });

        comboBox_Sort.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboBox_Sort.setName("comboBox_Sort"); // NOI18N
        comboBox_Sort.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboBox_SortItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(sumScrollPane, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(szamlaScrollPane, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(kereses, javax.swing.GroupLayout.DEFAULT_SIZE, 423, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(nemNyomtatottCheckBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(comboBox_Sort, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(EasySearchComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sorszamozas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jPanel1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(sorszamozas)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(kereses, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(EasySearchComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(comboBox_Sort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(nemNyomtatottCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(szamlaScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 369, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sumScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void table_InvoicesComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_table_InvoicesComponentResized
        if (b)
        {
            String width = "";
            int count = table_Invoices.getColumnCount();
            
            for (int i = 0; i < count; i++)
            {
                int w = table_Invoices.getColumnModel().getColumn(i).getWidth();
                sumTable.getColumnModel().getColumn(i).setPreferredWidth(w);
                width += w + "";
                
                if (i < count - 1)
                {
                    width += ";";
                }
            }
            
            Settings.set("szamlaTableHeader", width);
        }
    }//GEN-LAST:event_table_InvoicesComponentResized

    private void sumTableComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_sumTableComponentResized
        if (b)
        {
            for (int i = 0; i < sumTable.getColumnCount(); i++)
            {
                try
                {
                    table_Invoices.getColumnModel().getColumn(i).setPreferredWidth(sumTable.getColumnModel().getColumn(i).getWidth());
                }
                catch(ArrayIndexOutOfBoundsException e)
                {
                    break;
                }
            }
        }
    }//GEN-LAST:event_sumTableComponentResized

    private void keresesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_keresesKeyReleased
        keresesFrissites();
        //frissites();
    }//GEN-LAST:event_keresesKeyReleased

    private void keresesFrissites()
    {
        if(frissitesThread.isAlive())
        {
            frissitesThread.setCount();
        }
        else
        {
            frissites();
        }
    }
    
    private void nemNyomtatottCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nemNyomtatottCheckBoxActionPerformed
        frissites();
    }//GEN-LAST:event_nemNyomtatottCheckBoxActionPerformed

    private void ShowPopupMenu(MouseEvent evt)
    {
        JTable source = (JTable) evt.getSource();
        int row = source.rowAtPoint(evt.getPoint());
        int column = source.columnAtPoint(evt.getPoint());

        if (!source.isRowSelected(row))
        {
            source.changeSelection(row, column, false, false);
        }

        int[] rows = source.getSelectedRows();
        Component[] comps = szamlaPopupMenu.getComponents();

        if (rows.length > 1)
        {
            for (Component c : comps)
            {
                c.setVisible(false);
            }
        }
        else
        {
            for (Component c : comps)
            {
                c.setVisible(true);
            }
        }

        csvKeszitesEgyszeruMenuItem.setVisible(true);
        csvKeszitesReszletesMenuItem.setVisible(true);
        kifizetesMenuItem.setVisible(true);
        kijeloltekTermekdijKimutatas.setVisible(true);

        kintlevosegLevelEmailMenuItem.setVisible(true);
        kintlevosegLevelPDFMenuItem.setVisible(true);
        szamlaKuldesEmailMenuItem.setVisible(true);
        //kintlevosegMenuEmail.setVisible(true);
        //kintlevosegMenuPDF.setVisible(true);
        nyomtatasMenuItem.setVisible(true);

        boolean glsAvailable = GLSAvailable();
        menuItem_GLS.setVisible(glsAvailable);
        
        teljesitesIgazolasNyomtatasaMenuItem.setVisible(true);

        szamlaPopupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
    }
    
    private void table_InvoicesMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_InvoicesMouseReleased
        if (evt.isPopupTrigger())
        {
            ShowPopupMenu(evt);
        }
        else if(evt.getButton() == MouseEvent.BUTTON1 && KeyPressed.Pressed())
        {
            System.err.println("Mac OS jobb klikk");
            ShowPopupMenu(evt);
        }
    }//GEN-LAST:event_table_InvoicesMouseReleased

    private void masolatMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_masolatMenuItemActionPerformed
        int row = table_Invoices.getSelectedRow();

        String indentifier = String.valueOf(table_Invoices.getValueAt(row, 18));
        Invoice invoice = new Invoice(Invoice.INVOICE, Invoice.InvoiceType.COPY, "szamlazo_invoices", indentifier);
        
        ResizeableInvoiceView view = new ResizeableInvoiceView(invoice);
        
        if(view.getReturnStatus() != ResizeableInvoiceView.RET_CANCEL)
        {
            frissites();
        }
    }//GEN-LAST:event_masolatMenuItemActionPerformed

    private void jPanel1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseEntered
        jPanel1.setBackground(Color.decode("#abd043"));
    }//GEN-LAST:event_jPanel1MouseEntered

    private void jPanel1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseExited
        jPanel1.setBackground(Color.decode("#f0f0f0"));
    }//GEN-LAST:event_jPanel1MouseExited

    private void table_InvoicesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_InvoicesMouseClicked
        int row = table_Invoices.getSelectedRow();
        
        if (evt.getClickCount() == 2 && evt.getButton() == MouseEvent.BUTTON1)
        {
            folyamatbanDialog = new FolyamatbanDialog("Számla adatok betöltése...");
            SzamlaAdatokThread sz = new SzamlaAdatokThread(row);
            folyamatbanDialog.setVisible(true);
        }
        
        osszesit();
    }//GEN-LAST:event_table_InvoicesMouseClicked

    private void table_InvoicesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_table_InvoicesKeyReleased
        osszesit();
    }//GEN-LAST:event_table_InvoicesKeyReleased

    private void table_InvoicesMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_InvoicesMouseDragged
        osszesit();
    }//GEN-LAST:event_table_InvoicesMouseDragged

    private void kifizetesMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kifizetesMenuItemActionPerformed
        int[] rows = table_Invoices.getSelectedRows();
        
        if (rows.length == 1)
        {
            SzamlaKifizetesDialog k = new SzamlaKifizetesDialog
            (
                String.valueOf(table_Invoices.getValueAt(rows[0], 0)),
                String.valueOf(table_Invoices.getValueAt(rows[0], 18)),
                String.valueOf(table_Invoices.getValueAt(rows[0], 7)),
                Invoice.INVOICE
            );
            
            if (k.getReturnStatus() == 1)
            {
                frissites();
            }
        }
        else
        {
            String[][] ids = new String[rows.length][3];
            
            for (int i = 0; i < rows.length; i++)
            {
                ids[i][0] = String.valueOf(table_Invoices.getValueAt(rows[i], 0));
                ids[i][1] = String.valueOf(table_Invoices.getValueAt(rows[i], 18));
                ids[i][2] = String.valueOf(table_Invoices.getValueAt(rows[i], 7));
            }
            
            SzamlaKifizetesDialog k = new SzamlaKifizetesDialog(ids, Invoice.INVOICE);
            
            if (k.getReturnStatus() == 1)
            {
                frissites();
            }
        }
    }//GEN-LAST:event_kifizetesMenuItemActionPerformed

    private void nyomtatasMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nyomtatasMenuItemActionPerformed
        int[] rows = table_Invoices.getSelectedRows();
       
        if (rows.length != 0)
        {
            for (int i = 0; i < rows.length; i++)
            {
                nyomtatasAzon = String.valueOf(table_Invoices.getValueAt(rows[i], 18));
                
                nyomtatasLabel.setText("A(z) " + String.valueOf(table_Invoices.getValueAt(rows[i], 0)) + " számla nyomtatása");
                nyit(nyomtatasDialog, "Nyomtatás");
            }
        }
    }//GEN-LAST:event_nyomtatasMenuItemActionPerformed

    private void jPanel2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel2MouseEntered
        jPanel2.setBackground(Color.decode("#abd043"));
    }//GEN-LAST:event_jPanel2MouseEntered

    private void jPanel2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel2MouseExited
        jPanel2.setBackground(Color.decode("#f0f0f0"));
    }//GEN-LAST:event_jPanel2MouseExited

    private void printingPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_printingPanelMouseEntered
        printingPanel.setBackground(Color.decode("#abd043"));
    }//GEN-LAST:event_printingPanelMouseEntered

    private void printingPanelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_printingPanelMouseExited
        printingPanel.setBackground(Color.decode("#f0f0f0"));
    }//GEN-LAST:event_printingPanelMouseExited

    private void jPanel4MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel4MouseEntered
        jPanel4.setBackground(Color.decode("#d24343"));
    }//GEN-LAST:event_jPanel4MouseEntered

    private void jPanel4MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel4MouseExited
        jPanel4.setBackground(Color.decode("#f0f0f0"));
    }//GEN-LAST:event_jPanel4MouseExited

    private void jPanel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel2MouseClicked
        folyamatbanDialog = new FolyamatbanDialog("Előnézet generálás folyamatban...");
        ElonezetThread e = new ElonezetThread();
        folyamatbanDialog.setVisible(true);
    }//GEN-LAST:event_jPanel2MouseClicked

    private void jPanel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel4MouseClicked
        nyomtatasDialog.setVisible(false);
    }//GEN-LAST:event_jPanel4MouseClicked

    private void printingPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_printingPanelMouseClicked
        Invoice invoice = new Invoice(Invoice.INVOICE, Invoice.InvoiceType.ORIGINAL, "szamlazo_invoices", nyomtatasAzon);
        
        ElonezetDialog e = new ElonezetDialog(invoice, Integer.parseInt(nyomtatasPeldany.getText()), ElonezetDialog.NYOMTATAS);
        App.db.insert("UPDATE szamlazo_szamla SET nyomtatva = 1 WHERE azon = '" + nyomtatasAzon + "'", null);
        nyomtatasDialog.setVisible(false);
    }//GEN-LAST:event_printingPanelMouseClicked

    private void csvKeszitesReszletesMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_csvKeszitesReszletesMenuItemActionPerformed
        File file = new File("reszletes_export.csv");
        
        try
        {
            JFileChooser chooser = new JFileChooser();
            chooser.setSelectedFile(file);
            chooser.showOpenDialog(null);
            File curFile = chooser.getSelectedFile();
            kijeloltCsvKeszitese(curFile, true);
        }
        catch (Exception ex)
        {
            HibaDialog h = new HibaDialog("Sikertelen mentés!", "Ok", "");
        }
    }//GEN-LAST:event_csvKeszitesReszletesMenuItemActionPerformed

    private void helyesbitoMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helyesbitoMenuItemActionPerformed
        /*int row = szamlakTable.getSelectedRow();
        String azon = String.valueOf(szamlakTable.getValueAt(row, 18));
        Szamla szml = new Szamla(azon);
        SzamlaDialogOld sz = SzamlaDialogOld.createModositoSzamla(szml);
        //SzamlaDialogOld sz = new SzamlaDialogOld(String.valueOf(szamlakTable.getValueAt(row, 18)), true);
        if (sz.getReturnStatus() == 1)
        {
            frissites();
        }*/
        
        int row = table_Invoices.getSelectedRow();
        String indentifier = String.valueOf(table_Invoices.getValueAt(row, 18));
        String originalInvoiceNumber = String.valueOf(table_Invoices.getValueAt(row, 0));
        Invoice invoice = new Invoice(Invoice.INVOICE, Invoice.InvoiceType.CORRECTION, "szamlazo_invoices", indentifier);
        invoice.setCorrectedInvoice(originalInvoiceNumber);
        
        //InvoiceView view = new InvoiceView(invoice);
        ResizeableInvoiceView view = new ResizeableInvoiceView(invoice);
        
        if(view.getReturnStatus() == ResizeableInvoiceView.RET_OK)
        {
            frissites();
        }
    }//GEN-LAST:event_helyesbitoMenuItemActionPerformed

    private void stornoMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stornoMenuItemActionPerformed
        int row = table_Invoices.getSelectedRow();
        String indentifier = String.valueOf(table_Invoices.getValueAt(row, 18));
        String originalInvoiceNumber = String.valueOf(table_Invoices.getValueAt(row, 0));
        
        Invoice invoice = new Invoice(Invoice.INVOICE, Invoice.InvoiceType.STORNO, "szamlazo_invoices", indentifier);
        invoice.setCorrectedInvoice(originalInvoiceNumber);

        ResizeableInvoiceView view = new ResizeableInvoiceView(invoice);
        
        if(view.getReturnStatus() == ResizeableInvoiceView.RET_OK)
        {
            frissites();
        }
    }//GEN-LAST:event_stornoMenuItemActionPerformed

    private void pdfKeszitesMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pdfKeszitesMenuItemActionPerformed
        int row = table_Invoices.getSelectedRow();
        folyamatbanDialog = new FolyamatbanDialog("Pdf generálás folyamatban...");
        PdfThread p = new PdfThread(row);
        folyamatbanDialog.setVisible(true);
    }//GEN-LAST:event_pdfKeszitesMenuItemActionPerformed

    private void kijeloltekTermekdijKimutatasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kijeloltekTermekdijKimutatasActionPerformed
        File file = new File("termekdij-kimutatas.csv");
        
        try
        {
            JFileChooser chooser = new JFileChooser();
            chooser.setSelectedFile(file);
            chooser.showOpenDialog(null);
            File curFile = chooser.getSelectedFile();
            kijeloltTermekdijKimutatasKeszitese(curFile);
        }
        catch (Exception ex)
        {
            HibaDialog h = new HibaDialog("Sikertelen mentés!", "Ok", "");
        }
    }//GEN-LAST:event_kijeloltekTermekdijKimutatasActionPerformed

    private void ReszletesKeresesMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ReszletesKeresesMouseExited
        ReszletesKereses.setBackground(Color.decode("#F0F0F0"));
    }//GEN-LAST:event_ReszletesKeresesMouseExited

    private void ReszletesKeresesMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ReszletesKeresesMouseEntered
        ReszletesKereses.setBackground(Color.decode("#ABD043"));
    }//GEN-LAST:event_ReszletesKeresesMouseEntered

    private void ReszletesKeresesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ReszletesKeresesMouseClicked
        nyit(reszletesKeresesDialog, "Részletes keresés");
    }//GEN-LAST:event_ReszletesKeresesMouseClicked

    private void keltTolCalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_keltTolCalMouseClicked
        CalendarDialog c = new CalendarDialog(this, keltTol);  
    }//GEN-LAST:event_keltTolCalMouseClicked

    private void keltIgCalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_keltIgCalMouseClicked
        CalendarDialog c = new CalendarDialog(this, keltIg);
    }//GEN-LAST:event_keltIgCalMouseClicked

    private void teljesitesTolCalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_teljesitesTolCalMouseClicked
        CalendarDialog c = new CalendarDialog(this, teljesitesTol);
    }//GEN-LAST:event_teljesitesTolCalMouseClicked

    private void teljesitesIgCalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_teljesitesIgCalMouseClicked
        CalendarDialog c = new CalendarDialog(this, teljesitesIg);
    }//GEN-LAST:event_teljesitesIgCalMouseClicked

    private void esedekessegTolCalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_esedekessegTolCalMouseClicked
        CalendarDialog c = new CalendarDialog(this, esedekessegTol);
    }//GEN-LAST:event_esedekessegTolCalMouseClicked

    private void esedekessegIgCalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_esedekessegIgCalMouseClicked
        CalendarDialog c = new CalendarDialog(this, esedekessegIg);
    }//GEN-LAST:event_esedekessegIgCalMouseClicked

    private void kifizetesTolCalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kifizetesTolCalMouseClicked
        CalendarDialog c = new CalendarDialog(this, kifizetesTol);
    }//GEN-LAST:event_kifizetesTolCalMouseClicked

    private void kifizetesIgCalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kifizetesIgCalMouseClicked
        CalendarDialog c = new CalendarDialog(this, kifizetesIg);
    }//GEN-LAST:event_kifizetesIgCalMouseClicked

    private void DetailedSearchToDefaultButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DetailedSearchToDefaultButtonActionPerformed
        keltTol.setText("");
        keltIg.setText("");
        teljesitesTol.setText("");
        teljesitesIg.setText("");
        esedekessegTol.setText("");
        esedekessegIg.setText("");
        kifizetesTol.setText("");
        kifizetesIg.setText("");
        normal.setSelected(false);
        helyesbito.setSelected(false);
        helyesbitett.setSelected(false);
        keszpenzCheckBox.setSelected(true);
        atutalasCheckBox.setSelected(true);
        utanvetCheckBox.setSelected(true);
    }//GEN-LAST:event_DetailedSearchToDefaultButtonActionPerformed

    private void DetailedSearchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DetailedSearchButtonActionPerformed
        //if (!kintlevosegCheckBoxInAdvancedSearch.isSelected() && !normal.isSelected() && !helyesbito.isSelected() && !helyesbitett.isSelected()) {
            //HibaDialog h = new HibaDialog("Legalább egy számla típus megadása kötelező!", "", "Ok");
        //} else
        
        if(!keszpenzCheckBox.isSelected() && !atutalasCheckBox.isSelected() && !utanvetCheckBox.isSelected())
        {
            HibaDialog h = new HibaDialog("Legalább egy számla kifizetés típus megadása kötelező!", "", "Ok");
        }
        else
        {
            reszletesKeresesDialog.setVisible(false);
            frissites();
        }
    }//GEN-LAST:event_DetailedSearchButtonActionPerformed

private void csvKeszitesEgyszeruMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_csvKeszitesEgyszeruMenuItemActionPerformed
    File file = new File("egyszeru_export.csv");
    
    try
    {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(file);
        chooser.showOpenDialog(null);
        File curFile = chooser.getSelectedFile();
        kijeloltCsvKeszitese(curFile, false);
    }
    catch (Exception ex)
    {
        HibaDialog h = new HibaDialog("Sikertelen mentés!", "Ok", "");
    }
}//GEN-LAST:event_csvKeszitesEgyszeruMenuItemActionPerformed

    private void teljesitesIgazolasNyomtatasaMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_teljesitesIgazolasNyomtatasaMenuItemActionPerformed
        int [] rows = table_Invoices.getSelectedRows();
        
        if(rows.length > 1)
        {
            System.err.println("Teljesítés igazolások nyomtatása");
            
            PrinterSelect select = new PrinterSelect();
            
            if(select.getReturnStatus() == PrinterSelect.RET_OK)
            {
                for(int i = 0; i < rows.length; i++)
                {
                    String indentifier = table_Invoices.getValueAt(rows[i], 18).toString();

                    Invoice invoice = new Invoice(Invoice.COMPLETION_CERTIFICATE, Invoice.InvoiceType.ORIGINAL, "szamlazo_invoices", indentifier);

                    ElonezetDialog e = new ElonezetDialog(invoice, 1, ElonezetDialog.NYOMTATAS);
                }
            }
        }
        else
        {
            System.err.println("Teljesítés igazolás nyomtatása");
            
            int row = table_Invoices.getSelectedRow();
            String indentifier = table_Invoices.getValueAt(row, 18).toString();
            String invoiceNumber = table_Invoices.getValueAt(row, 0).toString();

            Invoice invoice = new Invoice(Invoice.COMPLETION_CERTIFICATE, Invoice.InvoiceType.ORIGINAL, "szamlazo_invoices", indentifier);

            ResizeableInvoiceView view = new ResizeableInvoiceView(invoice);

            if(view.getReturnStatus() == ResizeableInvoiceView.RET_OK)
            {
                frissites();
            }
        }
    }//GEN-LAST:event_teljesitesIgazolasNyomtatasaMenuItemActionPerformed

    private void kintlevosegLevelPDFMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kintlevosegLevelPDFMenuItemActionPerformed
//        int[] rows = szamlakTable.getSelectedRows();
//        String[] azonositok = new String[rows.length];
////        String keresText = "azon IN (";
//        for (int i = 0; i < rows.length; i++) {
////            keresText += "'" + String.valueOf(szamlakTable.getValueAt(rows[i], 18)) + "', ";
//            azonositok[i] = String.valueOf(szamlakTable.getValueAt(rows[i], 18));
//        }
//        kintlevosegPDF(azonositok, true);
        isKintlevoseg = true;
        int[] rows = table_Invoices.getSelectedRows();
        String[] azonositok = new String[rows.length];
        String[] names = new String[rows.length];
        List<KintlevosegKapcsolattartokGroup> list = new ArrayList<>();

        for (int i = 0; i < rows.length; i++)
        {
            names[i] = Functions.getStringFromObject(table_Invoices.getValueAt(rows[i], 1));
            azonositok[i] = String.valueOf(table_Invoices.getValueAt(rows[i], 18));
        }

        int count = 0;
        
        for (int i = 0; i < azonositok.length; i++)
        {
            String azon = azonositok[i];
            String name = names[i];
            KintlevosegKapcsolattartokGroup group = KintlevosegKapcsolattartokGroup.create();
            group.setName(name);
            
            if (list.isEmpty())
            {
                list.add(group);
                list.get(0).addToAzonositok(azon);
            }
            else
            {
                for (int j = 0; j < list.size(); j++)
                {
                    if (list.get(j).equals(group))
                    {
                        list.get(j).addToAzonositok(azon);
                    }
                    else
                    {
                        if (!list.contains(group))
                        {
                            group.addToAzonositok(azon);
                            list.add(group);
                        }
                    }
                }
            }
        }

        for (KintlevosegKapcsolattartokGroup kintlevosegKapcsolattartokGroup : list)
        {
            kintlevosegPDF(kintlevosegKapcsolattartokGroup.getAzonositokAsArray(), true);
        }
    }//GEN-LAST:event_kintlevosegLevelPDFMenuItemActionPerformed

    private void modositoMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modositoMenuItemActionPerformed
        int row = table_Invoices.getSelectedRow();
        
        String indentifier = String.valueOf(table_Invoices.getValueAt(row, 18));
        String originalInvoiceNumber = String.valueOf(table_Invoices.getValueAt(row, 0));
        Invoice invoice = new Invoice(Invoice.INVOICE, Invoice.InvoiceType.MODIFICATION, "szamlazo_invoices", indentifier);
        invoice.setCorrectedInvoice(originalInvoiceNumber);
        
        ResizeableInvoiceView view = new ResizeableInvoiceView(invoice);
    }//GEN-LAST:event_modositoMenuItemActionPerformed

    private void statusRefreshMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statusRefreshMenuItemActionPerformed
        int row = table_Invoices.getSelectedRow();
        String indentifier = String.valueOf(table_Invoices.getValueAt(row, 18));
        String comment = String.valueOf(table_Invoices.getValueAt(row, 14));
        
        Invoice invoice = new Invoice(Invoice.INVOICE, Invoice.InvoiceType.ORIGINAL, "szamlazo_invoices", indentifier);
        
        if(comment.equals("2"))
        {
            invoice.setInvoiceType(InvoiceType.STORNO);
        }
        
        invoice.GetNavStatus();
        
        frissites();
    }//GEN-LAST:event_statusRefreshMenuItemActionPerformed
  
    private void sorszamozasItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_sorszamozasItemStateChanged
        if (sorszamozasItemStateChangeCounter > 1)
        {
            if (evt.getStateChange() == ItemEvent.SELECTED)
            {
                frissites();
            }
        }
        
        sorszamozasItemStateChangeCounter++;
    }//GEN-LAST:event_sorszamozasItemStateChanged

    private void EasySearchComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_EasySearchComboBoxItemStateChanged
        firstInitEasyCombobox = true;
        
        if (firstInitEasyCombobox)
        {
            if (evt.getStateChange() == ItemEvent.SELECTED)
            {
                frissites();
            }     
        }      
    }//GEN-LAST:event_EasySearchComboBoxItemStateChanged
    
    private void printerComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_printerComboBoxItemStateChanged
        PrinterGetSet printer = new PrinterGetSet();
        printer.setPrinterName(String.valueOf(printerComboBox.getSelectedItem()));
    }//GEN-LAST:event_printerComboBoxItemStateChanged

    private void szamlaKuldesEmailMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_szamlaKuldesEmailMenuItemActionPerformed
        isKintlevoseg = false;
        int[] rows = table_Invoices.getSelectedRows();
        String[] azonositok = new String[rows.length];
        String[] names = new String[rows.length];
        List<KintlevosegKapcsolattartokGroup> list = new ArrayList<>();

        for (int i = 0; i < rows.length; i++)
        {
            names[i] = Functions.getStringFromObject(table_Invoices.getValueAt(rows[i], 1));
            azonositok[i] = String.valueOf(table_Invoices.getValueAt(rows[i], 18));
        }
        
        for (int i = 0; i < azonositok.length; i++)
        {
            String azon = azonositok[i];
            String name = names[i];
            KintlevosegKapcsolattartokGroup group = KintlevosegKapcsolattartokGroup.create();
            group.setName(name);

            if (list.isEmpty())
            {
                list.add(group);
                list.get(0).addToAzonositok(azon);
            }
            else
            {
                for (int j = 0; j < list.size(); j++)
                {
                    if (list.get(j).equals(group))
                    {
                        list.get(j).addToAzonositok(azon);
                    }
                    else
                    {
                        if (!list.contains(group))
                        {
                            group.addToAzonositok(azon);
                            list.add(group);
                        }
                    }
                }
            }
        }
        
        for (KintlevosegKapcsolattartokGroup kintlevosegKapcsolattartokGroup : list)
        {
            kintlevosegLevel(kintlevosegKapcsolattartokGroup.getAzonositokAsArray(), true);
        }
    }//GEN-LAST:event_szamlaKuldesEmailMenuItemActionPerformed

    private void kintlevosegLevelEmailMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kintlevosegLevelEmailMenuItemActionPerformed
        isKintlevoseg = true;
        int[] rows = table_Invoices.getSelectedRows();
        String[] azonositok = new String[rows.length];
        String[] names = new String[rows.length];
        List<KintlevosegKapcsolattartokGroup> list = new ArrayList<>();

        for (int i = 0; i < rows.length; i++)
        {
            names[i] = Functions.getStringFromObject(table_Invoices.getValueAt(rows[i], 1));

            azonositok[i] = String.valueOf(table_Invoices.getValueAt(rows[i], 18));
        }
        
        for (int i = 0; i < azonositok.length; i++)
        {
            String azon = azonositok[i];
            String name = names[i];
            KintlevosegKapcsolattartokGroup group = KintlevosegKapcsolattartokGroup.create();
            group.setName(name);

            if (list.isEmpty())
            {
                list.add(group);
                list.get(0).addToAzonositok(azon);
            }
            else
            {
                for (int j = 0; j < list.size(); j++)
                {
                    if (list.get(j).equals(group))
                    {
                        list.get(j).addToAzonositok(azon);
                    }
                    else
                    {
                        if (!list.contains(group))
                        {
                            group.addToAzonositok(azon);
                            list.add(group);
                        }
                    }
                }
            }
        }
        
        for (KintlevosegKapcsolattartokGroup kintlevosegKapcsolattartokGroup : list)
        {
            kintlevosegLevel(kintlevosegKapcsolattartokGroup.getAzonositokAsArray(), true);       
        }
    }//GEN-LAST:event_kintlevosegLevelEmailMenuItemActionPerformed

    private void menuItem_GLSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_GLSActionPerformed
        int [] rows = table_Invoices.getSelectedRows();
        
        JSONObject json = new JSONObject();
        
        JSONArray orderIds = new JSONArray();
        String uniqid = "";
        
        for(int i = 0; i < rows.length; i++)
        {
            orderIds.add(table_Invoices.getValueAt(rows[i], 0).toString());
            uniqid += table_Invoices.getValueAt(rows[i], 0).toString();
        }
        
        json.put("orderIDs", orderIds);
        
        JSONObject sender = new JSONObject();
            sender.put("title", "");
            sender.put("first_name", "Feladó név");
            sender.put("middle_name", "");
            sender.put("last_name", "");
            sender.put("country", "Feladó ország");
            sender.put("postal_code", "Feladó irsz.");
            sender.put("city", "Feladó város");
            sender.put("address", "Feladó cím");
            sender.put("contact_name", "");
            sender.put("phone", "");
            sender.put("email", "");
            sender.put("name", "");
        json.put("sender", sender);
        
        JSONObject consignee = new JSONObject();
            consignee.put("title", "");
            consignee.put("first_name", "Címzett név");
            consignee.put("middle_name", "");
            consignee.put("last_name", "");
            consignee.put("country", "Címzett ország");
            consignee.put("postal_code", "Címzett irsz.");
            consignee.put("city", "Címzett város");
            consignee.put("address", "Címzett cím");
            consignee.put("contact_name", "");
            consignee.put("phone", "Címzett telefonszám");
            consignee.put("email", "Címzett email");
            consignee.put("name", "");
        json.put("consignee", consignee);
        
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy.MM.dd");
        json.put("pickupDate", ft.format(new Date()));
        
        json.put("content", "content");
        
        json.put("codamount", "codamount");
        
        String appellation = sorszamozas.getSelectedItem().toString();
        
        Query query = new Query.QueryBuilder()
            .select("gls_username, gls_password, gls_senderID")
            .from("szamlazo_suppliers")
            .where("serializationID = (SELECT id FROM szamlazo_szamla_sorszam WHERE appellation = '" + appellation + "')")
            .build();
        Object [][] gls_datas = App.db.select(query.getQuery());
        
        json.put("username", gls_datas[0][0].toString());
        json.put("password", gls_datas[0][1].toString());
        json.put("senderID", gls_datas[0][2].toString());
        
        try
        {
            HttpClient client = HttpClientFactory.getHttpsClient();
            
            //HttpPost request = new HttpPost("http://localhost/GLS/GLS_pre.php");
            HttpPost request = new HttpPost("http://cezereklam.com/GLS/GLS_pre.php");
            StringEntity params =new StringEntity("message=" + json.toString(),"UTF-8");
            request.addHeader("content-type", "application/x-www-form-urlencoded");
            request.setEntity(params);
            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();
            //System.err.println(org.apache.http.util.EntityUtils.toString(entity));

            String md5Hex = DigestUtils.md5Hex(uniqid);
            String fileName = "GLS_" + md5Hex + ".pdf";
            String fileUrl = System.getProperty("user.dir") + "/src/gls/shippingLabels/" + fileName;
            
            String responseString = EntityUtils.toString(response.getEntity());
            responseString = responseString.split("<Label>")[1].split("</Label>")[0];
            
            Base64.decodeToFile(responseString, fileUrl);
            
            PdfViewer viewer = new PdfViewer();
            viewer.openPDF(fileUrl);
            
            org.apache.http.util.EntityUtils.consume(entity);
            
            request.releaseConnection();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        
        /*HttpClient httpClient = new DefaultHttpClient();
        
        try
        {
            HttpPost request = new HttpPost("https://cezereklam.com/GLS/GLS_pre.php");
            StringEntity params =new StringEntity("message=" + json.toString(),"UTF-8");
            request.addHeader("content-type", "application/x-www-form-urlencoded");
            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);

            // handle response here...
            System.err.println(org.apache.http.util.EntityUtils.toString(response.getEntity()));
            String md5Hex = DigestUtils.md5Hex(uniqid);
            String fileName = "GLS_" + md5Hex + ".pdf";
            String fileUrl = "https://localhost/GLS/upload/shipping_label/" + fileName;
            InputStream in = new URL(fileUrl).openStream();
            Files.copy(in, Paths.get("gls/shipping_label/" + fileName), StandardCopyOption.REPLACE_EXISTING);
            
            PdfViewer viewer = new PdfViewer();
            viewer.openPDF("gls/shipping_label/" + fileName);
            
            
            org.apache.http.util.EntityUtils.consume(response.getEntity());
        }
        catch (IOException | ParseException ex)
        {
            System.err.println("SzamlakFrame.java/menuItem_GLSActionPerformed()/catch");
            ex.printStackTrace();
        }
        finally
        {
            httpClient.getConnectionManager().shutdown();
        }*/
    }//GEN-LAST:event_menuItem_GLSActionPerformed

    private void table_InvoicesMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_InvoicesMousePressed
        popupTimer = new PopupTimer();
        popupTimer.Start();
    }//GEN-LAST:event_table_InvoicesMousePressed

    private void comboBox_SortItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboBox_SortItemStateChanged
        if(!comboboxUpdate)
        {
            frissites();
        }
    }//GEN-LAST:event_comboBox_SortItemStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton DetailedSearchButton;
    private javax.swing.JButton DetailedSearchToDefaultButton;
    private javax.swing.JComboBox EasySearchComboBox;
    private javax.swing.JLabel ReszletesKereses;
    private javax.swing.JCheckBox atutalasCheckBox;
    private javax.swing.JComboBox<String> comboBox_Sort;
    private javax.swing.JMenuItem csvKeszitesEgyszeruMenuItem;
    private javax.swing.JMenuItem csvKeszitesReszletesMenuItem;
    private javax.swing.JTextField esedekessegIg;
    private javax.swing.JLabel esedekessegIgCal;
    private javax.swing.JTextField esedekessegTol;
    private javax.swing.JLabel esedekessegTolCal;
    private javax.swing.JCheckBox helyesbitett;
    private javax.swing.JCheckBox helyesbito;
    private javax.swing.JMenuItem helyesbitoMenuItem;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JTextField keltIg;
    private javax.swing.JLabel keltIgCal;
    private javax.swing.JTextField keltTol;
    private javax.swing.JLabel keltTolCal;
    private javax.swing.JTextField kereses;
    private javax.swing.JCheckBox keszpenzCheckBox;
    private javax.swing.JTextField kifizetesIg;
    private javax.swing.JLabel kifizetesIgCal;
    private javax.swing.JMenuItem kifizetesMenuItem;
    private javax.swing.JTextField kifizetesTol;
    private javax.swing.JLabel kifizetesTolCal;
    private javax.swing.JMenuItem kijeloltekTermekdijKimutatas;
    private javax.swing.JMenuItem kintlevosegLevelEmailMenuItem;
    private javax.swing.JMenuItem kintlevosegLevelPDFMenuItem;
    private javax.swing.JMenuItem masolatMenuItem;
    private javax.swing.JMenuItem menuItem_GLS;
    private javax.swing.JMenuItem modositoMenuItem;
    private javax.swing.JCheckBox nemNyomtatottCheckBox;
    private javax.swing.JCheckBox normal;
    private javax.swing.JDialog nyomtatasDialog;
    private javax.swing.JLabel nyomtatasLabel;
    private javax.swing.JMenuItem nyomtatasMenuItem;
    private javax.swing.JTextField nyomtatasPeldany;
    private javax.swing.JMenuItem pdfKeszitesMenuItem;
    private javax.swing.JComboBox<String> printerComboBox;
    private javax.swing.JPanel printingPanel;
    private javax.swing.JDialog reszletesKeresesDialog;
    private javax.swing.JComboBox sorszamozas;
    private javax.swing.JMenuItem statusRefreshMenuItem;
    private javax.swing.JMenuItem stornoMenuItem;
    private javax.swing.JScrollPane sumScrollPane;
    private javax.swing.JTable sumTable;
    private javax.swing.JMenuItem szamlaKuldesEmailMenuItem;
    private javax.swing.JPopupMenu szamlaPopupMenu;
    private javax.swing.JScrollPane szamlaScrollPane;
    private javax.swing.JTable table_Invoices;
    private javax.swing.JTextField teljesitesIg;
    private javax.swing.JLabel teljesitesIgCal;
    private javax.swing.JMenuItem teljesitesIgazolasNyomtatasaMenuItem;
    private javax.swing.JTextField teljesitesTol;
    private javax.swing.JLabel teljesitesTolCal;
    private javax.swing.JCheckBox utanvetCheckBox;
    // End of variables declaration//GEN-END:variables

    private void init()
    {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher()
        {
            @Override
            public boolean dispatchKeyEvent(KeyEvent ke)
            {
                synchronized (KeyPressed.class)
                {
                    switch (ke.getID())
                    {
                        case KeyEvent.KEY_PRESSED:
                            if (ke.getKeyCode() == KeyEvent.VK_A)
                            {
                                KeyPressed.pressed = true;
                            }
                            break;

                        case KeyEvent.KEY_RELEASED:
                            if (ke.getKeyCode() == KeyEvent.VK_A)
                            {
                                KeyPressed.pressed = false;
                            }
                            break;
                    }
                    
                    return false;
                }
            }
        });
        
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;

        java.net.URL url = ClassLoader.getSystemResource("cezeszamlazo/resources/print_20.png");
        java.awt.Image img = toolkit.createImage(url);

        nyomtatasDialog.setSize(nyomtatasDialog.getPreferredSize().width, nyomtatasDialog.getPreferredSize().height + 35);
        nyomtatasDialog.setIconImage(img);

        reszletesKeresesDialog.setSize(reszletesKeresesDialog.getPreferredSize().width + 35, reszletesKeresesDialog.getPreferredSize().height + 35);
        reszletesKeresesDialog.setIconImage(img);

        url = ClassLoader.getSystemResource("cezeszamlazo/resources/szamlak.png");
        img = toolkit.createImage(url);

        setIconImage(img);
        setLocation(x, y);
        setTitle("Számlák");

        UpdateComboBox_Sort();
        sorszamozasfrissites();
        PrinterGetSet fillprintercombobox = new PrinterGetSet();
        fillprintercombobox.fillPrinterCombobox(printerComboBox);

        szamlaScrollPane.getHorizontalScrollBar().addAdjustmentListener(new ScrollAdjustmentListener(sumScrollPane));
        sumScrollPane.getHorizontalScrollBar().addAdjustmentListener(new ScrollAdjustmentListener(szamlaScrollPane));

//        ActionListener actionListener1 = new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent actionEvent) {
//                JCheckBox checkbox = (JCheckBox) actionEvent.getSource();
////                AbstractButton abstractButton = (AbstractButton) actionEvent.getSource();
//                boolean selected = checkbox.getModel().isSelected();
//                kintlevosegCheckBox.setSelected(selected);
//
//            }
//        };
//        ActionListener actionListener2 = new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent actionEvent) {
//                JCheckBox checkbox = (JCheckBox) actionEvent.getSource();
////                AbstractButton abstractButton = (AbstractButton) actionEvent.getSource();
//                boolean selected = checkbox.getModel().isSelected();
////                System.out.println("NORMAL: " + selected);
//
//                kintlevosegCheckBoxInAdvancedSearch.setSelected(selected);
//
//            }
//        };
//        kintlevosegCheckBox.addItemListener(new ItemListener() {
//            @Override
//            public void itemStateChanged(ItemEvent e) {
//
//                if (e.getStateChange() == ItemEvent.SELECTED) {//checkbox has been selected
//                    kintlevosegCheckBoxInAdvancedSearch.setSelected(true);
//                } else {//checkbox has been deselected
//                    kintlevosegCheckBoxInAdvancedSearch.setSelected(false);
//                };
//                kintlevosegCheckBox.setSelected(false);
//            }
//        });
//
//        kintlevosegCheckBoxInAdvancedSearch.addItemListener(new ItemListener() {
//            @Override
//            public void itemStateChanged(ItemEvent e) {
//                if (e.getStateChange() == ItemEvent.SELECTED) {//checkbox has been selected
//                    kintlevosegCheckBox.setSelected(true);
//                } else {//checkbox has been deselected
//                    kintlevosegCheckBox.setSelected(false);
//                };
//                kintlevosegCheckBoxInAdvancedSearch.setSelected(false);
//            }
//        });
//        
//        ButtonGroup group = new ButtonGroup();
//        group.add(kintlevosegCheckBox);
//        group.add(kintlevosegCheckBoxInAdvancedSearch);
//        kintlevosegCheckBoxInAdvancedSearch.addActionListener(actionListener1);
//        kintlevosegCheckBox.addActionListener(actionListener2);

        kintlevosegLevelEmailMenuItem.setVisible(true);
        kintlevosegLevelPDFMenuItem.setVisible(true);
    }
    
    private void deletePDFs(File [] folders)
    {
        for(int i = 0; i < folders.length; i++)
        {
            File[] listOfFiles = folders[i].listFiles();
            
            for(int j = 0; j < listOfFiles.length; j++)
            {
                String fileName = listOfFiles[j].getName();
                String extension = FilenameUtils.getExtension(fileName);
                
                if(extension.equals("pdf"))
                {
                    listOfFiles[j].delete();
                }
            }
        }
    }
    
    private void UpdateComboBox_Sort()
    {
        comboboxUpdate = true;
        comboBox_Sort.removeAllItems();
        
        Calendar now = Calendar.getInstance();
        int currentYear = Integer.parseInt(String.valueOf(now.get(Calendar.YEAR)));
        
        String curYear = "Ideji év (" + currentYear + ")";
        comboBox_Sort.addItem(curYear);
        
        String lastYear = "Tavalyi év (" + (currentYear - 1) + ")";
        comboBox_Sort.addItem(lastYear);
        
        String currAndLast = "Ideji + tavalyi év (" + currentYear + "," + (currentYear - 1) + ")";
        comboBox_Sort.addItem(currAndLast);
        
        String all = "Minden év";
        comboBox_Sort.addItem(all);
        
        comboboxUpdate = false;
    }

    private void sorszamozasfrissites()
    {
        b = false;
        sorszamozas.removeAllItems();
        String whereString = "";
        String [] temp = App.user.getCeg().split(";");
        
        for (int i = 0; i < temp.length; i++)
        {
            whereString += "id = " + temp[i] + " || ";
        }
        
        whereString += "0";
        
        Query query = new Query.QueryBuilder()
            .select("serializationID")
            .from("szamlazo_suppliers")
            .where(whereString)
            .build();
        Object[][] select = App.db.select(query.getQuery());
        
        whereString = "";
        
        for (Object[] obj : select)
        {
            whereString += "id = " + obj[0] + " || ";
        }
        
        whereString += "0";
        
        query = new Query.QueryBuilder()
            .select("id, appellation")
            .from("szamlazo_szamla_sorszam")
            .where(whereString)
            .order("id")
            .build();
        Object[][] object = App.db.select(query.getQuery());
        
        for (Object[] obj : object)
        {
            sorszamozas.addItem(new Label(String.valueOf(obj[0]), String.valueOf(obj[1])));
        }
        
        b = true;
        
        int id = App.user.getId();
        
        query = new Query.QueryBuilder()
            .select("defaultCompanyID")
            .from("szamlazo_users")
            .where("id = " + id)
            .build();
        select = App.db.select(query.getQuery());
        
        String DefaultCompany = select[0][0].toString();
        
        query = new Query.QueryBuilder()
            .select("serializationID")
            .from("szamlazo_suppliers")
            .where("id = " + DefaultCompany)
            .build();
        select = App.db.select(query.getQuery());
        
        String szamlazo_ceg_adatok_Sorszamid = select[0][0].toString();
        
        query = new Query.QueryBuilder()
            .select("id, appellation")
            .from("szamlazo_szamla_sorszam")
            .where("id = " + szamlazo_ceg_adatok_Sorszamid)
            .build();
        select = App.db.select(query.getQuery());
        
        String szamlazoSzamlaSorszam_Id = select[0][0].toString();
        String szamlazoSzamlaSorszam_Megnevezes = select[0][1].toString();

        Object DefaultSerial = null;
        
        for(int i = 0; i < sorszamozas.getItemCount(); i++)
        {
            Object item = sorszamozas.getItemAt(i);
            if(item.toString().equals(szamlazoSzamlaSorszam_Megnevezes))
            {
                DefaultSerial = item;
            }
        }
        
        sorszamozas.setSelectedItem(DefaultSerial);
    }
    
    public String findOriginalInvoiceNumber(String invoiceNumber)
    {
        String originalInvoiceNumber = "";
        
        Query query = new Query.QueryBuilder()
            .select("szamla_sorszam, helyesbitett")
            .from("szamlazo_szamla")
            .where("szamla_sorszam LIKE '" + invoiceNumber + "'")
            .build();
        Object [][] select = App.db.select(query.getQuery());
        
        if(select[0][1].toString().equals(""))
        {
            originalInvoiceNumber = select[0][0].toString();
        }
        else
        {
            originalInvoiceNumber = findOriginalInvoiceNumber(select[0][1].toString());
        }
        
        return originalInvoiceNumber;
    }
    
    public String findLastModificationReference(String originalInvoiceNumber, String actualInvoiceNumber)
    {
        String lastModificationReference = "";
        
        Query query = new Query.QueryBuilder()
            .select("id, szamla_sorszam")
            .from("szamlazo_szamla")
            .where("helyesbitett LIKE '" + originalInvoiceNumber + "'")
            .order("id DESC")
            .build();
        Object[][] select = App.db.select(query.getQuery());       
        
        try
        {
            if (!actualInvoiceNumber.equals(lastModificationReference))
            {
                lastModificationReference = select[0][1].toString();
            }
            else
            {
                lastModificationReference = "";
            }
        }
        catch(ArrayIndexOutOfBoundsException e)
        {
            lastModificationReference = "";
        }
        
        return lastModificationReference;
    }
    
    //unused
    public String SearchForLineNumberRef(String originalInvoiceNumber, String modifiedproductID)
    {
        ArrayList<String[]> products = new ArrayList<>();
        String lineNumberReference = "";
        
        Query query = new Query.QueryBuilder()
            .select("modifiedproductID")
            .from("szamlazo_szamla_adatok")
            .where("szamla_sorszam LIKE '" + originalInvoiceNumber + "'")
            .build();
        Object [][] szamlazo_szamla_original = App.db.select(query.getQuery());
        
        for(int i = 0; i < szamlazo_szamla_original.length; i++)
        {      
            String [] product = {String.valueOf(i+1), szamlazo_szamla_original[i][0].toString()};
            products.add(product);
        }
        
        query = new Query.QueryBuilder()
            .select("id, szamla_sorszam")
            .from("szamlazo_szamla")
            .where("helyesbitett LIKE '" + originalInvoiceNumber + "'")
            .order("id ASC")
            .build();
        Object [][] szamlazo_szamla = App.db.select(query.getQuery());
        
        for(Object [] obj: szamlazo_szamla)
        {
            query = new Query.QueryBuilder()
                .select("id, modifiedproductID")
                .from("szamlazo_szamla_adatok")
                .where("szamla_sorszam LIKE '" + obj[1] + "'")
                .order("id ASC")
                .build();
            Object [][] select = App.db.select(query.getQuery());
            
            for(Object [] obj2 : select)
            {
                if(obj2[1].equals("0"))
                {
                    String [] product = {String.valueOf(products.size()), obj2[0].toString()};
                    products.add(product);
                }                
            }
        }
        
        for(int i = 0; i < products.size(); i++)
        {
            if(products.get(i)[1].equals(modifiedproductID))
            {
                lineNumberReference = String.valueOf(i+1);
            }
        }
        
        return lineNumberReference;
    }  

    public void frissites()
    {
        keres = "1";
        
        Calendar now = Calendar.getInstance();
        int currentYear = Integer.parseInt(String.valueOf(now.get(Calendar.YEAR)));
        
        switch(comboBox_Sort.getSelectedIndex())
        {
            case 0:
                keres += " && i.invoiceNumber LIKE '%" + currentYear + "%'";
                break;
            case 1:
                keres += " && i.invoiceNumber LIKE '%" + (currentYear - 1) + "%'";
                break;
            case 2:
                keres += " && i.invoiceNumber LIKE '%" + currentYear + "%' OR i.invoiceNumber LIKE '%" + (currentYear - 1) + "%'";
                break;
            case 3:
                
                break;
        }
        
        String searchText = EncodeDecode.encode(kereses.getText().replace("'", "\\\'"));
        
        keres += " && (i.invoiceNumber LIKE '%" + searchText + "%' || i.customerName LIKE '%" + searchText + "%')";
        
        if(nemNyomtatottCheckBox.isSelected())
        {
            keres += " && i.printed = 0";
        }
        
        keres += " && (0";
        
        /*if(kintlevosegCheckBoxInAdvancedSearch.isSelected())
        {
            keres += " || i.paymentDate = '0000-00-00'";
        }*/
        
        if(normal.isSelected())
        {
            keres += " || i.invoiceType = 'NEW'";
        }
        
        if (helyesbito.isSelected())
        {
            keres += " || i.invoiceType = 'CORRECTION' || i.invoiceType = 'MODIFICATION' || i.invoiceType = 'STORNO'";
        }
        
        if (helyesbitett.isSelected())
        {
            keres += " || i.invoiceType = 'GOTSTORNO'";
        }
        
        if (!checkIfNothingSelected())
        {
            keres += " || i.paymentDate = '0000-00-00'";
            keres += " || i.invoiceType = 'NEW'";
            keres += " || i.invoiceType = 'CORRECTION' || i.invoiceType = 'MODIFICATION' || i.invoiceType = 'STORNO'";
            keres += " || i.invoiceType = 'GOTSTORNO'";
        }
        
        keres += ") && (0";
        
        if (keszpenzCheckBox.isSelected())
        {
            keres += " || i.paymentMethod = 0";
        }
        
        if (atutalasCheckBox.isSelected())
        {
            keres += " || i.paymentMethod = 1";
        }
        
        if (utanvetCheckBox.isSelected())
        {
            keres += " || i.paymentMethod = 2";
        }
        
        keres += ")";
        
        if (!keltTol.getText().isEmpty() || !keltIg.getText().isEmpty())
        {
            keres += " && i.issueDate BETWEEN '" + (keltTol.getText().isEmpty() ? "0000-00-00" : keltTol.getText()) + "' AND " + (keltIg.getText().isEmpty() ? "NOW()" : "'" + keltIg.getText() + "'");
        }
        if (!teljesitesTol.getText().isEmpty() || !teljesitesIg.getText().isEmpty())
        {
            keres += " && i.paymentDate BETWEEN '" + (teljesitesTol.getText().isEmpty() ? "0000-00-00" : teljesitesTol.getText()) + "' AND " + (teljesitesIg.getText().isEmpty() ? "NOW()" : "'" + teljesitesIg.getText() + "'");
        }
        if (!esedekessegTol.getText().isEmpty() || !esedekessegIg.getText().isEmpty())
        {
            keres += " && i.maturityDate BETWEEN '" + (esedekessegTol.getText().isEmpty() ? "0000-00-00" : esedekessegTol.getText()) + "' AND " + (esedekessegIg.getText().isEmpty() ? "NOW()" : "'" + esedekessegIg.getText() + "'");
        }
        if (!kifizetesTol.getText().isEmpty() || !kifizetesIg.getText().isEmpty())
        {
            keres += " && i.paymentDate BETWEEN '" + (kifizetesTol.getText().isEmpty() ? "0000-00-00" : kifizetesTol.getText()) + "' AND " + (kifizetesIg.getText().isEmpty() ? "NOW()" : "'" + kifizetesIg.getText() + "'");
        }
        
        DefaultTableModel DTModel = (DefaultTableModel) table_Invoices.getModel();
        
        String[] header = {"Sorszám", "Vevő", "Kelt", "Nettó", "Áfaérték", "Bruttó", "Fizetendő", "Fennmaradó", "Esedékesség", "Teljesítés", "Kifizetés", "Késedelem", "Becsült kamat", "Fizetési mód", "Megjegyzés", "Csoport", "Valuta", "Középárfolyam", "Azonosító", "Teljesites igazolas sorszama", "Teljesítés Igazolás kelte", "NavStátusz"};
        
        Label selectedLabel = (Label) sorszamozas.getSelectedItem();
        
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(new Date());

        String where = "";
        
        //System.err.println("selectedLabel.getId(): " + selectedLabel.getId());
        switch(EasySearchComboBox.getSelectedItem().toString())
        {
            case "Összes":
                where = "serializationID = " + selectedLabel.getId();
                break;
            case "Kintlévőség":
                //where = "serializationID = " + selectedLabel.getId() + " AND paymentDate = '0000-00-00' AND invoiceType = 'UNPAID'";
                where = "serializationID = " + selectedLabel.getId() + " AND paymentDate = '0000-00-00'";
                break;
            case "Határidőn túli kintlévőség":
                //where = "serializationID = " + selectedLabel.getId() + " AND paymentDate = '0000-00-00' AND invoiceType = 'UNPAID' AND `maturityDate` < '" + date + "'";
                where = "serializationID = " + selectedLabel.getId() + " AND paymentDate = '0000-00-00' AND `maturityDate` < '" + date + "'";
                break;
        }
        
        Query query = new Query.QueryBuilder()
            .select("i.invoiceNumber, " // Sorszám
                + "i.customerName, " // Vevő
                + "i.issueDate, " // Kelt
                + "i.netPrice, " // Nettó
                + "i.vatAmount, " // Áfaérték
                + "i.netPrice + i.vatAmount, " // Bruttó
                + "i.netPrice + i.vatAmount, " // Fizetendő
                + "i.netPrice + i.vatAmount - (SELECT IF(SUM(amountPaid) IS NULL, 0, SUM(amountPaid)) from szamlazo_invoice_payments WHERE invoiceNumber = i.invoiceNumber && indentifier = i.indentifier), " // Fennmaradó
                + "i.maturityDate, " // Esedékesség
                + "i.completionDate, " // Teljesítés
                + "IF(i.paymentDate = '0000-00-00', '', i.paymentDate), " // Kifizetés
                + "CONCAT(IF(i.paymentDate = '0000-00-00', IF(DATEDIFF(NOW(), i.maturityDate) < 0, 0, DATEDIFF(NOW(), i.maturityDate)), DATEDIFF(i.paymentDate, i.maturityDate)), ' nap'), " // Késedelem
                + "(i.netPrice + i.vatAmount) * (SELECT alapkamat from szamlazo_jegybanki_alapkamat) / 100 * 2 / 365 * IF(i.paymentDate = '0000-00-00', IF(DATEDIFF(NOW(), i.maturityDate) < 0, 0, DATEDIFF(NOW(), i.maturityDate)), DATEDIFF(i.paymentDate, i.maturityDate)), " // Becsült kamat
                + "i.paymentMethod, " // Fizetési mód
                + "i.invoiceType, " // Megjegyzés
                + "IF(i.invoiceGroup = 0, 'nincs', 'Értékesítés'), " // Csoport
                + "i.currency, " // Valuta
                + "i.centralParity, " // Középárfolyam
                + "i.indentifier, " // Azonosító
                + " (SELECT szti.tig_sorszam FROM szamlazo_teljesites_igazolas AS szti WHERE szti.szamla_sorszam = i.invoiceNumber), "//TIG sorszam
                + " (SELECT szti.teljesites_igazolas_kelte  FROM szamlazo_teljesites_igazolas AS szti WHERE szti.szamla_sorszam = i.invoiceNumber), "//TIG kelte
                + "i.navStatus, "//NavStátusz
                + "SUBSTRING_INDEX(SUBSTRING_INDEX(i.invoiceNumber, '/', 1), ' ', -1) as code, "
                + "SUBSTRING_INDEX(i.invoiceNumber, '/', -1) as code2")
            .from("szamlazo_invoices i ")
            .where(keres + " AND " + where + " AND issueDate > '2012-12-31'")
            //.order(" i.issueDate DESC, i.id DESC")
            .order("code DESC, length(code2) DESC, code2 DESC")
            .build();
        System.err.println(query);
        Object [][] result = App.db.select(query.getQuery());
        
        if (result.length != 0)
        {
            for (int i = 0; i < result.length; i++)
            {
                if(String.valueOf(result[i][16]).equalsIgnoreCase("HUF"))//(Integer.valueOf(String.valueOf(result[i][13])) == 0 && String.valueOf(result[i][16]).equalsIgnoreCase("HUF"))
                {
                    boolean isUtalas = false;
                    
                    if(String.valueOf(result[i][13]).equalsIgnoreCase("1"))
                    {
                        isUtalas = true;
                    }
                    
                    result[i][6] = Functions.kerekit(Double.valueOf(String.valueOf(result[i][6])), isUtalas);
                }
                
                switch(Integer.valueOf(result[i][21].toString()))
                {
                    case 0: result[i][21] = "Beküldetlen";
                        break;
                    case 1: result[i][21] = "Beküldött";
                        break;
                    case 2: result[i][21] = "Feldolgozás alatt";
                        break;
                    case 3: result[i][21] = "Feldolgozott";
                        break;
                    case 4: result[i][21] = "Elutasított";
                        break;
                }
                
                switch(Integer.parseInt(result[i][13].toString()))
                {
                    case 0: 
                        result[i][13] = "Készpénz";
                        break;
                    case 1: 
                        result[i][13] = "Átutalás";
                        break;
                    case 2: 
                        result[i][13] = "Utánvét";
                        break;
                }
                
                if(result[i][14].toString().equals("GOTSTORNO"))
                {
                    result[i][14] = "3";
                }
                else if(result[i][14].toString().equals("STORNO"))
                {
                    result[i][14] = "2";
                }
                else if(result[i][10].toString().equals(""))
                {
                    result[i][14] = "0";
                }
                else
                {
                    result[i][14] = "1";
                }
            }
        }
        
        DTModel.setDataVector(result, header);
        
        DTModel = (DefaultTableModel) sumTable.getModel();            
        DTModel.setColumnIdentifiers(header);
        
        TableColumn col;
        SzamlaTableCellRender render = new SzamlaTableCellRender();
        int[] meret = {75, 150, 70, 75, 75, 75, 75, 75, 70, 70, 70, 70, 75, 75, 100, 100, 50, 50, 75, 75, 75, 75};
        String width = Settings.get("szamlaTableHeader");
        
        if (width != null && !width.isEmpty())
        {
            String[] w = width.split(";");
            
            if (w.length != 0)
            {
                for (int i = 0; i < Math.min(w.length, meret.length); i++)
                {
                    meret[i] = Integer.parseInt(w[i]);
                }
            }
        }
        
        b = false;

        for (int i = 0; i < 22; i++)
        {
            try
            {
                col = table_Invoices.getColumnModel().getColumn(i);
                col.setCellRenderer(render);
                col.setPreferredWidth(meret[i]);
                col = sumTable.getColumnModel().getColumn(i);
                col.setPreferredWidth(meret[i]);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        
        b = true;
        
        firstInitEasyCombobox = true;

        osszesit();
    }

    private void osszesit()
    {
        // 3 - 7, 13
        for (int i = 3; i <= 7; i++)
        {
            osszesit(i);
        }
        
        osszesit(12);
    }

    private void osszesit(int col)
    {
        int o = 0;
        int[] rows = table_Invoices.getSelectedRows();
        
        if (rows.length == 0)
        {
            for (int i = 0; i < table_Invoices.getRowCount(); i++)
            {
                if (!String.valueOf(table_Invoices.getValueAt(i, 16)).equalsIgnoreCase("huf"))
                {
                    o += (int) Math.round(Double.parseDouble(String.valueOf(table_Invoices.getValueAt(i, col))) * Double.parseDouble(String.valueOf(table_Invoices.getValueAt(i, 17))));
                }
                else
                {
                    o += (int) Math.round(Double.parseDouble(String.valueOf(table_Invoices.getValueAt(i, col))));
                }
            }
        }
        else
        {
            for (int i = 0; i < rows.length; i++)
            {
                if (!String.valueOf(table_Invoices.getValueAt(rows[i], 16)).equalsIgnoreCase("huf"))
                {
                    o += (int) Math.round(Double.parseDouble(String.valueOf(table_Invoices.getValueAt(rows[i], col))) * Double.parseDouble(String.valueOf(table_Invoices.getValueAt(rows[i], 17))));
                }
                else
                {
                    o += (int) Math.round(Double.parseDouble(String.valueOf(table_Invoices.getValueAt(rows[i], col))));
                }
            }
        }
        
        sumTable.setValueAt(EncodeDecode.numberFormat(String.valueOf(o), false) + " Ft", 0, col);
    }
    
    public void nyit()
    {
        frissites();
        setVisible(true);
    }

    private void nyit(Object dialog, String title)
    {
        if (dialog instanceof JDialog)
        {
            JDialog d = (JDialog) dialog;
            Dimension appSize = getSize();
            Point appLocation = getLocation();
            int x = (appSize.width - d.getWidth()) / 2 + appLocation.x;
            int y = (appSize.height - d.getHeight()) / 2 + appLocation.y;
            d.setLocation(x, y);
            d.setTitle(title);
            d.setVisible(true);
        }
        else if (dialog instanceof JFrame)
        {
            JFrame f = (JFrame) dialog;
            Dimension appSize = getSize();
            Point appLocation = getLocation();
            int x = (appSize.width - f.getWidth()) / 2 + appLocation.x;
            int y = (appSize.height - f.getHeight()) / 2 + appLocation.y;
            f.setLocation(x, y);
            f.setTitle(title);
            f.setVisible(true);
        }
    }
    
    private void kintlevosegPDF(String[] azonositok, boolean egybeVagyKulon)
    {
        try
        {
            KintlevosegLevel kintlevosegLevel = KintlevosegLevel.create(KintlevosegLevel.Type.PDF, isKintlevoseg);
            kintlevosegLevel.createPDF(azonositok, egybeVagyKulon, false, isKintlevoseg);
        }
        catch (KintlevosegLevelException exception)
        {
            HibaDialog h = new HibaDialog("Nincs kintlévőség kiválasztva!", "", "Ok");
        }

    }

    private void kintlevosegLevel(String[] azonositok, boolean egybeVagyKulon)
    {
        try
        {
            KintlevosegLevel kintlevosegLevel = KintlevosegLevel.create(KintlevosegLevel.Type.EMAIL, isKintlevoseg);
            kintlevosegLevel.createEmail(azonositok, egybeVagyKulon, false, isKintlevoseg);
        }
        catch (KintlevosegLevelException exception)
        {
            HibaDialog h = new HibaDialog("Nincs kintlévőség kiválasztva!", "", "Ok");
        }
    }

    public static void mailTo(String to, String subject, String body, String attachmentFileUrl)
    {
        String parameters = "\"to='" + to + "',subject='" + subject + "',body='" + body.replace("\n", "<br />") + "'\",format=1,";
        String[] fileUrls = attachmentFileUrl.split(";");
        String attachment = "attachment='";
        int count = 0;
        
        for (int i = 0; i < fileUrls.length; i++)
        {
            attachment += fileUrls[i];
            if (count != fileUrls.length - 1)
            {
                attachment += ",";
            }

            count++;
        }
        
        attachment += "'";
        attachment = attachment.replaceAll(" ", "");
        parameters += attachment;
        parameters = parameters.replaceAll("<br />", "");
        System.out.println(parameters);
        
        try
        {
            // thunderbird útvonal!!! TESZT
            Runtime.getRuntime().exec("cmd /c C:\\PROGRA~1\\MOZILL~1\\thunderbird.exe -compose " + parameters);
        }
        catch (Exception ex){}
        
        try
        {
            // thunderbird útvonal!!!
            Runtime.getRuntime().exec("cmd /c C:\\PROGRA~1\\MOZILL~2\\thunderbird.exe -compose " + parameters);
        }
        catch (Exception ex){}
        
        try
        {
            Runtime.getRuntime().exec("cmd /c C:\\PROGRA~1\\MOZILL~3\\thunderbird.exe -compose " + parameters);
        }
        catch (Exception ex){}
        
        try
        {
            Runtime.getRuntime().exec("cmd /c C:\\PROGRA~2\\MOZILL~1\\thunderbird.exe -compose " + parameters);
        } catch (Exception ex){}
        
        try
        {
            Runtime.getRuntime().exec("cmd /c C:\\PROGRA~2\\MOZILL~2\\thunderbird.exe -compose " + parameters);
        } catch (Exception ex){}
        
        try
        {
            Runtime.getRuntime().exec("cmd /c C:\\PROGRA~2\\MOZILL~3\\thunderbird.exe -compose " + parameters);
        }
        catch (Exception ex){}
    }

    private void kijeloltCsvKeszitese(File f, boolean reszletes)
    {
        FileOutputStream fos;
        DataOutputStream dos;
        
        String sep = "\n";
        byte[] data;
        int[] rows = table_Invoices.getSelectedRows();
        String whereText = "";
        
        for (int i = 0; i < rows.length; i++)
        {
            whereText += "invoiceNumber = '" + String.valueOf(table_Invoices.getValueAt(rows[i], 0)) + "' || ";
        }
        
        whereText += "0";
        
        Query query = new Query.QueryBuilder()
            .select("customerName, "
                + "CONCAT(invoiceNumber, IF(foreignCurrency = 1, 'V', '')), "
                + "issueDate, "
                + "netPrice, "
                + "vatAmount, "
                + "netPrice + vatAmount, "
                + "completionDate, "
                + "paymentMethod, "
                + "currency, "
                + "centralParity, "
                + "indentifier, "
                + "takeoverType, "
                + "foreignCurrency")
            .from("szamlazo_invoices")
            .where(whereText)
            .order("issueDate ASC, id ASC")
            .build();
        Object[][] s = App.db.select(query.getQuery());
        
        try
        {
            fos = new FileOutputStream(f);
            dos = new DataOutputStream(fos);

            String[] header = {"Számla sorszám", "Vevő", "Kelt", "Nettó", "Áfaérték", "Bruttó", "Teljesítés", "Fizetési mód", "Valuta", "Középárfolyam"};
            
            for (int i = 0; i < header.length; i++)
            {
                String fejlec = "\"" + header[i] + "\";";
                data = fejlec.getBytes("UTF-8");
                dos.write(data);
            }
            
            dos.writeChars(sep);
            
            for (int i = 0; i < s.length; i++)
            {
                String sor = "\"" + String.valueOf(s[i][1]) + "\";\"" + String.valueOf(s[i][0]) + "\";\"" + String.valueOf(s[i][2]) + "\";" + String.valueOf(s[i][3]).replace(".", ",") + ";"
                    + String.valueOf(s[i][4]).replace(".", ",") + ";" + String.valueOf(s[i][5]).replace(".", ",") + ";\"" + String.valueOf(s[i][6]) + "\";\"" + (String.valueOf(s[i][7]).matches("0") ? "készpénz" : "átutalás") + "\";"
                    + "\"" + String.valueOf(s[i][8]) + "\";" + String.valueOf(s[i][9]).replace(".", ",");
                data = sor.getBytes("UTF-8");
                dos.write(data);
                dos.writeChars(sep);

                if (reszletes)
                {
                    dos.writeChars(sep);
                    header = new String[]{"", "Termék", "VTSZ/TESZOR", "Mennyiség", "Mee.", "Egységár", "Nettó ár", "ÁFA", "Áfa érték", "Bruttó ár"};
                    
                    for (int j = 0; j < header.length; j++)
                    {
                        String fejlec = "\"" + header[j] + "\";";
                        data = fejlec.getBytes("UTF-8");
                        dos.write(data);
                    }

                    dos.writeChars(sep);
                    
                    /*query = new Query.QueryBuilder()
                        .select("termek, " // 0
                            + "vtsz_teszor, " // 1
                            + "mennyiseg, " // 2
                            + "mennyisegi_egyseg, " // 3
                            + "egysegar, " // 4
                            + "netto_ar, " // 5
                            + "afa," // 6
                            + "IF(" + String.valueOf(s[i][12]) + ", ROUND(netto_ar * afa) / 100.0, ROUND(netto_ar * afa / 100.0)), " // 7
                            + "IF(" + String.valueOf(s[i][12]) + ", ROUND(netto_ar * (100 + afa)) / 100.0, ROUND(netto_ar * (100 + afa) / 100.0)), " // 8
                            + "id ")
                        .from("szamlazo_szamla_adatok")
                        .where("azon = " + String.valueOf(s[i][10]))
                        .order("id ASC")
                        .build();*/
                    query = new Query.QueryBuilder()
                        .select("productName, "
                            + "vtszTeszor, "
                            + "quantity, "
                            + "amountUnits, "
                            + "unitPrice, "
                            + "netPrice, "
                            + "vatPercent, "
                            + "IF(" + s[i][12].toString() + ", ROUND(netPrice * vatPercent) / 100.0, ROUND(netPrice * vatPercent / 100.0)), "
                            + "IF(" + s[i][12].toString() + ", ROUND(netPrice * (100 + vatPercent)) / 100.0, ROUND(netPrice * (100 + vatPercent) / 100.0)), "
                            + "id ")
                        .from("szamlazo_invoice_products")
                        .where("indentifier = " + s[i][10].toString())
                        .order("id ASC")
                        .build();
                    Object[][] s2 = App.db.select(query.getQuery());
                    
                    for (int j = 0; j < s2.length; j++)
                    {
                        sor = "\"\";\"" + String.valueOf(s2[j][0]) + "\";\"" + String.valueOf(s2[j][1]) + "\";" + String.valueOf(s2[j][2]).replace(".", ",") + ";\"" + String.valueOf(s2[j][3]) + "\";"
                            + String.valueOf(s2[j][4]).replace(".", ",") + ";" + String.valueOf(s2[j][5]).replace(".", ",") + ";" + String.valueOf(s2[j][6]).replace(".", ",") + ";"
                            + String.valueOf(s2[j][7]).replace(".", ",") + ";" + String.valueOf(s2[j][8]).replace(".", ",");
                        data = sor.getBytes("UTF-8");
                        dos.write(data);
                        dos.writeChars(sep);
                        
                        /*query = new Query.QueryBuilder()
                            .select("id, "
                                + "ROUND(osszsuly * 10000.0) / 10000.0, " // 0
                                + "termekdij, " // 1
                                + "ROUND(termekdij * ROUND(osszsuly * 10000.0) / 10000.0), " // 2
                                + "ROUND(ROUND(termekdij * ROUND(osszsuly * 10000.0) / 10000.0) * 0.27), " // 3
                                + "ROUND(ROUND(termekdij * ROUND(osszsuly * 10000.0) / 10000.0) * 1.27), " // 4
                                + "csk, " // 5
                                + "kt ")
                            .from("szamlazo_szamla_termekdij")
                            .where("termekid = " + String.valueOf(s2[j][9]))
                            .build();*/
                        query = new Query.QueryBuilder()
                            .select("id, "
                                + "ROUND(totalWeight * 10000.0) / 10000.0, "
                                + "productFee, "
                                + "ROUND(productFee * ROUND(totalWeight * 10000.0) / 10000.0), "
                                + "ROUND(ROUND(productFee * ROUND(totalWeight * 10000.0) / 10000.0) * 0.27), "
                                + "ROUND(ROUND(productFee * ROUND(totalWeight * 10000.0) / 10000.0) * 1.27), "
                                + "csk, "
                                + "kt")
                            .from("szamlazo_invoice_productFee")
                            .where("productID = " + s2[j][9].toString())
                            .build();
                        Object[][] s3 = App.db.select(query.getQuery());
                        
                        if (s3.length != 0)
                        {
                            if (String.valueOf(s[i][11]).equalsIgnoreCase(""))
                            {
                                sor = "\"\";\"Környezetvédelmi termékdíj (" + (String.valueOf(s3[0][2]).equalsIgnoreCase("85") ? "reklámpapír" : "csomagolószer") + ")\";\"\";" + String.valueOf(s3[0][1]).replace(".", ",") + ";\"kg\";"
                                    + String.valueOf(s3[0][2]).replace(".", ",") + ";" + String.valueOf(s3[0][3]).replace(".", ",") + ";27;"
                                    + String.valueOf(s3[0][4]).replace(".", ",") + ";" + String.valueOf(s3[0][5]).replace(".", ",");
                                data = sor.getBytes("UTF-8");
                            }
                            else
                            {
                                sor = "\"\";\"" + (!String.valueOf(s3[0][6]).isEmpty() ? "CSK: " + String.valueOf(s3[0][6]) : (!String.valueOf(s3[0][7]).isEmpty() ? "KT: " + String.valueOf(s3[0][7]) : ""))
                                    + "\";\"\";" + String.valueOf(s3[0][1]).replace(".", ",") + ";\"kg\";";
                                data = sor.getBytes("UTF-8");
                            }
                            
                            dos.write(data);
                            dos.writeChars(sep);
                        }
                    }

                    dos.writeChars(sep);
                    dos.writeChars(sep);
                }
            }

            fos.close();
            dos.close();
        }
        catch (IOException ex)
        {
            HibaDialog h = new HibaDialog("A csv nem jött létre!\nHiba: IOException.", "Ok", "");
        }
        catch (Exception ex)
        {
            HibaDialog h = new HibaDialog("A csv nem jött létre!\nHiba: Exception.", "Ok", "");
            ex.printStackTrace();
        }
    }
    
    private void kijeloltTermekdijKimutatasKeszitese(File f)
    {
        FileOutputStream fos;
        DataOutputStream dos;
        String sep = "\n";
        byte[] data;
        int[] rows = table_Invoices.getSelectedRows();
        String whereText = "";
        
        for (int i = 0; i < rows.length; i++)
        {
            whereText += "sza.invoiceNumber = '" + String.valueOf(table_Invoices.getValueAt(rows[i], 0)) + "' || ";
        }
        
        whereText += "0";
        
        Query query = new Query.QueryBuilder()
            .select("sza.invoiceNumber, " // 0
                + "(SELECT sz.customerName FROM szamlazo_invoices sz WHERE sz.invoiceNumber = sza.invoiceNumber), " // 1
                + "(SELECT sz.completionDate FROM szamlazo_invoices sz WHERE sz.invoiceNumber = sza.invoiceNumber), " // 2
                + "sza.productName, " // 3
                // Math.round(width / 1000.0 * height / 1000.0 * unitWeight / 1000.0 * copy * 1000000.0) / 1000000.0
                + "IF(td.totalWeight != 0, td.totalWeight, ROUND(td.width / 1000.0 * td.height / 1000.0 * td.unitWeight / 1000.0 * copy * 1000000.0) / 1000000.0), " // 4
                + "'kg', " // 5
                + "td.productFee, " // 6
                + "'Ft/kg'," // 7
                + "ROUND(td.productFee * IF(td.totalWeight != 0, td.totalWeight, ROUND(td.width / 1000.0 * td.height / 1000.0 * td.unitWeight / 1000.0 * copy * 1000000.0) / 1000000.0)), " // 8
                + "'Ft', " // 9
                + "(SELECT IF(sz.takeoverType != \"\", 'igen', 'nem') FROM szamlazo_invoices sz WHERE sz.invoiceNumber = sza.invoiceNumber), " // 10
                + "td.csk ")
            .from("szamlazo_invoice_products sza, szamlazo_invoice_productFee td ")
            .where("(" + whereText + ") && sza.id = td.productID")
            .order("sza.id ASC")
            .build();
        Object[][] select = App.db.select(query.getQuery());

        try
        {
            fos = new FileOutputStream(f);
            dos = new DataOutputStream(fos);

            String[] header = {"Számla sorszám", "Vevő", "Teljesítés dátum", "Termék", "Össz.súly", "kg", "Termékdíj", "Ft/kg", "Össz.termékdíj", "Ft", "Átvállalt", "CSK/KT kód"};
            
            for (int i = 0; i < header.length; i++)
            {
                String fejlec = "\"" + header[i] + "\";";
                data = fejlec.getBytes("UTF-8");
                dos.write(data);
            }
            
            dos.writeChars(sep);
            
            for (int i = 0; i < select.length; i++)
            {
                String sor = "\"" + String.valueOf(select[i][0]) + "\";\"" + String.valueOf(select[i][1]) + "\";\"" + String.valueOf(select[i][2]) + "\";\"" + String.valueOf(select[i][3]) + "\";" + String.valueOf(select[i][4]).replace(".", ",") + ";\""
                    + String.valueOf(select[i][5]) + "\";" + String.valueOf(select[i][6]).replace(".", ",") + ";\"" + String.valueOf(select[i][7]) + "\";" + String.valueOf(select[i][8]).replace(".", ",") + ";"
                    + "\"" + String.valueOf(select[i][9]) + "\";\"" + String.valueOf(select[i][10]) + "\";\"" + String.valueOf(select[i][11]) + "\"";
                data = sor.getBytes("UTF-8");
                dos.write(data);
                dos.writeChars(sep);
            }
            
            fos.close();
            dos.close();
        }
        catch (IOException ex)
        {
            HibaDialog h = new HibaDialog("A csv nem jött létre!\nHiba: IOException.", "Ok", "");
        }
        catch (Exception ex)
        {
            HibaDialog h = new HibaDialog("A csv nem jött létre!\nHiba: Exception.", "Ok", "");
        }
    }
    
    private boolean checkIfNothingSelected()
    {
        return normal.isSelected()
            || helyesbito.isSelected()
            || helyesbitett.isSelected()
            /*|| kintlevosegCheckBoxInAdvancedSearch.isSelected()*/
            || nemNyomtatottCheckBox.isSelected();
    }
    
    class FrissitesThread extends Thread
    {
        int count = 0;

        public void setCount()
        {
            this.count = 1;
        }

        public FrissitesThread()
        {
            start();
        }

        @Override
        public void run()
        {
            while (true)
            {
                try
                {
                    Thread.sleep(250);
                    if (count != 0)
                    {
                        count++;
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                
                if (count == 4)
                {
                    frissites();
                    count = 0;
                }
            }
        }
    }

    class ScrollAdjustmentListener implements AdjustmentListener
    {
        private JScrollPane pane;

        public ScrollAdjustmentListener(JScrollPane pane)
        {
            this.pane = pane;
        }

        public void adjustmentValueChanged(AdjustmentEvent evt)
        {
            int value = evt.getValue();
            pane.getHorizontalScrollBar().setValue(value);
        }
    }

    class ElonezetThread extends Thread
    {
        public ElonezetThread()
        {
            start();
        }

        @Override
        public void run()
        {
            int row = table_Invoices.getSelectedRow();
            String comment = String.valueOf(table_Invoices.getValueAt(row, 14));
        
            InvoiceType invoiceType = Invoice.InvoiceType.ORIGINAL;
            
            Invoice invoice = new Invoice(Invoice.INVOICE, invoiceType, "szamlazo_invoices", nyomtatasAzon);
            
            if(comment.equals("2"))
            {
                invoice.setInvoiceType(InvoiceType.STORNO);
            }

            ElonezetDialog e = new ElonezetDialog(invoice, Integer.parseInt(nyomtatasPeldany.getText()), ElonezetDialog.ELONEZET);

            folyamatbanDialog.setVisible(false);
        }
    }

    class SzamlaAdatokThread extends Thread
    {
        private int row;

        public SzamlaAdatokThread(int row)
        {
            this.row = row;
            start();
        }

        @Override
        public void run()
        {
            SzamlaAdatokDialog sz = new SzamlaAdatokDialog(Invoice.INVOICE, Invoice.InvoiceType.ORIGINAL, "szamlazo_invoices", String.valueOf(table_Invoices.getValueAt(row, 18)));
            
            folyamatbanDialog.setVisible(false);
        }
    }

    class PdfThread extends Thread
    {
        private int row;

        public PdfThread(int row)
        {
            this.row = row;
            start();
        }

        @Override
        public void run()
        {
            String indentifier = String.valueOf(table_Invoices.getValueAt(row, 18));
            Invoice invoice = new Invoice(Invoice.INVOICE, Invoice.InvoiceType.ORIGINAL, "szamlazo_invoices", indentifier);

            ElonezetDialog e = new ElonezetDialog(invoice, 1, ElonezetDialog.PDF);
            
            folyamatbanDialog.setVisible(false);
        }
    }

    class TeljesitesIgazolasNyomtatasThread extends Thread
    {

        private int row;

        public TeljesitesIgazolasNyomtatasThread(int row)
        {
            this.row = row;
            start();
        }

        @Override
        public void run()
        {
            String indentifier = table_Invoices.getValueAt(row, 18).toString();
            Invoice invoice = new Invoice(Invoice.COMPLETION_CERTIFICATE, Invoice.InvoiceType.ORIGINAL, "szamlazo_invoices", indentifier);
            //InvoiceView view = new InvoiceView(invoice);
            ResizeableInvoiceView view = new ResizeableInvoiceView(invoice);

            //folyamatbanDialog.setVisible(false);
        }
    }
}