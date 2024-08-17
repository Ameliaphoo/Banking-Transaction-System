package com.example.mybank.Controllers.Admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class AdminMenuController {

    @FXML
    private Button dashboard_btn;
    @FXML
    private Button users_btn;
    @FXML
    private Button accounts_btn;
    @FXML
    private Button transaction_btn;
    @FXML
    private AnchorPane admin_menu_form;
    @FXML
    private PieChart pieChart;
    @FXML
    private LineChart<String, Number> lineChart;

    private int loggedInUserId;

    // Database connection details
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/mybank";
    private static final String DATABASE_USER = "root";
    private static final String DATABASE_PASSWORD = "";

    @FXML
    public void initialize() {
        loadPieChartData();
        loadLineChartData();
    }

    // Switch to user management
    @FXML
    public void userManagement(ActionEvent event) {
        if (event.getSource() == users_btn) {
            switchScene("/Fxml/Admin/UserManagement.fxml");
        }
    }

    // Switch to account management
    @FXML
    public void accountManagement(ActionEvent event) {
        if (event.getSource() == accounts_btn) {
            switchScene("/Fxml/Admin/AccountManagement.fxml");
        }
    }

    // Switch to transaction management
    @FXML
    public void transactionManagement(ActionEvent event) {
        if (event.getSource() == transaction_btn) {
            switchScene("/Fxml/Admin/TransactionManagement.fxml");
        }
    }

    private void switchScene(String fxmlPath) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) admin_menu_form.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadPieChartData() {
        String query = "SELECT account_type, COUNT(*) as count FROM accounts GROUP BY account_type";
        Map<String, Integer> accountTypeCounts = new HashMap<>();

        try (Connection conn = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                accountTypeCounts.put(rs.getString("account_type"), rs.getInt("count"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        pieChart.getData().clear();
        for (Map.Entry<String, Integer> entry : accountTypeCounts.entrySet()) {
            PieChart.Data slice = new PieChart.Data(entry.getKey(), entry.getValue());
            pieChart.getData().add(slice);
        }
    }

    private void loadLineChartData() {
        String query = "SELECT tran_date, SUM(amount) as total_amount FROM transactions GROUP BY tran_date";
        Map<LocalDate, Double> dailyTransactionSums = new HashMap<>();

        try (Connection conn = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                LocalDate date = rs.getDate("tran_date").toLocalDate();
                double amount = rs.getDouble("total_amount");
                dailyTransactionSums.put(date, amount);
            }

        } catch (Exception e) {  
            e.printStackTrace();
        }

        lineChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Daily Transactions");

        for (Map.Entry<LocalDate, Double> entry : dailyTransactionSums.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey().toString(), entry.getValue()));
        }

        lineChart.getData().add(series);
    }
}


