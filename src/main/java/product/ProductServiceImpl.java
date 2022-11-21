package product;

import java.sql.*;

public class ProductServiceImpl implements ProductService {
    private final Connection connection;

    public ProductServiceImpl(Connection connection) throws SQLException {
        this.connection = connection;
        this.connection.setAutoCommit(false);
    }

    @Override
    public Product getProductByID(int id) throws SQLException {
        Product product = null;
        String sql = "SELECT * FROM products WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String name = rs.getString("title");
            int cost = rs.getInt("price");
            product = new Product(id, name, cost);
        }
        ps.close();
        rs.close();
        return product;
    }

    @Override
    public Product getProductByTitle(String title) throws SQLException {
        Product product = null;
        String sql = "SELECT * FROM products WHERE title = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, title);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("id");
            int price = rs.getInt("price");
            product = new Product(id, title, price);
        }
        ps.close();
        rs.close();
        return product;
    }
}