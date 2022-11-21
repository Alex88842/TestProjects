package User;

import java.sql.SQLException;
import java.util.List;

public interface UserService {

    User getUserByID(int id) throws SQLException;
    List<User> getUsersByLastName(String lastName) throws SQLException;
}