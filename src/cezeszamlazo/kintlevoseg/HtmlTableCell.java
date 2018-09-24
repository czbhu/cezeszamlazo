/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeszamlazo.kintlevoseg;

import java.util.List;

/**
 *
 * @author szekus
 */
public class HtmlTableCell {

    private List<String> th;
    private List<String> td;

    private HtmlTableCell() {
    }

    public static HtmlTableCell create() {
        return new HtmlTableCell();
    }

    public void setTd(List<String> td) {
        this.td = td;
    }

    public void setTh(List<String> th) {
        this.th = th;
    }

    public List<String> getTd() {
        return td;
    }

    public List<String> getTh() {
        return th;
    }

    @Override
    public String toString() {
        String result = "";
        result += "th: ";
        for (String string : th) {
            result += string + "; ";
        }

        result += "\n td: ";
        for (String string : td) {
            result += string + "; ";
        }
        
        return result;
    }

}
