package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.Optional;
import java.util.function.Predicate;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


import application.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXRadioButton;

import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.util.StringConverter;


import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Callback;

public class AccountingViewController implements Initializable
{
    ObservableList<Accounting> accountingList = FXCollections.observableArrayList();
    FilteredList<Accounting> filteredAccountingList = new FilteredList<Accounting>(accountingList);


    @FXML
    private JFXButton updateTransactionBtn;

    @FXML
    private JFXRadioButton rentRadioButton;

    @FXML
    private JFXRadioButton purchaseRadioButton;

    private String radioButtonLabel;

    @FXML
    private JFXButton btnBack;

    @FXML
    private JFXTextField transactionAmountTF;

    @FXML
    private JFXButton deleteTransactionBtn;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private JFXTextField movieTitleTF;

    @FXML
    private DatePicker transactionDatePicker;

    @FXML
    private JFXTextField cusIDTF;

    @FXML
    private JFXTextField transactionIDTF;

    @FXML
    private DatePicker totalIncomeToDatePicker;

    @FXML
    private JFXButton createTransactionBtn;

    @FXML
    private ToggleGroup typeOfTransaction;



    @FXML
    private TableView<Accounting> accountingTable;

    @FXML
    private TableColumn<Accounting, String> transactionIDCol;

    @FXML
    private TableColumn<Accounting, String> customerIDCol;

    @FXML
    private TableColumn<Accounting, String> movieTitleCol;

    @FXML
    private TableColumn<Accounting, String> transactionTypeCol;

    @FXML
    private TableColumn<Accounting, String> dateOfTransactionCol;

    @FXML
    private TableColumn<Accounting, Float> transactionAmountCol;

    @FXML
    private TableColumn<Accounting, Float> incomeCol;



    @FXML
    private JFXTextField incomeTF;

    @FXML
    private JFXTextField searchTableTF;

    @FXML
    private JFXTextField totalIncomeTF;

    @FXML
    private JFXButton clearTransactionTF;

    @FXML
    private JFXButton refreshTransactionButton;

    @FXML
    private JFXButton btnExit;

    private Connection conn;

    private ResultSet rs;

    private PreparedStatement pst;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        initColumn();
        loadAccountingData();
        loadSumOfIncome();

        totalIncomeToDatePicker.setValue(LocalDate.now());
        final Callback<DatePicker, DateCell> dayCellFactory =
                new Callback<DatePicker, DateCell>() {
                    @Override
                    public DateCell call(final DatePicker datePicker) {
                        return new DateCell() {
                            @Override
                            public void updateItem(LocalDate item, boolean empty) {
                                super.updateItem(item, empty);


                                if (item.isBefore(LocalDate.now()))
                                {
                                    setDisable(true);
                                    setStyle("-fx-background-color: #ffc0cb;");
                                }

								else if (item.isAfter(LocalDate.now()))
								{
									setDisable(true);
									setStyle("-fx-background-color: #ffc0cb");
								}

                            }
                        };
                    }
                };
        totalIncomeToDatePicker.setDayCellFactory(dayCellFactory);

        rentRadioButton.setToggleGroup(typeOfTransaction);
        purchaseRadioButton.setToggleGroup(typeOfTransaction);

        rentRadioButton.setOnAction(e ->{
            radioButtonLabel = rentRadioButton.getText();
        });

