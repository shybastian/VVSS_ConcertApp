package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Concert;
import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.ApplicationException;
import services.IObserver;
import services.IServer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ControllerConcertsWithDate implements IObserver {
    private static final Logger logger = LogManager.getLogger(ControllerConcertsWithDate.class);

    private User currentUser;
    private IServer server;
    private Stage stage;
    private ControllerMenu controllerMenu;

    private ObservableList<Concert> concertsList;

    @FXML
    private DatePicker datePicker;
    @FXML
    private Button buttonBack;
    @FXML
    private Button buttonBuy;
    @FXML
    private TextField textFieldBuyerName;
    @FXML
    private TextField textFieldNrOfTickets;
    @FXML
    private Label labelStatus;
    @FXML
    private TableView<Concert> table;
    @FXML
    private TableColumn<Concert, Integer> colIdConcert;
    @FXML
    private TableColumn<Concert, String> colArtist;
    @FXML
    private TableColumn<Concert, String> colLocation;
    @FXML
    private TableColumn<Concert, LocalDate> colDate;
    @FXML
    private TableColumn<Concert, LocalTime> colTime;
    @FXML
    private TableColumn<Concert, Integer> colTotalTickets;
    @FXML
    private TableColumn<Concert, Integer> colSoldTickets;

    public IServer getServer() {
        return server;
    }

    public void setServer(IServer server) {
        this.server = server;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public ControllerMenu getControllerMenu() {
        return controllerMenu;
    }

    public void setControllerMenu(ControllerMenu controllerMenu) {
        this.controllerMenu = controllerMenu;
    }

    public ControllerConcertsWithDate getController()
    {
        return this;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void goBack(ActionEvent event) throws IOException {
//        Parent tableViewParent = FXMLLoader.load(getClass().getResource("/fxmls/menu.fxml"));
//        Scene tableViewSceneInFile = new Scene(tableViewParent);
//        tableViewSceneInFile.getStylesheets().add(getClass().getResource("/css/main.css").toExternalForm());
//
//        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
//        window.setScene(tableViewSceneInFile);
//        window.show();
        try
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ControllerMenu.class.getResource("/fxmls/menu.fxml"));
            Parent root = loader.load();
            ControllerMenu controllerMenu = loader.getController();
            controllerMenu.setServer(server);
            controllerMenu.setCurrentUser(currentUser);

            Stage mainStage = new Stage();
            mainStage.setTitle("Main Menu - " + currentUser.getUsername());
            Scene scene = new Scene(root);
            mainStage.setScene(scene);
            controllerMenu.setStage(mainStage);
            scene.getRoot().requestFocus();
            mainStage.show();
            this.stage.close();
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

//    private void colorSoldOutConcerts()
//    {
//        table.setRowFactory(row -> new TableRow<Concert>(){
//            @Override
//            public void updateItem(Concert item, boolean empty){
//                super.updateItem(item, empty);
//
//                if (item == null || empty) {
//                    setStyle("");
//                } else {
//                    if (item.getSoldTickets() == item.getTotalTickets()){
//                        for(int i=0; i<getChildren().size();i++){
//                            ((Labeled) getChildren().get(i)).setTextFill(Color.BLACK);
//                            ((Labeled) getChildren().get(i)).setStyle("-fx-background-color: #ff0044");
//                        }
//                    } else
//                        {
////                        if(getTableView().getSelectionModel().getSelectedItems().contains(item))
////                        {
////                            for(int i=0; i<getChildren().size();i++)
////                            {
////                                ((Labeled) getChildren().get(i)).setTextFill(Color.WHITE);;
////                            }
////                        }
////                        else
////                            {
////                            for(int i=0; i<getChildren().size();i++)
////                            {
////                                ((Labeled) getChildren().get(i)).setTextFill(Color.BLACK);;
////                            }
////                        }
//                            if (item.getSoldTickets() >= item.getTotalTickets()/2)
//                            {
//                                for(int i=0; i<getChildren().size();i++)
//                                {
//                                    ((Labeled) getChildren().get(i)).setTextFill(Color.BLACK);
//                                    ((Labeled) getChildren().get(i)).setStyle("-fx-background-color: #f1ff40");
//                                }
//                            }
//                            else
//                            {
//                                for(int i=0; i<getChildren().size();i++)
//                                {
//                                    ((Labeled) getChildren().get(i)).setTextFill(Color.BLACK);
//                                    ((Labeled) getChildren().get(i)).setStyle("-fx-background-color: #00f000");
//                                }
//                            }
//                    }
//                }
//            }
//        });
//    }

    private void colorSoldOutConcerts()
    {
        colSoldTickets.setCellFactory(column -> {
            return new TableCell<Concert, Integer>() {
                @Override
                protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);

                    setText(empty ? "" : getItem().toString());
                    setGraphic(null);

                    TableRow<Concert> currentRow = getTableRow();

                    if (!isEmpty()) {

                        if (item == currentRow.getItem().getTotalTickets())
                            currentRow.setStyle("-fx-background-color: red");
                        else if (item == currentRow.getItem().getTotalTickets()/2)
                            currentRow.setStyle("-fx-background-color: yellow");
                    }
                }
            };
        });
    }

    public void initializeTable(ActionEvent actionEvent)
    {
//        // TODO: Daca filteredConcerts.size() == 0; sari peste toata treaba de initializare a tabelului
//        logger.traceEntry();
//        LocalDate chosenDate = datePicker.getValue();
//        logger.trace("Date chosen: " + chosenDate.toString());
//        logger.trace("Proceeding to call Server to filter list.");
//        List<Concert> filteredConcerts = concertService.filterConcerts(chosenDate);
//        logger.trace("Server returned a list with size: " + filteredConcerts.size());
//        ObservableList<Concert> observableList = FXCollections.observableArrayList(filteredConcerts);
//
//        colIdConcert.setCellValueFactory(new PropertyValueFactory<>("id"));
//        colArtist.setCellValueFactory(new PropertyValueFactory<>("artist"));
//        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
//        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
//        colTime.setCellValueFactory(new PropertyValueFactory<>("time"));
//        colTotalTickets.setCellValueFactory(new PropertyValueFactory<>("totalTickets"));
//        colSoldTickets.setCellValueFactory(new PropertyValueFactory<>("soldTickets"));
//
//
//        table.setItems(observableList);
//
//        colorSoldOutConcerts();
//
//        table.refresh();

        colorSoldOutConcerts();
        logger.traceEntry();
        LocalDate chosenDate = datePicker.getValue();
        logger.trace("Date chosen: " + chosenDate.toString());

        List<Concert> filteredConcerts = new ArrayList<>();

        try
        {
            filteredConcerts = server.getFilteredConcerts(chosenDate);
        } catch (ApplicationException ex)
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("No Concerts found");
            alert.setContentText("No Concerts found during given date! Try again.");
            alert.showAndWait();
        }

        ObservableList<Concert> observableList = FXCollections.observableList(filteredConcerts);

        colIdConcert.setCellValueFactory(new PropertyValueFactory<>("id"));
        colArtist.setCellValueFactory(new PropertyValueFactory<>("artist"));
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        colTotalTickets.setCellValueFactory(new PropertyValueFactory<>("totalTickets"));
        colSoldTickets.setCellValueFactory(new PropertyValueFactory<>("soldTickets"));

        table.setItems(observableList);
        colorSoldOutConcerts();

    }

    private Concert getClickedRow() { return table.getSelectionModel().getSelectedItem(); }

    public void buyTickets(ActionEvent event)
    {
        /*(String buyerName = textFieldBuyerName.getText();
        int nrOfTickets = Integer.parseInt(textFieldNrOfTickets.getText());

        Concert clickedConcert = getClickedRow();

        Transactions newTransaction = new Transactions(clickedConcert.getId(),nrOfTickets,getCurrentUser(),buyerName);
        boolean checkIfValidTransaction = transactionsService.validateTransaction(newTransaction);
        if(checkIfValidTransaction)
        {
            int newSoldTickets = clickedConcert.getSoldTickets() + nrOfTickets;
            Concert toUpdateConcert = new Concert(clickedConcert.getId(),clickedConcert.getArtist(),
                    clickedConcert.getTotalTickets(),newSoldTickets,clickedConcert.getLocation(),
                    clickedConcert.getDate(),clickedConcert.getTime());
            concertService.update(toUpdateConcert);
            transactionsService.addTransaction(newTransaction);
            labelStatus.setText("Sold successfully!");
        }
        else
        {
            labelStatus.setText("Can not buy tickets for the selected concert!");
        }

        table.refresh();*/

        Concert clickedConcert = getClickedRow();
        if (textFieldBuyerName.getText().isEmpty() || textFieldNrOfTickets.getText().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Can not buy!");
            alert.setContentText("Can not buy tickets. All fields are requiered");
            alert.showAndWait();
        } else if (Integer.parseInt(textFieldNrOfTickets.getText()) < 0)
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Can not buy!");
            alert.setContentText("Can not buy a negative amount of tickets!");
            alert.showAndWait();
        } else {
            String buyerName = textFieldBuyerName.getText();
            int ticketsBought = Integer.parseInt(textFieldNrOfTickets.getText());
            try
            {
                server.buyTickets(currentUser.getUsername(),buyerName,ticketsBought,clickedConcert.getId());

                colorSoldOutConcerts();

                List<Concert> concertList = server.getAllConcerts();
                table.setItems(FXCollections.observableArrayList(concertList));
            } catch (ApplicationException ex)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Buy error");
                alert.setContentText("Sold out // Not enough tickets to sell. Sorry!");
                alert.showAndWait();
            }
        }

    }

    @Override
    public void update() throws ApplicationException {
        initializeTable(null);
        LocalDate date = datePicker.getValue();
        List<Concert> concertList = new ArrayList<>();

        try
        {
            concertList = server.getFilteredConcerts(date);
        } catch (ApplicationException ex)
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Nothing found!");
            alert.setContentText("Sorry. No concerts were found for the given date!");
            alert.showAndWait();
        }

        ObservableList<Concert> observableList = FXCollections.observableList(concertList);

        colIdConcert.setCellValueFactory(new PropertyValueFactory<>("id"));
        colArtist.setCellValueFactory(new PropertyValueFactory<>("artist"));
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        colTotalTickets.setCellValueFactory(new PropertyValueFactory<>("totalTickets"));
        colSoldTickets.setCellValueFactory(new PropertyValueFactory<>("soldTickets"));

        table.setItems(observableList);
        colorSoldOutConcerts();
    }

