package controller;

	import java.io.IOException;
	import java.net.URL;
	import java.util.ResourceBundle;

	import com.jfoenix.controls.JFXButton;
	import com.jfoenix.controls.JFXTextArea;
	import com.jfoenix.controls.JFXTextField;

	import javafx.event.ActionEvent;
	import javafx.fxml.FXML;
	import javafx.fxml.FXMLLoader;
	import javafx.fxml.Initializable;
	import javafx.scene.layout.AnchorPane;
	import javafx.beans.property.SimpleStringProperty;
	import javafx.scene.control.TableColumn;
	import javafx.scene.control.TableView;
	import javafx.scene.control.cell.PropertyValueFactory;
	import javafx.collections.FXCollections;
	import javafx.collections.ObservableList;
	import java.util.logging.Level;
	import java.util.logging.Logger;
	import application.DatabaseHandler;


	import java.sql.*;

public class CustomerViewController implements Initializable
{

		ObservableList<Customer> list = FXCollections.observableArrayList();

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

		@FXML
		private JFXButton btnCreateCustomer;

		@FXML
		AnchorPane rootPane;

		@FXML
		TableView <Customer> customerTable;

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

	@FXML
		private JFXButton btnBack;

		@FXML
		private JFXTextArea displayTF;

	@FXML
	void btnBackClicked(ActionEvent event) throws IOException
	{
		returnToMainMenu(event);
	}
	
	@FXML 
	public void returnToMainMenu(ActionEvent event) throws IOException
	{
		AnchorPane pane = FXMLLoader.load(getClass().getResource("/view/MainView.fxml"));
		rootPane.getChildren().setAll(pane);
	}
	
	public void btnCreateCustomerClicked(ActionEvent event) throws SQLException, ClassNotFoundException
	{
		Connection conn = null;
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys", "root", "password");
		System.out.println("Successful Database Connection.");

		String cusID = cusIDTF.getText();
		String fName = fNameTF.getText();
		String lName = lNameTF.getText();
		String email = emailTF.getText();
		String phoneNum = phoneNumTF.getText();
		String address = addressTF.getText();
		String apartmentNum = aptTF.getText();
		String city = cityTF.getText();
		String state = stateTF.getText();
		String zipCode = zipTF.getText();


		PreparedStatement statement = conn.prepareStatement("INSERT INTO CUSTOMER_INFORMATION VALUES (?,?,?,?,?,?,?,?,?,?)");

		statement.setString(1, cusID);
		statement.setString(2, fName);
		statement.setString(3, lName);
		statement.setString(4, email);
		statement.setString(5, phoneNum);
		statement.setString(6, address);
		statement.setString(7, apartmentNum);
		statement.setString(8, city);
		statement.setString(9, state);
		statement.setString(10, zipCode);

		statement.executeUpdate();


		String cusCCID = cusCCIDTF.getText();
		String nameOnCard = NOCardTF.getText();
		String cardType = typeTF.getText();
		String expirationDate = expTF.getText();
		String cardNumber = ccNumTF.getText();
		String securityCode = securityTF.getText();

		PreparedStatement statement2 = conn.prepareStatement("INSERT INTO CREDIT_CARD VALUES (?,?,?,?,?,?)");

		statement2.setString(1, cusCCID);
		statement2.setString(2, nameOnCard);
		statement2.setString(3, cardType);
		statement2.setString(4, expirationDate);
		statement2.setString(5, cardNumber);
		statement2.setString(6, securityCode);

		statement2.executeUpdate();


		conn.close();

		System.out.println("Customer has been added!");

		loadData();

	}

	public void initColumn()
	{

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



	}

	private void loadData()
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

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		initColumn();
		loadData();
	}

	public static class Customer
	{
		private final SimpleStringProperty Customer_ID;
		private final SimpleStringProperty First_Name;
		private final SimpleStringProperty Last_Name;
		private final SimpleStringProperty Email;
		private final SimpleStringProperty Phone_Number;
		private final SimpleStringProperty Street_Address;
		private final SimpleStringProperty APT;
		private final SimpleStringProperty City;
		private final SimpleStringProperty State;
		private final SimpleStringProperty Zip_Code;

		public Customer(String id, String firstName, String lastName, String email, String phoneNumber, String streetAddress, String APT, String city, String state, String zipCode)
		{
			this.Customer_ID = new SimpleStringProperty(id);
			this.First_Name = new SimpleStringProperty(firstName);
			this.Last_Name = new SimpleStringProperty(lastName);
			this.Email = new SimpleStringProperty(email);
			this.Phone_Number = new SimpleStringProperty(phoneNumber);
			this.Street_Address = new SimpleStringProperty(streetAddress);
			this.APT = new SimpleStringProperty(APT);
			this.City = new SimpleStringProperty(city);
			this.State = new SimpleStringProperty(state);
			this.Zip_Code = new SimpleStringProperty(zipCode);
		}


		public String getCustomer_ID() {
			return Customer_ID.get();
		}

		public String getFirst_Name() {
			return First_Name.get();
		}

		public String getLast_Name() {
			return Last_Name.get();
		}

		public String getEmail() {
			return Email.get();
		}

		public String getPhone_Number() {
			return Phone_Number.get();
		}

		public String getStreet_Address() {
			return Street_Address.get();
		}

		public String getAPT() {
			return APT.get();
		}

		public String getCity() {
			return City.get();
		}

		public String getState(){
			return State.get();
		}

		public String getZip_Code() {
			return Zip_Code.get();
		}
	}

}


