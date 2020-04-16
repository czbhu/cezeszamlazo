package invoice;

import controller.XmlLogger;
import database.Query;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import nav.NAV;
import view.UserMessage;

/**
 * @author Tomy
 * '//' jellel jelzem ami még nem biztos illetve késöbb kellhet még
 */
public class Invoice
{
    private final static String table_invoiceNumber = "szamlazo_szamla_sorszam";
    
    public final static String table_invoices = "szamlazo_invoices";
    private final static String table_invoice_products = "szamlazo_invoice_products";
    private final static String table_invoice_productFee = "szamlazo_invoice_productFee";
    
    private final static String table_pro_forms = "szamlazo_pro_forms";
    private final static String table_pro_form_products = "szamlazo_pro_form_products";
    private final static String table_pro_form_productFee = "szamlazo_pro_form_productFee";
    
    private final static String table_sketches = "szamlazo_sketches";
    private final static String table_sketch_products = "szamlazo_sketches_products";
    private final static String table_sketch_productFee = "szamlazo_sketches_productFee";
    
    private final static String table_CC = "szamlazo_teljesites_igazolas";
    
    private final static String table_valuta = "szamlazo_valuta";
    private final static String table_users = "szamlazo_users";
    
    private String productTable;
    private String productFeeTable;
    
    public static final int
        INVOICE = 1,                /*Számla*/
        PROFORMA = 2,               /*Díjbekérő*/
        SKETCH = 3,                 /*Vázlat*/
        COMPLETION_CERTIFICATE = 4; /*Teljesítés igazolás*/
    
    public enum InvoiceType
    {
        NEW,            /*Új*/
        COPY,           /*Másolat (friss adatokkal)*/
        CORRECTION,     /*Helyesbítő*/
        STORNO,         /*Stornó*/
        MODIFICATION,   /*Módosító*/
        ORIGINAL,       /*Másolat/Eredeti (minden adat az adatbázisban elmentett szerint)*/
    }
    
    int userId;
    
    private int type;                           /*Számla fajtája*/
    private InvoiceType invoiceType;            /*Számla típusa*/
    private int serializationID;                /*SorszámozásID (a szamla_sorszam tábla ID-ja)*/
    
    private String indentifier;                 /*A számla egyedi azonosítója (a mentés időpontja milisec-ben + Random szám)*/
    private String invoiceNumber;               /*A számla sorszáma*/
    private String correctedInvoice;            /*A módosított számla sorszáma (ha a számla az eredeti számla valamilyen módosítása)*/
    private String invoiceGroup;                /*Számla csoport (0-ás értéket rendelünk hozzá mivel már nincs használatban)*/
    
    private Supplier supplier;                  /*Szállító - A számlát kiállító cég*/
    private Customer customer;                  /*Vevő - A megrendelő*/
    
    private String issueDate;                   /*A számla kelte*/
    private String maturityDate;                /*Esedékeség dátuma*/
    private String completionDate;              /*A megrendelés teljesítésének ideje*/
    private String paymentDate;                 /*A kifizetés dátuma*/
    private String timestamp;                   /*Időbélyeg a számla keltéről*/
    
    private InvoiceProducts products = new InvoiceProducts();/*A számla termékei*/
    
    private int paymentMethod;                  /*A kifizetés típusa*/
    private String currency;                    /*A számla pénzneme*/
    private double centralParity;               /*A pénznemhez tartozó középárfolyam*/
    
    private String takeoverType = "";           /*A termékdíj átválalás típusa*/
    
    private double netPrice;                    /*A számla nettó összege*/
    private double vatAmount;                   /*A számla áfa öszege*/
    
    private List<Afa> vats;                     /*A számla termékekben lévő áfa %-ok*/
    ArrayList<String> vatLabels;
    
    private String comment;                     /*A számlához fűzött megjegyzés*/
    private String footer;                      /*A számla lábléce*/
    
    private boolean foreignCurrency;            /*A számla deviza számla-e*/
    private boolean printed;                    /*Nyomtatva lett-e*/
    
    private String fromTable;                   /*Melyik táblából hozzuk létre a számlát*/
    private String toTable;                     /*Melyik táblában kell a számlát menteni*/
    
    private String navStatus = "";              /*A beküldött számla státusza*/
    private String transactionID = "";          /*A lekérdezéshez használt tranzakcióID*/
    
    private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    
    /*New invoice with default datas*/
    public Invoice(int type)
    {
        this.type = type;
        invoiceType = InvoiceType.NEW;
        
        serializationID = 0;
        indentifier = "";
        invoiceNumber = "";
        correctedInvoice = "";
        invoiceGroup = "0";
        
        supplier = new Supplier();
        customer = new Customer();
        
        issueDate = format.format(new Date());
        maturityDate = format.format(new Date());
        completionDate = format.format(new Date());
        paymentDate = "";
        timestamp = "";
        
        paymentMethod = 0;
        currency = "Ft";
        centralParity = 1.0;
        
        takeoverType = "";
        
        netPrice = 0.0;
        vatAmount = 0.0;
        
        comment = "";
        footer = "";
        
        foreignCurrency = false;
        printed = false;
        
        fromTable = "szamlazo_invoices";
        
        SetToTable();
    }
    
    /*Copy of existing invoice*/
    public Invoice(int type, InvoiceType invoiceType, String table, String indentifier)
    {
        this.type = type;
        this.invoiceType = invoiceType;
        this.indentifier = indentifier;
        
        fromTable = table;
        SetToTable();
        FillInvoice();
    }
    
    private void SetToTable()
    {
        switch(type)
        {
            case INVOICE:
                toTable = "szamlazo_invoices";
                productTable = "szamlazo_invoice_products";
                productFeeTable = "szamlazo_invoice_productFee";
                break;
            case PROFORMA:
                toTable = "szamlazo_pro_forms";
                productTable = "szamlazo_pro_form_products";
                productFeeTable = "szamlazo_pro_form_productFee";
                break;
            case SKETCH:
                toTable = "szamlazo_sketches";
                productTable = "szamlazo_sketches_products";
                productFeeTable = "szamlazo_sketches_productFee";
                break;
            case COMPLETION_CERTIFICATE:
                toTable = "szamlazo_teljesites_igazolas";
                break;
        }
    }
    
    private void FillInvoice()
    {
        switch(fromTable)
        {
            case "szamlazo_invoices":
                GetInvoice();
                break;
            case "szamlazo_pro_forms":
                GetProForm();
                break;
            case "szamlazo_sketches":
                GetSketch();
                break;
        }
    }
    
    private void GetInvoice()
    {
        switch (invoiceType)
        {
            case COPY:
                GetUpdatedInvoice();
                break;
            case CORRECTION:
                System.err.println("invoice.java/GetInvoice/CORRECTION");
                break;
            case STORNO:
                GetOriginalInvoice();
                break;
            case MODIFICATION:
                System.err.println("invoice.java/GetInvoice/MODIFICATION");
                break;
            case ORIGINAL:
                GetOriginalInvoice();
                break;
            default:
                break;
        }
    }
    
