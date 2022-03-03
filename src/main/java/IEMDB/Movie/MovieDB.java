package IEMDB.Movie;

import java.util.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONObject;

public class MovieDB {
    Map<Integer, Movie> moviesById = new HashMap<>();

    public void addMovie(Movie movie) {
        moviesById.put(movie.getId(), movie);
    }

    public Movie getMovieById(Integer id) {
        return moviesById.get(id);
    }

    public String getMoviesList() {
        boolean comma = false;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        String output = "[";
        for (Movie mv : new ArrayList<>(moviesById.values())) {
            if (comma)
                output += ", ";
            String movieDetail = gson.toJson(mv);
            output += movieDetail;

            comma = true;
        }
        output += "]";

        Map<String, String> elements = new HashMap<>();
        elements.put("MoviesList", output);
        JSONObject json = new JSONObject(elements);

        return json.toString();
    }

    public String getMoviesByGenre(String genre) {
        List<String> output = new ArrayList<>();

        for (Movie mv : new ArrayList<>(moviesById.values())) {
            List<String> genres = mv.getGenres();
            if (genres != null && genres.contains(genre))
                output.add(mv.getSmallData());
        }

        return output.toString();
    }
}
