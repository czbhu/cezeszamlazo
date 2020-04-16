package cezeszamlazo;

import cezeszamlazo.controller.Functions;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import org.jdesktop.application.Application;
import cezeszamlazo.database.Query;
import cezeszamlazo.kintlevoseg.KintlevosegLevel;
import cezeszamlazo.kintlevoseg.KintlevosegLevelHtmlEditor;
import cezeszamlazo.interfaces.SharedValues;
import cezeszamlazo.model.LogModel;
import cezeszamlazo.views.ContactsView;
import cezeszamlazo.views.CustomersView;
import cezeszamlazo.views.InvoiceSketchesView;
import cezeszamlazo.views.LogsView;
import cezeszamlazo.views.ResizeableInvoiceView;
import cezeszamlazo.views.NewView;
import controller.Database;
import controller.DatabaseHost;
import invoice.Invoice;

/**
 * The application's main frame.
 */
public class View extends FrameView implements SharedValues
{
    // frame-ek
    private SzamlaCsoportokFrame szamlaCsoportokFrame;
    private SzamlakFrame szamlakFrame;
    private DijbekerokFrame dijbekerokFrame;
    private InvoiceSketchesView sketches;
    private AdatszolgaltatasFrame adatszolgaltatasFrame;
    private TermekCsoportokFrame termekCsoportokFrame;
    private TermekekFrame termekekFrame;
    private CustomersView customersView;
    private ContactsView contactsView;
    private VtszTeszorFrame vtszTeszorFrame;
    
    // egyéb
    private ResourceMap rm = Application.getInstance(cezeszamlazo.App.class).getContext().getResourceMap(View.class);

