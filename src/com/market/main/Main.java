package com.market.main;

import com.market.event.EventMenu;
import com.market.form.Form_1;
import com.market.form.Form_2;
import com.market.form.Form_3;
import com.market.form.Login;
import java.awt.Color;
import java.awt.Component;

public class Main extends javax.swing.JFrame {
    
    public Form_2 formTwo = new Form_2();
    
    public Main() {
        initComponents();
        setBackground(new Color(0, 0, 0, 0));
        EventMenu event = new EventMenu() {
            @Override
            public void selected(int index) {
                switch(index){
                    case 0:
                        showForm(new Form_1()); 
                        break;
                    case 1:
                        showForm(formTwo);
                        break;
                    case 2:
                        showForm(new Form_3());
                        break;
                    case 3:
                        logout();
                        break;
                }
            }
        };
        menu1.initMenu(event);
        showForm(new Form_1());
    }
    
    
    private void showForm(Component com) {
        jPanel1.removeAll();
        jPanel1.add(com);
        jPanel1.revalidate();
        jPanel1.repaint();
    }
    
    
    private void logout() {
        new Login().setVisible(true);
        this.dispose();
    }
    
    
    public void setUserName() {
        this.menu1.getUserName().setText(Login.user.getName());
    }
    
    public void setSellerName() {
        this.formTwo.getSeller().setText(Login.user.getName());
    }
    
  
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        roundPanel1 = new com.market.swing.RoundPanel();
        menu1 = new com.market.component.Menu();
        jPanel1 = new javax.swing.JPanel();
        header2 = new com.market.component.Header();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        roundPanel1.setBackground(new java.awt.Color(21, 21, 21));

        menu1.setPreferredSize(new java.awt.Dimension(230, 582));

        jPanel1.setOpaque(false);
        jPanel1.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout roundPanel1Layout = new javax.swing.GroupLayout(roundPanel1);
        roundPanel1.setLayout(roundPanel1Layout);
        roundPanel1Layout.setHorizontalGroup(
            roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(menu1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1009, Short.MAX_VALUE)
                .addGap(10, 10, 10))
            .addComponent(header2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        roundPanel1Layout.setVerticalGroup(
            roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundPanel1Layout.createSequentialGroup()
                .addComponent(header2, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(menu1, javax.swing.GroupLayout.DEFAULT_SIZE, 590, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(10, 10, 10))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(roundPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(roundPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true);
            }
        });
    }

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.market.component.Header header2;
    private javax.swing.JPanel jPanel1;
    private com.market.component.Menu menu1;
    private com.market.swing.RoundPanel roundPanel1;
    // End of variables declaration//GEN-END:variables
}
