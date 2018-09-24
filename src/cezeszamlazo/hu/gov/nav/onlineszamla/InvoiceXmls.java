/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeszamlazo.hu.gov.nav.onlineszamla;

import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
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
 * 
**/
public class InvoiceXmls
{
    public void InvoiceXmls()
    {
    
    }
    
    public void CreateBasicInvoiceType(Document doc)
    {
        Element Invoice = doc.createElement("Invoice");
        doc.appendChild(Invoice);
            
            Attr attr1 = doc.createAttribute("xmlns:xs");
            attr1.setValue("http://www.w3.org/2001/XMLSchema-instance");
            Invoice.setAttributeNode(attr1);
            
            Attr attr2 = doc.createAttribute("xmlns");
            attr2.setValue("http://schemas.nav.gov.hu/OSA/1.0/data");
            Invoice.setAttributeNode(attr2);
            
            Attr attr3 = doc.createAttribute("xs:schemaLocation");
            attr3.setValue("http://schemas.nav.gov.hu/OSA/1.0/data invoiceData.xsd");
            Invoice.setAttributeNode(attr3);
            
            Element invoiceExchange = doc.createElement("invoiceExchange");
            Invoice.appendChild(invoiceExchange);
    }

    public void CreateBasicInvoiceHeadType(Document doc, Element parent, String [] supplierDatas, Object [] billingData)
    {
        String customerNAME = billingData[9].toString();
        String countryCODE = "HU";
        /*
        need to get from db
        */
        String postalCODE = billingData[10].toString();
        String CITY = billingData[11].toString().trim();
        String [] detailedADDRESS = billingData[12].toString().split(" ");
            String streetNAME = detailedADDRESS[0];
            String publicPlaceCATEGORY = detailedADDRESS[1];
            String NUMBER = detailedADDRESS[2];
        String [] cutomerTaxNumber = billingData[17].toString().split("-");
            String taxpayerID =cutomerTaxNumber[0];
            String vatCODE = cutomerTaxNumber[1];
            String countyCODE = cutomerTaxNumber[2];           
        String invoiceNUMBER = billingData[21].toString();
        String invoiceCATEGORY = "NORMAL";
        /*
        Can be : AGGREGATE, SIMPLIFIED
        */        
        String paymentMETHOD = "";
        /*
        Other payment methods(CARD, VOUCHER) 
        */
        switch(Integer.valueOf(billingData[0].toString()))
        {
            case 1: paymentMETHOD = "TRANSFER";
                break;
            case 0: paymentMETHOD = "CASH";
                break;
            case 2: paymentMETHOD = "OTHER";
                break;
            
        }      
        String invoiceAPPEARANCE = "PAPER";
        /*
        Other Appearance = "ELECTRONIC","EDI","UNKNOWN"
        */
        Element invoiceHead = doc.createElement("invoiceHead");
        parent.appendChild(invoiceHead);
        
        //supplierInfo
        Element supplierInfo = doc.createElement("supplierInfo");
        invoiceHead.appendChild(supplierInfo);       
            //supplierTaxNumber
            Element supplierTaxNumber = doc.createElement("supplierTaxNumber");
            supplierInfo.appendChild(supplierTaxNumber);           
            
                Element taxpayerId = doc.createElement("taxpayerId");
                supplierTaxNumber.appendChild(taxpayerId);
                taxpayerId.appendChild(doc.createTextNode(supplierDatas[0]));
                
                Element vatCode = doc.createElement("vatCode");
                supplierTaxNumber.appendChild(vatCode);
                vatCode.appendChild(doc.createTextNode(supplierDatas[1]));
                
                Element countyCode = doc.createElement("countyCode");
                supplierTaxNumber.appendChild(countyCode);
                countyCode.appendChild(doc.createTextNode(supplierDatas[2]));
            //groupMemberTaxnumber
            //communityVatNumber
            //supplierName
            Element supplierName = doc.createElement("supplierName");
            supplierInfo.appendChild(supplierName);
            supplierName.appendChild(doc.createTextNode(supplierDatas[3]));
            //supplierAddress
            Element supplierAddress = doc.createElement("supplierAddress");
            supplierInfo.appendChild(supplierAddress);
            
                Element detailedAddress = doc.createElement("detailedAddress");
                supplierAddress.appendChild(detailedAddress);
                
                    Element countryCode = doc.createElement("countryCode");
                    detailedAddress.appendChild(countryCode);
                    countryCode.appendChild(doc.createTextNode(supplierDatas[12]));
                    
                    Element postalCode = doc.createElement("postalCode");
                    detailedAddress.appendChild(postalCode);
                    postalCode.appendChild(doc.createTextNode(supplierDatas[4]));
                    
                    Element city = doc.createElement("city");
                    detailedAddress.appendChild(city);
                    city.appendChild(doc.createTextNode(supplierDatas[5]));
                    
                    Element streetName = doc.createElement("streetName");
                    detailedAddress.appendChild(streetName);
                    streetName.appendChild(doc.createTextNode(supplierDatas[6]));
                    
                    Element publicPlaceCategory = doc.createElement("publicPlaceCategory");
                    detailedAddress.appendChild(publicPlaceCategory);
                    publicPlaceCategory.appendChild(doc.createTextNode(supplierDatas[7]));
                    
                    Element number = doc.createElement("number");
                    detailedAddress.appendChild(number);
                    number.appendChild(doc.createTextNode(supplierDatas[8]));
                    
                    if(supplierDatas[9] != null)
                    {
                        Element floor = doc.createElement("floor");
                        detailedAddress.appendChild(floor);
                        floor.appendChild(doc.createTextNode(supplierDatas[9]));
                    }
                    
                    if(supplierDatas[10] != null)
                    {
                        Element door = doc.createElement("door");
                        detailedAddress.appendChild(door);
                        door.appendChild(doc.createTextNode(supplierDatas[10]));
                    }
            //supplierBankAccountNumber                   
            Element supplierBankAccountNumber = doc.createElement("supplierBankAccountNumber");
            supplierInfo.appendChild(supplierBankAccountNumber);
            supplierBankAccountNumber.appendChild(doc.createTextNode(supplierDatas[11]));
            
            //individualExemption
            //exciseLicenceNumber
        
        //customerInfo
        Element customerInfo = doc.createElement("customerInfo");
        invoiceHead.appendChild(customerInfo);
            //customerTaxNumber
            Element customerTaxNumber = doc.createElement("customerTaxNumber");
            customerInfo.appendChild(customerTaxNumber);
            
                taxpayerId = doc.createElement("taxpayerId");
                customerTaxNumber.appendChild(taxpayerId);
                taxpayerId.appendChild(doc.createTextNode(taxpayerID));
                
                vatCode = doc.createElement("vatCode");
                customerTaxNumber.appendChild(vatCode);
                vatCode.appendChild(doc.createTextNode(vatCODE));
                
                countyCode = doc.createElement("countyCode");
                customerTaxNumber.appendChild(countyCode);
                countyCode.appendChild(doc.createTextNode(countyCODE));

            //groupMemberTaxNumber
            //communityVatNumber
            //thirdStateTaxId
            //customerName    
            Element customerName = doc.createElement("customerName");
            customerInfo.appendChild(customerName);
            customerName.appendChild(doc.createTextNode(customerNAME));
            //customerAddress
            Element customerAddress = doc.createElement("customerAddress");
            customerInfo.appendChild(customerAddress);
            
                detailedAddress = doc.createElement("detailedAddress");
                customerAddress.appendChild(detailedAddress);
                
                    countryCode = doc.createElement("countryCode");
                    detailedAddress.appendChild(countryCode);
                    countryCode.appendChild(doc.createTextNode(countryCODE));
                    
                    postalCode = doc.createElement("postalCode");
                    detailedAddress.appendChild(postalCode);
                    postalCode.appendChild(doc.createTextNode(postalCODE));
                    
                    city = doc.createElement("city");
                    detailedAddress.appendChild(city);
                    city.appendChild(doc.createTextNode(CITY));
                    
                    streetName = doc.createElement("streetName");
                    detailedAddress.appendChild(streetName);
                    streetName.appendChild(doc.createTextNode(streetNAME));
                    
                    publicPlaceCategory = doc.createElement("publicPlaceCategory");
                    detailedAddress.appendChild(publicPlaceCategory);
                    publicPlaceCategory.appendChild(doc.createTextNode(publicPlaceCATEGORY));
                    
                    number = doc.createElement("number");
                    detailedAddress.appendChild(number);
                    number.appendChild(doc.createTextNode(NUMBER));
                    
            //customerBankAccountNumber
                    
        //fiscalRepresentativeInfo
            //fiscalRepresentativeTaxNumber
            //fiscalRepresentativeName
            //fiscalRepresentativeAddress
            //fiscalRepresentativeBankAccountNumber
                    
        //invoiceData
        Element invoiceData = doc.createElement("invoiceData");
        invoiceHead.appendChild(invoiceData);
            //invoiceNumber
            Element invoiceNumber = doc.createElement("invoiceNumber");
            invoiceData.appendChild(invoiceNumber);
            invoiceNumber.appendChild(doc.createTextNode(invoiceNUMBER));
            //invoiceCategory
            Element invoiceCategory = doc.createElement("invoiceCategory");
            invoiceData.appendChild(invoiceCategory);
            invoiceCategory.appendChild(doc.createTextNode(invoiceCATEGORY));
            //invoiceIssueDate
            Element invoiceIssueDate = doc.createElement("invoiceIssueDate");
            invoiceData.appendChild(invoiceIssueDate);
            invoiceIssueDate.appendChild(doc.createTextNode(billingData[2].toString()));
            //invoiceDeliveryDate
            Element invoiceDeliveryDate = doc.createElement("invoiceDeliveryDate");
            invoiceData.appendChild(invoiceDeliveryDate);
            invoiceDeliveryDate.appendChild(doc.createTextNode(billingData[3].toString()));
            //invoiceDeliveryPeriodStart
            //invoiceDeliveryPeriodEnd
            //invoiceAccountingDeliveryDate
            //currencyCode
            Element currencyCode = doc.createElement("currencyCode");
            invoiceData.appendChild(currencyCode);
            currencyCode.appendChild(doc.createTextNode(billingData[22].toString().toUpperCase()));
            //exchangeRate
            //selfBillingIndicator
            //paymentMethod
            Element paymentMethod = doc.createElement("paymentMethod");
            invoiceData.appendChild(paymentMethod);
            paymentMethod.appendChild(doc.createTextNode(paymentMETHOD));
            //paymentDate
            Element paymentDate = doc.createElement("paymentDate");
            invoiceData.appendChild(paymentDate);
            paymentDate.appendChild(doc.createTextNode(billingData[4].toString()));
            //cashAccountingIndicator
            //invoiceappearance
            Element invoiceAppearance = doc.createElement("invoiceAppearance");
            invoiceData.appendChild(invoiceAppearance);
            invoiceAppearance.appendChild(doc.createTextNode(invoiceAPPEARANCE));
            //electronicInvoiceHash
            //additionalInvoiceData
            /*
            Element additionalInvoiceData = doc.createElement("additionalInvoiceData");
            invoiceData.appendChild(additionalInvoiceData);
            
                Element dataName = doc.createElement("dataName");
                additionalInvoiceData.appendChild(dataName);
                dataName.appendChild(doc.createTextNode("dataNAME"));
                
                Element dataDescription = doc.createElement("dataDescription");
                additionalInvoiceData.appendChild(dataDescription);
                dataDescription.appendChild(doc.createTextNode("dataDESCRIPTION"));
                
                Element dataValue = doc.createElement("dataValue");
                additionalInvoiceData.appendChild(dataValue);
                dataValue.appendChild(doc.createTextNode("dataVALUE"));
            */
    }
    
