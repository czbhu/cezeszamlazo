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
public class Suppliers {

    private static String SupplierID = "100";

    public void setSupplierID(String supplierID) {
        System.out.println("SupplierID: " + supplierID);
        SupplierID = supplierID;
    }

    public String getSupplierID()
    {
        return this.SupplierID;
    }
    
}
