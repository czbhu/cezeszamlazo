/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeszamlazo.model;

/**
 *
 * @author szekus
 */
public abstract class Model {
    protected String TABLE_NAME;
    protected Object[][] select;
    protected Object[] values;
   
    abstract void selectAllByQuery(String query);
    abstract void save();
    abstract void setValues();
    
    
    
}
