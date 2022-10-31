package com.market.database;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {
    
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost/marketdb";
    private static final String SERVER_URL = "jdbc:mysql://localhost/";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    
    private static volatile DB INSTANCE = null;
    
    private Connection conn = null;
    private Statement createStatement = null;
    
    net.proteanit.sql.DbUtils utils;
    
    private DB() {
        try {
            Class.forName(DB_DRIVER);
            conn = DriverManager.getConnection(SERVER_URL, USERNAME, PASSWORD);
            System.out.println("Connection was successful!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Connection has failed!");
        }
        
        
        if(conn != null) {
            try {
                createStatement = conn.createStatement();
            }catch(SQLException e) {
                e.printStackTrace();
                System.out.println("Failed to initialize createStatement!");
            }
        }else {
            System.out.println("Connection is null!");
        }
        
        
        try {
            conn = DriverManager.getConnection(SERVER_URL, USERNAME, PASSWORD);
            createStatement = conn.createStatement();
            createStatement.executeUpdate("CREATE DATABASE IF NOT EXISTS marketdb");
        }catch(SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to create marketdb database!");
        }
        
        
        try {
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            createStatement = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS registered "
                    + "(id INT ( 11 ) NOT NULL PRIMARY KEY AUTO_INCREMENT, "
                    + "name VARCHAR (75) NOT NULL, "
                    + "email VARCHAR(75) NOT NULL UNIQUE, "
                    + "password VARCHAR(175) NOT NULL)";
            createStatement.executeUpdate(sql);
        }catch(SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to create registered table!");
        }   
    }
    
    
    public static DB getInstance() {
        DB result = INSTANCE;
        if(result == null) {
            synchronized(DB.class) {
                result = INSTANCE;
                if(result == null) {
                    INSTANCE = result = new DB();
                }
            }
        }
        return result;
    }
    
    
    public Connection getConn() {
        return this.conn;
    }

    
}
