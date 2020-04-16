/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeszamlazo.ugyfel;

/**
 *
 * @author szekus
 */
class Label
{
    private long id;
    private String name;

    public Label(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Label(String id, String name) {
        this.id = Long.parseLong(id);
        this.name = name;
    }

    /* GET / SET metódusok */
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    /**
     * A jComboBox-ban megjelenő információ.
     *
     * @return a név
     */
    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        Label label = (Label) obj;
        return id == label.id;
    }

//    @Override
//    public int hashCode() {
//        final int prime = 31;
//        int result = 1;
//        result = prime * result + ((name == null) ? 0 : name.hashCode());
//        result = (int) (prime * result + id);
//        return result;
//    }
}