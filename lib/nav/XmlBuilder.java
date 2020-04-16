package nav;

import onlineInvoice.AdditionalQueryParamsType;
import onlineInvoice.AddressType;
import onlineInvoice.AnnulmentOperationType;
import onlineInvoice.AnnulmentOperationsType;
import onlineInvoice.DateIntervalParamType;
import onlineInvoice.DateTimeIntervalParamType;
import onlineInvoice.InvoiceExchangeType;
import onlineInvoice.InvoiceHeadType;
import onlineInvoice.InvoiceNumberQueryType;
import onlineInvoice.InvoiceOperationType;
import onlineInvoice.InvoiceOperationsType;
import onlineInvoice.InvoiceQueryParamsType;
import onlineInvoice.InvoiceReferenceType;
import onlineInvoice.LineType;
import onlineInvoice.MandatoryQueryParamsType;
import onlineInvoice.OriginalInvoiceNumberQueryType;
import onlineInvoice.ProductCodeType;
import onlineInvoice.ProductFeeDataType;
import onlineInvoice.ProductFeeSummaryType;
import onlineInvoice.ProductFeeTakeoverDataType;
import onlineInvoice.RelationQueryDateType;
import onlineInvoice.RelationQueryMonetaryType;
import onlineInvoice.RelationalQueryParamsType;
import onlineInvoice.SummaryByVatRateType;
import onlineInvoice.SummaryType;
import onlineInvoice.TransactionQueryParamsType;
import onlineInvoice.VatRateType;
import onlineInvoice.request.ManageAnnulmentRequest;
import onlineInvoice.request.ManageInvoiceRequest;
import onlineInvoice.request.QueryInvoiceCheckRequest;
import onlineInvoice.request.QueryInvoiceDataRequest;
import onlineInvoice.request.QueryInvoiceDigestRequest;
import onlineInvoice.request.QueryTransactionStatusRequest;
import onlineInvoice.request.QueryTaxpayerRequest;
import onlineInvoice.request.TokenExchangeRequest;
import java.io.StringWriter;
import java.util.ArrayList;
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
 * @author Tomy
 */
public class XmlBuilder
{
    DocumentBuilderFactory factory;
    DocumentBuilder builder;
    Document doc;
    
    String service = "";
    
    public String ManageAnnulment(ManageAnnulmentRequest request)
    {
        try
        {
            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
            doc = builder.newDocument();
            doc.setXmlStandalone(true);
            
            service = "ManageAnnulmentRequest";
            
            CreateBasicRequestType(request.header, request.user, request.software);
            
            CreateExchangeToken((Element)doc.getFirstChild(), request.getExchangeToken());
            CreateAnnulmentOperations((Element)doc.getFirstChild(), request.getAnnulmentOperations());
        }
        catch(ParserConfigurationException ex)
        {
            System.err.println("XmlBuilder.java/ManageAnnulment()");
        }
        
        String xml = getStringFromDocument();
        
        return xml;
    }
    
    public String ManageInvoice(ManageInvoiceRequest request)
    {
        try
        {
            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
            doc = builder.newDocument();
            doc.setXmlStandalone(true);
            
            service = "ManageInvoiceRequest";
            
            CreateBasicRequestType(request.header, request.user, request.software);
            
            CreateExchangeToken((Element)doc.getFirstChild(), request.getExchangeToken());
            CreateInvoiceOperations((Element)doc.getFirstChild(), request.getInvoiceOperations());
        }
        catch(ParserConfigurationException ex)
        {
            System.err.println("XmlBuilder.java/ManageInvoice()");
        }
        
        String xml = getStringFromDocument();
        
        return xml;
    }
    
    public String QueryInvoiceCheck(QueryInvoiceCheckRequest request)
    {
        try
        {
            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
            doc = builder.newDocument();
            doc.setXmlStandalone(true);
            
            service = "QueryInvoiceCheckRequest";
            
            CreateBasicRequestType(request.header, request.user, request.software);
            
            CreateInvoiceNumberQuery((Element)doc.getFirstChild(), request.getInvoiceNumberQuery());
        }
        catch(ParserConfigurationException ex)
        {
            System.err.println("XmlBuilder.java/QueryInvoiceCheck()");
        }
        
        String xml = getStringFromDocument();
        
        return xml;
    }
    
    public String QueryInvoiceData(QueryInvoiceDataRequest request)
    {
        try
        {
            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
            doc = builder.newDocument();
            doc.setXmlStandalone(true);
            
            service = "QueryInvoiceDataRequest";
            
            CreateBasicRequestType(request.header, request.user, request.software);
            
            CreateInvoiceNumberQuery((Element)doc.getFirstChild(), request.getInvoiceNumberQuery());
        }
        catch(ParserConfigurationException ex)
        {
            System.err.println("XmlBuilder.java/QueryInvoiceData()");
        }
        
        String xml = getStringFromDocument();
        
        return xml;
    }
    
    /*public String QueryInvoiceDigest(QueryInvoiceDigestRequest request)
    {
        try
        {
            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
            doc = builder.newDocument();
            doc.setXmlStandalone(true);
            
            service = "QueryInvoiceDigestRequest";
            
            CreateBasicRequestType(request.header, request.user, request.software);
            CreatePage((Element)doc.getFirstChild(), request.getPage());
            CreateInvoiceDirection((Element)doc.getFirstChild(), request.getInvoiceDirection());
            CreateInvoiceQueryParams((Element)doc.getFirstChild(), request.getInvoiceQueryParams());
        }
        catch(ParserConfigurationException ex)
        {
            System.err.println("XmlBuilder.java/QueryInvoiceDigest()");
        }
        
        String xml = getStringFromDocument();
        
        return xml;
    }*/
    
    public String QueryTransactionStatus(QueryTransactionStatusRequest request)
    {
        try
        {
            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
            doc = builder.newDocument();
            doc.setXmlStandalone(true);
            
            service = "QueryTransactionStatusRequest";
            
            CreateBasicRequestType(request.header, request.user, request.software);
            CreateTransactionId((Element)doc.getFirstChild(), request.getTransactionId());
            CreateReturnOriginalRequest((Element)doc.getFirstChild(), request.getReturnOriginalRequest());
        }
        catch(ParserConfigurationException ex)
        {
            System.err.println("XmlBuilder.java/QueryTransactionStatus()");
        }
        
        String xml = getStringFromDocument();
        
        return xml;
    }
    
    public String QueryTaxpayer(QueryTaxpayerRequest request)
    {
        try
        {
            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
            doc = builder.newDocument();
            doc.setXmlStandalone(true);
            
            service = "QueryTaxpayerRequest";
            
            CreateBasicRequestType(request.header, request.user, request.software);
            CreateTaxnumber(request.getTaxNumber());
        }
        catch(ParserConfigurationException ex)
        {
            System.err.println("XmlBuilder.java/TokenExchange()");
        }
        
        String xml = getStringFromDocument();
        
        return xml;
    }
    
    public String TokenExchange(TokenExchangeRequest request)
    {
        try
        {
            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
            doc = builder.newDocument();
            doc.setXmlStandalone(true);
            
            service = "TokenExchangeRequest";
            
            CreateBasicRequestType(request.header, request.user, request.software);
        }
        catch(ParserConfigurationException ex)
        {
            System.err.println("XmlBuilder.java/TokenExchange()");
        }
        
        String xml = getStringFromDocument();
        
        return xml;
    }
    
    public String CreateInvoiceExchangeType(InvoiceExchangeType invoice)
    {
        String xml = "";
        
        try
        {
            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
            doc = builder.newDocument();
            doc.setXmlStandalone(true);
            
            CreateBasicInvoiceType(invoice.invoiceNumber, invoice.invoiceIssueDate);
            CreateInvoiceReferenceType((Element)doc.getFirstChild().getFirstChild(), invoice.invoiceReference);
            CreateInvoiceHeadType((Element)doc.getFirstChild().getFirstChild(), invoice.invoiceHead);
            CreateInvoiceLinesType((Element)doc.getFirstChild().getFirstChild(), invoice.invoiceLines);
            
            if(invoice.isNeedProductFeeSummary())
            {
                CreateProductFeeSummaryType((Element)doc.getFirstChild().getFirstChild(), invoice.productFeeSummary);
            }
            
            CreateInvoiceSummaryType((Element)doc.getFirstChild().getFirstChild(), invoice.invoiceSummary);

            xml = getStringFromDocument();
        }
        catch(ParserConfigurationException ex)
        {
            ex.printStackTrace();
        }
        
        return xml;
    }
    
