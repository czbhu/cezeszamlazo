package cezeszamlazo.views;

import cezeszamlazo.AboutBox;
import cezeszamlazo.App;
import cezeszamlazo.DijbekerokFrame;
import cezeszamlazo.EncodeDecode;
import cezeszamlazo.FrissitesDialog;
import cezeszamlazo.LoginDialog;
import cezeszamlazo.SzamlaLablecDialog;
import cezeszamlazo.SzamlakFrame;
import cezeszamlazo.TermekCsoportokFrame;
import cezeszamlazo.TermekekFrame;
import cezeszamlazo.UpdateDialog;
import cezeszamlazo.User;
import cezeszamlazo.UserDialog;
import cezeszamlazo.VtszTeszorFrame;
import cezeszamlazo.controller.Functions;
import cezeszamlazo.database.Query;
import cezeszamlazo.interfaces.SharedValues;
import cezeszamlazo.kintlevoseg.KintlevosegLevel;
import cezeszamlazo.kintlevoseg.KintlevosegLevelHtmlEditor;
import cezeszamlazo.model.LogModel;
import controller.Database;
import controller.DatabaseHost;
import invoice.Invoice;
import java.awt.Toolkit;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.JDialog;
import javax.swing.JFrame;
import nav.NAV;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import view.ClientsView;

/**
 * @author Tomy
 */
public class NewView extends javax.swing.JFrame implements SharedValues
{
    JDialog dialog_About;
    
    SzamlakFrame frame_Invoices;
    DijbekerokFrame frame_ProForms;
    InvoiceSketchesView frame_Sketches;
    
    ClientsView clientsView;
    ContactsView frame_Contacts;
    
    TermekekFrame frame_Products;
    TermekCsoportokFrame frame_ProductGroups;
    VtszTeszorFrame frame_VtszTeszor;
    
    LogsView frame_Logs;
    
    private ResourceMap rm = Application.getInstance(cezeszamlazo.App.class).getContext().getResourceMap(NewView.class);

