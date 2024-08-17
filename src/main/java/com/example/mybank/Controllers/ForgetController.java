package com.example.mybank.Controllers;

import com.example.mybank.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;


public class ForgetController {
    @FXML
    private AnchorPane forgot_form;
    @FXML
    private PasswordField new_pass;
    @FXML
    private PasswordField confirm_pass;
    @FXML
    private Button proceed_btn;
    @FXML
    private Button back_btn;

    private String userEmail; // This will store the email from Forget1Controller

    // Setter method to set userEmail from Forget1Controller
    public void setUserEmail(String email) {
        this.userEmail = email;
    }

    public void changePassword(ActionEvent event) {
        AlertMessage alert = new AlertMessage();

        if (new_pass.getText().isEmpty() || confirm_pass.getText().isEmpty()) {
            alert.errorMessage("Please fill all the fields");

        } else if (!new_pass.getText().equals(confirm_pass.getText())) {
            alert.errorMessage("Passwords do not match");

        } else if (new_pass.getText().length() < 8) {
            alert.errorMessage("Password length should be at least 8 characters");
        } else {
            // Unique email is used to update the data
            String updateData = "UPDATE users SET password = ?, up_date = ? WHERE email = ?";

            // Connecting with database
            DatabaseConnection connection = new DatabaseConnection();
            Connection connectdb = connection.getConnection();

            try {
                PreparedStatement prepare = connectdb.prepareStatement(updateData);
                prepare.setString(1, new_pass.getText());
                java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());
                prepare.setDate(2, sqlDate);
                prepare.setString(3, userEmail); // Using the email from Forget1Controller

                int result = prepare.executeUpdate();
                if (result > 0) {
                    alert.successMessage("Password updated successfully.");
                    back_btnOnAction();
                } else {
                    alert.errorMessage("Failed to update password.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                alert.errorMessage("An error occurred: " + e.getMessage());
            } finally {
                try {
                    if (connectdb != null) connectdb.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    //Back to the login form after recovering the password
    public void back_btnOnAction() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/login.fxml"));
            Parent registerRoot = fxmlLoader.load();
            Stage stage = (Stage) forgot_form.getScene().getWindow();
            stage.getScene().setRoot(registerRoot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

