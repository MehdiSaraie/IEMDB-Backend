package repository;

import entities.Cast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CastRepository extends Repository<Cast>{

    private static final String TABLE_NAME = "cast";
    private static CastRepository instance;

    protected CastRepository() throws SQLException {
    }

    public static CastRepository getInstance() {
        try {
            if (instance == null)
                instance = new CastRepository();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in creating table");
        }
        return instance;
    }

    @Override
    public String getCreateTableQuery() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                "movie_id SMALLINT," +
                "actor_id SMALLINT," +
                "PRIMARY KEY (movie_id, actor_id)," +
                "FOREIGN KEY movie_id REFERENCES(movies.id)," +
                "FOREIGN KEY actor_id REFERENCES(actors.id)," +
                ")";
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String getInsertQueryValues(Cast cast) {
        return "" + Integer.toString(cast.getActorId()) + "," +
                "" + Integer.toString(cast.getMovieId());
    }

    @Override
    public String getColumns() {
        return null;
    }

    @Override
    public Cast fillObjectFromResult(Cast cast, ResultSet result) throws SQLException {
        cast.setActorId(result.getInt("actor_id"));
        cast.setMovieId(result.getInt("movie_id"));
        return cast;
    }

    public ArrayList<Integer> getMovieActorIds(int movieId) {
        ArrayList<Integer> actorIds = new ArrayList<>();
        try {
            Connection connection = dataSource.getConnection();

            PreparedStatement statement = connection.prepareStatement(
                    "SELECT actor_id FROM plays_in where movie_id = ?"
            );
            statement.setInt(1, movieId);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                actorIds.add(result.getInt(1));
            }

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return actorIds;
    }

    public ArrayList<Integer> getActorMovieIds(int actorId) {
        ArrayList<Integer> movieIds = new ArrayList<>();
        try {
            Connection connection = dataSource.getConnection();

            PreparedStatement statement = connection.prepareStatement(
                    "SELECT movie_id FROM plays_in where actor_id = ?"
            );
            statement.setInt(1, actorId);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                movieIds.add(result.getInt(1));
            }

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movieIds;
    }
}
