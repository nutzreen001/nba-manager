/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test;

/**
 *
 * @author Chanteq Demo
 */
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;

public class Contract extends JFrame {
    final private Font font = new Font("Arial", Font.BOLD, 15);
    final private Dimension dimension = new Dimension(150, 50);
    final private Dimension maxSize = new Dimension(600, 300);
    final private Dimension minSize = new Dimension(400, 100);

    private JPanel navigationPanel, sidebarPanel, contentPanel;
    private JComboBox<String> playerField;
    private DefaultListModel<String> contractListModel, renewedListModel;
    private JList<String> contractList, renewedList;
    private Queue<String> contractQueue, renewedQueue;

    public Contract() {
        initialize();
    }

    public void initialize() {
        setTitle("Contract Extension");
        setSize(1200, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // navigation Panel
        navigationPanel = new JPanel();
        navigationPanel.setBackground(Color.LIGHT_GRAY);
        navigationPanel.setPreferredSize(new Dimension(1200, 50));

        JLabel titleLabel = new JLabel("NBA Manager Game");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        navigationPanel.add(titleLabel);

        // sidebar Panel
        sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(Color.DARK_GRAY);
        sidebarPanel.setPreferredSize(new Dimension(200, 750));

        String[] buttonLabels = {"HOME", "TEAM", "PLAYER", "JOURNEY", "CONTRACT", "INJURY"};

        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.setPreferredSize(dimension);
            button.setMaximumSize(maxSize);
            button.setMinimumSize(minSize);
            button.setBackground(Color.LIGHT_GRAY);
            button.setFont(new Font("Arial", Font.BOLD, 15));
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    handleSidebarButtonClick(label);
                }
            });
            sidebarPanel.add(button);
        }

        // content Panel
        contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setPreferredSize(new Dimension(50, 500));

        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 5));
        JLabel playerNameLabel = new JLabel("       Player Name: ");
        playerNameLabel.setFont(new Font("Arial", Font.PLAIN, 17));
        inputPanel.add(playerNameLabel);
        playerField = new JComboBox<>();
        playerField.setPreferredSize(new Dimension(100, 30));
        loadPlayerName();
        inputPanel.add(playerField);

        JButton addButton = new JButton("ADD");
        addButton.setFont(font);
        addButton.setPreferredSize(new Dimension(100, 35));
        addButton.addActionListener(e -> addToContractList());

        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        btnPanel.add(addButton);
        inputPanel.add(btnPanel);

        contractListModel = new DefaultListModel<>();
        renewedListModel = new DefaultListModel<>();
        contractList = new JList<>(contractListModel);
        renewedList = new JList<>(renewedListModel);
        contractQueue = new LinkedList<>();
        renewedQueue = new LinkedList<>();

        contractList.setPreferredSize(new Dimension(50, 200));
        renewedList.setPreferredSize(new Dimension(400, 500));

        JPanel contractPanel = new JPanel(new BorderLayout());
        contractPanel.setBorder(new TitledBorder("Contract Extension Queue"));
        contractPanel.add(new JScrollPane(contractList), BorderLayout.CENTER);

        JPanel renewedPanel = new JPanel(new BorderLayout());
        renewedPanel.setBorder(new TitledBorder("Renewed Contract Queue"));
        renewedPanel.add(new JScrollPane(renewedList), BorderLayout.CENTER);

        JPanel listPanel = new JPanel(new GridLayout(1, 2, 10, 50));
        listPanel.add(contractPanel);
        listPanel.add(renewedPanel);

        JButton rmvButton = new JButton("REMOVE");
        rmvButton.setFont(font);
        rmvButton.setPreferredSize(new Dimension(110, 45));
        rmvButton.addActionListener(e -> rmvFromContractList());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(rmvButton);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        contentPanel.add(inputPanel, BorderLayout.NORTH);
        contentPanel.add(listPanel, BorderLayout.CENTER);
        contentPanel.add(bottomPanel, BorderLayout.SOUTH);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(navigationPanel, BorderLayout.NORTH);
        getContentPane().add(sidebarPanel, BorderLayout.WEST);
        getContentPane().add(contentPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void loadPlayerName() {
        String jdbcUrl = "jdbc:mysql://localhost:3306/test";
        String username = "root";
        String password = "";

        try (Connection con = DriverManager.getConnection(jdbcUrl, username, password)) {
            String sql = "SELECT name FROM players_stat_23_24";
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                String playername = rs.getString("name");
                playerField.addItem(playername);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addToContractList() {
        String playername = (String) playerField.getSelectedItem();

        if (playername != null) {
            String entry = String.format("Player: %-20s", playername);
            contractQueue.add(entry);
            contractListModel.addElement(entry);
            JOptionPane.showMessageDialog(this, "Player \"" + playername + "\" added to Contract Queue.");
        } else {
            JOptionPane.showMessageDialog(this, "Please select a player.");
        }
    }

    private void rmvFromContractList() {
        String removedPlayer = contractQueue.poll();

        if (removedPlayer != null) {
            contractListModel.removeElement(removedPlayer);
            renewedQueue.add(removedPlayer);
            renewedListModel.addElement(removedPlayer);
            JOptionPane.showMessageDialog(this, "Player \"" + removedPlayer + "\" has been moved to the Renewed Contract Queue.");
        } else {
            JOptionPane.showMessageDialog(this, "Contract Queue is empty.");
        }
    }

    private void handleSidebarButtonClick(String label) {
        // Close current frame before opening a new one if necessary
        this.dispose();
        switch (label) {
            case "HOME":
                new Home();
                break;
            case "TEAM":
            case "PLAYER":
            case "JOURNEY":
            case "CONTRACT":
            case "INJURY":
                new Temp();
                break;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Contract();
        });
    }
}

class Home extends JFrame {
    public Home() {
        setTitle("Home");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}

class Temp extends JFrame {
    public Temp() {
        setTitle("Temporary Window");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
