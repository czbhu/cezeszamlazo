/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeszamlazo.hu.gov.nav.onlineszamla;

import cezeszamlazo.App;
import cezeszamlazo.NAVConn;
import cezeszamlazo.database.Query;
import static cezeszamlazo.hu.gov.nav.onlineszamla.GenerateXml.getStringFromDocument;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
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

/**
 *
 * @author Tomy
 */
public class GenerateInvoiceXMLs
{
    private final dbconn conn = new dbconn();
    private final Connection NAVcon = conn.getConnection();
    
    protected String encoding(
        String str) throws UnsupportedEncodingException {

        if (str == null)
          return null;

        return new String(str.getBytes("UTF-8"));
    }
    
    public void CreateBasicInvoiceType(Document doc)
    {
        Element Invoice = doc.createElement("Invoice");
        doc.appendChild(Invoice);
        
            Attr xmlns_xs = doc.createAttribute("xmlns:xs");
            xmlns_xs.setValue("http://www.w3.org/2001/XMLSchema-instance");
            Invoice.setAttributeNode(xmlns_xs);
            
            Attr xmlns = doc.createAttribute("xmlns");
            xmlns.setValue("http://schemas.nav.gov.hu/OSA/1.0/data");
            Invoice.setAttributeNode(xmlns);
            
            /*Attr xs_schemeLocation = doc.createAttribute("xs:schemeLocation");
            xs_schemeLocation.setValue("http://schemas.nav.gov.hu/OSA/1.0/data invoiceData.xsd");
            Invoice.setAttributeNode(xs_schemeLocation);*/
            
            Element invoiceExchange = doc.createElement("invoiceExchange");
            Invoice.appendChild(invoiceExchange);
    }
    
    public void CreateBasicInvoiceReferenceType(Document doc, Element parent, String [] referenceDatas)
    {
        String originalInvoiceNUMBER = referenceDatas[0];
        String modificationIssueDATE = referenceDatas[1];
        String modificationTIMESTAMP = referenceDatas[2];
        String lastModificationREFERENCE = referenceDatas[3];
        String modifyWithoutMASTER = referenceDatas[4];
    
        if(!originalInvoiceNUMBER.equals("") && !modificationIssueDATE.equals("") && !modificationTIMESTAMP.equals("") && !modifyWithoutMASTER.equals(""))
        {
        //invoiceReference nem kötelező
            Element invoiceReference = doc.createElement("invoiceReference");
            parent.appendChild(invoiceReference);
            //originalInvoiceNumber      kötelező
                Element originalInvoiceNumber = doc.createElement("originalInvoiceNumber");
                invoiceReference.appendChild(originalInvoiceNumber);
                originalInvoiceNumber.appendChild(doc.createTextNode(originalInvoiceNUMBER));
            //modificationIssueDate     kötelező
                Element modificationIssueDate = doc.createElement("modificationIssueDate");
                invoiceReference.appendChild(modificationIssueDate);
                modificationIssueDate.appendChild(doc.createTextNode(modificationIssueDATE));
            //modificationTimestamp     kötelező
                Element modificationTimestamp = doc.createElement("modificationTimestamp");
                invoiceReference.appendChild(modificationTimestamp);
                modificationTimestamp.appendChild(doc.createTextNode(modificationTIMESTAMP));
            //lastModificationReference nem kötelező
                if(!lastModificationREFERENCE.equals(""))
                {
                    Element lastModificationReference = doc.createElement("lastModificationReference");
                    invoiceReference.appendChild(lastModificationReference);
                    lastModificationReference.appendChild(doc.createTextNode(lastModificationREFERENCE));
                }
            //modifyWithoutMaster       kötelező
                Element modifyWithoutMaster = doc.createElement("modifyWithoutMaster");
                invoiceReference.appendChild(modifyWithoutMaster);
                modifyWithoutMaster.appendChild(doc.createTextNode(modifyWithoutMASTER));
        }
    }
    
