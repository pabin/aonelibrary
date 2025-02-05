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
        mainPanel = new JPanel();
        mainPanel.setLayout(new FlowLayout());
        defineLowerPanel();

        isbnField = new JTextField(15);
        JButton searchButton = new JButton("Search");
        copiesLabel = new JLabel("Total Copies: 0");
        addCopyButton = new JButton("Add Copy");

        mainPanel.add(new JLabel("Enter ISBN:"));
        mainPanel.add(isbnField);
        mainPanel.add(searchButton);
        mainPanel.add(copiesLabel);
        mainPanel.add(addCopyButton);
        mainPanel.add(lowerPanel, BorderLayout.SOUTH);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean addCopy = false;
                handleAddbookCopy(addCopy);
            }
        });

        addCopyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean addCopy = true;
                handleAddbookCopy(addCopy);
            }
        });

        getContentPane().add(mainPanel);
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        isInitialized = true;
    }

    private void handleAddbookCopy(boolean addCopy) {
        // Move the book fetch logic to controller later
        DataAccess da = new DataAccessFacade();
        HashMap<String, Book> books = da.readBooksMap();
        List<Book> bookList = books.values()
                .stream()
                .toList();

        String isbn = isbnField.getText();

        if (isbn.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter ISBN to search book !!");
            return;
        }

        Optional<Book> book = bookList.stream()
                .filter(x -> x.getIsbn().equals(isbn)) // test example: 48-56882
                .findFirst();

        if (book.isPresent()) {
            int totalCopies = book.get().getCopies().length;
            copiesLabel.setText("Copies: " + totalCopies);
        } else {
            JOptionPane.showMessageDialog(this, "Book not found !!");
            return;
        }

        if (addCopy) {
            for (Book b : bookList) {
                if (b.getIsbn().equals(isbn)) {
                    b.addCopy();

                    int totalCopies = book.get().getCopies().length;
                    copiesLabel.setText("Copies: " + totalCopies);
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
        lowerPanel = new JPanel();
        lowerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        lowerPanel.add(backToMainButn);
    }

    class BackToMainListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent evt) {
            LibrarySystem.hideAllWindows();
            LibrarySystem.INSTANCE.setVisible(true);
            BookCopyWindow.INSTANCE.dispose(); // Dispose the current window
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
