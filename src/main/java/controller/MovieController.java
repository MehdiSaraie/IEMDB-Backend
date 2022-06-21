package controller;

import domain.*;
import entities.Actor;
import entities.Comment;
import entities.Movie;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MovieController {
  @RequestMapping(value = "/movies", method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<Movie>> getMovies(
    @RequestParam(value = "name", required = false) String name,
    @RequestParam(value = "genre", required = false) String genre,
    @RequestParam(value = "releaseDate", required = false) String releaseDate,
    @RequestParam(value = "sortBy", required = false) String sortBy,
    @RequestParam(value = "actor_id", required = false) Integer actorId) {

    IEMDB iemdb = IEMDB.getInstance();
    if (!iemdb.isLoggedIn()) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    ArrayList<Movie> movies = iemdb.getMovies(name, genre, releaseDate, actorId, sortBy);
    return new ResponseEntity<>(movies, HttpStatus.OK);
  }

  @RequestMapping(
    value = "/movies/{movie_id}",
    method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<Movie> getMovie(@PathVariable(value = "movie_id") int movieId) {
    IEMDB iemdb = IEMDB.getInstance();
    if (!iemdb.isLoggedIn()) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    return new ResponseEntity<>(iemdb.getMovieById(movieId), HttpStatus.OK);
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
    if (!iemdb.isLoggedIn()) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    else {
      iemdb.rateMovie(iemdb.getLoggedInUser().getEmail(), movieId, rate);
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

  @RequestMapping(
          value = "/comments",
          method = RequestMethod.GET,
          produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<List<Comment>> getComments(@RequestParam(value = "movie_id") int movieId) {
    IEMDB iemdb = IEMDB.getInstance();
    if (!iemdb.isLoggedIn()) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    return new ResponseEntity<>(iemdb.getMovieComments(movieId), HttpStatus.OK);
  }
}
