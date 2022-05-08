package service;

import classes.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class WatchlistService {
  @RequestMapping(value = "/watchlist", method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<Movie>> getWatchlistMovies() {
    List<Movie> watchlistMovies = new ArrayList<>();
    IEMDB iemdb = IEMDB.getInstance();
    User currentUser = iemdb.getLoggedInUser();
    if (currentUser == null) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    else {
      List<Integer> watchlist = currentUser.getWatchList();
      for (int i=0; i<watchlist.size(); i++) {
        watchlistMovies.add(iemdb.getMovieById(watchlist.get(i)));
      }
      return new ResponseEntity<>(watchlistMovies, HttpStatus.OK);
    }
  }

  @RequestMapping(value = "/watchlist", method = RequestMethod.POST,
    produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> addMovieToWatchlist(@RequestParam(value = "movie_id") int movieId) {
    try {
      IEMDB iemdb = IEMDB.getInstance();
      User currentUser = iemdb.getLoggedInUser();
      if (currentUser == null) {
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
      }
      iemdb.addToWatchList(currentUser.getEmail(), movieId);
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (CustomException e) {
      return new ResponseEntity<>("sorry, Age limit of this movie is greater than your age!", HttpStatus.FORBIDDEN);
    }
  }

  @RequestMapping(value = "/watchlist", method = RequestMethod.DELETE,
    produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> removeMovieFromWatchlist(@RequestParam(value = "movie_id") int movieId) {
    IEMDB iemdb = IEMDB.getInstance();
    User currentUser = iemdb.getLoggedInUser();
    if (currentUser == null) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    currentUser.removeFromWatchList(movieId);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
