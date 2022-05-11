package entities;

public class Vote {
    private int id;
    private int commentId;
    private int userId;
    private int vote;

    public Vote(int commentId, int userId, int vote) {
        this.commentId = commentId;
        this.userId = userId;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }
}
