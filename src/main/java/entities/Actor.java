package entities;

import java.util.ArrayList;
import java.util.Date;

public class Actor {
    private int id;
    private String name;
    private String birthDate;
    private Date date;
    private String nationality;
    private String image;
    private Integer age;

    public Actor(int id, String name, String birthDate, String nationality, String image) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.nationality = nationality;
        this.image = image;
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

    public Integer calculateAge() {
      if (this.date == null)
        return null;
      Date now = new Date();
      return now.getYear() - this.date.getYear();
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getImage() { return image; }

    public void setImage(String image) { this.image = image; }

    public Integer getAge() { return age; }

    public void setAge(Integer age) { this.age = age; }
}
