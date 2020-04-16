package cezeszamlazo;

/**
 * @author Balázs,Tomy
 */

import cezeszamlazo.database.Query;
import cezeszamlazo.views.UserMessage;
import com.sun.security.sasl.Provider;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import java.net.URL;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import org.xml.sax.InputSource;

public class NAVConn
{
    private static String loggerpath = "log//Logger.txt";
    //private static String xmlpath = "C://temp_szamlazo//taxpayerreq.xml";
    private static String xmlpath = "C://temp_szamlazo//TokenExchangeRequest.xml";
    
    //Trust Certs
    public void trustCertificates() throws Exception
    {
        Security.addProvider(new Provider());
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

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HostnameVerifier hv = new HostnameVerifier()
        {
            public boolean verify(String urlHostName, SSLSession session)
            {
                if (!urlHostName.equalsIgnoreCase(session.getPeerHost()))
                {
                    Logger("Figyelem '" + urlHostName + "' is different to SSLSession host '" + session.getPeerHost() + "'.", true);
                }
                return true;
            }
        };
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
    }
    
    public String GetQueryInvoiceData(String uri, String inputfile) throws Exception
    {
        String data = "";
        trustCertificates();
        //String s = new String(Files.readAllBytes(Paths.get(inputfile)));
        String s = inputfile;
        Logger(GetTimeStamp("yyyy.MM.dd HH:mm:ss") + s, true);
        URL url = new URL(getServerUrl()+uri);
        byte[] bytes = s.getBytes("UTF-8");

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
            Logger("Print:" + request.getResponseCode() + "" + request.getResponseMessage(), true);
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
            WriteXml(s2);
            data = s2;
        }
        
        /*
        get responde from the server to xml
        */
        if (request.getResponseCode() == 200)
        {
            Logger("Print:" + request.getResponseCode() + "" + request.getResponseMessage(), true);
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
            //System.out.println("Token is:" + GetToken(s2));
            WriteXml(s2);
            data = s2;
        }
        
