package domain;

import java.io.IOException;

import java.sql.SQLException;
import java.util.ArrayList;

import controller.AuthenticationHelper;
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

    public IEMDB() throws IOException, SQLException {
        String version2 = "v2/";
        String baseURL = "http://138.197.181.131:5000/api/";
        ArrayList<Movie> movies = MovieRepository.getInstance().loadFromURL(new URL(baseURL + version2 + "movies"), (Class<Movie>) Movie.class);
        ArrayList<Actor> actors = ActorRepository.getInstance().loadFromURL(new URL(baseURL + version2 + "actors"), (Class<Actor>) Actor.class);
        CastRepository.getInstance().load(movies);
        GenreRepository.getInstance().load(movies);
        WriterRepository.getInstance().load(movies);
        ArrayList<User> users = UserRepository.getInstance().loadFromURL(new URL(baseURL + "users"), (Class<User>) User.class);
        ArrayList<Comment> comments = CommentRepository.getInstance().loadFromURL(new URL(baseURL + "comments"), (Class<Comment>) Comment.class);
        RateRepository.getInstance();
        VoteRepository.getInstance();
        WatchlistRepository.getInstance();
    }

    public static IEMDB getInstance() {
        return iemdb;
    }

    // --------------------------- authentication ---------------------------

    public void login(String email, String password) throws Exception {
        AuthenticationHelper authenticator = new AuthenticationHelper();
        User user = UserRepository.getInstance().getByEmail(email);
        if (user == null) {
            throw new CustomException("User doesn't exist");
        }
        if (!authenticator.passwordMatches(password, user.getPassword())) {
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

    public Boolean doesUserExist(String email) {
        User user = UserRepository.getInstance().getByEmail(email);
        return user != null;
    }

    public void signup(String name, String nickname, String birthDate, String email, String passwordHash) {
        User user = new User(email, passwordHash, nickname, name, birthDate);
        UserRepository.getInstance().add(user);
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

    public ArrayList<Movie> getMovies(String name, String genre, String releaseDate, Integer actorId, String sortBy) {
        ArrayList<Movie> movies = MovieRepository.getInstance().search(name, genre, releaseDate, actorId, sortBy);
        return movies;
    }

    public void addComment(int movieId, String text) throws CustomException {
        User user = this.getLoggedInUser();
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
        Rate rate = new Rate(user.getEmail(), movieId, rateValue);
        RateRepository.getInstance().add(rate);
    }

    // ------------------------ comment service methods -------------------

    public Vote voteComment(int commentId, int voteValue) {
        User user = this.getLoggedInUser();
        Comment comment = CommentRepository.getInstance().getById(commentId);
        Vote vote = null;
        if (comment != null) {
            vote = new Vote(commentId, user.getEmail(), voteValue);
            VoteRepository.getInstance().add(vote);
        }
        return vote;
    }

    // ------------------------ watchlist service methods ---------------------

    public ArrayList<Movie> getWatchlist() {
        User user = this.getLoggedInUser();
        ArrayList<Movie> watchlist = WatchlistRepository.getInstance().getUserWatchlist(user.getEmail());
        return watchlist;
    }

    public void addToWatchList(int movieId) throws CustomException {
        User user = this.getLoggedInUser();
        Movie movie = MovieRepository.getInstance().getById(movieId);
        if (user.calculateAge() < movie.getAgeLimit())
            throw new CustomException("AgeLimitError");
        Watchlist watchlist = new Watchlist(movieId, user.getEmail());
        WatchlistRepository.getInstance().add(watchlist);
    }

    public void removeFromWatchList(int movieId) {
        User user = this.getLoggedInUser();
        WatchlistRepository.getInstance().removeFromWatchlist(user.getEmail(), movieId);
    }
}
