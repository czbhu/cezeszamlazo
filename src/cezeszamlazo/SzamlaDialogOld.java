package cezeszamlazo;

import cezeszamlazo.controller.Vevo;
import cezeszamlazo.controller.Szallito;
import cezeszamlazo.controller.Szamla;
import cezeszamlazo.controller.SzamlaTermek;
import cezeszamlazo.controller.Functions;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;
import cezeszamlazo.controller.TermekDij;
import cezeszamlazo.controller.Ugyfel;
import cezeszamlazo.functions.SzamlaFunctions;
import cezeszamlazo.interfaces.DialogInterface;
import cezeszamlazo.database.Query;
import cezeszamlazo.hu.gov.nav.onlineszamla.GenerateInvoiceXMLs;
import cezeszamlazo.hu.gov.nav.onlineszamla.GenerateXml;
//import cezeszamlazo.hu.gov.nav.onlineszamla.InvoiceService;
import java.awt.Component;
import java.io.RandomAccessFile;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.TimeZone;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.swing.JList;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

public class SzamlaDialogOld extends javax.swing.JDialog implements DialogInterface
{ 
    public enum SzamlaType
    {
        UJ, MASOLAT, HELYESBITO, STORNO, DEVIZA, MODOSIT
    }

    private int returnStatus = RET_CANCEL;

    private String sorszam = "", penznem = "", vevoid = "0", helyesbitett = "", helyesbitettTeljesites = "";
    private int termekid = -1;
    private double kozeparfolyam = 1.0, scale = 1.0;
    private boolean deviza = false, b = true, bezar = false, isModosit = true, storno = false;
    private static boolean isUjSzamla = false;

    private List termekek = new LinkedList<SzamlaTermek>();
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private SzamlaTermek aktualis = new SzamlaTermek("", "", "db", "27", "", "0", "0", "27%");
    private Szamla szamla;
    private FolyamatbanDialog folyamatbanDialog;
    private SzamlaType szamlaType;
    
    private ArrayList<String> takeOverTYPE = new ArrayList<>();
    private String [] billingSoftwareDatas = new String[10];
    private String [] SupplierDatas = new String[5];
    
    static SzamlaDialogOld create()
    {
        return new SzamlaDialogOld();
    }
    
    static SzamlaDialogOld createUjszamla(String valuta, boolean bolean, Double kozeparfolyam)
    {
        return new SzamlaDialogOld(valuta, bolean, kozeparfolyam);
    }
    
    public static SzamlaDialogOld createDevizaSzamla(String valuta, boolean bolean, Double kozeparfolyam)
    {
        return new SzamlaDialogOld(valuta, bolean, kozeparfolyam);
    }
    
    public static SzamlaDialogOld createStornoSzamla(String azon, boolean modosit) 
    {
        return new SzamlaDialogOld(azon, modosit);
    }
    
    static SzamlaDialogOld createModositoSzamla(Szamla szml)
    {
        return new SzamlaDialogOld(false, szml);
    }
    
    public static SzamlaDialogOld createMasolatSzamla(Szamla szla)
    {
        return new SzamlaDialogOld(true, szla);
    }

    /*static SzamlaDialogOld createHelyesbitoSzamla(String azon, boolean modosit)
    {
        return new SzamlaDialogOld(azon, modosit);
    }*/   

