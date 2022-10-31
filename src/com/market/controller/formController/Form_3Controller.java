package com.market.controller.formController;

import com.market.database.DB;
import java.sql.Connection;
import javax.swing.JTable;
import java.sql.ResultSet;
import java.sql.Statement;

public class Form_3Controller {
    
    DB db = DB.getInstance();
    Connection conn = db.getConn();
    Statement createStatement = null;
    ResultSet rs = null;
    
    
    public void showBill(JTable tableHistory) {
        try {
            createStatement = conn.createStatement();
            rs = createStatement.executeQuery("SELECT * FROM Bill");
            tableHistory.setModel(net.proteanit.sql.DbUtils.resultSetToTableModel(rs));
        }catch(Exception e) {
            e.printStackTrace();
            System.out.println("Something went wrong with the showBill method!");
        }
    }
    
    
}
