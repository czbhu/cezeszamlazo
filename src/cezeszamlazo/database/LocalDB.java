package cezeszamlazo.database;

import cezeszamlazo.EncodeDecode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author szekus
 */
public class LocalDB
{
    private Connection connection;
    private Statement statement;
    private ResultSet resultset;
    private PreparedStatement preparedStatement;
    private SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private int rowNum = 0, columnNum = 0;

    public static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    public static final String JDBC_URL = "jdbc:derby:testdb;create=true";

    private LocalDB(Connection connection) {
        this.connection = connection;
    }

    public static LocalDB create() throws ClassNotFoundException, SQLException {
        Class.forName(DRIVER);
        Connection connection = DriverManager.getConnection(JDBC_URL);
//        connection.createStatement().execute("create table test(id INTEGER, value varchar(20))");

//        connection.createStatement().execute("insert into test values(1,'1')");
        return new LocalDB(connection);
    }

    public Connection getConnection() {
        return connection;
    }

    private void query(String query) {
        try {
            System.out.println(f.format(Calendar.getInstance().getTime()) + " - LOCAL - " + query);
            preparedStatement.executeUpdate(query);
        } catch (SQLException ex) {
            System.out.println("SQLException váltódott ki: create('" + query + "')");
            ex.printStackTrace();
        }
    }

    public Object[][] select(String query) {
        query = convertMysqlToSqlite(query);
        Object[][] o = null;
        System.out.println(f.format(Calendar.getInstance().getTime()) + " - LOCAL - select('" + query + "')");
        try {
//            preparedStatement = connection.prepareStatement(query);
            statement = connection.createStatement();
            
            rowNum = rowNum(query);
//            resultset = preparedStatement.executeQuery();
            resultset = statement.executeQuery(query);
            ResultSetMetaData rsMetaData = resultset.getMetaData();
            columnNum = rsMetaData.getColumnCount();
            System.out.print(" (Row COUNT: " + rowNum);
            System.out.println(" Column COUNT: " + columnNum + ")");
            o = new Object[rowNum][columnNum];
            int j = 0;
            while (resultset.next()) {
                for (int i = 0; i < columnNum; i++) {
                    try {
                        o[j][i] = String.valueOf(EncodeDecode.decode(resultset.getString(i + 1)));
                    } catch (NullPointerException ex) {
                        o[j][i] = "";
                    }
                }
                j++;
            }
            resultset.close();
        } catch (SQLException ex) {
            System.out.println("SQLException váltódott ki: select('" + query + "'");
            ex.printStackTrace();
        }
        return o;
    }

    public void insert(String query, Object[] o, int cols) {
        this.insert(query, o, cols, true);
    }

    public void insert(String query, Object[] o, int cols, boolean d) {
        cols = o.length;
        query = convertMysqlToSqlite(query);
        if (d) {
            System.out.print(f.format(Calendar.getInstance().getTime()) + " - LOCAL - insert('" + query + "', o, " + cols + ") {");
        }
//        int count = 0;
//        String elements = "";
        try {
            preparedStatement = connection.prepareStatement(query);
            for (int i = 0; i < cols; i++) {
//                count = i;
                preparedStatement.setString(i + 1, EncodeDecode.encode(String.valueOf(o[i])));
//                elements += String.valueOf(o[i]) + ", ";
                if (d) {

                    System.out.print(String.valueOf(o[i]) + ", ");
                }
            }
//            elements += "}";
            if (d) {

                System.out.println("}");
            }
            preparedStatement.addBatch();
            connection.setAutoCommit(false);
            preparedStatement.executeBatch();
            connection.setAutoCommit(true);
        } catch (ArrayIndexOutOfBoundsException aioobe) {

//            System.out.println("BUG: ");
//            System.out.println("query: " + query);
//            System.out.println("elements: " + elements);
//            System.out.println("count: " + count);
//            System.out.println("cols: " + cols);
//            System.out.println("o.length: " + o.length);
            aioobe.printStackTrace();
        } catch (SQLException ex) {
            System.out.println("SQLException váltódott ki: insert('" + query + "', o, " + cols + ")");
            ex.printStackTrace();
        }
    }

    public void delete(String query) {
        query = convertMysqlToSqlite(query);
        System.out.println(f.format(Calendar.getInstance().getTime()) + " - LOCAL - delete('" + query + "')");
        try {
            preparedStatement.executeUpdate(query);
        } catch (SQLException ex) {
            System.out.println("SQLException váltódott ki: delete('" + query + "')");
            ex.printStackTrace();
        }
    }

    public int rowNum(String query) {
        int result = 0;
        int ny = 0;
        for (int i = 0; i < query.length(); i++) {
            String sub = query.substring(i);
            if (sub.startsWith("(")) {
                ny++;
            } else if (sub.startsWith(")")) {
                ny--;
            } else if (ny == 0 && sub.startsWith("FROM")) {
                String[] temp = query.split(" ");
                query = "SELECT count(*) " + sub;
                break;
            }
        }
        System.out.println(f.format(Calendar.getInstance().getTime()) + " - LOCAL - rowNum('" + query + "')");
        try {
            preparedStatement = connection.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
            result = rs.getInt(1);
            System.out.println("EE " + rs.getInt(1));
            return result;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return result;
        }
    }

    private String convertMysqlToSqlite(String query) {
        query = query.replace("&&", "AND");
        query = query.replace("||", "OR");
        return query;
    }

    public void insertSync(String query, Object[] o, int cols, boolean d) {
        cols = o.length;
        query = convertMysqlToSqlite(query);
        if (d) {
            System.out.print(f.format(Calendar.getInstance().getTime()) + " - LOCAL - insert('" + query + "', o, " + cols + ") {");
        }
//        int count = 0;
//        String elements = "";
        try {
            PreparedStatement pstmt;
            pstmt = connection.prepareStatement(query);
            for (int i = 0; i < cols; i++) {
//                count = i;
                pstmt.setString(i + 1, EncodeDecode.encode(String.valueOf(o[i])));
//                elements += String.valueOf(o[i]) + ", ";
                if (d) {

                    System.out.print(String.valueOf(o[i]) + ", ");
                }
            }
//            elements += "}";
            if (d) {

                System.out.println("}");
            }
            pstmt.addBatch();
            connection.setAutoCommit(false);
            pstmt.executeBatch();
            connection.setAutoCommit(true);
        } catch (ArrayIndexOutOfBoundsException aioobe) {

//            System.out.println("BUG: ");
//            System.out.println("query: " + query);
//            System.out.println("elements: " + elements);
//            System.out.println("count: " + count);
//            System.out.println("cols: " + cols);
//            System.out.println("o.length: " + o.length);
            aioobe.printStackTrace();
        } catch (SQLException ex) {
            System.out.println("SQLException váltódott ki: insert('" + query + "', o, " + cols + ")");
            ex.printStackTrace();
        }
    }
}