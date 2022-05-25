package controller;

import domain.*;
import entities.Movie;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class WatchlistService {
  @RequestMapping(
    value = "/watchlist",
    method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<List<Movie>> getWatchlistMovies() {
    IEMDB iemdb = IEMDB.getInstance();
    if (!iemdb.isLoggedIn()) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    ArrayList<Movie> watchlist = iemdb.getWatchlist();
    return new ResponseEntity<>(watchlist, HttpStatus.OK);
  }

  @RequestMapping(
    value = "/watchlist",
    method = RequestMethod.PUT,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<String> addMovieToWatchlist(
    @RequestParam(value = "movie_id") int movieId
  ) {
    try {
      IEMDB iemdb = IEMDB.getInstance();
      if (!iemdb.isLoggedIn()) {
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
      }
      iemdb.addToWatchList(movieId);
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (CustomException e) {
      return new ResponseEntity<>("sorry, Age limit of this movie is greater than your age!", HttpStatus.FORBIDDEN);
    }
  }

  @RequestMapping(
    value = "/watchlist",
    method = RequestMethod.DELETE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<String> removeMovieFromWatchlist(
    @RequestParam(value = "movie_id") int movieId
  ) {
    IEMDB iemdb = IEMDB.getInstance();
    if (!iemdb.isLoggedIn()) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    iemdb.removeFromWatchList(movieId);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
