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

	import javax.swing.table.TableColumn;
	import java.sql.*;

public class CustomerViewController implements Initializable {

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
		private TableColumn FirstName;

		@FXML
		private JFXButton btnCreateCustomer;

		@FXML
		AnchorPane rootPane;

		@FXML
		private JFXButton btnBack;

		@FXML
		private JFXTextArea displayTF;

	@FXML
	void btnBackClicked(ActionEvent event) throws IOException {
		returnToMainMenu(event);
	}
	
	@FXML 
	public void returnToMainMenu(ActionEvent event) throws IOException{
		AnchorPane pane = FXMLLoader.load(getClass().getResource("/view/MainView.fxml"));
		rootPane.getChildren().setAll(pane);
	}
	
	public void btnCreateCustomerClicked(ActionEvent event) throws SQLException, ClassNotFoundException{
		Connection conn = null;
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys", "root", "password");
		System.out.println("Successful Database Connection.");

		String cusID = cusIDTF.getText();
		String fName = fNameTF.getText();
		String lName = lNameTF.getText();
		String address = addressTF.getText();
		String apartmentNum = aptTF.getText();
		String city = cityTF.getText();
		String state = stateTF.getText();
		String zipCode = zipTF.getText();
		String email = emailTF.getText();
		String phoneNum = phoneNumTF.getText();


		PreparedStatement statement = conn.prepareStatement("INSERT INTO CUSTOMER_INFORMATION VALUES (?,?,?,?,?,?,?,?,?,?)");

		statement.setString(1, cusID);
		statement.setString(2, fName);
		statement.setString(3, lName);
		statement.setString(4, address);
		statement.setString(5, apartmentNum);
		statement.setString(6, city);
		statement.setString(7, state);
		statement.setString(8, zipCode);
		statement.setString(9, email);
		statement.setString(10, phoneNum);

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

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}
}