    public void CreateBasicInvoiceHeadType(Document doc, Element parent, String [] supplierINFO, String [] customerINFO, String [] fiscalRepresentativeINFO, String[] invoiceDATA, String [][] additionalInvoiceDATA)
    {
        for (int i=0; i<supplierINFO.length; i++) {
            if (supplierINFO[i] == null) {
                supplierINFO[i]="";
            }
        }
        for (int i=0; i<customerINFO.length; i++) {
            if (customerINFO[i] == null) {
                customerINFO[i]="";
            }
        }
        for (int i=0; i<fiscalRepresentativeINFO.length; i++) {
            if (fiscalRepresentativeINFO[i] == null) {
                fiscalRepresentativeINFO[i]="";
            }
        }
        for (int i=0; i<invoiceDATA.length; i++) {
            if (invoiceDATA[i] == null) {
                invoiceDATA[i]="";
            }
        }
        /*for (int i=0; i<additionalInvoiceDATA.length; i++) {
            if (additionalInvoiceDATA[i] == null) {
                additionalInvoiceDATA[i]="";
            }
        }*/
        
//supplierINFO
    //supplierTaxNumber
        String supplierTaxPayerID           = supplierINFO[0];
        String supplierVatCODE              = supplierINFO[1];
        String supplierCountyCODE           = supplierINFO[2];              
    //groupMemberTaxNumber
        String supplierGMtaxPayerID         = supplierINFO[3];
        String supplierGMvatCODE            = supplierINFO[4];
        String supplierGMcountyCODE         = supplierINFO[5];                
    //communityVatNumber
        String supplierCommunityVatNUMBER   = supplierINFO[6];               
    //supplierName
        String supplierNAME                 = supplierINFO[7];      
    //supplierAddress
        String supplierCountryCODE          = supplierINFO[8];
        String supplierREGION               = supplierINFO[9];
        String supplierPostalCODE           = supplierINFO[10];
        String supplierCITY                 = supplierINFO[11];
        String supplierStreetNAME           = supplierINFO[12];
        String supplierPublicPlaceCATEGORY  = supplierINFO[13];
        String supplierNUMBER               = supplierINFO[14];
        String supplierBUILDING             = supplierINFO[15];
        String supplierSTAIRCASE            = supplierINFO[16];
        String supplierFLOOR                = supplierINFO[17];
        String supplierDOOR                 = supplierINFO[18];
        String supplierLotNUMBER            = supplierINFO[19];              
    //supplierBankAccountNumber
        String supplierBankAccountNUMBER    = supplierINFO[20];          
    //individualExemption
        String individualEXEMPTION          = supplierINFO[21];               
    //exciseLicenceNum
        String exciseLicenceNUM             = supplierINFO[22];             
//customerINFO
    //customerTaxNumber
        String customerTaxPayerID           = customerINFO[0];
        String cutomerVatCODE               = customerINFO[1];
        String customerCountyCODE           = customerINFO[2];               
    //groupMemberTaxNumber
        String customerGMtaxPayerID         = customerINFO[3];
        String customerGMvatCODE            = customerINFO[4];
        String customerGMcountyCODE         = customerINFO[5];              
    //communityVatNumber
        String customerCommunityVatNUMBER   = customerINFO[6];                
    //thirdStateTaxId
        String thirdStateTaxID              = customerINFO[7];               
        //customerName
        String customerNAME                 = customerINFO[8];          
    //customerAddress
        String customerCountryCODE          = customerINFO[9];
        String customerREGION               = customerINFO[1];
        String customerPostalCODE           = customerINFO[11];
        String customerCITY                 = customerINFO[12];
        String customerStreetNAME           = customerINFO[13];
        String customerPublicPlaceCATEGORY  = customerINFO[14];
        String customerNUMBER               = customerINFO[15];
        String customerBUILDING             = customerINFO[16];
        String customerSTAIRCASE            = customerINFO[17];
        String customerFLOOR                = customerINFO[18];
        String customerDOOR                 = customerINFO[19];
        String customerLotNUMBER            = customerINFO[20];               
    //customerBankAccountNumber
        String customerBankAccountNUMBER    = customerINFO[21];                
//fiscalRepresentativeINFO
    //fiscalRepresentativeTaxNumber
        String fiscalTaxPayerID             = fiscalRepresentativeINFO[0];
        String fiscalVatCODE                = fiscalRepresentativeINFO[1];
        String fiscalCountyCODE             = fiscalRepresentativeINFO[2];                
    //fiscalRepresentativeName
        String fiscalRepresentativeNAME     = fiscalRepresentativeINFO[3];          
    //fiscalRepresentativeAddress
        String fiscalCountryCODE            = fiscalRepresentativeINFO[4];
        String fiscalREGION                 = fiscalRepresentativeINFO[5];
        String fiscalPostalCODE             = fiscalRepresentativeINFO[6];
        String fiscalCITY                   = fiscalRepresentativeINFO[7];
        String fiscalStreetNAME             = fiscalRepresentativeINFO[8];
        String fiscalPublicPlaceCATEGORY    = fiscalRepresentativeINFO[9];
        String fiscalNUMBER                 = fiscalRepresentativeINFO[10];
        String fiscalBUILDING               = fiscalRepresentativeINFO[11];
        String fiscalSTAIRCASE              = fiscalRepresentativeINFO[12];
        String fiscalFLOOR                  = fiscalRepresentativeINFO[13];
        String fiscalDOOR                   = fiscalRepresentativeINFO[14];
        String fiscalLotNUMBER              = fiscalRepresentativeINFO[15];          
    //fiscalRepresentativeBankAccountNumber
        String fiscalRepresentativeBankAccountNUMBER= fiscalRepresentativeINFO[16];             
//invoiceDATA
        String invoiceNUMBER                =invoiceDATA[0];
        String invoiceCATEGORY              =invoiceDATA[1];
        String invoiceIssueDATE             =invoiceDATA[2];
        String invoiceDeliveryDATE          =invoiceDATA[3];
        String invoiceDeliveryPeriodSTART   =invoiceDATA[4];
        String invoiceDeliveryPeriodEND     =invoiceDATA[5];
        String invoiceAccountingDeliveryDATE=invoiceDATA[6];
        String currencyCODE                 =invoiceDATA[7];
        String exchangeRATE                 =invoiceDATA[8];
        String selfBillingINDICATOR         =invoiceDATA[9];
        String paymentMETHOD                =invoiceDATA[10];
        String paymentDATE                  =invoiceDATA[11];
        String cashAccountingINDICATOR      =invoiceDATA[12];
        String invoiceAPPEARANCE            =invoiceDATA[13];
        String electronicInvoiceHASH        =invoiceDATA[14];
                
    //invoiceHead
        Element invoiceHead = doc.createElement("invoiceHead");
        parent.appendChild(invoiceHead);       
        //supplierInfo              kötelező
            Element supplierInfo = doc.createElement("supplierInfo");
            invoiceHead.appendChild(supplierInfo);
            //supplierTaxNumber         kötelező
                Element supplierTaxNumber = doc.createElement("supplierTaxNumber");
                supplierInfo.appendChild(supplierTaxNumber);               
                //taxPayerId                kötelező
                    Element taxPayerId = doc.createElement("taxpayerId");
                    supplierTaxNumber.appendChild(taxPayerId);
                    taxPayerId.appendChild(doc.createTextNode(supplierTaxPayerID));
            if(!supplierVatCODE.equals(""))
            {
                //vatCode                   nem kötelező
                    Element vatCode = doc.createElement("vatCode");
                    supplierTaxNumber.appendChild(vatCode);
                    vatCode.appendChild(doc.createTextNode(supplierVatCODE));
            }
            if(!supplierCountyCODE.equals(""))
            {
                //countyCode                nem kötelező
                    Element countyCode = doc.createElement("countyCode");
                    supplierTaxNumber.appendChild(countyCode);
                    countyCode.appendChild(doc.createTextNode(supplierCountyCODE));
            }
            System.out.println("supplierGMtaxPayerID: " + supplierGMtaxPayerID);
        if(!supplierGMtaxPayerID.equals(""))
        {
            //groupMemberTaxNumber      nem kötelező
                Element groupMemberTaxNumber = doc.createElement("groupMemberTaxNumber");
                supplierInfo.appendChild(groupMemberTaxNumber);
                //taxPayerId                kötelező
                    Element GMtaxPayerId = doc.createElement("taxpayerId");
                    groupMemberTaxNumber.appendChild(GMtaxPayerId);
                    GMtaxPayerId.appendChild(doc.createTextNode(supplierGMtaxPayerID));
            if(!supplierGMvatCODE.equals(""))
            {
                //vatCode                   nem kötelező
                    Element GMvatCode = doc.createElement("vatCode");
                    groupMemberTaxNumber.appendChild(GMvatCode);
                    GMvatCode.appendChild(doc.createTextNode(supplierGMvatCODE));
            }
            if(!supplierGMcountyCODE.equals(""))
            {
                //countyCode                nem kötelező
                    Element GMcountyCode = doc.createElement("countyCode");
                    groupMemberTaxNumber.appendChild(GMcountyCode);
                    GMcountyCode.appendChild(doc.createTextNode(supplierGMcountyCODE));
            }
        }
        if(!supplierCommunityVatNUMBER.equals(""))
        {
            //communityVatNumber         nem kötelező
                Element communityVatNumber = doc.createElement("communityVatNumber");
                supplierInfo.appendChild(communityVatNumber);
                communityVatNumber.appendChild(doc.createTextNode(supplierCommunityVatNUMBER));
        }         
            //supplierName              kötelező
                Element supplierName = doc.createElement("supplierName");
                supplierInfo.appendChild(supplierName);
                supplierName.appendChild(doc.createTextNode(supplierNAME));
            //supplierAddress           kötelező
                Element supplierAddress = doc.createElement("supplierAddress");
                supplierInfo.appendChild(supplierAddress);
                //detailedAddress           kötelező
                    Element supplierDetailedAddress = doc.createElement("detailedAddress");
                    supplierAddress.appendChild(supplierDetailedAddress);
                    //countryCode               kötelező
                        Element supplierCountryCode = doc.createElement("countryCode");
                        supplierDetailedAddress.appendChild(supplierCountryCode);
                        supplierCountryCode.appendChild(doc.createTextNode(supplierCountryCODE));
                if(!supplierREGION.equals(""))
                {
                    //region                    nem kötelező
                        Element region = doc.createElement("region");
                        supplierDetailedAddress.appendChild(region);
                        region.appendChild(doc.createTextNode(supplierREGION));
                }
                    //postalCode                kötelező
                        Element postalCode = doc.createElement("postalCode");
                        supplierDetailedAddress.appendChild(postalCode);
                        postalCode.appendChild(doc.createTextNode(supplierPostalCODE));
                    //city                      kötelező
                        Element city = doc.createElement("city");
                        supplierDetailedAddress.appendChild(city);
                        city.appendChild(doc.createTextNode(supplierCITY));
                    //streetName                kötelező
                        Element streetName = doc.createElement("streetName");
                        supplierDetailedAddress.appendChild(streetName);
                        streetName.appendChild(doc.createTextNode(supplierStreetNAME));
                    //publicPlaceCategory       kötelező
                        Element publicPlaceCategory = doc.createElement("publicPlaceCategory");
                        supplierDetailedAddress.appendChild(publicPlaceCategory);
                        publicPlaceCategory.appendChild(doc.createTextNode(supplierPublicPlaceCATEGORY));
                if(!supplierNUMBER.equals(""))
                {
                    //number                    nem kötelező
                        Element number = doc.createElement("number");
                        supplierDetailedAddress.appendChild(number);
                        number.appendChild(doc.createTextNode(supplierNUMBER));
                }
                if(!supplierBUILDING.equals(""))
                {
                    //building                  nem kötelező
                        Element building = doc.createElement("building");
                        supplierDetailedAddress.appendChild(building);
                        building.appendChild(doc.createTextNode(supplierBUILDING));
                }
                if(!supplierSTAIRCASE.equals(""))
                {
                    //staircase                 nem kötelező
                        Element staircase = doc.createElement("staircase");
                        supplierDetailedAddress.appendChild(staircase);
                        staircase.appendChild(doc.createTextNode(supplierSTAIRCASE));
                }
                if(!supplierFLOOR.equals(""))
                {
                    //floor                     nem kötelező
                        Element floor = doc.createElement("floor");
                        supplierDetailedAddress.appendChild(floor);
                        floor.appendChild(doc.createTextNode(supplierFLOOR));
                }
                if(!supplierDOOR.equals(""))
                {
                    //door                      nem kötelező
                        Element door = doc.createElement("door");
                        supplierDetailedAddress.appendChild(door);
                        door.appendChild(doc.createTextNode(supplierDOOR));
                }
                if(!supplierLotNUMBER.equals(""))
                {
                    //lotNumber                 nem kötelező
                        Element lotNumber = doc.createElement("lotNumber");
                        supplierDetailedAddress.appendChild(lotNumber);
                        lotNumber.appendChild(doc.createTextNode(supplierLotNUMBER));
                }
        if(!supplierBankAccountNUMBER.equals(""))
        {
            //supplierBankAccountNumber nem kötelező
                Element supplierBankAccountNumber = doc.createElement("supplierBankAccountNumber");
                supplierInfo.appendChild(supplierBankAccountNumber);
                supplierBankAccountNumber.appendChild(doc.createTextNode(supplierBankAccountNUMBER));
        }
        if(!individualEXEMPTION.equals("false"))
        {
            //individualExemption       nem kötelező
                Element individualExemption = doc.createElement("individualExemption");
                supplierInfo.appendChild(individualExemption);
                individualExemption.appendChild(doc.createTextNode(individualEXEMPTION));
        }
        if(!exciseLicenceNUM.equals(""))
        {
            //exciseLicenceNum          nem kötelező
                Element exciseLicenceNum = doc.createElement("exciseLicenceNum");
                supplierInfo.appendChild(exciseLicenceNum);
                exciseLicenceNum.appendChild(doc.createTextNode(exciseLicenceNUM));
        }
        
    /*if(!customerTaxPayerID.equals(""))
    {*/
        //customerInfo              nem kötelező
            Element customerInfo = doc.createElement("customerInfo");
            invoiceHead.appendChild(customerInfo);
        if(!customerTaxPayerID.equals(""))
        {
            //customerTaxNumber         nem kötelező
                Element customerTaxNumber = doc.createElement("customerTaxNumber");
                customerInfo.appendChild(customerTaxNumber);
                //taxPayerId                kötelező
                    taxPayerId = doc.createElement("taxpayerId");
                    customerTaxNumber.appendChild(taxPayerId);
                    taxPayerId.appendChild(doc.createTextNode(customerTaxPayerID));
            if(!cutomerVatCODE.equals(""))
            {
                //vatCode                   nem kötelező
                    Element cutomerVatCode = doc.createElement("vatCode");
                    customerTaxNumber.appendChild(cutomerVatCode);
                    cutomerVatCode.appendChild(doc.createTextNode(cutomerVatCODE));
            }
            if(!customerCountyCODE.equals(""))
            {
                //countyCode                nem kötelező
                    Element customerCountyCode = doc.createElement("countyCode");
                    customerTaxNumber.appendChild(customerCountyCode);
                    customerCountyCode.appendChild(doc.createTextNode(customerCountyCODE));
            }
        }
        if(!customerGMtaxPayerID.equals(""))
        {
            //groupMemberTaxNumber      nem kötelező
                Element groupMemberTaxNumber = doc.createElement("groupMemberTaxNumber");
                customerInfo.appendChild(groupMemberTaxNumber);
                //taxPayerId                kötelező
                    Element GMtaxPayerId = doc.createElement("taxpayerId");
                    groupMemberTaxNumber.appendChild(GMtaxPayerId);
                    GMtaxPayerId.appendChild(doc.createTextNode(customerGMtaxPayerID));
            if(!customerGMvatCODE.equals(""))
            {
                //vatCode                   nem kötelező
                    Element GMvatCode = doc.createElement("vatCode");
                    groupMemberTaxNumber.appendChild(GMvatCode);
                    GMvatCode.appendChild(doc.createTextNode(customerGMvatCODE));
            }
            if(!customerGMcountyCODE.equals(""))
            {
                //countyCode                nem kötelező
                    Element GMcountyCode = doc.createElement("countyCode");
                    groupMemberTaxNumber.appendChild(GMcountyCode);
                    GMcountyCode.appendChild(doc.createTextNode(customerGMcountyCODE));
            }
        }
        if(!customerCommunityVatNUMBER.equals(""))
        {
            //communityVatNumber        nem kötelező
                Element communityVatNumber = doc.createElement("communityVatNumber");
                customerInfo.appendChild(communityVatNumber);
                communityVatNumber.appendChild(doc.createTextNode(customerCommunityVatNUMBER));
        }
        if(!thirdStateTaxID.equals(""))
        {
            //thirdStateTaxId           nem kötelező
                Element thirdStateTaxId = doc.createElement("thirdStateTaxId");
                customerInfo.appendChild(thirdStateTaxId);
                thirdStateTaxId.appendChild(doc.createTextNode(thirdStateTaxID));
        }
            //customerName              kötelező
                Element customerName = doc.createElement("customerName");
                customerInfo.appendChild(customerName);
                customerName.appendChild(doc.createTextNode(customerNAME));
            //customerAddress           kötelező
                Element customerAddress = doc.createElement("customerAddress");
                customerInfo.appendChild(customerAddress);
                //detailedAddress           kötelező
                    Element customerDetailedAddress = doc.createElement("detailedAddress");
                    customerAddress.appendChild(customerDetailedAddress);
                    //countryCode               kötelező
                        Element customerCountryCode = doc.createElement("countryCode");
                        customerDetailedAddress.appendChild(customerCountryCode);
                        customerCountryCode.appendChild(doc.createTextNode(customerCountryCODE));
                if(!customerREGION.equals(""))
                {
                    //region                    nem kötelező
                        Element region = doc.createElement("region");
                        customerDetailedAddress.appendChild(region);
                        region.appendChild(doc.createTextNode(customerREGION));
                }
                    //postalCode                kötelező
                        postalCode = doc.createElement("postalCode");
                        customerDetailedAddress.appendChild(postalCode);
                        postalCode.appendChild(doc.createTextNode(customerPostalCODE));
                    //city                      kötelező
                        city = doc.createElement("city");
                        customerDetailedAddress.appendChild(city);
                        city.appendChild(doc.createTextNode(customerCITY));
                    //streetName                kötelező
                        streetName = doc.createElement("streetName");
                        customerDetailedAddress.appendChild(streetName);
                        streetName.appendChild(doc.createTextNode(customerStreetNAME));
                    //publicPlaceCategory       kötelező
                        publicPlaceCategory = doc.createElement("publicPlaceCategory");
                        customerDetailedAddress.appendChild(publicPlaceCategory);
                        publicPlaceCategory.appendChild(doc.createTextNode(customerPublicPlaceCATEGORY));
                if(!customerNUMBER.equals(""))
                {
                    //number                    nem kötelező
                        Element number = doc.createElement("number");
                        customerDetailedAddress.appendChild(number);
                        number.appendChild(doc.createTextNode(customerNUMBER));
                }
                if(!customerBUILDING.equals(""))
                {
                    //building                  nem kötelező
                        Element building = doc.createElement("building");
                        customerDetailedAddress.appendChild(building);
                        building.appendChild(doc.createTextNode(customerBUILDING));
                }
                if(!customerSTAIRCASE.equals(""))
                {
                    //staircase                 nem kötelező
                        Element staircase = doc.createElement("staircase");
                        customerDetailedAddress.appendChild(staircase);
                        staircase.appendChild(doc.createTextNode(customerSTAIRCASE));
                }
                if(!customerFLOOR.equals(""))
                {
                    //floor                     nem kötelező
                        Element floor = doc.createElement("floor");
                        customerDetailedAddress.appendChild(floor);
                        floor.appendChild(doc.createTextNode(customerFLOOR));
                }
                if(!customerDOOR.equals(""))
                {
                    //door                      nem kötelező
                        Element door = doc.createElement("door");
                        customerDetailedAddress.appendChild(door);
                        door.appendChild(doc.createTextNode(customerDOOR));
                }
                if(!customerLotNUMBER.equals(""))
                {
                    //lotNumber                 nem kötelező
                        Element lotNumber = doc.createElement("lotNumber");
                        customerDetailedAddress.appendChild(lotNumber);
                        lotNumber.appendChild(doc.createTextNode(customerLotNUMBER));
                }
        if(!customerBankAccountNUMBER.equals(""))
        {
            //customerBankAccountNumber nem kötelező
                Element customerBankAccountNumber  = doc.createElement("customerBankAccountNumber");
                customerDetailedAddress.appendChild(customerBankAccountNumber);
                customerBankAccountNumber.appendChild(doc.createTextNode(customerBankAccountNUMBER));
        }
    /*}*/
    if(!fiscalTaxPayerID.equals("") && !fiscalRepresentativeNAME.equals("") && !fiscalCountryCODE.equals(""))
    {
        //fiscalRepresentativeInfo  nem kötelező
            Element fiscalRepresentativeInfo = doc.createElement("fiscalRepresentativeInfo");
            invoiceHead.appendChild(fiscalRepresentativeInfo);
            //fiscalRepresentativeTaxNumber kötelező
                Element fiscalRepresentativeTaxNumber = doc.createElement("fiscalRepresentativeTaxNumber");
                fiscalRepresentativeInfo.appendChild(fiscalRepresentativeTaxNumber);
                //taxPayerId                    kötelező
                    Element fiscalTaxpayerId = doc.createElement("taxpayerId");
                    fiscalRepresentativeTaxNumber.appendChild(fiscalTaxpayerId);
                    fiscalTaxpayerId.appendChild(doc.createTextNode(fiscalTaxPayerID));
            if(!fiscalVatCODE.equals(""))
            {
                //vatCode                       nem kötelező
                    Element vatCode = doc.createElement("vatCode");
                    fiscalRepresentativeTaxNumber.appendChild(vatCode);
                    vatCode.appendChild(doc.createTextNode(fiscalVatCODE));
            }
            if(!fiscalCountyCODE.equals(""))
            {
                //countyCode                    nem kötelező
                    Element fiscalCountyCode = doc.createElement("countycode");
                    fiscalRepresentativeTaxNumber.appendChild(fiscalCountyCode);
                    fiscalCountyCode.appendChild(doc.createTextNode(fiscalCountyCODE));
            }
            //fiscalRepresentativeName      kötelező
                Element fiscalRepresentativeName = doc.createElement("fiscalRepresentativeName");
                fiscalRepresentativeInfo.appendChild(fiscalRepresentativeName);
                fiscalRepresentativeName.appendChild(doc.createTextNode(fiscalRepresentativeNAME));
            //fiscalRepresentativeAddress   kötelező
                Element fiscalRepresentativeAddress = doc.createElement("fiscalRepresentativeAddress");
                fiscalRepresentativeInfo.appendChild(fiscalRepresentativeAddress);
                //detailedAddress           kötelező
                    Element fiscalDetailedAddress = doc.createElement("detailedAddress");
                    fiscalRepresentativeAddress.appendChild(fiscalDetailedAddress);
                    //countryCode               kötelező
                        Element fiscalCountryCode = doc.createElement("countryCode");
                        fiscalDetailedAddress.appendChild(fiscalCountryCode);
                        fiscalCountryCode.appendChild(doc.createTextNode(fiscalCountryCODE));
                if(!fiscalREGION.equals(""))
                {
                    //region                    nem kötelező
                        Element region = doc.createElement("region");
                        fiscalDetailedAddress.appendChild(region);
                        region.appendChild(doc.createTextNode(fiscalREGION));
                }
                    //postalCode                kötelező
                        Element fiscalPostalCode = doc.createElement("postalCode");
                        fiscalDetailedAddress.appendChild(fiscalPostalCode);
                        fiscalPostalCode.appendChild(doc.createTextNode(fiscalPostalCODE));
                    //city                      kötelező
                        Element fiscalCity = doc.createElement("city");
                        fiscalDetailedAddress.appendChild(fiscalCity);
                        fiscalCity.appendChild(doc.createTextNode(fiscalCITY));
                    //streetName                kötelező
                        Element fiscalStreetName = doc.createElement("streetName");
                        fiscalDetailedAddress.appendChild(fiscalStreetName);
                        fiscalStreetName.appendChild(doc.createTextNode(fiscalStreetNAME));
                    //publicPlaceCategory       kötelező
                        Element fiscalPublicPlaceCategory = doc.createElement("publicPlaceCategory");
                        fiscalDetailedAddress.appendChild(fiscalPublicPlaceCategory);
                        fiscalPublicPlaceCategory.appendChild(doc.createTextNode(fiscalPublicPlaceCATEGORY));
                if(!fiscalNUMBER.equals(""))
                {
                    //number                    nem kötelező
                        Element number = doc.createElement("number");
                        fiscalDetailedAddress.appendChild(number);
                        number.appendChild(doc.createTextNode(fiscalNUMBER));
                }
                if(!fiscalBUILDING.equals(""))
                {
                    //building                  nem kötelező
                        Element building = doc.createElement("building");
                        fiscalDetailedAddress.appendChild(building);
                        building.appendChild(doc.createTextNode(fiscalBUILDING));
                }
                if(!fiscalSTAIRCASE.equals(""))
                {
                    //staircase                 nem kötelező
                        Element staircase = doc.createElement("staircase");
                        fiscalDetailedAddress.appendChild(staircase);
                        staircase.appendChild(doc.createTextNode(fiscalSTAIRCASE));
                }
                if(!fiscalFLOOR.equals(""))
                {
                    //floor                     nem kötelező
                        Element floor = doc.createElement("floor");
                        fiscalDetailedAddress.appendChild(floor);
                        floor.appendChild(doc.createTextNode(fiscalFLOOR));
                }
                if(!fiscalDOOR.equals(""))
                {
                    //door                      nem kötelező
                        Element door = doc.createElement("door");
                        fiscalDetailedAddress.appendChild(door);
                        door.appendChild(doc.createTextNode(fiscalDOOR));
                }
                if(!fiscalLotNUMBER.equals(""))
                {
                    //lotNumber                 nem kötelező
                        Element lotNumber = doc.createElement("lotNumber");
                        fiscalDetailedAddress.appendChild(lotNumber);
                        lotNumber.appendChild(doc.createTextNode(fiscalLotNUMBER));
                }
        if(!fiscalRepresentativeBankAccountNUMBER.equals(""))
        {
            //fiscalRepresentativeBankAccountNumber     nem kötelező
                Element fiscalRepresentativeBankAccountNumber = doc.createElement("fiscalRepresentativeBankAccountNumber");
                fiscalRepresentativeInfo.appendChild(fiscalRepresentativeBankAccountNumber);
                fiscalRepresentativeBankAccountNumber.appendChild(doc.createTextNode(fiscalRepresentativeBankAccountNUMBER));
        }
    }
        //invoiceData               kötelező
            Element invoiceData = doc.createElement("invoiceData");
            invoiceHead.appendChild(invoiceData);
            //invoiceNumber                 kötelező
                Element invoiceNumber = doc.createElement("invoiceNumber");
                invoiceData.appendChild(invoiceNumber);
                invoiceNumber.appendChild(doc.createTextNode(invoiceNUMBER));
            //invoiceCategory               kötelező
                Element invoiceCategory = doc.createElement("invoiceCategory");
                invoiceData.appendChild(invoiceCategory);
                invoiceCategory.appendChild(doc.createTextNode(invoiceCATEGORY));
        if(!invoiceIssueDATE.equals(""))
        {
            //invoiceIssueDate              nem kötelező
                Element invoiceIssueDate = doc.createElement("invoiceIssueDate");
                invoiceData.appendChild(invoiceIssueDate);
                invoiceIssueDate.appendChild(doc.createTextNode(invoiceIssueDATE));
        }
        if(!invoiceDeliveryDATE.equals(""))
        {
            //invoiceDeliveryDate           nem kötelező
                Element invoiceDeliveryDate = doc.createElement("invoiceDeliveryDate");
                invoiceData.appendChild(invoiceDeliveryDate);
                invoiceDeliveryDate.appendChild(doc.createTextNode(invoiceDeliveryDATE));
        }
        if(!invoiceDeliveryPeriodSTART.equals(""))
        {
            //invoiceDeliveryPeriodStart    nem kötelező
                Element invoiceDeliveryPeriodStart = doc.createElement("invoiceDeliveryPeriodStart");
                invoiceData.appendChild(invoiceDeliveryPeriodStart);
                invoiceDeliveryPeriodStart.appendChild(doc.createTextNode(invoiceDeliveryPeriodSTART));
        }
        if(!invoiceDeliveryPeriodEND.equals(""))
        {
            //invoiceDeliveryPeriodEnd      nem kötelező
                Element invoiceDeliveryPeriodEnd = doc.createElement("invoiceDeliveryPeriodEnd");
                invoiceData.appendChild(invoiceDeliveryPeriodEnd);
                invoiceDeliveryPeriodEnd.appendChild(doc.createTextNode(invoiceDeliveryPeriodEND));
        }
        if(!invoiceAccountingDeliveryDATE.equals(""))
        {
            //invoiceAccountingDeliveryDate nem kötelező
                Element invoiceAccountingDeliveryDate = doc.createElement("invoiceAccountingDeliveryDate");
                invoiceData.appendChild(invoiceAccountingDeliveryDate);
                invoiceAccountingDeliveryDate.appendChild(doc.createTextNode(invoiceAccountingDeliveryDATE));
        }
            //currencyCode                  kötelező
                Element currencyCode = doc.createElement("currencyCode");
                invoiceData.appendChild(currencyCode);
                currencyCode.appendChild(doc.createTextNode(currencyCODE));
        if(!currencyCODE.equals("HUF") && !exchangeRATE.equals(""))
        {
            //exchangeRate                  nem kötelező
                Element exchangeRate = doc.createElement("exchangeRate");
                invoiceData.appendChild(exchangeRate);
                exchangeRate.appendChild(doc.createTextNode(exchangeRATE));
        }
        if(!selfBillingINDICATOR.equals("false"))
        {
            //selfBillingIndicator          nem kötelező
                Element selfBillingIndicator = doc.createElement("selfBillingIndicator");
                invoiceData.appendChild(selfBillingIndicator);
                selfBillingIndicator.appendChild(doc.createTextNode(selfBillingINDICATOR));
        }
        if(!paymentMETHOD.equals(""))
        {
            //paymentMethod                 nem kötelező
                Element paymentMethod = doc.createElement("paymentMethod");
                invoiceData.appendChild(paymentMethod);
                paymentMethod.appendChild(doc.createTextNode(paymentMETHOD));
        }
        if(!paymentDATE.equals(""))
        {
            //paymentDate                   nem kötelező
                Element paymentDate = doc.createElement("paymentDate");
                invoiceData.appendChild(paymentDate);
                paymentDate.appendChild(doc.createTextNode(paymentDATE));
        }
        if(!cashAccountingINDICATOR.equals("false"))
        {
            //cashAccountingIndicator       nem kötelező
                Element cashAccountingIndicator = doc.createElement("cashAccountingIndicator");
                invoiceData.appendChild(cashAccountingIndicator);
                cashAccountingIndicator.appendChild(doc.createTextNode(cashAccountingINDICATOR));
        }
            //invoiceAppearance             kötelező
                Element invoiceAppearance = doc.createElement("invoiceAppearance");
                invoiceData.appendChild(invoiceAppearance);
                invoiceAppearance.appendChild(doc.createTextNode(invoiceAPPEARANCE));
        if(!electronicInvoiceHASH.equals(""))
        {
            //electronicInvoiceHash         nem kötelező
                Element electronicInvoiceHash = doc.createElement("electronicInvoiceHash");
                invoiceData.appendChild(electronicInvoiceHash);
                electronicInvoiceHash.appendChild(doc.createTextNode(electronicInvoiceHASH));
        }
    if(!additionalInvoiceDATA[0][0].equals("") && !additionalInvoiceDATA[0][1].equals("") && !additionalInvoiceDATA[0][2].equals(""))
    {
        for(int i = 0; i < additionalInvoiceDATA.length; i++)
        {
            //additionalInvoiceData         nem kötelező
                Element additionalInvoiceData = doc.createElement("additionalInvoiceData");
                invoiceData.appendChild(additionalInvoiceData);
                //dataName kötelező
                    Element dataName = doc.createElement("dataName");
                    additionalInvoiceData.appendChild(dataName);
                    dataName.appendChild(doc.createTextNode(additionalInvoiceDATA[i][0]));
                //dataDescription kötelező
                    Element dataDescription = doc.createElement("dataDescription");
                    additionalInvoiceData.appendChild(dataDescription);
                    dataDescription.appendChild(doc.createTextNode(additionalInvoiceDATA[i][1]));
                //dataValue kötelező
                    Element dataValue = doc.createElement("dataValue");
                    additionalInvoiceData.appendChild(dataValue);
                    dataValue.appendChild(doc.createTextNode(additionalInvoiceDATA[i][2]));
        }   
    }
    
    }
    
