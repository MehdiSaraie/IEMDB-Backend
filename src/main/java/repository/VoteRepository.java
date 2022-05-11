package repository;

import entities.Vote;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class VoteRepository extends Repository<Vote>{
    private static final String TABLE_NAME = "votes";
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
        return "CREATE TABLE IF NOT EXISTS comments (" +
                "id INT NOT NULL AUTO_INCREMENT," +
                "comment_id INT," +
                "user_id INT," +
                "vote INT," +
                "PRIMARY KEY (id))";
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public ArrayList<String> getColumns() {
        return new ArrayList<>(Arrays.asList("comment_id", "user_id", "vote"));
    }

    @Override
    public PreparedStatement fillInsertQuery(PreparedStatement statement, Vote vote) throws SQLException {
        statement.setInt(1, vote.getCommentId());
        statement.setInt(2, vote.getUserId());
        statement.setInt(3, vote.getVote());
        return statement;
    }

    @Override
    public Vote fillObjectFromResult(ResultSet result) throws SQLException {
        Vote vote = new Vote();
        vote.setId(result.getInt("id"));
        vote.setCommentId(result.getInt("comment_id"));
        vote.setUserId(result.getInt("user_id"));
        vote.setVote(result.getInt("vote"));
        return vote;
    }


}
