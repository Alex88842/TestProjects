package order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
public class Order {
    private int id;
    private int user_id;
    private int product_id;
    private Date date_of_by;

    public Order(int user_id, int product_id, Date date_of_by) {
        this(-1, user_id, product_id, date_of_by);
    }


    @Override
    public String toString() {
        return "Order{id = " + id + ", userID = " + user_id + ", productID = " + product_id +
                ", date = " + date_of_by + '}';
    }
}