package repository;


import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

import entities.Comment;
import entities.User;

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
        return "CREATE TABLE IF NOT EXISTS comments (" +
                "id INT NOT NULL AUTO_INCREMENT," +
                "user_id INT," +
                "movie_id INT," +
                "text TEXT," +
                "PRIMARY KEY (id))";
//                "FOREIGN KEY (user_id) REFERENCES users(id)," +
//                "FOREIGN KEY (movie_id) REFERENCES movies(id))";
    }

    @Override
    public PreparedStatement fillInsertQuery(PreparedStatement statement, Comment comment) throws SQLException {
        User user = UserRepository.getInstance().getByEmail(comment.getUserEmail());
        statement.setInt(1, user.getId());
        statement.setInt(2, comment.getMovieId());
        statement.setString(3, comment.getText());
        return statement;
    }

    @Override
    public ArrayList<String> getColumns() {
        return new ArrayList<String>(Arrays.asList("user_id", "movie_id", "text"));
    }

    @Override
    public Comment fillObjectFromResult(ResultSet result) throws SQLException {
        Comment comment = new Comment();
        comment.setId(result.getInt("id"));
        comment.setMovieId(result.getInt("movie_id"));
        comment.setUserId(result.getInt("user_id"));
        comment.setText(result.getString("text"));
        return comment;
    }

    public ArrayList<Comment> getByMovieId(int movieId) {
        ArrayList<Comment> comments = new ArrayList<>();
        try {
            Connection connection = dataSource.getConnection();

            PreparedStatement statement = connection.prepareStatement(
                    "SELECT ? FROM ? WHERE movie_id = ?"
            );
            statement.setArray(1, (Array) this.getColumns());
            statement.setString(2, this.getTableName());
            statement.setInt(3, movieId);
            ResultSet result = statement.executeQuery();

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

