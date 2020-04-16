/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeszamlazo.kintlevoseg;

import cezeszamlazo.App;
import cezeszamlazo.ElonezetDialog;
import cezeszamlazo.EncodeDecode;
import cezeszamlazo.DijbekerokFrame;
import static cezeszamlazo.DijbekerokFrame.mailTo;
import cezeszamlazo.controller.Functions;
import cezeszamlazo.database.Query;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSmartCopy;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import invoice.Invoice;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFileChooser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

/**
 *
 * @author szekus
 */
public class KintlevosegLevel
{
    private List<KintlevosegLevelAttributum> attributums;
    private String selectString;
    private KintlevosegLevelKapcsolattarto kapcsolattartok;
    private String[] szamlaAzonositok;
    private boolean dijbekero = false, isKintlevoseg;
    private ArrayList<String> files = new ArrayList<>();

    private String getOsszesen()
    {
        String tabla = "";
        if(dijbekero)
        {
            tabla = "szamlazo_pro_forms";
        }
        else
        {
            tabla = "szamlazo_invoices";
        }
        
        String result = "";
        int sum = 0;
        for (int i = 0; i < this.szamlaAzonositok.length; i++)
        {
            Query query = new Query.QueryBuilder()
                .select("netPrice, vatAmount")
                .from(tabla)
                .where("indentifier = '" + this.szamlaAzonositok[i] + "'")
                .build();
            Object[][] select = App.db.select(query.getQuery());

            //int netto = Functions.getIntFromObject(select[0][0]);
            int afa_ertek = Functions.getIntFromObject(select[0][1]);
            int netto = (int) Double.parseDouble(select[0][0].toString());

            sum += netto + afa_ertek;
        }
        result = String.valueOf(sum);
        return result;
    }

    public enum Type
    {
        EMAIL, PDF
    };

    private int id;
    private StringBuilder htmlString;
    private String subject;
    private Type type;

//    private KintlevosegLevel() {
//    }
    private KintlevosegLevel(Type type, boolean isKintlevoseg)
    {
        this.type = type;
        this.isKintlevoseg = isKintlevoseg;
    }

    public static KintlevosegLevel create(Type type, boolean isKintlevoseg)
    {
        KintlevosegLevel kintlevosegLevel = new KintlevosegLevel(type, isKintlevoseg);
        kintlevosegLevel.attributums = kintlevosegLevel.getAttributes();
        kintlevosegLevel.selectString = kintlevosegLevel.createSelect();
        return kintlevosegLevel;
    }

    public String[] getSzamlaAzonositok() {
        return szamlaAzonositok;
    }

    public void setSzamlaAzonositok(String[] szamlaAzonositok) {
        this.szamlaAzonositok = szamlaAzonositok;
    }

    public KintlevosegLevelKapcsolattarto getKapcsolattartok() {
        return kapcsolattartok;
    }

    public void setKapcsolattartok(KintlevosegLevelKapcsolattarto kapcsolattartok) {
        this.kapcsolattartok = kapcsolattartok;
    }

    public String getSelectString() {
        return selectString;
    }

    public void setSelectString(String selectString) {
        this.selectString = selectString;
    }

    public List<KintlevosegLevelAttributum> getAttributums() {
        return attributums;
    }

