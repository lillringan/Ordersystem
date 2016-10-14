package se.root.ordersystem.helpers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static se.root.ordersystem.helpers.DBInfo.*;


/**
 * The Class SQL.
 * @author  Root Group
 * @version 1.0
 */
public final class SQL {


    private final String url;
    
    private final List<Object> parameters;
    
    private String query;

    /**
     * Instantiates a new sql.
     *
     * @param url the url
     */
    public SQL(String url) {
        this.url = url;
        this.parameters = new ArrayList<>();
    }

    /**
     * Query.
     *
     * @param query the query
     * @return the sql
     */
    public SQL query(String query) {
        this.query = query;
        return this;
    }

    /**
     * Parameter.
     *
     * @param parameter the parameter
     * @return the sql
     */
    public SQL parameter(Object parameter) {
        parameters.add(parameter);
        return this;
    }

    /**
     * Many- to get result from database in list.
     *
     * @param <T> the generic type
     * @param mapper the mapper
     * @return the list
     * @throws SQLException the SQL exception
     */
    public <T> List<T> many(ResultMapper<T> mapper) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, databaseUsername, databasePassword)) {
            try (PreparedStatement statement = prepareStatement(connection);
                 ResultSet resultSet = statement.executeQuery()) {
                List<T> result = new ArrayList<>();
                while (resultSet.next()) {
                    result.add(mapper.map(resultSet));
                }
                return result;
            } catch (SQLException e) {
                throw new RuntimeException("Could not ready many from database, please checkout your query:" + query);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Something went wrong with the connection, please check your url: " + url);
        }
    }

    /**
     * Single.
     *
     * @param <T> the generic type
     * @param mapper the mapper
     * @return the t
     * @throws SQLException the SQL exception
     */
    public <T> T single(ResultMapper<T> mapper) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, databaseUsername, databasePassword)) {
            try (PreparedStatement statement = prepareStatement(connection);
                 ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapper.map(resultSet);
                } else {
                    throw new RuntimeException("No data found in the database with this query: " + query);
                }
            } catch (Exception e) {
                throw new SQLException("Couldn't get single with query :" + query);
            }
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong with the connection, please check your url: " + url);
        }
    }

    /**
     * Insert.
     *
     * @return the long
     * @throws SQLException the SQL exception
     */
    public long insert() throws SQLException {
        long key;
        try (Connection connection = DriverManager.getConnection(url, databaseUsername, databasePassword)) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = prepareStatement(connection)) {
                statement.executeUpdate();
                ResultSet keys = statement.getGeneratedKeys();
                if (keys.next()) {
                    key = keys.getLong(1);
                } else {
                    throw new RuntimeException("No data found in the databse with this query: " + query);
                }
                connection.commit();
                return key;
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException("Could not insert row into the database, please checkout your query:" + query);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Something went wrong with the connection, please check your url: " + url);
        }
    }

    public void update() throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, databaseUsername, databasePassword)) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = prepareStatement(connection)) {
                statement.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException("Could not update row in the database, please checkout your query:" + query);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Something went wrong with the connection, please check your url: " + url);
        }
    }

    /**
     * Prepare statement.
     *
     * @param connection the connection
     * @return the prepared statement
     * @throws SQLException the SQL exception
     */
    private PreparedStatement prepareStatement(Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        for (int i = 0; i < parameters.size(); i++) {
            statement.setObject(i + 1, parameters.get(i));
        }
        return statement;
    }
}