    public ResultSet CreateBasicInvoiceLinesType(Connection con, Document doc, Element parent, String sorszam)
    {  
        dbconn conn = new dbconn();
        Connection NAVcon = conn.getConnection();
        Element InvoiceLines = doc.createElement("invoiceLines");
        parent.appendChild(InvoiceLines);  
        try
        {     
            PreparedStatement ps = NAVcon.prepareStatement("SELECT termek, mennyiseg, mennyisegi_egyseg, egysegar, netto_ar, afa, kozeparfolyam, szamlazo_szamla_adatok.szamla_sorszam FROM szamlazo_szamla INNER JOIN szamlazo_szamla_adatok ON szamlazo_szamla_adatok.szamla_sorszam = szamlazo_szamla.szamla_sorszam WHERE szamlazo_szamla_adatok.szamla_sorszam LIKE '" + sorszam + "'");
            ResultSet rs = ps.executeQuery();
            int lines = 1;
            while(rs.next())
            {
                Element line = doc.createElement("line");
                InvoiceLines.appendChild(line);

                    Element lineNumber = doc.createElement("lineNumber");
                    line.appendChild(lineNumber);
                    lineNumber.appendChild(doc.createTextNode(String.valueOf(lines)));

                    /*Termékkód
                    Element productCodes = doc.createElement("productCodes");
                    line.appendChild(productCodes);

                        Element productCode = doc.createElement("productCode");
                        productCodes.appendChild(productCode);

                            Element productCodeCategory = doc.createElement("productCodeCategory");
                            productCode.appendChild(productCodeCategory);
                            productCodeCategory.appendChild(doc.createTextNode("productCodeCATEGORY"));

                            Element productCodeValue = doc.createElement("productCodeValue");
                            productCode.appendChild(productCodeValue);
                            productCodeValue.appendChild(doc.createTextNode("productCodeVALUE"));
                    */
                    Element lineDescription = doc.createElement("lineDescription");
                    line.appendChild(lineDescription);
                    lineDescription.appendChild(doc.createTextNode(rs.getString("termek")));

                    Element quantity = doc.createElement("quantity");
                    line.appendChild(quantity);
                    float Quantity = Float.valueOf(rs.getString("mennyiseg"));
                    String QUANTITY = String.format (java.util.Locale.US,"%.2f", Quantity);
                    quantity.appendChild(doc.createTextNode(QUANTITY));

                    Element unitOfMeasure = doc.createElement("unitOfMeasure");
                    line.appendChild(unitOfMeasure);
                    unitOfMeasure.appendChild(doc.createTextNode(rs.getString("mennyisegi_egyseg")));

                    Element unitPrice = doc.createElement("unitPrice");
                    line.appendChild(unitPrice);
                    float UnitPrice = Float.valueOf(rs.getString("egysegar"));
                    String unitPRICE = String.format (java.util.Locale.US,"%.2f", UnitPrice);
                    unitPrice.appendChild(doc.createTextNode(unitPRICE));

                    Element lineAmountsNormal = doc.createElement("lineAmountsNormal");
                    line.appendChild(lineAmountsNormal);
                        
                        Element lineNetAmount = doc.createElement("lineNetAmount");
                        lineAmountsNormal.appendChild(lineNetAmount);
                        float LineNetAmount = Quantity * UnitPrice;
                        String lineNetAMOUNT = String.format (java.util.Locale.US,"%.2f", LineNetAmount);
                        lineNetAmount.appendChild(doc.createTextNode(lineNetAMOUNT));

                        Element lineVatRate = doc.createElement("lineVatRate");
                        lineAmountsNormal.appendChild(lineVatRate);

                            Element vatPercentage = doc.createElement("vatPercentage");
                            lineVatRate.appendChild(vatPercentage);
                            float Afa = Float.valueOf(rs.getString("afa"))/100;
                            String AFA = String.format(java.util.Locale.US, "%.2f", Afa);
                            vatPercentage.appendChild(doc.createTextNode(AFA));
                        
                        Element lineVatAmount = doc.createElement("lineVatAmount");
                        lineAmountsNormal.appendChild(lineVatAmount);
                        float LineVatAmount = LineNetAmount * Afa;
                        String LineVatAMOUNT = String.format(java.util.Locale.US, "%.2f", LineVatAmount);
                        lineVatAmount.appendChild(doc.createTextNode(LineVatAMOUNT));

                        Element lineGrossAmountNormal = doc.createElement("lineGrossAmountNormal");
                        lineAmountsNormal.appendChild(lineGrossAmountNormal);
                        float LineGrossAmount = LineNetAmount +  LineVatAmount;
                        String LineGrossAmountNORMAL = String.format(java.util.Locale.US, "%.2f", LineGrossAmount);
                        lineGrossAmountNormal.appendChild(doc.createTextNode(LineGrossAmountNORMAL));
                        
                    /*if(obligatedForPruductFee)
                    {
                        Element obligatedForProductFee = doc.createElement("obligatedForProductFee");
                        line.appendChild(obligatedForProductFee);
                        obligatedForProductFee.appendChild(doc.createTextNode(String.valueOf(takeOver)));
                        
                        if(takeOver)
                        {
                            String takeoverREASON = takeoverData[lines-1][0];
                            String takeoverAMOUNT = takeoverData[lines-1][1];
                            
                            Element productFeeClause = doc.createElement("productFeeClause");
                            line.appendChild(productFeeClause);

                                Element productFeeTakeoverData = doc.createElement("productFeeTakeoverData");
                                productFeeClause.appendChild(productFeeTakeoverData);

                                    Element takeoverReason = doc.createElement("takeoverReason");
                                    productFeeTakeoverData.appendChild(takeoverReason);
                                    takeoverReason.appendChild(doc.createTextNode(takeoverREASON));

                                    Element takeoverAmount = doc.createElement("takeoverAmount");
                                    productFeeTakeoverData.appendChild(takeoverAmount);
                                    takeoverAmount.appendChild(doc.createTextNode(takeoverAMOUNT));
                        }
                        else
                        {
                            String productCodeCATEGORY = takeoverData[lines-1][0];
                            String productCodeVALUE = takeoverData[lines-1][1];
                            String productFeeQUANTITY = takeoverData[lines-1][2];
                            String productFeeMeasuringUNIT = takeoverData[lines-1][3];
                            String productFeeRATE = takeoverData[lines-1][4];
                            String productFeeAMOUNT = takeoverData[lines-1][5];
                            
                            Element lineProductFeeContent = doc.createElement("lineProductFeeContent");
                            line.appendChild(lineProductFeeContent);
                        
                                Element productFeeCode = doc.createElement("productFeeCode");
                                lineProductFeeContent.appendChild(productFeeCode);

                                    Element productCodeCategory = doc.createElement("productCodeCategory");
                                    productFeeCode.appendChild(productCodeCategory);
                                    productCodeCategory.appendChild(doc.createTextNode(productCodeCATEGORY));

                                    Element productCodeValue = doc.createElement("productCodeValue");
                                    productFeeCode.appendChild(productCodeValue);
                                    productCodeValue.appendChild(doc.createTextNode(productCodeVALUE));

                                Element productFeeQuantity = doc.createElement("productFeeQuantity");
                                lineProductFeeContent.appendChild(productFeeQuantity);
                                productFeeQuantity.appendChild(doc.createTextNode(productFeeQUANTITY));

                                Element productFeeMeasuringUnit = doc.createElement("ProductFeeMeasuringUnit");
                                lineProductFeeContent.appendChild(productFeeMeasuringUnit);
                                productFeeMeasuringUnit.appendChild(doc.createTextNode(productFeeMeasuringUNIT));

                                Element productFeeRate = doc.createElement("productFeeRate");
                                lineProductFeeContent.appendChild(productFeeRate);
                                productFeeRate.appendChild(doc.createTextNode(productFeeRATE));

                                Element productFeeAmount = doc.createElement("productFeeAmount");
                                lineProductFeeContent.appendChild(productFeeAmount);
                                productFeeAmount.appendChild(doc.createTextNode(productFeeAMOUNT));
                        }
                    }*/
                lines++;
            }
            
        }
        catch (SQLException ex)
        {
            System.out.println(ex);
        }
        ResultSet rs2 = null;
        try
        {
            PreparedStatement ps2 = NAVcon.prepareStatement("SELECT termek, mennyiseg, mennyisegi_egyseg, egysegar, netto_ar, afa, kozeparfolyam, szamlazo_szamla_adatok.szamla_sorszam FROM szamlazo_szamla INNER JOIN szamlazo_szamla_adatok ON szamlazo_szamla_adatok.szamla_sorszam = szamlazo_szamla.szamla_sorszam WHERE szamlazo_szamla_adatok.szamla_sorszam LIKE '" + sorszam + "'");
            ResultSet rs = ps2.executeQuery();
        }
        catch (SQLException ex)
        {
            System.out.println(ex);
        }
        return rs2;
    }   
    
