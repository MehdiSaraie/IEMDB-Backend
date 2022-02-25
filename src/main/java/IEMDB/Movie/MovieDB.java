package IEMDB.Movie;

import java.util.*;

import IEMDB.Exception.ActorNotFoundException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
//import jdk.internal.org.jline.reader.ScriptEngine;
import org.apache.commons.lang3.BooleanUtils;

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

    public void getMoviesList() {

        StringBuilder output = new StringBuilder();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        for (Movie mv : movies) {
            String movieDetail = gson.toJson(mv);
            output.append(movieDetail);
        }
    }

    public Movie getMoviesByGenre(String genre) {
        System.out.println("Hanooz Kamel Nashode");
        return new Movie();
    }
}
