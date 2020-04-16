/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeszamlazo.model;

/**
 *
 * @author szekus
 */
public class Client
{
    private int id;
    private String name;
    private String databaseName;

    public Client(int id, String name, String databaseName) {
        this.id = id;
        this.name = name;
        this.databaseName = databaseName;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    } 
}