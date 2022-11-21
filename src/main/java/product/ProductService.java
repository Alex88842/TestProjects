package product;
import java.sql.SQLException;
import java.util.List;

public interface ProductService {

    Product getProductByID(int id) throws SQLException;
    Product getProductByTitle(String title) throws SQLException;
}