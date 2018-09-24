/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeszamlazo.controller;

import cezeszamlazo.App;
import cezeszamlazo.database.Query;
import java.util.LinkedList;

/**
 *
 * @author pappadam
 */
public class ProForma {

    /**
     * az adatbázis tábla neve
     */
    public static final String TABLE = "szamlazo_pro_forma";
    /**
     * fizetési módok
     */
    public static final int KESZPENZ = 0, ATUTALAS = 1, UTANVET = 2;
    /**
     * az adatbázis táblában lévő mezők
     */
    private int id;
    private String sorszam;
    private int fizetesiMod;
    private String kelt;
    private String esedekesseg;
    private String kifizetes;
    private double netto;
    private double afaErtek;
    private int tipus;
    private String nev;
    private int irsz;
    private String varos;
    private String utca;
    private String kozterulet;
    private String hazszam;
    private String orszag;
    private String telefon;
    private String email;
    private String bankszamlaszam;
    private boolean szamlanMegjelenik;
    private String adoszam;
    private String euAdoszam;
    private String megjegyzes;
    private String lablec;
    private String azon;
    private boolean nyomtatva;
    private String szallitoNev;
    private String szallitoIrsz;
    private String szallitoVaros;
    private String szallitoUtca;
    private String szallitoKozterulet;
    private String szallitoHazszam;
    private String szallitoAdoszam;
    private String szallitoEuAdoszam;
    private String szallitoBankszamlaszam;
    private String szallitoEgyeb;
    private String szallitoSzamlaLablec;
    /**
     * a pro forma díjbekérőhöz tartozó termékek
     */
    private LinkedList<ProFormaTermek> termekek;

    public ProForma() {
        this(0);
    }

    public ProForma(int id) {
        if (id != 0) {
            Query query = new Query.QueryBuilder()
                    .select("sorszam, " //0
                            + "fizetesi_mod, " //1
                            + "kelt, " //2
                            + "esedekesseg, " //3
                            + "kifizetes, " //4
                            + "netto, " //5
                            + "afa_ertek, " //6
                            + "tipus, " //7
                            + "nev, " //8
                            + "irsz, " //9
                            + "varos, " //10
                            + "utca, " //11 
                            + "kozterulet, " //12
                            + "hazszam, " //13
                            + "orszag, " //14
                            + "telefon, " //15
                            + "email, " //16
                            + "bankszamlaszam, " //17
                            + "szamlan_megjelenik, " //18
                            + "adoszam, " //19
                            + "eu_adoszam, " //20
                            + "megjegyzes, " //21
                            + "lablec, " //22
                            + "azon, " //23
                            + "nyomtatva, " //24
                            + "szallito_nev, " //25
                            + "szallito_irsz, " //26
                            + "szallito_varos, " //27
                            + "szallito_utca, " //28
                            + "szallito_kozterulet" //29
                            + "szallito_hazszam" //30
                            + "szallito_adoszam, " //31
                            + "szallito_eu_adoszam, " //32
                            + "szallito_bankszamlaszam, " //33
                            + "szallito_egyeb, " //34
                            + "szallito_szamla_labelec ") //35
                    .from(TABLE)
                    .where("id = " + id)
                    .build();
            Object[][] select = App.db.select(query.getQuery());

            if (select.length != 0) {
                this.id = id;
                this.sorszam = String.valueOf(select[0][0]);
                this.fizetesiMod = Integer.parseInt(String.valueOf(select[0][1]));
                this.kelt = String.valueOf(select[0][2]);
                this.esedekesseg = String.valueOf(select[0][3]);
                this.kifizetes = String.valueOf(select[0][4]);
                this.netto = Double.parseDouble(String.valueOf(select[0][5]));
                this.afaErtek = Double.parseDouble(String.valueOf(select[0][6]));
                this.tipus = Integer.parseInt(String.valueOf(select[0][7]));
                this.nev = String.valueOf(select[0][8]);
                this.irsz = Integer.parseInt(String.valueOf(select[0][9]));
                this.varos = String.valueOf(select[0][10]);
                this.utca = String.valueOf(select[0][11]);
                this.kozterulet = String.valueOf(select[0][12]);
                this.hazszam = String.valueOf(select[0][13]);
                this.orszag = String.valueOf(select[0][14]);
                this.telefon = String.valueOf(select[0][15]);
                this.email = String.valueOf(select[0][16]);
                this.bankszamlaszam = String.valueOf(select[0][17]);
                this.szamlanMegjelenik = String.valueOf(select[0][18]).equalsIgnoreCase("1");
                this.adoszam = String.valueOf(select[0][19]);
                this.euAdoszam = String.valueOf(select[0][20]);
                this.megjegyzes = String.valueOf(select[0][21]);
                this.lablec = String.valueOf(select[0][22]);
                this.azon = String.valueOf(select[0][23]);
                this.nyomtatva = String.valueOf(select[0][24]).equalsIgnoreCase("1");
                this.szallitoNev = String.valueOf(select[0][25]);
                this.szallitoIrsz = String.valueOf(select[0][26]);
                this.szallitoVaros = String.valueOf(select[0][27]);
                this.szallitoUtca = String.valueOf(select[0][28]);
                this.szallitoKozterulet = String.valueOf(select[0][29]);
                this.szallitoHazszam = String.valueOf(select[0][30]);
                this.szallitoAdoszam = String.valueOf(select[0][31]);
                this.szallitoEuAdoszam = String.valueOf(select[0][32]);
                this.szallitoBankszamlaszam = String.valueOf(select[0][33]);
                this.szallitoEgyeb = String.valueOf(select[0][34]);
                this.szallitoSzamlaLablec = String.valueOf(select[0][35]);
            } else {
                init();
            }
        } else {
            init();
        }
    }

