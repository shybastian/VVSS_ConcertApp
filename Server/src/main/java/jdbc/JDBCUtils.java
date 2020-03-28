package jdbc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCUtils {
    private Properties properties = new Properties();
    private static final Logger logger = LogManager.getLogger(JDBCUtils.class);
    private static final String propertiesFile = "C:\\Users\\paraus\\IdeaProjects\\VVSS_ConcertApp\\Server\\src\\main\\resources\\bd.config";

    private Connection instance = null;

    public Connection getNewConnection() {
        try {
            logger.traceEntry();
            properties.load(new FileInputStream(propertiesFile));
            logger.trace("Loaded DB Properties");
        } catch (IOException ex) {
            logger.warn(ex.getMessage());
            ex.printStackTrace();
        }

        String driver = properties.getProperty("jdbc.driver");
        logger.trace("DRIVER=" + driver);
        String url = properties.getProperty("jdbc.url");
        logger.trace("URL=" + url);
        String user = properties.getProperty("jdbc.user");
        logger.trace("USER=" + user);
        String password = properties.getProperty("jdbc.pass");
        logger.trace("PASSWORD=" + password);

        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, user, password);
            logger.trace("Created Connection");
        } catch (SQLException ex) {
            logger.warn(ex.toString());
            ex.printStackTrace();
        }
        logger.traceExit();
        return connection;
    }

    public Connection getConnection() {
        logger.traceEntry();
        try {
            if (instance == null || instance.isClosed()) {
                instance = getNewConnection();
            }
        } catch (SQLException ex) {
            logger.error(ex);
            ex.printStackTrace();
        }
        logger.traceExit();
        return instance;
    }
}
