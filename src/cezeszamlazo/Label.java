package cezeszamlazo;

/**
 * @author Papp Ádám - Ceze Reklam
 */
public class Label
{
    private String name, id;

    public Label(String id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public String getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }
    
    @Override
    public String toString()
    {
        return name;
    }
}