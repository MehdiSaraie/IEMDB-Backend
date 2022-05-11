package service;

import domain.*;
import entities.Comment;
import entities.Movie;
import entities.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MovieService {
  @RequestMapping(value = "/movies", method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
  public List<Movie> getMovies(
    @RequestParam(value = "name", required = false) String name,
    @RequestParam(value = "genre", required = false) String genre,
    @RequestParam(value = "releaseDate", required = false) String releaseDate,
    @RequestParam(value = "sortBy", required = false) String sortBy,
    @RequestParam(value = "actor_id", required = false) Integer actorId) {

    ArrayList<Movie> movies = IEMDB.getInstance().getMovies(name, genre, releaseDate, actorId, sortBy);
    return movies;
  }

  @RequestMapping(
    value = "/movies/{movie_id}",
    method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public Movie getMovie(@PathVariable(value = "movie_id") int movieId) {
    return IEMDB.getInstance().getMovieById(movieId);
  }

  @RequestMapping(
    value = "/movies/{movie_id}/addRate",
    method = RequestMethod.POST,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<String> rateMovie(
    @PathVariable(value = "movie_id") int movieId,
    @RequestParam(value = "rate") int rate
  ) {
    IEMDB iemdb = IEMDB.getInstance();
    User currentUser = iemdb.getLoggedInUser();
    if (currentUser == null) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    else {
      iemdb.rateMovie(currentUser.getEmail(), movieId, rate);
      return new ResponseEntity<>(HttpStatus.OK);
    }
  }

  @RequestMapping(
    value = "/movies/{movie_id}/addComment",
    method = RequestMethod.POST,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<List<Comment>> addCommentToMovie(
    @PathVariable(value = "movie_id") int movieId,
    @RequestBody() String commentText
  ) {
    try {
      IEMDB iemdb = IEMDB.getInstance();
      if (!iemdb.isLoggedIn()) {
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
      }
      iemdb.addComment(movieId, commentText);
      return new ResponseEntity<>(iemdb.getMovieComments(movieId), HttpStatus.OK);
    } catch (CustomException e) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
  }
}
