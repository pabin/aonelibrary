package librarysystem;

import business.Address;
import business.Author;
import business.Book;
import dataaccess.DataAccess;
import dataaccess.DataAccessFacade;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class AddNewBookWindow extends JFrame implements LibWindow {
    private static final long serialVersionUID = 1L;
    public static final AddNewBookWindow INSTANCE = new AddNewBookWindow();
    private boolean isInitialized = false;

    private JTextField isbnField, titleField, copiesField;
    private JButton saveButton;
    private JPanel lowerPanel;
    private JComboBox<String> checkoutLengthComboBox, authorsComboBox;

    private AddNewBookWindow() {
    }

    public void init() {
        setTitle("Add New Book");
        setSize(400, 300);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        defineLowerPanel();

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("ISBN:"));
        isbnField = new JTextField();
        panel.add(isbnField);

        panel.add(new JLabel("Title:"));
        titleField = new JTextField();
        panel.add(titleField);

        panel.add(new JLabel("Authors:"));
        String[] authorOptions = {"David", "Julia", "Resham"};
        authorsComboBox = new JComboBox<>(authorOptions);
        panel.add(authorsComboBox);

        panel.add(new JLabel("Max Checkout Length (in days):"));
        String[] checkoutOptions = {"7", "14"};
        checkoutLengthComboBox = new JComboBox<>(checkoutOptions);
        panel.add(checkoutLengthComboBox);

        panel.add(new JLabel("Number of Copies:"));
        copiesField = new JTextField();
        panel.add(copiesField);

        panel.add(lowerPanel, BorderLayout.SOUTH);

        saveButton = new JButton("Save");
        panel.add(saveButton);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleAddNewBook();
            }
        });

        add(panel, BorderLayout.CENTER);
        isInitialized = true;
    }

    private void handleAddNewBook() {
        String isbn = isbnField.getText();
        String title = titleField.getText();
        String authors = (String) authorsComboBox.getSelectedItem();
        String chekout = (String) checkoutLengthComboBox.getSelectedItem();
        String copies = copiesField.getText();

        if (!isbn.isEmpty() && !title.isEmpty() && !authors.isEmpty() && !chekout.isEmpty() && !copies.isEmpty()) {
            // implement author select feature
            // implement select for checkout len with 7 and 21 option
            Address add = new Address("120 S. Main", "Fairfield", "IA", "52556");
            Author author = new Author(authors, "Tesla", "980-203-1234", add, "popular author");
            List<Author> authorList = new ArrayList<>();
            authorList.add(author);

            Book bookItem = new Book(isbn, title, Integer.parseInt(chekout), authorList);

            for (int i = 1; i <= Integer.parseInt(copies); i++) {
                bookItem.addCopy();
            }

            DataAccess da = new DataAccessFacade();
            HashMap<String, Book> books = da.readBooksMap();
            List<Book> bookList = new ArrayList<>(books.values()
                    .stream()
                    .toList());

            System.out.println("bookList size before: " + bookList.size());
            bookList.add(bookItem);
            DataAccessFacade.loadBookMap(bookList);
            System.out.println("bookList size after: " + bookList.size());

            JOptionPane.showMessageDialog(INSTANCE, "Book added successfully");
        } else {
            JOptionPane.showMessageDialog(INSTANCE, "All fields are required!");
        }

    }

    public void defineLowerPanel() {
        JButton backToMainButn = new JButton("<= Back to Main");
        backToMainButn.addActionListener(new BackToMainListener());
        lowerPanel = new JPanel();
        lowerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        lowerPanel.add(backToMainButn);
    }

    class BackToMainListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent evt) {
            LibrarySystem.hideAllWindows();
            LibrarySystem.INSTANCE.setVisible(true);
            AddNewBookWindow.INSTANCE.dispose(); // Dispose the current window
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
}

