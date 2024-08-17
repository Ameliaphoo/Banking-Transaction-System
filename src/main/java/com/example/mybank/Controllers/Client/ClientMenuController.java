package com.example.mybank.Controllers.Client;

import com.example.mybank.Controllers.Admin.Transaction;
import com.example.mybank.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ClientMenuController implements Initializable {

    @FXML
    public Button client_transaction_btn, client_accounts_btn, client_profile_btn, client_logout_btn;

    @FXML
    public Label userNameLabel, depositAccountBalanceLabel, depositAccountNumberLabel, savingAccountNumberLabel, savingAccountBalanceLabel, incomeLabel, outcomeLabel, todayDateLabel, accountsLabel, noAccountsLabel, summaryLabel, transactionLabel;

    @FXML
    public TableView<Transaction> user_transaction_table;

    @FXML
    public TableColumn<Transaction, String> user_account_number, user_action, user_payee_address;

    @FXML
    public TableColumn<Transaction, Double> user_amount;

    @FXML
    public AnchorPane client_menu_page, depositAccountPane, savingAccountPane, account_information_box, myaccount_info;

    private int loggedInUserId;
    private ObservableList<Transaction> transactionList= FXCollections.observableArrayList();

    // Database connection
    DatabaseConnection connection = new DatabaseConnection();
    Connection connectdb = connection.getConnection();

    public void setLoggedInUserId(int userId) {
        this.loggedInUserId = userId;
        loadAccountData();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        todayDateLabel.setText("Today: " + today.format(formatter));

        // Initialize table columns
        user_account_number.setCellValueFactory(new PropertyValueFactory<>("account_id"));
        user_amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        user_action.setCellValueFactory(new PropertyValueFactory<>("action"));
        user_payee_address.setCellValueFactory(new PropertyValueFactory<>("payee_address"));

        // Load initial data
        loadAccountData();



    }

    private void loadAccountData() {
        if (loggedInUserId == 0) {
            System.err.println("Logged-in user ID is not set.");
            return;
        }

        String accountQuery = "SELECT * FROM accounts WHERE user_id = ?";
        String transactionQuery = "SELECT * FROM transactions WHERE account_id = ?";

        try {
            PreparedStatement accountStatement = connectdb.prepareStatement(accountQuery);
            accountStatement.setInt(1, loggedInUserId);
            ResultSet accountResult = accountStatement.executeQuery();


            double income = 0.0;
            double outcome = 0.0;
            boolean hasAccounts = false;

            // Initially hide account panes
            depositAccountPane.setVisible(false);
            savingAccountPane.setVisible(false);

            while (accountResult.next()) {
                hasAccounts = true;
                int accountId = accountResult.getInt("account_id");
                String accountNumber = accountResult.getString("account_number");
                String accountType = accountResult.getString("account_type");
                double balance = accountResult.getDouble("balance");

                if (accountType.equals("Deposit Account")) {
                    depositAccountPane.setVisible(true);
                    depositAccountBalanceLabel.setText("Ks" + balance);
                    depositAccountNumberLabel.setText(accountNumber);
                }
                if (accountType.equals("Saving Account")) {
                    savingAccountPane.setVisible(true);
                    savingAccountBalanceLabel.setText("Ks" + balance);
                    savingAccountNumberLabel.setText(accountNumber);
                }

                // Now load transactions for this account
                PreparedStatement transactionStatement = connectdb.prepareStatement(transactionQuery);
                transactionStatement.setInt(1, accountId);
                ResultSet transactionResult = transactionStatement.executeQuery();

                while (transactionResult.next()) {
                    String transactionType = transactionResult.getString("action");
                    double amount = transactionResult.getDouble("amount");

                    if (transactionType.equals("Transferred")) {
                        outcome += amount;
                    }
                    if (transactionType.equals("Received")) {
                        income += amount;
                    }
                    // Create a Transaction object and add it to the TableView
                    Transaction transaction = new Transaction(
                            transactionResult.getInt("tran_id"),
                            accountId,
                            transactionResult.getString("tran_type"),
                            String.valueOf(amount),
                            transactionResult.getDate("tran_date").toLocalDate(),
                            transactionResult.getString("bank"),
                            transactionResult.getString("payee_address"),
                            transactionResult.getString("received_name"),
                            transactionResult.getString("status"),
                            transactionType
                    );
                    user_transaction_table.getItems().add(transaction);
                }
            }

            // Display income and outcome
            incomeLabel.setText("+ " + income + " kyats");
            outcomeLabel.setText("- " + outcome + " kyats");

            if (!hasAccounts) {
                accountsLabel.setVisible(false);
                noAccountsLabel.setVisible(true);
                summaryLabel.setVisible(false);
                account_information_box.setVisible(false);
                transactionLabel.setVisible(false);
                user_transaction_table.setVisible(false);
            } else {
                accountsLabel.setVisible(true);
                noAccountsLabel.setVisible(false);
                summaryLabel.setVisible(true);
                account_information_box.setVisible(true);
                transactionLabel.setVisible(true);
                user_transaction_table.setVisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void switchTransaction(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/Client/Transaction.fxml"));
            Parent transactionRoot = fxmlLoader.load();

            TransactionController transactionController = fxmlLoader.getController();
            transactionController.setLoggedInUserId(loggedInUserId);

            Stage stage = (Stage) client_menu_page.getScene().getWindow();
            stage.getScene().setRoot(transactionRoot);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void switchProfile(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/Client/ClientInfo.fxml"));
            Parent profileRoot = fxmlLoader.load();

            ClientInfoController clientInfoController = fxmlLoader.getController();
            clientInfoController.setLoggedInUserId(loggedInUserId);

            Stage stage = (Stage) client_menu_page.getScene().getWindow();
            stage.getScene().setRoot(profileRoot);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void switchLogOut(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/login.fxml"));
            Parent loginRoot = fxmlLoader.load();
            Stage stage = (Stage) client_menu_page.getScene().getWindow();
            stage.getScene().setRoot(loginRoot);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
