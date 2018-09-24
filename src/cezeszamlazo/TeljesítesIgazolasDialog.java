package cezeszamlazo;

import cezeszamlazo.controller.Vevo;
import cezeszamlazo.controller.Szallito;
import cezeszamlazo.controller.Szamla;
import cezeszamlazo.controller.SzamlaTermek;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.print.PageFormat;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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
import cezeszamlazo.database.Query;
import cezeszamlazo.model.TeljesitesIgazolasModel;

public class TeljesítesIgazolasDialog extends javax.swing.JDialog {

    /**
     * A return status code - returned if Cancel button has been pressed
     */
    public static final int RET_CANCEL = 0;
    /**
     * A return status code - returned if OK button has been pressed
     */
    public static final int RET_OK = 1;
    private String sorszam = "", penznem = "", vevoid = "0", helyesbitett = "", helyesbitettTeljesites = "";
    private int termekid = -1;
    private double kozeparfolyam = 1.0, scale = 1.0;
    private boolean deviza = false, b = true, bezar = false, modosit = true, storno = false;
    private List termekek = new LinkedList<SzamlaTermek>();
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private SzamlaTermek aktualis = new SzamlaTermek("", "", "db", "27", "", "0", "0", "27");
    private Szamla szla;
    private FolyamatbanDialog folyamatbanDialog;
    private TeljesitesIgazolasModel teljesitesIgazolasModel;

