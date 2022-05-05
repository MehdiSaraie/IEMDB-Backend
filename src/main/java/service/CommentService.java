package service;

import classes.Comment;
import classes.IEMDB;
import classes.Movie;
import classes.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CommentService {
//  @RequestMapping(value = "/comments/{comment_id}", method = RequestMethod.GET,
//          produces = MediaType.APPLICATION_JSON_VALUE)
//  public Comment getComment(@PathVariable(value = "comment_id") int commentId) {
//    return IEMDB.getInstance().getCommentById(commentId);
//  }

  @RequestMapping(value = "/comments/{comment_id}/addVote", method = RequestMethod.POST,
    produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Comment> voteComment(
    @PathVariable(value = "comment_id") int commentId,
    @RequestParam(value = "vote") int vote) {
    IEMDB iemdb = IEMDB.getInstance();
    User currentUser = iemdb.getLoggedInUser();
    if (currentUser == null) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    else {
      Comment comment = iemdb.getCommentById(commentId);
      if (comment == null) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
      iemdb.voteComment(currentUser.getEmail(), commentId, vote);
      return new ResponseEntity<>(comment, HttpStatus.OK);
    }
  }
}
