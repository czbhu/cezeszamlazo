package cezeszamlazo.functions;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.CRC32;
import java.util.zip.Checksum;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.crypto.digests.SHA3Digest;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

/**
 * @author Tomy
 */
public class Encode
{
    public static String sha512(String input)
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
        
	return toReturn.toUpperCase();
    }
    
    public static String sha3_512(String input)
    {
        String toReturn = null;
        
        SHA3Digest md = new SHA3Digest(512);
        md.reset();
        md.update(input.getBytes(), 0, input.getBytes().length);
        byte[] hashedBytes = new byte[512 / 8];
        md.doFinal(hashedBytes, 0);
        toReturn = ByteUtils.toHexString(hashedBytes);
        
	return toReturn.toUpperCase();
    }
    
    public static String Base64(String input)
    {
        String output = "";
        
        try
        {
            output = new String(Base64.encodeBase64(input.getBytes("UTF-8")));
        }
        catch (UnsupportedEncodingException ex)
        {
            Logger.getLogger(Encode.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return output;
    }
    
    public static String CRC32(String input)
    {
        Long invoiceCRC32 = null;
        String checksum = "";
        
        try
        {
            String res = new String(Base64.encodeBase64(input.getBytes("UTF-8")));
            byte [] bytes = res.getBytes("UTF-8");
            Checksum sum = new CRC32();
            sum.update(bytes, 0, bytes.length);
            long value = sum.getValue();
            invoiceCRC32 = value;
            checksum = Long.toString(value);
        }
        catch (UnsupportedEncodingException ex)
        {
            Logger.getLogger(Encode.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return checksum;
        //return invoiceCRC32;
    }
}