package cezeszamlazo.ugyfel;

import cezeszamlazo.App;

/**
 * Ügyfelek létrehozására, betöltésére, módosítására szolgáló osztály.
 *
 * @author Fejlesztés
 */
public class Ugyfel
{
    public static final String TABLE = "pixi_ugyfel";

    /**
     * ügyfél azonosító
     */
    private int id = 0;
    /**
     * ügyfél név
     */
    private String nev = "";
    /**
     * ügyfél irsz
     */
    private String irsz = "";
    /**
     * ügyfél város
     */
    private String varos = "";
    /**
     * ügyfél utca, hsz.
     */
    private String utca = "";
    /**
     * ügyfél adószám
     */
    private String adoszam = "";
    /**
     * ügyfél bankszámlaszám
     */
    private String bankszamlaszam = "";
    /**
     * felhasználó azonosító, aki létrehozta
     */
    private int userid;
    /**
     * az ügyfél százalékos kedvezménye amit az optimális árból kell levonni, de az ajánlott ár nem lehet kisebb a minimum árnál
     */
    private int kedvezmeny = 0;

    /**
     * alap konstruktor
     */
    public Ugyfel()
    {
        
    }

    /**
     * meglévő ügyfél betöltése adatbázisból 'id' alapján
     *
     * @param id
     */
    public Ugyfel(int id)
    {
        Object[][] s = App.db.select("SELECT nev, irsz, varos, utca, adoszam, bankszamlaszam, userid, kedvezmeny FROM " + TABLE + " WHERE id = " + id);
        
        if (s.length != 0)
        {
            this.id = id;
            this.nev = String.valueOf(s[0][0]);
            this.irsz = String.valueOf(s[0][1]);
            this.varos = String.valueOf(s[0][2]);
            this.utca = String.valueOf(s[0][3]);
            this.adoszam = String.valueOf(s[0][4]);
            this.bankszamlaszam = String.valueOf(s[0][5]);
            this.userid = Integer.parseInt(String.valueOf(s[0][6]));
            this.kedvezmeny = Integer.parseInt(String.valueOf(s[0][7]));
        }
        else
        {
            // minden alapállapotban, azaz új ügyfél
        }
    }

    /**
     * Ügyfél létrehozására szolgáló konstruktor
     * 
     * @param id
     * @param nev
     * @param irsz
     * @param varos
     * @param utca
     * @param adoszam
     * @param bankszamlaszam
     * @param userid
     * @param kedvezmeny 
     */
    public Ugyfel(int id, String nev, String irsz, String varos, String utca, String adoszam, String bankszamlaszam, int userid, int kedvezmeny)
    {
        this.id = id;
        this.nev = nev;
        this.irsz = irsz;
        this.varos = varos;
        this.utca = utca;
        this.adoszam = adoszam;
        this.bankszamlaszam = bankszamlaszam;
        this.userid = userid;
        this.kedvezmeny = kedvezmeny;
    }

    /**
     * Kedvezmény lekérdezése kapcsolattartó azonosító alapján.
     * Azaz megkeressük egy kapcsolattartó ügyfelét és annak a kedvezményével térünk vissza.
     * 
     * @param kapcsolattartoId azon kapcsolattartó azonosítója akinek a kedvezményére vagyunk kíváncsiak
     * @return a kedvezmény mértéke
     */
    public static int getKedvezmeny(int kapcsolattartoId)
    {
        Object[][] s = App.db.select("SELECT discount FROM " + TABLE + " WHERE id = (SELECT customerID FROM szamlazo_contact WHERE id = " + kapcsolattartoId + ")");
        
        if (s.length != 0)
        {
            return Integer.parseInt(String.valueOf(s[0][0]));
        }
        else
        {
            return 0;
        }
    }

    /**
     * Ügyfelek lekérdezése csoportosan.
     * 
     * @param ids a keresett ügyfelek azonosítói
     * @return egy egydimenziós Ugyfel tomb melyben a keresett Ugyfelek vannak betöltve
     */
    public static Ugyfel[] getUgyfelekArray(int[] ids)
    {
        String w = "(";
        
        for (int i = 0; i < ids.length; i++)
        {
            w += ids[i] + ", ";
        }
        
        w += "0)";
        Object[][] s = App.db.select("SELECT id, nev, irsz, varos, utca, adoszam, bankszamlaszam, userid, kedvezmeny FROM " + TABLE + " WHERE id IN " + w);
        
        if (s.length != 0)
        {
            Ugyfel[] ugyfelek = new Ugyfel[s.length];
            for (int i = 0; i < s.length; i++) {
                ugyfelek[i] = new Ugyfel(Integer.parseInt(String.valueOf(s[i][0])), String.valueOf(s[i][1]), String.valueOf(s[i][2]), String.valueOf(s[i][3]), String.valueOf(s[i][4]), String.valueOf(s[i][5]), String.valueOf(s[i][6]), Integer.parseInt(String.valueOf(s[i][7])), Integer.parseInt(String.valueOf(s[i][8])));
            }
            return ugyfelek;
        }
        else
        {
            return null;
        }
    }

