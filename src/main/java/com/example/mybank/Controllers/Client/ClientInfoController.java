package com.example.mybank.Controllers.Client;

import com.example.mybank.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ClientInfoController {

    @FXML
    private TextField client_name;

    @FXML
    private TextField client_email;

    @FXML
    private TextField client_recovery_question;

    @FXML
    private TextField client_answer;

    @FXML
    private TextField client_deposit_account;

    @FXML
    private TextField client_saving_account;

    @FXML
    private TextField client_phone;

    @FXML
    private TextField client_address;

    private int loggedInUserId;

    // Database connection
    DatabaseConnection connection = new DatabaseConnection();
    Connection connectdb = connection.getConnection();

    public void setLoggedInUserId(int userId) {
        this.loggedInUserId = userId;
        loadClientData();
    }

    public void initialize() {
        // This will be called by FXMLLoader when initialization is complete
    }

    private void loadClientData() {
        if (loggedInUserId == 0) {
            System.err.println("Logged-in user ID is not set.");
            return;
        }

        String userQuery = "SELECT * FROM users WHERE user_id = ?";
        String accountQuery = "SELECT * FROM accounts WHERE user_id = ?";

        try {
            // Fetch user information
            PreparedStatement userStatement = connectdb.prepareStatement(userQuery);
            userStatement.setInt(1, loggedInUserId);
            ResultSet userResult = userStatement.executeQuery();

            if (userResult.next()) {
                client_name.setText(userResult.getString("username"));
                client_email.setText(userResult.getString("email"));
                client_recovery_question.setText(userResult.getString("question"));
                client_answer.setText(userResult.getString("answer"));
                client_phone.setText(userResult.getString("phone"));
                client_address.setText(userResult.getString("address"));
            }

            // Fetch account information
            PreparedStatement accountStatement = connectdb.prepareStatement(accountQuery);
            accountStatement.setInt(1, loggedInUserId);
            ResultSet accountResult = accountStatement.executeQuery();

            while (accountResult.next()) {
                String accountType = accountResult.getString("account_type");
                String accountNumber = accountResult.getString("account_number");

                if (accountType.equals("Deposit Account")) {
                    client_deposit_account.setText(accountNumber);
                } else if (accountType.equals("Saving Account")) {
                    client_saving_account.setText(accountNumber);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


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
