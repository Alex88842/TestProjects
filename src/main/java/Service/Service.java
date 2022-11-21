package Service;

import java.io.Closeable;
import java.io.IOException;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

import User.User;
import User.UserServiceImpl;
import Utils.ConnectionUtil;
import product.Product;
import product.ProductServiceImpl;
import javafx.util.Pair;
import order.Order;
import order.OrderServiceImpl;
import search.*;



public class Service implements Closeable {
    private Connection connection;
    private Parser parser;

    public Service() {
        try {
            Properties properties = new Properties();
            properties.load(this.getClass().getResourceAsStream("/connection.properties"));
            String url = properties.getProperty("URL");
            String user = properties.getProperty("USER");
            String pass = properties.getProperty("PASSWORD");
            connection = ConnectionUtil.getConnection(url, user, pass);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public void getCommand(String command, String inputFileName, String outputFileName) {
        parser = new Parser(outputFileName);
        switch (command) {
            case "search":
                search(inputFileName);
                break;
            case "stat":
                stat(inputFileName);
                break;
            default:
                parser.printError(command + " неизвестная команда");
        }
    }

    private void search(String inputFileName) {
        try {
            UserServiceImpl userService = new UserServiceImpl(connection);
            ProductServiceImpl productService = new ProductServiceImpl(connection);
            OrderServiceImpl orderService = new OrderServiceImpl(connection);

            List<Search> arguments = parser.parseForSearch(inputFileName);
            List<Search> results = new LinkedList<>();

            for (Search argument : arguments) {
                Search result = new Search();
                result.setArgument(argument);
                List<User> users;

                switch (argument.getType()) {
                    case USERS_BY_LAST_NAME:
                        users = userService.getUsersByLastName(argument.getCriteria().toString());
                        break;
                    case USERS_WHO_BUY:
                        Pair<String, Integer> criteria1 = (Pair<String, Integer>) argument.getCriteria();
                        Product product = productService.getProductByTitle(criteria1.getKey());
                        List<Integer> uIDs1 = orderService.getUsersWhichBought(product.getId(), criteria1.getValue());
                        users = new LinkedList<>();
                        for (Integer id : uIDs1) {
                            users.add(userService.getUserByID(id));
                        }
                        break;
                    case USERS_WITH_ORDERS:
                        Pair<Integer, Integer> criteria2 = (Pair<Integer, Integer>) argument.getCriteria();
                        List<Integer> uIDs2 = orderService.getUsersWithExpenses(criteria2.getKey(), criteria2.getValue());
                        users = new LinkedList<>();
                        for (Integer id : uIDs2) {
                            users.add(userService.getUserByID(id));
                        }
                        break;
                    case BAD_USERS:
                        Integer number = (Integer) argument.getCriteria();
                        List<Integer> uIDs3 = orderService.getBadUsers(number);
                        users = new LinkedList<>();
                        for (Integer id : uIDs3) {
                            users.add(userService.getUserByID(id));
                        }
                        break;
                    default:
                        parser.printError("Неизвестный тип аргумента для поиска: " + argument.getType());
                        return;
                }

                result.setUsers(users);
                results.add(result);
            }

            parser.printForSearch(results);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            parser.printError(e.getMessage());
        }
    }

    private void stat(String inputFileName) {
        try {
            UserServiceImpl userService = new UserServiceImpl(connection);
            ProductServiceImpl productService = new ProductServiceImpl(connection);
            OrderServiceImpl orderService = new OrderServiceImpl(connection);

            Pair<Date, Date> args = parser.parseForStat(inputFileName);
            Date dateStart = args.getKey();
            Date dateEnd = args.getValue();
            LocalDate ldStart = dateStart.toLocalDate();
            LocalDate ldEnd = dateEnd.toLocalDate();
            long days = ChronoUnit.DAYS.between(LocalDate.parse(ldStart.toString()), LocalDate.parse(ldEnd.toString()));

            List<Order> orders = orderService.getOrdersBetweenDates(dateStart, dateEnd);
            Map<User, List<Product>> ordersMap = new HashMap<>();
            for (Order order : orders) {
                int userID = order.getUser_id();
                User user = userService.getUserByID(userID);
                ordersMap.computeIfAbsent(user, b -> new LinkedList<>());
                int productID = order.getProduct_id();
                Product product = productService.getProductByID(productID);
                ordersMap.get(user).add(product);
            }
            parser.printForStat(days, ordersMap);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            parser.printError(e.getMessage());
        }
    }

    @Override
    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            parser.printError(e.getMessage());
        }


    }
}