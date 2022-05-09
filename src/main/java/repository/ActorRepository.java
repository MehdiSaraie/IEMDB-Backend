package repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import entities.Actor;

public class ActorRepository extends Repository<Actor> {
    private static final String TABLE_NAME = "actors";
    private static ActorRepository instance;

    protected ActorRepository() throws SQLException {
    }

    public static ActorRepository getInstance() {
        try {
            if (instance == null)
                instance = new ActorRepository();
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
                "" +
                "" +
                "" +
                ")";
    }

    @Override
    public String getInsertQueryValues(Actor actor) {
        return "'" + actor.getName() + "'," +
            "'" + actor.getBirthDate() + "'," +
            "'" + + "'," +
            "'" + + "'," +
            "'" + + "'," +
    }

    @Override
    public String getColumns() {
        return "";
    }

    @Override
    public Actor fillObjectFromResult(Actor actor, ResultSet result) throws SQLException {
        actor.setId(result.getInt("id"));
        return actor;
    }
}
