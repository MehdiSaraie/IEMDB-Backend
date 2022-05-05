package service;

import classes.Actor;
import classes.IEMDB;
import classes.Movie;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ActorService {
  @RequestMapping(value = "/actors", method = RequestMethod.GET,
          produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<Actor>> getActors(@RequestParam(value = "movie_id") int movie_id) {
    ArrayList<Actor> actors = new ArrayList<>();
    IEMDB iemdb = IEMDB.getInstance();
    Movie movie = iemdb.getMovieById(movie_id);
    if (movie == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    for (int actorId : movie.getCast()) {
      actors.add(iemdb.getActorById(actorId));
    }
    return new ResponseEntity<>(actors, HttpStatus.OK);
  }

  @RequestMapping(value = "/actors/{actor_id}", method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
  public Actor getActor(@PathVariable(value = "actor_id") int actorId) {
    return IEMDB.getInstance().getActorById(actorId);
  }
}
