package entities;

public class Rate {
    private int id;
    private String userEmail;
    private int movieId;
    private int rateValue;

    public Rate(String userEmail, int movieId, int rateValue) {
        this.userEmail = userEmail;
        this.movieId = movieId;
        this.rateValue = rateValue;
    }

    public Rate() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
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
