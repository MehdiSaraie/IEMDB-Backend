package IEMDB.Movie;

import java.util.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONObject;

public class MovieDB {
    private List<Movie> movies = new ArrayList<>();
    Map<Integer, Movie> moviesById = new HashMap<>();

    public void addMovie(Movie movie) {
        movies.add(movie);
        moviesById.put(movie.getId(), movie);
    }

    public Movie getMovieById(Integer id) {
        return moviesById.get(id);
    }

    public String getMoviesList() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        List<String> output = new ArrayList<>();
        for (Movie mv : movies) {
            String movieDetail = gson.toJson(mv);
            output.add(movieDetail);
        }

        Map<String, String> elements = new HashMap<>();
        elements.put("MoviesList", output.toString());
        JSONObject json = new JSONObject(elements);

        return json.toString();
    }

    public String getMoviesByGenre(String genre) {
        List<String> output = new ArrayList<>();

        for (Movie mv : movies) {
            List<String> genres = mv.getGenres();
            if (genres != null && genres.contains(genre))
                output.add(mv.getSmallData());
        }

        return output.toString();
    }
}
