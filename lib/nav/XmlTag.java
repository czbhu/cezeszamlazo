package nav;

/**
 * @author Tomy
 */
public class XmlTag
{
    
    String xmlString;
    
    public XmlTag(String xml)
    {
        xmlString = xml;
    }
    
    public String get(String tag)
    {
        try
        {
            return xmlString.split("<" + tag + ">")[1].split("</" + tag + ">")[0];
        }
        catch(ArrayIndexOutOfBoundsException ex)
        {
            return  "";
        }
    }
    
    public String get(String startTag, String endTag)
    {
        try
        {
            String xmlsplit = xmlString.split("<" + startTag + ">")[1];
            xmlsplit = xmlsplit.split("</" + endTag + ">")[0];

            return xmlsplit;
        }
        catch(ArrayIndexOutOfBoundsException ex)
        {
            return "";
        }
    }
}