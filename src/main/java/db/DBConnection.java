package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static DBConnection dbConnection;
    private Connection conn;

    private DBConnection() throws SQLException {
        this.conn= DriverManager.getConnection("jdbc:mysql://localhost/thogakade","root","Kavi@123");
    }
    public static DBConnection getInstance() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return dbConnection==null ? (dbConnection=new DBConnection()) : dbConnection;
    }
    public Connection getConnection(){
        return conn;
    }

}
