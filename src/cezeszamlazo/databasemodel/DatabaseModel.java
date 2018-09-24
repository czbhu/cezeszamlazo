/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeszamlazo.databasemodel;

/**
 *
 * @author szekus
 */
public abstract class DatabaseModel {
    
    protected String TABLE_NAME;
    protected int id;
    protected DataObject[][] dataObject;
    
    abstract void selectByQuery(String query);
    abstract void save();
    abstract void setValues();
}
