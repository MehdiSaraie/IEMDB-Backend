package service;

import entities.Actor;
import domain.IEMDB;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ActorService {
  @RequestMapping(
    value = "/actors",
    method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<List<Actor>> getActors(@RequestParam(value = "movie_id") int movie_id) {
    ArrayList<Actor> actors = IEMDB.getInstance().getMovieCast(movie_id);
    return new ResponseEntity<>(actors, HttpStatus.OK);
  }

  @RequestMapping(
    value = "/actors/{actor_id}",
    method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public Actor getActor(@PathVariable(value = "actor_id") int actorId) {
    return IEMDB.getInstance().getActorById(actorId);
  }
}
