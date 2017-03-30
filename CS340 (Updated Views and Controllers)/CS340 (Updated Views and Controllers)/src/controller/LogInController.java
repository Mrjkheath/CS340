package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

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
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	
	@FXML
	void btnLoginClicked(ActionEvent event) throws IOException {
		String userName = userTextField.getText();
		String password = pwTextField.getText();
		
		if(userName.equals("") && password.equals(""))
			System.out.println("Welcome");
		else
			System.out.println("Wrong User Name and/or Password");
		
		loadMainMenu(event);
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

}
