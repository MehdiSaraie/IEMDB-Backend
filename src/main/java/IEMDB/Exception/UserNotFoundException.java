package IEMDB.Exception;

public class UserNotFoundException extends Exception {
    public UserNotFoundException() {
        super("UserNotFound");
    }
}