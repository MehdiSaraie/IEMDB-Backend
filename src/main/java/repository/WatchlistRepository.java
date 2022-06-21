package repository;

import entities.Movie;
import entities.Watchlist;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class WatchlistRepository extends Repository<Watchlist>{
    private static WatchlistRepository instance;
    private String TABLE_NAME = "watchlist";

    protected WatchlistRepository() throws SQLException {
    }

    public static WatchlistRepository getInstance() {
        try {
            if (instance == null)
                instance = new WatchlistRepository();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in creating table");
        }
        return instance;
    }


    @Override
    public String getCreateTableQuery() {
        return "CREATE TABLE IF NOT EXISTS watchlist (" +
                "movie_id INT," +
                "user_email VARCHAR(50)," +
                "PRIMARY KEY (user_email, movie_id)," +
                "FOREIGN KEY (user_email) REFERENCES users(email)," +
                "FOREIGN KEY (movie_id) REFERENCES movies(id)" +
                ")";
    }

    public String getTableName() {
        return this.TABLE_NAME;
    }

    @Override
    public ArrayList<String> getColumns() {
        return new ArrayList<>(Arrays.asList("movie_id", "user_email"));
    }

    @Override
    public PreparedStatement fillInsertQuery(PreparedStatement statement, Watchlist watchlist) throws SQLException {
        statement.setInt(1, watchlist.getMovieId());
        statement.setString(2, watchlist.getUserEmail());
        return statement;
    }

    @Override
    public Watchlist fillObjectFromResult(ResultSet result) throws SQLException {
        Watchlist watchlist = new Watchlist();
        watchlist.setMovieId(result.getInt("movie_id"));
        watchlist.setUserEmail(result.getString("user_email"));
        return watchlist;
    }

    public ArrayList<Movie> getUserWatchlist(String userEmail) {
        ArrayList<Movie> watchlist = new ArrayList<>();
        try {
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();

            String query = String.format("SELECT movie_id FROM watchlist WHERE user_email='%s'", userEmail);
            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                int movieId = result.getInt(1);
                Movie movie = MovieRepository.getInstance().getById(movieId);
                watchlist.add(movie);
            }

            result.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return watchlist;
    }

    public void removeFromWatchlist(String userEmail, int movieId) {
        try {
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();

            String query = String.format("DELETE FROM watchlist WHERE user_email='%s' AND movie_id=%d", userEmail, movieId);
            statement.execute(query);

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
