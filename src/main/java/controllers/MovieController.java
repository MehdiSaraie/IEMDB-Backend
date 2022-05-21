package controllers;

import classes.Comment;
import classes.IEMDB;
import classes.Movie;
import classes.User;
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
  public List<Movie> getMovies(
    @RequestParam(value = "name", required = false) String name,
    @RequestParam(value = "genre", required = false) String genre,
    @RequestParam(value = "releaseDate", required = false) String releaseDate,
    @RequestParam(value = "sortBy", required = false) String sortBy,
    @RequestParam(value = "actor_id", required = false) Integer actorId) {
    IEMDB iemdb = IEMDB.getInstance();
    ArrayList<Movie> selectedMovies = iemdb.getMovies();
    if (actorId != null) {
      ArrayList<Movie> actedMovies = new ArrayList<>();
      for (Movie movie : selectedMovies) {
        if (movie.getCast().contains(actorId))
          actedMovies.add(movie);
      }
      return actedMovies;
    }
    if (name != null && !name.isEmpty())
      selectedMovies = iemdb.searchMoviesByName(name, selectedMovies);
    if (genre != null && !genre.isEmpty())
      selectedMovies = iemdb.searchMoviesByGenre(genre, selectedMovies);
    if (releaseDate != null && !releaseDate.isEmpty())
      selectedMovies = iemdb.searchMoviesByReleaseDate(releaseDate, selectedMovies);
    if (sortBy != null) {
      if (sortBy.equals("date"))
        selectedMovies = iemdb.sortByDate(selectedMovies);
      if (sortBy.equals("imdbRate"))
        selectedMovies = iemdb.sortByImdb(selectedMovies);
    }
    return selectedMovies;
  }

  @RequestMapping(value = "/movies/{movie_id}", method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
  public Movie getMovie(@PathVariable(value = "movie_id") int movieId) {
    return IEMDB.getInstance().getMovieById(movieId);
  }

  @RequestMapping(value = "/movies/{movie_id}/addRate", method = RequestMethod.POST,
    produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> rateMovie(
    @PathVariable(value = "movie_id") int movieId,
    @RequestParam(value = "rate") int rate) {
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

  @RequestMapping(value = "/movies/{movie_id}/addComment", method = RequestMethod.POST,
    produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<Comment>> addCommentToMovie(
    @PathVariable(value = "movie_id") int movieId,
    @RequestBody() String commentText) {
    IEMDB iemdb = IEMDB.getInstance();
    User currentUser = iemdb.getLoggedInUser();
    if (currentUser == null) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    else {
      iemdb.addComment(currentUser.getEmail(), movieId, commentText);
      return new ResponseEntity<>(iemdb.getMovieById(movieId).getComments(), HttpStatus.OK);
    }
  }
}
