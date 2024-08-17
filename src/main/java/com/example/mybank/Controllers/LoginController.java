package com.example.mybank.Controllers;

import com.example.mybank.Controllers.Client.ClientMenuController;
import com.example.mybank.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoginController
{
    @FXML
    public CheckBox login_showpassword;
    @FXML
    public Hyperlink login_forgotpassword;
    @FXML
    private Button login_cancel_btn;

    @FXML
    private Button login_btn;

    @FXML
    private Button login_register_btn;
    @FXML
    private Label login_message;

    @FXML
    private TextField login_username;
    @FXML
    private TextField login_password_visible;
    @FXML
    private PasswordField login_password;

    @FXML
    private BorderPane login_form;

    private int loggedInUserId; // To store the logged-in user ID

    // Login Button Function
    @FXML
    public void login_btnOnAction(ActionEvent e)
    {
        AlertMessage alert = new AlertMessage();
        if (!login_username.getText().isBlank() && !login_password.getText().isBlank())
        {
            validLogin();
        } else {
            alert.errorMessage("Please Enter Username and Password");
        }
    }

    public void validLogin() {
        AlertMessage alert = new AlertMessage();

        DatabaseConnection connection = new DatabaseConnection();
        Connection connectdb = connection.getConnection();

        String verifyLogin = "SELECT * FROM users WHERE email='" + login_username.getText() +
                "' AND password='" + login_password.getText() + "'";
        try {
            Statement statement = connectdb.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyLogin);

            if (queryResult.next()) {
                loggedInUserId = queryResult.getInt("user_id");
                String userType = queryResult.getString("user_type");
                alert.successMessage("You Are Welcome");
                navigateBasedOnUserType(userType);
            } else {
                alert.errorMessage("Invalid Login! Try Again");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Navigating the user type to switch the form
    public void navigateBasedOnUserType(String userType)
    {
        try {
            FXMLLoader fxmlloader;
            Parent root;
            if ("admin".equalsIgnoreCase(userType)) {
                fxmlloader = new FXMLLoader(getClass().getResource("/Fxml/Admin/AdminMenu.fxml"));
                root=fxmlloader.load();
            } else {
                fxmlloader = new FXMLLoader(getClass().getResource("/Fxml/Client/ClientMenu.fxml"));
                root = fxmlloader.load();

                // Pass the user ID to the ClientMenuController
                ClientMenuController controller = fxmlloader.getController();
                controller.setLoggedInUserId(loggedInUserId);
            }
                Stage stage = (Stage) login_form.getScene().getWindow();
                stage.getScene().setRoot(root);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Password Visible Function
    @FXML
    public void showPassword(ActionEvent e) {
        if (login_showpassword.isSelected()) {
            login_password_visible.setText(login_password.getText());
            login_password_visible.setVisible(true);
            login_password.setVisible(true);
        } else {
            login_password.setText(login_password_visible.getText());
            login_password_visible.setVisible(false);
            login_password.setVisible(true);
        }
    }

    // Switching Form to the Register
    public void showRegisterForm(ActionEvent e)
    {
        if (e.getSource() == login_register_btn)
        {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/Register.fxml"));
                Parent registerRoot = fxmlLoader.load();
                Stage stage = (Stage) login_form.getScene().getWindow();
                stage.getScene().setRoot(registerRoot);

            } catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }

    // Switching Form to the Forgot Form by Hyperlink of forgot
    @FXML
    public void switchForgotForm(ActionEvent e) {
        if (e.getSource() == login_forgotpassword) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/Forgot1.fxml"));
                Parent forgotRoot = fxmlLoader.load();
                Stage stage = (Stage) login_form.getScene().getWindow();
                stage.getScene().setRoot(forgotRoot);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    // Closing the Login Form
    @FXML
    public void login_cancel_btnOnAction(ActionEvent e)
    {
        Stage stage = (Stage) login_cancel_btn.getScene().getWindow();
        stage.close();
    }
}
