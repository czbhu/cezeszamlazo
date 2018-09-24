/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeszamlazo.controller;

import cezeszamlazo.database.Query;
import cezeszamlazo.model.Field;
import java.util.ArrayList;

/**
 *
 * @author szekus
 */
class Controller {

    protected ArrayList<cezeszamlazo.model.Field> fields;
    Query query;

    public Controller() {

    }

    public Object getFieldValue(String fieldName) {
        for (Field field : fields) {
            if (field.getName().equals(fieldName)) {
                return field.getValue();
            }
        }
        return null;
    }
    
    public void setFieldValue(String fieldName, Object value) {
        for (Field field : fields) {
            if (field.getName().equals(fieldName)) {
                 field.setValue(value);
            }
        }
    }
    
    public void save() {
        for (Field field : fields) {
            
        }
    }
}
