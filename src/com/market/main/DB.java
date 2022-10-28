package com.market.main;

import com.market.swing.TextField;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.dbutils.DbUtils;

public class DB {
    
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost/marketdb";
    private static final String SERVER_URL = "jdbc:mysql://localhost/";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    
    private static volatile DB INSTANCE = null;
    
    Connection conn = null;
    Statement createStatement = null;
    PreparedStatement preparedStatement = null;
    ResultSet rs = null;
    
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
            }catch(SQLException e){
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
    
    
    /* === Login / Register === */
    public void addUser(User user) {
        try{
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
    
    
    public boolean alreadyRegistered(String email) {
        boolean registered = false;
        try{
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
    
    
    public User getUser(String name, String password) {
        User user = null;
        try{
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
    
    
    /* ===== 1st Form ===== */
    // showing products
    public JTable showProducts(JTable table) {       
        try {
            rs = createStatement.executeQuery("SELECT * FROM product");
            table.setModel(utils.resultSetToTableModel(rs));
        }catch(SQLException e) {
            e.printStackTrace();
            System.out.println("Something went wrong with the showProducts method!");
        }
        return table;
    }
    
    
    public JTable filterProducts(JComboBox filterList, JTable table) {
        try {
            rs = createStatement.executeQuery("SELECT * FROM product WHERE category = '" + filterList.getSelectedItem().toString() + "'");
            table.setModel(utils.resultSetToTableModel(rs));
        }catch(Exception e) {
            e.printStackTrace();
            System.out.println("Something went wrong with the filterProducts method!");
        }
        return table;
    }
    
    // Actions
    int count;
    
    private void count() {
        try{
            rs = createStatement.executeQuery("SELECT MAX(ID) FROM product");
            rs.next();           
            count = rs.getInt(1) + 1;
        }catch (SQLException e) {
            e.printStackTrace();
            System.out.println("count method has failed!");
        }
    }
    
    
    int key;
    
    public void productClicked(JTable table, TextField txtName, TextField txtPrice) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        int index = table.getSelectedRow();
        key = Integer.valueOf(model.getValueAt(index, 0).toString());
        txtName.setText(model.getValueAt(index, 1).toString());
        txtPrice.setText(model.getValueAt(index, 3).toString());         
    }
    
    
    public void addProduct(TextField txtName, TextField txtPrice, JComboBox categoryList) {
        String name = txtName.getText();
        String price = txtPrice.getText();
        int category = categoryList.getSelectedIndex();
        
        if(name.isBlank() || price.isBlank() || category == -1) {
            JOptionPane.showMessageDialog(null, "Missing information!");
        }else {
            try{
                count();
                preparedStatement = conn.prepareStatement("INSERT INTO product VALUES (?, ?, ?, ?)");
                preparedStatement.setInt(1, count);
                preparedStatement.setString(2, name);
                preparedStatement.setString(3, categoryList.getSelectedItem().toString());
                preparedStatement.setInt(4, Integer.valueOf(price));
                preparedStatement.executeUpdate();
                JOptionPane.showMessageDialog(null, "Item added!");
                txtName.setText("");
                txtPrice.setText("");
                categoryList.setSelectedIndex(0);
            }catch(SQLException e) {
                e.printStackTrace();
                System.out.println("Something went wrong with the addProduct method!");
            }
        }        
    }
    
    
    public void editProduct(TextField txtName, TextField txtPrice, JComboBox categoryList) {
        String name = txtName.getText();
        String price = txtPrice.getText();
        int category = categoryList.getSelectedIndex();
        
        if(name.isBlank() || price.isBlank() || category == -1) {
            JOptionPane.showMessageDialog(null, "Missing information!");
        }else {
            try{
                count();
                preparedStatement = conn.prepareStatement("UPDATE product SET name=?, category=?, price=? WHERE id=?");
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, categoryList.getSelectedItem().toString());
                preparedStatement.setInt(3, Integer.valueOf(price));
                preparedStatement.setInt(4, key);
                preparedStatement.executeUpdate();
                JOptionPane.showMessageDialog(null, "Item updated!");
                txtName.setText("");
                txtPrice.setText("");
                categoryList.setSelectedIndex(0);
            }catch(SQLException e) {
                e.printStackTrace();
                System.out.println("Something went wrong with the editProduct method!");
            }
        }        
    }
    
    
    public void deleteProduct(TextField txtName, TextField txtPrice, JComboBox categoryList) {
        String name = txtName.getText();
        String price = txtPrice.getText();
        int category = categoryList.getSelectedIndex();
        
        if(name.isBlank() || price.isBlank() || category == -1) {
            JOptionPane.showMessageDialog(null, "Missing information!");
        }else {
            try{
                count();
                preparedStatement = conn.prepareStatement("DELETE FROM product WHERE id=?");
                preparedStatement.setInt(1, key);
                preparedStatement.executeUpdate();
                JOptionPane.showMessageDialog(null, "Item deleted!");
                txtName.setText("");
                txtPrice.setText("");
                categoryList.setSelectedIndex(0);
            }catch(SQLException e) {
                e.printStackTrace();
                System.out.println("Something went wrong with the deleteProduct method!");
            }
        }        
    }
    
    
    /* ===== 2nd Form ===== */
    // Table
    int id;
    
    public void countBill() {
        try{
            rs = createStatement.executeQuery("SELECT MAX(id) FROM bill");
            rs.next();
            id = rs.getInt(1) + 1;
        }catch(SQLException e) {
            e.printStackTrace();
            System.out.println("Something went wrong with the countBill method!");
        }       
    }
    
    
    int key2;
    
    public void rowClicked(JTable itemList, TextField txtName, TextField txtPrice) {
        DefaultTableModel model = (DefaultTableModel) itemList.getModel();
        int index = itemList.getSelectedRow();
        key = Integer.valueOf(model.getValueAt(index, 0).toString());
        txtName.setText(model.getValueAt(index, 1).toString());
        txtPrice.setText(model.getValueAt(index, 3).toString()); 
    }

      
    // Actions   
    int grd;
    
    public void addSelling(TextField txtName, TextField txtQuantity, TextField txtPrice, JLabel labelTotal, JTable billTable) {
        String name = txtName.getText();
        String quantity = txtQuantity.getText();
        String price = txtPrice.getText();
        
        if(name.isBlank() || quantity.isBlank()) {
            JOptionPane.showMessageDialog(null, "Fields cannot be empty!");
        }else {
            int total = Integer.valueOf(price) * Integer.valueOf(quantity);
            grd = grd + total;
            labelTotal.setText("" + grd);
            DefaultTableModel model = (DefaultTableModel) billTable.getModel();
            String nextRowID = Integer.toString(model.getRowCount());
            model.addRow(new Object[] {
                Integer.valueOf(nextRowID) + 1,
                name,
                price,
                quantity,
                total         
            });       
        }
    }
    
    
    public void insertBill(TextField txtSeller) {
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            LocalDateTime now = LocalDateTime.now();
            countBill();
            preparedStatement = conn.prepareStatement("INSERT INTO bill VALUES (?, ?, ?, ?)");
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, txtSeller.getText());
            preparedStatement.setString(3, now.toString().substring(0, 10));
            preparedStatement.setInt(4, grd); 
            preparedStatement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Bill added!");              
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Something went wrong with the insertBill method!");
        }      
    }
    
    
    /* ===== 3rd Form ===== */
    public void showBill(JTable tableHistory) {
        try{
            rs = createStatement.executeQuery("SELECT * FROM Bill");
            tableHistory.setModel(net.proteanit.sql.DbUtils.resultSetToTableModel(rs));
        }catch(Exception e) {
            e.printStackTrace();
            System.out.println("Something went wrong with the showBill method!");
        }
    }
    
    
}
