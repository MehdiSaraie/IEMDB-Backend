package entities;

import java.util.Date;

public class Comment {
    private String userEmail;
    private int movieId;
    private String text;
    private int id;
    private Date date = new Date();
    private int like;
    private int dislike;
    private String userNickname;

    public Comment(String userEmail, int movieId, String text) {
        this.userEmail = userEmail;
        this.movieId = movieId;
        this.text = text;
        this.id = -1;
        this.like = 0;
        this.dislike = 0;
    }

    public Comment() {

    }

    public String getUserEmail() {
        return userEmail;
    }

    public int getMovieId() {
        return movieId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public int getDislike() {
        return dislike;
    }

    public void setDislike(int dislike) {
        this.dislike = dislike;
    }

    public void addVote(int vote) {
        if (vote == 1)
            this.like++;
        if (vote == -1)
            this.dislike++;
    }

    public void removeVote(int vote) {
        if (vote == 1)
            this.like--;
        if (vote == -1)
            this.dislike--;
    }
}
