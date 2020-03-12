package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.ApplicationException;
import services.IServer;

import java.io.IOException;

public class ControllerMain {
    private static final Logger logger = LogManager.getLogger(ControllerMain.class);
    private User currentUser;
    private IServer server;
    private ControllerMenu controllerMenu;
    private Stage stage;

    @FXML
    private TextField textFieldUsername;

    @FXML
    private TextField textFieldPassword;

    @FXML
    private Button buttonLogIn;

    @FXML
    private Label labelStatus;

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public IServer getServer() {
        return server;
    }

    public void setServer(IServer server) {
        this.server = server;
    }

    public ControllerMenu getControllerMenu() {
        return controllerMenu;
    }

    public void setControllerMenu(ControllerMenu controllerMenu) {
        this.controllerMenu = controllerMenu;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void logInUser(ActionEvent event) throws IOException {
//        String username = textFieldUsername.getText();
//        String password = textFieldPassword.getText();
//        User currentUser = new User(username,password);
//        boolean check = service.canLogIn(currentUser);
//        if (check)
//        {
////            setCurrentUser(username);
////            Parent tableViewParent = FXMLLoader.load(getClass().getResource("/fxmls/menu.fxml"));
////            Scene tableViewSceneInFile = new Scene(tableViewParent);
////
////            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
////            window.setScene(tableViewSceneInFile);
////            window.show();
//
//            setCurrentUser(username);
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/menu.fxml"));
//            Parent tableViewParent = loader.load();
//            Scene tableViewSceneInFile = new Scene(tableViewParent);
//            tableViewSceneInFile.getStylesheets().add(getClass().getResource("/css/main.css").toExternalForm());
//            ControllerMenu controllerToSet = loader.getController();
//            controllerToSet.getController().setCurrentUser(getCurrentUser());
//
//            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
//            window.setScene(tableViewSceneInFile);
//            window.show();
//        }
//        else
//        {
//            labelStatus.setText("Username or Password is wrong!");
//        }

        Alert alert = new Alert(Alert.AlertType.WARNING);
        if (textFieldUsername.getText().isEmpty())
        {
            alert.setContentText("Username empty");
            alert.setHeaderText("Invalid username");
            alert.showAndWait();
        } else if (textFieldPassword.getText().isEmpty())
        {
            alert.setContentText("Password empty");
            alert.setHeaderText("Invalid password");
            alert.showAndWait();
        } else {
            try
            {
                String userName = textFieldUsername.getText();
                String password = textFieldPassword.getText();
                this.currentUser = new User(userName,password);

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(ControllerMenu.class.getResource("/fxmls/menu.fxml"));
                Parent root = loader.load();
                ControllerMenu controllerMenu = loader.getController();
                controllerMenu.setServer(server);
                controllerMenu.setCurrentUser(currentUser);

                server.login(currentUser.getUsername(),currentUser.getPassword(), controllerMenu);

                if (currentUser != null)
                {
                    Stage mainStage = new Stage();
                    mainStage.setTitle("Main Menu - " + currentUser.getUsername());
                    Scene scene = new Scene(root);
                    mainStage.setScene(scene);
                    controllerMenu.setStage(mainStage);
                    scene.getRoot().requestFocus();
                    mainStage.show();
                    this.stage.close();
                }
            } catch (IOException ex)
            {
                ex.printStackTrace();
            } catch (ApplicationException ex)
            {
                alert.setContentText("Invalid Username and Password");
                alert.setHeaderText("Login failed");
                alert.showAndWait();
            }
        }
    }
}
