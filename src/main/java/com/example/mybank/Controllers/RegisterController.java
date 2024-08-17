package com.example.mybank.Controllers;

import com.example.mybank.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {
    @FXML
    public AnchorPane register_form;
    @FXML
    public Hyperlink register_login;
    @FXML
    public PasswordField register_confirms;
    @FXML
    public TextField register_ph;
    @FXML
    public TextField register_address;
    @FXML
    public DatePicker register_birthday;
    @FXML
    private Button register_btn;
    @FXML
    private TextField register_email;
    @FXML
    private PasswordField register_password;
    @FXML
    private TextField register_username;
    @FXML
    private ComboBox<String> register_selectquestion;
    @FXML
    private TextField register_answer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        questions();
        configureDatePicker();
    }

    private void configureDatePicker() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        register_birthday.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                return (date != null) ? dateFormatter.format(date) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                try {
                    return (string != null && !string.isEmpty()) ? LocalDate.parse(string, dateFormatter) : null;
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid date format");
                    return null;
                }
            }
        });
    }

    public void register_btnOnAction(ActionEvent e) {
        AlertMessage alert = new AlertMessage();

        // Creating Database connection
        DatabaseConnection connection = new DatabaseConnection();
        Connection connectdb = connection.getConnection();

        // Check if there are empty fields
        if (register_email.getText().isEmpty() ||
                register_username.getText().isEmpty() ||
                register_password.getText().isEmpty() ||
                register_confirms.getText().isEmpty() ||
                register_selectquestion.getSelectionModel().getSelectedItem() == null ||
                register_answer.getText().isEmpty() ||
                register_ph.getText().isEmpty() ||
                register_address.getText().isEmpty() ||
                register_birthday.getValue() == null) {

            alert.errorMessage("All fields are required to be filled!!");
        } else if (!register_password.getText().equals(register_confirms.getText())) {
            // Check if passwords match
            alert.errorMessage("Passwords do not match");
        } else if (register_password.getText().length() < 8) {
            // Check if the password length is at least 8
            alert.errorMessage("Password length should be at least 8 characters");
        } else {
            // Check if the email is already in use
            String checkEmail = "SELECT * FROM users WHERE email='" + register_email.getText() + "'";

            try {
                Statement statement = connectdb.createStatement();
                ResultSet result = statement.executeQuery(checkEmail);

                if (result.next()) {
                    alert.errorMessage("Email is already in use!");
                } else {
                    String insertData = "INSERT INTO users (email, username, password, question, answer, date, up_date, user_type, phone, address, birthday) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, 'user', ?, ?, ?)";
                    PreparedStatement prepar = connectdb.prepareStatement(insertData);
                    prepar.setString(1, register_email.getText());
                    prepar.setString(2, register_username.getText());
                    prepar.setString(3, register_password.getText());
                    prepar.setString(4, register_selectquestion.getSelectionModel().getSelectedItem());
                    prepar.setString(5, register_answer.getText());

                    // Correct date conversion
                    java.sql.Date sqlDate = new java.sql.Date(new Date().getTime());
                    prepar.setDate(6, sqlDate);
                    prepar.setString(7, sqlDate.toString());
                    prepar.setString(8, register_ph.getText());
                    prepar.setString(9, register_address.getText());

                    // Handling Date Picker for Birthday
                    LocalDate dob = register_birthday.getValue();
                    java.sql.Date dobsqlDate = java.sql.Date.valueOf(dob);
                    prepar.setDate(10, dobsqlDate);

                    prepar.executeUpdate();
                    alert.successMessage("Registered Successfully!");

                    registerAfter();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    // Clearing the field after the action of registration
    public void registerAfter() {
        register_email.setText("");
        register_username.setText("");
        register_password.setText("");
        register_confirms.setText("");
        register_answer.setText("");
        register_selectquestion.getSelectionModel().selectFirst();
        register_address.setText("");
        register_ph.setText("");
        register_birthday.setValue(null);
    }

    // Going Back to Login Form from hyperlink
    public void showLoginForm(ActionEvent e) {
        if (e.getSource() == register_login) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/login.fxml"));
                Parent registerRoot = fxmlLoader.load();
                Stage stage = (Stage) register_form.getScene().getWindow();
                stage.getScene().setRoot(registerRoot);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    // Selecting questions field
    public String[] questionList = {
            "What is your favorite food?",
            "What is your favorite color?",
            "What is your school?",
            "What is the name of your pet?",
            "What is your favourite sport?"
    };

    public void questions() {
        List<String> listQ = new ArrayList<>();
        for (String data : questionList) {
            listQ.add(data);
        }
        ObservableList<String> listData = FXCollections.observableArrayList(listQ);
        register_selectquestion.setItems(listData);
    }
}
