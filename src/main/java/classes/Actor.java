package classes;

import java.util.ArrayList;
import java.util.Date;

public class Actor {
    private int id;
    private String name;
    private String birthDate;
    private Date date;
    private String nationality;
    private ArrayList<Integer> actedMovies = new ArrayList<>();

    public Actor(int id, String name, String birthDate, String nationality) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.nationality = nationality;
    }

    public Actor() {

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

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public int calculateAge() {
        Date now = new Date();
        return now.getYear() - this.date.getYear();
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void addActedMovies(int movieId) {
        this.actedMovies.add(movieId);
    }

    public ArrayList<Integer> getActedMovie() {
        return this.actedMovies;
    }
}
