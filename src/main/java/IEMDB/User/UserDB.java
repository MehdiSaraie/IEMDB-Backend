package IEMDB.User;

import java.util.*;

import IEMDB.Movie.Actor;
import IEMDB.Movie.Movie;
import org.apache.commons.lang3.BooleanUtils;

public class UserDB {
    private List<User> Users = new ArrayList<>();
    Map<String, User> usersByEmail = new HashMap<String, User>();

    private boolean doesUserExist(User user) {
        for (User userItem : Users)
            if (BooleanUtils.isTrue(userItem.equals(user)))
                return true;
        return false;
    }

    public void addUser(User user) throws Exception {
        if (doesUserExist(user))
            throw new Exception("Error: Duplicate User");
        Users.add(user);
        usersByEmail.put(user.getEmail(), user);
    }

    public User getUserByEmail(String email) {
        return usersByEmail.get(email);
    }
}
