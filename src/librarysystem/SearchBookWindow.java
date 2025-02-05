package librarysystem;

import business.*;
import dataaccess.DataAccessFacade;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class SearchBookWindow extends JFrame implements LibWindow {
    public static final SearchBookWindow INSTANCE = new SearchBookWindow();
    private boolean isInitialized = false;
    private JTextField isbnField;
    private JTable searchTable;
    private JScrollPane tableScrollPane;
    ControllerInterface ci = new SystemController();

    private SearchBookWindow() {
    }

    public void init() {
        if (isInitialized) return;

        setTitle("Search Book");
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

        getContentPane().add(mainPanel, BorderLayout.CENTER);
        defineBackButtonPanel();

        isInitialized = true;
    }

    private void defineTopPanel(JPanel mainPanel) {
        JPanel topPanel = new JPanel();
        JLabel titleLabel = new JLabel("Search Book");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(titleLabel);
        mainPanel.add(topPanel, BorderLayout.NORTH);
    }

    private void defineInputPanel(JPanel mainPanel) {
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

        isbnField = new JTextField(10);
        JButton searchButton = new JButton("Search");

        searchButton.addActionListener(this::handleSearch);

        inputPanel.add(new JLabel("Book ISBN:"));
        inputPanel.add(isbnField);
        inputPanel.add(searchButton);

        mainPanel.add(inputPanel, BorderLayout.CENTER);
    }

    private void defineTablePanel(JPanel mainPanel) {
        String[] columnNames = {"Member ID", "ISBN", "Checkout Date", "Due Date", "isOverdue"};
        Object[][] data = {};
        searchTable = new JTable(new javax.swing.table.DefaultTableModel(data, columnNames));
        tableScrollPane = new JScrollPane(searchTable);
        tableScrollPane.setPreferredSize(new Dimension(750, 150));
        tableScrollPane.setVisible(true);

        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);

        mainPanel.add(tablePanel, BorderLayout.SOUTH);
    }

    private void defineBackButtonPanel() {
        JPanel backButtonPanel = new JPanel();
        JButton backButton = new JButton("<== Back to Main");

        backButton.addActionListener(evt -> {
            clearFields();
            clearTable(); // Clear table when clicking the back button
            setVisible(false); // Hide the search book screen
            LibrarySystem.hideAllWindows();
            LibrarySystem.INSTANCE.setVisible(true);
        });

        backButtonPanel.add(backButton);
        getContentPane().add(backButtonPanel, BorderLayout.SOUTH);
    }

    private void handleSearch(ActionEvent e) {
        String isbn = isbnField.getText().trim();

        if (isbn.isEmpty()) {
            JOptionPane.showMessageDialog(this, "ISBN field must be filled out!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Book> books = ci.getBooks();
        Optional<Book> book = books.stream().filter(b -> b.getIsbn().equals(isbn)).findFirst();

        if (book.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Book does not exist in the system!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<BookCopy> bookCopies = Arrays.stream(book.get().getCopies()).toList();

        if (bookCopies.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No Book Copy Records for the provided Book!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        updateTable(bookCopies);
    }

    private void updateTable(List<BookCopy> bookCopies) {
        Object[][] data = new Object[bookCopies.size()][7];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        for (int i = 0; i < bookCopies.size(); i++) {


            BookCopy copy = bookCopies.get(i);

            data[i][0] = copy.getBook().getIsbn();
            data[i][1] = copy.getBook().getTitle();
            data[i][2] = copy.getCopyNum();
            data[i][3] = copy.isAvailable() ? "Yes" : "No";

            if (!copy.isAvailable()) {
                CheckoutEntry entry = copy.getCheckoutEntries().getFirst();
                LocalDate dueDate = LocalDate.parse(entry.getDueDate(), formatter);
                boolean isOverdue = LocalDate.now().isAfter(dueDate);
                data[i][4] = Optional.ofNullable(entry.getMember().getMemberId()).orElse("");
                data[i][5] = Optional.ofNullable(entry.getDueDate()).orElse("");
                data[i][6] = isOverdue ? "Yes" : "No";
            }

        }
        searchTable.setModel(new javax.swing.table.DefaultTableModel(data, new String[]{"ISBN", "Title", "Copy No", "Is Available", "Library Member", "Due Date", "isOverdue"}));
        tableScrollPane.setVisible(true);
        SwingUtilities.updateComponentTreeUI(this);
    }

    private void clearTable() {
        searchTable.setModel(new javax.swing.table.DefaultTableModel(new Object[][]{}, new String[]{"Member ID", "ISBN", "Checkout Date", "Due Date", "isOverdue"}));
    }

    private void clearFields() {
        isbnField.setText("");
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
