package project;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.util.List;

public class AddStockFrame extends JFrame {

    private BinaryTree tree;
    private DefaultTableModel mainTableModel;
    private DefaultTableModel lowStockTableModel;
    private static final String HISTORY = "inventory_history.txt";
    private static final int THRESHOLD = 200;

    public AddStockFrame(BinaryTree tree, JFrame parentFrame) {
        super("Add / Remove Inventory");
        this.tree = tree;

        // Login dialog
        LoginDialog login = new LoginDialog(parentFrame);
        login.setVisible(true);
        if (!login.isOk()) {
            dispose();
            return;
        }

        setSize(1000, 650);
        setLocationRelativeTo(parentFrame);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        add(Header.build(), BorderLayout.NORTH);

        // ================= TABLE AREA ====================
        mainTableModel = new DefaultTableModel(new String[]{"Item No", "Name", "Price", "Qty"}, 0);
        JTable mainTable = new JTable(mainTableModel);

        lowStockTableModel = new DefaultTableModel(new String[]{"Item No", "Name", "Qty"}, 0);
        JTable lowStockTable = new JTable(lowStockTableModel);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("All Items", new JScrollPane(mainTable));
        tabbedPane.addTab("Low Stock (<200)", new JScrollPane(lowStockTable));
        add(tabbedPane, BorderLayout.CENTER);

        // =============== FORM AREA =======================
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        JTextField itemNoField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField qtyField = new JTextField();

        formPanel.add(new JLabel("Item No"));  formPanel.add(itemNoField);
        formPanel.add(new JLabel("Name"));     formPanel.add(nameField);
        formPanel.add(new JLabel("Price"));    formPanel.add(priceField);
        formPanel.add(new JLabel("Quantity")); formPanel.add(qtyField);

        JButton addBtn = new JButton("ADD");
        JButton remBtn = new JButton("REMOVE SELECTED");
        JButton backBtn = new JButton("BACK");
        formPanel.add(addBtn); formPanel.add(remBtn);
        formPanel.add(new JLabel()); formPanel.add(backBtn);
        add(formPanel, BorderLayout.SOUTH);

        // =============== BUTTON: OPEN HISTORY ============
        JButton openLogBtn = new JButton("Open Inventory History");
        openLogBtn.addActionListener(e -> openHistoryFile());
        add(openLogBtn, BorderLayout.WEST);

        // =============== ADD ITEM ========================
        addBtn.addActionListener(e -> {
            try {
                int itemNo = Integer.parseInt(itemNoField.getText().trim());
                String name = nameField.getText().trim();
                double price = Double.parseDouble(priceField.getText().trim());
                int qty = Integer.parseInt(qtyField.getText().trim());

                Item newItem = new Item(itemNo, name, price, qty);
                tree.insert(newItem);
                InventoryStore.save(); // Save after add
                logHistory("ADD", newItem);
                refreshTables();
                JOptionPane.showMessageDialog(this, "Item added!");

                itemNoField.setText("");
                nameField.setText("");
                priceField.setText("");
                qtyField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input.");
            }
        });

        // =============== REMOVE ITEM ======================
        remBtn.addActionListener(e -> {
            int row = mainTable.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Please select a row to remove.");
                return;
            }
            int itemNo = (int) mainTableModel.getValueAt(row, 0);
            String name = (String) mainTableModel.getValueAt(row, 1);

            tree.delete(itemNo);
            InventoryStore.save(); // Save after remove
            logHistory("REMOVE", new Item(itemNo, name, 0, 0));
            refreshTables();
        });

        // =============== BACK BUTTON ======================
        backBtn.addActionListener(e -> {
            InventoryStore.save();
            dispose();
        });

        // ======= INITIAL TABLE LOAD =========
        refreshTables();
        setVisible(true);
    }

    private void refreshTables() {
        mainTableModel.setRowCount(0);
        lowStockTableModel.setRowCount(0);
        List<Item> allItems = tree.toList();

        for (Item item : allItems) {
            mainTableModel.addRow(new Object[]{item.itemNo, item.name, item.price, item.quantity});
            if (item.quantity < THRESHOLD) {
                lowStockTableModel.addRow(new Object[]{item.itemNo, item.name, item.quantity});
            }
        }
    }

    private void logHistory(String action, Item it) {
        try {
            FileWriter fw = new FileWriter(HISTORY, true);
            fw.write(LocalDateTime.now() + " | " + action + " | " +
                     it.itemNo + " | " + it.name +
                     " | price=" + it.price + " | qty=" + it.quantity + "\n");
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openHistoryFile() {
        try {
            Desktop.getDesktop().open(new java.io.File(HISTORY));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Cannot open history file.");
        }
    }
}
