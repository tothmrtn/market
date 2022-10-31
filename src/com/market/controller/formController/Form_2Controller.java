package com.market.controller.formController;

import com.market.database.DB;
import com.market.swing.TextField;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Form_2Controller {

    DB db = DB.getInstance();
    Connection conn = db.getConn();
    Statement createStatement = null;
    PreparedStatement preparedStatement = null;
    ResultSet rs = null;
    
    net.proteanit.sql.DbUtils utils;
    
    
    // Table
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
    

    int id; 
    public void countBill() {
        try {
            createStatement = conn.createStatement();
            rs = createStatement.executeQuery("SELECT MAX(id) FROM bill");
            rs.next();
            id = rs.getInt(1) + 1;
        }catch(SQLException e) {
            e.printStackTrace();
            System.out.println("Something went wrong with the countBill method!");
        }       
    }
    
    
    int key; 
    public void rowClicked(JTable itemList, TextField txtName, TextField txtPrice) {
        DefaultTableModel model = (DefaultTableModel) itemList.getModel();
        int index = itemList.getSelectedRow();
        key = Integer.valueOf(model.getValueAt(index, 0).toString());
        txtName.setText(model.getValueAt(index, 1).toString());
        txtPrice.setText(model.getValueAt(index, 3).toString()); 
    }

    
    // Buttons  
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
    
    
}
