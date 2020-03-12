package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class AbstractServer {
    private int port;
    private ServerSocket serverSocket = null;
    private static final Logger logger = LogManager.getLogger(AbstractServer.class);

    public AbstractServer(int port)
    {
        this.port = port;
    }

    public void start() throws ServerException
    {
        try
        {
            logger.traceEntry("Started Server!");
            serverSocket = new ServerSocket(port);
            logger.trace("Created ServerSocket on port: " + port);
            while(true)
            {
                logger.trace("Waiting for clients...");
                Socket client = serverSocket.accept();
                logger.trace("Client connected! Processing its request...");
                processRequest(client);
            }

        } catch (IOException e) {
            throw new ServerException("Starting server error: ", e);
        } finally {
            try {
                serverSocket.close();

            } catch (IOException e) {
                throw new ServerException("Closing server error: " , e);
            }
        }
    }

    protected abstract void processRequest(Socket client);

    public void stop() throws ServerException
    {
        try{
            serverSocket.close();
        } catch (IOException e) {
            throw new ServerException("Closing server error: ", e);
        }
    }
}