    public void CreateBasicInvoiceSummaryType(Connection con, Document doc, Element parent, ResultSet rs/*, String invoiceNumber*/)
    {
        Element InvoiceSummary = doc.createElement("invoiceSummary");
        parent.appendChild(InvoiceSummary);
        
            Element summaryNormal = doc.createElement("summaryNormal");
            InvoiceSummary.appendChild(summaryNormal);
            
            ArrayList<String []> vat0 = new ArrayList<>();
            ArrayList<String []> vat5 = new ArrayList<>();
            ArrayList<String []> vat10 = new ArrayList<>();
            ArrayList<String []> vat27 = new ArrayList<>();
            
            try
            {
                while(rs.next())
                {
                    String [] data = new String [4];
                    switch(Integer.valueOf(rs.getString("afa")))
                    {
                        case 0:
                            
                            data[0] = rs.getString("mennyiseg");
                            data[1] = rs.getString("egysegar");
                            data[2] = rs.getString("afa");
                            data[3] = rs.getString("kozeparfolyam");
                            vat0.add(data);
                            break;
                        case 5:
                            
                            data[0] = rs.getString("mennyiseg");
                            data[1] = rs.getString("egysegar");
                            data[2] = rs.getString("afa");
                            data[3] = rs.getString("kozeparfolyam");
                            vat5.add(data);
                            break;
                        case 10:
                            
                            data[0] = rs.getString("mennyiseg");
                            data[1] = rs.getString("egysegar");
                            data[2] = rs.getString("afa");
                            data[3] = rs.getString("kozeparfolyam");
                            vat10.add(data);
                            break;
                        case 27:
                            
                            data[0] = rs.getString("mennyiseg");
                            data[1] = rs.getString("egysegar");
                            data[2] = rs.getString("afa");
                            data[3] = rs.getString("kozeparfolyam");
                            vat27.add(data);
                            break;
                    }                   
                }
            }
            catch (SQLException ex)
            {
                //System.out.println(ex);
            }
            
            float invoiceNetAMOUNT = 0.0f;
            float invoiceVatAMOUNT = 0.0f;
            float invoiceVatAMOUNTHUF = 0.0f;
            float MiddleExchangeRate = 0.00f;
            
            try
            {
                if(!vat0.get(0)[0].isEmpty())
                {
                    float LineNetAMOUNT = 0.00f;
                    float vatPercentage = 0.00f;
                    
                    for(String [] data : vat0)
                    {              
                        float Quantity = Float.valueOf(data[0]);
                        float UnitPrice = Float.valueOf(data[1]);
                        vatPercentage = Float.valueOf(data[2])/100;
                        MiddleExchangeRate = Float.valueOf(data[3]);
                        float LineNetAmount = Quantity * UnitPrice;
                        LineNetAMOUNT += LineNetAmount;
                    }
                    float [] data = CreateBasicSummaryByVatRateType(doc, summaryNormal, vatPercentage, LineNetAMOUNT, MiddleExchangeRate);
                    invoiceNetAMOUNT += data[0];
                    invoiceVatAMOUNT += data[1];
                }
            }
            catch(IndexOutOfBoundsException iobE)
            {
                //System.out.println("Nem volt termék 0%-os áfával!");
            }
            try
            {
                if(!vat5.get(0)[0].isEmpty())
                {
                    float LineNetAMOUNT = 0.00f;
                    float vatPercentage = 0.00f;
                    MiddleExchangeRate = 0.00f;
                    for(String [] data : vat5)
                    {              
                        float Quantity = Float.valueOf(data[0]);
                        float UnitPrice = Float.valueOf(data[1]);
                        vatPercentage = Float.valueOf(data[2])/100;
                        MiddleExchangeRate = Float.valueOf(data[3]);
                        float LineNetAmount = Quantity * UnitPrice;
                        LineNetAMOUNT += LineNetAmount;
                    }
                    float [] data = CreateBasicSummaryByVatRateType(doc, summaryNormal, vatPercentage, LineNetAMOUNT, MiddleExchangeRate);
                    invoiceNetAMOUNT += data[0];
                    invoiceVatAMOUNT += data[1];
                }
            }
            catch(IndexOutOfBoundsException iobE)
            {
                //System.out.println("Nem volt termék 5%-os áfával!");
            }
            try
            {
                if(!vat10.get(0)[0].isEmpty())
                {
                    float LineNetAMOUNT = 0.00f;
                    float vatPercentage = 0.00f;
                    MiddleExchangeRate = 0.00f;
                    for(String [] data : vat10)
                    {              
                        float Quantity = Float.valueOf(data[0]);
                        float UnitPrice = Float.valueOf(data[1]);
                        vatPercentage = Float.valueOf(data[2])/100;
                        MiddleExchangeRate = Float.valueOf(data[3]);
                        float LineNetAmount = Quantity * UnitPrice;
                        LineNetAMOUNT += LineNetAmount;
                    }
                    float[] data = CreateBasicSummaryByVatRateType(doc, summaryNormal, vatPercentage, LineNetAMOUNT, MiddleExchangeRate);                   
                    invoiceNetAMOUNT += data[0];
                    invoiceVatAMOUNT += data[1];
                }
            }
            catch(IndexOutOfBoundsException iobE)
            {
                //System.out.println("Nem volt termék 10%-os áfával!");
            }
            try
            {
                if(!vat27.get(0)[0].equals(""))
                {
                    float LineNetAMOUNT = 0.00f;
                    float vatPercentage = 0.00f;
                    MiddleExchangeRate = 0.00f;
                    for(String [] data : vat27)
                    {              
                        float Quantity = Float.valueOf(data[0]);
                        float UnitPrice = Float.valueOf(data[1]);
                        vatPercentage = Float.valueOf(data[2])/100;
                        MiddleExchangeRate = Float.valueOf(data[3]);
                        float LineNetAmount = Quantity * UnitPrice;
                        LineNetAMOUNT = LineNetAMOUNT + LineNetAmount;
                    }
                    float [] data = CreateBasicSummaryByVatRateType(doc, summaryNormal, vatPercentage, LineNetAMOUNT, MiddleExchangeRate);
                    invoiceNetAMOUNT += data[0];
                    invoiceVatAMOUNT += data[1];
                }
            }
            catch(IndexOutOfBoundsException iobE)
            {
                //System.out.println("Nem volt termék 27%-os áfával!");
            }                                               
                    
                Element invoiceNetAmount = doc.createElement("invoiceNetAmount");
                summaryNormal.appendChild(invoiceNetAmount);
                invoiceNetAmount.appendChild(doc.createTextNode(String.format(java.util.Locale.US, "%.2f", invoiceNetAMOUNT)));
                
                Element invoiceVatAmount = doc.createElement("invoiceVatAmount");
                summaryNormal.appendChild(invoiceVatAmount);
                invoiceVatAmount.appendChild(doc.createTextNode(String.format(java.util.Locale.US, "%.2f", invoiceVatAMOUNT)));
                
                Element invoiceVatAmountHUF = doc.createElement("invoiceVatAmountHUF");
                summaryNormal.appendChild(invoiceVatAmountHUF);
                invoiceVatAMOUNTHUF = invoiceVatAMOUNT * MiddleExchangeRate;
                invoiceVatAmountHUF.appendChild(doc.createTextNode(String.format(java.util.Locale.US, "%.2f", invoiceVatAMOUNTHUF)));
                    
            Element invoiceGrossAmount = doc.createElement("invoiceGrossAmount");
            InvoiceSummary.appendChild(invoiceGrossAmount);
            float invoiceGrossAMOUNT = invoiceNetAMOUNT + invoiceVatAMOUNT;
            invoiceGrossAmount.appendChild(doc.createTextNode(String.format(java.util.Locale.US, "%.2f", invoiceGrossAMOUNT)));
    }
    
