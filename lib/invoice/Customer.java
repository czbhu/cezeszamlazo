package invoice;

import database.Query;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import nav.NAV;

/**
 * @author Tomy
 */

public class Customer
{
    private static final String table_customers = "szamlazo_customers";
    private static final String table_pixiUgyfel = "pixi_ugyfel";
    private final static String table_contact = "szamlazo_contact";
    private static final String table_pixiKapcsolattarto = "pixi_kapcsolattarto";
    
    private int id;
    private String name;
    private String countryCode;
    private String country;
    private String postalCode;
    private String city;
    private String street;
    private String publicPlace;
    private String houseNumber;
    private String telNumber;
    private String email;
    private String taxNumber;
    private String euTaxNumber;
    private String bankAccountNumber;
    
    private boolean showInInvoice;
    private boolean paymentMethodRequired;
    
    private int paymentMethod;
    private int maturityDays;
    
    private int discount;
    
    public Customer()
    {
        id = 0;
        name = "";
        countryCode = "";
        country = "";
        postalCode = "";
        city = "";
        street = "";
        publicPlace = "";
        houseNumber = "";
        telNumber = "";
        email = "";
        taxNumber = "";
        euTaxNumber = "";
        bankAccountNumber = "";
        showInInvoice = false;
        paymentMethod = 0;
        paymentMethodRequired = false;
        maturityDays = 0;
    }
    
    public Customer(int customerID)
    {
        this();

        if(customerID != 0)
        {
            this.id = customerID;

            Query query = new Query.QueryBuilder()
                .select(
                      "name, "
                    + "countryCode, "
                    + "country, "
                    + "postalCode, "
                    + "city, "
                    + "street, "
                    + "publicPlace, "
                    + "houseNumber, "
                    + "telNumber, "
                    + "email, "
                    + "taxNumber, "
                    + "euTaxNumber, "
                    + "bankAccountNumber, "
                    + "showInInvoice, "
                    + "paymentMethod, "
                    + "paymentMethodRequired, "
                    + "maturityDays")
                .from(table_customers)
                .where("id = '" + id + "'")
                .build();
            Object [][] customerDatas = NAV.db.select(query.getQuery());

            name = customerDatas[0][0].toString();
            countryCode = customerDatas[0][1].toString();
            country = customerDatas[0][2].toString();
            postalCode = customerDatas[0][3].toString();
            city = customerDatas[0][4].toString();
            street = customerDatas[0][5].toString();
            publicPlace = customerDatas[0][6].toString();
            houseNumber = customerDatas[0][7].toString();
            telNumber = customerDatas[0][8].toString();
            email = customerDatas[0][9].toString();
            taxNumber = customerDatas[0][10].toString();
            euTaxNumber = customerDatas[0][11].toString();
            bankAccountNumber = customerDatas[0][12].toString();
            showInInvoice = (!customerDatas[0][13].toString().equals("0"));
            paymentMethod = Integer.parseInt(customerDatas[0][14].toString());
            paymentMethodRequired = (!customerDatas[0][15].toString().equals("0"));
            maturityDays = Integer.parseInt(customerDatas[0][16].toString());
        }
    }
    