    /*Másolat számla (ha valami változott az eredeti számla kiállítása óta azt figyelembe veszi)*/
    private void GetUpdatedInvoice()
    {
        Query query = new Query.QueryBuilder()
            .select("serializationID, "
                + "supplierID, "
                + "customerID, "
                + "paymentMethod, "
                + "currency, "
                + "takeoverType, "
                + "netPrice, "
                + "vatAmount, "
                + "comment, "
                + "footer, "
                + "foreignCurrency, "
                + "printed "
                + "transactionID")
            .from(fromTable)
            .where("indentifier = '" + indentifier + "'")
            .build();
        Object [][] invoice = NAV.db.select(query.getQuery());
        
        serializationID = Integer.parseInt(invoice[0][0].toString());
        invoiceNumber = GetNewInvoiceNumber();
        correctedInvoice = ""; //ez késöbb majd még kellhet (módosító, helyesbítő)
        
        supplier = new Supplier(Integer.parseInt(invoice[0][1].toString()));
        
        /*int customerID = Integer.parseInt(invoice[0][2].toString());
        
        if(customerID == 0)
        {
            customer = new Customer(indentifier, fromTable);
        }
        else
        {
            customer = new Customer(customerID); 
        }*/
        customer = new Customer(indentifier, fromTable);
        
        issueDate = format.format(new Date());
        maturityDate = GetMaturityDate();
        completionDate = format.format(new Date());
        paymentDate = "0000-00-00";
        timestamp = "";
        
        GetProducts(table_invoice_products, table_invoice_productFee);
        
        paymentMethod = Integer.parseInt(invoice[0][3].toString());
        currency = invoice[0][4].toString();
        GetNewCentralParity();
        
        takeoverType = invoice[0][5].toString();
        
        netPrice = Double.parseDouble(invoice[0][6].toString());
        vatAmount = Double.parseDouble(invoice[0][7].toString());
        
        comment = invoice[0][8].toString();
        footer = invoice[0][9].toString();
        
        foreignCurrency = (invoice[0][10].toString().equals("1"));
        printed = (invoice[0][11].toString().equals("1"));
        
        try
        {
            transactionID = invoice[0][12].toString();
        }
        catch(ArrayIndexOutOfBoundsException ex)
        {
            transactionID = "";
        }
    }
    
    /*Másolat számla (minden ugyan olyan marad, akkor is ha az adatok változtak az eredeti számla kiállítása óta)*/
    private void GetOriginalInvoice()
    {
        Query query = new Query.QueryBuilder()
            .select("serializationID, "
                + "invoiceNumber, "
                + "correctedInvoice, "
                + "issueDate, "
                + "maturityDate, "
                + "completionDate, "
                + "paymentDate, "
                + "timestamp, "
                + "paymentMethod, "
                + "currency, "
                + "centralParity, "
                + "takeoverType, "
                + "netPrice, "
                + "vatAmount, "
                + "comment, "
                + "footer, "
                + "foreignCurrency, "
                + "printed, "
                + "transactionID")
            .from(fromTable)
            .where("indentifier = " + indentifier)
            .build();
        Object [][] invoice = NAV.db.select(query.getQuery());
        
        serializationID = Integer.parseInt(invoice[0][0].toString());
        
        if(invoiceType == InvoiceType.STORNO)
        {
            invoiceNumber = GetNewInvoiceNumber();
        }
        else
        {
            invoiceNumber = (type == COMPLETION_CERTIFICATE ? "TIG " : "") + invoice[0][1].toString();
        }
        
        correctedInvoice = invoice[0][2].toString();
        
        supplier = new Supplier(indentifier, fromTable);
        customer = new Customer(indentifier, fromTable);
        
        if(invoiceType == InvoiceType.STORNO)
        {
            issueDate = format.format(new Date());
        }
        else
        {
            issueDate = invoice[0][3].toString();
        }
        
        maturityDate = invoice[0][4].toString();
        completionDate = invoice[0][5].toString();
        paymentDate = invoice[0][6].toString();
        timestamp = invoice[0][7].toString();
        
        GetProducts(table_invoice_products, table_invoice_productFee);
        
        paymentMethod = Integer.parseInt(invoice[0][8].toString());
        
        currency = invoice[0][9].toString();
        centralParity = Double.parseDouble(invoice[0][10].toString());
        
        takeoverType = invoice[0][11].toString();
        
        netPrice = Double.parseDouble(invoice[0][12].toString());
        vatAmount = Double.parseDouble(invoice[0][13].toString());
        
        comment = invoice[0][14].toString();
        footer = invoice[0][15].toString();
        
        foreignCurrency = (invoice[0][16].toString().equals("1"));
        printed = (invoice[0][17].toString().equals("1"));
        
        if(invoiceType != InvoiceType.STORNO)
        {
            transactionID = invoice[0][18].toString();
        }
    }
    
    private void GetProForm()
    {
        switch (invoiceType)
        {
            case COPY:
                GetUpdatedProForm();
                break;
            case CORRECTION:
                System.err.println("invoice.java/GetProForm/CORRECTION");
                break;
            case STORNO:
                GetOriginalProForm();
                break;
            case MODIFICATION:
                System.err.println("invoice.java/GetProForm/MODIFICATION");
                break;
            case ORIGINAL:
                GetOriginalProForm();
                break;
            default:
                break;
        }
    }
    
    /*Másolat díjbekérő (ha valami változott az eredeti számla kiállítása óta azt figyelembe veszi)*/
    private void GetUpdatedProForm()
    {
        Query query = new Query.QueryBuilder()
            .select("serializationID, "
                + "supplierID, "
                + "customerID, "
                + "paymentMethod, "
                + "currency, "
                + "takeoverType, "
                + "netPrice, "
                + "vatAmount, "
                + "comment, "
                + "footer, "
                + "foreignCurrency, "
                + "printed")
            .from(fromTable)
            .where("indentifier = " + indentifier)
            .build();
        Object [][] proForm = NAV.db.select(query.getQuery());
        
        serializationID = Integer.parseInt(proForm[0][0].toString());
        invoiceNumber = GetNewInvoiceNumber();
        correctedInvoice = ""; //ez késöbb majd még kellhet (módosító, helyesbítő)
        
        supplier = new Supplier(Integer.parseInt(proForm[0][1].toString()));
        
        int customerID = Integer.parseInt(proForm[0][2].toString());
        
        if(customerID == 0)
        {
            customer = new Customer(indentifier, fromTable);
        }
        else
        {
            customer = new Customer(customerID); 
        }
        
        issueDate = format.format(new Date());
        maturityDate = GetMaturityDate();
        completionDate = format.format(new Date());
        paymentDate = "0000-00-00";
        timestamp = "";
        
        GetProducts(table_pro_form_products, table_pro_form_productFee);
        
        paymentMethod = Integer.parseInt(proForm[0][3].toString());
        currency = proForm[0][4].toString();
        GetNewCentralParity();
        
        takeoverType = proForm[0][5].toString();
        
        netPrice = Double.parseDouble(proForm[0][6].toString());
        vatAmount = Double.parseDouble(proForm[0][7].toString());
        
        comment = proForm[0][8].toString();
        footer = proForm[0][9].toString();
        
        foreignCurrency = (proForm[0][10].toString().equals("1"));
        printed = (proForm[0][11].toString().equals("1"));
    }
    
