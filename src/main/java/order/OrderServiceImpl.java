package order;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class OrderServiceImpl implements OrderService {
    private final Connection connection;

    public OrderServiceImpl(Connection connection) throws SQLException {
        this.connection = connection;
        this.connection.setAutoCommit(false);
    }

    @Override
    public List<Integer> getUsersWhichBought(int product_id, int minTimes) throws SQLException {
        List<Integer> users = new LinkedList<>();
        String sql = "WITH T1 AS (SELECT * FROM orders WHERE product_id = ?)," +
                "T2 AS (SELECT COUNT(*) AS COUNT, user_id FROM T1 GROUP BY user_id)" +
                "SELECT * FROM T2 WHERE count >= ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, product_id);
        ps.setInt(2, minTimes);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int userId = rs.getInt("user_id");
            users.add(userId);
        }
        ps.close();
        rs.close();
        return users;
    }

    @Override
    public List<Integer> getUsersWithExpenses(int minExpenses, int maxExpenses) throws SQLException {
        List<Integer> users = new LinkedList<>();
        String sql = "WITH T1 AS (SELECT * FROM orders)," +
                "T2 AS (SELECT id, price FROM products)," +
                "T3 AS (SELECT * FROM T1 FULL JOIN T2 ON T1.product_id = T2.id)," +
                "T4 AS (SELECT user_id, product_id, price FROM T3)," +
                "T5 AS (SELECT user_id, SUM(price) AS SUM FROM T4 GROUP BY user_id ORDER BY user_id)" +
                "SELECT * FROM T5 WHERE sum >= ? AND sum <= ?;";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, minExpenses);
        ps.setInt(2, maxExpenses);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int userId = rs.getInt("user_id");
            users.add(userId);
        }
        ps.close();
        rs.close();
        return users;
    }

    @Override
    public List<Integer> getBadUsers(int number) throws SQLException {
        List<Integer> users = new LinkedList<>();
        String sql = "SELECT user_id, COUNT(*) AS COUNT FROM orders GROUP BY user_id ORDER BY COUNT;";
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int userId = rs.getInt("user_id");
            users.add(userId);
        }
        ps.close();
        rs.close();
        int cSize = users.size();
        for (int i = 1; i <= cSize - number; i++) {
            users.remove(cSize - i);
        }
        return users;
    }

    @Override
    public List<Order> getOrdersBetweenDates(Date date1, Date date2) throws SQLException {
        List<Order> orders = new LinkedList<>();
        String sql = "SELECT * FROM orders WHERE date_of_by >= ? AND date_of_by <= ?;";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setDate(1, date1);
        ps.setDate(2, date2);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("id");
            int customerID = rs.getInt("user_id");
            int itemID = rs.getInt("product_id");
            Date date = rs.getDate("date_of_by");
            Order order = new Order(id, customerID, itemID, date);
            orders.add(order);
        }
        ps.close();
        rs.close();
        return orders;
    }
}