package cezeszamlazo;

//import cezeszamlazo.hu.gov.nav.schemas._2013.szamla.GenerateXml;
import java.awt.Dimension;
import java.awt.Toolkit;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

// multi-user
/**
 * The main class of the application.
 */
public class App extends SingleFrameApplication {

    public static Database db;
    public static Database mainDB;
    public static Database localdb;
    public static User user;
    public static String[] args;
    
    /**
     * At startup create and show the main frame of the application.
     */
    @Override protected void startup() {
        
	show(new View(this));
	View szamlazoView = new View(this);
        szamlazoView.getFrame().setSize(480, 550);
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = toolkit.getScreenSize();
        final int x = (screenSize.width - szamlazoView.getFrame().getWidth()) / 2;
        final int y = (screenSize.height - szamlazoView.getFrame().getHeight()) / 2;
        szamlazoView.getFrame().setLocation(x, y);
        szamlazoView.getFrame().setResizable(false);
        // elérhető frissítések keresése
	ValutaFrissitesThread vft = new ValutaFrissitesThread(false);
	vft.start();
	szamlazoView.frissitesKeres();
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of App
     */
    public static App getApplication() {
        return Application.getInstance(App.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) throws Exception
    {
        App.args = args;
        System.setProperty("file.encoding","UTF-8");
        System.out.println("encoding: " + System.getProperty("file.encoding"));
        launch(App.class, args);
        
        
        //Országok beillesztése az adatbázisba
        /*RandomAccessFile raf = new RandomAccessFile("orszagok.txt","r");
        int count = 1;
        for(String row=raf.readLine(); row != null; row=raf.readLine())
        {            
            count++;
        }
        raf.close();
        
        ArrayList<String> orszag = new ArrayList<>();
        RandomAccessFile raf2 = new RandomAccessFile("orszagok.txt","r");
        for(String row=raf2.readLine(); row != null; row=raf2.readLine())
        {            
            String [] split = row.split("-");
            String [] splitSeged = new String[2];
            splitSeged[0] = "";
            
            if(split.length >= 3)
            {
                int k = 0;
                for(int i=0; i<split.length-2; i++)
                {
                    splitSeged[0] += split[i] + "-";
                    k=i;
                }
                splitSeged[1] = split[k+1];
                
                split[0] = splitSeged[0];
                split[1] = splitSeged[1];               
                orszag.add(split[0] + "" + split[1] + "_" + split[split.length-1]);
            }
            else
            {
                orszag.add(split[0] + "_" + split[split.length-1]);
            }
        }
        
        Collections.sort(orszag);
    
        int i = 0;
        for(String o : orszag)
        {
            System.out.println(o);
            Object [] insert = new Object[2];
            String [] split = o.split("_");
            insert[0] = split[0];
            //System.out.println(insert[0]);
            insert[1] = split[1];
            //System.out.println(insert[1]);
            dbconn con = new dbconn();
            Connection conn = con.getConnection();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO countries (CountryName, CountryCode) VALUES (?, ?)");
            ps.setString(1, insert[0].toString());
            ps.setString(2, insert[1].toString());
            ps.execute();
            //App.db.insert("INSERT INTO countries (CountryName, CountryCode) VALUES (?, ?)", insert, 2);
            i++;
        }
        */      
    }
}