    /*Másolat díjbekérő(minden ugyan olyan marad, akkor is ha az adatok változtak az eredeti számla kiállítása óta)*/
    private void GetOriginalProForm()
    {
        Query query = new Query.QueryBuilder()
            .select("serializationID, "
                + "invoiceNumber, "
                + "issueDate, "
                + "maturityDate, "
                + "completionDate, "
                + "paymentDate, "
                + "paymentMethod, "
                + "currency, "
                + "centralParity, "
                + "takeoverType, "
                + "netPrice, "
                + "vatAmount, "
                + "comment, "
                + "footer, "
                + "foreignCurrency, "
                + "printed")
            .from(fromTable)
            .where("indentifier = " + indentifier)
            .build();
        Object [][] proForm = NAV.db.select(query.getQuery());
        
        serializationID = Integer.parseInt(proForm[0][0].toString());
        invoiceNumber = (type == COMPLETION_CERTIFICATE ? "TIG " : "") + proForm[0][1].toString();
        correctedInvoice = proForm[0][2].toString();
        
        supplier = new Supplier(indentifier, fromTable);
        customer = new Customer(indentifier, fromTable);
        
        issueDate = proForm[0][2].toString();
        maturityDate = proForm[0][3].toString();
        completionDate = proForm[0][4].toString();
        paymentDate = proForm[0][5].toString();
        
        GetProducts(table_pro_form_products, table_pro_form_productFee);
        
        paymentMethod = Integer.parseInt(proForm[0][6].toString());
        
        currency = proForm[0][7].toString();
        centralParity = Double.parseDouble(proForm[0][8].toString());
        
        takeoverType = proForm[0][9].toString();
        
        netPrice = Double.parseDouble(proForm[0][10].toString());
        vatAmount = Double.parseDouble(proForm[0][11].toString());
        
        comment = proForm[0][12].toString();
        footer = proForm[0][13].toString();
        
        foreignCurrency = (proForm[0][14].toString().equals("1"));
        printed = (proForm[0][15].toString().equals("1"));
    }
    
    private void GetSketch()
    {
        GetUpdatedSketch();
    }
    
    private void GetUpdatedSketch()
    {
        Query query = new Query.QueryBuilder()
            .select("serializationID, "
                + "invoiceNumber, "
                + "supplierID, "
                + "customerName, "
                + "customerCountryCode, "
                + "customerPostalCode, "
                + "customerCity, "
                + "customerStreet, "
                + "customerPublicPlace, "
                + "customerHouseNumber, "
                + "customerTelNumber, "
                + "customerEmail, "
                + "customerBankAccountNumber, "
                + "showInInvoice, "
                + "customerTaxNumber, "
                + "customerEuTaxNumber, "
                + "paymentMethod, "
                + "takeoverType, "
                + "currency, "
                + "foreignCurrency, "
                + "netPrice, "
                + "vatAmount, "
                + "comment, "
                + "footer")
            .from(fromTable)
            .where("indentifier = " + indentifier)
            .build();
        Object [][] sketch = NAV.db.select(query.getQuery());
        
        serializationID = Integer.parseInt(sketch[0][0].toString());
        invoiceNumber = sketch[0][1].toString();
        
        int supplierID = Integer.parseInt(sketch[0][2].toString());
        
        if(supplierID > 0)
        {
            supplier = new Supplier(supplierID);
            serializationID = GetSerializationID(supplierID);
        }
        else if(supplierID == 0)
        {
            Query queryCompany = new Query.QueryBuilder()
                .select("defaultCompanyID")
                .from(table_users)
                //.where("id = " + NAV.user.getId())
                    .where("id = " + NAV.userId)
                .build();
            Object [][] companyID = NAV.db.select(queryCompany.getQuery());
            
            supplier = new Supplier(Integer.parseInt(companyID[0][0].toString()));
            serializationID = GetSerializationID(Integer.parseInt(companyID[0][0].toString()));
        }
        
        customer = new Customer();
        
        customer.setName(sketch[0][3].toString());
        customer.setCountryCode(sketch[0][4].toString());
        customer.setPostalCode(sketch[0][5].toString());
        customer.setCity(sketch[0][6].toString());
        customer.setStreet(sketch[0][7].toString());
        customer.setPublicPlace(sketch[0][8].toString());
        customer.setHouseNumber(sketch[0][9].toString());
        customer.setTelNumber(sketch[0][10].toString());
        customer.setEmail(sketch[0][11].toString());
        customer.setBankAccountNumber(sketch[0][12].toString());
        customer.setShowInInvoice(sketch[0][13].toString().equals("1"));
        customer.setTaxNumber(sketch[0][14].toString());
        customer.setEuTaxNumber(sketch[0][15].toString());
        customer.setPaymentMethod(Integer.parseInt(sketch[0][16].toString()));
        
        issueDate = format.format(new Date());
        maturityDate = format.format(new Date());
        completionDate = format.format(new Date());
        paymentDate = "0000-00-00";
        
        GetProducts(table_sketch_products, table_sketch_productFee);
        
        paymentMethod = Integer.parseInt(sketch[0][16].toString());
        
        takeoverType = sketch[0][17].toString();
        
        currency = sketch[0][18].toString();
        GetNewCentralParity();
        
        foreignCurrency = sketch[0][19].toString().equals("1");
        
        netPrice = Double.parseDouble(sketch[0][20].toString());
        vatAmount = Double.parseDouble(sketch[0][21].toString());
        
        comment = sketch[0][22].toString();
        footer = sketch[0][23].toString();
    }
    
    private String GetNewInvoiceNumber()
    {
        String selectText = "";
        
        switch(type)
        {
            case(Invoice.INVOICE):
                selectText = "invoiceQuantity,";
                break;
            case(Invoice.PROFORMA):
                selectText = "proFormaQuantity,";
                break;
            case(Invoice.SKETCH):
                if(invoiceType == InvoiceType.STORNO)
                {
                    selectText = "invoiceQuantity,";
                }
                else
                {
                    selectText = "sketchQuantity,";
                }
                break;
        }
        
        Query query = new Query.QueryBuilder()
            .select(selectText + " year, prefix")
            .from(table_invoiceNumber)
            .where("id = " + serializationID + "")
            .build();
        Object [][] number = NAV.db.select(query.getQuery());

        String returnNumber = String.valueOf(number[0][1]) + "/" + (Integer.parseInt(String.valueOf(number[0][0])) + 1);

        if (!String.valueOf(number[0][2]).isEmpty())
        {
            returnNumber = String.valueOf(number[0][2]) + " " + returnNumber;
        }
        
        return returnNumber;
    }
    
