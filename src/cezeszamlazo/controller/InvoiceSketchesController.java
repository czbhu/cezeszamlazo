package cezeszamlazo.controller;

import cezeszamlazo.model.InvoiceSketchesModel;

/**
 * @author Tomy
 */

public class InvoiceSketchesController
{
    private InvoiceSketchesModel model = new InvoiceSketchesModel();
    
    public Object [][] getSketches()
    {
        return model.getSketches();
    }

    public Object [][] getProducts(String indentifier)
    {
        return model.getProducts(indentifier);
    }
}