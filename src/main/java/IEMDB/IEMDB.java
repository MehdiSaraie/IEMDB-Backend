package IEMDB;

import IEMDB.Exception.*;
import IEMDB.Movie.*;
import IEMDB.User.User;
import IEMDB.User.UserDB;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IEMDB {
    private UserDB userDB = new UserDB();
    private MovieDB movieDB = new MovieDB();
    private ActorDB actorDB = new ActorDB();
    private CommentDB commentDB = new CommentDB();

    private Integer uniqueCommentID = 1;

    private void castValidation(Movie movie) throws Exception {
        List<Integer> cast = movie.getCast();
        for (Integer actorId : cast) {
            if (!actorDB.actorExists(actorId))
                throw new ActorNotFoundException();
        }
    }

    public void addActor(Actor actor) {
        actorDB.addActor(actor);
    }

    public Actor findActor(Integer actorId) throws Exception{
        Actor actor = actorDB.findActor(actorId);
        if (actor == null)
            throw new ActorNotFoundException();

        return actor;
    }

    public void addMovie(Movie movie) {
        try {
            castValidation(movie);
            movieDB.addMovie(movie);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void addUser(User user) throws Exception {
        User tempUser = userDB.findUser(user.getEmail());
        if (tempUser != null)
            throw new DuplicateEmailAddressException();

        userDB.addUser(user);
    }

    public User findUser(String userEmail) throws Exception {
        User user = userDB.findUser(userEmail);
        if (user == null)
            throw new UserNotFoundException();
        return user;
    }

    public void removeUser(User user) {
        userDB.removeUser(user);
    }

    public void addComment(Comment comment) throws Exception {
        Movie tempMovie = movieDB.findMovie(comment.getMovieId());
        if (tempMovie == null)
            throw new MovieNotFoundException();

        User tempUser = userDB.findUser(comment.getUserEmail());
        if (tempUser == null)
            throw new UserNotFoundException();

        Comment tempCM = new Comment(uniqueCommentID, comment.getUserEmail(), comment.getMovieId(), comment.getText());
        tempMovie.addComment(tempCM);
        commentDB.addComment(tempCM);
        uniqueCommentID += 1;
    }
    public void addRateToMovie(Rate rate) throws Exception {
        Movie tempMovie = movieDB.findMovie(rate.getMovieId());
        if (tempMovie == null)
            throw new MovieNotFoundException();

        User tempUser = userDB.findUser(rate.getEmail());
        if (tempUser == null)
            throw new UserNotFoundException();

        if (rate.getScore() < 1 || rate.getScore() > 10)
            throw new InvalidRateScoreException();

        tempMovie.addRate(rate);
    }

    public void addVoteToComment(String userEmail, Integer commentId, Integer vote) throws Exception{
        User tempUser = userDB.findUser(userEmail);
        if (tempUser == null)
            throw new UserNotFoundException();

        Comment tempComment = commentDB.getCommentById(commentId);
        if (tempComment == null)
            throw new CommentNotFoundException();

        if (vote != 1 && vote != -1 && vote != 0)
            throw new InvalidVoteValueException();

        tempComment.addVote(userEmail, vote);
    }

    public void addMovieToWatchList(String userEmail, Integer movieId) throws Exception {
        Movie tempMovie = movieDB.findMovie(movieId);
        if (tempMovie == null)
            throw new MovieNotFoundException();

        User tempUser = userDB.findUser(userEmail);
        if (tempUser == null)
            throw new UserNotFoundException();

        Integer ageLimit = tempMovie.getAgeLimit();
        tempUser.addToWatchList(movieId, ageLimit);
    }

    public void removeMovieFromWatchList(String userEmail, Integer movieId) throws Exception {
        Movie tempMovie = movieDB.findMovie(movieId);
        if (tempMovie == null)
            throw new MovieNotFoundException();

        User tempUser = userDB.findUser(userEmail);
        if (tempUser == null)
            throw new UserNotFoundException();

        tempUser.removeFromWatchList(movieId);
    }

    public List<Movie> getMoviesList() {
        return movieDB.getMoviesList();
    }

    public Movie findMovie(Integer movieId) throws Exception{
        Movie movie = movieDB.findMovie(movieId);
        if (movie == null)
            throw new MovieNotFoundException();

        return movie;
    }

    public List<Movie> getMoviesByGenre(String genre) {
        return movieDB.getMoviesByGenre(genre);
    }

    public List<Movie> getMoviesByReleaseDate(Integer startYear, Integer endYear) {
        return movieDB.getMoviesByReleaseDate(startYear, endYear);
    }

    public String getWatchList(String userEmail) throws Exception{
        User tempUser = userDB.findUser(userEmail);
        if (tempUser == null)
            throw new UserNotFoundException();

        List<Integer> watchList = tempUser.getWatchList();

        boolean comma = false;
        String output = "[";
        for (Integer i : watchList) {
            if (comma)
                output += ", ";
            Movie tempMovie = movieDB.findMovie(i);
            String movieDetail = tempMovie.getSmallData();
            output += movieDetail;

            comma = true;
        }
        output += "]";

        Map<String, String> elements = new HashMap<>();
        elements.put("WatchList", output);
        JSONObject json = new JSONObject(elements);

        return json.toString();
    }

    public Comment getComment(Integer commentId) {
        return commentDB.getCommentById(commentId);
    }

    public Comment removeComment(Integer commentId) throws Exception {
        if (commentDB.getCommentById(commentId) == null)
            throw new CommentNotFoundException();
        uniqueCommentID -= 1;
        return commentDB.removeComment(commentId);
    }
}