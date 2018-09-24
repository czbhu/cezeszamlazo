/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeszamlazo.kintlevoseg;

/**
 *
 * @author szekus
 */
public class HtmlTableCreator {

    HtmlTableCell htmlTableCell;

    private HtmlTableCreator() {
    }

    public static HtmlTableCreator create() {
        return new HtmlTableCreator();
    }

    public void setHtmlTableCell(HtmlTableCell htmlTableCell) {
        this.htmlTableCell = htmlTableCell;
    }

    public HtmlTableCell getHtmlTableCell() {
        return htmlTableCell;
    }

    public String generateTable() {
        String table = "<table>";

        table += "<tr>";
        for (String th : htmlTableCell.getTh()) {
            table += "<th>" + th + "</th>";
        }
        table += "</tr>";
        
        table += "<tr>";
        for (String th : htmlTableCell.getTd()) {
            table += "<td>" + th + "</td>";
        }
        table += "</tr>";
        
        return table;
    }
}
