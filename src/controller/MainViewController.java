package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;

//import application.JFXDrawer;
import javafx.animation.Animation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class MainViewController implements Initializable{
	
	@FXML
	AnchorPane rootPane;

	@FXML
	private JFXButton btnInventory;

	@FXML
	private JFXButton btnManagement;

	@FXML
	private JFXButton btnCustomer;

	@FXML
	private JFXButton btnRentals;

	@FXML
	private JFXButton btnExit;

	@FXML
	void btnCustomerClicked(ActionEvent event) throws IOException 
	{
		System.out.println("Customer Button Clicked");
		loadCustomerMenu(event);
	}

	@FXML
	void btnRentalsClicked(ActionEvent event) throws IOException {
		loadRentalMenu(event);
	}

	@FXML
	void btnInventoryClicked(ActionEvent event) throws IOException {
		loadInventoryMenu(event);
	}

	@FXML
	void btnManagement(ActionEvent event) throws IOException {
		loadManagerMenu(event);
	}

	@FXML
	void btnExitClicked(ActionEvent event) {
		 System.exit(0);
	}

	

	
//	@FXML
//	private VBox custMenu;
	  
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		
	}
	
	@FXML 
	private void loadCustomerMenu(ActionEvent event) throws IOException{
		AnchorPane pane = FXMLLoader.load(getClass().getResource("/view/CustomerView.fxml"));
		rootPane.getChildren().setAll(pane);
	}
	
	@FXML 
	private void loadRentalMenu(ActionEvent event) throws IOException{
		AnchorPane pane = FXMLLoader.load(getClass().getResource("/view/RentalView.fxml"));
		rootPane.getChildren().setAll(pane);
	}
	
	@FXML 
	private void loadInventoryMenu(ActionEvent event) throws IOException{
		AnchorPane pane = FXMLLoader.load(getClass().getResource("/view/InventoryView.fxml"));
		rootPane.getChildren().setAll(pane);
	}
	
	@FXML 
	private void loadManagerMenu(ActionEvent event) throws IOException{
		AnchorPane pane = FXMLLoader.load(getClass().getResource("/view/ManagerView.fxml"));
		rootPane.getChildren().setAll(pane);
	}
	
	
}