    private void CreateBasicInvoiceType(String invoiceNUMBER, String invoiceIssueDATE)
    {
        Element InvoiceData = doc.createElement("InvoiceData");
        doc.appendChild(InvoiceData);
        
            Attr xmlns_xs = doc.createAttribute("xmlns:xs");
            xmlns_xs.setValue("http://www.w3.org/2001/XMLSchema-instance");
            InvoiceData.setAttributeNode(xmlns_xs);
            
            Attr xmlns = doc.createAttribute("xmlns");
            xmlns.setValue("http://schemas.nav.gov.hu/OSA/2.0/data");
            InvoiceData.setAttributeNode(xmlns);
            
            /*Attr xs_schemeLocation = doc.createAttribute("xs:schemeLocation");
            xs_schemeLocation.setValue("http://schemas.nav.gov.hu/OSA/1.0/data invoiceData.xsd");
            Invoice.setAttributeNode(xs_schemeLocation);*/
            
                Element invoiceNumber = doc.createElement("invoiceNumber");
                InvoiceData.appendChild(invoiceNumber);
                invoiceNumber.appendChild(doc.createTextNode(invoiceNUMBER));

                Element invoiceIssueDate = doc.createElement("invoiceIssueDate");
                InvoiceData.appendChild(invoiceIssueDate);
                invoiceIssueDate.appendChild(doc.createTextNode(invoiceIssueDATE));

                Element invoiceMain = doc.createElement("invoiceMain");
                InvoiceData.appendChild(invoiceMain);
            
                    Element invoice = doc.createElement("invoice");
                    invoiceMain.appendChild(invoice);
    }
    
    private void CreateInvoiceReferenceType(Element parent, InvoiceReferenceType reference)
    {
        if(reference != null)
        {
            Element invoiceReference = doc.createElement("invoiceReference");
            parent.appendChild(invoiceReference);
                Element originalInvoiceNumber = doc.createElement("originalInvoiceNumber");
                invoiceReference.appendChild(originalInvoiceNumber);
                originalInvoiceNumber.appendChild(doc.createTextNode(reference.getOriginalInvoiceNumber()));

                Element modificationIssueDate = doc.createElement("modificationIssueDate");
                invoiceReference.appendChild(modificationIssueDate);
                modificationIssueDate.appendChild(doc.createTextNode(reference.getModificationIssueDate()));

                Element modificationTimestamp = doc.createElement("modificationTimestamp");
                invoiceReference.appendChild(modificationTimestamp);
                modificationTimestamp.appendChild(doc.createTextNode(reference.getModificationTimestamp()));

                if(!reference.getLastModificationReference().isEmpty())
                {
                    Element lastModificationReference = doc.createElement("lastModificationReference");
                    invoiceReference.appendChild(lastModificationReference);
                    lastModificationReference.appendChild(doc.createTextNode(reference.getLastModificationReference()));
                }

                Element modifyWithoutMaster = doc.createElement("modifyWithoutMaster");
                invoiceReference.appendChild(modifyWithoutMaster);
                modifyWithoutMaster.appendChild(doc.createTextNode(reference.getModifyWithoutMaster()));
        }
    }
    
