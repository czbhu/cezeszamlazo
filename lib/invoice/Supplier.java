package invoice;

import database.Query;
import nav.NAV;


/**
 * @author Tomy
 */

public class Supplier
{
    public static final String TABLE = "szamlazo_suppliers";
    
    private int id;
    
    private String name = "";
    private String countryCode = "";
    private String country = "";
    private String postalCode = "";
    private String city = "";
    private String street = "";
    private String publicPlace = "";
    private String houseNumber = "";
    private String taxNumber = "";
    private String euTaxNumber = "";
    private String bankAccountNumber = "";
    private String comment = "";
    private String invoiceFooter = "";
    
    public Supplier()
    {
        
    }
    
    public Supplier(int supplierID)
    {
        if(supplierID != 0)
        {
            this.id = supplierID;

            Query query = new Query.QueryBuilder()
                .select(""
                    + "name, "
                    + "countryCode, "
                    + "country, "
                    + "postalCode, "
                    + "city, "
                    + "street, "
                    + "publicPlace, "
                    + "houseNumber, "
                    + "taxNumber, "
                    + "euTaxNumber, "
                    + "bankAccountNumber, "
                    + "comment, "
                    + "invoiceFooter")
                .from(TABLE)
                .where("id = '" + id + "'")
                .build();
            Object [][] supplierDatas = NAV.db.select(query.getQuery());

            name = supplierDatas[0][0].toString();
            countryCode = supplierDatas[0][1].toString();
            country = supplierDatas[0][2].toString();
            postalCode = supplierDatas[0][3].toString();
            city = supplierDatas[0][4].toString();
            street = supplierDatas[0][5].toString();
            publicPlace = supplierDatas[0][6].toString();
            houseNumber = supplierDatas[0][7].toString();
            taxNumber = supplierDatas[0][8].toString();
            euTaxNumber = supplierDatas[0][9].toString();
            bankAccountNumber = supplierDatas[0][10].toString();
            comment = supplierDatas[0][11].toString();
            invoiceFooter = supplierDatas[0][12].toString();
        }
    }
   
    public Supplier(String indentifier, String table)
    {
        Query query = new Query.QueryBuilder()
            .select("supplierID, "
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
                + "supplierInvoiceFooter")
            .from(table)
            .where("indentifier = " + indentifier)
            .build();
        Object [][] supplier = NAV.db.select(query.getQuery());
        
        id = Integer.parseInt(supplier[0][0].toString());
        name = supplier[0][1].toString();
        countryCode = supplier[0][2].toString();
        postalCode = supplier[0][3].toString();
        city = supplier[0][4].toString();
        street = supplier[0][5].toString();
        publicPlace = supplier[0][6].toString();
        houseNumber = supplier[0][7].toString();
        taxNumber = supplier[0][8].toString();
        euTaxNumber = supplier[0][9].toString();
        bankAccountNumber = supplier[0][10].toString();
        comment = supplier[0][11].toString();
        invoiceFooter = supplier[0][12].toString();
    }
    
    public void Load()
    {
        Query query = new Query.QueryBuilder()
            .select("id, "
                + "name, "
                + "countryCode, "
                + "country, "
                + "postalCode, "
                + "city, "
                + "street, "
                + "publicPlace, "
                + "houseNumber, "
                + "taxNumber, "
                + "euTaxnumber, "
                + "bankAccountNumber, "
                + "comment, "
                + "invoiceFooter")
            .from(TABLE)
            .where("id = " + id)
            .build();
        Object [][] supplierDatas = NAV.db.select(query.getQuery());
        
        id = Integer.parseInt(supplierDatas[0][0].toString());
        name = supplierDatas[0][1].toString();
        countryCode = supplierDatas[0][2].toString();
        country = supplierDatas[0][3].toString();
        postalCode = supplierDatas[0][4].toString();
        city = supplierDatas[0][5].toString();
        street = supplierDatas[0][6].toString();
        publicPlace = supplierDatas[0][7].toString();
        houseNumber = supplierDatas[0][8].toString();
        taxNumber = supplierDatas[0][9].toString();
        euTaxNumber = supplierDatas[0][10].toString();
        bankAccountNumber = supplierDatas[0][11].toString();
        comment = supplierDatas[0][12].toString();
        invoiceFooter = supplierDatas[0][13].toString();
    }
    
