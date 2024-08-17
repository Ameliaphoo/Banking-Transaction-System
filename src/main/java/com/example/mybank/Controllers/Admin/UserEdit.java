package com.example.mybank.Controllers.Admin;

import com.example.mybank.DatabaseConnection;
import com.example.mybank.Controllers.Admin.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class UserEdit {
    @FXML
    public Button cancel_btn;
    @FXML
    public Button edit_btn;
    @FXML
    public AnchorPane user_edit_form;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField addressField;
    @FXML
    private DatePicker birthdayField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField usertypeField;

    private User user;
    private DatabaseConnection connection = new DatabaseConnection();
    private Connection connectdb = connection.getConnection();

    public void setUser(User user) {
        this.user = user;
        usernameField.setText(user.getUserName());
        phoneField.setText(user.getPhone());
        emailField.setText(user.getEmail());
        addressField.setText(user.getAddress());
        birthdayField.setValue(user.getBirthday());
        passwordField.setText(user.getPassword());
        usertypeField.setText(user.getUsertype());
    }

    @FXML
    private void saveUser() {
        String query = "UPDATE users SET username=?, phone=?, email=?, address=?, birthday=?, password=?, user_type=? WHERE user_id=?";
        try (PreparedStatement prepare = connectdb.prepareStatement(query)) {
            prepare.setString(1, usernameField.getText());
            prepare.setString(2, phoneField.getText());
            prepare.setString(3, emailField.getText());
            prepare.setString(4, addressField.getText());
            prepare.setDate(5, java.sql.Date.valueOf(birthdayField.getValue()));
            prepare.setString(6, passwordField.getText());
            prepare.setString(7, usertypeField.getText());
            prepare.setInt(8, user.getUser_id());
            prepare.executeUpdate();

            // Close the form
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void cancel() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/Admin/UserManagement.fxml"));
            Parent accountManagementRoot = fxmlLoader.load();
            Stage stage = (Stage)user_edit_form.getScene().getWindow();
            stage.getScene().setRoot(accountManagementRoot);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

