package lk.ijse.pos_system.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    private static DbConnection DbConnection= null;
    private Connection connection;


    private DbConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/WholesaleStore",
                "root",
                "1234");
    }



    public static DbConnection getInstance() throws ClassNotFoundException, SQLException {
        if (DbConnection==null){
            DbConnection=new DbConnection();
        }
        return DbConnection;

    }

    public Connection getConnection(){
        return connection;
    }
}
