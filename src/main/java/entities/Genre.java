package entities;

public class Genre {
    private int movie_id;
    private String genre;

    public Genre(int movie_id, String genre) {
        this.movie_id = movie_id;
        this.genre = genre;
    }

    public int getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(int movie_id) {
        this.movie_id = movie_id;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
