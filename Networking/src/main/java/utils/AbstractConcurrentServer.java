package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.Socket;

public abstract class AbstractConcurrentServer extends AbstractServer {
    private static final Logger logger = LogManager.getLogger(AbstractConcurrentServer.class);

    public AbstractConcurrentServer(int port)
    {
        super(port);
        logger.trace("Abstract Concurrent Server!");
    }


    @Override
    protected void processRequest(Socket client) {
        logger.traceEntry("Proceeding to create Worker.");
        Thread threadWorker = createWorker(client);
        logger.trace("Created Worker. Starting thread.");
        threadWorker.start();
    }

    protected abstract Thread createWorker(Socket client);
}
