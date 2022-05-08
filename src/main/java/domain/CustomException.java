package domain;

public class CustomException extends Exception {
    private String message;

    public String getMessage() {
        return message;
    }

    public CustomException(String message) {
        this.message = message;
    }
}

