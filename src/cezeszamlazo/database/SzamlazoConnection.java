/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeszamlazo.database;

/**
 *
 * @author szekus
 */
public class SzamlazoConnection {
    
    String databaseName;
    String url;
    String user;
    String password;

    public SzamlazoConnection() {
    }

    public SzamlazoConnection(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    
}
