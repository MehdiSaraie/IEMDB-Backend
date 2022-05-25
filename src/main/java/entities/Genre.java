package entities;

public class Genre {
    private int movieId;
    private String genre;

    public Genre(int movieId, String genre) {
        this.movieId = movieId;
        this.genre = genre;
    }

    public Genre() {}

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
