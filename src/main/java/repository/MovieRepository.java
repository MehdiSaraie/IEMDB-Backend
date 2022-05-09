package repository;

import entities.Movie;

import java.sql.*;
import java.util.ArrayList;

public class MovieRepository extends Repository<Movie> {

    private static final String TABLE_NAME = "movies";
    private static MovieRepository instance;

    protected MovieRepository() throws SQLException {
    }

    public static MovieRepository getInstance() {
        try {
            if (instance == null)
                instance = new MovieRepository();
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
            "id SMALLINT," +
            "name VARCHAR," +
            "summary LONGVARCHAR," +
            "releaseDate VARCHAR," +
            "director VARCHAR," +
            "writers VARCHAR," +
            "genres VARCHAR," +
            "imdbRate DECIMAL," +
            "duration SMALLINT," +
            "ageLimit SMALLINT," +
            "image BLOB," +
            "coverImage BLOB," +
            "PRIMARY KEY (id)," +
        ")";
    }

    @Override
    public String getInsertQueryValues(Movie movie) {
        return " '" + movie.getName() + "'," +
                "'" + movie.getSummary() + "'," +
                "'" + movie.getReleaseDate() + "'," +
                "'" + movie.getDirector() + "'," +
                "'" + Float.toString(movie.getImdbRate()) + "'," +
                "'" + Integer.toString(movie.getDuration()) + "'," +
                "'" + Integer.toString(movie.getAgeLimit()) + "' ";
    }

    @Override
    public String getColumns() {
        return "id, name, summary, releaseDate, date, director, cast, imdbRate, duration, ageLimit, image, coverImage";
    }

    @Override
    public Movie fillObjectFromResult(Movie movie, ResultSet result) throws SQLException {
        movie.setId(result.getInt("id"));
        return movie;
    }

    public ArrayList<Movie> search(String name, String genre, String releaseDate, int actorId, String sortBy) {
        String query = String.format("SELECT %s FROM %s WHERE ", this.getColumns(), this,getTableName());

        ArrayList<String> whereClauses = new ArrayList<>();
        if (name != null) {
            whereClauses.add("");
        }
        if (genre != null) {
            whereClauses.add("");
        }
        if (releaseDate != null) {
            whereClauses.add("");
        }
        if (actorId > 0) {
            ArrayList<Integer> movieIds = CastRepository.getInstance().getActorMovieIds(actorId);
            whereClauses.add("id in (?)");
        }
        query += String.join(" AND ", whereClauses);

        if (sortBy.equals("imdbRate") || sortBy.equals("date")) {
            query += String.format(" ORDER BY %s DESC", sortBy);
        }

        ArrayList<Movie> movies = new ArrayList<>();
        try {
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();

            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                Movie movie = new Movie();
                movie = this.fillObjectFromResult(movie, result);
                movies.add(movie);
            }

            result.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movies;
    }
}
