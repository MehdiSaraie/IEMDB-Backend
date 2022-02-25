package IEMDB.User;

import IEMDB.Exception.MovieAlreadyExistsException;
import IEMDB.Exception.MovieNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
    private String email;
    private String password;
    private String name;
    private String nickname;
    private String birthDate;
    private List<Integer> userWatchList = new ArrayList<>();
    Map<Integer, Integer> voting = new HashMap<>();

//    private List<Comment> userCM = new ArrayList<>();

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

    public void addToWatchList(Integer movieId, Integer ageLimit) throws Exception{
        if (userWatchList == null)
            userWatchList = new ArrayList<>();

        if (userWatchList.contains(movieId))
            throw new MovieAlreadyExistsException();

        userWatchList.add(movieId);
    }

    public void removeFromWatchList(Integer movieId) throws Exception{
        if (userWatchList == null)
            userWatchList = new ArrayList<>();

        if (!userWatchList.contains(movieId))
            throw new MovieNotFoundException();

        userWatchList.remove(movieId);
    }

    public List<Integer> getWatchList() { return userWatchList; }

    public void addVote(Integer commentId, Integer vote) {
        voting.put(commentId, vote);
    }
}