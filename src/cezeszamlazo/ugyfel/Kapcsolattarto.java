package cezeszamlazo.ugyfel;

import cezeszamlazo.App;

/**
 * Egy egyszerű osztály a kapcsolattartók kezelésére.
 *
 * @author Fejlesztés
 */
public class Kapcsolattarto
{
    /**
     * a kapcsolattartók adatbázisbeli táblája
     */
    public static final String TABLE = "szamlazo_contact";
    /**
     * az adatbázisbeli 'hirlevel' státusz
     */
    public static final int LEIRATKOZVA = 0, FELIRATKOZVA = 1;

    /**
     * kapcsolattartó azonosító
     */
    private int id = 0;
    /**
     * kapcsolattartó ügyfél azonosító, az Ugyfel.TABLE idegen kulcsa
     */
    private int ugyfelId = 0;
    /**
     * kapcsolattartó név
     */
    private String nev = "";
    /**
     * kapcsolattartó telefon
     */
    private String telefon = "";
    /**
     * kapcsolattartó email
     */
    private String email = "";
    /**
     * jelzés hogy a felhasználó fel van e iratkozva hírlevélre
     */
    private boolean hirlevel = true;

    private boolean penzugyes = true;

    public Kapcsolattarto()
    {
        
    }

    /**
     * 'id' alapján betölti az adatbázisból a kapcsolattartót ha az 'id' nulla
     * akkor egy új kapcsolattartót hoz létre az alapértelmezett kezdőértékekkel
     *
     * @param id a kapcsolattartó adatbázisbeli azonosítója
     */
    public Kapcsolattarto(int id)
    {
        Object[][] s = App.db.select("SELECT ugyfelid, nev, telefon, email, hirlevel, penzugyes FROM " + TABLE + " WHERE id = " + id);
        
        if (s.length != 0)
        {
            this.id = id;
            this.ugyfelId = Integer.parseInt(String.valueOf(s[0][0]));
            this.nev = String.valueOf(s[0][1]);
            this.telefon = String.valueOf(s[0][2]);
            this.email = String.valueOf(s[0][3]);
            this.hirlevel = String.valueOf(s[0][4]).equalsIgnoreCase("1");
            this.penzugyes = String.valueOf(s[0][5]).equalsIgnoreCase("1");
        }
    }

    /**
     * Alap konstruktor létrehozáshoz.
     *
     * @param id
     * @param ugyfelId
     * @param nev
     * @param telefon
     * @param email
     * @param hirlevel
     * @param penzugyes
     */
    public Kapcsolattarto(int id, int ugyfelId, String nev, String telefon, String email, boolean hirlevel, boolean penzugyes)
    {
        this.id = id;
        this.ugyfelId = ugyfelId;
        this.nev = nev;
        this.telefon = telefon;
        this.email = email;
        this.hirlevel = hirlevel;
        this.penzugyes = penzugyes;

    }

    /**
     * Kapcsolattartókat ad vissza egy tömbben.
     *
     * @param ids a kapcsolattartók azonosítói akikre kíváncsiak vagyunk
     * @return Kapfcsolattarto tömb
     */
    public static Kapcsolattarto[] getKapcsolattartokArray(int[] ids)
    {
        String w = "(";
        
        for (int i = 0; i < ids.length; i++)
        {
            w += ids[i] + ", ";
        }
        
        w += "0)";
        Object[][] s = App.db.select("SELECT id, ugyfelid, nev, telefon, email, hirlevel, penzugyes FROM " + TABLE + " WHERE id IN " + w);
        
        if (s.length != 0)
        {
            Kapcsolattarto[] kapcsolattartok = new Kapcsolattarto[s.length];
            for (int i = 0; i < s.length; i++) {
                kapcsolattartok[i] = new Kapcsolattarto(
                    Integer.parseInt(String.valueOf(s[i][0])),
                    Integer.parseInt(String.valueOf(s[i][1])),
                    String.valueOf(s[i][2]),
                    String.valueOf(s[i][3]),
                    String.valueOf(s[i][4]),
                    String.valueOf(s[0][5]).equalsIgnoreCase("1"),
                    String.valueOf(s[0][6]).equalsIgnoreCase("1"));
            }
            
            return kapcsolattartok;
        }
        else
        {
            return null;
        }
    }

