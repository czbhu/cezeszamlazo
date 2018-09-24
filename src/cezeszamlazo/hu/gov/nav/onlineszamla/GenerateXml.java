/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeszamlazo.hu.gov.nav.onlineszamla;


/**
 *
 * @author Tomy
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import cezeszamlazo.NAVConn;
import org.apache.commons.codec.binary.Base64;
import com.mifmif.common.regex.Generex;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.TimeZone;
import java.util.zip.CRC32;
import java.util.zip.Checksum;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import static javax.xml.bind.DatatypeConverter.printHexBinary;

public class GenerateXml
{
    public GenerateXml()
    {
        
    }
    
        protected String encoding(
        String str) throws UnsupportedEncodingException {

        if (str == null)
          return null;

        return new String(str.getBytes("UTF-8"));
    }
        
    protected String generateRequestSignature(String requestId, Long invoicesCRC32, String key, String timeStamp) throws NoSuchAlgorithmException {    
        //@formatter:off
        String tmp = requestId + timeStamp + key + (invoicesCRC32 == null ? "" : invoicesCRC32);
        //@formatter:on

        return sha512(tmp);
        }

    public static byte[] sha512(
        byte[] data) throws NoSuchAlgorithmException {

        return MessageDigest.getInstance("SHA-512").digest(data);
    }

    public static String sha512(
        String str) throws NoSuchAlgorithmException {

        return printHexBinary(sha512(str.getBytes(UTF_8)));
    }

    protected long generateCRC32(
        byte[] invoiceXml) {

        return crc32(DatatypeConverter.printBase64Binary(invoiceXml).getBytes());
    }

  public static long crc32(
    byte[] data) {

    CRC32 crc32 = new CRC32();
    crc32.update(data);
    return crc32.getValue();
  }
    
    public void createBasicHeaderType(Document doc, Element appendTo, String requestID, String timeSTAMP, float requestVERSION, float headerVERSION)
    {
        Element header = doc.createElement("header");
        appendTo.appendChild(header);
                                       
        Element requestId = doc.createElement("requestId");
        requestId.appendChild(doc.createTextNode(requestID));
        header.appendChild(requestId);
                
        Element timeStamp = doc.createElement("timestamp");
        timeStamp.appendChild(doc.createTextNode(timeSTAMP));
        header.appendChild(timeStamp);
                
        Element requestVersion = doc.createElement("requestVersion");
        requestVersion.appendChild(doc.createTextNode(Float.toString(requestVERSION)));
        header.appendChild(requestVersion);
                
        Element headerVersion = doc.createElement("headerVersion");
        headerVersion.appendChild(doc.createTextNode(Float.toString(headerVERSION)));
        header.appendChild(headerVersion);
    }
    
    public void createBasicUserType(Document doc, Element appendTo, String Login, String passwordHASH, int taxNUMBER, String requestSIGNATURE)
    {
        Element user = doc.createElement("user");
        appendTo.appendChild(user);
            
        Element login = doc.createElement("login");
        login.appendChild(doc.createTextNode(Login));
        user.appendChild(login);
                
        Element passordHash = doc.createElement("passwordHash");
        passordHash.appendChild(doc.createTextNode(passwordHASH.toUpperCase()));
        user.appendChild(passordHash);
                
        Element taxNumber = doc.createElement("taxNumber");
        taxNumber.appendChild(doc.createTextNode(Integer.toString(taxNUMBER)));
        user.appendChild(taxNumber);
                
        Element requestSignature = doc.createElement("requestSignature");
        requestSignature.appendChild(doc.createTextNode(requestSIGNATURE));
        user.appendChild(requestSignature);          
    }
    
    public void createBasicSoftwareType(Document doc, Element appendTo, String softwareID, String softwareNAME, String softwareOPERATION, float softwareMainVERSION, String softwareDevNAME, String softwareDevCONTACT, String softwareDevCountryCODE, String softwareTaxNUMBER)
    {
        Element software = doc.createElement("software");
        appendTo.appendChild(software);
            
        Element softwareId = doc.createElement("softwareId");
        softwareId.appendChild(doc.createTextNode(softwareID));
        software.appendChild(softwareId);
                
        Element softwareName = doc.createElement("softwareName");
        softwareName.appendChild(doc.createTextNode(softwareNAME));
        software.appendChild(softwareName);
                
        Element softwareOperation = doc.createElement("softwareOperation");
        softwareOperation.appendChild(doc.createTextNode(softwareOPERATION));
        software.appendChild(softwareOperation);
                
        Element softwareMainVersion = doc.createElement("softwareMainVersion");
        softwareMainVersion.appendChild(doc.createTextNode(Float.toString(softwareMainVERSION)));
        software.appendChild(softwareMainVersion);
                
        Element softwareDevName = doc.createElement("softwareDevName");
        softwareDevName.appendChild(doc.createTextNode(softwareDevNAME));
        software.appendChild(softwareDevName);
                
        Element softwareDevContact = doc.createElement("softwareDevContact");
        softwareDevContact.appendChild(doc.createTextNode(softwareDevCONTACT));
        software.appendChild(softwareDevContact);
                
        Element softwareDevCountryCode = doc.createElement("softwareDevCountryCode");
        softwareDevCountryCode.appendChild(doc.createTextNode(softwareDevCountryCODE));
        software.appendChild(softwareDevCountryCode);
                
        Element softwareDevTaxNumber = doc.createElement("softwareDevTaxNumber");
        softwareDevTaxNumber.appendChild(doc.createTextNode(softwareTaxNUMBER));
        software.appendChild(softwareDevTaxNumber);
    }
    
    public void createBasicInvoiceOperationType(Document doc, Element parent, int Index, String Operation, String Invoice)
    {
        Element invoiceOperation = doc.createElement("invoiceOperation");
        parent.appendChild(invoiceOperation);
                
        Element index = doc.createElement("index");
        index.appendChild(doc.createTextNode(Integer.toString(Index)));
        invoiceOperation.appendChild(index);
                    
        Element operation = doc.createElement("operation");
        operation.appendChild(doc.createTextNode(Operation));
        invoiceOperation.appendChild(operation);
                    
        Element invoice = doc.createElement("invoice");
        invoiceOperation.appendChild(invoice);
        invoice.appendChild(doc.createTextNode(Invoice));       
    }
    
    public static String getStringFromDocument(Document doc) throws TransformerException
    {
        DOMSource domSource = new DOMSource(doc);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.transform(domSource, result);
        return writer.toString();
    }
    
    public String CreateXmlFile(Document doc)
    {
        try
        {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            doc.setXmlStandalone(true);
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(System.out);
                
            transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);
            String output = getStringFromDocument(doc); 
            return output;
        }
        catch (TransformerException tfe)
        {
            tfe.printStackTrace(System.out);
            String output = "";
            return output;
	}
    }
    
    private String decrypt_data(String encData, String exchangeKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException
    {
        SecretKeySpec skeySpec = new SecretKeySpec(exchangeKey.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);

        byte[] original = cipher.doFinal(Base64.decodeBase64(encData.getBytes()));
        return new String(original).trim();
    }
    
    public String getSHA512(String input)
    {
	String toReturn = null;
	try
        {
	    MessageDigest digest = MessageDigest.getInstance("SHA-512");
	    digest.reset();
	    digest.update(input.getBytes("UTF-8"));
	    toReturn = String.format("%040x", new BigInteger(1, digest.digest()));
	}
        catch (NoSuchAlgorithmException | UnsupportedEncodingException e)
        {
	    e.printStackTrace(System.out);
	}	
	return toReturn;
    }
    
    public String getRandomId(String expReg)
    {
        Generex generex = new Generex(expReg);
        String RandomId = generex.random(16, 16);
        return RandomId;
    }
    
    public String maskTimeStamp(String timeStamp)
    {
        String masked = "";
        char[] chars = timeStamp.toCharArray();
        masked = masked + chars[0] + chars[1] + chars[2] + chars[3]  + chars[5] + chars[6] + chars[8] + chars[9]  + chars[11] + chars[12] + chars[14] + chars[15] + chars[17] + chars[18];
        return masked;
    }
    
    public String GenerateTokenExchangeXml(String [] billingSoftwareDatas, String [] SupplierDatas) throws NoSuchAlgorithmException
    {     
        float requestVERSION =          Float.valueOf(billingSoftwareDatas[0]);
        float headerVERSION =           Float.valueOf(billingSoftwareDatas[1]);
        float softwareMainVERSION =     Float.valueOf(billingSoftwareDatas[2]);
        String softwareID =             billingSoftwareDatas[3];
        String softwareNAME =           billingSoftwareDatas[4];
        String softwareOPERATION =      billingSoftwareDatas[5];       
        String softwareDevNAME =        billingSoftwareDatas[6];
        String softwareDevCONTACT =     billingSoftwareDatas[7];
        String softwareDevCountryCODE = billingSoftwareDatas[8];
        String softwareTaxNUMBER =      billingSoftwareDatas[9];
        
        int taxNUMBER =         Integer.valueOf(SupplierDatas[0]);
        String Login =          SupplierDatas[1];
        String passwordHASH =   SupplierDatas[2];     
        String signingKey =     SupplierDatas[3];
        
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        String timeSTAMP = dateFormat.format(new Date());
        String maskedTimeStamp = maskTimeStamp(timeSTAMP);
        String requestID = getRandomId("[+a-zA-Z0-9_]{16,16}") + maskedTimeStamp;
        passwordHASH = getSHA512(passwordHASH);
        String toHash = requestID + maskedTimeStamp + signingKey;
        String requestSIGNATURE = generateRequestSignature(requestID, null, signingKey, maskedTimeStamp);
        //String requestSIGNATURE = getSHA512(toHash).toUpperCase();
        
        try
        {
            DocumentBuilderFactory docFactory =  DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();        
            Document doc = docBuilder.newDocument();
            
            Element tokenExchangeRequest = doc.createElement("TokenExchangeRequest");
            Attr attr = doc.createAttribute("xmlns");
            attr.setValue("http://schemas.nav.gov.hu/OSA/1.0/api");
            tokenExchangeRequest.setAttributeNode(attr);
            doc.appendChild(tokenExchangeRequest);
            
            createBasicHeaderType(doc, tokenExchangeRequest, requestID, timeSTAMP, requestVERSION, headerVERSION);
            createBasicUserType(doc, tokenExchangeRequest, Login, passwordHASH, taxNUMBER, requestSIGNATURE);
            createBasicSoftwareType(doc, tokenExchangeRequest, softwareID, softwareNAME, softwareOPERATION, softwareMainVERSION, softwareDevNAME, softwareDevCONTACT, softwareDevCountryCODE, softwareTaxNUMBER);               
            String output = CreateXmlFile(doc);
            return output;
        }
        catch (ParserConfigurationException ex)
        {
            ex.printStackTrace(System.out);
            String output = "Hiba a token exchange xml fájlkészítése közben!";
            return output;
        }       
    }

    public String GenerateManageInvoiceXml(String [] billingSoftwareDatas, String [] SupplierDatas, boolean technicalANNULMENT, boolean compressedCONTENT, int invoiceOperationCount, String xmlToUpload, String operation) throws IOException, KeyStoreException, UnrecoverableKeyException, CertificateException, KeyManagementException, Exception
    {
        float requestVERSION =          Float.valueOf(billingSoftwareDatas[0]);
        float headerVERSION =           Float.valueOf(billingSoftwareDatas[1]);
        float softwareMainVERSION =     Float.valueOf(billingSoftwareDatas[2]);
        String softwareID =             billingSoftwareDatas[3];
        String softwareNAME =           billingSoftwareDatas[4];
        String softwareOPERATION =      billingSoftwareDatas[5];       
        String softwareDevNAME =        billingSoftwareDatas[6];
        String softwareDevCONTACT =     billingSoftwareDatas[7];
        String softwareDevCountryCODE = billingSoftwareDatas[8];
        String softwareTaxNUMBER =      billingSoftwareDatas[9];
        
        int taxNUMBER =         Integer.valueOf(SupplierDatas[0]);
        String Login =          SupplierDatas[1];
        String passwordHASH =   SupplierDatas[2];     
        String signingKey =     SupplierDatas[3];
        String exchangeKey =    SupplierDatas[4];
        
        String xml = GenerateTokenExchangeXml(billingSoftwareDatas, SupplierDatas);
        String query = "tokenExchange";
        NAVConn nav = new NAVConn();
        String exchangeTOKEN = nav.GetExToken(query, xml);
        exchangeTOKEN = decrypt_data(exchangeTOKEN, exchangeKey);
        
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        String timeSTAMP = dateFormat.format(new Date());
        String maskedTimeStamp = maskTimeStamp(timeSTAMP);
        String requestID = getRandomId("[+a-zA-Z0-9_]{16,16}") + maskedTimeStamp;         
        passwordHASH = getSHA512(passwordHASH);
        
        String checksum = "";
        Long invoiceCRC32 = null;
        for(int i=0; i<invoiceOperationCount; i++)
        {
            String file = new String(xmlToUpload);
            String res = new String(Base64.encodeBase64(file.getBytes("UTF-8")));
            byte [] bytes = res.getBytes("UTF-8");
            Checksum sum = new CRC32();
            sum.update(bytes, 0, bytes.length);
            long value = sum.getValue();
            invoiceCRC32 = value;
            checksum = Long.toString(value);
        }
        String toHash = requestID + maskedTimeStamp + signingKey + checksum;
        String requestSIGNATURE = generateRequestSignature(requestID, invoiceCRC32, signingKey, maskedTimeStamp);
        //String requestSIGNATURE = getSHA512(toHash).toUpperCase();
        
        try
        {
            DocumentBuilderFactory docFactory =  DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();        
            Document doc = docBuilder.newDocument();
            
            Element manageInvoiceRequest = doc.createElement("ManageInvoiceRequest");
            doc.appendChild(manageInvoiceRequest);
            
            Attr attr = doc.createAttribute("xmlns");
            attr.setValue("http://schemas.nav.gov.hu/OSA/1.0/api");
            manageInvoiceRequest.setAttributeNode(attr);
            
            createBasicHeaderType(doc, manageInvoiceRequest, requestID, timeSTAMP, requestVERSION, headerVERSION);
            createBasicUserType(doc, manageInvoiceRequest, Login, passwordHASH, taxNUMBER, requestSIGNATURE);
            createBasicSoftwareType(doc, manageInvoiceRequest, softwareID, softwareNAME, softwareOPERATION, Float.valueOf(softwareMainVERSION), softwareDevNAME, softwareDevCONTACT, softwareDevCountryCODE, softwareTaxNUMBER);               
            
            Element exchangeToken = doc.createElement("exchangeToken");
            exchangeToken.appendChild(doc.createTextNode(exchangeTOKEN));
            manageInvoiceRequest.appendChild(exchangeToken);
            
            Element invoiceOperations = doc.createElement("invoiceOperations");
            manageInvoiceRequest.appendChild(invoiceOperations);
            
                Element technicalAnnulment = doc.createElement("technicalAnnulment");
                technicalAnnulment.appendChild(doc.createTextNode(String.valueOf(technicalANNULMENT)));
                invoiceOperations.appendChild(technicalAnnulment);
                
                Element compressedContent = doc.createElement("compressedContent");
                compressedContent.appendChild(doc.createTextNode(String.valueOf(compressedCONTENT)));
                invoiceOperations.appendChild(compressedContent);                                  
                
                for(int i=0; i<invoiceOperationCount; i++)
                {
                    String file = xmlToUpload;
                    System.err.println("INVOICE");
                    System.err.println(xmlToUpload);
                    String res = new String(Base64.encodeBase64(file.getBytes()));                                      
                    
                    if(technicalANNULMENT)
                    {
                        createBasicInvoiceOperationType(doc, invoiceOperations, i+1, "ANNUL", res);
                    }
                    else
                    {
                        System.out.print(operation);
                        switch(operation)
                        {
                            case "UJ":
                                createBasicInvoiceOperationType(doc, invoiceOperations, i+1, "CREATE", res);
                                break;
                            case "MASOLAT":
                                createBasicInvoiceOperationType(doc, invoiceOperations, i+1, "CREATE", res);
                                break;
                            case "DEVIZA":
                                createBasicInvoiceOperationType(doc, invoiceOperations, i+1, "CREATE", res);
                                break;
                            case "MODOSIT":
                                createBasicInvoiceOperationType(doc, invoiceOperations, i+1, "MODIFY", res);
                                break;
                            case "STORNO":
                                createBasicInvoiceOperationType(doc, invoiceOperations, i+1, "STORNO", res);
                                break;
                        }
                    }
                }               

            String output = CreateXmlFile(doc);
            return output;
        }
        catch (ParserConfigurationException ex)
        {
            ex.printStackTrace(System.out);
            String output = "";
            return output + ";" + exchangeTOKEN;
        }
    }   
    
    public String GenerateQueryInvoiceStatusXml(String [] billingSoftwareDatas, String [] SupplierDatas, String xmlToUpload, String operation) throws IOException, KeyStoreException, UnrecoverableKeyException, CertificateException, KeyManagementException, Exception
    {
        float requestVERSION =          Float.valueOf(billingSoftwareDatas[0]);
        float headerVERSION =           Float.valueOf(billingSoftwareDatas[1]);
        float softwareMainVERSION =     Float.valueOf(billingSoftwareDatas[2]);
        String softwareID =             billingSoftwareDatas[3];
        String softwareNAME =           billingSoftwareDatas[4];
        String softwareOPERATION =      billingSoftwareDatas[5];       
        String softwareDevNAME =        billingSoftwareDatas[6];
        String softwareDevCONTACT =     billingSoftwareDatas[7];
        String softwareDevCountryCODE = billingSoftwareDatas[8];
        String softwareTaxNUMBER =      billingSoftwareDatas[9];
        
        int taxNUMBER =         Integer.valueOf(SupplierDatas[0]);
        String Login =          SupplierDatas[1];
        String passwordHASH =   SupplierDatas[2];     
        String signingKey =     SupplierDatas[3];
        
        String file = "";
        String query = "manageInvoice";
        String xml = GenerateManageInvoiceXml(billingSoftwareDatas, SupplierDatas, false, false, 1, xmlToUpload, operation);
        String s = xml;
        byte[] bytes = s.getBytes("UTF-8");
        NAVConn nav = new NAVConn();
        //String TransactionXml = nav.GetTransactionID("https://api-test.onlineszamla.nav.gov.hu/invoiceService/"+query,xml);      
        String TransactionXml = nav.GetTransactionID(query,xml);
        String IdOfTransaction = nav.GetTransactionId(TransactionXml);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        String timeSTAMP = dateFormat.format(new Date());
        String maskedTimeStamp = maskTimeStamp(timeSTAMP);
        String requestID = getRandomId("[+a-zA-Z0-9_]{16,16}") + maskedTimeStamp;
        passwordHASH = getSHA512(passwordHASH);
        String toHash = requestID + maskedTimeStamp + signingKey;
        String requestSIGNATURE = generateRequestSignature(requestID, null, signingKey, maskedTimeStamp);
        //String requestSIGNATURE = getSHA512(toHash).toUpperCase();
        
        try
        {
            DocumentBuilderFactory docFactory =  DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();        
            Document doc = docBuilder.newDocument();
            
            Element queryInvoiceStatusRequest = doc.createElement("QueryInvoiceStatusRequest");
            doc.appendChild(queryInvoiceStatusRequest);
            
            Attr attr = doc.createAttribute("xmlns");
            attr.setValue("http://schemas.nav.gov.hu/OSA/1.0/api");
            queryInvoiceStatusRequest.setAttributeNode(attr);
            
            createBasicHeaderType(doc, queryInvoiceStatusRequest, requestID, timeSTAMP, requestVERSION, headerVERSION);
            createBasicUserType(doc, queryInvoiceStatusRequest, Login, passwordHASH, taxNUMBER, requestSIGNATURE);
            createBasicSoftwareType(doc, queryInvoiceStatusRequest, softwareID, softwareNAME, softwareOPERATION, softwareMainVERSION, softwareDevNAME, softwareDevCONTACT, softwareDevCountryCODE, softwareTaxNUMBER);                      
            
            Element transactionId = doc.createElement("transactionId");
            queryInvoiceStatusRequest.appendChild(transactionId);
            transactionId.appendChild(doc.createTextNode(IdOfTransaction));
            
            Element returnOriginalRequest = doc.createElement("returnOriginalRequest");
            queryInvoiceStatusRequest.appendChild(returnOriginalRequest);
            
            file = CreateXmlFile(doc);
        }
        catch (ParserConfigurationException ex)
        {
            ex.printStackTrace(System.out);
        }
        
        return file + ";" + IdOfTransaction;
    }
    
    public String GetInvoiceStatusXml(String [] billingSoftwareDatas, String [] SupplierDatas, String idOfTransaction) throws IOException, KeyStoreException, UnrecoverableKeyException, CertificateException, KeyManagementException, Exception
    {
        float requestVERSION =          Float.valueOf(billingSoftwareDatas[0]);
        float headerVERSION =           Float.valueOf(billingSoftwareDatas[1]);
        float softwareMainVERSION =     Float.valueOf(billingSoftwareDatas[2]);
        String softwareID =             billingSoftwareDatas[3];
        String softwareNAME =           billingSoftwareDatas[4];
        String softwareOPERATION =      billingSoftwareDatas[5];       
        String softwareDevNAME =        billingSoftwareDatas[6];
        String softwareDevCONTACT =     billingSoftwareDatas[7];
        String softwareDevCountryCODE = billingSoftwareDatas[8];
        String softwareTaxNUMBER =      billingSoftwareDatas[9];
        
        int taxNUMBER =         Integer.valueOf(SupplierDatas[0]);
        String Login =          SupplierDatas[1];
        String passwordHASH =   SupplierDatas[2];     
        String signingKey =     SupplierDatas[3];
        
        String file = "";
        String IdOfTransaction = idOfTransaction;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        String timeSTAMP = dateFormat.format(new Date());
        String requestID = getRandomId("[+a-zA-Z0-9_]{16,16}") + maskTimeStamp(timeSTAMP);
        passwordHASH = getSHA512(passwordHASH);
        String toHash = requestID + maskTimeStamp(timeSTAMP) + signingKey;
        String requestSIGNATURE = generateRequestSignature(requestID, null, signingKey, maskTimeStamp(timeSTAMP));
        //String requestSIGNATURE = getSHA512(toHash).toUpperCase();
        
        try
        {
            DocumentBuilderFactory docFactory =  DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();        
            Document doc = docBuilder.newDocument();
            
            Element queryInvoiceStatusRequest = doc.createElement("QueryInvoiceStatusRequest");
            doc.appendChild(queryInvoiceStatusRequest);
            
            Attr attr = doc.createAttribute("xmlns");
            attr.setValue("http://schemas.nav.gov.hu/OSA/1.0/api");
            queryInvoiceStatusRequest.setAttributeNode(attr);
            
            createBasicHeaderType(doc, queryInvoiceStatusRequest, requestID, timeSTAMP, requestVERSION, headerVERSION);
            createBasicUserType(doc, queryInvoiceStatusRequest, Login, passwordHASH, taxNUMBER, requestSIGNATURE);
            createBasicSoftwareType(doc, queryInvoiceStatusRequest, softwareID, softwareNAME, softwareOPERATION, softwareMainVERSION, softwareDevNAME, softwareDevCONTACT, softwareDevCountryCODE, softwareTaxNUMBER);                      
            
            Element transactionId = doc.createElement("transactionId");
            queryInvoiceStatusRequest.appendChild(transactionId);
            transactionId.appendChild(doc.createTextNode(IdOfTransaction));
            
            Element returnOriginalRequest = doc.createElement("returnOriginalRequest");
            queryInvoiceStatusRequest.appendChild(returnOriginalRequest);
            
            file = CreateXmlFile(doc);
        }
        catch (ParserConfigurationException ex)
        {
            ex.printStackTrace(System.out);
        }
        
        return file;
    }
    
    //Ha szükséges az Invoice Data Query Param.xml létrehozása
    public String GenerateQueryInvoiceDataXml(String [] billingSoftwareDatas, String [] SupplierDatas, int Page, String InvoiceNUMBER, String RequestAllMODIFICATION) throws NoSuchAlgorithmException
    {
        float requestVERSION =          Float.valueOf(billingSoftwareDatas[0]);
        float headerVERSION =           Float.valueOf(billingSoftwareDatas[1]);
        float softwareMainVERSION =     Float.valueOf(billingSoftwareDatas[2]);
        String softwareID =             billingSoftwareDatas[3];
        String softwareNAME =           billingSoftwareDatas[4];
        String softwareOPERATION =      billingSoftwareDatas[5];       
        String softwareDevNAME =        billingSoftwareDatas[6];
        String softwareDevCONTACT =     billingSoftwareDatas[7];
        String softwareDevCountryCODE = billingSoftwareDatas[8];
        String softwareTaxNUMBER =      billingSoftwareDatas[9];
        
        int taxNUMBER =         Integer.valueOf(SupplierDatas[0]);
        String Login =          SupplierDatas[1];
        String passwordHASH =   SupplierDatas[2];     
        String signingKey =     SupplierDatas[3];
        
        String file= "";
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        String timeSTAMP = dateFormat.format(new Date());       
        String requestID = getRandomId("[+a-zA-Z0-9_]{16,16}") + maskTimeStamp(timeSTAMP);         
        passwordHASH = getSHA512(passwordHASH);          
        String toHash = requestID + maskTimeStamp(timeSTAMP) + signingKey;
        String requestSIGNATURE = generateRequestSignature(requestID, null, signingKey, maskTimeStamp(timeSTAMP));
        //String requestSIGNATURE = getSHA512(toHash).toUpperCase();
        
        try
        {
            DocumentBuilderFactory docFactory =  DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();        
            Document doc = docBuilder.newDocument();
            
            Element queryInvoiceDataRequest = doc.createElement("QueryInvoiceDataRequest");
            doc.appendChild(queryInvoiceDataRequest);
            
            createBasicHeaderType(doc, queryInvoiceDataRequest, requestID, timeSTAMP, requestVERSION, headerVERSION);
            createBasicUserType(doc, queryInvoiceDataRequest, Login, passwordHASH, taxNUMBER, requestSIGNATURE);
            createBasicSoftwareType(doc, queryInvoiceDataRequest, softwareID, softwareNAME, softwareOPERATION, softwareMainVERSION, softwareDevNAME, softwareDevCONTACT, softwareDevCountryCODE, softwareTaxNUMBER);
            
            Element page = doc.createElement("page");
            queryInvoiceDataRequest.appendChild(page);           
            page.appendChild(doc.createTextNode(Integer.toString(Page)));
            
            Element invoiceQuery = doc.createElement("invoiceQuery");
            queryInvoiceDataRequest.appendChild(invoiceQuery);
            
            Attr attr = doc.createAttribute("xmlns");
            attr.setValue("http://schemas.nav.gov.hu/OSA/1.0/api");
            queryInvoiceDataRequest.setAttributeNode(attr);
            
                Element invoiceNumber = doc.createElement("invoiceNumber");
                queryInvoiceDataRequest.appendChild(invoiceNumber);
                invoiceNumber.appendChild(doc.createTextNode(InvoiceNUMBER));
                invoiceQuery.appendChild(invoiceNumber);
                
                Element requestAllModification = doc.createElement("requestAllModification");
                queryInvoiceDataRequest.appendChild(requestAllModification);
                requestAllModification.appendChild(doc.createTextNode(RequestAllMODIFICATION));
                invoiceQuery.appendChild(requestAllModification);
            
            file = CreateXmlFile(doc);
        }
        catch (ParserConfigurationException ex)
        {
            ex.printStackTrace(System.out);
            file = "";
        }
        return file;
    }
    
    public void GenerateQueryTaxpayerRequestXml(String [] billingSoftwareDatas , String [] SupplierDatas) throws NoSuchAlgorithmException
    {
        float requestVERSION =          Float.valueOf(billingSoftwareDatas[0]);
        float headerVERSION =           Float.valueOf(billingSoftwareDatas[1]);
        float softwareMainVERSION =     Float.valueOf(billingSoftwareDatas[2]);
        String softwareID =             billingSoftwareDatas[3];
        String softwareNAME =           billingSoftwareDatas[4];
        String softwareOPERATION =      billingSoftwareDatas[5];       
        String softwareDevNAME =        billingSoftwareDatas[6];
        String softwareDevCONTACT =     billingSoftwareDatas[7];
        String softwareDevCountryCODE = billingSoftwareDatas[8];
        String softwareTaxNUMBER =      billingSoftwareDatas[9];
        
        int taxNUMBER =         Integer.valueOf(SupplierDatas[0]);
        String Login =          SupplierDatas[1];
        String passwordHASH =   SupplierDatas[2];     
        String signingKey =     SupplierDatas[3];
        
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        String timeSTAMP = dateFormat.format(new Date());       
        String requestID = getRandomId("[+a-zA-Z0-9_]{16,16}") + maskTimeStamp(timeSTAMP);         
        passwordHASH = getSHA512(passwordHASH);          
        String toHash = requestID + maskTimeStamp(timeSTAMP) + signingKey;
        String requestSIGNATURE = generateRequestSignature(requestID, null, signingKey, maskTimeStamp(timeSTAMP));
        //String requestSIGNATURE = getSHA512(toHash).toUpperCase();
        
        try
        {
            DocumentBuilderFactory docFactory =  DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();        
            Document doc = docBuilder.newDocument();
            
            Element queryTaypayerRequest = doc.createElement("QueryInvoiceDataRequest");
            doc.appendChild(queryTaypayerRequest);
            
            createBasicHeaderType(doc, queryTaypayerRequest, requestID, timeSTAMP, requestVERSION, headerVERSION);
            createBasicUserType(doc, queryTaypayerRequest, Login, passwordHASH, taxNUMBER, requestSIGNATURE);
            createBasicSoftwareType(doc, queryTaypayerRequest, softwareID, softwareNAME, softwareOPERATION, softwareMainVERSION, softwareDevNAME, softwareDevCONTACT, softwareDevCountryCODE, softwareTaxNUMBER);
            
            Element taxnumber = doc.createElement("taxNumber");
            queryTaypayerRequest.appendChild(taxnumber);
            taxnumber.appendChild(doc.createTextNode(Integer.toString(taxNUMBER)));
            
            CreateXmlFile(doc);
        }
        catch (ParserConfigurationException ex)
        {
            ex.printStackTrace(System.out);
        }
    }
}