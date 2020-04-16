package cezeszamlazo.model;

import cezeszamlazo.controller.Functions;

/**
 *
 * @author szekus
 */
public class Field
{
    protected String name;
    protected Object value;
    protected String type;

    public static Field create(String name, String type) {
        name = Functions.formatFieldName(name);

        return new Field(name, type);
    }

    private Field(String name, String type)
    {
        switch (type)
        {
            case "int":
                value = 0;
                break;
            case "double":
                value = 0.0;
                break;
            case "String":
                value = "";
                break;
            case "boolean":
                value = false;
                break;
        }
        
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", value: " + value;
    }
}