    public NewView()
    {
        App.db = new Database(DatabaseHost.Type.ONLINE, DatabaseHost.Host.SZAMLAZO);
        App.db.Connect();
        
        App.pixi = new Database(DatabaseHost.Type.ONLINE, DatabaseHost.Host.PIXI);
        App.pixi.Connect();
        
        ReadUser();
        
        if (App.user == null)
        {
            LoginDialog l = new LoginDialog();
            
            if (l.getReturnStatus() == 1)
            {
                SaveUser();
            }
            else
            {
                System.exit(1);
            }
        }
        else
        {
            App.nav = new NAV.NavBuilder()
                .supplierId(App.user.getDefaultCompany())
                .szamlazoUserId(App.user.getId())
                .pixiDatabase(App.pixi)
                .szamlazoDatabase(App.db)
                .build();
        }

        if (App.args.length != 0)
        {
            SearchForUpdates();
            
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
        setIconImage(img);
        
        button_Settings.doClick();
        button_Products.setVisible(false);
        setVisible(true);
    }
    
    private void ReadUser()
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
                
                setTitle(rm.getString("Application.title") + " - " + EncodeDecode.decode(App.user.getNev()));
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
    
    private void SaveUser()
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
    
    public void SearchForUpdates()
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
    
    private void UpdateLogButton()
    {
        if(LogModel.isNewLog())
        {
            button_Log.setText("Napló (új bejegyzés)");
        }
    }
    
    @Action
    public void showAboutBox()
    {
        if(dialog_About == null)
        {
            JFrame mainFrame = App.getApplication().getMainFrame();
            dialog_About = new AboutBox(mainFrame);
            dialog_About.setLocationRelativeTo(mainFrame);
        }
        
        App.getApplication().show(dialog_About);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        button_NewInvoice = new javax.swing.JButton();
        button_Invoices = new javax.swing.JButton();
        button_NewProForm = new javax.swing.JButton();
        button_ProForms = new javax.swing.JButton();
        button_NewSketch = new javax.swing.JButton();
        button_Sketches = new javax.swing.JButton();
        button_Customers = new javax.swing.JButton();
        button_Contacts = new javax.swing.JButton();
        button_Products = new javax.swing.JButton();
        button_Settings = new javax.swing.JButton();
        panel_Settings = new javax.swing.JPanel();
        button_InvoiceGroups = new javax.swing.JButton();
        button_ProductGroups = new javax.swing.JButton();
        button_Currencies = new javax.swing.JButton();
        button_Vats = new javax.swing.JButton();
        button_VtszTeszor = new javax.swing.JButton();
        button_InvoiceFooterEditor = new javax.swing.JButton();
        button_NewUser = new javax.swing.JButton();
        button_OutstandingPdfEditor = new javax.swing.JButton();
        button_OutstangingEmailEditor = new javax.swing.JButton();
        button_InvoiceEmialEditor = new javax.swing.JButton();
        button_Log = new javax.swing.JButton();
        button_QueryInvoiceData = new javax.swing.JButton();
        menuBar_Menu = new javax.swing.JMenuBar();
        menu_File = new javax.swing.JMenu();
        menuItem_Quit = new javax.swing.JMenuItem();
        menu_Help = new javax.swing.JMenu();
        menuItem_About = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(551, 0));
        setName("Form"); // NOI18N
        addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                formWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
            }
        });

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(cezeszamlazo.App.class).getContext().getResourceMap(NewView.class);
        button_NewInvoice.setFont(resourceMap.getFont("button_Invoices.font")); // NOI18N
        button_NewInvoice.setIcon(resourceMap.getIcon("button_NewInvoice.icon")); // NOI18N
        button_NewInvoice.setText(resourceMap.getString("button_NewInvoice.text")); // NOI18N
        button_NewInvoice.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        button_NewInvoice.setName("button_NewInvoice"); // NOI18N
        button_NewInvoice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_NewInvoiceActionPerformed(evt);
            }
        });

        button_Invoices.setFont(resourceMap.getFont("button_Invoices.font")); // NOI18N
        button_Invoices.setIcon(resourceMap.getIcon("button_Invoices.icon")); // NOI18N
        button_Invoices.setText(resourceMap.getString("button_Invoices.text")); // NOI18N
        button_Invoices.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        button_Invoices.setName("button_Invoices"); // NOI18N
        button_Invoices.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_InvoicesActionPerformed(evt);
            }
        });

        button_NewProForm.setFont(resourceMap.getFont("button_Invoices.font")); // NOI18N
        button_NewProForm.setIcon(resourceMap.getIcon("button_NewProForm.icon")); // NOI18N
        button_NewProForm.setText(resourceMap.getString("button_NewProForm.text")); // NOI18N
        button_NewProForm.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        button_NewProForm.setName("button_NewProForm"); // NOI18N
        button_NewProForm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_NewProFormActionPerformed(evt);
            }
        });

        button_ProForms.setFont(resourceMap.getFont("button_Invoices.font")); // NOI18N
        button_ProForms.setIcon(resourceMap.getIcon("button_ProForms.icon")); // NOI18N
        button_ProForms.setText(resourceMap.getString("button_ProForms.text")); // NOI18N
        button_ProForms.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        button_ProForms.setName("button_ProForms"); // NOI18N
        button_ProForms.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_ProFormsActionPerformed(evt);
            }
        });

        button_NewSketch.setFont(resourceMap.getFont("button_Invoices.font")); // NOI18N
        button_NewSketch.setIcon(resourceMap.getIcon("button_NewSketch.icon")); // NOI18N
        button_NewSketch.setText(resourceMap.getString("button_NewSketch.text")); // NOI18N
        button_NewSketch.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        button_NewSketch.setName("button_NewSketch"); // NOI18N
        button_NewSketch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_NewSketchActionPerformed(evt);
            }
        });

        button_Sketches.setFont(resourceMap.getFont("button_Invoices.font")); // NOI18N
        button_Sketches.setIcon(resourceMap.getIcon("button_Sketches.icon")); // NOI18N
        button_Sketches.setText(resourceMap.getString("button_Sketches.text")); // NOI18N
        button_Sketches.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        button_Sketches.setName("button_Sketches"); // NOI18N
        button_Sketches.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_SketchesActionPerformed(evt);
            }
        });

        button_Customers.setFont(resourceMap.getFont("button_Customers.font")); // NOI18N
        button_Customers.setIcon(resourceMap.getIcon("button_Customers.icon")); // NOI18N
        button_Customers.setText(resourceMap.getString("button_Customers.text")); // NOI18N
        button_Customers.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        button_Customers.setName("button_Customers"); // NOI18N
        button_Customers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_CustomersActionPerformed(evt);
            }
        });

        button_Contacts.setFont(resourceMap.getFont("button_Contacts.font")); // NOI18N
        button_Contacts.setIcon(resourceMap.getIcon("button_Contacts.icon")); // NOI18N
        button_Contacts.setText(resourceMap.getString("button_Contacts.text")); // NOI18N
        button_Contacts.setEnabled(false);
        button_Contacts.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        button_Contacts.setName("button_Contacts"); // NOI18N
        button_Contacts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_ContactsActionPerformed(evt);
            }
        });

        button_Products.setFont(resourceMap.getFont("button_Products.font")); // NOI18N
        button_Products.setIcon(resourceMap.getIcon("button_Products.icon")); // NOI18N
        button_Products.setText(resourceMap.getString("button_Products.text")); // NOI18N
        button_Products.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        button_Products.setName("button_Products"); // NOI18N
        button_Products.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_ProductsActionPerformed(evt);
            }
        });

        button_Settings.setFont(resourceMap.getFont("button_Settings.font")); // NOI18N
        button_Settings.setIcon(resourceMap.getIcon("button_Settings.icon")); // NOI18N
        button_Settings.setText(resourceMap.getString("button_Settings.text")); // NOI18N
        button_Settings.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        button_Settings.setName("button_Settings"); // NOI18N
        button_Settings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_SettingsActionPerformed(evt);
            }
        });

        panel_Settings.setName("panel_Settings"); // NOI18N

        button_InvoiceGroups.setFont(resourceMap.getFont("button_InvoiceGroups.font")); // NOI18N
        button_InvoiceGroups.setIcon(resourceMap.getIcon("button_InvoiceGroups.icon")); // NOI18N
        button_InvoiceGroups.setText(resourceMap.getString("button_InvoiceGroups.text")); // NOI18N
        button_InvoiceGroups.setEnabled(false);
        button_InvoiceGroups.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        button_InvoiceGroups.setName("button_InvoiceGroups"); // NOI18N
        button_InvoiceGroups.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_InvoiceGroupsActionPerformed(evt);
            }
        });

        button_ProductGroups.setFont(resourceMap.getFont("jButton1.font")); // NOI18N
        button_ProductGroups.setIcon(resourceMap.getIcon("button_ProductGroups.icon")); // NOI18N
        button_ProductGroups.setText(resourceMap.getString("button_ProductGroups.text")); // NOI18N
        button_ProductGroups.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        button_ProductGroups.setName("button_ProductGroups"); // NOI18N
        button_ProductGroups.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_ProductGroupsActionPerformed(evt);
            }
        });

        button_Currencies.setFont(resourceMap.getFont("jButton1.font")); // NOI18N
        button_Currencies.setIcon(resourceMap.getIcon("button_Currencies.icon")); // NOI18N
        button_Currencies.setText(resourceMap.getString("button_Currencies.text")); // NOI18N
        button_Currencies.setEnabled(false);
        button_Currencies.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        button_Currencies.setName("button_Currencies"); // NOI18N
        button_Currencies.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_CurrenciesActionPerformed(evt);
            }
        });

        button_Vats.setFont(resourceMap.getFont("button_Vats.font")); // NOI18N
        button_Vats.setIcon(resourceMap.getIcon("button_Vats.icon")); // NOI18N
        button_Vats.setText(resourceMap.getString("button_Vats.text")); // NOI18N
        button_Vats.setEnabled(false);
        button_Vats.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        button_Vats.setName("button_Vats"); // NOI18N
        button_Vats.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_VatsActionPerformed(evt);
            }
        });

        button_VtszTeszor.setFont(resourceMap.getFont("button_VtszTeszor.font")); // NOI18N
        button_VtszTeszor.setIcon(resourceMap.getIcon("button_VtszTeszor.icon")); // NOI18N
        button_VtszTeszor.setText(resourceMap.getString("button_VtszTeszor.text")); // NOI18N
        button_VtszTeszor.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        button_VtszTeszor.setName("button_VtszTeszor"); // NOI18N
        button_VtszTeszor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_VtszTeszorActionPerformed(evt);
            }
        });

        button_InvoiceFooterEditor.setFont(resourceMap.getFont("button_InvoiceFooterEditor.font")); // NOI18N
        button_InvoiceFooterEditor.setText(resourceMap.getString("button_InvoiceFooterEditor.text")); // NOI18N
        button_InvoiceFooterEditor.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        button_InvoiceFooterEditor.setName("button_InvoiceFooterEditor"); // NOI18N
        button_InvoiceFooterEditor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_InvoiceFooterEditorActionPerformed(evt);
            }
        });

        button_NewUser.setFont(resourceMap.getFont("button_NewUser.font")); // NOI18N
        button_NewUser.setText(resourceMap.getString("button_NewUser.text")); // NOI18N
        button_NewUser.setEnabled(false);
        button_NewUser.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        button_NewUser.setName("button_NewUser"); // NOI18N
        button_NewUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_NewUserActionPerformed(evt);
            }
        });

        button_OutstandingPdfEditor.setFont(resourceMap.getFont("button_OutstandingPdfEditor.font")); // NOI18N
        button_OutstandingPdfEditor.setText(resourceMap.getString("button_OutstandingPdfEditor.text")); // NOI18N
        button_OutstandingPdfEditor.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        button_OutstandingPdfEditor.setName("button_OutstandingPdfEditor"); // NOI18N
        button_OutstandingPdfEditor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_OutstandingPdfEditorActionPerformed(evt);
            }
        });

        button_OutstangingEmailEditor.setFont(resourceMap.getFont("button_OutstangingEmailEditor.font")); // NOI18N
        button_OutstangingEmailEditor.setText(resourceMap.getString("button_OutstangingEmailEditor.text")); // NOI18N
        button_OutstangingEmailEditor.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        button_OutstangingEmailEditor.setName("button_OutstangingEmailEditor"); // NOI18N
        button_OutstangingEmailEditor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_OutstangingEmailEditorActionPerformed(evt);
            }
        });

        button_InvoiceEmialEditor.setFont(resourceMap.getFont("button_InvoiceEmialEditor.font")); // NOI18N
        button_InvoiceEmialEditor.setText(resourceMap.getString("button_InvoiceEmialEditor.text")); // NOI18N
        button_InvoiceEmialEditor.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        button_InvoiceEmialEditor.setName("button_InvoiceEmialEditor"); // NOI18N
        button_InvoiceEmialEditor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_InvoiceEmialEditorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel_SettingsLayout = new javax.swing.GroupLayout(panel_Settings);
        panel_Settings.setLayout(panel_SettingsLayout);
        panel_SettingsLayout.setHorizontalGroup(
            panel_SettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_SettingsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_SettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(button_InvoiceGroups, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(button_ProductGroups, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(button_Currencies, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(button_Vats, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(button_VtszTeszor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(button_InvoiceFooterEditor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(button_NewUser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(button_OutstandingPdfEditor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(button_OutstangingEmailEditor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(button_InvoiceEmialEditor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panel_SettingsLayout.setVerticalGroup(
            panel_SettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_SettingsLayout.createSequentialGroup()
                .addComponent(button_InvoiceGroups, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(button_ProductGroups, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(button_Currencies, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(button_Vats, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(button_VtszTeszor, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(button_NewUser, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(button_InvoiceFooterEditor, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addComponent(button_OutstandingPdfEditor, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(button_OutstangingEmailEditor, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(button_InvoiceEmialEditor, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        button_Log.setFont(resourceMap.getFont("button_Log.font")); // NOI18N
        button_Log.setIcon(resourceMap.getIcon("button_Log.icon")); // NOI18N
        button_Log.setText(resourceMap.getString("button_Log.text")); // NOI18N
        button_Log.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        button_Log.setName("button_Log"); // NOI18N
        button_Log.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_LogActionPerformed(evt);
            }
        });

        button_QueryInvoiceData.setFont(resourceMap.getFont("button_QueryInvoiceData.font")); // NOI18N
        button_QueryInvoiceData.setText(resourceMap.getString("button_QueryInvoiceData.text")); // NOI18N
        button_QueryInvoiceData.setEnabled(false);
        button_QueryInvoiceData.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        button_QueryInvoiceData.setName("button_QueryInvoiceData"); // NOI18N
        button_QueryInvoiceData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_QueryInvoiceDataActionPerformed(evt);
            }
        });

        menuBar_Menu.setName("menuBar_Menu"); // NOI18N

        menu_File.setText(resourceMap.getString("menu_File.text")); // NOI18N
        menu_File.setName("menu_File"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(cezeszamlazo.App.class).getContext().getActionMap(NewView.class, this);
        menuItem_Quit.setAction(actionMap.get("quit")); // NOI18N
        menuItem_Quit.setText(resourceMap.getString("menuItem_Quit.text")); // NOI18N
        menuItem_Quit.setName("menuItem_Quit"); // NOI18N
        menu_File.add(menuItem_Quit);

        menuBar_Menu.add(menu_File);

        menu_Help.setText(resourceMap.getString("menu_Help.text")); // NOI18N
        menu_Help.setName("menu_Help"); // NOI18N

        menuItem_About.setAction(actionMap.get("showAboutBox")); // NOI18N
        menuItem_About.setText(resourceMap.getString("menuItem_About.text")); // NOI18N
        menuItem_About.setName("menuItem_About"); // NOI18N
        menu_Help.add(menuItem_About);

        menuBar_Menu.add(menu_Help);

        setJMenuBar(menuBar_Menu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(button_NewInvoice, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                    .addComponent(button_QueryInvoiceData, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                    .addComponent(button_Invoices, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                    .addComponent(button_NewProForm, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                    .addComponent(button_ProForms, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                    .addComponent(button_NewSketch, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                    .addComponent(button_Sketches, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                    .addComponent(button_Customers, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                    .addComponent(button_Contacts, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                    .addComponent(button_Products, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                    .addComponent(button_Settings, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                    .addComponent(button_Log, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(panel_Settings, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(button_NewInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(button_Invoices, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(button_NewProForm, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(button_ProForms, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(button_NewSketch, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(button_Sketches, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(button_Customers, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(button_Contacts, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(button_Products, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(button_Settings, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(button_Log, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(button_QueryInvoiceData, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(panel_Settings, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowGainedFocus
        UpdateLogButton();
    }//GEN-LAST:event_formWindowGainedFocus

    private void button_NewInvoiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_NewInvoiceActionPerformed
        ResizeableInvoiceView view = new ResizeableInvoiceView(Invoice.INVOICE);
        
        if(view.getReturnStatus() == ResizeableInvoiceView.RET_REOPEN)
        {
            button_NewInvoice.doClick();
        }
    }//GEN-LAST:event_button_NewInvoiceActionPerformed

    private void button_InvoicesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_InvoicesActionPerformed
        if (frame_Invoices == null)
        {
            frame_Invoices = new SzamlakFrame();    
        }
        
        frame_Invoices.nyit();
    }//GEN-LAST:event_button_InvoicesActionPerformed

    private void button_NewProFormActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_NewProFormActionPerformed
        ResizeableInvoiceView view = new ResizeableInvoiceView(Invoice.PROFORMA);
    }//GEN-LAST:event_button_NewProFormActionPerformed

    private void button_ProFormsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_ProFormsActionPerformed
        if(frame_ProForms == null)
        {
            frame_ProForms = new DijbekerokFrame();
        }
        
        frame_ProForms.nyit();
    }//GEN-LAST:event_button_ProFormsActionPerformed

    private void button_NewSketchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_NewSketchActionPerformed
        ResizeableInvoiceView view = new ResizeableInvoiceView(Invoice.SKETCH);
    }//GEN-LAST:event_button_NewSketchActionPerformed

    private void button_SketchesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_SketchesActionPerformed
        if(frame_Sketches == null)
        {
            frame_Sketches = new InvoiceSketchesView();
        }
        
        frame_Sketches.Open();
    }//GEN-LAST:event_button_SketchesActionPerformed

    private void button_CustomersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_CustomersActionPerformed
        if(clientsView == null)
        {
            clientsView = new ClientsView(App.nav);
        }
        
        clientsView.Open(false);
    }//GEN-LAST:event_button_CustomersActionPerformed

    private void button_ContactsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_ContactsActionPerformed
        if (frame_Contacts == null)
        {
            frame_Contacts = new ContactsView();
        }
        
        frame_Contacts.Open();
    }//GEN-LAST:event_button_ContactsActionPerformed

    private void button_ProductsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_ProductsActionPerformed
        if (frame_Products == null)
        {
            frame_Products = new TermekekFrame();
        }
        
        frame_Products.nyit();
    }//GEN-LAST:event_button_ProductsActionPerformed

    private void button_SettingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_SettingsActionPerformed
        panel_Settings.setVisible(!panel_Settings.isVisible());
    }//GEN-LAST:event_button_SettingsActionPerformed

    private void button_InvoiceGroupsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_InvoiceGroupsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_button_InvoiceGroupsActionPerformed

    private void button_ProductGroupsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_ProductGroupsActionPerformed
        if (frame_ProductGroups == null)
        {
            frame_ProductGroups = new TermekCsoportokFrame();
        }
        
        frame_ProductGroups.nyit();
    }//GEN-LAST:event_button_ProductGroupsActionPerformed

    private void button_CurrenciesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_CurrenciesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_button_CurrenciesActionPerformed

    private void button_VatsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_VatsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_button_VatsActionPerformed

    private void button_VtszTeszorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_VtszTeszorActionPerformed
        if (frame_VtszTeszor == null)
        {
            frame_VtszTeszor = new VtszTeszorFrame();
        }
        
        frame_VtszTeszor.nyit();
    }//GEN-LAST:event_button_VtszTeszorActionPerformed

    private void button_InvoiceFooterEditorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_InvoiceFooterEditorActionPerformed
        SzamlaLablecDialog szamlaLablecDialog = new SzamlaLablecDialog();
    }//GEN-LAST:event_button_InvoiceFooterEditorActionPerformed

    private void button_NewUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_NewUserActionPerformed
        UserDialog userDialog = new UserDialog();
    }//GEN-LAST:event_button_NewUserActionPerformed

    private void button_OutstandingPdfEditorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_OutstandingPdfEditorActionPerformed
        KintlevosegLevelHtmlEditor kintlevosegLevelHtmlEditorPDF = KintlevosegLevelHtmlEditor.create(KintlevosegLevel.Type.PDF, true);
        kintlevosegLevelHtmlEditorPDF.run();
    }//GEN-LAST:event_button_OutstandingPdfEditorActionPerformed

    private void button_OutstangingEmailEditorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_OutstangingEmailEditorActionPerformed
        KintlevosegLevelHtmlEditor kintlevosegLevelHtmlEditorEMAIL = KintlevosegLevelHtmlEditor.create(KintlevosegLevel.Type.EMAIL, true);
        kintlevosegLevelHtmlEditorEMAIL.run();
    }//GEN-LAST:event_button_OutstangingEmailEditorActionPerformed

    private void button_InvoiceEmialEditorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_InvoiceEmialEditorActionPerformed
        KintlevosegLevelHtmlEditor kintlevosegLevelHtmlEditorEMAIL = KintlevosegLevelHtmlEditor.create(KintlevosegLevel.Type.EMAIL, false);
        kintlevosegLevelHtmlEditorEMAIL.run();
    }//GEN-LAST:event_button_InvoiceEmialEditorActionPerformed

    private void button_LogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_LogActionPerformed
        if(frame_Logs == null)
        {
            frame_Logs = new LogsView();
        }
        
        frame_Logs.Open();
        
        button_Log.setText("Napló");
    }//GEN-LAST:event_button_LogActionPerformed

    private void button_QueryInvoiceDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_QueryInvoiceDataActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_button_QueryInvoiceDataActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton button_Contacts;
    private javax.swing.JButton button_Currencies;
    private javax.swing.JButton button_Customers;
    private javax.swing.JButton button_InvoiceEmialEditor;
    private javax.swing.JButton button_InvoiceFooterEditor;
    private javax.swing.JButton button_InvoiceGroups;
    private javax.swing.JButton button_Invoices;
    private javax.swing.JButton button_Log;
    private javax.swing.JButton button_NewInvoice;
    private javax.swing.JButton button_NewProForm;
    private javax.swing.JButton button_NewSketch;
    private javax.swing.JButton button_NewUser;
    private javax.swing.JButton button_OutstandingPdfEditor;
    private javax.swing.JButton button_OutstangingEmailEditor;
    private javax.swing.JButton button_ProForms;
    private javax.swing.JButton button_ProductGroups;
    private javax.swing.JButton button_Products;
    private javax.swing.JButton button_QueryInvoiceData;
    private javax.swing.JButton button_Settings;
    private javax.swing.JButton button_Sketches;
    private javax.swing.JButton button_Vats;
    private javax.swing.JButton button_VtszTeszor;
    private javax.swing.JMenuBar menuBar_Menu;
    private javax.swing.JMenuItem menuItem_About;
    private javax.swing.JMenuItem menuItem_Quit;
    private javax.swing.JMenu menu_File;
    private javax.swing.JMenu menu_Help;
    private javax.swing.JPanel panel_Settings;
    // End of variables declaration//GEN-END:variables
}