    /**
     * Creates new form SzamlaDialogOld
     */
    public SzamlaDialogOld()
    {
        initComponents();

        // mentés vagy kilépés után bezárja a számlázót!
        bezar = true;

        penznem = "Ft";
        kozeparfolyam = 1.0;
        deviza = false;
        this.szamlaType = SzamlaType.UJ;

        penznemLabel1.setText(penznem);
        penznemLabel2.setText(penznem);
        penznemLabel3.setText(penznem);

        szamlaFejlec.setText(storno ? "Számlával egy tekintet alá eső okirat" : "Számla");

        szamlaCsoportFrissites();
        szallitoComboBoxFrissites(0);
        afaFrissites();
        if (App.args.length > 0)
        {
            vevoid = App.args[1];
            vevoFrissites(false);
            SzamlaTermek szamlaTermek;
            TermekDij termekDij;
            Object[][] select;
            for (int i = 2; i < App.args.length; i++)
            {
                Query query = new Query.QueryBuilder()
                        .select("termek, mee, egysegar, mennyiseg, id")
                        .from("pixi_ajanlatkeresek_adatai")
                        .where("id = " + App.args[i])
                        .build();
                select = App.db.select(query.getQuery());
                String DefaultVat = "27";
                String DefaultVatLabel = "27%";
                // String nev, String cikkszam, String mee, String afa, String vtszTeszor, String egysegar, String mennyiseg
                szamlaTermek = new SzamlaTermek(String.valueOf(select[0][0]).replace(";", ""), String.valueOf(select[0][4]), String.valueOf(select[0][1]), DefaultVat, "", String.valueOf(select[0][2]), String.valueOf(select[0][3]), DefaultVatLabel);
                double mennyiseg = Double.parseDouble(String.valueOf(select[0][3]));
                query = new Query.QueryBuilder()
                        .select("nev, szelesseg, magassag, peldany, egysegsuly, termekdij, osszsuly, csk, kt ")
                        .from("pixi_ajanlatkeresek_termekdij")
                        .where("ajanlatkeresid = " + App.args[i])
                        .build();
                select = App.db.select(query.getQuery());
                if (select.length != 0)
                {
                    // String nev, double szelesseg, double magassag, double peldany, double suly, double termekDij, double osszsuly
                    termekDij = new TermekDij.TermekDijBuilder()
                            .nev(String.valueOf(select[0][0]))
                            .szelesseg(Double.parseDouble(String.valueOf(select[0][1])))
                            .magassag(Double.parseDouble(String.valueOf(select[0][2])))
                            .peldany(szamlaTermek.getMennyiseg())
                            .egysegsuly(Double.parseDouble(String.valueOf(select[0][4])))
                            .termekDij(Double.parseDouble(String.valueOf(select[0][5])))
                            .osszsuly(Double.parseDouble(String.valueOf(select[0][6])))
                            .csk(String.valueOf(select[0][7]))
                            .kt(String.valueOf(select[0][8]))
                            .build();

                }
                else
                {
                    termekDij = null;
                }
                szamlaTermek.setTermekDij(termekDij);
                termekek.add(szamlaTermek);
            }
            szamlaTermekekFrissites();
        }

        init("Számla");

        // Close the dialog when Esc is pressed
        String cancelName = "cancel";
        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), cancelName);
        ActionMap actionMap = getRootPane().getActionMap();
        actionMap.put(cancelName, new AbstractAction()
        {
            public void actionPerformed(ActionEvent e)
            {
                doClose(RET_CANCEL);
            }
        });
    }

    public SzamlaDialogOld(String penznem, boolean deviza, double kozeparfolyam)
    {
        initComponents();
        
        penznemLabel1.setText(penznem);
        penznemLabel2.setText(penznem);
        penznemLabel3.setText(penznem);

        this.penznem = penznem;
        this.deviza = deviza;
        this.kozeparfolyam = kozeparfolyam;
        this.isModosit = false;

        if (deviza)
        {
            this.szamlaType = SzamlaType.DEVIZA;
        }
        else
        {
            this.szamlaType = SzamlaType.UJ;
        }
        termekekSize = termekek.size() -1;
        szamlaFejlec.setText(storno ? "Számlával egy tekintet alá eső okirat" : "Számla");

        szamlaCsoportFrissites();
        szallitoComboBoxFrissites(0);
        afaFrissites();

        init("Számla");

        // Close the dialog when Esc is pressed
        String cancelName = "cancel";
        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), cancelName);
        ActionMap actionMap = getRootPane().getActionMap();
        actionMap.put(cancelName, new AbstractAction()
        {
            public void actionPerformed(ActionEvent e)
            {
                doClose(RET_CANCEL);
            }
        });
    }

    public SzamlaDialogOld(String azon, boolean modosit)
    {
        initComponents();
        this.isModosit = modosit;
        this.storno = true;

        this.szamla = new Szamla(azon);
        this.szamlaType = szamlaType.STORNO;
        this.szamla.setTipus(2);
        this.penznem = (szamla.getValuta().equalsIgnoreCase("huf") || szamla.getValuta().equalsIgnoreCase("Ft") ? "Ft" : szamla.getValuta().toUpperCase());

        penznemLabel1.setText(penznem);
        penznemLabel2.setText(penznem);
        penznemLabel3.setText(penznem);

        this.deviza = szamla.isDeviza();
        this.kozeparfolyam = szamla.getKozeparfolyam();

        szamlaFejlec.setText(storno ? "Számlával egy tekintet alá eső okirat" : "Számla");
        termekek = szamla.getTermekek();
        termekekSize = termekek.size();
        if(this.szamlaType == szamlaType.UJ || this.szamlaType == szamlaType.DEVIZA || this.szamlaType == szamlaType.MASOLAT)
        {
            termekekSize--;
        }

        for (Object o : termekek)
        {
            SzamlaTermek szt = (SzamlaTermek) o;
            szt.szorozMennyiseg(-1.0);
            if (szt.getTermekDij() != null)
            {
                szt.getTermekDij().setSzorzo(-1.0);
            }
        }

        vevoFrissites(false);
        szamlaTermekekFrissites();
        szamlaCsoportFrissites();
        szallitoComboBoxFrissites(this.szamla.getSorszamasID());
        afaFrissites();

        init(storno ? "Számlával egy tekintet alá eső okirat" : "Számla");
        
        // Close the dialog when Esc is pressed
        String cancelName = "cancel";
        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), cancelName);
        ActionMap actionMap = getRootPane().getActionMap();
        actionMap.put(cancelName, new AbstractAction()
        {
            public void actionPerformed(ActionEvent e)
            {
                doClose(RET_CANCEL);
            }
        });       
    }
    
    public SzamlaDialogOld(boolean newInvoice, Szamla szla)
    {
        initComponents();

        if(newInvoice)
        {
            this.storno = false;
            this.szamlaType = szamlaType.MASOLAT;
        }
        else
        {
            this.storno = true;
            this.szamlaType = szamlaType.MODOSIT;
            szamlaTermekPopupMenu.add(modositMenuItem);
        }
        
        this.szamla = szla;
        this.penznem = (szla.getValuta().equalsIgnoreCase("huf") || szla.getValuta().equalsIgnoreCase("Ft") ? "Ft" : szla.getValuta().toUpperCase());

        penznemLabel1.setText(penznem);
        penznemLabel2.setText(penznem);
        penznemLabel3.setText(penznem);

        this.deviza = szla.isDeviza();
        this.kozeparfolyam = szla.getKozeparfolyam();

        szamlaFejlec.setText(storno ? "Számlával egy tekintet alá eső okirat" : "Számla");
        termekek = szla.getTermekek();
        termekekSize = termekek.size();
        if(this.szamlaType == szamlaType.MASOLAT)
        {
            termekekSize = -1;
        }
        vevoFrissites(false);

        szamlaTermekekFrissites();

        szamlaCsoportFrissites();
        System.out.println("this.szamla.getSorszamasID: " + this.szamla.getSorszamasID());
        szallitoComboBoxFrissites(this.szamla.getSorszamasID());
        afaFrissites();

        init("Számla");

        // Close the dialog when Esc is pressed
        String cancelName = "cancel";
        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), cancelName);
        ActionMap actionMap = getRootPane().getActionMap();
        actionMap.put(cancelName, new AbstractAction()
        {
            public void actionPerformed(ActionEvent e)
            {
                doClose(RET_CANCEL);
            }
        });
    }   

    /**
     * @return the return status of this dialog - one of RET_OK or RET_CANCEL
     */
    @Override
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

        szallitoDialog = new javax.swing.JDialog();
        jLabel103 = new javax.swing.JLabel();
        szallitoMegjegyzes = new javax.swing.JTextField();
        jLabel105 = new javax.swing.JLabel();
        jLabel111 = new javax.swing.JLabel();
        jLabel104 = new javax.swing.JLabel();
        szallitoAdoszam = new javax.swing.JTextField();
        jLabel102 = new javax.swing.JLabel();
        szallitoBankszamlaszam = new javax.swing.JTextField();
        jLabel101 = new javax.swing.JLabel();
        szallitoIrsz = new javax.swing.JTextField();
        szallitoUtcaText = new javax.swing.JTextField();
        szallitoVaros = new javax.swing.JTextField();
        jLabel106 = new javax.swing.JLabel();
        szallitoNev = new javax.swing.JTextField();
        exitSupplierDatasDialog = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        szallitoEuAdoszam = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        szallitoSzamlaLablecTextArea = new javax.swing.JTextArea();
        jLabel107 = new javax.swing.JLabel();
        supplierPublicPlaceText = new javax.swing.JTextField();
        jLabel108 = new javax.swing.JLabel();
        supplierNumberText = new javax.swing.JTextField();
        vevoDialog = new javax.swing.JDialog();
        vevoOkButton = new javax.swing.JButton();
        vevoMentesUjButton = new javax.swing.JButton();
        vevoModositasMentes = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jLabel59 = new javax.swing.JLabel();
        jLabel60 = new javax.swing.JLabel();
        jLabel61 = new javax.swing.JLabel();
        jLabel62 = new javax.swing.JLabel();
        jLabel63 = new javax.swing.JLabel();
        vevoBankszamlaszamText = new javax.swing.JTextField();
        vevoEuAdoszamText = new javax.swing.JTextField();
        vevoSzamlanMegjelenikCheckBox = new javax.swing.JCheckBox();
        vevoAdoszamText = new javax.swing.JTextField();
        vevoEsedekessegText = new javax.swing.JTextField();
        vevoFizetesiModComboBox = new javax.swing.JComboBox();
        vevoFizetesiModKotelezoCheckBox = new javax.swing.JCheckBox();
        jLabel64 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel52 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        vevoTelefonText = new javax.swing.JTextField();
        vevoEmailText = new javax.swing.JTextField();
        customerCountryCode = new javax.swing.JTextField();
        vevoUtcaText = new javax.swing.JTextField();
        vevoIrszText = new javax.swing.JTextField();
        vevoVarosText = new javax.swing.JTextField();
        vevoNevText = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        customerCountryName = new javax.swing.JTextField();
        customerPublicPlaceText = new javax.swing.JTextField();
        publicPlace = new javax.swing.JLabel();
        customerNumberText = new javax.swing.JTextField();
        jLabel66 = new javax.swing.JLabel();
        osszegzoDialog = new javax.swing.JDialog();
        jPanel10 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        fizVissz = new javax.swing.JLabel();
        azaz = new javax.swing.JLabel();
        osszegzoFizVissz = new javax.swing.JLabel();
        osszegzoBrutto = new javax.swing.JLabel();
        osszegzoAfaErtek = new javax.swing.JLabel();
        osszegzoNetto = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jPanel11 = new javax.swing.JPanel();
        nyomtatas = new javax.swing.JCheckBox();
        nyomtatasPeldany = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        elonezetLabel = new javax.swing.JLabel();
        pdfKeszites = new javax.swing.JCheckBox();
        mentesButton = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        printerCombobox = new javax.swing.JComboBox<String>();
        szamlaTermekPopupMenu = new javax.swing.JPopupMenu();
        modositMenuItem = new javax.swing.JMenuItem();
        editMenuItem = new javax.swing.JMenuItem();
        vtszMenuItem = new javax.swing.JMenuItem();
        torolMenuItem = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        szallitoAdatokButton = new javax.swing.JButton();
        szallitoComboBox = new javax.swing.JComboBox();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        esedekesseg = new javax.swing.JTextField();
        teljesites = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        vevoChangeButton = new javax.swing.JButton();
        szamlaCsoport = new javax.swing.JComboBox();
        fizetesiMod = new javax.swing.JComboBox();
        vevoTextField = new javax.swing.JTextField();
        customerInfosButton = new javax.swing.JButton();
        kelt = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        vtszTeszor = new javax.swing.JTextField();
        egysegar = new javax.swing.JTextField();
        mennyiseg = new javax.swing.JTextField();
        cikkszam = new javax.swing.JTextField();
        termek = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        penznemLabel2 = new javax.swing.JLabel();
        netto = new javax.swing.JTextField();
        brutto = new javax.swing.JTextField();
        penznemLabel3 = new javax.swing.JLabel();
        penznemLabel1 = new javax.swing.JLabel();
        afaComboBox = new javax.swing.JComboBox();
        engedmenyFelar = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        hozzaadModosit = new javax.swing.JButton();
        kiurit = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        vtszTeszorTallozas = new javax.swing.JButton();
        mee = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        szamlaTermekekTable = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        osszNetto = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        osszAfaErtek = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        osszBrutto = new javax.swing.JLabel();
        try
        {
            RandomAccessFile raf = new RandomAccessFile("takeOverTypes.txt", "r");
            for(String row = raf.readLine(); row != null; row = raf.readLine())
            {
                takeOverTYPE.add(row);
            }
        }
        catch(IOException ex)
        {
            System.out.println("takeOverTypes.txt nem található");
        }
        takeOverType = new javax.swing.JComboBox();
        termekdijAtvallalas = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        megjegyzes = new javax.swing.JTextArea();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        lablec = new javax.swing.JTextArea();
        szamlaFejlec = new javax.swing.JLabel();
        szamlaSorszam = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        Osszegzo = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();

        szallitoDialog.setMinimumSize(new java.awt.Dimension(400, 201));
        szallitoDialog.setModal(true);
        szallitoDialog.setName("szallitoDialog"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(cezeszamlazo.App.class).getContext().getResourceMap(SzamlaDialogOld.class);
        jLabel103.setText(resourceMap.getString("jLabel103.text")); // NOI18N
        jLabel103.setName("jLabel103"); // NOI18N

        szallitoMegjegyzes.setName("szallitoMegjegyzes"); // NOI18N

        jLabel105.setText(resourceMap.getString("jLabel105.text")); // NOI18N
        jLabel105.setName("jLabel105"); // NOI18N

        jLabel111.setText(resourceMap.getString("jLabel111.text")); // NOI18N
        jLabel111.setName("jLabel111"); // NOI18N

        jLabel104.setText(resourceMap.getString("jLabel104.text")); // NOI18N
        jLabel104.setName("jLabel104"); // NOI18N

        szallitoAdoszam.setName("szallitoAdoszam"); // NOI18N

        jLabel102.setText(resourceMap.getString("jLabel102.text")); // NOI18N
        jLabel102.setName("jLabel102"); // NOI18N

        szallitoBankszamlaszam.setName("szallitoBankszamlaszam"); // NOI18N

        jLabel101.setText(resourceMap.getString("jLabel101.text")); // NOI18N
        jLabel101.setName("jLabel101"); // NOI18N

        szallitoIrsz.setName("szallitoIrsz"); // NOI18N
        szallitoIrsz.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                szallitoIrszKeyReleased(evt);
            }
        });

        szallitoUtcaText.setName("szallitoUtcaText"); // NOI18N

        szallitoVaros.setName("szallitoVaros"); // NOI18N

        jLabel106.setText(resourceMap.getString("jLabel106.text")); // NOI18N
        jLabel106.setName("jLabel106"); // NOI18N

        szallitoNev.setName("szallitoNev"); // NOI18N

        exitSupplierDatasDialog.setText(resourceMap.getString("exitSupplierDatasDialog.text")); // NOI18N
        exitSupplierDatasDialog.setName("exitSupplierDatasDialog"); // NOI18N
        exitSupplierDatasDialog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitSupplierDatasDialogActionPerformed(evt);
            }
        });

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        szallitoEuAdoszam.setText(resourceMap.getString("szallitoEuAdoszam.text")); // NOI18N
        szallitoEuAdoszam.setName("szallitoEuAdoszam"); // NOI18N

        jLabel17.setText(resourceMap.getString("jLabel17.text")); // NOI18N
        jLabel17.setName("jLabel17"); // NOI18N

        jScrollPane4.setName("jScrollPane4"); // NOI18N

        szallitoSzamlaLablecTextArea.setColumns(20);
        szallitoSzamlaLablecTextArea.setFont(resourceMap.getFont("szallitoSzamlaLablecTextArea.font")); // NOI18N
        szallitoSzamlaLablecTextArea.setRows(5);
        szallitoSzamlaLablecTextArea.setName("szallitoSzamlaLablecTextArea"); // NOI18N
        szallitoSzamlaLablecTextArea.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                szallitoSzamlaLablecTextAreaKeyReleased(evt);
            }
        });
        jScrollPane4.setViewportView(szallitoSzamlaLablecTextArea);

        jLabel107.setText(resourceMap.getString("jLabel107.text")); // NOI18N
        jLabel107.setName("jLabel107"); // NOI18N

        supplierPublicPlaceText.setName("supplierPublicPlaceText"); // NOI18N

        jLabel108.setText(resourceMap.getString("jLabel108.text")); // NOI18N
        jLabel108.setName("jLabel108"); // NOI18N

        supplierNumberText.setName("supplierNumberText"); // NOI18N

        javax.swing.GroupLayout szallitoDialogLayout = new javax.swing.GroupLayout(szallitoDialog.getContentPane());
        szallitoDialog.getContentPane().setLayout(szallitoDialogLayout);
        szallitoDialogLayout.setHorizontalGroup(
            szallitoDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(szallitoDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(szallitoDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(exitSupplierDatasDialog, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(szallitoDialogLayout.createSequentialGroup()
                        .addGroup(szallitoDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(szallitoDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel104, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel103, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel102, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel101, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel107, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(25, 25, 25)
                        .addGroup(szallitoDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(szallitoUtcaText)
                            .addGroup(szallitoDialogLayout.createSequentialGroup()
                                .addComponent(szallitoIrsz, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel106)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(szallitoVaros))
                            .addComponent(szallitoNev)
                            .addComponent(szallitoAdoszam, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(szallitoEuAdoszam)
                            .addGroup(szallitoDialogLayout.createSequentialGroup()
                                .addComponent(supplierPublicPlaceText, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel108, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(supplierNumberText, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(szallitoDialogLayout.createSequentialGroup()
                        .addGroup(szallitoDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(szallitoDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel105, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel111))
                            .addComponent(jLabel17))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(szallitoDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(szallitoBankszamlaszam)
                            .addComponent(szallitoMegjegyzes)
                            .addComponent(jScrollPane4))))
                .addContainerGap())
        );
        szallitoDialogLayout.setVerticalGroup(
            szallitoDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(szallitoDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(szallitoDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel101)
                    .addComponent(szallitoNev, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(szallitoDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel102)
                    .addComponent(szallitoIrsz, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel106)
                    .addComponent(szallitoVaros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(szallitoDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel103)
                    .addComponent(szallitoUtcaText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(szallitoDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel107)
                    .addComponent(supplierPublicPlaceText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel108)
                    .addComponent(supplierNumberText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(szallitoDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel104)
                    .addComponent(szallitoAdoszam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(szallitoDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(szallitoEuAdoszam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(szallitoDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel105)
                    .addComponent(szallitoBankszamlaszam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(szallitoDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel111)
                    .addComponent(szallitoMegjegyzes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(szallitoDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(szallitoDialogLayout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(exitSupplierDatasDialog)
                .addContainerGap())
        );

        vevoDialog.setModal(true);
        vevoDialog.setName("vevoDialog"); // NOI18N

        vevoOkButton.setText(resourceMap.getString("vevoOkButton.text")); // NOI18N
        vevoOkButton.setName("vevoOkButton"); // NOI18N
        vevoOkButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vevoOkButtonActionPerformed(evt);
            }
        });

        vevoMentesUjButton.setText(resourceMap.getString("vevoMentesUjButton.text")); // NOI18N
        vevoMentesUjButton.setName("vevoMentesUjButton"); // NOI18N
        vevoMentesUjButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vevoMentesUjButtonActionPerformed(evt);
            }
        });

        vevoModositasMentes.setText(resourceMap.getString("vevoModositasMentes.text")); // NOI18N
        vevoModositasMentes.setName("vevoModositasMentes"); // NOI18N
        vevoModositasMentes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vevoModositasMentesActionPerformed(evt);
            }
        });

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jPanel9.border.title"))); // NOI18N
        jPanel9.setName("jPanel9"); // NOI18N

        jLabel59.setText(resourceMap.getString("jLabel59.text")); // NOI18N
        jLabel59.setName("jLabel59"); // NOI18N

        jLabel60.setText(resourceMap.getString("jLabel60.text")); // NOI18N
        jLabel60.setName("jLabel60"); // NOI18N

        jLabel61.setText(resourceMap.getString("jLabel61.text")); // NOI18N
        jLabel61.setName("jLabel61"); // NOI18N

        jLabel62.setText(resourceMap.getString("jLabel62.text")); // NOI18N
        jLabel62.setName("jLabel62"); // NOI18N

        jLabel63.setText(resourceMap.getString("jLabel63.text")); // NOI18N
        jLabel63.setName("jLabel63"); // NOI18N

        vevoBankszamlaszamText.setName("vevoBankszamlaszamText"); // NOI18N

        vevoEuAdoszamText.setName("vevoEuAdoszamText"); // NOI18N

        vevoSzamlanMegjelenikCheckBox.setText(resourceMap.getString("vevoSzamlanMegjelenikCheckBox.text")); // NOI18N
        vevoSzamlanMegjelenikCheckBox.setName("vevoSzamlanMegjelenikCheckBox"); // NOI18N

        vevoAdoszamText.setName("vevoAdoszamText"); // NOI18N

        vevoEsedekessegText.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        vevoEsedekessegText.setText(resourceMap.getString("vevoEsedekessegText.text")); // NOI18N
        vevoEsedekessegText.setName("vevoEsedekessegText"); // NOI18N
        vevoEsedekessegText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                vevoEsedekessegTextKeyReleased(evt);
            }
        });

        vevoFizetesiModComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Készpénz", "Átutalás", "Utánvét" }));
        vevoFizetesiModComboBox.setName("vevoFizetesiModComboBox"); // NOI18N
        vevoFizetesiModComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vevoFizetesiModComboBoxActionPerformed(evt);
            }
        });

        vevoFizetesiModKotelezoCheckBox.setText(resourceMap.getString("vevoFizetesiModKotelezoCheckBox.text")); // NOI18N
        vevoFizetesiModKotelezoCheckBox.setName("vevoFizetesiModKotelezoCheckBox"); // NOI18N
        vevoFizetesiModKotelezoCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vevoFizetesiModKotelezoCheckBoxActionPerformed(evt);
            }
        });

        jLabel64.setText(resourceMap.getString("jLabel64.text")); // NOI18N
        jLabel64.setName("jLabel64"); // NOI18N

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel63)
                    .addComponent(jLabel62)
                    .addComponent(jLabel61)
                    .addComponent(jLabel60)
                    .addComponent(jLabel59))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(vevoFizetesiModComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(vevoFizetesiModKotelezoCheckBox))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(vevoEsedekessegText, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel64))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(vevoAdoszamText, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(vevoEuAdoszamText, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(vevoBankszamlaszamText, javax.swing.GroupLayout.Alignment.LEADING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(vevoSzamlanMegjelenikCheckBox))))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel59)
                    .addComponent(vevoFizetesiModComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(vevoFizetesiModKotelezoCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel60)
                    .addComponent(vevoEsedekessegText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel64))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel61)
                    .addComponent(vevoAdoszamText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel62)
                    .addComponent(vevoEuAdoszamText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel63)
                    .addComponent(vevoBankszamlaszamText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(vevoSzamlanMegjelenikCheckBox))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jPanel8.border.title"))); // NOI18N
        jPanel8.setName("jPanel8"); // NOI18N

        jLabel52.setText(resourceMap.getString("jLabel52.text")); // NOI18N
        jLabel52.setName("jLabel52"); // NOI18N

        jLabel53.setText(resourceMap.getString("jLabel53.text")); // NOI18N
        jLabel53.setName("jLabel53"); // NOI18N

        jLabel54.setText(resourceMap.getString("jLabel54.text")); // NOI18N
        jLabel54.setName("jLabel54"); // NOI18N

        jLabel55.setText(resourceMap.getString("jLabel55.text")); // NOI18N
        jLabel55.setName("jLabel55"); // NOI18N

        jLabel56.setText(resourceMap.getString("jLabel56.text")); // NOI18N
        jLabel56.setName("jLabel56"); // NOI18N

        jLabel57.setText(resourceMap.getString("jLabel57.text")); // NOI18N
        jLabel57.setName("jLabel57"); // NOI18N

        jLabel58.setText(resourceMap.getString("jLabel58.text")); // NOI18N
        jLabel58.setName("jLabel58"); // NOI18N

        vevoTelefonText.setName("vevoTelefonText"); // NOI18N

        vevoEmailText.setName("vevoEmailText"); // NOI18N

        customerCountryCode.setName("customerCountryCode"); // NOI18N
        customerCountryCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                customerCountryCodeKeyReleased(evt);
            }
        });

        vevoUtcaText.setName("vevoUtcaText"); // NOI18N

        vevoIrszText.setName("vevoIrszText"); // NOI18N
        vevoIrszText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                vevoIrszTextKeyReleased(evt);
            }
        });

        vevoVarosText.setName("vevoVarosText"); // NOI18N

        vevoNevText.setName("vevoNevText"); // NOI18N
        vevoNevText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                vevoNevTextKeyReleased(evt);
            }
        });

        jLabel18.setText(resourceMap.getString("jLabel18.text")); // NOI18N
        jLabel18.setName("jLabel18"); // NOI18N

        customerCountryName.setText(resourceMap.getString("customerCountryName.text")); // NOI18N
        customerCountryName.setName("customerCountryName"); // NOI18N
        customerCountryName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                customerCountryNameKeyReleased(evt);
            }
        });

        customerPublicPlaceText.setName("customerPublicPlaceText"); // NOI18N

        publicPlace.setText(resourceMap.getString("publicPlace.text")); // NOI18N
        publicPlace.setName("publicPlace"); // NOI18N

        customerNumberText.setName("customerNumberText"); // NOI18N
        customerNumberText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customerNumberTextActionPerformed(evt);
            }
        });

        jLabel66.setText(resourceMap.getString("jLabel66.text")); // NOI18N
        jLabel66.setName("jLabel66"); // NOI18N

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel56, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel55, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel53, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel52, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel57, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel58, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(publicPlace, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(vevoTelefonText, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(vevoUtcaText, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel8Layout.createSequentialGroup()
                        .addComponent(vevoIrszText, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel54)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(vevoVarosText))
                    .addComponent(vevoNevText, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(vevoEmailText, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel8Layout.createSequentialGroup()
                        .addComponent(customerCountryCode, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(customerCountryName))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel8Layout.createSequentialGroup()
                        .addComponent(customerPublicPlaceText, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel66, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(customerNumberText, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel52)
                    .addComponent(vevoNevText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel53)
                    .addComponent(vevoIrszText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(vevoVarosText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel54))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel55)
                    .addComponent(vevoUtcaText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(publicPlace)
                    .addComponent(customerPublicPlaceText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(customerNumberText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel66))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel56)
                    .addComponent(customerCountryCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18)
                    .addComponent(customerCountryName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel57)
                    .addComponent(vevoTelefonText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel58)
                    .addComponent(vevoEmailText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jLabel56.getAccessibleContext().setAccessibleName(resourceMap.getString("jLabel56.AccessibleContext.accessibleName")); // NOI18N

        javax.swing.GroupLayout vevoDialogLayout = new javax.swing.GroupLayout(vevoDialog.getContentPane());
        vevoDialog.getContentPane().setLayout(vevoDialogLayout);
        vevoDialogLayout.setHorizontalGroup(
            vevoDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(vevoDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(vevoDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, vevoDialogLayout.createSequentialGroup()
                        .addComponent(vevoModositasMentes)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(vevoMentesUjButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(vevoOkButton))
                    .addGroup(vevoDialogLayout.createSequentialGroup()
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        vevoDialogLayout.setVerticalGroup(
            vevoDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(vevoDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(vevoDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(vevoOkButton)
                    .addComponent(vevoMentesUjButton)
                    .addComponent(vevoModositasMentes))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        osszegzoDialog.setModal(true);
        osszegzoDialog.setName("osszegzoDialog"); // NOI18N

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(resourceMap.getColor("jPanel10.border.border.lineColor")), resourceMap.getString("jPanel10.border.title"))); // NOI18N
        jPanel10.setName("jPanel10"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jLabel27.setText(resourceMap.getString("jLabel27.text")); // NOI18N
        jLabel27.setName("jLabel27"); // NOI18N

        jLabel29.setText(resourceMap.getString("jLabel29.text")); // NOI18N
        jLabel29.setName("jLabel29"); // NOI18N

        fizVissz.setFont(resourceMap.getFont("fizVissz.font")); // NOI18N
        fizVissz.setText(resourceMap.getString("fizVissz.text")); // NOI18N
        fizVissz.setName("fizVissz"); // NOI18N

        azaz.setText(resourceMap.getString("azaz.text")); // NOI18N
        azaz.setName("azaz"); // NOI18N

        osszegzoFizVissz.setFont(resourceMap.getFont("osszegzoFizVissz.font")); // NOI18N
        osszegzoFizVissz.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        osszegzoFizVissz.setText(resourceMap.getString("osszegzoFizVissz.text")); // NOI18N
        osszegzoFizVissz.setName("osszegzoFizVissz"); // NOI18N

        osszegzoBrutto.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        osszegzoBrutto.setText(resourceMap.getString("osszegzoBrutto.text")); // NOI18N
        osszegzoBrutto.setName("osszegzoBrutto"); // NOI18N

        osszegzoAfaErtek.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        osszegzoAfaErtek.setText(resourceMap.getString("osszegzoAfaErtek.text")); // NOI18N
        osszegzoAfaErtek.setName("osszegzoAfaErtek"); // NOI18N

        osszegzoNetto.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        osszegzoNetto.setText(resourceMap.getString("osszegzoNetto.text")); // NOI18N
        osszegzoNetto.setName("osszegzoNetto"); // NOI18N

        jSeparator3.setForeground(resourceMap.getColor("jSeparator3.foreground")); // NOI18N
        jSeparator3.setName("jSeparator3"); // NOI18N

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator3, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel29)
                            .addComponent(jLabel27)
                            .addComponent(jLabel2))
                        .addGap(60, 60, 60)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(osszegzoBrutto, javax.swing.GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE)
                            .addComponent(osszegzoAfaErtek, javax.swing.GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE)
                            .addComponent(osszegzoNetto, javax.swing.GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE)))
                    .addComponent(azaz, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                        .addComponent(fizVissz, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(osszegzoFizVissz, javax.swing.GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(osszegzoNetto))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(osszegzoAfaErtek))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(osszegzoBrutto))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fizVissz)
                    .addComponent(osszegzoFizVissz))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(azaz)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(resourceMap.getColor("jPanel11.border.border.lineColor")), resourceMap.getString("jPanel11.border.title"))); // NOI18N
        jPanel11.setName("jPanel11"); // NOI18N

        nyomtatas.setSelected(true);
        nyomtatas.setText(resourceMap.getString("nyomtatas.text")); // NOI18N
        nyomtatas.setName("nyomtatas"); // NOI18N
        nyomtatas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nyomtatasActionPerformed(evt);
            }
        });

        nyomtatasPeldany.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        nyomtatasPeldany.setText(resourceMap.getString("nyomtatasPeldany.text")); // NOI18N
        nyomtatasPeldany.setName("nyomtatasPeldany"); // NOI18N

        jLabel37.setText(resourceMap.getString("jLabel37.text")); // NOI18N
        jLabel37.setName("jLabel37"); // NOI18N

        elonezetLabel.setIcon(resourceMap.getIcon("elonezetLabel.icon")); // NOI18N
        elonezetLabel.setText(resourceMap.getString("elonezetLabel.text")); // NOI18N
        elonezetLabel.setToolTipText(resourceMap.getString("elonezetLabel.toolTipText")); // NOI18N
        elonezetLabel.setBorder(javax.swing.BorderFactory.createLineBorder(resourceMap.getColor("elonezetLabel.border.lineColor"))); // NOI18N
        elonezetLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        elonezetLabel.setName("elonezetLabel"); // NOI18N
        elonezetLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                elonezetLabelMouseReleased(evt);
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                elonezetLabelMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                elonezetLabelMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                elonezetLabelMousePressed(evt);
            }
        });

        pdfKeszites.setText(resourceMap.getString("pdfKeszites.text")); // NOI18N
        pdfKeszites.setEnabled(false);
        pdfKeszites.setName("pdfKeszites"); // NOI18N

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(nyomtatas)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nyomtatasPeldany, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel37)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(elonezetLabel))
                    .addComponent(pdfKeszites))
                .addContainerGap(139, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nyomtatas)
                    .addComponent(nyomtatasPeldany, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel37)
                    .addComponent(elonezetLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pdfKeszites)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        mentesButton.setText(resourceMap.getString("mentesButton.text")); // NOI18N
        mentesButton.setName("mentesButton"); // NOI18N
        mentesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mentesButtonActionPerformed(evt);
            }
        });

        jButton5.setText(resourceMap.getString("jButton5.text")); // NOI18N
        jButton5.setName("jButton5"); // NOI18N
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        printerCombobox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        printerCombobox.setName("printerCombobox"); // NOI18N
        printerCombobox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                printerComboboxItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout osszegzoDialogLayout = new javax.swing.GroupLayout(osszegzoDialog.getContentPane());
        osszegzoDialog.getContentPane().setLayout(osszegzoDialogLayout);
        osszegzoDialogLayout.setHorizontalGroup(
            osszegzoDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(osszegzoDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(osszegzoDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, osszegzoDialogLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(printerCombobox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(mentesButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton5)))
                .addContainerGap())
        );
        osszegzoDialogLayout.setVerticalGroup(
            osszegzoDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(osszegzoDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(osszegzoDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mentesButton)
                    .addComponent(jButton5)
                    .addComponent(printerCombobox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        szamlaTermekPopupMenu.setAlignmentX(0.5F);
        szamlaTermekPopupMenu.setName("szamlaTermekPopupMenu"); // NOI18N

        modositMenuItem.setText(resourceMap.getString("modositMenuItem.text")); // NOI18N
        modositMenuItem.setName("modositMenuItem"); // NOI18N
        modositMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modositMenuItemActionPerformed(evt);
            }
        });
        /*
        szamlaTermekPopupMenu.add(modositMenuItem);
        */

        editMenuItem.setLabel(resourceMap.getString("editMenuItem.label")); // NOI18N
        editMenuItem.setName("editMenuItem"); // NOI18N
        editMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editMenuItemActionPerformed(evt);
            }
        });
        szamlaTermekPopupMenu.add(editMenuItem);
        editMenuItem.getAccessibleContext().setAccessibleName(resourceMap.getString("editMenuItem.AccessibleContext.accessibleName")); // NOI18N

        vtszMenuItem.setText(resourceMap.getString("vtszMenuItem.text")); // NOI18N
        vtszMenuItem.setName("vtszMenuItem"); // NOI18N
        vtszMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vtszMenuItemActionPerformed(evt);
            }
        });
        szamlaTermekPopupMenu.add(vtszMenuItem);

        torolMenuItem.setText(resourceMap.getString("torolMenuItem.text")); // NOI18N
        torolMenuItem.setName("torolMenuItem"); // NOI18N
        torolMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                torolMenuItemActionPerformed(evt);
            }
        });
        szamlaTermekPopupMenu.add(torolMenuItem);

        jMenuItem1.setText(resourceMap.getString("jMenuItem1.text")); // NOI18N
        jMenuItem1.setName("jMenuItem1"); // NOI18N

        setName("Form"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jTabbedPane1.setName("jTabbedPane1"); // NOI18N

        jPanel1.setName("jPanel1"); // NOI18N

        szallitoAdatokButton.setText(resourceMap.getString("szallitoAdatokButton.text")); // NOI18N
        szallitoAdatokButton.setName("szallitoAdatokButton"); // NOI18N
        szallitoAdatokButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                szallitoAdatokButtonActionPerformed(evt);
            }
        });

        szallitoComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "szállító" }));
        szallitoComboBox.setName("szallitoComboBox"); // NOI18N
        szallitoComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                szallitoComboBoxActionPerformed(evt);
            }
        });

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(resourceMap.getColor("jPanel5.border.border.lineColor")), resourceMap.getString("jPanel5.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, resourceMap.getFont("jPanel5.border.titleFont"))); // NOI18N
        jPanel5.setName("jPanel5"); // NOI18N
        jPanel5.setPreferredSize(new java.awt.Dimension(575, 146));

        jLabel3.setFont(resourceMap.getFont("jLabel3.font")); // NOI18N
        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        esedekesseg.setEditable(false);
        esedekesseg.setBackground(resourceMap.getColor("esedekesseg.background")); // NOI18N
        esedekesseg.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        esedekesseg.setDisabledTextColor(resourceMap.getColor("esedekesseg.disabledTextColor")); // NOI18N
        esedekesseg.setEnabled(false);
        esedekesseg.setName("esedekesseg"); // NOI18N

        teljesites.setEditable(false);
        teljesites.setBackground(resourceMap.getColor("teljesites.background")); // NOI18N
        teljesites.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        teljesites.setDisabledTextColor(resourceMap.getColor("teljesites.disabledTextColor")); // NOI18N
        teljesites.setEnabled(false);
        teljesites.setName("teljesites"); // NOI18N

        jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        jLabel8.setFont(resourceMap.getFont("kelt.font")); // NOI18N
        jLabel8.setText(resourceMap.getString("jLabel8.text")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N

        jLabel9.setText(resourceMap.getString("jLabel9.text")); // NOI18N
        jLabel9.setName("jLabel9"); // NOI18N

        jSeparator1.setForeground(resourceMap.getColor("jSeparator1.foreground")); // NOI18N
        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator1.setName("jSeparator1"); // NOI18N

        jLabel10.setIcon(resourceMap.getIcon("jLabel10.icon")); // NOI18N
        jLabel10.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel10.setName("jLabel10"); // NOI18N
        jLabel10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel10MouseClicked(evt);
            }
        });

        jLabel11.setIcon(resourceMap.getIcon("jLabel11.icon")); // NOI18N
        jLabel11.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel11.setName("jLabel11"); // NOI18N
        jLabel11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel11MouseClicked(evt);
            }
        });

        vevoChangeButton.setText(resourceMap.getString("vevoChangeButton.text")); // NOI18N
        vevoChangeButton.setMargin(new java.awt.Insets(2, 4, 2, 4));
        vevoChangeButton.setName("vevoChangeButton"); // NOI18N
        vevoChangeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vevoChangeButtonActionPerformed(evt);
            }
        });

        szamlaCsoport.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "csoport" }));
        szamlaCsoport.setName("szamlaCsoport"); // NOI18N

        fizetesiMod.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Készpénz", "Átutalás", "Utánvét" }));
        fizetesiMod.setName("fizetesiMod"); // NOI18N
        fizetesiMod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fizetesiModActionPerformed(evt);
            }
        });

        vevoTextField.setText(resourceMap.getString("vevoTextField.text")); // NOI18N
        vevoTextField.setName("vevoTextField"); // NOI18N
        vevoTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                vevoTextFieldKeyReleased(evt);
            }
        });

        customerInfosButton.setText(resourceMap.getString("customerInfosButton.text")); // NOI18N
        customerInfosButton.setName("customerInfosButton"); // NOI18N
        customerInfosButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customerInfosButtonActionPerformed(evt);
            }
        });

        kelt.setFont(resourceMap.getFont("kelt.font")); // NOI18N
        kelt.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        kelt.setText(resourceMap.getString("kelt.text")); // NOI18N
        kelt.setName("kelt"); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fizetesiMod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(szamlaCsoport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(vevoTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(vevoChangeButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(customerInfosButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(teljesites)
                    .addComponent(esedekesseg)
                    .addComponent(kelt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(6, 6, 6)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(vevoTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(customerInfosButton)
                        .addComponent(vevoChangeButton))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(esedekesseg)
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(teljesites)
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(kelt))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(fizetesiMod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(szamlaCsoport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(resourceMap.getColor("jPanel6.border.border.lineColor")), resourceMap.getString("jPanel6.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, resourceMap.getFont("jPanel6.border.titleFont"))); // NOI18N
        jPanel6.setName("jPanel6"); // NOI18N

        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        jLabel12.setText(resourceMap.getString("jLabel12.text")); // NOI18N
        jLabel12.setName("jLabel12"); // NOI18N

        jLabel13.setText(resourceMap.getString("jLabel13.text")); // NOI18N
        jLabel13.setName("jLabel13"); // NOI18N

        jLabel14.setText(resourceMap.getString("jLabel14.text")); // NOI18N
        jLabel14.setName("jLabel14"); // NOI18N

        jLabel15.setText(resourceMap.getString("jLabel15.text")); // NOI18N
        jLabel15.setName("jLabel15"); // NOI18N

        vtszTeszor.setText(resourceMap.getString("vtszTeszor.text")); // NOI18N
        vtszTeszor.setName("vtszTeszor"); // NOI18N

        egysegar.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        egysegar.setText(resourceMap.getString("egysegar.text")); // NOI18N
        egysegar.setName("egysegar"); // NOI18N
        egysegar.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                egysegarFocusLost(evt);
            }
        });
        egysegar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                egysegarKeyReleased(evt);
            }
        });

        mennyiseg.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        mennyiseg.setText(resourceMap.getString("mennyiseg.text")); // NOI18N
        mennyiseg.setName("mennyiseg"); // NOI18N
        mennyiseg.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                mennyisegFocusLost(evt);
            }
        });
        mennyiseg.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                mennyisegKeyReleased(evt);
            }
        });

        cikkszam.setText(resourceMap.getString("cikkszam.text")); // NOI18N
        cikkszam.setName("cikkszam"); // NOI18N

        termek.setText(resourceMap.getString("termek.text")); // NOI18N
        termek.setName("termek"); // NOI18N

        jLabel16.setText(resourceMap.getString("jLabel16.text")); // NOI18N
        jLabel16.setName("jLabel16"); // NOI18N

        penznemLabel2.setText(resourceMap.getString("penznemLabel2.text")); // NOI18N
        penznemLabel2.setName("penznemLabel2"); // NOI18N

        netto.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        netto.setText(resourceMap.getString("netto.text")); // NOI18N
        netto.setName("netto"); // NOI18N
        netto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                nettoFocusLost(evt);
            }
        });
        netto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                nettoKeyReleased(evt);
            }
        });

        brutto.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        brutto.setText(resourceMap.getString("brutto.text")); // NOI18N
        brutto.setName("brutto"); // NOI18N
        brutto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                bruttoFocusLost(evt);
            }
        });
        brutto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                bruttoKeyReleased(evt);
            }
        });

        penznemLabel3.setText(resourceMap.getString("penznemLabel3.text")); // NOI18N
        penznemLabel3.setName("penznemLabel3"); // NOI18N

        penznemLabel1.setText(resourceMap.getString("penznemLabel1.text")); // NOI18N
        penznemLabel1.setName("penznemLabel1"); // NOI18N

        afaComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "afa" }));
        afaComboBox.setName("afaComboBox"); // NOI18N
        afaComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                afaComboBoxActionPerformed(evt);
            }
        });

        engedmenyFelar.setEditable(false);
        engedmenyFelar.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        engedmenyFelar.setText(resourceMap.getString("engedmenyFelar.text")); // NOI18N
        engedmenyFelar.setEnabled(false);
        engedmenyFelar.setName("engedmenyFelar"); // NOI18N

        jLabel21.setText(resourceMap.getString("jLabel21.text")); // NOI18N
        jLabel21.setName("jLabel21"); // NOI18N

        jLabel22.setText(resourceMap.getString("jLabel22.text")); // NOI18N
        jLabel22.setName("jLabel22"); // NOI18N

        jLabel23.setText(resourceMap.getString("jLabel23.text")); // NOI18N
        jLabel23.setName("jLabel23"); // NOI18N

        jLabel24.setText(resourceMap.getString("jLabel24.text")); // NOI18N
        jLabel24.setName("jLabel24"); // NOI18N

        jLabel25.setText(resourceMap.getString("jLabel25.text")); // NOI18N
        jLabel25.setName("jLabel25"); // NOI18N

        hozzaadModosit.setFont(resourceMap.getFont("hozzaadModosit.font")); // NOI18N
        hozzaadModosit.setText(resourceMap.getString("hozzaadModosit.text")); // NOI18N
        hozzaadModosit.setName("hozzaadModosit"); // NOI18N
        hozzaadModosit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hozzaadModositActionPerformed(evt);
            }
        });

        kiurit.setText(resourceMap.getString("kiurit.text")); // NOI18N
        kiurit.setName("kiurit"); // NOI18N
        kiurit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kiuritActionPerformed(evt);
            }
        });

        jSeparator2.setForeground(resourceMap.getColor("jSeparator2.foreground")); // NOI18N
        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator2.setName("jSeparator2"); // NOI18N

        vtszTeszorTallozas.setText(resourceMap.getString("vtszTeszorTallozas.text")); // NOI18N
        vtszTeszorTallozas.setMargin(new java.awt.Insets(2, 4, 2, 4));
        vtszTeszorTallozas.setName("vtszTeszorTallozas"); // NOI18N
        vtszTeszorTallozas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vtszTeszorTallozasActionPerformed(evt);
            }
        });

        mee.setText(resourceMap.getString("mee.text")); // NOI18N
        mee.setName("mee"); // NOI18N

        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel31.setIcon(resourceMap.getIcon("jLabel31.icon")); // NOI18N
        jLabel31.setText(resourceMap.getString("jLabel31.text")); // NOI18N
        jLabel31.setBorder(javax.swing.BorderFactory.createLineBorder(resourceMap.getColor("jLabel31.border.lineColor"))); // NOI18N
        jLabel31.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel31.setName("jLabel31"); // NOI18N
        jLabel31.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel31MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel31MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel31MouseExited(evt);
            }
        });

        jPanel14.setBorder(javax.swing.BorderFactory.createLineBorder(resourceMap.getColor("jPanel14.border.lineColor"))); // NOI18N
        jPanel14.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel14.setName("jPanel14"); // NOI18N
        jPanel14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel14MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel14MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanel14MouseExited(evt);
            }
        });

        jLabel32.setFont(resourceMap.getFont("jLabel32.font")); // NOI18N
        jLabel32.setText(resourceMap.getString("jLabel32.text")); // NOI18N
        jLabel32.setName("jLabel32"); // NOI18N

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel32)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel32)
        );

        jPanel15.setBorder(javax.swing.BorderFactory.createLineBorder(resourceMap.getColor("jPanel15.border.lineColor"))); // NOI18N
        jPanel15.setToolTipText(resourceMap.getString("jPanel15.toolTipText")); // NOI18N
        jPanel15.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel15.setName("jPanel15"); // NOI18N
        jPanel15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel15MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel15MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanel15MouseExited(evt);
            }
        });

        jLabel33.setFont(resourceMap.getFont("jLabel33.font")); // NOI18N
        jLabel33.setText(resourceMap.getString("jLabel33.text")); // NOI18N
        jLabel33.setName("jLabel33"); // NOI18N

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel33)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jScrollPane3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(resourceMap.getColor("jScrollPane3.border.border.lineColor")), resourceMap.getString("jScrollPane3.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, resourceMap.getFont("jScrollPane3.border.titleFont"))); // NOI18N
        jScrollPane3.setName("jScrollPane3"); // NOI18N

        szamlaTermekekTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Termék", "Cikkszám", "Mennyiség", "Mee.", "Egységár", "VTSZ/TESZOR", "Nettó ár", "ÁFA", "Áfa érték", "Bruttó ár"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, true, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        szamlaTermekekTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        szamlaTermekekTable.setName("szamlaTermekekTable"); // NOI18N
        szamlaTermekekTable.getTableHeader().setReorderingAllowed(false);
        szamlaTermekekTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                szamlaTermekekTableMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                szamlaTermekekTableMouseReleased(evt);
            }
        });
        jScrollPane3.setViewportView(szamlaTermekekTable);
        if (szamlaTermekekTable.getColumnModel().getColumnCount() > 0) {
            szamlaTermekekTable.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("szamlaTermekekTable.columnModel.title0")); // NOI18N
            szamlaTermekekTable.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("szamlaTermekekTable.columnModel.title1")); // NOI18N
            szamlaTermekekTable.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("szamlaTermekekTable.columnModel.title2")); // NOI18N
            szamlaTermekekTable.getColumnModel().getColumn(3).setHeaderValue(resourceMap.getString("szamlaTermekekTable.columnModel.title3")); // NOI18N
            szamlaTermekekTable.getColumnModel().getColumn(4).setHeaderValue(resourceMap.getString("szamlaTermekekTable.columnModel.title4")); // NOI18N
            szamlaTermekekTable.getColumnModel().getColumn(5).setHeaderValue(resourceMap.getString("szamlaTermekekTable.columnModel.title5")); // NOI18N
            szamlaTermekekTable.getColumnModel().getColumn(6).setHeaderValue(resourceMap.getString("szamlaTermekekTable.columnModel.title6")); // NOI18N
            szamlaTermekekTable.getColumnModel().getColumn(7).setHeaderValue(resourceMap.getString("szamlaTermekekTable.columnModel.title7")); // NOI18N
            szamlaTermekekTable.getColumnModel().getColumn(8).setHeaderValue(resourceMap.getString("szamlaTermekekTable.columnModel.title8")); // NOI18N
            szamlaTermekekTable.getColumnModel().getColumn(9).setHeaderValue(resourceMap.getString("szamlaTermekekTable.columnModel.title9")); // NOI18N
        }

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15)
                    .addComponent(jLabel14)
                    .addComponent(jLabel13)
                    .addComponent(jLabel12)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(egysegar)
                            .addComponent(mennyiseg, javax.swing.GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addComponent(penznemLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(mee)))
                    .addComponent(cikkszam)
                    .addComponent(vtszTeszor)
                    .addComponent(termek))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(vtszTeszorTallozas)
                        .addGap(8, 8, 8))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(afaComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(engedmenyFelar, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(brutto)
                                .addComponent(netto, javax.swing.GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE)))
                        .addGap(6, 6, 6)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(penznemLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(penznemLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(kiurit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(hozzaadModosit)))
                .addContainerGap())
            .addComponent(jScrollPane3)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                                    .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                                    .addComponent(termek, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel12)
                                    .addComponent(cikkszam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel13)
                                    .addComponent(mennyiseg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel16)
                                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(mee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(egysegar)
                                    .addComponent(engedmenyFelar)
                                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel14)
                                        .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel21)
                                        .addComponent(penznemLabel1))))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(netto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(penznemLabel2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(brutto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(penznemLabel3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(afaComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel15)
                                .addComponent(vtszTeszor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(vtszTeszorTallozas))
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(hozzaadModosit)
                                .addComponent(kiurit))))
                    .addComponent(jSeparator2, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(resourceMap.getColor("jPanel7.border.border.lineColor")), resourceMap.getString("jPanel7.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, resourceMap.getFont("jPanel7.border.titleFont"))); // NOI18N
        jPanel7.setName("jPanel7"); // NOI18N

        jLabel26.setFont(resourceMap.getFont("osszBrutto.font")); // NOI18N
        jLabel26.setText(resourceMap.getString("jLabel26.text")); // NOI18N
        jLabel26.setName("jLabel26"); // NOI18N

        osszNetto.setFont(resourceMap.getFont("osszBrutto.font")); // NOI18N
        osszNetto.setText(resourceMap.getString("osszNetto.text")); // NOI18N
        osszNetto.setName("osszNetto"); // NOI18N

        jLabel28.setFont(resourceMap.getFont("osszBrutto.font")); // NOI18N
        jLabel28.setText(resourceMap.getString("jLabel28.text")); // NOI18N
        jLabel28.setName("jLabel28"); // NOI18N

        osszAfaErtek.setFont(resourceMap.getFont("osszBrutto.font")); // NOI18N
        osszAfaErtek.setText(resourceMap.getString("osszAfaErtek.text")); // NOI18N
        osszAfaErtek.setName("osszAfaErtek"); // NOI18N

        jLabel30.setFont(resourceMap.getFont("osszBrutto.font")); // NOI18N
        jLabel30.setText(resourceMap.getString("jLabel30.text")); // NOI18N
        jLabel30.setName("jLabel30"); // NOI18N

        osszBrutto.setFont(resourceMap.getFont("osszBrutto.font")); // NOI18N
        osszBrutto.setText(resourceMap.getString("osszBrutto.text")); // NOI18N
        osszBrutto.setName("osszBrutto"); // NOI18N

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel26)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(osszNetto, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel28)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(osszAfaErtek, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel30)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(osszBrutto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(osszNetto, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28)
                    .addComponent(osszAfaErtek, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30)
                    .addComponent(osszBrutto, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        takeOverType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02_aa", "02_ab", "02_b", "02_c", "02_d", "02_ea", "02_eb", "02_fa", "02_fb", "02_ga", "02_gb" }));
        takeOverType.setName("takeOverType"); // NOI18N
        takeOverType.setEnabled(false);
        takeOverType.setRenderer(new BasicComboBoxRenderer()
            {
                public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
                {
                    if (isSelected)
                    {
                        setBackground(list.getSelectionBackground());
                        setForeground(list.getSelectionForeground());
                        if (index > -1)
                        {
                            list.setToolTipText(takeOverTYPE.get(index));
                        }
                    }
                    else
                    {
                        setBackground(list.getBackground());
                        setForeground(list.getForeground());
                    }
                    setFont(list.getFont());
                    setText((value == null) ? "" : value.toString());

                    return this;
                }
            });
            takeOverType.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    takeOverTypeActionPerformed(evt);
                }
            });
            takeOverType.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
                public void propertyChange(java.beans.PropertyChangeEvent evt) {
                    takeOverTypePropertyChange(evt);
                }
            });

            termekdijAtvallalas.setText(resourceMap.getString("termekdijAtvallalas.text")); // NOI18N
            termekdijAtvallalas.setName("termekdijAtvallalas"); // NOI18N
            termekdijAtvallalas.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    termekdijAtvallalasActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
            jPanel1.setLayout(jPanel1Layout);
            jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(termekdijAtvallalas)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(takeOverType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(0, 0, Short.MAX_VALUE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(szallitoAdatokButton)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(szallitoComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 586, Short.MAX_VALUE)
                                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addContainerGap())))
            );
            jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(szallitoAdatokButton)
                        .addComponent(szallitoComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(1, 1, 1)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(3, 3, 3)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(termekdijAtvallalas)
                        .addComponent(takeOverType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            jTabbedPane1.addTab(resourceMap.getString("jPanel1.TabConstraints.tabTitle"), jPanel1); // NOI18N

            jPanel2.setName("jPanel2"); // NOI18N

            jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(resourceMap.getColor("jPanel3.border.border.lineColor")), resourceMap.getString("jPanel3.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, resourceMap.getFont("jPanel3.border.titleFont"))); // NOI18N
            jPanel3.setName("jPanel3"); // NOI18N

            jScrollPane1.setName("jScrollPane1"); // NOI18N

            megjegyzes.setColumns(20);
            megjegyzes.setFont(resourceMap.getFont("megjegyzes.font")); // NOI18N
            megjegyzes.setLineWrap(true);
            megjegyzes.setRows(5);
            megjegyzes.setWrapStyleWord(true);
            megjegyzes.setName("megjegyzes"); // NOI18N
            jScrollPane1.setViewportView(megjegyzes);

            javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
            jPanel3.setLayout(jPanel3Layout);
            jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 556, Short.MAX_VALUE)
                    .addContainerGap())
            );
            jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE)
                    .addContainerGap())
            );

            jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(resourceMap.getColor("jPanel4.border.border.lineColor")))); // NOI18N
            jPanel4.setName("jPanel4"); // NOI18N

            jScrollPane2.setName("jScrollPane2"); // NOI18N

            lablec.setColumns(20);
            lablec.setFont(resourceMap.getFont("lablec.font")); // NOI18N
            lablec.setLineWrap(true);
            lablec.setRows(5);
            lablec.setText(resourceMap.getString("lablec.text")); // NOI18N
            lablec.setWrapStyleWord(true);
            lablec.setName("lablec"); // NOI18N
            jScrollPane2.setViewportView(lablec);

            javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
            jPanel4.setLayout(jPanel4Layout);
            jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 564, Short.MAX_VALUE)
                    .addContainerGap())
            );
            jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
                    .addContainerGap())
            );

            javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
            jPanel2.setLayout(jPanel2Layout);
            jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addContainerGap())
            );
            jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
            );

            jTabbedPane1.addTab(resourceMap.getString("jPanel2.TabConstraints.tabTitle"), jPanel2); // NOI18N

            szamlaFejlec.setFont(resourceMap.getFont("szamlaSorszam.font")); // NOI18N
            szamlaFejlec.setText(resourceMap.getString("szamlaFejlec.text")); // NOI18N
            szamlaFejlec.setName("szamlaFejlec"); // NOI18N

            szamlaSorszam.setFont(resourceMap.getFont("szamlaSorszam.font")); // NOI18N
            szamlaSorszam.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            szamlaSorszam.setText(resourceMap.getString("szamlaSorszam.text")); // NOI18N
            szamlaSorszam.setName("szamlaSorszam"); // NOI18N

            jPanel12.setBorder(javax.swing.BorderFactory.createLineBorder(resourceMap.getColor("jPanel12.border.lineColor"))); // NOI18N
            jPanel12.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            jPanel12.setName("jPanel12"); // NOI18N
            jPanel12.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    jPanel12MouseClicked(evt);
                }
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    jPanel12MouseEntered(evt);
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    jPanel12MouseExited(evt);
                }
            });

            Osszegzo.setFont(resourceMap.getFont("Osszegzo.font")); // NOI18N
            Osszegzo.setIcon(resourceMap.getIcon("Osszegzo.icon")); // NOI18N
            Osszegzo.setText(resourceMap.getString("Osszegzo.text")); // NOI18N
            Osszegzo.setName("Osszegzo"); // NOI18N

            javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
            jPanel12.setLayout(jPanel12Layout);
            jPanel12Layout.setHorizontalGroup(
                jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel12Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(Osszegzo)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            jPanel12Layout.setVerticalGroup(
                jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(Osszegzo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );

            jPanel13.setBorder(javax.swing.BorderFactory.createLineBorder(resourceMap.getColor("jPanel13.border.lineColor"))); // NOI18N
            jPanel13.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            jPanel13.setName("jPanel13"); // NOI18N
            jPanel13.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    jPanel13MouseClicked(evt);
                }
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    jPanel13MouseEntered(evt);
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    jPanel13MouseExited(evt);
                }
            });

            jLabel19.setIcon(resourceMap.getIcon("jLabel19.icon")); // NOI18N
            jLabel19.setText(resourceMap.getString("jLabel19.text")); // NOI18N
            jLabel19.setName("jLabel19"); // NOI18N

            javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
            jPanel13.setLayout(jPanel13Layout);
            jPanel13Layout.setHorizontalGroup(
                jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel13Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel19)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            jPanel13Layout.setVerticalGroup(
                jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
            );

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jTabbedPane1)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(szamlaFejlec)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(szamlaSorszam, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addGap(0, 0, Short.MAX_VALUE)
                            .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(szamlaFejlec)
                        .addComponent(szamlaSorszam))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 598, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            pack();
        }// </editor-fold>//GEN-END:initComponents

    /**
     * Closes the dialog
     */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        doClose(RET_CANCEL);
    }//GEN-LAST:event_closeDialog

    private void szallitoIrszKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_szallitoIrszKeyReleased
        if (szallitoIrsz.getText().length() == 4) {
            Query query = new Query.QueryBuilder()
                    .select("CONCAT(varos, IF(v = 1, CONCAT('-', varosresz), ''))")
                    .from("varosok")
                    .where("irsz = '" + szallitoIrsz.getText() + "'")
                    .build();
            Object[][] s = App.db.select(query.getQuery());
            if (s.length == 1) {
                szallitoVaros.setText(String.valueOf(s[0][0]));
            }
        }
    }//GEN-LAST:event_szallitoIrszKeyReleased

    private void vevoIrszTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_vevoIrszTextKeyReleased
        if (vevoIrszText.getText().length() == 4) {
            Query query = new Query.QueryBuilder()
                    .select("id, CONCAT(varos, IF(v = 1, '-', ''), varosresz) ")
                    .from("varosok")
                    .where("irsz = '" + vevoIrszText.getText() + "'")
                    .build();
            Object[][] o = App.db.select(query.getQuery());
            if (o.length != 0) {
                vevoVarosText.setText(String.valueOf(o[0][1]));
            }
        }
    }//GEN-LAST:event_vevoIrszTextKeyReleased

    private void vevoFizetesiModComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vevoFizetesiModComboBoxActionPerformed
        fizetesiMod.setSelectedIndex(vevoFizetesiModComboBox.getSelectedIndex());
        if (vevoFizetesiModComboBox.getSelectedIndex() == 1 && vevoEsedekessegText.getText().matches("0")) {
            Properties prop = new Properties();
            try {
                prop.load(new FileInputStream("dat/beallitasok.properties"));
                vevoEsedekessegText.setText(prop.getProperty("alapEsedekesseg"));
            } catch (IOException ex) {
                System.out.println("IOException váltódott ki!");
                ex.printStackTrace();
            }
        }
        if (vevoFizetesiModComboBox.getSelectedIndex() == 0) {
            vevoEsedekessegText.setText("0");
        }
        esedekessegFrissites();
    }//GEN-LAST:event_vevoFizetesiModComboBoxActionPerformed

    private void vevoOkButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vevoOkButtonActionPerformed
        esedekessegFrissites();
        vevoDialog.setVisible(false);
    }//GEN-LAST:event_vevoOkButtonActionPerformed

    private void vevoMentesUjButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vevoMentesUjButtonActionPerformed
        Object[] o = new Object[14];
        o[0] = vevoNevText.getText();
        o[1] = vevoIrszText.getText();
        o[2] = vevoVarosText.getText();
        o[3] = vevoUtcaText.getText();
        o[4] = customerCountryCode.getText();
        o[5] = vevoTelefonText.getText();
        o[6] = vevoEmailText.getText();
        o[7] = vevoFizetesiModComboBox.getSelectedIndex();
        o[8] = (vevoFizetesiModKotelezoCheckBox.isSelected() ? 1 : 0);
        o[9] = vevoEsedekessegText.getText();
        o[10] = vevoAdoszamText.getText();
        o[11] = vevoEuAdoszamText.getText();
        o[12] = vevoBankszamlaszamText.getText();
        o[13] = (vevoSzamlanMegjelenikCheckBox.isSelected() ? 1 : 0);
