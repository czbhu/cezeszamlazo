package nav;

import database.Query;
import functions.Encode;
import invoice.Supplier;

/**
 * @author Tomy
 */
public class UserHeaderType
{
    String login;
    String passwordHash;
    String taxNumber;
    String requestSignature;
    
    public UserHeaderType(BasicHeaderType header, int supplierId)
    {
        Query query = new Query.QueryBuilder()
            .select("userName, password, taxNumber, signingKey")
            .from(Supplier.TABLE)
            .where("id = " + supplierId)
            .build();
        Object [][] supplier = NAV.db.select(query.getQuery());
        
        login = supplier[0][0].toString();
        passwordHash = Encode.sha512(supplier[0][1].toString());
        taxNumber = supplier[0][2].toString().split("-")[0];
        
        String signingKey = supplier[0][3].toString();
        
        requestSignature = Encode.sha3_512(header.requestId + header.getMaskedTimeStamp() + signingKey);
    }
    
    public UserHeaderType(BasicHeaderType header, int supplierId, String hashResult)
    {
        Query query = new Query.QueryBuilder()
            .select("userName, password, taxNumber, signingKey")
            .from(Supplier.TABLE)
            .where("id = " + supplierId)
            .build();
        Object [][] supplier = NAV.db.select(query.getQuery());
        
        login = supplier[0][0].toString();
        passwordHash = Encode.sha512(supplier[0][1].toString());
        taxNumber = supplier[0][2].toString().split("-")[0];
        
        String signingKey = supplier[0][3].toString();

        requestSignature = Encode.sha3_512(header.requestId + header.getMaskedTimeStamp() + signingKey + hashResult);
    }
    
    public void Print()
    {
        System.err.println("UserHeaderType:");
        System.err.println("    login: " + login);
        System.err.println("    passwordHash: " + passwordHash);
        System.err.println("    taxNumber: " + taxNumber);
        System.err.println("    requestSignature: " + requestSignature);
    }
}