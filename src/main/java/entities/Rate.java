package entities;

public class Rate {
    private int userId;
    private int movieId;
    private int rateValue;

    public Rate(int userId, int movieId, int rateValue) {
        this.userId = userId;
        this.movieId = movieId;
        this.rateValue = rateValue;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public int getRateValue() {
        return rateValue;
    }

    public void setRateValue(int rateValue) {
        this.rateValue = rateValue;
    }
}
