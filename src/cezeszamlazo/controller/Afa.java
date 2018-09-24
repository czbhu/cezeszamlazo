/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeszamlazo.controller;

/**
 *
 * @author adam.papp
 */
public class Afa {
    
    /**
     * ÁFA értéke.
     */
    private double afa;
    /**
     * Adott ÁFÁ-hoz tartozó nettó.
     */
    private double netto;

    public Afa(double afa, double netto) {
	this.afa = afa;
	this.netto = netto;
    }

    public double getAfa() {
	return afa;
    }

    public void setAfa(double afa) {
	this.afa = afa;
    }

    public double getAfaErtek(boolean deviza) {
	return (deviza ? Math.round(netto * afa) / 100.0 : Math.round(netto * afa / 100.0));
    }

    public double getNetto(boolean deviza) {
	return (deviza ? netto : Math.round(netto));
    }

    public void setNetto(double netto) {
	this.netto = netto;
    }
    
    public void addNetto(double netto) {
	this.netto += netto;
    }
    
    public double getBrutto(boolean deviza) {
	return getNetto(deviza) + getAfaErtek(deviza);
    }

    @Override
    public String toString() {
	return this.afa + "%\t" + netto + "\t" + getAfaErtek(true) + "\t" + getBrutto(true) + "\n";
    }    
    
}
