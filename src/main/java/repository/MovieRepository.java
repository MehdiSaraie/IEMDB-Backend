package repository;

import entities.Movie;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;


public class MovieRepository extends Repository<Movie> {

    private String TABLE_NAME = "movies";
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

    public String getTableName() {
        return this.TABLE_NAME;
    }

    @Override
    public ArrayList<String> getColumns() {
        return new ArrayList<String>(Arrays.asList("name", "summary", "releaseDate", "director", "imdbRate", "duration",
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
        movie.setCoverImage(result.getString("coverImage"));

        return movie;
    }

    public ArrayList<Movie> search(String name, String genre, String releaseDate, Integer actorId, String sortBy) {
        try {
            String query = String.format("SELECT id, %s FROM %s WHERE ", String.join(",", this.getColumns()), this.getTableName());

            ArrayList<String> whereClauses = new ArrayList<>();
            if (name != null) {
                String where = String.format("name LIKE '%%%s%%'", name);
                whereClauses.add(where);
            }
            if (releaseDate != null) {
                String where = String.format("releaseDate = '%s'", releaseDate);
                whereClauses.add(where);
            }

            ArrayList<Integer> movieIds = new ArrayList<>();
            if (genre != null) {
                movieIds.addAll(GenreRepository.getInstance().getByGenre(genre));
            }
            if (actorId != null) {
                movieIds.addAll(CastRepository.getInstance().getActorMovieIds(actorId));
            }
            if (movieIds.size() > 0) {
                ArrayList<String> ids = new ArrayList<>();
                for (int i=0; i<movieIds.size(); i++) {
                    ids.add(String.valueOf(movieIds.get(i)));
                }
                String movieId = String.format("id in (%s)", String.join(",", ids));
                whereClauses.add(movieId);
            }

            if (whereClauses.size() >= 1) {
                query += String.join(" AND ", whereClauses);
            } else {
                query += " 1 ";
            }

            if (sortBy != null && (sortBy.equals("imdbRate") || sortBy.equals("releaseDate"))) {
                query += String.format(" ORDER BY %s DESC", sortBy);
            }

            query += " LIMIT 20 ";
            ArrayList<Movie> movies = new ArrayList<>();
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            System.out.println(query);
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