    public Customer(int customerID, String pixi_database_table)
    {
        this();
        
        if(customerID != 0)
        {
            this.id = customerID;

            Query query = new Query.QueryBuilder()
                .select(
                      "nev, "
                    + "(SELECT CountryCode FROM countries WHERE CountryName = orszag), "
                    + "orszag, "
                    + "irsz, "
                    + "varos, "
                    + "utca, "
                    + "kozterulet, "
                    + "hazszam, "
                    + "telefon, "
                    + "email, "
                    + "adoszam, "
                    + "eu_adoszam, "
                    + "bankszamlaszam, "
                    + "szamlan_megjelenik, "
                    + "fizetesi_mod, "
                    + "fizetesi_mod_kotelezo, "
                    + "esedekesseg")
                .from(pixi_database_table)
                .where("id = '" + id + "'")
                .build();
            Object [][] customerDatas = NAV.pixi.select(query.getQuery());

            name = customerDatas[0][0].toString();
            countryCode = customerDatas[0][1].toString();
            country = customerDatas[0][2].toString();
            postalCode = customerDatas[0][3].toString();
            city = customerDatas[0][4].toString();
            street = customerDatas[0][5].toString();
            publicPlace = customerDatas[0][6].toString();
            houseNumber = customerDatas[0][7].toString();
            telNumber = customerDatas[0][8].toString();
            email = customerDatas[0][9].toString();
            taxNumber = customerDatas[0][10].toString();
            euTaxNumber = customerDatas[0][11].toString();
            bankAccountNumber = customerDatas[0][12].toString();
            showInInvoice = (!customerDatas[0][13].toString().equals("0"));
            paymentMethod = Integer.parseInt(customerDatas[0][14].toString());
            paymentMethodRequired = (!customerDatas[0][15].toString().equals("0"));
            maturityDays = Integer.parseInt(customerDatas[0][16].toString());
        }
    }

    Customer(String indentifier, String table)
    {
        Query query = new Query.QueryBuilder()
            .select("customerID, "
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
                + "maturityDate")
            .from(table)
            .where("indentifier = " + indentifier)
            .build();
        Object [][] customer = NAV.db.select(query.getQuery());
        
        id = Integer.parseInt(customer[0][0].toString());
        name = customer[0][1].toString();
        countryCode = customer[0][2].toString();
        country = "";
        postalCode = customer[0][3].toString();
        city = customer[0][4].toString();
        street = customer[0][5].toString();
        publicPlace = customer[0][6].toString();
        houseNumber = customer[0][7].toString();
        telNumber = customer[0][8].toString();
        email = customer[0][9].toString();
        bankAccountNumber = customer[0][10].toString();
        showInInvoice = customer[0][11].toString().equals("1");
        taxNumber = customer[0][12].toString();
        euTaxNumber = customer[0][13].toString();
        paymentMethod = Integer.parseInt(customer[0][14].toString());
        paymentMethodRequired = false;
        maturityDays = GetMaturityDays(customer[0][15].toString(), customer[0][16].toString());
    }
    
    public void Update()
    {
        String query = "UPDATE " + table_customers + " SET "
            + "name = ?, "
            + "countryCode = ?, "
            + "country = ?, "
            + "postalCode = ?, "
            + "city = ?, "
            + "street = ?, "
            + "publicPlace = ?, "
            + "houseNumber = ?, "
            + "telNumber = ?, "
            + "email = ?, "
            + "taxNumber = ?, "
            + "euTaxNumber = ?, "
            + "bankAccountNumber = ?, "
            + "showInInvoice = ?, "
            + "paymentMethod = ?, "
            + "paymentMethodRequired = ?, "
            + "maturityDays = ?, "
            + "discount = ? "
            + "WHERE id = " + id;
        
        Object [] o = new Object[18];
        
        o[0] = name;
        o[1] = countryCode;
        o[2] = country;
        o[3] = postalCode;
        o[4] = city;
        o[5] = street;
        o[6] = publicPlace;
        o[7] = houseNumber;
        o[8] = telNumber;
        o[9] = email;
        o[10] = taxNumber;
        o[11] = euTaxNumber;
        o[12] = bankAccountNumber;
        o[13] = showInInvoice;
        o[14] = paymentMethod;
        o[15] = paymentMethodRequired;
        o[16] = maturityDays;
        o[17] = discount;
        
        NAV.db.insert(query, o, 18);
        
        query = "UPDATE "  + table_pixiUgyfel + " SET "
            + "nev = ?, "
            + "irsz = ?, "
            + "varos = ?, "
            + "utca = ?, "
            + "kozterulet = ?, "
            + "hazszam = ?, "
            + "orszag = ?, "
            + "telefon = ?, "
            + "email = ?, "
            + "fizetesi_mod = ?, "
            + "fizetesi_mod_kotelezo = ?, "
            + "esedekesseg = ?, "
            + "adoszam = ?, "
            + "eu_adoszam = ?, "
            + "bankszamlaszam = ?, "
            + "szamlan_megjelenik = ?, "
            + "userid = ?, "
            + "kedvezmeny = ? "
            + "WHERE id = " + id;
        
        o = new Object[18];
        
        o[0] = name;
        o[1] = postalCode;
        o[2] = city;
        o[3] = street;
        o[4] = publicPlace;
        o[5] = houseNumber;
        o[6] = country;
        o[7] = telNumber;
        o[8] = email;
        o[9] = paymentMethod;
        o[10] = paymentMethodRequired;
        o[11] = maturityDays;     
        o[12] = taxNumber;
        o[13] = euTaxNumber;
        o[14] = bankAccountNumber;
        o[15] = showInInvoice;
        o[16] = 0;//userid
        o[17] = discount;//kedvezmeny
        
        NAV.db.insert(query, o, 18);
    }
    
