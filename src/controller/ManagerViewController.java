package controller;

import java.io.IOException;
import java.net.URL;
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


import javax.swing.table.*;
import java.sql.*;

public class ManagerViewController implements Initializable {

    ObservableList<Employee> associateList = FXCollections.observableArrayList();
    ObservableList<Login> loginList = FXCollections.observableArrayList();
    FilteredList<Employee> filteredAssociateList = new FilteredList<Employee>(associateList);
    FilteredList<Login> filteredLoginList = new FilteredList<Login>(loginList);

    @FXML
    private JFXButton createLoginBtn;

    @FXML
    private JFXButton exitBtn;

    @FXML
    private JFXButton btnCreateEmployee;

    @FXML
    private JFXPasswordField passwordTF;

    @FXML
    private JFXButton updateEmployeeBtn;

    @FXML
    private JFXTextField hourlyRateTF;

    @FXML
    private JFXTextField ssnTF;

    @FXML
    private JFXButton deleteLoginBtn;

    @FXML
    private JFXTextField fNameTF;

    @FXML
    private JFXTextField lNameTF;


    @FXML
    private TableView<Employee> associateTable;

    @FXML
    private TableColumn<Employee, String> associateIDCol;

    @FXML
    private TableColumn<Employee, Double> hourlyRateCol;

    @FXML
    private TableColumn<Employee, String> associateStrtDateCol;

    @FXML
    private JFXButton clearEmployeeTF;

    @FXML
    private TableColumn<String, Employee> fNameCol;



    @FXML
    private TableView<Login> loginTable;

    @FXML
    private TableColumn<String, Login> associateLoginIDCol;

    @FXML
    private TableColumn<String, Login> passwordCol;



    @FXML
    private TableColumn<Employee, String> ssnCol;

    @FXML
    private TableColumn<Employee, String> pNumCol;

    @FXML
    private TableColumn<Employee, String> associateBDayCol;

    @FXML
    private JFXButton btnBack;

    @FXML
    private JFXRadioButton managerButton;

    @FXML
    private JFXTextField associateTableFilter;

    @FXML
    private DatePicker empStrtDatePicker;

    @FXML
    private JFXButton deleteEmployeeBtn;

    @FXML
    private JFXButton updateLoginBtn;

    @FXML
    private JFXTextField loginIDTF;

    @FXML
    private JFXButton clearLoginTF;

    @FXML
    private JFXButton refreshAssociateTable;

    @FXML
    private JFXButton refreshLoginTable;

    @FXML
    private TableColumn<String, Employee> lNameCol;

    @FXML
    private JFXTextField phoneNumTF;

    @FXML
    private JFXTextField loginTableFilter;

    @FXML
    private JFXTextField empIDTF;

    @FXML
    private DatePicker dobDatePicker;

    @FXML
    private AnchorPane rootPane;

    private Connection conn;

    private ResultSet rs;

    private PreparedStatement pst;

    public void initColumn()
    {

        // Establishes associate table columns
        associateIDCol.setCellValueFactory(new PropertyValueFactory("Associate_ID"));
        fNameCol.setCellValueFactory(new PropertyValueFactory("First_Name"));
        lNameCol.setCellValueFactory(new PropertyValueFactory("Last_Name"));
        associateStrtDateCol.setCellValueFactory(new PropertyValueFactory("Associate_Start_Date"));
        associateBDayCol.setCellValueFactory(new PropertyValueFactory("Associate_Birthday"));
        ssnCol.setCellValueFactory(new PropertyValueFactory("Social_Security_Number"));
        pNumCol.setCellValueFactory(new PropertyValueFactory("Phone_Number"));
        hourlyRateCol.setCellValueFactory(new PropertyValueFactory("Hourly_Rate"));


        // Establishes login table column
        associateLoginIDCol.setCellValueFactory(new PropertyValueFactory("Associate_ID"));
        passwordCol.setCellValueFactory(new PropertyValueFactory("Password"));

    }

