module com.example.mybank {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires mysql.connector.j;

    opens com.example.mybank to javafx.fxml;
    opens com.example.mybank.Controllers to javafx.fxml;
    opens com.example.mybank.Controllers.Client to javafx.fxml;
    opens com.example.mybank.Controllers.Admin to javafx.fxml;

    exports com.example.mybank;
    exports com.example.mybank.Controllers;
    exports com.example.mybank.Controllers.Admin;
    exports com.example.mybank.Controllers.Client;
    exports com.example.mybank.Models;
    exports com.example.mybank.Views;


}