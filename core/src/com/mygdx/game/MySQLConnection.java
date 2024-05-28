package com.mygdx.game;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLConnection {
    public static final String URL="jdbc:mysql://localhost:3306/";
    public static final String USERNAME="root";
    public static final String PASSWORD="";
    static Connection getConnection(){
        Connection c=null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("localhost connection success");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return c;
    }
    static Connection getDatabase(){
        Connection d=null;
        try{
            d = MySQLConnection.getConnection();
            Statement statement = d.createStatement();
            String query = "CREATE DATABASE IF NOT EXISTS dbTheVoid";
            int databaseCreated = statement.executeUpdate(query);
            System.out.println("Database created: " + databaseCreated);
            d = DriverManager.getConnection(URL + "dbTheVoid", USERNAME, PASSWORD);
            System.out.println("DB connection success");
        }catch (SQLException e){
            e.printStackTrace();
        }
        return d;
    }
}