    public void setAttributums(List<KintlevosegLevelAttributum> attributums) {
        this.attributums = attributums;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public StringBuilder getHtmlString() {
        return htmlString;
    }

    public void setHtmlString(StringBuilder htmlString) {
        this.htmlString = htmlString;
    }

    public void save()
    {
        Object[] updateObject = new Object[2];

        int id = 1;
        if (type == Type.PDF)
        {
            id = 1;
        } else if (type == Type.EMAIL)
        {
            id = 2;
        }
        if(!isKintlevoseg)
        {
            id = 3;
        }

        updateObject[0] = (htmlString.toString());
        updateObject[1] = subject;
        String quersString = "UPDATE szamlazo_kintlevoseg_level SET html_text = ?, subject = ? WHERE id = " + id;
        App.db.insert(quersString, updateObject);

    }

    public StringBuilder getHtmlStringFromDb()
    {
        int id = 1;
        
        if (type == Type.PDF)
        {
            id = 1;
        }
        else if (type == Type.EMAIL)
        {
            id = 2;
        }
        
        if(!isKintlevoseg)
        {
            id = 3;
        }

        Query query = new Query.QueryBuilder()
            .select("html_text")
            .from("szamlazo_kintlevoseg_level")
            .where("id = " + id)
            .build();
        Object[][] select = App.db.select(query.getQuery());

        StringBuilder builder = new StringBuilder(EncodeDecode.decode(String.valueOf(select[0][0])));

        return builder;
    }

    public String getSubjectFromDb() {
        int id = 1;
        if (type == Type.PDF)
        {
            id = 1;
        }
        else if (type == Type.EMAIL)
        {
            id = 2;
        }
        if(!isKintlevoseg)
        {
            id = 3;
        }
        
        Query query = new Query.QueryBuilder()
                .select("subject")
                .from("szamlazo_kintlevoseg_level")
                .where("id = " + id)
                .build();
        Object[][] select = App.db.select(query.getQuery());

        return Functions.getStringFromObject(select[0][0]);

    }

    public String getPlainText() {
        StringBuilder html = this.getHtmlStringFromDb();

        String plainText = html.toString().replaceAll("(?s)<[^>]*>(\\s*<[^>]*>)*", " ");
        return plainText;

    }

    public static Iterable<MatchResult> allMatches(final Pattern p, final CharSequence input)
    {
        return new Iterable<MatchResult>()
        {
            public Iterator<MatchResult> iterator()
            {
                return new Iterator<MatchResult>()
                {
                    // Use a matcher internally.
                    final Matcher matcher = p.matcher(input);
                    // Keep a match around that supports any interleaving of hasNext/next calls.
                    MatchResult pending;

                    public boolean hasNext() {
                        // Lazily fill pending, and avoid calling find() multiple times if the
                        // clients call hasNext() repeatedly before sampling via next().
                        if (pending == null && matcher.find())
                        {
                            pending = matcher.toMatchResult();
                        }
                        return pending != null;
                    }

                    public MatchResult next()
                    {
                        // Fill pending if necessary (as when clients call next() without
                        // checking hasNext()), throw if not possible.
                        if (!hasNext()) {
                            throw new NoSuchElementException();
                        }
                        // Consume pending so next call to hasNext() does a find().
                        MatchResult next = pending;
                        pending = null;
                        return next;
                    }

                    /**
                     * Required to satisfy the interface, but unsupported.
                     */
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }

    public List<String> getAttributesFromHtmlText()
    {
        List<String> allMatches = new ArrayList<String>();
        Pattern p = Pattern.compile("\\{.*?\\}");

        for (MatchResult match : allMatches(p, this.getPlainText()))
        {
            String result = match.group().replaceAll(" ", "");
            allMatches.add(result);
        }

        return allMatches;
    }

    public List<String> getColumNames() {
        List<String> list = new ArrayList();

        for (String string : this.getAttributesFromHtmlText())
        {
            string = string.replace("{", "");
            string = string.replace(" ", "");
            string = string.replace("}", "");
            list.add(string);
        }

        return list;
    }

    public List<KintlevosegLevelAttributum> getAttributes()
    {
        List<String> columnNames = this.getColumNames();
        List<KintlevosegLevelAttributum> localAttributums = new ArrayList<>();

        for (String columnName : columnNames)
        {
            KintlevosegLevelAttributum kintlevosegLevelAttributum = new KintlevosegLevelAttributum();
            localAttributums.add(kintlevosegLevelAttributum.getHtmlAttribute(columnName));
        }

        return localAttributums;
    }

    public String createSelect()
    {
        String select = "";

        int i = 0;
        for (KintlevosegLevelAttributum kintlevosegLevelAttributum : this.getAttributes())
        {
            if (kintlevosegLevelAttributum.getSqlCommand() != "")
            {
                select += kintlevosegLevelAttributum.getSqlCommand();
            }
            else
            {
                select += "sz." + kintlevosegLevelAttributum.getRefAttr();
            }

            if (i != this.getAttributes().size() - 1)
            {
                select += ",";
            }
            i++;
        }

        return select;
    }

    public void reWriteHtmlTable(String[] azonositok, boolean dijbekero)
    {
        this.szamlaAzonositok = azonositok;
//      List<KintlevosegLevelKapcsolattarto> kapcsolattartokLocal = new ArrayList<>();
//
//      for (int i = 0; i < azonositok.length; i++)
//      {
//          KintlevosegLevelKapcsolattarto kapcsolattarto = KintlevosegLevelKapcsolattarto.create();
//
//          kapcsolattarto.setKapcsolattartoBySzamlaAzonosito(azonositok[i]);
//          kapcsolattartokLocal.add(kapcsolattarto);
//      }

        String html = this.getHtmlStringFromDb().toString();
        org.jsoup.nodes.Document doc = Jsoup.parse(html);
        Elements tableElements = doc.select("table");

        List<String> selectValues = new ArrayList<>();
        Elements rows = tableElements.select("tr");
        
        for (Element row : rows)
        {
            Elements elems = row.select("td");
            for (Element elem : elems)
            {
                selectValues.add(elem.text());
            }
        }

        String selectString = "";
        int i = 0;
        
        for (String selectValue : selectValues)
        {
            if (i != selectValues.size() - 1)
            {
                selectString += selectValue.replace("{", "").replace("}", "") + ", ";
            }
            else
            {
                selectString += selectValue.replace("{", "").replace("}", "");
            }
            i++;
        }

        String tabla = "";
        
        if(dijbekero)
        {
            tabla = "szamlazo_pro_forms";
        }
        else
        {
            tabla = "szamlazo_invoices";
        }
        
        Object[][] tableRowValuesObject = new Object[azonositok.length][selectValues.size()];
        
        for (int j = 0; j < azonositok.length; j++)
        {
            Query query = new Query.QueryBuilder()
                .select(selectString)
                .from(tabla)
                .where("indentifier = '" + azonositok[j] + "'")
                .build();
            Object[][] object = App.db.select(query.getQuery());
            tableRowValuesObject[j] = object[0];
        }

        String[][] tableRowValuesString = new String[azonositok.length][selectValues.size()];

        for (int j = 0; j < tableRowValuesObject.length; j++)
        {
            for (int k = 0; k < tableRowValuesObject[j].length; k++)
            {
                String tmpString = Functions.getStringFromObject(tableRowValuesObject[j][k]);
                tableRowValuesString[j][k] = tmpString;
            }
        }

        for (Element row : rows)
        {
            Elements elems = row.select("td");
            
            for (Element elem : elems)
            {
                elem.remove();
            }
        }
        
        for (Element row : rows)
        {
            Elements elems = row.select("tr");
            
            for (Element elem : elems)
            {
                if (!elem.hasText() && elem.isBlock())
                {
                    elem.remove();
                }
            }
        }

        for (int j = 0; j < azonositok.length; j++)
        {
            Element trElement = new Element("tr");
            
            for (int k = 0; k < tableRowValuesString[j].length; k++)
            {
                trElement.append("<td style='text-align:center;'>" + tableRowValuesString[j][k] + "</td>");
            }

            tableElements.get(0).append(trElement.toString());
        }
        
        this.htmlString = this.getHtmlStringFromDb();

        doc = Jsoup.parse(this.htmlString.toString());
        Elements originalTableElements = doc.select("table");

        TextNode node = new TextNode("<table >" + tableElements.get(0) + "</table>", "");

        originalTableElements.get(0).replaceWith(tableElements.get(0));
        this.htmlString = new StringBuilder(doc.html());
    }

    public StringBuilder fetchDataToHtmlString(String[] azonositok, boolean isCombined, int index, boolean dijbekero, boolean isKintlevoseg)
    {
        StringBuilder tempTartalom = null;
        try
        {
            this.dijbekero = dijbekero;
            tempTartalom = this.htmlString;
            String nev = kapcsolattartok.getList().get(index).getName();
            String tipus = "";
            
            if(isKintlevoseg)
            {
                tipus = " && paymentDate = '0000-00-00'";
            }
            
            String whereText = "indentifier = " + azonositok[0] + tipus;

            String tabla = "";
            
            if(dijbekero)
            {
                tabla = "szamlazo_pro_forms sz";
            }
            else
            {
                tabla = "szamlazo_invoices sz";
            }

            Query query = new Query.QueryBuilder()
                .select(this.getSelectString())
                .from(tabla)
                .where(whereText)
                .build();
            Object[][] selectAttr = App.db.select(query.getQuery());

            int j = 0;
            
            for (Iterator<KintlevosegLevelAttributum> iterator = this.getAttributums().iterator(); iterator.hasNext();)
            {
                KintlevosegLevelAttributum next = iterator.next();
                
                if (next.getRefAttr().equals("nev"))
                {
                    next.setValue(nev);
                }
                else if (next.getRefAttr().equals("osszesen"))
                {
                    next.setValue(this.getOsszesen());
                }
                else
                {
                    next.setValue(selectAttr[0][j].toString());
                }
                
                j++;
            }

            for (KintlevosegLevelAttributum attr : this.getAttributums())
            {
                tempTartalom = new StringBuilder(tempTartalom.toString().replaceAll(attr.getRefAttr(), attr.getValue()));
                tempTartalom = new StringBuilder(tempTartalom.toString().replaceAll("\\{", ""));
                tempTartalom = new StringBuilder(tempTartalom.toString().replaceAll("\\}", ""));
            }
            
            //File file = new File("kintelevoseg_szamla_" + szla.getSorszam().replace(" ", "_").replace("/", "_") + ".pdf");
        }
        catch(ArrayIndexOutOfBoundsException ex)
        {
            System.err.println("Nincs kintlévőség ennél a számlánál");
        }
        
        return tempTartalom;
    }

    public void createEmail(String[] azonositok, boolean isCombined, boolean dijbekero, boolean isKintlevoseg) throws KintlevosegLevelException
    {
        this.isKintlevoseg = isKintlevoseg;
        this.reWriteHtmlTable(azonositok, dijbekero);

        List<KintlevosegLevelKapcsolattarto> kapcsolattartokLocal = new ArrayList<>();

        KintlevosegLevelKapcsolattarto kapcsolattarto = KintlevosegLevelKapcsolattarto.create();

        kapcsolattarto.setKapcsolattartoBySzamlaAzonosito(azonositok[0], dijbekero);
        this.setKapcsolattartok(kapcsolattarto);

        String nev = kapcsolattartok.getList().get(0).getName();
        String email = "";//kapcsolattartok.getList().get(0).getEmail();
        String targy = this.getSubjectFromDb();

        for (int k = 0; k < kapcsolattartok.getList().size(); k++)
        {
            nev = kapcsolattartok.getList().get(k).getName();
            
            email += kapcsolattartok.getList().get(k).getEmail() + ", ";
            //email = kapcsolattartok.getList().get(k).getEmail();
            
            if (isCombined)
            {
                StringBuilder tempTartalom = fetchDataToHtmlString(azonositok, isCombined, k, dijbekero, isKintlevoseg);
                
                try
                {
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    //File file = new File("kintelevoseg_szamla_" + szla.getSorszam().replace(" ", "_").replace("/", "_") + ".pdf");
                    //document = new Document(PageSize.A4);
                    
                    String fileName = "kintlevoseg_szamla_" + timestamp.getTime() + ".pdf";
                    OutputStream out = new FileOutputStream(fileName);
                    files.add(fileName);

                    //PdfWriter writer = PdfWriter.getInstance(document, out);
                    //document.open();

                    //final org.jsoup.nodes.Document document2 = Jsoup.parse(tempTartalom.toString(), "UTF-8");
                    //document2.outputSettings().syntax(org.jsoup.nodes.Document.OutputSettings.Syntax.xml);
                    //String html1 = EncodeDecode.decode(document2.html());
                    String concatAttachmentFileUrl = "";
                    int count = 0;
                    
                    for (int j = 0; j < azonositok.length; j++)
                    {
                        Invoice invoice = null;
                        
                        if(dijbekero)
                        {
                            invoice = new Invoice(Invoice.PROFORMA, Invoice.InvoiceType.ORIGINAL, "szamlazo_pro_forms", azonositok[j]);
                        }
                        else
                        {
                            invoice = new Invoice(Invoice.INVOICE, Invoice.InvoiceType.ORIGINAL, "szamlazo_invoices", azonositok[j]);
                        }
                        
                        ElonezetDialog e = new ElonezetDialog(invoice, ElonezetDialog.ATTACMENT);

                        e.setVisible(false);
                            
                        String attachmentFileUrl = e.createPDF();
                        concatAttachmentFileUrl += attachmentFileUrl;
                        
                        if (count != azonositok.length - 1)
                        {
                            concatAttachmentFileUrl += "; ";
                        }

                        count++;
                    }

                    if(k == kapcsolattartok.getList().size()-1)
                    {
                        mailTo(email, targy, tempTartalom.toString(), concatAttachmentFileUrl);
                    }
                    
                    //mailTo(email, targy, tempTartalom.toString(), concatAttachmentFileUrl);

                    //XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
                    //InputStream is = new ByteArrayInputStream(html1.getBytes(StandardCharsets.UTF_8));
                    //worker.parseXHtml(writer, document, is, Charset.forName("UTF-8"));
                }
                catch (FileNotFoundException ex)
                {
                    Logger.getLogger(DijbekerokFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else
            {
                for (int i = 0; i < kapcsolattartok.getList().size(); i++)
                {
                    fetchDataToHtmlString(azonositok, isCombined, i, dijbekero, isKintlevoseg);
                }
            }
        }
    }

    public void createPDF(String[] azonositok, boolean isCombined, boolean dijbekero, boolean isKintlevoseg) throws KintlevosegLevelException
    {
        this.isKintlevoseg = isKintlevoseg;
        this.reWriteHtmlTable(azonositok, dijbekero);

        List<KintlevosegLevelKapcsolattarto> kapcsolattartokLocal = new ArrayList<>();

        KintlevosegLevelKapcsolattarto kapcsolattarto
                = KintlevosegLevelKapcsolattarto.create();

        kapcsolattarto.setKapcsolattartoBySzamlaAzonosito(azonositok[0], dijbekero);
        this.setKapcsolattartok(kapcsolattarto);

        for (int k = 0; k < kapcsolattartok.getList().size(); k++)
        {
            if (isCombined)
            {
                StringBuilder tempTartalom = fetchDataToHtmlString(azonositok, isCombined, k, dijbekero, isKintlevoseg);
                try
                {
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    File file = new File("kintlevoseg_szamla_" + timestamp.getTime() + ".pdf");
                    JFileChooser chooser = new JFileChooser();
                    chooser.setSelectedFile(file);
                    chooser.showOpenDialog(null);
                    File curFile = chooser.getSelectedFile();
                    Document document = null;

                    if (curFile != null)
                    {
                        document = new Document(PageSize.A4);
                        OutputStream out = new FileOutputStream(chooser.getSelectedFile());
                        PdfWriter writer = PdfWriter.getInstance(document, out);
                        document.open();
//

                        final org.jsoup.nodes.Document document2 = Jsoup.parse(tempTartalom.toString(), "UTF-8");
                        document2.outputSettings().syntax(org.jsoup.nodes.Document.OutputSettings.Syntax.xml);
                        String html1 = EncodeDecode.decode(document2.html());

                        XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
                        InputStream is = new ByteArrayInputStream(html1.getBytes(StandardCharsets.UTF_8));
                        worker.parseXHtml(writer, document, is, Charset.forName("UTF-8"));
                        document.close();

                        List<File> files = new ArrayList<>();
                        files.add(chooser.getSelectedFile());
                        for (int j = 0; j < azonositok.length; j++) {
                            
                            Invoice invoice = new Invoice((dijbekero ? Invoice.PROFORMA : Invoice.INVOICE), Invoice.InvoiceType.ORIGINAL, (dijbekero ? "szamlazo_pro_forms" : "szamlazo_invoices"), azonositok[j]);
                            //Szamla szamla = new Szamla(azonositok[j]);

                            ElonezetDialog e = new ElonezetDialog(invoice, ElonezetDialog.ATTACMENT);

                            e.setVisible(false);

                            String attachmentFileUrl = e.createPDF();
                            files.add(new File(attachmentFileUrl));

                        }

                        String tempFileName = timestamp.getTime() + ".pdf";

                        this.concatenatePdfs(files, tempFileName);
                        FileChannel src = new FileInputStream(tempFileName).getChannel();
                        FileChannel dest = new FileOutputStream(chooser.getSelectedFile().getAbsolutePath()).getChannel();
                        dest.transferFrom(src, 0, src.size());
                        src.close();
                        dest.close();

                        File deleteFile = new File(tempFileName);
                        if (deleteFile.exists())
                        {
                            deleteFile.delete();
                        }
                    }
                }
                catch (DocumentException ex)
                {
                    ex.printStackTrace();
                }
                catch (FileNotFoundException ex)
                {
                    Logger.getLogger(DijbekerokFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                catch (IOException ex)
                {
                    Logger.getLogger(DijbekerokFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else
            {
                for (int i = 0; i < kapcsolattartok.getList().size(); i++)
                {
                    fetchDataToHtmlString(azonositok, isCombined, i, dijbekero, isKintlevoseg);
                }
            }
        }
    }

    public void concatenatePdfs(List<File> listOfPdfFiles, String outFile) throws DocumentException, IOException {
        Document document = new Document();
        FileOutputStream outputStream = new FileOutputStream(outFile);
        PdfCopy copy = new PdfSmartCopy(document, outputStream);
        document.open();
        for (File inFile : listOfPdfFiles) {
            PdfReader reader = new PdfReader(inFile.getAbsolutePath());
            copy.addDocument(reader);
            reader.close();
        }
        document.close();
    }
}