    public View(SingleFrameApplication app)
    {
        super(app);

        App.db = new Database(DatabaseHost.Type.ONLINE, DatabaseHost.Host.SZAMLAZO);
        App.db.Connect();
        
        App.pixi = new Database(DatabaseHost.Type.ONLINE, DatabaseHost.Host.PIXI);
        App.pixi.Connect();

        userOlvasas();
        
        if (App.user == null)
        {
            LoginDialog l = new LoginDialog();
            
            if (l.getReturnStatus() == 1)
            {
                userMentes();
            }
            else
            {
                getApplication().exit();
            }
        }

        if (App.args.length != 0)
        {
            frissitesKeres();
            
            ResizeableInvoiceView view = new ResizeableInvoiceView(Invoice.INVOICE);
            
            if(view.getReturnStatus() == ResizeableInvoiceView.RET_CANCEL)
            {
                System.exit(0);
            }
        }

        App.args = new String[0];

        initComponents();

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        java.net.URL url = ClassLoader.getSystemResource("cezeszamlazo/resources/icon.png");
        java.awt.Image img = toolkit.createImage(url);
        getFrame().setIconImage(img);
        
        Dimension screenSize = toolkit.getScreenSize();
        int x = (screenSize.width - this.getFrame().getWidth()) / 2;
        int y = (screenSize.height - this.getFrame().getHeight()) / 2;
        
        this.getFrame().setLocation(x, y);

        beallitasokAlPanel.setVisible(false);

        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        
        messageTimer = new Timer(messageTimeout, new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                statusMessageLabel.setText("");
            }
        });
        
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        
        for (int i = 0; i < busyIcons.length; i++)
        {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener()
        {
            @Override
            public void propertyChange(java.beans.PropertyChangeEvent evt)
            {
                String propertyName = evt.getPropertyName();
                
                if("started".equals(propertyName))
                {
                    if(!busyIconTimer.isRunning())
                    {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                }
                else if("done".equals(propertyName))
                {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                }
                else if("message".equals(propertyName))
                {
                    String text = (String) (evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                }
                else if("progress".equals(propertyName))
                {
                    int value = (Integer) (evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
        
        UpdateLogButton();
    }

    @Action
    public void showAboutBox()
    {
        if(aboutBox == null)
        {
            JFrame mainFrame = App.getApplication().getMainFrame();
            aboutBox = new AboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        
        App.getApplication().show(aboutBox);
    }
    
    private void UpdateLogButton()
    {
        if(LogModel.isNewLog())
        {
            button_Logs.setText("Log (új log)");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        szamlakPanel = new javax.swing.JPanel();
        SzamlakButton = new javax.swing.JLabel();
        ujProFormaPanel = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        proFormaPanel = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        panel_NewSketch = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        panel_Sketches = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        ugyfelekPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        termekekPanel = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        beallitasokPanel = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        beallitasokAlPanel = new javax.swing.JPanel();
        szamlaCsoportokPanel = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        termekCsoportokPanel = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        vtszTeszorPanel = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        szamlaLablecPanel = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        szamlaLablecPanel1 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        szamlaLablecPanel2 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        InvoiceEmailEditor = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        szamlaLablecPanel4 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        adatszolgaltatasPanel = new javax.swing.JPanel();
        adatszolgaltatas = new javax.swing.JLabel();
        kapocsolattartokPanel = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        button_NewInvoice = new javax.swing.JButton();
        button_Logs = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();

        mainPanel.setMinimumSize(new java.awt.Dimension(600, 700));
        mainPanel.setName("mainPanel"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(cezeszamlazo.App.class).getContext().getResourceMap(View.class);
        szamlakPanel.setBorder(javax.swing.BorderFactory.createLineBorder(resourceMap.getColor("szamlakPanel.border.lineColor"))); // NOI18N
        szamlakPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        szamlakPanel.setName("szamlakPanel"); // NOI18N
        szamlakPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                szamlakPanelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                szamlakPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                szamlakPanelMouseExited(evt);
            }
        });

        SzamlakButton.setFont(resourceMap.getFont("SzamlakButton.font")); // NOI18N
        SzamlakButton.setIcon(resourceMap.getIcon("SzamlakButton.icon")); // NOI18N
        SzamlakButton.setText(resourceMap.getString("SzamlakButton.text")); // NOI18N
        SzamlakButton.setName("SzamlakButton"); // NOI18N

        javax.swing.GroupLayout szamlakPanelLayout = new javax.swing.GroupLayout(szamlakPanel);
        szamlakPanel.setLayout(szamlakPanelLayout);
        szamlakPanelLayout.setHorizontalGroup(
            szamlakPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(szamlakPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(SzamlakButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        szamlakPanelLayout.setVerticalGroup(
            szamlakPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(szamlakPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(SzamlakButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        ujProFormaPanel.setBorder(javax.swing.BorderFactory.createLineBorder(resourceMap.getColor("ujProFormaPanel.border.lineColor"))); // NOI18N
        ujProFormaPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ujProFormaPanel.setName("ujProFormaPanel"); // NOI18N
        ujProFormaPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ujProFormaPanelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ujProFormaPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ujProFormaPanelMouseExited(evt);
            }
        });

        jLabel15.setFont(resourceMap.getFont("jLabel15.font")); // NOI18N
        jLabel15.setIcon(resourceMap.getIcon("jLabel15.icon")); // NOI18N
        jLabel15.setText(resourceMap.getString("jLabel15.text")); // NOI18N
        jLabel15.setName("jLabel15"); // NOI18N

        javax.swing.GroupLayout ujProFormaPanelLayout = new javax.swing.GroupLayout(ujProFormaPanel);
        ujProFormaPanel.setLayout(ujProFormaPanelLayout);
        ujProFormaPanelLayout.setHorizontalGroup(
            ujProFormaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ujProFormaPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel15)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        ujProFormaPanelLayout.setVerticalGroup(
            ujProFormaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ujProFormaPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel15)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        proFormaPanel.setBorder(javax.swing.BorderFactory.createLineBorder(resourceMap.getColor("proFormaPanel.border.lineColor"))); // NOI18N
        proFormaPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        proFormaPanel.setName("proFormaPanel"); // NOI18N
        proFormaPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                proFormaPanelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                proFormaPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                proFormaPanelMouseExited(evt);
            }
        });

        jLabel16.setFont(resourceMap.getFont("jLabel16.font")); // NOI18N
        jLabel16.setIcon(resourceMap.getIcon("jLabel16.icon")); // NOI18N
        jLabel16.setText(resourceMap.getString("jLabel16.text")); // NOI18N
        jLabel16.setName("jLabel16"); // NOI18N

        javax.swing.GroupLayout proFormaPanelLayout = new javax.swing.GroupLayout(proFormaPanel);
        proFormaPanel.setLayout(proFormaPanelLayout);
        proFormaPanelLayout.setHorizontalGroup(
            proFormaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(proFormaPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        proFormaPanelLayout.setVerticalGroup(
            proFormaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(proFormaPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panel_NewSketch.setBorder(javax.swing.BorderFactory.createLineBorder(resourceMap.getColor("panel_NewSketch.border.lineColor"))); // NOI18N
        panel_NewSketch.setName("panel_NewSketch"); // NOI18N
        panel_NewSketch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panel_NewSketchMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panel_NewSketchMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                panel_NewSketchMouseExited(evt);
            }
        });

        jLabel2.setFont(resourceMap.getFont("jLabel2.font")); // NOI18N
        jLabel2.setIcon(resourceMap.getIcon("jLabel2.icon")); // NOI18N
        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        javax.swing.GroupLayout panel_NewSketchLayout = new javax.swing.GroupLayout(panel_NewSketch);
        panel_NewSketch.setLayout(panel_NewSketchLayout);
        panel_NewSketchLayout.setHorizontalGroup(
            panel_NewSketchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_NewSketchLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panel_NewSketchLayout.setVerticalGroup(
            panel_NewSketchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_NewSketchLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                .addContainerGap())
        );

        panel_Sketches.setBorder(javax.swing.BorderFactory.createLineBorder(resourceMap.getColor("panel_Sketches.border.lineColor"))); // NOI18N
        panel_Sketches.setName("panel_Sketches"); // NOI18N
        panel_Sketches.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panel_SketchesMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panel_SketchesMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                panel_SketchesMouseExited(evt);
            }
        });

        jLabel22.setFont(resourceMap.getFont("jLabel22.font")); // NOI18N
        jLabel22.setIcon(resourceMap.getIcon("jLabel22.icon")); // NOI18N
        jLabel22.setText(resourceMap.getString("jLabel22.text")); // NOI18N
        jLabel22.setName("jLabel22"); // NOI18N

        javax.swing.GroupLayout panel_SketchesLayout = new javax.swing.GroupLayout(panel_Sketches);
        panel_Sketches.setLayout(panel_SketchesLayout);
        panel_SketchesLayout.setHorizontalGroup(
            panel_SketchesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_SketchesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panel_SketchesLayout.setVerticalGroup(
            panel_SketchesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_SketchesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                .addContainerGap())
        );

        ugyfelekPanel.setBorder(javax.swing.BorderFactory.createLineBorder(resourceMap.getColor("ugyfelekPanel.border.lineColor"))); // NOI18N
        ugyfelekPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ugyfelekPanel.setFont(resourceMap.getFont("ugyfelekPanel.font")); // NOI18N
        ugyfelekPanel.setName("ugyfelekPanel"); // NOI18N
        ugyfelekPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ugyfelekPanelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ugyfelekPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ugyfelekPanelMouseExited(evt);
            }
        });

        jLabel1.setFont(resourceMap.getFont("jLabel1.font")); // NOI18N
        jLabel1.setIcon(resourceMap.getIcon("jLabel1.icon")); // NOI18N
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        javax.swing.GroupLayout ugyfelekPanelLayout = new javax.swing.GroupLayout(ugyfelekPanel);
        ugyfelekPanel.setLayout(ugyfelekPanelLayout);
        ugyfelekPanelLayout.setHorizontalGroup(
            ugyfelekPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ugyfelekPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        ugyfelekPanelLayout.setVerticalGroup(
            ugyfelekPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ugyfelekPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        termekekPanel.setBorder(javax.swing.BorderFactory.createLineBorder(resourceMap.getColor("termekekPanel.border.lineColor"))); // NOI18N
        termekekPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        termekekPanel.setName("termekekPanel"); // NOI18N
        termekekPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                termekekPanelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                termekekPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                termekekPanelMouseExited(evt);
            }
        });

        jLabel7.setFont(resourceMap.getFont("jLabel7.font")); // NOI18N
        jLabel7.setIcon(resourceMap.getIcon("jLabel7.icon")); // NOI18N
        jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        javax.swing.GroupLayout termekekPanelLayout = new javax.swing.GroupLayout(termekekPanel);
        termekekPanel.setLayout(termekekPanelLayout);
        termekekPanelLayout.setHorizontalGroup(
            termekekPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(termekekPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        termekekPanelLayout.setVerticalGroup(
            termekekPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(termekekPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        beallitasokPanel.setBorder(javax.swing.BorderFactory.createLineBorder(resourceMap.getColor("beallitasokPanel.border.lineColor"))); // NOI18N
        beallitasokPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        beallitasokPanel.setName("beallitasokPanel"); // NOI18N
        beallitasokPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                beallitasokPanelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                beallitasokPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                beallitasokPanelMouseExited(evt);
            }
        });

        jLabel10.setFont(resourceMap.getFont("jLabel10.font")); // NOI18N
        jLabel10.setForeground(resourceMap.getColor("jLabel10.foreground")); // NOI18N
        jLabel10.setIcon(resourceMap.getIcon("jLabel10.icon")); // NOI18N
        jLabel10.setText(resourceMap.getString("jLabel10.text")); // NOI18N
        jLabel10.setName("jLabel10"); // NOI18N

        javax.swing.GroupLayout beallitasokPanelLayout = new javax.swing.GroupLayout(beallitasokPanel);
        beallitasokPanel.setLayout(beallitasokPanelLayout);
        beallitasokPanelLayout.setHorizontalGroup(
            beallitasokPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(beallitasokPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        beallitasokPanelLayout.setVerticalGroup(
            beallitasokPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(beallitasokPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        beallitasokAlPanel.setBackground(resourceMap.getColor("beallitasokAlPanel.background")); // NOI18N
        beallitasokAlPanel.setBorder(javax.swing.BorderFactory.createLineBorder(resourceMap.getColor("beallitasokAlPanel.border.lineColor"))); // NOI18N
        beallitasokAlPanel.setMinimumSize(new java.awt.Dimension(180, 580));
        beallitasokAlPanel.setName("beallitasokAlPanel"); // NOI18N

        szamlaCsoportokPanel.setBorder(javax.swing.BorderFactory.createLineBorder(resourceMap.getColor("szamlaCsoportokPanel.border.lineColor"))); // NOI18N
        szamlaCsoportokPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        szamlaCsoportokPanel.setEnabled(false);
        szamlaCsoportokPanel.setName("szamlaCsoportokPanel"); // NOI18N
        szamlaCsoportokPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                szamlaCsoportokPanelMouseClicked(evt);
            }
        });

        jLabel5.setFont(resourceMap.getFont("jLabel5.font")); // NOI18N
        jLabel5.setIcon(resourceMap.getIcon("jLabel5.icon")); // NOI18N
        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setEnabled(false);
        jLabel5.setName("jLabel5"); // NOI18N

        javax.swing.GroupLayout szamlaCsoportokPanelLayout = new javax.swing.GroupLayout(szamlaCsoportokPanel);
        szamlaCsoportokPanel.setLayout(szamlaCsoportokPanelLayout);
        szamlaCsoportokPanelLayout.setHorizontalGroup(
            szamlaCsoportokPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(szamlaCsoportokPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addContainerGap(77, Short.MAX_VALUE))
        );
        szamlaCsoportokPanelLayout.setVerticalGroup(
            szamlaCsoportokPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(szamlaCsoportokPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        termekCsoportokPanel.setBorder(javax.swing.BorderFactory.createLineBorder(resourceMap.getColor("termekCsoportokPanel.border.lineColor"))); // NOI18N
        termekCsoportokPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        termekCsoportokPanel.setName("termekCsoportokPanel"); // NOI18N
        termekCsoportokPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                termekCsoportokPanelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                termekCsoportokPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                termekCsoportokPanelMouseExited(evt);
            }
        });

        jLabel6.setFont(resourceMap.getFont("jLabel6.font")); // NOI18N
        jLabel6.setIcon(resourceMap.getIcon("jLabel6.icon")); // NOI18N
        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        javax.swing.GroupLayout termekCsoportokPanelLayout = new javax.swing.GroupLayout(termekCsoportokPanel);
        termekCsoportokPanel.setLayout(termekCsoportokPanelLayout);
        termekCsoportokPanelLayout.setHorizontalGroup(
            termekCsoportokPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(termekCsoportokPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addContainerGap(70, Short.MAX_VALUE))
        );
        termekCsoportokPanelLayout.setVerticalGroup(
            termekCsoportokPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(termekCsoportokPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(resourceMap.getColor("jPanel3.border.lineColor"))); // NOI18N
        jPanel3.setName("jPanel3"); // NOI18N

        jLabel12.setFont(resourceMap.getFont("jLabel12.font")); // NOI18N
        jLabel12.setIcon(resourceMap.getIcon("jLabel12.icon")); // NOI18N
        jLabel12.setText(resourceMap.getString("jLabel12.text")); // NOI18N
        jLabel12.setEnabled(false);
        jLabel12.setName("jLabel12"); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12)
                .addContainerGap(141, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(resourceMap.getColor("jPanel4.border.lineColor"))); // NOI18N
        jPanel4.setName("jPanel4"); // NOI18N

        jLabel13.setFont(resourceMap.getFont("jLabel13.font")); // NOI18N
        jLabel13.setIcon(resourceMap.getIcon("jLabel13.icon")); // NOI18N
        jLabel13.setText(resourceMap.getString("jLabel13.text")); // NOI18N
        jLabel13.setEnabled(false);
        jLabel13.setName("jLabel13"); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13)
                .addContainerGap(161, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        vtszTeszorPanel.setBorder(javax.swing.BorderFactory.createLineBorder(resourceMap.getColor("vtszTeszorPanel.border.lineColor"))); // NOI18N
        vtszTeszorPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        vtszTeszorPanel.setName("vtszTeszorPanel"); // NOI18N
        vtszTeszorPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                vtszTeszorPanelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                vtszTeszorPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                vtszTeszorPanelMouseExited(evt);
            }
        });

        jLabel14.setFont(resourceMap.getFont("jLabel14.font")); // NOI18N
        jLabel14.setIcon(resourceMap.getIcon("jLabel14.icon")); // NOI18N
        jLabel14.setText(resourceMap.getString("jLabel14.text")); // NOI18N
        jLabel14.setName("jLabel14"); // NOI18N

        javax.swing.GroupLayout vtszTeszorPanelLayout = new javax.swing.GroupLayout(vtszTeszorPanel);
        vtszTeszorPanel.setLayout(vtszTeszorPanelLayout);
        vtszTeszorPanelLayout.setHorizontalGroup(
            vtszTeszorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(vtszTeszorPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel14)
                .addContainerGap(40, Short.MAX_VALUE))
        );
        vtszTeszorPanelLayout.setVerticalGroup(
            vtszTeszorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(vtszTeszorPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel14)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        szamlaLablecPanel.setBorder(javax.swing.BorderFactory.createLineBorder(resourceMap.getColor("szamlaLablecPanel.border.lineColor"))); // NOI18N
        szamlaLablecPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        szamlaLablecPanel.setName("szamlaLablecPanel"); // NOI18N
        szamlaLablecPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                szamlaLablecPanelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                szamlaLablecPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                szamlaLablecPanelMouseExited(evt);
            }
        });

        jLabel17.setFont(resourceMap.getFont("jLabel17.font")); // NOI18N
        jLabel17.setText(resourceMap.getString("jLabel17.text")); // NOI18N
        jLabel17.setName("jLabel17"); // NOI18N

        javax.swing.GroupLayout szamlaLablecPanelLayout = new javax.swing.GroupLayout(szamlaLablecPanel);
        szamlaLablecPanel.setLayout(szamlaLablecPanelLayout);
        szamlaLablecPanelLayout.setHorizontalGroup(
            szamlaLablecPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(szamlaLablecPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(48, Short.MAX_VALUE))
        );
        szamlaLablecPanelLayout.setVerticalGroup(
            szamlaLablecPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(szamlaLablecPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        szamlaLablecPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(resourceMap.getColor("szamlaLablecPanel1.border.lineColor"))); // NOI18N
        szamlaLablecPanel1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        szamlaLablecPanel1.setName("szamlaLablecPanel1"); // NOI18N
        szamlaLablecPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                szamlaLablecPanel1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                szamlaLablecPanel1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                szamlaLablecPanel1MouseExited(evt);
            }
        });

        jLabel18.setFont(resourceMap.getFont("jLabel18.font")); // NOI18N
        jLabel18.setText(resourceMap.getString("jLabel18.text")); // NOI18N
        jLabel18.setName("jLabel18"); // NOI18N

        javax.swing.GroupLayout szamlaLablecPanel1Layout = new javax.swing.GroupLayout(szamlaLablecPanel1);
        szamlaLablecPanel1.setLayout(szamlaLablecPanel1Layout);
        szamlaLablecPanel1Layout.setHorizontalGroup(
            szamlaLablecPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(szamlaLablecPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        szamlaLablecPanel1Layout.setVerticalGroup(
            szamlaLablecPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(szamlaLablecPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel18)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        szamlaLablecPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(resourceMap.getColor("szamlaLablecPanel2.border.lineColor"))); // NOI18N
        szamlaLablecPanel2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        szamlaLablecPanel2.setName("szamlaLablecPanel2"); // NOI18N
        szamlaLablecPanel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                szamlaLablecPanel2MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                szamlaLablecPanel2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                szamlaLablecPanel2MouseExited(evt);
            }
        });

        jLabel19.setFont(resourceMap.getFont("jLabel19.font")); // NOI18N
        jLabel19.setText(resourceMap.getString("jLabel19.text")); // NOI18N
        jLabel19.setName("jLabel19"); // NOI18N

        javax.swing.GroupLayout szamlaLablecPanel2Layout = new javax.swing.GroupLayout(szamlaLablecPanel2);
        szamlaLablecPanel2.setLayout(szamlaLablecPanel2Layout);
        szamlaLablecPanel2Layout.setHorizontalGroup(
            szamlaLablecPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(szamlaLablecPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        szamlaLablecPanel2Layout.setVerticalGroup(
            szamlaLablecPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(szamlaLablecPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        InvoiceEmailEditor.setBorder(javax.swing.BorderFactory.createLineBorder(resourceMap.getColor("InvoiceEmailEditor.border.lineColor"))); // NOI18N
        InvoiceEmailEditor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        InvoiceEmailEditor.setName("InvoiceEmailEditor"); // NOI18N
        InvoiceEmailEditor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                InvoiceEmailEditorMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                InvoiceEmailEditorMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                InvoiceEmailEditorMouseExited(evt);
            }
        });

        jLabel20.setFont(resourceMap.getFont("jLabel20.font")); // NOI18N
        jLabel20.setText(resourceMap.getString("jLabel20.text")); // NOI18N
        jLabel20.setName("jLabel20"); // NOI18N

        javax.swing.GroupLayout InvoiceEmailEditorLayout = new javax.swing.GroupLayout(InvoiceEmailEditor);
        InvoiceEmailEditor.setLayout(InvoiceEmailEditorLayout);
        InvoiceEmailEditorLayout.setHorizontalGroup(
            InvoiceEmailEditorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(InvoiceEmailEditorLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        InvoiceEmailEditorLayout.setVerticalGroup(
            InvoiceEmailEditorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(InvoiceEmailEditorLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel20)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        szamlaLablecPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(resourceMap.getColor("szamlaLablecPanel4.border.lineColor"))); // NOI18N
        szamlaLablecPanel4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        szamlaLablecPanel4.setName("szamlaLablecPanel4"); // NOI18N
        szamlaLablecPanel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                szamlaLablecPanel4MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                szamlaLablecPanel4MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                szamlaLablecPanel4MouseExited(evt);
            }
        });

        jLabel23.setFont(resourceMap.getFont("jLabel23.font")); // NOI18N
        jLabel23.setText(resourceMap.getString("jLabel23.text")); // NOI18N
        jLabel23.setName("jLabel23"); // NOI18N

        javax.swing.GroupLayout szamlaLablecPanel4Layout = new javax.swing.GroupLayout(szamlaLablecPanel4);
        szamlaLablecPanel4.setLayout(szamlaLablecPanel4Layout);
        szamlaLablecPanel4Layout.setHorizontalGroup(
            szamlaLablecPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(szamlaLablecPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        szamlaLablecPanel4Layout.setVerticalGroup(
            szamlaLablecPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(szamlaLablecPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel23)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout beallitasokAlPanelLayout = new javax.swing.GroupLayout(beallitasokAlPanel);
        beallitasokAlPanel.setLayout(beallitasokAlPanelLayout);
        beallitasokAlPanelLayout.setHorizontalGroup(
            beallitasokAlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(beallitasokAlPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(beallitasokAlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(szamlaCsoportokPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(termekCsoportokPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(vtszTeszorPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(szamlaLablecPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(szamlaLablecPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(szamlaLablecPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(InvoiceEmailEditor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(szamlaLablecPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        beallitasokAlPanelLayout.setVerticalGroup(
            beallitasokAlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(beallitasokAlPanelLayout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(szamlaCsoportokPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(termekCsoportokPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(vtszTeszorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(szamlaLablecPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(szamlaLablecPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(szamlaLablecPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(szamlaLablecPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(InvoiceEmailEditor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        adatszolgaltatasPanel.setBorder(javax.swing.BorderFactory.createLineBorder(resourceMap.getColor("adatszolgaltatasPanel.border.lineColor"))); // NOI18N
        adatszolgaltatasPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        adatszolgaltatasPanel.setName("adatszolgaltatasPanel"); // NOI18N
        adatszolgaltatasPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                adatszolgaltatasPanelMouseClicked(evt);
            }
        });

        adatszolgaltatas.setFont(resourceMap.getFont("adatszolgaltatas.font")); // NOI18N
        adatszolgaltatas.setText(resourceMap.getString("adatszolgaltatas.text")); // NOI18N
        adatszolgaltatas.setName("adatszolgaltatas"); // NOI18N

        javax.swing.GroupLayout adatszolgaltatasPanelLayout = new javax.swing.GroupLayout(adatszolgaltatasPanel);
        adatszolgaltatasPanel.setLayout(adatszolgaltatasPanelLayout);
        adatszolgaltatasPanelLayout.setHorizontalGroup(
            adatszolgaltatasPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(adatszolgaltatasPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(adatszolgaltatas)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        adatszolgaltatasPanelLayout.setVerticalGroup(
            adatszolgaltatasPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, adatszolgaltatasPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(adatszolgaltatas)
                .addContainerGap())
        );

        kapocsolattartokPanel.setBorder(javax.swing.BorderFactory.createLineBorder(resourceMap.getColor("kapocsolattartokPanel.border.lineColor"))); // NOI18N
        kapocsolattartokPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        kapocsolattartokPanel.setName("kapocsolattartokPanel"); // NOI18N
        kapocsolattartokPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                kapocsolattartokPanelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                kapocsolattartokPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                kapocsolattartokPanelMouseExited(evt);
            }
        });

        jLabel21.setFont(resourceMap.getFont("jLabel21.font")); // NOI18N
        jLabel21.setIcon(resourceMap.getIcon("jLabel21.icon")); // NOI18N
        jLabel21.setText(resourceMap.getString("jLabel21.text")); // NOI18N
        jLabel21.setName("jLabel21"); // NOI18N

        javax.swing.GroupLayout kapocsolattartokPanelLayout = new javax.swing.GroupLayout(kapocsolattartokPanel);
        kapocsolattartokPanel.setLayout(kapocsolattartokPanelLayout);
        kapocsolattartokPanelLayout.setHorizontalGroup(
            kapocsolattartokPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kapocsolattartokPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(86, Short.MAX_VALUE))
        );
        kapocsolattartokPanelLayout.setVerticalGroup(
            kapocsolattartokPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kapocsolattartokPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        button_NewInvoice.setFont(resourceMap.getFont("button_NewInvoice.font")); // NOI18N
        button_NewInvoice.setIcon(resourceMap.getIcon("button_NewInvoice.icon")); // NOI18N
        button_NewInvoice.setText(resourceMap.getString("button_NewInvoice.text")); // NOI18N
        button_NewInvoice.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        button_NewInvoice.setMaximumSize(new java.awt.Dimension(73, 50));
        button_NewInvoice.setMinimumSize(new java.awt.Dimension(73, 50));
        button_NewInvoice.setName("button_NewInvoice"); // NOI18N
        button_NewInvoice.setPreferredSize(new java.awt.Dimension(73, 50));
        button_NewInvoice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_NewInvoiceActionPerformed(evt);
            }
        });

        button_Logs.setFont(resourceMap.getFont("button_Logs.font")); // NOI18N
        button_Logs.setText(resourceMap.getString("button_Logs.text")); // NOI18N
        button_Logs.setName("button_Logs"); // NOI18N
        button_Logs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_LogsActionPerformed(evt);
            }
        });

        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(button_Logs, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(adatszolgaltatasPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(beallitasokPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(termekekPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ugyfelekPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(szamlakPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(proFormaPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ujProFormaPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(kapocsolattartokPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panel_NewSketch, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panel_Sketches, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(button_NewInvoice, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(beallitasokAlPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addGap(164, 164, 164))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(button_NewInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(szamlakPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(ujProFormaPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(proFormaPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(panel_NewSketch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panel_Sketches, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(ugyfelekPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(kapocsolattartokPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(termekekPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(beallitasokPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(adatszolgaltatasPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(button_Logs)
                        .addContainerGap(31, Short.MAX_VALUE))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(beallitasokAlPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(cezeszamlazo.App.class).getContext().getActionMap(View.class, this);
        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setText(resourceMap.getString("exitMenuItem.text")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setText(resourceMap.getString("aboutMenuItem.text")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanelSeparator)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addGap(340, 340, 340)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusMessageLabel)
                    .addComponent(statusAnimationLabel)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents

    private void szamlakPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_szamlakPanelMouseEntered
        szamlakPanel.setBackground(Color.decode("#ABD043"));
    }//GEN-LAST:event_szamlakPanelMouseEntered

    private void ugyfelekPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ugyfelekPanelMouseEntered
        ugyfelekPanel.setBackground(Color.decode("#ABD043"));
    }//GEN-LAST:event_ugyfelekPanelMouseEntered

    private void szamlakPanelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_szamlakPanelMouseExited
        szamlakPanel.setBackground(Color.decode("#F0F0F0"));
    }//GEN-LAST:event_szamlakPanelMouseExited

    private void ugyfelekPanelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ugyfelekPanelMouseExited
        ugyfelekPanel.setBackground(Color.decode("#F0F0F0"));
    }//GEN-LAST:event_ugyfelekPanelMouseExited

    private void ugyfelekPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ugyfelekPanelMouseClicked
        if(customersView == null)
        {
            customersView = new CustomersView();
        }
        
        customersView.Open();
    }//GEN-LAST:event_ugyfelekPanelMouseClicked

    private void szamlakPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_szamlakPanelMouseClicked
        if (szamlakFrame == null)
        {
            szamlakFrame = new SzamlakFrame();    
        }
        
        szamlakFrame.nyit();
        
        UpdateLogButton();
    }//GEN-LAST:event_szamlakPanelMouseClicked

    private void termekekPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_termekekPanelMouseEntered
        termekekPanel.setBackground(Color.decode("#ABD043"));
    }//GEN-LAST:event_termekekPanelMouseEntered

    private void termekekPanelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_termekekPanelMouseExited
        termekekPanel.setBackground(Color.decode("#f0f0f0"));
    }//GEN-LAST:event_termekekPanelMouseExited

    private void beallitasokPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_beallitasokPanelMouseEntered
        beallitasokPanel.setBackground(Color.decode("#ABD043"));
    }//GEN-LAST:event_beallitasokPanelMouseEntered

    private void beallitasokPanelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_beallitasokPanelMouseExited
        beallitasokPanel.setBackground(Color.decode("#f0f0f0"));
    }//GEN-LAST:event_beallitasokPanelMouseExited

    private void termekekPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_termekekPanelMouseClicked
        if (termekekFrame == null)
        {
            termekekFrame = new TermekekFrame();
        }
        
        termekekFrame.nyit();
    }//GEN-LAST:event_termekekPanelMouseClicked

    private void beallitasokPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_beallitasokPanelMouseClicked
        beallitasokAlPanel.setVisible(!beallitasokAlPanel.isVisible());
        beallitasokAlPanel.setSize(400, 500);
    }//GEN-LAST:event_beallitasokPanelMouseClicked

    private void szamlaCsoportokPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_szamlaCsoportokPanelMouseClicked
        /*if (szamlaCsoportokFrame == null)
        {
            szamlaCsoportokFrame = new SzamlaCsoportokFrame();
        }
        
        szamlaCsoportokFrame.nyit();*/
    }//GEN-LAST:event_szamlaCsoportokPanelMouseClicked

    private void termekCsoportokPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_termekCsoportokPanelMouseEntered
        termekCsoportokPanel.setBackground(Color.decode("#ABD043"));
    }//GEN-LAST:event_termekCsoportokPanelMouseEntered

    private void termekCsoportokPanelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_termekCsoportokPanelMouseExited
        termekCsoportokPanel.setBackground(Color.decode("#F0F0F0"));
    }//GEN-LAST:event_termekCsoportokPanelMouseExited

    private void termekCsoportokPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_termekCsoportokPanelMouseClicked
        if (termekCsoportokFrame == null)
        {
            termekCsoportokFrame = new TermekCsoportokFrame();
        }
        
        termekCsoportokFrame.nyit();
    }//GEN-LAST:event_termekCsoportokPanelMouseClicked

    private void vtszTeszorPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_vtszTeszorPanelMouseEntered
        vtszTeszorPanel.setBackground(Color.decode("#ABD043"));
    }//GEN-LAST:event_vtszTeszorPanelMouseEntered

    private void vtszTeszorPanelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_vtszTeszorPanelMouseExited
        vtszTeszorPanel.setBackground(Color.decode("#F0F0F0"));
    }//GEN-LAST:event_vtszTeszorPanelMouseExited

    private void vtszTeszorPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_vtszTeszorPanelMouseClicked
        if (vtszTeszorFrame == null)
        {
            vtszTeszorFrame = new VtszTeszorFrame();
        }
        
        vtszTeszorFrame.nyit();
    }//GEN-LAST:event_vtszTeszorPanelMouseClicked

private void ujProFormaPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ujProFormaPanelMouseClicked
    ResizeableInvoiceView view = new ResizeableInvoiceView(Invoice.PROFORMA);
}//GEN-LAST:event_ujProFormaPanelMouseClicked

private void ujProFormaPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ujProFormaPanelMouseEntered
    ujProFormaPanel.setBackground(Color.decode("#ABD043"));
}//GEN-LAST:event_ujProFormaPanelMouseEntered

private void ujProFormaPanelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ujProFormaPanelMouseExited
    ujProFormaPanel.setBackground(Color.decode("#F0F0F0"));
}//GEN-LAST:event_ujProFormaPanelMouseExited

private void proFormaPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_proFormaPanelMouseClicked
    dijbekerokFrame = new DijbekerokFrame();
    dijbekerokFrame.nyit();
}//GEN-LAST:event_proFormaPanelMouseClicked

private void proFormaPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_proFormaPanelMouseEntered
    proFormaPanel.setBackground(Color.decode("#ABD043"));
}//GEN-LAST:event_proFormaPanelMouseEntered

private void proFormaPanelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_proFormaPanelMouseExited
    proFormaPanel.setBackground(Color.decode("#F0F0F0"));
}//GEN-LAST:event_proFormaPanelMouseExited

    private void adatszolgaltatasPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_adatszolgaltatasPanelMouseClicked
        if (adatszolgaltatasFrame == null)
        {
            adatszolgaltatasFrame = new AdatszolgaltatasFrame();
        }
        
        adatszolgaltatasFrame.nyit();
    }//GEN-LAST:event_adatszolgaltatasPanelMouseClicked

    private void szamlaLablecPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_szamlaLablecPanelMouseClicked
        SzamlaLablecDialog szamlaLablecDialog = new SzamlaLablecDialog();
    }//GEN-LAST:event_szamlaLablecPanelMouseClicked

    private void szamlaLablecPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_szamlaLablecPanelMouseEntered
        szamlaLablecPanel.setBackground(Color.decode("#ABD043"));
    }//GEN-LAST:event_szamlaLablecPanelMouseEntered

    private void szamlaLablecPanelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_szamlaLablecPanelMouseExited
        szamlaLablecPanel.setBackground(Color.decode("#F0F0F0"));
    }//GEN-LAST:event_szamlaLablecPanelMouseExited

    private void szamlaLablecPanel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_szamlaLablecPanel1MouseClicked
        UserDialog userDialog = new UserDialog();
    }//GEN-LAST:event_szamlaLablecPanel1MouseClicked

    private void szamlaLablecPanel1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_szamlaLablecPanel1MouseEntered
        szamlaLablecPanel1.setBackground(Color.decode("#ABD043"));
    }//GEN-LAST:event_szamlaLablecPanel1MouseEntered

    private void szamlaLablecPanel1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_szamlaLablecPanel1MouseExited
        szamlaLablecPanel1.setBackground(Color.decode("#F0F0F0"));
    }//GEN-LAST:event_szamlaLablecPanel1MouseExited

    private void szamlaLablecPanel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_szamlaLablecPanel2MouseClicked
        KintlevosegLevelHtmlEditor kintlevosegLevelHtmlEditorPDF = KintlevosegLevelHtmlEditor.create(KintlevosegLevel.Type.PDF, true);
        kintlevosegLevelHtmlEditorPDF.run();
    }//GEN-LAST:event_szamlaLablecPanel2MouseClicked

    private void szamlaLablecPanel2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_szamlaLablecPanel2MouseEntered
        szamlaLablecPanel2.setBackground(Color.decode("#ABD043"));
    }//GEN-LAST:event_szamlaLablecPanel2MouseEntered

    private void szamlaLablecPanel2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_szamlaLablecPanel2MouseExited
        szamlaLablecPanel2.setBackground(Color.decode("#F0F0F0"));
    }//GEN-LAST:event_szamlaLablecPanel2MouseExited

    private void InvoiceEmailEditorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_InvoiceEmailEditorMouseClicked
        KintlevosegLevelHtmlEditor kintlevosegLevelHtmlEditorEMAIL = KintlevosegLevelHtmlEditor.create(KintlevosegLevel.Type.EMAIL, false);
        kintlevosegLevelHtmlEditorEMAIL.run();
    }//GEN-LAST:event_InvoiceEmailEditorMouseClicked

    private void InvoiceEmailEditorMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_InvoiceEmailEditorMouseEntered
        InvoiceEmailEditor.setBackground(Color.decode("#ABD043"));
    }//GEN-LAST:event_InvoiceEmailEditorMouseEntered

    private void InvoiceEmailEditorMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_InvoiceEmailEditorMouseExited
        InvoiceEmailEditor.setBackground(Color.decode("#F0F0F0"));
    }//GEN-LAST:event_InvoiceEmailEditorMouseExited

    private void kapocsolattartokPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kapocsolattartokPanelMouseClicked
        if (contactsView == null)
        {
            contactsView = new ContactsView();
        }
        
        contactsView.Open();
    }//GEN-LAST:event_kapocsolattartokPanelMouseClicked

    private void kapocsolattartokPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kapocsolattartokPanelMouseEntered
        kapocsolattartokPanel.setBackground(Color.decode("#ABD043"));
    }//GEN-LAST:event_kapocsolattartokPanelMouseEntered

    private void kapocsolattartokPanelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kapocsolattartokPanelMouseExited
        kapocsolattartokPanel.setBackground(Color.decode("#F0F0F0"));
    }//GEN-LAST:event_kapocsolattartokPanelMouseExited

    private void szamlaLablecPanel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_szamlaLablecPanel4MouseClicked
        KintlevosegLevelHtmlEditor kintlevosegLevelHtmlEditorEMAIL = KintlevosegLevelHtmlEditor.create(KintlevosegLevel.Type.EMAIL, true);
        kintlevosegLevelHtmlEditorEMAIL.run();
    }//GEN-LAST:event_szamlaLablecPanel4MouseClicked

    private void szamlaLablecPanel4MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_szamlaLablecPanel4MouseEntered
        szamlaLablecPanel4.setBackground(Color.decode("#ABD043"));
    }//GEN-LAST:event_szamlaLablecPanel4MouseEntered

    private void szamlaLablecPanel4MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_szamlaLablecPanel4MouseExited
        szamlaLablecPanel4.setBackground(Color.decode("#F0F0F0"));
    }//GEN-LAST:event_szamlaLablecPanel4MouseExited

    private void panel_NewSketchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panel_NewSketchMouseClicked
        ResizeableInvoiceView view = new ResizeableInvoiceView(Invoice.SKETCH);
    }//GEN-LAST:event_panel_NewSketchMouseClicked

    private void panel_SketchesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panel_SketchesMouseClicked
        sketches = new InvoiceSketchesView();
        sketches.Open();
    }//GEN-LAST:event_panel_SketchesMouseClicked

    private void panel_NewSketchMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panel_NewSketchMouseEntered
        panel_NewSketch.setBackground(Color.decode("#ABD043"));
    }//GEN-LAST:event_panel_NewSketchMouseEntered

    private void panel_NewSketchMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panel_NewSketchMouseExited
        panel_NewSketch.setBackground(Color.decode("#F0F0F0"));
    }//GEN-LAST:event_panel_NewSketchMouseExited

    private void panel_SketchesMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panel_SketchesMouseEntered
        panel_Sketches.setBackground(Color.decode("#ABD043"));
    }//GEN-LAST:event_panel_SketchesMouseEntered

    private void panel_SketchesMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panel_SketchesMouseExited
        panel_Sketches.setBackground(Color.decode("#F0F0F0"));
    }//GEN-LAST:event_panel_SketchesMouseExited

    private void button_NewInvoiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_NewInvoiceActionPerformed
        ResizeableInvoiceView view = new ResizeableInvoiceView(Invoice.INVOICE);
        
        if(view.getReturnStatus() == ResizeableInvoiceView.RET_REOPEN)
        {
            button_NewInvoice.doClick();
        }
        
        UpdateLogButton();
    }//GEN-LAST:event_button_NewInvoiceActionPerformed

    private void button_LogsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_LogsActionPerformed
        LogsView view = new LogsView();
        button_Logs.setText("Log");
        //LogModel.addLog(this.getClass().getName(), "Leírás");
    }//GEN-LAST:event_button_LogsActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        NewView view = new NewView();
    }//GEN-LAST:event_jButton1ActionPerformed

    /*
        JSONObject json = new JSONObject();
        
        JSONArray orderIds = new JSONArray();
        
        String uniqid = "";
        
        String [] orderids = {"D 2019/167", "D 2019/166"};
        
        for(int i = 0; i < orderids.length; i++)
        {
            orderIds.add(orderids[i]);
            uniqid += orderids[i];
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
            sender.put("email", "sender@gmail.com");
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
            consignee.put("contact_name", "Címzett neve");
            consignee.put("phone", "Címzett telefonszám");
            consignee.put("email", "Címzett email");
            consignee.put("name", "");
        json.put("consignee", consignee);
        
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy.MM.dd");
        json.put("pickupDate", ft.format(new Date()));
        
        json.put("content", "content");
        
        json.put("codamount", "codamount");
        
        Query query = new Query.QueryBuilder()
            .select("gls_username, gls_password, gls_senderID")
            .from("szamlazo_suppliers")
            .where("serializationID = (SELECT id FROM szamlazo_szamla_sorszam WHERE appellation = 'DEMO')")
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
    */
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel InvoiceEmailEditor;
    private javax.swing.JLabel SzamlakButton;
    private javax.swing.JLabel adatszolgaltatas;
    private javax.swing.JPanel adatszolgaltatasPanel;
    private javax.swing.JPanel beallitasokAlPanel;
    private javax.swing.JPanel beallitasokPanel;
    private javax.swing.JButton button_Logs;
    private javax.swing.JButton button_NewInvoice;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel kapocsolattartokPanel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JPanel panel_NewSketch;
    private javax.swing.JPanel panel_Sketches;
    private javax.swing.JPanel proFormaPanel;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JPanel szamlaCsoportokPanel;
    private javax.swing.JPanel szamlaLablecPanel;
    private javax.swing.JPanel szamlaLablecPanel1;
    private javax.swing.JPanel szamlaLablecPanel2;
    private javax.swing.JPanel szamlaLablecPanel4;
    private javax.swing.JPanel szamlakPanel;
    private javax.swing.JPanel termekCsoportokPanel;
    private javax.swing.JPanel termekekPanel;
    private javax.swing.JPanel ugyfelekPanel;
    private javax.swing.JPanel ujProFormaPanel;
    private javax.swing.JPanel vtszTeszorPanel;
    // End of variables declaration//GEN-END:variables
    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;
    private JDialog aboutBox;

    public void frissitesKeres()
    {
        try
        {
            System.out.println("Új verzió keresése... (View.java/frissitesKeres())");
            
            Query query = new Query.QueryBuilder()
                .select("verzio, leiras")
                .from("szamlazo_versions")
                .where("1")
                .order("id DESC LIMIT 1")
                .build();
            Object[][] newversion = App.db.select(query.getQuery());
            
            String newversionString = Functions.getStringFromObject(newversion[0][0]);
            //if (!String.valueOf(newversion[0][0]).equals(rm.getString("Application.version"))) {
            
            if (!newversionString.equals(rm.getString("Application.version")))
            {
                System.out.println("Van új verzió: " + newversionString + " (View.java/frissitesKeres())");
                
                if (App.args.length == 0)
                {
                    // rákérdez a frissítésre
                    FrissitesDialog fd = new FrissitesDialog(String.valueOf(newversion[0][0]), EncodeDecode.decode(String.valueOf(newversion[0][1])));
                }
                else
                {
                    // frissítés a háttérben
                    UpdateDialog u = new UpdateDialog();
                }
            }
            else
            {
                System.out.println("Nincs új verzió! (View.java/frissitesKeres())");
            }
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
            System.out.println("View.java/frissitesKeres()");
            //System.out.println("Exception query: " + query.getQuery());
            //ex.printStackTrace();
        }
    }

    private void userMentes()
    {
        FileOutputStream fos;
        ObjectOutputStream out;
        String filename = "dat/userSzamlazo.dat";

        try
        {
            fos = new FileOutputStream(filename);
            out = new ObjectOutputStream(fos);
            out.writeObject(App.user);
            out.close();
        }
        catch (IOException ex)
        {
            System.err.println("View.java/userMentes()");
            //ex.printStackTrace();
        }
    }

    private void userOlvasas()
    {
        FileInputStream fis;
        ObjectInputStream in;

        try
        {
            fis = new FileInputStream("dat/userSzamlazo.dat");
            
            try
            {
                in = new ObjectInputStream(fis);
                App.user = (User) in.readObject();
                
                Query query = new Query.QueryBuilder()
                    .select("id, name, username, password, `group`, companyIDs, defaultCompanyID")
                    .from("szamlazo_users")
                    .where("username = '" + App.user.getUsernev() + "'")
                    .build();
                App.user = new User(App.db.select(query.getQuery()));
                
                getFrame().setTitle(getFrame().getTitle() + " - " + EncodeDecode.decode(App.user.getNev()));
                in.close();
            }
            catch (IOException | ClassNotFoundException ex)
            {
                System.err.println("View.java/userOlvas()");
                //ex.printStackTrace();
            }
        }
        catch (FileNotFoundException ex)
        {
            System.err.println("View.java/userOlvas()/FileNotFound");
            //ex.printStackTrace();
        }
    }

    private void nyit(Object dialog, String title)
    {
        if (dialog instanceof JDialog)
        {
            JDialog d = (JDialog) dialog;
            Dimension appSize = this.getFrame().getSize();
            Point appLocation = this.getFrame().getLocation();
            int x = (appSize.width - d.getWidth()) / 2 + appLocation.x;
            int y = (appSize.height - d.getHeight()) / 2 + appLocation.y;
            d.setLocation(x, y);
            d.setTitle(title);
            d.setVisible(true);
        }
        else if (dialog instanceof JFrame)
        {
            JFrame f = (JFrame) dialog;
            Dimension appSize = this.getFrame().getSize();
            Point appLocation = this.getFrame().getLocation();
            int x = (appSize.width - f.getWidth()) / 2 + appLocation.x;
            int y = (appSize.height - f.getHeight()) / 2 + appLocation.y;
            f.setLocation(x, y);
            f.setTitle(title);
            f.setVisible(true);
        }
    }
}