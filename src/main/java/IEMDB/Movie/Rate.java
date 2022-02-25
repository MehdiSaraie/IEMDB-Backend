package IEMDB.Movie;

public class Rate {
    private Integer score;
    private String email;
    private Integer movieId;

    public Rate(String email, Integer movieId, Integer score) {
        this.score = score;
        this.movieId = movieId;
        this.email = email;
    }

    public Integer getScore() {
        return score;
    }
    public Integer getMovieId() {
        return this.movieId;
    }
    public String getEmail() { return this.email; }
}
