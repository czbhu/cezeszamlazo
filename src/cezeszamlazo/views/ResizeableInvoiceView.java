package cezeszamlazo.views;

import cezeszamlazo.App;
import cezeszamlazo.CalendarDialog;
import cezeszamlazo.ElonezetDialog;
import cezeszamlazo.EncodeDecode;
import cezeszamlazo.FolyamatbanDialog;
import cezeszamlazo.HibaDialog;
import cezeszamlazo.Label;
import cezeszamlazo.PrinterGetSet;
import cezeszamlazo.TermekListaDialog;
import cezeszamlazo.UgyfelListaDialog;
import cezeszamlazo.VtszTeszorListaDialog;
import cezeszamlazo.database.Query;
import cezeszamlazo.controller.Functions;
import cezeszamlazo.controller.Takeover;
import cezeszamlazo.controller.Valuta;
import cezeszamlazo.functions.Formatter;
import cezeszamlazo.model.Background;
import cezeszamlazo.model.PopupTimer;
import controller.ReturnStatus;
import customs.CustomButton;
import invoice.Afa;
import invoice.Customer;
import invoice.Invoice;
import invoice.InvoiceNumber;
import invoice.InvoiceProduct;
import invoice.InvoiceProducts;
import invoice.MeasureOfUnit;
import invoice.ProductFee;
import invoice.Supplier;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ItemEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.prefs.Preferences;
import javax.imageio.ImageIO;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import javax.swing.table.DefaultTableModel;
import view.ProductFeesView;

/**
 * @author Tomy
 */

public class ResizeableInvoiceView extends javax.swing.JDialog
{
    public static final int RET_OK = 1, RET_CANCEL = 0, RET_REOPEN = 2;
    int returnStatus;
    
    FocusThread focusThread;
    
    Background background;
    String selectedPanel = "header";
    final ResizeThread resizeThread = new ResizeThread();
    Image selected;
    Image unselected;

    Invoice invoice;
    
    CustomerView customerView;
    SupplierView supplierView;
    
    final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    
    InvoiceProduct currentProduct = new InvoiceProduct();
    
    //TermekDij productfee;
    
    boolean init = false;
    boolean comboboxUpdate = false;
    
    boolean applyNewCurrency = false;
    String currency = "HUF";
    String oldCurrency = currency;
    String newCurrency;
    double centralParity = 1.0;
    String oldCentralParity = String.valueOf(centralParity);
    String newCentralParity = oldCentralParity;
    
    FolyamatbanDialog dialog_InProgress;
    
    String [] tooltipsTakeover;
    String [] tooltipsVat;
    String [] tooltipsMeasureOfUnits;
    
    PopupTimer popupTimer;
    
    public static final int DEFAULT_WIDTH = 300;
    public static final int DEFAULT_HEIGHT = 730;
    
    Preferences root = Preferences.userRoot();
    final Preferences node = root.node(this.getClass().getName());
    
    public ResizeableInvoiceView(int type)
    {
        initComponents();
        
        invoice = new Invoice(type);
        invoice.setInvoiceType(Invoice.InvoiceType.NEW);
        
        Init();
    }
    
    public ResizeableInvoiceView(Invoice invoice)
    {
        initComponents();
        
        this.invoice = invoice;
        
        Init();
    }
    
    public ResizeableInvoiceView(int type, Invoice.InvoiceType invoiceType, String table, String indentifier)
    {
        initComponents();
        
        invoice = new Invoice(type, invoiceType, table, indentifier);
        
        Init();
    }
    
