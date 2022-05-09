package entities;

import java.util.Date;

public class Comment {
    private int id;
    private int userId;
    private int movieId;
    private String text;
    private Date date = new Date();

    public Comment(int userId, int movieId, String text) {
        this.userId = userId;
        this.movieId = movieId;
        this.text = text;
    }

    public Comment() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
