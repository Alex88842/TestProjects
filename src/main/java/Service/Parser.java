package Service;

import java.io.Closeable;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.*;

import User.User;
import product.Product;
import javafx.util.Pair;
import org.json.JSONArray;
import org.json.JSONObject;
import search.Search;
import search.Type;



public class Parser  {
    private final String outputFileName;

    public Parser(String outputFileName) {
        this.outputFileName = outputFileName;
    }

    public List<Search> parseForSearch(String fileName) throws IOException {
        JSONObject object = getJsonObjectFromFile(fileName);
        JSONArray criterias = object.getJSONArray("criterias");
        List<Search> arguments = new LinkedList<>();

        for (int i = 0; i < criterias.length(); i++) {
            JSONObject criteria = criterias.getJSONObject(i);
            Search search = new Search();

            if (criteria.has("lastName")) {
                search.setType(Type.USERS_BY_LAST_NAME);
                search.setCriteria(criteria.get("lastName"));
            } else if (criteria.has("productName")) {
                String productName = criteria.getString("productName");
                int minTimes = criteria.getInt("minTimes");
                search.setType(Type.USERS_WHO_BUY);
                search.setCriteria(new Pair<>(productName, minTimes));
            } else if (criteria.has("minExpenses")) {
                int min = criteria.getInt("minExpenses");
                int max = criteria.getInt("maxExpenses");
                search.setType(Type.USERS_WITH_ORDERS);
                search.setCriteria(new Pair<>(min, max));
            } else if (criteria.has("badUsers")) {
                search.setType(Type.BAD_USERS);
                search.setCriteria(criteria.getInt("badUsers"));
            }

            arguments.add(search);
        }

        return arguments;
    }

    public Pair<Date, Date> parseForStat(String fileName) throws IOException {
        JSONObject object = getJsonObjectFromFile(fileName);
        Date startDate = Date.valueOf(object.getString("startDate"));
        Date endDate = Date.valueOf(object.getString("endDate"));
        return new Pair<>(startDate, endDate);
    }

    public void printForSearch(List<Search> results) {
        JSONArray AllResults = new JSONArray();

        for (Search result : results) {
            Search arg = result.getArgument();

            switch (arg.getType()) {
                case USERS_BY_LAST_NAME:
                    String lastName = arg.getCriteria().toString();
                    JSONObject jsonCriteria1 = new JSONObject();
                    jsonCriteria1.put("lastName", lastName);
                    JSONArray jsonResults1 = new JSONArray();
                    for (User user : result.getUsers()) {
                        JSONObject lastFirst = new JSONObject();
                        lastFirst.put("lastName", user.getLastName());
                        lastFirst.put("firstName", user.getFirstName());
                        jsonResults1.put(lastFirst);
                    }
                    JSONObject Results1 = new JSONObject();
                    Results1.put("criteria", jsonCriteria1);
                    Results1.put("results", jsonResults1);
                    AllResults.put(Results1);
                    break;
                case USERS_WHO_BUY:
                    Pair<Integer, Integer> criteria1 = (Pair<Integer, Integer>) arg.getCriteria();
                    JSONObject jsonCriteria2 = new JSONObject();
                    jsonCriteria2.put("productName", criteria1.getKey());
                    jsonCriteria2.put("minTimes", criteria1.getValue());
                    JSONArray jsonResults2 = new JSONArray();
                    for (User user : result.getUsers()) {
                        JSONObject lastFirst = new JSONObject();
                        lastFirst.put("lastName", user.getLastName());
                        lastFirst.put("firstName", user.getFirstName());
                        jsonResults2.put(lastFirst);
                    }
                    JSONObject jResults2 = new JSONObject();
                    jResults2.put("criteria", jsonCriteria2);
                    jResults2.put("results", jsonResults2);
                    AllResults.put(jResults2);
                    break;
                case USERS_WITH_ORDERS:
                    Pair<Integer, Integer> criteria2 = (Pair<Integer, Integer>) arg.getCriteria();
                    JSONObject jCriteria3 = new JSONObject();
                    jCriteria3.put("minExpenses", criteria2.getKey());
                    jCriteria3.put("maxExpenses", criteria2.getValue());
                    JSONArray jResults3Arr = new JSONArray();
                    for (User user : result.getUsers()) {
                        JSONObject lastFirst = new JSONObject();
                        lastFirst.put("lastName", user.getLastName());
                        lastFirst.put("firstName", user.getFirstName());
                        jResults3Arr.put(lastFirst);
                    }
                    JSONObject jResults3 = new JSONObject();
                    jResults3.put("criteria", jCriteria3);
                    jResults3.put("results", jResults3Arr);
                    AllResults.put(jResults3);
                    break;
                case BAD_USERS:
                    int number = (Integer) arg.getCriteria();
                    JSONObject jCriteria4 = new JSONObject();
                    jCriteria4.put("badUsers", number);
                    JSONArray jResults4Arr = new JSONArray();
                    for (User user : result.getUsers()) {
                        JSONObject lastFirst = new JSONObject();
                        lastFirst.put("lastName", user.getLastName());
                        lastFirst.put("firstName", user.getFirstName());
                        jResults4Arr.put(lastFirst);
                    }
                    JSONObject jResults4 = new JSONObject();
                    jResults4.put("criteria", jCriteria4);
                    jResults4.put("results", jResults4Arr);
                    AllResults.put(jResults4);
                    break;
                default:
                    printError("Unknown type of search argument: " + result.getArgument().getType());
                    return;
            }
        }

        JSONObject jObj = new JSONObject();
        jObj.put("type", "search");
        jObj.put("results", AllResults);
        printJsonObjectInFile(jObj);
    }

    public void printForStat(long daysBetween, Map<User, List<Product>> ordersMap) {
        JSONArray jSonAllUsers = new JSONArray();
        int totalExpenses = 0;

        for (User user : ordersMap.keySet()) {
            JSONObject jUser = new JSONObject();
            jUser.put("name", user.getLastName() + " " + user.getFirstName());
            JSONArray jPurchases = new JSONArray();
            int totalExpensesForCurrent = 0;

            for (Product product : ordersMap.get(user)) {
                JSONObject jproduct = new JSONObject();
                jproduct.put("title", product.getTitle());
                jproduct.put("expenses", product.getPrice());
                jPurchases.put(jproduct);
                totalExpensesForCurrent += product.getPrice();
            }

            jUser.put("buyes", jPurchases);
            jUser.put("totalExpenses", totalExpensesForCurrent);
            jSonAllUsers.put(jUser);
            totalExpenses += totalExpensesForCurrent;
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", "stat");
        jsonObject.put("totalDays", daysBetween);
        jsonObject.put("customers", jSonAllUsers);
        jsonObject.put("totalExpenses", totalExpenses);
        jsonObject.put("avgExpenses", (double) totalExpenses / (double) ordersMap.size());
        printJsonObjectInFile(jsonObject);
    }

    public void printError(String message) {
        JSONObject jObj = new JSONObject();
        jObj.put("type", "error");
        jObj.put("message", message);
        printJsonObjectInFile(jObj);
    }

    private JSONObject getJsonObjectFromFile(String fileName) throws IOException {
        String jsonString = new String(Files.readAllBytes(Paths.get(fileName)));
        return new JSONObject(jsonString);
    }

    private void printJsonObjectInFile(JSONObject jObj) {
        try(FileOutputStream fos = new FileOutputStream(outputFileName)) {
            fos.write(jObj.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
