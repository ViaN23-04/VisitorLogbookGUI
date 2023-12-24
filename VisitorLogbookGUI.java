package com.example.visitorlogbook;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class VisitorLogbookGUI {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/visitors";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "1.Yeswanth";

    public static void main(String[] args) {
        // Ensure the GUI is created on the Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Visitor Logbook");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        frame.getContentPane().add(panel);

        // Create labels, text fields, and button
        JLabel nameLabel = new JLabel("Visitor Name:");
        JTextField nameTextField = new JTextField(20);

        JLabel ageLabel = new JLabel("Visitor Age:");
        JTextField ageTextField = new JTextField(5);

        JButton logButton = new JButton("Log Entry");

        // Add components to the panel
        panel.add(nameLabel);
        panel.add(nameTextField);
        panel.add(ageLabel);
        panel.add(ageTextField);
        panel.add(logButton);

        // Add action listener to the button
        logButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String visitorName = nameTextField.getText();
                String ageText = ageTextField.getText();
                try {
                    int visitorAge = Integer.parseInt(ageText);
                    logVisitorEntry(visitorName, visitorAge);
                    JOptionPane.showMessageDialog(frame, "Visitor logged successfully.", "Entry Logged", JOptionPane.INFORMATION_MESSAGE);
                    nameTextField.setText("");
                    ageTextField.setText("");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter a valid age.", "Input Error", JOptionPane.ERROR_MESSAGE);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        frame.pack();
        frame.setLocationRelativeTo(null); // Center the window
        frame.setVisible(true);
    }

    private static void logVisitorEntry(String visitorName, int visitorAge) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            String insertQuery = "INSERT INTO visitor_log (visitor_name, visitor_age) VALUES (?, ?)";
            preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, visitorName);
            preparedStatement.setInt(2, visitorAge);
            preparedStatement.executeUpdate();
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }
}
