package com.market.swing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JComboBox;

public class DropDownMenu<E> extends JComboBox<E> {

    public DropDownMenu() {       
        setBackground(new Color(0, 0, 0, 0));
        setFont(new java.awt.Font("sansserif", 0, 13));
        setFocusable(false);
    }
    
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g; 
        g2.setColor(new Color(255,250,248));
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 5, 5);
        super.paintComponent(g);
    }
   
    
}
