package IEMDB.Movie;

import java.text.SimpleDateFormat;
import java.util.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONObject;

public class MovieDB {
    Map<Integer, Movie> moviesById = new HashMap<>();

    public void addMovie(Movie movie) {
        moviesById.put(movie.getId(), movie);
    }

    public Movie findMovie(Integer id) {
        return moviesById.get(id);
    }

    public List<Movie> getMoviesList() {
        return new ArrayList<>(moviesById.values());
    }

    public List<Movie> getMoviesByGenre(String genre) {
        List<Movie> output = new ArrayList<>();
        for (Movie mv : new ArrayList<>(moviesById.values())) {
            List<String> genres = mv.getGenres();
            if (genres != null && genres.contains(genre))
                output.add(mv);
        }
        return output;
    }

    public List<Movie> getMoviesByActor(Integer actorId) {
        List<Movie> output = new ArrayList<>();
        for (Movie mv : new ArrayList<>(moviesById.values())) {
            List<Integer> actorIds = mv.getCast();
            if (actorIds != null && actorIds.contains(actorId))
                output.add(mv);
        }
        return output;
    }

    public List<Movie> getMoviesByReleaseDate(Integer startYear, Integer endYear){
        try {
            List<Movie> output = new ArrayList<>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for (Movie mv : new ArrayList<>(moviesById.values())) {
                Integer releaseYear = sdf.parse(mv.getReleaseDate()).getYear() + 1900;
                if (releaseYear >= startYear && releaseYear <= endYear)
                    output.add(mv);
            }
            return output;
        }catch (Exception e){
            System.out.println(e.getStackTrace());
            return null;
        }
    }
}