    /**
     * Ügyfelek csoportosítása. Ha többször lett létrehozva egy ügyfél, vagy esetleg más néven, stb. összevonásra van lehetőség.
     * 
     * @param ugyfelek az összevonni kívánt ügyfelek
     * @param ugyfel azon ügyfél melybe össze akarjuk vonni
     */
    public static void csoportositas(Ugyfel[] ugyfelek, Ugyfel ugyfel)
    {
        String w = "(";
        String nevek = "";
        // a feltétel legenerálása
        for (int i = 0; i < ugyfelek.length; i++)
        {
            if (ugyfelek[i].getId() != ugyfel.getId())
            {
                w += ugyfelek[i].getId() + ", ";
                nevek += ugyfelek[i].getNev() + ", ";
            }
        }
        
        w += "0)";
        // frissíteni az összes olyan kapcsolattartó ügyfél azonosítóját az újra,
        // melyek benne vannak az összevonni kívánt tömbben
        App.db.insert("UPDATE " + Kapcsolattarto.TABLE + " SET ugyfelid = " + ugyfel.getId() + " WHERE ugyfelid IN " + w, null);
        // miután át lettek adva az ügyfelek kapcsolattartói ezen ügyfelek végeleges törlése
        App.db.delete("DELETE FROM " + TABLE + " WHERE id IN " + w);
//      Log.addMsg(0, PixiRendszer.user.getId(), "Ügyfelek csoportosítása", "A következő ügyfelek: " + nevek + " csoportosítása egy ügyféllé: " + ugyfel.toString());
    }

    /* GET / SET metódusok */
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNev() {
        return nev;
    }

    public void setNev(String nev) {
        this.nev = nev;
    }

    public String getIrsz() {
        return irsz;
    }

    public void setIrsz(String irsz) {
        this.irsz = irsz;
    }

    public String getVaros() {
        return varos;
    }

    public void setVaros(String varos) {
        this.varos = varos;
    }

    public String getUtca() {
        return utca;
    }

    public void setUtca(String utca) {
        this.utca = utca;
    }

    public String getAdoszam() {
        return adoszam;
    }

    public void setAdoszam(String adoszam) {
        this.adoszam = adoszam;
    }

    public String getBankszamlaszam() {
        return bankszamlaszam;
    }

    public void setBankszamlaszam(String bankszamlaszam) {
        this.bankszamlaszam = bankszamlaszam;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getKedvezmeny() {
        return kedvezmeny;
    }

    public void setKedvezmeny(int kedvezmeny) {
        this.kedvezmeny = kedvezmeny;
    }

    /**
     * Ügyfél mentése
     * ha 'id' nulla akkor insert
     * egyébként update
     */
    public void mentes()
    {
        Object[] o = new Object[8];
        o[0] = nev;
        o[1] = irsz;
        o[2] = varos;
        o[3] = utca;
        o[4] = adoszam;
        o[5] = bankszamlaszam;
        o[6] = userid;
        o[7] = kedvezmeny;
        
        if (id == 0)
        {
            o[6] = App.user.getId();
            // új mentése
            id = App.db.insert("INSERT INTO " + TABLE + " (nev, irsz, varos, utca, adoszam, bankszamlaszam, userid, kedvezmeny) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)", o);
//          PixiHelper.getInstance().updatePixiUgyfel();
//          Log.addMsg(0, PixiRendszer.user.getId(), "Új ügyfél létrehozása", toString());
        }
        else
        {
//           meglévő módosítása
            App.db.insert("UPDATE " + TABLE + " SET nev = ?, irsz = ?, varos = ?, utca = ?, adoszam = ?, bankszamlaszam = ?, userid = ?, kedvezmeny = ? "
                    + "WHERE id = " + id, o);
//          PixiHelper.getInstance().updatePixiUgyfel();
//          Log.addMsg(0, PixiRendszer.user.getId(), "Ügyfél módosítása", toString());
        }
    }

    /**
     * Validálás, kötelező nevet megadni.
     * @return a hiba, ha minden rendben akkor üres String
     */
    public String valid()
    {
        if (nev.isEmpty())
        {
            return "Nincs név megadva!";
        }
        
        return "";
    }

    /**
     * Ügyfél végleges törlése.
     */
    public void torles()
    {
        App.db.delete("DELETE FROM " + TABLE + " WHERE id = " + id);
//      Log.addMsg(0, PixiRendszer.user.getId(), "Ügyfél törlése", toString());
    }

    @Override
    public String toString()
    {
        return "ID:" + id + "; " + nev + "; " + irsz + " " + varos + ", " + utca + "; " + adoszam + "; " + bankszamlaszam + "; " + kedvezmeny + "%";
    }
}