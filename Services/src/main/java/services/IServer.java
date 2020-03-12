package services;

import model.Concert;

import java.time.LocalDate;
import java.util.List;

public interface IServer {
    void login(String username, String password, IObserver client) throws ApplicationException;
    void logout(String username, String password, IObserver client) throws ApplicationException;
    List<Concert> getAllConcerts() throws ApplicationException;
    List<Concert> getFilteredConcerts(LocalDate date) throws ApplicationException;
    void buyTickets(String username, String buyerName, int ticketsBought, int concertID)throws ApplicationException;
}
