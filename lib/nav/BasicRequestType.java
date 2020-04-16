package nav;

/**
 * @author Tomy
 */

/**
 * Minden request element kötelező része a BasicRequestType. 
 * A típuson belül a header az üzenetváltással kapcsolatos általános technikai adatokat, 
 * a user az authentikációval kapcsolatos adatokat, 
 * míg a software a műveletet végző számlázó program adatait tartalmazza.
*/
public class BasicRequestType
{
    protected BasicHeaderType header;
    protected UserHeaderType user;
    protected SoftwareType software;
    
    public BasicRequestType(int supplierId)
    {
        header = new BasicHeaderType();
        user = new UserHeaderType(header, supplierId);
        software = new SoftwareType();
    }
    
    public BasicRequestType(int supplierId, String checksum)
    {
        header = new BasicHeaderType();
        user = new UserHeaderType(header, supplierId, checksum);
        software = new SoftwareType();
    }
}