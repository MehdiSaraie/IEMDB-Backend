package repository;


import entities.User;
import org.springframework.ejb.access.SimpleRemoteSlsbInvokerInterceptor;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class UserRepository extends Repository<User> {
    private static final String TABLE_NAME = "users";
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
    public String getTableName() {
        return TABLE_NAME;
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

    @Override
    public ArrayList<String> getColumns() {
        return new ArrayList<>(Arrays.asList("email", "password", "nickname", "name", "birthDate"));
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
        User user  = new User();
        try {
            Connection connection = dataSource.getConnection();

            String sql = String.format("SELECT (id,%s) FROM %s WHERE email = ?", String.join(",", this.getColumns()), email);
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery();

            user = this.fillObjectFromResult(result);

            result.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

}
