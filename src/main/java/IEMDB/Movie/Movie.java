package IEMDB.Movie;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.lang.reflect.Type;

public class Movie {
    private String name;
    private String summary;
    private String releaseDate;
    private String director;

    private Integer id;
    private Integer ageLimit;
    private Float imdbRate;
    private Float duration;
    private double rating = 0;
    private Integer ratingCount = 0;

    private List<String> writers = new ArrayList<>();
    private List<String> genres = new ArrayList<>();
    private List<Integer> cast = new ArrayList<>();
    private List<Comment> userComments = new ArrayList<>();

    Map<String, Rate> ratesByEmail = new HashMap<>();

    public Movie() {}

    public Movie(Integer id, String name, String summary, String releaseDate, String director,
                 ArrayList<String> writers, ArrayList<String> genres, ArrayList<Integer> cast,
                 Float imdbRate, Float duration, Integer ageLimit) {
        this.id = id;
        this.name = name;
        this.summary = summary;
        this.releaseDate = releaseDate;
        this.director = director;
        this.writers = writers;
        this.genres = genres;
        this.cast = cast;
        this.imdbRate = imdbRate;
        this.duration = duration;
        this.ageLimit = ageLimit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie that = (Movie) o;
        return id.equals(that.getId());
    }

    private void calcMovieRate(Boolean isNew, Integer newScore, Integer oldScore) {
        double tempSum = rating * ratingCount;
        if (isNew) {
            rating = (tempSum + newScore) / (ratingCount + 1);
        }
        else {
            tempSum = tempSum - oldScore + newScore;
            rating = tempSum / ratingCount;
        }

        ratingCount += 1;
    }

    public void addRate(Rate rate) {
        if (ratesByEmail.containsKey(rate.getEmail()))
            calcMovieRate(false, rate.getScore(), ratesByEmail.get(rate.getEmail()).getScore());
        else
            calcMovieRate(true, rate.getScore(), 0);

        ratesByEmail.put(rate.getEmail(), rate);
    }

    public void addComment(Comment comment) {
        userComments.add(comment);
    }

    public String getSmallData() {
        Map<String, String> elements = new HashMap<>();
        elements.put("movieId", this.id.toString());
        elements.put("name", this.name);
        elements.put("director", this.director);
        elements.put("genre", this.genres.toString());
        elements.put("rating", String.valueOf(this.rating));

        Gson gson = new Gson();
        Type gsonType = new TypeToken<HashMap>(){}.getType();
        String gsonString = gson.toJson(elements, gsonType);

        return gsonString;
    }

    public Integer getId() { return this.id; }
    public Integer getAgeLimit() { return this.ageLimit; }
    public List<Integer> getCast() { return this.cast; }
    public List<String> getGenres() { return this.genres; }

//    public String getName() { return this.name; }
//    public String getSummary() { return this.summary; }
//    public String getReleaseDate() { return this.releaseDate; }
//    public String getDirector() { return this.director; }
//    public List<String> getWriters() { return this.writers; }
//    public Float getImdbRate() { return this.imdbRate; }
//    public Float getDuration() { return this.duration; }

//    public void updateMovieInfo(Movie movie) {
//        this.name = movie.getName();
//        this.summary = movie.getSummary();
//        this.releaseDate = movie.getReleaseDate();
//        this.director = movie.getDirector();
//        this.writers = movie.getWriters();
//        this.genres = movie.getGenres();
//        this.cast = movie.getCast();
//        this.imdbRate = movie.getImdbRate();
//        this.duration = movie.getDuration();
//        this.ageLimit = movie.getAgeLimit();
//    }

}