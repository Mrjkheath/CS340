package controller;

import java.io.IOException;

import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class ManagerViewController {

    @FXML
    private JFXButton btnBack;

    @FXML
    private JFXButton btnCreateEmp;

    @FXML
    private JFXButton btnExit;

    @FXML
    private JFXButton btnEditDelEmp;
    
    @FXML
   	AnchorPane rootPane;

    @FXML
    void btnCreateEmpClicked(ActionEvent event) {

    }

    @FXML
    void btnEditDelEmpClicked(ActionEvent event) {

    }

    @FXML
    void btnBackClicked(ActionEvent event) throws IOException {
    	returnToMainMenu(event);
    }

    @FXML
    void btnExitClicked(ActionEvent event) {
    	 System.exit(0);
    }
    
    @FXML 
  	public void returnToMainMenu(ActionEvent event) throws IOException{
  		AnchorPane pane = FXMLLoader.load(getClass().getResource("/view/MainView.fxml"));
  		rootPane.getChildren().setAll(pane);
  	}

}

