package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.layout.VBox;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.util.Callback;

public class MainController implements Initializable {
	  @FXML
	  private Button btnConnect;
	  @FXML
	  private Label lblDB, lblUser;
	  @FXML
	  private VBox tableList;
	  @FXML
	  private TableView tableView;
	  private ObservableList<ObservableList> tableData;

	  private String dbName, dbUser;
	  private Connection connection;
	  private ResultSet rs;


	  public MainController() {}
	  
//	  public MainController(Connection connection, String dbName, String dbUser) {
//		  this.connection = connection;
//		  System.out.println("constructor: " + connection.toString());
//	  } 
	  

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO Auto-generated method stub
		Platform.runLater(() -> {
			System.out.println("initializing...");
			lblDB.setText(dbName);
			lblUser.setText(dbUser);
			  
			try {
				ResultSet tables = connection.getMetaData().getTables(connection.getCatalog(), null, "%", null);
				while (tables.next()) {
					System.out.println(tables.getString("table_name"));
					String t = tables.getString("table_name");
					Button btn = new Button(t);
					btn.setOnAction(e -> {
						try {
							loadTable(e);
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
					});
					tableList.getChildren().add(btn);
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}

		});
	}
	  
	public void preSetup(Connection connection, String db, String user) throws SQLException {
		this.connection = connection;
		this.dbName = db;
		this.dbUser = user;
	}

	public void changeViewToConnect(ActionEvent e) throws IOException {
		String destination = "Connect";
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/" + destination + ".fxml"));
		loader.setController(new ConnectController());

		// gets Stage information
		Stage stage = (Stage)(((Node) e.getSource()).getScene().getWindow());
	    
		Scene newScene = new Scene(loader.load());
		stage.setScene(newScene);
		stage.show();
	}
	  
	// popup attempt
	public void openConnectWindow(ActionEvent e) throws IOException {
		String destination = "Connect";
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/" + destination + ".fxml"));
		loader.setController(new ConnectController());

		// gets Stage information
//		((Node)btnConnect).getScene().getWindow().hide();
		Stage stage = (Stage)(((Node) e.getSource()).getScene().getWindow());

		Scene newScene = new Scene(loader.load());
//		Stage stage = new Stage();
		stage.setScene(newScene);
		stage.show();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void loadTable(ActionEvent e) throws SQLException {
		tableView.getColumns().clear();
		Statement statement = connection.createStatement();
		String tableName = ((Button)e.getTarget()).getText();
		ResultSet rs = statement.executeQuery("select * from " + tableName);
		ResultSetMetaData rsMetaData = rs.getMetaData();
		  
		// create columns, and create plan for filling rows
		for (int i = 0; i < rsMetaData.getColumnCount(); i++) {
			System.out.println(rsMetaData.getColumnName(i + 1));
			TableColumn col = new TableColumn(rsMetaData.getColumnName(i + 1));
			final int j = i;
			col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
				public ObservableValue<String> call(CellDataFeatures<ObservableList, String> arg0) {
					return new SimpleStringProperty(arg0.getValue().get(j).toString());
				}
			});
			tableView.getColumns().addAll(col);
		  }
		  
		// add SQL information to table, row by row in the while, col by col in the for
		tableData = FXCollections.observableArrayList();
		while (rs.next()) {
			ObservableList<String> row = FXCollections.observableArrayList();
			for (int i = 0; i < rsMetaData.getColumnCount(); i++) {
				row.add(rs.getString(i + 1));
			}
			System.out.println("row added");
			tableData.add(row);
		}
		tableView.setItems(tableData);
	} // end of #loadTable


} // end of class