    private void CreateInvoiceHeadType(Element parent, InvoiceHeadType invoiceHead)
    {
        //kötelező
        Element invoiceHeadElement = doc.createElement("invoiceHead");
        parent.appendChild(invoiceHeadElement);
            
            //kötelező
            Element supplierInfo = doc.createElement("supplierInfo");
            invoiceHeadElement.appendChild(supplierInfo);
            
                //kötelező
                Element supplierTaxNumber = doc.createElement("supplierTaxNumber");
                supplierInfo.appendChild(supplierTaxNumber);
                
                    //kötelező
                    Element taxpayerId = doc.createElement("taxpayerId");
                    supplierTaxNumber.appendChild(taxpayerId);
                    taxpayerId.appendChild(doc.createTextNode(invoiceHead.getSupplierInfo().getTaxNumber().getTaxpayerId()));
                    
                    //nem kötelező
                    Element vatCode = doc.createElement("vatCode");
                    supplierTaxNumber.appendChild(vatCode);
                    vatCode.appendChild(doc.createTextNode(invoiceHead.getSupplierInfo().getTaxNumber().getVatCode()));
                    
                    //nem kötelező
                    Element countyCode = doc.createElement("countyCode");
                    supplierTaxNumber.appendChild(countyCode);
                    countyCode.appendChild(doc.createTextNode(invoiceHead.getSupplierInfo().getTaxNumber().getCountryCode()));
                
                //nem kötelező
                Element groupMemberTaxNumber;
                
                //nem kötelező
                Element communityVatNumber;
                
                //kötelező
                Element supplierName = doc.createElement("supplierName");
                supplierInfo.appendChild(supplierName);
                supplierName.appendChild(doc.createTextNode(invoiceHead.getSupplierInfo().getName()));
                
                //kötelező
                Element supplierAddress = doc.createElement("supplierAddress");
                supplierInfo.appendChild(supplierAddress);
                
                if(invoiceHead.getSupplierInfo().getAddress().getType() == AddressType.Type.SIMPLE)
                {
                    //kötelező
                    Element simpleAddress = doc.createElement("simpleAddress");
                    supplierAddress.appendChild(simpleAddress);
                    
                        //kötelező
                        Element supplierCountryCode = doc.createElement("countryCode");
                        simpleAddress.appendChild(supplierCountryCode);
                        supplierCountryCode.appendChild(doc.createTextNode(invoiceHead.getSupplierInfo().getAddress().getCountryCode()));

                        //nem kötelező
                        Element supplierRegion;

                        //kötelező
                        Element supplierPostalCode = doc.createElement("postalCode");
                        simpleAddress.appendChild(supplierPostalCode);
                        supplierPostalCode.appendChild(doc.createTextNode(invoiceHead.getSupplierInfo().getAddress().getPostalCode()));

                        //kötelező
                        Element supplierCity = doc.createElement("city");
                        simpleAddress.appendChild(supplierCity);
                        supplierCity.appendChild(doc.createTextNode(invoiceHead.getSupplierInfo().getAddress().getCity()));

                        //kötelező
                        Element supplierAdditionalAddressDetail = doc.createElement("additionalAddressDetail");
                        simpleAddress.appendChild(supplierAdditionalAddressDetail);
                        supplierAdditionalAddressDetail.appendChild(doc.createTextNode(invoiceHead.getSupplierInfo().getAddress().getAdditionalAddressDetail()));
                }
                else
                {
                    //kötelező
                    Element detailedAddress = doc.createElement("detailedAddress");
                    supplierAddress.appendChild(detailedAddress);
                    
                        //kötelező
                        Element supplierCountryCode = doc.createElement("countryCode");
                        detailedAddress.appendChild(supplierCountryCode);
                        supplierCountryCode.appendChild(doc.createTextNode(invoiceHead.getSupplierInfo().getAddress().getCountryCode()));

                        //nem kötelező
                        Element supplierRegion;

                        //kötelező
                        Element supplierPostalCode = doc.createElement("postalCode");
                        detailedAddress.appendChild(supplierPostalCode);
                        supplierPostalCode.appendChild(doc.createTextNode(invoiceHead.getSupplierInfo().getAddress().getPostalCode()));

                        //kötelező
                        Element supplierCity = doc.createElement("city");
                        detailedAddress.appendChild(supplierCity);
                        supplierCity.appendChild(doc.createTextNode(invoiceHead.getSupplierInfo().getAddress().getCity()));

                        //kötelező
                        Element supplierStreetName = doc.createElement("streetName");
                        detailedAddress.appendChild(supplierStreetName);
                        supplierStreetName.appendChild(doc.createTextNode(invoiceHead.getSupplierInfo().getAddress().getStreetName()));

                        //kötelező
                        Element supplierPublicPlaceCategory = doc.createElement("publicPlaceCategory");
                        detailedAddress.appendChild(supplierPublicPlaceCategory);
                        supplierPublicPlaceCategory.appendChild(doc.createTextNode(invoiceHead.getSupplierInfo().getAddress().getPublicPlaceCategory()));

                        //nem kötelező
                        Element supplierNumber;

                        //nem kötelező
                        Element supplierBuilding;

                        //nem kötelező
                        Element supplierStaircase;

                        //nem kötelező
                        Element supplierFloor;

                        //nem kötelező
                        Element supplierDoor;

                        //nem kötelező
                        Element supplierLotNumber;
                }
                
                //nem kötelező
                Element supplierBankAccountNumber;
                
                //nem kötelező
                Element individualExemption;
                
                //nem kötelező
                Element exciseLicenceNum;
            
            //nem kötelező
            Element customerInfo = doc.createElement("customerInfo");
            invoiceHeadElement.appendChild(customerInfo);
            
            if(!invoiceHead.getCustomerInfo().getTaxNumber().getTaxpayerId().isEmpty())
            {
                //nem kötelező
                Element taxNumber = doc.createElement("customerTaxNumber");
                customerInfo.appendChild(taxNumber);
                
                    //kötelező
                    Element customerTaxpayerId = doc.createElement("taxpayerId");
                    taxNumber.appendChild(customerTaxpayerId);
                    customerTaxpayerId.appendChild(doc.createTextNode(invoiceHead.getCustomerInfo().getTaxNumber().getTaxpayerId()));
                    
                    //nem kötelező
                    Element customerVatCode = doc.createElement("vatCode");
                    taxNumber.appendChild(customerVatCode);
                    customerVatCode.appendChild(doc.createTextNode(invoiceHead.getCustomerInfo().getTaxNumber().getVatCode()));
                    
                    //nem kötelező
                    Element customerCountyCode = doc.createElement("countyCode");
                    taxNumber.appendChild(customerCountyCode);
                    customerCountyCode.appendChild(doc.createTextNode(invoiceHead.getCustomerInfo().getTaxNumber().getCountryCode()));
            }
            
                //nem kötelező
                Element customerGroupMemberTaxNumber;
                
                //nem kötelező
                Element customerCommunityVatNumber;
                
                //nem kötelező
                Element thirdStateTaxId;
                
                //kötelező
                Element customerName = doc.createElement("customerName");
                customerInfo.appendChild(customerName);
                customerName.appendChild(doc.createTextNode(invoiceHead.getCustomerInfo().getCustomerName()));
                
                //kötelező
                Element customerAddress = doc.createElement("customerAddress");
                customerInfo.appendChild(customerAddress);
                
                if(invoiceHead.getCustomerInfo().getCustomerAddress().getType() == AddressType.Type.SIMPLE)
                {
                    //kötelező
                    Element simpleAddress = doc.createElement("simpleAddress");
                    customerAddress.appendChild(simpleAddress);
                    
                        //kötelező
                        Element customerCountryCode = doc.createElement("countryCode");
                        simpleAddress.appendChild(customerCountryCode);
                        customerCountryCode.appendChild(doc.createTextNode(invoiceHead.getSupplierInfo().getAddress().getCountryCode()));

                        //nem kötelező
                        Element customerRegion;

                        //kötelező
                        Element customerPostalCode = doc.createElement("postalCode");
                        simpleAddress.appendChild(customerPostalCode);
                        customerPostalCode.appendChild(doc.createTextNode(invoiceHead.getSupplierInfo().getAddress().getPostalCode()));

                        //kötelező
                        Element customerCity = doc.createElement("city");
                        simpleAddress.appendChild(customerCity);
                        customerCity.appendChild(doc.createTextNode(invoiceHead.getSupplierInfo().getAddress().getCity()));

                        //kötelező
                        Element customerAdditionalAddressDetail = doc.createElement("additionalAddressDetail");
                        simpleAddress.appendChild(customerAdditionalAddressDetail);
                        customerAdditionalAddressDetail.appendChild(doc.createTextNode(invoiceHead.getSupplierInfo().getAddress().getAdditionalAddressDetail()));
                }
                else
                {
                    //kötelező
                    Element detailedAddress = doc.createElement("detailedAddress");
                    customerAddress.appendChild(detailedAddress);
                    
                        //kötelező
                        Element customerCountryCode = doc.createElement("countryCode");
                        detailedAddress.appendChild(customerCountryCode);
                        customerCountryCode.appendChild(doc.createTextNode(invoiceHead.getSupplierInfo().getAddress().getCountryCode()));

                        //nem kötelező
                        Element customerRegion;

                        //kötelező
                        Element customerPostalCode = doc.createElement("postalCode");
                        detailedAddress.appendChild(customerPostalCode);
                        customerPostalCode.appendChild(doc.createTextNode(invoiceHead.getSupplierInfo().getAddress().getPostalCode()));

                        //kötelező
                        Element customerCity = doc.createElement("city");
                        detailedAddress.appendChild(customerCity);
                        customerCity.appendChild(doc.createTextNode(invoiceHead.getSupplierInfo().getAddress().getCity()));

                        //kötelező
                        Element customerStreetName = doc.createElement("streetName");
                        detailedAddress.appendChild(customerStreetName);
                        customerStreetName.appendChild(doc.createTextNode(invoiceHead.getSupplierInfo().getAddress().getStreetName()));

                        //kötelező
                        Element customerPublicPlaceCategory = doc.createElement("publicPlaceCategory");
                        detailedAddress.appendChild(customerPublicPlaceCategory);
                        customerPublicPlaceCategory.appendChild(doc.createTextNode(invoiceHead.getSupplierInfo().getAddress().getPublicPlaceCategory()));

                        //nem kötelező
                        Element customerNumber;

                        //nem kötelező
                        Element customerBuilding;

                        //nem kötelező
                        Element customerStaircase;

                        //nem kötelező
                        Element customerFloor;

                        //nem kötelező
                        Element customerDoor;

                        //nem kötelező
                        Element customerLotNumber;
                }
                
                //nem kötelező
                Element customerBankAccountnumber;
            
            //nem kötelező
            Element fiscalRepresentativeInfo;
            
            //kötelező
            Element invoiceData = doc.createElement("invoiceData");
            invoiceHeadElement.appendChild(invoiceData);
            
                //kötelező
                Element invoiceNumber = doc.createElement("invoiceNumber");
                invoiceData.appendChild(invoiceNumber);
                invoiceNumber.appendChild(doc.createTextNode(invoiceHead.getInvoiceData().getInvoiceNumber()));
                
                Element invoiceCategory = doc.createElement("invoiceCategory");
                invoiceData.appendChild(invoiceCategory);
                invoiceCategory.appendChild(doc.createTextNode(invoiceHead.getInvoiceData().getInvoiceCategory()));
                
                //CREATE operációban kötelező
                Element invoiceIssueDate = doc.createElement("invoiceIssueDate");
                invoiceData.appendChild(invoiceIssueDate);
                invoiceIssueDate.appendChild(doc.createTextNode(invoiceHead.getInvoiceData().getInvoiceIssueDate()));
                
                //CREATE operációban kötelező
                Element invoiceDeliveryDate = doc.createElement("invoiceDeliveryDate");
                invoiceData.appendChild(invoiceDeliveryDate);
                invoiceDeliveryDate.appendChild(doc.createTextNode(invoiceHead.getInvoiceData().getInvoiceDeliveryDate()));
                
                //nem kötelező
                Element invoiceDeliveryPeriodStart;
                
                //nem kötelező
                Element invoiceDeliveryPeriodEnd;
                
                //nem kötelező
                Element invoiceAccountingDeliveryDate;
                
                //kötelező
                Element currencyCode = doc.createElement("currencyCode");
                invoiceData.appendChild(currencyCode);
                currencyCode.appendChild(doc.createTextNode(invoiceHead.getInvoiceData().getCurrencyCode()));
                
                //kötelező
                Element exchangeRate = doc.createElement("exchangeRate");
                invoiceData.appendChild(exchangeRate);
                exchangeRate.appendChild(doc.createTextNode(invoiceHead.getInvoiceData().getExchangeRateStr()));
                
                //nem kötelező
                Element selfBillingIndicator;
                
                //nem kötelező
                Element paymentMethod;
                
                //nem kötelező
                Element paymentDate;
                
                //nem kötelező
                Element cashAccountingIndicator;
                
                //kötelező
                Element invoiceAppearance = doc.createElement("invoiceAppearance");
                invoiceData.appendChild(invoiceAppearance);
                invoiceAppearance.appendChild(doc.createTextNode(invoiceHead.getInvoiceData().getInvoiceAppearance()));
                
                //nem kötelező
                Element electronicInvoiceHash;
                
                //nem kötelező
                Element additionalInvoiceData;
    }
    
