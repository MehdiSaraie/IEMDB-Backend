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
            "id INT," +
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
        return new ArrayList<String>(Arrays.asList("id", "name", "summary", "releaseDate", "director", "imdbRate", "duration",
                "ageLimit", "image", "coverImage"));
    }

    @Override
    public Movie getById(int movieId){
        Movie movie = null;
        try {
            Connection connection = dataSource.getConnection();
            String sql = String.format("SELECT %s FROM %s WHERE id=?", String.join(",", this.getColumns()), this.getTableName());
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, movieId);
            ResultSet result = statement.executeQuery();
            System.out.println(statement);
            System.out.println(result);
            result.next();
            System.out.println(result);
            movie = this.fillObjectFromResult(result);
            System.out.println(movie);
            result.close();
            statement.close();

            joinWriters(connection, movie);
            joinGenres(connection, movie);
            joinCast(connection, movie);

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return movie;
    }

    public void joinWriters(Connection connection, Movie movie) {
        try {
            String sql = String.format("SELECT writer_name FROM %s INNER JOIN writers ON id=movie_id WHERE id=?", this.getTableName());
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, movie.getId());
            ResultSet result = statement.executeQuery();
            ArrayList<String> writers = new ArrayList<>();
            while (result.next()) {
                String writerName = result.getString("writer_name");
                writers.add(writerName);
            }
            movie.setWriters(writers);
            System.out.println(movie.getWriters());

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void joinGenres(Connection connection, Movie movie) {
        try {
            String sql = String.format("SELECT genre FROM %s INNER JOIN genres ON id=movie_id WHERE id=?", this.getTableName());
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, movie.getId());
            ResultSet result = statement.executeQuery();
            ArrayList<String> genres = new ArrayList<>();
            while (result.next()) {
                String genre = result.getString("genre");
                genres.add(genre);
            }
            movie.setGenres(genres);
            System.out.println(movie.getGenres());

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void joinCast(Connection connection, Movie movie) {
        try {
            String sql = String.format("SELECT actor_id FROM %s INNER JOIN cast ON id=movie_id WHERE id=?", this.getTableName());
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, movie.getId());
            ResultSet result = statement.executeQuery();
            ArrayList<Integer> cast = new ArrayList<>();
            while (result.next()) {
                int actorId = result.getInt("actor_id");
                cast.add(actorId);
            }
            movie.setCast(cast);
            System.out.println(movie.getCast());

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public PreparedStatement fillInsertQuery(PreparedStatement statement, Movie movie) throws SQLException {
        statement.setInt(1, movie.getId());
        statement.setString(2, movie.getName());
        statement.setString(3, movie.getSummary());
        statement.setString(4, movie.getReleaseDate());
        statement.setString(5, movie.getDirector());
        statement.setFloat(6, movie.getImdbRate());
        statement.setInt(7, movie.getDuration());
        statement.setInt(8, movie.getAgeLimit());
        statement.setString(9, movie.getImage());
        statement.setString(10, movie.getCoverImage());
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
            String query = String.format("SELECT %s FROM %s WHERE ", String.join(",", this.getColumns()), this.getTableName());

            ArrayList<String> whereClauses = new ArrayList<>();
            if (name != null && name != "") {
                String where = String.format("name LIKE '%%%s%%'", name);
                whereClauses.add(where);
            }
            if (releaseDate != null && releaseDate != "") {
                String where = String.format("releaseDate LIKE '%%%s%%'", releaseDate);
                whereClauses.add(where);
            }

            ArrayList<Integer> movieIds = new ArrayList<>();
            if (genre != null && genre != "") {
                movieIds.addAll(GenreRepository.getInstance().getMoviesByGenre(genre));
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
