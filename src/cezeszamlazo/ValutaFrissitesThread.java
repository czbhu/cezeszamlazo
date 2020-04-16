package cezeszamlazo;

import cezeszamlazo.database.Query;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ValutaFrissitesThread extends Thread
{
    private boolean mind = true,
            kesz = false;

    public ValutaFrissitesThread(boolean mind)
    {
        this.mind = mind;
    }

    @Override
    public void run()
    {
        kesz = false;
        
        Query query = new Query.QueryBuilder()
            .select("currency")
            .from("szamlazo_valuta")
            .where("currency != 'HUF'" + (!mind ? " && DATEDIFF(NOW(), lastModification) >= 1" : ""))
            .build();
        Object[][] select = App.db.select(query.getQuery());
        
        Object[] object = new Object[1];
        
        for (int i = 0; i < select.length; i++)
        {
            object[0] = arfolyam(String.valueOf(select[i][0]));
            App.db.insert("UPDATE szamlazo_valuta SET centralParity = ?, lastModification = NOW() WHERE currency = '" + String.valueOf(select[i][0]) + "'", object);
        }
        
        kesz = true;
    }

    public boolean isKesz()
    {
        return kesz;
    }

    private double arfolyam(String valuta)
    {
        try
        {
            // Construct data
            String data = "http://api.napiarfolyam.hu/?valuta=" + valuta + "&valutanem=deviza&bank=mnb";

            // Send data
            URL url = new URL(data);
            URLConnection urlconn = url.openConnection();
            urlconn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(urlconn.getOutputStream());
            wr.write(data);
            wr.flush();

            // Get the response
            BufferedReader rd = new BufferedReader(new InputStreamReader(urlconn.getInputStream()));
            BufferedWriter out = new BufferedWriter(new FileWriter("dat/" + valuta + ".xml"));

            String line;
            
            while ((line = rd.readLine()) != null)
            {
                out.write(line + "\n");
            }
            
            wr.close();
            rd.close();
            out.close();

            File file = new File("dat/" + valuta + ".xml");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder dbuilder = dbf.newDocumentBuilder();
            Document doc = dbuilder.parse(file);
            doc.getDocumentElement().normalize();
            NodeList nodeLst = doc.getElementsByTagName("item");
            
            for (int s = 0; s < nodeLst.getLength(); s++)
            {
                Node fstNode = nodeLst.item(s);
                if (fstNode.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element fstElmnt = (Element) fstNode;
                    NodeList fstNmElmntLst = fstElmnt.getElementsByTagName("kozep");
                    Element fstNmElmnt = (Element) fstNmElmntLst.item(0);
                    NodeList fstNm = fstNmElmnt.getChildNodes();
                    return Double.parseDouble(((Node) fstNm.item(0)).getNodeValue());
                }
            }
        }
        catch (Exception e) {}
        
        return 0.0;
    }
}