    public void Insert()
    {
        String query = "INSERT INTO " + table_customers + " ("
            + "name, "
            + "countryCode, "
            + "country, "
            + "postalCode, "
            + "city, "
            + "street, "
            + "publicPlace, "
            + "houseNumber, "
            + "telNumber, "
            + "email, "
            + "taxNumber, "
            + "euTaxNumber, "
            + "bankAccountNumber, "
            + "showInInvoice, "
            + "paymentMethod, "
            + "paymentMethodRequired, "
            + "maturityDays, "
            + "discount) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        Object [] o = new Object[18];
        
        o[0] = name;
        o[1] = countryCode;
        o[2] = country;
        o[3] = postalCode;
        o[4] = city;
        o[5] = street;
        o[6] = publicPlace;
        o[7] = houseNumber;
        o[8] = telNumber;
        o[9] = email;
        o[10] = taxNumber;
        o[11] = euTaxNumber;
        o[12] = bankAccountNumber;
        o[13] = showInInvoice;
        o[14] = paymentMethod;
        o[15] = paymentMethodRequired;
        o[16] = maturityDays;
        o[17] = discount;
        
        NAV.db.insert(query, o, 18);
        
        query = "INSERT INTO "  + table_pixiUgyfel + " ("
            + "nev, "
            + "irsz, "
            + "varos, "
            + "utca, "
            + "kozterulet, "
            + "hazszam, "
            + "orszag, "
            + "telefon, "
            + "email, "
            + "fizetesi_mod, "
            + "fizetesi_mod_kotelezo, "
            + "esedekesseg, "
            + "adoszam, "
            + "eu_adoszam, "
            + "bankszamlaszam, "
            + "szamlan_megjelenik, "
            + "userid, "
            + "kedvezmeny) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        o = new Object[18];
        
        o[0] = name;
        o[1] = postalCode;
        o[2] = city;
        o[3] = street;
        o[4] = publicPlace;
        o[5] = houseNumber;
        o[6] = country;
        o[7] = telNumber;
        o[8] = email;
        o[9] = paymentMethod;
        o[10] = paymentMethodRequired;
        o[11] = maturityDays;     
        o[12] = taxNumber;
        o[13] = euTaxNumber;
        o[14] = bankAccountNumber;
        o[15] = showInInvoice;
        o[16] = 0;//userid
        o[17] = discount;//kedvezmeny
        
        NAV.db.insert(query, o, 18);
        
        Query query2 = new Query.QueryBuilder()
            .select("id")
            .from(table_customers)
            .order("id DESC LIMIT 1")
            .build();
        Object [][] customerID = NAV.db.select(query2.getQuery());
        
        this.id = Integer.parseInt(customerID[0][0].toString());
    }
    
    public void Delete()
    {
        String query = "DELETE FROM " + table_customers + " WHERE id = " + id;
        NAV.db.delete(query);
    }
    
