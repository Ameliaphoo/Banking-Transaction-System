package com.example.mybank.Controllers;

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
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Forget1Controller implements Initializable {

    @FXML
    public AnchorPane forget1_form;
    @FXML
    public Button forgot_back_btn;
    @FXML
    public TextField forgot_email;
    @FXML
    public ComboBox<String> forgot_selectquestion;
    @FXML
    public TextField forgot_answer;
    @FXML
    public Button forgot_proceed_btn;

    @FXML
    public void proceed_btnOnAction(ActionEvent e) {
        AlertMessage alert = new AlertMessage();
        if (forgot_email.getText().isEmpty() ||
                forgot_selectquestion.getSelectionModel().getSelectedItem() == null ||
                forgot_answer.getText().isEmpty()) {
            alert.errorMessage("Please fill all the fields");
        } else {
            String checkData = "SELECT email, question, answer FROM users " +
                    "WHERE email = ? AND question = ? AND answer = ?";

            DatabaseConnection connection = new DatabaseConnection();
            Connection connectdb = connection.getConnection();
            ResultSet result = null;
            PreparedStatement prepare = null;

            try {
                prepare = connectdb.prepareStatement(checkData);
                prepare.setString(1, forgot_email.getText());
                prepare.setString(2, forgot_selectquestion.getSelectionModel().getSelectedItem());
                prepare.setString(3, forgot_answer.getText());
                result = prepare.executeQuery();

                // If information is correct
                if (result.next()) {
                    changePasswordForm();
                } else {
                    alert.errorMessage("Incorrect information");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                try {
                    if (result != null) result.close();
                    if (prepare != null) prepare.close();
                    if (connectdb != null) connectdb.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    // ForgotList Question
    public void forgotListQuestion() {
        RegisterController rc = new RegisterController();
        List<String> listQ = new ArrayList<>();

        for (String data : rc.questionList) {
            listQ.add(data);
        }
        ObservableList<String> listData = FXCollections.observableArrayList(listQ);
        forgot_selectquestion.setItems(listData);
    }

    // For Back Button
    public void loginForm(ActionEvent e) {
        if (e.getSource() == forgot_back_btn) {
            forget1_form.setVisible(false);
            back_btnOnAction();
        }
    }

    public void back_btnOnAction() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/login.fxml"));
            Parent registerRoot = fxmlLoader.load();
            Stage stage = (Stage) forget1_form.getScene().getWindow();
            stage.getScene().setRoot(registerRoot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Going to change Password Form
    public void changePasswordForm() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/Forgot.fxml"));
            Parent registerRoot = fxmlLoader.load();

            ForgetController fg=fxmlLoader.getController();
            fg.setUserEmail(forgot_email.getText());

            Stage stage = (Stage) forget1_form.getScene().getWindow();
            stage.getScene().setRoot(registerRoot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        forgotListQuestion();
    }
}
