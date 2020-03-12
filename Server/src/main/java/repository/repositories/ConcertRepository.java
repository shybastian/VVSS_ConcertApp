package repository.repositories;

import jdbc.JDBCUtils;
import model.Concert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import repository.interfaces.IConcertRepository;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ConcertRepository implements IConcertRepository {
    private Connection connection;
    private static final Logger logger = LogManager.getLogger(ConcertRepository.class);


    @Override
    public Iterable<Concert> filterConcerts(LocalDate date) {
        logger.traceEntry();
        try {
            connection = new JDBCUtils().getConnection();
            logger.trace("Established Connection");
            connection.setAutoCommit(false);
            List<Concert> filteredConcerts = new ArrayList<>();
            String sqlQuery = "select idConcert, Artist, Locatie, Data, Ora, BileteTotale, BileteVandute from concerts where Data=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            System.out.println(date.toString());
            preparedStatement.setDate(1, Date.valueOf(date));
            ResultSet resultSet = preparedStatement.executeQuery();
            logger.trace("Executed Query for Date=" + date.toString());
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String artist = resultSet.getString(2);
                String location = resultSet.getString(3);
                LocalDate data = resultSet.getDate(4).toLocalDate();
                LocalTime time = resultSet.getTime(5).toLocalTime();
                int totalTickets = resultSet.getInt(6);
                int soldTickets = resultSet.getInt(7);

                Concert toAddConcert = new Concert(id, artist, totalTickets, soldTickets, location, data, time);
                logger.trace("Got Information=" + toAddConcert.toString());
                filteredConcerts.add(toAddConcert);
                logger.trace("Added information to List");
            }
            logger.trace("Added " + filteredConcerts.size() + " concerts to List");
            logger.traceExit();
            connection.commit();
            connection.setAutoCommit(true);
            connection.close();
            return filteredConcerts;
        } catch (SQLException e) {
            logger.warn(e.toString());
            e.printStackTrace();
        }
        logger.traceExit("Exited with NULL");
        return null;
    }

    @Override
    public void add(Concert concert) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public void update(Concert concert) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    //Overload
    public void update(int concertID, int ticketsToAdd) {
//        logger.traceEntry();
//        try {
//            connection = new JDBCUtils().getConnection();
//            logger.trace("Established connection");
//            connection.setAutoCommit(false);
//            String sqlUpdate = "update concerts set BileteVandute = ? where idConcert = ?";
//            PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdate);
//            preparedStatement.setInt(1, concert.getSoldTickets());
//            preparedStatement.setInt(2, concert.getId());
//            connection.commit();
//            preparedStatement.executeUpdate();
//            logger.trace("Executed Update: idConcert=" + concert.getId() + " changedValue=" + concert.getSoldTickets());
//            connection.commit();
//            logger.traceEntry("Committed transaction");
//            connection.setAutoCommit(true);
//            connection.close();
//            logger.trace("Closed connection");
//        } catch (SQLException e) {
//            logger.warn(e.toString());
//            e.printStackTrace();
//        }
//        logger.traceExit();

        logger.traceEntry();
        try
        {
            connection = new JDBCUtils().getConnection();
            logger.traceEntry("Established connection");
            connection.setAutoCommit(false);
            String sqlQuery = "select BileteVandute from concerts where idConcert=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1,concertID);
            ResultSet resultSet = preparedStatement.executeQuery();
            int soldTickets = 0;
            while (resultSet.next())
            {
                soldTickets = resultSet.getInt(1);
                logger.trace("Got value: " + soldTickets);
            }

            connection.commit();

            logger.trace("Preparing to update idConcert=" + concertID + "with currentTickets=" + soldTickets);
            String sqlUpdate = "update concerts set BileteVandute = ? where idConcert = ?";
            int updatedTickets = soldTickets + ticketsToAdd;
            logger.trace("Updating currentTickets to: " + updatedTickets + " ...");
            PreparedStatement preparedStatementUpdate = connection.prepareStatement(sqlUpdate);
            preparedStatementUpdate.setInt(1,updatedTickets);
            preparedStatementUpdate.setInt(2,concertID);
            preparedStatementUpdate.executeUpdate();
            logger.trace("Executed Update: idConcert=" + concertID + " changedValue=" + updatedTickets);
            connection.commit();
            logger.traceEntry("Committed transaction");
            connection.setAutoCommit(true);
            connection.close();
            logger.trace("Closed connection");

        } catch (SQLException ex)
        {
            logger.warn(ex.toString());
            ex.printStackTrace();
        }
        logger.traceExit();

    }

    @Override
    public void delete(Integer integer) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public Concert findOne(Integer integer) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public Iterable<Concert> findAll() {
        logger.traceEntry();
        try {
            List<Concert> allConcerts = new ArrayList<>();
            connection = new JDBCUtils().getConnection();
            logger.trace("Established connection");
            connection.setAutoCommit(false);
            String sqlQuery = "select * from concerts";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next())
            {
                int idConcert = resultSet.getInt(1);
                String artist = resultSet.getString(2);
                String location = resultSet.getString(3);
                LocalDate date = resultSet.getDate(4).toLocalDate();
                LocalTime time = resultSet.getTime(5).toLocalTime();
                int totalTickets = resultSet.getInt(6);
                int soldTickets = resultSet.getInt(7);

                Concert toAddConcert = new Concert(idConcert,artist,totalTickets,soldTickets,location,date,time);
                allConcerts.add(toAddConcert);
            }
            connection.commit();
            connection.setAutoCommit(true);
            connection.close();
            return allConcerts;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}