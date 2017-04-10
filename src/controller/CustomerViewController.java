package controller;

	import java.io.IOException;
	import java.net.URL;
	import java.util.ResourceBundle;
	import java.util.Optional;
	import java.util.function.Predicate;


	import com.jfoenix.controls.JFXButton;
	import com.jfoenix.controls.JFXTextArea;
	import com.jfoenix.controls.JFXTextField;

	import javafx.scene.control.ButtonType;
	import javafx.scene.control.Alert;
	import javafx.scene.control.Alert.AlertType;


	import javafx.event.ActionEvent;
	import javafx.scene.input.MouseEvent;
	import javafx.scene.input.KeyEvent;
	import javafx.fxml.FXML;
	import javafx.fxml.FXMLLoader;
	import javafx.fxml.Initializable;
	import javafx.scene.layout.AnchorPane;
	import javafx.beans.property.SimpleStringProperty;
	import javafx.scene.control.TableColumn;
	import javafx.scene.control.TableView;
	import javafx.scene.control.cell.PropertyValueFactory;
	import javafx.scene.layout.Region;
	import javafx.collections.FXCollections;
	import javafx.collections.ObservableList;
	import javafx.collections.transformation.FilteredList;
	import javafx.collections.transformation.SortedList;
	import java.util.logging.Level;
	import java.util.logging.Logger;


	import application.DatabaseHandler;
	import application.Customer;
	import application.CreditCard;

	import java.sql.*;

public class CustomerViewController implements Initializable
{

		// These are the lists that are used to display the information in the tables.
		ObservableList<Customer> list = FXCollections.observableArrayList();
		ObservableList<CreditCard> creditList = FXCollections.observableArrayList();
		FilteredList<Customer> filteredCustomerList = new FilteredList<Customer>(list);
		FilteredList<CreditCard> filteredCreditCardList = new FilteredList<CreditCard>(creditList);

		@FXML
		AnchorPane rootPane;

		// The information for the Credit Card of the Customers

		@FXML
		private JFXTextField cusCCIDTF;

		@FXML
		private JFXTextField expTF;

		@FXML
		private JFXTextField NOCardTF;

		@FXML
		private JFXTextField ccNumTF;

		@FXML
		private JFXTextField typeTF;

		@FXML
		private JFXTextField securityTF;


		// The text fields for the Customer Information

		@FXML
		private JFXTextField cusIDTF;

	    @FXML
	    private JFXTextField addressTF;

	    @FXML
	    private JFXTextField stateTF;

	    @FXML
	    private JFXTextField lNameTF;

	    @FXML
	    private JFXTextField cityTF;

	    @FXML
	    private JFXTextField emailTF;

	    @FXML
	    private JFXTextField phoneNumTF;

	    @FXML
	    private JFXTextField zipTF;

	    @FXML
	    private JFXTextField fNameTF;

	    @FXML
	    private JFXTextField aptTF;


		// The Variables needed for creating the customer table

		@FXML
		TableView<Customer> customerTable;

		@FXML
		TableColumn<Customer, String> customerID;

		@FXML
		TableColumn<Customer, String> firstName;

		@FXML
		TableColumn<Customer, String> lastName;

		@FXML
		TableColumn<Customer, String> email;

		@FXML
		TableColumn<Customer, String> phoneNumber;

		@FXML
		TableColumn<Customer, String> streetAddress;

		@FXML
		TableColumn<Customer, String> apartment;

		@FXML
		TableColumn<Customer, String> city;

		@FXML
		TableColumn<Customer, String> state;

		@FXML
		TableColumn<Customer, String> zipCode;


		// Credit Card Table Info
		@FXML
		TableView<CreditCard> creditCardTable;

		@FXML
		TableColumn<CreditCard, String> ccCustomerID;

		@FXML
		TableColumn<CreditCard, String> nameOnCard;

		@FXML
		TableColumn<CreditCard, String> cardType;

