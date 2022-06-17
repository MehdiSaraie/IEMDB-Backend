package repository;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;

public abstract class Repository<T> {
    ComboPooledDataSource dataSource;
    private String TABLE_NAME;

    protected Repository() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        dataSource = new ComboPooledDataSource();

//        dataSource.setJdbcUrl(String.format("jdbc:mysql://localhost:%s/%s",
//                System.getenv("MYSQL_PORT"), System.getenv("MYSQL_DATABASE")));
//        dataSource.setUser(System.getenv("MYSQL_USER"));
//        dataSource.setPassword(System.getenv("MYSQL_PASSWORD"));

        try {
            dataSource.setDriverClass("com.mysql.cj.jdbc.Driver");
            dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/iemdb");
            dataSource.setUser("root");
            dataSource.setPassword("mahshid123");

            dataSource.setInitialPoolSize(5);
            dataSource.setMinPoolSize(5);
            dataSource.setAcquireIncrement(5);
            dataSource.setMaxPoolSize(20);
            dataSource.setMaxStatements(100);
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }

        this.createTable();
    }

    private void createTable() throws SQLException {
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        statement.execute(this.getCreateTableQuery());
        statement.close();
        connection.close();
    }

    public ArrayList<T> loadFromURL(URL url, Class<T> tClass) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, tClass);
        ArrayList<T> objects = objectMapper.readValue(url, listType);
        this.addBulk(objects);
        return objects;
    }

    public T getById(int objectId){
        T object = null;
        try {
            Connection connection = dataSource.getConnection();
            String sql = String.format("SELECT id,%s FROM %s WHERE id=?", String.join(",", this.getColumns()), this.getTableName());
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, objectId);
            ResultSet result = statement.executeQuery();
            System.out.println(statement);
            System.out.println(result);
            result.next();
            System.out.println(result);
            object = this.fillObjectFromResult(result);
            System.out.println(object);
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
            System.out.println(statement.toString());
            statement.execute();

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addBulk(ArrayList<T> objects) {
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
        return String.format("INSERT IGNORE INTO %s (%s) VALUES (%s)", this.getTableName(), String.join(",", this.getColumns()), joinedQuestionMarks);
    }

    public abstract String getCreateTableQuery();

    public abstract String getTableName();
    public abstract PreparedStatement fillInsertQuery(PreparedStatement p, T t) throws SQLException;
    public abstract ArrayList<String> getColumns();
    public abstract T fillObjectFromResult(ResultSet result) throws SQLException;

}
