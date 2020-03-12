package client;

import controller.ControllerMain;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import rpcProtocol.ServerRPCProxy;
import services.IServer;

public class StartRCPClient extends Application {
    private static int defaultPort = 55555;
    private static String defaultServer = "localhost";

    @Override
    public void start(Stage primaryStage) throws Exception {
        IServer server = new ServerRPCProxy(defaultServer,defaultPort);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ControllerMain.class.getResource("/fxmls/main.fxml"));
        Parent root = loader.load();
        ControllerMain controllerMain = loader.getController();

        controllerMain.setServer(server);
        controllerMain.setStage(primaryStage);
        primaryStage.setTitle("Festival Muzica - Login");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