    private void Init()
    {
        init = true;
        
        if(invoice.getType() == Invoice.PROFORMA)
        {
            checkBox_Print.setSelected(false);
            textField_Copy.setText("1");
        }
        
        button_CustomerDropdown.doClick();
        button_SummaryDropdown.doClick();
        button_ProductSelect.setVisible(false);
        
        textField_CustomerName.setText(invoice.getCustomer().getName());
        
        panel_HeaderAndProductDatas.setVisible(true);
        panel_CommentAndFooter.setVisible(false);
        panel_CommentAndFooter.setSize(panel_HeaderAndProductDatas.getWidth(), panel_HeaderAndProductDatas.getHeight());
        
        setModal(true);
        
        int left = node.getInt("left", 0);
        int top = node.getInt("top", 0);
        int width = node.getInt("width", DEFAULT_WIDTH);
        int height = node.getInt("height", DEFAULT_HEIGHT);
        setBounds(left, top, width, height);
        
        dialog_ChangeCurrency.setSize((int) Math.round(dialog_ChangeCurrency.getPreferredSize().getWidth() + 15), (int) Math.round(dialog_ChangeCurrency.getPreferredSize().getHeight() + 35));
        dialog_Summary.setSize((int) Math.round(dialog_Summary.getPreferredSize().getWidth() + 15), (int) Math.round(dialog_Summary.getPreferredSize().getHeight() + 35));
        
        background = new Background("cezeszamlazo/resources/images/szamlazo_hatter.png");
        panel_background.setImage(background.getImage());
        
        try
        {
            selected = ImageIO.read(ClassLoader.getSystemResource(("cezeszamlazo/resources/images/selected.png")));
            selected.getScaledInstance(36, 180, Image.SCALE_SMOOTH);
            imagePanel_HeaderAndProductDatas.setImage(selected);
            
            unselected = ImageIO.read(ClassLoader.getSystemResource(("cezeszamlazo/resources/images/notSelected.png")));
            unselected.getScaledInstance(36, 180, Image.SCALE_SMOOTH);
            imagePanel_CommentAndFooter.setImage(unselected);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        
        SetInvoiceTitle();
        UpdateCurrencyComboBox();
        UpdateSupplierComboBox();
        
        label_InvoiceNumber.setText(invoice.getInvoiceNumber());
        label_IssueDate.setText(invoice.getIssueDate());
        label_MaturityDate.setText(invoice.getMaturityDate());
        label_CompletionDate.setText(invoice.getCompletionDate());
        
        
        UpdateVatComboBox();
        tooltipsVat = Afa.getVatTooltips();
        comboBox_Vat.setRenderer(new VatTooltip());
        UpdateMeasureOfUnitComboBox();
        tooltipsMeasureOfUnits = MeasureOfUnit.getTooltips();
        combobox_MeasureOfUnit.setRenderer(new MeasureOfUnitTooltip());
        
        if (App.args.length > 0)
        {
            int customerID = Integer.parseInt(App.args[1]);
            
            Customer customer = new Customer(customerID);
            customerView = new CustomerView(customer);
            invoice.setCustomer(customer);
            textField_CustomerName.setText(customer.getName());
            
            UpdateMaturityDate();
            
            comboBox_PaymentMethod.setSelectedIndex(customer.getPaymentMethod());
            comboBox_PaymentMethod.setEnabled(!customer.getPaymentMethodRequired());
            
            InvoiceProduct product;
            ProductFee fee;
            Object[][] select;
            
            for (int i = 2; i < App.args.length; i++)
            {
                Query query = new Query.QueryBuilder()
                    .select("name, "
                        + "measureOfUnit, "
                        + "(SELECT purchasePrice * multiplier FROM pixi_prices WHERE id = priceId), "
                        + "quantity, "
                        + "id")
                    .from("pixi_orders")
                    .where("id = " + App.args[i])
                    .build();
                //System.err.println(query);
                select = App.pixi.select(query.getQuery());

                String productName = String.valueOf(select[0][0]).replace(";", "");
                String itemNumber = select[0][4].toString();
                double quantity = Double.parseDouble(String.valueOf(select[0][3]));
                //String amountUnits = select[0][1].toString();
                MeasureOfUnit mou = new MeasureOfUnit(select[0][1].toString());
                int discount = 0;
                String vtszTeszor = "";
                double unitPrice = Double.parseDouble(select[0][2].toString());

                Label selectedVat = (Label) comboBox_Vat.getItemAt(0);
                String vatLabel = selectedVat.getName();
                double vatAmount = Double.parseDouble(selectedVat.getId());

                product = new InvoiceProduct(productName, itemNumber, quantity, mou, discount, vtszTeszor, unitPrice, vatLabel, vatAmount);

                query = new Query.QueryBuilder()
                    .select("nev, termekdij, IF(osszsuly > 0,osszsuly, egysegsuly * peldany), csk, kt")
                    .from("pixi_ajanlatkeresek_termekdij")
                    .where("orderId = " + App.args[i])
                    .build();
                System.err.println(query);
                select = App.pixi.select(query.getQuery());

                for(int k = 0; k < select.length; k++)
                {
                    fee = new ProductFee();
                    fee.setName(select[k][0].toString());
                    fee.setKgPrice(Double.parseDouble(select[k][1].toString()));
                    fee.setWeight(Double.parseDouble(select[k][2].toString()));

                    String csk = select[k][3].toString();
                    String kt = select[k][4].toString();

                    if(!csk.isEmpty())
                    {
                        fee.setType(ProductFee.Type.PACKAGING);
                        fee.setCode(csk);
                    }

                    if(!kt.isEmpty())
                    {
                        fee.setType(ProductFee.Type.ADVERTISING);
                        fee.setCode(kt);
                    }

                    product.AddProductFee(fee);
                }

                invoice.getProducts().Add(product);
            }
        }
        
        if(!invoice.getTakeoverType().isEmpty())
        {
            checkBox_Takeover.doClick();
            
            for(int i = 0; i < comboBox_TakeoverType.getItemCount(); i++)
            {
                if(invoice.getTakeoverType().equals(comboBox_TakeoverType.getItemAt(i).toString()))
                {
                    comboBox_TakeoverType.setSelectedIndex(i);
                    break;
                }
            }
        }
        
        comboBox_PaymentMethod.setSelectedIndex(invoice.getCustomer().getPaymentMethod());
        UpdateInvoiceProducts(Double.parseDouble(oldCentralParity), Double.parseDouble(newCentralParity));
        init = false;
        
        setVisible(true);
    }
    
    private void Open(Object dialog, String title)
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
    
    private void Close(int status)
    {
        returnStatus = status;
        
        node.putInt("left", getX());
        node.putInt("top", getY());
        node.putInt("width", getWidth());
        node.putInt("height", getHeight());
        
        setVisible(false);
        dispose();
    }
    
    private void SetInvoiceTitle()
    {
        String prefix = "";
        String title = "";
        
        switch(invoice.getType())
        {
            case Invoice.INVOICE:
                title = "Számla";
                break;
            case Invoice.PROFORMA:
                title = "Díjbekérő";
                break;
            case Invoice.SKETCH:
                title = "Vázlat";
                break;
            case Invoice.COMPLETION_CERTIFICATE:
                title = "Teljesítés igazolás";
                break;
        }
        
        switch(invoice.getInvoiceType())
        {
            case NEW:
                prefix = "Új";
                break;
            case COPY:
                prefix = "Új";
                break;
            case CORRECTION:
                prefix = "Helyesbítő";
                break;
            case MODIFICATION:
                prefix = "Módosító";
                break;
            case STORNO:
                prefix = "Stornó";
                break;
        }
        
        setTitle(prefix + " " + title);
        label_InvoiceTitle.setText(prefix + " " + title);
    }
    
    private void UpdateSupplierComboBox()
    {
        comboboxUpdate = true;
        comboBox_Supplier.removeAllItems();
        
        String [] companyIDs = App.user.getCeg().split(";");

        Object [][] serializationIDs = Supplier.getSerializationIDsByIDs(companyIDs);
        Object [][] suppliers = Supplier.getSuppliers(serializationIDs);
        
        for (Object[] obj : suppliers)
        {
            String str = String.valueOf(obj[1]);
            
            if (str.length() > 40)
            {
                str = str.substring(0, 37) + "...";
            }

            Label label = new Label(String.valueOf(obj[0]), str);
            comboBox_Supplier.addItem(label);
        }
        
        comboboxUpdate = false;
        
        Object [][] defaultSupplier;
        
        if(invoice.getInvoiceType() == Invoice.InvoiceType.NEW || invoice.getType() == Invoice.SKETCH)
        {
            defaultSupplier = Supplier.getDefaultSupplierByUserID(App.user.getId());
        }
        else
        {
            defaultSupplier = Supplier.getDefaultSupplier(invoice.getFromTable(), invoice.getIndentifier());
        }
        
        for(int i = 0; i < comboBox_Supplier.getItemCount(); i++)
        {
            Label l =(Label) comboBox_Supplier.getItemAt(i);

            if(l.getName().equals(defaultSupplier[0][1].toString()))
            {
                if(i == 0)
                {
                    comboboxUpdate = true;
                    comboBox_Supplier.setSelectedIndex(i + 1);
                    comboboxUpdate = (invoice.getInvoiceType() == Invoice.InvoiceType.ORIGINAL);
                }
                
                comboBox_Supplier.setSelectedIndex(i);
                comboboxUpdate = false;
                break;
            }
        }
    }
    
    private void UpdateCurrencyComboBox()
    {
        comboboxUpdate = true;
        comboBox_Currency.removeAllItems();
        
        Object [][] currencies = Valuta.getCurrencies();
        
        for(int i = 0; i < currencies.length; i++)
        {
            currency = currencies[i][0].toString();
            centralParity = Double.parseDouble(currencies[i][1].toString());
            comboBox_Currency.addItem(new Label(String.valueOf(centralParity), currency));
        }
        
        currency = currencies[0][0].toString();
        centralParity = Double.parseDouble(currencies[0][1].toString());
        
        comboboxUpdate = false;
        
        for(int i = 0; i < comboBox_Currency.getItemCount(); i++)
        {
            Label l = (Label) comboBox_Currency.getItemAt(i);
            
            if(l.getName().equals(invoice.getCurrency().toUpperCase()))
            {
                comboBox_Currency.setSelectedIndex(i);
                
                currency = l.getName();
                centralParity = Double.parseDouble(l.getId());
                newCurrency = l.getName();
                newCentralParity = l.getId();
                oldCurrency = l.getName();
                oldCentralParity = l.getId();
            }
        }
    }
    
    private void UpdateMeasureOfUnitComboBox()
    {
        comboboxUpdate = true;
        combobox_MeasureOfUnit.removeAllItems();
        
        Object [][] measureOfUnits = MeasureOfUnit.getMeasureOfUnits();
        
        for(int i = 0; i < measureOfUnits.length; i++)
        {
            String shortName = measureOfUnits[i][0].toString();
            
            combobox_MeasureOfUnit.addItem(shortName);
        }
        
        combobox_MeasureOfUnit.setSelectedItem("db");
        
        comboboxUpdate = false;
    }
    
    private void UpdateVatComboBox()
    {
        comboboxUpdate = true;
        comboBox_Vat.removeAllItems();
        
        Object [][] vats = Afa.getVats();
        
        for(int i = 0; i < vats.length; i++)
        {
            comboBox_Vat.addItem(new Label(vats[i][1].toString(), vats[i][0].toString()));
            //System.err.println(vats[i][1].toString() + " " + vats[i][0].toString());
        }
        
        comboboxUpdate = false;
    }
    
    private void UpdatePrinterComboBox()
    {
        comboboxUpdate = true;
        comboBox_Printer.removeAllItems();
        
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        for (PrintService printer : printServices)
        {
            comboBox_Printer.addItem(String.valueOf(printer.getName()));
        }
        PrintService service = PrintServiceLookup.lookupDefaultPrintService();
        comboBox_Printer.getModel().setSelectedItem(String.valueOf(service.getName()));
        
        comboboxUpdate = false;
        
        PrinterGetSet printer = new PrinterGetSet();
        printer.setPrinterName(String.valueOf(comboBox_Printer.getSelectedItem()));
    }
    
    private void UpdateMaturityDate()
    {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        String maturityDate;
        
        if(invoice.getCustomer().getMaturityDays() < 1)
        {
            int defaultMaturityDays = invoice.getSupplier().getDefaultMaturityDays();
            c.add(Calendar.DATE, defaultMaturityDays);
            maturityDate = format.format(c.getTime());
        }
        else
        {
            int maturityDays = invoice.getCustomer().getMaturityDays();
            c.add(Calendar.DATE, maturityDays);
            maturityDate = format.format(c.getTime());
        }
        
        label_MaturityDate.setText(maturityDate);
    }

    private void NumberVerification()
    {
        Calendar calendar = Calendar.getInstance();
        Label selectedSupplier = (Label) comboBox_Supplier.getSelectedItem();
        int supplierID = Integer.parseInt(selectedSupplier.getId());
        
        Object [][] year = InvoiceNumber.getYear(supplierID);
        
        if(year.length != 0 && Integer.parseInt(String.valueOf(year[0][1])) < calendar.get(Calendar.YEAR))
        {
            String id = year[0][0].toString();
            InvoiceNumber.UpdateYear(id, invoice.getType());
        }
        else
        {
            Object [][] lastIssueDate = Invoice.getLastSupplierInvoiceIssueDate(supplierID);
            
            if (lastIssueDate.length != 0)
            {
                String[] date = String.valueOf(lastIssueDate[0][0]).split("-");

                int e = Integer.parseInt(date[0]);
                int h = Integer.parseInt(date[1]);
                int n = Integer.parseInt(date[2]);
                
                if (calendar.get(Calendar.YEAR) < e || (calendar.get(Calendar.YEAR) == e && calendar.get(Calendar.MONTH) + 1 < h) || (calendar.get(Calendar.YEAR) == e && calendar.get(Calendar.MONTH) + 1 == h && calendar.get(Calendar.DATE) < n))
                {
                    HibaDialog hd = new HibaDialog("A legutóbbi számla kelte nagyobb, mint a beállított rendszeridő! (ResizeableInvoiceView/NumberVerification()))", "Ok", "");
                }
            }
        }
    }
    
    /*private void CalculateByQuantity()
    {
        double quantity = getQuantity();
        double unitPrice = getUnitPrice();
        Label vatLabel = (Label) comboBox_Vat.getSelectedItem();
        
        double vatAmount = Double.parseDouble(vatLabel.getId()) / 100.0 * quantity * unitPrice;
        
        if(invoice.isForeignCurrency())
        {
            double net = Math.round(quantity * unitPrice * 10000.0) / 10000.0;
            double gross = Math.round((net + vatAmount) * 100) / 100.0;
            
            textField_NetPrice.setText(net + "");
            textField_GrossPrice.setText(gross + "");
        }
        else
        {
            int net = (int) (Math.round(quantity * unitPrice * 100) / 100.0);
            int gross = (int) Math.round(net + vatAmount);
                    
            textField_NetPrice.setText(net + "");
            textField_GrossPrice.setText(gross + "");
        }
    }
    
    private void CalculateByNet()
    {
        double quantity = getQuantity();
        double unitPrice;
        double net = Double.parseDouble(Functions.csakszam(textField_NetPrice.getText().replace(",", "."), 10, true));
        
        Label vatLabel = (Label) comboBox_Vat.getSelectedItem();
        double vatAmount = Double.parseDouble(vatLabel.getId()) / 100.0 * net;
        
        if(invoice.isForeignCurrency())
        {
            double gross = Math.round(net + vatAmount);
            unitPrice = Math.round(net / quantity * 10000.0) / 10000.0;
            
            textField_GrossPrice.setText(gross + "");
        }
        else
        {
            int gross = (int) Math.round(net + vatAmount);
            unitPrice = Math.round(net / quantity * 100.0) / 100.0;
            
            textField_GrossPrice.setText(gross + "");
        }
        
        textField_UnitPrice.setText(unitPrice + "");
    }
    
    private void CalculateByGross()
    {
        double quantity = getQuantity();
        double unitPrice;
        double gross = Double.parseDouble(textField_GrossPrice.getText().replace(",", "."));
        
        Label vatLabel = (Label) comboBox_Vat.getSelectedItem();
        double vat = Double.parseDouble(vatLabel.getId());
        double vatAmount = gross / (100 + vat) * vat;
        
        if(invoice.isForeignCurrency())
        {
            double net = Math.round(gross * 100.0 - vatAmount * 100.0) / 100.0;
            unitPrice = Math.round(net / quantity * 10000.0) / 10000.0;
            textField_NetPrice.setText(net + "");
        }
        else
        {
            double net = Math.round(gross * 100.0 - vatAmount * 100.0) / 100.0;
            unitPrice = Math.round(net / quantity * 100.0) / 100.0;
            textField_NetPrice.setText(net + "");
        }
        
        textField_UnitPrice.setText(unitPrice + "");
    }
    
    private void NumberOnly(KeyEvent evt, boolean decimal)
    {
        JTextField field = (JTextField) evt.getSource();
        int pos = field.getCaretPosition();
        
        String numberOnly = Functions.csakszam(field.getText(), 10, decimal);
        System.err.println("numberOnly: " + numberOnly);
        String currencyFormat = Formatter.currencyFormat(Integer.parseInt(numberOnly));
        System.err.println("currencyFormat: " + currencyFormat);

        field.setText(currencyFormat);
        
        try
        {
            field.setCaretPosition(pos);
        }
        catch (Exception ex){}
    }*/
    
    private void Evacuate()
    {
        currentProduct = new InvoiceProduct();
        SetProductFields();
    }
    
    private void SetProductFields()
    {
        textField_ProductName.setText(currentProduct.getName());
        textField_ItemNumber.setText(currentProduct.getItemNumber());
        combobox_MeasureOfUnit.setSelectedItem(currentProduct.getMeasureOfUnit().getShortName());
        textField_VtszTeszor.setText(currentProduct.getVtszTeszor());
        
        SetCalculatedProductFields();
        
        comboboxUpdate = true;
        
        for(int i = 0; i < comboBox_Vat.getItemCount(); i++)
        {
            Label l = (Label) comboBox_Vat.getItemAt(i);
            if(l.getName().equalsIgnoreCase(currentProduct.getVatLabel()))
            {
                comboBox_Vat.setSelectedIndex(i);
                break;
            }
        }

        comboboxUpdate = false;
        button_AddProduct.setText("Hozzáad");
    }
    
    private void SetCalculatedProductFields()
    {
        textField_Quantity.setText(Formatter.currencyFormat(currentProduct.getQuantity()));
        textField_UnitPrice.setText(Formatter.currencyFormat(currentProduct.getUnitPrice()));
        textField_NetPrice.setText(Formatter.currencyFormat(currentProduct.getNetPrice(invoice.isForeignCurrency())));
        textField_GrossPrice.setText(Formatter.currencyFormat(currentProduct.getGrossPrice(invoice.isForeignCurrency())));
    }
    
    private void UpdateInvoiceProducts()
    {
        DefaultTableModel DTModel = (DefaultTableModel) table_Products.getModel();
        
        for(int i = table_Products.getRowCount() - 1; i >= 0; i--)
        {
            DTModel.removeRow(i);
        }
        
        InvoiceProducts invoiceProducts = invoice.getProducts();
        
        for(int i = 0; i < invoiceProducts.Size(); i++)
        {
            Object [] row = new Object[11];
            
            InvoiceProduct product = invoiceProducts.Get(i);
            
            row[0] = product.getName();
            row[1] = product.getItemNumber();
            row[2] = product.getQuantity();
            row[3] = product.getMeasureOfUnit().getShortName();
            row[4] = product.getUnitPrice();
            row[5] = product.getVtszTeszor();
            row[6] = product.getNetPrice(invoice.isForeignCurrency());
            row[7] = product.getVatLabel();
            row[8] = product.getVatAmount(invoice.isForeignCurrency());
            row[9] = product.getGrossPrice(invoice.isForeignCurrency());
            row[10]= null;
            
            if(!product.getProductFees().isEmpty() && !checkBox_Takeover.isSelected())
            {
                row[10] = product.getTotalGrossProductFee(invoice.isForeignCurrency());
            }
            
            DTModel.addRow(row);
        }
        
        double allNet = invoice.getProducts().getTotalNet(InvoiceProducts.PRODUCT, invoice.isForeignCurrency());
        double allVat = invoice.getProducts().getTotalVat(InvoiceProducts.PRODUCT, invoice.isForeignCurrency());
        double allGross = invoice.getProducts().getTotalGross(InvoiceProducts.PRODUCT, invoice.isForeignCurrency());
        
        double allNetFee = invoice.getProducts().getTotalNet(InvoiceProducts.PRODUCTFEE, invoice.isForeignCurrency());
        double allVatFee = invoice.getProducts().getTotalVat(InvoiceProducts.PRODUCTFEE, invoice.isForeignCurrency());
        double allGrossFee = invoice.getProducts().getTotalGross(InvoiceProducts.PRODUCTFEE, invoice.isForeignCurrency());
        
        if(!checkBox_Takeover.isSelected())
        {
            allNet = allNet + allNetFee;
            allVat = allVat + allVatFee;
            allGross = allGross + allGrossFee;
        }
        
        String allNetStr = EncodeDecode.numberFormat(allNet, invoice.isForeignCurrency(), invoice.getPaymentMethod());
        String allVatStr = EncodeDecode.numberFormat(allVat, invoice.isForeignCurrency(), invoice.getPaymentMethod());
        String allGrossStr = EncodeDecode.numberFormat(allGross, invoice.isForeignCurrency(), invoice.getPaymentMethod());
        String allGrossFeeStr = EncodeDecode.numberFormat(allGrossFee, invoice.isForeignCurrency(), invoice.getPaymentMethod());
        
        textField_SumNet.setText(allNetStr);
        textField_SumVat.setText(allVatStr);
        textField_SumGross.setText(allGrossStr);
        label_SummaryDropdown.setText(allGrossStr + " (" + allNetStr + " + " + allVatStr + ")");
        
        textField_TotalProductFee.setText(allGrossFeeStr);
        
        textField_NetSummary.setText(allNetStr);
        textField_VatSummary.setText(allVatStr);
        textField_GrossSummary.setText(allGrossStr);
        
        if (allGross < 0)
        {
            label_Pay.setText("Visszatérítendő:");
            textField_Pay.setText(EncodeDecode.numberFormat(allGross * -1, invoice.isForeignCurrency(), invoice.getPaymentMethod()));
            textField_IE.setText(Functions.betuvel((allGross* -1)) + " " + invoice.getCurrency());
        }
        else
        {
            label_Pay.setText("Fizetendő:");
            textField_Pay.setText(allGrossStr);
            textField_IE.setText(Functions.betuvel((allGross)) + " " + invoice.getCurrency());
        }
    }
    
    private void UpdateInvoiceProducts(double oldCentralParity, double newCentralParity)
    {
        DefaultTableModel DTModel = (DefaultTableModel) table_Products.getModel();
        
        for(int i = table_Products.getRowCount() - 1; i >= 0; i--)
        {
            DTModel.removeRow(i);
        }
        
        for(int i = 0; i < invoice.getProducts().Size(); i++)
        {
            InvoiceProduct product = invoice.getProducts().Get(i);
            DecimalFormat df = new DecimalFormat("0.00");

            double oldUnitPrice = product.getUnitPrice();
            double newUnitPrice = (double)oldUnitPrice * oldCentralParity / newCentralParity;
            String formatedUnitPrice = df.format(newUnitPrice).replaceAll(",", ".");
            invoice.getProducts().Get(i).setUnitPrice(Double.parseDouble(formatedUnitPrice));
            
            Object [] row = new Object[11];
            
            row[0] = product.getName();
            row[1] = product.getItemNumber();
            row[2] = product.getQuantity();
            row[3] = product.getMeasureOfUnit().getShortName();
            row[4] = formatedUnitPrice;
            row[5] = product.getVtszTeszor();
            row[6] = product.getNetPrice(invoice.isForeignCurrency());
            row[7] = product.getVatLabel();
            row[8] = product.getVatAmount(invoice.isForeignCurrency());
            row[9] = product.getGrossPrice(invoice.isForeignCurrency());
            row[10]= null;
            
            
            if(!product.getProductFees().isEmpty() && !checkBox_Takeover.isSelected())
            {
                for(ProductFee fee : product.getProductFees())
                {
                    double oldProductFee = fee.getKgPrice();
                    double newProductFee = (double)oldProductFee * oldCentralParity / newCentralParity;
                    String formatedProductFee = df.format(newProductFee).replaceAll(",", ".");
                    fee.setKgPrice(Double.parseDouble(formatedProductFee));
                }
                
                row[10]= product.getTotalGrossProductFee(invoice.isForeignCurrency());
            }
            
            DTModel.addRow(row);
        }

        double allNet = invoice.getProducts().getTotalNet(InvoiceProducts.PRODUCT, invoice.isForeignCurrency());
        double allVat = invoice.getProducts().getTotalVat(InvoiceProducts.PRODUCT, invoice.isForeignCurrency());
        double allGross = invoice.getProducts().getTotalGross(InvoiceProducts.PRODUCT, invoice.isForeignCurrency());
        
        double allNetFee = invoice.getProducts().getTotalNet(InvoiceProducts.PRODUCTFEE, invoice.isForeignCurrency());
        double allVatFee = invoice.getProducts().getTotalVat(InvoiceProducts.PRODUCTFEE, invoice.isForeignCurrency());
        double allGrossFee = invoice.getProducts().getTotalGross(InvoiceProducts.PRODUCTFEE, invoice.isForeignCurrency());
        
        if(!checkBox_Takeover.isSelected())
        {
            allNet = allNet + allNetFee;
            allVat = allVat + allVatFee;
            allGross = allGross + allGrossFee;
        }
        
        String allNetStr = EncodeDecode.numberFormat(allNet, invoice.isForeignCurrency(), invoice.getPaymentMethod());
        String allVatStr = EncodeDecode.numberFormat(allVat, invoice.isForeignCurrency(), invoice.getPaymentMethod());
        String allGrossStr = EncodeDecode.numberFormat(allGross, invoice.isForeignCurrency(), invoice.getPaymentMethod());
        String allGrossFeeStr = EncodeDecode.numberFormat(allGrossFee, invoice.isForeignCurrency(), invoice.getPaymentMethod());
        
        textField_SumNet.setText(allNetStr);
        textField_SumVat.setText(allVatStr);
        textField_SumGross.setText(allGrossStr);
        
        label_SummaryDropdown.setText(allGrossStr + " (" + allNetStr + " + " + allVatStr + ")");
        
        textField_TotalProductFee.setText(allGrossFeeStr);
        
        textField_NetSummary.setText(allNetStr);
        textField_VatSummary.setText(allVatStr);
        textField_GrossSummary.setText(allGrossStr);
        
        if (allGross < 0)
        {
            label_Pay.setText("Visszatérítendő:");
            textField_Pay.setText(EncodeDecode.numberFormat(allGross * -1, invoice.isForeignCurrency(), invoice.getPaymentMethod()));
            textField_IE.setText(Functions.betuvel((allGross* -1)) + " " + invoice.getCurrency());
        }
        else
        {
            label_Pay.setText("Fizetendő:");
            textField_Pay.setText(allGrossStr);
            textField_IE.setText(Functions.betuvel((allGross)) + " " + invoice.getCurrency());
        }
    }
    
    private void SaveInvoice()
    {
        LoadInvoice();
        
        invoice.Save(App.user.getId());
    }
    
    private void LoadInvoice()
    {
        //invoice = new Invoice();
        
        //invoice.setIndentifier(indentifier);
        //invoice.setType(type);
        //invoice.setInvoiceType(invoiceType);
        //invoice.setInvoiceNumber(number);
        //invoice.setCorrectedInvoice(correctedInvoice);
        //invoice.setInvoiceGroup(invoiceGroup);
        //invoice.setSupplier(supplier);
        //invoice.setCustomer(customer);
        invoice.setIssueDate(label_IssueDate.getText());
        invoice.setMaturityDate(label_MaturityDate.getText());
        invoice.setCompletionDate(label_CompletionDate.getText());
        //invoice.setInvoiceProducts(invoiceProducts);
        invoice.setPaymentMethod(comboBox_PaymentMethod.getSelectedIndex());
        invoice.setCurrency(currency);
        invoice.setCentralParity(centralParity);
        invoice.setForeignCurrency(invoice.isForeignCurrency());
        invoice.setTakeoverType(checkBox_Takeover.isSelected() ? comboBox_TakeoverType.getSelectedItem().toString() : "");
        
        double totalNet = invoice.getProducts().getTotalNet(InvoiceProducts.PRODUCT, invoice.isForeignCurrency());
        double totalVat = invoice.getProducts().getTotalVat(InvoiceProducts.PRODUCT, invoice.isForeignCurrency());
        double totalFeeNet = invoice.getProducts().getTotalNet(InvoiceProducts.PRODUCTFEE, invoice.isForeignCurrency());
        double totalFeeVat = invoice.getProducts().getTotalVat(InvoiceProducts.PRODUCTFEE, invoice.isForeignCurrency());
        
        invoice.setNetPrice(totalNet + totalFeeNet);
        invoice.setVatAmount(totalVat + totalFeeVat);
        invoice.setComment(textField_Comment.getText());
        invoice.setFooter(textField_Footer.getText());
        invoice.setPrinted(checkBox_Print.isSelected());
    }
    
    private void UpdatePixiProducts()
    {
        if(App.args.length > 0)
        {
            String [] argString = App.args;
            String search = "(0";
            
            for (int j = 2; j < argString.length; j++)
            {
                search += ", " + argString[j];
            }
            
            search += ")";
            
            String query = "UPDATE pixi_ajanlatkeresek_adatai "
                + "SET statusz = 5, szamla = '" + invoice.getInvoiceNumber() + "', szamla_tipus = " + comboBox_PaymentMethod.getSelectedIndex()
                + " WHERE id IN " + search;
            
            App.pixi.insert(query, null);
        }
    }
    
    private void UpdateTakeoverTypesComboBox()
    {
        comboboxUpdate = true;
        
        comboBox_TakeoverType.removeAllItems();
        Object [][] takeoverTypes = Takeover.getTakeoverTypes();
        comboBox_TakeoverType.addItem(new Label(takeoverTypes[0][1].toString(), "01"));

        for(int i = 1; i < takeoverTypes.length; i++)
        {
            Label l = new Label(takeoverTypes[i][1].toString(), takeoverTypes[i][0].toString());
            comboBox_TakeoverType.addItem(l);
        }
        
        comboboxUpdate = false;
    }
    
    private void ShowPopupMenu(MouseEvent evt)
    {
        JTable source = (JTable) evt.getSource();
        int row = source.rowAtPoint(evt.getPoint());
        int column = source.columnAtPoint(evt.getPoint());

        if(!source.isRowSelected(row))
        {
            source.changeSelection(row, column, false, false);
        }

        int [] products = table_Products.getSelectedRows();

        popupMenu_InvoiceProduct.show(evt.getComponent(), evt.getX(), evt.getY());
    }
    
    private void Dropdown(CustomButton button, JPanel parent, JPanel child)
    {
        if(button.getText().equals("-"))
        {
            button.setText("+");
            Dimension d = new Dimension(parent.getWidth(), 40);
            parent.setPreferredSize(d);
            child.setVisible(false);
            
        }
        else if(button.getText().equals("+"))
        {
            button.setText("-");
            Dimension parentSize = parent.getPreferredSize();
            Dimension childSize = child.getPreferredSize();
            
            Dimension newSize = new Dimension(parentSize.width, parentSize.height + childSize.height);
            parent.setPreferredSize(newSize);
            child.setVisible(true);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dialog_ChangeCurrency = new javax.swing.JDialog();
        panel_CurrencyChange = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        label_CurrencyChangeText = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        label_CurrencyCurrency = new javax.swing.JLabel();
        label_NewCurrency = new javax.swing.JLabel();
        textField_CurrentCurrency = new javax.swing.JTextField();
        textField_NewCurrency = new javax.swing.JTextField();
        label_CentralParity = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        textField_CentralParity = new javax.swing.JTextField();
        button_ChangeCurrency = new customs.CustomButton();
        button_ExitChangeCurrencyDialog = new customs.CustomButton();
        dialog_Summary = new javax.swing.JDialog();
        panel_Sum = new javax.swing.JPanel();
        panel_PriceSum = new javax.swing.JPanel();
        label_PriceSum = new javax.swing.JLabel();
        panel_PriceSummary = new javax.swing.JPanel();
        label_NetSummary = new javax.swing.JLabel();
        label_VatSummary = new javax.swing.JLabel();
        label_GrossSummary = new javax.swing.JLabel();
        textField_NetSummary = new javax.swing.JTextField();
        textField_VatSummary = new javax.swing.JTextField();
        textField_GrossSummary = new javax.swing.JTextField();
        label_Valuta8 = new javax.swing.JLabel();
        label_Valuta9 = new javax.swing.JLabel();
        label_Valuta10 = new javax.swing.JLabel();
        label_Pay = new javax.swing.JLabel();
        textField_Pay = new javax.swing.JTextField();
        label_Valuta11 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        label_IE = new javax.swing.JLabel();
        textField_IE = new javax.swing.JLabel();
        panel_Printing = new javax.swing.JPanel();
        label_Printing = new javax.swing.JLabel();
        panel_PrintAndPDF = new javax.swing.JPanel();
        label_Printer = new javax.swing.JLabel();
        comboBox_Printer = new customs.CustomCombobox();
        checkBox_Print = new javax.swing.JCheckBox();
        textField_Copy = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        button_Preview = new customs.CustomButton();
        checkBox_PDF = new javax.swing.JCheckBox();
        button_SaveAsInvoice = new customs.CustomButton();
        button_SaveAndPrint = new customs.CustomButton();
        button_ExitSummaryDialog = new customs.CustomButton();
        popupMenu_InvoiceProduct = new javax.swing.JPopupMenu();
        menuItem_Edit = new javax.swing.JMenuItem();
        menuItem_VtszTeszor = new javax.swing.JMenuItem();
        menuItem_Delete = new javax.swing.JMenuItem();
        panel_background = new imagepanel.ImagePanel();
        label_InvoiceTitle = new javax.swing.JLabel();
        label_InvoiceNumber = new javax.swing.JLabel();
        imagePanel_HeaderAndProductDatas = new imagepanel.ImagePanel();
        jLabel5 = new javax.swing.JLabel();
        imagePanel_CommentAndFooter = new imagepanel.ImagePanel();
        jLabel3 = new javax.swing.JLabel();
        panel_HeaderAndProductDatas = new javax.swing.JPanel();
        panel_Supplier = new javax.swing.JPanel();
        label_Supplier = new javax.swing.JLabel();
        comboBox_Supplier = new customs.CustomCombobox();
        button_SupplierDatas = new customs.CustomButton();
        panel_Customer = new javax.swing.JPanel();
        label_Customer = new javax.swing.JLabel();
        panel_CustomerSelect = new javax.swing.JPanel();
        comboBox_PaymentMethod = new customs.CustomCombobox();
        comboBox_Currency = new customs.CustomCombobox();
        button_CustomerDropdown = new customs.CustomButton();
        textField_CustomerName = new javax.swing.JTextField();
        button_CustomerDatas = new customs.CustomButton();
        panel_Dates = new javax.swing.JPanel();
        label_Dates = new javax.swing.JLabel();
        label_DropdownIssueDate = new javax.swing.JLabel();
        label_DropdownMaturityDate = new javax.swing.JLabel();
        label_DropdownCompletionDate = new javax.swing.JLabel();
        label_IssueDate = new javax.swing.JLabel();
        label_MaturityDate = new javax.swing.JLabel();
        label_CompletionDate = new javax.swing.JLabel();
        panel_ProductDatas = new javax.swing.JPanel();
        label_ProductDatas = new javax.swing.JLabel();
        panel_Product = new javax.swing.JPanel();
        label_ProductName = new javax.swing.JLabel();
        textField_ProductName = new javax.swing.JTextField();
        button_ProductSelect = new customs.CustomButton();
        button_ProductFee = new customs.CustomButton();
        button_DeleteProductFee = new customs.CustomButton();
        label_ItemNumber = new javax.swing.JLabel();
        textField_ItemNumber = new javax.swing.JTextField();
        label_UnitPrice = new javax.swing.JLabel();
        textField_UnitPrice = new javax.swing.JTextField();
        label_Valuta1 = new javax.swing.JLabel();
        label_Quantity = new javax.swing.JLabel();
        textField_Quantity = new javax.swing.JTextField();
        label_AmountUnits = new javax.swing.JLabel();
        label_NetPrice = new javax.swing.JLabel();
        textField_NetPrice = new javax.swing.JTextField();
        label_Valuta2 = new javax.swing.JLabel();
        label_VtszTeszor = new javax.swing.JLabel();
        textField_VtszTeszor = new javax.swing.JTextField();
        button_VtszTeszorSelect = new customs.CustomButton();
        label_GrossPrice = new javax.swing.JLabel();
        textField_GrossPrice = new javax.swing.JTextField();
        label_Valuta3 = new javax.swing.JLabel();
        label_Vat = new javax.swing.JLabel();
        comboBox_Vat = new customs.CustomCombobox();
        label_Discount = new javax.swing.JLabel();
        textField_Discount = new javax.swing.JTextField();
        label_Percent = new javax.swing.JLabel();
        button_Evacuate = new customs.CustomButton();
        button_AddProduct = new customs.CustomButton();
        combobox_MeasureOfUnit = new customs.CustomCombobox();
        button_NewMeasureOfUnit = new customs.CustomButton();
        button_ProductDatasDropdown = new customs.CustomButton();
        panel_Products = new javax.swing.JPanel();
        label_Products = new javax.swing.JLabel();
        scrollPane_Products = new javax.swing.JScrollPane();
        table_Products = new javax.swing.JTable();
        panel_Summary = new javax.swing.JPanel();
        label_Summary = new javax.swing.JLabel();
        panel_SumDatas = new javax.swing.JPanel();
        checkBox_Takeover = new javax.swing.JCheckBox();
        comboBox_TakeoverType = new customs.CustomCombobox();
        label_TotalProductFee = new javax.swing.JLabel();
        textField_TotalProductFee = new javax.swing.JTextField();
        label_Valuta7 = new javax.swing.JLabel();
        label_SumNet = new javax.swing.JLabel();
        textField_SumNet = new javax.swing.JTextField();
        label_Valuta4 = new javax.swing.JLabel();
        label_SumVat = new javax.swing.JLabel();
        textField_SumVat = new javax.swing.JTextField();
        label_Valuta5 = new javax.swing.JLabel();
        label_SumGross = new javax.swing.JLabel();
        textField_SumGross = new javax.swing.JTextField();
        label_Valuta6 = new javax.swing.JLabel();
        button_SummaryDropdown = new customs.CustomButton();
        label_SummaryDropdown = new javax.swing.JLabel();
        panel_CommentAndFooter = new javax.swing.JPanel();
        panel_Comment = new javax.swing.JPanel();
        label_Comment = new javax.swing.JLabel();
        scrollPane_Comment = new javax.swing.JScrollPane();
        textField_Comment = new javax.swing.JTextArea();
        panel_Footer = new javax.swing.JPanel();
        label_Footer = new javax.swing.JLabel();
        scrollPane_Footer = new javax.swing.JScrollPane();
        textField_Footer = new javax.swing.JTextArea();
        button_Summary = new customs.CustomButton();

        dialog_ChangeCurrency.setMinimumSize(new java.awt.Dimension(280, 0));
        dialog_ChangeCurrency.setModal(true);
        dialog_ChangeCurrency.setName("dialog_ChangeCurrency"); // NOI18N

        panel_CurrencyChange.setName("panel_CurrencyChange"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(cezeszamlazo.App.class).getContext().getResourceMap(ResizeableInvoiceView.class);
        jPanel1.setBackground(resourceMap.getColor("jPanel1.background")); // NOI18N
        jPanel1.setName("jPanel1"); // NOI18N

        jPanel2.setBackground(resourceMap.getColor("jPanel2.background")); // NOI18N
        jPanel2.setName("jPanel2"); // NOI18N

        label_CurrencyChangeText.setFont(resourceMap.getFont("label_CurrencyChangeText.font")); // NOI18N
        label_CurrencyChangeText.setForeground(resourceMap.getColor("label_CurrencyChangeText.foreground")); // NOI18N
        label_CurrencyChangeText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label_CurrencyChangeText.setText(resourceMap.getString("label_CurrencyChangeText.text")); // NOI18N
        label_CurrencyChangeText.setName("label_CurrencyChangeText"); // NOI18N

        jPanel3.setBackground(resourceMap.getColor("jPanel3.background")); // NOI18N
        jPanel3.setName("jPanel3"); // NOI18N

        label_CurrencyCurrency.setText(resourceMap.getString("label_CurrencyCurrency.text")); // NOI18N
        label_CurrencyCurrency.setName("label_CurrencyCurrency"); // NOI18N

        label_NewCurrency.setText(resourceMap.getString("label_NewCurrency.text")); // NOI18N
        label_NewCurrency.setName("label_NewCurrency"); // NOI18N

        textField_CurrentCurrency.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textField_CurrentCurrency.setText(resourceMap.getString("textField_CurrentCurrency.text")); // NOI18N
        textField_CurrentCurrency.setEnabled(false);
        textField_CurrentCurrency.setName("textField_CurrentCurrency"); // NOI18N

        textField_NewCurrency.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textField_NewCurrency.setText(resourceMap.getString("textField_NewCurrency.text")); // NOI18N
        textField_NewCurrency.setEnabled(false);
        textField_NewCurrency.setName("textField_NewCurrency"); // NOI18N

        label_CentralParity.setText(resourceMap.getString("label_CentralParity.text")); // NOI18N
        label_CentralParity.setName("label_CentralParity"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        textField_CentralParity.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textField_CentralParity.setText(resourceMap.getString("textField_CentralParity.text")); // NOI18N
        textField_CentralParity.setName("textField_CentralParity"); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(label_CentralParity, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(label_NewCurrency, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(label_CurrencyCurrency, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(textField_CentralParity)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2))
                    .addComponent(textField_CurrentCurrency)
                    .addComponent(textField_NewCurrency))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label_CurrencyCurrency)
                    .addComponent(textField_CurrentCurrency, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label_NewCurrency)
                    .addComponent(textField_NewCurrency, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label_CentralParity)
                    .addComponent(textField_CentralParity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label_CurrencyChangeText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label_CurrencyChangeText)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        button_ChangeCurrency.setText(resourceMap.getString("button_ChangeCurrency.text")); // NOI18N
        button_ChangeCurrency.setName("button_ChangeCurrency"); // NOI18N
        button_ChangeCurrency.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_ChangeCurrencyActionPerformed(evt);
            }
        });

        button_ExitChangeCurrencyDialog.setText(resourceMap.getString("button_ExitChangeCurrencyDialog.text")); // NOI18N
        button_ExitChangeCurrencyDialog.setName("button_ExitChangeCurrencyDialog"); // NOI18N
        button_ExitChangeCurrencyDialog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_ExitChangeCurrencyDialogActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(button_ChangeCurrency, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(button_ExitChangeCurrencyDialog, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(button_ChangeCurrency, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(button_ExitChangeCurrencyDialog, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panel_CurrencyChangeLayout = new javax.swing.GroupLayout(panel_CurrencyChange);
        panel_CurrencyChange.setLayout(panel_CurrencyChangeLayout);
        panel_CurrencyChangeLayout.setHorizontalGroup(
            panel_CurrencyChangeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panel_CurrencyChangeLayout.setVerticalGroup(
            panel_CurrencyChangeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout dialog_ChangeCurrencyLayout = new javax.swing.GroupLayout(dialog_ChangeCurrency.getContentPane());
        dialog_ChangeCurrency.getContentPane().setLayout(dialog_ChangeCurrencyLayout);
        dialog_ChangeCurrencyLayout.setHorizontalGroup(
            dialog_ChangeCurrencyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel_CurrencyChange, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        dialog_ChangeCurrencyLayout.setVerticalGroup(
            dialog_ChangeCurrencyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel_CurrencyChange, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        dialog_Summary.setModal(true);
        dialog_Summary.setName("dialog_Summary"); // NOI18N

        panel_Sum.setBackground(resourceMap.getColor("panel_Sum.background")); // NOI18N
        panel_Sum.setName("panel_Sum"); // NOI18N

        panel_PriceSum.setBackground(resourceMap.getColor("panel_PriceSum.background")); // NOI18N
        panel_PriceSum.setName("panel_PriceSum"); // NOI18N

        label_PriceSum.setForeground(resourceMap.getColor("label_PriceSum.foreground")); // NOI18N
        label_PriceSum.setText(resourceMap.getString("label_PriceSum.text")); // NOI18N
        label_PriceSum.setName("label_PriceSum"); // NOI18N

        panel_PriceSummary.setBackground(resourceMap.getColor("panel_PriceSummary.background")); // NOI18N
        panel_PriceSummary.setName("panel_PriceSummary"); // NOI18N

        label_NetSummary.setText(resourceMap.getString("label_NetSummary.text")); // NOI18N
        label_NetSummary.setName("label_NetSummary"); // NOI18N

        label_VatSummary.setText(resourceMap.getString("label_VatSummary.text")); // NOI18N
        label_VatSummary.setName("label_VatSummary"); // NOI18N

        label_GrossSummary.setText(resourceMap.getString("label_GrossSummary.text")); // NOI18N
        label_GrossSummary.setName("label_GrossSummary"); // NOI18N

        textField_NetSummary.setBackground(resourceMap.getColor("textField_Copy.background")); // NOI18N
        textField_NetSummary.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        textField_NetSummary.setText(resourceMap.getString("textField_NetSummary.text")); // NOI18N
        textField_NetSummary.setEnabled(false);
        textField_NetSummary.setName("textField_NetSummary"); // NOI18N

        textField_VatSummary.setBackground(resourceMap.getColor("textField_Copy.background")); // NOI18N
        textField_VatSummary.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        textField_VatSummary.setText(resourceMap.getString("textField_VatSummary.text")); // NOI18N
        textField_VatSummary.setEnabled(false);
        textField_VatSummary.setName("textField_VatSummary"); // NOI18N

        textField_GrossSummary.setBackground(resourceMap.getColor("textField_Copy.background")); // NOI18N
        textField_GrossSummary.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        textField_GrossSummary.setText(resourceMap.getString("textField_GrossSummary.text")); // NOI18N
        textField_GrossSummary.setEnabled(false);
        textField_GrossSummary.setName("textField_GrossSummary"); // NOI18N

        label_Valuta8.setText(resourceMap.getString("label_Valuta8.text")); // NOI18N
        label_Valuta8.setName("label_Valuta8"); // NOI18N

        label_Valuta9.setText(resourceMap.getString("label_Valuta9.text")); // NOI18N
        label_Valuta9.setName("label_Valuta9"); // NOI18N

        label_Valuta10.setText(resourceMap.getString("label_Valuta10.text")); // NOI18N
        label_Valuta10.setName("label_Valuta10"); // NOI18N

        label_Pay.setFont(resourceMap.getFont("label_Pay.font")); // NOI18N
        label_Pay.setText(resourceMap.getString("label_Pay.text")); // NOI18N
        label_Pay.setName("label_Pay"); // NOI18N

        textField_Pay.setBackground(resourceMap.getColor("textField_Copy.background")); // NOI18N
        textField_Pay.setFont(resourceMap.getFont("textField_Pay.font")); // NOI18N
        textField_Pay.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        textField_Pay.setText(resourceMap.getString("textField_Pay.text")); // NOI18N
        textField_Pay.setEnabled(false);
        textField_Pay.setName("textField_Pay"); // NOI18N

        label_Valuta11.setFont(resourceMap.getFont("textField_Pay.font")); // NOI18N
        label_Valuta11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label_Valuta11.setText(resourceMap.getString("label_Valuta11.text")); // NOI18N
        label_Valuta11.setName("label_Valuta11"); // NOI18N

        jSeparator1.setName("jSeparator1"); // NOI18N

        label_IE.setText(resourceMap.getString("label_IE.text")); // NOI18N
        label_IE.setName("label_IE"); // NOI18N

        textField_IE.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        textField_IE.setText(resourceMap.getString("textField_IE.text")); // NOI18N
        textField_IE.setName("textField_IE"); // NOI18N

        javax.swing.GroupLayout panel_PriceSummaryLayout = new javax.swing.GroupLayout(panel_PriceSummary);
        panel_PriceSummary.setLayout(panel_PriceSummaryLayout);
        panel_PriceSummaryLayout.setHorizontalGroup(
            panel_PriceSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_PriceSummaryLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_PriceSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addGroup(panel_PriceSummaryLayout.createSequentialGroup()
                        .addGroup(panel_PriceSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(label_NetSummary, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(label_VatSummary, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
                            .addComponent(label_GrossSummary, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(label_Pay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(label_IE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel_PriceSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panel_PriceSummaryLayout.createSequentialGroup()
                                .addComponent(textField_NetSummary)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(label_Valuta8))
                            .addGroup(panel_PriceSummaryLayout.createSequentialGroup()
                                .addComponent(textField_VatSummary)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(label_Valuta9))
                            .addGroup(panel_PriceSummaryLayout.createSequentialGroup()
                                .addComponent(textField_GrossSummary)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(label_Valuta10))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_PriceSummaryLayout.createSequentialGroup()
                                .addComponent(textField_Pay)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(label_Valuta11))
                            .addComponent(textField_IE, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE))))
                .addContainerGap())
        );
        panel_PriceSummaryLayout.setVerticalGroup(
            panel_PriceSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_PriceSummaryLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_PriceSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label_NetSummary)
                    .addComponent(textField_NetSummary, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label_Valuta8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_PriceSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label_VatSummary)
                    .addComponent(textField_VatSummary, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label_Valuta9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_PriceSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label_GrossSummary)
                    .addComponent(textField_GrossSummary, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label_Valuta10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panel_PriceSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label_Pay)
                    .addComponent(textField_Pay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label_Valuta11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_PriceSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(textField_IE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(label_IE))
                .addContainerGap())
        );

        javax.swing.GroupLayout panel_PriceSumLayout = new javax.swing.GroupLayout(panel_PriceSum);
        panel_PriceSum.setLayout(panel_PriceSumLayout);
        panel_PriceSumLayout.setHorizontalGroup(
            panel_PriceSumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_PriceSumLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label_PriceSum)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(panel_PriceSummary, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panel_PriceSumLayout.setVerticalGroup(
            panel_PriceSumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_PriceSumLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label_PriceSum)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel_PriceSummary, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panel_Printing.setBackground(resourceMap.getColor("panel_Printing.background")); // NOI18N
        panel_Printing.setName("panel_Printing"); // NOI18N

        label_Printing.setForeground(resourceMap.getColor("label_Printing.foreground")); // NOI18N
        label_Printing.setText(resourceMap.getString("label_Printing.text")); // NOI18N
        label_Printing.setName("label_Printing"); // NOI18N

        panel_PrintAndPDF.setBackground(resourceMap.getColor("panel_PrintAndPDF.background")); // NOI18N
        panel_PrintAndPDF.setName("panel_PrintAndPDF"); // NOI18N

        label_Printer.setText(resourceMap.getString("label_Printer.text")); // NOI18N
        label_Printer.setName("label_Printer"); // NOI18N

        comboBox_Printer.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Nyomtatók" }));
        comboBox_Printer.setName("comboBox_Printer"); // NOI18N
        comboBox_Printer.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboBox_PrinterItemStateChanged(evt);
            }
        });

        checkBox_Print.setSelected(true);
        checkBox_Print.setText(resourceMap.getString("checkBox_Print.text")); // NOI18N
        checkBox_Print.setName("checkBox_Print"); // NOI18N
        checkBox_Print.setOpaque(false);

        textField_Copy.setBackground(resourceMap.getColor("textField_Copy.background")); // NOI18N
        textField_Copy.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textField_Copy.setText(resourceMap.getString("textField_Copy.text")); // NOI18N
        textField_Copy.setName("textField_Copy"); // NOI18N

        jLabel15.setText(resourceMap.getString("jLabel15.text")); // NOI18N
        jLabel15.setName("jLabel15"); // NOI18N

        button_Preview.setIcon(resourceMap.getIcon("button_Preview.icon")); // NOI18N
        button_Preview.setText(resourceMap.getString("button_Preview.text")); // NOI18N
        button_Preview.setName("button_Preview"); // NOI18N
        button_Preview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_PreviewActionPerformed(evt);
            }
        });

        checkBox_PDF.setText(resourceMap.getString("checkBox_PDF.text")); // NOI18N
        checkBox_PDF.setName("checkBox_PDF"); // NOI18N
        checkBox_PDF.setOpaque(false);

        javax.swing.GroupLayout panel_PrintAndPDFLayout = new javax.swing.GroupLayout(panel_PrintAndPDF);
        panel_PrintAndPDF.setLayout(panel_PrintAndPDFLayout);
        panel_PrintAndPDFLayout.setHorizontalGroup(
            panel_PrintAndPDFLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_PrintAndPDFLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_PrintAndPDFLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_PrintAndPDFLayout.createSequentialGroup()
                        .addComponent(checkBox_PDF)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(panel_PrintAndPDFLayout.createSequentialGroup()
                        .addComponent(label_Printer)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboBox_Printer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panel_PrintAndPDFLayout.createSequentialGroup()
                        .addComponent(checkBox_Print)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textField_Copy)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(button_Preview, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        panel_PrintAndPDFLayout.setVerticalGroup(
            panel_PrintAndPDFLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_PrintAndPDFLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_PrintAndPDFLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label_Printer)
                    .addComponent(comboBox_Printer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panel_PrintAndPDFLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(checkBox_Print)
                    .addComponent(textField_Copy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15)
                    .addComponent(button_Preview, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(checkBox_PDF)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panel_PrintingLayout = new javax.swing.GroupLayout(panel_Printing);
        panel_Printing.setLayout(panel_PrintingLayout);
        panel_PrintingLayout.setHorizontalGroup(
            panel_PrintingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_PrintingLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label_Printing)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(panel_PrintAndPDF, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panel_PrintingLayout.setVerticalGroup(
            panel_PrintingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_PrintingLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label_Printing)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel_PrintAndPDF, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        button_SaveAsInvoice.setText(resourceMap.getString("button_SaveAsInvoice.text")); // NOI18N
        button_SaveAsInvoice.setName("button_SaveAsInvoice"); // NOI18N
        button_SaveAsInvoice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_SaveAsInvoiceActionPerformed(evt);
            }
        });

        button_SaveAndPrint.setText(resourceMap.getString("button_SaveAndPrint.text")); // NOI18N
        button_SaveAndPrint.setName("button_SaveAndPrint"); // NOI18N
        button_SaveAndPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_SaveAndPrintActionPerformed(evt);
            }
        });

        button_ExitSummaryDialog.setText(resourceMap.getString("button_ExitSummaryDialog.text")); // NOI18N
        button_ExitSummaryDialog.setName("button_ExitSummaryDialog"); // NOI18N
        button_ExitSummaryDialog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_ExitSummaryDialogActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel_SumLayout = new javax.swing.GroupLayout(panel_Sum);
        panel_Sum.setLayout(panel_SumLayout);
        panel_SumLayout.setHorizontalGroup(
            panel_SumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_SumLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_SumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panel_PriceSum, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panel_Printing, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_SumLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(button_SaveAsInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(button_SaveAndPrint, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(button_ExitSummaryDialog, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        panel_SumLayout.setVerticalGroup(
            panel_SumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_SumLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel_PriceSum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel_Printing, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_SumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(button_ExitSummaryDialog, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(button_SaveAndPrint, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(button_SaveAsInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout dialog_SummaryLayout = new javax.swing.GroupLayout(dialog_Summary.getContentPane());
        dialog_Summary.getContentPane().setLayout(dialog_SummaryLayout);
        dialog_SummaryLayout.setHorizontalGroup(
            dialog_SummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel_Sum, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        dialog_SummaryLayout.setVerticalGroup(
            dialog_SummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel_Sum, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        popupMenu_InvoiceProduct.setName("popupMenu_InvoiceProduct"); // NOI18N

        menuItem_Edit.setText(resourceMap.getString("menuItem_Edit.text")); // NOI18N
        menuItem_Edit.setName("menuItem_Edit"); // NOI18N
        menuItem_Edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItem_EditActionPerformed(evt);
            }
        });
        popupMenu_InvoiceProduct.add(menuItem_Edit);

        menuItem_VtszTeszor.setText(resourceMap.getString("menuItem_VtszTeszor.text")); // NOI18N
        menuItem_VtszTeszor.setName("menuItem_VtszTeszor"); // NOI18N
        menuItem_VtszTeszor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItem_VtszTeszorActionPerformed(evt);
            }
        });
        popupMenu_InvoiceProduct.add(menuItem_VtszTeszor);

        menuItem_Delete.setText(resourceMap.getString("menuItem_Delete.text")); // NOI18N
        menuItem_Delete.setName("menuItem_Delete"); // NOI18N
        menuItem_Delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItem_DeleteActionPerformed(evt);
            }
        });
        popupMenu_InvoiceProduct.add(menuItem_Delete);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setName("Form"); // NOI18N
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        panel_background.setName("panel_background"); // NOI18N
        panel_background.setPreferredSize(new java.awt.Dimension(650, 540));

        label_InvoiceTitle.setFont(resourceMap.getFont("label_InvoiceTitle.font")); // NOI18N
        label_InvoiceTitle.setForeground(resourceMap.getColor("label_InvoiceTitle.foreground")); // NOI18N
        label_InvoiceTitle.setText(resourceMap.getString("label_InvoiceTitle.text")); // NOI18N
        label_InvoiceTitle.setName("label_InvoiceTitle"); // NOI18N

        label_InvoiceNumber.setFont(resourceMap.getFont("label_InvoiceNumber.font")); // NOI18N
        label_InvoiceNumber.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label_InvoiceNumber.setText(resourceMap.getString("label_InvoiceNumber.text")); // NOI18N
        label_InvoiceNumber.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        label_InvoiceNumber.setName("label_InvoiceNumber"); // NOI18N

        imagePanel_HeaderAndProductDatas.setName("imagePanel_HeaderAndProductDatas"); // NOI18N
        imagePanel_HeaderAndProductDatas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                imagePanel_HeaderAndProductDatasMouseClicked(evt);
            }
        });

        jLabel5.setForeground(resourceMap.getColor("jLabel5.foreground")); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        javax.swing.GroupLayout imagePanel_HeaderAndProductDatasLayout = new javax.swing.GroupLayout(imagePanel_HeaderAndProductDatas);
        imagePanel_HeaderAndProductDatas.setLayout(imagePanel_HeaderAndProductDatasLayout);
        imagePanel_HeaderAndProductDatasLayout.setHorizontalGroup(
            imagePanel_HeaderAndProductDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(imagePanel_HeaderAndProductDatasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                .addContainerGap())
        );
        imagePanel_HeaderAndProductDatasLayout.setVerticalGroup(
            imagePanel_HeaderAndProductDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
        );

        imagePanel_CommentAndFooter.setName("imagePanel_CommentAndFooter"); // NOI18N
        imagePanel_CommentAndFooter.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                imagePanel_CommentAndFooterMouseClicked(evt);
            }
        });

        jLabel3.setForeground(resourceMap.getColor("jLabel3.foreground")); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        javax.swing.GroupLayout imagePanel_CommentAndFooterLayout = new javax.swing.GroupLayout(imagePanel_CommentAndFooter);
        imagePanel_CommentAndFooter.setLayout(imagePanel_CommentAndFooterLayout);
        imagePanel_CommentAndFooterLayout.setHorizontalGroup(
            imagePanel_CommentAndFooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(imagePanel_CommentAndFooterLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                .addContainerGap())
        );
        imagePanel_CommentAndFooterLayout.setVerticalGroup(
            imagePanel_CommentAndFooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        panel_HeaderAndProductDatas.setBackground(resourceMap.getColor("panel_HeaderAndProductDatas.background")); // NOI18N
        panel_HeaderAndProductDatas.setName("panel_HeaderAndProductDatas"); // NOI18N

        panel_Supplier.setBackground(resourceMap.getColor("panel_Supplier.background")); // NOI18N
        panel_Supplier.setName("panel_Supplier"); // NOI18N

        label_Supplier.setFont(resourceMap.getFont("label_Supplier.font")); // NOI18N
        label_Supplier.setForeground(resourceMap.getColor("label_Supplier.foreground")); // NOI18N
        label_Supplier.setText(resourceMap.getString("label_Supplier.text")); // NOI18N
        label_Supplier.setName("label_Supplier"); // NOI18N

        comboBox_Supplier.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Szállítók" }));
        comboBox_Supplier.setName("comboBox_Supplier"); // NOI18N
        comboBox_Supplier.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboBox_SupplierItemStateChanged(evt);
            }
        });
        comboBox_Supplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBox_SupplierActionPerformed(evt);
            }
        });

        button_SupplierDatas.setIcon(resourceMap.getIcon("button_SupplierDatas.icon")); // NOI18N
        button_SupplierDatas.setText(resourceMap.getString("button_SupplierDatas.text")); // NOI18N
        button_SupplierDatas.setName("button_SupplierDatas"); // NOI18N
        button_SupplierDatas.setPreferredSize(new java.awt.Dimension(30, 31));
        button_SupplierDatas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_SupplierDatasActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel_SupplierLayout = new javax.swing.GroupLayout(panel_Supplier);
        panel_Supplier.setLayout(panel_SupplierLayout);
        panel_SupplierLayout.setHorizontalGroup(
            panel_SupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_SupplierLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(label_Supplier)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(comboBox_Supplier, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(button_SupplierDatas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panel_SupplierLayout.setVerticalGroup(
            panel_SupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_SupplierLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_SupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panel_SupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(comboBox_Supplier, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(label_Supplier, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(button_SupplierDatas, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        panel_Customer.setBackground(resourceMap.getColor("panel_Customer.background")); // NOI18N
        panel_Customer.setName("panel_Customer"); // NOI18N

        label_Customer.setForeground(resourceMap.getColor("label_Dates.foreground")); // NOI18N
        label_Customer.setText(resourceMap.getString("label_Customer.text")); // NOI18N
        label_Customer.setName("label_Customer"); // NOI18N

        panel_CustomerSelect.setBackground(resourceMap.getColor("panel_CustomerSelect.background")); // NOI18N
        panel_CustomerSelect.setName("panel_CustomerSelect"); // NOI18N

        comboBox_PaymentMethod.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Készpénz", "Átutalás", "Utánvét" }));
        comboBox_PaymentMethod.setName("comboBox_PaymentMethod"); // NOI18N
        comboBox_PaymentMethod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBox_PaymentMethodActionPerformed(evt);
            }
        });

        comboBox_Currency.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Pénznem" }));
        comboBox_Currency.setName("comboBox_Currency"); // NOI18N
        comboBox_Currency.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboBox_CurrencyItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout panel_CustomerSelectLayout = new javax.swing.GroupLayout(panel_CustomerSelect);
        panel_CustomerSelect.setLayout(panel_CustomerSelectLayout);
        panel_CustomerSelectLayout.setHorizontalGroup(
            panel_CustomerSelectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_CustomerSelectLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_CustomerSelectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(comboBox_PaymentMethod, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(comboBox_Currency, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panel_CustomerSelectLayout.setVerticalGroup(
            panel_CustomerSelectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_CustomerSelectLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(comboBox_PaymentMethod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(comboBox_Currency, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        button_CustomerDropdown.setText(resourceMap.getString("button_CustomerDropdown.text")); // NOI18N
        button_CustomerDropdown.setName("button_CustomerDropdown"); // NOI18N
        button_CustomerDropdown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_CustomerDropdownActionPerformed(evt);
            }
        });

        textField_CustomerName.setBackground(resourceMap.getColor("textField_CustomerName.background")); // NOI18N
        textField_CustomerName.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textField_CustomerName.setText(resourceMap.getString("textField_CustomerName.text")); // NOI18N
        textField_CustomerName.setEnabled(false);
        textField_CustomerName.setName("textField_CustomerName"); // NOI18N
        textField_CustomerName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                textField_CustomerNameMouseClicked(evt);
            }
        });

        button_CustomerDatas.setIcon(resourceMap.getIcon("button_CustomerDatas.icon")); // NOI18N
        button_CustomerDatas.setText(resourceMap.getString("button_CustomerDatas.text")); // NOI18N
        button_CustomerDatas.setMaximumSize(new java.awt.Dimension(49, 23));
        button_CustomerDatas.setMinimumSize(new java.awt.Dimension(49, 23));
        button_CustomerDatas.setName("button_CustomerDatas"); // NOI18N
        button_CustomerDatas.setPreferredSize(new java.awt.Dimension(30, 23));
        button_CustomerDatas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_CustomerDatasActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel_CustomerLayout = new javax.swing.GroupLayout(panel_Customer);
        panel_Customer.setLayout(panel_CustomerLayout);
        panel_CustomerLayout.setHorizontalGroup(
            panel_CustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_CustomerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(button_CustomerDropdown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(label_Customer)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textField_CustomerName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(button_CustomerDatas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(panel_CustomerSelect, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panel_CustomerLayout.setVerticalGroup(
            panel_CustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_CustomerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_CustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(button_CustomerDatas, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panel_CustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(label_Customer)
                        .addComponent(button_CustomerDropdown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(textField_CustomerName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panel_CustomerSelect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        panel_Dates.setBackground(resourceMap.getColor("panel_Dates.background")); // NOI18N
        panel_Dates.setName("panel_Dates"); // NOI18N

        label_Dates.setForeground(resourceMap.getColor("label_Dates.foreground")); // NOI18N
        label_Dates.setText(resourceMap.getString("label_Dates.text")); // NOI18N
        label_Dates.setName("label_Dates"); // NOI18N

        label_DropdownIssueDate.setFont(resourceMap.getFont("label_DropdownIssueDate.font")); // NOI18N
        label_DropdownIssueDate.setForeground(resourceMap.getColor("label_DropdownIssueDate.foreground")); // NOI18N
        label_DropdownIssueDate.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        label_DropdownIssueDate.setText(resourceMap.getString("label_DropdownIssueDate.text")); // NOI18N
        label_DropdownIssueDate.setName("label_DropdownIssueDate"); // NOI18N

        label_DropdownMaturityDate.setFont(resourceMap.getFont("label_DropdownMaturityDate.font")); // NOI18N
        label_DropdownMaturityDate.setForeground(resourceMap.getColor("label_DropdownMaturityDate.foreground")); // NOI18N
        label_DropdownMaturityDate.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        label_DropdownMaturityDate.setText(resourceMap.getString("label_DropdownMaturityDate.text")); // NOI18N
        label_DropdownMaturityDate.setName("label_DropdownMaturityDate"); // NOI18N
        label_DropdownMaturityDate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                label_DropdownMaturityDateMouseClicked(evt);
            }
        });

        label_DropdownCompletionDate.setFont(resourceMap.getFont("label_DropdownCompletionDate.font")); // NOI18N
        label_DropdownCompletionDate.setForeground(resourceMap.getColor("label_DropdownCompletionDate.foreground")); // NOI18N
        label_DropdownCompletionDate.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        label_DropdownCompletionDate.setText(resourceMap.getString("label_DropdownCompletionDate.text")); // NOI18N
        label_DropdownCompletionDate.setName("label_DropdownCompletionDate"); // NOI18N
        label_DropdownCompletionDate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                label_DropdownCompletionDateMouseClicked(evt);
            }
        });

        label_IssueDate.setForeground(resourceMap.getColor("label_MaturityDate.foreground")); // NOI18N
        label_IssueDate.setText(resourceMap.getString("label_IssueDate.text")); // NOI18N
        label_IssueDate.setName("label_IssueDate"); // NOI18N

        label_MaturityDate.setForeground(resourceMap.getColor("label_MaturityDate.foreground")); // NOI18N
        label_MaturityDate.setText(resourceMap.getString("label_MaturityDate.text")); // NOI18N
        label_MaturityDate.setName("label_MaturityDate"); // NOI18N
        label_MaturityDate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                label_MaturityDateMouseClicked(evt);
            }
        });

        label_CompletionDate.setForeground(resourceMap.getColor("label_MaturityDate.foreground")); // NOI18N
        label_CompletionDate.setText(resourceMap.getString("label_CompletionDate.text")); // NOI18N
        label_CompletionDate.setName("label_CompletionDate"); // NOI18N
        label_CompletionDate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                label_CompletionDateMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout panel_DatesLayout = new javax.swing.GroupLayout(panel_Dates);
        panel_Dates.setLayout(panel_DatesLayout);
        panel_DatesLayout.setHorizontalGroup(
            panel_DatesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_DatesLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(label_Dates)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(label_DropdownIssueDate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(label_IssueDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(label_DropdownMaturityDate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(label_MaturityDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(label_DropdownCompletionDate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(label_CompletionDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panel_DatesLayout.setVerticalGroup(
            panel_DatesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_DatesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_DatesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label_CompletionDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(label_MaturityDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(label_IssueDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panel_DatesLayout.createSequentialGroup()
                        .addGroup(panel_DatesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(label_Dates)
                            .addComponent(label_DropdownIssueDate)
                            .addComponent(label_DropdownMaturityDate)
                            .addComponent(label_DropdownCompletionDate))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        panel_ProductDatas.setBackground(resourceMap.getColor("panel_ProductDatas.background")); // NOI18N
        panel_ProductDatas.setName("panel_ProductDatas"); // NOI18N

        label_ProductDatas.setForeground(resourceMap.getColor("label_ProductDatas.foreground")); // NOI18N
        label_ProductDatas.setText(resourceMap.getString("label_ProductDatas.text")); // NOI18N
        label_ProductDatas.setName("label_ProductDatas"); // NOI18N

        panel_Product.setBackground(resourceMap.getColor("panel_Product.background")); // NOI18N
        panel_Product.setName("panel_Product"); // NOI18N

        label_ProductName.setText(resourceMap.getString("label_ProductName.text")); // NOI18N
        label_ProductName.setName("label_ProductName"); // NOI18N

        textField_ProductName.setBackground(resourceMap.getColor("textField_ProductName.background")); // NOI18N
        textField_ProductName.setText(resourceMap.getString("textField_ProductName.text")); // NOI18N
        textField_ProductName.setName("textField_ProductName"); // NOI18N
        textField_ProductName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                textField_ProductNameFocusLost(evt);
            }
        });

        button_ProductSelect.setIcon(resourceMap.getIcon("button_ProductSelect.icon")); // NOI18N
        button_ProductSelect.setText(resourceMap.getString("button_ProductSelect.text")); // NOI18N
        button_ProductSelect.setEnabled(false);
        button_ProductSelect.setName("button_ProductSelect"); // NOI18N
        button_ProductSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_ProductSelectActionPerformed(evt);
            }
        });

        button_ProductFee.setText(resourceMap.getString("button_ProductFee.text")); // NOI18N
        button_ProductFee.setName("button_ProductFee"); // NOI18N
        button_ProductFee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_ProductFeeActionPerformed(evt);
            }
        });

        button_DeleteProductFee.setIcon(resourceMap.getIcon("button_DeleteProductFee.icon")); // NOI18N
        button_DeleteProductFee.setText(resourceMap.getString("button_DeleteProductFee.text")); // NOI18N
        button_DeleteProductFee.setName("button_DeleteProductFee"); // NOI18N
        button_DeleteProductFee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_DeleteProductFeeActionPerformed(evt);
            }
        });

        label_ItemNumber.setText(resourceMap.getString("label_ItemNumber.text")); // NOI18N
        label_ItemNumber.setName("label_ItemNumber"); // NOI18N

        textField_ItemNumber.setBackground(resourceMap.getColor("textField_VtszTeszor.background")); // NOI18N
        textField_ItemNumber.setText(resourceMap.getString("textField_ItemNumber.text")); // NOI18N
        textField_ItemNumber.setName("textField_ItemNumber"); // NOI18N
        textField_ItemNumber.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                textField_ItemNumberFocusLost(evt);
            }
        });

        label_UnitPrice.setText(resourceMap.getString("label_UnitPrice.text")); // NOI18N
        label_UnitPrice.setName("label_UnitPrice"); // NOI18N

        textField_UnitPrice.setBackground(resourceMap.getColor("textField_VtszTeszor.background")); // NOI18N
        textField_UnitPrice.setText(resourceMap.getString("textField_UnitPrice.text")); // NOI18N
        textField_UnitPrice.setName("textField_UnitPrice"); // NOI18N
        textField_UnitPrice.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                textField_UnitPriceFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                textField_UnitPriceFocusLost(evt);
            }
        });
        textField_UnitPrice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textField_UnitPriceKeyReleased(evt);
            }
        });

        label_Valuta1.setText(resourceMap.getString("label_Valuta1.text")); // NOI18N
        label_Valuta1.setName("label_Valuta1"); // NOI18N

        label_Quantity.setText(resourceMap.getString("label_Quantity.text")); // NOI18N
        label_Quantity.setName("label_Quantity"); // NOI18N

        textField_Quantity.setBackground(resourceMap.getColor("textField_VtszTeszor.background")); // NOI18N
        textField_Quantity.setText(resourceMap.getString("textField_Quantity.text")); // NOI18N
        textField_Quantity.setName("textField_Quantity"); // NOI18N
        textField_Quantity.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                textField_QuantityFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                textField_QuantityFocusLost(evt);
            }
        });
        textField_Quantity.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textField_QuantityKeyReleased(evt);
            }
        });

        label_AmountUnits.setText(resourceMap.getString("label_AmountUnits.text")); // NOI18N
        label_AmountUnits.setName("label_AmountUnits"); // NOI18N

        label_NetPrice.setText(resourceMap.getString("label_NetPrice.text")); // NOI18N
        label_NetPrice.setName("label_NetPrice"); // NOI18N

        textField_NetPrice.setBackground(resourceMap.getColor("textField_VtszTeszor.background")); // NOI18N
        textField_NetPrice.setText(resourceMap.getString("textField_NetPrice.text")); // NOI18N
        textField_NetPrice.setName("textField_NetPrice"); // NOI18N
        textField_NetPrice.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                textField_NetPriceFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                textField_NetPriceFocusLost(evt);
            }
        });
        textField_NetPrice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textField_NetPriceKeyReleased(evt);
            }
        });

        label_Valuta2.setText(resourceMap.getString("label_Valuta2.text")); // NOI18N
        label_Valuta2.setName("label_Valuta2"); // NOI18N

        label_VtszTeszor.setText(resourceMap.getString("label_VtszTeszor.text")); // NOI18N
        label_VtszTeszor.setName("label_VtszTeszor"); // NOI18N

        textField_VtszTeszor.setBackground(resourceMap.getColor("textField_VtszTeszor.background")); // NOI18N
        textField_VtszTeszor.setText(resourceMap.getString("textField_VtszTeszor.text")); // NOI18N
        textField_VtszTeszor.setName("textField_VtszTeszor"); // NOI18N
        textField_VtszTeszor.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                textField_VtszTeszorFocusLost(evt);
            }
        });

        button_VtszTeszorSelect.setText(resourceMap.getString("button_VtszTeszorSelect.text")); // NOI18N
        button_VtszTeszorSelect.setName("button_VtszTeszorSelect"); // NOI18N
        button_VtszTeszorSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_VtszTeszorSelectActionPerformed(evt);
            }
        });

        label_GrossPrice.setText(resourceMap.getString("label_GrossPrice.text")); // NOI18N
        label_GrossPrice.setName("label_GrossPrice"); // NOI18N

        textField_GrossPrice.setBackground(resourceMap.getColor("textField_VtszTeszor.background")); // NOI18N
        textField_GrossPrice.setText(resourceMap.getString("textField_GrossPrice.text")); // NOI18N
        textField_GrossPrice.setName("textField_GrossPrice"); // NOI18N
        textField_GrossPrice.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                textField_GrossPriceFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                textField_GrossPriceFocusLost(evt);
            }
        });
        textField_GrossPrice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textField_GrossPriceKeyReleased(evt);
            }
        });

        label_Valuta3.setText(resourceMap.getString("label_Valuta3.text")); // NOI18N
        label_Valuta3.setName("label_Valuta3"); // NOI18N

        label_Vat.setText(resourceMap.getString("label_Vat.text")); // NOI18N
        label_Vat.setName("label_Vat"); // NOI18N

        comboBox_Vat.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Áfa" }));
        comboBox_Vat.setName("comboBox_Vat"); // NOI18N
        comboBox_Vat.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboBox_VatItemStateChanged(evt);
            }
        });

        label_Discount.setText(resourceMap.getString("label_Discount.text")); // NOI18N
        label_Discount.setName("label_Discount"); // NOI18N

        textField_Discount.setBackground(resourceMap.getColor("textField_VtszTeszor.background")); // NOI18N
        textField_Discount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        textField_Discount.setText(resourceMap.getString("textField_Discount.text")); // NOI18N
        textField_Discount.setEnabled(false);
        textField_Discount.setName("textField_Discount"); // NOI18N

        label_Percent.setText(resourceMap.getString("label_Percent.text")); // NOI18N
        label_Percent.setName("label_Percent"); // NOI18N

        button_Evacuate.setText(resourceMap.getString("button_Evacuate.text")); // NOI18N
        button_Evacuate.setName("button_Evacuate"); // NOI18N
        button_Evacuate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_EvacuateActionPerformed(evt);
            }
        });

        button_AddProduct.setText(resourceMap.getString("button_AddProduct.text")); // NOI18N
        button_AddProduct.setName("button_AddProduct"); // NOI18N
        button_AddProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_AddProductActionPerformed(evt);
            }
        });

        combobox_MeasureOfUnit.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Mee" }));
        combobox_MeasureOfUnit.setName("combobox_MeasureOfUnit"); // NOI18N
        combobox_MeasureOfUnit.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                combobox_MeasureOfUnitItemStateChanged(evt);
            }
        });

        button_NewMeasureOfUnit.setText(resourceMap.getString("button_NewMeasureOfUnit.text")); // NOI18N
        button_NewMeasureOfUnit.setName("button_NewMeasureOfUnit"); // NOI18N
        button_NewMeasureOfUnit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_NewMeasureOfUnitActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel_ProductLayout = new javax.swing.GroupLayout(panel_Product);
        panel_Product.setLayout(panel_ProductLayout);
        panel_ProductLayout.setHorizontalGroup(
            panel_ProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_ProductLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_ProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_ProductLayout.createSequentialGroup()
                        .addGroup(panel_ProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(label_Vat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(label_VtszTeszor, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
                            .addComponent(label_Quantity, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(label_ItemNumber, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(label_ProductName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel_ProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panel_ProductLayout.createSequentialGroup()
                                .addComponent(textField_ProductName)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(button_ProductSelect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(button_ProductFee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(button_DeleteProductFee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panel_ProductLayout.createSequentialGroup()
                                .addGroup(panel_ProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(comboBox_Vat, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(textField_ItemNumber, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panel_ProductLayout.createSequentialGroup()
                                        .addGroup(panel_ProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(panel_ProductLayout.createSequentialGroup()
                                                .addComponent(textField_VtszTeszor)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                            .addGroup(panel_ProductLayout.createSequentialGroup()
                                                .addComponent(textField_Quantity, javax.swing.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(label_AmountUnits)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(combobox_MeasureOfUnit, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(4, 4, 4)))
                                        .addGroup(panel_ProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(button_NewMeasureOfUnit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(button_VtszTeszorSelect, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                .addGap(18, 18, 18)
                                .addGroup(panel_ProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(label_NetPrice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(label_GrossPrice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(label_UnitPrice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(label_Discount, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panel_ProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(textField_UnitPrice)
                                    .addComponent(textField_NetPrice)
                                    .addComponent(textField_GrossPrice)
                                    .addComponent(textField_Discount, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panel_ProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panel_ProductLayout.createSequentialGroup()
                                        .addGroup(panel_ProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(panel_ProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(label_Valuta1)
                                                .addComponent(label_Valuta2, javax.swing.GroupLayout.Alignment.TRAILING))
                                            .addComponent(label_Valuta3, javax.swing.GroupLayout.Alignment.TRAILING))
                                        .addGap(1, 1, 1))
                                    .addComponent(label_Percent, javax.swing.GroupLayout.Alignment.TRAILING)))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_ProductLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(button_Evacuate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(button_AddProduct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        panel_ProductLayout.setVerticalGroup(
            panel_ProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_ProductLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_ProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label_ProductName, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textField_ProductName, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(button_ProductSelect, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(button_ProductFee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(button_DeleteProductFee, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_ProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label_ItemNumber)
                    .addComponent(textField_ItemNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textField_UnitPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label_UnitPrice)
                    .addComponent(label_Valuta1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_ProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label_Quantity)
                    .addComponent(textField_Quantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label_NetPrice)
                    .addComponent(textField_NetPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label_Valuta2)
                    .addComponent(label_AmountUnits)
                    .addComponent(combobox_MeasureOfUnit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(button_NewMeasureOfUnit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_ProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_ProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(label_VtszTeszor)
                        .addComponent(textField_VtszTeszor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panel_ProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(button_VtszTeszorSelect, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panel_ProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(label_GrossPrice)
                            .addComponent(textField_GrossPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label_Valuta3))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_ProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_ProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(panel_ProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(label_Vat)
                            .addComponent(comboBox_Vat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(label_Discount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panel_ProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(textField_Discount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(label_Percent)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_ProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(button_AddProduct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(button_Evacuate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        button_ProductDatasDropdown.setText(resourceMap.getString("button_ProductDatasDropdown.text")); // NOI18N
        button_ProductDatasDropdown.setName("button_ProductDatasDropdown"); // NOI18N
        button_ProductDatasDropdown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_ProductDatasDropdownActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel_ProductDatasLayout = new javax.swing.GroupLayout(panel_ProductDatas);
        panel_ProductDatas.setLayout(panel_ProductDatasLayout);
        panel_ProductDatasLayout.setHorizontalGroup(
            panel_ProductDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_ProductDatasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(button_ProductDatasDropdown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(label_ProductDatas)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(panel_Product, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panel_ProductDatasLayout.setVerticalGroup(
            panel_ProductDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_ProductDatasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_ProductDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label_ProductDatas)
                    .addComponent(button_ProductDatasDropdown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel_Product, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panel_Products.setBackground(resourceMap.getColor("panel_Products.background")); // NOI18N
        panel_Products.setName("panel_Products"); // NOI18N

        label_Products.setForeground(resourceMap.getColor("label_Products.foreground")); // NOI18N
        label_Products.setText(resourceMap.getString("label_Products.text")); // NOI18N
        label_Products.setName("label_Products"); // NOI18N

        scrollPane_Products.setName("scrollPane_Products"); // NOI18N

        table_Products.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Termék", "Cikkszám", "Mennyiség", "Mee", "Egységár", "VTSZ/TESZOR", "Nettó", "Áfa", "Áfa érték", "Bruttó", "Termékdíj"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table_Products.setName("table_Products"); // NOI18N
        table_Products.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                table_ProductsMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                table_ProductsMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                table_ProductsMouseReleased(evt);
            }
        });
        scrollPane_Products.setViewportView(table_Products);

        javax.swing.GroupLayout panel_ProductsLayout = new javax.swing.GroupLayout(panel_Products);
        panel_Products.setLayout(panel_ProductsLayout);
        panel_ProductsLayout.setHorizontalGroup(
            panel_ProductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPane_Products, javax.swing.GroupLayout.DEFAULT_SIZE, 662, Short.MAX_VALUE)
            .addGroup(panel_ProductsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label_Products)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panel_ProductsLayout.setVerticalGroup(
            panel_ProductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_ProductsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label_Products)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollPane_Products, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE))
        );

        panel_Summary.setBackground(resourceMap.getColor("panel_Summary.background")); // NOI18N
        panel_Summary.setName("panel_Summary"); // NOI18N

        label_Summary.setForeground(resourceMap.getColor("label_Summary.foreground")); // NOI18N
        label_Summary.setText(resourceMap.getString("label_Summary.text")); // NOI18N
        label_Summary.setName("label_Summary"); // NOI18N

        panel_SumDatas.setBackground(resourceMap.getColor("panel_SumDatas.background")); // NOI18N
        panel_SumDatas.setName("panel_SumDatas"); // NOI18N

        checkBox_Takeover.setText(resourceMap.getString("checkBox_Takeover.text")); // NOI18N
        checkBox_Takeover.setName("checkBox_Takeover"); // NOI18N
        checkBox_Takeover.setOpaque(false);
        checkBox_Takeover.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBox_TakeoverActionPerformed(evt);
            }
        });

        comboBox_TakeoverType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Átvállalás típusa" }));
        comboBox_TakeoverType.setEnabled(false);
        comboBox_TakeoverType.setName("comboBox_TakeoverType"); // NOI18N
        comboBox_TakeoverType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboBox_TakeoverTypeItemStateChanged(evt);
            }
        });

        label_TotalProductFee.setText(resourceMap.getString("label_TotalProductFee.text")); // NOI18N
        label_TotalProductFee.setEnabled(false);
        label_TotalProductFee.setName("label_TotalProductFee"); // NOI18N

        textField_TotalProductFee.setBackground(resourceMap.getColor("textField_TotalProductFee.background")); // NOI18N
        textField_TotalProductFee.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textField_TotalProductFee.setText(resourceMap.getString("textField_TotalProductFee.text")); // NOI18N
        textField_TotalProductFee.setEnabled(false);
        textField_TotalProductFee.setName("textField_TotalProductFee"); // NOI18N

        label_Valuta7.setText(resourceMap.getString("label_Valuta7.text")); // NOI18N
        label_Valuta7.setEnabled(false);
        label_Valuta7.setName("label_Valuta7"); // NOI18N

        label_SumNet.setFont(resourceMap.getFont("label_SumNet.font")); // NOI18N
        label_SumNet.setText(resourceMap.getString("label_SumNet.text")); // NOI18N
        label_SumNet.setName("label_SumNet"); // NOI18N

        textField_SumNet.setBackground(resourceMap.getColor("textField_SumNet.background")); // NOI18N
        textField_SumNet.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textField_SumNet.setText(resourceMap.getString("textField_SumNet.text")); // NOI18N
        textField_SumNet.setEnabled(false);
        textField_SumNet.setName("textField_SumNet"); // NOI18N

        label_Valuta4.setText(resourceMap.getString("label_Valuta4.text")); // NOI18N
        label_Valuta4.setName("label_Valuta4"); // NOI18N

        label_SumVat.setText(resourceMap.getString("label_SumVat.text")); // NOI18N
        label_SumVat.setName("label_SumVat"); // NOI18N

        textField_SumVat.setBackground(resourceMap.getColor("textField_SumNet.background")); // NOI18N
        textField_SumVat.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textField_SumVat.setText(resourceMap.getString("textField_SumVat.text")); // NOI18N
        textField_SumVat.setEnabled(false);
        textField_SumVat.setName("textField_SumVat"); // NOI18N

        label_Valuta5.setText(resourceMap.getString("label_Valuta5.text")); // NOI18N
        label_Valuta5.setName("label_Valuta5"); // NOI18N

        label_SumGross.setText(resourceMap.getString("label_SumGross.text")); // NOI18N
        label_SumGross.setName("label_SumGross"); // NOI18N

        textField_SumGross.setBackground(resourceMap.getColor("textField_SumNet.background")); // NOI18N
        textField_SumGross.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textField_SumGross.setText(resourceMap.getString("textField_SumGross.text")); // NOI18N
        textField_SumGross.setEnabled(false);
        textField_SumGross.setName("textField_SumGross"); // NOI18N

        label_Valuta6.setText(resourceMap.getString("label_Valuta6.text")); // NOI18N
        label_Valuta6.setName("label_Valuta6"); // NOI18N

        javax.swing.GroupLayout panel_SumDatasLayout = new javax.swing.GroupLayout(panel_SumDatas);
        panel_SumDatas.setLayout(panel_SumDatasLayout);
        panel_SumDatasLayout.setHorizontalGroup(
            panel_SumDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_SumDatasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_SumDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_SumDatasLayout.createSequentialGroup()
                        .addComponent(checkBox_Takeover)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboBox_TakeoverType, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label_TotalProductFee)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textField_TotalProductFee)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label_Valuta7))
                    .addGroup(panel_SumDatasLayout.createSequentialGroup()
                        .addComponent(label_SumNet)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textField_SumNet)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label_Valuta4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(label_SumVat)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textField_SumVat)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label_Valuta5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(label_SumGross)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textField_SumGross)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label_Valuta6)))
                .addContainerGap())
        );
        panel_SumDatasLayout.setVerticalGroup(
            panel_SumDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_SumDatasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_SumDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(checkBox_Takeover)
                    .addComponent(comboBox_TakeoverType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textField_TotalProductFee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label_TotalProductFee)
                    .addComponent(label_Valuta7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_SumDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label_SumNet)
                    .addComponent(textField_SumNet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textField_SumVat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textField_SumGross, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label_SumVat)
                    .addComponent(label_SumGross)
                    .addComponent(label_Valuta4)
                    .addComponent(label_Valuta5)
                    .addComponent(label_Valuta6))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        button_SummaryDropdown.setText(resourceMap.getString("button_SummaryDropdown.text")); // NOI18N
        button_SummaryDropdown.setName("button_SummaryDropdown"); // NOI18N
        button_SummaryDropdown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_SummaryDropdownActionPerformed(evt);
            }
        });

        label_SummaryDropdown.setFont(resourceMap.getFont("label_SummaryDropdown.font")); // NOI18N
        label_SummaryDropdown.setForeground(resourceMap.getColor("label_SummaryDropdown.foreground")); // NOI18N
        label_SummaryDropdown.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        label_SummaryDropdown.setText(resourceMap.getString("label_SummaryDropdown.text")); // NOI18N
        label_SummaryDropdown.setName("label_SummaryDropdown"); // NOI18N

        javax.swing.GroupLayout panel_SummaryLayout = new javax.swing.GroupLayout(panel_Summary);
        panel_Summary.setLayout(panel_SummaryLayout);
        panel_SummaryLayout.setHorizontalGroup(
            panel_SummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_SummaryLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(button_SummaryDropdown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(label_Summary)
                .addGap(18, 18, 18)
                .addComponent(label_SummaryDropdown, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(panel_SumDatas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panel_SummaryLayout.setVerticalGroup(
            panel_SummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_SummaryLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_SummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label_SummaryDropdown, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panel_SummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(label_Summary)
                        .addComponent(button_SummaryDropdown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel_SumDatas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panel_HeaderAndProductDatasLayout = new javax.swing.GroupLayout(panel_HeaderAndProductDatas);
        panel_HeaderAndProductDatas.setLayout(panel_HeaderAndProductDatasLayout);
        panel_HeaderAndProductDatasLayout.setHorizontalGroup(
            panel_HeaderAndProductDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_HeaderAndProductDatasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_HeaderAndProductDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panel_ProductDatas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panel_Products, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panel_Summary, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panel_Customer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panel_Dates, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panel_Supplier, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panel_HeaderAndProductDatasLayout.setVerticalGroup(
            panel_HeaderAndProductDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_HeaderAndProductDatasLayout.createSequentialGroup()
                .addComponent(panel_Supplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel_Customer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(panel_Dates, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel_ProductDatas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel_Products, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel_Summary, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        panel_CommentAndFooter.setBackground(resourceMap.getColor("panel_CommentAndFooter.background")); // NOI18N
        panel_CommentAndFooter.setName("panel_CommentAndFooter"); // NOI18N
        panel_CommentAndFooter.setPreferredSize(new java.awt.Dimension(0, 590));

        panel_Comment.setBackground(resourceMap.getColor("panel_Comment.background")); // NOI18N
        panel_Comment.setName("panel_Comment"); // NOI18N

        label_Comment.setForeground(resourceMap.getColor("label_Comment.foreground")); // NOI18N
        label_Comment.setText(resourceMap.getString("label_Comment.text")); // NOI18N
        label_Comment.setName("label_Comment"); // NOI18N

        scrollPane_Comment.setName("scrollPane_Comment"); // NOI18N

        textField_Comment.setColumns(20);
        textField_Comment.setFont(resourceMap.getFont("textField_Comment.font")); // NOI18N
        textField_Comment.setRows(5);
        textField_Comment.setName("textField_Comment"); // NOI18N
        scrollPane_Comment.setViewportView(textField_Comment);

        javax.swing.GroupLayout panel_CommentLayout = new javax.swing.GroupLayout(panel_Comment);
        panel_Comment.setLayout(panel_CommentLayout);
        panel_CommentLayout.setHorizontalGroup(
            panel_CommentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_CommentLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_CommentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollPane_Comment)
                    .addGroup(panel_CommentLayout.createSequentialGroup()
                        .addComponent(label_Comment)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panel_CommentLayout.setVerticalGroup(
            panel_CommentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_CommentLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label_Comment)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollPane_Comment, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)
                .addContainerGap())
        );

        panel_Footer.setBackground(resourceMap.getColor("panel_Footer.background")); // NOI18N
        panel_Footer.setName("panel_Footer"); // NOI18N

        label_Footer.setForeground(resourceMap.getColor("label_Footer.foreground")); // NOI18N
        label_Footer.setText(resourceMap.getString("label_Footer.text")); // NOI18N
        label_Footer.setName("label_Footer"); // NOI18N

        scrollPane_Footer.setName("scrollPane_Footer"); // NOI18N

        textField_Footer.setColumns(20);
        textField_Footer.setFont(resourceMap.getFont("textField_Footer.font")); // NOI18N
        textField_Footer.setRows(5);
        textField_Footer.setText(resourceMap.getString("textField_Footer.text")); // NOI18N
        textField_Footer.setName("textField_Footer"); // NOI18N
        scrollPane_Footer.setViewportView(textField_Footer);

        javax.swing.GroupLayout panel_FooterLayout = new javax.swing.GroupLayout(panel_Footer);
        panel_Footer.setLayout(panel_FooterLayout);
        panel_FooterLayout.setHorizontalGroup(
            panel_FooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_FooterLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_FooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollPane_Footer)
                    .addGroup(panel_FooterLayout.createSequentialGroup()
                        .addComponent(label_Footer)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panel_FooterLayout.setVerticalGroup(
            panel_FooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_FooterLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label_Footer)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollPane_Footer, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout panel_CommentAndFooterLayout = new javax.swing.GroupLayout(panel_CommentAndFooter);
        panel_CommentAndFooter.setLayout(panel_CommentAndFooterLayout);
        panel_CommentAndFooterLayout.setHorizontalGroup(
            panel_CommentAndFooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_CommentAndFooterLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_CommentAndFooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panel_Comment, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panel_Footer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panel_CommentAndFooterLayout.setVerticalGroup(
            panel_CommentAndFooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_CommentAndFooterLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel_Comment, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel_Footer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        button_Summary.setIcon(resourceMap.getIcon("button_Summary.icon")); // NOI18N
        button_Summary.setText(resourceMap.getString("button_Summary.text")); // NOI18N
        button_Summary.setName("button_Summary"); // NOI18N
        button_Summary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_SummaryActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel_backgroundLayout = new javax.swing.GroupLayout(panel_background);
        panel_background.setLayout(panel_backgroundLayout);
        panel_backgroundLayout.setHorizontalGroup(
            panel_backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel_HeaderAndProductDatas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panel_CommentAndFooter, javax.swing.GroupLayout.DEFAULT_SIZE, 682, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_backgroundLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_backgroundLayout.createSequentialGroup()
                        .addComponent(label_InvoiceTitle)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(panel_backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(button_Summary, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label_InvoiceNumber, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(panel_backgroundLayout.createSequentialGroup()
                        .addComponent(imagePanel_HeaderAndProductDatas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(imagePanel_CommentAndFooter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panel_backgroundLayout.setVerticalGroup(
            panel_backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_backgroundLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(label_InvoiceTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                    .addGroup(panel_backgroundLayout.createSequentialGroup()
                        .addComponent(button_Summary, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(label_InvoiceNumber)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(imagePanel_HeaderAndProductDatas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(imagePanel_CommentAndFooter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel_HeaderAndProductDatas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel_CommentAndFooter, javax.swing.GroupLayout.DEFAULT_SIZE, 408, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel_background, javax.swing.GroupLayout.DEFAULT_SIZE, 682, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel_background, javax.swing.GroupLayout.DEFAULT_SIZE, 1274, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        if (resizeThread.isAlive())
        {
            resizeThread.setCount();
        }
        else
        {
            System.err.print("SET IMAGE (ResizeableInvoiceView.java/formComponentResized())");
            panel_background.setImage(background.Resize(getWidth()));
        }
    }//GEN-LAST:event_formComponentResized

    private void imagePanel_HeaderAndProductDatasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_imagePanel_HeaderAndProductDatasMouseClicked
        panel_HeaderAndProductDatas.setVisible(true);
        panel_CommentAndFooter.setVisible(false);
        
        if(!selectedPanel.equals("header"))
        {
            imagePanel_HeaderAndProductDatas.setImage(selected);
            imagePanel_CommentAndFooter.setImage(unselected);
        }
        
        selectedPanel = "header";
    }//GEN-LAST:event_imagePanel_HeaderAndProductDatasMouseClicked

    private void imagePanel_CommentAndFooterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_imagePanel_CommentAndFooterMouseClicked
        panel_HeaderAndProductDatas.setVisible(false);
        panel_CommentAndFooter.setVisible(true);
        
        if(!selectedPanel.equals("footer"))
        {
            imagePanel_HeaderAndProductDatas.setImage(unselected);
            imagePanel_CommentAndFooter.setImage(selected);
        }
        
        selectedPanel = "footer";
    }//GEN-LAST:event_imagePanel_CommentAndFooterMouseClicked

    private void comboBox_CurrencyItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboBox_CurrencyItemStateChanged
        if(!comboboxUpdate && evt.getStateChange() == ItemEvent.SELECTED)
        {
            Label l = (Label) comboBox_Currency.getSelectedItem();
            newCurrency = l.getName();
            newCentralParity = l.getId();
            invoice.setForeignCurrency(!newCurrency.equalsIgnoreCase("HUF"));
            
            textField_CurrentCurrency.setText(oldCurrency);
            textField_NewCurrency.setText(newCurrency);
            
            Label currencyLabel = (Label) comboBox_Currency.getSelectedItem();
            textField_CentralParity.setText(currencyLabel.getId());
            
            if(init)
            {
                currency = currencyLabel.getName();
                centralParity = Double.parseDouble(currencyLabel.getId());
                
                String valuta = "Ft";
                String invoiceNumberLabel = label_InvoiceNumber.getText().split("/V")[0];
                
                invoice.setCurrency(currency);
                
                if(!invoice.getCurrency().equals("HUF"))
                {
                    valuta = invoice.getCurrency().toLowerCase();
                    invoiceNumberLabel = invoiceNumberLabel + "/V";
                }
                
                label_InvoiceNumber.setText(invoiceNumberLabel);
                
                label_Valuta1.setText(valuta);
                label_Valuta2.setText(valuta);
                label_Valuta3.setText(valuta);
                label_Valuta4.setText(valuta);
                label_Valuta5.setText(valuta);
                label_Valuta6.setText(valuta);
                label_Valuta7.setText(valuta);
                label_Valuta8.setText(valuta);
                label_Valuta9.setText(valuta);
                label_Valuta10.setText(valuta);
                label_Valuta11.setText(valuta);
                
                applyNewCurrency = false;
                
                oldCurrency = newCurrency;
                oldCentralParity = newCentralParity;
            }
            else
            {
                Open(dialog_ChangeCurrency, "Pénznem változtatás");
            }
            
            if(applyNewCurrency)
            {
                currency = currencyLabel.getName();
                centralParity = Double.parseDouble(currencyLabel.getId());

                String valuta = "Ft";
                String invoiceNumberLabel = label_InvoiceNumber.getText().split("/V")[0];

                invoice.setCurrency(currency);

                if(!invoice.getCurrency().equals("HUF"))
                {
                    valuta = invoice.getCurrency().toLowerCase();
                    invoiceNumberLabel = invoiceNumberLabel + "/V";
                }

                label_InvoiceNumber.setText(invoiceNumberLabel);

                label_Valuta1.setText(valuta);
                label_Valuta2.setText(valuta);
                label_Valuta3.setText(valuta);
                label_Valuta4.setText(valuta);
                label_Valuta5.setText(valuta);
                label_Valuta6.setText(valuta);
                label_Valuta7.setText(valuta);
                label_Valuta8.setText(valuta);
                label_Valuta9.setText(valuta);
                label_Valuta10.setText(valuta);
                label_Valuta11.setText(valuta);

                applyNewCurrency = false;

                JTextField [] fields = {textField_UnitPrice, textField_NetPrice, textField_GrossPrice};

                for(int i = 0; i < fields.length; i++)
                {
                    if(fields[i].getText().length() != 0)
                    {
                        double oldValue = Double.parseDouble(Functions.csakszam(fields[i].getText(), 10, invoice.isForeignCurrency()));
                        double newValue = oldValue * Double.parseDouble(oldCentralParity) / Double.parseDouble(newCentralParity);
                        DecimalFormat df = new DecimalFormat("0.00");
                        String formatedValue = df.format(newValue);
                        fields[i].setText(String.valueOf(formatedValue.replaceAll(",", ".")));
                    }
                }

                SetProductFields();
                UpdateInvoiceProducts(Double.parseDouble(oldCentralParity), Double.parseDouble(newCentralParity));

                oldCurrency = newCurrency;
                oldCentralParity = newCentralParity;
            }
            else
            {
                comboboxUpdate = true;

                for(int i = 0; i < comboBox_Currency.getItemCount(); i++)
                {
                    if(comboBox_Currency.getItemAt(i).toString().equals(oldCurrency))
                    {
                        comboBox_Currency.setSelectedIndex(i);
                    }
                }
                comboboxUpdate = false;
            }
            
        }
    }//GEN-LAST:event_comboBox_CurrencyItemStateChanged

    private void button_ProductFeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_ProductFeeActionPerformed
        ProductFeesView view = new ProductFeesView(currentProduct.getProductFees());
        view.Open();
        
        if(view.getReturnStatus() == ReturnStatus.OK)
        {
            currentProduct.setProductFees(view.getProductFees());
            button_ProductFee.setColor(new Color(0, 0, 0));
        }
        
    }//GEN-LAST:event_button_ProductFeeActionPerformed

    private void comboBox_SupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBox_SupplierActionPerformed
        if(invoice.getType() != Invoice.SKETCH && invoice.getInvoiceType() != Invoice.InvoiceType.MODIFICATION)
        {
            if(!comboboxUpdate)
            {
                Label label = (Label)comboBox_Supplier.getSelectedItem();
                invoice.setSerializationID(Supplier.getSerializationIDByID(label.getId()));
                
                NumberVerification();
                
                Label selectedSupplier = (Label) comboBox_Supplier.getSelectedItem();
                int supplierID = Integer.parseInt(selectedSupplier.getId());
                
                Object [][] s = InvoiceNumber.getNumber(invoice.getType(), supplierID);
                
                String newNumber = String.valueOf(s[0][1]) + "/" + (Integer.parseInt(String.valueOf(s[0][0])) + 1);

                if (!String.valueOf(s[0][2]).isEmpty())
                {
                    newNumber = String.valueOf(s[0][2]) + " " + newNumber;
                }

                label_InvoiceNumber.setText(newNumber + (invoice.isForeignCurrency() ? "/V" : ""));

                if (!newNumber.matches(invoice.getInvoiceNumber()) && !invoice.getInvoiceNumber().isEmpty())
                {
                    HibaDialog hd = new HibaDialog("A sorszámozás megváltozott!\nAz új számla sorszám: " + newNumber, "Ok", "");
                }

                invoice.setInvoiceNumber(newNumber);
                
                if(invoice.getSupplier().getName().length() > 0)
                {
                    UpdateMaturityDate();
                }
            }
        }
        else
        {
            System.err.println("Vázlat módosító számla (ResizeableInvoiceView.java/comboBox_SupplierActionPerformed())");
        }
    }//GEN-LAST:event_comboBox_SupplierActionPerformed

    private void button_SupplierDatasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_SupplierDatasActionPerformed
        supplierView = new SupplierView(invoice);
        
        if(supplierView.getReturnStatus() == SupplierView.RET_OK)
        {
            invoice.setSupplier(supplierView.getSupplier());
            UpdateMaturityDate();
        }
    }//GEN-LAST:event_button_SupplierDatasActionPerformed

    private void comboBox_SupplierItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboBox_SupplierItemStateChanged
        if(!comboboxUpdate && evt.getStateChange() == ItemEvent.SELECTED)
        {
            Label selectedSupplier = (Label) comboBox_Supplier.getSelectedItem();

            invoice.setSupplier(new Supplier(Integer.parseInt(selectedSupplier.getId())));
        }
    }//GEN-LAST:event_comboBox_SupplierItemStateChanged

    private void button_CustomerDatasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_CustomerDatasActionPerformed
        customerView = new CustomerView(invoice.getCustomer());
        customerView.Open(true);
        
        if(customerView.getReturnStatus() == CustomerView.RET_OK)
        {
            UpdateMaturityDate();
            invoice.setCustomer(customerView.getCustomer());
            comboBox_PaymentMethod.setSelectedIndex(invoice.getCustomer().getPaymentMethod());
            textField_CustomerName.setText(invoice.getCustomer().getName());
        }
    }//GEN-LAST:event_button_CustomerDatasActionPerformed

    private void button_DeleteProductFeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_DeleteProductFeeActionPerformed
        //productfee = null;
        currentProduct.setProductFees(null);
    }//GEN-LAST:event_button_DeleteProductFeeActionPerformed

    private void button_VtszTeszorSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_VtszTeszorSelectActionPerformed
        VtszTeszorListaDialog view = new VtszTeszorListaDialog();
        
        if (view.getReturnStatus() == VtszTeszorListaDialog.RET_OK)
        {
            currentProduct.setVtszTeszor(view.getVtszTeszor());
            Label l;
            int j = 0;
            
            for (int i = 0; i < comboBox_Vat.getItemCount(); i++)
            {
                l = (Label) comboBox_Vat.getItemAt(i);
                
                if (l.getName().equalsIgnoreCase(view.getAfa()))
                {
                    j = i;
                    break;
                }
            }
            
            comboBox_Vat.setSelectedIndex(j);
            
            SetProductFields();
        }
    }//GEN-LAST:event_button_VtszTeszorSelectActionPerformed

    private void comboBox_VatItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboBox_VatItemStateChanged
        if(!init && !comboboxUpdate)
        {
            Label selectedItem = (Label)comboBox_Vat.getSelectedItem();

            currentProduct.setVatLabel(selectedItem.getName());
            currentProduct.setVatPercent(Integer.parseInt(selectedItem.getId()));

            /*if(!comboboxUpdate)
            {
                CalculateByQuantity();
            }*/
        }
    }//GEN-LAST:event_comboBox_VatItemStateChanged

    private void button_EvacuateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_EvacuateActionPerformed
        Evacuate();
    }//GEN-LAST:event_button_EvacuateActionPerformed

    private void button_AddProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_AddProductActionPerformed
        String valid = currentProduct.isValid();
        
        if(valid.isEmpty())
        {
            switch(button_AddProduct.getText())
            {
                case "Hozzáad":
                    invoice.getProducts().Add(currentProduct);
                    break;
                case "Módosítás mentése":
                    invoice.getProducts().Set(currentProduct.getRow(), currentProduct);
                    button_AddProduct.setText("Hozzáad");
                    break;
            }
            
            UpdateInvoiceProducts();
        }
        else
        {
            HibaDialog hd = new HibaDialog(valid, "Ok", "");
        }
        
        /*if(textField_ProductName.getText().isEmpty())
        {
            HibaDialog hd = new HibaDialog("Nincs megadva termék név!", "Ok", "");
        }
        else if(textField_Quantity.getText().isEmpty() || Double.parseDouble(textField_Quantity.getText()) == 0)
        {
            HibaDialog hd = new HibaDialog("A mennyiség nem lehet nulla!", "Ok", "");
        }
        else if(textField_UnitPrice.getText().isEmpty() || Double.parseDouble(textField_UnitPrice.getText()) == 0)
        {
            HibaDialog hd = new HibaDialog("Az egységár nem lehet nulla!", "Ok", "");
        }
        else
        {
            InvoiceProduct product = new InvoiceProduct();
            product.setProductName(textField_ProductName.getText());
            product.setItemNumber(textField_ItemNumber.getText());
            product.setQuantity(Double.parseDouble(textField_Quantity.getText()));
            product.setMeasureOfUnit(new MeasureOfUnit(combobox_MeasureOfUnit.getSelectedItem().toString()));
            product.setUnitPrice(textField_UnitPrice.getText().length() == 0 ? 0 : Double.parseDouble(textField_UnitPrice.getText()));
            //product.setNetPrice();
            product.setVatLabel(comboBox_Vat.getSelectedItem().toString());
            Label vatLabel = (Label) comboBox_Vat.getSelectedItem();
            product.setVatPercent(Integer.parseInt(vatLabel.getId()));
            product.setDiscount(Integer.parseInt(textField_Discount.getText()));
            product.setVtszTeszor(textField_VtszTeszor.getText());
            //product.setProductFee(productfee);
            
            switch(button_AddProduct.getText())
            {
                case "Hozzáad":
                    invoice.getProducts().Add(currentProduct);
                    break;
                case "Módosítás mentése":
                    invoice.getProducts().Set(currentProduct.getRow(), currentProduct);
                    button_AddProduct.setText("Hozzáad");
                    break;
            }
        }*/
    }//GEN-LAST:event_button_AddProductActionPerformed

    private void checkBox_TakeoverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBox_TakeoverActionPerformed
        String comment = "";
        
        boolean takeover = checkBox_Takeover.isSelected();
        
        if(takeover)
        {
            UpdateTakeoverTypesComboBox();
            Label l = (Label) comboBox_TakeoverType.getSelectedItem();
            comment = l.getId();

            comboBox_TakeoverType.setEnabled(true);
            tooltipsTakeover = Takeover.getTakeoverTooltips();
            comboBox_TakeoverType.setRenderer(new TakeoverTooltip());
        }
        else
        {
            comment = "";
            comboBox_TakeoverType.removeAllItems();
            comboBox_TakeoverType.setEnabled(false);
        }
        
        textField_Comment.setText(comment);
        label_TotalProductFee.setEnabled(takeover);
        label_Valuta7.setEnabled(takeover);
        
        UpdateInvoiceProducts();
    }//GEN-LAST:event_checkBox_TakeoverActionPerformed

    private void button_SummaryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_SummaryActionPerformed
        String valid = invoice.getCustomer().Valid();
        
        if(!valid.isEmpty())
        {
            HibaDialog h = new HibaDialog("Nincs vevő megadva!", "Ok", "");
        }
        else if(invoice.getProducts().Empty())
        {
            HibaDialog h = new HibaDialog("Nincs termék hozzáadva a számlához!", "Ok", "");
        }
        else
        {
            if(button_Summary.getText().equals("Mentés"))
            {
                dialog_InProgress = new FolyamatbanDialog("A mentés folyamatban. Kis türelmet...");
                InvoiceThread invoiceThread = new InvoiceThread();
                dialog_InProgress.setVisible(true);
            }
            else
            {
                if(invoice.getType() == Invoice.SKETCH && invoice.getInvoiceType() == Invoice.InvoiceType.MODIFICATION)
                {
                    button_SaveAsInvoice.setVisible(true);
                }
                else
                {
                    button_SaveAsInvoice.setVisible(false);
                }
                
                UpdatePrinterComboBox();
                Open(dialog_Summary, "Összegző");
            }
        }
    }//GEN-LAST:event_button_SummaryActionPerformed

    private void button_PreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_PreviewActionPerformed
        LoadInvoice();

        ElonezetDialog elonezet = new ElonezetDialog(invoice, Integer.parseInt(textField_Copy.getText()), ElonezetDialog.ELONEZET);
    }//GEN-LAST:event_button_PreviewActionPerformed

    private void comboBox_PaymentMethodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBox_PaymentMethodActionPerformed
        invoice.setPaymentMethod(comboBox_PaymentMethod.getSelectedIndex());
        invoice.getCustomer().setPaymentMethod(comboBox_PaymentMethod.getSelectedIndex());
        invoice.getCustomer().Update();
        
        UpdateInvoiceProducts();
    }//GEN-LAST:event_comboBox_PaymentMethodActionPerformed

    private void button_SaveAsInvoiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_SaveAsInvoiceActionPerformed
        invoice.setType(Invoice.SKETCH);
        invoice.setInvoiceType(Invoice.InvoiceType.STORNO);
        dialog_InProgress = new FolyamatbanDialog("A mentés folyamatban. Kis türelmet...");
        InvoiceThread invoiceThread = new InvoiceThread();
        dialog_InProgress.setVisible(true);
    }//GEN-LAST:event_button_SaveAsInvoiceActionPerformed

    private void button_SaveAndPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_SaveAndPrintActionPerformed
        dialog_InProgress = new FolyamatbanDialog("A mentés folyamatban. Kis türelmet...");
        InvoiceThread invoiceThread = new InvoiceThread();
        dialog_InProgress.setVisible(true);

        if (checkBox_Print.isSelected())
        {
            ElonezetDialog e = new ElonezetDialog(invoice, Integer.parseInt(textField_Copy.getText()), ElonezetDialog.NYOMTATAS);
        }

        if(checkBox_PDF.isSelected())
        {
            ElonezetDialog e = new ElonezetDialog(invoice, Integer.parseInt(textField_Copy.getText()), ElonezetDialog.PDF);
        }

        dialog_Summary.setVisible(false);

        if(App.args.length <= 0)
        {
            Close(RET_REOPEN);
        }
        else
        {
            System.exit(1);
        }
    }//GEN-LAST:event_button_SaveAndPrintActionPerformed

    private void table_ProductsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_ProductsMouseClicked
        if(evt.getButton() == MouseEvent.BUTTON1 && evt.getClickCount() == 2)
        {
            int row = table_Products.getSelectedRow();
            currentProduct = invoice.getProducts().Get(row);
            currentProduct.setRow(row);
            //productfee = currentProduct.getProductFee();
            
            SetProductFields();
            //CalculateByQuantity();
            
            button_AddProduct.setText("Módosítás mentése");
        }
    }//GEN-LAST:event_table_ProductsMouseClicked

    private void table_ProductsMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_ProductsMouseReleased
        if(evt.isPopupTrigger())
        {
            ShowPopupMenu(evt);
        }
        else
        {
            if(popupTimer.Stop())
            {
                ShowPopupMenu(evt);
                popupTimer.setStart(0);
            }
        }
    }//GEN-LAST:event_table_ProductsMouseReleased

    private void menuItem_EditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_EditActionPerformed
        int row = table_Products.getSelectedRow();
        currentProduct = invoice.getProducts().Get(row);
        currentProduct.setRow(row);

        SetProductFields();
        //CalculateByQuantity();

        button_AddProduct.setText("Módosítás mentése");
    }//GEN-LAST:event_menuItem_EditActionPerformed

    private void menuItem_VtszTeszorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_VtszTeszorActionPerformed
        VtszTeszorListaDialog v = new VtszTeszorListaDialog();
        
        if (v.getReturnStatus() == 1)
        {
            int[] rows = table_Products.getSelectedRows();
            
            for (int i = 0; i < rows.length; i++)
            {
                InvoiceProduct product = (InvoiceProduct) invoice.getProducts().Get(rows[i]);
                product.setVtszTeszor(v.getVtszTeszor());
                invoice.getProducts().Set(rows[i], product);
            }
            
            UpdateInvoiceProducts();
        }
    }//GEN-LAST:event_menuItem_VtszTeszorActionPerformed

    private void menuItem_DeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_DeleteActionPerformed
        int [] rows = table_Products.getSelectedRows();
        
        /*if(invoiceType == Invoice.InvoiceType.STORNO)
        {
            HibaDialog hd = new HibaDialog("Nem törölhet terméket az eredeti számláról!", "Rendben", "Vissza");
            System.out.println("Nem törölhet terméket az eredeti számláról!");
        }*/
        
        for(int i = rows.length - 1; i >=0; i--)
        {
            invoice.getProducts().Remove(rows[i]);
        }
        
        UpdateInvoiceProducts();
        
        button_AddProduct.setText("Hozzáad");
    }//GEN-LAST:event_menuItem_DeleteActionPerformed

    private void comboBox_TakeoverTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboBox_TakeoverTypeItemStateChanged
        if(!comboboxUpdate && evt.getStateChange() == ItemEvent.SELECTED)
        {
            Label l = (Label) comboBox_TakeoverType.getSelectedItem();

            textField_Comment.setText(l.getId());
        }
    }//GEN-LAST:event_comboBox_TakeoverTypeItemStateChanged

    private void button_ChangeCurrencyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_ChangeCurrencyActionPerformed
        applyNewCurrency = true;
        dialog_ChangeCurrency.setVisible(false);
    }//GEN-LAST:event_button_ChangeCurrencyActionPerformed

    private void button_ExitChangeCurrencyDialogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_ExitChangeCurrencyDialogActionPerformed
        dialog_ChangeCurrency.setVisible(false);
    }//GEN-LAST:event_button_ExitChangeCurrencyDialogActionPerformed

    private void table_ProductsMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_ProductsMousePressed
        popupTimer = new PopupTimer();
        popupTimer.Start();
    }//GEN-LAST:event_table_ProductsMousePressed

    private void button_CustomerDropdownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_CustomerDropdownActionPerformed
        Dropdown(button_CustomerDropdown, panel_Customer, panel_CustomerSelect);
    }//GEN-LAST:event_button_CustomerDropdownActionPerformed

    private void button_ProductDatasDropdownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_ProductDatasDropdownActionPerformed
        Dropdown(button_ProductDatasDropdown, panel_ProductDatas, panel_Product);
    }//GEN-LAST:event_button_ProductDatasDropdownActionPerformed

    private void button_SummaryDropdownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_SummaryDropdownActionPerformed
        Dropdown(button_SummaryDropdown, panel_Summary, panel_SumDatas);
    }//GEN-LAST:event_button_SummaryDropdownActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        node.putInt("left", getX());
        node.putInt("top", getY());
        node.putInt("width", getWidth());
        node.putInt("height", getHeight());
    }//GEN-LAST:event_formWindowClosed

    private void label_DropdownMaturityDateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_label_DropdownMaturityDateMouseClicked
        CalendarDialog cd = new CalendarDialog(null, label_MaturityDate);
    }//GEN-LAST:event_label_DropdownMaturityDateMouseClicked

    private void label_DropdownCompletionDateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_label_DropdownCompletionDateMouseClicked
        CalendarDialog cd = new CalendarDialog(null, label_CompletionDate);
    }//GEN-LAST:event_label_DropdownCompletionDateMouseClicked

    private void textField_CustomerNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_textField_CustomerNameMouseClicked
        UgyfelListaDialog ugyfelListaDialog = new UgyfelListaDialog();
        
        if(ugyfelListaDialog.getReturnStatus() == UgyfelListaDialog.RET_OK)
        {
            invoice.setCustomer(new Customer(ugyfelListaDialog.getId()));
            
            textField_CustomerName.setText(invoice.getCustomer().getName());
            comboBox_PaymentMethod.setSelectedIndex(invoice.getCustomer().getPaymentMethod());
            
            if(invoice.getCustomer().getPaymentMethodRequired())
            {
                comboBox_PaymentMethod.setEnabled(false);
            }
            else
            {
                comboBox_PaymentMethod.setEnabled(true);
            }
            
            UpdateMaturityDate();
        }
    }//GEN-LAST:event_textField_CustomerNameMouseClicked

    private void button_ExitSummaryDialogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_ExitSummaryDialogActionPerformed
        dialog_Summary.setVisible(false);
    }//GEN-LAST:event_button_ExitSummaryDialogActionPerformed

    private void button_NewMeasureOfUnitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_NewMeasureOfUnitActionPerformed
        NewMeasureOfUnitView view = new NewMeasureOfUnitView();
        view.Open();
        
        if(view.getReturnStatus() == NewMeasureOfUnitView.RET_OK)
        {
            UpdateMeasureOfUnitComboBox();
        }
    }//GEN-LAST:event_button_NewMeasureOfUnitActionPerformed

    private void comboBox_PrinterItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboBox_PrinterItemStateChanged
        PrinterGetSet printer = new PrinterGetSet();
        printer.setPrinterName(String.valueOf(comboBox_Printer.getSelectedItem()));
    }//GEN-LAST:event_comboBox_PrinterItemStateChanged

    private void button_ProductSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_ProductSelectActionPerformed
        TermekListaDialog tld = new TermekListaDialog();

        if(tld.getReturnStatus() == TermekListaDialog.RET_OK)
        {
            //currentProduct = tld.getProduct();
            SetProductFields();
            //CalculateByQuantity();
        }
    }//GEN-LAST:event_button_ProductSelectActionPerformed

    private void textField_ProductNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textField_ProductNameFocusLost
        currentProduct.setProductName(textField_ProductName.getText());
    }//GEN-LAST:event_textField_ProductNameFocusLost

    private void textField_ItemNumberFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textField_ItemNumberFocusLost
        currentProduct.setItemNumber(textField_ItemNumber.getText());
    }//GEN-LAST:event_textField_ItemNumberFocusLost

    private void combobox_MeasureOfUnitItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_combobox_MeasureOfUnitItemStateChanged
        if(!init && !comboboxUpdate)
        {
            String shortName = combobox_MeasureOfUnit.getSelectedItem().toString();

            currentProduct.setMeasureOfUnit(new MeasureOfUnit(shortName));
        }
    }//GEN-LAST:event_combobox_MeasureOfUnitItemStateChanged

    private void textField_VtszTeszorFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textField_VtszTeszorFocusLost
        currentProduct.setVtszTeszor(textField_VtszTeszor.getText());
    }//GEN-LAST:event_textField_VtszTeszorFocusLost

    private void textField_UnitPriceFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textField_UnitPriceFocusGained
        String numberOnly = Functions.csakszam(textField_UnitPrice.getText(), 10, true);
        
        focusThread = new FocusThread(textField_UnitPrice, numberOnly);
    }//GEN-LAST:event_textField_UnitPriceFocusGained

    private void textField_UnitPriceFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textField_UnitPriceFocusLost
        if(Functions.Valid(textField_UnitPrice))
        {
            String numberOnly = Functions.csakszam(textField_UnitPrice.getText(), 10, true);

            currentProduct.setUnitPrice(Double.parseDouble(numberOnly));
            
            focusThread.Update();
        }
        
        focusThread.Stop();
    }//GEN-LAST:event_textField_UnitPriceFocusLost

    private void textField_UnitPriceKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textField_UnitPriceKeyReleased
        String numberOnly = Functions.csakszam(textField_UnitPrice.getText(), 10, true);
        
        focusThread.setCount(numberOnly);
    }//GEN-LAST:event_textField_UnitPriceKeyReleased

    private void textField_QuantityFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textField_QuantityFocusGained
        String numberOnly = Functions.csakszam(textField_Quantity.getText(), 10, true);
        
        focusThread = new FocusThread(textField_Quantity, numberOnly);
    }//GEN-LAST:event_textField_QuantityFocusGained

    private void textField_QuantityFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textField_QuantityFocusLost
        if(Functions.Valid(textField_Quantity))
        {
            String numberOnly = Functions.csakszam(textField_Quantity.getText(), 10, true);

            currentProduct.setQuantity(Double.parseDouble(numberOnly));
            focusThread.Update();
        }
        
        focusThread.Stop();
    }//GEN-LAST:event_textField_QuantityFocusLost

    private void textField_QuantityKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textField_QuantityKeyReleased
        String numberOnly = Functions.csakszam(textField_Quantity.getText(), 10, true);
        
        focusThread.setCount(numberOnly);
    }//GEN-LAST:event_textField_QuantityKeyReleased

    private void textField_NetPriceFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textField_NetPriceFocusGained
        String numberOnly = Functions.csakszam(textField_NetPrice.getText(), 10, true);
        
        focusThread = new FocusThread(textField_NetPrice, numberOnly);
    }//GEN-LAST:event_textField_NetPriceFocusGained

    private void textField_NetPriceFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textField_NetPriceFocusLost
        if(Functions.Valid(textField_NetPrice))
        {
            String numberOnly = Functions.csakszam(textField_NetPrice.getText(), 10, true);

            currentProduct.setNetPrice(Double.parseDouble(numberOnly));
            focusThread.Update();
        }
        
        focusThread.Stop();
    }//GEN-LAST:event_textField_NetPriceFocusLost

    private void textField_NetPriceKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textField_NetPriceKeyReleased
        String numberOnly = Functions.csakszam(textField_NetPrice.getText(), 10, true);
        
        focusThread.setCount(numberOnly);
    }//GEN-LAST:event_textField_NetPriceKeyReleased

    private void textField_GrossPriceFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textField_GrossPriceFocusGained
        String numberOnly = Functions.csakszam(textField_GrossPrice.getText(), 10, true);
        
        focusThread = new FocusThread(textField_GrossPrice, numberOnly);
    }//GEN-LAST:event_textField_GrossPriceFocusGained

    private void textField_GrossPriceFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textField_GrossPriceFocusLost
        if(Functions.Valid(textField_GrossPrice))
        {
            String numberOnly = Functions.csakszam(textField_GrossPrice.getText(), 10, true);

            currentProduct.setGrossPrice(Double.parseDouble(numberOnly));
            focusThread.Update();
        }
        
        focusThread.Stop();
    }//GEN-LAST:event_textField_GrossPriceFocusLost

    private void textField_GrossPriceKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textField_GrossPriceKeyReleased
        String numberOnly = Functions.csakszam(textField_GrossPrice.getText(), 10, true);
        
        focusThread.setCount(numberOnly);
    }//GEN-LAST:event_textField_GrossPriceKeyReleased

    private void label_MaturityDateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_label_MaturityDateMouseClicked
        CalendarDialog cd = new CalendarDialog(null, label_MaturityDate);
    }//GEN-LAST:event_label_MaturityDateMouseClicked

    private void label_CompletionDateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_label_CompletionDateMouseClicked
        CalendarDialog cd = new CalendarDialog(null, label_CompletionDate);
    }//GEN-LAST:event_label_CompletionDateMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private customs.CustomButton button_AddProduct;
    private customs.CustomButton button_ChangeCurrency;
    private customs.CustomButton button_CustomerDatas;
    private customs.CustomButton button_CustomerDropdown;
    private customs.CustomButton button_DeleteProductFee;
    private customs.CustomButton button_Evacuate;
    private customs.CustomButton button_ExitChangeCurrencyDialog;
    private customs.CustomButton button_ExitSummaryDialog;
    private customs.CustomButton button_NewMeasureOfUnit;
    private customs.CustomButton button_Preview;
    private customs.CustomButton button_ProductDatasDropdown;
    private customs.CustomButton button_ProductFee;
    private customs.CustomButton button_ProductSelect;
    private customs.CustomButton button_SaveAndPrint;
    private customs.CustomButton button_SaveAsInvoice;
    private customs.CustomButton button_Summary;
    private customs.CustomButton button_SummaryDropdown;
    private customs.CustomButton button_SupplierDatas;
    private customs.CustomButton button_VtszTeszorSelect;
    private javax.swing.JCheckBox checkBox_PDF;
    private javax.swing.JCheckBox checkBox_Print;
    private javax.swing.JCheckBox checkBox_Takeover;
    private customs.CustomCombobox comboBox_Currency;
    private customs.CustomCombobox comboBox_PaymentMethod;
    private customs.CustomCombobox comboBox_Printer;
    private customs.CustomCombobox comboBox_Supplier;
    private customs.CustomCombobox comboBox_TakeoverType;
    private customs.CustomCombobox comboBox_Vat;
    private customs.CustomCombobox combobox_MeasureOfUnit;
    private javax.swing.JDialog dialog_ChangeCurrency;
    private javax.swing.JDialog dialog_Summary;
    private imagepanel.ImagePanel imagePanel_CommentAndFooter;
    private imagepanel.ImagePanel imagePanel_HeaderAndProductDatas;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel label_AmountUnits;
    private javax.swing.JLabel label_CentralParity;
    private javax.swing.JLabel label_Comment;
    private javax.swing.JLabel label_CompletionDate;
    private javax.swing.JLabel label_CurrencyChangeText;
    private javax.swing.JLabel label_CurrencyCurrency;
    private javax.swing.JLabel label_Customer;
    private javax.swing.JLabel label_Dates;
    private javax.swing.JLabel label_Discount;
    private javax.swing.JLabel label_DropdownCompletionDate;
    private javax.swing.JLabel label_DropdownIssueDate;
    private javax.swing.JLabel label_DropdownMaturityDate;
    private javax.swing.JLabel label_Footer;
    private javax.swing.JLabel label_GrossPrice;
    private javax.swing.JLabel label_GrossSummary;
    private javax.swing.JLabel label_IE;
    private javax.swing.JLabel label_InvoiceNumber;
    private javax.swing.JLabel label_InvoiceTitle;
    private javax.swing.JLabel label_IssueDate;
    private javax.swing.JLabel label_ItemNumber;
    private javax.swing.JLabel label_MaturityDate;
    private javax.swing.JLabel label_NetPrice;
    private javax.swing.JLabel label_NetSummary;
    private javax.swing.JLabel label_NewCurrency;
    private javax.swing.JLabel label_Pay;
    private javax.swing.JLabel label_Percent;
    private javax.swing.JLabel label_PriceSum;
    private javax.swing.JLabel label_Printer;
    private javax.swing.JLabel label_Printing;
    private javax.swing.JLabel label_ProductDatas;
    private javax.swing.JLabel label_ProductName;
    private javax.swing.JLabel label_Products;
    private javax.swing.JLabel label_Quantity;
    private javax.swing.JLabel label_SumGross;
    private javax.swing.JLabel label_SumNet;
    private javax.swing.JLabel label_SumVat;
    private javax.swing.JLabel label_Summary;
    private javax.swing.JLabel label_SummaryDropdown;
    private javax.swing.JLabel label_Supplier;
    private javax.swing.JLabel label_TotalProductFee;
    private javax.swing.JLabel label_UnitPrice;
    private javax.swing.JLabel label_Valuta1;
    private javax.swing.JLabel label_Valuta10;
    private javax.swing.JLabel label_Valuta11;
    private javax.swing.JLabel label_Valuta2;
    private javax.swing.JLabel label_Valuta3;
    private javax.swing.JLabel label_Valuta4;
    private javax.swing.JLabel label_Valuta5;
    private javax.swing.JLabel label_Valuta6;
    private javax.swing.JLabel label_Valuta7;
    private javax.swing.JLabel label_Valuta8;
    private javax.swing.JLabel label_Valuta9;
    private javax.swing.JLabel label_Vat;
    private javax.swing.JLabel label_VatSummary;
    private javax.swing.JLabel label_VtszTeszor;
    private javax.swing.JMenuItem menuItem_Delete;
    private javax.swing.JMenuItem menuItem_Edit;
    private javax.swing.JMenuItem menuItem_VtszTeszor;
    private javax.swing.JPanel panel_Comment;
    private javax.swing.JPanel panel_CommentAndFooter;
    private javax.swing.JPanel panel_CurrencyChange;
    private javax.swing.JPanel panel_Customer;
    private javax.swing.JPanel panel_CustomerSelect;
    private javax.swing.JPanel panel_Dates;
    private javax.swing.JPanel panel_Footer;
    private javax.swing.JPanel panel_HeaderAndProductDatas;
    private javax.swing.JPanel panel_PriceSum;
    private javax.swing.JPanel panel_PriceSummary;
    private javax.swing.JPanel panel_PrintAndPDF;
    private javax.swing.JPanel panel_Printing;
    private javax.swing.JPanel panel_Product;
    private javax.swing.JPanel panel_ProductDatas;
    private javax.swing.JPanel panel_Products;
    private javax.swing.JPanel panel_Sum;
    private javax.swing.JPanel panel_SumDatas;
    private javax.swing.JPanel panel_Summary;
    private javax.swing.JPanel panel_Supplier;
    private imagepanel.ImagePanel panel_background;
    private javax.swing.JPopupMenu popupMenu_InvoiceProduct;
    private javax.swing.JScrollPane scrollPane_Comment;
    private javax.swing.JScrollPane scrollPane_Footer;
    private javax.swing.JScrollPane scrollPane_Products;
    private javax.swing.JTable table_Products;
    private javax.swing.JTextField textField_CentralParity;
    private javax.swing.JTextArea textField_Comment;
    private javax.swing.JTextField textField_Copy;
    private javax.swing.JTextField textField_CurrentCurrency;
    private javax.swing.JTextField textField_CustomerName;
    private javax.swing.JTextField textField_Discount;
    private javax.swing.JTextArea textField_Footer;
    private javax.swing.JTextField textField_GrossPrice;
    private javax.swing.JTextField textField_GrossSummary;
    private javax.swing.JLabel textField_IE;
    private javax.swing.JTextField textField_ItemNumber;
    private javax.swing.JTextField textField_NetPrice;
    private javax.swing.JTextField textField_NetSummary;
    private javax.swing.JTextField textField_NewCurrency;
    private javax.swing.JTextField textField_Pay;
    private javax.swing.JTextField textField_ProductName;
    private javax.swing.JTextField textField_Quantity;
    private javax.swing.JTextField textField_SumGross;
    private javax.swing.JTextField textField_SumNet;
    private javax.swing.JTextField textField_SumVat;
    private javax.swing.JTextField textField_TotalProductFee;
    private javax.swing.JTextField textField_UnitPrice;
    private javax.swing.JTextField textField_VatSummary;
    private javax.swing.JTextField textField_VtszTeszor;
    // End of variables declaration//GEN-END:variables
    
    class VatTooltip extends BasicComboBoxRenderer
    {
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
        {
            if(isSelected)
            {
                setBackground(new Color(0, 87, 124));
                setForeground(Color.WHITE);
                
                if(index > -1)
                {
                    list.setToolTipText(tooltipsVat[index]);
                }
            }
            else
            {
                setBackground(new Color(0, 43, 64));
                setForeground(Color.WHITE);
            }
            
            setFont(list.getFont());
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }
    
    class TakeoverTooltip extends BasicComboBoxRenderer
    {
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
        {
            if(isSelected)
            {
                setBackground(new Color(0, 87, 124));
                setForeground(Color.WHITE);
                
                if(index > -1)
                {
                    list.setToolTipText(tooltipsTakeover[index]);
                }
            }
            else
            {
                setBackground(new Color(0, 43, 64));
                setForeground(Color.WHITE);
            }
            
            setFont(list.getFont());
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }
    
    class MeasureOfUnitTooltip extends BasicComboBoxRenderer
    {
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
        {
            if(isSelected)
            {
                setBackground(new Color(0, 87, 124));
                setForeground(Color.WHITE);
                
                if(index > -1)
                {
                    list.setToolTipText(tooltipsMeasureOfUnits[index]);
                }
            }
            else
            {
                setBackground(new Color(0, 43, 64));
                setForeground(Color.WHITE);
            }
            
            setFont(list.getFont());
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }
    
    class ResizeThread extends Thread
    {
        private int count = 0;

        public void setCount()
        {
            this.count = 1;
        }

        public ResizeThread()
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
                    panel_background.setImage(background.Resize(getWidth()));
                    count = 0;
                }
            }
        }
    }
    
    class InvoiceThread extends Thread
    {
        public InvoiceThread()
        {
            start();
        }
        
        @Override
        public void run()
        {
            if(invoice.getType() == Invoice.SKETCH && invoice.getInvoiceType() == Invoice.InvoiceType.MODIFICATION)
            {
                SaveInvoice();
                                
                dialog_Summary.setVisible(false);
                Close(RET_OK);
            }
            else
            {
                button_SaveAndPrint.setEnabled(false);
                Calendar now = Calendar.getInstance();

                Label l = (Label) comboBox_Supplier.getSelectedItem();
                int supplierID = Integer.parseInt(l.getId());
                
                String newNumber = "";
                
                if(invoice.getType() == Invoice.SKETCH && invoice.getInvoiceType() == Invoice.InvoiceType.STORNO)
                {
                    Object [][] s = InvoiceNumber.getNumber(Invoice.INVOICE, supplierID);
                    newNumber = now.get(Calendar.YEAR) + "/" + (Integer.parseInt(String.valueOf(s[0][0])) + 1);

                    if(!s[0][2].toString().isEmpty())
                    {
                        newNumber = s[0][2].toString() + " " + newNumber;
                    }
                }
                else if(invoice.getInvoiceType() == Invoice.InvoiceType.ORIGINAL)
                {
                    newNumber = invoice.getInvoiceNumber();
                }
                else
                {
                    Object [][] s = InvoiceNumber.getNumber(invoice.getType(), supplierID);
                    newNumber = now.get(Calendar.YEAR) + "/" + (Integer.parseInt(String.valueOf(s[0][0])) + 1);

                    if(!s[0][2].toString().isEmpty())
                    {
                        newNumber = s[0][2].toString() + " " + newNumber;
                    }
                }
                
                if(invoice.getInvoiceNumber().matches(newNumber))
                {
                    SaveInvoice();
                    
                    UpdatePixiProducts();

                    dialog_InProgress.setVisible(false);
                    dialog_Summary.setVisible(false);
                    //Close(RET_OK);
                }
                else
                {
                    invoice.setInvoiceNumber(newNumber);
                    HibaDialog h = new HibaDialog("A számla sorszáma időközben megváltozott. Új sorszám: " + invoice.getInvoiceNumber(), "Ok", "");
                    button_SaveAndPrint.setEnabled(true);
                    dialog_InProgress.setVisible(false);
                }
            }
        }
    }
    
    class FocusThread extends Thread
    {
        int count = 1;
        
        String text = "";
        String change;
        
        JTextField field = null;
        
        boolean run = true;

        public FocusThread()
        {
            start();
        }
        
        public FocusThread(JTextField field, String text)
        {
            System.err.println("\nFocusThread(JTextField field, String text)");
            field.selectAll();
            this.field = field;
            this.text = text;
            change = text;
            
            start();
        }
        
        public void Stop()
        {
            System.err.println("FocusThread/Stop()");

            field.setText(Formatter.currencyFormat(Double.parseDouble(text)));
            
            button_Summary.requestFocusInWindow();
            run = false;
        }
        
        public void setCount(String text)
        {
            count = 1;
            this.text = text;
        }
        
        public void Update()
        {
            System.err.println("FocusThread/Update()");
            System.err.println("field.getText(): " + field.getText() + " change: " + change);
            
            if(!field.getText().equals(change))
            {
                SetCalculatedProductFields();
            }
        }

        @Override
        public void run()
        {
            while(run)
            {
                System.err.println("run()");
                try
                {
                    Thread.sleep(1000);
                    
                    count++;
                }
                catch (Exception ex){}
                
                if (count == 5)
                {
                    Stop();
                }
            }
        }
    }
    
    //GETTERS
    public int getReturnStatus()
    {
        return returnStatus;
    }
    
    public double getQuantity()
    {
        return Double.parseDouble(textField_Quantity.getText());
    }
    
    public double getUnitPrice()
    {
        String text = Functions.csakszam(textField_UnitPrice.getText(), 10, true);
        
        return (text.length() == 0 ? 0 : Double.parseDouble(text));
    }

    public Invoice getInvoice()
    {
        return invoice;
    }
}