    /**
     * Kapcsolattartók egyesítésére szolgáló metódus. Ha egy kapcsolattartó
     * többször került rögzítésre akkor összevonható egybe és az ajánlatkérései
     * és megrendelései is egy helyre kerülnek.
     *
     * @param kapcsolattartok az összevonni kívánt kapcsolattartók
     * @param kapcsolattarto azon kapcsolattartó akibe össze akarjuk vonni
     */
    public static void csoportositas(Kapcsolattarto[] kapcsolattartok, Kapcsolattarto kapcsolattarto)
    {
        String w = "(";
        String nevek = "";
        
        for (int i = 0; i < kapcsolattartok.length; i++)
        {
            if (kapcsolattartok[i].getId() != kapcsolattarto.getId())
            {
                w += kapcsolattartok[i].getId() + ", ";
                nevek += kapcsolattartok[i].getNev() + ", ";
            }
        }
        
        w += "0)";
        // az ajánlatkérések átadása az új kapcsolattartónak
        App.db.insert("UPDATE " + "pixi_ajanlatkeresek_adatai" + " SET kapcsolattarto_id = " + kapcsolattarto.getId() + " WHERE kapcsolattarto_id IN " + w, null);
        // az összes többi kapcsolattartó törlése, mivel már hozzájuk nem tartozik ajánlatkérés vagy megrendelés
        App.db.delete("DELETE FROM " + TABLE + " WHERE id IN " + w);
        // logolás a műveletről
        System.out.println("");
//        Log.addMsg(0, App.user.getId(), "Ügyfelek csoportosítása", "A következő kapcsolattartók: " + nevek + " csoportosítása egy kapcsolattartóvá: " + kapcsolattarto.toString());
    }

    /* GET / SET metódusok */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUgyfelId() {
        return ugyfelId;
    }

    public void setUgyfelId(int ugyfelId) {
        this.ugyfelId = ugyfelId;
    }

    public String getNev() {
        return nev;
    }

    public void setNev(String nev) {
        this.nev = nev;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isHirlevel() {
        return hirlevel;
    }

    public void setHirlevel(boolean hirlevel) {
        this.hirlevel = hirlevel;
    }

    public boolean isPenzugyes() {
        return penzugyes;
    }

    public void setPenzugyes(boolean penzugyes) {
        this.penzugyes = penzugyes;
    }

    /**
     * a kapcsolattartó mentése ha az 'id' null akkor INSERT egyébként UPDATE
     */
    public void mentes()
    {
        Object[] o = new Object[6];
        
        if (ugyfelId == 0)
        {
            Ugyfel uf = new Ugyfel();
            uf.setNev(nev);
            uf.mentes();
            ugyfelId = uf.getId();
        }
        
        o[0] = ugyfelId;
        o[1] = nev;
        o[2] = telefon;
        o[3] = email;
        o[4] = (hirlevel ? 1 : 0);
        o[5] = (penzugyes ? 1 : 0);
        
        if (id == 0)
        {
            // új mentése
            id = App.db.insert("INSERT INTO " + TABLE + " (ugyfelid, nev, telefon, email, hirlevel, penzugyes) "
                    + "VALUES (?, ?, ?, ?, ?, ?)", o);
            // log bejegyzés a létrehozásról
//          Log.addMsg(0, PixiRendszer.user.getId(), "Új kapcsolattartó létrehozás", toString());
        }
        else
        {
            // meglévő módosítása
            App.db.insert("UPDATE " + TABLE + " SET ugyfelid = ?, nev = ?, telefon = ?, email = ?, hirlevel = ? , penzugyes = ? "
                    + "WHERE id = " + id, o);
            // log bejegyzés a módosításról
//          Log.addMsg(0, PixiRendszer.user.getId(), "Kapcsolattartó módosítása", toString());
        }
    }

    /**
     * a kapcsolattartó végleges törlése
     */
    public void torles()
    {
        App.db.delete("DELETE FROM " + TABLE + " WHERE id = " + id);
//        Log.addMsg(0, PixiRendszer.user.getId(), "Kapcsolattartó törlése", toString());
    }

    /**
     * A kapcsolattartó validálása, a név megadása kötelező
     *
     * @return ha hibás akkor a hibaüzenet, egyébként üres String
     */
    public String valid()
    {
        if (nev.isEmpty())
        {
            return "Nincs név megadva!";
        }
        
        return "";
    }

    @Override
    public String toString()
    {
        return "ID:" + id + "; " + nev + "; " + telefon + "; " + email;
    }
}