		@FXML
		TableColumn<CreditCard, String> cardNumber;

		@FXML
		TableColumn<CreditCard, String> expDate;

		@FXML
		TableColumn<CreditCard, String> cvvNum;


		// The two text fields below are used for the tables filter features.
		@FXML
		private JFXTextField searchCustomer;

		@FXML
		private JFXTextField searchCreditCard;

		Connection conn = null;

		PreparedStatement pst = null;

	//This method returns the user to the main menu when they click the back button.
	@FXML
	void btnBackClicked(ActionEvent event) throws IOException
	{
		returnToMainMenu(event);
	}

	// This method closes the program when the user clicks it.
	@FXML
	void exitBtnClicked(ActionEvent event) throws IOException
	{
		System.exit(0);
	}

	@FXML
	public void returnToMainMenu(ActionEvent event) throws IOException
	{
		AnchorPane pane = FXMLLoader.load(getClass().getResource("/view/MainView.fxml"));
		rootPane.getChildren().setAll(pane);
	}

	// Creates a credit card in the credit card database upon the user clicking the Create New Credit Card button.
	@FXML
	void btnCreateCreditCardClicked(ActionEvent event) throws ClassNotFoundException
	{
		try {
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys?autoReconnect=true&useSSL=false", "root", "password");

				PreparedStatement statement2 = conn.prepareStatement("INSERT INTO CREDIT_CARD VALUES (?,?,?,?,?,?)");

				statement2.setString(1, cusCCIDTF.getText());
				statement2.setString(2, NOCardTF.getText());
				statement2.setString(3, typeTF.getText());
				statement2.setString(4, ccNumTF.getText());
				statement2.setString(5, expTF.getText());
				statement2.setString(6, securityTF.getText());

				statement2.executeUpdate();

				// The block of code below displays a popup when the user successfully adds a credit card to the database.
				Alert addedCustomer = new Alert(AlertType.INFORMATION, NOCardTF.getText() + "'s Credit Card has been added to the system!", ButtonType.OK);
				addedCustomer.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
				addedCustomer.setHeaderText("Credit Card Created!");
				addedCustomer.setTitle("Credit Card");
				addedCustomer.show();

				loadCreditCardData();
				clearCreditCardTextFields();

				conn.close();
			}catch (SQLException e){
				// This block of code below displays a error pop up for the user when a SQL error is thrown
				Alert alert = new Alert(AlertType.ERROR, "The credit card you tried to enter was either: \nAlready entered into the database, \nor the Customer ID text field were left blank.", ButtonType.OK);
				alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
				alert.setHeaderText("Failed to add Credit Card");
				alert.setTitle("Credit Card");
				alert.show();
			}
	}