    public void Update()
    {
        String query = "UPDATE " + TABLE + " SET "
            + "name = ?, "
            + "countryCode = ?, "
            + "country = ?, "
            + "postalCode = ?, "
            + "city = ?, "
            + "street = ?, "
            + "publicPlace = ?, "
            + "houseNumber = ?, "
            + "taxNumber = ?, "
            + "euTaxNumber = ?, "
            + "bankAccountNumber = ?, "
            + "comment = ?, "
            + "invoiceFooter = ? "
            + "WHERE id = " + id;
        Object [] o = new Object[13];
        
        o[0] = name;
        o[1] = countryCode;
        o[2] = country;
        o[3] = postalCode;
        o[4] = city;
        o[5] = street;
        o[6] = publicPlace;
        o[7] = houseNumber;
        o[8] = taxNumber;
        o[9] = euTaxNumber;
        o[10] = bankAccountNumber;
        o[11] = comment;
        o[12] = invoiceFooter;
        
        NAV.db.insert(query, o, 13);
    }
    
    public void Insert()
    {
        String query = "INSERT INTO " + TABLE + " ("
            + "name, "
            + "countryCode, "
            + "country, "
            + "postalCode, "
            + "city, "
            + "street, "
            + "publicPlace, "
            + "houseNumber, "
            + "taxNumber, "
            + "euTaxNumber, "
            + "bankAccountNumber, "
            + "comment, "
            + "invoiceFooter) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        Object [] o = new Object[13];
        
        o[0] = name;
        o[1] = countryCode;
        o[2] = country;
        o[3] = postalCode;
        o[4] = city;
        o[5] = street;
        o[6] = publicPlace;
        o[7] = houseNumber;
        o[8] = taxNumber;
        o[9] = euTaxNumber;
        o[10] = bankAccountNumber;
        o[11] = comment;
        o[12] = invoiceFooter;
        
        NAV.db.insert(query, o, 13);
    }
    
    public int GetID()
    {
        Query query = new Query.QueryBuilder()
            .select("id")
            .from(TABLE)
            .where("name = '" + name + "'")
            .build();
        Object [][] ids = NAV.db.select(query.getQuery());
        
        return Integer.parseInt(ids[0][0].toString());
    }

    public static int getSerializationIDByID(String id)
    {
        Query query = new Query.QueryBuilder()
            .select("serializationID")
            .from(TABLE)
            .where("id = '" + id + "'")
            .build();
        Object [][] serId = NAV.db.select(query.getQuery());
        
        return Integer.parseInt(serId[0][0].toString());
    }
    
    public static Object[][] getDefaultSupplier(String table, String indentifier)
    {
        Query query = new Query.QueryBuilder()
            .select("serializationID")
            .from(table)
            .where("indentifier = " + indentifier)
            .build();
        Object [][] serializationID = NAV.db.select(query.getQuery());
        
        Object [][] supplier = getSupplier(Integer.parseInt(serializationID[0][0].toString()));
        
        return supplier;
    }
    
    public static Object[][] getSupplier(int serializationID)
    {
        Query query = new Query.QueryBuilder()
            .select("szsz.id, CONCAT(IF(szszs.prefix != '', CONCAT(szszs.prefix, ' - '), ''), szszs.appellation, IF(szsz.foreignCurrency = 1, ' (deviza adatokkal)', '')), szsz.foreignCurrency, szsz.serializationID")
            .from(TABLE + " AS szsz INNER JOIN " + InvoiceNumber.TABLE + " AS szszs ON szszs.id = szsz.serializationID ")
            .where("szsz.serializationID = " + serializationID)
            .build();
        Object [][] suppliers = NAV.db.select(query.getQuery());
        
        return suppliers;
    }
    
    public static Object [][] getDefaultSupplierByUserID(int userID)
    {
        int defaultCompanyID = getDefaultCompanyIDByID(userID);
        Object [][] supplier = getSupplierByID(defaultCompanyID);
        
        return supplier;
    }
    
    public static Object[][] getSupplierByID(int supplierID)
    {
        Query query = new Query.QueryBuilder()
            .select("szsz.id, CONCAT(IF(szszs.prefix != '', CONCAT(szszs.prefix, ' - '), ''), szszs.appellation, IF(szsz.foreignCurrency = 1, ' (deviza adatokkal)', '')), szsz.foreignCurrency, szsz.serializationID")
            .from(TABLE + " AS szsz INNER JOIN " + InvoiceNumber.TABLE + " AS szszs ON szszs.id = szsz.serializationID ")
            .where("szsz.id = " + supplierID)
            .build();
        Object [][] suppliers = NAV.db.select(query.getQuery());
        
        return suppliers;
    }
    
