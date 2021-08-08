package com.pablojvm.infrastructure;

import com.pablojvm.application.implDatabase;
import com.pablojvm.user.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MysqlDB implements implDatabase {
    private static final Logger LOGGER =
            Logger.getLogger(MysqlDB.class.getName());

    private final String URL =
            "jdbc:mysql://localhost:3306/books";
    private final String USER = "root";
    private final String PASS = "my-secret-pw";

    @Override
    public int saveUser(User user) {
        String query =
                "INSERT INTO user(user_name, user_lastname, user_email, " +
                        "user_password)" +
                        "VALUE (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement userStatement = connection.prepareStatement(
                     query,
                     Statement.RETURN_GENERATED_KEYS
             )) {

            userStatement.setString(1, user.getName());
            userStatement.setString(2, user.getLastname());
            userStatement.setString(3, user.getEmail());
            userStatement.setString(4, user.getPassword());

            userStatement.executeUpdate();
            int idUserCreated = 0;
            try (ResultSet resultSet = userStatement.getGeneratedKeys()) {
                while (resultSet.next()) {
                    idUserCreated = resultSet.getInt(1);
                }
            }

            return idUserCreated;

        } catch (SQLException throwables) {
            LOGGER.log(
                    Level.WARNING,
                    "The following error occurred in the database: " +
                            throwables.getMessage()
            );
        }
        return 0;
    }

    @Override
    public User getUser(String email) {
        // TODO: 8/8/21 get the user from mysql
        String query =
                "SELECT user_id, user_name, user_lastname, user_email, user_password " +
                        "FROM user WHERE user_email=?";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
