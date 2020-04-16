package database;

import functions.EncodeDecode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author adam924
 */
public class Database
{
    private PreparedStatement pstmt;
    private ResultSet rset;
    public Connection conn;
    //Connection localConnection;
    //LocalDB localDB;
    private int rowNum = 0, columnNum = 0;
    private EncodeDecode encode = new EncodeDecode();
    private SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    //private LocalDatabase ldb;
    private boolean c;
    private boolean isLogging = false;

    private final static Logger LOGGER = Logger.getLogger(Database.class.getName());

    String URL;
    String USER;
    String PASSWORD;
    
    public EncodeDecode getEncode()
    {
        return encode;
    }

    public Connection getConn()
    {
        return conn;
    }

    public Database(String url, String user, String password)
    {
        URL = url;
        USER = user;
        PASSWORD = password;
        //localDB = LocalDB.create();
        //this.localConnection = localDB.getConnection();
        c = true;
    }

    public boolean connect()
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");

            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            LOGGER.log(Level.INFO, f.format(Calendar.getInstance().getTime()) + " - URL: " + URL);
            LOGGER.log(Level.INFO, f.format(Calendar.getInstance().getTime()) + " - Connection: " + conn);
//            System.out.println(f.format(Calendar.getInstance().getTime()) + " - URL: " + url);
//            System.out.println(f.format(Calendar.getInstance().getTime()) + " - Connection: " + conn);
            //c = true;
            return c;
        } catch (SQLException ex) {
            System.out.println(f.format(Calendar.getInstance().getTime()) + " - SQLException váltódott ki!");
            ex.printStackTrace();

            return false;
//            return c;
        } catch (ClassNotFoundException ex) {
            System.out.println(f.format(Calendar.getInstance().getTime()) + " - ClassNotFoundException váltódott ki!");
            ex.printStackTrace();
            //c = false;
            return false;
        }
    }

    public void disconnect()
    {
        try
        {
            conn.close();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    private void reconnect()
    {
        disconnect();
        connect();
    }

    /**
     * @deprecated Új verzió miatt.
     */
    public Object[][] select(String query, int col)
    {
        return select(query);
    }

    public Object[][] select(String query)
    {
//        Object[][] select = localDB.select(query);
        //unused
        long start = System.currentTimeMillis();
        //unused
        long stop = 0;
        
        Object[][] result = null;
        
        if (c)
        {
            if (isLogging)
            {
                System.out.print(f.format(Calendar.getInstance().getTime()) + " - select('" + query + "')");
            }

            try
            {
                //rowNum = rowNum(query);
                
                pstmt = conn.prepareStatement(query);

//                pstmt = localConnection.prepareStatement(query);
//                System.out.println(pstmt);
                rset = pstmt.executeQuery();
                rset.last();
                rowNum = rset.getRow();
//                System.out.print(" (Row COUNT: " + rowNum);
                ResultSetMetaData rsMetaData = rset.getMetaData();
                columnNum = rsMetaData.getColumnCount();
//                System.out.println(" Column COUNT: " + columnNum + ")");
                result = new Object[rowNum][columnNum];
                int i = 0;
                rset.beforeFirst();
                
                while (rset.next())
                {
                    for (int j = 1; j <= columnNum; j++)
                    {
                        if (j == 1)
                        {
                            try
                            {
                                result[i][j - 1] = rset.getInt(j);
                            }
                            catch (Exception ex)
                            {
                                result[i][j - 1] = encode.decode(rset.getString(j));
                            }
                        }
                        else
                        {
                            result[i][j - 1] = encode.decode(rset.getString(j));
                        }
                    }
                    i++;
                }
                
                //stop = System.currentTimeMillis();
                //System.out.println("A lekérdezés időtartama: " + ((stop - start)) + " ms");
                return result;
            }
            catch (SQLException ex)
            {
                ex.printStackTrace();
                System.out.println("Exceptionquery: " + query);
                reconnect();
                return result;
            }
        }
        else
        {
            //return ldb.select(query, col);
            return null;
        }
    }

    public int insert(String query, Object[] o, int cols)
    {
        if (c)
        {
            String elements = "{";
            
            if (o != null)
            {
                for (Object obj : o)
                {
                    elements += "'" + String.valueOf(obj) + "'; ";
                }
                elements = elements.substring(0, elements.length() - 2);
            }
            
            elements += "}";
            System.out.println(f.format(Calendar.getInstance().getTime()) + " - insert('" + query + "', o, " + cols + ") Elements: " + elements + " (Database.java/insert())");
            
            try
            {
                pstmt = conn.prepareStatement(query);
                
                for (int i = 1; i <= cols; i++)
                {
                    pstmt.setString(i, /*encode.encode(*/String.valueOf(o[i - 1]/*)*/));
//Ha az encode bennt van akkor az adatbázisba nem ír ékezetes betűket, helyette azok HTML kódját
                }
                
                pstmt.executeUpdate();
                ResultSet rs = pstmt.getGeneratedKeys();
                rs.beforeFirst();
               
                if (rs.next())
                {
                    int id = rs.getInt(1);
                    rs.close();
                    return id;
                }
                else
                {
                    rs.close();
                    return 0;
                }
            }
            catch (SQLException ex)
            {
                ex.printStackTrace();
//                System.out.println("pstmt: " + pstmt);
                reconnect();
                return 0;
            }
        }
        else
        {
            //ldb.insert(query, o, cols);
            return 0;
        }
    }

    public void delete(String query)
    {
        if (c)
        {
//            System.out.println(f.format(Calendar.getInstance().getTime()) + " - delete('" + query + "')");
            try
            {
                pstmt = conn.prepareStatement(query);
                pstmt.executeUpdate();
            }
            catch (SQLException ex)
            {
                ex.printStackTrace();
                reconnect();
            }
        }
        else
        {
            //ldb.delete(query);
        }
    }

    public int rowNum(String query)
    {
        int result = 0;
        String[] temp = query.split("FROM");
        String[] temp2 = temp[0].split(",");
        query = "SELECT count(" + temp2[0].replace("SELECT", "") + ") db FROM" + temp[1];
        //System.out.println(f.format(Calendar.getInstance().getTime()) + " - rowNum('" + query + "')");
        try
        {
            pstmt = conn.prepareStatement(query);
            rset = pstmt.executeQuery();
            rset.next();
            result = rset.getInt(1);
            return result;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            return result;
        }
    }
}