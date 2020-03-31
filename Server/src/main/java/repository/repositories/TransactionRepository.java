package repository.repositories;

import jdbc.JDBCUtils;
import model.Transactions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import repository.interfaces.ITransactionRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionRepository implements ITransactionRepository {
    private Connection connection;
    private static final Logger logger = LogManager.getLogger(TransactionRepository.class);
    private final String insertTransaction = "insert into transactions(UsernameVanzator,Cumparator,idConcert,TicheteCumparate) " +
            "values(?,?,?,?)";
    @Override
    public Transactions findOne(int idTransaction) {
        logger.traceEntry();
        try{
            logger.trace("Trying to establish connection");
            connection = new JDBCUtils().getConnection();
            logger.trace("Established connection");
            connection.setAutoCommit(false);
            String sqlQuery = "select * from transactions where idTranzactie=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1,idTransaction);
            logger.trace("Trying to execute add");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
            {
                String sellerUsername = resultSet.getString(2);
                String buyerName = resultSet.getString(3);
                int idConcert = resultSet.getInt(4);
                int boughtTickets = resultSet.getInt(5);
                Transactions transaction = new Transactions(idConcert,boughtTickets,sellerUsername,buyerName);
                return  transaction;
            }
            else
            {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void add(Transactions transaction) throws Exception {
        if (transaction.getBoughtTickets() < 0) throw new Exception("Bought Tickets is invalid");
        if (transaction.getBuyerName().equals("") || transaction.getBuyerName().length() > 50) throw new Exception("Buyer Name invalid");
        if (transaction.getSellerUsername().equals("") || transaction.getSellerUsername().length() > 50) throw new Exception ("Seller Name invalid");

        logger.traceEntry();
        try{
            logger.traceEntry("Trying to establish connection");
            connection = new JDBCUtils().getConnection();
            logger.traceEntry("Established Connection");
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = prepareInsertStatement(transaction);
            logger.traceEntry("Trying to execute add");
            preparedStatement.executeUpdate();
            logger.traceEntry("Updated successfully");
            logger.traceEntry("Trying to commit the transaction");
            connection.commit();
            logger.traceEntry("Transaction commited. Freed locks on DB");
            connection.setAutoCommit(true);
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private PreparedStatement prepareInsertStatement(Transactions transaction) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(insertTransaction);
        preparedStatement.setString(1, transaction.getSellerUsername());
        preparedStatement.setString(2, transaction.getBuyerName());
        preparedStatement.setInt(3, transaction.getIdConcert());
        preparedStatement.setInt(4, transaction.getBoughtTickets());
        return preparedStatement;
    }

    @Override
    public void update(Transactions transactions) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public void delete(Integer integer) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public Transactions findOne(Integer integer) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public Iterable<Transactions> findAll() {
        throw new UnsupportedOperationException("Not yet implemented.");
    }
}
