package cezeszamlazo;

import cezeszamlazo.views.NewView;
import controller.Database;
import java.awt.Dimension;
import java.awt.Toolkit;
import nav.NAV;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

// multi-user
/**
 * The main class of the application.
 */
public class App extends SingleFrameApplication
{
    public static Database db;
    //public static Database cezetesztdb;
    public static Database pixi;
    //public static Database mainDB;
    //public static Database localdb;
    public static User user;
    public static String[] args;
    
    public static NAV nav;
    
    /**
     * At startup create and show the main frame of the application.
     */
    @Override protected void startup()
    {
	/*show(new View(this));
	View szamlazoView = new View(this);
        szamlazoView.getFrame().setSize(480, 550);
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = toolkit.getScreenSize();
        final int x = (screenSize.width - szamlazoView.getFrame().getWidth()) / 2;
        final int y = (screenSize.height - szamlazoView.getFrame().getHeight()) / 2;
        szamlazoView.getFrame().setLocation(x, y);
        szamlazoView.getFrame().setResizable(false);
        
        //Elérhető frissítések keresése
	ValutaFrissitesThread vft = new ValutaFrissitesThread(false);
	vft.start();
	szamlazoView.frissitesKeres();*/
        
        NewView view = new NewView();
        
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = toolkit.getScreenSize();
        final int x = (screenSize.width - view.getWidth()) / 2;
        final int y = (screenSize.height - view.getHeight()) / 2;
        view.setLocation(x, y);
        
        ValutaFrissitesThread vft = new ValutaFrissitesThread(false);
	vft.start();
	view.SearchForUpdates();
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     * @param root
     */
    @Override protected void configureWindow(java.awt.Window root){}

    /**
     * A convenient static getter for the application instance.
     * @return the instance of App
     */
    public static App getApplication()
    {
        return Application.getInstance(App.class);
    }

    public static void main(String[] args) throws Exception
    {
        App.args = args;
        System.setProperty("file.encoding","UTF-8");
        System.out.println("encoding: " + System.getProperty("file.encoding") + " (App.java/main())");
        launch(App.class, args);
    }
}