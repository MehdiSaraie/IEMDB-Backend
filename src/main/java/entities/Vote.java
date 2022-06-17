package entities;

public class Vote {
    private int id;
    private int commentId;
    private String userEmail;
    private int vote;

    public Vote(int commentId, String userEmail, int vote) {
        this.commentId = commentId;
        this.userEmail = userEmail;
        this.vote = vote;
    }

    public Vote() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }
}
