package controller;

import entities.Actor;
import domain.IEMDB;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ActorController {
  @RequestMapping(
    value = "/actors",
    method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<List<Actor>> getActors(@RequestParam(value = "movie_id") int movieId) {
    IEMDB iemdb = IEMDB.getInstance();
    if (!iemdb.isLoggedIn()) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    ArrayList<Actor> actors = iemdb.getMovieCast(movieId);
    return new ResponseEntity<>(actors, HttpStatus.OK);
  }

  @RequestMapping(
    value = "/actors/{actor_id}",
    method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<Actor> getActor(@PathVariable(value = "actor_id") int actorId) {
    IEMDB iemdb = IEMDB.getInstance();
    if (!iemdb.isLoggedIn()) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    return new ResponseEntity<>(iemdb.getActorById(actorId), HttpStatus.OK);
  }
}
