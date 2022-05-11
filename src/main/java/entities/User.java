package entities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class User {
    private int id;
    private String email;
    private String password;
    private String nickname;
    private String name;
    private String birthDate;
    private Date bDay;

    public User(String email, String password, String nickname, String name, String birthDate) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.name = name;
        this.birthDate = birthDate;
        this.getBDateFromString();
    }

    public User() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public void getBDateFromString() {
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM d, yyyy");
        try {
            this.setBDay(formatter.parse(this.getBirthDate()));
        } catch (ParseException e) {
            this.setBDay(null);
        }

    }

    public Date getBDay() {
        return bDay;
    }

    public void setBDay(Date bDay) {
        this.bDay = bDay;
    }

    public int calculateAge() {
        return new Date().getYear() - this.getBDay().getYear();
    }

}