    public ProForma(int id, String sorszam, int fizetesiMod, String kelt, String esedekesseg, String kifizetes, double netto, double afaErtek,
            int tipus, String nev, int irsz, String varos, String utca, String kozterulet, String hazszam, String orszag, String telefon, String email, String bankszamlaszam,
            boolean szamlanMegjelenik, String adoszam, String euAdoszam, String megjegyzes, String lablec, String azon, boolean nyomtatva,
            String szallitoNev, String szallitoIrsz, String szallitoVaros, String szallitoUtca, String szallitoKozterulet, String szallitoHazszam, String szallitoAdoszam, String szallitoEuAdoszam,
            String szallitoBankszamlaszam, String szallitoEgyeb, String szallitoSzamlaLablec) {
        this.id = id;
        this.sorszam = sorszam;
        this.fizetesiMod = fizetesiMod;
        this.kelt = kelt;
        this.esedekesseg = esedekesseg;
        this.kifizetes = kifizetes;
        this.netto = netto;
        this.afaErtek = afaErtek;
        this.tipus = tipus;
        this.nev = nev;
        this.irsz = irsz;
        this.varos = varos;
        this.utca = utca;
        this.kozterulet = kozterulet;
        this.hazszam = hazszam;
        this.orszag = orszag;
        this.telefon = telefon;
        this.email = email;
        this.bankszamlaszam = bankszamlaszam;
        this.szamlanMegjelenik = szamlanMegjelenik;
        this.adoszam = adoszam;
        this.euAdoszam = euAdoszam;
        this.megjegyzes = megjegyzes;
        this.lablec = lablec;
        this.azon = azon;
        this.nyomtatva = nyomtatva;
        this.szallitoNev = szallitoNev;
        this.szallitoIrsz = szallitoIrsz;
        this.szallitoVaros = szallitoVaros;
        this.szallitoUtca = szallitoUtca;
        this.szallitoKozterulet = szallitoKozterulet;
        this.szallitoHazszam = szallitoHazszam;
        this.szallitoAdoszam = szallitoAdoszam;
        this.szallitoEuAdoszam = szallitoEuAdoszam;
        this.szallitoBankszamlaszam = szallitoBankszamlaszam;
        this.szallitoEgyeb = szallitoEgyeb;
        this.szallitoSzamlaLablec = szallitoSzamlaLablec;
    }

