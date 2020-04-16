package cezeszamlazo.databasemodel;

/**
 *
 * @author szekus
 */
public class DataObject
{ 
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

    public void  printValues()
    {
        for (int i = 0; i < this.values.length; i++)
        {
            for (int j = 0; j < this.values[i].length; j++)
            {
                System.out.println(String.valueOf(values[i][j]));
            }
        }
    }
}