    //ez még nem lett tesztelve
    private int GetMaturityDays(String issueDate, String maturityDate)
    {
        LocalDate d1 = LocalDate.parse(issueDate, DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDate d2 = LocalDate.parse(maturityDate, DateTimeFormatter.ISO_LOCAL_DATE);
        Duration diff = Duration.between(d1.atStartOfDay(), d2.atStartOfDay());
        int diffDays = (int)diff.toDays();
        
        return diffDays;
    }
    
    public static Customer[] getCustomers(int[] ids)
    {
        String whereText = "(";
        
        for (int i = 0; i < ids.length; i++)
        {
            whereText += ids[i] + ", ";
        }
        
        whereText += "0)";
        Object[][] s = NAV.db.select("SELECT id FROM " + table_customers + " WHERE id IN " + whereText + " ORDER BY name ASC");
        
        if (s.length != 0)
        {
            Customer[] customers = new Customer[s.length];
            
            for (int i = 0; i < s.length; i++)
            {
                int customerID = Integer.parseInt(s[i][0].toString());
                Customer customer = new Customer(customerID);
                customers[i] = customer;
            }
            
            return customers;
        }
        else
        {
            return null;
        }
    }
    
    public String isValid()
    {
        String result = "";
        
        if(name.isEmpty())
        {
            result = "A név mező üresen maradt!";
        }
        else if(countryCode.isEmpty())
        {
            result = "Az országkód mező üresen maradt!";
        }
        else if(country.isEmpty())
        {
            result = "A ország mező üresen maradt!";
        }
        else if(postalCode.isEmpty())
        {
            result = "Az irányítószám mező üresen maradt!";
        }
        else if(city.isEmpty())
        {
            result = "A város mező üresen maradt!";
        }
        else if(street.isEmpty())
        {
            result = "Az utca mező üresen maradt!";
        }
        
        return result;
    }
    
    public void Grouping(Customer[] customers)
    {
        String whereText = "(";
        
        for(int i = 0; i < customers.length; i++)
        {
            if(customers[i].getId() != id)
            {
                whereText += customers[i].getId() + ", ";
            }
        }
        
        whereText += "0)";
        
        NAV.db.insert("UPDATE " + table_contact + " SET customerID = " + id + " WHERE customerID IN " + whereText, null, 0);
        NAV.db.delete("DELETE FROM " + table_customers + " WHERE id IN " + whereText);
        
        NAV.pixi.insert("UPDATE " + table_pixiKapcsolattarto + " SET ugyfelid = " + id + " WHERE ugyfelid IN " + whereText, null, 0);
        NAV.pixi.delete("DELETE FROM " + table_pixiUgyfel + " WHERE id IN " + whereText);
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
    
    public String getTelNumber()
    {
        return telNumber;
    }

    public String getEmail()
    {
        return email;
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

    public boolean getShowInInvoice()
    {
        return showInInvoice;
    }

    public int getPaymentMethod()
    {
        return paymentMethod;
    }
    
    public boolean getPaymentMethodRequired()
    {
        return paymentMethodRequired;
    }

    public int getMaturityDays()
    {
        //ha az id 0 akkor név alapján kell rákeresni
        return maturityDays;
    }

    public int getDiscount()
    {
        return discount;
    }
    
    //SETTERS
    public void setId(int id)
    {
        this.id = id;
    }

    public boolean Valid()
    {
        return !name.isEmpty();
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
    
    public String getAddress()
    {
        return street + " " + publicPlace + " " + houseNumber;
    }

    public void setTelNumber(String telNumber)
    {
        this.telNumber = telNumber;
    }

    public void setEmail(String email)
    {
        this.email = email;
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

    public void setShowInInvoice(boolean showInInvoice)
    {
        this.showInInvoice = showInInvoice;
    }

    public void setPaymentMethod(int paymentMethod)
    {
        this.paymentMethod = paymentMethod;
    }
    
    public void setPaymentMethodRequired(boolean paymentMethodRequired)
    {
        this.paymentMethodRequired = paymentMethodRequired;
    }

    public void setMaturityDays(int maturityDays)
    {
        this.maturityDays = maturityDays;
    }

    public void setDiscount(int discount)
    {
        this.discount = discount;
    }
}