    public void CreateBasicInvoiceLinesType(Document doc, Element parent, String [][] lines, String [][] lineProductFeeCONTENT, String [][] additionalLineDATA)
    {
        //invoiceLines
            Element invoiceLines = doc.createElement("invoiceLines");
            parent.appendChild(invoiceLines);
        for(int i = 0; i<lines.length; i++)
        {
        //lineModificationReference
            String lineNumberREFERENCE          =lines[i][0];
            String lineOPERATION                =lines[i][1];
        //referencesToOtherLines
            String referencesToOtherLINES       =lines[i][2];
        //advanceIndicator
            String advanceINDICATOR             =lines[i][3]; 
        //productCodes
            String productCODES                 =lines[i][4];
        //lineDescription
            String lineDESCRIPTION              =lines[i][5];
        //quantity
            String QUANTITY                     =lines[i][6];
        //unitOfMeasure
            String unitOfMEASURE                =lines[i][7];
        //unitPrice
            String unitPRICE                    =lines[i][8];
        //lineDiscountdata
            String discountDESCRIPTION          =lines[i][9];
            String discountVALUE                =lines[i][10];
            String discountRATE                 =lines[i][11];
        //lineAmountsNormal
            String lineNetAMOUNT                =lines[i][12];
            String lineVatRATE                  =lines[i][13];
                String vatPERCENTAGE            =lines[i][14];
                String vatEXEMPTION             =lines[i][15];
                String vatOutOfSCOPE            =lines[i][16];
                String VatDomesticReverseCHARGE =lines[i][17];
                String marginSchemeVAT          =lines[i][18];
                String marginSchemeNoVAT        =lines[i][19];
            String lineVatAMOUNT                =lines[i][20];
            String lineVatAMOUNT_HUF            =lines[i][21];
            String lineGrossAmountNORMAL        =lines[i][22];
        //intermediatedService
            String intermediatedSERVICE         =lines[i][23];
        //lineExchangeRate
            String lineExchangeRATE             =lines[i][24];
        //lineDeliveryRate
            String lineDeliveryDATE             =lines[i][25];
        //newTransportMean
            String BRAND                        =lines[i][26];
            String serialNUM                    =lines[i][27];
            String engineNUM                    =lines[i][28];
            String firstEntryIntoSERVICE        =lines[i][29];
            String transportMEAN                =lines[i][30];
            //vehicle
                String engineCAPACITY           =lines[i][31];
                String enginePOWER              =lines[i][32];
                String KMS                      =lines[i][33];
            //vessel
                String LENGTH                   =lines[i][34];
                String activityREFERRED         =lines[i][35];
                String sailedHOURS              =lines[i][36];
            //aircraft
                String takeOffWEIGHT            =lines[i][37];
                String airCARGO                 =lines[i][38];
                String operationHOURS           =lines[i][39];
        //depositIndicator
            String depositINDICATOR             =lines[i][40];
        //marginSchemeIndicator
            String marginSchemeINDICATOR        =lines[i][41];
        //ekaerIDs
            String ekaerIDs                     =lines[i][42];
        //obligatedForProductFee
            String obligatedForProductFEE       =lines[i][43];
        //GPCExcise
            String GPCEXCISE                    =lines[i][44];
        //dieselOilPurchase
            String purchaseCountryCODE          =lines[i][45];
            String purchaseREGION               =lines[i][46];
            String purchasePostalCODE           =lines[i][47];
            String purchaseCITY                 =lines[i][48];
            String additionalAddressDETAIL      =lines[i][49];
            String purchaseDATE                 =lines[i][50];
            String vehicleRegistrationNUMBER    =lines[i][51];
            String dieselOilQUANTITY            =lines[i][52];
        //nettaDeclaration
            String netaDECLARATION              =lines[i][53];
        //productFeeClause
            String takeoverREASON               =lines[i][54];
            String takeoverAMOUNT               =lines[i][55];
        //lineProductFeeContent
            String productCodeCATEGORY          =lineProductFeeCONTENT[i][0];
            String productCodeVALUE             =lineProductFeeCONTENT[i][1];
            String productFeeQUANTITY           =lineProductFeeCONTENT[i][2];
            String productFeeMeasuringUNIT      =lineProductFeeCONTENT[i][3];
            String productFeeRATE               =lineProductFeeCONTENT[i][4];
            String productFeeAMOUNT             =lineProductFeeCONTENT[i][5];
        //additionalLineData
            String dataNAME         = additionalLineDATA[i][0];
            String dataDESCRIPTION  = additionalLineDATA[i][1];
            String dataVALUE        = additionalLineDATA[i][2];
                        
            //line                      kötelező
                Element line = doc.createElement("line");
                invoiceLines.appendChild(line);
                //lineNumber                kötelező
                    Element lineNumber = doc.createElement("lineNumber");
                    line.appendChild(lineNumber);
                    lineNumber.appendChild(doc.createTextNode(String.valueOf(i+1)));
            if(!lineNumberREFERENCE.equals("") && !lineOPERATION.equals(""))
            {
                //lineModificationReference nem kötelező
                    Element lineModificationReference = doc.createElement("lineModificationReference");
                    line.appendChild(lineModificationReference);
                    //lineNumberReference           kötelező
                        Element lineNumberReference = doc.createElement("lineNumberReference");
                        lineModificationReference.appendChild(lineNumberReference);
                        lineNumberReference.appendChild(doc.createTextNode(lineNumberREFERENCE));
                    //lineOperation                 kötelező
                        Element lineOperation = doc.createElement("lineOperation");
                        lineModificationReference.appendChild(lineOperation);
                        lineOperation.appendChild(doc.createTextNode(lineOPERATION));
            }
            if(!referencesToOtherLINES.equals(""))
            {
                //referencesToOtherLines    nem kötelező
                    Element referencesToOtherLines = doc.createElement("referencesToOtherLines");
                    line.appendChild(referencesToOtherLines);
                    String [] split = referencesToOtherLINES.split(";");
                for (String referencesToOtherLINE : split)
                {
                    //referenceToOtherLine          kötelező
                        Element referencesToOtherLine = doc.createElement("referencesToOtherLine");
                        referencesToOtherLines.appendChild(referencesToOtherLine);
                        referencesToOtherLine.appendChild(doc.createTextNode(referencesToOtherLINE));
                }
            }
            if(!advanceINDICATOR.equals(""))
            {
                //advanceIndicator          nem kötelező
                    Element advanceIndicator = doc.createElement("advanceIndicator");
                    line.appendChild(advanceIndicator);
                    advanceIndicator.appendChild(doc.createTextNode(advanceINDICATOR));
            }
            if(!productCODES.equals(""))
            {
                //productCodes              nem kötelező
                    Element productCodes = doc.createElement("productCodes");
                    line.appendChild(productCodes);
                    String [] codes = productCODES.split(".");
                for (String code1 : codes)
                {
                    String[] code = code1.split(";");
                    //productCode                   kötelező
                        Element productCode = doc.createElement("productCode");
                        productCodes.appendChild(productCode);
                        //productCodeCategory           kötelező
                            Element productCodeCategory = doc.createElement("productCodeCategory");
                            productCode.appendChild(productCodeCategory);
                            productCodeCategory.appendChild(doc.createTextNode(code[0]));
                        //productCodeValue              kötelező
                            Element productCodeValue = doc.createElement("productCodeValue");
                            productCode.appendChild(productCodeValue);
                            productCodeValue.appendChild(doc.createTextNode(code[1]));
                }
            }
            if(!lineDESCRIPTION.equals(""))
            {
                //lineDescription           nem kötelező
                    Element lineDescription = doc.createElement("lineDescription");
                    line.appendChild(lineDescription);
                    lineDescription.appendChild(doc.createTextNode(lineDESCRIPTION));
            }
            if(!QUANTITY.equals(""))
            {
                //quantity                  nem kötelező
                    Element quantity = doc.createElement("quantity");
                    line.appendChild(quantity);
                    quantity.appendChild(doc.createTextNode(QUANTITY));
            }
            if(!unitOfMEASURE.equals(""))
            {
                //unitOfMeasure             nem kötelező
                    Element unitOfMeasure = doc.createElement("unitOfMeasure");
                    line.appendChild(unitOfMeasure);
                    unitOfMeasure.appendChild(doc.createTextNode(unitOfMEASURE));
            }
            if(!unitPRICE.equals(""))
            {
                //unitPrice                 nem kötelező
                    Element unitPrice = doc.createElement("unitPrice");
                    line.appendChild(unitPrice);
                    unitPrice.appendChild(doc.createTextNode(unitPRICE));
            }
            if(!discountDESCRIPTION.equals("") || !discountVALUE.equals("") || !discountRATE.equals(""))
            {
                //lineDiscountData          nem kötelező
                    Element lineDiscountData = doc.createElement("lineDiscountData");
                    line.appendChild(lineDiscountData);
                if(!discountDESCRIPTION.equals(""))
                {
                    //discountDescription       nem kötelező
                        Element discountDescription = doc.createElement("discountDescription");
                        lineDiscountData.appendChild(discountDescription);
                        discountDescription.appendChild(doc.createTextNode(discountDESCRIPTION));
                }
                if(!discountVALUE.equals(""))
                {
                    //discountValue             nem kötelező
                        Element discountValue = doc.createElement("discountValue");
                        lineDiscountData.appendChild(discountValue);
                        discountValue.appendChild(doc.createTextNode(discountVALUE));
                }
                if(!discountRATE.equals(""))
                {
                    //discountRate              nem kötelező
                        Element discountRate = doc.createElement("discountRate");
                        lineDiscountData.appendChild(discountRate);
                        discountRate.appendChild(doc.createTextNode(discountRATE));
                }
            }
            System.out.println("lineNetAMOUNT :" + lineNetAMOUNT + " " + lineVatRATE);
            if(!lineNetAMOUNT.equals("") && !lineVatRATE.equals(""))
            {
                //lineAmountsNormal         nem kötelező
                    Element lineAmountsNormal = doc.createElement("lineAmountsNormal");
                    line.appendChild(lineAmountsNormal);
                    //lineNetAmount                 kötelező
                        Element lineNetAmount = doc.createElement("lineNetAmount");
                        lineAmountsNormal.appendChild(lineNetAmount);
                        lineNetAmount.appendChild(doc.createTextNode(lineNetAMOUNT));
                    //lineVatRate                   kötelező
                        Element lineVatRate = doc.createElement("lineVatRate");
                        lineAmountsNormal.appendChild(lineVatRate);                        
                        switch(lineVatRATE)
                        {
                            case "vatPercentage":
                                Element vatPercentage = doc.createElement("vatPercentage");
                                lineVatRate.appendChild(vatPercentage);
                                vatPercentage.appendChild(doc.createTextNode(vatPERCENTAGE));
                                break;
                            case "vatExemption":
                                Element vatExemption = doc.createElement("vatExemption");
                                lineVatRate.appendChild(vatExemption);
                                vatExemption.appendChild(doc.createTextNode(vatEXEMPTION));
                                break;
                            case "vatOutOfScope":
                                Element vatOutOfScope = doc.createElement("vatOutOfScope");
                                lineVatRate.appendChild(vatOutOfScope);
                                vatOutOfScope.appendChild(doc.createTextNode(vatOutOfSCOPE));
                                break;
                            case "VatDomesticReverseCharge":
                                Element VatDomesticReverseCharge = doc.createElement("VatDomesticReverseCharge");
                                lineVatRate.appendChild(VatDomesticReverseCharge);
                                VatDomesticReverseCharge.appendChild(doc.createTextNode(VatDomesticReverseCHARGE));
                                break;
                            case "marginSchemeVat":
                                Element marginSchemeVat = doc.createElement("marginSchemeVat");
                                lineVatRate.appendChild(marginSchemeVat);
                                marginSchemeVat.appendChild(doc.createTextNode(marginSchemeVAT));
                                break;
                            case "marginSchemeNoVat":
                                Element marginSchemeNoVat = doc.createElement("marginSchemeNoVat");
                                lineVatRate.appendChild(marginSchemeNoVat);
                                marginSchemeNoVat.appendChild(doc.createTextNode(marginSchemeNoVAT));
                                break;
                        }
                        //vatPercentage/vatExemption/vatOutOfScope/VatDomesticReverseCharge/marginSchemeVat/marginSchemeNoVat                           
                if(!lineVatAMOUNT.equals(""))
                {
                    //lineVatAmount                 nem kötelező
                        Element lineVatAmount = doc.createElement("lineVatAmount");
                        lineAmountsNormal.appendChild(lineVatAmount);
                        lineVatAmount.appendChild(doc.createTextNode(lineVatAMOUNT));
                }
                if(!lineVatAMOUNT_HUF.equals(""))
                {
                    //lineVatAmountHUF              nem kötelező
                        Element lineVatAmountHUF = doc.createElement("lineVatAmountHUF");
                        lineAmountsNormal.appendChild(lineVatAmountHUF);
                        lineVatAmountHUF.appendChild(doc.createTextNode(lineVatAMOUNT_HUF));
                }
                if(!lineGrossAmountNORMAL.equals(""))
                {
                    //lineGrossAmountNormal         nem kötelező
                        Element lineGrossAmountNormal = doc.createElement("lineGrossAmountNormal");
                        lineAmountsNormal.appendChild(lineGrossAmountNormal);
                        lineGrossAmountNormal.appendChild(doc.createTextNode(lineGrossAmountNORMAL));
                }
            }
            if(!intermediatedSERVICE.equals("false"))
            {
                //intermediatedService      nem kötelező
                    Element intermediatedService = doc.createElement("intermediatedService");
                    line.appendChild(intermediatedService);
                    intermediatedService.appendChild(doc.createTextNode(intermediatedSERVICE));
            }
            if(!lineDeliveryDATE.equals(""))
            {
                //aggregateInvoiceLineData  nem kötelező
                    Element aggregateInvoiceLineData = doc.createElement("aggregateInvoiceLineData");
                    line.appendChild(aggregateInvoiceLineData);
                    //lineExchangeRate              nem kötelező
                        Element lineExchangeRate = doc.createElement("lineExchangeRate");
                        aggregateInvoiceLineData.appendChild(lineExchangeRate);
                        lineExchangeRate.appendChild(doc.createTextNode(lineExchangeRATE));
                    //lineDeliverRate               kötelező
                        Element lineDeliverRate = doc.createElement("lineDeliverRate");
                        aggregateInvoiceLineData.appendChild(lineDeliverRate);
                        lineDeliverRate.appendChild(doc.createTextNode(lineDeliveryDATE));
            }
            if(!firstEntryIntoSERVICE.equals("") && !transportMEAN.equals(""))
            {
                //newTransportMean          nem kötelező
                    Element newTransportMean = doc.createElement("newTransportMean");
                    line.appendChild(newTransportMean);
                if(!BRAND.equals(""))
                {
                    //brand                     nem kötelező
                        Element brand = doc.createElement("brand");
                        newTransportMean.appendChild(brand);
                        brand.appendChild(doc.createTextNode(BRAND));
                }
                if(!serialNUM.equals(""))
                {
                    //serialNum                 nem kötelező
                        Element serialNum = doc.createElement("serialNum");
                        newTransportMean.appendChild(serialNum);
                        serialNum.appendChild(doc.createTextNode(serialNUM));
                }
                if(!engineNUM.equals(""))
                {
                    //engineNum                 nem kötelező
                        Element engineNum = doc.createElement("engineNum");
                        newTransportMean.appendChild(engineNum);
                        engineNum.appendChild(doc.createTextNode(engineNUM));
                }
                    //firstEntryIntoService     kötelező
                        Element firstEntryIntoService = doc.createElement("firstEntryIntoService");
                        newTransportMean.appendChild(firstEntryIntoService);
                        firstEntryIntoService.appendChild(doc.createTextNode(firstEntryIntoSERVICE));
                    //vehicle/vessel/aircraft   kötelező
                        switch(transportMEAN)
                        {
                            case "vehicle":
                        //vehicle
                            Element vehicle = doc.createElement("vehicle");
                            newTransportMean.appendChild(vehicle);
                            //engineCapacity    kötelező
                                Element engineCapacity = doc.createElement("engineCapacity");
                                vehicle.appendChild(engineCapacity);
                                engineCapacity.appendChild(doc.createTextNode(engineCAPACITY));
                            //enginePower       kötelező
                                Element enginePower = doc.createElement("enginePower");
                                vehicle.appendChild(enginePower);
                                enginePower.appendChild(doc.createTextNode(enginePOWER));
                            //kms               kötelező
                                Element kms = doc.createElement("kms");
                                vehicle.appendChild(kms);
                                kms.appendChild(doc.createTextNode(KMS));
                                break;
                                
                            case "vessel":
                        //vessel
                            Element vessel = doc.createElement("vessel");
                            newTransportMean.appendChild(vessel);
                                //length
                                    Element length = doc.createElement("length");
                                    vessel.appendChild(length);
                                    length.appendChild(doc.createTextNode(LENGTH));
                                //activityReferred
                                    Element activityReferred = doc.createElement("activityReferred");
                                    vessel.appendChild(activityReferred);
                                    activityReferred.appendChild(doc.createTextNode(activityREFERRED));
                                //sailedHours 
                                    Element sailedHours = doc.createElement("sailedHours");
                                    vessel.appendChild(sailedHours);
                                    sailedHours.appendChild(doc.createTextNode(sailedHOURS));
                                break;
                                
                            case "aircraft":
                        //aircraft
                            Element aircraft = doc.createElement("aircraft");
                            newTransportMean.appendChild(aircraft);
                                //takeOffWeight
                                    Element takeOffWeight = doc.createElement("takeOffWeight");
                                    aircraft.appendChild(takeOffWeight);
                                    takeOffWeight.appendChild(doc.createTextNode(takeOffWEIGHT));
                                //airCargo
                                    Element airCargo = doc.createElement("airCargo");
                                    aircraft.appendChild(airCargo);
                                    airCargo.appendChild(doc.createTextNode(airCARGO));
                                //operationHours
                                    Element operationHours = doc.createElement("operationHours");
                                    aircraft.appendChild(operationHours);
                                    operationHours.appendChild(doc.createTextNode(operationHOURS));
                                break;
                        }
            }
            if(!depositINDICATOR.equals("false"))
            {
                //depositIndicator          nem kötelező
                    Element depositIndicator = doc.createElement("depositIndicator");
                    line.appendChild(depositIndicator);
                    depositIndicator.appendChild(doc.createTextNode(depositINDICATOR));
            }
            if(!marginSchemeINDICATOR.equals(""))
            {
                //marginSchemeIndicator     nem kötelező
                    Element marginSchemeIndicator = doc.createElement("marginSchemeIndicator");
                    line.appendChild(marginSchemeIndicator);
                    marginSchemeIndicator.appendChild(doc.createTextNode(marginSchemeINDICATOR));
            }
            if(!ekaerIDs.equals(""))
            {
                //ekaerIds                  nem kötelező
                    Element ekaerIds= doc.createElement("ekaerIds");
                    line.appendChild(ekaerIds);
                    String [] split = ekaerIDs.split(";");
                for(int j = 0; j < split.length; j++)
                {
                    //ekaerId                       kötelező
                        Element ekaerId = doc.createElement("ekaerId");
                        line.appendChild(ekaerId);
                        ekaerId.appendChild(doc.createTextNode(split[j]));
                }
            }
            if(!obligatedForProductFEE.equals("false"))
            {
                //obligatedForProductFee    nem kötelező
                    Element obligatedForProductFee = doc.createElement("obligatedForProductFee");
                    line.appendChild(obligatedForProductFee);
                    obligatedForProductFee.appendChild(doc.createTextNode(obligatedForProductFEE));
            }
            if(!GPCEXCISE.equals(""))
            {
                //GPCExcise                 nem kötelező
                    Element GPCExcise = doc.createElement("GPCExcise");
                    line.appendChild(GPCExcise);
                    GPCExcise.appendChild(doc.createTextNode(GPCEXCISE));
            }
            if(!purchaseCountryCODE.equals("") && !purchasePostalCODE.equals("") && !purchaseCITY.equals("") && !additionalAddressDETAIL.equals("") && !purchaseDATE.equals("") && !vehicleRegistrationNUMBER.equals(""))
            {
                //dieselOilPurchase         nem kötelező
                    Element dieselOilPurchase = doc.createElement("dieselOilPurchase");
                    line.appendChild(dieselOilPurchase);
                    //purchaseLocation          kötelező
                        Element purchaseLocation = doc.createElement("purchaseLocation");
                        dieselOilPurchase.appendChild(purchaseLocation);
                        //countryCode               kötelező
                            Element countryCode = doc.createElement("countryCode");
                            purchaseLocation.appendChild(countryCode);
                            countryCode.appendChild(doc.createTextNode(purchaseCountryCODE));
                    if(!purchaseREGION.equals(""))
                    {
                        //region                    nem kötelező
                            Element region = doc.createElement("region");
                            purchaseLocation.appendChild(region);
                            region.appendChild(doc.createTextNode(purchaseREGION));
                    }
                        //postalCode                kötelező
                            Element postalCode = doc.createElement("postalCode");
                            purchaseLocation.appendChild(postalCode);
                            postalCode.appendChild(doc.createTextNode(purchasePostalCODE));
                        //city                      kötelező
                            Element city = doc.createElement("city");
                            purchaseLocation.appendChild(city);
                            city.appendChild(doc.createTextNode(purchaseCITY));
                        //additionalAddressDetail   kötelező
                            Element additionalAddressDetail = doc.createElement("additionalAddressDetail");
                            purchaseLocation.appendChild(additionalAddressDetail);
                            additionalAddressDetail.appendChild(doc.createTextNode(additionalAddressDETAIL));
                    //purchaseDate              kötelező
                        Element purchaseDate = doc.createElement("purchaseDate");
                        dieselOilPurchase.appendChild(purchaseDate);
                        purchaseDate.appendChild(doc.createTextNode(purchaseDATE));
                    //vehicleRegistrationNumber kötelező
                        Element vehicleRegistrationNumber = doc.createElement("vehicleRegistrationNumber");
                        dieselOilPurchase.appendChild(vehicleRegistrationNumber);
                        vehicleRegistrationNumber.appendChild(doc.createTextNode(vehicleRegistrationNUMBER));
                if(!dieselOilQUANTITY.equals(""))
                {
                    //dieselOilQuantity         nem kötelező
                        Element dieselOilQuantity = doc.createElement("dieselOilQuantity");
                        dieselOilPurchase.appendChild(dieselOilQuantity);
                        dieselOilQuantity.appendChild(doc.createTextNode(dieselOilQUANTITY));
                }
            }
            if(!netaDECLARATION.equals("false"))
            {
                //netaDeclaration           nem kötelező
                    Element netaDeclaration = doc.createElement("netaDeclaration");
                    line.appendChild(netaDeclaration);
                    netaDeclaration.appendChild(doc.createTextNode(netaDECLARATION));
            }
            if(!takeoverREASON.equals(""))
            {
                //productFeeClause          nem kötelező
                    Element productFeeClause = doc.createElement("productFeeClause");
                    line.appendChild(productFeeClause);
                    /*customerDeclaration       ezt nem használjuk*/
                    //productFeeTakeoverData    ezt használjuk helyette
                        Element productFeeTakeoverData = doc.createElement("productFeeTakeoverData");
                        productFeeClause.appendChild(productFeeTakeoverData);
                        //takeoverReason        kötelező
                            Element takeoverReason = doc.createElement("takeoverReason");
                            productFeeTakeoverData.appendChild(takeoverReason);
                            takeoverReason.appendChild(doc.createTextNode(takeoverREASON));
                    if(!takeoverAMOUNT.equals(""))
                    {
                        //takeoverAmount        nem kötelező
                            Element takeoverAmount = doc.createElement("takeoverAmount");
                            productFeeTakeoverData.appendChild(takeoverAmount);
                            takeoverAmount.appendChild(doc.createTextNode(takeoverAMOUNT));
                    }
            }
            
            if(!productCodeCATEGORY.equals("") && !productCodeVALUE.equals("") && !productFeeQUANTITY.equals("") && !productFeeMeasuringUNIT.equals("") && !productFeeRATE.equals("") && !productFeeAMOUNT.equals(""))
            {
                //lineProductFeeContent     nem kötelező
                    Element lineProductFeeContent = doc.createElement("lineProductFeeContent");
                    line.appendChild(lineProductFeeContent);
                    //productFeeCode                kötelező
                        Element productFeeCode = doc.createElement("productFeeCode");
                        lineProductFeeContent.appendChild(productFeeCode);
                        //productCodeCategory           kötelező
                            Element productCodeCategory = doc.createElement("productCodeCategory");
                            productFeeCode.appendChild(productCodeCategory);
                            productCodeCategory.appendChild(doc.createTextNode(productCodeCATEGORY));
                        //productCodeValue              kötelező
                            Element productCodeValue = doc.createElement("productCodeValue");
                            productFeeCode.appendChild(productCodeValue);
                            productCodeValue.appendChild(doc.createTextNode(productCodeVALUE));
                    //productFeeQuantity            kötelező
                        Element productFeeQuantity = doc.createElement("productFeeQuantity");
                        lineProductFeeContent.appendChild(productFeeQuantity);
                        productFeeQuantity.appendChild(doc.createTextNode(productFeeQUANTITY));
                    //productFeeMeasuringUnit       kötelező
                        Element productFeeMeasuringUnit = doc.createElement("productFeeMeasuringUnit");
                        lineProductFeeContent.appendChild(productFeeMeasuringUnit);
                        productFeeMeasuringUnit.appendChild(doc.createTextNode(productFeeMeasuringUNIT));
                    //productFeeRate                kötelező
                        Element productFeeRate = doc.createElement("productFeeRate");
                        lineProductFeeContent.appendChild(productFeeRate);
                        productFeeRate.appendChild(doc.createTextNode(productFeeRATE));
                    //productFeeAmount              kötelező
                        Element productFeeAmount = doc.createElement("productFeeAmount");
                        lineProductFeeContent.appendChild(productFeeAmount);
                        productFeeAmount.appendChild(doc.createTextNode(productFeeAMOUNT));
            }
            if(!dataNAME.equals("") && !dataDESCRIPTION.equals("") && !dataVALUE.equals(""))
            {
                //additionalLineData        nem kötelező
                    Element additionalLineData = doc.createElement("additionalLineData");
                    line.appendChild(additionalLineData);
                    //dataName                      kötelező
                        Element dataName = doc.createElement("dataName");
                        additionalLineData.appendChild(dataName);
                        dataName.appendChild(doc.createTextNode(dataNAME));
                    //dataDescription               kötelező
                        Element dataDescription = doc.createElement("dataDescription");
                        additionalLineData.appendChild(dataDescription);
                        dataDescription.appendChild(doc.createTextNode(dataDESCRIPTION));
                    //dataValue                     kötelező
                        Element dataValue = doc.createElement("dataValue");
                        additionalLineData.appendChild(dataValue);
                        dataValue.appendChild(doc.createTextNode(dataVALUE));
            }
        }   
    }
    
