package entities;

public class Writer {
    private int movieId;
    private String writerName;

    public Writer(int movieId, String writerName) {
        this.movieId = movieId;
        this.writerName = writerName;
    }

    public Writer() {}

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getWriterName() {
        return writerName;
    }

    public void setWriterName(String writerName) {
        this.writerName = writerName;
    }
}
