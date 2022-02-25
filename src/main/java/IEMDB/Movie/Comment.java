package IEMDB.Movie;

import java.util.HashMap;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Comment {
    private String text;
    private String userEmail;
    private Integer id;
    private Integer movieId;
    private Integer likesCount = 0;
    private Integer dislikesCount = 0;
    private Date writingTime;

    Map<String, Integer> voters = new HashMap<String, Integer>();

    public Comment(Integer id, String userEmail, Integer movieId, String text) {
        this.id = id;
        this.text = text;
        this.movieId = movieId;
        this.userEmail = userEmail;
        this.writingTime = new Date();

//        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//        System.out.println(formatter.format(writingTime));
    }

    public Integer getId() {
        return this.id;
    }

    public Integer getMovieId() {
        return this.movieId;
    }

    public void addVote(String userEmail, Integer vote) {
        if (voters.containsKey(userEmail)) {
            if (voters.get(userEmail) == -1)
                dislikesCount -= 1;
            else if (voters.get(userEmail) == 1)
                likesCount -= 1;
        }
        voters.put(userEmail, vote);
        if (vote == -1)
            dislikesCount += 1;
        else if (vote == 1)
            likesCount += 1;
    }

    public Map<String, Integer> getVoters() { return this.voters; }

    public Integer getLikesCount() { return likesCount; }

    public Integer getDislikesCount() { return dislikesCount; }
}