    /*public void CreateBasicProductFeeSummaryType(Document doc, Element parent, String [][] lineProductFeeCONTENT)
    {
        String productFeeOPERATION = ;//REFUND or DEPOSIT
        
    //productFeeSummary
        Element productFeeSummary = doc.createElement("productFeeSummary");
        parent.appendChild(productFeeSummary);
        //productFeeOperation           kötelező
            Element productFeeOperation = doc.createElement("productFeeOperation");
            productFeeSummary.appendChild(productFeeOperation);
            productFeeOperation.appendChild(doc.createTextNode(productFeeOPERATION));
        for(int i = 0; i < valami ; i++)
        {
            String productCodeCATEGORY = ;
            String productCodeVALUE = ;
            String productFeeQUANTITY = ;
            String productFeeMeasuringUNIT = "KG";
            String productFeeRATE = ;
            String productFeeAMOUNT = QUANTITY * FeeRATE;
            String productChargeSUM = ;
            
        //productFeeData                kötelező
            Element productFeeData = doc.createElement("productFeeData");
            productFeeSummary.appendChild(productFeeData);
            //productFeeCode                kötelező
                Element productFeeCode = doc.createElement("productFeeCode");
                productFeeData.appendChild(productFeeCode);
                //productCodeCategory           kötelező
                    Element productCodeCategory = doc.createElement("productCodeCategory");
                    productFeeCode.appendChild(productCodeCategory);
                    productCodeCategory.appendChild(doc.createTextNode(productCodeCATEGORY));
                //productCodeValue              kötelező
                    Element productCodeValue = doc.createElement("productCodeValue");
                    productFeeCode.appendChild(productCodeValue);
                    productCodeValue.appendChild(doc.createTextNode(productCodeVALUE));
            //productFeeQuantity            kötelező
                Element productFeeQuantity = doc.createElement("productFeeQuantity");
                productFeeData.appendChild(productFeeQuantity);
                productFeeQuantity.appendChild(doc.createTextNode(productFeeQUANTITY));
            //productFeeMeasuringUnit       kötelező
                Element productFeeMeasuringUnit = doc.createElement("productFeeMeasuringUnit");
                productFeeData.appendChild(productFeeMeasuringUnit);
                productFeeMeasuringUnit.appendChild(doc.createTextNode(productFeeMeasuringUNIT));
            //productFeeRate                kötelező
                Element productFeeRate = doc.createElement("productFeeRate");
                productFeeData.appendChild(productFeeRate);
                productFeeRate.appendChild(doc.createTextNode(productFeeRATE));
            //productFeeAmount              kötelező
                Element productFeeAmount = doc.createElement("productFeeAmount");
                productFeeData.appendChild(productFeeAmount);
                productFeeAmount.appendChild(doc.createTextNode(productFeeAMOUNT));
        }
        //productChargeSum              kötelező
            Element productChargeSum = doc.createElement("productChargeSum");
            productFeeSummary.appendChild(productChargeSum);
            productChargeSum.appendChild(doc.createTextNode(productChargeSUM));
        //paymentEvidenceDocumentData   nem kötelező
            //evidenceDocumentNo kötelező
            //evidenceDocumentDate
            //obligatedName
            //obligtedAddress
                //detailedAddress           kötelező
                    //countryCode               kötelező
                    //region                    nem kötelező
                    //postalCode                kötelező
                    //city                      kötelező
                    //streetName                kötelező
                    //publicPlaceCategory       kötelező
                    //number                    nem kötelező
                    //building                  nem kötelező
                    //staircase                 nem kötelező
                    //floor                     nem kötelező
                    //door                      nem kötelező
                    //lotNumber                 nem kötelező
            //obligatedTaxNumber
                //taxPayerId                kötelező
                //vatCode                   nem kötelező
                //countyCode                nem kötelező
    }*/
    
