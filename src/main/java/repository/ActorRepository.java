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
                "id SMALLINT," +
                "name VARCHAR," +
                "birthDate VARCHAR," +
                "nationality VARCHAR," +
                "image BLOB," +
                "PRIMARY KEY (id)" +
                ")";
    }

    @Override
    public String getInsertQueryValues(Actor actor) {
        return
            "'" + actor.getName() + "'," +
            "'" + actor.getBirthDate() + "'," +
            "'" + actor.getNationality() + "'," +
            "'" + actor.getImage() + "'";
    }

    @Override
    public String getColumns() {
        return "id, name, birthdate, nationality, image";
    }

    @Override
    public Actor fillObjectFromResult(Actor actor, ResultSet result) throws SQLException {
        actor.setId(result.getInt("id"));
        actor.setName(result.getString("name"));
        actor.setBirthDate(result.getString("birthDate"));
        actor.setNationality(result.getString("nationality"));
        actor.setImage(result.getString("image"));
        return actor;
    }
}
