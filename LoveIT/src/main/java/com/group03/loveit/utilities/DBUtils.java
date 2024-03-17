package com.group03.loveit.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DUNGHUYNH
 */
public abstract class DBUtils {

    private final static String serverName = "localhost";
    private final static String dbName = "LoveIt";
    private final static String portNumber = "1435";
    private final static String instance = "";
    //private final static String instance = "MSSQLSERVER_2019";//LEAVE THIS ONE EMPTY IF YOUR SQL IS A SINGLE INSTANCE

    // Don't change these values, change the environment variables instead
    private final static String userID = "sa";
    private final static String password = "12345";

    /**
     * Close the connection
     *
     * @param conn connection need closing
     */
    public final static void closeConnection(Connection conn) {
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Get the connection on connection string Multiple will access to the
     *
     * @return a connection instance
     */
    public synchronized final static Connection getConnection() {
        String url;
        if (instance == null || instance.trim().isEmpty()) {
            url = "jdbc:sqlserver://" + serverName + ":" + portNumber + ";databaseName=" + dbName;
        } else {
            url = "jdbc:sqlserver://" + serverName + ":" + portNumber + "\\" + instance + ";databaseName=" + dbName;
        }

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException ex) {
            System.out.println("PRJ301DEMO: Can not load JDBC Driver. Please check your pom file");
        }

        try {
            Connection con = DriverManager.getConnection(url, userID, password);
            return con;
        } catch (SQLException ex) {
            System.out.println("PRJ301DEMO: Can not connect SQL Server. Reason: " + ex.getMessage());
        }
        return null;
    }
}
