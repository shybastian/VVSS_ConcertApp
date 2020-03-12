package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rpcProtocol.ClientRPCWorker;
import services.IServer;

import java.net.Socket;

public class RpcConcurrentServer extends AbstractConcurrentServer {
    private IServer server;
    private static final Logger logger = LogManager.getLogger(RpcConcurrentServer.class);

    public RpcConcurrentServer(int port, IServer server)
    {
        super(port);
        this.server = server;
        logger.trace("RpcConcurrentServer!");

    }

    @Override
    protected Thread createWorker(Socket client) {
        ClientRPCWorker worker = new ClientRPCWorker(server, client);

        Thread threadWorker = new Thread(worker);
        return threadWorker;
    }
}
