package IEMDB.Movie;

import java.util.*;
import org.apache.commons.lang3.BooleanUtils;

public class ActorDB {
    private List<Actor> Actors = new ArrayList<>();
    private Map<Integer, Actor> actorsById = new HashMap<>();

    public void addActor(Actor actor) { actorsById.put(actor.getId(), actor); }
    public boolean actorExists(int id) { return actorsById.containsKey(id); }
}