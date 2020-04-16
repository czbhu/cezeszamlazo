package nav;

import database.Query;
import invoice.Software;

/**
 * @author Tomy
 */
public class SoftwareType
{
    String softwareId;
    String softwareName;
    String softwareOperation;
    String softwareMainVersion;
    String softwareDevName;
    String softwareDevContact;
    String softwareDevCountryCode;
    String softwareDevTaxNumber;
    
    public SoftwareType()
    {
        Query query = new Query.QueryBuilder()
            .select("softwareId, softwareName, softwareOperation, softwareMainVersion, softwareDevName, softwareDevContact, softwareDevCountryCode, softwareTaxNumber")
            .from(Software.TABLE)
            .build();
        Object [][] softwareDatas = NAV.db.select(query.getQuery());
        
        softwareId = softwareDatas[0][0].toString();
        softwareName = softwareDatas[0][1].toString();
        softwareOperation = softwareDatas[0][2].toString();
        softwareMainVersion = softwareDatas[0][3].toString();
        softwareDevName = softwareDatas[0][4].toString();
        softwareDevContact = softwareDatas[0][5].toString();
        softwareDevCountryCode = softwareDatas[0][6].toString();
        softwareDevTaxNumber = softwareDatas[0][7].toString();
    }
    
    public void Print()
    {
        System.err.println("SoftwareType");
        System.err.println("    softwareId: " + softwareId);
        System.err.println("    softwareName: " + softwareName);
        System.err.println("    softwareOperation: " + softwareOperation);
        System.err.println("    softwareMainVersion: " + softwareMainVersion);
        System.err.println("    softwareDevName: " + softwareDevName);
        System.err.println("    softwareDevContact: " + softwareDevContact);
        System.err.println("    softwareDevCountryCode: " + softwareDevCountryCode);
        System.err.println("    softwareDevTaxNumber: " + softwareDevTaxNumber);
    }
}