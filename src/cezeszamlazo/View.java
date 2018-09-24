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
import cezeszamlazo.controller.Valuta;
import cezeszamlazo.database.Query;
import cezeszamlazo.database.SzamlazoConnection;
import cezeszamlazo.kintlevoseg.KintlevosegLevel;
import cezeszamlazo.kintlevoseg.KintlevosegLevelHtmlEditor;
import cezeszamlazo.interfaces.SharedValues;
import cezeszamlazo.ugyfel.KapcsolattartokFrame;
import cezeszamlazo.NAVConn;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The application's main frame.
 */
public class View extends FrameView implements SharedValues {

    // frame-ek
    private SzamlaCsoportokFrame szamlaCsoportokFrame;
    private SzamlakFrame szamlakFrame;
    private AdatszolgaltatasFrame adatszolgaltatasFrame;
    private TermekCsoportokFrame termekCsoportokFrame;
    private TermekekFrame termekekFrame;
    private cezeszamlazo.ugyfel.UgyfelekFrame ugyfelekFrame;
    private VtszTeszorFrame vtszTeszorFrame;
    private SzamlaLablecFrame szamlaLablecFrame;
    // egyéb
    private ResourceMap rm = Application.getInstance(cezeszamlazo.App.class).getContext().getResourceMap(View.class);
    private boolean b = true;
    private KapcsolattartokFrame kapcsolattartokFrame;
    private TimeStamp TimeStamp = new TimeStamp();

