package controller;

import java.io.IOException;

import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class RentalViewController {

    @FXML
    private JFXButton btnBack;

    @FXML
    private JFXButton btnCreateNewCustomer;

    @FXML
    private JFXButton btnExit;

    @FXML
    private JFXButton btnRentMovie;
    
    @FXML
   	AnchorPane rootPane;

    @FXML
    void btnCreateNewCustomerclicked(ActionEvent event) {
    	// This should switch to the Customer view page
    }

    @FXML
    void btnRentMovieClicked(ActionEvent event) {
    	// This should switch to the search customer view page
    	// Then the either the Customer name or ID should be returned
    	// Next switch to the Search Inventory page
    	// Once a film has been selected the on hand should be updated
    }

    @FXML
    void btnBackClicked(ActionEvent event) throws IOException {
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

}

