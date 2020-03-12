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

public class UserRepository implements IUserRepository {
    private static final Logger logger = LogManager.getLogger(UserRepository.class);
    private Connection connection;

    @Override
    public User login(User user) {
        logger.traceEntry();
        connection = new JDBCUtils().getConnection();
        logger.trace("Connection established");
        String sqlString = "select Username from users where Username = Any (select Username from users where Username=? and Password=?)";
        try {
            connection.setAutoCommit(false);
            logger.trace("Trying to find user="+user.getUsername()+" passw="+user.getPassword());
            PreparedStatement preparedStatement = connection.prepareStatement(sqlString);
            preparedStatement.setString(1,user.getUsername());
            preparedStatement.setString(2,user.getPassword());
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
            if (check)
            {
                return user;
            }
            else
            {
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
    public void add(User user) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public void update(User user) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public void delete(Integer integer) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public User findOne(Integer integer) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public Iterable<User> findAll() {
        throw new UnsupportedOperationException("Not yet implemented.");
    }
}
