package repository;


import entities.User;
import org.springframework.ejb.access.SimpleRemoteSlsbInvokerInterceptor;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;


public class UserRepository extends Repository<User> {
    private String TABLE_NAME = "users";
    private static UserRepository instance;

    protected UserRepository() throws SQLException {
    }

    public static UserRepository getInstance() {
        try {
            if (instance == null)
                instance = new UserRepository();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in creating table");
        }
        return instance;
    }


    @Override
    public String getCreateTableQuery() {
        return "CREATE TABLE IF NOT EXISTS users (" +
            "id INT NOT NULL AUTO_INCREMENT," +
            "email TEXT," +
            "password TEXT," +
            "nickname TEXT," +
            "name TEXT," +
            "birthDate DATE," +
            "PRIMARY KEY (id))";
    }

    public String getTableName() {
        return this.TABLE_NAME;
    }

    @Override
    public ArrayList<String> getColumns() {
        return new ArrayList<String>(Arrays.asList("email", "password", "nickname", "name", "birthDate"));
    }

    @Override
    public PreparedStatement fillInsertQuery(PreparedStatement statement, User user) throws SQLException {
        statement.setString(1, user.getEmail());
        statement.setString(2, user.getPassword());
        statement.setString(3, user.getNickname());
        statement.setString(4, user.getName());
        statement.setString(5, user.getBirthDate());
        return statement;
    }

    @Override
    public User fillObjectFromResult(ResultSet result) throws SQLException {
        User user = new User();
        user.setId(result.getInt("id"));
        user.setEmail(result.getString("email"));
        user.setPassword(result.getString("password"));
        user.setNickname(result.getString("nickname"));
        user.setName(result.getString("name"));
        user.setBirthDate(result.getString("birthDate"));
        return user;
    }

    public User getByEmail(String email) {
        User user = null;
        try {
            Connection connection = dataSource.getConnection();

            String query = String.format("SELECT id,%s FROM %s WHERE email='%s'", String.join(",", this.getColumns()), this.getTableName(), email);
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);
            if (result.next()) {
                user = this.fillObjectFromResult(result);
            }
            result.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

}
