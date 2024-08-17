package com.example.mybank.Controllers.Client;

import com.example.mybank.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class TransactionController implements Initializable {
    @FXML
    public TextField received_name;
    @FXML
    public Button go_backbtn;
    @FXML
    private TextField payee_address;
    @FXML
    private TextField amount;
    @FXML
    private ComboBox<String> bank;
    @FXML
    private TextField pin;
    @FXML
    private ComboBox<String> transfer_type;
    @FXML
    private ComboBox<String> myaccount;
    @FXML
    private Button transfer_btn;

    private int loggedInUserId;

    // Database connection
    DatabaseConnection connection = new DatabaseConnection();
    Connection connectdb = connection.getConnection();

    public void setLoggedInUserId(int userId) {
        this.loggedInUserId = userId;
        loadUserAccounts();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        transfer_btn.setOnAction(event -> handleTransfer());
        initializeTransferTypes();
        initializeBanks();
        loadUserAccounts();
    }

    private void loadUserAccounts() {
        String accountQuery = "SELECT account_number FROM accounts WHERE user_id = ? AND status = 'Active'";
        try {
            PreparedStatement accountStatement = connectdb.prepareStatement(accountQuery);
            accountStatement.setInt(1, loggedInUserId);
            ResultSet accountResult = accountStatement.executeQuery();

            while (accountResult.next()) {
                myaccount.getItems().add(accountResult.getString("account_number"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleTransfer() {
        try {
            String enteredPin = pin.getText();
            double transferAmount = Double.parseDouble(amount.getText());
            String receivedName= received_name.getText();
            String payeeAddress = payee_address.getText();
            String selectedAccountNumber = myaccount.getSelectionModel().getSelectedItem();
            String selectedBank = bank.getSelectionModel().getSelectedItem();
            String selectedTransferType = transfer_type.getSelectionModel().getSelectedItem();

            if (selectedAccountNumber == null) {
                showAlert("No Account Selected", "Please select an account to transfer from.", Alert.AlertType.ERROR);
                return;
            }

            if (selectedTransferType == null) {
                showAlert("No Transfer Type Selected", "Please select a transfer type.", Alert.AlertType.ERROR);
                return;
            }

            if (selectedBank == null) {
                showAlert("No Bank Selected", "Please select a bank.", Alert.AlertType.ERROR);
                return;
            }

            String pinQuery = "SELECT pin, balance, account_id FROM accounts WHERE account_number = ?";
            PreparedStatement pinStatement = connectdb.prepareStatement(pinQuery);
            pinStatement.setString(1, selectedAccountNumber);
            ResultSet pinResult = pinStatement.executeQuery();

            if (pinResult.next()) {
                String accountPin = pinResult.getString("pin");
                double accountBalance = pinResult.getDouble("balance");
                int accountId = pinResult.getInt("account_id");

                if (!enteredPin.equals(accountPin)) {
                    showAlert("Invalid PIN", "The PIN you entered is incorrect.", Alert.AlertType.ERROR);
                    return;
                }

                if (transferAmount > accountBalance) {
                    showAlert("Insufficient Funds", "You do not have enough balance to make this transfer.", Alert.AlertType.ERROR);
                    return;
                }

                // Deduct from user account
                String updateUserBalanceQuery = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
                PreparedStatement updateUserBalanceStatement = connectdb.prepareStatement(updateUserBalanceQuery);
                updateUserBalanceStatement.setDouble(1, transferAmount);
                updateUserBalanceStatement.setString(2, selectedAccountNumber);
                updateUserBalanceStatement.executeUpdate();

                // Add to payee account
                String updatePayeeBalanceQuery = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
                PreparedStatement updatePayeeBalanceStatement = connectdb.prepareStatement(updatePayeeBalanceQuery);
                updatePayeeBalanceStatement.setDouble(1, transferAmount);
                updatePayeeBalanceStatement.setString(2, payeeAddress);
                updatePayeeBalanceStatement.executeUpdate();

                // Add transaction record
                String addTransactionQuery = "INSERT INTO transactions (account_id, tran_type, amount, tran_date, payee_address, bank,received_name,status,Action) VALUES (?, ?, ?, ?, ?, ?,?,'success','transferred')";
                PreparedStatement addTransactionStatement = connectdb.prepareStatement(addTransactionQuery);
                addTransactionStatement.setInt(1, accountId);
                addTransactionStatement.setString(2, selectedTransferType);
                addTransactionStatement.setDouble(3, transferAmount);
                addTransactionStatement.setDate(4, java.sql.Date.valueOf(LocalDate.now()));
                addTransactionStatement.setString(5, payeeAddress);
                addTransactionStatement.setString(6, selectedBank);
                addTransactionStatement.setString(7,receivedName);
                addTransactionStatement.executeUpdate();

                showAlert("Transfer Successful", "The money has been successfully transferred.", Alert.AlertType.INFORMATION);


            } else {
                showAlert("Account Not Found", "The selected account does not exist.", Alert.AlertType.ERROR);
            }
            clearFields();;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public String[] transferTypeList = {"Internal Transfer", "External Transfer"};
    public String[] bankList = {"My Bank", "AYA Bank", "KBZ Bank", "CB Bank"};

    public void initializeTransferTypes() {
        transfer_type.getItems().addAll(transferTypeList);
    }

    public void initializeBanks() {
        bank.getItems().addAll(bankList);
    }

    private void clearFields() {
        received_name.clear();
        payee_address.clear();
        amount.clear();
        pin.clear();
        transfer_type.getSelectionModel().clearSelection();
        myaccount.getSelectionModel().clearSelection();
        bank.getSelectionModel().clearSelection();
    }

    @FXML
    private Button backButton;

    @FXML
    private void goBackToClientMenu(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/Client/ClientMenu.fxml"));
            Parent clientMenuRoot = fxmlLoader.load();

            ClientMenuController clientMenuController = fxmlLoader.getController();
            clientMenuController.setLoggedInUserId(loggedInUserId);  // Pass the user ID back to the ClientMenuController

            Scene scene = new Scene(clientMenuRoot);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
