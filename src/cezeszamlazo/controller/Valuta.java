package cezeszamlazo.controller;

public class Valuta {
    
    private String valuta,
	    valutaNev;
    private Double kozeparfolyam;

    public Valuta(String valuta, String valutaNev, Double kozeparfolyam) {
        this.valuta = valuta;
	this.valutaNev = valutaNev;
        this.kozeparfolyam = kozeparfolyam;
    }
    
    public Valuta(Object o) {
	if (o instanceof Valuta) {
	    Valuta v = (Valuta) o;
	    this.valuta = v.getValuta();
	    this.valutaNev = v.getValutaNev();
	    this.kozeparfolyam = v.getKozeparfolyam();
	}
    }

    public String getValutaNev() {
	return valutaNev;
    }

    public Double getKozeparfolyam() {
        return kozeparfolyam;
    }

    public String getValuta() {
        return valuta;
    }
    
    @Override
    public String toString() {
        return valutaNev + " (" + valuta + ")";
    }
    
}