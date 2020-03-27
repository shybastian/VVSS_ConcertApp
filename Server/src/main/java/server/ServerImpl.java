package server;

import model.Concert;
import model.Transactions;
import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.ConcertService;
import service.TransactionService;
import service.UserService;
import services.ApplicationException;
import services.IObserver;
import services.IServer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerImpl implements IServer {
    private static final Logger logger = LogManager.getLogger(ServerImpl.class);
    private final int defaultThreadsNumber = 5;

    private ConcertService concertService;
    private TransactionService transactionService;
    private UserService userService;

    private Map<String, IObserver> loggedClients;

    public ServerImpl(ConcertService concertService, TransactionService transactionService, UserService userService) {
        this.concertService = concertService;
        this.transactionService = transactionService;
        this.userService = userService;

        loggedClients = new ConcurrentHashMap<>();
    }

    @Override
    public void login(String username, String password, IObserver client) throws ApplicationException {
        User userToLogIn = new User(username, password);
        User searchedUser = userService.login(userToLogIn);
        if (searchedUser != null)
        {
            if (loggedClients.get(searchedUser.getUsername()) != null)
            {
                throw new ApplicationException("User already logged in!");
            }
            loggedClients.put(searchedUser.getUsername(),client);
        }
        else
        {
            throw new ApplicationException("Authentification failed!");
        }
    }

    @Override
    public void logout(String username, String password, IObserver client) throws ApplicationException {
        IObserver localClient = loggedClients.remove(username);
        if (localClient == null)
        {
            throw new ApplicationException("User: " + username + " is not logged in!");
        }
    }

    @Override
    public List<Concert> getAllConcerts() throws ApplicationException {
        List<Concert> concertList = new ArrayList<>();
        Iterable<Concert> concertIterable = concertService.findAll();
        concertIterable.forEach(concertList::add);

        if(concertList.isEmpty())
        {
            throw new ApplicationException("No Concerts found!");
        }
        return concertList;
    }

    @Override
    public List<Concert> getFilteredConcerts(LocalDate date) throws ApplicationException {
        List<Concert> concertFilteredList = new ArrayList<>();
        Iterable<Concert> concertIterable = concertService.filterConcerts(date);
        concertIterable.forEach(concertFilteredList::add);
        if(concertFilteredList.isEmpty())
        {
            throw new ApplicationException("No Concerts with given date: " + date + " have been found");
        }
        return concertFilteredList;
    }

    @Override
    public void buyTickets(String username, String buyerName, int ticketsBought, int concertID) throws ApplicationException {
        Transactions toCheck = new Transactions(concertID,ticketsBought,username,buyerName);
        boolean check = transactionService.validateTransaction(toCheck);
        if (check)
        {
            transactionService.addTransaction(toCheck);
            try {
                concertService.updateConcert(concertID,ticketsBought);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
        {
            throw new ApplicationException("Unable to buy tickets");
        }
        notifyUsers();
    }
    public void notifyUsers()
    {
        ExecutorService executor= Executors.newFixedThreadPool(defaultThreadsNumber);
        for(Map.Entry<String, IObserver> entry: loggedClients.entrySet()) {
            IObserver clients = entry.getValue();
            executor.execute(()-> {
                try {
                    clients.update();
                } catch (ApplicationException ex) {
                    logger.error("ERROR notifying Clients");
                }
                ;
            });
        }
        executor.shutdown();
    }
}
