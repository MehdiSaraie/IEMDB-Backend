package IEMDB.User;

import IEMDB.Exception.AgeLimitErrorException;
import IEMDB.Exception.MovieAlreadyExistsException;
import IEMDB.Exception.MovieNotFoundException;

import java.text.SimpleDateFormat;
import java.util.*;

public class User {
    private String email;
    private String password;
    private String name;
    private String nickname;
    private String birthDate;
    private List<Integer> userWatchList = new ArrayList<>();

    public User(String email, String password, String nickname, String name, String birthDate) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.name = name;
        this.birthDate = birthDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User that = (User) o;
        return email.equals(that.email);
    }

    public String getEmail() {
        return this.email;
    }

    public void addToWatchList(Integer movieId, Integer ageLimit) throws Exception {
        if (userWatchList == null)
            userWatchList = new ArrayList<>();

        if (userWatchList.contains(movieId))
            throw new MovieAlreadyExistsException();

        if (getAge() < ageLimit)
            throw new AgeLimitErrorException();

        userWatchList.add(movieId);
    }

    public void removeFromWatchList(Integer movieId) throws Exception {
        if (userWatchList == null)
            userWatchList = new ArrayList<>();

        if (!userWatchList.contains(movieId))
            throw new MovieNotFoundException();

        userWatchList.remove(movieId);
    }

    public List<Integer> getWatchList() { return userWatchList; }

    public long getAge() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = sdf.parse(birthDate);
        Date d2 = sdf.parse(sdf.format(new Date()));
        return d2.getYear() - d1.getYear();
    }
}