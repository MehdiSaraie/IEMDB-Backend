package service;

import classes.IEMDB;
import classes.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CommentService {
  @RequestMapping(value = "/comments/{comment_id}/addVote", method = RequestMethod.PUT,
    produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> voteComment(
    @PathVariable(value = "comment_id") int commentId,
    @RequestParam(value = "vote") int vote) {
    IEMDB iemdb = IEMDB.getInstance();
    User currentUser = iemdb.getLoggedInUser();
    if (currentUser == null) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    else {
      iemdb.voteComment(currentUser.getEmail(), commentId, vote);
      return new ResponseEntity<>(HttpStatus.OK);
    }
  }
}