        purchaseRadioButton.setOnAction(e ->{
           radioButtonLabel = purchaseRadioButton.getText();
        });
    }

    void loadSumOfIncome()
    {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys?autoReconnect=true&useSSL=false", "root", "password");

            totalIncomeTF.clear();
            DatabaseHandler handler = DatabaseHandler.getInstance();
            String qu = "SELECT SUM(INCOME) AS TOTAL_INCOME FROM ACCOUNTING";
            ResultSet rs = handler.execQuery(qu);

            while(rs.next())
            {
                totalIncomeTF.setText(Float.toString(rs.getFloat("TOTAL_INCOME")));
            }
        } catch (SQLException ex){
            ex.printStackTrace();
        } catch (ClassNotFoundException ex){
            ex.printStackTrace();
        }
    }

    @FXML
    void btnBackClicked(ActionEvent event) throws IOException
    {
        returnToMainMenu(event);
    }

    @FXML
    void btnExitClicked(ActionEvent event)
    {
        System.exit(0);
    }

    @FXML
    void btnRefreshTableButton(ActionEvent event)
    {
        accountingList.clear();
        loadAccountingData();
    }

    @FXML
    void keySearchTable (KeyEvent event)
    {
        searchTableTF.textProperty().addListener((observableValue, oldValue, newValue) ->{
            filteredAccountingList.setPredicate((Predicate<? super Accounting>) Accounting->{
                if(newValue == null || newValue.isEmpty()){
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if(Accounting.getTransaction_ID().contains(newValue)){
                    return true;
                }else if(Accounting.getCustomer_ID().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }else if(Accounting.getMovie_Title().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }
                return false;
            });
        });
        SortedList<Accounting> sortedData = new SortedList<>(filteredAccountingList);
        sortedData.comparatorProperty().bind(accountingTable.comparatorProperty());
        accountingTable.setItems(sortedData);
    }

    @FXML
    void returnToMainMenu(ActionEvent event) throws IOException
    {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/view/MainView.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    public void initColumn()
    {
        transactionIDCol.setCellValueFactory(new PropertyValueFactory("Transaction_ID"));
        customerIDCol.setCellValueFactory(new PropertyValueFactory("Customer_ID"));
        movieTitleCol.setCellValueFactory(new PropertyValueFactory("Movie_Title"));
        transactionTypeCol.setCellValueFactory(new PropertyValueFactory("Transaction_Type"));
        dateOfTransactionCol.setCellValueFactory(new PropertyValueFactory("Date_Of_Transaction"));
        transactionAmountCol.setCellValueFactory(new PropertyValueFactory("Transaction_Amount"));
        incomeCol.setCellValueFactory(new PropertyValueFactory("Income"));
    }

    private void loadAccountingData()
    {
        DatabaseHandler handler = DatabaseHandler.getInstance();
        String qu = "SELECT * FROM ACCOUNTING";
        ResultSet rs = handler.execQuery(qu);
        accountingList.clear();

        try {
            while (rs.next()) {
                String transactionID = rs.getString("TRANSACTION_ID");
                String customerID = rs.getString("CUSTOMER_ID");
                String movieTitle = rs.getString("MOVIE_TITLE");
                String transactionType = rs.getString("TRANSACTION_TYPE");
                String DOT = rs.getString("DATE_OF_TRANSACTION");
                Float transactionAmount = rs.getFloat("TRANSACTION_AMOUNT");
                Float income = rs.getFloat("INCOME");

                accountingList.add(new Accounting(transactionID, customerID, movieTitle, transactionType, DOT, transactionAmount,income));

            }
        } catch (SQLException ex) {
            Logger.getLogger(ManagerViewController.class.getName()).log(Level.SEVERE, null, ex);
        }

        accountingTable.getItems().setAll(accountingList);

    }


    @FXML
    void handleAccountingTableEvent(MouseEvent event) throws SQLException, ClassNotFoundException
    {
        Class.forName("com.mysql.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys?autoReconnect=true&useSSL=false", "root", "password");
        String query = "SELECT * FROM ACCOUNTING WHERE TRANSACTION_ID = ?";
        ResultSet rs;
        pst = conn.prepareStatement(query);
        Accounting accounting = accountingTable.getSelectionModel().getSelectedItem();
        pst.setString(1, accounting.getTransaction_ID());

        rs = pst.executeQuery();

        while(rs.next())
        {
            transactionIDTF.setText(rs.getString("TRANSACTION_ID"));
            cusIDTF.setText(rs.getString("CUSTOMER_ID"));
            movieTitleTF.setText(rs.getString("MOVIE_TITLE"));
            ((TextField)transactionDatePicker.getEditor()).setText(rs.getString("DATE_OF_TRANSACTION"));
            transactionAmountTF.setText(Float.toString(rs.getFloat("TRANSACTION_AMOUNT")));
            incomeTF.setText(Float.toString(rs.getFloat("INCOME")));

            if(null != rs.getString("TRANSACTION_TYPE"))switch (rs.getString("TRANSACTION_TYPE")) {
                case "Rent":
                    rentRadioButton.setSelected(true);
                    break;
                case "Purchase":
                    purchaseRadioButton.setSelected(true);
                    break;
                default:
                    rentRadioButton.setSelected(false);
                    purchaseRadioButton.setSelected(false);
                    break;
            }else{
                rentRadioButton.setSelected(false);
                purchaseRadioButton.setSelected(false);
            }
        }

        pst.close();
        rs.close();
    }

    @FXML
    void btnDeleteTransactionClicked(ActionEvent event)
    {
        try {

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Transaction");
            alert.setHeaderText("Delete: " + transactionIDTF.getText() + " from the system.");
            alert.setContentText("Are you sure?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                Class.forName("com.mysql.jdbc.Driver");
                conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys?autoReconnect=true&useSSL=false", "root", "password");
                String query = "DELETE FROM ACCOUNTING WHERE TRANSACTION_ID = ?";
                pst = conn.prepareStatement(query);

                pst.setString(1, transactionIDTF.getText());

                pst.execute();

                clearTransactionInfoForm();
                loadSumOfIncome();

            } else {
                // ... user chose CANCEL or closed the dialog
            }

        } catch (SQLException e){
            Alert alert = new Alert(AlertType.ERROR,"Make sure you included an ID for the Customer!\n(It's required!)", ButtonType.OK);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.setHeaderText("SQL Error");
            alert.show();
        } catch (ClassNotFoundException ex){
            Alert alert = new Alert(AlertType.ERROR);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.setHeaderText("Could Not Connect to Database!");
            alert.show();
        }
    }

    @FXML
    void btnCreateTransactionClicked(ActionEvent event) throws ClassNotFoundException
    {
        try {

            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys?autoReconnect=true&useSSL=false", "root", "password");
            System.out.println("Successful Database Connection.");
            PreparedStatement statement2 = conn.prepareStatement("INSERT INTO ACCOUNTING VALUES (?,?,?,?,?,?,?)");

            statement2.setString(1, transactionIDTF.getText());
            statement2.setString(2, cusIDTF.getText());
            statement2.setString(3, movieTitleTF.getText());
            statement2.setString(4, radioButtonLabel);
            statement2.setString(5, ((TextField) transactionDatePicker.getEditor()).getText());
            statement2.setFloat(6, Float.parseFloat(transactionAmountTF.getText()));
            statement2.setFloat(7, Float.parseFloat(incomeTF.getText()));
            statement2.executeUpdate();

            // The block of code below displays a popup when the user successfully adds a credit card to the database.
            Alert addedMovie = new Alert(AlertType.INFORMATION, "The Transaction ID" + transactionIDTF.getText() + "  has been added to the system!", ButtonType.OK);
            addedMovie.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            addedMovie.setHeaderText("Transaction Complete!");
            addedMovie.setTitle("Transaction");
            addedMovie.show();

            accountingList.clear();
            loadAccountingData();
            loadSumOfIncome();
            conn.close();

        } catch (SQLException e) {
            // This block of code below displays a error pop up for the user when a SQL error is thrown
            Alert alert = new Alert(AlertType.ERROR, "The movie you tried to enter was either: \nAlready entered into the database, \nor the MOVIE ID text field were left blank.", ButtonType.OK);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.setHeaderText("Failed to add Movie");
            alert.setTitle("Movie");
            alert.show();
        } catch (NumberFormatException ex) {
            Alert alert = new Alert(AlertType.ERROR, "Make sure you enter a number from both the\nOn hand quantity, as well as,\nThe total quantity.", ButtonType.OK);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.setHeaderText("Blank Text Fields");
            alert.setTitle("Movie");
            alert.show();
        }
    }

    @FXML
    void btnUpdateTransactionClicked(ActionEvent event)
    {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys?autoReconnect=true&useSSL=false", "root", "password");
            String query = "UPDATE ACCOUNTING SET TRANSACTION_ID = ?, CUSTOMER_ID = ?, MOVIE_TITLE =?, TRANSACTION_TYPE = ?, DATE_OF_TRANSACTION = ?, TRANSACTION_AMOUNT = ?, INCOME = ? WHERE TRANSACTION_ID='" + transactionIDTF.getText() + "' ";
            pst = conn.prepareStatement(query);

            pst.setString(1, transactionIDTF.getText());
            pst.setString(2, cusIDTF.getText());
            pst.setString(3, movieTitleTF.getText());
            pst.setString(4, radioButtonLabel);
            pst.setString(5, ((TextField)transactionDatePicker.getEditor()).getText());
            pst.setFloat(6, Float.parseFloat(transactionAmountTF.getText()));
            pst.setFloat(7, Float.parseFloat(incomeTF.getText()));


            Alert alert = new Alert(AlertType.INFORMATION, "The Transaction: " + transactionIDTF.getText() + " has been updated!", ButtonType.OK);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.setHeaderText("Transaction Updated!");
            alert.setTitle("Transaction");
            alert.show();

            pst.execute();

            accountingList.clear();
            loadAccountingData();
            loadSumOfIncome();

        } catch (SQLException e){
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR,"Make sure you included an ID for the Transaction and Customer!\n(It's required!)", ButtonType.OK);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.setHeaderText("Empty Text Field Error");
            alert.show();
        } catch (ClassNotFoundException ex){
            Alert alert = new Alert(AlertType.ERROR);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.setHeaderText("Could Not Connect to Database!");
            alert.show();
        }
    }

    @FXML
    void clearTransactionInfoForm()
    {
        transactionIDTF.clear();
        cusIDTF.clear();
        movieTitleTF.clear();
        rentRadioButton.setSelected(false);
        purchaseRadioButton.setSelected(false);
        transactionDatePicker.getEditor().clear();
        transactionAmountTF.clear();
        incomeTF.clear();
    }
}
