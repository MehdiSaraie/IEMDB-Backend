package repository;

import entities.Vote;

import java.sql.ResultSet;
import java.sql.SQLException;

public class VoteRepository extends Repository<Vote>{

    @Override
    public String getCreateTableQuery() {
        return null;
    }

    @Override
    public String getTableName() {
        return null;
    }

    @Override
    public String getInsertQueryValues(Vote object) {
        return null;
    }

    @Override
    public String getColumns() {
        return null;
    }

    @Override
    public Vote fillObjectFromResult(Vote object, ResultSet result) throws SQLException {
        return null;
    }


}
