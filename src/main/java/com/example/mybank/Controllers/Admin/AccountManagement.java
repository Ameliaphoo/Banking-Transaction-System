package com.example.mybank.Controllers.Admin;

import com.example.mybank.Controllers.AlertMessage;
import com.example.mybank.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class AccountManagement {
    @FXML
    public TableColumn<Account, Integer> account_id;
    @FXML
    public TableColumn<Account, Integer> user_id;
    @FXML
    public TableColumn<Account, String> account_number;
    @FXML
    public TableColumn<Account, String> account_type;
    @FXML
    public TableColumn<Account, Integer> balance;
    @FXML
    public TableColumn<Account, Integer> pin;
    @FXML
    public TableColumn<Account, String> status;
    @FXML
    public TableColumn<Account, Void> action_col;
    @FXML
    public TableView<Account> account_table;
    @FXML
    public Button search_btn;
    @FXML
    public TextField search_field;
    @FXML
    public AnchorPane account_management_page;

    DatabaseConnection connection = new DatabaseConnection();
    Connection connectdb = connection.getConnection();
    ObservableList<Account> accountList = FXCollections.observableArrayList();

    public void initialize() {
        account_id.setCellValueFactory(new PropertyValueFactory<>("account_id"));
        user_id.setCellValueFactory(new PropertyValueFactory<>("user_id"));
        account_number.setCellValueFactory(new PropertyValueFactory<>("account_number"));
        account_type.setCellValueFactory(new PropertyValueFactory<>("account_type"));
        balance.setCellValueFactory(new PropertyValueFactory<>("balance"));
        pin.setCellValueFactory(new PropertyValueFactory<>("pin"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));

        account_table.setItems(accountList);
        loadAccounts();
        addButtonToTable();
    }

    public void loadAccounts() {
        accountList.clear();
        String query = "SELECT * FROM accounts";
        try {
            Statement statement = connectdb.createStatement();
            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                accountList.add(new Account(
                        result.getInt("account_id"),
                        result.getInt("user_id"),
                        result.getString("account_number"),
                        result.getString("account_type"),
                        result.getInt("balance"),
                        result.getInt("pin"),
                        result.getString("status")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addButtonToTable() {
        Callback<TableColumn<Account, Void>, TableCell<Account, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Account, Void> call(final TableColumn<Account, Void> param) {
                final TableCell<Account, Void> cell = new TableCell<>() {
                    private final Button editButton = new Button("Edit");

                    {
                        editButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
                        editButton.setOnAction((ActionEvent event) -> {
                            Account data = getTableView().getItems().get(getIndex());
                            handleEdit(data);
                        });
                    }

                    HBox buttons = new HBox(editButton);

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(buttons);
                        }
                    }
                };
                return cell;
            }
        };

        action_col.setCellFactory(cellFactory);
    }

    //Edit button function
    public void handleEdit(Account account) {
        AlertMessage alert = new AlertMessage();
        alert.editAccountStatusDialog(account, () -> {
            updateAccountStatus(account);
            account_table.refresh();
        });
    }

    //updating the account status
    private void updateAccountStatus(Account account) {
        String query = "UPDATE accounts SET status = '" + account.getStatus() + "' WHERE account_id = " + account.getAccount_id();
        try {
            Statement statement = connectdb.createStatement();
            statement.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //Search by UserId
    public void searchUserID(ActionEvent e)
    {
        String searchText = search_field.getText();
        if (searchText.isEmpty())
        {
            loadAccounts();
        }
        else
        {
            accountList.clear();
            String query = "SELECT * FROM accounts WHERE user_id LIKE ?";
            try
            {
                PreparedStatement prepare = connectdb.prepareStatement(query);
                prepare.setString(1, "%" + searchText + "%");
                ResultSet result = prepare.executeQuery();

                while (result.next())
                {
                    accountList.add(new Account(
                            result.getInt("account_id"),
                            result.getInt("user_id"),
                            result.getString("account_number"),
                            result.getString("account_type"),
                            result.getInt("balance"),
                            result.getInt("pin"),
                            result.getString("status")
                    ));
                }
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }

    // Switching the Form of Add User Form
    public void addAccountForm(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/Admin/AccountCreate.fxml"));
            Parent accountManagementRoot = fxmlLoader.load();
            Stage stage = (Stage) account_management_page.getScene().getWindow();
            stage.getScene().setRoot(accountManagementRoot);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //Switching Form to Dashboard
    public void switchDashboard(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/Admin/AdminMenu.fxml"));
            Parent accountManagementRoot = fxmlLoader.load();
            Stage stage = (Stage) account_management_page.getScene().getWindow();
            stage.getScene().setRoot(accountManagementRoot);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    //Switching Form to Accounts
    public void switchUsers(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/Admin/UserManagement.fxml"));
            Parent accountManagementRoot = fxmlLoader.load();
            Stage stage = (Stage) account_management_page.getScene().getWindow();
            stage.getScene().setRoot(accountManagementRoot);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    //Switching Form to Transaction
    public void switchTransaction(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/Admin/TransactionManagement.fxml"));
            Parent accountManagementRoot = fxmlLoader.load();
            Stage stage = (Stage) account_management_page.getScene().getWindow();
            stage.getScene().setRoot(accountManagementRoot);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    //logouting
    public void switchLogin(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/login.fxml"));
            Parent accountManagementRoot = fxmlLoader.load();
            Stage stage = (Stage) account_management_page.getScene().getWindow();
            stage.getScene().setRoot(accountManagementRoot);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
