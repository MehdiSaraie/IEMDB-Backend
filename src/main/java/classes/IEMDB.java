package classes;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Tools.PairComparator.PairFIComparator;
import Tools.PairComparator.PairSIComparator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.util.Pair;


public class IEMDB {

    private ArrayList<Movie> movies;
    private ArrayList<Actor> actors;
    private ArrayList<User> users;
    private ArrayList<Comment> comments;

    private HashMap<Pair<String, Integer>, Integer> rates = new HashMap<>();
    private HashMap<Pair<String, Integer>, Integer> votes = new HashMap<>();

    private User loggedInUser;

    private static IEMDB iemdb;

    static {
        try {
            iemdb = new IEMDB();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public IEMDB() throws IOException, ParseException {
        this.loadActors();
        this.loadMovies();
        this.loadUsers();
        this.loadComments();
    }

    public void loadMovies() throws IOException, ParseException {
        URL url = new URL("http://138.197.181.131:5000/api/v2/movies");
        ObjectMapper objectMapper = new ObjectMapper();
        movies = objectMapper.readValue(url, new TypeReference<ArrayList<Movie>>() {
        });
        for (Movie movie : movies) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
            movie.setDate(formatter.parse(movie.getReleaseDate()));
            for (Integer actorId : movie.getCast()) {
                Actor actor = getActorById(actorId);
                actor.addActedMovies(movie.getId());
            }
        }
        setMovies(sortByImdb(movies));
    }

    public void loadActors() throws IOException, ParseException {
        URL url = new URL("http://138.197.181.131:5000/api/v2/actors");
        ObjectMapper objectMapper = new ObjectMapper();
        actors = objectMapper.readValue(url, new TypeReference<ArrayList<Actor>>() {
        });
      for (Actor actor: actors) {
            SimpleDateFormat formatter = new SimpleDateFormat("MMMM d, yyyy");
            try {
              actor.setDate(formatter.parse(actor.getBirthDate()));
            } catch (ParseException e) {
              actor.setDate(null);
            }
            actor.setAge(actor.calculateAge());
        }
    }

    public void loadUsers() throws IOException {
        URL url = new URL("http://138.197.181.131:5000/api/users");
        ObjectMapper objectMapper = new ObjectMapper();
        users = objectMapper.readValue(url, new TypeReference<ArrayList<User>>() {
        });
    }

    public void loadComments() throws IOException {
        URL url = new URL("http://138.197.181.131:5000/api/comments");
        ObjectMapper objectMapper = new ObjectMapper();
        comments = objectMapper.readValue(url, new TypeReference<ArrayList<Comment>>() {
        });
        for (int i = 0; i < comments.size(); i++) {
            Comment comment = comments.get(i);
            comments.get(i).setId(i + 1);
            Movie movie = getMovieById(comment.getMovieId());
            User user = getUserByEmail(comment.getUserEmail());
            comments.get(i).setUserNickname(user.getNickname());
            movie.addComment(comment);
        }
    }

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    public void setMovies(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    public static IEMDB getInstance() {
        return iemdb;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
    }

    public User getUserByEmail(String userEmail) {
        for (User user : this.users)
            if (user.getEmail().equals(userEmail))
                return user;
        return null;
    }

    public Movie getMovieById(Integer movieId) {
        for (Movie movie : this.movies)
            if (movie.getId() == movieId)
                return movie;
        return null;
    }

    public Actor getActorById(Integer actorId) {
        for (Actor actor : actors)
            if (actor.getId() == actorId)
                return actor;
        return null;
    }

    public Comment getCommentById(int commentId) {
        for (Comment comment : comments)
            if (comment.getId() == commentId)
                return comment;
        return null;
    }

    public ArrayList<Movie> searchMoviesByName(String searchValue, ArrayList<Movie> movies) {
      ArrayList<Movie> selectedMovies = new ArrayList<>();
      for (Movie movie : movies) {
        String movieName = movie.getName().toLowerCase();
        if (movieName.contains(searchValue.toLowerCase()))
          selectedMovies.add(movie);
      }
      return selectedMovies;
    }

    public ArrayList<Movie> searchMoviesByGenre(String searchValue, ArrayList<Movie> movies) {
      ArrayList<Movie> selectedMovies = new ArrayList<>();
      for (Movie movie : movies) {
        for (String genre : movie.getGenres()) {
          if (genre.toLowerCase().contains(searchValue.toLowerCase())) {
            selectedMovies.add(movie);
            break;
          }
        }
      }
      return selectedMovies;
    }

    public ArrayList<Movie> searchMoviesByReleaseDate(String searchValue, ArrayList<Movie> movies) {
      ArrayList<Movie> selectedMovies = new ArrayList<>();
      for (Movie movie : movies) {
        String releaseDate = movie.getReleaseDate();
        if (releaseDate.contains(searchValue))
          selectedMovies.add(movie);
      }
      return selectedMovies;
    }

    public ArrayList<Movie> sortByImdb(ArrayList<Movie> movies) {
        ArrayList<Pair<Float, Integer>> movieList = new ArrayList<>();
        for (Movie movie : movies)
            movieList.add(new Pair(movie.getImdbRate(), movie.getId()));
        movieList.sort(new PairFIComparator());
        ArrayList<Movie> sortedMovies = new ArrayList<>();
        for (Pair<Float, Integer> pair : movieList)
            sortedMovies.add(getMovieById(pair.getValue()));
        return sortedMovies;
    }

    public ArrayList<Movie> sortByDate(ArrayList<Movie> movies) {
        ArrayList<Pair<String, Integer>> movieList = new ArrayList<>();
        for (Movie movie : movies)
            movieList.add(new Pair(movie.getReleaseDate(), movie.getId()));
        movieList.sort(new PairSIComparator());
        ArrayList<Movie> sortedMovies = new ArrayList<>();
        for (Pair<String, Integer> pair : movieList)
            sortedMovies.add(getMovieById(pair.getValue()));
        return sortedMovies;
    }

    public void rateMovie(String userEmail, int movieId, int rate) {
        User user = IEMDB.getInstance().getUserByEmail(userEmail);
        Movie movie = IEMDB.getInstance().getMovieById(movieId);
        Pair<String, Integer> rater = new Pair<>(user.getEmail(), movie.getId());
        if (rates.containsKey(rater))
            movie.removeRate(rates.get(rater));
        movie.addRate(rate);
        rates.put(rater, rate);
    }

    public void addToWatchList(String userEmail, int movieId) throws CustomException {
        User user = getUserByEmail(userEmail);
        Movie movie = getMovieById(movieId);
        if (user.calculateAge() < movie.getAgeLimit())
            throw new CustomException("AgeLimitError");
        user.addToWatchList(movieId);
    }

    public void voteComment(String userEmail, int commentId, int vote) {
        User user = getUserByEmail(userEmail);
        Comment comment = getCommentById(commentId);
        Pair<String, Integer> key = new Pair<>(user.getEmail(), comment.getId());
        if (votes.containsKey(key))
            comment.removeVote(votes.get(key));
        comment.addVote(vote);
        votes.put(key, vote);
    }

    public void addComment(String userEmail, int movieId, String text) {
        Comment comment = new Comment(userEmail, movieId, text);
        comment.setId(comments.size()+1);
        Movie movie = getMovieById(comment.getMovieId());
        User user = getUserByEmail(comment.getUserEmail());
        comment.setUserNickname(user.getNickname());
        movie.addComment(comment);
        comments.add(comment);
    }
}