//    private void dontNeedThis()
//    {
//                colSoldTickets.setCellFactory(column ->{
//            return new TableCell<Concert,Integer>(){
//                @Override
//                protected void updateItem(int item, boolean empty)
//                {
//                    super.updateItem(item, empty);
//
//                    if(item == 0 || empty)
//                    {
//                        setText(null);
//                        setStyle("");
//                    }
//                    else
//                    {
//                        setText(String.valueOf(item));
//
//                        Concert concert = getTableView().getItems().get(getIndex());
//
//                        if(concert.getSoldTickets() == concert.getTotalTickets())
//                        {
//                            setTextFill(Color.BLACK);
//                            setStyle("-fx-background-color: red");
//                        }
//                        else{
//                            if(concert.getSoldTickets() >= concert.getTotalTickets()/2)
//                            {
//                                setTextFill(Color.BLACK);
//                                setStyle("-fx-background-color: yellow");
//                            }
//                            else
//                            {
//                                setTextFill(Color.BLACK);
//                                setStyle("-fx-background-color: green");
//                            }
//                        }
//                    }
//                }
//            };
//        });
//
//
//        System.out.println();
//
//                colSoldTickets.setCellFactory(column -> {
//            return new TableCell<Concert, Integer>() {
//                @Override
//                protected void updateItem(int item, boolean empty) {
//                    super.updateItem(item, empty);
//
//                    setText(empty ? "" : getItem().toString());
//                    setGraphic(null);
//
//                    TableRow<Concert> currentRow = getTableRow();
//
//                    if (!isEmpty()) {
//
//                        if (item ==)
//                            currentRow.setStyle("-fx-background-color:lightcoral");
//                        else
//                            currentRow.setStyle("-fx-background-color:lightgreen");
//                    }
//                }
//            };
//        });
//    }
}
