package repository.repositories;

import jdbc.JDBCUtils;
import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import repository.interfaces.IUserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRepository implements IUserRepository {
    private static final Logger logger = LogManager.getLogger(UserRepository.class);
    private Connection connection;
    private static final String SELECT_USERNAME_FOR_LOGIN = "select Username from users where Username = Any (select Username from users where Username=? and Password=?)";
    private static final String GET_ALL_USERS = "SELECT * FROM users";
    private static final String INSERT_USER = "INSERT INTO users VALUES (?, ?)";
    private static final String FIND_USER = "SELECT * FROM users where username=?";



    @Override
    public User login(User user) {
        logger.traceEntry();
        connection = new JDBCUtils().getConnection();
        logger.trace("Connection established");
        try {
            connection.setAutoCommit(false);
            logger.trace("Trying to find user=" + user.getUsername() + " passw=" + user.getPassword());
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USERNAME_FOR_LOGIN);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            ResultSet resultSet = preparedStatement.executeQuery();
            logger.trace("Executed Query");
            connection.commit();
            logger.trace("Connection commited");
            connection.setAutoCommit(true);
            boolean check = resultSet.next();
            logger.trace(check + "was returned");
            connection.close();
            logger.trace("Connection closed");
            logger.traceExit();
            if (check) {
                return user;
            } else {
                return null;
            }
            //return check;
        } catch (SQLException e) {
            logger.warn(e.toString());
            e.printStackTrace();
        }
        logger.traceExit("Returned value FALSE // Either User wasn't found or doesn't exist");
        return null;
    }

    @Override
    public void add(User user) throws Exception {
        if(isValidUser(user)) {
            try {
                connection = new JDBCUtils().getConnection();
                logger.trace("Established connection");
                connection.setAutoCommit(false);
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER);
                preparedStatement.setString(1, user.getUsername());
                preparedStatement.setString(2, user.getPassword());
                preparedStatement.executeUpdate();
                connection.commit();
                connection.setAutoCommit(true);
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void update(User user) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public void delete(String integer) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public User findOne(String username) throws Exception{
        if (username.isEmpty()) {
            throw new Exception("The username is empty!");
        }
        try {
            connection = new JDBCUtils().getConnection();
            logger.trace("Established connection");
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_USER);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next())
            {
                return extractUserFromResultSet(resultSet);
            }
            connection.commit();
            connection.setAutoCommit(true);
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> findAll() {
        logger.traceEntry();
        try {
            List<User> users = new ArrayList<>();
            connection = new JDBCUtils().getConnection();
            logger.trace("Established connection");
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_USERS);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = extractUserFromResultSet(resultSet);
                users.add(user);
            }
            connection.commit();
            connection.setAutoCommit(true);
            connection.close();
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private User extractUserFromResultSet(ResultSet resultSet) throws SQLException {
        String username = resultSet.getString(1);
        String password = resultSet.getString(2);

        return new User(username, password);
    }

    private List<String> getExistingUsernames(List<User> users) {
        List<String> usernames = new ArrayList<>();
        for (User user : users) {
            usernames.add(user.getUsername());
        }
        return usernames;
    }

    private Boolean isValidUser(User user) throws Exception{
        List<User> users = findAll();
        List<String> usernames = getExistingUsernames(users);

        if(usernames.contains(user.getUsername())) {
            throw new Exception("This username already exists!");
        }

        if (user.getUsername().isEmpty()) {
            throw new Exception("Username cannot be empty!");
        }

        if (user.getPassword().isEmpty()) {
            throw new Exception("Password cannot be empty");
        }

        if (user.getPassword().length() < 4) {
            throw new Exception("Password must have at least 4 characters!");
        }

        if (!user.getPassword().matches("[a-zA-Z]*[0-9]+[a-zA-Z]*")) {
            throw new Exception("Password should contain at least one digit and only alphanumeric characters!");
        }
        return true;
    }
}
