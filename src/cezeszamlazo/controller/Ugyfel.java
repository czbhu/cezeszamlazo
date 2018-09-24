package cezeszamlazo.controller;

import java.io.Serializable;
import cezeszamlazo.model.Field;
import java.util.ArrayList;

/**
 *
 * @author szekus
 */
public class Ugyfel extends Controller implements Serializable {

    protected ArrayList<Field> fields = new ArrayList<Field>() {
        {
            add(Field.create("nev", "string"));
            add(Field.create("cegnev", "string"));
            add(Field.create("irsz", "int"));
            add(Field.create("varos", "string"));
            add(Field.create("utca", "string"));
            add(Field.create("telefon", "string"));
            add(Field.create("fizetesi_mod", "int"));
            add(Field.create("fizetesi_mod_kotelezo", "int"));
            add(Field.create("esedekesseg", "int"));
            add(Field.create("adoszam", "string"));
            add(Field.create("eu_adoszam", "string"));
            add(Field.create("szamlan_megjelenik", "int"));
            add(Field.create("bankszamla_szam", "string"));

        }
    };

    private Ugyfel() {

    }

    private Ugyfel(UgyfelBuilder ugyfelBuilder) {
        this.fields = ugyfelBuilder.builderFields;
    }

    public static Ugyfel create() {
        return new Ugyfel();
    }

    public static class UgyfelBuilder {
        public ArrayList<Field> builderFields = new ArrayList<>();
        public UgyfelBuilder addValue(String fieldName, Object value) {
            for (Field field : Ugyfel.create().fields) {
                if (field.getName().equals(fieldName)) {
                    field.setValue(value);
                    builderFields.add(field);
                }
            }
            return this;
        }
        public Ugyfel build() {
            return new Ugyfel(this);
        }
    }

}