    /**
     * Creates new form SzamlaDialogOld
     */
    public TeljesítesIgazolasDialog() {
        initComponents();

        // mentés vagy kilépés után bezárja a számlázót!
        bezar = true;

        penznem = "Ft";
        kozeparfolyam = 1.0;
        deviza = false;

        penznemLabel1.setText(penznem);
        penznemLabel2.setText(penznem);
        penznemLabel3.setText(penznem);

        szamlaFejlec.setText("Teljesítés Igazolás");

        szamlaCsoportFrissites();
        szallitoFrissites();
        afaFrissites();

        if (App.args.length > 0) {
            vevoid = App.args[1];
            vevoFrissites();
            SzamlaTermek szamlaTermek;
            TermekDij termekDij;
            Object[][] select;
            for (int i = 2; i < App.args.length; i++) {
                Query query = new Query.QueryBuilder()
                        .select("termek, mee, egysegar, mennyiseg, id ")
                        .from("pixi_ajanlatkeresek_adatai")
                        .where("id = " + App.args[i])
                        .build();
                select = App.db.select(query.getQuery());
                // String nev, String cikkszam, String mee, String afa, String vtszTeszor, String egysegar, String mennyiseg
                szamlaTermek = new SzamlaTermek(String.valueOf(select[0][0]).replace(";", ""), String.valueOf(select[0][4]), String.valueOf(select[0][1]), "27", "", String.valueOf(select[0][2]), String.valueOf(select[0][3]), "27");
                double mennyiseg = Double.parseDouble(String.valueOf(select[0][3]));
                query = new Query.QueryBuilder()
                        .select("nev, szelesseg, magassag, peldany, egysegsuly, termekdij, osszsuly, csk, kt ")
                        .from("pixi_ajanlatkeresek_termekdij")
                        .where("ajanlatkeresid = " + App.args[i])
                        .build();
                select = App.db.select(query.getQuery());
                if (select.length != 0) {
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
                } else {
                    termekDij = null;
                }
                szamlaTermek.setTermekDij(termekDij);
                termekek.add(szamlaTermek);
            }
            szamlaTermekekFrissites();

        }

        init("Teljesítés Igazolás nyomtatása");

        // Close the dialog when Esc is pressed
        String cancelName = "cancel";
        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), cancelName);
        ActionMap actionMap = getRootPane().getActionMap();
        actionMap.put(cancelName, new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                doClose(RET_CANCEL);
            }
        });
    }

    public TeljesítesIgazolasDialog(String penznem, boolean deviza, double kozeparfolyam) {
        initComponents();

        penznemLabel1.setText(penznem);
        penznemLabel2.setText(penznem);
        penznemLabel3.setText(penznem);

        this.penznem = penznem;
        this.deviza = deviza;
        this.kozeparfolyam = kozeparfolyam;

        szamlaFejlec.setText("Teljesítés Igazolás");

        szamlaCsoportFrissites();
        szallitoFrissites();
        afaFrissites();

        init("Teljesítés Igazolás nyomtatása");

        // Close the dialog when Esc is pressed
        String cancelName = "cancel";
        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), cancelName);
        ActionMap actionMap = getRootPane().getActionMap();
        actionMap.put(cancelName, new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                doClose(RET_CANCEL);
            }
        });
    }

    public TeljesítesIgazolasDialog(Szamla szla) {
        initComponents();

        this.szla = szla;
        this.penznem = (szla.getValuta().equalsIgnoreCase("huf") || szla.getValuta().equalsIgnoreCase("Ft") ? "Ft" : szla.getValuta().toUpperCase());

        penznemLabel1.setText(penznem);
        penznemLabel2.setText(penznem);
        penznemLabel3.setText(penznem);

        this.deviza = szla.isDeviza();
        this.kozeparfolyam = szla.getKozeparfolyam();

        szamlaFejlec.setText("Teljesítés Igazolás");
        termekek = szla.getTermekek();

        vevoFrissites();

        szamlaTermekekFrissites();

        szamlaCsoportFrissites();
        szallitoFrissites();
        afaFrissites();

        init("Teljesítés Igazolás nyomtatása");

        // Close the dialog when Esc is pressed
        String cancelName = "cancel";
        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), cancelName);
        ActionMap actionMap = getRootPane().getActionMap();
        actionMap.put(cancelName, new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                doClose(RET_CANCEL);
            }
        });
    }

    public TeljesítesIgazolasDialog(String azon, boolean modosit) {
        initComponents();
        this.modosit = modosit;
        this.storno = true;

        this.szla = new Szamla(azon);
        this.szla.setTipus(2);
        this.penznem = (szla.getValuta().equalsIgnoreCase("huf") || szla.getValuta().equalsIgnoreCase("Ft") ? "Ft" : szla.getValuta().toUpperCase());

        penznemLabel1.setText(penznem);
        penznemLabel2.setText(penznem);
        penznemLabel3.setText(penznem);

        this.deviza = szla.isDeviza();
        this.kozeparfolyam = szla.getKozeparfolyam();

        szamlaFejlec.setText("Teljesítés Igazolás");
        termekek = szla.getTermekek();

        for (Object o : termekek) {
            SzamlaTermek szt = (SzamlaTermek) o;
            szt.szorozMennyiseg(-1.0);
            if (szt.getTermekDij() != null) {
                szt.getTermekDij().setSzorzo(-1.0);
            }
        }

        vevoFrissites();

        szamlaTermekekFrissites();

        szamlaCsoportFrissites();
        szallitoFrissites();
        afaFrissites();

        init("Teljesítés Igazolás nyomtatása");

        // Close the dialog when Esc is pressed
        String cancelName = "cancel";
        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), cancelName);
        ActionMap actionMap = getRootPane().getActionMap();
        actionMap.put(cancelName, new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                doClose(RET_CANCEL);
            }
        });
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
        szallitoUtca = new javax.swing.JTextField();
        szallitoVaros = new javax.swing.JTextField();
        jLabel106 = new javax.swing.JLabel();
        szallitoNev = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        szallitoEuAdoszam = new javax.swing.JTextField();
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
        vevoOrszagText = new javax.swing.JTextField();
        vevoUtcaText = new javax.swing.JTextField();
        vevoIrszText = new javax.swing.JTextField();
        vevoVarosText = new javax.swing.JTextField();
        vevoNevText = new javax.swing.JTextField();
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
        szamlaTermekPopupMenu = new javax.swing.JPopupMenu();
        modositMenuItem = new javax.swing.JMenuItem();
        vtszMenuItem = new javax.swing.JMenuItem();
        torolMenuItem = new javax.swing.JMenuItem();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        szallitoAdatokButton = new javax.swing.JButton();
        szallitoComboBox = new javax.swing.JComboBox();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        esedekessegTextField = new javax.swing.JTextField();
        teljesitesTextField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        esedekessegNaptar = new javax.swing.JLabel();
        teljesitesNaptar = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        szamlaCsoport = new javax.swing.JComboBox();
        fizetesiMod = new javax.swing.JComboBox();
        vevo = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        kelt = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        teljesitesIgazolasKelteTextField = new javax.swing.JTextField();
        teljesitesIgazolasKelteNaptar = new javax.swing.JLabel();
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
        afa = new javax.swing.JComboBox();
        jLabel20 = new javax.swing.JLabel();
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
        printAndSaveButton = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        TIGSorszamTextField = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();

        szallitoDialog.setMinimumSize(new java.awt.Dimension(400, 201));
        szallitoDialog.setModal(true);
        szallitoDialog.setName("szallitoDialog"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(cezeszamlazo.App.class).getContext().getResourceMap(TeljesítesIgazolasDialog.class);
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

        szallitoUtca.setName("szallitoUtca"); // NOI18N

        szallitoVaros.setName("szallitoVaros"); // NOI18N

        jLabel106.setText(resourceMap.getString("jLabel106.text")); // NOI18N
        jLabel106.setName("jLabel106"); // NOI18N

        szallitoNev.setName("szallitoNev"); // NOI18N

        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        szallitoEuAdoszam.setText(resourceMap.getString("szallitoEuAdoszam.text")); // NOI18N
        szallitoEuAdoszam.setName("szallitoEuAdoszam"); // NOI18N

        javax.swing.GroupLayout szallitoDialogLayout = new javax.swing.GroupLayout(szallitoDialog.getContentPane());
        szallitoDialog.getContentPane().setLayout(szallitoDialogLayout);
        szallitoDialogLayout.setHorizontalGroup(
            szallitoDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(szallitoDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(szallitoDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(szallitoDialogLayout.createSequentialGroup()
                        .addGroup(szallitoDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel105, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel111))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(szallitoDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(szallitoBankszamlaszam, javax.swing.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE)
                            .addComponent(szallitoMegjegyzes, javax.swing.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE)))
                    .addGroup(szallitoDialogLayout.createSequentialGroup()
                        .addGroup(szallitoDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(szallitoDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel104, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel103, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel102, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel101, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(25, 25, 25)
                        .addGroup(szallitoDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(szallitoUtca, javax.swing.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE)
                            .addGroup(szallitoDialogLayout.createSequentialGroup()
                                .addComponent(szallitoIrsz, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel106)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(szallitoVaros, javax.swing.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE))
                            .addComponent(szallitoNev, javax.swing.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE)
                            .addComponent(szallitoAdoszam, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE)
                            .addComponent(szallitoEuAdoszam, javax.swing.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE))))
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
                    .addComponent(szallitoUtca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
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
                            .addComponent(vevoAdoszamText, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
                            .addComponent(vevoEuAdoszamText, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
                            .addComponent(vevoBankszamlaszamText, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE))
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

        vevoOrszagText.setName("vevoOrszagText"); // NOI18N

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

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel56)
                    .addComponent(jLabel55)
                    .addComponent(jLabel57)
                    .addComponent(jLabel58)
                    .addComponent(jLabel53)
                    .addComponent(jLabel52))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(vevoIrszText, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel54)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(vevoVarosText, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE))
                    .addComponent(vevoEmailText, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE)
                    .addComponent(vevoTelefonText, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE)
                    .addComponent(vevoUtcaText, javax.swing.GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE)
                    .addComponent(vevoOrszagText, javax.swing.GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE)
                    .addComponent(vevoNevText, javax.swing.GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE))
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
                    .addComponent(jLabel56)
                    .addComponent(vevoOrszagText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel57)
                    .addComponent(vevoTelefonText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel58)
                    .addComponent(vevoEmailText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout vevoDialogLayout = new javax.swing.GroupLayout(vevoDialog.getContentPane());
        vevoDialog.getContentPane().setLayout(vevoDialogLayout);
        vevoDialogLayout.setHorizontalGroup(
            vevoDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
            .addGroup(vevoDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(vevoDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, vevoDialogLayout.createSequentialGroup()
                        .addComponent(vevoModositasMentes)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(vevoMentesUjButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 103, Short.MAX_VALUE)
                        .addComponent(vevoOkButton)))
                .addContainerGap())
        );
        vevoDialogLayout.setVerticalGroup(
            vevoDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 408, Short.MAX_VALUE)
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
                .addContainerGap(19, Short.MAX_VALUE))
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
                    .addComponent(jButton5))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        szamlaTermekPopupMenu.setName("szamlaTermekPopupMenu"); // NOI18N

        modositMenuItem.setText(resourceMap.getString("modositMenuItem.text")); // NOI18N
        modositMenuItem.setName("modositMenuItem"); // NOI18N
        modositMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modositMenuItemActionPerformed(evt);
            }
        });
        szamlaTermekPopupMenu.add(modositMenuItem);

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

        setName("Form"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jTabbedPane1.setEnabled(false);
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
        szallitoComboBox.setEnabled(false);
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

        esedekessegTextField.setEditable(false);
        esedekessegTextField.setBackground(resourceMap.getColor("esedekessegTextField.background")); // NOI18N
        esedekessegTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        esedekessegTextField.setDisabledTextColor(resourceMap.getColor("esedekessegTextField.disabledTextColor")); // NOI18N
        esedekessegTextField.setEnabled(false);
        esedekessegTextField.setName("esedekessegTextField"); // NOI18N

        teljesitesTextField.setEditable(false);
        teljesitesTextField.setBackground(resourceMap.getColor("teljesitesTextField.background")); // NOI18N
        teljesitesTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        teljesitesTextField.setDisabledTextColor(resourceMap.getColor("teljesitesTextField.disabledTextColor")); // NOI18N
        teljesitesTextField.setEnabled(false);
        teljesitesTextField.setName("teljesitesTextField"); // NOI18N

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

        esedekessegNaptar.setIcon(resourceMap.getIcon("esedekessegNaptar.icon")); // NOI18N
        esedekessegNaptar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        esedekessegNaptar.setEnabled(false);
        esedekessegNaptar.setName("esedekessegNaptar"); // NOI18N
        esedekessegNaptar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                esedekessegNaptarMouseClicked(evt);
            }
        });

        teljesitesNaptar.setIcon(resourceMap.getIcon("teljesitesNaptar.icon")); // NOI18N
        teljesitesNaptar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        teljesitesNaptar.setEnabled(false);
        teljesitesNaptar.setName("teljesitesNaptar"); // NOI18N
        teljesitesNaptar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                teljesitesNaptarMouseClicked(evt);
            }
        });

        jButton2.setText(resourceMap.getString("jButton2.text")); // NOI18N
        jButton2.setEnabled(false);
        jButton2.setMargin(new java.awt.Insets(2, 4, 2, 4));
        jButton2.setName("jButton2"); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        szamlaCsoport.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "csoport" }));
        szamlaCsoport.setEnabled(false);
        szamlaCsoport.setName("szamlaCsoport"); // NOI18N

        fizetesiMod.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Készpénz", "Átutalás", "Utánvét" }));
        fizetesiMod.setEnabled(false);
        fizetesiMod.setName("fizetesiMod"); // NOI18N
        fizetesiMod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fizetesiModActionPerformed(evt);
            }
        });

        vevo.setText(resourceMap.getString("vevo.text")); // NOI18N
        vevo.setEnabled(false);
        vevo.setName("vevo"); // NOI18N
        vevo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                vevoKeyReleased(evt);
            }
        });

        jButton3.setText(resourceMap.getString("jButton3.text")); // NOI18N
        jButton3.setEnabled(false);
        jButton3.setName("jButton3"); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        kelt.setFont(resourceMap.getFont("kelt.font")); // NOI18N
        kelt.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        kelt.setText(resourceMap.getString("kelt.text")); // NOI18N
        kelt.setName("kelt"); // NOI18N

        jLabel17.setText(resourceMap.getString("jLabel17.text")); // NOI18N
        jLabel17.setName("jLabel17"); // NOI18N

        teljesitesIgazolasKelteTextField.setEditable(false);
        teljesitesIgazolasKelteTextField.setBackground(resourceMap.getColor("teljesitesIgazolasKelteTextField.background")); // NOI18N
        teljesitesIgazolasKelteTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        teljesitesIgazolasKelteTextField.setDisabledTextColor(resourceMap.getColor("teljesitesIgazolasKelteTextField.disabledTextColor")); // NOI18N
        teljesitesIgazolasKelteTextField.setEnabled(false);
        teljesitesIgazolasKelteTextField.setName("teljesitesIgazolasKelteTextField"); // NOI18N

        teljesitesIgazolasKelteNaptar.setIcon(resourceMap.getIcon("teljesitesNaptar.icon")); // NOI18N
        teljesitesIgazolasKelteNaptar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        teljesitesIgazolasKelteNaptar.setName("teljesitesIgazolasKelteNaptar"); // NOI18N
        teljesitesIgazolasKelteNaptar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                teljesitesIgazolasKelteNaptarMouseClicked(evt);
            }
        });

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
                    .addComponent(vevo, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(teljesitesIgazolasKelteTextField)
                    .addComponent(esedekessegTextField)
                    .addComponent(kelt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(teljesitesTextField))
                .addGap(6, 6, 6)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(esedekessegNaptar)
                        .addComponent(teljesitesNaptar))
                    .addComponent(teljesitesIgazolasKelteNaptar, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(vevo, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3)
                    .addComponent(jButton2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                        .addComponent(szamlaCsoport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                    .addGap(26, 26, 26)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(esedekessegNaptar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(esedekessegTextField)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(teljesitesTextField)
                        .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(teljesitesNaptar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(teljesitesIgazolasKelteNaptar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(teljesitesIgazolasKelteTextField)))
                    .addContainerGap()))
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
        vtszTeszor.setEnabled(false);
        vtszTeszor.setName("vtszTeszor"); // NOI18N

        egysegar.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        egysegar.setText(resourceMap.getString("egysegar.text")); // NOI18N
        egysegar.setEnabled(false);
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
        mennyiseg.setEnabled(false);
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
        cikkszam.setEnabled(false);
        cikkszam.setName("cikkszam"); // NOI18N

        termek.setText(resourceMap.getString("termek.text")); // NOI18N
        termek.setEnabled(false);
        termek.setName("termek"); // NOI18N

        jLabel16.setText(resourceMap.getString("jLabel16.text")); // NOI18N
        jLabel16.setName("jLabel16"); // NOI18N

        penznemLabel2.setText(resourceMap.getString("penznemLabel2.text")); // NOI18N
        penznemLabel2.setName("penznemLabel2"); // NOI18N

        netto.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        netto.setText(resourceMap.getString("netto.text")); // NOI18N
        netto.setEnabled(false);
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
        brutto.setEnabled(false);
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

        afa.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "afa" }));
        afa.setEnabled(false);
        afa.setName("afa"); // NOI18N
        afa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                afaActionPerformed(evt);
            }
        });

        jLabel20.setText(resourceMap.getString("jLabel20.text")); // NOI18N
        jLabel20.setName("jLabel20"); // NOI18N

        engedmenyFelar.setEditable(false);
        engedmenyFelar.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        engedmenyFelar.setText(resourceMap.getString("engedmenyFelar.text")); // NOI18N
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
        hozzaadModosit.setEnabled(false);
        hozzaadModosit.setName("hozzaadModosit"); // NOI18N
        hozzaadModosit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hozzaadModositActionPerformed(evt);
            }
        });

        kiurit.setText(resourceMap.getString("kiurit.text")); // NOI18N
        kiurit.setEnabled(false);
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
        vtszTeszorTallozas.setEnabled(false);
        vtszTeszorTallozas.setMargin(new java.awt.Insets(2, 4, 2, 4));
        vtszTeszorTallozas.setName("vtszTeszorTallozas"); // NOI18N
        vtszTeszorTallozas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vtszTeszorTallozasActionPerformed(evt);
            }
        });

        mee.setText(resourceMap.getString("mee.text")); // NOI18N
        mee.setEnabled(false);
        mee.setName("mee"); // NOI18N

        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel31.setIcon(resourceMap.getIcon("jLabel31.icon")); // NOI18N
        jLabel31.setText(resourceMap.getString("jLabel31.text")); // NOI18N
        jLabel31.setBorder(javax.swing.BorderFactory.createLineBorder(resourceMap.getColor("jLabel31.border.lineColor"))); // NOI18N
        jLabel31.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel31.setEnabled(false);
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
        jLabel32.setEnabled(false);
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
        jLabel33.setEnabled(false);
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
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(afa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel20))
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(engedmenyFelar, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(brutto)
                                .addComponent(netto, javax.swing.GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(penznemLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(penznemLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(kiurit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(hozzaadModosit)))
                .addContainerGap())
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
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(afa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel20))))
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
                .addContainerGap())
        );

        jScrollPane3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(resourceMap.getColor("jScrollPane3.border.border.lineColor")), resourceMap.getString("jScrollPane3.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, resourceMap.getFont("jScrollPane3.border.titleFont"))); // NOI18N
        jScrollPane3.setEnabled(false);
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

        termekdijAtvallalas.setText(resourceMap.getString("termekdijAtvallalas.text")); // NOI18N
        termekdijAtvallalas.setEnabled(false);
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
                    .addComponent(termekdijAtvallalas)
                    .addComponent(jScrollPane3)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 626, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(szallitoAdatokButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(szallitoComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(szallitoAdatokButton)
                    .addComponent(szallitoComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(termekdijAtvallalas)
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 596, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 378, Short.MAX_VALUE)
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
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 604, Short.MAX_VALUE)
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

        printAndSaveButton.setBorder(javax.swing.BorderFactory.createLineBorder(resourceMap.getColor("printAndSaveButton.border.lineColor"))); // NOI18N
        printAndSaveButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        printAndSaveButton.setName("printAndSaveButton"); // NOI18N
        printAndSaveButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                printAndSaveButtonMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                printAndSaveButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                printAndSaveButtonMouseExited(evt);
            }
        });

        jLabel18.setFont(resourceMap.getFont("jLabel18.font")); // NOI18N
        jLabel18.setIcon(resourceMap.getIcon("jLabel18.icon")); // NOI18N
        jLabel18.setText(resourceMap.getString("jLabel18.text")); // NOI18N
        jLabel18.setName("jLabel18"); // NOI18N

        javax.swing.GroupLayout printAndSaveButtonLayout = new javax.swing.GroupLayout(printAndSaveButton);
        printAndSaveButton.setLayout(printAndSaveButtonLayout);
        printAndSaveButtonLayout.setHorizontalGroup(
            printAndSaveButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(printAndSaveButtonLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel18)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        printAndSaveButtonLayout.setVerticalGroup(
            printAndSaveButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

        TIGSorszamTextField.setText(resourceMap.getString("TIGSorszamTextField.text")); // NOI18N
        TIGSorszamTextField.setName("TIGSorszamTextField"); // NOI18N
        TIGSorszamTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TIGSorszamTextFieldActionPerformed(evt);
            }
        });

        jButton4.setText(resourceMap.getString("jButton4.text")); // NOI18N
        jButton4.setName("jButton4"); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

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
                        .addComponent(szamlaSorszam, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(TIGSorszamTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(printAndSaveButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(szamlaFejlec)
                    .addComponent(szamlaSorszam)
                    .addComponent(TIGSorszamTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(printAndSaveButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
            Object[][] select = App.db.select(query.getQuery());
            if (select.length == 1) {
                szallitoVaros.setText(String.valueOf(select[0][0]));
            }
        }
    }//GEN-LAST:event_szallitoIrszKeyReleased

    private void vevoIrszTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_vevoIrszTextKeyReleased
        if (vevoIrszText.getText().length() == 4) {
            Query query = new Query.QueryBuilder()
                    .select("id, CONCAT(varos, IF(v = 1, '-', ''), varosresz)")
                    .from("varosok")
                    .where("irsz = '" + vevoIrszText.getText() + "'")
                    .build();
            Object[][] object = App.db.select(query.getQuery());
            if (object.length != 0) {
                vevoVarosText.setText(String.valueOf(object[0][1]));
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
        o[4] = vevoOrszagText.getText();
        o[5] = vevoTelefonText.getText();
        o[6] = vevoEmailText.getText();
        o[7] = vevoFizetesiModComboBox.getSelectedIndex();
        o[8] = (vevoFizetesiModKotelezoCheckBox.isSelected() ? 1 : 0);
        o[9] = vevoEsedekessegText.getText();
        o[10] = vevoAdoszamText.getText();
        o[11] = vevoEuAdoszamText.getText();
        o[12] = vevoBankszamlaszamText.getText();
        o[13] = (vevoSzamlanMegjelenikCheckBox.isSelected() ? 1 : 0);
        App.db.insert("INSERT INTO pixi_ugyfel (nev, irsz, varos, utca, orszag, telefon, email, fizetesi_mod, fizetesi_mod_kotelezo, esedekesseg, adoszam, eu_adoszam, bankszamlaszam, szamlan_megjelenik) VALUES " + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", o, 14);
        Query query = new Query.QueryBuilder()
                .select("id")
                .from("pixi_ugyfel")
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
            o[4] = vevoOrszagText.getText();
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
            App.db.insert("UPDATE pixi_ugyfel (nev, irsz, varos, utca, orszag, telefon, email, fizetesi_mod, fizetesi_mod_kotelezo, esedekesseg, adoszam, eu_adoszam, bankszamlaszam, szamlan_megjelenik) WHERE id = ?", o, 15);
        }
        vevoDialog.setVisible(false);
    }//GEN-LAST:event_vevoModositasMentesActionPerformed

    private void vevoNevTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_vevoNevTextKeyReleased
        vevo.setText(vevoNevText.getText());
    }//GEN-LAST:event_vevoNevTextKeyReleased

    private void vevoEsedekessegTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_vevoEsedekessegTextKeyReleased
        String e = csakszam(vevoEsedekessegText.getText(), 0, false);
        vevoEsedekessegText.setText(e);
    }//GEN-LAST:event_vevoEsedekessegTextKeyReleased

    private void vevoFizetesiModKotelezoCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vevoFizetesiModKotelezoCheckBoxActionPerformed
        fizetesiMod.setEnabled(!vevoFizetesiModKotelezoCheckBox.isSelected());
    }//GEN-LAST:event_vevoFizetesiModKotelezoCheckBoxActionPerformed

    private void szallitoComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_szallitoComboBoxActionPerformed
//        if (b) {
//            betoltSzallito();
//            sorszamEllenorzes();
//            Label labelObject = (Label) szallito.getSelectedItem();
//            Object[][] select = App.db.select("SELECT db, ev, elotag FROM szamlazo_szamla_sorszam WHERE id = (SELECT sorszamid from szamlazo_ceg_adatok WHERE id = " + labelObject.getId() + ")", 3);
//            String ujSorszam = String.valueOf(select[0][1]) + "/" + (Integer.parseInt(String.valueOf(select[0][0])) + 1);
//            if (!String.valueOf(select[0][2]).isEmpty()) {
//                ujSorszam = String.valueOf(select[0][2]) + " " + ujSorszam;
//            }
//            szamlaSorszam.setText(ujSorszam + (deviza ? "/V" : ""));
//            if (!ujSorszam.matches(sorszam) && !sorszam.isEmpty()) {
//                HibaDialog hd = new HibaDialog("A sorszámozás megváltozott!\nAz új számla sorszám: " + ujSorszam, "Ok", "");
//            }
//            sorszam = ujSorszam;
//        }
    }//GEN-LAST:event_szallitoComboBoxActionPerformed

    private void szallitoAdatokButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_szallitoAdatokButtonActionPerformed
