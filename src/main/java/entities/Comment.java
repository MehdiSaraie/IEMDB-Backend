package entities;

import java.util.Date;

public class Comment {
    private int id;
    private String userEmail;
    private String userNickname;
    private int movieId;
    private String text;
    private Date date = new Date();

    public Comment(String userEmail, int movieId, String text) {
        this.userEmail = userEmail;
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

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserNickname() { return userNickname; }

    public void setUserNickname(String userNickname) { this.userNickname = userNickname; }

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
