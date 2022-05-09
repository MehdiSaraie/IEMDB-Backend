package service;

import entities.Comment;
import domain.IEMDB;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CommentService {
  @RequestMapping(
    value = "/comments/{comment_id}/addVote",
    method = RequestMethod.POST,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<Comment> voteComment(
    @PathVariable(value="comment_id") int commentId,
    @RequestParam(value="vote") int vote
  ) {
    try {
      IEMDB.getInstance().voteComment(commentId, vote);
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
  }
}
