package repository;

import entities.Movie;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

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
        return "CREATE TABLE IF NOT EXISTS movies (" +
            "id INT NOT NULL AUTO_INCREMENT," +
            "name TEXT," +
            "summary TEXT," +
            "releaseDate DATE," +
            "director TEXT," +
            "imdbRate DECIMAL," +
            "duration INT," +
            "ageLimit INT," +
            "image TEXT," +
            "coverImage TEXT," +
            "PRIMARY KEY (id)" +
        ")";
    }

    @Override
    public ArrayList<String> getColumns() {
        return new ArrayList<>(Arrays.asList("name", "summary", "releaseDate", "director", "imdbRate", "duration",
                "ageLimit", "image", "coverImage"));
    }

    @Override
    public PreparedStatement fillInsertQuery(PreparedStatement statement, Movie movie) throws SQLException {
        statement.setString(1, movie.getName());
        statement.setString(2, movie.getSummary());
        statement.setString(3, movie.getReleaseDate());
        statement.setString(4, movie.getDirector());
        statement.setFloat(5, movie.getImdbRate());
        statement.setInt(6, movie.getDuration());
        statement.setInt(7, movie.getAgeLimit());
        statement.setString(8, movie.getImage());
        statement.setString(9, movie.getCoverImage());
        return statement;
    }

    @Override
    public Movie fillObjectFromResult(ResultSet result) throws SQLException {
        Movie movie = new Movie();
        movie.setId(result.getInt("id"));
        movie.setName(result.getString("name"));
        movie.setSummary(result.getString("summary"));
        // @TODO
        movie.setReleaseDate(result.getString("releaseDate"));
        movie.setDirector(result.getString("director"));
        movie.setImdbRate(result.getFloat("imdbRate"));
        movie.setDuration(result.getInt("duration"));
        movie.setAgeLimit(result.getInt("ageLimit"));
        movie.setImage(result.getString("image"));
        movie.setCoverImage(result.getString("cover_image"));

        return movie;
    }

    //TODO
    public ArrayList<Movie> search(String name, String genre, String releaseDate, int actorId, String sortBy) {
        try {
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

            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();

            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                Movie movie = this.fillObjectFromResult(result);
                movies.add(movie);
            }

            result.close();
            statement.close();
            connection.close();

            return movies;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