    public void CreateBasicInvoiceReferenceType(Document doc, Element parent, String originalInvoiceNUMBER)
    {
        Element invoiceReference = doc.createElement("invoiceReference");
        parent.appendChild(invoiceReference);
        
            DateFormat timeStamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            timeStamp.setTimeZone(TimeZone.getTimeZone("GMT"));
            String timeSTAMP = timeStamp.format(new Date());
            
            DateFormat issueDate = new SimpleDateFormat("yyyy-MM-dd");
            String issueDATE = issueDate.format(new Date());
        
            Element originalInvoiceNumber = doc.createElement("originalInvoiceNumber");
            invoiceReference.appendChild(originalInvoiceNumber);
            originalInvoiceNumber.appendChild(doc.createTextNode(originalInvoiceNUMBER));
            
            Element modificationIssueDate = doc.createElement("modificationIssueDate");
            invoiceReference.appendChild(modificationIssueDate);
            modificationIssueDate.appendChild(doc.createTextNode(issueDATE));
            
            Element modificationTimestamp = doc.createElement("modificationTimestamp");
            invoiceReference.appendChild(modificationTimestamp);
            modificationTimestamp.appendChild(doc.createTextNode(timeSTAMP));
            
            //Default false
            //Akkor true ha a NAV adatbázisban nincs adat az eredeti számláról
            Element modifyWithoutMaster = doc.createElement("modifyWithoutMaster");
            invoiceReference.appendChild(modifyWithoutMaster);
           // modifyWithoutMaster.appendChild(doc.createTextNode());
    }
    
