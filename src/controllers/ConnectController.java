package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ConnectController implements Initializable {
	  @FXML
	  private TextField tfURL, tfUser, tfPassword;
	  @FXML
	  private Label lblDB, lblUser, lblConnectResult;
	  @FXML
	  private Button btnConnect, btnContinue;
	  private Connection connection;
	  private String db, dbUser, dbPass;

	  // @FXML
	  // private URL location;
	  // @FXML
	  // private ResourceBundle resources;

	  public ConnectController() {}
	  
	  @Override
	  public void initialize(URL url, ResourceBundle rb) {
		  btnContinue.setVisible(false);
	  }

	  @FXML
	  private void initializeDB() {
		  try {
			  // Load the JDBC driver
			  Class.forName("com.mysql.cj.jdbc.Driver");
			  System.out.println("Driver loaded");
			  
			  // Establish a connection
			  this.db = tfURL.getText();
			  String address = "jdbc:mysql://localhost/" + db;
			  this.dbUser = tfUser.getText();
			  this.dbPass = tfPassword.getText();
			  
			  connection = DriverManager.getConnection(address, dbUser, dbPass);
			  
			  // lblDB.setText(address);
			  // lblUser.setText(user);
			  
			  lblConnectResult.setText("Database connected.");
			  btnConnect.setVisible(false);
			  btnContinue.setVisible(true);
			  System.out.println("Database connected: " + connection);
		  }
		  catch (SQLSyntaxErrorException ex) {
			  lblConnectResult.setText("ERR: No such database found. Please try again.");
		  } catch (SQLException ex) {
			  lblConnectResult.setText("ERR: Wrong username and/or password. Please try again.");
		  } catch (Exception ex) {
			  System.out.println(ex.getMessage());
		  }
	  }

	  public void changeViewToMain(ActionEvent e) throws Exception {

	    // Node node = (Node) e.getSource();
	    // Stage stage = (Stage) node.getScene().getWindow();

	    String destination = "Main";
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/" + destination + ".fxml"));
	    Stage stage = (Stage)(((Node) e.getSource()).getScene().getWindow());
	    
	    Scene newScene = new Scene(loader.load());
	    System.out.println(newScene);
	    System.out.println((MainController)loader.getController());
	    ((MainController) loader.getController()).preSetup(connection, db, dbUser);
	    stage.setScene(newScene);
	    stage.show();
	  }



	}