//        nyit(szallitoDialog, "Szállító adatok");
    }//GEN-LAST:event_szallitoAdatokButtonActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
//        nyit(vevoDialog, "Vevő adatok");
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
//        UgyfelListaDialog u = new UgyfelListaDialog();
//        if (u.getReturnStatus() == 1) {
//            vevoid = u.getId();
//            vevoFrissites();
//        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void vevoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_vevoKeyReleased
        vevoNevText.setText(vevo.getText());
    }//GEN-LAST:event_vevoKeyReleased

    private void esedekessegNaptarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_esedekessegNaptarMouseClicked
//        CalendarDialog cd = new CalendarDialog(null, esedekesseg);
    }//GEN-LAST:event_esedekessegNaptarMouseClicked

    private void teljesitesNaptarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_teljesitesNaptarMouseClicked
//        CalendarDialog cd = new CalendarDialog(null, teljesites);
//        esedekessegFrissites();
    }//GEN-LAST:event_teljesitesNaptarMouseClicked

    private void vtszTeszorTallozasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vtszTeszorTallozasActionPerformed
//        VtszTeszorListaDialog vevoObject = new VtszTeszorListaDialog();
//        if (vevoObject.getReturnStatus() == 1) {
//            vtszTeszor.setText(vevoObject.getVtszTeszor());
//            Label labelObject;
//            int j = 0;
//            for (int i = 0; i < afa.getItemCount(); i++) {
//                labelObject = (Label) afa.getItemAt(i);
//                if (labelObject.getName().equalsIgnoreCase(vevoObject.getAfa())) {
//                    j = i;
//                    break;
//                }
//            }
//            afa.setSelectedIndex(j);
//        }
    }//GEN-LAST:event_vtszTeszorTallozasActionPerformed

    private void mennyisegKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_mennyisegKeyReleased
//        csakszam(evt, true);
//        if (!mennyiseg.getText().isEmpty()) {
//            szamolMennyisegAlapjan();
//        }
    }//GEN-LAST:event_mennyisegKeyReleased

    private void mennyisegFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_mennyisegFocusLost
        JTextField field = (JTextField) evt.getSource();
        if (field.getText().isEmpty()) {
            field.setText("0");
            szamolMennyisegAlapjan();
        }
    }//GEN-LAST:event_mennyisegFocusLost

    private void egysegarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_egysegarKeyReleased
//        csakszam(evt, true);
//        if (!egysegar.getText().isEmpty()) {
//            szamolMennyisegAlapjan();
//        }
    }//GEN-LAST:event_egysegarKeyReleased

    private void egysegarFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_egysegarFocusLost
