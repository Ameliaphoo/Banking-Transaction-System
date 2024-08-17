package com.example.mybank.Controllers.Admin;

import com.example.mybank.Controllers.AlertMessage;
import com.example.mybank.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.w3c.dom.events.MouseEvent;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AccountCreate implements Initializable {

    @FXML
    public TextField create_userid;
    @FXML
    public TextField create_accountnum;
    @FXML
    public ComboBox<String> create_acctype;
    @FXML
    public TextField create_balance;
    @FXML
    public PasswordField create_pin;
    @FXML
    public PasswordField create_repin;
    @FXML
    public AnchorPane back_btn;
    @FXML
    public AnchorPane create_account_form;
    @FXML
    public Button create_account_btn;


    //Creating the account
    @FXML
    public void createAccount(ActionEvent e) {
        AlertMessage alert = new AlertMessage();

        if (create_userid.getText().isEmpty() ||
                create_accountnum.getText().isEmpty() ||
                create_acctype.getSelectionModel().getSelectedItem() == null ||
                create_balance.getText().isEmpty() ||
                create_pin.getText().isEmpty() ||
                create_repin.getText().isEmpty()) {
            alert.errorMessage("Please fill every field!!");
        } else if (!create_pin.getText().equals(create_repin.getText())) {
            // Check if passwords match
            alert.errorMessage("Pins do not match");
        } else if (create_pin.getText().length() < 6) {
            alert.errorMessage("Pin should be 6 digits");
        } else {
            Connection connectdb = null;
            PreparedStatement prepar = null;
            try {
                DatabaseConnection connection = new DatabaseConnection();
                connectdb = connection.getConnection();
                String insertData = "INSERT INTO accounts (user_id, account_number, account_type, balance, pin, status) " +
                        "VALUES (?, ?, ?, ?, ?, 'suspended')";
                prepar = connectdb.prepareStatement(insertData);
                prepar.setString(1, create_userid.getText());
                prepar.setString(2, create_accountnum.getText());
                prepar.setString(3, create_acctype.getSelectionModel().getSelectedItem());
                prepar.setString(4, create_balance.getText());
                prepar.setString(5, create_pin.getText());

                prepar.executeUpdate();
                alert.successMessage("Created Account Successfully!");
            } catch (SQLException ex) {
                ex.printStackTrace();
                alert.errorMessage("Error creating account. Please try again.");
            } finally {
                // Close resources
                if (prepar != null) {
                    try {
                        prepar.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
                if (connectdb != null) {
                    try {
                        connectdb.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }


    // For to choose the account type
    public String[] accountList = {"Deposit Account", "Saving Account"};

    public void accountType() {
        List<String> listAccount = new ArrayList<>();
        for (String data : accountList) {
            listAccount.add(data);
        }
        ObservableList<String> accountTypeList = FXCollections.observableArrayList(listAccount);
        create_acctype.setItems(accountTypeList);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        accountType();
    }

    public void back_to_Acc(javafx.scene.input.MouseEvent mouseEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/Admin/AccountManagement.fxml"));
            Parent accountManagementRoot = fxmlLoader.load();
            Stage stage = (Stage) create_account_form.getScene().getWindow();
            stage.getScene().setRoot(accountManagementRoot);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
