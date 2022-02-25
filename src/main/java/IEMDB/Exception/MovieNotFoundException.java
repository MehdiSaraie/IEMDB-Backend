package IEMDB.Exception;

public class MovieNotFoundException extends Exception{
    public MovieNotFoundException() {
        super("MovieNotFound");
    }
}
