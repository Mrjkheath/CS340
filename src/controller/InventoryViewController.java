package controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

import application.Movie;
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
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InventoryViewController implements Initializable{

    @FXML
    AnchorPane rootPane;

    // These are the lists that are used to display the information in the tables.
    ObservableList<Movie> list = FXCollections.observableArrayList();
    FilteredList<Movie> filteredInventoryList = new FilteredList<Movie>(list);

    // UI Buttons
    @FXML
    private JFXButton btnBack;

    @FXML
    private JFXButton btnRefresh;

    @FXML
    private JFXButton btnClear;

    @FXML
    private JFXButton btnUpdate;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXButton btnAdd;

    @FXML
    private JFXButton btnExit;

    // UI Text Fields
    @FXML
    private JFXTextField movie_ID;

    @FXML
    private JFXTextField titleTF;

    @FXML
    private JFXTextField mActorTF;

    @FXML
    private JFXTextField sActorTF;

    @FXML
    private JFXTextField directorTF;

    @FXML
    private JFXTextField genreTF;

    @FXML
    private JFXTextField ratingTF;

    @FXML
    private JFXTextField relaseYearTF;

    @FXML
    private JFXTextField rentPriceTF;

    @FXML
    private JFXTextField purchPriceTF;

    @FXML
    private JFXTextField totalQuantTF;

    @FXML
    private JFXTextField quantOnHandTF;

    // The text fields below is used for the tables filter features.
    @FXML
    private JFXTextField searchInventoryTF;

    // The Variables needed for creating the customer table

    @FXML
    TableView<Movie> movieTable;

    @FXML
    TableColumn<Movie, String> movieID;

    @FXML
    TableColumn<Movie, String> title;

    @FXML
    TableColumn<Movie, String> rating;

    @FXML
    TableColumn<Movie, String> mainActor;

    @FXML
    TableColumn<Movie, String> suppActor;

    @FXML
    TableColumn<Movie, String> director;

    @FXML
    TableColumn<Movie, String> genre;

    @FXML
    TableColumn<Movie, String> releaseDate;

    @FXML
    TableColumn<Movie, String> rentPrice;

    @FXML
    TableColumn<Movie, String> purchasePrice;

    @FXML
    TableColumn<Movie, Integer> onHandQuantity;

    @FXML
    TableColumn<Movie, Integer> totalQuantity;



    Connection conn = null;
    PreparedStatement pst = null;

    @FXML
    void btnRefreshClicked(ActionEvent event) {
        refreshInventoryTable();
    }

    @FXML
    void btnClearClicked(ActionEvent event) {
        clearInventoryTextFields();
    }


    @FXML
    void btnDeleteClicked(ActionEvent event) {

            try {

                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Movie");
                alert.setHeaderText("Delete: " + titleTF.getText() + " from the Inventory");
                alert.setContentText("Are you sure?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    Class.forName("com.mysql.jdbc.Driver");
                    conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys?autoReconnect=true&useSSL=false", "root", "password");
                    String query = "DELETE FROM INVENTORY WHERE MOVIE_ID = ?";
                    pst = conn.prepareStatement(query);

                    pst.setString(1, movie_ID.getText());

                    pst.execute();

                    clearInventoryTextFields();

                    refreshInventoryTable();
                } else {
                    // ... user chose CANCEL or closed the dialog
                }

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

    public void refreshInventoryTable() {
        list.clear();
        loadInventoryData();
    }


    @FXML
    void btnBackclicked(ActionEvent event) throws IOException {
        returnToMainMenu(event);
    }


    @FXML
    void btnExitclicked(ActionEvent event) {
        System.exit(0);
    }


    @FXML
    void btnAddclicked(ActionEvent event) throws SQLException, ClassNotFoundException {
        if (validateMovie_ID()) {
            String titleCheck = titleTF.getText();
            String directorCheck = directorTF.getText();
            if (titleCheck.equals(""))
            {

                Alert alert = new Alert(AlertType.ERROR, "Please enter the title of the movie.", ButtonType.OK);
                alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                alert.setHeaderText("Blank Text Fields");
                alert.setTitle("Movie");
                alert.show();
            }
            else if(directorCheck.equals(""))
            {
                Alert alert = new Alert(AlertType.ERROR, "Please enter the director of the movie.", ButtonType.OK);
                alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                alert.setHeaderText("Blank Text Fields");
                alert.setTitle("Movie");
                alert.show();
            } else {
                        try {

                            Class.forName("com.mysql.jdbc.Driver");
                            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys?autoReconnect=true&useSSL=false", "root", "password");
                            System.out.println("Successful Database Connection.");
                            PreparedStatement statement2 = conn.prepareStatement("INSERT INTO INVENTORY VALUES (?,?,?,?,?,?,?,?,?,?,?,?)");

                            statement2.setString(1, movie_ID.getText());
                            statement2.setString(2, titleTF.getText());
                            statement2.setString(3, ratingTF.getText());
                            statement2.setString(4, mActorTF.getText());
                            statement2.setString(5, sActorTF.getText());
                            statement2.setString(6, directorTF.getText());
                            statement2.setString(7, genreTF.getText());
                            statement2.setString(8, relaseYearTF.getText());
                            statement2.setString(9, rentPriceTF.getText());
                            statement2.setString(10, purchPriceTF.getText());
                            statement2.setInt(11, Integer.parseInt(quantOnHandTF.getText()));
                            statement2.setInt(12, Integer.parseInt(totalQuantTF.getText()));
                            statement2.executeUpdate();

                            // The block of code below displays a popup when the user successfully adds a credit card to the database.
                            Alert addedMovie = new Alert(AlertType.INFORMATION, titleTF.getText() + "  has been added to the system!", ButtonType.OK);
                            addedMovie.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                            addedMovie.setHeaderText("Movie Created!");
                            addedMovie.setTitle("Movie");
                            addedMovie.show();

                            loadInventoryData();
                            clearInventoryTextFields();
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
            }
        }


    @FXML
    public void returnToMainMenu(ActionEvent event) throws IOException
    {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/view/MainView.fxml"));
        rootPane.getChildren().setAll(pane);
    }



    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        initColumn();
        loadInventoryData(); // Loads inventory data into the customer table
    }


    // This method just loads the customer information from the database into the Customer Table.

    private void loadInventoryData()
    {
        DatabaseHandler handler = DatabaseHandler.getInstance();
        String qu = "SELECT * FROM INVENTORY";
        ResultSet rs = handler.execQuery(qu);
        list.clear();

        try {
            while (rs.next()) {
                String id = rs.getString("MOVIE_ID");
                String title = rs.getString("MOVIE_TITLE");
                String rating = rs.getString("RATING");
                String mainActor = rs.getString("MAIN_ACTOR");
                String suppActor = rs.getString("SUPPORTING_ACTOR");
                String director = rs.getString("DIRECTOR");
                String genre = rs.getString("GENRE");
                String releaseDate = rs.getString("RELEASE_DATE");
                String rentalPrice = rs.getString("RENTAL_PRICE");
                String purchasePrice = rs.getString("PURCHASE_PRICE");
                Integer onHandQuant = rs.getInt("ON_HAND_QUANTITY");
                Integer totalQuant = rs.getInt("TOTAL_QUANTITY");

                list.add(new Movie(id, title, rating, mainActor, suppActor, director, genre, releaseDate, rentalPrice, purchasePrice, onHandQuant, totalQuant));

            }
        } catch (SQLException ex) {
            Logger.getLogger(InventoryViewController.class.getName()).log(Level.SEVERE, null, ex);
        }

            movieTable.getItems().setAll(list);

    }

    // Clears the text fields for the inventory information form.

    public void clearInventoryTextFields()
    {
        movie_ID.clear();
        titleTF.clear();
        ratingTF.clear();
        mActorTF.clear();
        sActorTF.clear();
        directorTF.clear();
        genreTF.clear();
        relaseYearTF.clear();
        rentPriceTF.clear();
        purchPriceTF.clear();
        totalQuantTF.clear();
        quantOnHandTF.clear();

    }
    private boolean validateCustomerID(){
        Pattern p = Pattern.compile("[0-9]+");
        Matcher m = p.matcher(movie_ID.getText());
        if(m.find() && m.group().equals(movie_ID
                .getText())){
            return true;
        }else{
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Validate ID Number");
            alert.setHeaderText(null);
            alert.setContentText("Please Enter a Valid Customer ID \n(Only 5 digit numbers)");
            alert.showAndWait();

            return false;
        }
    }

    // This method establishes the columns for the tables.

    public void initColumn()
    {
        // Establishes customer table columns
        movieID.setCellValueFactory(new PropertyValueFactory("Movie_ID"));
        title.setCellValueFactory(new PropertyValueFactory("Movie_Title"));
        rating.setCellValueFactory(new PropertyValueFactory("Rating"));
        mainActor.setCellValueFactory(new PropertyValueFactory("Main_Actor"));
        suppActor.setCellValueFactory(new PropertyValueFactory("Supporting_Actor"));
        director.setCellValueFactory(new PropertyValueFactory("Director"));
        genre.setCellValueFactory(new PropertyValueFactory("Genre"));
        releaseDate.setCellValueFactory(new PropertyValueFactory("Release_Date"));
        rentPrice.setCellValueFactory(new PropertyValueFactory("Rental_Price"));
        purchasePrice.setCellValueFactory(new PropertyValueFactory("Purchase_Price"));
        onHandQuantity.setCellValueFactory(new PropertyValueFactory("On_Hand_Quantity"));
        totalQuantity.setCellValueFactory(new PropertyValueFactory("Total_Quantity"));
    }


    @FXML
    void movieFilterKeySearch(KeyEvent keyEvent)
    {
        searchInventoryTF.textProperty().addListener((observableValue, oldValue, newValue) ->{
            filteredInventoryList.setPredicate((Predicate<? super Movie>) Movie->{
                if(newValue == null || newValue.isEmpty()){
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if(Movie.getMovie_ID().contains(newValue)){
                    return true;
                }else if(Movie.getMovie_Title().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }else if(Movie.getMain_Actor().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }else if(Movie.getSupporting_Actor().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }else if(Movie.getDirector().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }else if(Movie.getGenre().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }else if(Movie.getMovie_Title().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }
                return false;
            });
        });
        SortedList<Movie> sortData = new SortedList<>(filteredInventoryList);
        sortData.comparatorProperty().bind(movieTable.comparatorProperty());
        movieTable.setItems(sortData);
    }

    @FXML
    void handleInventoryTableEvent(MouseEvent mouseEvent) throws ClassNotFoundException, SQLException
    {
        System.out.println("Mouse Event triggered!");
        Class.forName("com.mysql.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys?autoReconnect=true&useSSL=false", "root", "password");
        String query = "SELECT * FROM INVENTORY WHERE MOVIE_ID = ?";
        ResultSet rs;
        pst = conn.prepareStatement(query);
        Movie movie = movieTable.getSelectionModel().getSelectedItem();
        pst.setString(1, movie.getMovie_ID());
        System.out.println("Mouse Event Triggered");
        rs = pst.executeQuery();

        while(rs.next()){
            movie_ID.setText(rs.getString("MOVIE_ID"));
            titleTF.setText(rs.getString("MOVIE_TITLE"));
            ratingTF.setText(rs.getString("RATING"));
            mActorTF.setText(rs.getString("MAIN_ACTOR"));
            sActorTF.setText(rs.getString("SUPPORTING_ACTOR"));
            directorTF.setText(rs.getString("DIRECTOR"));
            genreTF.setText(rs.getString("GENRE"));
            relaseYearTF.setText(rs.getString("RELEASE_DATE"));
            rentPriceTF.setText(rs.getString("RENTAL_PRICE"));
            purchPriceTF.setText(rs.getString("PURCHASE_PRICE"));
            quantOnHandTF.setText(Integer.toString(rs.getInt("ON_HAND_QUANTITY")));
            totalQuantTF.setText(Integer.toString(rs.getInt("TOTAL_QUANTITY")));

        }

        pst.close();
        rs.close();
    }


    @FXML
    void btnUpdateMovieClicked(ActionEvent event)
    {
        String titleTest = titleTF.getText();
        if(titleTest.equals("")){
            Alert alert = new Alert(AlertType.ERROR, "Please select a Movie, from  \nthe table below, by clicking on \nthe row where the movie is found.", ButtonType.OK);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.setHeaderText("Blank Text Fields");
            alert.setTitle("Movie");
            alert.show();
        }else{
            try {
                Class.forName("com.mysql.jdbc.Driver");
                conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys?autoReconnect=true&useSSL=false", "root", "password");
                String query = "UPDATE INVENTORY SET MOVIE_ID = ?, MOVIE_TITLE = ?, RATING = ?, MAIN_ACTOR = ?, SUPPORTING_ACTOR = ?, DIRECTOR = ?, GENRE = ?, RELEASE_DATE = ?, RENTAL_PRICE = ? , PURCHASE_PRICE = ?, ON_HAND_QUANTITY = ?, TOTAL_QUANTITY = ? WHERE MOVIE_ID='" + movie_ID.getText() + "'";
                pst = conn.prepareStatement(query);

                pst.setString(1, movie_ID.getText());
                pst.setString(2, titleTF.getText());
                pst.setString(3, ratingTF.getText());
                pst.setString(4, mActorTF.getText());
                pst.setString(5, sActorTF.getText());
                pst.setString(6, directorTF.getText());
                pst.setString(7, genreTF.getText());
                pst.setString(8, relaseYearTF.getText());
                pst.setString(9, rentPriceTF.getText());
                pst.setString(10, purchPriceTF.getText());
                pst.setInt(11, Integer.parseInt(quantOnHandTF.getText()));
                pst.setInt(12, Integer.parseInt(totalQuantTF.getText()));

                Alert alert = new Alert(AlertType.INFORMATION, titleTF.getText() + "has been updated!", ButtonType.OK);
                alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                alert.setHeaderText("Movie Updated!");
                alert.setTitle("Movie");
                alert.show();

                pst.execute();
                clearInventoryTextFields();
                refreshInventoryTable();

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
    }

    private boolean validateMovie_ID(){
        Pattern p = Pattern.compile("[0-9]+");
        Matcher m = p.matcher(movie_ID.getText());
        if(m.find() && m.group().equals(movie_ID.getText())){
            return true;
        }else{
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Validate ID Number");
            alert.setHeaderText(null);
            alert.setContentText("Please Enter a Valid Movie ID \n(Only 5 digit numbers)");
            alert.showAndWait();

            return false;
        }
    }
}

