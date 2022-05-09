package repository;


import entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    public String getTableName() {
        return TABLE_NAME;
    }

    public String getCreateTableQuery() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                "" +
                "" +
                "" +
                ")";
    }

    public String getInsertQueryValues(User user) {
        return "'" + "" + "'," +
                "'" + "" + "'," +
                "'" + "" + "'," +
                "'" + "" + "'," +
                "'" + "";
    }

    public String getColumns() {
        return "";
    }

    public User fillObjectFromResult(User user, ResultSet result) throws SQLException {
        user.setId(result.getInt("id"));
        return user;
    }

    public User getByEmail(String email) {
        User user  = new User();
        try {
            Connection connection = dataSource.getConnection();

            PreparedStatement statement = connection.prepareStatement(
                    "SELECT ? FROM ? WHERE email = ?"
            );
            statement.setString(1, this.getColumns());
            statement.setString(2, this.getTableName());
            statement.setString(3, email);
            ResultSet result = statement.executeQuery();

            user = this.fillObjectFromResult(user, result);

            result.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

}
