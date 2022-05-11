package repository;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
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
        Statement statement = connection.createStatement();
        statement.execute(this.getCreateTableQuery());
        statement.close();
        connection.close();
    }

    public void loadFromURL(URL url, Class<T> tClass) throws SQLException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, tClass);
        ArrayList<T> objects = objectMapper.readValue(url, listType);
        this.addBulk(objects);
    }

    public T getById(int objectId){
        T object = null;
        try {
            Connection connection = dataSource.getConnection();
            String sql = String.format("SELECT %s FROM %s WHERE id=?", String.join(",", this.getColumns()), this.getTableName());
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, objectId);
            ResultSet result = statement.executeQuery();
            result.next();

            object = this.fillObjectFromResult(result);
            result.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return object;
    }

    public void add(T object) {
        try {
            Connection connection = dataSource.getConnection();

            PreparedStatement statement = connection.prepareStatement(this.generateInsertQueryTemplate());
            statement = this.fillInsertQuery(statement, object);
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

            PreparedStatement statement = connection.prepareStatement(this.generateInsertQueryTemplate());
            for (T object : objects) {
                statement = connection.prepareStatement(this.generateInsertQueryTemplate());
                statement = this.fillInsertQuery(statement, object);
                System.out.println(statement.toString());
                statement.execute();
            }
            connection.commit();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String generateInsertQueryTemplate() {
        ArrayList<String> questionMarks = new ArrayList<>();
        for (int i = 0; i < this.getColumns().size(); i++) {
            questionMarks.add("?");
        }
        String joinedQuestionMarks = String.join(",", questionMarks);
        return String.format("INSERT INTO %s (%s) VALUES (%s)", this.getTableName(), String.join(",", this.getColumns()), joinedQuestionMarks);
    }

    public abstract String getCreateTableQuery();
    public abstract String getTableName();
    public abstract PreparedStatement fillInsertQuery(PreparedStatement p, T t) throws SQLException;
    public abstract ArrayList<String> getColumns();
    public abstract T fillObjectFromResult(ResultSet result) throws SQLException;

}