    public float[] CreateBasicSummaryByVatRateType(Document doc, Element parent, float vat_Percentage, float vatRateNet_Amount, float MiddleExchangeRate)
    {
        Element summaryByVatRate = doc.createElement("summaryByVatRate");
        parent.appendChild(summaryByVatRate);

            Element vatRate = doc.createElement("vatRate");
            summaryByVatRate.appendChild(vatRate);

                Element vatPercentage = doc.createElement("vatPercentage");
                vatRate.appendChild(vatPercentage);              
                String vatPERCENTAGE = String.format(java.util.Locale.US, "%.2f", vat_Percentage);
                vatPercentage.appendChild(doc.createTextNode(vatPERCENTAGE));

            Element vatRateNetAmount = doc.createElement("vatRateNetAmount");
            summaryByVatRate.appendChild(vatRateNetAmount);
            String vatRateNetAMOUNT = String.format(java.util.Locale.US, "%.2f", vatRateNet_Amount);
            vatRateNetAmount.appendChild(doc.createTextNode(vatRateNetAMOUNT));

            Element vatRateVatAmount = doc.createElement("vatRateVatAmount");
            summaryByVatRate.appendChild(vatRateVatAmount);

            float vatRATEVatAmount = vatRateNet_Amount * vat_Percentage;
            String vatRateVatAMOUNT = String.format(java.util.Locale.US, "%.2f",vatRATEVatAmount);
            vatRateVatAmount.appendChild(doc.createTextNode(vatRateVatAMOUNT));

            Element vatRateVatAmountHUF = doc.createElement("vatRateVatAmountHUF");
            summaryByVatRate.appendChild(vatRateVatAmountHUF);
            float vatRateVATAmount = vatRATEVatAmount * MiddleExchangeRate;
            String vatRateVatAMOUNTHUF = String.format(java.util.Locale.US, "%.2f",vatRateVATAmount);
            vatRateVatAmountHUF.appendChild(doc.createTextNode(vatRateVatAMOUNTHUF));

            Element vatRateGrossAmount = doc.createElement("vatRateGrossAmount");
            summaryByVatRate.appendChild(vatRateGrossAmount);
            float vatRATeGrossAmount = vatRateNet_Amount + vatRATEVatAmount;
            String vatRateGrossAMOUNT = String.format(java.util.Locale.US, "%.2f",vatRATeGrossAmount);
            vatRateGrossAmount.appendChild(doc.createTextNode(vatRateGrossAMOUNT));
            
            float [] invoice = {vatRateNet_Amount, vatRATEVatAmount};
            return invoice; 
    }
    
