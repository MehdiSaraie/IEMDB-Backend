package repository;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import entities.Comment;

public class CommentRepository extends Repository<Comment> {

    private static final String TABLE_NAME = "comments";
    private static CommentRepository instance;

    protected CommentRepository() throws SQLException {
    }

    public static CommentRepository getInstance() {
        try {
            if (instance == null)
                instance = new CommentRepository();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in creating table");
        }
        return instance;
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String getCreateTableQuery() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                "" +
                "" +
                "" +
                ")";
    }

    @Override
    public String getInsertQueryValues(Comment comment) {
        return "'" + "" + "'," +
                "'" + "" + "'," +
                "'" + + "'," +
                "'" + + "'," +
                "'" + + "'," +
    }

    @Override
    public String getColumns() {
        return "";
    }

    @Override
    public Comment fillObjectFromResult(Comment comment, ResultSet result) throws SQLException {
        comment.setId(result.getInt("id"));
        return comment;
    }

    public ArrayList<Comment> getByMovieId(int movieId) {
        ArrayList<Comment> comments = new ArrayList<>();
        try {
            Connection connection = dataSource.getConnection();

            PreparedStatement statement = connection.prepareStatement(
                    "SELECT ? FROM ? WHERE movie_id = ?"
            );
            statement.setString(1, this.getColumns());
            statement.setString(2, this.getTableName());
            statement.setInt(3, movieId);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                Comment comment = this.fillObjectFromResult(new Comment(), result);
                comments.add(comment);
            }

            result.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comments;
    }
}

