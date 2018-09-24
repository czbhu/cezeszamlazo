/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeszamlazo;

/**
 *
 * @author czbhu
 */
public class PrinterGetSet {

    private static String PrinterName = "100";

    public void setPrinterName(String printerName) {
        System.out.println("SupplierID: " + printerName);
        PrinterName = printerName;
    }

    public String getPrinterName()
    {
        return this.PrinterName;
    }
    
}