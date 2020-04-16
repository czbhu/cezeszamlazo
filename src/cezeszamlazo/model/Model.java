package cezeszamlazo.model;

/**
 * @author szekus
 */
public abstract class Model
{
    protected String TABLE_NAME;
    protected Object[][] select;
    protected Object[] values;
   
    abstract void selectAllByQuery(String query);
    abstract void save();
    abstract void setValues();
}