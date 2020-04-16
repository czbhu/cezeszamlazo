package nav;

import controller.XmlLogger;
import database.Query;
import functions.Decode;
import functions.Encode;
import onlineInvoice.request.ManageAnnulmentRequest;
import onlineInvoice.request.ManageInvoiceRequest;
import onlineInvoice.request.QueryInvoiceCheckRequest;
import onlineInvoice.request.QueryInvoiceDataRequest;
import onlineInvoice.request.QueryInvoiceDigestRequest;
import onlineInvoice.request.QueryTransactionStatusRequest;
import onlineInvoice.request.QueryTaxpayerRequest;
import onlineInvoice.request.TokenExchangeRequest;
import onlineInvoice.response.ManageInvoiceResponse;
import onlineInvoice.response.QueryTransactionStatusResponse;
import onlineInvoice.response.QueryTaxpayerResponse;
import onlineInvoice.response.TokenExchangeResponse;
import onlineInvoice.response.queryTaxpayer.QueryTaxpayerResponseType;
import database.Database;
import invoice.Invoice;
import invoice.Supplier;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import onlineInvoice.InvoiceExchangeType;
import onlineInvoice.InvoiceOperationType;
import view.UserMessage;

/**
 * @author Tomy
 */
public class NAV
{
    ManageAnnulmentRequest manageAnnulment;// a technikai érvénytelenítés beküldésére szolgáló operáció
    ManageInvoiceRequest manageInvoice; // a számla adatszolgáltatás beküldésre szolgáló operáció, ezen keresztül van lehetőség számla vagy módosító okirat adatait a NAV részére beküldeni
    QueryInvoiceCheckRequest queryInvoiceCheck; // a megadott számlaszámról szóló adatszolgáltatás létezését ellenőrzi a rendszerben, a számla teljes adattartalmának visszaadása nélkül. 
    QueryInvoiceDataRequest queryInvoiceData; // a megadott számlaszám teljes adattartalmát adja vissza a válaszban.
    QueryInvoiceDigestRequest queryInvoiceDigest;
    QueryTransactionStatusRequest queryTransactionStatus; //a számla adatszolgáltatás feldolgozás aktuális állapotának és eredményének lekérdezésére szolgáló operáció
    //QueryServiceMetricsRequest queryServiceMetrics;
    QueryTaxpayerRequest queryTaxpayer; //belföldi adószám validáló operáció, mely a számlakiállítás folyamatába építve képes a megadott adószám valódiságáról és érvényességéről a NAV adatbázisa alapján adatot szolgáltatni
    TokenExchangeRequest tokenExchange; //a számla adatszolgáltatás beküldést megelőző egyszer használatos adatszolgáltatási token kiadását végző operáció 
    
    //ArrayList<Invoice> invoices = new ArrayList<>();
    Invoice invoice;
    
    String serverUrl;
    
    int supplierId;
    
    int errorCount = 0;
    
    public static int userId;
    
    public static Database db;
    public static Database pixi;
    
    // ********** dev ********** // 
    
    static final String URL = "jdbc:mysql://phpmyadmin2.ceze.eu/szamlazo_dev?characterEncoding=UTF8";
    static final String USERNAME = "szamla_demo";
    static final String PASSWORD = "demo";

    //static final String PIXI_URL = "jdbc:mysql://phpmyadmin2.ceze.eu/minta_tabla_szamlazo?characterEncoding=UTF8";
    //static final String PIXI_USERNAME = "szamla_demo";
    //static final String PIXI_PASSWORD = "demo";
    static final String PIXI_URL = "jdbc:mysql://phpmyadmin2.ceze.eu/pixirendszer_dev?characterEncoding=UTF8";
    static final String PIXI_USERNAME = "szamla_demo";
    static final String PIXI_PASSWORD = "demo";

    // ******** online ********* //
    /*
    static final String URL = "jdbc:mysql://phpmyadmin2.ceze.eu/szamlazo?characterEncoding=UTF8";
    static final String USERNAME = "szamla_demo";
    static final String PASSWORD = "demo";

    static final String PIXI_URL = "jdbc:mysql://phpmyadmin2.ceze.eu/cezetesztdb?characterEncoding=UTF8";
    static final String PIXI_USERNAME = "szamla_demo";
    static final String PIXI_PASSWORD = "demo";
    //static final String PIXI_URL = "jdbc:mysql://phpmyadmin2.ceze.eu/pixirendszer_ceze?characterEncoding=UTF8";
    //static final String PIXI_USERNAME = "szamla_demo";
    //static final String PIXI_PASSWORD = "demo";
    */
    
