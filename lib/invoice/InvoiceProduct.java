package invoice;

import database.Query;
import functions.NumericFunctions;
import nav.NAV;

/**
 * @author Tomy
 */

public class InvoiceProduct
{
    public static final String TABLE_INVOICE = "szamlazo_invoice_products";
    public static final String TABLE_PROFORM = "szamlazo_pro_form_products";
    public static final String TABLE_SKETCH = "szamlazo_sketches_products";
    
    private int row;
    private int id;
    private String type = "";
    private String indentifier;
    private String name;
    private String itemNumber;
    private double quantity;
    private MeasureOfUnit measureOfUnit;
    private double unitPrice;
    private double netPrice;
    private int vatPercent;
    private String vatLabel;
    private int discount = 0;
    private String vtszTeszor;
    
    private TermekDij productFee;
    private int modifiedProductID = 0;  /*Only used in case of INVOICE.Invoice*/
    //private boolean foreignCurrency;
    
    private String table_product;
    private String table_productFee;
    
    public InvoiceProduct()
    {
    
    }
    
    public InvoiceProduct(String name, String itemNumber, double quantity, MeasureOfUnit measureOfUnit, int discount, String vtszTeszor, double unitPrice, String vatLabel, double vatPercent)
    {
        this.name = name;
        this.itemNumber = itemNumber;
        this.quantity = quantity;
        this.measureOfUnit = measureOfUnit;
        this.unitPrice = unitPrice;
        this.netPrice = unitPrice * quantity;
        this.vatPercent = (int)vatPercent;
        this.vatLabel = vatLabel;
        this.discount = discount;
        this.vtszTeszor = vtszTeszor;

        this.productFee = null;
    }
    
    public InvoiceProduct(int id, String table_product, String table_productFee)
    {
        this.id = id;
        this.table_product = table_product;
        this.table_productFee = table_productFee;
        
        boolean t = table_product.equals(TABLE_INVOICE);
       
        Query query = new Query.QueryBuilder()
            .select("productName, "
                + (t ? "type, " : "")
                + "itemNumber, "
                + "quantity, "
                + "amountUnits, "
                + "unitPrice, "
                + "netPrice, "
                + "vatPercent, "
                + "vatLabel, "
                + "discount, "
                + "vtszTeszor"
                + (t ? ", modifiedProduct" : ""))
            .from(table_product)
            .where("id = " + id)
            .build();
        //System.err.println(query);
        Object[][] product = NAV.db.select(query.getQuery());
        
        int i = 0;
        
        name = product[0][i++].toString();
        
        if(t)
        {
            type = product[0][i++].toString();
        }
        
        itemNumber = product[0][i++].toString();
        quantity = Double.parseDouble(product[0][i++].toString());
        
        measureOfUnit = new MeasureOfUnit(product[0][i++].toString());
        unitPrice = Double.parseDouble(product[0][i++].toString());
        netPrice = Double.parseDouble(product[0][i++].toString());
        vatPercent = Integer.parseInt(product[0][i++].toString());
        vatLabel = product[0][i++].toString();
        discount = Integer.parseInt(product[0][i++].toString());
        vtszTeszor = product[0][i++].toString();
        
        GetProductFee();
    }
    
    private void GetProductFee()
    {
        Query query = new Query.QueryBuilder()
            .select("id")
            .from(table_productFee)
            .where("productID = " + id)
            .build();
        Object [][] productFEE = NAV.db.select(query.getQuery());
        
        if(productFEE.length > 0)
        {
            productFee = new TermekDij(productFEE[0][0].toString(), table_productFee);
        }
    }
    
