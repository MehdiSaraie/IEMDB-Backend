package service;

import classes.Actor;
import classes.IEMDB;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ActorService {
  @RequestMapping(value = "/actors/{actor_id}", method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
  public Actor getActor(@PathVariable(value = "actor_id") int actorId) {
    return IEMDB.getInstance().getActorById(actorId);
  }
}