    private void CreateInvoiceLinesType(Element parent, ArrayList<LineType> lines)
    {
        //nem kötelező
        Element invoiceLines = doc.createElement("invoiceLines");
        parent.appendChild(invoiceLines);
        
        for(int i = 0; i < lines.size(); i++)
        {
            LineType line = lines.get(i);
            
            //kötelező
            Element lineElement = doc.createElement("line");
            invoiceLines.appendChild(lineElement);
            
                //kötelező
                Element lineNumber = doc.createElement("lineNumber");
                lineElement.appendChild(lineNumber);
                lineNumber.appendChild(doc.createTextNode(String.valueOf(i + 1)));
                
            if(line.getLineModificationReference() != null)
            {
                //módosítás esetén kötelező
                    Element lineModificationReference = doc.createElement("lineModificationReference");
                    lineElement.appendChild(lineModificationReference);
                    /*lineNumberReference           kötelező*/
                        Element lineNumberReference = doc.createElement("lineNumberReference");
                        lineModificationReference.appendChild(lineNumberReference);
                        lineNumberReference.appendChild(doc.createTextNode(line.getLineModificationReference().getLineNumberReferenceStr()));
                    /*lineOperation                 kötelező*/
                        Element lineOperation = doc.createElement("lineOperation");
                        lineModificationReference.appendChild(lineOperation);
                        lineOperation.appendChild(doc.createTextNode(line.getLineModificationReference().getLineOperation()));
            }
                
                //nem kötelező
                Element referencesToOtherLines;
                
                //nem kötelező
                Element advanceIndicator;
                
                //nem kötelező
                Element productCodes;
                
                //nem kötelező
                Element lineExpressionIndicator = doc.createElement("lineExpressionIndicator");
                lineElement.appendChild(lineExpressionIndicator);
                lineExpressionIndicator.appendChild(doc.createTextNode(line.getLineExpressionIndicator()));
                
                //kötelező
                Element lineDescription = doc.createElement("lineDescription");
                lineElement.appendChild(lineDescription);
                lineDescription.appendChild(doc.createTextNode(line.getLineDescription()));
                
            if(line.isLineExpressionIndicator())
            {
                //kötelező ha a lineExpressionIndicator értéke true
                Element quantity = doc.createElement("quantity");
                lineElement.appendChild(quantity);
                quantity.appendChild(doc.createTextNode(line.getQuantityStr()));
                
                //kötelező ha a lineExpressionIndicator értéke true
                Element unitOfMeasure = doc.createElement("unitOfMeasure");
                lineElement.appendChild(unitOfMeasure);
                unitOfMeasure.appendChild(doc.createTextNode(line.getUnitOfMeasure()));
            
                if(!line.getUnitOfMeasureOwn().isEmpty())
                {
                    //kötelező saját mennyiségi egység esetén
                    Element unitOfMeasureOwn = doc.createElement("unitOfMeasureOwn");
                    lineElement.appendChild(unitOfMeasureOwn);
                    unitOfMeasureOwn.appendChild(doc.createTextNode(line.getUnitOfMeasureOwn()));
                }
            
                //kötelező ha a lineExpressionIndicator értéke true
                Element unitPrice = doc.createElement("unitPrice");
                lineElement.appendChild(unitPrice);
                unitPrice.appendChild(doc.createTextNode(line.getUnitPriceStr()));
            }
            
                //nem kötelező
                Element lineDiscountData;
                
                //normál számla esetén
                Element lineAmountsNormal = doc.createElement("lineAmountsNormal");
                lineElement.appendChild(lineAmountsNormal);
                
                    //kötelező
                    Element lineNetAmount = doc.createElement("lineNetAmount");
                    lineAmountsNormal.appendChild(lineNetAmount);
                    lineNetAmount.appendChild(doc.createTextNode(line.getLineAmountsNormal().getLineNetAmountStr()));
                    
                    //kötelező
                    Element lineVatRate = doc.createElement("lineVatRate");
                    lineAmountsNormal.appendChild(lineVatRate);
                        
                        VatRateType vatRate = line.getLineAmountsNormal().getLineVatRate();
                        
                        Element vatRateType = null;
                        
                        switch(vatRate.getType())
                        {
                            case VAT_PERCENTEGE:
                                vatRateType = doc.createElement("vatPercentage");
                                break;
                            case VAT_EXEMPTION:
                                vatRateType = doc.createElement("vatExemption");
                                break;
                            case VAT_OUT_OF_SCOPE:
                                vatRateType = doc.createElement("vatOutOfScope");
                                break;
                            case VAT_DOMESTIC_REVERSE_CHARGE:
                                vatRateType = doc.createElement("vatDomesticReverseCharge");
                                break;
                            case MARGIN_SCHEME_VAT:
                                vatRateType = doc.createElement("marginSchemeVat");
                                break;
                            case MARGIN_SCHEME_NO_VAT:
                                vatRateType = doc.createElement("marginSchemeNoVat");
                                break;
                        }

                        lineVatRate.appendChild(vatRateType);
                        vatRateType.appendChild(doc.createTextNode(vatRate.toString()));
                    
                    //nem kötelező
                    Element lineVatAmount;
                    
                    //nem kötelező
                    Element lineVatAmountHUF;
                    
                    //nem kötelező
                    Element lineGrossAmountNormal = doc.createElement("lineGrossAmountNormal");
                    lineAmountsNormal.appendChild(lineGrossAmountNormal);
                    lineGrossAmountNormal.appendChild(doc.createTextNode(line.getLineAmountsNormal().getLineGrossAmountNormalStr()));
                
                //egyszerűsített számla esetén
                Element lineAmountsSimplified;
                
                //nem kötelező
                Element intermediatedService;
                
                //nem kötelező
                Element aggregateInvoiceLineData;
                
                //nem kötelező
                Element newTransportMean;
                
                //nem kötelező
                Element depositIndicator;
                
                //nem kötelező
                Element marginSchemeIndicator;
                
                //nem kötelező
                Element ekaerIds;
                
            if(line.isObligatedForProductFee())
            {
                //termékdíjas számla esetén kötelező
                Element obligatedForProductFee = doc.createElement("obligatedForProductFee");
                lineElement.appendChild(obligatedForProductFee);
                obligatedForProductFee.appendChild(doc.createTextNode(line.getObligatedForProductFee()));
            }
                
                //nem kötelező
                Element GPCExcise;
                
                //nem kötelező
                Element dieselOilPurchase;
                
                //nem kötelező
                Element netaDeclaration;
                
            if(line.isObligatedForProductFee())
            {
                ProductFeeTakeoverDataType pftdt = line.getProductFeeClause().getProductFeeTakeoverData();
                
                if(!pftdt.getTakeoverReason().isEmpty())
                {
                    //nem kötelező
                    Element productFeeClause = doc.createElement("productFeeClause");
                    lineElement.appendChild(productFeeClause);
                        
                        //kötelező
                        Element productFeeTakeoverData = doc.createElement("productFeeTakeoverData");
                        productFeeClause.appendChild(productFeeTakeoverData);

                            //kötelező
                            Element takeoverReason = doc.createElement("takeoverReason");
                            productFeeTakeoverData.appendChild(takeoverReason);
                            takeoverReason.appendChild(doc.createTextNode(pftdt.getTakeoverReason()));

                            //nem kötelező
                            Element takeoverAmount = doc.createElement("takeoverAmount");
                            productFeeTakeoverData.appendChild(takeoverAmount);
                            takeoverAmount.appendChild(doc.createTextNode(pftdt.getTakeoverAmountStr()));
                }
                else if(line.getProductFeeClause().getCustomerDeclaration() != null)
                {
                    //ezt mi nem használjuk
                    //kötelező
                    Element customerDeclaration = doc.createElement("customerDeclaration");
                    
                        //kötelező
                        Element productStream = doc.createElement("productStream");
                        
                        //nem kötelező
                        Element productFeeWeight;
                }
            }
            
            if(line.isObligatedForProductFee())
            {
                ProductFeeDataType lineProductFeeContent = line.getLineProductFeeContent();
                
                //nem kötelező
                Element lineProductFeeContentElement = doc.createElement("lineProductFeeContent");
                lineElement.appendChild(lineProductFeeContentElement);
                
                    //kötelező
                    Element productFeeCode = doc.createElement("productFeeCode");
                    lineProductFeeContentElement.appendChild(productFeeCode);
                    
                        //kötelező
                        Element productCodeCategory = doc.createElement("productCodeCategory");
                        productFeeCode.appendChild(productCodeCategory);
                        productCodeCategory.appendChild(doc.createTextNode(lineProductFeeContent.getProductFeeCode().getProductCodeCategory()));
                        
                    if(lineProductFeeContent.getProductFeeCode().getType() != ProductCodeType.Type.OWN)
                    {
                        //kötelező
                        Element productCodeValue = doc.createElement("productCodeValue");
                        productFeeCode.appendChild(productCodeValue);
                        productCodeValue.appendChild(doc.createTextNode(lineProductFeeContent.getProductFeeCode().getProductCodeValue().replaceAll(" ", "")));
                    }
                    else
                    {
                        //kötelező
                        Element productCodeOwnValue = doc.createElement("productCodeOwnValue");
                        productFeeCode.appendChild(productCodeOwnValue);
                        productCodeOwnValue.appendChild(doc.createTextNode(lineProductFeeContent.getProductFeeCode().getProductCodeOwnValue().replaceAll(" ", "")));
                    }
                    
                    //kötelező
                    Element productFeeQuantity = doc.createElement("productFeeQuantity");
                    lineProductFeeContentElement.appendChild(productFeeQuantity);
                    productFeeQuantity.appendChild(doc.createTextNode(lineProductFeeContent.getProductFeeQuantityStr()));
                    
                    //kötelező
                    Element productFeeMeasuringUnit = doc.createElement("productFeeMeasuringUnit");
                    lineProductFeeContentElement.appendChild(productFeeMeasuringUnit);
                    productFeeMeasuringUnit.appendChild(doc.createTextNode(lineProductFeeContent.getProductFeeMeasuringUnit()));
                    
                    //kötelező
                    Element productFeeRate = doc.createElement("productFeeRate");
                    lineProductFeeContentElement.appendChild(productFeeRate);
                    productFeeRate.appendChild(doc.createTextNode(lineProductFeeContent.getProductFeeRateStr()));
                    
                    //kötelező
                    Element productFeeAmount = doc.createElement("productFeeAmount");
                    lineProductFeeContentElement.appendChild(productFeeAmount);
                    productFeeAmount.appendChild(doc.createTextNode(lineProductFeeContent.getProductFeeAmountStr()));
            }
                
                //nem kötelező
                Element additionalLineData;
        }
    }
    
