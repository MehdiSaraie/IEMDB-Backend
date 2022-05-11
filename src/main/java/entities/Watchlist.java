package entities;

public class Watchlist {
    private int movieId;
    private int userId;

    public Watchlist(int movieId, int userId) {
        this.movieId = movieId;
        this.userId = userId;
    }

    public Watchlist() {}

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