//        App.db.insert("INSERT INTO szamlazo_ugyfel (nev, irsz, varos, utca, orszag, telefon, email, fizetesi_mod, fizetesi_mod_kotelezo, esedekesseg, adoszam, eu_adoszam, bankszamlaszam, szamlan_megjelenik) VALUES " + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", o, 14);
        App.db.insert("INSERT INTO pixi_ugyfel (nev, irsz, varos, utca, orszag, telefon, email, fizetesi_mod, fizetesi_mod_kotelezo, esedekesseg, adoszam, eu_adoszam, bankszamlaszam, szamlan_megjelenik) VALUES " + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", o, 14);
        Query query = new Query.QueryBuilder()
                .select("id")
                .from("pixi_ugyfel")
                //.from("szamlazo_ugyfel")
                .where("1")
                .order("id DESC LIMIT 1")
                .build();
        Object[][] select = App.db.select(query.getQuery());
        vevoid = String.valueOf(String.valueOf(select[0][0]));
        vevoDialog.setVisible(false);
    }//GEN-LAST:event_vevoMentesUjButtonActionPerformed

    private void vevoModositasMentesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vevoModositasMentesActionPerformed
        if (!vevoid.equalsIgnoreCase("0")) {
            Object[] o = new Object[15];
            o[0] = vevoNevText.getText();
            o[1] = vevoIrszText.getText();
            o[2] = vevoVarosText.getText();
            o[3] = vevoUtcaText.getText();
            o[4] = customerCountryCode.getText();
            o[5] = vevoTelefonText.getText();
            o[6] = vevoEmailText.getText();
            o[7] = vevoFizetesiModComboBox.getSelectedIndex();
            o[8] = (vevoFizetesiModKotelezoCheckBox.isSelected() ? 1 : 0);
            o[9] = vevoEsedekessegText.getText();
            o[10] = vevoAdoszamText.getText();
            o[11] = vevoEuAdoszamText.getText();
            o[12] = vevoBankszamlaszamText.getText();
            o[13] = (vevoSzamlanMegjelenikCheckBox.isSelected() ? 1 : 0);
            o[14] = vevoid;
            String query = "UPDATE pixi_ugyfel  SET nev = ?, irsz = ?, varos = ?, "
                    + "utca = ? , orszag= ? , telefon = ?, "
                    + "email = ?, fizetesi_mod = ?, fizetesi_mod_kotelezo = ?, "
                    + "esedekesseg = ?, adoszam = ?, "
                    + "eu_adoszam = ?, bankszamlaszam = ?, szamlan_megjelenik = ? "
                    + "WHERE id = ?";
//            System.out.println("query: " + query);
            App.db.insert(query, o, 15);
        }
        vevoDialog.setVisible(false);
    }//GEN-LAST:event_vevoModositasMentesActionPerformed

    private void vevoNevTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_vevoNevTextKeyReleased
        vevoTextField.setText(vevoNevText.getText());
    }//GEN-LAST:event_vevoNevTextKeyReleased

    private void vevoEsedekessegTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_vevoEsedekessegTextKeyReleased
        String e = SzamlaFunctions.csakszam(vevoEsedekessegText.getText(), 0, false);
        vevoEsedekessegText.setText(e);
    }//GEN-LAST:event_vevoEsedekessegTextKeyReleased

    private void vevoFizetesiModKotelezoCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vevoFizetesiModKotelezoCheckBoxActionPerformed
        fizetesiMod.setEnabled(!vevoFizetesiModKotelezoCheckBox.isSelected());
    }//GEN-LAST:event_vevoFizetesiModKotelezoCheckBoxActionPerformed

    private void szallitoComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_szallitoComboBoxActionPerformed
        if (b) {
            betoltSzallito();
            sorszamEllenorzes();
            Label l = (Label) szallitoComboBox.getSelectedItem();
            Query query = new Query.QueryBuilder()
                    .select("db, ev, elotag")
                    .from("szamlazo_szamla_sorszam")
                    .where("id = (SELECT sorszamid from szamlazo_ceg_adatok WHERE id = " + l.getId() + ")")
                    .build();
            Object[][] s = App.db.select(query.getQuery());
            String ujSorszam = String.valueOf(s[0][1]) + "/" + (Integer.parseInt(String.valueOf(s[0][0])) + 1);
            if (!String.valueOf(s[0][2]).isEmpty()) {
                ujSorszam = String.valueOf(s[0][2]) + " " + ujSorszam;
            }
            szamlaSorszam.setText(ujSorszam + (deviza ? "/V" : ""));
            if (!ujSorszam.matches(sorszam) && !sorszam.isEmpty()) {
                HibaDialog hd = new HibaDialog("A sorszámozás megváltozott!\nAz új számla sorszám: " + ujSorszam, "Ok", "");
            }
            sorszam = ujSorszam;
        }
    }//GEN-LAST:event_szallitoComboBoxActionPerformed

    private void szallitoAdatokButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_szallitoAdatokButtonActionPerformed
        nyit(szallitoDialog, "Szállító adatok");
    }//GEN-LAST:event_szallitoAdatokButtonActionPerformed

    private void customerInfosButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customerInfosButtonActionPerformed
        nyit(vevoDialog, "Vevő adatok");
    }//GEN-LAST:event_customerInfosButtonActionPerformed

    private void vevoChangeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vevoChangeButtonActionPerformed
        UgyfelListaDialog ugyfelListaDialog = new UgyfelListaDialog();
        if (ugyfelListaDialog.getReturnStatus() == 1)
        {
            vevoid = ugyfelListaDialog.getId();
            vevoFrissites(true);
        }
    }//GEN-LAST:event_vevoChangeButtonActionPerformed

    private void vevoTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_vevoTextFieldKeyReleased
        vevoNevText.setText(vevoTextField.getText());
    }//GEN-LAST:event_vevoTextFieldKeyReleased

    private void jLabel10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel10MouseClicked
        CalendarDialog cd = new CalendarDialog(null, esedekesseg);
    }//GEN-LAST:event_jLabel10MouseClicked

    private void jLabel11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel11MouseClicked
        CalendarDialog cd = new CalendarDialog(null, teljesites);
        esedekessegFrissites();
    }//GEN-LAST:event_jLabel11MouseClicked

    private void elonezetLabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elonezetLabelMouseExited
    }//GEN-LAST:event_elonezetLabelMouseExited

    private void elonezetLabelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elonezetLabelMousePressed
    }//GEN-LAST:event_elonezetLabelMousePressed

    private void elonezetLabelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elonezetLabelMouseReleased
    }//GEN-LAST:event_elonezetLabelMouseReleased

    private void elonezetLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elonezetLabelMouseClicked
        elonezet(ElonezetDialog.ELONEZET);
    }//GEN-LAST:event_elonezetLabelMouseClicked

    private void szamlaTermekekTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_szamlaTermekekTableMouseReleased
              
