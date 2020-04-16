package cezeszamlazo.model;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * @author Tomy
 */
public class HttpClientFactory
{
    private static CloseableHttpClient client;

    public static HttpClient getHttpsClient()
    {
        if (client != null)
        {
            return client;
        }
        
        try
        {
            SSLContext sslcontext = SSLContexts.custom().useSSL().build();
            sslcontext.init(null, new X509TrustManager[]{new HttpsTrustManager()}, new SecureRandom());
            SSLConnectionSocketFactory factory = new SSLConnectionSocketFactory(sslcontext, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
            client = HttpClients.custom().setSSLSocketFactory(factory).build();
        }
        catch (NoSuchAlgorithmException ex)
        {
            Logger.getLogger(HttpClientFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (KeyManagementException ex)
        {
            Logger.getLogger(HttpClientFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return client;
    }

    public static void releaseInstance()
    {
        client = null;
    }
}