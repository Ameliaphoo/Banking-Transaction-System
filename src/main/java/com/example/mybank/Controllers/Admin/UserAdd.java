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
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.w3c.dom.events.MouseEvent;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.*;

public class UserAdd implements Initializable
{
    @FXML
    public AnchorPane back_btn;

    public TextField add_username;
    @FXML
    public TextField add_email;
    @FXML
    public PasswordField add_password;
    @FXML
    public PasswordField add_confirmpass;
    @FXML
    public TextField add_phone;
    @FXML
    public TextField add_address;
    @FXML
    public DatePicker add_birthday;
    @FXML
    public TextField add_answer;
    @FXML
    public ComboBox<String> add_recoveryquestion;
    @FXML
    public Button add_btn;
    @FXML
    public AnchorPane add_form;
    @FXML
    public ComboBox<String> add_usertype;

    //For the BACK Button
    public void back(javafx.scene.input.MouseEvent mouseEvent)
    {
            try{
                FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("/Fxml/Admin/UserManagement.fxml"));
                Parent registerRoot=fxmlLoader.load();
                Stage stage=(Stage) add_form.getScene().getWindow();
                stage.getScene().setRoot(registerRoot);

            }catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }



//For Adding the user information
public void addUser(javafx.scene.input.MouseEvent mouseEvent) {
        AlertMessage alert = new AlertMessage();
        DatabaseConnection connection = new DatabaseConnection();
        Connection dbconnect = connection.getConnection();

        if (add_username.getText().isEmpty() ||
                add_email.getText().isEmpty() ||
                add_password.getText().isEmpty() ||
                add_confirmpass.getText().isEmpty() ||
                add_phone.getText().isEmpty() ||
                add_address.getText().isEmpty() ||
                add_birthday.getValue() == null ||
                add_recoveryquestion.getSelectionModel().getSelectedItem() == null ||
                add_answer.getText().isEmpty()) {
            alert.errorMessage("Please fill all the fields");
        } else if (!add_password.getText().equals(add_confirmpass.getText())) {
            alert.errorMessage("Passwords do not match");
        } else if (add_password.getText().length() < 8) {
            alert.errorMessage("Password length ");
        } else {
            String checkMail_Registered = "SELECT * FROM users WHERE email='" + add_email.getText() + "'";
           try{
               Statement statement = dbconnect.createStatement();
               ResultSet result = statement.executeQuery(checkMail_Registered);

            if (result.next()) {
                alert.errorMessage("Email is already in use!");
            } else {

                    if (result.next()) {
                        alert.errorMessage("Email is already in use!");
                    } else {
                        String insertData = "INSERT INTO users (email, username, password, question, answer, date,up_date,user_type,phone,address,birthday) " +
                                "VALUES (?, ?, ?, ?, ?, ?,?, 'user',?,?,?)";
                        PreparedStatement prepar = dbconnect.prepareStatement(insertData);
                        prepar.setString(1, add_email.getText());
                        prepar.setString(2, add_username.getText());
                        prepar.setString(3, add_password.getText());
                        prepar.setString(4, add_recoveryquestion.getSelectionModel().getSelectedItem());
                        prepar.setString(5, add_answer.getText());

                        // Correct date conversion
                        java.sql.Date sqlDate = new java.sql.Date(new Date().getTime());
                        prepar.setDate(6, sqlDate);
                        prepar.setString(7,sqlDate.toString());
                        prepar.setString(8,add_phone.getText());
                        prepar.setString(9,add_address.getText());

                        //Handling Date Picker for Birthday
                        LocalDate dob=add_birthday.getValue();
                        java.sql.Date dobsqlDate=java.sql.Date.valueOf(dob);
                        prepar.setDate(10,dobsqlDate);

                        prepar.executeUpdate();
                        alert.successMessage("Add a new user successfully!");

                    }

                }
                }catch (Exception ex) {
               ex.printStackTrace();
            }
            }
        }
        //clearing the fields after the process of registering
        public void addAfter() {
            add_email.setText("");
            add_username.setText("");
            add_password.setText("");
            add_confirmpass.setText("");
            add_answer.setText("");
            add_recoveryquestion.getSelectionModel().selectFirst();
            add_address.setText("");
            add_phone.setText("");
            add_birthday.setValue(null);
        }

    //For  the recovery question of password
    public String[] questionList = {"What is your favorite food?",
            "What is your favorite color?",
            "What is your school?",
            "What is the name of your pet?",
            "What is your favourite sport?"};

    public void questions() {
        List<String> listQ = new ArrayList<>();

        for (String data : questionList) {
            listQ.add(data);
        }
        ObservableList<String> listData = FXCollections.observableArrayList(listQ);
       add_recoveryquestion.setItems(listData);
    }

    //For to choose the user type
    public String[] userList={"Admin","User"};

    public void userType(){
        List<String> listUser=new ArrayList<>();
        for(String data:userList)
        {
            listUser.add(data);
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        questions();
        userType();

    }

}