//        boolean bool = true;
//        if (szamlaType == SzamlaType.UJ || this.szamlaType == SzamlaType.MASOLAT) {
//            bool = true;
//        } else {
//            bool = false;
//        }
        if (evt.isPopupTrigger()/*&& bool*/)
        {
            JTable source = (JTable) evt.getSource();
            int row = source.rowAtPoint(evt.getPoint());
            int column = source.columnAtPoint(evt.getPoint());

            if (!source.isRowSelected(row))
            {
                source.changeSelection(row, column, false, false);
            }

            int[] rows = szamlaTermekekTable.getSelectedRows();

            modositMenuItem.setVisible(rows.length == 1);

            szamlaTermekPopupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
        
        
            int clickedItem = szamlaTermekekTable.getSelectedRow();
            if(clickedItem + 1 > termekekSize)
            {
                szamlaTermekPopupMenu.removeAll();
                szamlaTermekPopupMenu.add(editMenuItem);
                szamlaTermekPopupMenu.add(vtszMenuItem);
                szamlaTermekPopupMenu.add(torolMenuItem);
                szamlaTermekPopupMenu.updateUI();    
            }
            else
            {
                szamlaTermekPopupMenu.add(modositMenuItem);
                szamlaTermekPopupMenu.add(editMenuItem);
                szamlaTermekPopupMenu.add(vtszMenuItem);
                szamlaTermekPopupMenu.add(torolMenuItem);
                szamlaTermekPopupMenu.setSize(175, 94);
                szamlaTermekPopupMenu.updateUI(); 
            }
        }
    }//GEN-LAST:event_szamlaTermekekTableMouseReleased

    int termekekSize;
    private void torolMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_torolMenuItemActionPerformed
        int[] rows = szamlaTermekekTable.getSelectedRows();
        
        for(int i=rows.length - 1; i >= 0; i--)
        {           
            if(rows[i] <= termekekSize)
            {
                System.out.println("Nem törölhet terméket az eredeti számláról!");
            }
            else
            {
                termekek.remove(rows[i]);
            }
            System.out.print(rows[i]);
        }
        szamlaTermekekFrissites();
        hozzaadModosit.setText("Hozzáad");
    }//GEN-LAST:event_torolMenuItemActionPerformed

    private void modositMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modositMenuItemActionPerformed
        int[] rows = szamlaTermekekTable.getSelectedRows();
        termekid = rows[0];
        aktualis = (SzamlaTermek) termekek.get(termekid);
        termek.setText(aktualis.getNev());
        cikkszam.setText(aktualis.getCikkszam());
        mennyiseg.setText(aktualis.getMennyiseg() + "");
        mee.setText(aktualis.getMee());
        egysegar.setText(aktualis.getEgysegar() + "");
        vtszTeszor.setText(aktualis.getVtszTeszor());
        Label l;
        int j = 0;
        for (int i = 0; i < afaComboBox.getItemCount(); i++)
        {
            l = (Label) afaComboBox.getItemAt(i);
            //System.out.print(afaComboBox.getItemAt(i));
            //System.out.print(String.valueOf(aktualis.getAfa()) + "%-os");
            
            if (String.valueOf(l.getName()).equals(String.valueOf((int)aktualis.getAfa()) + "%-os"))
            {
                j = i;
                break;
            }
        }
        afaComboBox.setSelectedIndex(j);
        hozzaadModosit.setText("Módosít");
    }//GEN-LAST:event_modositMenuItemActionPerformed

    private void nyomtatasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nyomtatasActionPerformed
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        if (nyomtatas.isSelected()) {
            //java.net.URL url = ClassLoader.getSystemResource("cezeszamlazo/resources/print_20.png");
            //java.awt.Image img = toolkit.createImage(url);
            mentesButton.setText("Mentés és nyomtatás");
            //mentesLabel.setIcon((Icon) img);
        } else {
            //java.net.URL url = ClassLoader.getSystemResource("cezeszamlazo/resources/checkmark_20.png");
            //java.awt.Image img = toolkit.createImage(url);
            mentesButton.setText("Mentés");
            //mentesLabel.setIcon((Icon) img);
        }
        nyomtatasPeldany.setEnabled(nyomtatas.isSelected());
        nyomtatasPeldany.setEditable(nyomtatas.isSelected());
    }//GEN-LAST:event_nyomtatasActionPerformed

    private void fizetesiModActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fizetesiModActionPerformed
        vevoFizetesiModComboBox.setSelectedIndex(fizetesiMod.getSelectedIndex());
    }//GEN-LAST:event_fizetesiModActionPerformed

    private void jPanel12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel12MouseClicked
        //boolean v = false;
        int j = 0;
        for (int i = 0; i < termekek.size(); i++) {
            SzamlaTermek szt = (SzamlaTermek) termekek.get(i);
            /*if (szt.getVtszTeszor().isEmpty())
            {
               v = true;
                j = i + 1;
                break;
            }*/
        }
        /*if (v) {
            HibaDialog h = new HibaDialog("A(z) " + j + ". termékhez nincs megadva VTSZ/TESZOR szám!", "Ok", "");
        } else*/
        if (termekek.isEmpty())
        {
            HibaDialog h = new HibaDialog("Nincs termék hozzáadva a számlához!", "Ok", "");
        }
        else if (vevoTextField.getText().isEmpty())
        {
            HibaDialog h = new HibaDialog("Nincs vevő megadva!", "Ok", "");
        }
        else
        {
            nyit(osszegzoDialog, "Összegző");
        }
    }//GEN-LAST:event_jPanel12MouseClicked

    private void jPanel13MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel13MouseClicked
        doClose(RET_CANCEL);
    }//GEN-LAST:event_jPanel13MouseClicked

    private void jPanel12MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel12MouseEntered
        jPanel12.setBackground(Color.decode("#abd043"));
    }//GEN-LAST:event_jPanel12MouseEntered

    private void jPanel12MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel12MouseExited
        jPanel12.setBackground(Color.decode("#f0f0f0"));
    }//GEN-LAST:event_jPanel12MouseExited

    private void jPanel13MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel13MouseEntered
        jPanel13.setBackground(Color.decode("#d24343"));
    }//GEN-LAST:event_jPanel13MouseEntered

    private void jPanel13MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel13MouseExited
        jPanel13.setBackground(Color.decode("#f0f0f0"));
    }//GEN-LAST:event_jPanel13MouseExited

    private void mentesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mentesButtonActionPerformed
        folyamatbanDialog = new FolyamatbanDialog("A mentés folyamatban. Kis türelmet...");
        SzamlaThread sz = new SzamlaThread();
        folyamatbanDialog.setVisible(true);
    }//GEN-LAST:event_mentesButtonActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        osszegzoDialog.setVisible(false);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void szamlaTermekekTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_szamlaTermekekTableMouseClicked
        if (evt.getClickCount() == 2 && evt.getButton() == MouseEvent.BUTTON1 /*&& isModosit*/)
        {
            int[] rows = szamlaTermekekTable.getSelectedRows();
            termekid = rows[0];
            aktualis = (SzamlaTermek) termekek.get(termekid);
            termek.setText(aktualis.getNev());
            cikkszam.setText(aktualis.getCikkszam());
            mennyiseg.setText(aktualis.getMennyiseg() + "");
            mee.setText(aktualis.getMee());
            egysegar.setText(aktualis.getEgysegar() + "");
            vtszTeszor.setText(aktualis.getVtszTeszor());
            Label l;
            int j = 0;
            for (int i = 0; i < afaComboBox.getItemCount(); i++)
            {
                l = (Label) afaComboBox.getItemAt(i);
                String comboboxCurrent = "";
                String actual= "";
                try
                {
                    comboboxCurrent = String.valueOf(l.getName()).split("%")[0];
                    actual = String.valueOf(aktualis.getAfaLabel()).split("%")[0];
                }
                catch(NullPointerException e)
                {
                    comboboxCurrent = String.valueOf(l.getName());
                    actual = String.valueOf(aktualis.getAfaLabel());
                }

                //System.err.println(String.valueOf(l.getName()));
                //System.err.println(String.valueOf(aktualis.getAfaLabel()));
                if (comboboxCurrent.equals(actual))
                {
                    j = i;
                    break;
                }
            }
            afaComboBox.setSelectedIndex(j);
            
            if(this.szamlaType == szamlaType.MODOSIT)
            {
                hozzaadModosit.setText("Módosít");
            }
            else if(this.szamlaType == SzamlaType.UJ || this.szamlaType == SzamlaType.DEVIZA || this.szamlaType == SzamlaType.MASOLAT)
            {
                hozzaadModosit.setText("Szerkeszt");
            }
            
        }
    }//GEN-LAST:event_szamlaTermekekTableMouseClicked

    private void vtszMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vtszMenuItemActionPerformed
        VtszTeszorListaDialog v = new VtszTeszorListaDialog();
        if (v.getReturnStatus() == 1) {
            int[] rows = szamlaTermekekTable.getSelectedRows();
            for (int i = 0; i < rows.length; i++) {
                SzamlaTermek szt = (SzamlaTermek) termekek.get(rows[i]);
                szt.setVtszTeszor(v.getVtszTeszor());
                szt.setAfa(Double.parseDouble(v.getAfa()));
                termekek.set(rows[i], szt);
            }
            szamlaTermekekFrissites();
        }
    }//GEN-LAST:event_vtszMenuItemActionPerformed

    private void termekdijAtvallalasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_termekdijAtvallalasActionPerformed
        szamlaTermekekFrissites();
        String m = megjegyzes.getText();
        String s = "a termékdíj-kötelezettség a Ktdt. 14. § (5) bekezdés d) pontja alapján a vevőt terheli.";
        if (termekdijAtvallalas.isSelected())
        {
            if (!m.contains(s))
            {
                m = s + " " + m;
            }
            megjegyzes.setText(m);
            takeOverType.setEnabled(true);
        }
        else
        {
            m = m.replace(s, "");
            megjegyzes.setText(m);
            takeOverType.setEnabled(false);
        }
    }//GEN-LAST:event_termekdijAtvallalasActionPerformed

    private void szallitoSzamlaLablecTextAreaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_szallitoSzamlaLablecTextAreaKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_szallitoSzamlaLablecTextAreaKeyReleased

    
    private void takeOverTypePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_takeOverTypePropertyChange
        String selectedItem = takeOverType.getSelectedItem().toString();
        String comment = "";
        switch(selectedItem)
        {
            case "00":
                comment = "";
                break;
            case "01":
                comment = takeOverTYPE.get(1);
                break;
            case "02_aa":
                comment = takeOverTYPE.get(2);
                    break;
            case "02_ab":
                comment = takeOverTYPE.get(3);
                break;
            case "02_b":
                comment = takeOverTYPE.get(4);
                break;
            case "02_c":
                comment = takeOverTYPE.get(5);
                break;
            case "02_d":
                comment = takeOverTYPE.get(6);
                break;
            case "02_ea":
                comment = takeOverTYPE.get(7);
                break;
            case "02_eb":
                comment = takeOverTYPE.get(8);
                break;
            case "02_fa":
                comment = takeOverTYPE.get(9);
                break;
            case "02_fb":
                comment = takeOverTYPE.get(10);
                break;
            case "02_ga":
                comment = takeOverTYPE.get(11);
                break;
            case "02_gb":
                comment = takeOverTYPE.get(12);
                break;
        }
        megjegyzes.setText(comment);
    }//GEN-LAST:event_takeOverTypePropertyChange

    int editableRow;
    private void editMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editMenuItemActionPerformed
        int row = szamlaTermekekTable.getSelectedRow();
        if(row < termekekSize)
        {
            System.out.println("Szerkesztés csak új tétel esetében lehetséges");
        }
        else
        {
            System.out.println("Szerkesztés lehetséges");
            editableRow = row;
            termekid = row;
            aktualis = (SzamlaTermek) termekek.get(termekid);
            termek.setText(aktualis.getNev());
            cikkszam.setText(aktualis.getCikkszam());
            mennyiseg.setText(aktualis.getMennyiseg() + "");
            mee.setText(aktualis.getMee());
            egysegar.setText(aktualis.getEgysegar() + "");
            vtszTeszor.setText(aktualis.getVtszTeszor());
            Label l;
            int j = 0;
            for (int i = 0; i < afaComboBox.getItemCount(); i++)
            {
                l = (Label) afaComboBox.getItemAt(i);

                if (String.valueOf(l.getName()).equals(String.valueOf((int)aktualis.getAfa()) + "%-os"))
                {
                    j = i;
                    break;
                }
            }
            afaComboBox.setSelectedIndex(j);
        
            hozzaadModosit.setText("Szerkeszt");
        }
    }//GEN-LAST:event_editMenuItemActionPerformed

    private void exitSupplierDatasDialogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitSupplierDatasDialogActionPerformed
        szallitoDialog.setVisible(false);
    }//GEN-LAST:event_exitSupplierDatasDialogActionPerformed

    private void jPanel15MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel15MouseExited
        jPanel15.setBackground(Color.decode("#f0f0f0"));
    }//GEN-LAST:event_jPanel15MouseExited

    private void jPanel15MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel15MouseEntered
        jPanel15.setBackground(Color.decode("#d24343"));
    }//GEN-LAST:event_jPanel15MouseEntered

    private void jPanel15MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel15MouseClicked
        aktualis.setTermekDij(null);
    }//GEN-LAST:event_jPanel15MouseClicked

    private void jPanel14MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel14MouseExited
        jPanel14.setBackground(Color.decode("#f0f0f0"));
    }//GEN-LAST:event_jPanel14MouseExited

    private void jPanel14MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel14MouseEntered
        jPanel14.setBackground(Color.decode("#abd043"));
    }//GEN-LAST:event_jPanel14MouseEntered

    private void jPanel14MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel14MouseClicked
        TermekDijDialog tdd;
        if (aktualis.getTermekDij() == null)
        {
            tdd = new TermekDijDialog();
        }
        else
        {
            tdd = new TermekDijDialog(aktualis.getTermekDij());
        }
        if (tdd.getReturnStatus() == 1)
        {
            aktualis.setTermekDij(tdd.getTermekDij());
        }
    }//GEN-LAST:event_jPanel14MouseClicked

    private void jLabel31MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel31MouseExited
        jLabel31.setBackground(Color.decode("#f0f0f0"));
    }//GEN-LAST:event_jLabel31MouseExited

    private void jLabel31MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel31MouseEntered
        jLabel31.setBackground(Color.decode("#abd043"));
    }//GEN-LAST:event_jLabel31MouseEntered

    private void jLabel31MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel31MouseClicked
        TermekListaDialog t = new TermekListaDialog();
        if (t.getReturnStatus() == 1) {
            termek.setText(t.getTermek());
            mee.setText(t.getMee());
            egysegar.setText(t.getEgysegar());
            cikkszam.setText(t.getCikkszam());
            vtszTeszor.setText(t.getVtszTeszor());
            for (int i = 0; i < afaComboBox.getItemCount(); i++) {
                Label l = (Label) afaComboBox.getItemAt(i);
                if (l.getId().equalsIgnoreCase(t.getAfa())) {
                    afaComboBox.setSelectedIndex(i);
                    break;
                }
            }
        }
    }//GEN-LAST:event_jLabel31MouseClicked

    private void vtszTeszorTallozasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vtszTeszorTallozasActionPerformed
        VtszTeszorListaDialog v = new VtszTeszorListaDialog();
        if (v.getReturnStatus() == 1) {
            vtszTeszor.setText(v.getVtszTeszor());
            Label l;
            int j = 0;
            for (int i = 0; i < afaComboBox.getItemCount(); i++) {
                l = (Label) afaComboBox.getItemAt(i);
                if (l.getName().equalsIgnoreCase(v.getAfa())) {
                    j = i;
                    break;
                }
            }
            afaComboBox.setSelectedIndex(j);
        }
    }//GEN-LAST:event_vtszTeszorTallozasActionPerformed

    private void kiuritActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kiuritActionPerformed
        kiurit();
    }//GEN-LAST:event_kiuritActionPerformed

    private void hozzaadModositActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hozzaadModositActionPerformed

        if (termek.getText().isEmpty())
        {
            HibaDialog hd = new HibaDialog("Nincs megadva termék név!", "Ok", "");
        }
        else if (Double.parseDouble(mennyiseg.getText()) == 0)
        {
            HibaDialog hd = new HibaDialog("Nulla a mennyiség!", "Ok", "");
            /*
            This method is not needed anymore.
            */
            /*}
        else if (vtszTeszor.getText().isEmpty())
        {
            HibaDialog hd = new HibaDialog("Nincs megadva VTSZ/TESZOR szám!", "Ok", "");*/
        }
        else
        {
            if(hozzaadModosit.getText().equals("Szerkeszt"))
            {
                Label l = (Label) afaComboBox.getSelectedItem();
                String afaLabel = String.valueOf(afaComboBox.getSelectedItem());
                SzamlaTermek szt = new SzamlaTermek(termek.getText(), cikkszam.getText(), mee.getText(), l.getId(), vtszTeszor.getText(), egysegar.getText(), mennyiseg.getText(), afaLabel);
                szt.setTermekDij(aktualis.getTermekDij());

                if (termekid == -1)
                {
                    termekek.set(editableRow, szt);
                }
                else
                {
                    termekek.set(editableRow, szt);
                    termekid = -1;
                    hozzaadModosit.setText("Hozzáad");
                }
            }
            else
            {           
                Label l = (Label) afaComboBox.getSelectedItem();
                String afaLabel = String.valueOf(afaComboBox.getSelectedItem());
                SzamlaTermek szt = new SzamlaTermek(termek.getText(), cikkszam.getText(), mee.getText(), l.getId(), vtszTeszor.getText(), egysegar.getText(), mennyiseg.getText(), afaLabel);
                szt.setTermekDij(aktualis.getTermekDij());

                if (termekid == -1)
                {
                    termekek.add(szt);
                }
                else
                {
                    termekek.add(szt);
                    termekid = -1;
                    hozzaadModosit.setText("Hozzáad");
                }
            }
            szamlaTermekekFrissites();
        }
    }//GEN-LAST:event_hozzaadModositActionPerformed

    private void afaComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_afaComboBoxActionPerformed
        if (b) {
            szamolMennyisegAlapjan();
        }
    }//GEN-LAST:event_afaComboBoxActionPerformed

    private void bruttoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bruttoKeyReleased
        csakszam(evt, deviza);
        if (!brutto.getText().isEmpty()) {
            szamolBruttoAlapjan();
        }
    }//GEN-LAST:event_bruttoKeyReleased

    private void bruttoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_bruttoFocusLost
        JTextField field = (JTextField) evt.getSource();
        if (field.getText().isEmpty()) {
            field.setText("0");
            szamolBruttoAlapjan();
        }
    }//GEN-LAST:event_bruttoFocusLost

    private void nettoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nettoKeyReleased
        csakszam(evt, true);
        if (!netto.getText().isEmpty()) {
            szamolNettoAlapjan();
        }
    }//GEN-LAST:event_nettoKeyReleased

    private void nettoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nettoFocusLost
        JTextField field = (JTextField) evt.getSource();
        if (field.getText().isEmpty()) {
            field.setText("0");
            szamolNettoAlapjan();
        }
    }//GEN-LAST:event_nettoFocusLost

    private void mennyisegKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_mennyisegKeyReleased
        csakszam(evt, true);
        if (!mennyiseg.getText().isEmpty()) {
            szamolMennyisegAlapjan();
        }
    }//GEN-LAST:event_mennyisegKeyReleased

    private void mennyisegFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_mennyisegFocusLost
        JTextField field = (JTextField) evt.getSource();
        if (field.getText().isEmpty()) {
            field.setText("0");
            szamolMennyisegAlapjan();
        }
    }//GEN-LAST:event_mennyisegFocusLost

    private void egysegarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_egysegarKeyReleased
        csakszam(evt, true);
        if (!egysegar.getText().isEmpty()) {
            szamolMennyisegAlapjan();
        }
    }//GEN-LAST:event_egysegarKeyReleased

    private void egysegarFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_egysegarFocusLost
        JTextField field = (JTextField) evt.getSource();
        if (field.getText().isEmpty()) {
            field.setText("0");
            szamolMennyisegAlapjan();
        }
    }//GEN-LAST:event_egysegarFocusLost

    private void takeOverTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_takeOverTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_takeOverTypeActionPerformed

    private void customerCountryCodeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_customerCountryCodeKeyReleased
        if (customerCountryCode.getText().length() == 2) {
            Query query = new Query.QueryBuilder()
                    .select("id, countryName, countryCode ")
                    .from("countries")
                    .where("countryCode = '" + customerCountryCode.getText() + "'")
                    .build();
            Object[][] o = App.db.select(query.getQuery());
            if (o.length != 0)
            {               
                customerCountryName.setText(String.valueOf(o[0][1]));
            }
            System.out.println(o[0][0]);
        }
    }//GEN-LAST:event_customerCountryCodeKeyReleased

    private void customerCountryNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_customerCountryNameKeyReleased
        if (customerCountryName.getText().length() >= 2) {
            Query query = new Query.QueryBuilder()
                    .select("id, countryName, countryCode ")
                    .from("countries")
                    .where("countryName = '" + customerCountryName.getText() + "'")
                    .build();
            Object[][] o = App.db.select(query.getQuery());
            if (o.length != 0)
            {               
                customerCountryCode.setText(String.valueOf(o[0][2]));
            }
        }
    }//GEN-LAST:event_customerCountryNameKeyReleased

    private void printerComboboxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_printerComboboxItemStateChanged
        //Label printerLabel = (Label) printerCombobox.getSelectedItem();
        //System.out.println("printerID: " + printerLabel.getId() + " printerCombobox.getSelectedItem(): " + printerCombobox.getSelectedItem());
        PrinterGetSet printer = new PrinterGetSet();
        printer.setPrinterName(String.valueOf(printerCombobox.getSelectedItem()));
    }//GEN-LAST:event_printerComboboxItemStateChanged

    private void customerNumberTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customerNumberTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_customerNumberTextActionPerformed

    public void doClose(int retStatus) {
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Osszegzo;
    private javax.swing.JComboBox afaComboBox;
    private javax.swing.JLabel azaz;
    private javax.swing.JTextField brutto;
    private javax.swing.JTextField cikkszam;
    private javax.swing.JTextField customerCountryCode;
    private javax.swing.JTextField customerCountryName;
    private javax.swing.JButton customerInfosButton;
    private javax.swing.JTextField customerNumberText;
    private javax.swing.JTextField customerPublicPlaceText;
    private javax.swing.JMenuItem editMenuItem;
    private javax.swing.JTextField egysegar;
    private javax.swing.JLabel elonezetLabel;
    private javax.swing.JTextField engedmenyFelar;
    private javax.swing.JTextField esedekesseg;
    private javax.swing.JButton exitSupplierDatasDialog;
    private javax.swing.JLabel fizVissz;
    private javax.swing.JComboBox fizetesiMod;
    private javax.swing.JButton hozzaadModosit;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel101;
    private javax.swing.JLabel jLabel102;
    private javax.swing.JLabel jLabel103;
    private javax.swing.JLabel jLabel104;
    private javax.swing.JLabel jLabel105;
    private javax.swing.JLabel jLabel106;
    private javax.swing.JLabel jLabel107;
    private javax.swing.JLabel jLabel108;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel111;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel kelt;
    private javax.swing.JButton kiurit;
    private javax.swing.JTextArea lablec;
    private javax.swing.JTextField mee;
    private javax.swing.JTextArea megjegyzes;
    private javax.swing.JTextField mennyiseg;
    private javax.swing.JButton mentesButton;
    private javax.swing.JMenuItem modositMenuItem;
    private javax.swing.JTextField netto;
    private javax.swing.JCheckBox nyomtatas;
    private javax.swing.JTextField nyomtatasPeldany;
    private javax.swing.JLabel osszAfaErtek;
    private javax.swing.JLabel osszBrutto;
    private javax.swing.JLabel osszNetto;
    private javax.swing.JLabel osszegzoAfaErtek;
    private javax.swing.JLabel osszegzoBrutto;
    private javax.swing.JDialog osszegzoDialog;
    private javax.swing.JLabel osszegzoFizVissz;
    private javax.swing.JLabel osszegzoNetto;
    private javax.swing.JCheckBox pdfKeszites;
    private javax.swing.JLabel penznemLabel1;
    private javax.swing.JLabel penznemLabel2;
    private javax.swing.JLabel penznemLabel3;
    private javax.swing.JComboBox<String> printerCombobox;
    private javax.swing.JLabel publicPlace;
    private javax.swing.JTextField supplierNumberText;
    private javax.swing.JTextField supplierPublicPlaceText;
    private javax.swing.JButton szallitoAdatokButton;
    private javax.swing.JTextField szallitoAdoszam;
    private javax.swing.JTextField szallitoBankszamlaszam;
    private javax.swing.JComboBox szallitoComboBox;
    private javax.swing.JDialog szallitoDialog;
    private javax.swing.JTextField szallitoEuAdoszam;
    private javax.swing.JTextField szallitoIrsz;
    private javax.swing.JTextField szallitoMegjegyzes;
    private javax.swing.JTextField szallitoNev;
    private javax.swing.JTextArea szallitoSzamlaLablecTextArea;
    private javax.swing.JTextField szallitoUtcaText;
    private javax.swing.JTextField szallitoVaros;
    private javax.swing.JComboBox szamlaCsoport;
    private javax.swing.JLabel szamlaFejlec;
    private javax.swing.JLabel szamlaSorszam;
    private javax.swing.JPopupMenu szamlaTermekPopupMenu;
    private javax.swing.JTable szamlaTermekekTable;
    private javax.swing.JComboBox takeOverType;
    private javax.swing.JTextField teljesites;
    private javax.swing.JTextField termek;
    private javax.swing.JCheckBox termekdijAtvallalas;
    private javax.swing.JMenuItem torolMenuItem;
    private javax.swing.JTextField vevoAdoszamText;
    private javax.swing.JTextField vevoBankszamlaszamText;
    private javax.swing.JButton vevoChangeButton;
    private javax.swing.JDialog vevoDialog;
    private javax.swing.JTextField vevoEmailText;
    private javax.swing.JTextField vevoEsedekessegText;
    private javax.swing.JTextField vevoEuAdoszamText;
    private javax.swing.JComboBox vevoFizetesiModComboBox;
    private javax.swing.JCheckBox vevoFizetesiModKotelezoCheckBox;
    private javax.swing.JTextField vevoIrszText;
    private javax.swing.JButton vevoMentesUjButton;
    private javax.swing.JButton vevoModositasMentes;
    private javax.swing.JTextField vevoNevText;
    private javax.swing.JButton vevoOkButton;
    private javax.swing.JCheckBox vevoSzamlanMegjelenikCheckBox;
    private javax.swing.JTextField vevoTelefonText;
    private javax.swing.JTextField vevoTextField;
    private javax.swing.JTextField vevoUtcaText;
    private javax.swing.JTextField vevoVarosText;
    private javax.swing.JMenuItem vtszMenuItem;
    private javax.swing.JTextField vtszTeszor;
    private javax.swing.JButton vtszTeszorTallozas;
    // End of variables declaration//GEN-END:variables

    private void init(String title)
    {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;

        java.net.URL url = ClassLoader.getSystemResource("cezeszamlazo/resources/uj-szamla.png");
        java.awt.Image img = toolkit.createImage(url);

        setIconImage(img);
        fillPrinterCombobox();

        szallitoDialog.setSize((int) Math.round(szallitoDialog.getPreferredSize().getWidth()), (int) Math.round(szallitoDialog.getPreferredSize().getHeight()) + 35);
        szallitoDialog.setIconImage(img);
        vevoDialog.setSize((int) Math.round(vevoDialog.getPreferredSize().getWidth()), (int) Math.round(vevoDialog.getPreferredSize().getHeight()) + 35);
        vevoDialog.setIconImage(img);
        osszegzoDialog.setSize((int) Math.round(osszegzoDialog.getPreferredSize().getWidth()), (int) Math.round(osszegzoDialog.getPreferredSize().getHeight()) + 35);

        kelt.setText(format.format(new Date()));
        esedekesseg.setText(format.format(new Date()));
        teljesites.setText(format.format(new Date()));

        esedekessegFrissites();

        setLocation(x, y);
        setTitle(title);
        setModal(true);
        setVisible(true);     
    }
    
    private void fillPrinterCombobox () 
    {
        printerCombobox.removeAllItems();
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        for (PrintService printer : printServices){
            printerCombobox.addItem(String.valueOf(printer.getName()));
        }
        PrintService service = PrintServiceLookup.lookupDefaultPrintService();
        printerCombobox.getModel().setSelectedItem(String.valueOf(service.getName()));
    }

    private void elonezet(int tipus)
    {
        Szallito szall = new Szallito(
                szallitoNev.getText(),
                szallitoIrsz.getText(),
                szallitoVaros.getText(),
                szallitoUtcaText.getText(),
                supplierPublicPlaceText.getText(),
                supplierNumberText.getText(),
                szallitoAdoszam.getText(),
                szallitoBankszamlaszam.getText(),
                szallitoMegjegyzes.getText(),
                szallitoSzamlaLablecTextArea.getText());
        Vevo v = new Vevo(
                vevoTextField.getText(),
                vevoIrszText.getText(),
                vevoVarosText.getText(),
                vevoUtcaText.getText(),
                customerPublicPlaceText.getText(),
                customerNumberText.getText(),
                customerCountryCode.getText(),
                vevoTelefonText.getText(),
                vevoEmailText.getText(),
                vevoFizetesiModComboBox.getSelectedIndex(),
                vevoFizetesiModKotelezoCheckBox.isSelected(),
                vevoEsedekessegText.getText(),
                vevoAdoszamText.getText(),
                vevoEuAdoszamText.getText(),
                vevoBankszamlaszamText.getText(),
                vevoSzamlanMegjelenikCheckBox.isSelected());
        Label l = (Label) szamlaCsoport.getSelectedItem();
        Szamla sz = new Szamla(
                szall,
                v,
                sorszam,
                kelt.getText(),
                teljesites.getText(),
                esedekesseg.getText(),
                l.getId(),
                penznem,
                kozeparfolyam,
                helyesbitett,
                helyesbitettTeljesites,
                deviza,
                megjegyzes.getText(),
                lablec.getText(),
                termekek,
                termekdijAtvallalas.isSelected());
        sz.setTipus(szamla != null ? this.szamla.getTipus() : 0);
        sz.setHelyesbitettTeljesites(szamla != null ? this.szamla.getTeljesites() : "");
        sz.setHelyesbitett(szamla != null ? this.szamla.getSorszam() : "");
        ElonezetDialog elonezet = new ElonezetDialog(sz, Integer.parseInt(nyomtatasPeldany.getText()), tipus);
    }   
    
    private void szamlaTermekekFrissites()
    {
        DefaultTableModel model = (DefaultTableModel) szamlaTermekekTable.getModel();
        for (int i = szamlaTermekekTable.getRowCount() - 1; i >= 0; i--)
        {
            model.removeRow(i);
        }
        Object[] row = new Object[10];
        double oN = 0.0, oA = 0.0, ossszBrutto = 0.0;
        for (int i = 0; i < termekek.size(); i++)
        {
            SzamlaTermek szt = (SzamlaTermek) termekek.get(i);
            row[0] = szt.getNev();
            row[1] = szt.getCikkszam();
            row[2] = szt.getMennyiseg();
            row[3] = szt.getMee();
            row[4] = String.format(java.util.Locale.US, "%.2f", szt.getEgysegar());
            row[5] = szt.getVtszTeszor();
            row[6] = String.format(java.util.Locale.US, "%.2f", szt.getNetto(deviza));
            row[7] = szt.getAfaLabel();
            oN += szt.getNetto(deviza);
            oA += szt.getAfaErtek(deviza);////String.format(java.util.Locale.US, "%.2f", szt.getAfaErtek(deviza));
            ossszBrutto += szt.getBrutto(deviza);////String.format(java.util.Locale.US, "%.2f", szt.getBrutto(deviza));
            if (szt.getTermekDij() != null && !termekdijAtvallalas.isSelected())
            {
                oA += szt.getTermekDij().getOsszTermekDijAfaErtek(deviza);
                oN += szt.getTermekDij().getOsszTermekDijNetto(deviza);
                ossszBrutto += szt.getTermekDij().getOsszTermekDijBrutto(deviza);
            }
            row[8] = String.format(java.util.Locale.US, "%.2f", szt.getAfaErtek(deviza));
            row[9] = String.format(java.util.Locale.US, "%.2f", szt.getBrutto(deviza));
            model.addRow(row);
        }
                
        osszNetto.setText(EncodeDecode.numberFormat(String.valueOf(oN), deviza) + " " + penznem);
        osszAfaErtek.setText(EncodeDecode.numberFormat(String.valueOf(oA), deviza) + " " + penznem);
        osszBrutto.setText(EncodeDecode.numberFormat(String.valueOf(ossszBrutto), deviza) + " " + penznem);
        osszegzoNetto.setText(EncodeDecode.numberFormat(String.valueOf(oN), deviza) + " " + penznem);
        osszegzoAfaErtek.setText(EncodeDecode.numberFormat(String.valueOf(oA), deviza) + " " + penznem);
        osszegzoBrutto.setText(EncodeDecode.numberFormat(String.valueOf(ossszBrutto), deviza) + " " + penznem);

        //System.out.println("oB: " + ossszBrutto);
        if (ossszBrutto < 0)
        {
            //ossszBrutto *= 1;
            fizVissz.setText("Visszatérítendő:");
        }
        osszegzoFizVissz.setText(EncodeDecode.numberFormat(String.valueOf(ossszBrutto), deviza) + " " + penznem);
        azaz.setText("azaz " + SzamlaFunctions.betuvel(ossszBrutto) + " " + penznem);
//      if (!isModosit)
//      {
//          szamlaTermekekTable.setEnabled(false);
//          hozzaadModosit.setEnabled(false);
//      }

        if(szamlaType == SzamlaType.UJ || this.szamlaType == SzamlaType.MASOLAT || this.szamlaType == SzamlaType.MODOSIT)
        {
            szamlaTermekekTable.setEnabled(true);
            hozzaadModosit.setEnabled(true);
        }
        else if (szamlaType == SzamlaType.STORNO)
        {
            szamlaTermekekTable.setEnabled(false);
            hozzaadModosit.setEnabled(false);
        }
    }

    private void szamlaCsoportFrissites()
    {
        Query query = new Query.QueryBuilder()
                .select("id, nev")
                .from("szamlazo_szamla_csoportok")
                .where("1")
                .order("nev")
                .build();
        Object[][] s = App.db.select(query.getQuery());
        szamlaCsoport.removeAllItems();
        for (int i = 0; i < s.length; i++)
        {
            szamlaCsoport.addItem(new Label(String.valueOf(s[i][0]), String.valueOf(s[i][1])));
        }
    }

    private void szallitoComboBoxFrissites(int selectedSzallitoId)
    {
        String[] id = App.user.getCeg().split(";");
        int alapertelmezettCegId = -1;
        String queryString = "";
        Object[][] select = null;
        if (selectedSzallitoId == 0)
        {
            queryString = "SELECT sorszamid FROM szamlazo_ceg_adatok AS szca "
                    + "WHERE szca.id = "
                    + "(SELECT alapertelmezett_ceg "
                    + "FROM szamlazo_users "
                    + "WHERE id = " + App.user.getId() + " )";
            select = App.db.select(queryString);
            System.out.println(queryString);
            alapertelmezettCegId = Integer.valueOf(String.valueOf(select[0][0]));
        }

        String keres = "";
        for (int i = 0; i < id.length; i++)
        {
            keres += "a.id = " + id[i] + " || ";
        }
        keres += "0";
        b = false;
        Query query = new Query.QueryBuilder()
                .select("a.id, "
                        + "CONCAT(IF(s.elotag != '', "
                        + "CONCAT(s.elotag, ' - '), ''), "
                        + "s.megnevezes, "
                        + "IF(a.deviza = 1, ' (deviza adatokkal)', '')), "
                        + "a.deviza, "
                        + "a.sorszamid ")
                .from("szamlazo_ceg_adatok a, szamlazo_szamla_sorszam s ")
                .where("a.sorszamid = s.id && (" + keres + ") ")
                .order("a.id")
                .build();

        select = App.db.select(query.getQuery());
        szallitoComboBox.removeAllItems();
//        System.out.println("query: " + query.getQuery());
        int i = 0;
        int selectedIndex = 0;
        int szamlaSorszamID = -1;
        int devizaNumber = -1;

        for (Object[] obj : select)
        {
            szamlaSorszamID = Integer.valueOf(String.valueOf(obj[3]));
            devizaNumber = Integer.valueOf(String.valueOf(obj[2]));

            if (this.deviza)
            {
                if (devizaNumber == 1 && szamlaSorszamID == selectedSzallitoId)
                {
                    selectedIndex = i;
                }
            }
            else if (selectedSzallitoId == 0 && devizaNumber != 1)
            {
                if (szamlaSorszamID == alapertelmezettCegId) {
                    selectedIndex = i;
                }
            }
            else if (szamlaSorszamID == selectedSzallitoId && devizaNumber != 1)
            {
                selectedIndex = i;
            }

            String str = String.valueOf(obj[1]);
            if (str.length() > 40)
            {
                str = str.substring(0, 37) + "...";
            }

            Label label = new Label(String.valueOf(obj[0]), str);
            szallitoComboBox.addItem(label);

            i++;
        }
        
        b = true;
        szallitoComboBox.setSelectedIndex(selectedIndex);

        switch (this.szamlaType)
        {
            case MODOSIT:
            {
                szallitoComboBox.setEnabled(false);
                break;
            }
            case UJ:
            {
                szallitoComboBox.setEnabled(true);
                break;
            }
            case MASOLAT:
            {
                szallitoComboBox.setEnabled(true);
                break;
            }
        }
    }

    private void setVevoDialogFields(Vevo vevo)
    {
        vevoTextField.setText(vevo.getNev());
        vevoNevText.setText(vevo.getNev());
        vevoIrszText.setText(vevo.getIrsz());
        vevoVarosText.setText(vevo.getVaros());
        vevoUtcaText.setText(vevo.getUtca());
        customerPublicPlaceText.setText(vevo.getKozterulet());
        customerNumberText.setText(vevo.getHazszam());
        customerCountryCode.setText(vevo.getOrszag());
        vevoTelefonText.setText(vevo.getTelefon());
        vevoEmailText.setText(vevo.getEmail());
        vevoFizetesiModComboBox.setSelectedIndex(vevo.getFizetesiMod());
        vevoFizetesiModKotelezoCheckBox.setSelected(vevo.isFizetesiModKotelezo());
        if (vevo.getFizetesiMod() == 1 && vevo.getEsedekesseg().equalsIgnoreCase("0"))
        {
            Properties prop = new Properties();
            try
            {
                prop.load(new FileInputStream("dat/beallitasok.properties"));
                vevoEsedekessegText.setText(prop.getProperty("alapEsedekesseg"));
            }
            catch (IOException ex)
            {
                System.out.println("IOException váltódott ki!");
                ex.printStackTrace();
                vevoEsedekessegText.setText("0");
            }
        }
        else
        {
            vevoEsedekessegText.setText(vevo.getEsedekesseg());
        }
        vevoAdoszamText.setText(vevo.getAdoszam());
        vevoEuAdoszamText.setText(vevo.getEuAdoszam());
        vevoBankszamlaszamText.setText(vevo.getBankszamlaszam());
        vevoSzamlanMegjelenikCheckBox.setSelected(vevo.isSzamlanMegjelenik());
    }

    private void setVevoDialogFields()
    {
        vevoTextField.setText(szamla.getVevo().getNev());
        vevoNevText.setText(szamla.getVevo().getNev());
        vevoIrszText.setText(szamla.getVevo().getIrsz());
        vevoVarosText.setText(szamla.getVevo().getVaros());
        vevoUtcaText.setText(szamla.getVevo().getUtca());
        customerPublicPlaceText.setText(szamla.getVevo().getKozterulet());
        customerNumberText.setText(szamla.getVevo().getHazszam());
        customerCountryCode.setText(szamla.getVevo().getOrszag());
        vevoTelefonText.setText(szamla.getVevo().getTelefon());
        vevoEmailText.setText(szamla.getVevo().getEmail());
        vevoFizetesiModComboBox.setSelectedIndex(szamla.getVevo().getFizetesiMod());
        vevoFizetesiModKotelezoCheckBox.setSelected(szamla.getVevo().isFizetesiModKotelezo());
        if (szamla.getVevo().getFizetesiMod() == 1 && szamla.getVevo().getEsedekesseg().equalsIgnoreCase("0"))
        {
            Properties prop = new Properties();
            try
            {
                prop.load(new FileInputStream("dat/beallitasok.properties"));
                vevoEsedekessegText.setText(prop.getProperty("alapEsedekesseg"));
            }
            catch (IOException ex)
            {
                System.out.println("IOException váltódott ki!");
                ex.printStackTrace();
                vevoEsedekessegText.setText("0");
            }
        }
        else
        {
            vevoEsedekessegText.setText(szamla.getVevo().getEsedekesseg());
        }
        vevoAdoszamText.setText(szamla.getVevo().getAdoszam());
        vevoEuAdoszamText.setText(szamla.getVevo().getEuAdoszam());
        vevoBankszamlaszamText.setText(szamla.getVevo().getBankszamlaszam());
        vevoSzamlanMegjelenikCheckBox.setSelected(szamla.getVevo().isSzamlanMegjelenik());
    }

    private void vevoFrissites(boolean isModified)
    {
        Vevo vevo = new Vevo(Integer.valueOf(vevoid));
        if (szamla == null)
        {
            setVevoDialogFields(vevo);
        }
        else
        {
            if (isModified)// ha a szamla vevo-szamlaTermek modositani kell
            { 
                setVevoDialogFields(vevo);
            }
            else
            {
                setVevoDialogFields();
            }
        }
        esedekessegFrissites();
    }
    
    private void afaFrissites()
    {
        b = false;
        Query query = new Query.QueryBuilder()
                .select("afa, megnevezes")
                .from("szamlazo_afa")
                .order("afa DESC")
                .build();
        Object[][] s = App.db.select(query.getQuery());
        afaComboBox.removeAllItems();
        for (int i = 0; i < s.length; i++)
        {
            afaComboBox.addItem(new Label(String.valueOf(s[i][0]), String.valueOf(s[i][1])));
        }
        b = true;
    }

    private void esedekessegFrissites()
    {
        Calendar c = Calendar.getInstance();
        String[] d = kelt.getText().split("-");
        c.set(Integer.parseInt(d[0]), Integer.parseInt(d[1]) - 1, Integer.parseInt(d[2]));
        if (!vevoEsedekessegText.getText().matches("0") && !vevoEsedekessegText.getText().isEmpty())
        {
            c.add(Calendar.DATE, Integer.parseInt(vevoEsedekessegText.getText()));
            esedekesseg.setText(format.format(c.getTime()));
        }
        else if (vevoEsedekessegText.getText().matches("0"))
        {
            c.add(Calendar.DATE, 0);
            esedekesseg.setText(format.format(c.getTime()));
        }
    }

    private void sorszamEllenorzes() {
        Calendar calendar = Calendar.getInstance();
        Label label = (Label) szallitoComboBox.getSelectedItem();
        Query query = new Query.QueryBuilder()
                .select("id, ev ")
                .from("szamlazo_szamla_sorszam")
                .where("id = (SELECT sorszamid from szamlazo_ceg_adatok WHERE id = " + label.getId() + ")")
                .build();
        Object[][] select = App.db.select(query.getQuery());
        if (select.length != 0 && Integer.parseInt(String.valueOf(select[0][1])) < calendar.get(Calendar.YEAR)) {

            Object[] object = new Object[1];
            object[0] = String.valueOf(calendar.get(Calendar.YEAR));
            App.db.insert("UPDATE szamlazo_szamla_sorszam SET ev = ?, db = 0 WHERE id = " + String.valueOf(select[0][0]), object, 1);
        } else {
            query = new Query.QueryBuilder()
                    .select("kelt")
                    .from("szamlazo_szamla")
                    .where("sorszamozasid = (SELECT sorszamid from szamlazo_ceg_adatok WHERE id = " + label.getId() + ")")
                    .order("id DESC LIMIT 1")
                    .build();
            select = App.db.select(query.getQuery());
            if (select.length != 0) {
                String[] datum = String.valueOf(select[0][0]).split("-");
                System.out.println(String.valueOf(select[0][0]));
                int e = Integer.parseInt(datum[0]),
                        h = Integer.parseInt(datum[1]),
                        n = Integer.parseInt(datum[2]);
                if (calendar.get(Calendar.YEAR) < e
                        || (calendar.get(Calendar.YEAR) == e && calendar.get(Calendar.MONTH) + 1 < h)
                        || (calendar.get(Calendar.YEAR) == e && calendar.get(Calendar.MONTH) + 1 == h && calendar.get(Calendar.DATE) < n)) {
                    // valami nem stimmel
                    HibaDialog hd = new HibaDialog("A legutóbbi számla kelte nagyobb, mint a beállított rendszeridő!", "Ok", "");
                }
            }
        }
    }

    private void betoltSzallito() {
        Label l = (Label) szallitoComboBox.getSelectedItem();
        Query query = new Query.QueryBuilder()
                .select("nev, irsz, varos, utca, kozterulet, hazszam, adoszam, bankszamlaszam, megjegyzes, szamla_lablec")
                .from("szamlazo_ceg_adatok")
                .where("id = " + l.getId())
                .build();
        Object[][] s = App.db.select(query.getQuery());
        szallitoNev.setText(String.valueOf(s[0][0]));
        szallitoIrsz.setText(String.valueOf(s[0][1]));
        szallitoVaros.setText(String.valueOf(s[0][2]));
        szallitoUtcaText.setText(String.valueOf(s[0][3]));
        supplierPublicPlaceText.setText(String.valueOf(s[0][4]));
        supplierNumberText.setText(String.valueOf(s[0][5]));
        szallitoAdoszam.setText(String.valueOf(s[0][6]));
        szallitoBankszamlaszam.setText(String.valueOf(s[0][7]));
        szallitoMegjegyzes.setText(String.valueOf(s[0][8]));
        szallitoSzamlaLablecTextArea.setText(String.valueOf(s[0][9]));
    }

    private void kiurit() {
        termek.setText("");
        cikkszam.setText("");
        mennyiseg.setText("1");
        mee.setText("db");
        egysegar.setText("0");
        vtszTeszor.setText("");
        netto.setText("0");
        brutto.setText("0");
        b = false;
        afaComboBox.setSelectedIndex(0);
        termekid = -1;
        hozzaadModosit.setText("Hozzáad");
        aktualis = new SzamlaTermek("", "", "db", "27", "", "0", "0", "27%");
        b = true;
    }

    private void csakszam(KeyEvent evt, boolean tizedes) {
        JTextField field = (JTextField) evt.getSource();
        int pos = field.getCaretPosition();
        field.setText(SzamlaFunctions.csakszam(field.getText(), 0, tizedes));
        try {
            field.setCaretPosition(pos);
        } catch (Exception ex) {
        }
    }

    private void szamolMennyisegAlapjan() {
        double m = Double.parseDouble(mennyiseg.getText()),
                e = Double.parseDouble(egysegar.getText()),
                a = 0.0;
        Label l = (Label) afaComboBox.getSelectedItem();
        a = Double.parseDouble(l.getId()) + 100;
        if (deviza) {
            double net = Math.round(m * e * 10000.0) / 10000.0,
                    bru = Math.round(net * a) / 100.0;
            netto.setText(net + "");
            brutto.setText(bru + "");
        } else {
            double net = Math.round(m * e * 100.0) / 100.0;
            int bru = (int) Math.round(net * a / 100.0);
            netto.setText(net + "");
            brutto.setText(bru + "");
        }
    }

    private void szamolNettoAlapjan() {
        double m = Double.parseDouble(mennyiseg.getText()),
                e = 0.0,
                a = 0.0;
        Label l = (Label) afaComboBox.getSelectedItem();
        a = Double.parseDouble(l.getId()) + 100;
        if (deviza) {
            double net = Double.parseDouble(netto.getText().replace(",", ".")),
                    bru = (int) Math.round(net * a) / 100.0;
            e = Math.round(net / m * 10000.0) / 10000.0;
            brutto.setText(bru + "");
        } else {
            double net = Double.parseDouble(netto.getText().replace(",", ".")),
                    bru = (int) Math.round(net * a / 100.0);
            e = Math.round(net / m * 100.0) / 100.0;
            brutto.setText(bru + "");
        }
        egysegar.setText(e + "");
    }

    private void szamolBruttoAlapjan() {
        double m = Double.parseDouble(mennyiseg.getText()),
                e = 0.0,
                a = 0.0;
        Label l = (Label) afaComboBox.getSelectedItem();
        a = Double.parseDouble(l.getId()) + 100;
        if (deviza) {
            double net = 0.0,
                    bru = Double.parseDouble(brutto.getText());
            net = Math.round(bru * 100.0 / a * 100.0) / 100.0;
            e = Math.round(net / m * 10000.0) / 10000.0;
            netto.setText(net + "");
        } else {
            double net = 0;
            int bru = Integer.parseInt(brutto.getText());
            net = Math.round(bru * 100.0 / a * 100.0) / 100.0;
            e = Math.round(net / m * 100.0) / 100.0;
            netto.setText(net + "");
        }
        egysegar.setText(e + "");
    }

    private void nyit(Object dialog, String title) {
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

    private void szamlaMentes()
    { 
        int nyomtatva = 0;
        Calendar now = Calendar.getInstance();
        String azon = String.valueOf(now.getTimeInMillis()).substring(0, 10) + String.valueOf((int) Math.round(Math.random() * 89) + 10);

        Object[] szamlaObject = new Object[44];
        
        Label szallitoLabel = (Label) szallitoComboBox.getSelectedItem();
        //System.out.println("szallitoLabelID: " + szallitoLabel.getId() + " szallitoComboBox.getSelectedItem(): " + szallitoComboBox.getSelectedItem());
        Suppliers suppliers = new Suppliers();
        suppliers.setSupplierID(szallitoLabel.getId());
        szamlaObject[0] = fizetesiMod.getSelectedIndex();
        Label l = (Label) szamlaCsoport.getSelectedItem();
        szamlaObject[1] = l.getId();
        szamlaObject[2] = kelt.getText();
        szamlaObject[3] = esedekesseg.getText();
        szamlaObject[4] = teljesites.getText();
        if (fizetesiMod.getSelectedIndex() == 0)
        {
            szamlaObject[5] = format.format(new Date());
        }
        else
        {
            szamlaObject[5] = "0000-00-00";
        }

        szamlaObject[6] = SzamlaFunctions.csakszam(osszNetto.getText(), 0, deviza); // ?
        szamlaObject[7] = SzamlaFunctions.csakszam(osszAfaErtek.getText(), 0, deviza); // ?
        
        if (szamla != null && szamla.getTipus() == 2)
        {
            helyesbitett = szamla.getSorszam();
            helyesbitettTeljesites = szamla.getTeljesites();
        }
        if (helyesbitett.isEmpty())
        {
            if (fizetesiMod.getSelectedIndex() == 0)
            {
                szamlaObject[8] = "1";
            }
            else
            {
                szamlaObject[8] = "0";
            }
        }
        else
        {
            szamlaObject[8] = "2";
        }
        szamlaObject[9] = vevoTextField.getText();
        szamlaObject[10] = vevoIrszText.getText();
        szamlaObject[11] = vevoVarosText.getText();
        szamlaObject[12] = vevoUtcaText.getText(); 
        szamlaObject[13] = customerPublicPlaceText.getText();
        szamlaObject[14] = customerNumberText.getText();
        szamlaObject[15] = customerCountryCode.getText();
        szamlaObject[16] = vevoTelefonText.getText();
        szamlaObject[17] = vevoEmailText.getText();
        szamlaObject[18] = vevoBankszamlaszamText.getText();
        szamlaObject[19] = vevoAdoszamText.getText();
        szamlaObject[20] = vevoEuAdoszamText.getText();
        szamlaObject[21] = megjegyzes.getText();
        szamlaObject[22] = (nyomtatas.isSelected() ? 1 : 0);
        szamlaObject[23] = sorszam;
        if (penznem.matches("Ft"))
        {
            szamlaObject[24] = "huf";
        }
        else if (penznem.matches("EUR"))
        {
            szamlaObject[24] = "eur";
        }
        else if (penznem.matches("USD"))
        {
            szamlaObject[24] = "usd";
        }
        szamlaObject[25] = kozeparfolyam;
        szamlaObject[26] = szallitoNev.getText();
        szamlaObject[27] = szallitoIrsz.getText();
        szamlaObject[28] = szallitoVaros.getText();
        szamlaObject[29] = szallitoUtcaText.getText(); 
        szamlaObject[30] = supplierPublicPlaceText.getText();
        szamlaObject[31] = supplierNumberText.getText();
        szamlaObject[32] = szallitoAdoszam.getText(); 
        szamlaObject[33] = szallitoEuAdoszam.getText();  
        szamlaObject[34] = szallitoBankszamlaszam.getText();
        szamlaObject[35] = ""; //szallito_egyeb
        szamlaObject[36] = (vevoSzamlanMegjelenikCheckBox.isSelected() ? 1 : 0);
        szamlaObject[37] = helyesbitett;
        szamlaObject[38] = lablec.getText();
        szamlaObject[39] = azon;
        szamlaObject[40] = (deviza ? 1 : 0);
        szamlaObject[41] = (termekdijAtvallalas.isSelected() ? 1 : 0);
        
        //innentől írtam én (Tomy)
        //Adatok összegyüjtése számla küldéshez
        String [] referenceDatas = new String[5];
        if(this.szamlaType != szamlaType.UJ && this.szamlaType != szamlaType.DEVIZA && this.szamlaType != szamlaType.MASOLAT)
        {
        //timeStamp
            DateFormat timeStamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            timeStamp.setTimeZone(TimeZone.getTimeZone("GMT"));
            String timeSTAMP = timeStamp.format(new Date());
        //issueDate
            DateFormat issueDate = new SimpleDateFormat("yyyy-MM-dd");
            String issueDATE = issueDate.format(new Date());
        //lastModificationReference
            String originalInvoiceNumber = findOriginalInvoiceNumber(sorszam);
            
            String lastModificationReference = "";
            Query query = new Query.QueryBuilder()
                    .select("id, szamla_sorszam")
                    .from("szamlazo_szamla")
                    .where("helyesbitett LIKE '" + originalInvoiceNumber + "'")
                    .order("id DESC")
                    .build();
            Object[][] select = App.db.select(query.getQuery());       

            lastModificationReference = select[0][1].toString();

            try
            {
                lastModificationReference = select[0][1].toString();
            }
            catch(ArrayIndexOutOfBoundsException e)
            {
                lastModificationReference = "";
            }

        //NAVStatus
            String [] helyesbitettSplit = helyesbitett.split("/");
            System.out.println("helyesbitett: " + helyesbitett);
            query = new Query.QueryBuilder()
                    .select("NAVStatus")
                    .from("szamlazo_szamla")
                    .where("szamla_sorszam LIKE '" + helyesbitettSplit[0] + "/" + helyesbitettSplit[1]+"'")
                    .build();
            Object [][] NAVstatus = App.db.select(query.getQuery());
            int NAVStatus = (Integer) NAVstatus[0][0];

            referenceDatas[0] = helyesbitett;
            referenceDatas[1] = issueDATE;
            referenceDatas[2] = timeSTAMP;
            
            if(lastModificationReference.equals(helyesbitett))
            {
                referenceDatas[3] = "";
            }
            else
            {
                referenceDatas[3] = lastModificationReference;
            }
            
            if(NAVStatus == 0)
            {
                referenceDatas[4] = "true";
            }
            else
            {
                referenceDatas[4] = "false"; 
            }           
        }
        else
        {
            for(int i = 0; i < referenceDatas.length; i++)
            {
                referenceDatas[i] = "";
            }
        }        
               
        String [] supplierInfo = new String[23];
    //supplierTaxNumber
        String supplierTaxNumber = szallitoAdoszam.getText().replaceAll("[^0-9]","");
        supplierInfo[0] = supplierTaxNumber.substring(0,8);//TaxPayerId
        supplierInfo[1] = supplierTaxNumber.substring(8,9);//VatCode
        supplierInfo[2] = supplierTaxNumber.substring(9);//CountyCode      
    //groupMemberTaxNumber
        supplierInfo[3] = "";//groupMemberTaxPayerId
        supplierInfo[4] = "";//groupMemberVatCode
        supplierInfo[5] = "";//groupMemberCountyCode
        supplierInfo[6] = "";//communityVatNumber
        supplierInfo[7] = szallitoNev.getText();//supplierName
    //supplierAddress       
        Query query = new Query.QueryBuilder()
                .select("countryCode")
                .from("szamlazo_ceg_adatok")
                .where("adoszam LIKE '" + supplierInfo[0] + "-" + supplierInfo[1] + "-" + supplierInfo[2] + "'")                
                .build();
        Object [][] countryCODE = App.db.select(query.getQuery());
        
        supplierInfo[8] = countryCODE[0][0].toString();
        supplierInfo[9] = "";//region
        supplierInfo[10] = szallitoIrsz.getText();//postalCode
        supplierInfo[11] = szallitoVaros.getText();//city
        supplierInfo[12] = szallitoUtcaText.getText();//streetName
        supplierInfo[13] = supplierPublicPlaceText.getText();//publicPlaceCategory
        supplierInfo[14] = supplierNumberText.getText();//number
        supplierInfo[15] = "";//building
        supplierInfo[16] = "";//staircase
        supplierInfo[17] = "";//floor
        supplierInfo[18] = "";//door
        supplierInfo[19] = "";//lotNumber
        
        supplierInfo[20] = szallitoBankszamlaszam.getText();//supplierBankAccountNumber        
        supplierInfo[21] = "false";//individualExemption        
        supplierInfo[22] = "";//exciseLicenceNum
        
        String [] customerInfo = new String[22];
    //customerTaxNumber
        String customerTaxNumber = vevoAdoszamText.getText().replaceAll("[^0-9]","");
        if(customerTaxNumber.equals(""))
        {
            customerInfo[0] = "";//TaxPayerId
            customerInfo[1] = "";//VatCode
            customerInfo[2] = "";//CountyCode
        }
        else
        {       
            customerInfo[0] = customerTaxNumber.substring(0,8);//TaxPayerId
            customerInfo[1] = customerTaxNumber.substring(8,9);//VatCode
            customerInfo[2] = customerTaxNumber.substring(9);//CountyCode
        }
        
    //groupMemberTaxNumber
        customerInfo[3] = "";//groupMemberTaxPayerId
        customerInfo[4] = "";//groupMemberVatCode
        customerInfo[5] = "";//groupMemberCountyCode
        customerInfo[6] = "";//communityVatNumber    
        customerInfo[7] = "";//thirdStateTaxId
        customerInfo[8] = vevoNevText.getText();
    //customerAddress
        customerInfo[9] = customerCountryCode.getText();//countryCode
        customerInfo[10] = "";//region
        customerInfo[11] = szallitoIrsz.getText();//postalCode
        customerInfo[12] = szallitoVaros.getText();//city
        customerInfo[13] = vevoUtcaText.getText();//streetName
        customerInfo[14] = customerPublicPlaceText.getText();//publicPlaceCategory
        customerInfo[15] = customerNumberText.getText();//number
        customerInfo[16] = "";//building
        customerInfo[17] = "";//staircase
        customerInfo[18] = "";//floor
        customerInfo[19] = "";//door
        customerInfo[20] = "";//lotNumber
               
        customerInfo[21] = vevoBankszamlaszamText.getText();//customerBankAccountNumber
        
        String [] fiscalRepresentativeInfo = new String[17];
    //fiscalRepresentativeTaxNumber
        fiscalRepresentativeInfo[0] = "";//TaxPayerId
        fiscalRepresentativeInfo[1] = "";//VatCode
        fiscalRepresentativeInfo[2] = "";//CountyCode   
        fiscalRepresentativeInfo[3] = "";//fiscalName
    //fiscalRepresentativeAddress
        fiscalRepresentativeInfo[4] = "";//countryCode
        fiscalRepresentativeInfo[5] = "";//region
        fiscalRepresentativeInfo[6] = "";//postalCode
        fiscalRepresentativeInfo[7] = "";//city
        fiscalRepresentativeInfo[8] = "";//streetName
        fiscalRepresentativeInfo[9] = "";//publicPlaceCategory
        fiscalRepresentativeInfo[10] = "";//number
        fiscalRepresentativeInfo[11] = "";//building
        fiscalRepresentativeInfo[12] = "";//staircase
        fiscalRepresentativeInfo[13] = "";//floor
        fiscalRepresentativeInfo[14] = "";//door
        fiscalRepresentativeInfo[15] = "";//lotNumber
        
        fiscalRepresentativeInfo[16] = "";//fiscalBankAccountNumber
        
        String [] invoiceData = new String[15];
        invoiceData[0] = sorszam;//invoiceNumber
        invoiceData[1] = "NORMAL";//invoiceCategory  others: SIMPLIFIED,AGGREGATE
        invoiceData[2] = kelt.getText();//invoiceIssueDate
        invoiceData[3] = esedekesseg.getText();//invoiceDeliveryDate
        invoiceData[4] = "";//invoiceDeliveryPeriodStart
        invoiceData[5] = "";//invoiceDeliveryPeriodEnd
        invoiceData[6] = "";//invoiceAccountingDeliveryDate
        if (penznem.matches("Ft"))//currencyCode
        {
            invoiceData[7] = "HUF";
        }
        else if (penznem.matches("EUR"))
        {
            invoiceData[7] = "EUR";
        }
        else if (penznem.matches("USD"))
        {
            invoiceData[7] = "USD";
        }
        invoiceData[8] = String.valueOf(kozeparfolyam);//exchangeRate
        invoiceData[9] = "";//selfBillingIndicator
        if (fizetesiMod.getSelectedItem().toString().matches("Készpénz"))//paymentMethod
        {
            invoiceData[10] = "CASH";
        }
        else if (fizetesiMod.getSelectedItem().toString().matches("Átutalás"))
        {
            invoiceData[10] = "TRANSFER";
        }
        else if (fizetesiMod.getSelectedItem().toString().matches("Utánvét"))
        {
            invoiceData[10] = "OTHER";
        }
        invoiceData[11] = teljesites.getText();//paymentDATE
        invoiceData[12] = "";//cashAccountingIndicator
        invoiceData[13] = "PAPER";//invoiceAppearance others: ELECTRONIC,EDI,UNKNOWN
        invoiceData[14] = "";//electronicInvoiceHash
        
        //Ha van egyéb adat, azt itt kell közölni
        String [][] additionalInvoiceData = new String[1][3];
        additionalInvoiceData[0][0] = "";
        additionalInvoiceData[0][1] = "";
        additionalInvoiceData[0][2] = "";
        
        String [][] lines = new String[termekek.size()][56]; 
        String [][] lineProductFeeCONTENT = new String[termekek.size()][6];
        int i = 0;
        
        double totalVatValue = 0;
        
        for (Object termekObject : termekek)
        {
            SzamlaTermek szt = (SzamlaTermek) termekObject;
            if(this.szamlaType == SzamlaType.STORNO)
            {    
                String lineNumberReference = String.valueOf(i+1);
                String lineOperation = "MODIFY";
                lines[i][0] = lineNumberReference;//lineNumberReference
                lines[i][1] = lineOperation;//lineOperation
            }
            else if(this.szamlaType == SzamlaType.MODOSIT)
            {       
                String lineNumberReference = String.valueOf(i+1);//hiba: a line number ref értékét meg kell kapni
                String lineOperation = "MODIFY/CREATE";
                lines[i][0] = lineNumberReference;//lineNumberReference
                lines[i][1] = lineOperation;//lineOperation
            }
            else
            {
                lines[i][0] = "";//lineNumberReference
                lines[i][1] = "";//lineOperation
            }
            lines[i][2] = "";//referencesToOtherLines
            lines[i][3] = "";//advanceIndicator
            lines[i][4] = "";//productCodes
            lines[i][5] = szt.getNev();//lineDescription
            lines[i][6] = String.valueOf(szt.getMennyiseg());//quantity
            lines[i][7] = szt.getMee();//unitOfMeasure
            lines[i][8] = String.format(java.util.Locale.US, "%.2f", szt.getEgysegar());//unitPrice
            lines[i][9] = "";//discountDescription
            lines[i][10] = "";//discountValue
            lines[i][11] = "";//discountRate
            lines[i][12] = String.format(java.util.Locale.US, "%.2f", szt.getNetto(false));//lineNetAmount
            lines[i][14] = "";
            lines[i][15] = "";
            lines[i][16] = "";
            lines[i][17] = "";
            lines[i][18] = "";
            lines[i][19] = "";
            //System.out.println("szt.getAfaLabel(): " + szt.getAfaLabel());
            switch(szt.getAfaLabel())
            {
                case "5%":
                    lines[i][13] = "vatPercentage";//lineVatRate
                    lines[i][14] = String.valueOf(0.05);
                    break;
                case "10%":
                    lines[i][13] = "vatPercentage";//lineVatRate
                    lines[i][14] = String.valueOf(0.1);
                    break;
                case "27%":
                    lines[i][13] = "vatPercentage";//lineVatRate
                    lines[i][14] = String.valueOf(0.27);
                    break;
                case "AAM":
                    lines[i][13] = "vatExemption";//lineVatRate
                    lines[i][15] = String.valueOf(0.0);
                    break;
                case "Az Áfa törvény hatályán kívüli":
                    lines[i][13] = "vatOutOfScope";//lineVatRate
                    lines[i][16] = String.valueOf("true");
                    break;
                case "Belföldi fordított adózás":
                    lines[i][13] = "VatDomesticReverseCharge";//lineVatRate
                    lines[i][17] = String.valueOf("true");
                    break;
                case "Áthárított adót tartalmazó különbözet szerinti adózás":
                    lines[i][13] = "marginSchemeVat";//lineVatRate
                    lines[i][18] = String.valueOf("true");
                    break;
                case "Áthárított adót nem tartalmazó különbözet szerinti adózás":
                    lines[i][13] = "marginSchemeNoVat";//lineVatRate
                    lines[i][19] = String.valueOf("true");
                    break;
            }          
            lines[i][20] = String.format(java.util.Locale.US, "%.2f", szt.getAfaErtek(false));
            lines[i][21] = String.format(java.util.Locale.US, "%.2f", szt.getAfaErtek(false)* kozeparfolyam);
            totalVatValue += szt.getAfaErtek(false)* kozeparfolyam;
            lines[i][22] = String.format(java.util.Locale.US, "%.2f", szt.getNetto(false)+szt.getAfaErtek(false));
            lines[i][23] = "";//intermediatedService
            lines[i][24] = "";//lineExchangeRATE
            lines[i][25] = "";//lineDeliveryDATE
            
            lines[i][26] = "";//brand
            lines[i][27] = "";//serialNum
            lines[i][28] = "";//engineNum
            lines[i][29] = "";//firstEntryIntoService
            lines[i][30] = "";//transportMean
                lines[i][31] = "";//engineCapacity
                lines[i][32] = "";//enginePower
                lines[i][33] = "";//kms
                
                lines[i][34] = "";//lenght
                lines[i][35] = "";//activityReferred
                lines[i][36] = "";//sailedHours
                        
                lines[i][37] = "";//takeOffWeight
                lines[i][38] = "";//airCargo
                lines[i][39] = "";//operationHOURS
            lines[i][40] = "false";//depositIndicator
            lines[i][41] = ""; //TRAVEL_AGENCY, SECOND_HAND, ARTWORK, ANTIQUES marginSchemeIndicator
            lines[i][42] = ""; //ekaerIds
            lines[i][43] = ""; //obligatedForProductFEE
            lines[i][44] = ""; //GPCEXCISE
            lines[i][45] = ""; //purchaseCountryCODE
            lines[i][46] = ""; //purchaseREGION
            lines[i][47] = ""; //purchasePostalCODE
            lines[i][48] = ""; //purchaseCITY
            lines[i][49] = ""; //additionalAddressDETAIL
            lines[i][50] = ""; //purchaseDATE
            lines[i][51] = ""; //vehicleRegistrationNUMBER
            lines[i][52] = ""; //dieselOilQUANTITY
            lines[i][53] = ""; //netaDECLARATION
            
            lines[i][54] = ""; //takeoverREASON
            lines[i][55] = ""; //takeoverAMOUNT
            
            lineProductFeeCONTENT[i][0] = "";
            lineProductFeeCONTENT[i][1] = "";
            lineProductFeeCONTENT[i][2] = "";
            lineProductFeeCONTENT[i][3] = "";
            lineProductFeeCONTENT[i][4] = "";
            lineProductFeeCONTENT[i][5] = "";
            /*Termékdíj átválalás
            if(termekdijAtvallalas.isSelected())
            {
                lines[i][54] = takeOverType.getSelectedItem().toString(); //takeoverREASON
                lines[i][55] = String.format (java.util.Locale.US,"%.2f", szt.getTermekDij().getOsszTermekDijNetto(deviza)); //takeoverAMOUNT
            }
            else
            {
                lines[i][54] = ""; //takeoverREASON
                lines[i][55] = ""; //takeoverAMOUNT
            }
                        
            if(!szt.getTermekDij().getKt().equals(""))
            {
                lineProductFeeCONTENT [i][0] = "KT";
                lineProductFeeCONTENT [i][1] = szt.getTermekDij().getKt();
            }
            else if(!szt.getTermekDij().getCsk().equals(""))
            {
                lineProductFeeCONTENT [i][0] = "CSK";
                lineProductFeeCONTENT [i][1] = szt.getTermekDij().getCsk().replaceAll(" ", "");
            }
            else
            {
                System.err.println("Termékdíj esetén vagy a CSK kód vagy a KT kód mezőt kötelező kitölteni");
            }
            
            double productFeeQUANTITY = szt.getTermekDij().getOsszsuly();
            lineProductFeeCONTENT [i][2] = String.valueOf(productFeeQUANTITY); //productFeeQUANTITY 
            lineProductFeeCONTENT [i][3] = "KG"; //productFeeMeasuringUNIT
            lineProductFeeCONTENT [i][4] = String.valueOf(szt.getTermekDij().getTermekDij()); //productFeeRATE 
            double productFeeAMOUNT = szt.getTermekDij().getOsszsuly() * szt.getTermekDij().getTermekDij();
            lineProductFeeCONTENT [i][5] = String.format (java.util.Locale.US,"%.2f", productFeeAMOUNT); //productFeeAMOUNT
            */
            i++;
        }   
        
        String [][] additionalLineDATA = new String[termekek.size()][3];
        for(int j = 0; j < termekek.size(); j++)
        {           
            additionalLineDATA[j][0] = ""; //dataNAME
            additionalLineDATA[j][1] = ""; //dataDESCRIPTION
            additionalLineDATA[j][2] = ""; //dataVALUE
        }
        
        Object [] InvoiceAllData= new Object[9];
        InvoiceAllData[0] = referenceDatas;
        InvoiceAllData[1] = supplierInfo;
        InvoiceAllData[2] = customerInfo;
        InvoiceAllData[3] = fiscalRepresentativeInfo;
        InvoiceAllData[4] = invoiceData;
        InvoiceAllData[5] = additionalInvoiceData;
        InvoiceAllData[6] = lines;
        InvoiceAllData[7] = lineProductFeeCONTENT;
        InvoiceAllData[8] = additionalLineDATA;
//A számla elküldése a NAV részére (Tomy)
        
        Label szallitolabel = (Label) szallitoComboBox.getSelectedItem();
        query = new Query.QueryBuilder()
                .select("vatLimit")
                .from("szamlazo_ceg_adatok")
                .where("id LIKE '" + szallitolabel.getId() + "'")
                .build();
        Object [][] select = App.db.select(query.getQuery());       
        
        if(this.szamlaType == szamlaType.STORNO)
        {
            totalVatValue *= -1;
        }
        if(Integer.valueOf(select[0][0].toString()) <= totalVatValue)
        {
            GenerateInvoiceXMLs generate = new GenerateInvoiceXMLs();

            GenerateXml gen = new GenerateXml();        
            String xmlToUpload = "";
            try
            {
                xmlToUpload = generate.generateSzamlaXml(InvoiceAllData, this.szamlaType.toString());
                
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
            System.err.println("XML TO UPLOAD: ");
            System.err.println(xmlToUpload);
            String queryResponse = "";
            String [] file = null;
            try
            {
                query = new Query.QueryBuilder()
                        .select("requestVersion, headerVersion, softwareMainVersion, softwareId, softwareName, softwareOperation, softwareDevName, softwareDevContact, softwareDevCountryCode, softwareTaxNumber")
                        .from("szamlazo_szoftver_adatok")
                        .where("1")
                        .build();
                Object [][] softwareDataResponse = App.db.select(query.getQuery());
                
                billingSoftwareDatas[0] = softwareDataResponse[0][0].toString();
                billingSoftwareDatas[1] = softwareDataResponse[0][1].toString();
                billingSoftwareDatas[2] = softwareDataResponse[0][2].toString();
                billingSoftwareDatas[3] = softwareDataResponse[0][3].toString();
                billingSoftwareDatas[4] = softwareDataResponse[0][4].toString();
                billingSoftwareDatas[5] = softwareDataResponse[0][5].toString();
                billingSoftwareDatas[6] = softwareDataResponse[0][6].toString();
                billingSoftwareDatas[7] = softwareDataResponse[0][7].toString();
                billingSoftwareDatas[8] = softwareDataResponse[0][8].toString();
                billingSoftwareDatas[9] = softwareDataResponse[0][9].toString();

                query = new Query.QueryBuilder()
                        .select("felhasznalonev, password, alairo_kulcs, csere_kulcs")
                        .from("szamlazo_ceg_adatok")
                        .where("nev LIKE '" + supplierInfo[7] + "'")
                        .build();
                Object [][] supplierDataResponse = App.db.select(query.getQuery());
                
                SupplierDatas[0] = supplierInfo[0];
                SupplierDatas[1] = supplierDataResponse[0][0].toString();
                SupplierDatas[2] = supplierDataResponse[0][1].toString();
                SupplierDatas[3] = supplierDataResponse[0][2].toString();
                SupplierDatas[4] = supplierDataResponse[0][3].toString();

                //próba
                /*System.err.println("Próba");
                boolean Annul = false;
                boolean Compressed = false;
                String [] InvoiceData = new String[1];
                ArrayList<String[]> xmlsToUpload = new ArrayList<>();
                InvoiceData[0] = xmlToUpload;
                ArrayList<String> Operations = new ArrayList<>();
                switch(this.szamlaType.toString())
                {
                    case "UJ":
                        InvoiceData[1] = "CREATE";
                        break;
                    case "MASOLAT":
                        InvoiceData[1] = "CREATE";
                        break;
                    case "HELYESBITO":
                        InvoiceData[1] = "MODOFY";
                        break;
                    case "STORNO":
                        InvoiceData[1] = "STORNO";
                        break;
                    case "DEVIZA":
                        InvoiceData[1] = "CREATE";
                        break;
                    case "MODOSIT":
                        InvoiceData[1] = "MODOFY";
                        break;
                }
                xmlsToUpload.add(InvoiceData);
                InvoiceService service = new InvoiceService();
                String manageInvoiceRequest = service.ManageInvoice(Annul, Compressed, xmlsToUpload);
                System.err.println("Próba vége");
                */
                //próba vége
                
                
                file = gen.GenerateQueryInvoiceStatusXml(billingSoftwareDatas, SupplierDatas, xmlToUpload, this.szamlaType.toString()).split(";");       
                NAVConn conn = new NAVConn();
                String uri = "queryInvoiceStatus";
                
                queryResponse = conn.GetStatus(uri, file[0]);
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
            String status = "";

            try
            {
                status = queryResponse.split("<invoiceStatus>")[1].split("</invoiceStatus>")[0];
            }
            catch(NullPointerException ex)
            {
                //szamlaMentes();
                System.err.println("Invoice status hiba");
            }

            switch(status)
            {
                case "RECEIVED":
                    szamlaObject[42] = "1";
                    break;
                case "PROCESSING":
                    szamlaObject[42] = "2";
                    break;
                case "DONE":
                    szamlaObject[42] = "3";
                    break;
                case "ABORTED":
                    szamlaObject[42] = "4";
                    break;
                default:
                    szamlaObject[42] = "0";
                    break;
            }
            szamlaObject[43] = file[1];
            //Számla elküldve A NAV részére(Tomy)
        }
        else
        {
            //A számla áfa összege nem haladja meg a 100.000 Ft-ot
            szamlaObject[42] = "0";
            szamlaObject[43] = "";
        }
        //eddig írtam (Tomy)

        double osszeg = 0;
        try
        {
            Label label = (Label) szallitoComboBox.getSelectedItem();
            App.db.insert("INSERT INTO szamlazo_szamla (fizetesi_mod, "//0
                    + "szamla_csoport, " //1
                    + "kelt, " //2
                    + "esedekesseg, " //3
                    + "teljesites, " //4
                    + "kifizetes, " //5
                    + "netto, " //6
                    + "afa_ertek, " //7
                    + "tipus, " //8
                    + "nev, " //9
                    + "irsz, " //10
                    + "varos, " //11
                    + "utca, "  //12 
                    + "kozterulet, " //13
                    + "hazszam, " //14
                    + "orszag, " //15
                    + "telefon, " //16
                    + "email, " //17
                    + "bankszamlaszam, " //18
                    + "adoszam, " //19
                    + "eu_adoszam, " //20
                    + "megjegyzes, " //21
                    + "nyomtatva, " //22
                    + "szamla_sorszam, " //23
                    + "valuta, " //24
                    + "kozeparfolyam, " //25
                    + "szallito_nev, " //26
                    + "szallito_irsz, " //27
                    + "szallito_varos, " //28
                    + "szallito_utca, " //29 
                    + "szallito_kozterulet, " //30
                    + "szallito_hazszam, " //31
                    + "szallito_adoszam, " //32
                    + "szallito_eu_adoszam, " //33
                    + "szallito_bankszamlaszam, " //34
                    + "szallito_egyeb, " //35
                    + "szamlan_megjelenik, " //36
                    + "helyesbitett, " //37
                    + "lablec, " //38
                    + "sorszamozasid, "
                    + "azon, " //39
                    + "deviza, " //40
                    + "atvallal, " //41
                    + "NAVStatus, " //42
                    + "transactionID) " //43
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, (SELECT sorszamid FROM szamlazo_ceg_adatok WHERE id = " + label.getId() + "), ?, ?, ?, ?, ?)", szamlaObject, 44);            
            
            int idInInvoice = 0;
            if(this.szamlaType == szamlaType.UJ || this.szamlaType == szamlaType.DEVIZA || this.szamlaType == szamlaType.MASOLAT)
            {
                idInInvoice = 1;
            }
            else
            {
                query = new Query.QueryBuilder()
                    .select("szamla_sorszam")
                    .from("szamlazo_szamla")
                    .where("azon LIKE " + azon)
                    .build();
                Object [][] original = App.db.select(query.getQuery());
                
                String invoiceNumber = original[0][0].toString();
                String originalInvoiceNumber = findOriginalInvoiceNumber(invoiceNumber);
                idInInvoice = Functions.GetLastProduct(originalInvoiceNumber);//originalInvoiceNumber
                
            }
            
            for (Object termekObject : termekek)
            {
                SzamlaTermek szamlaTermek = (SzamlaTermek) termekObject;
                Object[] tmpTermek = new Object[13];
                tmpTermek[0] = sorszam;
                tmpTermek[1] = szamlaTermek.getNev();
                tmpTermek[2] = szamlaTermek.getCikkszam();
                tmpTermek[3] = szamlaTermek.getMennyiseg();
                tmpTermek[4] = szamlaTermek.getMee();
                tmpTermek[5] = szamlaTermek.getEgysegar();
                tmpTermek[6] = szamlaTermek.getNetto(deviza);
                tmpTermek[7] = szamlaTermek.getAfa();
                tmpTermek[8] = "0";
                tmpTermek[9] = szamlaTermek.getVtszTeszor();
                tmpTermek[10] = azon;
                tmpTermek[11] = szamlaTermek.getAfaLabel();   
                tmpTermek[12] = idInInvoice; 
                idInInvoice++;
                App.db.insert("INSERT INTO szamlazo_szamla_adatok (szamla_sorszam, termek, termek_kod, mennyiseg, mennyisegi_egyseg, egysegar, netto_ar, afa, engedmeny_felar, vtsz_teszor, azon, afa_label, modifiedproductID) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", tmpTermek, 13);                
                // ha !!!készpénzes!!! akkor a pixi rendszer kasszában regisztrálni!
                if (fizetesiMod.getSelectedIndex() == 0) {
                    Object[] kifizetesObject = new Object[5];
                    kifizetesObject[0] = szamlaTermek.getCikkszam();
                    kifizetesObject[1] = vevoTextField.getText();
                    kifizetesObject[2] = szamlaTermek.getBrutto(false);
                    // ha Balázs a létrehozó akkor a "Balázs kasszába" számolja
                    kifizetesObject[3] = (App.user.getId() == 1 ? 1 : 0);
                    kifizetesObject[4] = sorszam;
                    App.db.insert("INSERT INTO pixi_kifizetesek (termek_id, nev, datum, osszeg, kassza_tipus, szamla_szam) "
                            + "VALUES (?, ?, NOW(), ?, ?, ?)", kifizetesObject, kifizetesObject.length);
                }
                if (kozeparfolyam == 1.0)
                {
                    osszeg += (int) szamlaTermek.getBrutto(deviza);
                }
                else
                {
                    osszeg += szamlaTermek.getBrutto(deviza);
                }
                if (szamlaTermek.getTermekDij() != null)
                {
                    Query query2 = new Query.QueryBuilder()
                            .select("id")
                            .from("szamlazo_szamla_adatok")
                            .where("1")
                            .order("id DESC LIMIT 1")
                            .build();
                    select = App.db.select(query2.getQuery());
                    Object[] termekDijObject = new Object[10];
                    termekDijObject[0] = String.valueOf(select[0][0]);
                    termekDijObject[1] = szamlaTermek.getTermekDij().getNev();
                    termekDijObject[2] = szamlaTermek.getTermekDij().getSzelesseg();
                    termekDijObject[3] = szamlaTermek.getTermekDij().getMagassag();
                    termekDijObject[4] = szamlaTermek.getTermekDij().getPeldany();
                    termekDijObject[5] = szamlaTermek.getTermekDij().getEgysegsuly();
                    termekDijObject[6] = szamlaTermek.getTermekDij().getTermekDij();
                    termekDijObject[7] = szamlaTermek.getTermekDij().getOsszsuly();
                    termekDijObject[8] = szamlaTermek.getTermekDij().getCsk();
                    termekDijObject[9] = szamlaTermek.getTermekDij().getKt();
                    if (kozeparfolyam == 1.0)
                    {
                        osszeg += (int) szamlaTermek.getTermekDij().getOsszTermekDijBrutto(deviza);
                    }
                    else
                    {
                        osszeg += szamlaTermek.getTermekDij().getOsszTermekDijBrutto(deviza);
                    }
                    App.db.insert("INSERT INTO szamlazo_szamla_termekdij (termekid, nev, szelesseg, magassag, peldany, egysegsuly, termekdij, osszsuly, csk, kt) "
                            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", termekDijObject, 10);
                }
            }
            
            if (!helyesbitett.isEmpty()) {
                Object[] szamlazoSzamlaObject = new Object[2];
                szamlazoSzamlaObject[0] = "3";
                szamlazoSzamlaObject[1] = helyesbitett;
                App.db.insert("UPDATE szamlazo_szamla SET tipus = ? WHERE szamla_sorszam = ?", szamlazoSzamlaObject, 2);
            }
            //if (printJob.printDialog()) {
            if (nyomtatas.isSelected()) {
                elonezet(ElonezetDialog.NYOMTATAS);
            }
            //}

            if (fizetesiMod.getSelectedIndex() == 0)
            {
                Object[] szamlazoSzamlaKifizetesekObject = new Object[3];
                szamlazoSzamlaKifizetesekObject[0] = sorszam;
                szamlazoSzamlaKifizetesekObject[1] = osszeg;
                szamlazoSzamlaKifizetesekObject[2] = azon;
                App.db.insert("INSERT INTO szamlazo_szamla_kifizetesek (szamla_sorszam, datum, osszeg, azon) VALUES (?, NOW(), ?, ?)", szamlazoSzamlaKifizetesekObject, 3);
            }
            nyomtatva = 1;
            if (pdfKeszites.isSelected())
            {
                elonezet(ElonezetDialog.PDF);
            }
        

        }
        catch (Throwable ex)
        {
            ex.printStackTrace();
        }
        
       /*if(bezar) {
        updatePixiProductsAfterSave();
        }*/
       /* if(bezar)
        {
            System.out.println("Bezárás kezdete:");
            String[] argString = App.args;
//            if (argString[0].equalsIgnoreCase("p")) {
            // HA PIXIBŐL LETT INDÍTVA!! a paraméter: p cégID termékID1, termékID2
            String keres = "(0";
            for (int j = 2; j < argString.length; j++) {
                keres += ", " + argString[j];
            }
            keres += ")";
            Object[] pixiAjanlatkeresekAdataiObject = new Object[1];
            pixiAjanlatkeresekAdataiObject[0] = sorszam;
            App.db.insert("UPDATE pixi_ajanlatkeresek_adatai "
                    + "SET statusz = 5, szamla = ?, szamla_tipus = " + fizetesiMod.getSelectedIndex()
                    + "WHERE id IN " + keres,
                    pixiAjanlatkeresekAdataiObject,
                    1);
//                System.out.println("ok");           
            System.exit(1);
            //App.getApplication().exit();
//            } else {
//                String keres = "(0";
//                for (int i = 1; i < argString.length; i++) {
//                    keres += ", " + argString[i];
//                }
//                keres += ")";
//                Object[] o4 = new Object[1];
//                o4[0] = sorszam;
//                App.db.insert("UPDATE cr_ajanlatkeresek_adatai SET statusz = 6, szamla = ? WHERE id IN " + keres, o4, 1);
//                System.out.println("ok");
//                System.exit(1);
//            }
        }*/

    }
    
    private void updatePixiProductsAfterSave ()
            {
            String[] argString = App.args;
//            if (argString[0].equalsIgnoreCase("p")) {
            // HA PIXIBŐL LETT INDÍTVA!! a paraméter: p cégID termékID1, termékID2
            String keres = "(0";
            for (int j = 2; j < argString.length; j++) {
                keres += ", " + argString[j];
            }
            keres += ")";
            Object[] pixiAjanlatkeresekAdataiObject = new Object[1];
            pixiAjanlatkeresekAdataiObject[0] = sorszam;
            //System.out.println("UPDATE pixi_ajanlatkeresek_adatai " + "SET statusz = 5, szamla = " + pixiAjanlatkeresekAdataiObject + ", szamla_tipus = " + fizetesiMod.getSelectedIndex() + "WHERE id IN " + keres + ",1");
            App.db.insert("UPDATE pixi_ajanlatkeresek_adatai "
                    + "SET statusz = 5, szamla = ?, szamla_tipus = " + fizetesiMod.getSelectedIndex()
                    + " WHERE id IN " + keres,
                    pixiAjanlatkeresekAdataiObject,
                    1);       
           System.exit(1);
        }

    private void folyamatbanNyit() {
        mentesButton.setEnabled(false);
        folyamatbanDialog.setSize(folyamatbanDialog.getPreferredSize());
        folyamatbanDialog.setLocation(getLocationOnScreen().x + (getSize().width - folyamatbanDialog.getSize().width) / 2, getLocationOnScreen().y + (getSize().height - folyamatbanDialog.getSize().height) / 2);
        folyamatbanDialog.setVisible(true);
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
        
        try
        {
            originalInvoiceNumber = select[0][0].toString();
        }
        catch(ArrayIndexOutOfBoundsException e)
        {
            originalInvoiceNumber = "";
            //originalInvoiceNumber = findOriginalInvoiceNumber(select[0][1].toString());
        }
        
        return originalInvoiceNumber;
    }
    
    private String findLastModificationReference(String invoiceNumber)
    {
        String lastModificationReference = "";
        
        Query query = new Query.QueryBuilder()
                .select("szamla_sorszam")
                .from("szamlazo_szamla")
                .where("helyesbitett LIKE '" + invoiceNumber + "'")
                .build();
        Object [][] select = App.db.select(query.getQuery());
        
        
        
        return lastModificationReference;
    }

    class SzamlaThread extends Thread {

        public SzamlaThread() {
            start();
        }

        @Override
        public void run()
        {
            mentesButton.setEnabled(false);
            String szamlaSorszamString = sorszam;
            Calendar now = Calendar.getInstance();
            Label label = (Label) szallitoComboBox.getSelectedItem();
            Query query = new Query.QueryBuilder()
                    .select("db, ev, elotag")
                    .from("szamlazo_szamla_sorszam")
                    .where("id = (SELECT sorszamid from szamlazo_ceg_adatok WHERE id = " + label.getId() + ")")
                    .order("")
                    .build();
            Object[][] s = App.db.select(query.getQuery());

            sorszam = now.get(Calendar.YEAR) + "/" + (Integer.parseInt(String.valueOf(s[0][0])) + 1);
            if (!String.valueOf(s[0][2]).isEmpty())
            {
                sorszam = String.valueOf(s[0][2]) + " " + sorszam;
            }

            if (szamlaSorszamString.matches(sorszam))
            {
                szamlaMentes();
                App.db.insert("UPDATE szamlazo_szamla_sorszam SET db = db + 1 WHERE id = (SELECT sorszamid from szamlazo_ceg_adatok WHERE id = " + label.getId() + ")", null, 0);
                if(bezar) {
                    updatePixiProductsAfterSave();
                }
                folyamatbanDialog.setVisible(false);
                osszegzoDialog.setVisible(false);
                doClose(RET_OK);
            }
            else
            {
                if (!deviza)
                {
                    HibaDialog h = new HibaDialog("A számla sorszáma időközben megváltozott. Új sorszám: " + sorszam, "Ok", "");
                }
                else
                {
                    HibaDialog h = new HibaDialog("A számla sorszáma időközben megváltozott. Új sorszám: " + sorszam + "/V", "Ok", "");
                }
            }
            folyamatbanDialog.setVisible(false);
        }
    }
}