    public void GenerateXmlFile(Document doc)
    {
        try
        {
            TransformerFactory tfFactory = TransformerFactory.newInstance();
            Transformer transformer = tfFactory.newTransformer();
            doc.setXmlStandalone(true);
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(System.out);
            
            transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);    
        }
        catch(TransformerException tfE)
        {
            tfE.printStackTrace(System.out);
        }
    }
    
    public Document BelfoldiTermekertekesites(Object [] billingData)
    {
        dbconn conn = new dbconn();
        Connection con = conn.getConnection();
        String [] data = new String [7];
        try
        {
            PreparedStatement ps = con.prepareStatement("SELECT nev, irsz, varos, utca, adoszam, bankszamlaszam, countryCode FROM szamlazo_ceg_adatok WHERE adoszam LIKE ?");
            ps.setString(1,billingData[28].toString());
            ResultSet rs = ps.executeQuery();          
            
            while(rs.next())
            {
                data[0] = rs.getString("nev");
                data[1] = rs.getString("irsz");
                data[2] = rs.getString("varos");
                data[3] = rs.getString("utca");
                data[4] = rs.getString("adoszam");
                data[5] = rs.getString("bankszamlaszam");
                data[6] = rs.getString("countryCode");
            }
        }
        catch (SQLException ex)
        {
            System.out.println(ex);
        }
        
        String [] supplierDatas = new String [13];
               
        supplierDatas[3] = data[0];
        supplierDatas[4] = data[1];
        supplierDatas[5] = data[2];
        String [] detailedAddress = new String [5];
        detailedAddress = data[3].split(" ");
            supplierDatas[6] = detailedAddress[0];
            supplierDatas[7] = detailedAddress[1];
            supplierDatas[8] = detailedAddress[2];
            try
            {
                supplierDatas[9] = detailedAddress[3];
                supplierDatas[10] = detailedAddress[4];
            }
            catch(IndexOutOfBoundsException iofb)
            {
                supplierDatas[9] = null;
                supplierDatas[10] = null;
            }
            
        String [] supplierTaxNumber = data[4].split("-");
            supplierDatas[0] = supplierTaxNumber[0];
            supplierDatas[1] = supplierTaxNumber[1];
            supplierDatas[2] = supplierTaxNumber[2];
        supplierDatas[11] = data[5];
        supplierDatas[12] = data[6];
        
        String invoiceNumber = billingData[21].toString();
        Document doc = null;      
        try
        {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            doc = docBuilder.newDocument();
            
            CreateBasicInvoiceType(doc);
                CreateBasicInvoiceHeadType(doc, (Element)doc.getFirstChild().getFirstChild(), supplierDatas, billingData);
                ResultSet rs = CreateBasicInvoiceLinesType(con, doc, (Element)doc.getFirstChild().getFirstChild(),invoiceNumber);
                CreateBasicInvoiceSummaryType(con, doc, (Element)doc.getFirstChild().getFirstChild(), rs/*, invoiceNumber*/);
                
            GenerateXmlFile(doc);
        }
        catch (ParserConfigurationException ex)
        {
            ex.printStackTrace(System.out);
        }
        return doc;
    }
    
    public Document TevesTermekHelyesbítese(String originalInvoiceNumber, String [] supplierDatas, Object [] billingData, Connection con)
    {
        Document doc = null;
        
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
        CreateBasicInvoiceReferenceType(doc, (Element)doc.getFirstChild().getFirstChild(), originalInvoiceNumber);
        CreateBasicInvoiceHeadType(doc, (Element)doc.getFirstChild().getFirstChild(), supplierDatas, billingData);
        //ResultSet rs = CreateBasicInvoiceLinesType(con, doc, (Element)doc.getFirstChild().getFirstChild(), originalInvoiceNumber, obligatedForProductFee, takeOver, takeoverData);
        //CreateBasicInvoiceSummaryType(con, doc, (Element)doc.getFirstChild().getFirstChild(), rs);
        
        return doc;
    }
    
    public Document TermekdijasSzamla(String [] supplierDatas, Object [] billingData)
    {
        Document doc = null;
        
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
        CreateBasicInvoiceHeadType(doc, (Element)doc.getFirstChild().getFirstChild(), supplierDatas, billingData);
        
        
        return doc;
    }   
}