package cezeszamlazo.controller;

import cezeszamlazo.App;
import cezeszamlazo.database.Query;
import cezeszamlazo.model.TeljesitesIgazolasModel;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Szamla implements Serializable {

    private Szallito szallito;
    private Vevo vevo;
    private String sorszam;
    private String kelt;
    private String teljesites;
    private String esedekesseg;
    private String szamlaCsoportId;
    private int nyomtatva = 0;
    private int tipus = 0;
    private String valuta;
    private double kozeparfolyam;
    private String helyesbitett;
    private String helyesbitettTeljesites;
    private boolean deviza;
    private String megjegyzes;
    private String lablec;
    private List<SzamlaTermek> termekek;
    private List<Afa> afak;
    private String azon = "";
    private boolean atvallal = false;
    private int sorszamasID = 0;

    private String teljesitesIgazolasDatuma;
    private TeljesitesIgazolasModel teljesitesIgazolasModel;

    public Szamla(Szallito szallito, Vevo vevo, String sorszam, String kelt, String teljesites, String esedekesseg, String szamlaCsoportId, String valuta, double kozeparfolyam, String helyesbitett, String helyesbitettTeljesites, boolean deviza, String megjegyzes, String lablec, List<SzamlaTermek> termekek, boolean atvallal) {
        this.szallito = szallito;
        this.vevo = vevo;
        this.sorszam = sorszam;
        this.kelt = kelt;
        this.teljesites = teljesites;
        this.esedekesseg = esedekesseg;
        this.szamlaCsoportId = szamlaCsoportId;
        this.valuta = valuta;
        this.kozeparfolyam = kozeparfolyam;
        this.helyesbitett = helyesbitett;
        this.helyesbitettTeljesites = helyesbitettTeljesites;
        this.deviza = deviza;
        this.megjegyzes = megjegyzes;
        this.lablec = lablec;
        this.termekek = termekek;
        this.atvallal = atvallal;
    }

    public Szamla(String azon) {
        this.azon = azon;
        Query query = new Query.QueryBuilder()
                .select(
                        // szállító
                        "sz.szallito_nev, " //  0
                        + "sz.szallito_irsz, " //  1
                        + "sz.szallito_varos, " //  2
                        + "sz.szallito_utca, "  //  3
                                
                        + "sz.szallito_kozterulet, " //4
                        + "sz.szallito_hazszam, "  //5
                                
                        + "sz.szallito_adoszam, " //  6
                        + "sz.szallito_eu_adoszam, " //  7
                        + "sz.szallito_bankszamlaszam, " //  8
                        + "sz.szallito_egyeb, " //  9
                    // vevő
                        + "sz.nev, " //  10
                        + "sz.irsz, " //  11
                        + "sz.varos, " // 12
                        + "sz.utca, " // 13
                                
                        + "sz.kozterulet, " //14
                        + "sz.hazszam, " //15
                                
                        + "sz.orszag, " // 16
                        + "sz.telefon, " // 17
                        + "sz.email, " // 18
                        + "sz.fizetesi_mod, " // 19
                        + "sz.adoszam, " // 20
                        + "sz.eu_adoszam, " // 21
                        + "sz.bankszamlaszam, " // 22
                        + "sz.szamlan_megjelenik, " // 23
                        // számla adatok
                        + "sz.szamla_sorszam, " // 24
                        + "sz.fizetesi_mod, " // 25
                        + "sz.szamla_csoport, " // 26
                        + "sz.kelt, " // 27
                        + "sz.esedekesseg, " // 28
                        + "sz.teljesites, " // 29
                        + "sz.kifizetes, " // 30
                        + "sz.afa_ertek, " // 31
                        + "sz.tipus, " // 32
                        + "sz.megjegyzes, " // 33
                        + "sz.lablec, " // 34
                        + "sz.nyomtatva, " // 35
                        + "sz.valuta, " // 36
                        + "sz.kozeparfolyam, " // 37
                        + "sz.sorszamozasid, " // 38
                        + "sz.helyesbitett, " // 39
                        + "sz.deviza, " // 40
                        + "IFNULL((SELECT teljesites FROM szamlazo_szamla WHERE szamla_sorszam = sz.helyesbitett LIMIT 1), ''), " // 41
                        + "sz.atvallal," // 42
                        + "sz.sorszamozasid ") // 43
                .from("szamlazo_szamla sz ")
                .where("sz.azon = '" + azon + "'")
                .build();
        Object[][] select = App.db.select(query.getQuery());
        /*
	 * Szallito(String nev, String irsz, String varos, String utca, String adoszam, String euAdoszam, String bankszamlaszam, String egyeb)
         */
        this.szallito = new Szallito(
                String.valueOf(select[0][0]), // nev
                String.valueOf(select[0][1]), // irsz
                String.valueOf(select[0][2]), // varos
                String.valueOf(select[0][3]), // utca
                String.valueOf(select[0][4]),// kozterulet
                String.valueOf(select[0][5]),//hazszam
                String.valueOf(select[0][6]), // adoszam
                String.valueOf(select[0][8]), // bankszamlaszam
                String.valueOf(select[0][9]));   // egyeb
        /* 
	 * Vevo(String nev, String irsz, String varos, String utca, String orszag, String telefon, String email, int fizetesiMod, boolean fizetesiModKotelezo,
	 *	String esedekesseg, String adoszam, String euAdoszam, String bankszamlaszam, boolean szamlanMegjelenik) 
         */
        this.vevo = new Vevo(
                String.valueOf(select[0][10]), // nev
                String.valueOf(select[0][11]), // irsz
                String.valueOf(select[0][12]), // varos
                String.valueOf(select[0][13]), // utca
                String.valueOf(select[0][14]),// kozterulet
                String.valueOf(select[0][15]),//hazszam
                String.valueOf(select[0][16]), // orszag
                String.valueOf(select[0][17]), // telefon
                String.valueOf(select[0][18]), // email
                Integer.parseInt(String.valueOf(select[0][19])), // fizMod
                false, // fizetesiModKotelezo
                "0", // esedekesseg
                String.valueOf(select[0][20]), // adoszam
                String.valueOf(select[0][21]), // euAdoszam
                String.valueOf(select[0][22]), // bankszamlaszam
                String.valueOf(select[0][23]).equalsIgnoreCase("1"));// szamlanMegjelenik
        this.sorszam = String.valueOf(select[0][24]);
        this.szamlaCsoportId = String.valueOf(select[0][26]);
        this.kelt = String.valueOf(select[0][27]);
        this.esedekesseg = String.valueOf(select[0][28]);
        this.teljesites = String.valueOf(select[0][29]);
        this.tipus = Integer.parseInt(String.valueOf(select[0][32]));
        this.megjegyzes = String.valueOf(select[0][33]);
        this.lablec = String.valueOf(select[0][34]);
        this.nyomtatva = Integer.parseInt(String.valueOf(select[0][35]));
        this.valuta = (String.valueOf(select[0][36]).equalsIgnoreCase("Ft") || String.valueOf(select[0][36]).equalsIgnoreCase("huf") ? "Ft" : String.valueOf(select[0][36]).toUpperCase());
        this.kozeparfolyam = Double.parseDouble(String.valueOf(select[0][37]));
        this.helyesbitett = String.valueOf(select[0][39]);
        this.deviza = String.valueOf(select[0][40]).equalsIgnoreCase("1");
        this.helyesbitettTeljesites = String.valueOf(select[0][41]);
        this.atvallal = String.valueOf(select[0][42]).equalsIgnoreCase("1");
        this.sorszamasID = Functions.getIntFromObject(select[0][43]);

        this.termekek = new LinkedList<SzamlaTermek>();

        query = new Query.QueryBuilder()
                .select("id")
                .from("szamlazo_szamla_adatok")
                .where("azon = '" + azon + "'")
                .build();
        select = App.db.select(query.getQuery());
        for (int i = 0; i < select.length; i++) {
            termekek.add(new SzamlaTermek(String.valueOf(select[i][0])));
        }
    }

    public boolean isAtvallal() {
        return atvallal;
    }

    public void setAtvallal(boolean atvallal) {
        this.atvallal = atvallal;
    }

    public boolean isDeviza()
    {
        return deviza;
    }

    public String getEsedekesseg() {
        return esedekesseg;
    }

    public String getHelyesbitett() {
        return helyesbitett;
    }

    public String getHelyesbitettTeljesites() {
        return helyesbitettTeljesites;
    }

    public String getKelt() {
        return kelt;
    }

    public double getKozeparfolyam()
    {
        return kozeparfolyam;
    }

    public String getLablec() {
        return lablec;
    }

    public String getMegjegyzes() {
        return megjegyzes;
    }

    public int getNyomtatva() {
        return nyomtatva;
    }

    public String getSorszam() {
        return sorszam;
    }

    public Szallito getSzallito() {
        return szallito;
    }

    public String getSzamlaCsoportId() {
        return szamlaCsoportId;
    }

    public String getTeljesites() {
        return teljesites;
    }

    public List<SzamlaTermek> getTermekek() {
        return termekek;
    }

    public int getTipus() {
        return tipus;
    }

    public String getValuta() {
        return valuta;
    }

    public Vevo getVevo() {
        return vevo;
    }

    public String getAzon() {
        return azon;
    }

    public void setMegjegyzes(String megjegyzes) {
        this.megjegyzes = megjegyzes;
    }

    public void setTipus(int tipus) {
        this.tipus = tipus;
    }

    public void setHelyesbitettTeljesites(String helyesbitettTeljesites) {
        this.helyesbitettTeljesites = helyesbitettTeljesites;
    }

    public void setHelyesbitett(String helyesbitett) {
        this.helyesbitett = helyesbitett;
    }

    public void setNyomtatva(int nyomtatva) {
        this.nyomtatva = nyomtatva;
    }

    public List<Afa> getAfak(boolean termekDij) {
        afak = new LinkedList<Afa>();

        for (SzamlaTermek t : termekek) {
            int index = keresAfa(t.getAfa());
            if (index < 0) {
                // még nincs
                afak.add(new Afa(t.getAfa(), t.getNetto(isDeviza())));
            } else {
                afak.get(index).addNetto(t.getNetto(isDeviza()));
            }
            if (termekDij && t.getTermekDij() != null && atvallal == false) {
                index = keresAfa(27.0);
                if (index < 0) {
                    // még nincs
                    afak.add(new Afa(27.0, t.getTermekDij().getOsszTermekDijNetto(isDeviza())));
                } else {
                    afak.get(index).addNetto(t.getTermekDij().getOsszTermekDijNetto(isDeviza()));
                }
            }
        }
        return afak;
    }

    private int keresAfa(double afa) {
        for (int i = 0; i < afak.size(); i++) {
            if (afak.get(i).getAfa() == afa) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public String toString() {
        String result = "Számla\n" + sorszam + "\n" + szallito + "\n" + vevo + "\nKelt: " + kelt + "\nTeljesítés: " + teljesites + "\nEsedékesség: " + esedekesseg + "\n"
                + szamlaCsoportId + "\n" + (nyomtatva == 1 ? "igen" : "nem") + "\nTípus: " + tipus + "\n" + valuta + " = " + kozeparfolyam + "\n" + helyesbitett + "\n"
                + helyesbitettTeljesites + "\n" + (deviza ? "devizás" : "nem devizás") + "\n'" + megjegyzes + "'\n'" + lablec + "'\n\nSzámla termékek:\n";
        for (int i = 0; i < termekek.size(); i++) {
            result += termekek.get(i).toString();
        }
        result += "\n\nÁfák:\n";
        for (int i = 0; i < afak.size(); i++) {
            result += afak.get(i).toString();
        }
        return result;
    }

    public String getTeljesitesIgazolasDatuma() {
        return teljesitesIgazolasDatuma;
    }

    public void setTeljesitesIgazolasDatuma(String teljesitesIgazolasDatuma) {
        this.teljesitesIgazolasDatuma = teljesitesIgazolasDatuma;
    }

    public TeljesitesIgazolasModel getTeljesitesIgazolasModel() {
        return teljesitesIgazolasModel;
    }

    public void setTeljesitesIgazolasModel(TeljesitesIgazolasModel teljesitesIgazolasModel) {
        this.teljesitesIgazolasModel = teljesitesIgazolasModel;
    }

    public int getSorszamasID() {
        return sorszamasID;
    }

}