    private void init() {
        this.id = 0;
        this.sorszam = "";
        this.fizetesiMod = 0;
        this.kelt = "0000-00-00";
        this.esedekesseg = "0000-00-0";
        this.kifizetes = "0000-00-00";
        this.netto = 0.0;
        this.afaErtek = 0.0;
        this.tipus = 0;
        this.nev = "";
        this.irsz = 0;
        this.varos = "";
        this.utca = "";
        this.kozterulet = "";
        this.hazszam = "";
        this.orszag = "";
        this.telefon = "";
        this.email = "";
        this.bankszamlaszam = "";
        this.szamlanMegjelenik = false;
        this.adoszam = "";
        this.euAdoszam = "";
        this.megjegyzes = "";
        this.lablec
                = "A pénzügyi teljesítést követően eljuttatjuk Önhöz a terméket és a végleges számlát!\n"
                + "\n"
                + "(A Pro Forma számla számviteli elszámolás és ÁFA visszaigénylésre nem jogosít!)\n"
                + "\n"
                + "Átvette:";
        this.azon = "";
        this.nyomtatva = false;
        this.szallitoNev = "";
        this.szallitoIrsz = "";
        this.szallitoVaros = "";
        this.szallitoUtca = "";
        this.szallitoKozterulet = "";
        this.szallitoHazszam = "";
        this.szallitoAdoszam = "";
        this.szallitoEuAdoszam = "";
        this.szallitoBankszamlaszam = "";
        this.szallitoEgyeb = "";
        this.szallitoSzamlaLablec = "";

        this.termekek = new LinkedList<ProFormaTermek>();
    }

    /**
     * GET / SET metódusok
     */
    public String getAdoszam() {
        return adoszam;
    }

    public void setAdoszam(String adoszam) {
        this.adoszam = adoszam;
    }

    public double getAfaErtek() {
        return afaErtek;
    }

    public void setAfaErtek(double afaErtek) {
        this.afaErtek = afaErtek;
    }

    public String getAzon() {
        return azon;
    }

    public void setAzon(String azon) {
        this.azon = azon;
    }

    public String getBankszamlaszam() {
        return bankszamlaszam;
    }

