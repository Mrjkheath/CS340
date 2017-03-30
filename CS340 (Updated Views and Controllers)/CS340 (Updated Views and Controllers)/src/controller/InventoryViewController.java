package controller;

import java.io.IOException;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class InventoryViewController {

    @FXML
    private JFXTextField mActorTF;

    @FXML
    private JFXButton btnBack;

    @FXML
    private JFXButton btnSearch;

    @FXML
    private JFXButton btnEdit;

    @FXML
    private JFXTextField genreTF;

    @FXML
    private JFXTextField relaseYearTF;

    @FXML
    private JFXTextField quantOnHandTF;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXButton btnAdd;

    @FXML
    private JFXTextField sActorTF;

    @FXML
    private JFXTextField totalQuantTF;

    @FXML
    private JFXTextField directorTF;

    @FXML
    private JFXTextField titleTF;

    @FXML
    private JFXButton btnExit;
    
    @FXML
	AnchorPane rootPane;

    @FXML
    void btnSearchClicked(ActionEvent event) {
    	// Add search functionality 
    }

    @FXML
    void btnEditclicked(ActionEvent event) {
    	// Add search and replace functionality
    }

    @FXML
    void btnDeleteClicked(ActionEvent event) {
    	// Add search and remove functionality
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
    void btnAddclicked(ActionEvent event) {
    	// Should be able to add a new movie to the inventory
    }
    
    @FXML 
	public void returnToMainMenu(ActionEvent event) throws IOException{
		AnchorPane pane = FXMLLoader.load(getClass().getResource("/view/MainView.fxml"));
		rootPane.getChildren().setAll(pane);
	}

}

