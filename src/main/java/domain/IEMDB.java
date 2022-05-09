package domain;

import java.io.IOException;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

import entities.*;
import repository.*;
import java.net.URL;

public class IEMDB {
    private User loggedInUser;
    private static IEMDB iemdb;

    static {
        try {
            iemdb = new IEMDB();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public IEMDB() throws IOException, ParseException, SQLException {
        String baseURL = "http://138.197.181.131:5000/api/v2/";
        MovieRepository.getInstance().loadFromURL(new URL(baseURL + "movies"));
        CastRepository.getInstance().loadFromURL(new URL(baseURL + "movies"));
        ActorRepository.getInstance().loadFromURL(new URL(baseURL + "actors"));
        UserRepository.getInstance().loadFromURL(new URL(baseURL + "users"));
        CommentRepository.getInstance().loadFromURL(new URL(baseURL + "comments"));
    }

    public static IEMDB getInstance() {
        return iemdb;
    }

    // --------------------------- authentication ---------------------------
    public void login(String email, String password) throws Exception {
        User user = UserRepository.getInstance().getByEmail(email);
        if (user == null) {
            throw new CustomException("User doesn't exist");
        }
        if (!user.getPassword().equals(password)) {
            throw new CustomException("Wrong password");
        }
        this.setLoggedInUser(user);
    }

    public void logout() {
        this.setLoggedInUser(null);
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
    }

    public Boolean isLoggedIn() {
        return this.loggedInUser != null;
    }

    // --------------------------- general methods ------------------------

    public Actor getActorById(Integer actorId) {
        Actor actor = ActorRepository.getInstance().getById(actorId);
        return actor;
    }

    public Movie getMovieById(int movieId) {
        Movie movie = MovieRepository.getInstance().getById(movieId);
        return movie;
    }

    // ------------------------ actor service methods ---------------------

    public ArrayList<Actor> getMovieCast(int movieId) {
        ArrayList<Integer> actorIds = CastRepository.getInstance().getMovieActorIds(movieId);
        ArrayList<Actor> actors = new ArrayList<>();
        for (int actorId : actorIds) {
            Actor actor = ActorRepository.getInstance().getById(actorId);
            actors.add(actor);
        }
        return actors;
    }

    // ------------------------ movie service methods ---------------------

    public ArrayList<Movie> getMovies(String name, String genre, String releaseDate, int actorId, String sortBy) {
        ArrayList<Movie> movies = MovieRepository.getInstance().search(name, genre, releaseDate, actorId, sortBy);
        return movies;
    }

    public void addComment(int movieId, String text) throws CustomException {
        User user = this.getLoggedInUser();
        if (user == null) {
            throw new CustomException("Not logged in");
        }
        Comment comment = new Comment(user.getEmail(), movieId, text);
        CommentRepository.getInstance().add(comment);
    }

    public ArrayList<Comment> getMovieComments(int movieId) {
        ArrayList<Comment> comments = CommentRepository.getInstance().getByMovieId(movieId);
        return comments;
    }

    public void rateMovie(String userEmail, int movieId, int rateValue) {
        User user = UserRepository.getInstance().getByEmail(userEmail);
        Movie movie = MovieRepository.getInstance().getById(movieId);
        Rate rate = new Rate(user.getId(), movieId, rateValue);
        RateRepository.getInstance().add(rate);
    }

    // ------------------------ comment service methods -------------------

    public void voteComment(int commentId, int voteValue) throws Exception {
        User user = getLoggedInUser();
        if (user == null) {
            throw new Exception();
        }
        Comment comment = CommentRepository.getInstance().getById(commentId);
        if (comment == null) {
            throw new Exception();
        }
        Vote vote  = new Vote(commentId, user.getId(), voteValue);
        VoteRepository.getInstance().add(vote);
    }

    // -------------------------watchlist service methods ---------------------

    public ArrayList<Movie> getWatchlist() {
        User user = this.getLoggedInUser();
        ArrayList<Movie> watchlist = WatchlistRepository.getInstance().getUserWatchlist(user.getid());
        return watchlist;
    }

    public void addToWatchList(int movieId) throws CustomException {
        User user = this.getLoggedInUser();
        Movie movie = MovieRepository.getInstance().getById(movieId);
        if (user.calculateAge() < movie.getAgeLimit())
            throw new CustomException("AgeLimitError");
        WatchlistRepository.getInstance().addToWatchlist(user.getId(), movieId);
    }

    public void removeFromWatchList(int movieId) {
        User user = this.getLoggedInUser();
        WatchlistRepository.getInstance().removeFromWatchlist(user.getId(), movieId);

    }





}
