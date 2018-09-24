package cezeszamlazo.controller;

import cezeszamlazo.App;
import cezeszamlazo.database.Query;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

public class Vevo extends Controller implements Serializable {

    private int id = 0;
    private String nev;
    private String irsz;
    private String varos;
    private String utca;
    private String kozterulet;
    private String hazszam;
    private String orszag;
    private String telefon;
    private String email;
    private int fizetesiMod;
    private boolean fizetesiModKotelezo;
    private String esedekesseg;
    private String adoszam;
    private String euAdoszam;
    private String bankszamlaszam;
    private boolean szamlanMegjelenik;

    public Vevo(int vevoid) {
        super();
        if (vevoid != 0) {
            query = new Query.QueryBuilder()
                    .select("nev, irsz, varos, utca, kozterulet, hazszam, "
                            + "orszag, telefon, email, fizetesi_mod, "
                            + "fizetesi_mod_kotelezo, esedekesseg, "
                            + "adoszam, eu_adoszam, szamlan_megjelenik, bankszamlaszam ")
                    .from("pixi_ugyfel")
                    .where("id = " + vevoid)
                    .build();
            Object[][] select = App.db.select(query.getQuery());
            
            System.out.println(query.getQuery());
            
            this.nev = String.valueOf(select[0][0]);
            this.irsz = String.valueOf(select[0][1]);
            this.varos = String.valueOf(select[0][2]);
            this.utca = String.valueOf(select[0][3]);
            
            this.kozterulet = String.valueOf(select[0][4]);
            this.hazszam = String.valueOf(select[0][5]);
            
            this.orszag = String.valueOf(select[0][6]);
            this.telefon = String.valueOf(select[0][7]);
            this.email = String.valueOf(select[0][8]);
            this.fizetesiMod = Integer.parseInt(String.valueOf(select[0][9]));
            this.fizetesiModKotelezo = String.valueOf(select[0][10]).matches("1") ? true : false;

            if (Integer.parseInt(String.valueOf(select[0][9])) == 1 && String.valueOf(select[0][11]).matches("0")) {
                Properties prop = new Properties();
                try {
                    prop.load(new FileInputStream("dat/beallitasok.properties"));
                    this.esedekesseg = prop.getProperty("alapEsedekesseg");
                } catch (IOException ex) {
                    System.out.println("IOException váltódott ki!");
                    ex.printStackTrace();
                    this.esedekesseg = String.valueOf(select[0][11]);
                }
            } else {
                this.esedekesseg = String.valueOf(select[0][11]);
            }

            this.adoszam = String.valueOf(select[0][12]);
            this.euAdoszam = String.valueOf(select[0][13]);
            this.bankszamlaszam = String.valueOf(select[0][14]).matches("0") ? "" : String.valueOf(select[0][14]);
            this.szamlanMegjelenik = String.valueOf(select[0][15]).matches("1");
        }

    }

    public Vevo(String nev, String irsz, String varos, String utca, String kozterulet, String hazszam, String orszag, String telefon, String email, int fizetesiMod, boolean fizetesiModKotelezo, String esedekesseg, String adoszam, String euAdoszam, String bankszamlaszam, boolean szamlanMegjelenik) {
        super();
        this.nev = nev;
        this.irsz = irsz;
        this.varos = varos;
        this.utca = utca;
        this.kozterulet = kozterulet;
        this.hazszam = hazszam;
        this.orszag = orszag;
        this.telefon = telefon;
        this.email = email;
        this.fizetesiMod = fizetesiMod;
        this.fizetesiModKotelezo = fizetesiModKotelezo;
        this.esedekesseg = esedekesseg;
        this.adoszam = adoszam;
        this.euAdoszam = euAdoszam;
        this.bankszamlaszam = bankszamlaszam;
        this.szamlanMegjelenik = szamlanMegjelenik;
    }

    public String getAdoszam() {
        return adoszam;
    }

    public String getBankszamlaszam() {
        return bankszamlaszam;
    }

    public String getEmail() {
        return email;
    }

    public String getEsedekesseg() {
        return esedekesseg;
    }

    public String getEuAdoszam() {
        return euAdoszam;
    }

    public int getFizetesiMod() {
        return fizetesiMod;
    }

    public boolean isFizetesiModKotelezo() {
        return fizetesiModKotelezo;
    }

    public String getIrsz() {
        return irsz;
    }

    public String getNev() {
        return nev;
    }

    public String getOrszag() {
        return orszag;
    }

    public boolean isSzamlanMegjelenik() {
        return szamlanMegjelenik;
    }

    public String getTelefon() {
        return telefon;
    }

    public String getUtca() {
        return utca;
    }
    
    public String getKozterulet()
    {
        return kozterulet;
    }
    
    public String getHazszam()
    {
        return hazszam;
    }

    public String getVaros() {
        return varos;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "id: " + id + "\n" + nev + "\n"
                + (!irsz.isEmpty() && !irsz.equalsIgnoreCase("0") ? irsz + ", " : "")
                + (varos.isEmpty() ? "" : varos + "\n")
                + (utca.isEmpty() ? "" : utca + "\n")
                + (kozterulet.isEmpty() ? "" : kozterulet + "\n")
                + (hazszam.isEmpty() ? "" : hazszam + "\n")
                + (adoszam.isEmpty() ? "" : adoszam + "\n")
                + (euAdoszam.isEmpty() ? "" : euAdoszam)
                + (szamlanMegjelenik ? "\n" + bankszamlaszam : "");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (this == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        // Class name is Employ & have lastname
        Vevo vevo = (Vevo) obj;
        return this.nev.equals(vevo.getNev()) || this.irsz.equals(vevo.getIrsz())
                || this.varos.equals(vevo.getVaros()) || this.utca.equals(vevo.getUtca())
                || this.kozterulet.equals(vevo.getKozterulet()) || this.hazszam.equals(vevo.getHazszam())
                || this.orszag.equals(vevo.getOrszag()) || this.telefon.equals(vevo.getTelefon())
                || this.email.equals(vevo.getEmail()) || this.fizetesiMod == (vevo.getFizetesiMod())
                || this.fizetesiModKotelezo == (vevo.isFizetesiModKotelezo()) || this.esedekesseg.equals(vevo.getEsedekesseg())
                || this.adoszam.equals(vevo.getAdoszam()) || this.euAdoszam.equals(vevo.getEuAdoszam())
                || this.bankszamlaszam.equals(vevo.getBankszamlaszam()) || this.szamlanMegjelenik == (vevo.isSzamlanMegjelenik());
    }
}
