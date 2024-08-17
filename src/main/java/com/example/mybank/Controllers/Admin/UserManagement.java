package com.example.mybank.Controllers.Admin;

import com.example.mybank.Controllers.AlertMessage;
import com.example.mybank.DatabaseConnection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.layout.HBox;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;

public class UserManagement
{
    @FXML
    public TableView<User> usertable;
    @FXML
    public TableColumn<User,Integer> userid;
    @FXML
    public TableColumn<User,String> username;
    @FXML
    public TableColumn<User,String> phone;
    @FXML
    public TableColumn<User,String> email;
    @FXML
    public TableColumn<User,String> address;
    @FXML
    public TableColumn<User, LocalDate> birthday;
    @FXML
    public TableColumn <User,String> password;
    @FXML
    public TableColumn<User,String> usertype;
    @FXML
    public TableColumn<User, Void> actioncol; // Changed type to Void for custom cells with buttons
    @FXML
    public Button search_username;
    @FXML
    public TextField searchfield;
    @FXML
    public AnchorPane add_user_btn;
    @FXML
    public AnchorPane user_management_page;

    DatabaseConnection connection = new DatabaseConnection();
    Connection connectdb = connection.getConnection();
    ObservableList<User> userList = FXCollections.observableArrayList();

    public void initialize()
    {
        userid.setCellValueFactory(new PropertyValueFactory<>("user_id"));
        username.setCellValueFactory(new PropertyValueFactory<>("userName"));
        phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        address.setCellValueFactory(new PropertyValueFactory<>("address"));
        birthday.setCellValueFactory(new PropertyValueFactory<>("birthday"));
        password.setCellValueFactory(new PropertyValueFactory<>("password"));
        usertype.setCellValueFactory(new PropertyValueFactory<>("usertype"));
        usertable.setItems(userList);
        loadUsers();

        // Add custom buttons to the action column
        addButtonToTable();

        usertable.setRowFactory(tv -> {
            TableRow<User> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    User rowData = row.getItem();
                    handleEdit(rowData);
                }
            });
            return row;
        });
    }

    public void loadUsers()
    {
        userList.clear();
        String query = "SELECT * FROM users";
        try
        {
            Statement statement = connectdb.createStatement();
            ResultSet result = statement.executeQuery(query);

            while(result.next())
            {
                userList.add(new User(
                        result.getInt("user_id"),
                        result.getString("username"),
                        result.getString("phone"),
                        result.getString("email"),
                        result.getString("address"),
                        result.getDate("birthday").toLocalDate(),
                        result.getString("password"),
                        result.getString("user_type")
                ));
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    // Method to add Edit and Delete buttons to the action column
    private void addButtonToTable() {
        Callback<TableColumn<User, Void>, TableCell<User, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<User, Void> call(final TableColumn<User, Void> param) {
                final TableCell<User, Void> cell = new TableCell<>() {

                    private final Button editButton = new Button("Edit");
                    private final Button deleteButton = new Button("Delete");

                    {
                        // Style buttons
                        editButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
                        deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");

                        // Add button actions
                        editButton.setOnAction((ActionEvent event) -> {
                            User data = getTableView().getItems().get(getIndex());
                            handleEdit(data);
                        });
                        deleteButton.setOnAction((ActionEvent event) -> {
                            User data = getTableView().getItems().get(getIndex());
                            handleDelete(data);
                        });
                    }

                    HBox buttons = new HBox(editButton, deleteButton);
//                buttons.setSpacing(10); // Add spacing between buttons
//                buttons.setAlignment(Pos.CENTER); // Align buttons to the center

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

        actioncol.setCellFactory(cellFactory);
    }

    // Method to handle edit action
    private void handleEdit(User user) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Admin/UserEdit.fxml"));
            Parent root = loader.load();

          UserEdit controller = loader.getController();
            controller.setUser(user);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Edit User");
            stage.showAndWait();

            // Refresh the user list after editing
            loadUsers();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // Method to handle delete action
    private void handleDelete(User user) {
        AlertMessage alert=new AlertMessage();
        alert.confirmationMessage("Delete User","Are you sure you want to delete user:"+user.getUserName(),"?",()->
        {
            String query="DELETE FROM users WHERE user_id=?";
            try{
                PreparedStatement prepare=connectdb.prepareStatement(query);
                prepare.setInt(1,user.getUser_id());
                loadUsers();
                alert.successMessage("User deleted successfully");

            }catch(Exception e)
            {
                e.printStackTrace();
                alert.errorMessage("An errour while deleting the user");
            }
        });

    }

    // Searching by Username from the user list
    public void searchUserName(ActionEvent e)
    {
        String searchText = searchfield.getText();
        if (searchText.isEmpty())
        {
            loadUsers();
        }
        else
        {
            userList.clear();
            String query = "SELECT * FROM users WHERE username LIKE ?";
            try
            {
                PreparedStatement prepare = connectdb.prepareStatement(query);
                prepare.setString(1, "%" + searchText + "%");
                ResultSet result = prepare.executeQuery();

                while (result.next())
                {
                    userList.add(new User(
                            result.getInt("user_id"),
                            result.getString("username"),
                            result.getString("phone"),
                            result.getString("email"),
                            result.getString("address"),
                            result.getDate("birthday").toLocalDate(),
                            result.getString("password"),
                            result.getString("user_type")
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
    public void addUserForm(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/Admin/UserAdd.fxml"));
            Parent accountManagementRoot = fxmlLoader.load();
            Stage stage = (Stage) user_management_page.getScene().getWindow();
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
            Stage stage = (Stage) user_management_page.getScene().getWindow();
            stage.getScene().setRoot(accountManagementRoot);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    //Switching Form to Accounts
    public void switchAccounts(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/Admin/AccountManagement.fxml"));
            Parent accountManagementRoot = fxmlLoader.load();
            Stage stage = (Stage) user_management_page.getScene().getWindow();
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
            Stage stage = (Stage) user_management_page.getScene().getWindow();
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
            Stage stage = (Stage) user_management_page.getScene().getWindow();
            stage.getScene().setRoot(accountManagementRoot);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

