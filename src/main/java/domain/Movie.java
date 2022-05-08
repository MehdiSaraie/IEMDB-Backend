package domain;

import java.util.ArrayList;
import java.util.Date;

public class Movie {
    private int id;
    private String name;
    private String summary;
    private String releaseDate;
    private Date date;
    private String director;
    private ArrayList<String> writers;
    private ArrayList<String> genres;
    private ArrayList<Integer> cast;
    private float imdbRate;
    private int duration;
    private int ageLimit;
    private String image;
    private String coverImage;
    private ArrayList<Comment> comments = new ArrayList<>();
    private float rating;
    private int ratingCount;

    public Movie(int id, String name, String summary, String releaseDate, String director, ArrayList<String> writers,
                 ArrayList<String> genres, ArrayList<Integer> cast, float imdbRate, int duration, int ageLimit, String image, String coverImage) {
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
        this.image = image;
        this.coverImage = coverImage;
    }

    public Movie() {

    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    public void removeRate(int rate) {
        if (ratingCount > 1)
            this.rating = (this.rating * this.ratingCount - rate) / (this.ratingCount - 1);
        else
            this.rating = 0;
        this.ratingCount--;
    }

    public void addRate(int rate) {
        this.rating = (this.rating * this.ratingCount + rate) / (this.ratingCount + 1);
        this.ratingCount++;
    }

//    public MovieBriefInfo getBriefInfo() {
//        return new MovieBriefInfo(this.id, this.name, this.director, this.genres, this.rating);
//    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releasDate) {
        this.releaseDate = releasDate;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public ArrayList<String> getWriters() {
        return writers;
    }

    public void setWriters(ArrayList<String> writers) {
        this.writers = writers;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }

    public ArrayList<Integer> getCast() {
        return cast;
    }

    public void setCast(ArrayList<Integer> cast) {
        this.cast = cast;
    }

    public float getImdbRate() {
        return imdbRate;
    }

    public void setImdbRate(float imdbRate) {
        this.imdbRate = imdbRate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getAgeLimit() {
        return ageLimit;
    }

    public void setAgeLimit(int ageLimit) {
        this.ageLimit = ageLimit;
    }

    public String getImage() { return image; }

    public void setImage(String image) { this.image = image; }

    public String getCoverImage() { return coverImage; }

    public void setCoverImage(String coverImage) { this.coverImage = coverImage; }

    public float getRating() { return rating; }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getRatingCount() { return ratingCount; }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public int getSimilarly(Movie movie2) {
        int similarity = 0;
        for (String genre : this.getGenres())
            if (movie2.getGenres().contains(genre))
                similarity++;
        return similarity;
    }
}
