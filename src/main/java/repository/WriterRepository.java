package repository;

import entities.Movie;
import entities.Writer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class WriterRepository extends Repository<Writer> {

    private String TABLE_NAME = "writers";
    private static WriterRepository instance;

    protected WriterRepository() throws SQLException {
    }

    public static WriterRepository getInstance() {
        try {
            if (instance == null)
                instance = new WriterRepository();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in creating table");
        }
        return instance;
    }

    public void load(ArrayList<Movie> movies) {
        for (Movie movie : movies) {
            for (String writer_name : movie.getWriters()) {
                Writer writer = new Writer(movie.getId(), writer_name);
                this.add(writer);
            }
        }
    }

    @Override
    public String getCreateTableQuery() {
        return "CREATE TABLE IF NOT EXISTS writers (" +
                "movie_id INT," +
                "writer_name VARCHAR(50)," +
                "PRIMARY KEY (movie_id, writer_name)," +
                "FOREIGN KEY (movie_id) REFERENCES movies(id)" +
                ")";
    }

    public String getTableName() {
        return this.TABLE_NAME;
    }

    @Override
    public PreparedStatement fillInsertQuery(PreparedStatement statement, Writer writer) throws SQLException {
        statement.setInt(1, writer.getMovieId());
        statement.setString(2, writer.getWriterName());
        return statement;
    }

    @Override
    public ArrayList<String> getColumns() {
        return new ArrayList<String>(Arrays.asList("movie_id", "writer_name"));
    }

    @Override
    public Writer fillObjectFromResult(ResultSet result) throws SQLException {
        return null;
    }
}
