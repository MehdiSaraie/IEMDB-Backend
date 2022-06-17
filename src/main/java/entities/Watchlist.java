package entities;

public class Watchlist {
    private int movieId;
    private String userEmail;

    public Watchlist(int movieId, String userEmail) {
        this.movieId = movieId;
        this.userEmail = userEmail;
    }

    public Watchlist() {}

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
