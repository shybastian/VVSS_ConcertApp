package repository.validators;

import jdbc.JDBCUtils;
import model.Transactions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionRepoValidator {
    private Connection connection;
    private static final Logger logger = LogManager.getLogger(TransactionRepoValidator.class);

    public boolean canContinueTransaction(Transactions transaction)
    {
        logger.traceEntry();
        try {
            connection = new JDBCUtils().getConnection();
            logger.trace("Established Connection");
            connection.setAutoCommit(false);
            String sqlQuery = "select BileteTotale, BileteVandute from Concerts where idConcert=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1,transaction.getIdConcert());
            logger.trace("Proceeding to execute query for idConcert="+ transaction.getIdConcert());
            ResultSet resultSet = preparedStatement.executeQuery();
            logger.trace("Executed Query successfully");
            connection.commit();
            logger.trace("Connection commited");
            logger.trace("Entering resultSet if true");
            if (resultSet.next())
            {
                logger.trace("Entered resultSet");
                int totalTickets = resultSet.getInt(1);
                int soldTickets = resultSet.getInt(2);
                logger.trace("Got values: totalTickets="+totalTickets+" soldTickets="+soldTickets);
                connection.setAutoCommit(true);
                connection.close();
                logger.trace("Connection closed");
                int total = soldTickets + transaction.getBoughtTickets();
                logger.traceExit("Exiting with boolean variable if totalTickets="+totalTickets+">="+total);
                return totalTickets >= total;
            }
            logger.trace("resultSet empty");
            connection.close();
            logger.trace("Connection closed");
            logger.traceExit("Returning value FALSE // Can not continue transaction. Exceeding total tickets");
            return false;
        } catch (SQLException e) {
            logger.warn(e.toString());
            e.printStackTrace();
        }
        logger.traceExit("Returning value FALSE // Can not continue transaction // Either connection not established" +
                "or other faults");
        return false;
    }
}
