package search;



import User.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Search {
    private Type type;
    private Object criteria;
    private Search argument;
    private List<User> users;
}
