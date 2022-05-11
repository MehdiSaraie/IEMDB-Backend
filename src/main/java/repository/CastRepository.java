package repository;

import entities.Cast;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class CastRepository extends Repository<Cast>{

    private static final String TABLE_NAME = "cast";
    private static CastRepository instance;

    protected CastRepository() throws SQLException {
        super();
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
        return "CREATE TABLE IF NOT EXISTS cast (" +
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
    public ArrayList<String> getColumns() {
        return new ArrayList<>(Arrays.asList("movie_id", "actor_id"));
    }

    @Override
    public Cast fillObjectFromResult(ResultSet result) throws SQLException {
        Cast cast = new Cast();
        cast.setActorId(result.getInt("actor_id"));
        cast.setMovieId(result.getInt("movie_id"));
        return cast;
    }

    @Override
    public PreparedStatement fillInsertQuery(PreparedStatement statement, Cast cast) throws SQLException {
        statement.setInt(1, cast.getMovieId());
        statement.setInt(2, cast.getActorId());
        return statement;
    }

    public ArrayList<Integer> getMovieActorIds(int movieId) {
        ArrayList<Integer> actorIds = new ArrayList<>();
        try {
            Connection connection = dataSource.getConnection();

            PreparedStatement statement = connection.prepareStatement(
                    "SELECT actor_id FROM cast where movie_id = ?"
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
                    "SELECT movie_id FROM cast where actor_id = ?"
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
