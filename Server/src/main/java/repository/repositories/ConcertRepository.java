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
    private static final String selectConcertFiltered = "select idConcert, Artist, Locatie, Data, Ora, BileteTotale, BileteVandute from concerts where Data=?";
    private static final String updateConcert = "update concerts set BileteVandute = ? where idConcert = ?";
    private static final String selectSoldTickets = "select BileteVandute from concerts where idConcert=?";
    private static final String selectTotalTickets = "select BileteTotale from concerts where idConcert=?";
    private static final String selectAllConcerts = "select * from concerts";
    private static final String findOne = "select * from concerts where idConcert=?";
    private static final String insert = "insert into concerts(Artist, Locatie, Data, Ora, BileteTotale, BileteVandute) VALUES (?, ?, ?, ?, ?, ?)";

    @Override
    public List<Concert> filterConcerts(LocalDate date) {
        logger.traceEntry();
        try {
            connection = new JDBCUtils().getConnection();
            logger.trace("Established Connection");
            connection.setAutoCommit(false);
            List<Concert> filteredConcerts = new ArrayList<>();
            PreparedStatement preparedStatement = connection.prepareStatement(selectConcertFiltered);
            System.out.println(date.toString());
            preparedStatement.setDate(1, Date.valueOf(date));
            ResultSet resultSet = preparedStatement.executeQuery();
            logger.trace("Executed Query for Date=" + date.toString());
            while (resultSet.next()) {
                Concert toAddConcert = determineConcertFromResultSet(resultSet);
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
    public void add(Concert concert) throws Exception {

        if (concert.getArtist().equals("") || concert.getArtist().length() > 50) {
            throw new Exception("Artist name invalid");
        }

        if (concert.getTotalTickets() < 0 || concert.getSoldTickets() < 0) {
            throw new Exception("Invalid tickets");
        }

        if (concert.getDate().isBefore(LocalDate.of(2019, 1, 1)) || concert.getDate().isAfter(LocalDate.of(2026, 1, 1))) {
            throw new Exception("Invalid date");
        }

        try {
            connection = new JDBCUtils().getConnection();
            logger.trace("Established connection");
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(insert);
            preparedStatement.setString(1, concert.getArtist());
            preparedStatement.setString(2, concert.getLocation());
            preparedStatement.setDate(3, Date.valueOf(concert.getDate()));
            preparedStatement.setTime(4, Time.valueOf(concert.getTime()));
            preparedStatement.setInt(5, concert.getTotalTickets());
            preparedStatement.setInt(6, concert.getSoldTickets());

            preparedStatement.executeUpdate();
            connection.commit();
            connection.setAutoCommit(true);
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Concert concert) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    //Overload
    public void update(int concertID, int ticketsToAdd) throws Exception {
        logger.traceEntry();
        try
        {
            connection = new JDBCUtils().getConnection();
            logger.traceEntry("Established connection");
            connection.setAutoCommit(false);
            int soldTickets = determineCurrentNumberOfTickets(concertID, selectSoldTickets);
            int totalTickets = determineCurrentNumberOfTickets(concertID, selectTotalTickets);

            if (totalTickets - soldTickets - ticketsToAdd < 0  || ticketsToAdd < 0) {
                throw new Exception("Too few tickets left");
            }

            connection.commit();

            logger.trace("Preparing to update idConcert=" + concertID + "with currentTickets=" + soldTickets);
            updateNumberOfSoldTickets(concertID, ticketsToAdd, soldTickets);
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

    private void updateNumberOfSoldTickets(int concertID, int ticketsToAdd, int soldTickets) throws SQLException {
        int updatedTickets = soldTickets + ticketsToAdd;
        logger.trace("Updating currentTickets to: " + updatedTickets + " ...");
        PreparedStatement preparedStatementUpdate = connection.prepareStatement(updateConcert);
        preparedStatementUpdate.setInt(1,updatedTickets);
        preparedStatementUpdate.setInt(2,concertID);
        preparedStatementUpdate.executeUpdate();
        logger.trace("Executed Update: idConcert=" + concertID + " changedValue=" + updatedTickets);
    }

    private int determineCurrentNumberOfTickets(int concertID, String query) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1,concertID);
        ResultSet resultSet = preparedStatement.executeQuery();
        int soldTickets = 0;
        while (resultSet.next())
        {
            soldTickets = resultSet.getInt(1);
            logger.trace("Got value: " + soldTickets);
        }
        return soldTickets;
    }


    @Override
    public void delete(Integer integer) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public Concert findOne(Integer idConcert) throws Exception {
        if (idConcert < 0) {
            throw new Exception("Invalid ID");
        }
        try {
            connection = new JDBCUtils().getConnection();
            logger.trace("Established connection");
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(findOne);
            preparedStatement.setInt(1, idConcert);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next())
            {
                return determineConcertFromResultSet(resultSet);
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
    public List<Concert> findAll() {
        logger.traceEntry();
        try {
            List<Concert> allConcerts = new ArrayList<>();
            connection = new JDBCUtils().getConnection();
            logger.trace("Established connection");
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(selectAllConcerts);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next())
            {
                Concert toAddConcert = determineConcertFromResultSet(resultSet);
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

    private Concert determineConcertFromResultSet(ResultSet resultSet) throws SQLException {
        int idConcert = resultSet.getInt(1);
        String artist = resultSet.getString(2);
        String location = resultSet.getString(3);
        LocalDate date = resultSet.getDate(4).toLocalDate();
        LocalTime time = resultSet.getTime(5).toLocalTime();
        int totalTickets = resultSet.getInt(6);
        int soldTickets = resultSet.getInt(7);

        return new Concert(idConcert, artist, totalTickets, soldTickets, location, date, time);
    }
}