    // This method implements the filter table feature for the Associate table.
    // It allows the user to filter the table by associate id, first name, or last name.
    @FXML
    void associateFilterKeySearch(KeyEvent keyEvent)
    {
        associateTableFilter.textProperty().addListener((observableValue, oldValue, newValue) ->{
            filteredAssociateList.setPredicate((Predicate<? super Employee>) Employee->{
                if(newValue == null || newValue.isEmpty())
                {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if(Employee.getAssociate_ID().contains(newValue))
                {
                    return true;
                }
                else if(Employee.getFirst_Name().toLowerCase().contains(lowerCaseFilter))
                {
                    return true;
                }
                else if(Employee.getLast_Name().toLowerCase().contains(lowerCaseFilter))
                {
                    return true;
                }
                return false;
            });
        });
        SortedList<Employee> sortedData = new SortedList<>(filteredAssociateList);
        sortedData.comparatorProperty().bind(associateTable.comparatorProperty());
        associateTable.setItems(sortedData);
    }

    // This method implements the filter table feature for the login table.
    // It allows the user to filter the table by associate id, or Password.
    @FXML
    void loginFilterKeySearch(KeyEvent keyEvent)
    {
        loginTableFilter.textProperty().addListener((observableValue, oldValue, newValue) ->{
            filteredLoginList.setPredicate((Predicate<? super Login>) Login->{
                if(newValue == null || newValue.isEmpty())
                {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if(Login.getAssociate_ID().contains(newValue))
                {
                    return true;
                }
                else if(Login.getPassword().toLowerCase().contains(lowerCaseFilter))
                {
                    return true;
                }
                return false;
            });
        });
        SortedList<Login> sortedData = new SortedList<>(filteredLoginList);
        sortedData.comparatorProperty().bind(loginTable.comparatorProperty());
        loginTable.setItems(sortedData);
    }


    @FXML
    void btnDeleteLoginClicked(ActionEvent event)
    {
        try {

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Employee");
            alert.setHeaderText("Delete: " + loginIDTF.getText() +" from the system.");
            alert.setContentText("Are you sure?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                Class.forName("com.mysql.jdbc.Driver");
                conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys?autoReconnect=true&useSSL=false", "root", "password");
                String query = "DELETE FROM LOGIN WHERE ASSOCIATE_ID = ?";
                pst = conn.prepareStatement(query);

                pst.setString(1, loginIDTF.getText());

                pst.execute();

                clearLoginInfoForm();
                loginList.clear();
                loadLoginData();

            } else {
                // ... user chose CANCEL or closed the dialog
            }

        } catch (SQLException e){
            Alert alert = new Alert(AlertType.ERROR,"Make sure you included an ID for the Associate Username!\n(It's required!)", ButtonType.OK);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.setHeaderText("Employee Login");
            alert.show();
        } catch (ClassNotFoundException ex){
            Alert alert = new Alert(AlertType.ERROR);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.setHeaderText("Could Not Connect to Database!");
            alert.show();
        }
    }

    @FXML
    void btnCreateLoginClicked(ActionEvent event) throws ClassNotFoundException
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys?autoReconnect=true&useSSL=false", "root", "password");

            PreparedStatement statement2 = conn.prepareStatement("INSERT INTO LOGIN VALUES (?,?)");

            statement2.setString(1, loginIDTF.getText());
            statement2.setString(2, passwordTF.getText());

            statement2.executeUpdate();

            loginList.clear();
            loadLoginData();

            // The block of code below displays a popup when the user successfully adds a credit card to the database.
            Alert addedLogin = new Alert(AlertType.INFORMATION, loginIDTF.getText() + "'s Login has been added to the system!", ButtonType.OK);
            addedLogin.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            addedLogin.setHeaderText("Login Created!");
            addedLogin.setTitle("Login Information");
            addedLogin.show();

            conn.close();
        } catch (SQLException e) {
            // This block of code below displays a error pop up for the user when a SQL error is thrown
            Alert alert = new Alert(AlertType.ERROR, "The login you tried to enter was either: \nAlready entered into the database, \nor the Associate ID text field were left blank.", ButtonType.OK);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.setHeaderText("Failed to add Login");
            alert.setTitle("Login Information");
            alert.show();
        }
    }

