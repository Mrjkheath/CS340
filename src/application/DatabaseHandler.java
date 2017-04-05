package application;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;


public final class DatabaseHandler {

    private static DatabaseHandler handler=null;

    //private static final String DB_URL = "jdbc:mysql://localhost:3306/sys" ;
    private static Connection conn = null;
    private static Statement stmt = null;

    private DatabaseHandler() {
        createConnection();
    }

    public static DatabaseHandler getInstance()
    {
        if(handler==null)
        {
            handler = new DatabaseHandler();
        }
        return handler;
    }


    void createConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys", "root", "password");
            System.out.println("Connection Successful!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ResultSet execQuery(String query) {
        ResultSet result;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys", "root", "password");
            stmt = conn.createStatement();
            result = stmt.executeQuery(query);
        } catch (SQLException ex) {
            System.out.println("Exception at execQuery:dataHandler" + ex.getLocalizedMessage());
            return null;
        } finally {
        }
        return result;
    }


    public boolean execAction(String qu) {
        try {
            stmt = conn.createStatement();
            stmt.execute(qu);
            return true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error:" + ex.getMessage(), "Error Occured", JOptionPane.ERROR_MESSAGE);
            System.out.println("Exception at execQuery:dataHandler" + ex.getLocalizedMessage());
            return false;
        } finally {
        }
    }

}
