package nav;

import com.mifmif.common.regex.Generex;
import database.Query;
import invoice.Software;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author Tomy
 */
public class BasicHeaderType
{
    String requestId = "";  //pattern: [+a-zA-Z0-9_]{1,30}
    String timestamp;     
    String requestVersion;  
    String headerVersion;   
    
    public BasicHeaderType()
    {
        setTimestamp();
        setRequestId();       
        setRequestAndHeaderVersion();
    }
    
    private void setTimestamp()
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        timestamp = dateFormat.format(new Date());
    }
    
    private void setRequestId()
    {
        requestId = getRandomId("[+a-zA-Z0-9_]{16,16}") + getMaskedTimeStamp();
    }
    
    private void setRequestAndHeaderVersion()
    {
        Query query = new Query.QueryBuilder()
            .select("requestVersion, headerVersion")
            .from(Software.TABLE)
            .build();
        Object [][] versions = NAV.db.select(query.getQuery());
        
        requestVersion = versions[0][0].toString().replace(",", ".");
        headerVersion = versions[0][1].toString().replace(",", ".");
    }
    
    public String getRandomId(String expReg)
    {
        Generex generex = new Generex(expReg);
        String RandomId = generex.random(16, 16);
        return RandomId;
    }

    public String getMaskedTimeStamp()
    {
        String masked = "";
        char[] chars = timestamp.toCharArray();
        masked = masked + chars[0] + chars[1] + chars[2] + chars[3]  + chars[5] + chars[6] + chars[8] + chars[9]  + chars[11] + chars[12] + chars[14] + chars[15] + chars[17] + chars[18];
        
        return masked;
    }
    
    public void Print()
    {
        System.err.println("BasicHeaderType");
        System.err.println("    requestId: " + requestId);
        System.err.println("    timestamp: " + timestamp);
        System.err.println("    requestVersion: " + requestVersion);
        System.err.println("    headerVersion: " + headerVersion);
    }
    
    //GETTERS
    public String getRequestId() {
        return requestId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getRequestVersion() {
        return requestVersion;
    }

    public String getHeaderVersion() {
        return headerVersion;
    }
}