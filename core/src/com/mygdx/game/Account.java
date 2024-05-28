package com.mygdx.game;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Account implements Runnable{
    public void run() {
        try(Connection c = MySQLConnection.getDatabase(); Statement statement = c.createStatement()) {
            String query = "CREATE TABLE IF NOT EXISTS tbluser (" +
                    "userID INT PRIMARY KEY AUTO_INCREMENT," +
                    "username VARCHAR(50) NOT NULL," +
                    "password TEXT NOT NULL," +
                    "email TEXT NOT NULL)";
            statement.execute(query);
            System.out.println("Table [user] created successfully!");
            query = "CREATE TABLE IF NOT EXISTS tblplayer (" +
                    "charID INT PRIMARY KEY AUTO_INCREMENT," +
                    "userID INT," +
                    "FOREIGN KEY (userID) REFERENCES tbluser(userID)," +
                    "positionx FLOAT NOT NULL," +
                    "positionz FLOAT NOT NULL)";
            statement.execute(query);
            System.out.println("Table [player] created successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public int findUser(String username, int password){
        try(Connection c = MySQLConnection.getDatabase();
            Statement statement = c.createStatement()){
            String query = "SELECT userID FROM tbluser where username='"+username+"' and password='"+password+"'";
            ResultSet res = statement.executeQuery(query);
            while(res.next()){
                return res.getInt("userID");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return -1;
    }
}
