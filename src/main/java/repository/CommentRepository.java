package repository;


import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

import entities.Comment;
import entities.User;

public class CommentRepository extends Repository<Comment> {

    private String TABLE_NAME = "comments";
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
    public String getCreateTableQuery() {
        return "CREATE TABLE IF NOT EXISTS comments (" +
                "id INT AUTO_INCREMENT," +
                "user_email VARCHAR(50)," +
                "movie_id INT," +
                "text TEXT," +
                "PRIMARY KEY (id)," +
                "FOREIGN KEY (user_email) REFERENCES users(email)," +
                "FOREIGN KEY (movie_id) REFERENCES movies(id)" +
                ")";
    }

    public String getTableName() {
        return this.TABLE_NAME;
    }

    @Override
    public PreparedStatement fillInsertQuery(PreparedStatement statement, Comment comment) throws SQLException {
        statement.setString(1, comment.getUserEmail());
        statement.setInt(2, comment.getMovieId());
        statement.setString(3, comment.getText());
        return statement;
    }

    @Override
    public ArrayList<String> getColumns() {
        return new ArrayList<String>(Arrays.asList("user_email", "movie_id", "text"));
    }

    @Override
    public Comment fillObjectFromResult(ResultSet result) throws SQLException {
        Comment comment = new Comment();
        comment.setId(result.getInt("id"));
        comment.setMovieId(result.getInt("movie_id"));
        comment.setUserEmail(result.getString("user_email"));
        comment.setText(result.getString("text"));
        return comment;
    }

    public ArrayList<Comment> getByMovieId(int movieId) {
        ArrayList<Comment> comments = new ArrayList<>();
        try {
            Connection connection = dataSource.getConnection();

            String colStr = String.join(",", this.getColumns());
            String query = String.format("SELECT id,%s FROM %s WHERE movie_id=%d", colStr, this.getTableName(), movieId);
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);

            while (result.next()) {
                Comment comment = this.fillObjectFromResult(result);
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

