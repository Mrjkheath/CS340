package controller;

import java.io.IOException;
import java.net.URL;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import application.DatabaseHandler;
import application.Movie;
import application.Customer;
import application.Rental;
import application.RentalCustomer;

import java.security.Key;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RentalViewController implements Initializable{


    // These are the lists that are used to display the information in the tables.
    ObservableList<Movie> movieList = FXCollections.observableArrayList();
    FilteredList<Movie> filteredMovieList = new FilteredList<Movie>(movieList);

    ObservableList<RentalCustomer> customerList = FXCollections.observableArrayList();
    FilteredList<RentalCustomer> filteredCustomerList = new FilteredList<RentalCustomer>(customerList);

    ObservableList<Rental> rentalList = FXCollections.observableArrayList();
    FilteredList<Rental> filteredRentalList = new FilteredList<Rental>(rentalList);


    Connection conn = null;
    PreparedStatement pst = null;
    /*
     * TEXTFIELDS
     */
    @FXML
    private JFXTextField searchMovieTF;

    @FXML
    private JFXTextField customerIDTF;

    @FXML
    private JFXTextField firstNameTF;

    @FXML
    private JFXTextField movieTitleTF;

    @FXML
    private JFXTextField searchCustomerTF;

    @FXML
    private JFXTextField lastNameTF;

    @FXML
    private JFXTextField searchRentalTF;

    @FXML
    private JFXTextField movieIDTF;

    @FXML
    private JFXTextField numRentalTF;

	/*
	 * BUTTONS
	 */

    @FXML
    private JFXButton btnExit;

    @FXML
    private JFXButton btnClearFields;

    @FXML
    private JFXButton btnReturnMovie;

    @FXML
    private JFXButton btnRefreshMovTable;

    @FXML
    private JFXButton btnRefreshRentals;


    @FXML
    private JFXButton btnBack;

    @FXML
    private JFXButton btnRefreshCustTable;

    @FXML
    private JFXButton btnPurchaseMovie;

    @FXML
    private JFXButton btnRentMovie;

    @FXML
    private JFXTextField movieSearchFilter;

	/*
	 * TABLES
	 */

    // CUSTOMER TABLE
    @FXML
    private TableView<RentalCustomer> customerTable;

    @FXML
    private TableColumn<RentalCustomer, String> firstNameCustCol;

    @FXML
    private TableColumn<RentalCustomer, String> lastNameCustCol;

    @FXML
    private TableColumn< RentalCustomer, Integer > numRentalsCustCol;

    @FXML
    private TableColumn< RentalCustomer, String > expirationCustCol;

    @FXML
    private TableColumn< RentalCustomer, String > customerIDCustCol;

    @FXML
    private TableColumn< RentalCustomer, String > creditCardNumCustCol;

    // MOVIE TABLE
    @FXML
    private TableView<Movie> movieTable;

    @FXML
    private TableColumn<Movie, String> titleMovCol;

    @FXML
    private TableColumn<Movie, Integer > totalQantMovCol;

    @FXML
    private TableColumn<Movie, Integer > onHandMovCol;

    @FXML
    private TableColumn<Movie, String > movieIDMovCol;

    @FXML
    private TableColumn<Movie, String > rentalPriceMovCol;

    @FXML
    private TableColumn<Movie, String > relDateMovCol;

    @FXML
    private TableColumn<Movie, String > purchPriceMovCol;

    @FXML
    private TableColumn<Movie, String > ratingMovCol;

    @FXML
    private TableColumn<Movie, String > genreCol;

    // RENTAL TABLE

    @FXML
    private TableView<Rental> rentalTable;

    @FXML
    private TableColumn<Rental, String> customIDRentCol;

    @FXML
    private TableColumn<Rental, String> fNameRentCol;

    @FXML
    private TableColumn<Rental, String> lNameRentCol;

    @FXML
    private TableColumn<Rental, String> movieIDRentCol;

    @FXML
    private TableColumn<Rental, String> movieTitleRentCol;

    @FXML
    AnchorPane rootPane;

    /*
     *  BUTTON ACTION METHODS
     */

    @FXML
    void btnRentMovieClicked(ActionEvent event) throws SQLException, ClassNotFoundException
    {
        try {

            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys?autoReconnect=true&useSSL=false", "root", "password");
            System.out.println("Successful Database Connection.");
            PreparedStatement statement2 = conn.prepareStatement("INSERT INTO RENTAL VALUES (?,?,?,?,?)");

            statement2.setString(1, customerIDTF.getText());
            statement2.setString(2, firstNameTF.getText());
            statement2.setString(3, lastNameTF.getText());
            statement2.setString(4, movieIDTF.getText());
            statement2.setString(5, movieTitleTF.getText());
            statement2.executeUpdate();

            // The block of code below displays a popup when the user successfully adds a credit card to the database.
            Alert addedMovie = new Alert(AlertType.INFORMATION, firstNameTF.getText() + "  has sucessfully rented "+movieIDTF.getText()+"!", ButtonType.OK);
            addedMovie.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            addedMovie.setHeaderText("Movie Rented!");
            addedMovie.setTitle("Rental");
            addedMovie.show();

            incrementNumOfRentals();
            decrementOnHand();
            clearRentalTextFields();
            refreshCustomerTable();
            refreshRentalTable();
            refreshInventoryTable();
            conn.close();

        }catch (SQLException e){
            e.printStackTrace();
            // This block of code below displays a error pop up for the user when a SQL error is thrown
            Alert alert = new Alert(AlertType.ERROR, "The movie you tried to enter was either: \nAlready entered into the database, \nor the MOVIE ID text field were left blank.", ButtonType.OK);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.setHeaderText("Failed to add Movie");
            alert.setTitle("Movie");
            alert.show();
        }

    }





    @FXML
    void btnReturnMovieClicked(ActionEvent event) {
        try {

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Rental Return");
            alert.setHeaderText("Return: " + firstNameTF.getText() + "'s rental of "+movieTitleTF.getText());
            alert.setContentText("Are you sure?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                Class.forName("com.mysql.jdbc.Driver");
                conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys?autoReconnect=true&useSSL=false", "root", "password");
                String query = "DELETE FROM RENTAL WHERE MOVIE_ID = ?";
                pst = conn.prepareStatement(query);

                pst.setString(1, movieIDTF.getText());

                pst.execute();

                decrementNumOfRentals();
                incrementOnHand();
                clearRentalTextFields();

                refreshCustomerTable();
                refreshRentalTable();
                refreshInventoryTable();
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
    void btnClearFieldsClicked(ActionEvent event) {
        customerIDTF.clear();
        firstNameTF.clear();
        lastNameTF.clear();
        movieIDTF.clear();
        movieTitleTF.clear();
        numRentalTF.clear();
    }

    void clearRentalTextFields(){
        customerIDTF.clear();
        firstNameTF.clear();
        lastNameTF.clear();
        movieIDTF.clear();
        movieTitleTF.clear();
    }

    @FXML
    void customerSearchFilter(KeyEvent event)
    {
        searchCustomerTF.textProperty().addListener((observableValue, oldValue, newValue) ->{
            filteredCustomerList.setPredicate((Predicate<? super RentalCustomer>) RentalCustomer->{
                if(newValue == null || newValue.isEmpty()){
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if(RentalCustomer.getCust_ID().contains(newValue)){
                    return true;
                }else if(RentalCustomer.getFirstName().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }else if(RentalCustomer.getLastName().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }
                return false;
            });
        });
        SortedList<RentalCustomer> sortedData = new SortedList<>(filteredCustomerList);
        sortedData.comparatorProperty().bind(customerTable.comparatorProperty());
        customerTable.setItems(sortedData);
    }

    @FXML
    void movieSearchFilter(KeyEvent event)
    {
        searchMovieTF.textProperty().addListener((observableValue, oldValue, newValue) ->{
            filteredMovieList.setPredicate((Predicate<? super Movie>) Movie->{
                if(newValue == null || newValue.isEmpty()){
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if(Movie.getMovie_ID().contains(newValue)){
                    return true;
                }else if(Movie.getMovie_Title().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }
                return false;
            });
        });
        SortedList<Movie> sortedData = new SortedList<>(filteredMovieList);
        sortedData.comparatorProperty().bind(movieTable.comparatorProperty());
        movieTable.setItems(sortedData);
    }

    @FXML
    void currentRentalFilter(KeyEvent event)
    {
        searchRentalTF.textProperty().addListener((observableValue, oldValue, newValue) ->{
            filteredRentalList.setPredicate((Predicate<? super Rental>) Rental->{
                if(newValue == null || newValue.isEmpty()){
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if(Rental.getCust_ID().contains(newValue)){
                    return true;
                }else if(Rental.getFirstName().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }else if(Rental.getLastName().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }
                return false;
            });
        });
        SortedList<Rental> sortedData = new SortedList<>(filteredRentalList);
        sortedData.comparatorProperty().bind(rentalTable.comparatorProperty());
        rentalTable.setItems(sortedData);
    }

    @FXML
    void btnPurchaseMovieClicked(ActionEvent event)
    {

    }

    @FXML
    void btnRefreshCustTableClicked(ActionEvent event)
    {
        customerList.clear();
        loadCustomerData();
    }

    @FXML
    void btnRefreshMovTableClicked(ActionEvent event)
    {
        movieList.clear();
        loadMovieData();
    }

    @FXML
    void btnRefreshRentalsClicked(ActionEvent event)
    {
        rentalList.clear();
        loadRentalData();
    }

    @FXML
    void btnBackClicked(ActionEvent event) throws IOException
    {
        returnToMainMenu(event);
    }

    @FXML
    void btnExitClicked(ActionEvent event){
        System.exit(0);
    }

    @FXML
    public void returnToMainMenu(ActionEvent event) throws IOException{
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/view/MainView.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadMovieData();
        initMovieColumn();
        loadCustomerData();
        initCustomerColumn();
        loadRentalData();
        initRentalColumn();

    }

    private void loadMovieData()
    {
        DatabaseHandler handler = DatabaseHandler.getInstance();
        String qu = "SELECT * FROM INVENTORY";
        ResultSet rs = handler.execQuery(qu);
        movieList.clear();

        try {
            while (rs.next()) {
                String id = rs.getString("MOVIE_ID");
                String title = rs.getString("MOVIE_TITLE");
                String rating = rs.getString("RATING");
                String genre = rs.getString("GENRE");
                String releaseDate = rs.getString("RELEASE_DATE");
                String rentalPrice = rs.getString("RENTAL_PRICE");
                String purchasePrice = rs.getString("PURCHASE_PRICE");
                Integer onHandQuant = rs.getInt("ON_HAND_QUANTITY");
                Integer totalQuant = rs.getInt("TOTAL_QUANTITY");

                movieList.add(new Movie(id, title, rating, genre, releaseDate, rentalPrice, purchasePrice, onHandQuant, totalQuant ));

            }
        } catch (SQLException ex) {
            Logger.getLogger(InventoryViewController.class.getName()).log(Level.SEVERE, null, ex);
        }

        movieTable.getItems().setAll(movieList);

    }

    public void initMovieColumn()
    {
        // Establishes customer table columns
        movieIDMovCol.setCellValueFactory(new PropertyValueFactory("Movie_ID"));
        titleMovCol.setCellValueFactory(new PropertyValueFactory("Movie_Title"));
        ratingMovCol.setCellValueFactory(new PropertyValueFactory("Rating"));
        genreCol.setCellValueFactory(new PropertyValueFactory("Genre"));
        relDateMovCol.setCellValueFactory(new PropertyValueFactory("Release_Date"));
        rentalPriceMovCol.setCellValueFactory(new PropertyValueFactory("Rental_Price"));
        purchPriceMovCol.setCellValueFactory(new PropertyValueFactory("Purchase_Price"));
        onHandMovCol.setCellValueFactory(new PropertyValueFactory("On_Hand_Quantity"));
        totalQantMovCol.setCellValueFactory(new PropertyValueFactory("Total_Quantity"));
    }

    private void loadCustomerData()
    {
        DatabaseHandler handler = DatabaseHandler.getInstance();
        String qu = "SELECT T1.CUSTOMER_ID, FIRST_NAME, LAST_NAME, NUMBER_OF_RENTALS, CARD_NUM, EXP_DATE FROM CUSTOMER_INFORMATION T1  LEFT JOIN CREDIT_CARD T2  ON T1.CUSTOMER_ID = T2.CUSTOMER_ID";

        ResultSet rs = handler.execQuery(qu);
        customerList.clear();

        try {
            while (rs.next()) {
                String id = rs.getString("CUSTOMER_ID");
                String firstName = rs.getString("FIRST_NAME");
                String lastName = rs.getString("LAST_NAME");
                Integer rentalNum = rs.getInt("NUMBER_OF_RENTALS");
                String ccNumber = rs.getString("CARD_NUM");
                String ccExpiration = rs.getString("EXP_DATE");

                customerList.add(new RentalCustomer(id, firstName, lastName, rentalNum, ccNumber, ccExpiration));

            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerViewController.class.getName()).log(Level.SEVERE, null, ex);
        }

        customerTable.getItems().setAll(customerList);

    }

    private void loadRentalData()
    {
        DatabaseHandler handler = DatabaseHandler.getInstance();
        String qu = "SELECT * FROM RENTAL";

        ResultSet rs = handler.execQuery(qu);
        rentalList.clear();

        try {
            while (rs.next()) {
                String custID = rs.getString("CUSTOMER_ID");
                String firstName = rs.getString("FIRST_NAME");
                String lastName = rs.getString("LAST_NAME");
                String movieID = rs.getString("MOVIE_ID");
                String title = rs.getString("MOVIE_TITLE");

                rentalList.add(new Rental(custID, firstName, lastName, movieID, title));

            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        rentalTable.getItems().setAll(rentalList);
    }

    public void initRentalColumn()
    {
        // Establishes customer table columns
        customIDRentCol.setCellValueFactory(new PropertyValueFactory("Cust_ID"));
        fNameRentCol.setCellValueFactory(new PropertyValueFactory("FirstName"));
        lNameRentCol.setCellValueFactory(new PropertyValueFactory("LastName"));
        movieIDRentCol.setCellValueFactory(new PropertyValueFactory("Movie_ID"));
        movieTitleRentCol.setCellValueFactory(new PropertyValueFactory("Movie_Title"));

    }

    public void initCustomerColumn()
    {
        // Establishes customer table columns
        customerIDCustCol.setCellValueFactory(new PropertyValueFactory("Cust_ID"));
        firstNameCustCol.setCellValueFactory(new PropertyValueFactory("FirstName"));
        lastNameCustCol.setCellValueFactory(new PropertyValueFactory("LastName"));
        numRentalsCustCol.setCellValueFactory(new PropertyValueFactory("NumRentals"));
        creditCardNumCustCol.setCellValueFactory(new PropertyValueFactory("CC_NUM"));
        expirationCustCol.setCellValueFactory(new PropertyValueFactory("CC_Exp"));

    }

    @FXML
    void handleMovieTableEvent(MouseEvent mouseEvent) throws ClassNotFoundException, SQLException
    {
        Class.forName("com.mysql.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys?autoReconnect=true&useSSL=false", "root", "password");
        String qu = "SELECT MOVIE_ID,MOVIE_TITLE,RATING,GENRE,RELEASE_DATE,RENTAL_PRICE,PURCHASE_PRICE, ON_HAND_QUANTITY, TOTAL_QUANTITY FROM INVENTORY WHERE MOVIE_ID = ?";
        ResultSet rs;
        pst = conn.prepareStatement(qu);
        Movie movie = movieTable.getSelectionModel().getSelectedItem();
        pst.setString(1, movie.getMovie_ID());
        rs = pst.executeQuery();

        while(rs.next()){
            movieIDTF.setText(rs.getString("MOVIE_ID"));
            movieTitleTF.setText(rs.getString("MOVIE_TITLE"));
        }

        pst.close();
        rs.close();
    }

    @FXML
    void handleCustomerTableEvent(MouseEvent mouseEvent) throws ClassNotFoundException, SQLException {


        Class.forName("com.mysql.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys?autoReconnect=true&useSSL=false", "root", "password");
        String query = "SELECT CUSTOMER_ID,FIRST_NAME,LAST_NAME,NUMBER_OF_RENTALS FROM CUSTOMER_INFORMATION WHERE CUSTOMER_ID = ?";
        ResultSet rs;
        pst = conn.prepareStatement(query);
        RentalCustomer customer = customerTable.getSelectionModel().getSelectedItem();
        pst.setString(1, customer.getCust_ID());

        rs = pst.executeQuery();

        while(rs.next()){
            customerIDTF.setText(rs.getString("CUSTOMER_ID"));
            firstNameTF.setText(rs.getString("FIRST_NAME"));
            lastNameTF.setText(rs.getString("LAST_NAME"));
           // numRentalTF.setText(Integer.toString(rs.getInt("NUMBER_OF_RENTALS")));
        }

        pst.close();
        rs.close();


    }

    @FXML
    void handleRentalTableEvent(MouseEvent mouseEvent) throws ClassNotFoundException, SQLException {


        Class.forName("com.mysql.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys?autoReconnect=true&useSSL=false", "root", "password");
        String query = "SELECT * FROM RENTAL WHERE MOVIE_ID = ?";
        ResultSet rs;
        pst = conn.prepareStatement(query);
        Rental rental = rentalTable.getSelectionModel().getSelectedItem();
        pst.setString(1, rental.getMovie_ID());

        rs = pst.executeQuery();

        while(rs.next()){
            customerIDTF.setText(rs.getString("CUSTOMER_ID"));
            firstNameTF.setText(rs.getString("FIRST_NAME"));
            lastNameTF.setText(rs.getString("LAST_NAME"));
            movieIDTF.setText(rs.getString("MOVIE_ID"));
            movieTitleTF.setText(rs.getString("MOVIE_TITLE"));
            //numRentalTF.setText(Integer.toString(rs.getInt("NUMBER_OF_RENTALS")));
        }

        pst.close();
        rs.close();


    }

    void incrementNumOfRentals()
    {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys?autoReconnect=true&useSSL=false", "root", "password");
            String query = "UPDATE CUSTOMER_INFORMATION SET NUMBER_OF_RENTALS = NUMBER_OF_RENTALS + 1 WHERE CUSTOMER_ID ='" + customerIDTF.getText() + "' ";
            pst = conn.prepareStatement(query);
            pst.execute();


        } catch (SQLException e){
            Alert alert = new Alert(AlertType.ERROR,"ERROR UPDATING NUMBER OF RENTALS!\n", ButtonType.OK);
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

    void decrementNumOfRentals()
    {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys?autoReconnect=true&useSSL=false", "root", "password");
            String query = "UPDATE CUSTOMER_INFORMATION SET NUMBER_OF_RENTALS = NUMBER_OF_RENTALS - 1 WHERE CUSTOMER_ID ='" + customerIDTF.getText() + "' ";
            pst = conn.prepareStatement(query);
            pst.execute();


        } catch (SQLException e){
            Alert alert = new Alert(AlertType.ERROR,"ERROR UPDATING NUMBER OF RENTALS!\n", ButtonType.OK);
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

    void incrementOnHand() throws ClassNotFoundException, SQLException
    {
        Class.forName("com.mysql.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys?autoReconnect=true&useSSL=false", "root", "password");
        String query = "UPDATE INVENTORY SET ON_HAND_QUANTITY = ON_HAND_QUANTITY + 1 WHERE MOVIE_ID ='" + movieIDTF.getText() + "' ";
        pst = conn.prepareStatement(query);
        pst.execute();
    }

    void decrementOnHand() throws ClassNotFoundException, SQLException
    {
        Class.forName("com.mysql.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys?autoReconnect=true&useSSL=false", "root", "password");
        String query = "UPDATE INVENTORY SET ON_HAND_QUANTITY = ON_HAND_QUANTITY - 1 WHERE MOVIE_ID ='" + movieIDTF.getText() + "' ";
        pst = conn.prepareStatement(query);
        pst.execute();
    }

    void updateNumberOfRentals()
    {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys?autoReconnect=true&useSSL=false", "root", "password");
            String query = "UPDATE CUSTOMER_INFORMATION SET NUMBER_OF_RENTALS = ? WHERE CUSTOMER_ID='" + customerIDTF.getText() + "'";
            pst = conn.prepareStatement(query);

            pst.setInt(1, Integer.parseInt(numRentalTF.getText()));



            pst.execute();


        } catch (SQLException e){
            Alert alert = new Alert(AlertType.ERROR,"Make sure you included an ID for the Movie!\n(It's required!)", ButtonType.OK);
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


    void refreshRentalTable()
    {
        rentalList.clear();
        loadRentalData();
    }

    void refreshCustomerTable()
    {
        customerList.clear();
        loadCustomerData();
    }

    void refreshInventoryTable()
    {
        movieList.clear();
        loadMovieData();
    }
}



