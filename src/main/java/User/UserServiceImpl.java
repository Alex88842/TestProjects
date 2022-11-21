package User;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class UserServiceImpl implements UserService {
    private final Connection connection;

    public UserServiceImpl(Connection connection) throws SQLException {
        this.connection = connection;
        this.connection.setAutoCommit(false);
    }


    @Override
    public User getUserByID(int id) throws SQLException {
        User user = null;
        String sql = "SELECT * FROM users WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String firstName = rs.getString("first_name");
            String lastName = rs.getString("last_name");
            user = new User(id, firstName, lastName);
        }
        ps.close();
        rs.close();
        return user;
    }

    @Override
    public List<User> getUsersByLastName(String lastName) throws SQLException {
        List<User> users = new LinkedList<>();
        String sql = "SELECT * FROM users WHERE last_name = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, lastName);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("id");
            String firstName = rs.getString("first_name");
            User user = new User(id, firstName, lastName);
            users.add(user);
        }
        ps.close();
        rs.close();
        return users;
    }
}