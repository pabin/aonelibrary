package librarysystem;

import business.*;
import dataaccess.DataAccessFacade;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class CheckoutWindow extends JFrame implements LibWindow {
    public static final CheckoutWindow INSTANCE = new CheckoutWindow();
    private boolean isInitialized = false;
    private JTextField memberIdField;
    private JTextField isbnField;
    private JComboBox<Integer> durationDropdown;
    private JTable checkoutTable;
    private JScrollPane tableScrollPane;
    ControllerInterface ci = new SystemController();

    private CheckoutWindow() {
    }

    public void init() {
        if (isInitialized) return;

        setTitle("Checkout Book");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window on the screen
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setLayout(new BorderLayout());

        defineTopPanel(mainPanel);
        defineInputPanel(mainPanel);
        defineTablePanel(mainPanel);

        getContentPane().add(mainPanel);
        defineBackButtonPanel(); // Added to define the back button panel
        isInitialized = true;
    }

    private void defineTopPanel(JPanel mainPanel) {
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Process Checkout");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        mainPanel.add(topPanel, BorderLayout.NORTH);
    }

    private void defineInputPanel(JPanel mainPanel) {
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

        memberIdField = new JTextField(10);
        isbnField = new JTextField(10);
        Integer[] durations = {7, 21};
        durationDropdown = new JComboBox<>(durations);

        JButton checkoutButton = new JButton("Checkout");

        checkoutButton.addActionListener(this::handleCheckout);

        inputPanel.add(new JLabel("Member ID:"));
        inputPanel.add(memberIdField);
        inputPanel.add(new JLabel("ISBN:"));
        inputPanel.add(isbnField);
        inputPanel.add(new JLabel("Duration:"));
        inputPanel.add(durationDropdown);
        inputPanel.add(checkoutButton);

        mainPanel.add(inputPanel, BorderLayout.NORTH);
    }

    private void defineTablePanel(JPanel mainPanel) {
        String[] columnNames = {"Member ID", "ISBN", "Checkout Date", "Due Date"};
        Object[][] data = {};
        checkoutTable = new JTable(new javax.swing.table.DefaultTableModel(data, columnNames));
        tableScrollPane = new JScrollPane(checkoutTable);
        tableScrollPane.setPreferredSize(new Dimension(750, 150));
        tableScrollPane.setVisible(true);

        JPanel tablePanel = new JPanel();
        tablePanel.add(tableScrollPane);

        mainPanel.add(tablePanel, BorderLayout.CENTER);
    }

    private void defineBackButtonPanel() {
        JPanel backButtonPanel = new JPanel();
        JButton backButton = new JButton("<== Back to Main");

        backButton.addActionListener(evt -> {
            clearFields();
            clearTable(); // Clear table when clicking the back button
            LibrarySystem.hideAllWindows();
            LibrarySystem.INSTANCE.setVisible(true);
        });

        backButtonPanel.add(backButton);
        add(backButtonPanel, BorderLayout.SOUTH);
    }

    private void handleCheckout(ActionEvent e) {
        String memberId = memberIdField.getText().trim();
        String isbn = isbnField.getText().trim();
        Integer duration = (Integer) durationDropdown.getSelectedItem();

        if (memberId.isEmpty() || isbn.isEmpty() || duration == null) {
            JOptionPane.showMessageDialog(this, "All fields must be filled out!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Book> books = ci.getBooks();
        List<LibraryMember> members = ci.getMembers();
        Optional<Book> book = books.stream().filter(b -> b.getIsbn().equals(isbn)).findFirst();
        Optional<LibraryMember> member = members.stream().filter(m -> m.getMemberId().equals(memberId)).findFirst();

        if (book.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Book does not exist in the system!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (member.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Member does not exist in the system!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        BookCopy availableCopy = book.get().getNextAvailableCopy();
        if (!book.get().isAvailable() || availableCopy == null) {
            JOptionPane.showMessageDialog(this, "Book is not available at this time!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (book.get().getMaxCheckoutLength() < duration) {
            JOptionPane.showMessageDialog(this, "Book cannot be checked out for the provided duration!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }


        String issuedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        CheckoutEntry entry = new CheckoutEntry(generateID(), issuedDate, duration, availableCopy, member.get());
        List<CheckoutEntry> entries = new ArrayList<>(ci.getCheckOutEntries());
        entries.add(entry);
        availableCopy.changeAvailability();

        DataAccessFacade.loadEntryRecordsMap(entries);

        JOptionPane.showMessageDialog(this, "Checkout successful! Member ID: " + memberId + " | ISBN: " + isbn, "Success", JOptionPane.INFORMATION_MESSAGE);
        updateTable(memberId, isbn, issuedDate, entry.getDueDate());
        clearFields();
    }

    private void updateTable(String memberId, String isbn, String issuedDate, String dueDate) {
        Object[][] newData = {{memberId, isbn, issuedDate, dueDate}};
        checkoutTable.setModel(new javax.swing.table.DefaultTableModel(newData, new String[]{"Member ID", "ISBN", "Checkout Date", "Checkout Date"}));
        tableScrollPane.setVisible(true);
        SwingUtilities.updateComponentTreeUI(this);
    }

    private void clearTable() {
        checkoutTable.setModel(new javax.swing.table.DefaultTableModel(new Object[][]{}, new String[]{"Member ID", "ISBN", "Checkout Date", "Due Date"}));
    }

    public static String generateID() {
        Random random = new Random();
        return String.valueOf(random.nextInt(9000) + 1000);
    }

    private void clearFields() {
        memberIdField.setText("");
        isbnField.setText("");
        durationDropdown.setSelectedIndex(0);
    }

    @Override
    public boolean isInitialized() {
        return isInitialized;
    }

    @Override
    public void isInitialized(boolean val) {
        isInitialized = val;
    }
}
