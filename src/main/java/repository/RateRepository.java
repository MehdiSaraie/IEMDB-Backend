package repository;

import entities.Rate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class RateRepository extends Repository<Rate>{
    private static final String TABLE_NAME = "rate";
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
        return "CREATE TABLE IF NOT EXISTS rate (" +
            "id INT NOT NULL AUTO_INCREMENT," +
            "user_id INT," +
            "movie_id INT," +
            "value INT," +
            "PRIMARY KEY (id))";
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public ArrayList<String> getColumns() {
        return new ArrayList<>(Arrays.asList("user_id", "movie_id", "value"));
    }

    @Override
    public PreparedStatement fillInsertQuery(PreparedStatement statement, Rate rate) throws SQLException {
        statement.setInt(1, rate.getUserId());
        statement.setInt(2, rate.getMovieId());
        statement.setInt(3, rate.getRateValue());
        return statement;
    }

    @Override
    public Rate fillObjectFromResult(ResultSet result) throws SQLException {
        Rate rate = new Rate();
        rate.setId(result.getInt("id"));
        rate.setMovieId(result.getInt("movie_id"));
        rate.setUserId(result.getInt("user_id"));
        rate.setRateValue(result.getInt("value"));
        return rate;
    }
}
