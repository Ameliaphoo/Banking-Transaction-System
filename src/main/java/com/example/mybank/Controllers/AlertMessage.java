package com.example.mybank.Controllers;

import com.example.mybank.Controllers.Admin.Account;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;

import java.util.Optional;

public class AlertMessage {

    private Alert alert;

    public void errorMessage(String message) {
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Message");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void successMessage(String message) {
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Message");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void confirmationMessage(String title, String content, String s, Runnable onConfirm) {
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            onConfirm.run();
        }
    }

    public void editAccountStatusDialog(Account account, Runnable onConfirm) {
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Edit Account Status");
        alert.setHeaderText("Change the status of the account");

        ComboBox<String> statusComboBox = new ComboBox<>();
        statusComboBox.getItems().addAll("Active", "Inactive", "Suspended");
        statusComboBox.setValue(account.getStatus());

        VBox vbox = new VBox(statusComboBox);
        vbox.setSpacing(10);
        alert.getDialogPane().setContent(vbox);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            account.setStatus(statusComboBox.getValue());
            onConfirm.run();
        }
    }
}
