package repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public abstract class Repository<T> {
    ComboPooledDataSource dataSource;

    protected Repository() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        dataSource = new ComboPooledDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/iemdb");
        dataSource.setUser("root");
        dataSource.setPassword("mahshid123");

        dataSource.setInitialPoolSize(5);
        dataSource.setMinPoolSize(5);
        dataSource.setAcquireIncrement(5);
        dataSource.setMaxPoolSize(20);
        dataSource.setMaxStatements(100);

        this.createTable();
    }

    private void createTable() throws SQLException {
        Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(this.getCreateTableQuery());
        statement.executeUpdate();
        statement.close();
        connection.close();
    }

    public void loadFromURL(URL url) throws SQLException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<T> objects = objectMapper.readValue(url, new TypeReference<ArrayList<T>>() {});
        this.addBulk(objects);
    }

    public T getById(int objectId) {
        T object = null;
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT ? FROM ? WHERE id=?");
            statement.setString(1, getColumns());
            statement.setString(2, this.getTableName());
            statement.setInt(3, objectId);
            ResultSet result = statement.executeQuery();
            result.next();

            object = this.fillObjectFromResult(object, result);

            result.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return object;
    }

    public void add(T object) {
        try {
            Connection connection = dataSource.getConnection();

            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO ? (?) VALUES (?)"
            );
            statement.setString(1, this.getTableName());
            statement.setString(2, this.getColumns());
            statement.setString(3, this.getInsertQueryValues(object));
            statement.executeQuery();

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addBulk(ArrayList<T> objects) throws SQLException {
        try {
            Connection connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO ? (?) VALUES (?)"
            );
            statement.setString(1, this.getTableName());
            statement.setString(2, this.getColumns());

            for (T object : objects) {
                statement.setString(3, this.getInsertQueryValues(object));
                statement.executeQuery();
            }
            connection.commit();

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public abstract String getCreateTableQuery();
    public abstract String getTableName();
    public abstract String getInsertQueryValues(T object);
    public abstract String getColumns();
    public abstract T fillObjectFromResult(T object, ResultSet result) throws SQLException;

}
