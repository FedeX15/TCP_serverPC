/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fedex.tcpserver;

import java.awt.*;

/**
 *
 * @author Federico
 */
public class PlayGUI extends javax.swing.JFrame {

    /**
     * Creates new form PlayGUI
     */
    public PlayGUI(int width, int height) {
        initComponents();
        setSize(width, height);
        btnOpponent.setSize(new Dimension(100, 25));
        btnOpponent.setLocation(this.getWidth()/2 + btnOpponent.getWidth()/2, btnOpponent.getY());
        btnUser.setSize(100, 25);
        btnUser.setLocation(this.getWidth()/2 + btnUser.getWidth()/2, btnUser.getY());
        setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnUser = new javax.swing.JButton();
        btnOpponent = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnUser)
                    .addComponent(btnOpponent))
                .addContainerGap(354, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnOpponent)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 258, Short.MAX_VALUE)
                .addComponent(btnUser)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void setOpponentPosition(int x) {
        btnOpponent.setLocation(x, btnOpponent.getY());
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnOpponent;
    private javax.swing.JButton btnUser;
    // End of variables declaration//GEN-END:variables
}