    private void CreateProductFeeSummaryType(Element parent, ProductFeeSummaryType summary)
    {
        
        //nem kötelező
        Element productFeeSummary = doc.createElement("productFeeSummary");
        parent.appendChild(productFeeSummary);
        
            //kötelező
            Element productFeeOperation = doc.createElement("productFeeOperation");
            productFeeSummary.appendChild(productFeeOperation);
            productFeeOperation.appendChild(doc.createTextNode(summary.getProductfeeOperation()));
            
        for(int i = 0; i < summary.getProductFeeData().size(); i++)
        {
            ProductFeeDataType productFeeData = summary.getProductFeeData().get(i);
            
            //kötelező
            Element productFeeDataElement = doc.createElement("productFeeData");
            productFeeSummary.appendChild(productFeeDataElement);
            
                //kötelező
                Element productFeeCode = doc.createElement("productFeeCode");
                productFeeDataElement.appendChild(productFeeCode);
                
                    //kötelező
                    Element productCodeCategory = doc.createElement("productCodeCategory");
                    productFeeCode.appendChild(productCodeCategory);
                    productCodeCategory.appendChild(doc.createTextNode(productFeeData.getProductFeeCode().getProductCodeCategory()));
                    
                if(!productFeeData.getProductFeeCode().getProductCodeValue().isEmpty())
                {
                    //kötelező
                    Element productCodeValue = doc.createElement("productCodeValue");
                    productFeeCode.appendChild(productCodeValue);
                    productCodeValue.appendChild(doc.createTextNode(productFeeData.getProductFeeCode().getProductCodeValue()));
                }
                else
                {
                    //kötelező
                    Element productCodeOwnValue = doc.createElement("productCodeOwnValue");
                    productFeeCode.appendChild(productCodeOwnValue);
                    productCodeOwnValue.appendChild(doc.createTextNode(productFeeData.getProductFeeCode().getProductCodeOwnValue()));
                }
                
                //kötelező
                Element productFeeQuantity = doc.createElement("productFeeQuantity");
                productFeeDataElement.appendChild(productFeeQuantity);
                productFeeQuantity.appendChild(doc.createTextNode(productFeeData.getProductFeeQuantityStr()));
                
                //kötelező
                Element productFeeMeasuringUnit = doc.createElement("productFeeMeasuringUnit");
                productFeeDataElement.appendChild(productFeeMeasuringUnit);
                productFeeMeasuringUnit.appendChild(doc.createTextNode(productFeeData.getProductFeeMeasuringUnit()));
                
                //kötelező
                Element productFeeRate = doc.createElement("productFeeRate");
                productFeeDataElement.appendChild(productFeeRate);
                productFeeRate.appendChild(doc.createTextNode(productFeeData.getProductFeeRateStr()));
                
                //kötelező
                Element productFeeAmount = doc.createElement("productFeeAmount");
                productFeeDataElement.appendChild(productFeeAmount);
                productFeeAmount.appendChild(doc.createTextNode(productFeeData.getProductFeeAmountStr()));
        }
        
            //kötelező
            Element productChargeSum = doc.createElement("productChargeSum");
            productFeeSummary.appendChild(productChargeSum);
            productChargeSum.appendChild(doc.createTextNode(summary.getProductChargeSumStr()));
            
            //nem kötelező
            Element paymentEvidenceDocumentData;
    }
    
