package IEMDB.Movie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentDB {
//    private List<Comment> comments = new ArrayList<>();
    Map<Integer, Comment> commentsById = new HashMap<Integer, Comment>();

    public void addComment(Comment comment) throws Exception {
//        comments.add(comment);
        commentsById.put(comment.getId(), comment);
    }

    public Comment removeComment(Integer commentId) {
        return commentsById.remove(commentId);
    }

    public Comment getCommentById(Integer id) {
        return commentsById.get(id);
    }
}