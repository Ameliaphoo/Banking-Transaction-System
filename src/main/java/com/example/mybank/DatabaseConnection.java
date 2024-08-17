package com.example.mybank;
import java.sql.*;

public class DatabaseConnection {

    public Connection databaseLink;

    public Connection getConnection()
    {
        String databaseName="mybank";
        String user="root";
        String password="";
        String url="jdbc:mysql://localhost:3306/"+databaseName;

        try{
//            Class.forName("com.mysql.jdbc.Driver");
            databaseLink=DriverManager.getConnection(url,user,password);

        }catch(Exception e)
        {
           e.printStackTrace();
        }
        return databaseLink;
    }
}
