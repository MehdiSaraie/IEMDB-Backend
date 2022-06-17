package repository;

import entities.Genre;
import entities.Movie;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class GenreRepository extends Repository<Genre>{
    private String TABLE_NAME = "genres";
    private static GenreRepository instance;

    protected GenreRepository() throws SQLException {
    }

    public static GenreRepository getInstance() {
        try {
            if (instance == null)
                instance = new GenreRepository();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in creating table");
        }
        return instance;
    }

    public void load(ArrayList<Movie> movies) {
        for (Movie movie : movies) {
            for (String genre_title : movie.getGenres()) {
                Genre genre = new Genre(movie.getId(), genre_title);
                this.add(genre);
            }
        }
    }

    @Override
    public String getCreateTableQuery() {
        return "CREATE TABLE IF NOT EXISTS genres (" +
                "movie_id INT," +
                "genre VARCHAR(30)," +
                "PRIMARY KEY (movie_id, genre)," +
                "FOREIGN KEY (movie_id) REFERENCES movies(id)" +
                ")";
    }

    public String getTableName() {
        return this.TABLE_NAME;
    }

    @Override
    public PreparedStatement fillInsertQuery(PreparedStatement statement, Genre genre) throws SQLException {
        statement.setInt(1, genre.getMovieId());
        statement.setString(2, genre.getGenre());
        return statement;
    }

    @Override
    public ArrayList<String> getColumns() {
        return new ArrayList<String>(Arrays.asList("movie_id", "genre"));
    }

    @Override
    public Genre fillObjectFromResult(ResultSet result) throws SQLException {
        return null;
    }

    public ArrayList<Integer> getByGenre(String genre) {
        ArrayList<Integer> movieIds = new ArrayList<>();

        return movieIds;
    }
}
