import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import server.ServerImpl;
import service.ConcertService;
import service.TransactionService;
import service.UserService;
import services.IServer;
import utils.AbstractServer;
import utils.RpcConcurrentServer;
import utils.ServerException;

public class StartRCPServer {
    private static int defaultPort = 55555;
    private static final Logger logger = LogManager.getLogger(StartRCPServer.class);

    public static void main(String[] args)
    {
        ApplicationContext concertContext =
                new AnnotationConfigApplicationContext(service.beanConfigs.ConcertServiceConfig.class);
        ApplicationContext transactionContext =
                new AnnotationConfigApplicationContext(service.beanConfigs.TransactionServiceConfig.class);
        ApplicationContext userContext =
                new AnnotationConfigApplicationContext(service.beanConfigs.UserServiceConfig.class);

        UserService userService = userContext.getBean(UserService.class);
        TransactionService transactionService = transactionContext.getBean(TransactionService.class);
        ConcertService concertService = concertContext.getBean(ConcertService.class);

        IServer server = new ServerImpl(concertService,transactionService,userService);

        int serverPort = defaultPort;
        logger.trace("Starting server on port: " + serverPort);
        AbstractServer abstractServer = new RpcConcurrentServer(serverPort,server);
        try
        {
            abstractServer.start();
        } catch (ServerException e)
        {
            logger.error("Error starting the server: " + e.getMessage());
        }
    }
}
