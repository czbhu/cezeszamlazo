package cezeszamlazo.database;

/**
 *
 * @author szekus
 */
public class MyColumn
{
    private  int columnNumber;
    private String columnName;

    public MyColumn(int columnNumber, String columnName) {
        this.columnNumber = columnNumber;
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public int getColumnNumber() {
        return columnNumber;
    }

    public void setColumnNumber(int columnNumber) {
        this.columnNumber = columnNumber;
    }

    @Override
    public String toString() {
        return "columnNumber: " + columnNumber + " - " + "columnName: " + columnName  ;
    }
}