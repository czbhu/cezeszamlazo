package nav;

/**
 * @author Tomy
 */

/**
 * Minden response element kötelező része a BasicResponseType. 
 * A típuson belül a header a válasz tranzakcionális adatait, 
 * a result a feldolgozás eredményét, 
 * míg a software a műveletet végző számlázó program adatait tartalmazza. 
 */
public class BasicResponseType
{
    BasicHeaderType header;
    BasicResultType result;
    SoftwareType software;
    
    public BasicResponseType(String xml)
    {
        
    }
}
