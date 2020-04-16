package cezeszamlazo.databasemodel;

/**
 *
 * @author szekus
 */
public abstract class DatabaseModel
{
    protected String TABLE_NAME;
    protected int id;
    protected DataObject[][] dataObject;
    
    abstract void selectByQuery(String query);
    abstract void save();
    abstract void setValues();
}