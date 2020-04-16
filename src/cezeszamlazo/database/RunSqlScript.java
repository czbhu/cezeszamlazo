package cezeszamlazo.database;

/**
 *
 * @author szekus
 */
import com.ibatis.common.jdbc.ScriptRunner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author Dhinakaran Pragasam
 */
public class RunSqlScript
{
    private String fileUrl;
    private Connection connection;

    private RunSqlScript(Connection connection) {
        this.connection = connection;
    }

    private RunSqlScript(String fileUrl, Connection connection) {
        this.fileUrl = fileUrl;
        this.connection = connection;
    }

    public static RunSqlScript create(Connection connection) {
        return new RunSqlScript(connection);
    }

    public static RunSqlScript create(String fileUrl, Connection connection) {
        return new RunSqlScript(connection);
    }

    public void run() throws ClassNotFoundException, SQLException {
        String aSQLScriptFilePath = this.fileUrl;

        PreparedStatement stmt = null;

        try {
            // Initialize object for ScripRunner
            ScriptRunner sr = new ScriptRunner(connection, true, true);

            // Give the input file to Reader
            Reader reader = new BufferedReader(
                    new FileReader(aSQLScriptFilePath));

            // Exctute script
            sr.runScript(reader);

        } catch (Exception e) {
            System.err.println("Failed to Execute" + aSQLScriptFilePath
                    + " The error is " + e.getMessage());
        }
    }

    public void run(Reader reader) throws ClassNotFoundException, SQLException {
        String aSQLScriptFilePath = this.fileUrl;

        PreparedStatement stmt = null;

        try {
            // Initialize object for ScripRunner
            ScriptRunner sr = new ScriptRunner(connection, true, true);
            // Exctute script
            sr.runScript(reader);

        } catch (Exception e) {
            System.err.println("Failed to Execute" + aSQLScriptFilePath
                    + " The error is " + e.getMessage());
        }
    }

    public void run(StringBuilder stringBuilder) throws ClassNotFoundException, SQLException {

        Reader reader = new StringReader(stringBuilder.toString());

        try {
            // Initialize object for ScripRunner
            ScriptRunner sr = new ScriptRunner(connection, false, false);
            // Exctute script
            sr.runScript(reader);

        } catch (Exception e) {
            System.err.println("Failed to Execute =>  The error is: " + e.getMessage());
            e.printStackTrace();
        }
    }

//    /**
//     * @param args the command line arguments
//     */
//    public static void main(String[] args) throws ClassNotFoundException,
//            SQLException {
//
//        String aSQLScriptFilePath = "path/to/sql/script.sql";
//
//        // Create MySql Connection
//        Class.forName("com.mysql.jdbc.Driver");
//        Connection con = DriverManager.getConnection(
//                "jdbc:mysql://localhost:3306/database", "username", "password");
//        Statement stmt = null;
//
//        try {
//            // Initialize object for ScripRunner
//            ScriptRunner sr = new ScriptRunner(con, false, false);
//
//            // Give the input file to Reader
//            Reader reader = new BufferedReader(
//                    new FileReader(aSQLScriptFilePath));
//
//            // Exctute script
//            sr.runScript(reader);
//
//        } catch (Exception e) {
//            System.err.println("Failed to Execute" + aSQLScriptFilePath
//                    + " The error is " + e.getMessage());
//        }
//    }
}