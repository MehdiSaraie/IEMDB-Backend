package repository;

import entities.Movie;
import entities.Watchlist;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class WatchlistRepository extends Repository<Watchlist>{
    private static WatchlistRepository instance;
    private static final String TABLE_NAME = "watchlist";

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
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String getCreateTableQuery() {
        return "CREATE TABLE IF NOT EXISTS watchlist (" +
                "id INT NOT NULL AUTO_INCREMENT," +
                "movie_id INT," +
                "user_id INT" +
                "PRIMARY_KEY(user_id, movie_id)," +
                "FOREIGN_KEY user_id REFERENCES user(id)" +
                "FOREIGN_KEY movie_id REFERENCES movie(id))";
    }

    @Override
    public ArrayList<String> getColumns() {
        return new ArrayList<>(Arrays.asList("movie_id", "user_id"));
    }

    @Override
    public PreparedStatement fillInsertQuery(PreparedStatement statement, Watchlist watchlist) throws SQLException {
        statement.setInt(1, watchlist.getMovieId());
        statement.setInt(2, watchlist.getUserId());
        return statement;
    }

    @Override
    public Watchlist fillObjectFromResult(ResultSet result) throws SQLException {
        Watchlist watchlist = new Watchlist();
        watchlist.setMovieId(result.getInt("movie_id"));
        watchlist.setUserId(result.getInt("user_id"));
        return watchlist;
    }

    public ArrayList<Movie> getUserWatchlist(int userId) {
        ArrayList<Movie> watchlist = new ArrayList<>();
        try {
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();

            ResultSet result = statement.executeQuery(String.format("SELECT movie_id FROM watchlist WHERE user_id=%d", userId));
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

    //TODO
    public void removeFromWatchlist(int userId, int movieId) {
    }

    //TODO
    public void addToWatchlist(int userId, int movieId) {
    }
}