    public NAV(Invoice invoice, int szamlazoUserId)
    {
        userId = szamlazoUserId;
        InitDatabase();
        Connect();
        //invoices.add(invoice);
        this.invoice = invoice;
        supplierId = invoice.getSupplier().getId();
        setServerUrl();
    }
    
    public NAV(int supplierId, int szamlazoUserId)
    {
        userId = szamlazoUserId;
        InitDatabase();
        Connect();
        //invoices.add(invoice);
        this.supplierId = supplierId;
        setServerUrl();
    }
    
    private void InitDatabase()
    {
        db = new Database(URL, USERNAME, PASSWORD);
        pixi = new Database(PIXI_URL, PIXI_USERNAME, PIXI_PASSWORD);
    }
    
    private void Connect()
    {
        trustCertificates();
    }
    
    public void trustCertificates()
    {
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        TrustManager[] trustAllCerts = new TrustManager[]
        {
            new X509TrustManager()
            {
                public X509Certificate[] getAcceptedIssuers()
                {
                    return null;
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException
                {
                    return;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException
                {
                    return;
                }
            }
        };

        SSLContext sc = null;
        
        try
        {
            sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
        }
        catch (KeyManagementException ex)
        {
            ex.printStackTrace();
        }
        catch (NoSuchAlgorithmException ex)
        {
            ex.printStackTrace();
        }
        
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HostnameVerifier hv = new HostnameVerifier()
        {
            public boolean verify(String urlHostName, SSLSession session)
            {
                if (!urlHostName.equalsIgnoreCase(session.getPeerHost()))
                {
                    UserMessage message = new UserMessage("WARNING", "Figyelem '" + urlHostName + "' is different to SSLSession host '" + session.getPeerHost() + "'.");
                }
                return true;
            }
        };
        
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
    }
    
    public String getToken()
    {
        String exchangeToken = "";
        //tokenExchange = new TokenExchangeRequest(invoices.get(0));
        tokenExchange = new TokenExchangeRequest(invoice);
        String request = tokenExchange.getRequestXml();
        System.err.println(request);
        
        String uri = serverUrl + "tokenExchange";
        
        String response = GetResponseXML(uri, request);
        System.err.println(response);
        XmlLogger.addLog(this.getClass().getName(), response);
        
        TokenExchangeResponse ToExRe = new TokenExchangeResponse(response);
        
        if(!ToExRe.getEncodedExchangeToken().equals(""))
        {
            exchangeToken = ToExRe.getEncodedExchangeToken();
        }
        else
        {
            if(errorCount < 3)
            {
                errorCount++;
                exchangeToken = getToken();
            }
            else
            {
                UserMessage message = new UserMessage("TokenExchange", ToExRe.getErrors());
                errorCount = 0;
            }
        }
        
        return exchangeToken;
    }
    
    public String getTransactionId()
    {
        String transactionId = "";
        //String exchangeKey = getExchangeKey(invoices.get(0).getSupplier().getId());
        String exchangeKey = getExchangeKey(invoice.getSupplier().getId());
        
        String exchangeToken = getToken();
        
        if(!exchangeToken.isEmpty())
        {
            exchangeToken = Decode.aes128(exchangeToken, exchangeKey);

            if(!exchangeToken.equals(""))
            {
                /*String checksum = "";

                for(int i = 0; i < invoices.size(); i++)
                {
                    InvoiceExchangeType invoiceExchange = new InvoiceExchangeType(invoices.get(i));
                    XmlBuilder builder = new XmlBuilder();
                    String xml = builder.CreateInvoiceExchangeType(invoiceExchange);

                    checksum += Encode.CRC32(xml);
                }*/

                InvoiceExchangeType invoiceExchange = new InvoiceExchangeType(invoice);
                XmlBuilder builder = new XmlBuilder();
                String xml = builder.CreateInvoiceExchangeType(invoiceExchange);
                
                //String hash = Encode.CRC32(xml);

                InvoiceOperationType operation = new InvoiceOperationType(invoice, xml);
                String hash = operation.getInvoiceOperation() + Encode.Base64(operation.getInvoiceData());
                //manageInvoice = new ManageInvoiceRequest(invoices, exchangeToken, checksum);
                manageInvoice = new ManageInvoiceRequest(invoice, exchangeToken, xml, hash);
                String request = manageInvoice.getRequestXml();
                //System.out.println(request);

                String uri = serverUrl + "manageInvoice";

                String response = GetResponseXML(uri, request);
                //System.err.println(response);
                XmlLogger.addLog(this.getClass().getName(), response);

                ManageInvoiceResponse MaInRe = new ManageInvoiceResponse(response);

                if(!MaInRe.getTransactionId().equals(""))
                {
                    invoice.setTransactionID(MaInRe.getTransactionId());
                    invoice.UpdateTransactionId();

                    transactionId = MaInRe.getTransactionId();
                }
                else
                {
                    if(errorCount < 3)
                    {
                        errorCount++;
                        transactionId = getTransactionId();
                    }
                    else
                    {
                        UserMessage message = new  UserMessage("ManageInvoice", MaInRe.getErrors());
                        errorCount = 0;
                    }
                }
            }
        }
        return transactionId;
    }
    
    public String getStatus()
    {
        String status = "";
        /*if(invoices.get(0).getTransactionID().isEmpty())
        {
            invoices.get(0).setTransactionID(getTransactionId());
        }*/
        
        if(invoice.getTransactionID().isEmpty())
        {
            //System.err.println("NAV.java/gatStatus()/tranzakcióId nincs a számlához");
            invoice.setTransactionID(getTransactionId());
        }
        
        if(!invoice.getTransactionID().isEmpty())
        {
            //queryInvoiceStatus = new QueryInvoiceStatusRequest(invoices.get(0));
            queryTransactionStatus = new QueryTransactionStatusRequest(invoice);
            String request = queryTransactionStatus.getRequestXml();
            //System.err.println(request);

            String uri = serverUrl + "queryTransactionStatus";

            String response = GetResponseXML(uri, request);
            //System.err.println(response);
            XmlLogger.addLog(this.getClass().getName(), response);

            QueryTransactionStatusResponse QuTrStRe = new QueryTransactionStatusResponse(response);
            
            if(!QuTrStRe.getStatus().isEmpty())
            {
                status = QuTrStRe.getStatus();
            }
            else
            {
                if(errorCount < 3)
                {
                    errorCount++;
                    status = getStatus();
                }
                else
                {
                    UserMessage message = new UserMessage("QueryTransactionStatusRequest", QuTrStRe.getErrors());
                    errorCount = 0;
                }
            }
        }
        
        return status;
    }
    
    public QueryTaxpayerResponseType getTaxpayer(String taxNumber)
    {
        //queryTaxpayer = new QueryTaxpayerRequest(invoices.get(0));
        queryTaxpayer = new QueryTaxpayerRequest(invoice.getSupplier().getId(), taxNumber);
        String request = queryTaxpayer.getRequestXml();
        //System.err.println(request);
        
        String uri = serverUrl + "queryTaxpayer";
        
        String response = GetResponseXML(uri, request);
        //System.err.println(response);
        XmlLogger.addLog(this.getClass().getName(), response);
        
        QueryTaxpayerResponse taxpayerResponse = new QueryTaxpayerResponse(response);
        
        QueryTaxpayerResponseType responseType = taxpayerResponse.getQueryTaxpayerResponse();
        
        return responseType;
    }
    
    public QueryTaxpayerResponseType getTaxpayer(String taxNumber, int supplierId)
    {
        this.supplierId = supplierId;
        //queryTaxpayer = new QueryTaxpayerRequest(invoices.get(0));
        queryTaxpayer = new QueryTaxpayerRequest(supplierId, taxNumber);
        String request = queryTaxpayer.getRequestXml();
        //System.err.println(request);
        
        String uri = serverUrl + "queryTaxpayer";
        
        String response = GetResponseXML(uri, request);
        //System.err.println(response);
        XmlLogger.addLog(this.getClass().getName(), response);
        
        QueryTaxpayerResponse taxpayerResponse = new QueryTaxpayerResponse(response);
        
        QueryTaxpayerResponseType responseType = taxpayerResponse.getQueryTaxpayerResponse();
        
        return responseType;
    }
    
    /*public String GetResponseXML(String uri, String inputfile)
    {
        String xml = "";
        try
        {
            trustCertificates();
            String s = inputfile;
            URL url = new URL(uri);
            //byte[] bytes = s.getBytes("UTF-8");

            HttpsURLConnection request = (HttpsURLConnection) url.openConnection();
            request.setDoOutput(true);
            request.setRequestMethod("POST");
            request.setRequestProperty("Content-Type", "application/xml");
            request.setRequestProperty("Accept", "application/xml");
            request.setRequestProperty("Method", "POST");
            DataOutputStream outputStream = new DataOutputStream(new BufferedOutputStream(request.getOutputStream()));
            outputStream.writeBytes(s);
            outputStream.flush();
            outputStream.close();
  
            if (request.getResponseCode() != 200)
            {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(request.getErrorStream()));

                StringBuilder content = new StringBuilder();

                String line;
                
                while ((line = bufferedReader.readLine()) != null)
                {
                    content.append(line + "\n");
                }

                bufferedReader.close();
                byte[] contentbytes = content.toString().getBytes("UTF-8");
                String s2 = new String(contentbytes, "UTF-8");
                xml = s2;
            }

            if (request.getResponseCode() == 200)
            {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(request.getInputStream()));
                StringBuilder content = new StringBuilder();
                String line;
                
                while ((line = bufferedReader.readLine()) != null)
                {
                    content.append(line + "\n");
                }
                
                bufferedReader.close();
                byte[] contentbytes = content.toString().getBytes("UTF-8");
                String s2 = new String(contentbytes, "UTF-8");
                xml = s2;
            } 
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return xml;
    }*/
    
    public String GetResponseXML(String uri, String inputfile)
    {
        String xml = "";
        
        try
        {
            trustCertificates();
            String s = inputfile;
            URL url = new URL(uri);
            //byte[] bytes = s.getBytes("UTF-8");

            HttpsURLConnection request = (HttpsURLConnection) url.openConnection();
            request.setConnectTimeout(10000);
            request.setDoOutput(true);
            request.setRequestMethod("POST");
            request.setRequestProperty("Content-Type", "application/xml");
            request.setRequestProperty("Accept", "application/xml");
            request.setRequestProperty("Method", "POST");
            DataOutputStream outputStream = new DataOutputStream(new BufferedOutputStream(request.getOutputStream()));
            outputStream.writeBytes(s);
            outputStream.flush();
            outputStream.close();
  
            System.err.println("RESPONSE CODE: " + request.getResponseCode());
            
            if (request.getResponseCode() != 200)
            {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(request.getErrorStream()));

                StringBuilder content = new StringBuilder();

                String line;
                
                while ((line = bufferedReader.readLine()) != null)
                {
                    content.append(line + "\n");
                }

                bufferedReader.close();
                byte[] contentbytes = content.toString().getBytes("UTF-8");
                String s2 = new String(contentbytes, "UTF-8");
                xml = s2;
            }

            if (request.getResponseCode() == 200)
            {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(request.getInputStream()));
                StringBuilder content = new StringBuilder();
                String line;
                
                while ((line = bufferedReader.readLine()) != null)
                {
                    content.append(line + "\n");
                }
                
                bufferedReader.close();
                byte[] contentbytes = content.toString().getBytes("UTF-8");
                String s2 = new String(contentbytes, "UTF-8");
                xml = s2;
            } 
        }
        catch(SocketTimeoutException ex)
        {
            UserMessage msg = new UserMessage("Időtúllépés", "A NAV szerver válaszideje 10 mp fölött van!");
            
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return xml;
    }
    
    private void setServerUrl()
    {
        Query query = new Query.QueryBuilder()
            .select("serverurl")
            .from(Supplier.TABLE)
            .where("id = " + supplierId)
            .build();
        Object [][] url = db.select(query.getQuery());
        
        serverUrl = url[0][0].toString();
    }
    
    private String getExchangeKey(int supplierId)
    {
        Query query = new Query.QueryBuilder()
            .select("exchangeKey")
            .from(Supplier.TABLE)
            .where("id = " + supplierId)
            .build();
        Object [][] key = db.select(query.getQuery());
        
        return key[0][0].toString();
    }
}