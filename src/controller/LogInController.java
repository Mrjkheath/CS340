package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.DatabaseHandler;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;


import javax.swing.*;
import javax.swing.text.html.ImageView;
import java.sql.*;
import application.DatabaseHandler;

public class LogInController implements Initializable {

	 @FXML
	 private AnchorPane rootPane;
	
	 @FXML
	 private JFXTextField userTextField;

	 @FXML
	 private JFXPasswordField pwTextField;

	 @FXML
	 private JFXButton btnLogin;

	 @FXML
	 private JFXButton btnExit;

	 private PreparedStatement pst;

	 private Connection conn;

	 private ResultSet rs;
	
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		
	}

	// Lets the user enter the system upon a successful login attempt
	// By pressing the Login button.
	@FXML
	void btnLoginClicked(ActionEvent event) throws IOException, SQLException, ClassNotFoundException
	{
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys?autoReconnect=true&useSSL=false", "root", "password");
		String query = "SELECT * FROM LOGIN WHERE ASSOCIATE_ID=? AND PASSWORD=?";
		pst = conn.prepareStatement(query);
		pst.setString(1, userTextField.getText());
		pst.setString(2, pwTextField.getText());
		ResultSet rs = pst.executeQuery();

		// Associate ID must start with an "M" or else the user
		// will not receive access to the management features.
		if(rs.next()) {
			if (userTextField.getText().startsWith("M"))
				loadMainMenu(event);
			else
				loadMainEmployeeView(event);
		}
		else{
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Validate ID Number");
			alert.setHeaderText(null);
			alert.setContentText("Please Enter a Valid Employee ID and/or Password.");
			alert.showAndWait();
		}

	}

	// Lets the user press Enter to login instead of pressing the button
	// While the user is within the password text field.
	@FXML
	void OnEnterPressed(KeyEvent event) throws SQLException, ClassNotFoundException, IOException
	{
		if(event.getCode() == KeyCode.ENTER) {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys?autoReconnect=true&useSSL=false", "root", "password");
			String query = "SELECT * FROM LOGIN WHERE ASSOCIATE_ID=? AND PASSWORD=?";
			pst = conn.prepareStatement(query);
			pst.setString(1, userTextField.getText());
			pst.setString(2, pwTextField.getText());
			ResultSet rs = pst.executeQuery();

			// Associate ID must start with an "M" or else the user
			// will not receive access to the management features.
			if (rs.next()) {
				if (userTextField.getText().startsWith("M")) {
					AnchorPane pane = FXMLLoader.load(getClass().getResource("/view/MainView.fxml"));
					rootPane.getChildren().setAll(pane);
				} else {
					AnchorPane pane = FXMLLoader.load(getClass().getResource("/view/MainViewEmployee.fxml"));
					rootPane.getChildren().setAll(pane);
				}
			} else {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Validate ID Number");
				alert.setHeaderText(null);
				alert.setContentText("Please Enter a Valid Employee ID and/or Password.");
				alert.showAndWait();
			}
		}
	}


	// Lets the user press Enter to login instead of pressing the button
	// While the user is within the username text field.
	@FXML
	void OnEnterUsername(KeyEvent event) throws SQLException, ClassNotFoundException, IOException
	{
		if(event.getCode() == KeyCode.ENTER) {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys?autoReconnect=true&useSSL=false", "root", "password");
			String query = "SELECT * FROM LOGIN WHERE ASSOCIATE_ID=? AND PASSWORD=?";
			pst = conn.prepareStatement(query);
			pst.setString(1, userTextField.getText());
			pst.setString(2, pwTextField.getText());
			ResultSet rs = pst.executeQuery();

			// Associate ID must start with an "M" or else the user
			// will not receive access to the management features.
			if (rs.next()) {
				if (userTextField.getText().startsWith("M")) {
					AnchorPane pane = FXMLLoader.load(getClass().getResource("/view/MainView.fxml"));
					rootPane.getChildren().setAll(pane);
				} else {
					AnchorPane pane = FXMLLoader.load(getClass().getResource("/view/MainViewEmployee.fxml"));
					rootPane.getChildren().setAll(pane);
				}
			} else {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Validate ID Number");
				alert.setHeaderText(null);
				alert.setContentText("Please Enter a Valid Employee ID and/or Password.");
				alert.showAndWait();
			}
		}
	}


	@FXML
	void btnExitClicked(ActionEvent event) {
		 System.exit(0);
	}
	
	@FXML 
	private void loadMainMenu(ActionEvent event) throws IOException{
		AnchorPane pane = FXMLLoader.load(getClass().getResource("/view/MainView.fxml"));
		rootPane.getChildren().setAll(pane);
	}

	@FXML
	private void loadMainEmployeeView(ActionEvent event) throws IOException
	{
		AnchorPane pane = FXMLLoader.load(getClass().getResource("/view/MainViewEmployee.fxml"));
		rootPane.getChildren().setAll(pane);
	}

}