    private void CreateInvoiceSummaryType(Element parent, SummaryType summary)
    {
        //kötelező
        Element invoiceSummary = doc.createElement("invoiceSummary");
        parent.appendChild(invoiceSummary);
        
        if(summary.getType() == SummaryType.Type.NORMAL)
        {
            //kötelező
            Element summaryNormal = doc.createElement("summaryNormal");
            invoiceSummary.appendChild(summaryNormal);
            
            for(int i = 0; i < summary.getSummaryNormal().getSummaryByVatRate().size(); i++)
            {
                //kötelező
                Element summaryByVatRate = doc.createElement("summaryByVatRate");
                summaryNormal.appendChild(summaryByVatRate);
                
                    SummaryByVatRateType vatRate = summary.getSummaryNormal().getSummaryByVatRate().get(i);
                    
                    //kötelező
                    Element vatRateElement = doc.createElement("vatRate");
                    summaryByVatRate.appendChild(vatRateElement);
                    
                        Element vatRateType = null;
                    
                    switch(vatRate.getVatRate().getType())
                    {
                        case VAT_PERCENTEGE:
                            vatRateType = doc.createElement("vatPercentage");
                            break;
                        case VAT_EXEMPTION:
                            vatRateType = doc.createElement("vatExemption");
                            break;
                        case VAT_OUT_OF_SCOPE:
                            vatRateType = doc.createElement("vatOutOfScope");
                            break;
                        case VAT_DOMESTIC_REVERSE_CHARGE:
                            vatRateType = doc.createElement("vatDomesticReverseCharge");
                            break;
                        case MARGIN_SCHEME_VAT:
                            vatRateType = doc.createElement("marginSchemeVat");
                            break;
                        case MARGIN_SCHEME_NO_VAT:
                            vatRateType = doc.createElement("marginSchemeNoVat");
                            break;
                    }
                    
                        vatRateElement.appendChild(vatRateType);
                        vatRateType.appendChild(doc.createTextNode(vatRate.getVatRate().toString()));
                    
                    //kötelező
                    Element vatRateNetAmount = doc.createElement("vatRateNetAmount");
                    summaryByVatRate.appendChild(vatRateNetAmount);
                    //vatRateNetAmount.appendChild(doc.createTextNode(vatRate.getVatRateNetAmountStr()));
                    vatRateNetAmount.appendChild(doc.createTextNode(String.format (java.util.Locale.US,"%.2f", vatRate.getVatRateNetAmount())));
                    
                    //kötelező
                    Element vatRateVatAmount = doc.createElement("vatRateVatAmount");
                    summaryByVatRate.appendChild(vatRateVatAmount);
                    vatRateVatAmount.appendChild(doc.createTextNode(vatRate.getVatRateVatAmountStr()));
                    
                    //nem kötelező
                    Element vatRateVatAmountHUF;
                    
                    //nem kötelező
                    Element vatRateGrossAmount = doc.createElement("vatRateGrossAmount");
                    summaryByVatRate.appendChild(vatRateGrossAmount);
                    vatRateGrossAmount.appendChild(doc.createTextNode(vatRate.getVatRateGrossAmountStr()));
            }
                
                //kötelező
                Element invoiceNetAmount = doc.createElement("invoiceNetAmount");
                summaryNormal.appendChild(invoiceNetAmount);
                //invoiceNetAmount.appendChild(doc.createTextNode(summary.getSummaryNormal().getInvoiceNetAmountStr()));
                double invoiceNetAmountDouble = summary.getSummaryNormal().getInvoiceNetAmount();
                invoiceNetAmount.appendChild(doc.createTextNode(String.format (java.util.Locale.US,"%.2f", invoiceNetAmountDouble)));
                
                //kötelező
                Element invoiceVatAmount = doc.createElement("invoiceVatAmount");
                summaryNormal.appendChild(invoiceVatAmount);
                invoiceVatAmount.appendChild(doc.createTextNode(summary.getSummaryNormal().getInvoiceVatAmountStr()));
                
                //kötelező
                Element invoiceVatAmountHUF = doc.createElement("invoiceVatAmountHUF");
                summaryNormal.appendChild(invoiceVatAmountHUF);
                invoiceVatAmountHUF.appendChild(doc.createTextNode(summary.getSummaryNormal().getInvoiceVatAmountHUFStr()));
        }
        else
        {
            for(int i = 0; i < summary.getSummarySimplified().size(); i++)
            {
                //kötelező
                Element summarySimplified;

                    //kötelező
                    Element vatContent;

                    //kötelező
                    Element vatContentGrossAmount;
            }
        }
    }
    
    public void CreateBasicRequestType(BasicHeaderType header, UserHeaderType user, SoftwareType software)
    {
        Element serviceElement = doc.createElement(service);
        doc.appendChild(serviceElement);

        Attr attr = doc.createAttribute("xmlns");
        attr.setValue("http://schemas.nav.gov.hu/OSA/2.0/api");
        serviceElement.setAttributeNode(attr);
        
        CreateBasicHeaderType(header, serviceElement);
        CreateUserHeaderType(user, serviceElement);
        CreateSoftwareType(software, serviceElement);
    }
    
    private void CreateBasicHeaderType(BasicHeaderType header, Element serviceElement)
    {
        Element headerElement = doc.createElement("header");
        serviceElement.appendChild(headerElement);
                                       
        Element requestId = doc.createElement("requestId");
        requestId.appendChild(doc.createTextNode(header.requestId));
        headerElement.appendChild(requestId);
                
        Element timeStamp = doc.createElement("timestamp");
        timeStamp.appendChild(doc.createTextNode(header.timestamp));
        headerElement.appendChild(timeStamp);
                
        Element requestVersion = doc.createElement("requestVersion");
        requestVersion.appendChild(doc.createTextNode(header.requestVersion));
        headerElement.appendChild(requestVersion);
                
        Element headerVersion = doc.createElement("headerVersion");
        headerVersion.appendChild(doc.createTextNode(header.headerVersion));
        headerElement.appendChild(headerVersion);
        
        //header.Print();
    }
    
    private void CreateUserHeaderType(UserHeaderType user, Element serviceElement)
    {
        Element userElement = doc.createElement("user");
        serviceElement.appendChild(userElement);
            
        Element login = doc.createElement("login");
        login.appendChild(doc.createTextNode(user.login));
        userElement.appendChild(login);
                
        Element passordHash = doc.createElement("passwordHash");
        passordHash.appendChild(doc.createTextNode(user.passwordHash));
        userElement.appendChild(passordHash);
                
        Element taxNumber = doc.createElement("taxNumber");
        taxNumber.appendChild(doc.createTextNode(user.taxNumber));
        userElement.appendChild(taxNumber);
                
        Element requestSignature = doc.createElement("requestSignature");
        requestSignature.appendChild(doc.createTextNode(user.requestSignature));
        userElement.appendChild(requestSignature);
        
        //user.Print();
    }
    
    private void CreateSoftwareType(SoftwareType software, Element serviceElement)
    {
        Element softwareElement = doc.createElement("software");
        serviceElement.appendChild(softwareElement);
            
        Element softwareId = doc.createElement("softwareId");
        softwareId.appendChild(doc.createTextNode(software.softwareId));
        softwareElement.appendChild(softwareId);
                
        Element softwareName = doc.createElement("softwareName");
        softwareName.appendChild(doc.createTextNode(software.softwareName));
        softwareElement.appendChild(softwareName);
                
        Element softwareOperation = doc.createElement("softwareOperation");
        softwareOperation.appendChild(doc.createTextNode(software.softwareOperation));
        softwareElement.appendChild(softwareOperation);
                
        Element softwareMainVersion = doc.createElement("softwareMainVersion");
        softwareMainVersion.appendChild(doc.createTextNode(software.softwareMainVersion));
        softwareElement.appendChild(softwareMainVersion);
                
        Element softwareDevName = doc.createElement("softwareDevName");
        softwareDevName.appendChild(doc.createTextNode(software.softwareDevName));
        softwareElement.appendChild(softwareDevName);
                
        Element softwareDevContact = doc.createElement("softwareDevContact");
        softwareDevContact.appendChild(doc.createTextNode(software.softwareDevContact));
        softwareElement.appendChild(softwareDevContact);
                
        Element softwareDevCountryCode = doc.createElement("softwareDevCountryCode");
        softwareDevCountryCode.appendChild(doc.createTextNode(software.softwareDevCountryCode));
        softwareElement.appendChild(softwareDevCountryCode);
                
        Element softwareDevTaxNumber = doc.createElement("softwareDevTaxNumber");
        softwareDevTaxNumber.appendChild(doc.createTextNode(software.softwareDevTaxNumber));
        softwareElement.appendChild(softwareDevTaxNumber);
        
        //software.Print();
    }
    
    private void CreateExchangeToken(Element serviceElement, String exchangeToken)
    {
        Element exchangeTokenElement = doc.createElement("exchangeToken");
        exchangeTokenElement.appendChild(doc.createTextNode(exchangeToken));
        serviceElement.appendChild(exchangeTokenElement);
    }
    
    private void CreateAnnulmentOperations(Element parent, AnnulmentOperationsType operations)
    {
        //kötelező
        Element annulmentOperations = doc.createElement("annulmentOperations");
        parent.appendChild(annulmentOperations);
            
        for(int i = 0; i < operations.getOperations().size(); i++)
        {
            AnnulmentOperationType operationType = operations.getOperations().get(i);
            
            //kötelező
            Element annulmentOperation = doc.createElement("annulmentOperation");
            annulmentOperation.appendChild(annulmentOperation);
            
                //kötelező
                Element index = doc.createElement("index");
                annulmentOperation.appendChild(index);
                index.appendChild(doc.createTextNode((i + 1) + ""));
                
                //kötelező
                Element operation = doc.createElement("annulmentOperation");
                annulmentOperation.appendChild(operation);
                operation.appendChild(doc.createTextNode(operationType.getAnnulmentOperation()));
                
                //kötelező
                Element invoice = doc.createElement("invoiceAnnulment");
                annulmentOperation.appendChild(invoice);
                invoice.appendChild(doc.createTextNode(operationType.getInvoiceAnnulment()));
        }
    }
    
