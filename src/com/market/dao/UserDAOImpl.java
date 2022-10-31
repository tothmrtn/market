package com.market.dao;

import com.market.database.DB;
import com.market.model.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class UserDAOImpl implements UserDAO {
    
    DB db = DB.getInstance();
    Connection conn = db.getConn();
    PreparedStatement preparedStatement = null;
    ResultSet rs = null;

    
    // Login - Register
    @Override
    public void addUser(User user) {
        try {
            preparedStatement = conn.prepareStatement("INSERT INTO registered (name, email, password) VALUES (?, ?, ?)");
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.executeUpdate();
        }catch(SQLException e) {
            e.printStackTrace();
            System.out.println("Something went wrong with the addUser method!");
        }  
    }

    
    @Override
    public boolean alreadyRegistered(String email) {
        boolean registered = false;
        try {
            preparedStatement = conn.prepareStatement("SELECT * FROM registered WHERE email=?");
            preparedStatement.setString(1, email);
            rs = preparedStatement.executeQuery();           
            if (rs.next()) {
                registered = true;
            }                          
        }catch(SQLException e) {
            e.printStackTrace();
            System.out.println("Something went wrong with the alreadyRegistered method!");
        }
        return registered;
    }
    
    
    @Override
    public User getUser(String name, String password) {
        User user = null;
        try {
            preparedStatement = conn.prepareStatement("SELECT * FROM registered WHERE name=? AND password=?");
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, password);
            rs = preparedStatement.executeQuery();
            
            if(rs.next()) {
                user = new User();
                user.setName(rs.getString("Name"));
                user.setEmail(rs.getString("Email"));
                user.setPassword(rs.getString("Password"));
            }   
        }catch(SQLException e) {
            e.printStackTrace();
            System.out.println("Something went wrong with the getUser method!");
        }
        return user;
    }
    
    
}