	// Creates a customer for the customer database upon the user clicking the Create New Customer Button.
	public void btnCreateCustomerClicked(ActionEvent event) throws SQLException, ClassNotFoundException
	{

		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys?autoReconnect=true&useSSL=false", "root", "password");
			System.out.println("Successful Database Connection.");


			PreparedStatement statement = conn.prepareStatement("INSERT INTO CUSTOMER_INFORMATION VALUES (?,?,?,?,?,?,?,?,?,?)");

			statement.setString(1, cusIDTF.getText());
			statement.setString(2, fNameTF.getText());
			statement.setString(3, lNameTF.getText());
			statement.setString(4, emailTF.getText());
			statement.setString(5, phoneNumTF.getText());
			statement.setString(6, addressTF.getText());
			statement.setString(7, aptTF.getText());
			statement.setString(8, cityTF.getText());
			statement.setString(9, stateTF.getText());
			statement.setString(10, zipTF.getText());

			statement.executeUpdate();


			Alert addedCustomer = new Alert(AlertType.INFORMATION,  fNameTF.getText() + " has been added to the system!", ButtonType.OK);
			addedCustomer.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			addedCustomer.setHeaderText("Customer Added!");
			addedCustomer.setTitle("Customer");
			addedCustomer.show();

			list.clear();
			loadCustomerData();
			clearCustomerTextFields();

		}catch (SQLException e){
			Alert alert = new Alert(AlertType.ERROR, "The customer you tried to enter was either: \nAlready entered into the database, \nor one or more fields were left blank.", ButtonType.OK);
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.setHeaderText("Failed to add Customer");
			alert.setTitle("Customer");
			alert.show();
		}

	}

	/*
	 * Updates a selected Customers information and loads it
	 * into the database upon the user clicking the
	 * Update Customer Button.
	 */
	@FXML
	void btnUpdateCustomerClicked(ActionEvent event)
	{
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys?autoReconnect=true&useSSL=false", "root", "password");
			String query = "UPDATE CUSTOMER_INFORMATION SET CUSTOMER_ID = ?, FIRST_NAME = ?, LAST_NAME = ?, EMAIL = ?, PHONE_NUMBER = ?, STREET_ADDRESS = ?, APARTMENT = ?, CITY = ?, STATE = ?, ZIP_CODE = ? WHERE CUSTOMER_ID='" + cusIDTF.getText() + "' ";
			pst = conn.prepareStatement(query);

			pst.setString(1, cusIDTF.getText());
			pst.setString(2, fNameTF.getText());
			pst.setString(3, lNameTF.getText());
			pst.setString(4, emailTF.getText());
			pst.setString(5, phoneNumTF.getText());
			pst.setString(6, addressTF.getText());
			pst.setString(7, aptTF.getText());
			pst.setString(8, cityTF.getText());
			pst.setString(9, stateTF.getText());
			pst.setString(10, zipTF.getText());


			Alert alert = new Alert(AlertType.INFORMATION, "The customer: " + fNameTF.getText() + " " + lNameTF.getText() + " has been updated!", ButtonType.OK);
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.setHeaderText("Customer Updated!");
			alert.setTitle("Customer");
			alert.show();

			pst.execute();

			clearCustomerTextFields();

			refreshCustomerTable();

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

	/*
	 * Updates a selected Credit Card's information and
	 * loads it into the database upon the user clicking the
	 * Update Credit Card Button.
	 */
	@FXML
	void btnUpdateCreditCardClicked(ActionEvent event)
	{
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys?autoReconnect=true&useSSL=false", "root", "password");
			String query = "UPDATE CREDIT_CARD SET CUSTOMER_ID = ?, NAME_ON_CARD = ?, CARD_TYPE = ?, CARD_NUM = ?, EXP_DATE = ?, SECURITY_CODE = ? WHERE CUSTOMER_ID ='" + cusCCIDTF.getText() + "' ";
			pst = conn.prepareStatement(query);

			pst.setString(1, cusCCIDTF.getText());
			pst.setString(2, NOCardTF.getText());
			pst.setString(3, typeTF.getText());
			pst.setString(4, ccNumTF.getText());
			pst.setString(5, expTF.getText());
			pst.setString(6, securityTF.getText());


			Alert alert = new Alert(AlertType.INFORMATION, "The credit card belonging to: " + NOCardTF.getText() + " has been updated!", ButtonType.OK);
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.setHeaderText("Credit Card Updated!");
			alert.setTitle("Credit Card");
			alert.show();

			pst.execute();

			refreshCreditCardTable();

		} catch (SQLException e){
			Alert alert = new Alert(AlertType.ERROR,"Make sure you included an ID for the Credit Card!\n(It's required!)", ButtonType.OK);
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

	/*
	 * Deletes a selected Customer's information from the
	 * customer information table Upon the user clicking the
	 * Delete Customer Button and selecting OK on the
	 * popup dialog.
	 */
	@FXML
	void btnDeleteCustomerClicked(ActionEvent event)
	{
		try {

			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Customer");
			alert.setHeaderText("Delete: " + fNameTF.getText() + " " + lNameTF.getText());
			alert.setContentText("Are you sure?");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK){
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys?autoReconnect=true&useSSL=false", "root", "password");
				String query = "DELETE FROM CUSTOMER_INFORMATION WHERE CUSTOMER_ID = ?";
				pst = conn.prepareStatement(query);

				pst.setString(1, cusIDTF.getText());

				pst.execute();

				clearCustomerTextFields();

				refreshCustomerTable();
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

	/*
	 * Deletes a selected Credit Card's information from the
	 * credit card information table Upon the user clicking the
	 * Delete Credit Card Button and selecting OK on the
	 * popup dialog.
	 */
	@FXML
	void btnDeleteCreditCardClicked(ActionEvent event)
	{
		try {

			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Credit Card");
			alert.setHeaderText("Delete: " + NOCardTF.getText() + "'s Credit Card");
			alert.setContentText("Are you sure?");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK){
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys?autoReconnect=true&useSSL=false", "root", "password");
				String query = "DELETE FROM CREDIT_CARD WHERE CUSTOMER_ID = ?";
				pst = conn.prepareStatement(query);

				pst.setString(1, cusCCIDTF.getText());

				pst.execute();

				clearCreditCardTextFields();

				refreshCreditCardTable();
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

	// This method just loads the customer information from the database into the Customer Table.
	private void loadCustomerData()
	{
		DatabaseHandler handler = DatabaseHandler.getInstance();
		String qu = "SELECT * FROM CUSTOMER_INFORMATION";
		ResultSet rs = handler.execQuery(qu);
		list.clear();

		try {
			while (rs.next()) {
				String id = rs.getString("CUSTOMER_ID");
				String firstName = rs.getString("FIRST_NAME");
				String lastName = rs.getString("LAST_NAME");
				String email = rs.getString("EMAIL");
				String phoneNumber = rs.getString("PHONE_NUMBER");
				String streetAddress = rs.getString("STREET_ADDRESS");
				String APT = rs.getString("APARTMENT");
				String city = rs.getString("CITY");
				String state = rs.getString("STATE");
				String zipCode = rs.getString("ZIP_CODE");

				list.add(new Customer(id, firstName, lastName, email, phoneNumber, streetAddress, APT, city, state, zipCode));

			}
		} catch (SQLException ex) {
			Logger.getLogger(CustomerViewController.class.getName()).log(Level.SEVERE, null, ex);
		}

		customerTable.getItems().setAll(list);

	}

	// This method just loads the credit card information from the database into the credit card table.
	private void loadCreditCardData()
	{
		DatabaseHandler handler = DatabaseHandler.getInstance();
		String qu = "SELECT * FROM CREDIT_CARD";
		ResultSet rs = handler.execQuery(qu);
		creditList.clear();

		try {
			while (rs.next()) {
				String id = rs.getString("CUSTOMER_ID");
				String nameOnCreditCard = rs.getString("NAME_ON_CARD");
				String creditCardType = rs.getString("CARD_TYPE");
				String creditCardNumber = rs.getString("CARD_NUM");
				String expirationDate = rs.getString("EXP_DATE");
				String cVV = rs.getString("SECURITY_CODE");

				creditList.add(new CreditCard(id, nameOnCreditCard, creditCardType, creditCardNumber, expirationDate, cVV));

			}
		} catch (SQLException ex) {
			Logger.getLogger(CustomerViewController.class.getName()).log(Level.SEVERE, null, ex);
		}

		creditCardTable.getItems().setAll(creditList);


	}


	// This method controls what happens when the customer menu window is loading in the UI.
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		initColumn();
		loadCustomerData(); // Loads customer data into the customer table
		loadCreditCardData(); // Loads credit card data int the credit card table
	}

	// This method establishes the columns for the tables.
	public void initColumn()
	{

		// Establishes customer table columns
		customerID.setCellValueFactory(new PropertyValueFactory("Customer_ID"));
		firstName.setCellValueFactory(new PropertyValueFactory("First_Name"));
		lastName.setCellValueFactory(new PropertyValueFactory("Last_Name"));
		email.setCellValueFactory(new PropertyValueFactory("Email"));
		phoneNumber.setCellValueFactory(new PropertyValueFactory("Phone_Number"));
		streetAddress.setCellValueFactory(new PropertyValueFactory("Street_Address"));
		apartment.setCellValueFactory(new PropertyValueFactory("APT"));
		city.setCellValueFactory(new PropertyValueFactory("City"));
		state.setCellValueFactory(new PropertyValueFactory("State"));
		zipCode.setCellValueFactory(new PropertyValueFactory("Zip_Code"));

		// Establishes credit card table columns
		ccCustomerID.setCellValueFactory(new PropertyValueFactory("Customer_ID"));
		nameOnCard.setCellValueFactory(new PropertyValueFactory("Name_On_Card"));
		cardType.setCellValueFactory(new PropertyValueFactory("Card_Type"));
		cardNumber.setCellValueFactory(new PropertyValueFactory("Credit_Card_Number"));
		expDate.setCellValueFactory(new PropertyValueFactory("Expiration_Date"));
		cvvNum.setCellValueFactory(new PropertyValueFactory("Card_Verification_Value"));

	}


	/*
	 * This method below allows the user to select a specific row in the Customer table
	 * and have the information in that row appear in the Customer Information
	 * form text fields. The user can then edit those fields and update the specific
	 * customers information that they selected.
	 */

	@FXML
	void handleCustomerTableEvent(MouseEvent mouseEvent) throws ClassNotFoundException, SQLException {


		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys?autoReconnect=true&useSSL=false", "root", "password");
		String query = "SELECT * FROM CUSTOMER_INFORMATION WHERE CUSTOMER_ID = ?";
		ResultSet rs;
		pst = conn.prepareStatement(query);
		Customer customer = customerTable.getSelectionModel().getSelectedItem();
		pst.setString(1, customer.getCustomer_ID());

		rs = pst.executeQuery();

		while(rs.next()){
			cusIDTF.setText(rs.getString("CUSTOMER_ID"));
			fNameTF.setText(rs.getString("FIRST_NAME"));
			lNameTF.setText(rs.getString("LAST_NAME"));
			emailTF.setText(rs.getString("EMAIL"));
			phoneNumTF.setText(rs.getString("PHONE_NUMBER"));
			addressTF.setText(rs.getString("STREET_ADDRESS"));
			aptTF.setText(rs.getString("APARTMENT"));
			cityTF.setText(rs.getString("CITY"));
			stateTF.setText(rs.getString("STATE"));
			zipTF.setText(rs.getString("ZIP_CODE"));
		}

		pst.close();
		rs.close();


	}

	/*
	 * This method below allows the user to select a specific row in the Credit Card table
	 * and have the information in that row appear in the Credit Card Information
	 * form text fields. The user can then edit those fields and update the specific
	 * credit card information that they selected.
	 */
	@FXML
	void handleCreditCardEvent(MouseEvent mouseEvent) throws ClassNotFoundException, SQLException
	{

		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys?autoReconnect=true&useSSL=false", "root", "password");
		String query = "SELECT * FROM CREDIT_CARD WHERE CUSTOMER_ID = ?";
		ResultSet rs;
		pst = conn.prepareStatement(query);
		CreditCard creditCard = creditCardTable.getSelectionModel().getSelectedItem();
		pst.setString(1, creditCard.getCustomer_ID());

		rs = pst.executeQuery();

		while(rs.next())
		{
			cusCCIDTF.setText(rs.getString("CUSTOMER_ID"));
			NOCardTF.setText(rs.getString("NAME_ON_CARD"));
			typeTF.setText(rs.getString("CARD_TYPE"));
			ccNumTF.setText(rs.getString("CARD_NUM"));
			expTF.setText(rs.getString("EXP_DATE"));
			securityTF.setText(rs.getString("SECURITY_CODE"));
		}

		rs.close();

	}


	// This method implements the filter table feature for the customer table.
	// It allows the user to filter the table by customer id, first name, or last name.
	@FXML
	void customerFilterKeySearch(KeyEvent keyEvent)
	{
		searchCustomer.textProperty().addListener((observableValue, oldValue, newValue) ->{
			filteredCustomerList.setPredicate((Predicate<? super Customer>) Customer->{
				if(newValue == null || newValue.isEmpty()){
					return true;
				}
				String lowerCaseFilter = newValue.toLowerCase();
				if(Customer.getCustomer_ID().contains(newValue)){
					return true;
				}else if(Customer.getFirst_Name().toLowerCase().contains(lowerCaseFilter)){
					return true;
				}else if(Customer.getLast_Name().toLowerCase().contains(lowerCaseFilter)){
					return true;
				}
				return false;
			});
		});
		SortedList<Customer> sortedData = new SortedList<>(filteredCustomerList);
		sortedData.comparatorProperty().bind(customerTable.comparatorProperty());
		customerTable.setItems(sortedData);
	}

	// This method implements the filter table feature for the credit card table.
	// It allows the user to filter the table by customer id, or Name on the credit card.
	@FXML
	void creditCardFilterKeySearch(KeyEvent keyEvent)
	{
		searchCreditCard.textProperty().addListener((observableValue, oldValue, newValue) ->{
			filteredCreditCardList.setPredicate((Predicate<? super CreditCard>) CreditCard->{
				if(newValue == null || newValue.isEmpty()){
					return true;
				}
				String lowerCaseFilter = newValue.toLowerCase();
				if(CreditCard.getCustomer_ID().contains(newValue)){
					return true;
				}else if(CreditCard.getName_On_Card().toLowerCase().contains(lowerCaseFilter)){
					return true;
				}
				return false;
			});
		});
		SortedList<CreditCard> sortedData = new SortedList<>(filteredCreditCardList);
		sortedData.comparatorProperty().bind(creditCardTable.comparatorProperty());
		creditCardTable.setItems(sortedData);
	}


	// Clears the text fields for the customer information form.
	public void clearCustomerTextFields()
	{
		cusIDTF.clear();
		fNameTF.clear();
		lNameTF.clear();
		emailTF.clear();
		phoneNumTF.clear();
		addressTF.clear();
		aptTF.clear();
		cityTF.clear();
		stateTF.clear();
		zipTF.clear();
	}

	// Clears the text fields for the credit card information form.
	public void clearCreditCardTextFields()
	{
		cusCCIDTF.clear();
		NOCardTF.clear();
		NOCardTF.clear();
		typeTF.clear();
		ccNumTF.clear();
		expTF.clear();
		securityTF.clear();
	}

	// Implements the above clearCustomerTextFields method for a button.
	@FXML
	void clearCustomerInfoForm(ActionEvent event)
	{
		clearCustomerTextFields();
	}

	// Implements the above clearCreditCardTextFields method for a button.
	@FXML
	void clearCreditCardInfoForm(ActionEvent event)
	{
		clearCreditCardTextFields();
	}

	/*
	 * The below two methods give buttons the function
	 * to refresh the database tables for the Customer
	 * and Credit Card tables respectively.
	 */

	@FXML
	void btnRefreshCustomerTable(ActionEvent event)
	{
		list.clear();
		loadCustomerData();
	}

	@FXML
	void btnRefreshCreditCardTable(ActionEvent event)
	{
		creditList.clear();
		loadCreditCardData();
	}

	/*
	 * The below two methods refresh the tables.
	 * This gives the user a most up to date view
	 * of the information within the table.
	 */

	public void refreshCustomerTable()
	{
		list.clear();
		loadCustomerData();
	}

	public void refreshCreditCardTable()
	{
		creditList.clear();
		loadCreditCardData();
	}

}