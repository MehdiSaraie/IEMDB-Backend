package domain;

import java.util.ArrayList;
import java.util.Date;

public class User {
    private String email;
    private String password;
    private String nickname;
    private String name;
    private Date birthDate;
    private ArrayList<Integer> watchList = new ArrayList<>();

    public User(String email, String password, String nickname, String name, Date birthDate) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.name = name;
        this.birthDate = birthDate;
    }

    public User() {

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

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public ArrayList<Integer> getWatchList() {
        return this.watchList;
    }


    public int calculateAge() {
        Date now = new Date();
        return now.getYear() - this.birthDate.getYear();
    }

    public void addToWatchList(int movieId) {
        this.watchList.add(movieId);
    }

    public void removeFromWatchList(int movieId) {
        this.watchList.remove(new Integer(movieId));
    }
}
