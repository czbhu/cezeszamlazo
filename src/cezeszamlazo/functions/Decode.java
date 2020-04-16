package cezeszamlazo.functions;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

/**
 * @author Tomy
 */
public class Decode
{
    public static String aes128(String encodedString, String exchangeKey)
    {
        try
        {
            SecretKeySpec skeySpec = new SecretKeySpec(exchangeKey.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            
            byte[] original = cipher.doFinal(Base64.decodeBase64(encodedString.getBytes()));
            return new String(original).trim();
        }
        catch (NoSuchAlgorithmException ex)
        {
            Logger.getLogger(Decode.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (NoSuchPaddingException ex)
        {
            Logger.getLogger(Decode.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (InvalidKeyException ex)
        {
            Logger.getLogger(Decode.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IllegalBlockSizeException ex)
        {
            Logger.getLogger(Decode.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (BadPaddingException ex)
        {
            Logger.getLogger(Decode.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return "";
    }
}