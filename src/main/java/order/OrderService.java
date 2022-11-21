package order;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public interface OrderService {

    List<Integer> getUsersWhichBought(int product_id, int min_times) throws SQLException;
    List<Integer> getUsersWithExpenses(int minExpenses, int maxExpenses) throws SQLException;
    List<Integer> getBadUsers(int number) throws SQLException;
    List<Order> getOrdersBetweenDates(Date dateStart, Date dateEnd) throws SQLException;
}