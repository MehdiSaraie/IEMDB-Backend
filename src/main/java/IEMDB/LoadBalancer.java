package IEMDB;

import IEMDB.Exception.*;
import IEMDB.Movie.*;
import IEMDB.User.User;
import IEMDB.User.UserDB;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoadBalancer {
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

    public void addMovie(Movie movie) {
        try {
            castValidation(movie);
            movieDB.addMovie(movie);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void addUser(User user) throws Exception {
        User tempUser = userDB.getUserByEmail(user.getEmail());
        if (tempUser != null)
            throw new DuplicateEmailAddressException();

        userDB.addUser(user);
    }

    public void removeUser(User user) {
        userDB.removeUser(user);
    }

    public void addComment(String userEmail, Integer movieId, String text) throws Exception {
        Movie tempMovie = movieDB.getMovieById(movieId);
        if (tempMovie == null)
            throw new MovieNotFoundException();

        User tempUser = userDB.getUserByEmail(userEmail);
        if (tempUser == null)
            throw new UserNotFoundException();

        Comment tempCM = new Comment(uniqueCommentID, userEmail, movieId, text);
        tempMovie.addComment(tempCM);
        commentDB.addComment(tempCM);
        uniqueCommentID += 1;
    }
    public void addRateToMovie(Rate rate) throws Exception {
        Movie tempMovie = movieDB.getMovieById(rate.getMovieId());
        if (tempMovie == null)
            throw new MovieNotFoundException();

        User tempUser = userDB.getUserByEmail(rate.getEmail());
        if (tempUser == null)
            throw new UserNotFoundException();

        if (rate.getScore() < 1 || rate.getScore() > 10)
            throw new InvalidRateScoreException();

        tempMovie.addRate(rate);
    }
    public void addVoteToComment(String userEmail, Integer commentId, Integer vote) throws Exception{
        User tempUser = userDB.getUserByEmail(userEmail);
        if (tempUser == null)
            throw new UserNotFoundException();

        Comment tempComment = commentDB.getCommentById(commentId);
        if (tempComment == null)
            throw new CommentNotFoundException();

        if (vote != 1 && vote != -1 && vote != 0)
            throw new InvalidVoteValueException();

        tempComment.addVote(userEmail, vote);
//        tempUser.addVote(commentId, vote);
    }
    public void addMovieToWatchList(String userEmail, Integer movieId) throws Exception {
        Movie tempMovie = movieDB.getMovieById(movieId);
        if (tempMovie == null)
            throw new MovieNotFoundException();

        User tempUser = userDB.getUserByEmail(userEmail);
        if (tempUser == null)
            throw new UserNotFoundException();

        Integer ageLimit = tempMovie.getAgeLimit();
        tempUser.addToWatchList(movieId, ageLimit);
        //Agelimit Check Beshe.
    }
    public void removeMovieFromWatchList(String userEmail, Integer movieId) throws Exception {
        Movie tempMovie = movieDB.getMovieById(movieId);
        if (tempMovie == null)
            throw new MovieNotFoundException();

        User tempUser = userDB.getUserByEmail(userEmail);
        if (tempUser == null)
            throw new UserNotFoundException();

        tempUser.removeFromWatchList(movieId);
    }
    public String getMoviesList() {
        String moviesList = movieDB.getMoviesList();
        return moviesList;
    }

    public String getMovieById(Integer movieId) throws Exception{
        Movie tempMovie = movieDB.getMovieById(movieId);
        if (tempMovie == null)
            throw new MovieNotFoundException();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String movieDetail = gson.toJson(tempMovie);

        return movieDetail;
    }
    public String getMoviesByGenre(String genre) {
        String moviesData = movieDB.getMoviesByGenre(genre);
        Map<String, String> elements = new HashMap<>();
        elements.put("MoviesListByGenre", moviesData);
        JSONObject json = new JSONObject(elements);

        return json.toString();
    }
    public String getWatchList(String userEmail) throws Exception{
        User tempUser = userDB.getUserByEmail(userEmail);
        if (tempUser == null)
            throw new UserNotFoundException();

        List<Integer> watchList = tempUser.getWatchList();

        boolean comma = false;
        String output = "[";
        for (Integer i : watchList) {
            if (comma)
                output += ", ";
            Movie tempMovie = movieDB.getMovieById(i);
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