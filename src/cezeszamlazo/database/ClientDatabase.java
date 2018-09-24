package cezeszamlazo.database;

import cezeszamlazo.App;
import cezeszamlazo.Database;
import cezeszamlazo.interfaces.SharedValues;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author szekus
 */
public class ClientDatabase extends Database implements SharedValues {

    public String dbName;
    public ClientConnctionHandler cch;

    public ClientDatabase(String dbName) {
        this.dbName = dbName;
        this.cch = new ClientConnctionHandler();
    }

    public ClientDatabase(int szolgaltatoId) {
        Query query = new Query.QueryBuilder()
                .select("basedir")
                .from("teddra_szolgaltato")
                .where("id = " + szolgaltatoId)
                .build();
        Object[][] result = App.db.select(query.query);
        this.dbName = String.valueOf(result[0][0]);
        this.cch = new ClientConnctionHandler();
    }

    @Override
    public boolean connect() {
        try {
            conn = cch.getConnection(this.dbName);
        } catch (SQLException ex) {
            Logger.getLogger(ClientDatabase.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClientDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

}
