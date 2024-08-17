package com.example.mybank.Controllers.Admin;

import com.example.mybank.Controllers.AlertMessage;
import com.example.mybank.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.layout.HBox;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;

public class TransactionManagement {
    @FXML
    public TableView<Transaction> transactionTable;
    @FXML
    public TableColumn<Transaction, Integer> id;
    @FXML
    public TableColumn<Transaction, Integer> account_id;
    @FXML
    public TableColumn<Transaction, String> tran_type;
    @FXML
    public TableColumn<Transaction, String> amount;
    @FXML
    public TableColumn<Transaction, LocalDate> tran_date;
    @FXML
    public TableColumn<Transaction, String> bank;

    @FXML
    public TableColumn<Transaction, String> received_name;
    @FXML
    public TableColumn<Transaction, String> status;
    @FXML
    public TableColumn<Transaction,String> action;
    @FXML
    public TableColumn<Transaction,String> payee_address;

    @FXML
    public Button searchButton;
    @FXML
    public TextField searchField;
    @FXML
    public AnchorPane transactionManagementPage;

    DatabaseConnection connection = new DatabaseConnection();
    Connection connectdb = connection.getConnection();
    ObservableList<Transaction> transactionList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        account_id.setCellValueFactory(new PropertyValueFactory<>("account_id"));
        tran_type.setCellValueFactory(new PropertyValueFactory<>("tran_type"));
        amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        tran_date.setCellValueFactory(new PropertyValueFactory<>("tran_date"));
        bank.setCellValueFactory(new PropertyValueFactory<>("bank"));
        payee_address.setCellValueFactory(new PropertyValueFactory<>("payee_address"));
        received_name.setCellValueFactory(new PropertyValueFactory<>("received_name"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
        action.setCellValueFactory(new PropertyValueFactory<>("action"));
        transactionTable.setItems(transactionList);
        loadTransactions();

    }

    public void loadTransactions() {
        transactionList.clear();
        String query = "SELECT * FROM transactions";
        try {
            Statement statement = connectdb.createStatement();
            ResultSet result = statement.executeQuery(query);

            while (result.next()) {
                transactionList.add(new Transaction(
                        result.getInt("tran_id"),
                        result.getInt("account_id"),
                        result.getString("tran_type"),
                        result.getString("amount"),
                        result.getDate("tran_date").toLocalDate(),
                        result.getString("bank"),
                        result.getString("payee_address"),
                        result.getString("received_name"),
                        result.getString("status"),
                        result.getString("Action")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Searching by Account Number from the transaction list
    public void searchTransaction(ActionEvent e) {
        String searchText = searchField.getText();
        if (searchText.isEmpty()) {
            loadTransactions();
        } else {
            transactionList.clear();
            String query = "SELECT * FROM transactions WHERE account_id LIKE ?";
            try {
                PreparedStatement prepare = connectdb.prepareStatement(query);
                prepare.setString(1, "%" + searchText + "%");
                ResultSet result = prepare.executeQuery();

                while (result.next()) {
                    transactionList.add(new Transaction(
                            result.getInt("tran_id"),
                            result.getInt("account_id"),
                            result.getString("tran_type"),
                            result.getString("amount"),
                            result.getDate("tran_date").toLocalDate(),
                            result.getString("bank"),
                            result.getString("payee_address"),
                            result.getString("received_name"),
                            result.getString("status"),
                            result.getString("action")
                    ));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    // Switching Form to Dashboard
    public void switchDashboard(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/Admin/AdminMenu.fxml"));
            Parent accountManagementRoot = fxmlLoader.load();
            Stage stage = (Stage) transactionManagementPage.getScene().getWindow();
            stage.getScene().setRoot(accountManagementRoot);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Switching Form to Users
    public void switchUsers(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/Admin/UserManagement.fxml"));
            Parent accountManagementRoot = fxmlLoader.load();
            Stage stage = (Stage) transactionManagementPage.getScene().getWindow();
            stage.getScene().setRoot(accountManagementRoot);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Switching Form to Accounts
    public void switchAccounts(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/Admin/AccountManagement.fxml"));
            Parent accountManagementRoot = fxmlLoader.load();
            Stage stage = (Stage) transactionManagementPage.getScene().getWindow();
            stage.getScene().setRoot(accountManagementRoot);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Switching Form to Transactions
    public void switchTransactions(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/Admin/TransactionManagement.fxml"));
            Parent accountManagementRoot = fxmlLoader.load();
            Stage stage = (Stage) transactionManagementPage.getScene().getWindow();
            stage.getScene().setRoot(accountManagementRoot);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Logging out
    public void switchLogin(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/login.fxml"));
            Parent accountManagementRoot = fxmlLoader.load();
            Stage stage = (Stage) transactionManagementPage.getScene().getWindow();
            stage.getScene().setRoot(accountManagementRoot);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