    public View(SingleFrameApplication app) {
        super(app);

//        
//        String dbName = "cezetesztdb";
//        String dbName = "szamlazo_" + Settings.getId();
//        
//        String clientUrl = "jdbc:mysql://localhost/"+dbName
//                + "?useUnicode=true&characterEncoding=UTF-8";
//        SzamlazoConnection szamlazoConnection = new SzamlazoConnection(clientUrl, USERNAME, PASSWORD);
        SzamlazoConnection szamlazoConnection = new SzamlazoConnection(URL, USERNAME, PASSWORD);
        App.db = new Database(szamlazoConnection);
        App.db.connect();

        userOlvasas();
        if (App.user == null) {
            LoginDialog l = new LoginDialog();
            if (l.getReturnStatus() == 1) {
                userMentes();
            } else {
                getApplication().exit();
            }
        }

        if (App.args.length != 0) {
            frissitesKeres();
            SzamlaDialogOld sz = SzamlaDialogOld.create();
            if (sz.getReturnStatus() == 0) {
                System.exit(0);
            }
        }

        Loop loop = new Loop();
        loop.start();

        App.args = new String[0];

        initComponents();

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        java.net.URL url = ClassLoader.getSystemResource("cezeszamlazo/resources/icon.png");
        java.awt.Image img = toolkit.createImage(url);
        getFrame().setIconImage(img);

        beallitasokAlPanel.setVisible(false);

        devizaDialog.setSize(devizaDialog.getPreferredSize().width, devizaDialog.getPreferredSize().height + 30);

        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String) (evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer) (evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = App.getApplication().getMainFrame();
            aboutBox = new AboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        App.getApplication().show(aboutBox);
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
        ugyfelekPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        szamlakPanel = new javax.swing.JPanel();
        SzamlakButton = new javax.swing.JLabel();
        ujDevizaSzamlaPanel = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        ujSzamlaPanel = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
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
        szamlaLablecPanel3 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        proFormaPanel = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        ujProFormaPanel = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        adatszolgaltatasPanel = new javax.swing.JPanel();
        adatszolgaltatas = new javax.swing.JLabel();
        kapocsolattartokPanel = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
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
        devizaDialog = new javax.swing.JDialog();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        devizaKozeparfolyam = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        devizaComboBox = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();
        devizaSzamlaLetrehozasButton = new javax.swing.JButton();

        mainPanel.setName("mainPanel"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(cezeszamlazo.App.class).getContext().getResourceMap(View.class);
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

        ujDevizaSzamlaPanel.setBorder(javax.swing.BorderFactory.createLineBorder(resourceMap.getColor("ujDevizaSzamlaPanel.border.lineColor"))); // NOI18N
        ujDevizaSzamlaPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ujDevizaSzamlaPanel.setName("ujDevizaSzamlaPanel"); // NOI18N
        ujDevizaSzamlaPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ujDevizaSzamlaPanelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ujDevizaSzamlaPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ujDevizaSzamlaPanelMouseExited(evt);
            }
        });

        jLabel3.setFont(resourceMap.getFont("jLabel3.font")); // NOI18N
        jLabel3.setIcon(resourceMap.getIcon("jLabel3.icon")); // NOI18N
        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        javax.swing.GroupLayout ujDevizaSzamlaPanelLayout = new javax.swing.GroupLayout(ujDevizaSzamlaPanel);
        ujDevizaSzamlaPanel.setLayout(ujDevizaSzamlaPanelLayout);
        ujDevizaSzamlaPanelLayout.setHorizontalGroup(
            ujDevizaSzamlaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ujDevizaSzamlaPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        ujDevizaSzamlaPanelLayout.setVerticalGroup(
            ujDevizaSzamlaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ujDevizaSzamlaPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        ujSzamlaPanel.setBorder(javax.swing.BorderFactory.createLineBorder(resourceMap.getColor("ujSzamlaPanel.border.lineColor"))); // NOI18N
        ujSzamlaPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ujSzamlaPanel.setName("ujSzamlaPanel"); // NOI18N
        ujSzamlaPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ujSzamlaPanelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ujSzamlaPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ujSzamlaPanelMouseExited(evt);
            }
        });

        jLabel4.setFont(resourceMap.getFont("jLabel4.font")); // NOI18N
        jLabel4.setIcon(resourceMap.getIcon("jLabel4.icon")); // NOI18N
        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        javax.swing.GroupLayout ujSzamlaPanelLayout = new javax.swing.GroupLayout(ujSzamlaPanel);
        ujSzamlaPanel.setLayout(ujSzamlaPanelLayout);
        ujSzamlaPanelLayout.setHorizontalGroup(
            ujSzamlaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ujSzamlaPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        ujSzamlaPanelLayout.setVerticalGroup(
            ujSzamlaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ujSzamlaPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
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
        beallitasokAlPanel.setName("beallitasokAlPanel"); // NOI18N

        szamlaCsoportokPanel.setBorder(javax.swing.BorderFactory.createLineBorder(resourceMap.getColor("szamlaCsoportokPanel.border.lineColor"))); // NOI18N
        szamlaCsoportokPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        szamlaCsoportokPanel.setName("szamlaCsoportokPanel"); // NOI18N
        szamlaCsoportokPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                szamlaCsoportokPanelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                szamlaCsoportokPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                szamlaCsoportokPanelMouseExited(evt);
            }
        });

        jLabel5.setFont(resourceMap.getFont("jLabel5.font")); // NOI18N
        jLabel5.setIcon(resourceMap.getIcon("jLabel5.icon")); // NOI18N
        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
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

        szamlaLablecPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(resourceMap.getColor("szamlaLablecPanel3.border.lineColor"))); // NOI18N
        szamlaLablecPanel3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        szamlaLablecPanel3.setName("szamlaLablecPanel3"); // NOI18N
        szamlaLablecPanel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                szamlaLablecPanel3MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                szamlaLablecPanel3MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                szamlaLablecPanel3MouseExited(evt);
            }
        });

        jLabel20.setFont(resourceMap.getFont("jLabel20.font")); // NOI18N
        jLabel20.setText(resourceMap.getString("jLabel20.text")); // NOI18N
        jLabel20.setName("jLabel20"); // NOI18N

        javax.swing.GroupLayout szamlaLablecPanel3Layout = new javax.swing.GroupLayout(szamlaLablecPanel3);
        szamlaLablecPanel3.setLayout(szamlaLablecPanel3Layout);
        szamlaLablecPanel3Layout.setHorizontalGroup(
            szamlaLablecPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(szamlaLablecPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        szamlaLablecPanel3Layout.setVerticalGroup(
            szamlaLablecPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(szamlaLablecPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel20)
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
                    .addComponent(szamlaLablecPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        beallitasokAlPanelLayout.setVerticalGroup(
            beallitasokAlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(beallitasokAlPanelLayout.createSequentialGroup()
                .addGap(0, 0, 0)
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
                .addComponent(szamlaLablecPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
        jLabel16.setEnabled(false);
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
        jLabel15.setEnabled(false);
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

        adatszolgaltatasPanel.setBorder(javax.swing.BorderFactory.createLineBorder(resourceMap.getColor("adatszolgaltatasPanel.border.lineColor"))); // NOI18N
        adatszolgaltatasPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        adatszolgaltatasPanel.setName("adatszolgaltatasPanel"); // NOI18N
        adatszolgaltatasPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                adatszolgaltatasPanelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                adatszolgaltatasPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                adatszolgaltatasPanelMouseExited(evt);
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

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(adatszolgaltatasPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(beallitasokPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(termekekPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ugyfelekPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(szamlakPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ujSzamlaPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ujDevizaSzamlaPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(proFormaPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ujProFormaPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(kapocsolattartokPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(beallitasokAlPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(85, Short.MAX_VALUE))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(beallitasokAlPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(174, 174, 174))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(ujSzamlaPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ujDevizaSzamlaPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(szamlakPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ujProFormaPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(proFormaPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)
                        .addComponent(ugyfelekPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(kapocsolattartokPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(termekekPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(beallitasokPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(adatszolgaltatasPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(116, 116, 116))))
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

        devizaDialog.setModal(true);
        devizaDialog.setName("devizaDialog"); // NOI18N

        jLabel8.setText(resourceMap.getString("jLabel8.text")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N

        jLabel9.setText(resourceMap.getString("jLabel9.text")); // NOI18N
        jLabel9.setName("jLabel9"); // NOI18N

        devizaKozeparfolyam.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        devizaKozeparfolyam.setText(resourceMap.getString("devizaKozeparfolyam.text")); // NOI18N
        devizaKozeparfolyam.setName("devizaKozeparfolyam"); // NOI18N

        jLabel11.setText(resourceMap.getString("jLabel11.text")); // NOI18N
        jLabel11.setName("jLabel11"); // NOI18N

        devizaComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "deviza" }));
        devizaComboBox.setName("devizaComboBox"); // NOI18N
        devizaComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                devizaComboBoxActionPerformed(evt);
            }
        });

        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        devizaSzamlaLetrehozasButton.setText(resourceMap.getString("devizaSzamlaLetrehozasButton.text")); // NOI18N
        devizaSzamlaLetrehozasButton.setName("devizaSzamlaLetrehozasButton"); // NOI18N
        devizaSzamlaLetrehozasButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                devizaSzamlaLetrehozasButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout devizaDialogLayout = new javax.swing.GroupLayout(devizaDialog.getContentPane());
        devizaDialog.getContentPane().setLayout(devizaDialogLayout);
        devizaDialogLayout.setHorizontalGroup(
            devizaDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(devizaDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(devizaDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(jLabel8))
                .addGap(18, 18, 18)
                .addGroup(devizaDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(devizaDialogLayout.createSequentialGroup()
                        .addComponent(devizaKozeparfolyam, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11))
                    .addComponent(devizaComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(161, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, devizaDialogLayout.createSequentialGroup()
                .addContainerGap(188, Short.MAX_VALUE)
                .addComponent(devizaSzamlaLetrehozasButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap())
        );
        devizaDialogLayout.setVerticalGroup(
            devizaDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(devizaDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(devizaDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(devizaComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(devizaDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(devizaKozeparfolyam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(devizaDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(devizaSzamlaLetrehozasButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents

    private void ujSzamlaPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ujSzamlaPanelMouseEntered
        ujSzamlaPanel.setBackground(Color.decode("#ABD043"));
    }//GEN-LAST:event_ujSzamlaPanelMouseEntered

    private void ujDevizaSzamlaPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ujDevizaSzamlaPanelMouseEntered
        ujDevizaSzamlaPanel.setBackground(Color.decode("#ABD043"));
    }//GEN-LAST:event_ujDevizaSzamlaPanelMouseEntered

    private void szamlakPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_szamlakPanelMouseEntered
        szamlakPanel.setBackground(Color.decode("#ABD043"));
    }//GEN-LAST:event_szamlakPanelMouseEntered

    private void ugyfelekPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ugyfelekPanelMouseEntered
        ugyfelekPanel.setBackground(Color.decode("#ABD043"));
    }//GEN-LAST:event_ugyfelekPanelMouseEntered

    private void ujSzamlaPanelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ujSzamlaPanelMouseExited
        ujSzamlaPanel.setBackground(Color.decode("#F0F0F0"));
    }//GEN-LAST:event_ujSzamlaPanelMouseExited

    private void ujDevizaSzamlaPanelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ujDevizaSzamlaPanelMouseExited
        ujDevizaSzamlaPanel.setBackground(Color.decode("#F0F0F0"));
    }//GEN-LAST:event_ujDevizaSzamlaPanelMouseExited

    private void szamlakPanelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_szamlakPanelMouseExited
        szamlakPanel.setBackground(Color.decode("#F0F0F0"));
    }//GEN-LAST:event_szamlakPanelMouseExited

    private void ugyfelekPanelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ugyfelekPanelMouseExited
        ugyfelekPanel.setBackground(Color.decode("#F0F0F0"));
    }//GEN-LAST:event_ugyfelekPanelMouseExited

    private void ugyfelekPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ugyfelekPanelMouseClicked
        if (ugyfelekFrame == null) {
            ugyfelekFrame = new cezeszamlazo.ugyfel.UgyfelekFrame();
        }
        ugyfelekFrame.nyit();

    }//GEN-LAST:event_ugyfelekPanelMouseClicked

    private void szamlakPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_szamlakPanelMouseClicked
        if (szamlakFrame == null)
        {
            System.out.println("Számlák gomb klikk: " + TimeStamp.getTimeStamp());
            szamlakFrame = new SzamlakFrame();    
        }
        szamlakFrame.nyit();
    }//GEN-LAST:event_szamlakPanelMouseClicked

    private void ujSzamlaPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ujSzamlaPanelMouseClicked
        SzamlaDialogOld sz = SzamlaDialogOld.createUjszamla("Ft", false, 1.0);
        if (sz.getReturnStatus() == 1) {
            if (szamlakFrame != null) {
                szamlakFrame.frissites();
            }
        }
    }//GEN-LAST:event_ujSzamlaPanelMouseClicked

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
        if (termekekFrame == null) {
            termekekFrame = new TermekekFrame();
        }
        termekekFrame.nyit();
    }//GEN-LAST:event_termekekPanelMouseClicked

    private void ujDevizaSzamlaPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ujDevizaSzamlaPanelMouseClicked
        b = false;
        devizaComboBox.removeAllItems();
        devizaComboBox.removeAll();
        if (devizaComboBox.getItemCount() == 0) {
            Query query = new Query.QueryBuilder()
                    .select("valuta, valuta_nev, kozeparfolyam ")
                    .from("szamlazo_valuta")
                    .order("id")
                    .build();
            Object[][] s = App.db.select(query.getQuery());
            for (int i = 0; i < s.length; i++) {
                devizaComboBox.addItem(new Valuta(String.valueOf(s[i][0]), String.valueOf(s[i][1]), Double.parseDouble(String.valueOf(s[i][2]))));
            }
        }
        b = true;
        devizaComboBox.setSelectedIndex(0);
        Valuta v = new Valuta(devizaComboBox.getSelectedItem());
        devizaKozeparfolyam.setText(String.valueOf(v.getKozeparfolyam()));
        nyit(devizaDialog, "Új deviza számla");
    }//GEN-LAST:event_ujDevizaSzamlaPanelMouseClicked

    private void devizaComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_devizaComboBoxActionPerformed
        if (b) {
            Valuta v = (Valuta) devizaComboBox.getSelectedItem();
            devizaKozeparfolyam.setText(String.valueOf(v.getKozeparfolyam()));
        }
    }//GEN-LAST:event_devizaComboBoxActionPerformed

    private void devizaSzamlaLetrehozasButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_devizaSzamlaLetrehozasButtonActionPerformed
        Valuta v = (Valuta) devizaComboBox.getSelectedItem();

        SzamlaDialogOld sz = SzamlaDialogOld.createDevizaSzamla(v.getValuta(), true, v.getKozeparfolyam());
//        SzamlaDialogOld sz = new SzamlaDialogOld(v.getValuta(), true, v.getKozeparfolyam());
        if (sz.getReturnStatus() == 1) {
            if (szamlakFrame != null) {
                szamlakFrame.frissites();
            }
        }
        devizaDialog.setVisible(false);
    }//GEN-LAST:event_devizaSzamlaLetrehozasButtonActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        devizaDialog.setVisible(false);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void beallitasokPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_beallitasokPanelMouseClicked
        beallitasokAlPanel.setVisible(!beallitasokAlPanel.isVisible());
    }//GEN-LAST:event_beallitasokPanelMouseClicked

    private void szamlaCsoportokPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_szamlaCsoportokPanelMouseEntered
        szamlaCsoportokPanel.setBackground(Color.decode("#ABD043"));
    }//GEN-LAST:event_szamlaCsoportokPanelMouseEntered

    private void szamlaCsoportokPanelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_szamlaCsoportokPanelMouseExited
        szamlaCsoportokPanel.setBackground(Color.decode("#F0F0F0"));
    }//GEN-LAST:event_szamlaCsoportokPanelMouseExited

    private void szamlaCsoportokPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_szamlaCsoportokPanelMouseClicked
        if (szamlaCsoportokFrame == null) {
            szamlaCsoportokFrame = new SzamlaCsoportokFrame();
        }
        szamlaCsoportokFrame.nyit();
    }//GEN-LAST:event_szamlaCsoportokPanelMouseClicked

    private void termekCsoportokPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_termekCsoportokPanelMouseEntered
        termekCsoportokPanel.setBackground(Color.decode("#ABD043"));
    }//GEN-LAST:event_termekCsoportokPanelMouseEntered

    private void termekCsoportokPanelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_termekCsoportokPanelMouseExited
        termekCsoportokPanel.setBackground(Color.decode("#F0F0F0"));
    }//GEN-LAST:event_termekCsoportokPanelMouseExited

    private void termekCsoportokPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_termekCsoportokPanelMouseClicked
        if (termekCsoportokFrame == null) {
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
        if (vtszTeszorFrame == null) {
            vtszTeszorFrame = new VtszTeszorFrame();
        }
        vtszTeszorFrame.nyit();
    }//GEN-LAST:event_vtszTeszorPanelMouseClicked

private void ujProFormaPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ujProFormaPanelMouseClicked
    UjProFormaDialog sz = new UjProFormaDialog();

}//GEN-LAST:event_ujProFormaPanelMouseClicked

private void ujProFormaPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ujProFormaPanelMouseEntered
    ujProFormaPanel.setBackground(Color.decode("#ABD043"));
}//GEN-LAST:event_ujProFormaPanelMouseEntered

private void ujProFormaPanelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ujProFormaPanelMouseExited
    ujProFormaPanel.setBackground(Color.decode("#F0F0F0"));
}//GEN-LAST:event_ujProFormaPanelMouseExited