    public void CreateBasicInvoiceSummaryType(Document doc, Element parent, String lines [][])
    {
        float [][] summary = new float[8][1];
        float invoiceNetAmount = 0.0f;
        float invoiceVatAmount = 0.0f;
    //invoiceSummary
        Element invoiceSummary = doc.createElement("invoiceSummary");
        parent.appendChild(invoiceSummary);
        //summaryNormal         kötelező
            Element summaryNormal = doc.createElement("summaryNormal");
            invoiceSummary.appendChild(summaryNormal);
        for(int i = 0; i < lines.length; i++)
        {
            switch (lines[i][13])
            {
                case "vatPercentage":
                    switch (lines[i][14])
                    {
                        case "0.05":
                            summary[0][0] += Float.valueOf(lines[i][12]);
                            break;
                        case "0.1":
                            summary[1][0] += Float.valueOf(lines[i][12]);
                            break;
                        case "0.27":
                            summary[2][0] += Float.valueOf(lines[i][12]);
                            break;
                    }
                    break;
                case "vatExemption":
                    summary[3][0] += Float.valueOf(lines[i][12]);
                    break;
                case "vatOutOfScope":
                    summary[4][0] += Float.valueOf(lines[i][12]);
                    break;
                case "VatDomesticReverseCharge":
                    summary[5][0] += Float.valueOf(lines[i][12]);
                    break;
                case "marginSchemeVat":
                    summary[6][0] += Float.valueOf(lines[i][12]);
                    break;
                case "marginSchemeNoVat":
                    summary[7][0] += Float.valueOf(lines[i][12]);
                    break;
            }
        }

        for(int i = 0; i <= 7; i++)
        {
            if(summary[i][0] != 0.0)
            {   
                    //summaryByVatRate          nem kötelező
                        Element summaryByVatRate = doc.createElement("summaryByVatRate");
                        summaryNormal.appendChild(summaryByVatRate);
                        float vatRATE = 0.0f;
                        Element vatRate = null;
                        if(i == 0)
                        {
                            vatRATE = 0.05f;//vatRate   kötelező
                            vatRate = doc.createElement("vatRate");
                            summaryByVatRate.appendChild(vatRate);                                 
                        }
                        if(i == 1)
                        {
                            vatRATE = 0.10f;//vatRate   kötelező
                            vatRate = doc.createElement("vatRate");
                            summaryByVatRate.appendChild(vatRate);
                        }
                        if(i == 2)
                        {
                            vatRATE = 0.27f;//vatRate   kötelező
                            vatRate = doc.createElement("vatRate");
                            summaryByVatRate.appendChild(vatRate);
                        }
                        if(i >= 0 && i<=2)
                        {
                            Element vatPercentage = doc.createElement("vatPercentage");
                            vatRate.appendChild(vatPercentage);
                            vatPercentage.appendChild(doc.createTextNode(String.format (java.util.Locale.US,"%.2f", vatRATE)));
                        }
                        if(i == 3)
                        {
                            vatRATE = 0.0f;//vatRate   kötelező
                            vatRate = doc.createElement("vatRate");
                            summaryByVatRate.appendChild(vatRate);
                            
                                Element vatExemption = doc.createElement("vatExemption");
                                vatRate.appendChild(vatExemption);
                                vatExemption.appendChild(doc.createTextNode(String.format (java.util.Locale.US,"%.2f", vatRATE)));
                        }
                        if(i == 4)
                        {
                            vatRATE = 0.0f;//vatRate   kötelező
                            vatRate = doc.createElement("vatRate");
                            summaryByVatRate.appendChild(vatRate);
                            
                                Element vatOutOfScope = doc.createElement("vatOutOfScope");
                                vatRate.appendChild(vatOutOfScope);
                                vatOutOfScope.appendChild(doc.createTextNode("true"));
                        }
                        if(i == 5)
                        {
                            vatRATE = 0.0f;//vatRate   kötelező
                            vatRate = doc.createElement("vatRate");
                            summaryByVatRate.appendChild(vatRate);
                            
                                Element vatDomesticReverseCharge = doc.createElement("vatOutOfScope");
                                vatRate.appendChild(vatDomesticReverseCharge);
                                vatDomesticReverseCharge.appendChild(doc.createTextNode("true"));
                        }
                        if(i == 6)
                        {
                            vatRATE = 0.0f;//vatRate   kötelező
                            vatRate = doc.createElement("vatRate");
                            summaryByVatRate.appendChild(vatRate);
                            
                                Element marginSchemeVat = doc.createElement("vatOutOfScope");
                                vatRate.appendChild(marginSchemeVat);
                                marginSchemeVat.appendChild(doc.createTextNode("true"));
                        }
                        if(i == 7)
                        {
                            vatRATE = 0.0f;//vatRate   kötelező
                            vatRate = doc.createElement("vatRate");
                            summaryByVatRate.appendChild(vatRate);
                            
                                Element marginSchemeNoVat = doc.createElement("vatOutOfScope");
                                vatRate.appendChild(marginSchemeNoVat);
                                marginSchemeNoVat.appendChild(doc.createTextNode("true"));
                        }
                        
                        //vatRateNetAmount              kötelező
                            float vatRateNetAMOUNT = summary[i][0];
                            Element vatRateNetAmount = doc.createElement("vatRateNetAmount");
                            summaryByVatRate.appendChild(vatRateNetAmount);
                            vatRateNetAmount.appendChild(doc.createTextNode(String.format (java.util.Locale.US,"%.2f", vatRateNetAMOUNT)));
                        //vatRateVatAmount              kötelező
                            float vatRateVatAMOUNT = vatRATE*vatRateNetAMOUNT;
                            System.err.println("vatRATE" + vatRATE);
                            System.err.println("vatRateVatAMOUNT" + vatRateVatAMOUNT);
                            Element vatRateVatAmount = doc.createElement("vatRateVatAmount");
                            summaryByVatRate.appendChild(vatRateVatAmount);
                            vatRateVatAmount.appendChild(doc.createTextNode(String.format (java.util.Locale.US,"%.2f", vatRateVatAMOUNT)));
                        //vatRateVatAmountHUF           nem kötelező
                        //vatRateGrossAmount            nem kötelező 
                    invoiceNetAmount += vatRateNetAMOUNT;
                    invoiceVatAmount += vatRateVatAMOUNT;
                    System.err.println("invoiceVatAmount: " + invoiceVatAmount);
            }           
        }
        float middleExchangeRate = Float.valueOf(lines[0][21])/Float.valueOf(lines[0][20]);
            //invoiceNetAmount          kötelező
                Element invoiceNetAMOUNT = doc.createElement("invoiceNetAmount");
                summaryNormal.appendChild(invoiceNetAMOUNT);                
                invoiceNetAMOUNT.appendChild(doc.createTextNode(String.format (java.util.Locale.US,"%.2f", invoiceNetAmount)));
            //invoiceVatAmount          kötelező
                Element invoiceVatAMOUNT = doc.createElement("invoiceVatAmount");
                summaryNormal.appendChild(invoiceVatAMOUNT);
                invoiceVatAMOUNT.appendChild(doc.createTextNode(String.format (java.util.Locale.US,"%.2f", invoiceVatAmount)));
            //invoiceVatAmountHUF       kötelező
                Element invoiceVatAmountHUF = doc.createElement("invoiceVatAmountHUF");
                summaryNormal.appendChild(invoiceVatAmountHUF);
                float value = 0.0f;
                if(invoiceVatAmount == 0.0f)
                {
                    value = 0.0f;
                }
                else
                {
                    value = invoiceVatAmount * middleExchangeRate;
                }
                System.out.println("invoiceVatAmountHUF: " + String.format (java.util.Locale.US,"%.2f", value));
                invoiceVatAmountHUF.appendChild(doc.createTextNode(String.format (java.util.Locale.US,"%.2f", value)));
        //invoiceGrossAmount    nem kötelező
    }
    
