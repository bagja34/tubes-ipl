package view;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class DashboardView extends JFrame {
    public DashboardView() {
        setTitle("Ware_Hub");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLayout(new BorderLayout());

        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(7, 1, 0, 10)); // Tambahkan satu baris untuk tombol input barang
        sidebar.setBackground(new Color(45, 45, 45));
        sidebar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Text-like buttons
        JButton dashboardButton = createSidebarButton("DASHBOARD");
        JButton gudangButton = createSidebarButton("GUDANG");
        JButton pembayaranButton = createSidebarButton("PEMBAYARAN");
        JButton historyButton = createSidebarButton("HISTORY");
        JButton inputBarangButton = createSidebarButton("INPUT BARANG"); // Tombol untuk navigasi ke InputBarangView
        JButton logoutButton = createSidebarButton("LOGOUT"); // Tombol untuk navigasi ke LoginView

        sidebar.add(dashboardButton);
        sidebar.add(gudangButton);
        sidebar.add(pembayaranButton);
        sidebar.add(historyButton);
        sidebar.add(inputBarangButton); // Tambahkan tombol input barang
        sidebar.add(logoutButton); // Tambahkan tombol logout

        // Dashboard Panel
        JPanel dashboardPanel = new JPanel(new BorderLayout());
        dashboardPanel.setBackground(Color.WHITE);
        dashboardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

        JPanel tablePanel = createTablePanel();

        dashboardPanel.add(tablePanel, BorderLayout.CENTER);

        add(sidebar, BorderLayout.WEST);
        add(dashboardPanel, BorderLayout.CENTER);

        // Button functionalities
        dashboardButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Navigated to DASHBOARD"));
        gudangButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Navigated to GUDANG"));
        pembayaranButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Navigated to PEMBAYARAN"));
        historyButton.addActionListener(e -> {
            new HistoryView().setVisible(true); // Menampilkan HistoryView
            dispose(); // Menutup DashboardView
        });
        inputBarangButton.addActionListener(e -> {
            new input_barang().setVisible(true); // Menampilkan InputBarangView
            dispose(); // Menutup DashboardView
        });
        logoutButton.addActionListener(e -> {
            new LoginView().setVisible(true); // Menampilkan LoginView
            dispose(); // Menutup DashboardView
        });

        setLocationRelativeTo(null);
    }

    private JButton createSidebarButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setForeground(new Color(173, 216, 230)); // Light Blue on hover
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setForeground(Color.WHITE);
            }
        });

        return button;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY), "Customer Information",
                0, 0, new Font("SansSerif", Font.BOLD, 14), Color.DARK_GRAY));

        JTable table = new JTable();
        table.setRowHeight(25);
        table.setShowGrid(true);
        table.setGridColor(Color.LIGHT_GRAY);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
        header.setBackground(new Color(230, 230, 230));
        header.setForeground(Color.BLACK);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton viewAllButton = new JButton("View Customer");
        viewAllButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        viewAllButton.setBackground(new Color(60, 179, 113));
        viewAllButton.setForeground(Color.WHITE);
        viewAllButton.setFocusPainted(false);
        viewAllButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        viewAllButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        viewAllButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                viewAllButton.setBackground(new Color(46, 139, 87));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                viewAllButton.setBackground(new Color(60, 179, 113));
            }
        });

        panel.add(viewAllButton, BorderLayout.SOUTH);

        viewAllButton.addActionListener(e -> {
            try {
                loadDataToTable(table);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "Error loading data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    private void loadDataToTable(JTable table) throws SQLException {
        String url = "jdbc:mysql://localhost:3306/warehub";
        String username = "root";
        String password = "";

        String query = "SELECT nama, email, perusahaan FROM costumer";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            Vector<String> columnNames = new Vector<>();
            for (int column = 1; column <= columnCount; column++) {
                columnNames.add(metaData.getColumnName(column));
            }

            Vector<Vector<Object>> data = new Vector<>();
            while (resultSet.next()) {
                Vector<Object> row = new Vector<>();
                for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                    row.add(resultSet.getObject(columnIndex));
                }
                data.add(row);
            }

            table.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
        }
    }

    public static void main(String[] args) {
        new DashboardView().setVisible(true);
    }
}