    private void CreateInvoiceOperations(Element parent, InvoiceOperationsType operations)
    {
        //kötelező
        Element invoiceOperations = doc.createElement("invoiceOperations");
        parent.appendChild(invoiceOperations);

            //kötelező
            Element compressedContent = doc.createElement("compressedContent");
            invoiceOperations.appendChild(compressedContent);
            compressedContent.appendChild(doc.createTextNode(operations.getCompressedContent()));
            
        for(int i = 0; i < operations.getOperations().size(); i++)
        {
            InvoiceOperationType operationType = operations.getOperations().get(i);
            
            //kötelező
            Element invoiceOperation = doc.createElement("invoiceOperation");
            invoiceOperations.appendChild(invoiceOperation);
            
                //kötelező
                Element index = doc.createElement("index");
                invoiceOperation.appendChild(index);
                index.appendChild(doc.createTextNode((i + 1) + ""));
                
                //kötelező
                Element operation = doc.createElement("invoiceOperation");
                invoiceOperation.appendChild(operation);
                operation.appendChild(doc.createTextNode(operationType.getInvoiceOperation()));
                
                //kötelező
                Element invoice = doc.createElement("invoiceData");
                invoiceOperation.appendChild(invoice);
                invoice.appendChild(doc.createTextNode(operationType.getInvoiceData()));
        }
    }
    
    private void CreateInvoiceNumberQuery(Element parent, InvoiceNumberQueryType invoiceNumberQuery)
    {
        //kötelező
        Element invoiceNumberQueryElement = doc.createElement("invoiceNumberQuery");
        parent.appendChild(invoiceNumberQueryElement);
        
            //kötelező
            Element invoiceNumber = doc.createElement("invoiceNumber");
            invoiceNumberQueryElement.appendChild(invoiceNumber);
            invoiceNumber.appendChild(doc.createTextNode(invoiceNumberQuery.getInvoiceNumber()));

            //kötelező
            Element invoiceDirection = doc.createElement("invoiceDirection");
            invoiceNumberQueryElement.appendChild(invoiceDirection);
            invoiceDirection.appendChild(doc.createTextNode(invoiceNumberQuery.getInvoiceDirection()));

            //nem kötelező
            Element batchIndex = doc.createElement("batchIndex");
            //invoiceNumberQueryElement.appendChild(batchIndex);
            //batchIndex.appendChild(doc.createTextNode(invoiceNumberQuery.getBatchIndex() + ""));
            
            //nem kötelező
            Element supplierTaxNumber = doc.createElement("supplierTaxNumber");
            //invoiceNumberQueryElement.appendChild(supplierTaxNumber);
            //supplierTaxNumber.appendChild(doc.createTextNode(invoiceNumberQuery.getSupplierTaxNumber()));
    }
    
    private void CreatePage(Element parent, int page)
    {
        //kötelező
        Element pageElement = doc.createElement("page");
        parent.appendChild(pageElement);
        pageElement.appendChild(doc.createTextNode(page + ""));
    }
    
    private void CreateInvoiceDirection(Element parent, String invoiceDirection)
    {
        //kötelező
        Element invoiceDir = doc.createElement("invoiceDirection");
        parent.appendChild(invoiceDir);
        invoiceDir.appendChild(doc.createTextNode(invoiceDirection));
    }
    
    /*private void CreateInvoiceQueryParams(Element parent, InvoiceQueryParamsType invoiceQueryPARAMS)
    {
        //kötelező
        Element invoiceQueryParams = doc.createElement("invoiceQueryParams");
        parent.appendChild(invoiceQueryParams);
        
            //kötelező
            Element mandatoryQueryParams = doc.createElement("mandatoryQueryParams");
            invoiceQueryParams.appendChild(mandatoryQueryParams);
            
                MandatoryQueryParamsType mandatory = invoiceQueryPARAMS.getMandatoryQueryParams();
            
                switch(invoiceQueryPARAMS.getType())
                {
                    case invoiceIssueDate:
                        DateIntervalParamType invoiceIssueDate = mandatory.getInvoiceIssueDate();
                        CreateInvoiceIssueDate(mandatoryQueryParams, invoiceIssueDate.getDateFrom(), invoiceIssueDate.getDateTo());
                        break;
                    case insDate:
                        DateTimeIntervalParamType insDate = mandatory.getInsDate();
                        CreateInsDate(mandatoryQueryParams, insDate.getDateTimeFrom(), insDate.getDateTimeTo());
                        break;
                    case originalInvoiceNumber:
                        OriginalInvoiceNumberQueryType original = mandatory.getOriginalInvoiceNumber();
                        CreateOriginalInvoiceNumber(mandatoryQueryParams, original.getOriginalInvoiceNumber(), original.getSupplierTaxNumber());
                        break;
                }
            
        if(invoiceQueryPARAMS.getAdditionalQueryParams().isActive())
        {
            AdditionalQueryParamsType additional = invoiceQueryPARAMS.getAdditionalQueryParams();
            
            //nem kötelező
            Element additionalQueryParams = doc.createElement("additionalQueryParams");
            invoiceQueryParams.appendChild(additionalQueryParams);
            
                //nem kötelező
                Element groupMemberTaxNumber = doc.createElement("groupMemberTaxNumber");
                //additionalQueryParams.appendChild(groupMemberTaxNumber);
                //groupMemberTaxNumber.appendChild(doc.createTextNode(additional.getGroupMemberTaxNumber()));
                
                //nem köütelező
                Element name = doc.createElement("name");
                //additionalQueryParams.appendChild(name);
                //name.appendChild(doc.createTextNode(additional.getName()));
                
                //nem kötelező
                Element invoiceCategory = doc.createElement("invoiceCategory");
                //additionalQueryParams.appendChild(invoiceCategory);
                //invoiceCategory.appendChild(doc.createTextNode(additional.getInvoiceCategory()));
                
                //nem kötelező
                Element paymentMethod = doc.createElement("paymentMethod");
                //additionalQueryParams.appendChild(paymentMethod);
                //paymentMethod.appendChild(doc.createTextNode(additional.getPaymentMethod()));
                
                //nem kötelező
                Element invoiceAppereance = doc.createElement("invoiceappearance");
                //additionalQueryParams.appendChild(invoiceAppereance);
                //invoiceAppereance.appendChild(doc.createTextNode(additional.getInvoiceAppearance()));
                
                //nem kötelező
                Element source = doc.createElement("source");
                //additionalQueryParams.appendChild(source);
                //source.appendChild(doc.createTextNode(additional.getSource()));
                
                //nem kötelező
                Element currency = doc.createElement("currency");
                //additionalQueryParams.appendChild(currency);
                //currency.appendChild(doc.createTextNode(additional.getCurrency()));
        }
            
        if(invoiceQueryPARAMS.getRelationalQueryParams().isActive())
        {
            RelationalQueryParamsType relational = invoiceQueryPARAMS.getRelationalQueryParams();
            
            //nem kötelező
            Element relationalQueryParams = doc.createElement("relationalQueryParams");
            invoiceQueryParams.appendChild(relationalQueryParams);
            
            if(relational.getInvoiceDeliveries().isActive())
            {
                RelationQueryDateType [] invoiceDeliveries = relational.getInvoiceDeliveries();
                
                for(int i = 0; i < invoiceDeliveries.length; i++)
                {
                    //nem kötelező
                    Element invoiceDelivery = doc.createElement("invoiceDelivery");
                    relationalQueryParams.appendChild(invoiceDelivery);
                    
                        //kötelező
                        Element queryOperator = doc.createElement("queryOperator");
                        invoiceDelivery.appendChild(queryOperator);
                        queryOperator.appendChild(doc.createTextNode(invoiceDeliveries[i].getQueryOperator()));
                        
                        //kötelező
                        Element queryValue = doc.createElement("queryValue");
                        invoiceDelivery.appendChild(queryValue);
                        queryValue.appendChild(doc.createTextNode(invoiceDeliveries[i].getQueryValue()));
                }
            }

            if(relational.getPaymentDates().isActive())
            {
                RelationQueryDateType [] paymentDates = relational.getPaymentDates();
                
                for(int i = 0; i < paymentDates.length; i++)
                {
                    //nem kötelező
                    Element paymentDate = doc.createElement("paymentDate");
                    relationalQueryParams.appendChild(paymentDate);
                    
                        //kötelező
                        Element queryOperator = doc.createElement("queryOperator");
                        paymentDate.appendChild(queryOperator);
                        queryOperator.appendChild(doc.createTextNode(paymentDates[i].getQueryOperator()));
                        
                        //kötelező
                        Element queryValue = doc.createElement("queryValue");
                        paymentDate.appendChild(queryValue);
                        queryValue.appendChild(doc.createTextNode(paymentDates[i].getQueryValue()));
                }
            }
            
            if(relational.getInvoiceNetAmounts().isActive())
            {
                RelationQueryMonetaryType [] invoiceNetAmounts = relational.getInvoiceNetAmounts();
                
                for(int i = 0; i < invoiceNetAmounts.length; i++)
                {
                    //nem kötelező
                    Element invoiceNetAmount = doc.createElement("invoiceNetAmount");
                    relationalQueryParams.appendChild(invoiceNetAmount);
                    
                        //kötelező
                        Element queryOperator = doc.createElement("queryOperator");
                        invoiceNetAmount.appendChild(queryOperator);
                        queryOperator.appendChild(doc.createTextNode(invoiceNetAmounts[i].getQueryOperator()));
                        
                        //kötelező
                        Element queryValue = doc.createElement("queryValue");
                        invoiceNetAmount.appendChild(queryValue);
                        queryValue.appendChild(doc.createTextNode(invoiceNetAmounts[i].getQueryValue() + ""));
                }
            }
            
            if(relational.getInvoiceNetAmountsHUF().isActive())
            {
                RelationQueryMonetaryType [] invoiceNetAmountsHUF = relational.getInvoiceNetAmountsHUF();
                
                for(int i = 0; i < invoiceNetAmountsHUF.length; i++)
                {
                    //nem kötelező
                    Element invoiceNetAmountHUF = doc.createElement("invoiceNetAmountHUF");
                    relationalQueryParams.appendChild(invoiceNetAmountHUF);
                    
                        //kötelező
                        Element queryOperator = doc.createElement("queryOperator");
                        invoiceNetAmountHUF.appendChild(queryOperator);
                        queryOperator.appendChild(doc.createTextNode(invoiceNetAmountsHUF[i].getQueryOperator()));
                        
                        //kötelező
                        Element queryValue = doc.createElement("queryValue");
                        invoiceNetAmountHUF.appendChild(queryValue);
                        queryValue.appendChild(doc.createTextNode(invoiceNetAmountsHUF[i].getQueryValue() + ""));
                }
            }
            
            if(relational.getInvoiceVatAmounts().isActive())
            {
                RelationQueryMonetaryType [] invoiceVatAmounts = relational.getInvoiceVatAmounts();
                
                for(int i = 0; i < invoiceVatAmounts.length; i++)
                {
                    //nem kötelező
                    Element invoiceVatAmount = doc.createElement("invoiceVatAmount");
                    relationalQueryParams.appendChild(invoiceVatAmount);
                    
                        //kötelező
                        Element queryOperator = doc.createElement("queryOperator");
                        invoiceVatAmount.appendChild(queryOperator);
                        queryOperator.appendChild(doc.createTextNode(invoiceVatAmounts[i].getQueryOperator()));
                        
                        //kötelező
                        Element queryValue = doc.createElement("queryValue");
                        invoiceVatAmount.appendChild(queryValue);
                        queryValue.appendChild(doc.createTextNode(invoiceVatAmounts[i].getQueryValue() + ""));
                }
            }
            
            if(relational.getInvoiceVatAmountsHUF().isActive())
            {
                RelationQueryMonetaryType [] invoiceVatAmountsHUF = relational.getInvoiceVatAmountsHUF();
                
                for(int i = 0; i < invoiceVatAmountsHUF.length; i++)
                {
                    //nem kötelező
                    Element invoiceVatAmountHUF = doc.createElement("invoiceVatAmountHUF");
                    relationalQueryParams.appendChild(invoiceVatAmountHUF);
                    
                        //kötelező
                        Element queryOperator = doc.createElement("queryOperator");
                        invoiceVatAmountHUF.appendChild(queryOperator);
                        queryOperator.appendChild(doc.createTextNode(invoiceVatAmountsHUF[i].getQueryOperator()));
                        
                        //kötelező
                        Element queryValue = doc.createElement("queryValue");
                        invoiceVatAmountHUF.appendChild(queryValue);
                        queryValue.appendChild(doc.createTextNode(invoiceVatAmountsHUF[i].getQueryValue() + ""));
                }
            }
        }
            
        if(invoiceQueryPARAMS.getTransactionQueryParams().isActive())
        {
            TransactionQueryParamsType transaction = invoiceQueryPARAMS.getTransactionQueryParams();
            
            //nem kötelező
            Element transactionQueryParams = doc.createElement("transactionQueryParams");
            invoiceQueryParams.appendChild(transactionQueryParams);
            
                //kötelező
                Element transactionId = doc.createElement("transactionId");
                transactionQueryParams.appendChild(transactionId);
                transactionId.appendChild(doc.createTextNode(transaction.getTransactionId()));

                //nem kötelező
                Element index = doc.createElement("index");
                transactionQueryParams.appendChild(index);
                index.appendChild(doc.createTextNode(transaction.getIndex() + ""));
                
                //nem kötelező
                Element invoiceOperation = doc.createElement("invoiceOperation");
                transactionQueryParams.appendChild(invoiceOperation);
                invoiceOperation.appendChild(doc.createTextNode(transaction.getInvoiceOperation()));
        }
    }*/
    
