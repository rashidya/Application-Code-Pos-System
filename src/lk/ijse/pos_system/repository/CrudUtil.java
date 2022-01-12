package lk.ijse.pos_system.repository;

import lk.ijse.pos_system.db.DbConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CrudUtil {
    private static PreparedStatement generatePreparedStatement(String query ,Object... args) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(query);
        for (int i = 0; i < args.length; i++) {
            preparedStatement.setObject(i+1,args[i]);
        }
        return preparedStatement;
    }

    public static boolean executeUpdate(String query ,Object... args) throws SQLException, ClassNotFoundException {
        return generatePreparedStatement(query,args).executeUpdate()>0;
    }


    public static ResultSet executeQuery(String query , Object... args) throws SQLException, ClassNotFoundException {

        return generatePreparedStatement(query,args).executeQuery();
    }
}
