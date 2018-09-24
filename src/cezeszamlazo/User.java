package cezeszamlazo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

/**
 *
 * @author Papp Ádám - Ceze Reklam
 */
public class User implements Serializable {

    private int id = 0;
    private String nev = "";
    private String usernev = "";
    private String jelszo = "";
    private int csoportid = 0;
    private String ceg = "";
//    private List<SzamlaTermek> termekek;
//    private List<Ugyfel> ugyfelek;

    public User(Object[][] o) {
        try {
            this.id = Integer.parseInt(String.valueOf(o[0][0]));
            this.nev = String.valueOf(o[0][1]);
            this.usernev = String.valueOf(o[0][2]);
            this.jelszo = String.valueOf(o[0][3]);
            this.csoportid = Integer.parseInt(String.valueOf(o[0][4]));
            this.ceg = String.valueOf(o[0][5]);
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("ArrayIndexOutOfBoundsException váltódott ki!");
            ex.printStackTrace();
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
    
//    public List<SzamlaTermek> getTermekek(){
//        return termekek;
//    }
//    
//    public List<Ugyfel> getUgyfelek(){
//        return ugyfelek;
//    }
//    
//    public void addTermek(SzamlaTermek t){
//        termekek.add(t);
//    }
//    
//    public void addUgyfel(Ugyfel m){
//        ugyfelek.add(m);
//    }
//    
//    public void removeTermek(int index){
//        termekek.remove(index);
//    }
//    
//    public void removeUgyfel(int index){
//        ugyfelek.remove(index);
//    }
    
}