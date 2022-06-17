package repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import entities.Actor;

public class ActorRepository extends Repository<Actor> {
    private String TABLE_NAME = "actors";
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

    public String getTableName() {
        return this.TABLE_NAME;
    }

    @Override
    public String getCreateTableQuery() {
        return "CREATE TABLE IF NOT EXISTS actors (" +
            "id INT," +
            "name VARCHAR(30)," +
            "birthDate TEXT," +
            "nationality VARCHAR(30)," +
            "image TEXT," +
            "PRIMARY KEY (id)" +
        ")";
    }

    @Override
    public ArrayList<String> getColumns() {
        return new ArrayList<String>(Arrays.asList("id", "name", "birthdate", "nationality", "image"));
    }

    @Override
    public PreparedStatement fillInsertQuery(PreparedStatement statement, Actor actor) throws SQLException {
        statement.setInt(1, actor.getId());
        statement.setString(2, actor.getName());
        statement.setString(3, actor.getBirthDate());
        statement.setString(4, actor.getNationality());
        statement.setString(5, actor.getImage());
        return statement;
    }

    @Override
    public Actor fillObjectFromResult(ResultSet result) throws SQLException {
        Actor actor = new Actor();
        actor.setId(result.getInt("id"));
        actor.setName(result.getString("name"));
        actor.setBirthDate(result.getString("birthDate"));
        actor.setNationality(result.getString("nationality"));
        actor.setImage(result.getString("image"));
        return actor;
    }
}
