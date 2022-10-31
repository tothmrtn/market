package com.market.controller.formController;

import com.market.database.DB;
import com.market.swing.TextField;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Form_1Controller {
    
    DB db = DB.getInstance();
    Connection conn = db.getConn();
    Statement createStatement = null;
    PreparedStatement preparedStatement = null;
    ResultSet rs = null;
    
    net.proteanit.sql.DbUtils utils;
    
    
    public JTable showProducts(JTable table) {       
        try {
            createStatement = conn.createStatement();
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
            createStatement = conn.createStatement();
            rs = createStatement.executeQuery("SELECT * FROM product WHERE category = '" + filterList.getSelectedItem().toString() + "'");
            table.setModel(utils.resultSetToTableModel(rs));
        }catch(Exception e) {
            e.printStackTrace();
            System.out.println("Something went wrong with the filterProducts method!");
        }
        return table;
    }
    
    
    int count; 
    private void count() {
        try {
            createStatement = conn.createStatement();
            rs = createStatement.executeQuery("SELECT MAX(ID) FROM product");
            rs.next();           
            count = rs.getInt(1) + 1;
        }catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Count method has failed!");
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
 
    
    // Buttons
    public void addProduct(TextField txtName, TextField txtPrice, JComboBox categoryList) {
        String name = txtName.getText();
        String price = txtPrice.getText();
        int category = categoryList.getSelectedIndex();
        
        if(name.isBlank() || price.isBlank() || category == -1) {
            JOptionPane.showMessageDialog(null, "Missing information!");
        }else {
            try {
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
            try {
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
            try {
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
 
    
}
