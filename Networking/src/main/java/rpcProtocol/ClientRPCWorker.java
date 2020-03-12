package rpcProtocol;

import dto.*;
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

public class ClientRPCWorker implements Runnable, IObserver {
    private static final Logger logger = LogManager.getLogger(ClientRPCWorker.class);

    private IServer server;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    public ClientRPCWorker(IServer server, Socket connection)
    {
        this.server = server;
        this.connection = connection;

        try{
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            connected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (connected)
        {
            try{
                Object request = input.readObject();
                Response response = handleRequest((Request) request);
                if (response != null)
                {
                    sendResponse(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try{
                Thread.sleep(1000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try{
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Response okResponse = new Response.Builder().type(ResponseType.OK).build();

    private Response handleRequest(Request request)
    {
        Response response = null;
        if (request.type() == RequestType.LOGIN)
        {
            logger.trace("LOGIN request");
            UserDTO userDTO = (UserDTO) request.data();
            User user = DTOUtils.getUserFromDTO(userDTO);
            try
            {
                server.login(user.getUsername(), user.getPassword(), this);
                return okResponse;
            } catch (ApplicationException ex)
            {
                connected = false;
                return new Response.Builder().type(ResponseType.ERROR).data(ex.getMessage()).build();
            }
        }

        if (request.type() == RequestType.LOGOUT)
        {
            logger.trace("LOGOUT request");
            UserDTO userDTO = (UserDTO) request.data();
            User user = DTOUtils.getUserFromDTO(userDTO);
            try{
                server.logout(user.getUsername(), user.getPassword(), this);
                connected = false;
                return okResponse;
            } catch (ApplicationException ex)
            {
                return new Response.Builder().type(ResponseType.ERROR).data(ex.getMessage()).build();
            }
        }

        if (request.type() == RequestType.GETALL)
        {
            logger.trace("GETALL request");
            try
            {
                ConcertsListDTO listDTO = DTOUtils.getDTOFromList(server.getAllConcerts());
                return new Response.Builder().type(ResponseType.GETALL).data(listDTO).build();
            } catch (ApplicationException ex)
            {
                return new Response.Builder().type(ResponseType.ERROR).data(ex.getMessage()).build();
            }
        }

        if (request.type() == RequestType.GETSORTED)
        {
            logger.trace("GETALL request");
            LocalDate date = DTOUtils.getDatefromDTO((DateDTO)request.data());
            try
            {
                ConcertsListDTO concertsListDTO = DTOUtils.getDTOFromList(server.getFilteredConcerts(date));
                return new Response.Builder().type(ResponseType.GETSORTED).data(concertsListDTO).build();
            } catch (ApplicationException ex)
            {
                return new Response.Builder().type(ResponseType.ERROR).data(ex.getMessage()).build();
            }
        }

        if (request.type() == RequestType.BUYTICKET)
        {
            logger.trace("BUYTICKET request");
            Transactions transaction = DTOUtils.getTransactionsFromDTO((TransactionDTO) request.data());
            try
            {
                server.buyTickets(transaction.getSellerUsername(),transaction.getBuyerName(),
                        transaction.getBoughtTickets(),transaction.getIdConcert());
                return new Response.Builder().type(ResponseType.UPDATE).build();
            } catch (ApplicationException ex)
            {
                return new Response.Builder().type(ResponseType.ERROR).data(ex.getMessage()).build();
            }
        }

        return response;
    }

    private void sendResponse(Response response) throws IOException
    {
        logger.trace("Sending response: " + response);
        output.writeObject(response);
        output.flush();
    }

    @Override
    public void update() throws ApplicationException {
        Response response = new Response.Builder().type(ResponseType.UPDATE).build();
        logger.trace("Updating");
        try
        {
            sendResponse(response);
        } catch (IOException ex)
        {
            throw new ApplicationException("Sending error: " + ex);
        }
    }
}
