package librarysystem;

import business.Book;
import dataaccess.DataAccess;
import dataaccess.DataAccessFacade;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class BookCopyWindow extends JFrame implements LibWindow {
    public static final BookCopyWindow INSTANCE = new BookCopyWindow();
    private boolean isInitialized = false;

    private JPanel mainPanel;
    private JTextField isbnField;
    private JLabel copiesLabel;
    private JButton addCopyButton;
    private JPanel lowerPanel;

    private BookCopyWindow() {
    }

    public void init() {
        if (isInitialized) {
            refreshUI();
            return;
        }

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Top Section Panel
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBorder(BorderFactory.createTitledBorder("Search Book"));

        isbnField = new JTextField(15);
        JButton searchButton = new JButton("Search");

        topPanel.add(new JLabel("Enter ISBN:"));
        topPanel.add(isbnField);
        topPanel.add(searchButton);

        // Info Section
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        copiesLabel = new JLabel("Total Copies: 0");
        infoPanel.add(copiesLabel);

        // Lower Section Panel
        defineLowerPanel();
        addCopyButton = new JButton("Add Copy");

        JPanel actionPanel = new JPanel(new BorderLayout());
        actionPanel.add(lowerPanel, BorderLayout.WEST);
        actionPanel.add(addCopyButton, BorderLayout.EAST);

        // Adding components to main panel
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(infoPanel, BorderLayout.CENTER);
        mainPanel.add(actionPanel, BorderLayout.SOUTH);

        searchButton.addActionListener(e -> handleAddbookCopy(false));
        addCopyButton.addActionListener(e -> handleAddbookCopy(true));

        getContentPane().add(mainPanel);
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        isInitialized = true;
    }

    private void handleAddbookCopy(boolean addCopy) {
        DataAccess da = new DataAccessFacade();
        HashMap<String, Book> books = da.readBooksMap();
        List<Book> bookList = books.values().stream().toList();

        String isbn = isbnField.getText();

        System.out.println("ISBN: " + isbn);

        if (isbn.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter ISBN to search book !!");
            return;
        }

        Optional<Book> book = bookList.stream()
                .filter(x -> x.getIsbn().equals(isbn))
                .findFirst();

        if (book.isPresent()) {
            int totalCopies = book.get().getCopies().length;
            copiesLabel.setText("Total Copies: " + totalCopies);
        } else {
            JOptionPane.showMessageDialog(this, "Book not found !!");
            return;
        }

        if (addCopy) {
            for (Book b : bookList) {
                if (b.getIsbn().equals(isbn)) {
                    b.addCopy();
                    copiesLabel.setText("Total Copies: " + b.getCopies().length);
                    JOptionPane.showMessageDialog(this, "Book copy added successfully");
                    break;
                }
            }
            DataAccessFacade.loadBookMap(bookList);
        }
    }

    public void defineLowerPanel() {
        JButton backToMainButn = new JButton("<= Back to Main");
        backToMainButn.addActionListener(new BackToMainListener());
        lowerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lowerPanel.add(backToMainButn);
    }

    class BackToMainListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent evt) {
            LibrarySystem.hideAllWindows();
            LibrarySystem.INSTANCE.setVisible(true);
            BookCopyWindow.INSTANCE.dispose();
        }
    }

    @Override
    public boolean isInitialized() {
        return isInitialized;
    }

    @Override
    public void isInitialized(boolean val) {
        isInitialized = val;
    }

    private void refreshUI() {
        isbnField.setText("");  // Clear previous input
        copiesLabel.setText("Total Copies: 0"); // Reset label
    }
}