private void proFormaPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_proFormaPanelMouseClicked
// TODO
    //if (szamlakFrame == null) {
//            szamlakFrame = new SzamlakFrame();
//        }
//        szamlakFrame.nyit();
}//GEN-LAST:event_proFormaPanelMouseClicked

private void proFormaPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_proFormaPanelMouseEntered
    proFormaPanel.setBackground(Color.decode("#ABD043"));
}//GEN-LAST:event_proFormaPanelMouseEntered

private void proFormaPanelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_proFormaPanelMouseExited
    proFormaPanel.setBackground(Color.decode("#F0F0F0"));
}//GEN-LAST:event_proFormaPanelMouseExited

    private void adatszolgaltatasPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_adatszolgaltatasPanelMouseClicked
        if (adatszolgaltatasFrame == null) {
            adatszolgaltatasFrame = new AdatszolgaltatasFrame();
        }
        adatszolgaltatasFrame.nyit();
    }//GEN-LAST:event_adatszolgaltatasPanelMouseClicked

    private void adatszolgaltatasPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_adatszolgaltatasPanelMouseEntered

    }//GEN-LAST:event_adatszolgaltatasPanelMouseEntered

    private void adatszolgaltatasPanelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_adatszolgaltatasPanelMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_adatszolgaltatasPanelMouseExited

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
        // TODO add your handling code here:
    }//GEN-LAST:event_szamlaLablecPanel1MouseEntered

    private void szamlaLablecPanel1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_szamlaLablecPanel1MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_szamlaLablecPanel1MouseExited

    private void szamlaLablecPanel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_szamlaLablecPanel2MouseClicked
        KintlevosegLevelHtmlEditor kintlevosegLevelHtmlEditorPDF = KintlevosegLevelHtmlEditor.create(KintlevosegLevel.Type.PDF);
        kintlevosegLevelHtmlEditorPDF.run();
    }//GEN-LAST:event_szamlaLablecPanel2MouseClicked

    private void szamlaLablecPanel2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_szamlaLablecPanel2MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_szamlaLablecPanel2MouseEntered

    private void szamlaLablecPanel2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_szamlaLablecPanel2MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_szamlaLablecPanel2MouseExited

    private void szamlaLablecPanel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_szamlaLablecPanel3MouseClicked
        KintlevosegLevelHtmlEditor kintlevosegLevelHtmlEditorEMAIL = KintlevosegLevelHtmlEditor.create(KintlevosegLevel.Type.EMAIL);
        kintlevosegLevelHtmlEditorEMAIL.run();
    }//GEN-LAST:event_szamlaLablecPanel3MouseClicked

    private void szamlaLablecPanel3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_szamlaLablecPanel3MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_szamlaLablecPanel3MouseEntered

    private void szamlaLablecPanel3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_szamlaLablecPanel3MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_szamlaLablecPanel3MouseExited

    private void kapocsolattartokPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kapocsolattartokPanelMouseClicked
       if (kapcsolattartokFrame == null) {
            kapcsolattartokFrame = new KapcsolattartokFrame();
        }
       kapcsolattartokFrame.nyit();
    }//GEN-LAST:event_kapocsolattartokPanelMouseClicked

    private void kapocsolattartokPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kapocsolattartokPanelMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_kapocsolattartokPanelMouseEntered

    private void kapocsolattartokPanelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kapocsolattartokPanelMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_kapocsolattartokPanelMouseExited

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel SzamlakButton;
    private javax.swing.JLabel adatszolgaltatas;
    private javax.swing.JPanel adatszolgaltatasPanel;
    private javax.swing.JPanel beallitasokAlPanel;
    private javax.swing.JPanel beallitasokPanel;
    private javax.swing.JComboBox devizaComboBox;
    private javax.swing.JDialog devizaDialog;
    private javax.swing.JTextField devizaKozeparfolyam;
    private javax.swing.JButton devizaSzamlaLetrehozasButton;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel kapocsolattartokPanel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JPanel proFormaPanel;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JPanel szamlaCsoportokPanel;
    private javax.swing.JPanel szamlaLablecPanel;
    private javax.swing.JPanel szamlaLablecPanel1;
    private javax.swing.JPanel szamlaLablecPanel2;
    private javax.swing.JPanel szamlaLablecPanel3;
    private javax.swing.JPanel szamlakPanel;
    private javax.swing.JPanel termekCsoportokPanel;
    private javax.swing.JPanel termekekPanel;
    private javax.swing.JPanel ugyfelekPanel;
    private javax.swing.JPanel ujDevizaSzamlaPanel;
    private javax.swing.JPanel ujProFormaPanel;
    private javax.swing.JPanel ujSzamlaPanel;
    private javax.swing.JPanel vtszTeszorPanel;
    // End of variables declaration//GEN-END:variables
    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;
    private JDialog aboutBox;

    public void frissitesKeres() {
        Query query = null;
        try {
            System.out.println("Új verzió keresése...");
            query = new Query.QueryBuilder()
                    .select("verzio, leiras ")
                    .from("szamlazo_versions")
                    .where("1")
                    .order("id DESC LIMIT 1")
                    .build();
            Object[][] newversion = App.db.select(query.getQuery());
            String newversionString = Functions.getStringFromObject(newversion[0][0]);
//             if (!String.valueOf(newversion[0][0]).equals(rm.getString("Application.version"))) {
            if (!newversionString.equals(rm.getString("Application.version"))) {
                System.out.println("Van új verzió: " + newversionString);
                if (App.args.length == 0) {
                    // rákérdez a frissítésre
                    FrissitesDialog fd = new FrissitesDialog(String.valueOf(newversion[0][0]), EncodeDecode.decode(String.valueOf(newversion[0][1])));
                } else {
                    // frissítés a háttérben
                    UpdateDialog u = new UpdateDialog();
                }
            } else {
                System.out.println("Nincs új verzió!");
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("ArrayIndexOutOfBoundsException váltódott ki!");
            System.out.println("Exception query: " + query.getQuery());
            ex.printStackTrace();
        }
    }

    private void userMentes() {
        FileOutputStream fos = null;
        ObjectOutputStream out = null;
        String filename = "dat/userSzamlazo.dat";

        try {
            fos = new FileOutputStream(filename);
            out = new ObjectOutputStream(fos);
            out.writeObject(App.user);
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void userOlvasas() {
        FileInputStream fis = null;
        ObjectInputStream in = null;

        try {
            fis = new FileInputStream("dat/userSzamlazo.dat");
            try {
                in = new ObjectInputStream(fis);
                App.user = (User) in.readObject();
                Query query = new Query.QueryBuilder()
                        .select("id, nev, usernev, jelszo, csoport, ceg ")
                        .from("szamlazo_users")
                        .where("usernev = '" + App.user.getUsernev() + "'")
                        .build();
                App.user = new User(App.db.select(query.getQuery()));
                getFrame().setTitle(getFrame().getTitle() + " - " + EncodeDecode.decode(App.user.getNev()));
                in.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        } catch (FileNotFoundException ex) {
        }
    }

    private void nyit(Object dialog, String title) {
        if (dialog instanceof JDialog) {
            JDialog d = (JDialog) dialog;
            Dimension appSize = this.getFrame().getSize();
            Point appLocation = this.getFrame().getLocation();
            int x = (appSize.width - d.getWidth()) / 2 + appLocation.x;
            int y = (appSize.height - d.getHeight()) / 2 + appLocation.y;
            d.setLocation(x, y);
            d.setTitle(title);
            d.setVisible(true);
        } else if (dialog instanceof JFrame) {
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
