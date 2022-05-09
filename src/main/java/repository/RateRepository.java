package repository;

import entities.Rate;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RateRepository extends Repository<Rate>{
    @Override
    public String getCreateTableQuery() {
        return null;
    }

    @Override
    public String getTableName() {
        return null;
    }

    @Override
    public String getInsertQueryValues(Rate object) {
        return null;
    }

    @Override
    public String getColumns() {
        return null;
    }

    @Override
    public Rate fillObjectFromResult(Rate object, ResultSet result) throws SQLException {
        return null;
    }
}