    @FXML
    void btnUpdateLoginClicked(ActionEvent event)
    {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys?autoReconnect=true&useSSL=false", "root", "password");
            String query = "UPDATE LOGIN SET ASSOCIATE_ID = ?, PASSWORD = ? WHERE ASSOCIATE_ID='" + loginIDTF.getText() + "' ";
            pst = conn.prepareStatement(query);

            pst.setString(1, loginIDTF.getText());
            pst.setString(2, fNameTF.getText());

            Alert alert = new Alert(AlertType.INFORMATION, "The associate number: " + loginIDTF.getText() +" has been updated!", ButtonType.OK);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.setHeaderText("Associate Login Updated!");
            alert.setTitle("Associate");
            alert.show();

            pst.execute();

            loginList.clear();
            loadLoginData();

        } catch (SQLException e){
            Alert alert = new Alert(AlertType.ERROR,"Make sure you included an ID for the Associate!\n(It's required!)", ButtonType.OK);
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
    void btnDeleteEmployeeClicked(ActionEvent event)
    {
        try {

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Employee");
            alert.setHeaderText("Delete: " + fNameTF.getText() + " " + lNameTF.getText() + " from the system.");
            alert.setContentText("Are you sure?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                Class.forName("com.mysql.jdbc.Driver");
                conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys?autoReconnect=true&useSSL=false", "root", "password");
                String query = "DELETE FROM SALES_ASSOCIATE WHERE ASSOCIATE_ID = ?";
                pst = conn.prepareStatement(query);

                pst.setString(1, empIDTF.getText());

                pst.execute();

                clearEmployeeInfoForm();

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
    void btnUpdateEmployeeClicked(ActionEvent event)
    {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys?autoReconnect=true&useSSL=false", "root", "password");
            String query = "UPDATE SALES_ASSOCIATE SET ASSOCIATE_ID = ?, FIRST_NAME = ?, LAST_NAME = ?, ASSOCIATE_START_DATE = ?, ASSOCIATE_BIRTHDAY = ?, SOCIAL_SECURITY_NUMBER = ?, PHONE_NUMBER = ?, HOURLY_RATE = ? WHERE ASSOCIATE_ID='" + empIDTF.getText() + "' ";
            pst = conn.prepareStatement(query);

            pst.setString(1, empIDTF.getText());
            pst.setString(2, fNameTF.getText());
            pst.setString(3, lNameTF.getText());
            pst.setString(4, ((TextField)empStrtDatePicker.getEditor()).getText());
            pst.setString(5, ((TextField)dobDatePicker.getEditor()).getText());
            pst.setString(6, ssnTF.getText());
            pst.setString(7, phoneNumTF.getText());
            pst.setFloat(8, Float.parseFloat(hourlyRateTF.getText()));


            Alert alert = new Alert(AlertType.INFORMATION, "The customer: " + fNameTF.getText() + " " + lNameTF.getText() + " has been updated!", ButtonType.OK);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.setHeaderText("Customer Updated!");
            alert.setTitle("Customer");
            alert.show();

            pst.execute();

            associateList.clear();
            loadAssociateData();

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
    void btnCreateEmployeeClicked(ActionEvent event) throws ClassNotFoundException
    {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys?autoReconnect=true&useSSL=false", "root", "password");
            System.out.println("Successful Database Connection.");


            PreparedStatement statement = conn.prepareStatement("INSERT INTO SALES_ASSOCIATE VALUES (?,?,?,?,?,?,?,?)");

            statement.setString(1, empIDTF.getText());
            statement.setString(2, fNameTF.getText());
            statement.setString(3, lNameTF.getText());
            statement.setString(4, ((TextField) empStrtDatePicker.getEditor()).getText());
            statement.setString(5, ((TextField) dobDatePicker.getEditor()).getText());
            statement.setString(6, ssnTF.getText());
            statement.setString(7, phoneNumTF.getText());
            statement.setFloat(8, Float.parseFloat(hourlyRateTF.getText()));


            statement.executeUpdate();


            Alert addedCustomer = new Alert(AlertType.INFORMATION, fNameTF.getText() + " has been added to the system!", ButtonType.OK);
            addedCustomer.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            addedCustomer.setHeaderText("Associate Added!");
            addedCustomer.setTitle("Associate");
            addedCustomer.show();

            associateList.clear();
            clearEmployeeInfoForm();
            loadAssociateData();

        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR, "The assocaite you tried to enter was either: \nAlready entered into the database, \nor one or more fields were left blank.", ButtonType.OK);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.setHeaderText("Failed to add Associate");
            alert.setTitle("Associate");
            alert.show();
        }
    }

    @FXML
    void btnBackClicked(ActionEvent event) throws IOException
    {
        returnToMainMenu(event);
    }

    @FXML
    void btnRefreshAssociateTable(ActionEvent event)
    {
        associateList.clear();
        loadAssociateData();
    }

    @FXML
    void btnRefreshLoginTable(ActionEvent event)
    {
        loginList.clear();
        loadLoginData();
    }

    @FXML
    void clearLoginInfoForm()
    {
        loginIDTF.clear();
        passwordTF.clear();
    }

    @FXML
    void clearEmployeeInfoForm()
    {
        empIDTF.clear();
        fNameTF.clear();
        lNameTF.clear();
        empStrtDatePicker.getEditor().clear();
        dobDatePicker.getEditor().clear();
        phoneNumTF.clear();
        ssnTF.clear();
        phoneNumTF.clear();
        hourlyRateTF.clear();

    }

    @FXML
    void handleLoginTableClick(MouseEvent event) throws SQLException, ClassNotFoundException
    {
        Class.forName("com.mysql.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys?autoReconnect=true&useSSL=false", "root", "password");
        String query = "SELECT * FROM LOGIN WHERE ASSOCIATE_ID = ?";
        ResultSet rs;
        pst = conn.prepareStatement(query);
        Login login = loginTable.getSelectionModel().getSelectedItem();
        pst.setString(1, login.getAssociate_ID());

        rs = pst.executeQuery();

        while(rs.next())
        {
            loginIDTF.setText(rs.getString("ASSOCIATE_ID"));
            passwordTF.setText(rs.getString("PASSWORD"));
        }

        pst.close();
        rs.close();

    }

    @FXML
    void handleEmployeeTableClick(MouseEvent event) throws SQLException, ClassNotFoundException
    {
        Class.forName("com.mysql.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys?autoReconnect=true&useSSL=false", "root", "password");
        String query = "SELECT * FROM SALES_ASSOCIATE WHERE ASSOCIATE_ID = ?";
        ResultSet rs;
        pst = conn.prepareStatement(query);
        Employee employee = associateTable.getSelectionModel().getSelectedItem();
        pst.setString(1, employee.getAssociate_ID());


        rs = pst.executeQuery();

        while(rs.next()){
            empIDTF.setText(rs.getString("ASSOCIATE_ID"));
            fNameTF.setText(rs.getString("FIRST_NAME"));
            lNameTF.setText(rs.getString("LAST_NAME"));
            ((TextField)empStrtDatePicker.getEditor()).setText(rs.getString("ASSOCIATE_START_DATE"));
            ((TextField)dobDatePicker.getEditor()).setText(rs.getString("ASSOCIATE_BIRTHDAY"));
            ssnTF.setText(rs.getString("SOCIAL_SECURITY_NUMBER"));
            phoneNumTF.setText(rs.getString("PHONE_NUMBER"));
            hourlyRateTF.setText(Float.toString(rs.getFloat("HOURLY_RATE")));
        }

        pst.close();
        rs.close();

    }

    // This method just loads the customer information from the database into the Customer Table.
    private void loadAssociateData()
    {
        DatabaseHandler handler = DatabaseHandler.getInstance();
        String qu = "SELECT * FROM SALES_ASSOCIATE";
        ResultSet rs = handler.execQuery(qu);
        associateList.clear();

        try {
            while (rs.next()) {
                String id = rs.getString("ASSOCIATE_ID");
                String firstName = rs.getString("FIRST_NAME");
                String lastName = rs.getString("LAST_NAME");
                String associateStartDate = rs.getString("ASSOCIATE_START_DATE");
                String associateBirthday = rs.getString("ASSOCIATE_BIRTHDAY");
                String socialSecurityNumber = rs.getString("SOCIAL_SECURITY_NUMBER");
                String phoneNumber = rs.getString("PHONE_NUMBER");
                Float hourlyRate = rs.getFloat("HOURLY_RATE");

                associateList.add(new Employee(id, firstName, lastName, associateStartDate, associateBirthday, socialSecurityNumber, phoneNumber, hourlyRate));

            }
        } catch (SQLException ex) {
            Logger.getLogger(ManagerViewController.class.getName()).log(Level.SEVERE, null, ex);
        }

        associateTable.getItems().setAll(associateList);

    }

    // This method just loads the login information from the database into the login table.
    private void loadLoginData()
    {
        DatabaseHandler handler = DatabaseHandler.getInstance();
        String qu = "SELECT * FROM LOGIN";
        ResultSet rs = handler.execQuery(qu);
        loginList.clear();

        try {
            while (rs.next()) {
                String id = rs.getString("ASSOCIATE_ID");
                String password = rs.getString("PASSWORD");

                loginList.add(new Login(id, password));

            }
        } catch (SQLException ex) {
            Logger.getLogger(ManagerViewController.class.getName()).log(Level.SEVERE, null, ex);
        }

        loginTable.getItems().setAll(loginList);


    }

    @FXML
    void btnExitClicked(ActionEvent event) {
    	 System.exit(0);
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
        loadAssociateData();
        loadLoginData();

        dobDatePicker.setValue(LocalDate.now().minusYears(18));
        final Callback<DatePicker, DateCell> dayCellFactory =
                new Callback<DatePicker, DateCell>() {
                    @Override
                    public DateCell call(final DatePicker datePicker) {
                        return new DateCell() {
                            @Override
                            public void updateItem(LocalDate item, boolean empty) {
                                super.updateItem(item, empty);


                                if (item.isBefore(LocalDate.now()) && item.isAfter(LocalDate.now().minusYears(18)))
                                {
                                    setDisable(true);
                                    setStyle("-fx-background-color: #ffc0cb;");
                                }
								/*
								else if (item.isAfter(dobDatePicker.getValue().minusYears(18)))
								{
									setDisable(true);
									setStyle("-fx-background-color: #ffc0cb");
								}
								*/
                            }
                        };
                    }
                };
        dobDatePicker.setDayCellFactory(dayCellFactory);
    }

}

