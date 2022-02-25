package IEMDB.Movie;

public class Rate {
    private Integer score;
    private String userEmail;
    private Integer movieId;

    public Rate(String userEmail, Integer movieId, Integer score) {
        this.score = score;
        this.movieId = movieId;
        this.userEmail = userEmail;
    }

    public Integer getScore() {
        return score;
    }
    public Integer getMovieId() {
        return this.movieId;
    }
    public String getEmail() { return this.userEmail; }
}
