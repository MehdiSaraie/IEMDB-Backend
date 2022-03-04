package IEMDB.Movie;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Movie {
    private String name;
    private String summary;
    private String releaseDate;
    private String director;

    private Integer id;
    private Integer ageLimit;
    private Double imdbRate;
    private Integer duration;
    private Double rating = null;
    private Integer ratingCount = 0;

    private List<String> writers = new ArrayList<>();
    private List<String> genres = new ArrayList<>();
    private List<Integer> cast = new ArrayList<>();
    private List<Comment> userComments = new ArrayList<>();
    Map<String, Rate> ratesByEmail = new HashMap<>();

    public Movie() {}

    public Movie(Integer id, String name, String summary, String releaseDate, String director,
                 ArrayList<String> writers, ArrayList<String> genres, ArrayList<Integer> cast,
                 Double imdbRate, Integer duration, Integer ageLimit) {
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
        if (rating == null)
            rating = 0.0;
        Double tempSum = rating * ratingCount;
        if (isNew) {
            ratingCount += 1;
            rating = (tempSum + newScore) / ratingCount;
        }
        else {
            tempSum = tempSum - oldScore + newScore;
            rating = tempSum / ratingCount;
        }
    }

    public void addRate(Rate rate) {
        if (ratesByEmail.containsKey(rate.getEmail()))
            calcMovieRate(false, rate.getScore(), ratesByEmail.get(rate.getEmail()).getScore());
        else
            calcMovieRate(true, rate.getScore(), null);

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

        Gson gson = new Gson();
        Type gsonType = new TypeToken<HashMap>(){}.getType();
        String gsonString = gson.toJson(elements, gsonType);

        return gsonString;
    }

    public Integer getId() { return this.id; }
    public String getName() { return this.name; }
    public String getSummary() { return this.summary; }
    public String getReleaseDate() { return this.releaseDate; }
    public String getDirector() { return this.director; }
    public List<String> getWriters() { return this.writers; }
    public List<String> getGenres() { return this.genres; }
    public List<Integer> getCast() { return this.cast; }
    public Double getImdbRate() { return this.imdbRate; }
    public Integer getDuration() { return this.duration; }
    public Integer getAgeLimit() { return this.ageLimit; }
    public Map<String, Rate> getRatesByEmail() { return this.ratesByEmail; }
    public List<Comment> getUserComments() { return this.userComments; }
    public Double getRating() { return this.rating; }

}