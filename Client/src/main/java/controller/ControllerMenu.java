package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import model.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import service.UserService;
import services.ApplicationException;
import services.IObserver;
import services.IServer;

import java.io.IOException;

public class ControllerMenu implements IObserver {
    private User currentUser;
    private IServer server;
    private ControllerAllConcerts controllerAllConcerts;
    private ControllerConcertsWithDate controllerConcertsWithDate;
    private Stage stage;

    @FXML
    private Button buttonLogOut;
    @FXML
    private Button buttonSearchByDate;
    @FXML
    private Button buttonShowAll;

    public IServer getServer() {
        return server;
    }

    public void setServer(IServer server) {
        this.server = server;
    }

    public ControllerAllConcerts getControllerAllConcerts() {
        return controllerAllConcerts;
    }

    public void setControllerAllConcerts(ControllerAllConcerts controllerAllConcerts) {
        this.controllerAllConcerts = controllerAllConcerts;
    }

    public ControllerConcertsWithDate getControllerConcertsWithDate() {
        return controllerConcertsWithDate;
    }

    public void setControllerConcertsWithDate(ControllerConcertsWithDate controllerConcertsWithDate) {
        this.controllerConcertsWithDate = controllerConcertsWithDate;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public ControllerMenu getController() { return this; }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) { this.currentUser = currentUser; }

    public void logOut(ActionEvent event) throws IOException {
//        Parent tableViewParent = FXMLLoader.load(getClass().getResource("/fxmls/main.fxml"));
//        Scene tableViewSceneInFile = new Scene(tableViewParent);
//        tableViewSceneInFile.getStylesheets().add(getClass().getResource("/css/main.css").toExternalForm());
//
//        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
//        window.setScene(tableViewSceneInFile);
//        window.show();

        try
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ControllerMain.class.getResource("/fxmls/main.fxml"));
            Parent root = loader.load();
            ControllerMain controllerMain = loader.getController();

            ApplicationContext context = new AnnotationConfigApplicationContext(service.beanConfigs.UserServiceConfig.class);
            UserService userService = context.getBean(UserService.class);

            server.logout(currentUser.getUsername(),currentUser.getPassword(),this);

            Stage loginStage = new Stage();
            controllerMain.setServer(server);
            controllerMain.setStage(loginStage);
            loginStage.setTitle("Festival Muzica - Login");
            loginStage.setScene(new Scene(root));
            loginStage.show();
            this.stage.close();
        } catch (Exception ex)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Logout Error");
            alert.setContentText("Couldn't Log Out");
            alert.showAndWait();
        }
    }

    public void moveToSearchByDate(ActionEvent event) throws IOException {
//        Parent tableViewParent = FXMLLoader.load(getClass().getResource("/fxmls/concertsWithDate.fxml"));
//        Scene tableViewSceneInFile = new Scene(tableViewParent);
//
//        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
//        window.setScene(tableViewSceneInFile);
//        window.show();

//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/concertsWithDate.fxml"));
//        Parent tableViewParent = loader.load();
//        Scene tableViewSceneInFile = new Scene(tableViewParent);
//        ControllerConcertsWithDate controllerToSet = loader.getController();
//        controllerToSet.getController().setCurrentUser(getCurrentUser());
//
//        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
//        window.setScene(tableViewSceneInFile);
//        window.show();

        try
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ControllerConcertsWithDate.class.getResource("/fxmls/concertsWithDate.fxml"));
            Parent root = loader.load();
            ControllerConcertsWithDate concertsWithDate = loader.getController();
            concertsWithDate.setServer(server);
            concertsWithDate.setCurrentUser(currentUser);

            server.logout(currentUser.getUsername(),currentUser.getPassword(),this);
            server.login(currentUser.getUsername(),currentUser.getPassword(),concertsWithDate);

            Stage mainStage = new Stage();
            mainStage.setTitle("Concerts With Date - " + currentUser.getUsername());
            Scene scene = new Scene(root);
            mainStage.setScene(scene);
            concertsWithDate.setStage(mainStage);
            scene.getRoot().requestFocus();
            mainStage.show();
            this.stage.close();
        } catch (IOException ex)
        {
            ex.printStackTrace();
        } catch (ApplicationException e) {
            e.printStackTrace();
        }
    }

    public void moveToShowAll(ActionEvent event) throws IOException
    {
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/allConcerts.fxml"));
//        Parent tableViewParent = loader.load();
//        Scene tableViewSceneInFile = new Scene(tableViewParent);
//        ControllerAllConcerts controllerToSet = loader.getController();
//        controllerToSet.getController().setCurrentUser(getCurrentUser());
//        controllerToSet.getController().initializeTable(null);
//
//        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
//        window.setScene(tableViewSceneInFile);
//        window.show();
        try
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ControllerAllConcerts.class.getResource("/fxmls/allConcerts.fxml"));
            Parent root = loader.load();
            ControllerAllConcerts allConcerts = loader.getController();
            allConcerts.setServer(server);
            allConcerts.setCurrentUser(currentUser);
            allConcerts.initializeTable(null);

            server.logout(currentUser.getUsername(),currentUser.getPassword(),this);
            server.login(currentUser.getUsername(),currentUser.getPassword(), allConcerts);

            Stage mainStage = new Stage();
            mainStage.setTitle("Concerts With Date - " + this.currentUser.getUsername());
            Scene scene = new Scene(root);
            mainStage.setScene(scene);
            allConcerts.setStage(mainStage);
            scene.getRoot().requestFocus();
            mainStage.show();
            this.stage.close();
        } catch (IOException ex)
        {
            ex.printStackTrace();
        } catch (ApplicationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update() throws ApplicationException {

    }
}
