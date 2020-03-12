package rpcProtocol;

import dto.*;
import javafx.application.Platform;
import model.Concert;
import model.Transactions;
import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.ApplicationException;
import services.IObserver;
import services.IServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class ServerRPCProxy implements IServer {
    private static final Logger logger = LogManager.getLogger(ServerRPCProxy.class);


    private String host;
    private int port;

    private IObserver client;

    private ObjectInputStream input;
    private ObjectOutputStream output;

    private Socket connection;
    private BlockingQueue<Response> responsesQueue;

    private volatile boolean finished;

    public ServerRPCProxy(String host, int port)
    {
        this.host = host;
        this.port = port;

        responsesQueue = new LinkedBlockingQueue<Response>();
    }




    @Override
    public void login(String username, String password, IObserver client) throws ApplicationException {
        initializeConnection();
        UserDTO userDTO = DTOUtils.getDTOFromUser(new User(username, password));
        Request request = new Request.Builder().type(RequestType.LOGIN).data(userDTO).build();
        sendRequest(request);
        Response response = readResponse();
        if (response.getType() == ResponseType.OK)
        {
            this.client = client;
            return;
        }
        if (response.getType() == ResponseType.ERROR)
        {
            String error = response.getData().toString();
            closeConnection();
            throw new ApplicationException(error);
        }
    }

    @Override
    public void logout(String username, String password, IObserver client) throws ApplicationException {
        UserDTO userDTO = DTOUtils.getDTOFromUser(new User(username,password));
        Request request = new Request.Builder().type(RequestType.LOGOUT).data(userDTO).build();
        sendRequest(request);
        Response response = readResponse();
        closeConnection();
        if (response.getType() == ResponseType.ERROR)
        {
            String error = response.getData().toString();
            throw new ApplicationException(error);
        }

    }

    @Override
    public List<Concert> getAllConcerts() throws ApplicationException {
        List<Concert> concertList = null;
        Request request = new Request.Builder().type(RequestType.GETALL).build();
        sendRequest(request);
        Response response = readResponse();
        if (response.getType() == ResponseType.GETALL)
        {
            concertList = DTOUtils.getListFromDTO((ConcertsListDTO) response.getData());
            return concertList;
        }
        if (response.getType() == ResponseType.ERROR)
        {
            String error = response.getData().toString();
            throw new ApplicationException(error);
        }
        return concertList;
    }

    @Override
    public List<Concert> getFilteredConcerts(LocalDate date) throws ApplicationException {
        List<Concert> concertFilteredList = null;
        DateDTO dateDTO = DTOUtils.getDTOfromDate(date);
        Request request = new Request.Builder().type(RequestType.GETSORTED).data(dateDTO).build();
        sendRequest(request);
        Response response = readResponse();

        if (response.getType() == ResponseType.GETSORTED)
        {
            concertFilteredList = DTOUtils.getListFromDTO((ConcertsListDTO) response.getData());
            return concertFilteredList;
        }
        if (response.getType() == ResponseType.ERROR)
        {
            String error = response.getData().toString();
            throw new ApplicationException(error);
        }

        return concertFilteredList;
    }

    @Override
    public void buyTickets(String username, String buyerName, int ticketsBought, int concertID) throws ApplicationException {
        TransactionDTO transactionDTO = DTOUtils.getDTOFromTransaction(new Transactions(concertID,ticketsBought,
                username,buyerName));
        Request request = new Request.Builder().type(RequestType.BUYTICKET).data(transactionDTO).build();
        sendRequest(request);
    }

    private void handleUpdate(Response response)
    {
        try
        {
            Platform.runLater(() -> {
                try{
                    client.update();
                } catch (ApplicationException ex)
                {
                    ex.printStackTrace();
                }
            });
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void closeConnection()
    {
        finished = true;
        try
        {
            input.close();
            output.close();
            connection.close();
            client = null;
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    private void sendRequest(Request request) throws ApplicationException
    {
        try
        {
            output.writeObject(request);
            output.flush();
        } catch (IOException ex)
        {
            throw new ApplicationException("Error sending object " + ex);
        }
    }

    private Response readResponse() throws ApplicationException
    {
        Response response = null;
        try
        {
            response = responsesQueue.take();
        } catch (InterruptedException ex)
        {
            ex.printStackTrace();
        }
        return response;
    }

    private void initializeConnection() throws ApplicationException
    {
        try
        {
            connection = new Socket(host, port);
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            finished = false;
            startReader();
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    private void startReader()
    {
        Thread threadWorker = new Thread(new ReaderThread());
        threadWorker.start();
    }

    private class ReaderThread implements Runnable
    {
        @Override
        public void run() {
            while(!finished)
            {
                try
                {
                    Object response = input.readObject();
                    logger.trace("Response received " + response);
                    if (((Response)response).getType()==ResponseType.UPDATE)
                    {
                        handleUpdate((Response)response);
                    } else
                    {
                        try
                        {
                            responsesQueue.put((Response)response);
                        } catch (InterruptedException ex)
                        {
                            ex.printStackTrace();
                        }
                    }
                } catch (IOException ex)
                {
                    logger.warn("Reading error " + ex);
                } catch (ClassNotFoundException ex)
                {
                    logger.warn("Reading error " + ex);
                }
            }
        }
    }
}