    public String haveModRef(String InvoiceNumber, String supplierName, String supplierTaxNumber) throws Exception
    {
        Query query = new Query.QueryBuilder()
                .select("helyesbitett")
                .from("szamlazo_szamla")
                .where("szamla_sorszam LIKE '" + InvoiceNumber + "'")
                .build();
        Object [][] select = App.db.select(query.getQuery());

        System.err.println("InvoiceNumber" + InvoiceNumber);
        String originalnvoiceNumber = "";
        /*if(!select[0][0].toString().equals(""))
        {
            originalnvoiceNumber = select[0][0].toString();
            System.err.println("select[0][0]" + select[0][0].toString());
        }
        else
        {
            originalnvoiceNumber = "";
            System.err.println("1598");
        }*/
        try
        {
            originalnvoiceNumber = select[0][0].toString();
        }
        catch(ArrayIndexOutOfBoundsException e)
        {
            originalnvoiceNumber = "";
        }
        
        /*GenerateXml gen = new GenerateXml(); 
        NAVConn navcon = new NAVConn();
        String query = "queryInvoiceData";   
        
        String [] billingSoftwareDatas = new String[10];
        String [] SupplierDatas = new String[5];
        
        Query queryRes = new Query.QueryBuilder()
                        .select("requestVersion, headerVersion, softwareMainVersion, softwareId, softwareName, softwareOperation, softwareDevName, softwareDevContact, softwareDevCountryCode, softwareTaxNumber")
                        .from("szamlazo_szoftver_adatok")
                        .build();
                Object [][] softwareDataResponse = App.db.select(queryRes.getQuery());
                
                billingSoftwareDatas[0] = softwareDataResponse[0][0].toString();
                billingSoftwareDatas[1] = softwareDataResponse[0][1].toString();
                billingSoftwareDatas[2] = softwareDataResponse[0][2].toString();
                billingSoftwareDatas[3] = softwareDataResponse[0][3].toString();
                billingSoftwareDatas[4] = softwareDataResponse[0][4].toString();
                billingSoftwareDatas[5] = softwareDataResponse[0][5].toString();
                billingSoftwareDatas[6] = softwareDataResponse[0][6].toString();
                billingSoftwareDatas[7] = softwareDataResponse[0][7].toString();
                billingSoftwareDatas[8] = softwareDataResponse[0][8].toString();
                billingSoftwareDatas[9] = softwareDataResponse[0][9].toString();

                queryRes = new Query.QueryBuilder()
                        .select("felhasznalonev, password, alairo_kulcs, csere_kulcs")
                        .from("szamlazo_ceg_adatok")
                        .where("nev LIKE '" + supplierName + "'")
                        .build();
                System.out.println(queryRes);
                Object [][] supplierDataResponse = App.db.select(queryRes.getQuery());
                
                SupplierDatas[0] = supplierTaxNumber;
                SupplierDatas[1] = supplierDataResponse[0][0].toString();
                SupplierDatas[2] = supplierDataResponse[0][1].toString();
                SupplierDatas[3] = supplierDataResponse[0][2].toString();
                SupplierDatas[4] = supplierDataResponse[0][3].toString();
        
        int page = 1;
        String inputfile = gen.GenerateQueryInvoiceDataXml(billingSoftwareDatas, SupplierDatas, page ,originalInvoiceNumber, "true");               
        String response = navcon.GetQueryInvoiceData(query, inputfile);*/              
        
        return originalnvoiceNumber;
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
    
    public String generateSzamlaXml(Object [] InvoiceAllData, String operation) throws Exception
    {
        System.err.println("generateSzamlaXml eleje");
        Document doc = null;
        
        String [] referenceDatas = (String[]) InvoiceAllData[0];
        String [] supplierInfo = (String[]) InvoiceAllData[1];
        String [] customerInfo = (String[]) InvoiceAllData[2];
        String [] fiscalRepresentativeInfo = (String[]) InvoiceAllData[3];
        String [] invoiceData = (String[]) InvoiceAllData[4];
        String [][] additionalInvoiceData = (String[][]) InvoiceAllData[5];
        String [][] lines = (String[][]) InvoiceAllData[6];
        String [][] lineProductFeeCONTENT = (String[][]) InvoiceAllData[7];
        String [][] additionalLineDATA = (String[][]) InvoiceAllData[8];
        
        try                                  
        {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            doc = docBuilder.newDocument();
        }
        catch (ParserConfigurationException ex)
        {
            System.out.println(ex);
        }
        
        CreateBasicInvoiceType(doc);
                
        String response = haveModRef(invoiceData[0], supplierInfo[7], supplierInfo[0]);
        
        if(!referenceDatas[0].toString().equals(""))
        {       
            CreateBasicInvoiceReferenceType(doc, (Element)doc.getFirstChild().getFirstChild(), referenceDatas);        
        }
        
        CreateBasicInvoiceHeadType(doc, (Element)doc.getFirstChild().getFirstChild(), supplierInfo, customerInfo, fiscalRepresentativeInfo, invoiceData, additionalInvoiceData);
        CreateBasicInvoiceLinesType(doc, (Element)doc.getFirstChild().getFirstChild(), lines, lineProductFeeCONTENT, additionalLineDATA);
        /*CreateBasicProductFeeSummaryType(doc);*/
        CreateBasicInvoiceSummaryType(doc, (Element)doc.getFirstChild().getFirstChild(), lines);
        
        String xmlString = CreateXmlFile(doc);
        System.err.println("generateSzamlaXml vége");
        System.err.println(xmlString);
        return xmlString;
    }
}
