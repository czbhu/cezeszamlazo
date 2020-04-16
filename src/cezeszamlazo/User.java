package cezeszamlazo;

import java.io.Serializable;

/**
 *
 * @author Papp Ádám - Ceze Reklam
 */
public class User implements Serializable
{
    public static final String TABLE = "szamlazo_users";
    
    private int id = 0;
    private String nev = "";
    private String usernev = "";
    private String jelszo = "";
    private int csoportid = 0;
    private String ceg = "";
    int defaultCompany = 0;

    public User(Object[][] o)
    {
        try
        {
            id = Integer.parseInt(String.valueOf(o[0][0]));
            nev = String.valueOf(o[0][1]);
            usernev = String.valueOf(o[0][2]);
            jelszo = String.valueOf(o[0][3]);
            csoportid = Integer.parseInt(String.valueOf(o[0][4]));
            ceg = String.valueOf(o[0][5]);
            defaultCompany = Integer.parseInt(o[0][6].toString());
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
            System.out.println("ArrayIndexOutOfBoundsException váltódott ki! User.java/User()");
            //ex.printStackTrace();
        }
    }

    public int getCsoportid() {
        return csoportid;
    }

    public String getCeg() {
        return ceg;
    }
    
    public int getId() {
        return id;
    }

    public String getNev() {
        return nev;
    }
    
    public String getUsernev() {
        return usernev;
    }

    public String getJelszo() {
        return jelszo;
    }

    public int getDefaultCompany() {
        return defaultCompany;
    }
}