/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeszamlazo.kintlevoseg;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author szekus
 */
public class KintlevosegKapcsolattartokGroup {

    private String name;
    private List<String> azonositok;

    private KintlevosegKapcsolattartokGroup() {
    }

    public static KintlevosegKapcsolattartokGroup create() {
        KintlevosegKapcsolattartokGroup group = new KintlevosegKapcsolattartokGroup();
        group.name = "";
        group.azonositok = new ArrayList<>();
        return group;

    }

    public void addToAzonositok(String azonosito) {
        this.azonositok.add(azonosito);
    }

    public List<String> getAzonositok() {
        return azonositok;
    }

    public void setAzonositok(List<String> azonositok) {
        this.azonositok = azonositok;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
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
        KintlevosegKapcsolattartokGroup guest = (KintlevosegKapcsolattartokGroup) obj;
        return (name == null ? guest.name == null : name.equals(guest.name));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public String toString() {
        String result = "";
        result += "name: " + name + " => azonositok:\n";

        for (String azon : azonositok) {
            result += azon + "\n";
        }

        return result;
    }

    public String[] getAzonositokAsArray() {

        Set<String> set = new HashSet<>();
        set.addAll(azonositok);
        azonositok.clear();
        azonositok.addAll(set);
        String[] result = new String[azonositok.size()];
        result = azonositok.toArray(result);

        for (int i = 0; i < result.length; i++) {
            String tmp = result[i];
            if (true) {

            }
        }
        return result;
    }

}
