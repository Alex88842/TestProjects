package Utils;

import java.sql.*;
import org.postgresql.Driver;

public class ConnectionUtil {
    public static Connection getConnection(String url, String user, String password) throws SQLException {
        DriverManager.registerDriver(new Driver());
        return DriverManager.getConnection(url, user, password);
    }
}