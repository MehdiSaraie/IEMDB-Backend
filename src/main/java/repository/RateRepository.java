package repository;

import entities.Rate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class RateRepository extends Repository<Rate>{
    private String TABLE_NAME = "rates";
    private static RateRepository instance;

    protected RateRepository() throws SQLException {
    }

    public static RateRepository getInstance() {
        try {
            if (instance == null)
                instance = new RateRepository();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in creating table");
        }
        return instance;
    }

    @Override
    public String getCreateTableQuery() {
        return "CREATE TABLE IF NOT EXISTS rates (" +
            "movie_id INT," +
            "user_email VARCHAR(50)," +
            "value INT," +
            "PRIMARY KEY (user_email, movie_id)," +
            "FOREIGN KEY (movie_id) REFERENCES movies(id)," +
            "FOREIGN KEY (user_email) REFERENCES users(email)" +
        ")";
    }

    public String getTableName() {
        return this.TABLE_NAME;
    }

    @Override
    public ArrayList<String> getColumns() {
        return new ArrayList<>(Arrays.asList("user_email", "movie_id", "value"));
    }

    @Override
    public PreparedStatement fillInsertQuery(PreparedStatement statement, Rate rate) throws SQLException {
        statement.setString(1, rate.getUserEmail());
        statement.setInt(2, rate.getMovieId());
        statement.setInt(3, rate.getRateValue());
        return statement;
    }

    @Override
    public Rate fillObjectFromResult(ResultSet result) throws SQLException {
        Rate rate = new Rate();
        rate.setId(result.getInt("id"));
        rate.setMovieId(result.getInt("movie_id"));
        rate.setUserEmail(result.getString("user_email"));
        rate.setRateValue(result.getInt("value"));
        return rate;
    }
}