    private void CreateInvoiceIssueDate(Element parent, String from, String to)
    {
        //kötelező
        Element invoiceIssueDate = doc.createElement("invoiceissueDate");
        parent.appendChild(invoiceIssueDate);
        
            //kötelező
            Element dateFrom = doc.createElement("dateFrom");
            invoiceIssueDate.appendChild(dateFrom);
            dateFrom.appendChild(doc.createTextNode(from));
            
            //kötelező
            Element dateTo = doc.createElement("dateTo");
            invoiceIssueDate.appendChild(dateTo);
            dateFrom.appendChild(doc.createTextNode(to));
    }
    
    private void CreateInsDate(Element parent, String from, String to)
    {
        //kötelező
        Element insDate = doc.createElement("insDate");
        parent.appendChild(insDate);
        
            //kötelező
            Element dateFrom = doc.createElement("dateTimeFrom");
            insDate.appendChild(dateFrom);
            dateFrom.appendChild(doc.createTextNode(from));
            
            //kötelező
            Element dateTo = doc.createElement("dateTimeTo");
            insDate.appendChild(dateTo);
            dateFrom.appendChild(doc.createTextNode(to));
    }
    
    private void CreateOriginalInvoiceNumber(Element parent, String originalInvoiceNumber, String supplierTaxNUMBER)
    {
        //kötelező
        Element original = doc.createElement("originalInvoiceNumber");
        parent.appendChild(original);
        
            //kötelező
            Element originalNumber = doc.createElement("originalInvoiceNumber");
            original.appendChild(originalNumber);
            originalNumber.appendChild(doc.createTextNode(originalInvoiceNumber));
            
            //nem kötelező
            Element supplierTaxNumber = doc.createElement("supplierTaxNumber");
            //original.appendChild(supplierTaxNumber);
            //supplierTaxNumber.appendChild(doc.createTextNode(supplierTaxNUMBER));
    }
    
    private void CreateTransactionId(Element parent, String transactionID)
    {
        //kötelező
        Element transactionId = doc.createElement("transactionId");
        parent.appendChild(transactionId);
        transactionId.appendChild(doc.createTextNode(transactionID));
    }
    
    private void CreateReturnOriginalRequest(Element parent, String returnOriginalREQUEST)
    {
        //nem kötelező
        Element returnOrignalRequest = doc.createElement("returnOriginalRequest");
        parent.appendChild(returnOrignalRequest);
        returnOrignalRequest.appendChild(doc.createTextNode(returnOriginalREQUEST));
    }
    
    private void CreateTaxnumber(String taxNumber)
    {
        //kötelező
        Element taxNumberElement = doc.createElement("taxNumber");
        doc.getFirstChild().appendChild(taxNumberElement);
        taxNumberElement.appendChild(doc.createTextNode(taxNumber));
    }
    
    private String getStringFromDocument()
    {
        DOMSource domSource = new DOMSource(doc);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        TransformerFactory tf = TransformerFactory.newInstance();
        
        try
        {
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
            transformer.transform(domSource, result);
        }
        catch (TransformerException ex)
        {
            System.err.println("XmlBuilder.java/getStringFromDocument()");
        }
        
        return writer.toString();
    }
}