    private static int getDefaultCompanyIDByID(int id)
    {
        Query query = new Query.QueryBuilder()
            .select("defaultCompanyID")
            .from("szamlazo_users")
            .where("id = " + id)
            .build();
        Object [][] defaultCompanyIDs = NAV.db.select(query.getQuery());
        
        return Integer.parseInt(defaultCompanyIDs[0][0].toString());
    }
    
    public static Object[][] getSuppliers(Object[][] serializationIDs)
    {
        String whereText = "";
        
        for(int i = 0; i < serializationIDs.length; i++)
        {
            whereText += "szsz.serializationID = " + serializationIDs[i][0] + " || ";
        }
        
        whereText += "0";
        
        Query query = new Query.QueryBuilder()
            .select("szsz.id, CONCAT(IF(szszs.prefix != '', CONCAT(szszs.prefix, ' - '), ''), szszs.appellation, IF(szsz.foreignCurrency = 1, ' (deviza adatokkal)', '')), szsz.foreignCurrency, szsz.serializationID")
            .from(TABLE + " AS szsz INNER JOIN " + InvoiceNumber.TABLE + " AS szszs ON szszs.id = szsz.serializationID ")
            .where(whereText)
            .build();
        Object [][] suppliers = NAV.db.select(query.getQuery());
        
        return suppliers;
    }
    
    public static Object[][] getSerializationIDsByIDs(String [] companyIDs)
    {
        String whereText = "";
        
        for(int i = 0; i < companyIDs.length; i++)
        {
            whereText += "id = " + companyIDs[i] + " || ";
        }
        
        whereText += "0";
        
        Query query = new Query.QueryBuilder()
            .select("serializationID")
            .from(TABLE)
            .where(whereText)
            .build();
        Object [][] serializationIDs = NAV.db.select(query.getQuery());
        
        return serializationIDs;
    }
    
    //GETTERS
    public int getId()
    {
        return id;
    }
    
    public String getName()
    {
        return name;
    }

    public String getCountryCode()
    {
        return countryCode;
    }

    public String getCountry()
    {
        return country;
    }

    public String getPostalCode()
    {
        return postalCode;
    }

    public String getCity()
    {
        return city;
    }

    public String getStreet()
    {
        return street;
    }

    public String getPublicPlace()
    {
        return publicPlace;
    }

    public String getHouseNumber()
    {
        return houseNumber;
    }
    
    public String getAddress()
    {
        return street + " " + publicPlace + " " + houseNumber;
    }

    public String getTaxNumber()
    {
        return taxNumber;
    }

    public String getEuTaxNumber()
    {
        return euTaxNumber;
    }

    public String getBankAccountNumber()
    {
        return bankAccountNumber;
    }

    public String getComment()
    {
        return comment;
    }

    public String getInvoiceFooter()
    {
        return invoiceFooter;
    }
    
    //SETTERS
    
    public void setId(int id)
    {
        this.id = id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setCountryCode(String countryCode)
    {
        this.countryCode = countryCode;
    }
    
    public void setCountry(String country)
    {
        this.country = country;
    }

    public void setPostalCode(String postalCode)
    {
        this.postalCode = postalCode;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public void setStreet(String street)
    {
        this.street = street;
    }

    public void setPublicPlace(String publicPlace)
    {
        this.publicPlace = publicPlace;
    }

    public void setHouseNumber(String houseNumber)
    {
        this.houseNumber = houseNumber;
    }

    public void setTaxNumber(String taxNumber)
    {
        this.taxNumber = taxNumber;
    }

    public void setEuTaxNumber(String euTaxNumber)
    {
        this.euTaxNumber = euTaxNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber)
    {
        this.bankAccountNumber = bankAccountNumber;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public void setInvoiceFooter(String invoiceFooter)
    {
        this.invoiceFooter = invoiceFooter;
    }

    public int getDefaultMaturityDays()
    {
        Query query = new Query.QueryBuilder()
            .select("defaultMaturityDays")
            .from(TABLE)
            .where("id = " + id)
            .build();
        Object [][] maturities = NAV.db.select(query.getQuery());
        
        return Integer.parseInt(maturities[0][0].toString());
    }
}