    public void setBankszamlaszam(String bankszamlaszam) {
        this.bankszamlaszam = bankszamlaszam;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEsedekesseg() {
        return esedekesseg;
    }

    public void setEsedekesseg(String esedekesseg) {
        this.esedekesseg = esedekesseg;
    }

    public String getEuAdoszam() {
        return euAdoszam;
    }

    public void setEuAdoszam(String euAdoszam) {
        this.euAdoszam = euAdoszam;
    }

    public int getFizetesiMod() {
        return fizetesiMod;
    }

    public void setFizetesiMod(int fizetesiMod) {
        this.fizetesiMod = fizetesiMod;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIrsz() {
        return irsz;
    }

    public void setIrsz(int irsz) {
        this.irsz = irsz;
    }

    public String getKelt() {
        return kelt;
    }

    public void setKelt(String kelt) {
        this.kelt = kelt;
    }

    public String getKifizetes() {
        return kifizetes;
    }

    public void setKifizetes(String kifizetes) {
        this.kifizetes = kifizetes;
    }

    public String getLablec() {
        return lablec;
    }

    public void setLablec(String lablec) {
        this.lablec = lablec;
    }

    public String getMegjegyzes() {
        return megjegyzes;
    }

    public void setMegjegyzes(String megjegyzes) {
        this.megjegyzes = megjegyzes;
    }

    public double getNetto() {
        return netto;
    }

    public void setNetto(double netto) {
        this.netto = netto;
    }

    public String getNev() {
        return nev;
    }

    public void setNev(String nev) {
        this.nev = nev;
    }

    public boolean isNyomtatva() {
        return nyomtatva;
    }

    public void setNyomtatva(boolean nyomtatva) {
        this.nyomtatva = nyomtatva;
    }

    public String getOrszag() {
        return orszag;
    }

    public void setOrszag(String orszag) {
        this.orszag = orszag;
    }

    public String getSorszam() {
        return sorszam;
    }

    public void setSorszam(String sorszam) {
        this.sorszam = sorszam;
    }

    public String getSzallitoAdoszam() {
        return szallitoAdoszam;
    }

    public void setSzallitoAdoszam(String szallitoAdoszam) {
        this.szallitoAdoszam = szallitoAdoszam;
    }

    public String getSzallitoBankszamlaszam() {
        return szallitoBankszamlaszam;
    }

    public void setSzallitoBankszamlaszam(String szallitoBankszamlaszam) {
        this.szallitoBankszamlaszam = szallitoBankszamlaszam;
    }

    public String getSzallitoEgyeb() {
        return szallitoEgyeb;
    }

    public void setSzallitoEgyeb(String szallitoEgyeb) {
        this.szallitoEgyeb = szallitoEgyeb;
    }

    public String getSzallitoEuAdoszam() {
        return szallitoEuAdoszam;
    }

    public void setSzallitoEuAdoszam(String szallitoEuAdoszam) {
        this.szallitoEuAdoszam = szallitoEuAdoszam;
    }

    public String getSzallitoIrsz() {
        return szallitoIrsz;
    }

    public void setSzallitoIrsz(String szallitoIrsz) {
        this.szallitoIrsz = szallitoIrsz;
    }

    public String getSzallitoNev() {
        return szallitoNev;
    }

    public void setSzallitoNev(String szallitoNev) {
        this.szallitoNev = szallitoNev;
    }

    public String getSzallitoUtca() {
        return szallitoUtca;
    }

    public void setSzallitoUtca(String szallitoUtca) {
        this.szallitoUtca = szallitoUtca;
    }
    
    public String getSzallitoKozterulet() {
        return szallitoKozterulet;
    }

    public void setSzallitoKozterulet(String szallitoKozterulet) {
        this.szallitoKozterulet = szallitoKozterulet;
    }
    
    public String getSzallitoHazszam() {
        return szallitoHazszam;
    }

    public void setSzallitoHazszam(String szallitoHazszam) {
        this.szallitoHazszam = szallitoHazszam;
    }

    public String getSzallitoVaros() {
        return szallitoVaros;
    }

    public void setSzallitoVaros(String szallitoVaros) {
        this.szallitoVaros = szallitoVaros;
    }

    public boolean isSzamlanMegjelenik() {
        return szamlanMegjelenik;
    }

    public void setSzamlanMegjelenik(boolean szamlanMegjelenik) {
        this.szamlanMegjelenik = szamlanMegjelenik;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public int getTipus() {
        return tipus;
    }

    public void setTipus(int tipus) {
        this.tipus = tipus;
    }

    public String getUtca() {
        return utca;
    }

    public void setUtca(String utca) {
        this.utca = utca;
    }
    
    public String getKozterulet() {
        return kozterulet;
    }

    public void setKozterulet(String kozterulet) {
        this.kozterulet = kozterulet;
    }
    
    public String getHazszam() {
        return hazszam;
    }

    public void setHazszam(String hazszam) {
        this.hazszam = hazszam;
    }

    public String getVaros() {
        return varos;
    }

    public void setVaros(String varos) {
        this.varos = varos;
    }

    public Szallito getSzallito() {
        return new Szallito(szallitoNev, szallitoIrsz, szallitoVaros, szallitoUtca, szallitoKozterulet, szallitoHazszam, szallitoAdoszam, szallitoBankszamlaszam, szallitoEgyeb, szallitoSzamlaLablec);
    }

    public void setSzallito(Szallito szallito) {
        szallitoNev = szallito.getNev();
        szallitoIrsz = szallito.getIrsz();
        szallitoVaros = szallito.getVaros();
        szallitoUtca = szallito.getAddress();
        szallitoKozterulet = szallito.getKozterulet();
        szallitoHazszam = szallito.getHazszam();
        szallitoAdoszam = szallito.getAdoszam();
        szallitoBankszamlaszam = szallito.getBankszamlaszam();
        szallitoEgyeb = szallito.getMegjegyzes();
        szallitoSzamlaLablec = szallito.getSzamlaLablec();
    }

    public LinkedList<ProFormaTermek> getTermekek() {
        return termekek;
    }

    public void setTermekek(LinkedList<ProFormaTermek> termekek) {
        this.termekek = termekek;
    }

    public String getSzallitoSzamlaLablec() {
        return szallitoSzamlaLablec;
    }

    public void setSzallitoSzamlaLablec(String szallitoSzamlaLablec) {
        this.szallitoSzamlaLablec = szallitoSzamlaLablec;
    }

    public void mentes() {
        if (id == 0) {
        } else {
        }
    }
}
