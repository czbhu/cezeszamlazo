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
public class DataObject {
    
    private Object[][] values;
    
    
    private DataObject(Object[][] values) {
        this.values = values;
    }
    
    private DataObject() {
    }
    
    public static DataObject create(){
        return new DataObject();
    }
    
    public static DataObject create(Object[][] values){
        return new DataObject(values);
    }

    public Object[][] getValues() {
        return values;
    }

    public void setValues(Object[][] values) {
        this.values = values;
    }

    public void  printValues() {
//        System.out.println("this.values[0].length: " + this.values.length);
//        ;
        for (int i = 0; i < this.values.length; i++) {
            for (int j = 0; j < this.values[i].length; j++) {
                System.out.println(String.valueOf(values[i][j]));
            }
        }
    }
    
    
    

}
