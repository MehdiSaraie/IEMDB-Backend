package repository;

import entities.Vote;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class VoteRepository extends Repository<Vote>{
    private String TABLE_NAME = "votes";
    private static VoteRepository instance;

    protected VoteRepository() throws SQLException {
    }

    public static VoteRepository getInstance() {
        try {
            if (instance == null)
                instance = new VoteRepository();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in creating table");
        }
        return instance;
    }
    @Override
    public String getCreateTableQuery() {
        return "CREATE TABLE IF NOT EXISTS votes (" +
                "comment_id INT," +
                "user_email VARCHAR(50)," +
                "vote INT," +
                "PRIMARY KEY (comment_id, user_email)," +
                "FOREIGN KEY (user_email) REFERENCES users(email)," +
                "FOREIGN KEY (comment_id) REFERENCES comments(id)" +
                ")";
    }

    public String getTableName() {
        return this.TABLE_NAME;
    }

    @Override
    public ArrayList<String> getColumns() {
        return new ArrayList<>(Arrays.asList("comment_id", "user_email", "vote"));
    }

    @Override
    public PreparedStatement fillInsertQuery(PreparedStatement statement, Vote vote) throws SQLException {
        statement.setInt(1, vote.getCommentId());
        statement.setString(2, vote.getUserEmail());
        statement.setInt(3, vote.getVote());
        return statement;
    }

    @Override
    public Vote fillObjectFromResult(ResultSet result) throws SQLException {
        Vote vote = new Vote();
        vote.setId(result.getInt("id"));
        vote.setCommentId(result.getInt("comment_id"));
        vote.setUserEmail(result.getString("user_email"));
        vote.setVote(result.getInt("vote"));
        return vote;
    }


}