//        JTextField field = (JTextField) evt.getSource();
//        if (field.getText().isEmpty()) {
//            field.setText("0");
//            szamolMennyisegAlapjan();
//        }
    }//GEN-LAST:event_egysegarFocusLost

    private void nettoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nettoKeyReleased
//        csakszam(evt, true);
//        if (!netto.getText().isEmpty()) {
//            szamolNettoAlapjan();
//        }
    }//GEN-LAST:event_nettoKeyReleased

    private void nettoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nettoFocusLost
//        JTextField field = (JTextField) evt.getSource();
//        if (field.getText().isEmpty()) {
//            field.setText("0");
//            szamolNettoAlapjan();
//        }
    }//GEN-LAST:event_nettoFocusLost

    private void bruttoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bruttoKeyReleased
//        csakszam(evt, deviza);
//        if (!brutto.getText().isEmpty()) {
//            szamolBruttoAlapjan();
//        }
    }//GEN-LAST:event_bruttoKeyReleased

    private void bruttoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_bruttoFocusLost
//        JTextField field = (JTextField) evt.getSource();
//        if (field.getText().isEmpty()) {
//            field.setText("0");
//            szamolBruttoAlapjan();
//        }
    }//GEN-LAST:event_bruttoFocusLost

    private void afaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_afaActionPerformed
//        if (b) {
//            szamolMennyisegAlapjan();
//        }
    }//GEN-LAST:event_afaActionPerformed

    private void kiuritActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kiuritActionPerformed
//        kiurit();
    }//GEN-LAST:event_kiuritActionPerformed

    private void hozzaadModositActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hozzaadModositActionPerformed
//        if (termek.getText().isEmpty()) {
//            HibaDialog hd = new HibaDialog("Nincs megadva termék név!", "Ok", "");
//        } else if (Double.parseDouble(mennyiseg.getText()) == 0) {
//            HibaDialog hd = new HibaDialog("Nulla a mennyiség!", "Ok", "");
//        } else if (vtszTeszor.getText().isEmpty()) {
//            HibaDialog hd = new HibaDialog("Nincs megadva VTSZ/TESZOR szám!", "Ok", "");
//        } else {
//            Label labelObject = (Label) afa.getSelectedItem();
//            SzamlaTermek szt = new SzamlaTermek(termek.getText(), cikkszam.getText(), mee.getText(), labelObject.getId(), vtszTeszor.getText(), egysegar.getText(), mennyiseg.getText());
//            szt.setTermekDij(aktualis.getTermekDij());
//            if (termekid == -1) {
//                termekek.add(szt);
//            } else {
//                termekek.set(termekid, szt);
//                termekid = -1;
//                hozzaadModosit.setText("Hozzáad");
//            }
//            szamlaTermekekFrissites();
//        }
    }//GEN-LAST:event_hozzaadModositActionPerformed

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
//        if (evt.isPopupTrigger() && modosit) {
//            JTable source = (JTable) evt.getSource();
//            int row = source.rowAtPoint(evt.getPoint());
//            int column = source.columnAtPoint(evt.getPoint());
//
//            if (!source.isRowSelected(row)) {
//                source.changeSelection(row, column, false, false);
//            }
//
//            int[] rows = szamlaTermekekTable.getSelectedRows();
//
//            modositMenuItem.setVisible(rows.length == 1);
//
//            szamlaTermekPopupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
//        }
    }//GEN-LAST:event_szamlaTermekekTableMouseReleased

    private void torolMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_torolMenuItemActionPerformed
//        int[] rows = szamlaTermekekTable.getSelectedRows();
//        for (int i = rows.length - 1; i >= 0; i--) {
//            termekek.remove(rows[i]);
//        }
//        szamlaTermekekFrissites();
    }//GEN-LAST:event_torolMenuItemActionPerformed

    private void modositMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modositMenuItemActionPerformed
//        int[] rows = szamlaTermekekTable.getSelectedRows();
//        termekid = rows[0];
//        aktualis = (SzamlaTermek) termekek.get(termekid);
//        termek.setText(aktualis.getNev());
//        cikkszam.setText(aktualis.getCikkszam());
//        mennyiseg.setText(aktualis.getMennyiseg() + "");
//        mee.setText(aktualis.getMee());
//        egysegar.setText(aktualis.getEgysegar() + "");
//        vtszTeszor.setText(aktualis.getVtszTeszor());
//        Label labelObject;
//        int j = 0;
//        for (int i = 0; i < afa.getItemCount(); i++) {
//            labelObject = (Label) afa.getItemAt(i);
//            if (Double.parseDouble(labelObject.getName()) == aktualis.getAfa()) {
//                j = i;
//                break;
//            }
//        }
//        afa.setSelectedIndex(j);
//        hozzaadModosit.setText("Módosít");
    }//GEN-LAST:event_modositMenuItemActionPerformed

    private void nyomtatasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nyomtatasActionPerformed
//        Toolkit toolkit = Toolkit.getDefaultToolkit();
//        if (nyomtatas.isSelected()) {
//            //java.net.URL url = ClassLoader.getSystemResource("cezeszamlazo/resources/print_20.png");
//            //java.awt.Image img = toolkit.createImage(url);
//            mentesButton.setText("Mentés és nyomtatás");
//            //mentesLabel.setIcon((Icon) img);
//        } else {
//            //java.net.URL url = ClassLoader.getSystemResource("cezeszamlazo/resources/checkmark_20.png");
//            //java.awt.Image img = toolkit.createImage(url);
//            mentesButton.setText("Mentés");
//            //mentesLabel.setIcon((Icon) img);
//        }
//        nyomtatasPeldany.setEnabled(nyomtatas.isSelected());
//        nyomtatasPeldany.setEditable(nyomtatas.isSelected());
    }//GEN-LAST:event_nyomtatasActionPerformed

    private void fizetesiModActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fizetesiModActionPerformed
        vevoFizetesiModComboBox.setSelectedIndex(fizetesiMod.getSelectedIndex());
    }//GEN-LAST:event_fizetesiModActionPerformed

    private void printAndSaveButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_printAndSaveButtonMouseClicked
        boolean v = false;
        int j = 0;
        for (int i = 0; i < termekek.size(); i++) {
            SzamlaTermek szt = (SzamlaTermek) termekek.get(i);
            if (szt.getVtszTeszor().isEmpty()) {
                v = true;
                j = i + 1;
                break;
            }
        }
        if (v) {
            HibaDialog h = new HibaDialog("A(z) " + j + ". termékhez nincs megadva VTSZ/TESZOR szám!", "Ok", "");
        } else if (termekek.isEmpty()) {
            HibaDialog h = new HibaDialog("Nincs termék hozzáadva a számlához!", "Ok", "");
        } else if (vevo.getText().isEmpty()) {
            HibaDialog h = new HibaDialog("Nincs vevő megadva!", "Ok", "");
        } else {
            elonezet(ElonezetDialog.NYOMTATAS);
//            doClose(RET_OK);
//            nyit(osszegzoDialog, "Összegző");
        }
    }//GEN-LAST:event_printAndSaveButtonMouseClicked

    private void jPanel13MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel13MouseClicked
        doClose(RET_CANCEL);
    }//GEN-LAST:event_jPanel13MouseClicked

    private void printAndSaveButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_printAndSaveButtonMouseEntered
        printAndSaveButton.setBackground(Color.decode("#abd043"));
    }//GEN-LAST:event_printAndSaveButtonMouseEntered

    private void printAndSaveButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_printAndSaveButtonMouseExited
        printAndSaveButton.setBackground(Color.decode("#f0f0f0"));
    }//GEN-LAST:event_printAndSaveButtonMouseExited

    private void jPanel13MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel13MouseEntered
        jPanel13.setBackground(Color.decode("#d24343"));
    }//GEN-LAST:event_jPanel13MouseEntered

    private void jPanel13MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel13MouseExited
        jPanel13.setBackground(Color.decode("#f0f0f0"));
    }//GEN-LAST:event_jPanel13MouseExited

    private void jLabel31MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel31MouseEntered
        jLabel31.setBackground(Color.decode("#abd043"));
    }//GEN-LAST:event_jLabel31MouseEntered

    private void jLabel31MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel31MouseExited
        jLabel31.setBackground(Color.decode("#f0f0f0"));
    }//GEN-LAST:event_jLabel31MouseExited

    private void jLabel31MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel31MouseClicked
        TermekListaDialog t = new TermekListaDialog();
        if (t.getReturnStatus() == 1) {
            termek.setText(t.getTermek());
            mee.setText(t.getMee());
            egysegar.setText(t.getEgysegar());
            cikkszam.setText(t.getCikkszam());
            vtszTeszor.setText(t.getVtszTeszor());
            for (int i = 0; i < afa.getItemCount(); i++) {
                Label l = (Label) afa.getItemAt(i);
                if (l.getId().equalsIgnoreCase(t.getAfa())) {
                    afa.setSelectedIndex(i);
                    break;
                }
            }
        }
    }//GEN-LAST:event_jLabel31MouseClicked

    private void jPanel14MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel14MouseEntered
        jPanel14.setBackground(Color.decode("#abd043"));
    }//GEN-LAST:event_jPanel14MouseEntered

    private void jPanel14MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel14MouseExited
        jPanel14.setBackground(Color.decode("#f0f0f0"));
    }//GEN-LAST:event_jPanel14MouseExited

    private void jPanel14MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel14MouseClicked