        return data;
    }
    
    public String GetStatus(String uri, String inputfile) throws Exception
    {
        trustCertificates();
        //String s = new String(Files.readAllBytes(Paths.get(inputfile)));
        String s = inputfile;
        String s2 = "";
        Logger(GetTimeStamp("yyyy.MM.dd HH:mm:ss") + s, true);
        URL url = new URL(getServerUrl()+uri);
        byte[] bytes = s.getBytes("UTF-8");

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
            Logger("Print:" + request.getResponseCode() + "" + request.getResponseMessage(), true);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(request.getErrorStream()));
            StringBuilder content = new StringBuilder();
            String line;
            
            while ((line = bufferedReader.readLine()) != null)
            {
                content.append(line + "\n");
            }
            
            bufferedReader.close();
            byte[] contentbytes = content.toString().getBytes("UTF-8");
            s2 = new String(contentbytes, "UTF-8");
            WriteXml(s2);
        }
        /*
        get responde from the server to xml
        */
        if (request.getResponseCode() == 200)
        {
            Logger("Print:" + request.getResponseCode() + "" + request.getResponseMessage(), true);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(request.getInputStream()));
            StringBuilder content = new StringBuilder();
            String line;
            
            while ((line = bufferedReader.readLine()) != null)
            {
                content.append(line + "\n");
            }
            
            bufferedReader.close();
            byte[] contentbytes = content.toString().getBytes("UTF-8");
            s2 = new String(contentbytes, "UTF-8");
            WriteXml(s2);
        }
        
        return s2;
    }
    
    //Post message
    void Post(String uri, String inputfile) throws NoSuchAlgorithmException, KeyManagementException, IOException, KeyStoreException, CertificateException, UnrecoverableKeyException, Exception
    {
        trustCertificates();
        //String s = new String(Files.readAllBytes(Paths.get(inputfile)));
        String s = inputfile;
        Logger(GetTimeStamp("yyyy.MM.dd HH:mm:ss") + s, true);
        URL url = new URL(getServerUrl()+uri);
        byte[] bytes = s.getBytes("UTF-8");

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
            Logger("Print:" + request.getResponseCode() + "" + request.getResponseMessage(), true);
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
            WriteXml(s2);
        }
        /*
        get responde from the server to xml
        */
        if (request.getResponseCode() == 200)
        {
            Logger("Print:" + request.getResponseCode() + "" + request.getResponseMessage(), true);
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
            WriteXml(s2);
        }       
    }
    
    //GET <encodedExchangeToken> element as a variable
    public String GetToken(String xml) throws UnsupportedEncodingException
    {
        String s = xml;
        byte[] bytes = s.getBytes("UTF-8");
        String xmlSplit = "";
        
        try
        {
            xmlSplit = s.split("<" + "encodedExchangeToken" + ">")[1].split("</" + "encodedExchangeToken" + ">")[0];
        }
        catch(ArrayIndexOutOfBoundsException ex)
        {
            System.err.println("NAVConn.java/GetToken()");
            //ex.printStackTrace();
            UserMessage message = new UserMessage("GetToken Error", s);
        }
        
        return xmlSplit;
        
    }  
    
    public String GetExToken(String uri, String inputfile) throws NoSuchAlgorithmException, KeyManagementException, IOException, KeyStoreException, CertificateException, UnrecoverableKeyException, Exception
    {
        trustCertificates();
        String s = inputfile;
        Logger(GetTimeStamp("yyyy.MM.dd HH:mm:ss") + s, true);
        URL url = new URL(getServerUrl()+uri);
        byte[] bytes = s.getBytes("UTF-8");

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
        
        String token = "";
        if (request.getResponseCode() != 200)
        {          
            Logger("Print:" + request.getResponseCode() + "" + request.getResponseMessage(), true);
            
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
            WriteXml(s2);
            token = GetToken(s2);
        }
        /*
        get responde from the server to xml
        */
        if (request.getResponseCode() == 200)
        {
            Logger("Print:" + request.getResponseCode() + "" + request.getResponseMessage(), true);
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
            System.out.println("Token is:" + GetToken(s2));
            WriteXml(s2);
            token = GetToken(s2);
        }   
        return token;
    }
    
    public String GetTransactionId(String xml) throws UnsupportedEncodingException
    {
        String s = xml;
        byte[] bytes = s.getBytes("UTF-8");

        String xmlSplit = "";
        
        try
        {
            xmlSplit = s.split("<"+"transactionId"+">")[1].split("</"+"transactionId"+">")[0];
        }
        catch(ArrayIndexOutOfBoundsException ex)
        {
            System.err.println("NAVConn.java/GetTransactionId()");
            //ex.printStackTrace();
            UserMessage message = new UserMessage("TransactionID error", s);
        }

        return xmlSplit;
    }
    
    public String GetTransactionID(String uri, String inputfile) throws NoSuchAlgorithmException, KeyManagementException, IOException, KeyStoreException, CertificateException, UnrecoverableKeyException, Exception
    {
        trustCertificates();
        String s = inputfile;
        String s2 = "";
        Logger(GetTimeStamp("yyyy.MM.dd HH:mm:ss") + s, true);
        URL url = new URL(getServerUrl()+uri);
        byte[] bytes = s.getBytes("UTF-8");

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
        
        String Id = "";
        
        if (request.getResponseCode() != 200)
        {
            Logger("Print:" + request.getResponseCode() + "" + request.getResponseMessage(), true);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(request.getErrorStream()));
            StringBuilder content = new StringBuilder();
            String line;
            
            while ((line = bufferedReader.readLine()) != null)
            {
                content.append(line + "\n");
            }
            
            bufferedReader.close();
            byte[] contentbytes = content.toString().getBytes("UTF-8");
            s2 = new String(contentbytes, "UTF-8");
            WriteXml(s2);
            //System.out.println(s2);
            Id = GetTransactionId(s2);
        }
        
        if (request.getResponseCode() == 200)
        {
            Logger("Print:" + request.getResponseCode() + "" + request.getResponseMessage(), true);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(request.getInputStream()));
            StringBuilder content = new StringBuilder();
            String line;
            
            while ((line = bufferedReader.readLine()) != null)
            {
                content.append(line + "\n");
            }
            
            bufferedReader.close();
            byte[] contentbytes = content.toString().getBytes("UTF-8");
            s2 = new String(contentbytes, "UTF-8");
            WriteXml(s2);
            Id = GetTransactionId(s2);
        }
        
        return s2;
    }
    
    //XML formatter
    public void WriteXml(String xml)
    {
        try
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xml)));

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            DOMSource domSource = new DOMSource(document);
            StringWriter outWriter = new StringWriter();
            StreamResult streamResult = new StreamResult(outWriter);
            //StreamResult streamResult = new StreamResult(new File("C:\\temp_szamlazo\\new.xml")); //a választ kiírja egy xml file-ba.
            transformer.transform(domSource, streamResult);
            StringBuffer sb = outWriter.getBuffer();
            String finalstring = sb.toString();
            Logger("\r\n" + finalstring, true);
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void Logger(String logstring, boolean toconsole)
    {
        try
        {
            if (toconsole)
            {
                System.out.println(logstring);
            }
            
            Files.write(Paths.get(loggerpath), logstring.getBytes(), StandardOpenOption.APPEND);
        }
        catch (IOException e)
        {
            System.out.println("Error occured: " + loggerpath + " (NAVConn.java/Logger())");
            e.printStackTrace();
        }
    }
    
    //Timestamp
    public String GetTimeStamp(String dateformat)
    {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Calendar calendar = Calendar.getInstance(timeZone);
        Date timestamp = calendar.getTime();
        SimpleDateFormat df = new SimpleDateFormat(dateformat);
        df.setTimeZone(timeZone);
        String timestampStr = df.format(timestamp);
        return timestampStr;
    }
    
    public String getServerUrl()
    {
        Suppliers s = new Suppliers();
        String SupplierID = s.getSupplierID();
        
        Query query = new Query.QueryBuilder()
            .select("serverurl")
            .from("szamlazo_suppliers")
            .where("id LIKE '" + SupplierID + "'")
            .build();
        Object [][] serverURL = App.db.select(query.getQuery());

        System.out.println("serverURL :" + serverURL[0][0].toString() + " (NAVConn.kava/getServerUrl())");
        System.out.println("encoding: " + System.getProperty("file.encoding") + " (NAVConn.kava/getServerUrl())");
        return serverURL[0][0].toString();
    }

    public static void run() throws NoSuchAlgorithmException, KeyManagementException, IOException, KeyStoreException, CertificateException, UnrecoverableKeyException, Exception
    {
        NAVConn transp = new NAVConn();
        String query = "tokenExchange";
        System.out.println("NAVconnRun");
        transp.Post(transp.getServerUrl() + query, xmlpath);
    }
    
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
                Logger("Print:" + request.getResponseCode() + "" + request.getResponseMessage(), true);

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
                WriteXml(s2);
                System.err.println("No response");
                xml = s2;
            }

            if (request.getResponseCode() == 200)
            {
                Logger("Print:" + request.getResponseCode() + "" + request.getResponseMessage(), true);
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
                WriteXml(s2);
                xml = s2;
            } 
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return xml;
    }
}