    public void Save(String indentifier, String invoiceNumber, int paymentMethod, String customerName, boolean isForeignCurrency)
    {
        this.indentifier = indentifier;
        
        boolean t = table_product.equals("szamlazo_invoice_products"); //jelzi, hogy a számlák táblába kell-e menteni, mivel ilyenkor megváltozik a query
        
        String query = "INSERT INTO " + table_product + " ("
            + "indentifier, "
            + "invoiceNumber, "
            + "productName, "
            + (t ? "type, " : "")
            + "itemNumber, "
            + "quantity, "
            + "amountUnits, "
            + "unitPrice, "
            + "netPrice, "
            + "vatPercent, "
            + "vatLabel, "
            + "discount, "
            + "vtszTeszor"
            + (t ? ",modifiedProduct)" : ")")
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?" + (t ? ", ?, ?)" : ")");
        
        Object[] values = getValues(t, invoiceNumber);
        
        NAV.db.insert(query, values, values.length);
        
        GetNewProductID();
        
        if(productFee != null)
        {
            productFee.Save(id, table_productFee);
        }
        
        if (paymentMethod == 0)
        {
            Object[] kifizetesObject = new Object[5];
            kifizetesObject[0] = itemNumber;
            kifizetesObject[1] = customerName;
            kifizetesObject[2] = getGrossPrice(isForeignCurrency);
            // ha Balázs a létrehozó akkor a "Balázs kasszába" számolja
            //kifizetesObject[3] = (NAV.user.getId() == 1 ? 1 : 0);
            kifizetesObject[3] = (NAV.userId == 1 ? 1 : 0);
            kifizetesObject[4] = invoiceNumber;

            NAV.pixi.insert("INSERT INTO pixi_kifizetesek (termek_id, nev, datum, osszeg, kassza_tipus, szamla_szam) VALUES (?, ?, NOW(), ?, ?, ?)", kifizetesObject, kifizetesObject.length);
        }
    }
    
    private Object [] getValues(boolean invoice, String invoiceNumber)
    {
        Object [] product = new Object[(invoice ? 14 : 12)];
        int i = 0;
        
        product[i++] = indentifier;
        product[i++] = invoiceNumber;
        product[i++] = name;
        
        if(invoice)
        {
            product[i++] = type;
        }
        
        product[i++] = itemNumber;
        product[i++] = quantity;
        product[i++] = measureOfUnit.getShortName();
        product[i++] = unitPrice;
        product[i++] = netPrice;
        product[i++] = vatPercent;
        product[i++] = vatLabel;
        product[i++] = discount;
        product[i++] = vtszTeszor;
        
        if(invoice)
        {
            product[i++] = modifiedProductID;
        }
        
        return product;
    }
    
    private void GetNewProductID()
    {
        Query query = new Query.QueryBuilder()
            .select("id")
            .from(table_product)
            .where("indentifier = " + indentifier + " AND productName = '" + name + "'")
            .order("id DESC")
            .build();
        Object [][] productIDs = NAV.db.select(query.getQuery());
        
        id = Integer.parseInt(productIDs[0][0].toString());
    }
    
    public void Update()
    {
        String query = "UPDATE " + table_product + " SET "
            + "productName = ?, "
            + "type = ?, "
            + "itemNumber = ?, "
            + "quantity = ?, "
            + "amountUnits = ?, "
            + "unitPrice = ?, "
            + "netPrice = ?, "
            + "vatPercent = ?, "
            + "vatLabel = ?, "
            + "discount = ?, "
            + "vtszTeszor = ? "
            + "WHERE id = " + id;
        
        Object [] values = new Object[11];
        
        values[0] = name;
        values[1] = type;
        values[2] = itemNumber;
        values[3] = quantity;
        //values[4] = amountUnits;
        values[4] = measureOfUnit.getShortName();
        values[5] = unitPrice;
        values[6] = netPrice;
        values[7] = vatPercent;
        values[8] = vatLabel;
        values[9] = discount;
        values[10] = vtszTeszor;
        
        NAV.db.insert(query, values, values.length);
        
        if(productFee != null)
        {
            productFee.Update(id, table_productFee);
        }
    }
    
    //GETTERS
    
    public int getRow()
    {
        return row;
    }

    public int getId() {
        return id;
    }
    
    public String getName()
    {
        return name;
    }

    public String getType() {
        return type;
    }
    
    public String getItemNumber()
    {
        return itemNumber;
    }
    
    public double getQuantity()
    {
        return quantity;
    }
    
    public MeasureOfUnit getMeasureOfUnit()
    {
        return measureOfUnit;
    }

    public int getDiscount()
    {
        return discount;
    }
    
    public String getVtszTeszor()
    {
        return vtszTeszor;
    }
    
    public double getUnitPrice()
    {
        return unitPrice;
    }
    
    public int getVatPercent()
    {
        return vatPercent;
    }
    
    public String getVatLabel()
    {
        return vatLabel;
    }
    
    public TermekDij getProductFee()
    {
        return productFee;
    }
    
    public int getNetPrice(boolean foreignCurrency)
    {
        double net = unitPrice * quantity;
        return NumericFunctions.Round(foreignCurrency ? Math.round(net * 100.0) / 100.0 : Math.round(net));
    }
    
    public int getVatAmount(boolean foreignCurrency)
    {
        double vatAmount = getNetPrice(foreignCurrency) * (vatPercent / 100.0);
        return NumericFunctions.Round(foreignCurrency ? Math.round(vatAmount * 100.0) / 100.0 : Math.round(vatAmount));
    }
    
    public int getGrossPrice(boolean foreignCurrency)
    {
        //System.err.println("Product.java/getGrossPrice(): " + getNetPrice(foreignCurrency) + " + " + getVatAmount(foreignCurrency) + " = " + NumericFunctions.Round(getNetPrice(foreignCurrency) + getVatAmount(foreignCurrency)));
        return NumericFunctions.Round(getNetPrice(foreignCurrency) + getVatAmount(foreignCurrency));
    }

    public int getModifiedProductID()
    {
        return modifiedProductID;
    }
    
    //SETTERS
    public void setRow(int row)
    {
        this.row = row;
    }
    
    public void setProductName(String name)
    {
        this.name = name;
    }
    
    public void setItemNumber(String itemNumber)
    {
        this.itemNumber = itemNumber;
    }
    
    public void setQuantity(String quantity)
    {
        this.quantity = Double.parseDouble(quantity);
    }
    
    public void setQuantity(Double quantity)
    {
        this.quantity = quantity;
    }
    
    public void setMeasureOfUnit(MeasureOfUnit measureOfUnit)
    {
        this.measureOfUnit = measureOfUnit;
    }

    public void setDiscount(int discount)
    {
        this.discount = discount;
    }
    
    public void setVtszTeszor(String vtszTeszor)
    {
        this.vtszTeszor = vtszTeszor;
    }
    
    public void setUnitPrice(double unitPrice)
    {
        this.unitPrice = unitPrice;
    }
    
    public void setProductFee(TermekDij productFee)
    {
        this.productFee = productFee;
    }

    public void setModifiedProductID(int modifiedProductID)
    {
        this.modifiedProductID = modifiedProductID;
    }

    public void setTable_product(String table_product) {
        this.table_product = table_product;
    }

    public void setTable_productFee(String table_productFee) {
        this.table_productFee = table_productFee;
    }

    public void setNetPrice() {
        netPrice = unitPrice * quantity;
    }

    public void setVatPercent(int vatPercent) {
        this.vatPercent = vatPercent;
    }

    public void setVatLabel(String vatLabel) {
        this.vatLabel = vatLabel;
    }
    
    
}