//        TermekDijDialog tdd;
//        if (aktualis.getTermekDij() == null) {
//            tdd = new TermekDijDialog();
//        } else {
//            tdd = new TermekDijDialog(aktualis.getTermekDij());
//        }
//        if (tdd.getReturnStatus() == 1) {
//            aktualis.setTermekDij(tdd.getTermekDij());
//        }
    }//GEN-LAST:event_jPanel14MouseClicked

    private void jPanel15MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel15MouseEntered
        jPanel15.setBackground(Color.decode("#d24343"));
    }//GEN-LAST:event_jPanel15MouseEntered

    private void jPanel15MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel15MouseExited
        jPanel15.setBackground(Color.decode("#f0f0f0"));
    }//GEN-LAST:event_jPanel15MouseExited

    private void jPanel15MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel15MouseClicked
        aktualis.setTermekDij(null);
    }//GEN-LAST:event_jPanel15MouseClicked

    private void mentesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mentesButtonActionPerformed
        folyamatbanDialog = new FolyamatbanDialog("A mentés folyamatban. Kis türelmet...");
        SzamlaThread sz = new SzamlaThread();
        folyamatbanDialog.setVisible(true);
    }//GEN-LAST:event_mentesButtonActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        osszegzoDialog.setVisible(false);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void szamlaTermekekTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_szamlaTermekekTableMouseClicked
        if (evt.getClickCount() == 2 && evt.getButton() == MouseEvent.BUTTON1 && modosit) {
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
            for (int i = 0; i < afa.getItemCount(); i++) {
                l = (Label) afa.getItemAt(i);
                if (Double.parseDouble(l.getName()) == aktualis.getAfa()) {
                    j = i;
                    break;
                }
            }
            afa.setSelectedIndex(j);
            hozzaadModosit.setText("Módosít");
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
        if (termekdijAtvallalas.isSelected()) {
            if (!m.contains(s)) {
                m = s + " " + m;
            }
            megjegyzes.setText(m);
        } else {
            m = m.replace(s, "");
            megjegyzes.setText(m);
        }
    }//GEN-LAST:event_termekdijAtvallalasActionPerformed

    private void teljesitesIgazolasKelteNaptarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_teljesitesIgazolasKelteNaptarMouseClicked
        if (teljesitesIgazolasKelteNaptar.isEnabled()) {
            CalendarDialog cd = new CalendarDialog(null, teljesitesIgazolasKelteTextField);
        }

    }//GEN-LAST:event_teljesitesIgazolasKelteNaptarMouseClicked

    private void TIGSorszamTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TIGSorszamTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TIGSorszamTextFieldActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        elonezet(ElonezetDialog.ELONEZET);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void doClose(int retStatus) {
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField TIGSorszamTextField;
    private javax.swing.JComboBox afa;
    private javax.swing.JLabel azaz;
    private javax.swing.JTextField brutto;
    private javax.swing.JTextField cikkszam;
    private javax.swing.JTextField egysegar;
    private javax.swing.JLabel elonezetLabel;
    private javax.swing.JTextField engedmenyFelar;
    private javax.swing.JLabel esedekessegNaptar;
    private javax.swing.JTextField esedekessegTextField;
    private javax.swing.JLabel fizVissz;
    private javax.swing.JComboBox fizetesiMod;
    private javax.swing.JButton hozzaadModosit;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel101;
    private javax.swing.JLabel jLabel102;
    private javax.swing.JLabel jLabel103;
    private javax.swing.JLabel jLabel104;
    private javax.swing.JLabel jLabel105;
    private javax.swing.JLabel jLabel106;
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
    private javax.swing.JLabel jLabel20;
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
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
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
    private javax.swing.JPanel printAndSaveButton;
    private javax.swing.JButton szallitoAdatokButton;
    private javax.swing.JTextField szallitoAdoszam;
    private javax.swing.JTextField szallitoBankszamlaszam;
    private javax.swing.JComboBox szallitoComboBox;
    private javax.swing.JDialog szallitoDialog;
    private javax.swing.JTextField szallitoEuAdoszam;
    private javax.swing.JTextField szallitoIrsz;
    private javax.swing.JTextField szallitoMegjegyzes;
    private javax.swing.JTextField szallitoNev;
    private javax.swing.JTextField szallitoUtca;
    private javax.swing.JTextField szallitoVaros;
    private javax.swing.JComboBox szamlaCsoport;
    private javax.swing.JLabel szamlaFejlec;
    private javax.swing.JLabel szamlaSorszam;
    private javax.swing.JPopupMenu szamlaTermekPopupMenu;
    private javax.swing.JTable szamlaTermekekTable;
    private javax.swing.JLabel teljesitesIgazolasKelteNaptar;
    private javax.swing.JTextField teljesitesIgazolasKelteTextField;
    private javax.swing.JLabel teljesitesNaptar;
    private javax.swing.JTextField teljesitesTextField;
    private javax.swing.JTextField termek;
    private javax.swing.JCheckBox termekdijAtvallalas;
    private javax.swing.JMenuItem torolMenuItem;
    private javax.swing.JTextField vevo;
    private javax.swing.JTextField vevoAdoszamText;
    private javax.swing.JTextField vevoBankszamlaszamText;
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
    private javax.swing.JTextField vevoOrszagText;
    private javax.swing.JCheckBox vevoSzamlanMegjelenikCheckBox;
    private javax.swing.JTextField vevoTelefonText;
    private javax.swing.JTextField vevoUtcaText;
    private javax.swing.JTextField vevoVarosText;
    private javax.swing.JMenuItem vtszMenuItem;
    private javax.swing.JTextField vtszTeszor;
    private javax.swing.JButton vtszTeszorTallozas;
    // End of variables declaration//GEN-END:variables
    private int returnStatus = RET_CANCEL;

    private void init(String title) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;

        java.net.URL url = ClassLoader.getSystemResource("cezeszamlazo/resources/uj-szamla.png");
        java.awt.Image img = toolkit.createImage(url);

        setIconImage(img);

//        szallitoDialog.setSize((int) Math.round(szallitoDialog.getPreferredSize().getWidth()), (int) Math.round(szallitoDialog.getPreferredSize().getHeight()) + 35);
//        szallitoDialog.setIconImage(img);
//        vevoDialog.setSize((int) Math.round(vevoDialog.getPreferredSize().getWidth()), (int) Math.round(vevoDialog.getPreferredSize().getHeight()) + 35);
//        vevoDialog.setIconImage(img);
//        osszegzoDialog.setSize((int) Math.round(osszegzoDialog.getPreferredSize().getWidth()), (int) Math.round(osszegzoDialog.getPreferredSize().getHeight()) + 35);
        kelt.setText(format.format(new Date()));
        esedekessegTextField.setText(format.format(new Date()));
        teljesitesIgazolasModel = new TeljesitesIgazolasModel();

        teljesitesIgazolasModel.setSzamlaSorszam(szla.getSorszam());

        if (teljesitesIgazolasModel.isExistTeljesitesIgazolas()) {
            teljesitesIgazolasModel.setTeljesitestIgazolasBySzamlaSorszam(szla.getSorszam());

            teljesitesTextField.setText(szla.getTeljesites());
            teljesitesIgazolasKelteTextField.setText(teljesitesIgazolasModel.getTeljesitesIgazolasKelte());
            TIGSorszamTextField.setText(teljesitesIgazolasModel.getTIGSorszam());

//            teljesitesTextField.setEditable(false);
//            teljesitesIgazolasKelteTextField.setEditable(false);
//            TIGSorszamTextField.setEditable(false);
//            teljesitesIgazolasKelteNaptar.setEnabled(false);
        } else {
            teljesitesTextField.setText(format.format(new Date()));
            teljesitesIgazolasKelteTextField.setText(format.format(new Date()));
            TIGSorszamTextField.setText("TIG " + szla.getSorszam());
        }

        szamlaSorszam.setText("");

        esedekessegFrissites();

        setLocation(x, y);
        setTitle(title);
        setModal(true);
        setVisible(true);
    }

    private void elonezet(int tipus) {
//        Szallito szallitoObject = new Szallito(
//                szallitoNev.getText(),
//                szallitoIrsz.getText(),
//                szallitoVaros.getText(),
//                szallitoUtca.getText(),
//                szallitoAdoszam.getText(),
//                szallitoBankszamlaszam.getText(),
//                szallitoMegjegyzes.getText());
//        Vevo vevoObject = new Vevo(
//                vevo.getText(),
//                vevoIrszText.getText(),
//                vevoVarosText.getText(),
//                vevoUtcaText.getText(),
//                vevoOrszagText.getText(),
//                vevoTelefonText.getText(),
//                vevoEmailText.getText(),
//                vevoFizetesiModComboBox.getSelectedIndex(),
//                vevoFizetesiModKotelezoCheckBox.isSelected(),
//                vevoEsedekessegText.getText(),
//                vevoAdoszamText.getText(),
//                vevoEuAdoszamText.getText(),
//                vevoBankszamlaszamText.getText(),
//                vevoSzamlanMegjelenikCheckBox.isSelected());
//        Label labelObject = (Label) szamlaCsoport.getSelectedItem();
//        Szamla szamlazoUgyfelObject = new Szamla(
//                szallitoObject,
//                vevoObject,
//                sorszam,
//                kelt.getText(),
//                teljesitesTextField.getText(),
//                esedekessegTextField.getText(),
//                labelObject.getId(),
//                penznem,
//                kozeparfolyam,
//                helyesbitett,
//                helyesbitettTeljesites,
//                deviza,
//                megjegyzes.getText(),
//                lablec.getText(),
//                termekek,
//                termekdijAtvallalas.isSelected());
//        szamlazoUgyfelObject.setTipus(szla != null ? this.szla.getTipus() : 0);
//        szamlazoUgyfelObject.setHelyesbitettTeljesites(szla != null ? this.szla.getTeljesites() : "");
//        szamlazoUgyfelObject.setHelyesbitett(szla != null ? this.szla.getSorszam() : "");

        szla.setTeljesitesIgazolasDatuma(teljesitesIgazolasKelteTextField.getText());

        teljesitesIgazolasModel.setSzamlaSorszam(szla.getSorszam());
        if (TIGSorszamTextField.getText().equals("")) {
            teljesitesIgazolasModel.setTIGSorszam("TIG " + szla.getSorszam());
        } else {
            teljesitesIgazolasModel.setTIGSorszam(TIGSorszamTextField.getText());
        }

        teljesitesIgazolasModel.setTeljesitesIgazolasKelte(teljesitesIgazolasKelteTextField.getText());
        teljesitesIgazolasModel.save();
        szla.setTeljesitesIgazolasModel(teljesitesIgazolasModel);
//         ElonezetTeljesitesDialog elonezet = new ElonezetTeljesitesDialog(szla, Integer.parseInt(nyomtatasPeldany.getText()), tipus);
        ElonezetTeljesitesDialog elonezet = new ElonezetTeljesitesDialog(szla, 1, tipus);
        doClose(RET_OK);
    }

    private void szamlaTermekekFrissites() {
        DefaultTableModel model = (DefaultTableModel) szamlaTermekekTable.getModel();
        for (int i = szamlaTermekekTable.getRowCount() - 1; i >= 0; i--) {
            model.removeRow(i);
        }
        Object[] row = new Object[10];
        double oN = 0.0, oA = 0.0, ossszBrutto = 0.0;
        for (int i = 0; i < termekek.size(); i++) {
            SzamlaTermek szt = (SzamlaTermek) termekek.get(i);
            row[0] = szt.getNev();
            row[1] = szt.getCikkszam();
            row[2] = szt.getMennyiseg();
            row[3] = szt.getMee();
            row[4] = szt.getEgysegar();
            row[5] = szt.getVtszTeszor();
            row[6] = szt.getNetto(deviza);
            row[7] = szt.getAfa();
            oN += szt.getNetto(deviza);
            oA += szt.getAfaErtek(deviza);
            ossszBrutto += szt.getBrutto(deviza);
            if (szt.getTermekDij() != null && !termekdijAtvallalas.isSelected()) {
                oA += szt.getTermekDij().getOsszTermekDijAfaErtek(deviza);
                oN += szt.getTermekDij().getOsszTermekDijNetto(deviza);
                ossszBrutto += szt.getTermekDij().getOsszTermekDijBrutto(deviza);
            }
            row[8] = szt.getAfaErtek(deviza);
            row[9] = szt.getBrutto(deviza);
            model.addRow(row);
        }
        osszNetto.setText(EncodeDecode.numberFormat(String.valueOf(oN), deviza) + " " + penznem);
        osszAfaErtek.setText(EncodeDecode.numberFormat(String.valueOf(oA), deviza) + " " + penznem);
        osszBrutto.setText(EncodeDecode.numberFormat(String.valueOf(ossszBrutto), deviza) + " " + penznem);
        osszegzoNetto.setText(EncodeDecode.numberFormat(String.valueOf(oN), deviza) + " " + penznem);
        osszegzoAfaErtek.setText(EncodeDecode.numberFormat(String.valueOf(oA), deviza) + " " + penznem);
        osszegzoBrutto.setText(EncodeDecode.numberFormat(String.valueOf(ossszBrutto), deviza) + " " + penznem);

        System.out.println("oB: " + ossszBrutto);
        if (ossszBrutto < 0) {
//            ossszBrutto *= 1;
            fizVissz.setText("Visszatérítendő:");
        }
        osszegzoFizVissz.setText(EncodeDecode.numberFormat(String.valueOf(ossszBrutto), deviza) + " " + penznem);
        azaz.setText("azaz " + betuvel(ossszBrutto) + " " + penznem);
        if (!modosit) {
            szamlaTermekekTable.setEnabled(false);
            hozzaadModosit.setEnabled(false);
        }
    }

    private void szamlaCsoportFrissites() {
        Query query = new Query.QueryBuilder()
                .select("id, nev")
                .from("szamlazo_szamla_csoportok")
                .where("1")
                .order("nev")
                .build();
        Object[][] s = App.db.select(query.getQuery());
        szamlaCsoport.removeAllItems();
        for (int i = 0; i < s.length; i++) {
            szamlaCsoport.addItem(new Label(String.valueOf(s[i][0]), String.valueOf(s[i][1])));
        }
    }

    private void szallitoFrissites() {
        String[] id = App.user.getCeg().split(";");
        String keres = "";
        for (int i = 0; i < id.length; i++) {
            keres += "a.id = " + id[i] + " || ";
        }
        keres += "0";
        b = false;
        Query query = new Query.QueryBuilder()
                .select(" a.id, "
                        + "CONCAT(IF(s.elotag != '', CONCAT(s.elotag, ' - '), ''), "
                        + "s.megnevezes, "
                        + "IF(a.deviza = 1, ' (deviza adatokkal)', '')), "
                        + "a.deviza ")
                .from("szamlazo_ceg_adatok a, szamlazo_szamla_sorszam s")
                .where("a.sorszamid = s.id && (" + keres + ")")
                .order(" a.id")
                .build();
        Object[][] object = App.db.select(query.getQuery());
        szallitoComboBox.removeAllItems();
        int i = 0, j = 0;
        for (Object[] obj : object) {
            if (String.valueOf(obj[2]).equalsIgnoreCase("1") && deviza) {
                j = i;
            }
            String s = String.valueOf(obj[1]);
            if (s.length() < 40) {
                szallitoComboBox.addItem(new Label(String.valueOf(obj[0]), s));
            } else {
                szallitoComboBox.addItem(new Label(String.valueOf(obj[0]), s.substring(0, 37) + "..."));
            }
            i++;
        }
        b = true;
        szallitoComboBox.setSelectedIndex(j);
    }

    private void vevoFrissites() {
        if (szla == null) {
            Query query = new Query.QueryBuilder()
                    .select("nev, irsz, varos, utca, orszag, telefon, email, "
                            + "fizetesi_mod, fizetesi_mod_kotelezo, esedekesseg, "
                            + "adoszam, eu_adoszam, szamlan_megjelenik, bankszamla_szam ")
                    .from("pixi_ugyfel")
                    .where("id = " + vevoid)
                    .build();
            Object[][] szamlazoUgyfelObject = App.db.select(query.getQuery());
            vevo.setText(String.valueOf(szamlazoUgyfelObject[0][0]));
            vevoNevText.setText(String.valueOf(szamlazoUgyfelObject[0][0]));
            vevoIrszText.setText(String.valueOf(szamlazoUgyfelObject[0][1]));
            vevoVarosText.setText(String.valueOf(szamlazoUgyfelObject[0][2]));
            vevoUtcaText.setText(String.valueOf(szamlazoUgyfelObject[0][3]));
            vevoOrszagText.setText(String.valueOf(szamlazoUgyfelObject[0][4]));
            vevoTelefonText.setText(String.valueOf(szamlazoUgyfelObject[0][5]));
            vevoEmailText.setText(String.valueOf(szamlazoUgyfelObject[0][6]));
            vevoFizetesiModComboBox.setSelectedIndex(Integer.parseInt(String.valueOf(szamlazoUgyfelObject[0][7])));
            vevoFizetesiModKotelezoCheckBox.setSelected((String.valueOf(szamlazoUgyfelObject[0][8]).matches("1") ? true : false));
            if (Integer.parseInt(String.valueOf(szamlazoUgyfelObject[0][7])) == 1 && String.valueOf(szamlazoUgyfelObject[0][9]).matches("0")) {
                Properties prop = new Properties();
                try {
                    prop.load(new FileInputStream("dat/beallitasok.properties"));
                    vevoEsedekessegText.setText(prop.getProperty("alapEsedekesseg"));
                } catch (IOException ex) {
                    System.out.println("IOException váltódott ki!");
                    ex.printStackTrace();
                    vevoEsedekessegText.setText(String.valueOf(szamlazoUgyfelObject[0][9]));
                }
            } else {
                vevoEsedekessegText.setText(String.valueOf(szamlazoUgyfelObject[0][9]));
            }
            vevoAdoszamText.setText(String.valueOf(szamlazoUgyfelObject[0][10]));
            vevoEuAdoszamText.setText(String.valueOf(szamlazoUgyfelObject[0][11]));
            vevoBankszamlaszamText.setText(String.valueOf(szamlazoUgyfelObject[0][13]));
            vevoSzamlanMegjelenikCheckBox.setSelected((String.valueOf(szamlazoUgyfelObject[0][12]).matches("1") ? true : false));

        } else {
            Query query = new Query.QueryBuilder()
                    .select("nev, irsz, varos, utca, orszag, telefon, email, "
                            + "fizetesi_mod, fizetesi_mod_kotelezo, esedekesseg, "
                            + "adoszam, eu_adoszam, szamlan_megjelenik, bankszamla_szam ")
                    .from("pixi_ugyfel")
                    .where("id = '" + vevoid + "'")
                    .build();
            Object[][] szamlazoUgyfelObject = App.db.select(query.getQuery());
            vevo.setText(szla.getVevo().getNev());
            vevoNevText.setText(szla.getVevo().getNev());
            vevoIrszText.setText(szla.getVevo().getIrsz());
            vevoVarosText.setText(szla.getVevo().getVaros());
            vevoUtcaText.setText(szla.getVevo().getAddress());
            vevoOrszagText.setText(szla.getVevo().getOrszag());
            vevoTelefonText.setText(szla.getVevo().getTelefon());
            vevoEmailText.setText(szla.getVevo().getEmail());
            vevoFizetesiModComboBox.setSelectedIndex(szla.getVevo().getFizetesiMod());
            vevoFizetesiModKotelezoCheckBox.setSelected(szla.getVevo().isFizetesiModKotelezo());
            if (szla.getVevo().getFizetesiMod() == 1 && szla.getVevo().getEsedekesseg().equalsIgnoreCase("0")) {
                Properties prop = new Properties();
                try {
                    prop.load(new FileInputStream("dat/beallitasok.properties"));
                    vevoEsedekessegText.setText(prop.getProperty("alapEsedekesseg"));
                } catch (IOException ex) {
                    System.out.println("IOException váltódott ki!");
                    ex.printStackTrace();
                    vevoEsedekessegText.setText("0");
                }
            } else {
                vevoEsedekessegText.setText(szla.getVevo().getEsedekesseg());
            }
            vevoAdoszamText.setText(szla.getVevo().getAdoszam());
            vevoEuAdoszamText.setText(szla.getVevo().getEuAdoszam());
            vevoBankszamlaszamText.setText(szla.getVevo().getBankszamlaszam());
            vevoSzamlanMegjelenikCheckBox.setSelected(szla.getVevo().isSzamlanMegjelenik());
        }
        esedekessegFrissites();
    }

    private void afaFrissites() {
        b = false;
        Query query = new Query.QueryBuilder()
                .select("afa")
                .from("szamlazo_afa")
                .order("afa DESC")
                .build();
        Object[][] s = App.db.select(query.getQuery());
        afa.removeAllItems();
        for (int i = 0; i < s.length; i++) {
            afa.addItem(new Label(String.valueOf(s[i][0]), String.valueOf(s[i][0])));
        }
        b = true;
    }

    private void esedekessegFrissites() {
        Calendar c = Calendar.getInstance();
        String[] d = kelt.getText().split("-");
        c.set(Integer.parseInt(d[0]), Integer.parseInt(d[1]) - 1, Integer.parseInt(d[2]));
        if (!vevoEsedekessegText.getText().matches("0") && !vevoEsedekessegText.getText().isEmpty()) {
            c.add(Calendar.DATE, Integer.parseInt(vevoEsedekessegText.getText()));
            esedekessegTextField.setText(format.format(c.getTime()));
        } else if (vevoEsedekessegText.getText().matches("0")) {
            c.add(Calendar.DATE, 0);
            esedekessegTextField.setText(format.format(c.getTime()));
        }
    }

    private void sorszamEllenorzes() {
        Calendar c = Calendar.getInstance();
        Label l = (Label) szallitoComboBox.getSelectedItem();
        Query query = new Query.QueryBuilder()
                .select("id, ev ")
                .from("szamlazo_szamla_sorszam")
                .where("id = (SELECT sorszamid from szamlazo_ceg_adatok WHERE id = " + l.getId() + ")")
                .build();
        Object[][] select = App.db.select(query.getQuery());
        if (select.length != 0 && Integer.parseInt(String.valueOf(select[0][1])) < c.get(Calendar.YEAR)) {

            Object[] o = new Object[1];
            o[0] = String.valueOf(c.get(Calendar.YEAR));
            App.db.insert("UPDATE szamlazo_szamla_sorszam SET ev = ?, db = 0 WHERE id = " + String.valueOf(select[0][0]), o, 1);
        } else {
            query = new Query.QueryBuilder()
                    .select("kelt")
                    .from("szamlazo_szamla")
                    .where("sorszamozasid = (SELECT sorszamid from szamlazo_ceg_adatok WHERE id = " + l.getId() + ") ")
                    .order("id DESC LIMIT 1")
                    .build();
            select = App.db.select(query.getQuery());
            if (select.length != 0) {
                String[] datum = String.valueOf(select[0][0]).split("-");
                System.out.println(String.valueOf(select[0][0]));
                int e = Integer.parseInt(datum[0]),
                        h = Integer.parseInt(datum[1]),
                        n = Integer.parseInt(datum[2]);
                if (c.get(Calendar.YEAR) < e
                        || (c.get(Calendar.YEAR) == e && c.get(Calendar.MONTH) + 1 < h)
                        || (c.get(Calendar.YEAR) == e && c.get(Calendar.MONTH) + 1 == h && c.get(Calendar.DATE) < n)) {
                    // valami nem stimmel
                    HibaDialog hd = new HibaDialog("A legutóbbi számla kelte nagyobb, mint a beállított rendszeridő!", "Ok", "");
                }
            }
        }
    }

    private void betoltSzallito() {
        Label l = (Label) szallitoComboBox.getSelectedItem();
        Query query = new Query.QueryBuilder()
                .select("nev, irsz, varos, utca, adoszam, bankszamlaszam, megjegyzes ")
                .from("szamlazo_ceg_adatok")
                .where("id = " + l.getId())
                .build();
        Object[][] select = App.db.select(query.getQuery());
        szallitoNev.setText(String.valueOf(select[0][0]));
        szallitoIrsz.setText(String.valueOf(select[0][1]));
        szallitoVaros.setText(String.valueOf(select[0][2]));
        szallitoUtca.setText(String.valueOf(select[0][3]));
        szallitoAdoszam.setText(String.valueOf(select[0][4]));
        szallitoBankszamlaszam.setText(String.valueOf(select[0][5]));
        szallitoMegjegyzes.setText(String.valueOf(select[0][6]));
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
        afa.setSelectedIndex(0);
        termekid = -1;
        hozzaadModosit.setText("Hozzáad");
        aktualis = new SzamlaTermek("", "", "db", "27", "", "0", "0", "27");
        b = true;
    }

    private void csakszam(KeyEvent evt, boolean tizedes) {
        JTextField field = (JTextField) evt.getSource();
        int pos = field.getCaretPosition();
        field.setText(csakszam(field.getText(), 0, tizedes));
        try {
            field.setCaretPosition(pos);
        } catch (Exception ex) {
        }
    }

    private void szamolMennyisegAlapjan() {
        double m = Double.parseDouble(mennyiseg.getText()),
                e = Double.parseDouble(egysegar.getText()),
                a = 0.0;
        Label l = (Label) afa.getSelectedItem();
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
        Label l = (Label) afa.getSelectedItem();
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
        Label l = (Label) afa.getSelectedItem();
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

    private String csakszam(String text, int size, boolean tizedes) {
        String valid = "+-0123456789";
        if (tizedes) {
            valid += ".";
        }
        text = text.replace(",", ".");
        String result = "";
        for (int i = 0; i < text.length(); i++) {
            if (valid.contains(text.substring(i, i + 1))) {
                result += text.substring(i, i + 1);
            }
        }
        if (size != 0) {
            if (result.length() > size) {
                result = result.substring(0, size);
            }
        }
        return result;
    }

    private void nyit(Object dialog, String title) {
        if (dialog instanceof JDialog) {
            JDialog d = (JDialog) dialog;
            Dimension appSize = getSize();
            Point appLocation = getLocation();
            int x = (appSize.width - d.getWidth()) / 2 + appLocation.x;
            int y = (appSize.height - d.getHeight()) / 2 + appLocation.y;
            d.setLocation(x, y);
            d.setTitle(title);
            d.setVisible(true);
        } else if (dialog instanceof JFrame) {
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

    private String betuvel(double osszeg) {
        String result = "";
        int num = (int) Math.floor(osszeg);
        int tizedes = (int) Math.round((osszeg - Math.floor(osszeg)) * 100);
        int ezres = num % 1000;
        num /= 1000;
        int szazezres = num % 1000;
        num /= 1000;
        int millios = num % 1000;
        num /= 1000;
        int milliardos = num % 1000;
        num /= 1000;
        if (milliardos != 0) {
            result += azaz(milliardos) + "millárd";
            if (millios != 0 || szazezres != 0 || ezres != 0) {
                result += "-";
            }
        }
        if (millios != 0) {
            result += azaz(millios) + "millió";
            if (szazezres != 0 || ezres != 0) {
                result += "-";
            }
        }
        if (szazezres != 0) {
            result += azaz(szazezres) + "ezer";
            if (ezres != 0 && osszeg > 2000) {
                result += "-";
            }
        }
        if (ezres != 0) {
            result += azaz(ezres);
        }
        if (osszeg < 1) {
            result = "nulla";
        }
        result = result.substring(0, 1).toUpperCase() + result.substring(1);
        if (tizedes != 0) {
            result += "\\" + tizedes;
        }
        return result;
    }

    private String azaz(int osszeg) {
        String result = "";
        int sz, t, e;
        String[] egyes = {"", "egy", "kettő", "három", "négy", "öt", "hat", "hét", "nyolc", "kilenc"};
        String[] tizes1 = {"", "tizen", "huszon", "harminc", "negyven", "ötven", "hatvan", "hetven", "nyolcvan", "kilencven"};
        String[] tizes2 = {"", "tíz", "húsz", "harminc", "negyven", "ötven", "hatvan", "hetven", "nyolcvan", "kilencven"};

        sz = osszeg / 100;
        t = osszeg % 100 / 10;
        e = osszeg % 10;

        if (sz < 0) {
            sz = sz * -1;
        }
        if (t < 0) {
            t = t * -1;
        }
        if (e < 0) {
            e = e * -1;
        }

        if (sz != 0) {
            result += egyes[sz] + "száz";
        }
        if (t != 0 && e != 0) {
            result += tizes1[t] + egyes[e];
        } else if (t != 0 && e == 0) {
            result += tizes2[t];
        } else if (t == 0 && e != 0) {
            result += egyes[e];
        }

        return result;
    }

    private void szamlaMentes() {
        int nyomtatva = 0;
        Calendar now = Calendar.getInstance();
        String azon = String.valueOf(now.getTimeInMillis()).substring(0, 10) + String.valueOf((int) Math.round(Math.random() * 89) + 10);

        Object[] o3 = new Object[36];
        o3[0] = fizetesiMod.getSelectedIndex();
        Label l = (Label) szamlaCsoport.getSelectedItem();
        o3[1] = l.getId();
        o3[2] = kelt.getText();
        o3[3] = esedekessegTextField.getText();
        o3[4] = teljesitesTextField.getText();
        if (fizetesiMod.getSelectedIndex() == 0) {
            o3[5] = format.format(new Date());
        } else {
            o3[5] = "0000-00-00";
        }
        o3[6] = csakszam(osszNetto.getText(), 0, deviza); // ?
        o3[7] = csakszam(osszAfaErtek.getText(), 0, deviza); // ?
        if (szla != null && szla.getTipus() == 2) {
            helyesbitett = szla.getSorszam();
            helyesbitettTeljesites = szla.getTeljesites();
        }
        if (helyesbitett.isEmpty()) {
            if (fizetesiMod.getSelectedIndex() == 0) {
                o3[8] = "1";
            } else {
                o3[8] = "0";
            }
        } else {
            o3[8] = "2";
        }
        o3[9] = vevo.getText();
        o3[10] = vevoIrszText.getText();
        o3[11] = vevoVarosText.getText();
        o3[12] = vevoUtcaText.getText();
        o3[13] = vevoOrszagText.getText();
        o3[14] = vevoTelefonText.getText();
        o3[15] = vevoEmailText.getText();
        o3[16] = vevoBankszamlaszamText.getText();
        o3[17] = vevoAdoszamText.getText();
        o3[18] = vevoEuAdoszamText.getText();
        o3[19] = megjegyzes.getText();
        o3[20] = (nyomtatas.isSelected() ? 1 : 0);
        o3[21] = sorszam;
        if (penznem.matches("Ft")) {
            o3[22] = "huf";
        } else if (penznem.matches("EUR")) {
            o3[22] = "eur";
        } else if (penznem.matches("USD")) {
            o3[22] = "usd";
        }
        o3[23] = kozeparfolyam;
        o3[24] = szallitoNev.getText();
        o3[25] = szallitoIrsz.getText();
        o3[26] = szallitoVaros.getText();
        o3[27] = szallitoUtca.getText();
        o3[28] = szallitoAdoszam.getText();
        o3[29] = szallitoBankszamlaszam.getText();
        o3[30] = (vevoSzamlanMegjelenikCheckBox.isSelected() ? 1 : 0);
        o3[31] = helyesbitett;
        o3[32] = lablec.getText();
        o3[33] = azon;
        o3[34] = (deviza ? 1 : 0);
        o3[35] = (termekdijAtvallalas.isSelected() ? 1 : 0);
        double osszeg = 0;
        try {
            Label k = (Label) szallitoComboBox.getSelectedItem();
            App.db.insert("INSERT INTO szamlazo_szamla (fizetesi_mod, szamla_csoport, kelt, esedekesseg, teljesites, kifizetes, netto, afa_ertek, "
                    + "tipus, nev, irsz, varos, utca, orszag, telefon, email, bankszamlaszam, adoszam, eu_adoszam, megjegyzes, nyomtatva, "
                    + "szamla_sorszam, valuta, kozeparfolyam, szallito_nev, szallito_irsz, szallito_varos, szallito_utca, szallito_adoszam, szallito_bankszamlaszam, "
                    + "szamlan_megjelenik, helyesbitett, lablec, sorszamozasid, azon, deviza, atvallal) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, (SELECT sorszamid FROM szamlazo_ceg_adatok WHERE id = " + k.getId() + "), ?, ?, ?)", o3, 36);
            for (Object o : termekek) {
                SzamlaTermek szt = (SzamlaTermek) o;
                Object[] t = new Object[11];
                t[0] = sorszam;
                t[1] = szt.getNev();
                t[2] = szt.getCikkszam();
                t[3] = szt.getMennyiseg();
                t[4] = szt.getMee();
                t[5] = szt.getEgysegar();
                t[6] = szt.getNetto(deviza);
                t[7] = szt.getAfa();
                t[8] = "0";
                t[9] = szt.getVtszTeszor();
                t[10] = azon;
                App.db.insert("INSERT INTO szamlazo_szamla_adatok (szamla_sorszam, termek, termek_kod, mennyiseg, mennyisegi_egyseg, egysegar, netto_ar, afa, engedmeny_felar, vtsz_teszor, azon) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", t, 11);
                // ha !!!készpénzes!!! akkor a pixi rendszer kasszában regisztrálni!
                if (fizetesiMod.getSelectedIndex() == 0) {
                    Object[] oo = new Object[5];
                    oo[0] = szt.getCikkszam();
                    oo[1] = vevo.getText();
                    oo[2] = szt.getBrutto(false);
                    // ha Balázs a létrehozó akkor a "Balázs kasszába" számolja
                    oo[3] = (App.user.getId() == 1 ? 1 : 0);
                    oo[4] = sorszam;
                    App.db.insert("INSERT INTO pixi_kifizetesek (termek_id, nev, datum, osszeg, kassza_tipus, szamla_szam) "
                            + "VALUES (?, ?, NOW(), ?, ?, ?)", oo, oo.length);
                }
                if (kozeparfolyam == 1.0) {
                    osszeg += (int) szt.getBrutto(deviza);
                } else {
                    osszeg += szt.getBrutto(deviza);
                }
                if (szt.getTermekDij() != null) {
                    Query query = new Query.QueryBuilder()
                            .select("id")
                            .from("szamlazo_szamla_adatok")
                            .where("1")
                            .order("id DESC LIMIT 1")
                            .build();
                    Object[][] s = App.db.select(query.getQuery());
                    Object[] td = new Object[10];
                    td[0] = String.valueOf(s[0][0]);
                    td[1] = szt.getTermekDij().getNev();
                    td[2] = szt.getTermekDij().getSzelesseg();
                    td[3] = szt.getTermekDij().getMagassag();
                    td[4] = szt.getTermekDij().getPeldany();
                    td[5] = szt.getTermekDij().getEgysegsuly();
                    td[6] = szt.getTermekDij().getTermekDij();
                    td[7] = szt.getTermekDij().getOsszsuly();
                    td[8] = szt.getTermekDij().getCsk();
                    td[9] = szt.getTermekDij().getKt();
                    if (kozeparfolyam == 1.0) {
                        osszeg += (int) szt.getTermekDij().getOsszTermekDijBrutto(deviza);
                    } else {
                        osszeg += szt.getTermekDij().getOsszTermekDijBrutto(deviza);
                    }
                    App.db.insert("INSERT INTO szamlazo_szamla_termekdij (termekid, nev, szelesseg, magassag, peldany, egysegsuly, termekdij, osszsuly, csk, kt) "
                            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", td, 10);
                }
            }
            if (!helyesbitett.isEmpty()) {
                Object[] o4 = new Object[2];
                o4[0] = "3";
                o4[1] = helyesbitett;
                App.db.insert("UPDATE szamlazo_szamla SET tipus = ? WHERE szamla_sorszam = ?", o4, 2);
            }
            //if (printJob.printDialog()) {
            if (nyomtatas.isSelected()) {
                elonezet(ElonezetDialog.NYOMTATAS);
            }
            //}

            if (fizetesiMod.getSelectedIndex() == 0) {
                Object[] o4 = new Object[3];
                o4[0] = sorszam;
                o4[1] = osszeg;
                o4[2] = azon;
                App.db.insert("INSERT INTO szamlazo_szamla_kifizetesek (szamla_sorszam, datum, osszeg, azon) VALUES (?, NOW(), ?, ?)", o4, 3);
            }
            nyomtatva = 1;
            if (pdfKeszites.isSelected()) {
                elonezet(ElonezetDialog.PDF);
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        if (bezar) {
            String[] s = App.args;
            if (s[0].equalsIgnoreCase("p")) {
                // HA PIXIBŐL LETT INDÍTVA!!
                String keres = "(0";
                for (int i = 2; i < s.length; i++) {
                    keres += ", " + s[i];
                }
                keres += ")";
                Object[] o4 = new Object[1];
                o4[0] = sorszam;
                App.db.insert("UPDATE pixi_ajanlatkeresek_adatai SET statusz = 5, szamla = ? WHERE id IN " + keres, o4, 1);
                System.out.println("ok");
                System.exit(1);
                //App.getApplication().exit();
            } else {
                String keres = "(0";
                for (int i = 1; i < s.length; i++) {
                    keres += ", " + s[i];
                }
                keres += ")";
                Object[] o4 = new Object[1];
                o4[0] = sorszam;
                App.db.insert("UPDATE cr_ajanlatkeresek_adatai SET statusz = 6, szamla = ? WHERE id IN " + keres, o4, 1);
                System.out.println("ok");
                System.exit(1);
            }
        }
    }

    private String numberFormat(String num, boolean tizedes) {
        String result = "";
        double szam = Double.parseDouble(num);
        int elojel = 1;
        if (szam < 0) {
            elojel = -1;
            szam = Math.abs(szam);
        }
        NumberFormat formatter;
        if (tizedes) {
            formatter = new DecimalFormat("#,###.00");
            result = (elojel < 0 ? "-" : "") + (Math.floor(szam) == 0 ? "0" : "") + formatter.format(szam);
        } else {
            formatter = new DecimalFormat("#,###");
            result = (elojel < 0 ? "-" : "") + formatter.format(szam);
        }
        return result;
    }

    private String numberFormat(String num) {
        String result = "";
        double szam = Double.parseDouble(num);
        int elojel = 1;
        if (szam < 0) {
            elojel = -1;
            szam = Math.abs(szam);
        }
        NumberFormat formatter;
        formatter = new DecimalFormat("#,###.0000");
        result = formatter.format(szam);
        result = (elojel < 0 ? "-" : "") + (Math.floor(szam) == 0 ? "0" : "") + formatter.format(szam);
        return result;
    }

    private String dateFormat(String date) {
        String[] temp = date.split("-");
        return temp[0] + "." + temp[1] + "." + temp[2] + ".";
    }

    private String dateFormatEn(String date) {
        String[] temp = date.split("-");
        return temp[2] + "-" + temp[1] + "-" + temp[0];
    }

    private void folyamatbanNyit() {
        mentesButton.setEnabled(false);
        folyamatbanDialog.setSize(folyamatbanDialog.getPreferredSize());
        folyamatbanDialog.setLocation(getLocationOnScreen().x + (getSize().width - folyamatbanDialog.getSize().width) / 2, getLocationOnScreen().y + (getSize().height - folyamatbanDialog.getSize().height) / 2);
        folyamatbanDialog.setVisible(true);
    }

    class SzamlaThread extends Thread {

        public SzamlaThread() {
            start();
        }

        @Override
        public void run() {
            mentesButton.setEnabled(false);
            String ssz = sorszam;
            Calendar now = Calendar.getInstance();
            Label l = (Label) szallitoComboBox.getSelectedItem();
            Query query = new Query.QueryBuilder()
                    .select("db, ev, elotag ")
                    .from("szamlazo_szamla_sorszam ")
                    .where("id = (SELECT sorszamid from szamlazo_ceg_adatok WHERE id = " + l.getId() + ")")
                    .build();
            Object[][] s = App.db.select(query.getQuery());
            App.db.insert("UPDATE szamlazo_szamla_sorszam SET db = db + 1 WHERE id = (SELECT sorszamid from szamlazo_ceg_adatok WHERE id = " + l.getId() + ")", null, 0);
            sorszam = now.get(Calendar.YEAR) + "/" + (Integer.parseInt(String.valueOf(s[0][0])) + 1);
            if (!String.valueOf(s[0][2]).isEmpty()) {
                sorszam = String.valueOf(s[0][2]) + " " + sorszam;
            }
            if (ssz.matches(sorszam)) {
                szamlaMentes();
                folyamatbanDialog.setVisible(false);
                osszegzoDialog.setVisible(false);
                doClose(RET_OK);
            } else {
                if (!deviza) {
                    HibaDialog h = new HibaDialog("A számla sorszáma időközben megváltozott. Új sorszám: " + sorszam, "Ok", "");
                } else {
                    HibaDialog h = new HibaDialog("A számla sorszáma időközben megváltozott. Új sorszám: " + sorszam + "/V", "Ok", "");
                }
            }
            folyamatbanDialog.setVisible(false);
        }
    }
}