    private String GetMaturityDate()
    {
        int maturityDays = customer.getMaturityDays();
        
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, maturityDays);
        return format.format(c.getTime());
    }
    
    private void GetProducts(String product_table, String productFee_table)
    {
        Query query = new Query.QueryBuilder()
            .select("id")
            .from(product_table)
            .where("indentifier = " + indentifier)
            .build();
        Object [][] productIDs = NAV.db.select(query.getQuery());
        
        for(int i = 0; i < productIDs.length; i++)
        {
            int productID = Integer.parseInt(productIDs[i][0].toString());
            
            InvoiceProduct product = new InvoiceProduct(productID, product_table, productFee_table);
            
            if(invoiceType == InvoiceType.STORNO)
            {
                product.setQuantity(product.getQuantity() * -1);
                
                if(product.getProductFee() != null)
                {
                    //product.getProductFee().setSzorzo(-1);
                    //product.getProductFee().setOsszsuly(product.getProductFee().getOsszsuly() * -1);
                }
            }
            
            products.Add(product);
        }
    }
    
    private void GetNewCentralParity()
    {
        String where = (currency.equals("Ft") ? "HUF" : currency.toUpperCase());
        Query query = new Query.QueryBuilder()
            .select("centralParity")
            .from(table_valuta)
            .where("currency = '" + where + "'")
            .build();
        Object [][] parity = NAV.db.select(query.getQuery());
        
        centralParity = Double.parseDouble(parity[0][0].toString());
    }
    
    public int GetSerializationID(int supplierID)
    {
        Query query = new Query.QueryBuilder()
            .select("serializationID")
            .from(Supplier.TABLE)
            .where("id = " + supplierID)
            .build();
        Object [][] result = NAV.db.select(query.getQuery());
        
        return Integer.parseInt(result[0][0].toString());
    }
    
    public void Save(int userId)
    {
        this.userId = userId;
        Calendar now = Calendar.getInstance();
        
        if(type != SKETCH && invoiceType != InvoiceType.MODIFICATION)
        {
           indentifier = String.valueOf(now.getTimeInMillis()).substring(0, 10) + String.valueOf((int) Math.round(Math.random() * 89) + 10);
        }

        if(serializationID == 0)
        {
            serializationID = GetSerializationID(supplier.getId());
        }
        
        switch(invoiceType)
        {
            case NEW:
                switch(type)
                {
                    case INVOICE:
                        SaveInvoice();
                        break;
                    case PROFORMA:
                        SaveProForm();
                        break;
                    case SKETCH:
                        if(invoiceType == InvoiceType.MODIFICATION)
                        {
                            UpdateSketch();
                        }
                        else if(invoiceType == InvoiceType.STORNO)
                        {
                            SaveInvoice();
                        }
                        else
                        {
                            SaveSketch();
                        }
                        break;
                    case COMPLETION_CERTIFICATE:
                        SaveCC();
                        break;
                }
                break;
            case COPY:
                switch(type)
                {
                    case INVOICE:
                        SaveInvoice();
                        break;
                    case PROFORMA:
                        SaveProForm();
                        break;
                    case SKETCH:
                        SaveSketch();
                        break;
                }
                break;
            case CORRECTION:
                System.err.println("Invoice.java/Save()/CORRECTION");
                break;
            case MODIFICATION:
                System.err.println("Invoice.java/Save()/MODIFICATION");
                break;
            case STORNO:
                switch(type)
                {
                    case INVOICE:
                        SaveInvoice();
                        break;
                    case PROFORMA:
                        SaveProForm();
                        break;
                    case SKETCH:
                        SaveSketch();
                        break;
                }
                break;
            case ORIGINAL:
                if(type == COMPLETION_CERTIFICATE)
                {
                    SaveCC();
                }
                else
                {
                    System.err.println("Invoice.java/Save()/ORIGINAL");
                    System.err.println("    Itt nincs mentés, használd a COPY-t");
                }
                break;
        }
        
        if(type == SKETCH && invoiceType == InvoiceType.MODIFICATION)
        {}
        else if(invoiceType == InvoiceType.ORIGINAL)
        {}
        else
        {
            UpdateNumber();
        }
    }
    
    public void UpdateNumber()
    {
        String column = "";
        
        switch(type)
        {
            case Invoice.INVOICE:
                column = "invoiceQuantity";
                break;
            case Invoice.PROFORMA:
                column = "proFormaQuantity";
                break;
            case Invoice.SKETCH:
                if(invoiceType == InvoiceType.STORNO)
                {
                    column = "invoiceQuantity";
                }
                else
                {
                    column = "sketchQuantity";
                }
                break;
        }
        
        Object [] o = new Object[2];
        o[0] = column;
        o[1] = serializationID;

        NAV.db.insert("UPDATE " + table_invoiceNumber + " SET " + column + " = " + column + " + 1 WHERE id = " + serializationID, null, 0);
    }
    
    public void Delete(String indentifier)
    {
        this.indentifier = indentifier;
        
        switch(type)
        {
            case Invoice.INVOICE:
                DeleteInvoice();
                break;
            case Invoice.PROFORMA:
                DeleteProForm();
                break;
            case Invoice.SKETCH:
                DeleteSketch();
                break;
        }
    }
    
    private void DeleteInvoice()
    {
        String query = "DELETE FROM " + table_invoices + " WHERE indentifier = " + indentifier;
        NAV.db.insert(query, null, 0);

        for(int i = 0; i < products.Size(); i++)
        {
            InvoiceProduct product = products.Get(i);

            query = "DELETE FROM " + table_invoice_productFee + " WHERE productID = " + product.getId();
            NAV.db.insert(query, null, 0);
        }

        query = "DELETE FROM " + table_invoice_products + " WHERE indentifier = " + indentifier;
        NAV.db.insert(query, null, 0);
    }
    
    private void DeleteProForm()
    {
        String query = "DELETE FROM " + table_pro_forms + " WHERE indentifier = " + indentifier;
        NAV.db.insert(query, null, 0);

        for(int i = 0; i < products.Size(); i++)
        {
            InvoiceProduct product = products.Get(i);

            query = "DELETE FROM " + table_pro_form_productFee + " WHERE productID = " + product.getId();
            NAV.db.insert(query, null, 0);
        }

        query = "DELETE FROM " + table_pro_form_products + " WHERE indentifier = " + indentifier;
        NAV.db.insert(query, null, 0);

    }
    
    private void DeleteSketch()
    {
        String query = "DELETE FROM " + table_sketches + " WHERE indentifier = " + indentifier;
        NAV.db.insert(query, null, 0);

        for(int i = 0; i < products.Size(); i++)
        {
            InvoiceProduct product = products.Get(i);

            query = "DELETE FROM " + table_sketch_productFee + " WHERE productID = " + product.getId();
            NAV.db.insert(query, null, 0);
        }

        query = "DELETE FROM " + table_sketch_products + " WHERE indentifier = " + indentifier;
        NAV.db.insert(query, null, 0);
    }
    
    public void SaveInvoice()
    {
        String query = "INSERT INTO " + table_invoices + " ("
            + "indentifier, "
            + "invoiceNumber, "
            + "invoiceType, "
            + "correctedInvoice, "
            + "serializationID, "
            + "invoiceGroup, "
            + "supplierID, "
            + "supplierName, "
            + "supplierCountryCode, "
            + "supplierPostalCode, "
            + "supplierCity, "
            + "supplierStreet, "
            + "supplierPublicPlace, "
            + "supplierHouseNumber, "
            + "supplierTaxNumber, "
            + "supplierEuTaxNumber, "
            + "supplierBankAccountNumber, "
            + "supplierComment, "
            + "supplierInvoiceFooter, "
            + "customerID, "
            + "customerName, "
            + "customerCountryCode, "
            + "customerPostalCode, "
            + "customerCity, "
            + "customerStreet, "
            + "customerPublicPlace, "
            + "customerHouseNumber, "
            + "customerTelNumber, "
            + "customerEmail, "
            + "customerBankAccountNumber, "
            + "showInInvoice, "
            + "customerTaxNumber, "
            + "customerEuTaxNumber, "
            + "paymentMethod, "
            + "issueDate, "
            + "maturityDate, "
            + "completionDate, "
            + "paymentDate, "
            + "takeoverType, "
            + "comment, "
            + "footer, "
            + "printed, "
            + "foreignCurrency, "
            + "currency, "
            + "centralParity, "
            + "netPrice, "
            + "vatAmount) "
            //+ "navStatus, "
            //+ "transactionID, "
            //+ "timeStamp)"
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";//, ?, ?, ?)";
        
        Object [] values = new Object[/*50*/47];
        
        values[0] = indentifier;
        values[1] = invoiceNumber;
        
        switch(invoiceType)
        {
            case NEW:
                values[2] = "NEW";
                break;
            case COPY:
                values[2] = "NEW";
                break;
            case CORRECTION:
                values[2] = "CORRECTION";
                break;
            case MODIFICATION:
                values[2] = "MODIFICATION";
                break;
            case STORNO:
                values[2] = "STORNO";
                break;
        }
        
        values[3] = correctedInvoice;
        values[4] = serializationID;
        values[5] = invoiceGroup;
        values[6] = supplier.GetID();
        values[7] = supplier.getName();
        values[8] = supplier.getCountryCode();
        values[9] = supplier.getPostalCode();
        values[10] = supplier.getCity();
        values[11] = supplier.getStreet();
        values[12] = supplier.getPublicPlace();
        values[13] = supplier.getHouseNumber();
        values[14] = supplier.getTaxNumber();
        values[15] = supplier.getEuTaxNumber();
        values[16] = supplier.getBankAccountNumber();
        values[17] = supplier.getComment();
        values[18] = supplier.getInvoiceFooter();
        values[19] = customer.getId();
        values[20] = customer.getName();
        values[21] = customer.getCountryCode();
        values[22] = customer.getPostalCode();
        values[23] = customer.getCity();
        values[24] = customer.getStreet();
        values[25] = customer.getPublicPlace();
        values[26] = customer.getHouseNumber();
        values[27] = customer.getTelNumber();
        values[28] = customer.getEmail();
        values[29] = customer.getBankAccountNumber();
        values[30] = customer.getShowInInvoice();
        values[31] = customer.getTaxNumber();
        values[32] = customer.getEuTaxNumber();
        values[33] = paymentMethod;
        values[34] = issueDate;
        values[35] = maturityDate;
        values[36] = completionDate;
        
        if(paymentMethod == 0)
        {
            paymentDate = format.format(new Date());
            values[37] = paymentDate;
        }
        else
        {
            values[2] = "UNPAID";
            values[37] = "0000-00-00";
        }
        
        values[38] = takeoverType;
        values[39] = comment;
        values[40] = footer;
        values[41] = printed;
        values[42] = foreignCurrency;
        values[43] = currency;
        values[44] = centralParity;
        values[45] = netPrice;
        values[46] = vatAmount;
        
        //values[47] = navStatus;
        //values[48] = transactionID;
        //values[49] = timestamp;
        
        NAV.db.insert(query, values, /*50*/47);
        
        if(invoiceType == InvoiceType.CORRECTION || invoiceType == InvoiceType.MODIFICATION || invoiceType == InvoiceType.STORNO)
        {
            Object [] o = new Object[2];
            o[0] = "GOT" + values[2];
            o[1] = correctedInvoice;
            
            NAV.db.insert("UPDATE " + table_invoices + " SET invoiceType = ? WHERE invoiceNumber = ?", o, 2);
        }
        
        SaveProducts();
        
        GetNavStatus();
    }
    
    public void SaveProForm()
    {
        String query = "INSERT INTO " + table_pro_forms + " ("
            + "indentifier, "
            + "invoiceNumber, "
            + "invoiceType, "
            + "serializationID, "
            + "supplierID, "
            + "supplierName, "
            + "supplierCountryCode, "
            + "supplierPostalCode, "
            + "supplierCity, "
            + "supplierStreet, "
            + "supplierPublicPlace, "
            + "supplierHouseNumber, "
            + "supplierTaxNumber, "
            + "supplierEuTaxNumber, "
            + "supplierBankAccountNumber, "
            + "supplierComment, "
            + "supplierInvoiceFooter, "
            + "customerID, "
            + "customerName, "
            + "customerCountryCode, "
            + "customerPostalCode, "
            + "customerCity, "
            + "customerStreet, "
            + "customerPublicPlace, "
            + "customerHouseNumber, "
            + "customerTelNumber, "
            + "customerEmail, "
            + "customerBankAccountNumber, "
            + "showInInvoice, "
            + "customerTaxNumber, "
            + "customerEuTaxNumber, "
            + "paymentMethod, "
            + "issueDate, "
            + "maturityDate, "
            + "completionDate, "
            + "paymentDate, "
            + "takeoverType, "
            + "comment, "
            + "footer, "
            + "printed, "
            + "foreignCurrency, "
            + "currency, "
            + "centralParity, "
            + "netPrice, "
            + "vatAmount) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        Object [] values = new Object[45];
        
        values[0] = indentifier;
        values[1] = invoiceNumber;
        
        switch(invoiceType)
        {
            case NEW:
                values[2] = "NEW";
                break;
            case COPY:
                values[2] = "NEW";
                break;
            case CORRECTION:
                values[2] = "CORRECTION";
                break;
            case MODIFICATION:
                values[2] = "MODIFICATION";
                break;
            case STORNO:
                values[2] = "STORNO";
                break;
        }
        
        values[3] = serializationID;
        values[4] = supplier.GetID();
        values[5] = supplier.getName();
        values[6] = supplier.getCountryCode();
        values[7] = supplier.getPostalCode();
        values[8] = supplier.getCity();
        values[9] = supplier.getStreet();
        values[10] = supplier.getPublicPlace();
        values[11] = supplier.getHouseNumber();
        values[12] = supplier.getTaxNumber();
        values[13] = supplier.getEuTaxNumber();
        values[14] = supplier.getBankAccountNumber();
        values[15] = supplier.getComment();
        values[16] = supplier.getInvoiceFooter();
        values[17] = customer.getId();
        values[18] = customer.getName();
        values[19] = customer.getCountryCode();
        values[20] = customer.getPostalCode();
        values[21] = customer.getCity();
        values[22] = customer.getStreet();
        values[23] = customer.getPublicPlace();
        values[24] = customer.getHouseNumber();
        values[25] = customer.getTelNumber();
        values[26] = customer.getEmail();
        values[27] = customer.getBankAccountNumber();
        values[28] = customer.getShowInInvoice();
        values[29] = customer.getTaxNumber();
        values[30] = customer.getEuTaxNumber();
        values[31] = paymentMethod;
        values[32] = issueDate;
        values[33] = maturityDate;
        values[34] = completionDate;
        
        if(paymentMethod == 0)
        {
            paymentDate = format.format(new Date());
            values[35] = paymentDate;
        }
        else
        {
            values[35] = "0000-00-00";
        }
        
        values[36] = takeoverType;
        values[37] = comment;
        values[38] = footer;
        values[39] = printed;
        values[40] = foreignCurrency;
        values[41] = currency;
        values[42] = centralParity;
        values[43] = netPrice;
        values[44] = vatAmount;
        
        NAV.db.insert(query, values, values.length);
        
        SaveProducts();
    }
    
    public void SaveSketch()
    {
        String query = "INSERT INTO " + table_sketches + " ("
            + "indentifier, "
            + "invoiceNumber, "
            + "serializationID, "
            + "supplierID, "
            + "customerName, "
            + "customerCountryCode, "
            + "customerPostalCode, "
            + "customerCity, "
            + "customerStreet, "
            + "customerPublicPlace, "
            + "customerHouseNumber, "
            + "customerTelNumber, "
            + "customerEmial, "
            + "customerBankAccountNumber, "
            + "showInInvoice, "
            + "customerTaxNumber, "
            + "customerEuTaxNumber, "
            + "paymentMethod, "
            + "takeoverType, "
            + "currency, "
            + "centralParity, "
            + "foreignCurrency, "
            + "netPrice, "
            + "vatAmount, "
            + "comment, "
            + "footer) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        Object [] values = new Object[26];
        
        values[0] = indentifier;
        values[1] = invoiceNumber;
        values[2] = serializationID;
        values[3] = supplier.getId();
        values[4] = customer.getName();
        values[5] = customer.getCountryCode();
        values[6] = customer.getPostalCode();
        values[7] = customer.getCity();
        values[8] = customer.getStreet();
        values[9] = customer.getPublicPlace();
        values[10] = customer.getHouseNumber();
        values[11] = customer.getTelNumber();
        values[12] = customer.getEmail();
        values[13] = customer.getBankAccountNumber();
        values[14] = customer.getShowInInvoice();
        values[15] = customer.getTaxNumber();
        values[16] = customer.getEuTaxNumber();
        values[17] = paymentMethod;
        values[18] = takeoverType;
        values[19] = currency;
        values[20] = centralParity;
        values[21] = foreignCurrency;
        values[22] = netPrice;
        values[23] = vatAmount;
        values[24] = comment;
        values[25] = footer;
        
        NAV.db.insert(query, values, 26);
        
        SaveProducts();
    }
    
    public void UpdateSketch()
    {
        String query = "UPDATE " + table_sketches + " SET "
            + "indentifier = ?, "
            + "invoiceNumber = ?, "
            + "serializationID = ?, "
            + "supplierID = ?, "
            + "customerName = ?, "
            + "customerCountryCode = ?, "
            + "customerPostalCode = ?, "
            + "customerCity = ?, "
            + "customerStreet = ?, "
            + "customerPublicPlace = ?, "
            + "customerHouseNumber = ?, "
            + "customerTelNumber = ?, "
            + "customerEmail = ?, "
            + "customerBankAccountNumber = ?, "
            + "showInInvoice = ?, "
            + "customerTaxNumber = ?, "
            + "customerEuTaxNumber = ?, "
            + "paymentMethod = ?, "
            + "takeoverType = ?, "
            + "currency = ?, "
            + "centralParity = ?, "
            + "foreignCurrency = ?, "
            + "netPrice = ?, "
            + "vatAmount = ?, "
            + "comment = ?, "
            + "footer = ? "
            + "WHERE indentifier = " + indentifier;
        
        Object [] values = new Object[26];
        
        values[0] = indentifier;
        values[1] = invoiceNumber;
        values[2] = serializationID;
        values[3] = supplier.getId();
        values[4] = customer.getName();
        values[5] = customer.getCountryCode();
        values[6] = customer.getPostalCode();
        values[7] = customer.getCity();
        values[8] = customer.getStreet();
        values[9] = customer.getPublicPlace();
        values[10] = customer.getHouseNumber();
        values[11] = customer.getTelNumber();
        values[12] = customer.getEmail();
        values[13] = customer.getBankAccountNumber();
        values[14] = customer.getShowInInvoice();
        values[15] = customer.getTaxNumber();
        values[16] = customer.getEuTaxNumber();
        values[17] = paymentMethod;
        values[18] = takeoverType;
        values[19] = currency;
        values[20] = centralParity;
        values[21] = foreignCurrency;
        values[22] = netPrice;
        values[23] = vatAmount;
        values[24] = comment;
        values[25] = footer;
        
        NAV.db.insert(query, values, 26);
        
        UpdateSketchProducts();
    }
    
    private void UpdateSketchProducts()
    {
        Query query = new Query.QueryBuilder()
            .select("id")
            .from(table_sketch_products)
            .where("indentifier = " + indentifier)
            .build();
        Object[][] productIDs = NAV.db.select(query.getQuery());
        
        for(int i = 0; i < productIDs.length; i++)
        {
            boolean stay = false;
            int productID = Integer.parseInt(productIDs[i][0].toString());
            
            for(int j = 0; j < products.Size(); j++)
            {
                InvoiceProduct product = products.Get(j);
                
                if(productID == product.getId())
                {
                    stay = true;
                    product.Update();
                }
            }
            
            if(!stay)
            {
                DeleteSketchProduct(productID);
            }
        }
    }
    
    private void DeleteSketchProduct(int productID)
    {
        String query = "DELETE FROM " + table_sketch_productFee + " WHERE productID = " + productID;
        
        NAV.db.insert(query, null, 0);
        
        query = "DELETE FROM " + table_sketch_products + " WHERE id = " + productID;
        
        NAV.db.insert(query, null, 0);
    }
    
    public void SaveCC()
    {
        String query = "INSERT INTO " + table_CC + " ("
            + "tig_sorszam, "
            + "szamla_sorszam, "
            + "teljesites_igazolas_kelte) "
            + "VALUES (?, ?, ?)";
        
        Object [] values = new Object[3];
        values[0] = invoiceNumber;
        values[1] = invoiceNumber .replaceAll("TIG ", "");
        values[2] = format.format(new Date());
        
        NAV.db.insert(query, values, values.length);
    }
    
    private void SaveProducts()
    {
        for(int i = 0; i < products.Size(); i++)
        {
            InvoiceProduct product = products.Get(i);
            product.setTable_product(productTable);
            product.setTable_productFee(productFeeTable);
            product.Save(indentifier, invoiceNumber, paymentMethod, customer.getName(), isForeignCurrency());
        }
    }
    
    public void UpdateTransactionId()
    {
        String query = "UPDATE " + fromTable + " SET "
            + "transactionID = '" + transactionID + "' "
            + "WHERE indentifier = " + indentifier;
        NAV.db.insert(query, null, 0);
    }
    
    public void UpdateStatus()
    {
        String query = "UPDATE " + fromTable + " SET "
            + "navStatus = '" + navStatus + "' "
            + "WHERE indentifier = " + indentifier;
        NAV.db.insert(query, null, 0);
    }
    
    public void GetNavStatus()
    {
        double Vat = getProducts().getTotalVat(InvoiceProducts.PRODUCT, foreignCurrency);
        double VatFee = getProducts().getTotalVat(InvoiceProducts.PRODUCTFEE, foreignCurrency);
        double vatLimit = GetVatLimit();
        double totalVat = Vat + VatFee;
        //double totalVat = 1000000000;
        
        /*if(invoiceType == InvoiceType.STORNO)
        {
            totalVat *= -1;
        }*/
        
        if(totalVat < 0)
        {
            totalVat *= -1;
        }
        
        if(totalVat >= vatLimit)
        {
            NAV nav = new NAV(this, userId);
            String status = nav.getStatus();
            String statusStr;
            
            switch(status)
            {
                case "RECEIVED":
                    navStatus = "1";
                    statusStr = "A számla adatszolgáltatás befogadásra került!";
                    break;
                case "PROCESSING":
                    navStatus = "2";
                    statusStr = "A számla adatszolgáltatás feldolgozása megkezdődött!";
                    break;
                case "DONE":
                    navStatus = "3";
                    statusStr = "A számla adatszolgáltatás feldolgozása sikeresen befejeződött!";
                    break;
                case "ABORTED":
                    navStatus = "4";
                    statusStr = "A számla adatszolgáltatás feldolgozása sikertelen volt!";
                    break;
                default:
                    navStatus = "0";
                    statusStr = "Invoice.java/GetNavStatus()/navstatus = 0";
                    UserMessage message = new UserMessage("Számla státusza", statusStr);
                    break;
            }
        }
        else
        {
            //String messageTitle = "Áfa összeg határérték alatt";
            String description = "A számla áfa összege nem haladja meg az adatbázisban megadott értéket (" + vatLimit + " Ft)";
            //UserMessage message = new UserMessage(messageTitle, description);
            XmlLogger.addLog(this.getClass().getName(), description);
            
            navStatus = "0";
        }
        
        UpdateStatus();
    }
    
    private double GetVatLimit()
    {
        Query query = new Query.QueryBuilder()
            .select("vatLimit")
            .from(Supplier.TABLE)
            .where("id LIKE '" + supplier.getId() + "'")
            .build();
        Object [][] vatLimit = NAV.db.select(query.getQuery());
        
        return Double.parseDouble(vatLimit[0][0].toString());
    }
    
    private String GetLastModificationReference()
    {
        Query query = new Query.QueryBuilder()
            .select("invoiceNumber")
            .from(table_invoices)
            .where("correctedInvoice = '" + correctedInvoice + "'")
            .order("id DESC")
            .build();
        Object [][] numbers = NAV.db.select(query.getQuery());
        
        if(numbers.length != 0)
        {
            return numbers[0][0].toString();
        }
        else
        {
            return "";
        }
    }
    
    private String GetModifyWithoutMaster()
    {
        Query query = new Query.QueryBuilder()
            .select("navStatus")
            .from(table_invoices)
            .where("invoiceNumber = '" + correctedInvoice + "'")
            .build();
        Object [][] status = NAV.db.select(query.getQuery());
        
        if(Integer.parseInt(status[0][0].toString()) == 0)
        {
            return "true";
        }
        else
        {
            return "false";
        }
    }
    
    public String getNumber(int type, InvoiceType invoiceType)
    {
        String selectText = "";
        
        switch(type)
        {
            case(Invoice.INVOICE):
                selectText = "invoiceQuantity,";
                break;
            case(Invoice.PROFORMA):
                selectText = "proFormaQuantity,";
                break;
            case(Invoice.SKETCH):
                if(invoiceType == InvoiceType.STORNO)
                {
                    selectText = "invoiceQuantity,";
                }
                else
                {
                    selectText = "sketchQuantity,";
                }
                break;
        }
        
        Query query = new Query.QueryBuilder()
            .select(selectText + " year, prefix")
            .from(table_invoiceNumber)
            .where("id = " + serializationID + "")
            .build();
        Object [][] number = NAV.db.select(query.getQuery());

        String returnNumber = String.valueOf(number[0][1]) + "/" + (Integer.parseInt(String.valueOf(number[0][0])) + 1);

        if (!String.valueOf(number[0][2]).isEmpty())
        {
            returnNumber = String.valueOf(number[0][2]) + " " + returnNumber;
        }
        
        return returnNumber;
    }
    
    public static Object[][] getLastSupplierInvoiceIssueDate(int supplierID)
    {
        Query query = new Query.QueryBuilder()
            .select("issueDate")
            .from(table_invoices)
            .where("serializationID = (SELECT serializationID FROM " + Supplier.TABLE + " WHERE id = " + supplierID + ")")
            .order("id DESC LIMIT 1")
            .build();
        Object [][] lastIssueDate = NAV.db.select(query.getQuery());
        
        return lastIssueDate;
    }
    
    //GETTERS
    public int getType() {
        return type;
    }

    public InvoiceType getInvoiceType() {
        return invoiceType;
    }

    public int getSerializationID() {
        return serializationID;
    }

    public String getIndentifier() {
        return indentifier;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public String getCorrectedInvoice() {
        return correctedInvoice;
    }

    public String getInvoiceGroup() {
        return invoiceGroup;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public Customer getCustomer() {
        return customer;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public String getMaturityDate() {
        return maturityDate;
    }

    public String getCompletionDate() {
        return completionDate;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public InvoiceProducts getProducts() {
        return products;
    }

    public int getPaymentMethod() {
        return paymentMethod;
    }
    
    public String getPaymentMethodStr()
    {
        String method = "";
        
        switch(paymentMethod)
        {
            case 0:
                method = "CASH";
                break;
            case 1:
                method = "TRANSFER";
                break;
            case 2:
                method = "OTHER";
                break;
        }
        
        return method;
    }

    public String getCurrency() {
        return currency;
    }

    public double getCentralParity() {
        return centralParity;
    }

    public String getTakeoverType() {
        return takeoverType;
    }

    public double getNetPrice() {
        return netPrice;
    }

    public double getVatAmount() {
        return vatAmount;
    }

    public List<Afa> getVats(boolean productFee)
    {
        vats = new LinkedList<>();
        
        for(int i = 0; i < products.Size(); i++)
        {
            InvoiceProduct product = products.Get(i);
            int index = findVat(product.getVatPercent());
            
            if (index < 0)
            {
                // még nincs
                vats.add(new Afa(product.getVatPercent(), product.getNetPrice(isForeignCurrency())));
            }
            else
            {
                vats.get(index).addNetto(product.getNetPrice(isForeignCurrency()));
            }
            
            if (productFee && product.getProductFee() != null && !takeoverType.equals(""))
            {
                index = findVat(27);
                
                if (index < 0)
                {
                    // még nincs
                    vats.add(new Afa(27.0, product.getProductFee().getOsszTermekDijNetto(foreignCurrency)));
                }
                else
                {
                    vats.get(index).addNetto(product.getProductFee().getOsszTermekDijNetto(foreignCurrency));
                }
            }
        }
        
        return vats;
    }
    
    private int findVat(int vat)
    {
        for (int i = 0; i < vats.size(); i++)
        {
            if (vats.get(i).getAfa() == vat)
            {
                return i;
            }
        }
        
        return -1;
    }
    
    public ArrayList<String> getVatLabels()
    {
        vatLabels = new ArrayList<>();
        
        for(int i = 0; i < products.Size(); i++)
        {
            InvoiceProduct product = products.Get(i);
            int index = findLabel(product.getVatLabel());
            
            if(index < 0)
            {
                vatLabels.add(product.getVatLabel());
            }
        }
        
        return vatLabels;
    }
    
    private int findLabel(String vatLabel)
    {
        for(int i = 0; i < vatLabels.size(); i++)
        {
            if(vatLabels.get(i).equals(vatLabel))
            {
                return i;
            }
        }
        
        return -1;
    }

    public String getComment() {
        return comment;
    }

    public String getFooter() {
        return footer;
    }

    public boolean isForeignCurrency() {
        return foreignCurrency;
    }

    public int getPrinted() {
        return (printed ? 1 : 0);
    }

    public String getNavStatus() {
        return navStatus;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public String getFromTable() {
        return fromTable;
    }

    public String getToTable() {
        return toTable;
    }
    
    /*public double getTotal()
    {
        double total = 0.0;
        
        for(int i = 0; i < products.Size(); i++)
        {
            InvoiceProduct product = products.Get(i);
            total += product.getGrossPrice(foreignCurrency);
            
            if(takeoverType.isEmpty())
            {
                total += (product.getProductFee() != null? product.getProductFee().getOsszTermekDijBrutto(foreignCurrency) : 0);
            }
        }
        
        return total;
    }*/
    
    public int getTotal()
    {
        int total = 0;
        
        for(int i = 0; i < products.Size(); i++)
        {
            InvoiceProduct product = products.Get(i);

            int productGrossPrice = product.getGrossPrice(foreignCurrency);
            //System.err.println("ProductGrossPrice: " + productGrossPrice);
            total += productGrossPrice;
            
            if(takeoverType.isEmpty() && product.getProductFee() != null)
            {
                total += product.getProductFee().getOsszTermekDijBrutto(foreignCurrency);
            }
        }
        
        return total;
    }
    
    public int getTotalNet()
    {
        int total = 0;
        
        for(int i = 0; i < products.Size(); i++)
        {
            InvoiceProduct product = products.Get(i);

            int productGrossPrice = product.getNetPrice(foreignCurrency);
            //System.err.println("ProductGrossPrice: " + productGrossPrice);
            total += productGrossPrice;
            
            if(takeoverType.isEmpty() && product.getProductFee() != null)
            {
                total += product.getProductFee().getOsszTermekDijBrutto(foreignCurrency);
            }
        }
        
        return total;
    }
    
    public int getTotalVat()
    {
        int total = 0;
        
        for(int i = 0; i < products.Size(); i++)
        {
            InvoiceProduct product = products.Get(i);

            int productGrossPrice = product.getVatAmount(foreignCurrency);
            //System.err.println("ProductGrossPrice: " + productGrossPrice);
            total += productGrossPrice;
            
            if(takeoverType.isEmpty() && product.getProductFee() != null)
            {
                total += product.getProductFee().getOsszTermekDijBrutto(foreignCurrency);
            }
        }
        
        return total;
    }
    
    public double getRefund()
    {
        return Math.abs(getTotal());
    }
    
    
    //SETTERS
    public void setType(int type) {
        this.type = type;
    }

    public void setInvoiceType(InvoiceType invoiceType) {
        this.invoiceType = invoiceType;
    }

    public void setSerializationID(int serializationID) {
        this.serializationID = serializationID;
    }

    public void setIndentifier(String indentifier) {
        this.indentifier = indentifier;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public void setCorrectedInvoice(String correctedInvoice) {
        this.correctedInvoice = correctedInvoice;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public void setMaturityDate(String maturityDate) {
        this.maturityDate = maturityDate;
    }

    public void setCompletionDate(String completionDate) {
        this.completionDate = completionDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public void setProducts(InvoiceProducts invoiceProducts) {
        this.products = invoiceProducts;
    }

    public void setPaymentMethod(int paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setCentralParity(double centralParity) {
        this.centralParity = centralParity;
    }

    public void setTakeoverType(String takeoverType) {
        this.takeoverType = takeoverType;
    }

    public void setNetPrice(double netPrice) {
        this.netPrice = netPrice;
    }

    public void setVatAmount(double vatAmount) {
        this.vatAmount = vatAmount;
    }

    public void setVats(List<Afa> vats) {
        this.vats = vats;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public void setForeignCurrency(boolean foreignCurrency) {
        this.foreignCurrency = foreignCurrency;
    }

    public void setPrinted(boolean printed) {
        this.printed = printed;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public void setStatus(String navStatus) {
        this.navStatus = navStatus;
    }
}